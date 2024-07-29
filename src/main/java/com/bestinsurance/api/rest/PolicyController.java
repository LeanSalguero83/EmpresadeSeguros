package com.bestinsurance.api.rest;

import com.bestinsurance.api.domain.Coverage;
import com.bestinsurance.api.domain.Policy;
import com.bestinsurance.api.dto.PolicyCreation;
import com.bestinsurance.api.dto.PolicyUpdate;
import com.bestinsurance.api.dto.PolicyView;
import com.bestinsurance.api.dto.mappers.DTOMapper;
import com.bestinsurance.api.dto.mappers.PolicyViewMapper;
import com.bestinsurance.api.services.CrudService;
import com.bestinsurance.api.services.PolicyService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/**
 * Controller impelmenting the Policies crud API
 */
@RestController
@SecurityRequirement(name = "security_auth")
@RequestMapping("/policies")
public class PolicyController extends AbstractSimpleIdCrudController<PolicyCreation, PolicyUpdate, PolicyView, Policy> {
    public static final String NAME_CONTAINS = "nameContains";
    public static final String PRICE_MORE_THAN = "priceMoreThan";
    public static final String PRICE_LESS_THAN = "priceLessThan";
    public static final String PRICE = "price";
    public static final String ORDERBY = "orderBy";

    @Autowired
    private PolicyService policyService;
    @Override
    protected CrudService<Policy, UUID> getService() {
        return this.policyService;
    }

    @Override
    @Parameter(in = ParameterIn.QUERY, name = NAME_CONTAINS, schema = @Schema(type="string"), required = false)
    @Parameter(in = ParameterIn.QUERY, name = PRICE_MORE_THAN, schema = @Schema(type="number"), required = false)
    @Parameter(in = ParameterIn.QUERY, name = PRICE_LESS_THAN, schema = @Schema(type="number"), required = false)
    @Parameter(in = ParameterIn.QUERY, name = PRICE, schema = @Schema(type="number"), required = false)
    @Parameter(in = ParameterIn.QUERY, name = ORDERBY, schema = @Schema(type="string", allowableValues = {"NAME", "PRICE"}), required = false)
    public List<PolicyView> all(Map<String, String> filters) {
        try {
            BigDecimal priceMoreThan = filters.get(PRICE_MORE_THAN) == null ? null : new BigDecimal(filters.get(PRICE_MORE_THAN));
            BigDecimal priceLessThan = filters.get(PRICE_LESS_THAN) == null ? null : new BigDecimal(filters.get(PRICE_LESS_THAN));
            BigDecimal price = filters.get(PRICE) == null ? null : new BigDecimal(filters.get(PRICE));
            String nameContains = filters.get(NAME_CONTAINS);
            PolicyService.PolicyOrderBy orderBy =
                    filters.get(ORDERBY) == null ? null : PolicyService.PolicyOrderBy.valueOf(filters.get(ORDERBY).toUpperCase());

            return policyService.findAllWithFilters(priceMoreThan, priceLessThan, price, nameContains, orderBy)
                    .stream().map(this.getSearchDtoMapper()::map).toList();
        } catch (Exception e){
            logger.error("Error during searchById: ", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected DTOMapper<PolicyCreation, Policy> getCreateDtoMapper() {
        return (dto) -> {
            Policy policy = new Policy();
            policy.setName(dto.getName());
            policy.setDescription(dto.getDescription());
            policy.setPrice(dto.getPrice());
            policy.setCoverages(new HashSet<>());
            for (String coverageId: dto.getCoveragesIds()) {
                Coverage coverage = new Coverage();
                coverage.setCoverageId(UUID.fromString(coverageId));
                policy.getCoverages().add(coverage);
            }
            return policy;
        };
    }

    @Override
    protected DTOMapper<PolicyUpdate, Policy> getUpdateDtoMapper() {
        return (dto) -> {
            Policy policy = new Policy();
            policy.setName(dto.getName());
            policy.setDescription(dto.getDescription());
            policy.setPrice(dto.getPrice());
            policy.setCoverages(new HashSet<>());
            for (String coverageId: dto.getCoveragesIds()) {
                Coverage coverage = new Coverage();
                coverage.setCoverageId(UUID.fromString(coverageId));
                policy.getCoverages().add(coverage);
            }
            return policy;
        };
    }

    @Override
    protected DTOMapper<Policy, PolicyView> getSearchDtoMapper() {
        return new PolicyViewMapper();
    }

}
