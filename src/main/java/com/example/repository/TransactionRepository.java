package com.example.repository;

import com.example.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByDueDate(LocalDate dueDate);

    List<Transaction> findByReturnDateIsNull();

    List<Transaction> findByDueDateBeforeAndReturnDateIsNull(LocalDate today);
}

