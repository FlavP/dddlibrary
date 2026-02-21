package com.flp.ddd.dddlibrary.lending.application;

import com.flp.ddd.dddlibrary.lending.domain.copy.CopyDTO;
import com.flp.ddd.dddlibrary.lending.infrastructure.config.CacheConfig;
import com.flp.ddd.dddlibrary.shared.events.CopyCreatedEvent;
import com.github.benmanes.caffeine.cache.Cache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = {
        CopyCreatedEventListener.class,
        CacheConfig.class
})
@EnableAsync
public class CopyCreatedEventListenerTest {
    @Autowired
    private ApplicationEventPublisher testEventPublisher;
    @Autowired
    Cache<UUID, CopyDTO> copyDTOCache;

    @Test
    void shouldAddTheCopyToCacheWhenOrderCreatedEvent() {
        UUID copyId = UUID.randomUUID();
        testEventPublisher.publishEvent(new CopyCreatedEvent(
                copyId, "000111222", true));

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            CopyDTO lendingCopyDTO = copyDTOCache.getIfPresent(copyId);
            assertThat(lendingCopyDTO).isNotNull();
            assertThat(lendingCopyDTO.available()).isEqualTo(true);
        });
    }
}
