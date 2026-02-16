package com.flp.ddd.dddlibrary.catalog.application.dto;

import com.flp.ddd.dddlibrary.catalog.domain.book.Book;

import java.util.List;

public record BookDTO(
        String id,
        String title,
        String isbn,
        List<CopyDTO> copies
) {
    public static BookDTO from(Book book) {
        return new BookDTO(
                book.getId().id().toString(),
                book.getTitle(),
                book.getIsbn().value(),
                book.getCopies().stream()
                        .map(CopyDTO::from).toList()
        );
    }
}
