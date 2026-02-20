package com.flp.ddd.dddlibrary.lending.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface CopyRepository {
    void save(CopyDTO copyDTO);
    CopyDTO findByCopyId(CopyId copyId);
}
