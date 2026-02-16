package com.flp.ddd.dddlibrary.catalog.application;

import com.flp.ddd.dddlibrary.catalog.application.dto.BookDTO;
import com.flp.ddd.dddlibrary.catalog.domain.book.AddBookRequest;
import com.flp.ddd.dddlibrary.catalog.domain.book.Book;
import com.flp.ddd.dddlibrary.catalog.domain.book.Isbn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CatalogController {
    private final AddBookToCatalogUseCase addBookToCatalogUseCase;

    @PostMapping("/api/catalog/book")
    public ResponseEntity<BookDTO> save(@RequestBody AddBookRequest request) {
        Book book = addBookToCatalogUseCase.execute(new Isbn(request.isbn()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BookDTO.from(book));
    }

}
