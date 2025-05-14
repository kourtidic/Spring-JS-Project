package com.bookmanagement.app.service;

import com.bookmanagement.app.dto.AuthorDTO;

import java.util.List;
import java.util.Set;

public interface AuthorService {
    
    List<AuthorDTO> getAllAuthors();
    
    AuthorDTO getAuthorById(Long id);
    
    List<AuthorDTO> getAuthorsByName(String name);
    
    List<AuthorDTO> getAuthorsByNationality(String nationality);
    
    List<AuthorDTO> getAuthorsByBookId(Long bookId);
    
    AuthorDTO createAuthor(AuthorDTO authorDTO);
    
    AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO);
    
    void deleteAuthor(Long id);
    
    AuthorDTO addBooksToAuthor(Long authorId, Set<Long> bookIds);
    
    AuthorDTO removeBooksFromAuthor(Long authorId, Set<Long> bookIds);
}
