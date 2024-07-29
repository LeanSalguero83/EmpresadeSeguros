package com.bestinsurance.api.services;

import com.bestinsurance.api.domain.*;
import com.bestinsurance.api.dto.CsvSubscriptions;
import com.bestinsurance.api.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CsvService {
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

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Transactional
    public void save(List<CsvSubscriptions> csvCustomers) {

        for (CsvSubscriptions csvCustomer: csvCustomers) {
            Policy policy = policyRepository.findByName(csvCustomer.getPolicyName());
            if (policy == null) {
                throw new NoSuchElementException("Policy " + csvCustomer.getPolicyName() + " not found");
            }
            City city = cityRepository.selectByStateNameAndCityName(csvCustomer.getStateName(), csvCustomer.getCityName());
            if (city == null) {
                throw new NoSuchElementException("City: " + csvCustomer.getCityName() + " State: " + csvCustomer.getStateName() + " not found");
            }
            Optional<Customer> byEmail = customerRepository.findByEmail(csvCustomer.getEmail());
            Customer savedCust;
            if (byEmail.isPresent()) {
                savedCust = this.updateCustomer(byEmail.get(), byEmail.get().getAddress(), city, csvCustomer);
            } else {
                Address address = new Address();
                Customer customer = new Customer();
                customer.setAddress(address);
                savedCust = this.updateCustomer(customer, address, city, csvCustomer);
            }

            Subscription subscription = new Subscription();
            SubscriptionId id = new SubscriptionId();
            id.setPolicyId(policy.getPolicyId());
            id.setCustomerId(savedCust.getCustomerId());
            subscription.setId(id);
            subscription.setPolicy(policy);
            subscription.setCustomer(savedCust);
            subscription.setPaidPrice(new BigDecimal(csvCustomer.getPaidPrice()));
            subscription.setStartDate(LocalDate.parse(csvCustomer.getStartDate(), this.dateTimeFormatter));
            subscription.setEndDate(LocalDate.parse(csvCustomer.getEndDate(), this.dateTimeFormatter));
            subscriptionRepository.save(subscription);
        }
    }
    private Customer updateCustomer(Customer customer, Address address, City city, CsvSubscriptions csvCustomer) {
        address.setPostalCode(csvCustomer.getPostalcode());
        address.setAddress(csvCustomer.getAddress());
        address.setCountry(city.getCountry());
        address.setState(city.getState());
        address.setCity(city);
        customer.setName(csvCustomer.getName());
        customer.setEmail(csvCustomer.getEmail());
        customer.setSurname(csvCustomer.getSurname());
        customer.setBirthDate(LocalDate.parse(csvCustomer.getBirthDate(), this.dateTimeFormatter));
        return customerRepository.save(customer);

    }
}
