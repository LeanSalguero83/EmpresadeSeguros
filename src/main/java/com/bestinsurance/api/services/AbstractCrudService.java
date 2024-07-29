package com.bestinsurance.api.services;

import com.bestinsurance.api.domain.DomainObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Abstract class defining the common behavior of the impelmenting services
 * @param <DomainObj> The domain object managed by the implementing class
 * @param <DomainObjId> The domain object id managed by the implementing class
 */
public abstract class AbstractCrudService<DomainObj extends DomainObject<DomainObjId>, DomainObjId>
        implements CrudService<DomainObj, DomainObjId> {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public DomainObj create(DomainObj domainObj) {
        return this.getRepository().save(domainObj);
    }

    @Override
    public List<DomainObj> findAll() {
        List<DomainObj> list = new ArrayList<>();
        this.getRepository().findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public Optional<DomainObj> getById(DomainObjId id) {
        return this.getRepository().findById(id);
    }

    @Override
    public DomainObj update(DomainObjId id, DomainObj domainObj) {
        Optional<DomainObj> fetchedObj = this.getRepository().findById(id);
        if (fetchedObj.isPresent()) {
            domainObj.setId(id);
            this.updatePreSave(fetchedObj.get(), domainObj);
            return this.getRepository().save(domainObj);
        } else {
            logger.debug("Tried to update a not existing record");
            throw new NoSuchElementException("Entity not found in database");
        }
    }

    protected void updatePreSave(DomainObj fetchedObj, DomainObj toSave) {
        /*
         * The default implementation does not do anything
         */
    }

    @Override
    public void delete(DomainObjId id) {
        Optional<DomainObj> toDeleteOpt = this.getRepository().findById(id);
        if (toDeleteOpt.isPresent()) {
            this.getRepository().delete(toDeleteOpt.get());
        } else {
            logger.debug("Tried to delete a not existing record");
            throw new NoSuchElementException("Entity not found in database");
        }

    }

    protected abstract CrudRepository<DomainObj, DomainObjId> getRepository();
}
