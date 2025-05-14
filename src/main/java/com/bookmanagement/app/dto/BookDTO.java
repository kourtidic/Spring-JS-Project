package com.bookmanagement.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;
public class BookDTO {
    
    private Long id;
    
    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "^[0-9-]+$", message = "ISBN must only contain numbers and hyphens")
    private String isbn;
    
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 200, message = "Title must be between 1 and 200 characters")
    private String title;
    
    @NotBlank(message = "Category is required")
    private String category;
    
    @NotNull(message = "Publication year is required")
    private Integer publicationYear;
    
    private Set<AuthorDTO> authors = new HashSet<>();
    private Set<Long> authorIds = new HashSet<>();
    
    public BookDTO() {
    }
    
    public BookDTO(Long id, String isbn, String title, String category, Integer publicationYear, 
               Set<AuthorDTO> authors, Set<Long> authorIds) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.category = category;
        this.publicationYear = publicationYear;
        this.authors = authors;
        this.authorIds = authorIds;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public Integer getPublicationYear() {
        return publicationYear;
    }
    
    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }
    
    public Set<AuthorDTO> getAuthors() {
        return authors;
    }
    
    public void setAuthors(Set<AuthorDTO> authors) {
        this.authors = authors;
    }
    
    public Set<Long> getAuthorIds() {
        return authorIds;
    }
    
    public void setAuthorIds(Set<Long> authorIds) {
        this.authorIds = authorIds;
    }
}
