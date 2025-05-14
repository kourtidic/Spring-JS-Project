package com.bookmanagement.app.service;

import com.bookmanagement.app.dto.BookDTO;

import java.util.List;
import java.util.Set;

public interface BookService {
    
    List<BookDTO> getAllBooks();
    
    BookDTO getBookById(Long id);
    
    BookDTO getBookByIsbn(String isbn);
    
    List<BookDTO> getBooksByTitle(String title);
    
    List<BookDTO> getBooksByCategory(String category);
    
    List<BookDTO> getBooksByAuthorId(Long authorId);
    
    BookDTO createBook(BookDTO bookDTO);
    
    BookDTO updateBook(Long id, BookDTO bookDTO);
    
    void deleteBook(Long id);
    
    BookDTO addAuthorsToBook(Long bookId, Set<Long> authorIds);
    
    BookDTO removeAuthorsFromBook(Long bookId, Set<Long> authorIds);
}
