package com.flp.ddd.dddlibrary.catalog.domain.book;

import com.flp.ddd.dddlibrary.catalog.domain.copy.Copy;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Book {
    private final BookId id;
    private String title;
    private Isbn isbn;
    private List<Copy> copies;
}
