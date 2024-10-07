package com.example.service;

import com.example.books.config.TwilioConfig;
import com.example.model.Transaction;
import com.example.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReminderService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TwilioConfig twilioConfig;

    @Scheduled(cron = "0 0 9 * * ?") // Every day at 9 AM
    public void sendDueDateReminders() {
        LocalDate today = LocalDate.now();
        List<Transaction> upcomingDueTransactions = transactionRepository.findByDueDate(today.plusDays(3)); // Due in 3 days

        for (Transaction transaction : upcomingDueTransactions) {
            transactionService.sendSms(transaction.getUser().getPhoneNumber(), "Reminder: Your book '" + transaction.getBook().getTitle() + "' is due on " + transaction.getDueDate());
        }
    }

    @Scheduled(cron = "0 0 9 * * ?") // Every day at 9 AM
    public void sendOverdueNotifications() {
        LocalDate today = LocalDate.now();
        List<Transaction> overdueTransactions = transactionRepository.findByDueDate(today.minusDays(1)); // Overdue transactions

        for (Transaction transaction : overdueTransactions) {
            transactionService.sendSms(transaction.getUser().getPhoneNumber(), "Alert: Your book '" + transaction.getBook().getTitle() + "' is overdue. Please return it as soon as possible.");
        }
    }
}



