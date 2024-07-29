package com.bestinsurance.api.controllers;

import com.bestinsurance.api.dto.SubscriptionRevenueView;
import com.bestinsurance.api.jpa.AbstractCustomerSubcriptionStatesTest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SubscriptionQueryTest extends AbstractCustomerSubcriptionStatesTest {
    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public void init() {
        om.registerModule(new JavaTimeModule());
    }

    @Test
    public void testRevenues() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/subscriptions/revenues")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(51)))
                .andReturn();
        List<SubscriptionRevenueView> list = om.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {});
        for (SubscriptionRevenueView revenue: list) {
            if (revenue.stateName().equals("Rhode Island")) {
                assertEquals(new BigDecimal("800.00"), revenue.revenue());
                assertEquals(8L, revenue.customersCount());
            } else if (revenue.stateName().equals("District of Columbia") || revenue.stateName().equals("Hawaii")) {
                assertEquals(new BigDecimal("100.00"), revenue.revenue());
                assertEquals(1L, revenue.customersCount());
            }else {
                assertEquals(new BigDecimal("1000.00"), revenue.revenue());
                assertEquals(10L, revenue.customersCount());
            }
        }
    }
}
