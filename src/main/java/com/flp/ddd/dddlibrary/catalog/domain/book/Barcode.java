package com.flp.ddd.dddlibrary.catalog.domain.book;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.Assert;

public record Barcode(String barcode) {
    public Barcode {
        Assert.notNull(barcode, "The barcode must not be null");
    }

    public Barcode() {
        this(RandomStringUtils.random(7, false, true));
    }
}
