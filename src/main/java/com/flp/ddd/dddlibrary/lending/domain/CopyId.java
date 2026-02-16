package com.flp.ddd.dddlibrary.lending.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.util.Assert;

import java.util.UUID;

public record CopyId(@JsonValue UUID id) {
    @JsonCreator
    public CopyId {
        Assert.notNull(id, "id must not be null");
    }

    public CopyId() {
        this(UUID.randomUUID());
    }
}
