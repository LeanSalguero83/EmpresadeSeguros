package com.bestinsurance.api.dto.mappers;

import com.bestinsurance.api.domain.Policy;
import com.bestinsurance.api.dto.PolicyView;

public class PolicyViewMapper implements DTOMapper<Policy, PolicyView> {
    @Override
    public PolicyView map(Policy entity) {
        PolicyView dto = new PolicyView();
        dto.setId(entity.getPolicyId().toString());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setCreated(entity.getCreated());
        dto.setUpdated(entity.getUpdated());
        dto.setPoliciesCoverages(entity.getCoverages().stream().map(new CoverageViewMapper()::map).toList());
        return dto;
    }
}
