package com.bookmanagement.app.repository;

import com.bookmanagement.app.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    
    List<Author> findByNameContaining(String name);
    
    List<Author> findByNationality(String nationality);
    
    @Query("SELECT a FROM Author a JOIN a.books b WHERE b.id = :bookId")
    List<Author> findAuthorsByBookId(Long bookId);
}
