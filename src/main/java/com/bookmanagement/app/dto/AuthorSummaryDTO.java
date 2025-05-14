package com.bookmanagement.app.dto;

import java.time.LocalDate;
public class AuthorSummaryDTO {
    
    private Long id;
    private String name;
    private String nationality;
    private LocalDate dateOfBirth;
    
    public AuthorSummaryDTO() {
    }
    
    public AuthorSummaryDTO(Long id, String name, String nationality, LocalDate dateOfBirth) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
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
}
