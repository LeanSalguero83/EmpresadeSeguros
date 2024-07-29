package com.bestinsurance.api.rest;

import com.bestinsurance.api.dto.mappers.DTOMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Map;
import java.util.UUID;

/**
 * Specification of the AbstractCrudController, extended by controllrs with an id of type UUID
 * @param <CreateDTO> Dto Generic for the create operation
 * @param <UpdateDTO> Dto Generic for the update operation
 * @param <SearchDTO> Dto Generic for the search operation
 * @param <DomainObj> The domain object managed by the implementing controller
 */
public abstract class AbstractSimpleIdCrudController<CreateDTO, UpdateDTO , SearchDTO, DomainObj> extends
        AbstractCrudController<CreateDTO, UpdateDTO , SearchDTO, DomainObj, UUID>{
    protected static final String ID = "id";

    @GetMapping("/{" + ID + "}")
    @Parameter(in = ParameterIn.PATH, name = ID, schema = @Schema(type="string"), required = true)
    @Override
    public SearchDTO searchById(Map<String, String> idMap) {
        return super.searchById(idMap);
    }

    @PutMapping("/{" + ID + "}")
    @Parameter(in = ParameterIn.PATH, name = ID, schema = @Schema(type="string"), required = true)
    @Override
    public SearchDTO update(Map<String, String> id, UpdateDTO updateDTO) {
        return super.update(id, updateDTO);
    }

    @DeleteMapping("/{" + ID + "}")
    @Parameter(in = ParameterIn.PATH, name = ID, schema = @Schema(type="string"), required = true)
    @Override
    public void delete(Map<String, String> id) {
        super.delete(id);
    }

    @Override
    protected DTOMapper<Map<String, String>, UUID> getIdMapper() {
        return (idMap) -> UUID.fromString(idMap.get(ID));
    }
}
