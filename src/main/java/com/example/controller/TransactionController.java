package com.example.controller;

import com.example.model.Transaction;
import com.example.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/issue")
    public Transaction issueBook(@RequestParam Long userId, @RequestParam Long bookId) {
        return transactionService.issueBook(userId, bookId);
    }

    @PostMapping("/return")
    public Transaction returnBook(@RequestParam Long transactionId) {
        return transactionService.returnBook(transactionId);
    }

    @GetMapping(value = "/due", produces = "application/json")
    public List<Transaction> getDueTransactions() {
        return transactionService.getDueTransactions();
    }

    @GetMapping(value = "/overdue", produces = "application/json")
    public List<Transaction> getOverdueTransactions() {
        return transactionService.getOverdueTransactions();
    }
}

