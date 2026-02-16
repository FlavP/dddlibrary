package com.flp.ddd.dddlibrary.catalog.infrastructure.persistence.book;

import com.flp.ddd.dddlibrary.catalog.domain.book.Book;
import com.flp.ddd.dddlibrary.catalog.domain.book.BookRepository;
import com.flp.ddd.dddlibrary.catalog.infrastructure.persistence.copy.CopyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DbBookRepository implements BookRepository {
    private final JpaBookRepository jpaBookRepository;

    @Override
    public void save(Book book) {
        BookEntity bookEntity = toEntity(book);
        jpaBookRepository.save(bookEntity);
    }

    private BookEntity toEntity(Book book) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setBookId(book.getId().id());
        bookEntity.setTitle(book.getTitle());
        bookEntity.setIsbn(book.getIsbn());

        List<CopyEntity> copyEntities = book.getCopies().stream()
                .map(copy -> CopyEntity.builder()
                        .copyId(copy.getId().id())
                        .barcode(copy.getBarcode().barcode())
                        .isAvailable(copy.isAvailable())
                        .build()).toList();
        bookEntity.setCopies(copyEntities);
        copyEntities.forEach(copy -> copy.setBook(bookEntity));

        return bookEntity;
    }
}
