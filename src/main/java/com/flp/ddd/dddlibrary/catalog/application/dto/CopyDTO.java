package com.flp.ddd.dddlibrary.catalog.application.dto;

import com.flp.ddd.dddlibrary.catalog.domain.copy.Copy;

public record CopyDTO(
        String id,
        String barcode,
        boolean available
) {
    public static CopyDTO from(Copy copy) {
        return new CopyDTO(
                copy.getId().id().toString(),
                copy.getBarcode().barcode(),
                copy.isAvailable()
        );
    }
}
