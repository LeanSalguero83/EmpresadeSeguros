package com.bestinsurance.api.controllers;

import com.bestinsurance.api.domain.Address;
import com.bestinsurance.api.domain.Customer;
import com.bestinsurance.api.domain.Policy;
import com.bestinsurance.api.jpa.PersistenceEntitiesUtil;
import com.bestinsurance.api.repos.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.bestinsurance.api.jpa.PersistenceEntitiesUtil.instancePolicy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CsvUploadTest {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PolicyRepository policyRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public void initDB(){
        Policy silver = instancePolicy("Silver", "Silver Policy", new BigDecimal(100));
        Policy gold = instancePolicy("Gold", "Gold Policy", new BigDecimal(200));
        Policy platinum = instancePolicy("Platinum", "Silver Policy", new BigDecimal(300));

        policyRepository.save(silver);
        policyRepository.save(gold);
        policyRepository.save(platinum);
    }

    @AfterAll
    public void cleanDB() {
        policyRepository.deleteAll();
    }

    @AfterEach
    public void cleanSingleTestDB() {
        subscriptionRepository.deleteAll();
        customerRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    public void testCsvUpload() throws Exception {
        ClassPathResource res = new ClassPathResource("customers.csv");
        MockMultipartFile multipartFile = new MockMultipartFile("file", new FileInputStream(res.getFile()));
        mockMvc.perform(multipart("/subscriptions/upload")
                        .file(multipartFile)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
        Iterable<Customer> all = customerRepository.findAll();
        List<Customer> customers = new ArrayList<>();
        all.forEach(customers::add);
        assertEquals(500, customers.size());
    }

    @Test
    public void noCityError() throws Exception {
        ClassPathResource res = new ClassPathResource("customers_nocityerror.csv");
        MockMultipartFile multipartFile = new MockMultipartFile("file", new FileInputStream(res.getFile()));
        mockMvc.perform(multipart("/subscriptions/upload")
                        .file(multipartFile)
                    .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound());
        Iterable<Customer> all = customerRepository.findAll();
        List<Customer> customers = new ArrayList<>();
        all.forEach(customers::add);
        assertEquals(0, customers.size());
    }

    @Test
    public void noStateError() throws Exception {
        ClassPathResource res = new ClassPathResource("customers_nostateerror.csv");
        MockMultipartFile multipartFile = new MockMultipartFile("file", new FileInputStream(res.getFile()));
        mockMvc.perform(multipart("/subscriptions/upload")
                        .file(multipartFile)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound());
        Iterable<Customer> all = customerRepository.findAll();
        List<Customer> customers = new ArrayList<>();
        all.forEach(customers::add);
        assertEquals(0, customers.size());
    }

    @Test
    public void existingEmail() throws Exception {
        final String email = "alvinhoke@hotmail.com";
        final String newCityId = "035c1dbb-6f5e-48c4-a6f7-22abf17a9f7d";
        final String newPostalCode = "30114";
        Address address = PersistenceEntitiesUtil.instanceAddress("OLD ADDRESS", "OLDPostalCode",
                cityRepository.findById(UUID.fromString("3d8dfce1-49bc-4ad7-88b5-f4294c410fba")).get());
        Customer customer = PersistenceEntitiesUtil.instanceCustomer("Matha", "Loera", email,
                LocalDate.parse("1957-01-31", DateTimeFormatter.ofPattern("yyyy-MM-dd")), address);
        customerRepository.save(customer);

        ClassPathResource res = new ClassPathResource("customers_emailtest.csv");
        MockMultipartFile multipartFile = new MockMultipartFile("file", new FileInputStream(res.getFile()));
        mockMvc.perform(multipart("/subscriptions/upload")
                        .file(multipartFile)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
        Iterable<Customer> all = customerRepository.findAll();
        int size = 0;
        for (Customer c: all){
            if (c.getEmail().equals(email)) {
                assertEquals("4203 Parsonage Circle", c.getAddress().getAddress());
                assertEquals(newCityId, c.getAddress().getCity().getCityId().toString());
                assertEquals(newPostalCode, c.getAddress(). getPostalCode());
            }
            size++;
        }
        assertEquals(3, size);
    }
}
