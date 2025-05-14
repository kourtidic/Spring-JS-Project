package com.bookmanagement.app.service.impl;

import com.bookmanagement.app.dto.AuthorDTO;
import com.bookmanagement.app.dto.BookDTO;
import com.bookmanagement.app.model.Author;
import com.bookmanagement.app.model.Book;
import com.bookmanagement.app.repository.AuthorRepository;
import com.bookmanagement.app.repository.BookRepository;
import com.bookmanagement.app.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
        return convertToDTO(book);
    }

    @Override
    public BookDTO getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ISBN: " + isbn));
        return convertToDTO(book);
    }

    @Override
    public List<BookDTO> getBooksByTitle(String title) {
        return bookRepository.findByTitleContaining(title).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> getBooksByCategory(String category) {
        return bookRepository.findByCategory(category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> getBooksByAuthorId(Long authorId) {
        return bookRepository.findBooksByAuthorId(authorId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookDTO createBook(BookDTO bookDTO) {
        Book book = convertToEntity(bookDTO);
        Book savedBook = bookRepository.save(book);
        return convertToDTO(savedBook);
    }

    @Override
    @Transactional
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
        
        existingBook.setIsbn(bookDTO.getIsbn());
        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setCategory(bookDTO.getCategory());
        existingBook.setPublicationYear(bookDTO.getPublicationYear());
        
        // Handle author associations if authorIds are provided
        if (bookDTO.getAuthorIds() != null && !bookDTO.getAuthorIds().isEmpty()) {
            Set<Author> authors = bookDTO.getAuthorIds().stream()
                    .map(authorId -> authorRepository.findById(authorId)
                            .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + authorId)))
                    .collect(Collectors.toSet());
            existingBook.setAuthors(authors);
        }
        
        Book updatedBook = bookRepository.save(existingBook);
        return convertToDTO(updatedBook);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public BookDTO addAuthorsToBook(Long bookId, Set<Long> authorIds) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));
        
        for (Long authorId : authorIds) {
            Author author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + authorId));
            book.addAuthor(author);
        }
        
        Book updatedBook = bookRepository.save(book);
        return convertToDTO(updatedBook);
    }

    @Override
    @Transactional
    public BookDTO removeAuthorsFromBook(Long bookId, Set<Long> authorIds) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));
        
        for (Long authorId : authorIds) {
            Author author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + authorId));
            book.removeAuthor(author);
        }
        
        Book updatedBook = bookRepository.save(book);
        return convertToDTO(updatedBook);
    }

    // Helper method to convert Entity to DTO
    private BookDTO convertToDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setCategory(book.getCategory());
        bookDTO.setPublicationYear(book.getPublicationYear());
        
        Set<AuthorDTO> authorDTOs = book.getAuthors().stream()
                .map(author -> {
                    AuthorDTO authorDTO = new AuthorDTO();
                    authorDTO.setId(author.getId());
                    authorDTO.setName(author.getName());
                    authorDTO.setNationality(author.getNationality());
                    authorDTO.setDateOfBirth(author.getDateOfBirth());
                    return authorDTO;
                })
                .collect(Collectors.toSet());
        
        bookDTO.setAuthors(authorDTOs);
        return bookDTO;
    }

    // Helper method to convert DTO to Entity
    private Book convertToEntity(BookDTO bookDTO) {
        Book book = new Book();
        book.setIsbn(bookDTO.getIsbn());
        book.setTitle(bookDTO.getTitle());
        book.setCategory(bookDTO.getCategory());
        book.setPublicationYear(bookDTO.getPublicationYear());
        
        // Handle author associations if authorIds are provided
        if (bookDTO.getAuthorIds() != null && !bookDTO.getAuthorIds().isEmpty()) {
            Set<Author> authors = bookDTO.getAuthorIds().stream()
                    .map(authorId -> authorRepository.findById(authorId)
                            .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + authorId)))
                    .collect(Collectors.toSet());
            book.setAuthors(authors);
        }
        
        return book;
    }
}
