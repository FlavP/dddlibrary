package com.flp.ddd.dddlibrary.catalog.infrastructure.persistence;

import com.flp.ddd.dddlibrary.catalog.infrastructure.persistence.copy.CopyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TestCopyRepository extends JpaRepository<CopyEntity, Integer> {
    Optional<CopyEntity> findByCopyId(UUID copyId);
}

