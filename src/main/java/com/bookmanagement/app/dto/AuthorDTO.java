package com.bookmanagement.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
public class AuthorDTO {
    
    private Long id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private String nationality;
    
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
    
    private Set<BookSummaryDTO> books = new HashSet<>();
    private Set<Long> bookIds = new HashSet<>();
    
    public AuthorDTO() {
    }
    
    public AuthorDTO(Long id, String name, String nationality, LocalDate dateOfBirth, 
                 Set<BookSummaryDTO> books, Set<Long> bookIds) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
        this.books = books;
        this.bookIds = bookIds;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getNationality() {
        return nationality;
    }
    
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public Set<BookSummaryDTO> getBooks() {
        return books;
    }
    
    public void setBooks(Set<BookSummaryDTO> books) {
        this.books = books;
    }
    
    public Set<Long> getBookIds() {
        return bookIds;
    }
    
    public void setBookIds(Set<Long> bookIds) {
        this.bookIds = bookIds;
    }
}
