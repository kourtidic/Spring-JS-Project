package com.bookmanagement.app.controller;

import com.bookmanagement.app.dto.BookDTO;
import com.bookmanagement.app.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/books")
@Tag(name = "Book Management", description = "Operations pertaining to books in the Book Management System")
@CrossOrigin(origins = "*")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(summary = "Get all books", description = "Returns a list of all books in the system")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Returns a single book by its ID")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        BookDTO book = bookService.getBookById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }
    
    @GetMapping("/isbn/{isbn}")
    @Operation(summary = "Get book by ISBN", description = "Returns a book by its ISBN")
    public ResponseEntity<BookDTO> getBookByIsbn(@PathVariable String isbn) {
        BookDTO book = bookService.getBookByIsbn(isbn);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }
    
    @GetMapping("/title/{title}")
    @Operation(summary = "Get books by title", description = "Returns books containing the given title string")
    public ResponseEntity<List<BookDTO>> getBooksByTitle(@PathVariable String title) {
        List<BookDTO> books = bookService.getBooksByTitle(title);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    
    @GetMapping("/category/{category}")
    @Operation(summary = "Get books by category", description = "Returns books in the given category")
    public ResponseEntity<List<BookDTO>> getBooksByCategory(@PathVariable String category) {
        List<BookDTO> books = bookService.getBooksByCategory(category);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create a new book", description = "Creates a new book with the provided data")
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) {
        BookDTO createdBook = bookService.createBook(bookDTO);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing book", description = "Updates a book with the provided data")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO) {
        BookDTO updatedBook = bookService.updateBook(id, bookDTO);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book", description = "Deletes a book by its ID")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @PostMapping("/{id}/authors")
    @Operation(summary = "Add authors to a book", description = "Assigns authors to a book")
    public ResponseEntity<BookDTO> addAuthorsToBook(
            @PathVariable Long id,
            @RequestBody Set<Long> authorIds) {
        BookDTO updatedBook = bookService.addAuthorsToBook(id, authorIds);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}/authors")
    @Operation(summary = "Remove authors from a book", description = "Removes authors from a book")
    public ResponseEntity<BookDTO> removeAuthorsFromBook(
            @PathVariable Long id,
            @RequestBody Set<Long> authorIds) {
        BookDTO updatedBook = bookService.removeAuthorsFromBook(id, authorIds);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }
}
