package com.flp.ddd.dddlibrary.catalog.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flp.ddd.dddlibrary.catalog.application.dto.BookDTO;
import com.flp.ddd.dddlibrary.catalog.domain.book.*;
import com.flp.ddd.dddlibrary.catalog.domain.copy.Copy;
import com.flp.ddd.dddlibrary.catalog.domain.copy.CopyId;
import com.flp.ddd.dddlibrary.catalog.domain.exceptions.BookNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(CatalogController.class)
@ExtendWith(MockitoExtension.class)
public class CatalogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    AddBookToCatalogUseCase addBookToCatalogUseCase;

    @Test
    void shouldCreateNewBookAndNewCopy() throws Exception {
        String bookJson = """
                {
                    "isbn": "9780132350884"
                }
                """;

        Book expectedBook = Book.builder()
                .id(new BookId())
                .title("Clean Code")
                .isbn(new Isbn("9780132350884"))
                .build();

        Copy newCopy = Copy.builder()
                .id(new CopyId())
                .barcode(new Barcode())
                .bookId(expectedBook.getId())
                .build();

        expectedBook.setCopies(List.of(newCopy));

        when(addBookToCatalogUseCase.execute(any(Isbn.class)))
                .thenReturn(expectedBook);

        String response = mockMvc.perform(post("/api/catalog/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        BookDTO returnedBook = objectMapper.readValue(response, BookDTO.class);

        assertThat(returnedBook.id()).isNotNull();
        assertThat(returnedBook.title()).isEqualTo("Clean Code");
        assertThat(returnedBook.isbn()).isEqualTo("9780132350884");
        assertThat(returnedBook.copies().size() == 1);
    }

    @Test
    void itThrows401ExceptionWhenIsnbIsInvalid() throws Exception {
        String bookJson = """
                {
                    "isbn": "xxxxx"
                }
                """;

        mockMvc.perform(post("/api/catalog/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void itThrows404ExceptionWhenBookNotFound() throws Exception {
        String bookJson = """
                    {"isbn": "9780123456786"}
                """;

        when(addBookToCatalogUseCase.execute(any(Isbn.class)))
                .thenThrow(new BookNotFoundException("Book not found"));

        mockMvc.perform(post("/api/catalog/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isNotFound());

    }
}
