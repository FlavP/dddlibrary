package com.flp.ddd.dddlibrary.catalog.infrastructure.persistence.copy;

import com.flp.ddd.dddlibrary.catalog.infrastructure.persistence.book.BookEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "copies")
public class CopyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "copy_id", unique = true, nullable = false)
    private UUID copyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;

    private String barcode;
    private boolean isAvailable;
}