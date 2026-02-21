package com.flp.ddd.dddlibrary.lending.infrastructure.external;


import com.flp.ddd.dddlibrary.lending.domain.copy.CopyDTO;
import com.flp.ddd.dddlibrary.lending.domain.copy.CopyId;
import com.flp.ddd.dddlibrary.lending.domain.copy.CopyRepository;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class CopyCacheRepository implements CopyRepository {
    private final Cache<UUID, CopyDTO> cache;

    @Override
    public void save(CopyDTO copyDTO) {
        cache.put(copyDTO.copyId().id(), copyDTO);
    }

    @Override
    public CopyDTO findByCopyId(CopyId copyId) {
        return cache.getIfPresent(copyId.id());
    }
}
