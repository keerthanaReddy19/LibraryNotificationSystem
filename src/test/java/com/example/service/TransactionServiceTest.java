package com.example.service;
import com.example.model.Book;
import com.example.model.Transaction;
import com.example.model.User;
import com.example.repository.BookRepository;
import com.example.repository.TransactionRepository;
import com.example.repository.UserRepository;
import com.example.books.config.TwilioConfig;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private TwilioConfig twilioConfig;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void getDueTransactions_success() {
        // Arrange
        LocalDate dueDate = LocalDate.now().plusDays(3);
        List<Transaction> transactions = Arrays.asList(new Transaction(new User("Aliya", "+1234567890"), new Book("The Book Thief", "Markus Zusak"), LocalDate.now(), dueDate));
        when(transactionRepository.findByDueDate(any(LocalDate.class))).thenReturn(transactions);

        // Act
        List<Transaction> result = transactionService.getDueTransactions();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("The Book Thief", result.get(0).getBook().getTitle());
    }

    @Test
    void getOverdueTransactions_success() {
        // Arrange
        LocalDate overdueDate = LocalDate.now().minusDays(3);
        List<Transaction> transactions = Arrays.asList(new Transaction(new User("Aliya", "+1234567890"), new Book("The Book Thief", "Markus Zusak"), LocalDate.now().minusDays(10), overdueDate));
        when(transactionRepository.findByDueDateBeforeAndReturnDateIsNull(any(LocalDate.class))).thenReturn(transactions);

        // Act
        List<Transaction> result = transactionService.getOverdueTransactions();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("The Book Thief", result.get(0).getBook().getTitle());
    }
}
