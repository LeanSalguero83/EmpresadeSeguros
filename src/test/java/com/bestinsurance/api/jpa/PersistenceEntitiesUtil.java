package com.bestinsurance.api.jpa;

import com.bestinsurance.api.domain.*;
import org.junit.jupiter.api.Disabled;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;
@Disabled
public class PersistenceEntitiesUtil {


    public static Customer instanceCustomer(String name, String surname, String email, LocalDate birthDate, Address address) {
        Customer c = new Customer();
        c.setName(name);
        c.setSurname(surname);
        c.setEmail(email);
        c.setBirthDate(birthDate);
        c.setAddress(address);
        return c;
    }

    public static Address instanceAddress(String street, String postalCode, City city) {
        Address a = new Address();
        a.setCity(city);
        State s = new State();
        s.setStateId(city.getState().getStateId());
        a.setState(s);
        Country co = new Country();
        co.setCountryId(city.getCountry().getCountryId());
        a.setCountry(co);
        a.setAddress(street);
        a.setPostalCode(postalCode);
        return a;
    }

    public static Policy instancePolicy(String name, String description, BigDecimal price) {
        Policy policy = new Policy();
        policy.setName(name);
        policy.setDescription(description);
        policy.setPrice(price);
        return policy;
    }

    public static Policy instancePolicy(String name, String description, BigDecimal price, Coverage... coverages) {
        Policy policy = new Policy();
        policy.setName(name);
        policy.setDescription(description);
        policy.setPrice(price);
        if (coverages != null && coverages.length > 0) {
            policy.setCoverages(Arrays.stream(coverages).collect(Collectors.toSet()));
        }
        return policy;
    }

    public static Subscription instanceSubscription(Customer customer, Policy policy, BigDecimal paidPrice,
                                                    LocalDate startDate, LocalDate endDate) {
        Subscription subscription = new Subscription();
        subscription.setCustomer(customer);
        subscription.setPolicy(policy);
        subscription.setPaidPrice(paidPrice);
        subscription.setStartDate(startDate);
        subscription.setEndDate(endDate);
        SubscriptionId id = new SubscriptionId();
        id.setCustomerId(customer.getCustomerId());
        id.setPolicyId(policy.getPolicyId());
        subscription.setId(id);
        return subscription;
    }

    public static Coverage instanceCoverage(String name, String description) {
        Coverage coverage = new Coverage();
        coverage.setName(name);
        coverage.setDescription(description);
        return coverage;
    }
}
