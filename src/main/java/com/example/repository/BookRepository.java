package com.example.repository;
import com.example.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // Additional query methods can be defined here if needed

    List<Book> findByAvailableTrue();
}

