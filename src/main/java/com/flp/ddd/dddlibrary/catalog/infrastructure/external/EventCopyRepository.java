package com.flp.ddd.dddlibrary.catalog.infrastructure.external;

import com.flp.ddd.dddlibrary.catalog.domain.copy.Copy;
import com.flp.ddd.dddlibrary.catalog.domain.copy.CopyRepository;
import com.flp.ddd.dddlibrary.shared.events.CopyCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EventCopyRepository implements CopyRepository {
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void save(Copy copy) {
        eventPublisher.publishEvent(new CopyCreatedEvent(
                copy.getId().id(),
                copy.getBarcode().barcode(),
                copy.isAvailable()
        ));
    }
}
