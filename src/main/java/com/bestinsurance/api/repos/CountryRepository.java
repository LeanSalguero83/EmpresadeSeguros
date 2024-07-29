package com.bestinsurance.api.repos;

import com.bestinsurance.api.domain.Country;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;


public interface CountryRepository extends CrudRepository<Country, UUID> {
}
