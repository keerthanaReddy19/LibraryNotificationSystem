package com.example.controller;

import com.example.controller.TransactionController;
import com.example.model.Transaction;
import com.example.model.User;
import com.example.model.Book;
import com.example.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    void issueBook_success() throws Exception {
        User user = new User("Keerthana", "+123456789");
        Book book = new Book("The Book Thief", "Markus Zusak");
        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(14);

        Transaction transaction = new Transaction(user, book, issueDate, dueDate);

        // Mock the service layer to return the correct transaction when issuing a book
        when(transactionService.issueBook(anyLong(), anyLong())).thenReturn(transaction);

        mockMvc.perform(post("/api/transactions/issue").param("userId", "1").param("bookId", "1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.issueDate").value("2024-10-06")).andExpect(jsonPath("$.dueDate").value("2024-10-20"));
    }

    @Test
    void returnBook_success() throws Exception {
        User user = new User("Keerthana", "+123456789");
        Book book = new Book("The Book Thief", "Markus Zusak");
        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(14);
        LocalDate returnDate = LocalDate.now().plusDays(5);

        Transaction transaction = new Transaction(user, book, issueDate, dueDate);
        transaction.setReturnDate(returnDate);

        // Mock the service layer to return the correct transaction when returning a book
        when(transactionService.returnBook(anyLong())).thenReturn(transaction);

        mockMvc.perform(post("/api/transactions/return").param("transactionId", "1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.returnDate").value(returnDate.toString()));
    }

    @Test
    void getDueTransactions_success() throws Exception {
        User user1 = new User("Keerthana", "+123456789");
        Book book1 = new Book("The Book Thief", "Markus Zusak");
        User user2 = new User("Kathie", "+123456789");
        Book book2 = new Book("The Book Thief", "Markus Zusak");

        LocalDate issueDate1 = LocalDate.now().minusDays(10);
        LocalDate dueDate1 = issueDate1.plusDays(14);
        LocalDate issueDate2 = LocalDate.now().minusDays(5);
        LocalDate dueDate2 = issueDate2.plusDays(14);

        List<Transaction> transactions = Arrays.asList(new Transaction(user1, book1, issueDate1, dueDate1), new Transaction(user2, book2, issueDate2, dueDate2));

        // Mock the service layer to return the list of due transactions
        when(transactionService.getDueTransactions()).thenReturn(transactions);

        mockMvc.perform(get("/api/transactions/due").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$[0].issueDate").value("2024-09-26")).andExpect(jsonPath("$[1].issueDate").value("2024-10-01"));
    }

    @Test
    void getOverdueTransactions_success() throws Exception {
        User user1 = new User("Keerthana", "+123456789");
        Book book1 = new Book("The Book Thief", "Markus Zusak");
        User user2 = new User("Kathie", "+123467439");
        Book book2 = new Book("The Book Thief", "Markus Zusak");

        LocalDate issueDate1 = LocalDate.now().minusDays(30);
        LocalDate dueDate1 = issueDate1.plusDays(14);
        LocalDate issueDate2 = LocalDate.now().minusDays(25);
        LocalDate dueDate2 = issueDate2.plusDays(14);

        List<Transaction> transactions = Arrays.asList(new Transaction(user1, book1, issueDate1, dueDate1), new Transaction(user2, book2, issueDate2, dueDate2));

        // Mock the service layer to return the list of overdue transactions
        when(transactionService.getOverdueTransactions()).thenReturn(transactions);

        mockMvc.perform(get("/api/transactions/overdue").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$[0].issueDate").value("2024-09-06")).andExpect(jsonPath("$[0].dueDate").value("2024-09-20"));
    }
}
