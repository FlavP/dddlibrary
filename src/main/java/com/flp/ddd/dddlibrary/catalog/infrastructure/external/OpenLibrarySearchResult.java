package com.flp.ddd.dddlibrary.catalog.infrastructure.external;

import lombok.Builder;

import java.util.List;

@Builder
public record OpenLibrarySearchResult(List<String> publishers,
                                      String title,
                                      List<String> isbn_13,
                                      int revisions) {
}
