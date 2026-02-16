package com.flp.ddd.dddlibrary.catalog.infrastructure.persistence;

import com.flp.ddd.dddlibrary.catalog.domain.book.Isbn;
import com.flp.ddd.dddlibrary.catalog.infrastructure.persistence.book.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestBookRepository extends JpaRepository<BookEntity, Integer> {
    Optional<BookEntity> findByIsbn(Isbn isbn);
}
