package com.flp.ddd.dddlibrary.catalog.application;

import com.flp.ddd.dddlibrary.catalog.domain.book.Isbn;
import org.springframework.stereotype.Service;

@Service
public interface BookSearchService {
    BookInformation search(Isbn isbn);
}
