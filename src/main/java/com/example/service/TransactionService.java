package com.example.service;
import com.example.books.config.TwilioConfig;
import com.example.exception.BookNotFoundException;
import com.example.exception.UserNotFoundException;
import com.example.model.Book;
import com.example.model.Transaction;
import com.example.model.User;
import com.example.repository.BookRepository;
import com.example.repository.TransactionRepository;
import com.example.repository.UserRepository;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TwilioConfig twilioConfig;

    public Transaction issueBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book not found"));

        if (!book.isAvailable()) {
            throw new BookNotFoundException("Book is not available");
        }

        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(24);

        Transaction transaction = new Transaction(user, book, issueDate, dueDate);
        transactionRepository.save(transaction);

        book.setAvailable(false);
        bookRepository.save(book);

        // Send SMS to notify about the book issuance
        sendSms(user.getPhoneNumber(), "Book issued: " + book.getTitle() + ". Due on: " + dueDate);

        return transaction;
    }


    public Transaction returnBook(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setReturnDate(LocalDate.now());
        transactionRepository.save(transaction);

        // Mark book as available again
        Book book = transaction.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        // Book returned SMS
        sendSms(transaction.getUser().getPhoneNumber(), "Book returned: " + book.getTitle());
        return transaction;
    }

    void sendSms(String to, String message) {
        Message msg = Message.creator(new PhoneNumber(to), //to phone#
                new PhoneNumber(twilioConfig.getPhone_number()), message). //from phone#, twilio API
                create();

        System.out.println("Message sent with SID: " + msg.getSid());
    }

    public List<Transaction> getDueTransactions() {
        LocalDate today = LocalDate.now();
        return transactionRepository.findByDueDate(today.plusDays(3));  // Transactions due in 3 days
    }


    public List<Transaction> getOverdueTransactions() {
        LocalDate today = LocalDate.now();
        return transactionRepository.findByDueDateBeforeAndReturnDateIsNull(today);  // Transactions overdue
    }

}

