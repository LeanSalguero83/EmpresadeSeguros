package com.bestinsurance.api.controllers;

import com.bestinsurance.api.domain.*;
import com.bestinsurance.api.dto.SubscriptionCreation;
import com.bestinsurance.api.dto.SubscriptionLogMsg;
import com.bestinsurance.api.repos.CoverageRepository;
import com.bestinsurance.api.repos.CustomerRepository;
import com.bestinsurance.api.repos.PolicyRepository;
import com.bestinsurance.api.repos.SubscriptionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.jms.TextMessage;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = { "eventlistener.enabled=true" })
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SubscriptionCRUDControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private CoverageRepository coverageRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Value("${eventlistener.queue.name}")
    String queueName;

    private Customer customer;
    private Policy policy;

    private Subscription subscription;

    @BeforeAll
    public void init() throws Exception {
        om.registerModule(new JavaTimeModule());
        this.customer = createTestCustomer();
        this.policy = createTestPolicy("init");
        this.subscription = createSubscription(this.customer, this.policy);
        this.checkJMSMessageAndConsume(this.customer.getCustomerId(), this.policy.getPolicyId());
    }

    @AfterAll
    public void cleanup() {
        this.subscriptionRepository.deleteAll();
        this.customerRepository.deleteAll();
        this.policyRepository.deleteAll();
    }


    @Test
    public void testCreateSubscription() throws Exception  {
        SubscriptionCreation subscriptionDTO = new SubscriptionCreation();
        subscriptionDTO.setCustomerId(this.customer.getCustomerId().toString());
        // Let's create a new policy to create a new subscription
        Policy testPolicy = createTestPolicy("testCreate");
        subscriptionDTO.setPolicyId(testPolicy.getPolicyId().toString());
        subscriptionDTO.setStartDate(LocalDate.now());
        subscriptionDTO.setEndDate(LocalDate.now().plusYears(1));
        subscriptionDTO.setPaidPrice(new BigDecimal(100.00));
        MvcResult mvcResult = mockMvc.perform(post("/subscriptions")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(subscriptionDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer.id", notNullValue()))
                .andExpect(jsonPath("$.policy.id", notNullValue()))
                .andReturn();
        //JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        this.checkJMSMessageAndConsume(this.customer.getCustomerId(), testPolicy.getPolicyId());
    }

    @Test
    public void testFindById() throws Exception {
         mockMvc.perform(get("/subscriptions/{idCustomer}/{idPolicy}", this.subscription.getCustomer().getCustomerId().toString()
                         , this.subscription.getPolicy().getPolicyId().toString())
                         .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                 .contentType(MediaType.APPLICATION_JSON)
                 .queryParam("idCustomer", this.customer.getCustomerId().toString())
                 .queryParam("idPolicy", this.policy.getPolicyId().toString()))
                 .andDo(print())
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$.customer.id", notNullValue()))
                 .andExpect(jsonPath("$.policy.id", notNullValue()));
    }

    @Test
    public void testUpdate() throws Exception {
        LocalDate updateDate = LocalDate.now().plusYears(3);
        String formattedDateTime = updateDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        SubscriptionCreation subscriptionDTO = new SubscriptionCreation();
        subscriptionDTO.setCustomerId(this.customer.getCustomerId().toString());
        subscriptionDTO.setPolicyId(this.policy.getPolicyId().toString());
        subscriptionDTO.setStartDate(LocalDate.now());
        subscriptionDTO.setEndDate(updateDate);
        subscriptionDTO.setPaidPrice(new BigDecimal(150));

        mockMvc.perform(put("/subscriptions/{idCustomer}/{idPolicy}", this.subscription.getCustomer().getCustomerId().toString()
                        , this.subscription.getPolicy().getPolicyId().toString())
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("idCustomer", this.customer.getCustomerId().toString())
                        .queryParam("idPolicy", this.policy.getPolicyId().toString())
                        .content(om.writeValueAsString(subscriptionDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer.id", notNullValue()))
                .andExpect(jsonPath("$.policy.id", notNullValue()))
                .andExpect(jsonPath("$.paidPrice", is(150)))
                .andExpect(jsonPath("$.endDate", is(formattedDateTime)));

        this.checkJMSMessageAndConsume(this.subscription.getCustomer().getCustomerId(), this.subscription.getPolicy().getPolicyId());
    }

    @Test
    public void testDelete() throws Exception {
        Policy testPolicy = createTestPolicy("testDElete");
        Subscription subscription1 = createSubscription(this.customer, testPolicy);
        mockMvc.perform(delete("/subscriptions/{idCustomer}/{idPolicy}", subscription1.getCustomer().getCustomerId().toString()
                        , subscription1.getPolicy().getPolicyId().toString())
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("idCustomer", customer.getCustomerId().toString())
                        .queryParam("idPolicy", testPolicy.getPolicyId().toString()))
                .andDo(print())
                .andExpect(status().isOk());
        this.checkJMSMessageAndConsume(subscription1.getCustomer().getCustomerId(), testPolicy.getPolicyId());
    }

    private Customer createTestCustomer(){
        Customer customer = new Customer();
        customer.setName("testNameSubs");
        customer.setSurname("testSurnameSubs");
        customer.setEmail("testEmailSubs@email.com");
        customer.setBirthDate(LocalDate.now().minusYears(30));
        Address address = new Address();
        address.setAddress("123 Test Street, APT 4");
        address.setPostalCode("12345-44");
        Country country = new Country();
        country.setCountryId(UUID.fromString("d4153ed2-91e6-40da-a3c5-1de8a6d0119c"));
        address.setCountry(country);
        State state = new State();
        state.setStateId(UUID.fromString("4b62177f-7eb0-448e-86c1-3e168e44cc29"));
        address.setState(state);
        City city = new City();
        city.setCityId(UUID.fromString("45576d7c-8d84-4422-9440-19ef80fa16f3"));
        address.setCity(city);
        customer.setAddress(address);
        return customerRepository.save(customer);
    }
    //SAve and return result
    private Policy createTestPolicy(String name) {
        Set<Coverage> coverages = new LinkedHashSet<>();
        for (int i=0; i <= 2; i++) {
            Coverage coverage = new Coverage();
            coverage.setName("testCoverage" + i);
            coverage.setDescription("subs coverage description " + i);
            coverages.add(coverageRepository.save(coverage));
        }

        Policy policy = new Policy();
        policy.setName(name);
        policy.setDescription("policy test subscriptions description");
        policy.setPrice(new BigDecimal(100.50));
        policy.setCoverages(coverages);

        return policyRepository.save(policy);
    }

    private Subscription createSubscription(Customer c, Policy p) {
        SubscriptionId id = new SubscriptionId();
        id.setCustomerId(c.getCustomerId());
        id.setPolicyId(p.getPolicyId());
        Subscription subscription = new Subscription();
        subscription.setId(id);
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusYears(1));
        subscription.setPaidPrice(new BigDecimal(100.00));
        subscription.setCustomer(c);
        subscription.setPolicy(p);
        return subscriptionRepository.save(subscription);
    }

    /**
     * Checks if the subscription has been notified.-.
     * @param checkIdCustomer
     * @param checkIdPolicy
     * @throws Exception
     */
    private void checkJMSMessageAndConsume(UUID checkIdCustomer, UUID checkIdPolicy) throws Exception {
        TextMessage browse = this.jmsTemplate.<TextMessage>browse(this.queueName, (session, browser) -> {
            Enumeration<?> browserEnumeration = browser.getEnumeration();
            assertTrue(browserEnumeration.hasMoreElements());
            boolean messageSent = false;
            while (browserEnumeration.hasMoreElements() && !messageSent) {
                TextMessage message = (TextMessage) browserEnumeration.nextElement();
                String logMessageJson = message.getText();
                try {
                    SubscriptionLogMsg logMessage = om.readValue(logMessageJson, SubscriptionLogMsg.class);

                if(checkIdCustomer.equals(logMessage.getCustomerId()) && checkIdPolicy.equals(logMessage.getPolicyId())) {
                    messageSent = true;

                }
                } catch (Exception e) {
                    fail();
                }
            }
            assertTrue(messageSent);
            return null;
        });
    }

}
