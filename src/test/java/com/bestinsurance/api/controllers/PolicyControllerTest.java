package com.bestinsurance.api.controllers;

import com.bestinsurance.api.dto.PolicyCreation;
import com.bestinsurance.api.dto.PolicyView;
import com.bestinsurance.api.jpa.AbstractPolicyInitializedTest;
import com.bestinsurance.api.rest.PolicyController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.compress.utils.Lists;
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
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PolicyControllerTest extends AbstractPolicyInitializedTest {
    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public void initOM() {
        om.registerModule(new JavaTimeModule());
    }

    @Test
    public void testPolicyCreation() throws Exception {
        PolicyCreation policy = new PolicyCreation();
        policy.setName("test");
        policy.setDescription("description");
        policy.setPrice(new BigDecimal(100));
        List<String> coveragesIds = Lists.newArrayList();
        coveragesIds.add(coverages.get(0).getCoverageId().toString());
        coveragesIds.add(coverages.get(1).getCoverageId().toString());
        coveragesIds.add(coverages.get(2).getCoverageId().toString());
        policy.setCoveragesIds(coveragesIds);
        MvcResult mvcResult = mockMvc.perform(post("/policies")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(policy)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andReturn();
    }

    @Test
    public void testAllFiltersInitialized() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/policies")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam(PolicyController.NAME_CONTAINS, "A")
                        .queryParam(PolicyController.PRICE_LESS_THAN, "300.50")
                        .queryParam(PolicyController.PRICE_MORE_THAN, "149.50")
                        .queryParam(PolicyController.PRICE, "1000")
                        .queryParam(PolicyController.ORDERBY, "PRICE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andReturn();
        assertTrue(checkOrderByPrice(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));

        mvcResult = mockMvc.perform(get("/policies")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam(PolicyController.NAME_CONTAINS, "A")
                        .queryParam(PolicyController.PRICE_LESS_THAN , "300")
                        .queryParam(PolicyController.PRICE_MORE_THAN , "150")
                        .queryParam(PolicyController.PRICE , "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(3)))
                .andReturn();
        assertTrue(checkOrderByName(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));

        mvcResult = mockMvc.perform(get("/policies")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam(PolicyController.NAME_CONTAINS, "A")
                        .queryParam(PolicyController.PRICE_LESS_THAN , "300")
                        .queryParam(PolicyController.PRICE_MORE_THAN , "150")
                        .queryParam(PolicyController.PRICE , "1000")
                        .queryParam(PolicyController.ORDERBY , "NAME"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(3)))
                .andReturn();
        assertTrue(checkOrderByName(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));
    }

    @Test
    public void testPriceNameSearch() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/policies")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam(PolicyController.NAME_CONTAINS, "A")
                        .queryParam(PolicyController.PRICE , "100")
                        .queryParam(PolicyController.ORDERBY , "PRICE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(2)))
                .andReturn();
        assertTrue(checkOrderByName(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));

        mvcResult = mockMvc.perform(get("/policies")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam(PolicyController.NAME_CONTAINS, "A")
                        .queryParam(PolicyController.PRICE , "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(2)))
                .andReturn();
        assertTrue(checkOrderByName(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));

        mvcResult = mockMvc.perform(get("/policies")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam(PolicyController.NAME_CONTAINS, "A")
                        .queryParam(PolicyController.PRICE , "100")
                        .queryParam(PolicyController.ORDERBY , "NAME"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(2)))
                .andReturn();
        assertTrue(checkOrderByName(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));

    }

    @Test
    public void testBetweenPrices() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/policies")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam(PolicyController.PRICE_LESS_THAN , "300")
                        .queryParam(PolicyController.PRICE_MORE_THAN , "150")
                        .queryParam(PolicyController.ORDERBY , "PRICE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(30)))
                .andReturn();
        assertTrue(checkOrderByPrice(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));

        mvcResult = mockMvc.perform(get("/policies")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam(PolicyController.PRICE_LESS_THAN , "300")
                        .queryParam(PolicyController.PRICE_MORE_THAN , "150")
                        .queryParam(PolicyController.ORDERBY , "NAME"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(30)))
                .andReturn();
        assertTrue(checkOrderByName(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));

        mvcResult = mockMvc.perform(get("/policies")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam(PolicyController.PRICE_LESS_THAN , "300")
                        .queryParam(PolicyController.PRICE_MORE_THAN , "150"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(30)))
                .andReturn();
        assertTrue(checkOrderByName(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));
    }

    @Test
    public void testNameSearch() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/policies")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam(PolicyController.NAME_CONTAINS, "Double")
                        .queryParam(PolicyController.ORDERBY , "PRICE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(10)))
                .andReturn();
        assertTrue(checkOrderByPrice(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));

        mvcResult = mockMvc.perform(get("/policies")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam(PolicyController.NAME_CONTAINS, "Double")
                        .queryParam(PolicyController.ORDERBY , "NAME"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(10)))
                .andReturn();
        assertTrue(checkOrderByName(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));

        mvcResult = mockMvc.perform(get("/policies")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam(PolicyController.NAME_CONTAINS, "Double"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(10)))
                .andReturn();
        assertTrue(checkOrderByName(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));
    }

    @Test
    public void testPriceSearch() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/policies")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam(PolicyController.PRICE , "200")
                        .queryParam(PolicyController.ORDERBY , "PRICE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(10)))
                .andReturn();
        assertTrue(checkOrderByName(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));

        mvcResult = mockMvc.perform(get("/policies")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam(PolicyController.PRICE , "200")
                        .queryParam(PolicyController.ORDERBY , "PRICE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(10)))
                .andReturn();
        assertTrue(checkOrderByName(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));

        mvcResult = mockMvc.perform(get("/policies")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam(PolicyController.PRICE , "200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(10)))
                .andReturn();
        assertTrue(checkOrderByName(om.readValue(mvcResult.getResponse().getContentAsString(), PolicyView[].class)));

    }

    @Test
    public void testOrderbyLowerCase() throws Exception {
        mockMvc.perform(get("/policies")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam(PolicyController.NAME_CONTAINS, "Double")
                        .queryParam(PolicyController.ORDERBY , "price"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(10)));
        mockMvc.perform(get("/policies")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam(PolicyController.NAME_CONTAINS, "Double")
                        .queryParam(PolicyController.ORDERBY , "name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(10)));
    }

    private boolean checkOrderByPrice(PolicyView[] policies) {
        for (int i = 1; i < policies.length; i++) {
            if (policies[i].getPrice().compareTo(policies[i-1].getPrice()) == -1) return false;
        }
        return true;
    }

    private boolean checkOrderByName(PolicyView[] policies) {
        for (int i = 1; i < policies.length; i++) {
            if (policies[i].getName().compareTo(policies[i-1].getName()) == -1) return false;
        }
        return true;
    }
}
