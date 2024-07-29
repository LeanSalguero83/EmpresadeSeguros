package com.bestinsurance.api.repos;

import com.bestinsurance.api.domain.Customer;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface CustomerRepository extends CrudRepository<Customer, UUID>, JpaSpecificationExecutor<Customer> {
    public Optional<Customer> findByEmail(String email);
    @Query("""
            SELECT c FROM Customer c JOIN c.customerSubscriptions s 
            JOIN s.policy p 
            WHERE p.policyId = :policyId 
            ORDER BY c.name
            """)
    public List<Customer> selectCustomersByPolicy(UUID policyId);

    @Query("""
            SELECT c FROM Customer c 
            JOIN c.customerSubscriptions s 
            JOIN s.policy p JOIN p.coverages coverages 
            WHERE coverages.coverageId = :coverageId 
            ORDER BY c.name
            """)
    public List<Customer> selectCustomersByCoverage(UUID coverageId);

    @Query("""
            SELECT c FROM Customer c 
            JOIN c.customerSubscriptions s 
            JOIN s.policy p 
            WHERE s.paidPrice < p.price
            """)
    public List<Customer> selectCustomersWithDiscount();

    @Query("""
            SELECT c FROM Customer c 
            JOIN c.customerSubscriptions s 
            JOIN s.policy p 
            WHERE s.startDate > :start AND s.endDate < :end
            """)
    public List<Customer> selectCustomersWithSubscriptionBetween(LocalDate start, LocalDate end);


}
