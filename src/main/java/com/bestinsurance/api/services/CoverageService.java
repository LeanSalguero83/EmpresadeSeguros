package com.bestinsurance.api.services;

import com.bestinsurance.api.domain.Coverage;
import com.bestinsurance.api.repos.CoverageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service class managing the Coverages.-.
 */
@Service
public class CoverageService extends AbstractCrudService<Coverage, UUID> {
    @Autowired
    private CoverageRepository coverageRepository;
    @Override
    protected CrudRepository<Coverage, UUID> getRepository() {
        return this.coverageRepository;
    }
}
