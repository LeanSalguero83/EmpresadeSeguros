package com.bestinsurance.api.repos;

import com.bestinsurance.api.domain.State;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;


public interface StateRepository extends CrudRepository<State, UUID> {
    @Query("select distinct(state) from State state join fetch state.stateCities c order by state.name, c.name asc")
    List<State> selectStatesAndCitiesOrdered();
}
