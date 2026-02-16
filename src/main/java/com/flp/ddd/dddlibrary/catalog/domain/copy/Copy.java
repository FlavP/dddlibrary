package com.flp.ddd.dddlibrary.catalog.domain.copy;

import com.flp.ddd.dddlibrary.catalog.domain.book.Barcode;
import com.flp.ddd.dddlibrary.catalog.domain.book.BookId;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Copy {
    private final CopyId id;
    private Barcode barcode;
    private BookId bookId;
    private boolean available;
}
