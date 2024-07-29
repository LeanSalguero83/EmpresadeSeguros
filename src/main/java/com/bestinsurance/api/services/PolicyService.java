package com.bestinsurance.api.services;

import com.bestinsurance.api.domain.Policy;
import com.bestinsurance.api.repos.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
/**
 * Service class managing the Policies.-.
 */
@Service
public class PolicyService extends AbstractCrudService<Policy, UUID> {
    @Autowired
    private PolicyRepository repository;
    @Override
    protected CrudRepository<Policy, UUID> getRepository() {
        return this.repository;
    }

    public enum PolicyOrderBy {
        NAME, PRICE;
    }

    public List<Policy> findAllWithFilters(BigDecimal priceMoreThan, BigDecimal priceLessThan,
                                           BigDecimal price, String nameContains, PolicyOrderBy orderBy) {

        if (priceMoreThan != null && priceLessThan != null) {
            if (nameContains != null) {
                if (orderBy == PolicyOrderBy.PRICE) {
                    return repository.findByNameContainingAndPriceGreaterThanEqualAndPriceLessThanEqualOrderByPriceAsc(
                            nameContains, priceMoreThan, priceLessThan);
                }
                return repository.findByNameContainingAndPriceGreaterThanEqualAndPriceLessThanEqualOrderByNameAsc(
                        nameContains, priceMoreThan, priceLessThan);
            } else {
                if (orderBy == PolicyOrderBy.PRICE) {
                    return repository.findByPriceGreaterThanEqualAndPriceLessThanEqualOrderByPriceAsc(priceMoreThan, priceLessThan);
                }
                return repository.findByPriceGreaterThanEqualAndPriceLessThanEqualOrderByNameAsc(priceMoreThan, priceLessThan);
            }
        }

        if (priceMoreThan != null) { //Here priceLessThan is surely null
            if (nameContains != null) {
                if (orderBy == PolicyOrderBy.PRICE) {
                    return repository.findByPriceGreaterThanAndNameContainingOrderByPriceAsc(priceMoreThan, nameContains);
                }
                return repository.findByPriceGreaterThanAndNameContainingOrderByNameAsc(priceMoreThan, nameContains);
            } else {
                if (orderBy == PolicyOrderBy.PRICE) {
                    return repository.findByPriceGreaterThanOrderByPriceAsc(priceMoreThan);
                }
                return repository.findByPriceGreaterThanOrderByNameAsc(priceMoreThan);
            }
        }

        if (priceLessThan != null ) { //Here priceMoreThan is surely null
            if (nameContains != null) {
                if (orderBy == PolicyOrderBy.PRICE) {
                    return repository.findByPriceLessThanAndNameContainingOrderByPriceAsc(priceLessThan, nameContains);
                }
                return repository.findByPriceLessThanAndNameContainingOrderByNameAsc(priceLessThan, nameContains);
            } else {
                if (orderBy == PolicyOrderBy.PRICE) {
                    return repository.findByPriceLessThanOrderByPriceAsc(priceLessThan);
                }
                return repository.findByPriceLessThanOrderByNameAsc(priceLessThan);
            }
        }

        if (price != null) {
            if (nameContains != null) {
                //Ordering by price doesn't make sense
                return repository.findByNameContainingAndPriceOrderByNameAsc(nameContains, price);
            } else {
                return repository.findByPriceOrderByNameAsc(price);
            }
        }

        if (nameContains != null) {
            if (orderBy == PolicyOrderBy.PRICE) {
                return repository.findByNameContainingOrderByPriceAsc(nameContains);
            }
            return repository.findByNameContainingOrderByNameAsc(nameContains);

        }

        return this.findAll();
    }
}
