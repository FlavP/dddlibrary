package com.flp.ddd.dddlibrary.shared.events;

import java.util.UUID;

public record CopyUpdatedEvent(
        UUID copyId,
        boolean loaned
) {
}
