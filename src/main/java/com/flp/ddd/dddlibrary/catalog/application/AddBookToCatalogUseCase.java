package com.flp.ddd.dddlibrary.catalog.application;

import com.flp.ddd.dddlibrary.UseCase;
import com.flp.ddd.dddlibrary.catalog.domain.book.*;
import com.flp.ddd.dddlibrary.catalog.domain.copy.Copy;
import com.flp.ddd.dddlibrary.catalog.domain.copy.CopyId;
import com.flp.ddd.dddlibrary.shared.events.CopyCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class AddBookToCatalogUseCase {
    private final BookSearchService bookSearchService;
    private final BookRepository bookRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Book execute(Isbn isbn) {
        BookInformation result = bookSearchService.search(isbn);
        BookId bookId = new BookId(UUID.randomUUID());
        Copy copy = Copy.builder()
                .id(new CopyId(UUID.randomUUID()))
                .barcode(new Barcode(RandomStringUtils.random(7, true, false)))
                .bookId(bookId)
                .available(true)
                .build();
        Book book = Book.builder()
                .title(result.title())
                .isbn(isbn)
                .id(bookId)
                .copies(List.of(copy))
                .build();
        bookRepository.save(book);
        eventPublisher.publishEvent(new CopyCreatedEvent(
                copy.getId().id(),
                copy.getBarcode().barcode(),
                copy.isAvailable()
        ));
        return book;
    }
}
