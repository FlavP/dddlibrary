package com.flp.ddd.dddlibrary.lending.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(LendingController.class)
@ExtendWith(MockitoExtension.class)
public class LendingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private RentBookUseCase rentBookUseCase;

    @Test
    void shouldRentANewBookWhenBookIsAvailable() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID copyId = UUID.randomUUID();
        String copyJson = """
                {
                    "userId": "%s",
                    "copyId": "%s"
                }
                """.formatted(userId, copyId);

        mockMvc.perform(post("/api/lending/copy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(copyJson))
                .andExpect(status().isCreated());
    }
}
