package com.bestinsurance.api.rest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Controller interface that defines all the crud operations implemented by the
 * REST controllrs of the BestInsurance API.
 * The DTOs are specific for each implementation and managed by each controller
 * @param <CreateDTO> Dto Generic for the create operation
 * @param <UpdateDTO> Dto Generic for the update operation
 * @param <SearchDTO> Dto Generic for the search operation
 */
@OpenAPIDefinition(info = @Info(title = "BestInsurance API", version = "v1"))
public interface CrudController<CreateDTO, UpdateDTO, SearchDTO> {
    /**
     * Create a new Object mapping the CreateDTO into a domain object
     * and returns the created object as SearchDTO
     * @param dto the object to create
     * @return the object created
     */
    @PostMapping
    SearchDTO create(@Valid @RequestBody CreateDTO dto);

    /**
     * Returns all the object managed by the implementing controller
     * @param filters a map containing the search filters
     * @return a list of SearchDTOs
     */
    @GetMapping
    List<SearchDTO> all(@RequestParam(required = false) Map<String, String> filters);

    /**
     * Allows the search by id. As the id maybe composed by more than one field
     * we use a map to get the id fields
     * @param idMap A map containing the couple <idFieldName, value>
     * @return The object matching with the provides id
     */
    SearchDTO searchById(@PathVariable(required = false) Map<String, String> idMap);

    /**
     * Updates the object matching with the provided id. As the id maybe composed by more than one field
     * we use a map to get the id fields
     * @param idMap A map containing the couple <idFieldName, value>
     * @param dto the object fields to update as UpdateDTO
     * @return the updated object
     */
    SearchDTO update(@PathVariable(required = false) Map<String, String> idMap, @Valid @RequestBody UpdateDTO dto);

    /**
     * Deletes the object matching the provided id
     * @param idMap A map containing the couple <idFieldName, value>
     */
    void delete(@PathVariable(required = false) Map<String, String> idMap);
}
