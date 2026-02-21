package com.flp.ddd.dddlibrary.lending.application;

import com.flp.ddd.dddlibrary.lending.domain.copy.CopyDTO;
import com.flp.ddd.dddlibrary.lending.domain.copy.CopyId;
import com.flp.ddd.dddlibrary.shared.events.CopyCreatedEvent;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CopyCreatedEventListener {
    private final Cache<UUID, CopyDTO> copyDTOCache;

    @Async
    @EventListener
    void on(CopyCreatedEvent event) {
        copyDTOCache.put(event.copyId(), new CopyDTO(new CopyId(event.copyId()), event.isAvailable()));
    }
}
