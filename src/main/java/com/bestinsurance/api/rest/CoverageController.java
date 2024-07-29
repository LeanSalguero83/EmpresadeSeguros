package com.bestinsurance.api.rest;

import com.bestinsurance.api.domain.Coverage;
import com.bestinsurance.api.dto.CoverageCreation;
import com.bestinsurance.api.dto.CoverageUpdate;
import com.bestinsurance.api.dto.CoverageView;
import com.bestinsurance.api.dto.mappers.CoverageViewMapper;
import com.bestinsurance.api.dto.mappers.DTOMapper;
import com.bestinsurance.api.services.CoverageService;
import com.bestinsurance.api.services.CrudService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Controller impelmenting the Coverages crud API
 */
@RestController
@SecurityRequirement(name = "security_auth")
@RequestMapping("/coverages")
public class CoverageController extends AbstractSimpleIdCrudController<CoverageCreation, CoverageUpdate, CoverageView, Coverage> {
    @Autowired
    private CoverageService coverageService;
    @Override
    protected CrudService<Coverage, UUID> getService() {
        return this.coverageService;
    }

    @Override
    protected DTOMapper<CoverageCreation, Coverage> getCreateDtoMapper() {
        return (dto) -> {
            Coverage entity = new Coverage();
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            return entity;
        };
    }

    @Override
    protected DTOMapper<CoverageUpdate, Coverage> getUpdateDtoMapper() {
        return (dto) -> {
            Coverage entity = new Coverage();
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            return entity;
        };
    }

    @Override
    protected DTOMapper<Coverage, CoverageView> getSearchDtoMapper() {
        return new CoverageViewMapper();
    }

}
