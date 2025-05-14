package com.bookmanagement.app.dto;
public class BookSummaryDTO {
    
    private Long id;
    private String isbn;
    private String title;
    private String category;
    private Integer publicationYear;
    
    public BookSummaryDTO() {
    }
    
    public BookSummaryDTO(Long id, String isbn, String title, String category, Integer publicationYear) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.category = category;
        this.publicationYear = publicationYear;
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
}
