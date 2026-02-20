package com.flp.ddd.dddlibrary.catalog.infrastructure.persistence.copy;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaCopyRepository extends JpaRepository<CopyEntity, Integer> {
    Optional<CopyEntity> findByCopyId(UUID copyId);
}

