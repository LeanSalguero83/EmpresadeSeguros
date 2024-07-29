package com.bestinsurance.api.rest;

import com.bestinsurance.api.dto.mappers.DTOMapper;
import com.bestinsurance.api.services.CrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Abstract Controller class implementing the crud operations common behavior.
 * This class is extended by the REST controllrs that provide the service implementing
 * the persistence business logic and the mappers between dto and domain objects
 * @param <CreateDTO> Dto Generic for the create operation
 * @param <UpdateDTO> Dto Generic for the update operation
 * @param <SearchDTO> Dto Generic for the search operation
 * @param <DomainObj> The domain object managed by the implementing controller
 * @param <DomainObjId> The domain object id managed by the implementing controller
 */
public abstract class AbstractCrudController<CreateDTO, UpdateDTO , SearchDTO, DomainObj, DomainObjId>
                    implements CrudController<CreateDTO, UpdateDTO, SearchDTO>{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public SearchDTO create(CreateDTO dto) {
        try {
            return this.getSearchDtoMapper().map(this.getService().create(this.getCreateDtoMapper().map(dto)));
        } catch (Exception e){
            logger.error("Error during creation: ", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<SearchDTO> all(Map<String, String> filters)  {
        try {
            return getService().findAll().stream().map(this.getSearchDtoMapper()::map).toList();
        } catch (Exception e){
            logger.error("Error during findAll: ", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public SearchDTO searchById(Map<String, String> id) {
        try {
            Optional<DomainObj> byId = this.getService().getById(this.getIdMapper().map(id));
            if (byId.isPresent()) return this.getSearchDtoMapper().map(byId.get());
        } catch (IllegalArgumentException ille) {
            logger.error("searchById: Illegal argument", ille);
            throw ille;
        } catch (Exception e){
            logger.error("Error during searchById: ", e);
            throw new RuntimeException(e.getMessage());
        }
        throw new NoSuchElementException("Entity not found in database");
    }

    @Override
    public SearchDTO update(Map<String, String> id, UpdateDTO dto) {
        try {
            return this.getSearchDtoMapper().map(this.getService().update(this.getIdMapper().map(id), this.getUpdateDtoMapper().map(dto)));
        }  catch (IllegalArgumentException ille) {
            logger.error("searchById: Illegal argument", ille);
            throw ille;
        }catch (NoSuchElementException ne) {
            logger.error("Error during update: ", ne);
            throw ne;
        } catch (Exception e){
            logger.error("Error during update: ", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(Map<String, String> id) {
        try {
            getService().delete(this.getIdMapper().map(id));
        }  catch (IllegalArgumentException ille) {
            logger.error("searchById: Illegal argument", ille);
            throw ille;
        }catch (NoSuchElementException nse) {
            logger.error("Tried to delete not existing id from db: {} ", id);
            throw nse;
        }catch (Exception e){
            logger.error("Error during delete: ", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    protected abstract CrudService<DomainObj, DomainObjId> getService();
    protected abstract DTOMapper<CreateDTO, DomainObj> getCreateDtoMapper();
    protected abstract DTOMapper<UpdateDTO, DomainObj> getUpdateDtoMapper();
    protected abstract DTOMapper<DomainObj, SearchDTO> getSearchDtoMapper();
    protected abstract DTOMapper<Map<String, String>, DomainObjId> getIdMapper();
}
