package com.flp.ddd.dddlibrary.catalog.infrastructure.external;

import com.flp.ddd.dddlibrary.catalog.application.BookInformation;
import com.flp.ddd.dddlibrary.catalog.application.BookSearchService;
import com.flp.ddd.dddlibrary.catalog.domain.book.Isbn;
import com.flp.ddd.dddlibrary.catalog.domain.exceptions.BookNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class OpenLibraryBookSearchService implements BookSearchService {
    private final RestClient restClient;

    @Override
    public BookInformation search(Isbn isbn) {
        OpenLibrarySearchResult result = restClient.get()
                .uri("isbn/{isbn}.json", isbn.value())
                .retrieve()
                .body(OpenLibrarySearchResult.class);
        if (result == null) {
            throw new BookNotFoundException("Book Not Found");
        }
        return new BookInformation(result.title());
    }
}
