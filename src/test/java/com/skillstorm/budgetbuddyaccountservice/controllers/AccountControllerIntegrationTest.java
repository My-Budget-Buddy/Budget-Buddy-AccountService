package com.skillstorm.budgetbuddyaccountservice.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAccountsList() throws Exception {
        // Set the User-ID header
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-ID", "1");

        mockMvc.perform(get("/accounts")
                .headers(headers))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    void shouldReturnAccountById() throws Exception {
        mockMvc.perform(get("/accounts/{accountId}", 1)
                .header("User-ID", "1"))  // Ensure that User-ID header is present
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturnBadRequestWhenUserIdHeaderMissing() throws Exception {
        mockMvc.perform(get("/accounts"))
                .andExpect(status().isBadRequest());  // Expect bad request if User-ID header is missing
    }
}
