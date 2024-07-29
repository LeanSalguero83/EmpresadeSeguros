package com.bestinsurance.api.dto.mappers;

import com.bestinsurance.api.domain.Coverage;
import com.bestinsurance.api.dto.CoverageView;

public class CoverageViewMapper implements DTOMapper<Coverage, CoverageView> {
    @Override
    public CoverageView map(Coverage entity) {
        return new CoverageView(entity.getCoverageId().toString(), entity.getName(), entity.getDescription());
    }
}
