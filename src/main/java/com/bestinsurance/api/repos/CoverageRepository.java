package com.bestinsurance.api.repos;

import com.bestinsurance.api.domain.Coverage;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;


public interface CoverageRepository extends CrudRepository<Coverage, UUID> {
}
