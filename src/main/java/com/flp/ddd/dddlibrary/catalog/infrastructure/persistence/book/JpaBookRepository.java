package com.flp.ddd.dddlibrary.catalog.infrastructure.persistence.book;

import org.springframework.data.repository.CrudRepository;

public interface JpaBookRepository extends CrudRepository<BookEntity, Integer> {
}
