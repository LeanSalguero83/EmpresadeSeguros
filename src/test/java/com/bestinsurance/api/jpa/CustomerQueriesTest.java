package com.bestinsurance.api.jpa;

import com.bestinsurance.api.config.DomainConfig;
import com.bestinsurance.api.domain.Customer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@Disabled
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Import(DomainConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerQueriesTest extends AbstractCustomerWithAssociationsTest {


    @Test
    public void testSelectCustomersByPolicy() {
        List<Customer> customersByPolicy = customerRepository.selectCustomersByPolicy(policies.get(0).getPolicyId());
        assertEquals(3, customersByPolicy.size());
    }

    @Test
    public void testSelectCustomersByCoverage() {
        List<Customer> customerByCoverage = customerRepository.selectCustomersByCoverage(coverages.get(0).getCoverageId());
        assertEquals(3, customerByCoverage.size());
    }
    @Test
    public void testSelectCustomersWithSubscriptionBetween() {
        List<Customer> customerByCoverage = customerRepository.selectCustomersWithSubscriptionBetween(
                LocalDate.now().minusDays(1), LocalDate.now().plusMonths(7));
        assertEquals(7, customerByCoverage.size());
        assertNotNull(customerByCoverage.get(0).getCustomerSubscriptions().stream().findFirst().get().getPolicy());
    }

    @Test
    public void testSelectCustomersWithDiscount() {
        List<Customer> customerByCoverage = customerRepository.selectCustomersWithDiscount();
        assertEquals(7, customerByCoverage.size());
    }


}
