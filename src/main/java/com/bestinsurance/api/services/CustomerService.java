package com.bestinsurance.api.services;

import com.bestinsurance.api.domain.Customer;
import com.bestinsurance.api.repos.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
/**
 * Service class managing the Customers.-.
 */
@Service
public class CustomerService extends AbstractCrudService<Customer, UUID>{
    @Autowired
    CustomerRepository customerRepository;

    @Override
    protected void updatePreSave(Customer fetched, Customer toSave) {
        toSave.setName(fetched.getName());
        toSave.setSurname(fetched.getSurname());
        toSave.setBirthDate(fetched.getBirthDate());
    }

    public enum OrderBy {
        name, surname, email, birthDate;
    }

    public enum OrderDirection {
        ASC, DESC
    }

    @Override
    protected CrudRepository<Customer, UUID> getRepository() {
        return this.customerRepository;
    }

    public List<Customer> findAllWithFilters(String name, String surname, String email, Integer ageFrom, Integer ageTo,
                                             OrderBy orderBy, OrderDirection direction, Integer pageSize, Integer pageNumber){

        Customer customer = new Customer();
        customer.setName(name);
        customer.setSurname(surname);
        customer.setEmail(email);

        ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher(OrderBy.name.name(), ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher(OrderBy.surname.name(), ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher(OrderBy.email.name(), ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<Customer> exampleCustomer = Example.of(customer, customExampleMatcher);
        Sort sort;
        if (direction == null) {
            sort = Sort.by(Sort.Direction.ASC, orderBy.name());
        } else {
            switch (direction) {
                case ASC -> sort = Sort.by(Sort.Direction.ASC, orderBy.name());
                case DESC -> sort = Sort.by(Sort.Direction.DESC, orderBy.name());
                default -> sort = Sort.by(Sort.Direction.ASC, orderBy.name());
            }
        }

        List<Customer> customerList;

        if (pageNumber != null && pageSize !=null ) {
            Page<Customer> customers = customerRepository.findAll(this.getSpecFromDatesAndExample(ageFrom, ageTo, exampleCustomer), PageRequest.of(pageNumber, pageSize, sort));
            customerList = customers.stream().toList();
        } else {
            Iterable<Customer> all = customerRepository.findAll(this.getSpecFromDatesAndExample(ageFrom, ageTo, exampleCustomer), sort);
            customerList = new ArrayList<Customer>();
            all.iterator().forEachRemaining(customerList::add);
        }
        return customerList;
    }

    private Specification<Customer> getSpecFromDatesAndExample(Integer ageFrom, Integer ageTo, Example<Customer> example) {
        return (root, query, builder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            if (ageFrom != null && ageTo != null) {
                LocalDate startDate = LocalDate.now().minusYears(ageTo);
                LocalDate endDate = LocalDate.now().minusYears(ageFrom);
                predicates.add(builder.between(root.get(OrderBy.birthDate.name()), startDate, endDate));
            }
            Predicate examplePredicate = QueryByExamplePredicateBuilder.getPredicate(root, builder, example);
            if (examplePredicate != null) {
                predicates.add(examplePredicate);
            }

            return builder.and(predicates.toArray(Predicate[] :: new));
        };
    }

    public List<Customer> findByPolicy(UUID policyId) {
        return this.customerRepository.selectCustomersByPolicy(policyId);
    }

    public List<Customer> findByCoverage(UUID coverageId) {
        return this.customerRepository.selectCustomersByCoverage(coverageId);
    }

    public List<Customer> findWithDiscount() {
        return this.customerRepository.selectCustomersWithDiscount();
    }

    public List<Customer> findWithSubscriptionBetween(LocalDate start, LocalDate end) {
        return this.customerRepository.selectCustomersWithSubscriptionBetween(start, end);
    }


}
