package com.flp.ddd.dddlibrary.shared.events;

import java.util.UUID;

public record CopyCreatedEvent(
        UUID copyId,
        String barcode,
        boolean isAvailable
) {
}
