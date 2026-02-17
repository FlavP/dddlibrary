package com.flp.ddd.dddlibrary.lending.infrastructure.external;

import com.flp.ddd.dddlibrary.lending.application.CopySearchServiceInterface;
import com.flp.ddd.dddlibrary.lending.domain.CopyDTO;
import com.flp.ddd.dddlibrary.lending.domain.CopyId;
import com.flp.ddd.dddlibrary.lending.domain.exceptions.CopyNotFoundException;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CopySearchService implements CopySearchServiceInterface {
    private final Cache<UUID, CopyDTO> copyCache;

    @Override
    public CopyDTO findCopy(CopyId copyId) {
        CopyDTO copyDTO = copyCache.getIfPresent(copyId.id());
        if (copyDTO == null) {
            throw new CopyNotFoundException(String.format("Copy with id: %s was not fount", copyId.id()));
        }
        
        return copyDTO;
    }
}
