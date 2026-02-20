package com.flp.ddd.dddlibrary.catalog.application;

import com.flp.ddd.dddlibrary.catalog.infrastructure.persistence.copy.JpaCopyRepository;
import com.flp.ddd.dddlibrary.shared.events.CopyUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CopyUpdatedEventListener {
    private final JpaCopyRepository jpaCopyRepository;

    @Async
    @EventListener
    @Transactional
    public void on(CopyUpdatedEvent event) {
        jpaCopyRepository.findByCopyId(event.copyId())
                .ifPresent(copyEntity -> {
                    // loaned = true means the copy is NOT available
                    copyEntity.setAvailable(!event.loaned());
                    jpaCopyRepository.save(copyEntity);
                });
    }
}


