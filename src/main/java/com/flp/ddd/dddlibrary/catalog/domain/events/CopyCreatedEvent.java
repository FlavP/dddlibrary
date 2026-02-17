package com.flp.ddd.dddlibrary.catalog.domain.events;

import com.flp.ddd.dddlibrary.catalog.application.dto.CopyDTO;

import java.util.UUID;

public record CopyCreatedEvent(UUID copyId, CopyDTO copyDTO) {
}
