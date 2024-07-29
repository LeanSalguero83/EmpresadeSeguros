package com.bestinsurance.api.controllers;

import com.bestinsurance.api.jpa.AbstractCustomerWithAssociationsTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerControllerSearchTest extends AbstractCustomerWithAssociationsTest {
    @Autowired
    private MockMvc mockMvc;
    @Test
    public void testSelectCustomersByPolicy() throws Exception {
        mockMvc.perform(get("/customers/policy/{id}", policies.get(0).getPolicyId().toString())
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void testSelectCustomersByCoverage() throws Exception {
        mockMvc.perform(get("/customers/coverage/{id}", coverages.get(0).getCoverageId().toString())
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void testSelectCustomersWithSubscriptionBetween() throws Exception {
        LocalDate startDate = LocalDate.now().minusDays(1);
        String startDateString = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.now().plusMonths(7);
        String endDateString = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        mockMvc.perform(get("/customers/subscriptions")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("startDate", startDateString)
                        .queryParam("endDate", endDateString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(7)));
    }
    @Test
    public void testSelectCustomersWithDiscount() throws Exception {
        mockMvc.perform(get("/customers/subscriptions/discountedPrice")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(7)));
    }
}
