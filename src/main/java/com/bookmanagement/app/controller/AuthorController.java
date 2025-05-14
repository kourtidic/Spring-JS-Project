package com.bookmanagement.app.controller;

import com.bookmanagement.app.dto.AuthorDTO;
import com.bookmanagement.app.service.AuthorService;
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
@RequestMapping("/authors")
@Tag(name = "Author Management", description = "Operations pertaining to authors in the Book Management System")
@CrossOrigin(origins = "*")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    @Operation(summary = "Get all authors", description = "Returns a list of all authors in the system")
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        List<AuthorDTO> authors = authorService.getAllAuthors();
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get author by ID", description = "Returns a single author by their ID")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        AuthorDTO author = authorService.getAuthorById(id);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }
    
    @GetMapping("/name/{name}")
    @Operation(summary = "Get authors by name", description = "Returns authors containing the given name string")
    public ResponseEntity<List<AuthorDTO>> getAuthorsByName(@PathVariable String name) {
        List<AuthorDTO> authors = authorService.getAuthorsByName(name);
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }
    
    @GetMapping("/nationality/{nationality}")
    @Operation(summary = "Get authors by nationality", description = "Returns authors of the given nationality")
    public ResponseEntity<List<AuthorDTO>> getAuthorsByNationality(@PathVariable String nationality) {
        List<AuthorDTO> authors = authorService.getAuthorsByNationality(nationality);
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }
    
    @GetMapping("/{id}/books")
    @Operation(summary = "Get author's books", description = "Returns a list of books written by the specified author")
    public ResponseEntity<AuthorDTO> getAuthorWithBooks(@PathVariable Long id) {
        AuthorDTO author = authorService.getAuthorById(id);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create a new author", description = "Creates a new author with the provided data")
    public ResponseEntity<AuthorDTO> createAuthor(@Valid @RequestBody AuthorDTO authorDTO) {
        AuthorDTO createdAuthor = authorService.createAuthor(authorDTO);
        return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing author", description = "Updates an author with the provided data")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorDTO authorDTO) {
        AuthorDTO updatedAuthor = authorService.updateAuthor(id, authorDTO);
        return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an author", description = "Deletes an author by their ID")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @PostMapping("/{id}/books")
    @Operation(summary = "Add books to an author", description = "Assigns books to an author")
    public ResponseEntity<AuthorDTO> addBooksToAuthor(
            @PathVariable Long id,
            @RequestBody Set<Long> bookIds) {
        AuthorDTO updatedAuthor = authorService.addBooksToAuthor(id, bookIds);
        return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}/books")
    @Operation(summary = "Remove books from an author", description = "Removes books from an author")
    public ResponseEntity<AuthorDTO> removeBooksFromAuthor(
            @PathVariable Long id,
            @RequestBody Set<Long> bookIds) {
        AuthorDTO updatedAuthor = authorService.removeBooksFromAuthor(id, bookIds);
        return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);
    }
}
