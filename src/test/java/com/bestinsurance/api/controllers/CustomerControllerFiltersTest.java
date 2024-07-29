package com.bestinsurance.api.controllers;

import com.bestinsurance.api.dto.CustomerView;
import com.bestinsurance.api.jpa.AbstractCustomerInitializedTest;
import com.bestinsurance.api.rest.CustomerController;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Comparator;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerControllerFiltersTest extends AbstractCustomerInitializedTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public void init() {
        om.registerModule(new JavaTimeModule());
    }

    @Test
    public void testAllFiltersInitializedOrderByNameASC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(CustomerController.NAME).setOrderDirection("ASC").runTest(3);
    }
    @Test
    public void testAllFiltersInitializedOrderByName() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(CustomerController.NAME).runTest(3);
    }

    @Test
    public void testAllFiltersInitialized() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").runTest(3);
    }
    @Test
    public void testAllFiltersInitializedOrderByNameDESC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(CustomerController.NAME).setOrderDirection("DESC").runTest(3);
    }
    @Test
    public void testAllFiltersInitializedOrderBySurnameASC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(CustomerController.SURNAME).setOrderDirection("ASC").runTest(3);
    }
    @Test
    public void testAllFiltersInitializedOrderBySurname() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(CustomerController.SURNAME).runTest(3);
    }
    @Test
    public void testAllFiltersInitializedOrderBySurnameDESC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(CustomerController.SURNAME).setOrderDirection("DESC").runTest(3);
    }

    @Test
    public void testAllFiltersInitializedOrderByAgeASC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(CustomerController.AGE).setOrderDirection("ASC").runTest(3);
    }

    @Test
    public void testAllFiltersInitializedOrderByAge() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(CustomerController.AGE).runTest(3);
    }
    @Test
    public void testAllFiltersInitializedOrderByAgeDESC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(CustomerController.AGE).setOrderDirection("DESC").runTest(3);
    }

    @Test
    public void testAllFiltersInitializedOrderByEmailASC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(CustomerController.EMAIL).setOrderDirection("ASC").runTest(3);
    }
    @Test
    public void testAllFiltersInitializedOrderByEmail() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(CustomerController.EMAIL).runTest(3);
    }
    @Test
    public void testAllFiltersInitializedOrderByEmailDESC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").setSurname("Acustomer").setAgeFrom("30").setAgeTo("60")
                .setEmail("A").setOrderBy(CustomerController.EMAIL).setOrderDirection("DESC").runTest(3);
    }

    @Test
    public void testSearchByNameOrderByAgeASC() throws Exception {
        new CustomerSearchTestHelper().setName("Acustomer").runTest(5);
    }

    @Test
    public void testAgeFromTo() throws Exception {
        new CustomerSearchTestHelper().setAgeFrom("30").setAgeTo("60").runTest(30);
    }
    @Test
    public void testAge() throws Exception {
        new CustomerSearchTestHelper().setAgeFrom("60").setAgeTo("60").runTest(10);
    }

    @Test
    public void testEmail() throws Exception {
        new CustomerSearchTestHelper().setEmail("AN").runTest(5);
    }

    @Test
    public void testAgeFromToPaged() throws Exception {
        new CustomerSearchTestHelper().setAgeFrom("0").setAgeTo("60").setPageSize("20").setPageNumber("1")
                .runTest(20);
    }

    @Test
    public void testAgeFromToPagedDefaultPageSize() throws Exception {
        new CustomerSearchTestHelper().setAgeFrom("30").setAgeTo("60").setPageNumber("1")
                .runTest(10);
    }


    private class CustomerSearchTestHelper {
        private String name;
        private String surname;
        private String email;
        private String ageFrom;
        private String ageTo;
        private String orderBy;
        private String orderDirection;
        private String pageNumber;
        private String pageSize;

        private void runTest(int expectedResults) throws Exception {
            MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/customers")
                    .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                    .contentType(MediaType.APPLICATION_JSON);
            if (name != null) mockHttpServletRequestBuilder.queryParam(CustomerController.NAME, name);
            if (surname != null) mockHttpServletRequestBuilder.queryParam(CustomerController.SURNAME, surname);
            if (email != null) mockHttpServletRequestBuilder.queryParam(CustomerController.EMAIL, email);
            if (ageFrom != null) mockHttpServletRequestBuilder.queryParam(CustomerController.AGE_FROM, ageFrom);
            if (ageTo!=null) mockHttpServletRequestBuilder.queryParam(CustomerController.AGE_TO, ageTo);
            if (orderBy != null) mockHttpServletRequestBuilder.queryParam(CustomerController.ORDER_BY, orderBy);
            if (orderDirection != null) mockHttpServletRequestBuilder.queryParam(CustomerController.ORDER_DIRECTION, orderDirection);
            if (pageNumber != null) mockHttpServletRequestBuilder.queryParam(CustomerController.PAGE_NUMBER, pageNumber);
            if (pageSize != null) mockHttpServletRequestBuilder.queryParam(CustomerController.PAGE_SIZE, pageSize);
            ResultActions actions = mockMvc.perform(mockHttpServletRequestBuilder);
            MvcResult mvcResult =  actions.andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(expectedResults)))
                    .andReturn();
            boolean ascending = orderDirection==null || !orderDirection.equals("DESC");

            Comparator<CustomerView> comparator;
            if (orderBy == null) {
                comparator = customerCompareByName();
            } else {
                switch (orderBy) {
                    case CustomerController.NAME -> comparator = customerCompareByName();
                    case CustomerController.SURNAME -> comparator = customerCompareBySurname();
                    case CustomerController.EMAIL -> comparator = customerCompareByEmail();
                    case CustomerController.AGE -> comparator = customerCompareByBirthDate();
                    default -> comparator = customerCompareByName();
                }
            }
            assertTrue(this.checkCustomersOrder(om.readValue(mvcResult.getResponse().getContentAsString(), CustomerView[].class),
                    comparator, ascending));
        }

        private boolean checkCustomersOrder(CustomerView[] customers, Comparator<CustomerView> comparator, boolean ascendingOrder) {
            for (int i = 1; i < customers.length; i++) {
                if (ascendingOrder) {
                    if (comparator.compare(customers[i - 1], customers[i]) == 1) return false;
                } else {
                    if (comparator.compare(customers[i - 1], customers[i]) == -1) return false;
                }
            }
            return true;
        }

        private Comparator<CustomerView> customerCompareByName() {
            return (CustomerView c1, CustomerView c2) -> CharSequence.compare(c1.getName(), c2.getName());
        }
        private Comparator<CustomerView> customerCompareBySurname() {
            return (CustomerView c1, CustomerView c2) -> CharSequence.compare(c1.getSurname(), c2.getSurname());
        }
        private Comparator<CustomerView> customerCompareByEmail() {
            return (CustomerView c1, CustomerView c2) -> CharSequence.compare(c1.getEmail(), c2.getEmail());
        }
        private Comparator<CustomerView> customerCompareByBirthDate() {
            return Comparator.comparing(CustomerView::getBirthDate);
        }

        public CustomerSearchTestHelper setName(String name) {
            this.name = name;
            return this;
        }

        public CustomerSearchTestHelper setSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public CustomerSearchTestHelper setEmail(String email) {
            this.email = email;
            return this;
        }

        public CustomerSearchTestHelper setAgeFrom(String ageFrom) {
            this.ageFrom = ageFrom;
            return this;
        }

        public CustomerSearchTestHelper setAgeTo(String ageTo) {
            this.ageTo = ageTo;
            return this;
        }

        public CustomerSearchTestHelper setOrderBy(String orderBy) {
            this.orderBy = orderBy;
            return this;
        }

        public CustomerSearchTestHelper setOrderDirection(String orderDirection) {
            this.orderDirection = orderDirection;
            return this;
        }

        public CustomerSearchTestHelper setPageNumber(String pageNumber) {
            this.pageNumber = pageNumber;
            return this;
        }

        public CustomerSearchTestHelper setPageSize(String pageSize) {
            this.pageSize = pageSize;
            return this;
        }
    }
}
