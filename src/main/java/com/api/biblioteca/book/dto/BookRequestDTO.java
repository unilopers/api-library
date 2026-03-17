package com.api.biblioteca.book.dto;

import lombok.Data;

@Data
public class BookRequestDTO {
    private String title;
    private String author;
    private Integer year;
    private Boolean available;
}