package com.skillstorm.budgetbuddyaccountservice.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

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
                .header("User-ID", "1"))  
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturnBadRequestWhenUserIdHeaderMissing() throws Exception {
        mockMvc.perform(get("/accounts"))
                .andExpect(status().isBadRequest()); 
                //should get 400 bad request because the User-ID header is missing
                //getting 403 instead
    }

    @Test
    void shouldCreateNewAccount() throws Exception {
        // Set the User-ID header
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-ID", "1");

        // Create a new account JSON
        String newAccountJson = "{\"name\":\"New Account\",\"balance\":1000}";

        mockMvc.perform(post("/accounts")
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newAccountJson))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("New Account"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(1000));
    }

    @Test
    void shouldUpdateExistingAccount() throws Exception {
        // Set the User-ID header
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-ID", "1");

        // Update account JSON
        String updateAccountJson = "{\"name\":\"Updated Account\",\"balance\":2000}";

        mockMvc.perform(put("/accounts/{accountId}", 1)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateAccountJson))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Account"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(2000));
    }

    @Test
    void shouldDeleteAccount() throws Exception {
        // Set the User-ID header
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-ID", "1");

        mockMvc.perform(delete("/accounts/{accountId}", 1)
                .headers(headers))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundForNonExistentAccount() throws Exception {
        // Set the User-ID header
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-ID", "1");

        mockMvc.perform(get("/accounts/{accountId}", 999)
                .headers(headers))
                .andExpect(status().isNotFound());
    }
}
