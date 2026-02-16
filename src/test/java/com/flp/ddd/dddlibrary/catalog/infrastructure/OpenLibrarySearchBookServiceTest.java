package com.flp.ddd.dddlibrary.catalog.infrastructure;

import com.flp.ddd.dddlibrary.catalog.application.BookInformation;
import com.flp.ddd.dddlibrary.catalog.domain.book.Isbn;
import com.flp.ddd.dddlibrary.catalog.domain.exceptions.BookNotFoundException;
import com.flp.ddd.dddlibrary.catalog.infrastructure.external.OpenLibraryBookSearchService;
import com.flp.ddd.dddlibrary.catalog.infrastructure.external.OpenLibrarySearchResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OpenLibrarySearchBookServiceTest {
    @Mock
    RestClient restClient;

    @InjectMocks
    OpenLibraryBookSearchService bookSearchService;

    @Test
    void itReturnsBookInformationWhenRequestIsSuccessful() {
        Isbn isbn = new Isbn("9780132350884");
        OpenLibrarySearchResult result = OpenLibrarySearchResult.builder()
                .publishers(List.of("Open Press", "Others"))
                .title("Real Code")
                .isbn_13(List.of("First Isbn", "Second Isbn")).build();
        RestClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("isbn/{isbn}.json","9780132350884")).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(OpenLibrarySearchResult.class)).thenReturn(result);

        BookInformation bookInformation = bookSearchService.search(isbn);
        assertEquals("Real Code", bookInformation.title());
    }

    @Test
    void itThrowsBookNotFoundExceptionWhenRequestReturnsAnEmptyObject() {
        Isbn isbn = new Isbn("9780132350884");
        OpenLibrarySearchResult result = OpenLibrarySearchResult.builder()
                .publishers(List.of("Open Press", "Others"))
                .title("Real Code")
                .isbn_13(List.of("First Isbn", "Second Isbn")).build();
        RestClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("isbn/{isbn}.json","9780132350884")).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(OpenLibrarySearchResult.class)).thenReturn(null);

        BookNotFoundException exception = assertThrows(
                BookNotFoundException.class,
                () -> bookSearchService.search(isbn)
        );
    }
}
