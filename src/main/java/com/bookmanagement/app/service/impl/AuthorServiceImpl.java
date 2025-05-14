package com.bookmanagement.app.service.impl;

import com.bookmanagement.app.dto.AuthorDTO;
import com.bookmanagement.app.dto.BookSummaryDTO;
import com.bookmanagement.app.model.Author;
import com.bookmanagement.app.model.Book;
import com.bookmanagement.app.repository.AuthorRepository;
import com.bookmanagement.app.repository.BookRepository;
import com.bookmanagement.app.service.AuthorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));
        return convertToDTO(author);
    }

    @Override
    public List<AuthorDTO> getAuthorsByName(String name) {
        return authorRepository.findByNameContaining(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuthorDTO> getAuthorsByNationality(String nationality) {
        return authorRepository.findByNationality(nationality).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuthorDTO> getAuthorsByBookId(Long bookId) {
        return authorRepository.findAuthorsByBookId(bookId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        Author author = convertToEntity(authorDTO);
        Author savedAuthor = authorRepository.save(author);
        return convertToDTO(savedAuthor);
    }

    @Override
    @Transactional
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));
        
        existingAuthor.setName(authorDTO.getName());
        existingAuthor.setNationality(authorDTO.getNationality());
        existingAuthor.setDateOfBirth(authorDTO.getDateOfBirth());
        
        // Handle book associations if bookIds are provided
        if (authorDTO.getBookIds() != null && !authorDTO.getBookIds().isEmpty()) {
            Set<Book> books = authorDTO.getBookIds().stream()
                    .map(bookId -> bookRepository.findById(bookId)
                            .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId)))
                    .collect(Collectors.toSet());
            
            // Clear existing associations and add new ones
            existingAuthor.getBooks().clear();
            for (Book book : books) {
                book.getAuthors().add(existingAuthor);
                existingAuthor.getBooks().add(book);
            }
        }
        
        Author updatedAuthor = authorRepository.save(existingAuthor);
        return convertToDTO(updatedAuthor);
    }

    @Override
    @Transactional
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new EntityNotFoundException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
    }

    @Override
    @Transactional
    public AuthorDTO addBooksToAuthor(Long authorId, Set<Long> bookIds) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + authorId));
        
        for (Long bookId : bookIds) {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));
            book.addAuthor(author);
        }
        
        Author updatedAuthor = authorRepository.save(author);
        return convertToDTO(updatedAuthor);
    }

    @Override
    @Transactional
    public AuthorDTO removeBooksFromAuthor(Long authorId, Set<Long> bookIds) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + authorId));
        
        for (Long bookId : bookIds) {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));
            book.removeAuthor(author);
        }
        
        Author updatedAuthor = authorRepository.save(author);
        return convertToDTO(updatedAuthor);
    }

    // Helper method to convert Entity to DTO
    private AuthorDTO convertToDTO(Author author) {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setName(author.getName());
        authorDTO.setNationality(author.getNationality());
        authorDTO.setDateOfBirth(author.getDateOfBirth());
        
        Set<BookSummaryDTO> bookDTOs = author.getBooks().stream()
                .map(book -> {
                    BookSummaryDTO bookDTO = new BookSummaryDTO();
                    bookDTO.setId(book.getId());
                    bookDTO.setIsbn(book.getIsbn());
                    bookDTO.setTitle(book.getTitle());
                    bookDTO.setCategory(book.getCategory());
                    bookDTO.setPublicationYear(book.getPublicationYear());
                    return bookDTO;
                })
                .collect(Collectors.toSet());
        
        authorDTO.setBooks(bookDTOs);
        return authorDTO;
    }

    // Helper method to convert DTO to Entity
    private Author convertToEntity(AuthorDTO authorDTO) {
        Author author = new Author();
        author.setName(authorDTO.getName());
        author.setNationality(authorDTO.getNationality());
        author.setDateOfBirth(authorDTO.getDateOfBirth());
        
        // Handle book associations if bookIds are provided
        if (authorDTO.getBookIds() != null && !authorDTO.getBookIds().isEmpty()) {
            Set<Book> books = authorDTO.getBookIds().stream()
                    .map(bookId -> bookRepository.findById(bookId)
                            .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId)))
                    .collect(Collectors.toSet());
            
            for (Book book : books) {
                book.getAuthors().add(author);
                author.getBooks().add(book);
            }
        }
        
        return author;
    }
}
