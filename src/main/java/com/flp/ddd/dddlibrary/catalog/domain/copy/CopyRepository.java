package com.flp.ddd.dddlibrary.catalog.domain.copy;

import com.flp.ddd.dddlibrary.catalog.infrastructure.persistence.copy.CopyEntity;

import java.util.List;

public interface CopyRepository {
    void save(CopyEntity copy);
    List<CopyEntity> getCopyEntitiesById(Integer id);
}
