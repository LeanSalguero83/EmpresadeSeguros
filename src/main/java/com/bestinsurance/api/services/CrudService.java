package com.bestinsurance.api.services;

import java.util.List;
import java.util.Optional;

/**
 * Service interface that defines the crud operaitons business logic on domain objects
 * @param <DomainObj> The domain object managed by the implementing class
 * @param <DomainObjId> The domain object id managed by the implementing class
 */
public interface CrudService<DomainObj, DomainObjId> {
    /**
     * Creates a Domain object
     * @param obj the object to create
     * @return The object created
     */
    DomainObj create(DomainObj obj);

    /**
     * Retrieves all the objects
     * @return a list of DomainObj
     */
    List<DomainObj> findAll();

    /**
     * Implements the search by Id
     * @param id The Id of the object to search
     * @return the object found
     */
    Optional<DomainObj> getById(DomainObjId id);

    /**
     * Updates a domain object
     * @param id the id of the object to update
     * @param obj The object containing the changes to apply
     * @return the updated object
     */
    DomainObj update(DomainObjId id, DomainObj obj);

    /**
     * Deletes a domain object by id
     * @param id the id of the object to delete
     */
    void delete(DomainObjId id);
}
