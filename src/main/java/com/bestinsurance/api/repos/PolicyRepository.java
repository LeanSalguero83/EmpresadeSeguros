package com.bestinsurance.api.repos;

import com.bestinsurance.api.domain.Policy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


public interface PolicyRepository extends CrudRepository<Policy, UUID>, QueryByExampleExecutor<Policy> {
    Policy findByName(String name);
    List<Policy> findByPriceGreaterThanOrderByNameAsc(BigDecimal price);
    List<Policy> findByPriceGreaterThanOrderByPriceAsc(BigDecimal price);
    List<Policy> findByPriceLessThanOrderByNameAsc(BigDecimal price);
    List<Policy> findByPriceLessThanOrderByPriceAsc(BigDecimal price);

    List<Policy> findByPriceGreaterThanAndNameContainingOrderByNameAsc(BigDecimal price, String name);
    List<Policy> findByPriceGreaterThanAndNameContainingOrderByPriceAsc(BigDecimal price, String name);
    List<Policy> findByPriceOrderByNameAsc(BigDecimal price);

    List<Policy> findByPriceGreaterThanEqualAndPriceLessThanEqualOrderByNameAsc(BigDecimal min ,BigDecimal max);
    List<Policy> findByPriceGreaterThanEqualAndPriceLessThanEqualOrderByPriceAsc(BigDecimal min ,BigDecimal max);
    List<Policy> findByNameContainingOrderByNameAsc(String search);
    List<Policy> findByNameContainingOrderByPriceAsc(String search);
    List<Policy> findByNameContainingAndPriceGreaterThanEqualAndPriceLessThanEqualOrderByNameAsc(String search, BigDecimal min ,BigDecimal max);
    List<Policy> findByNameContainingAndPriceGreaterThanEqualAndPriceLessThanEqualOrderByPriceAsc(String search, BigDecimal min ,BigDecimal max);
    List<Policy> findByNameContainingAndPriceOrderByNameAsc(String search, BigDecimal price);

    List<Policy> findByPriceLessThanAndNameContainingOrderByPriceAsc(BigDecimal priceLessThan, String nameContains);

    List<Policy> findByPriceLessThanAndNameContainingOrderByNameAsc(BigDecimal priceLessThan, String nameContains);
}
