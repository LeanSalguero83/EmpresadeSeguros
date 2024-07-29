package com.bestinsurance.api.repos;

import com.bestinsurance.api.domain.StateSubscriptionRevenue;
import com.bestinsurance.api.domain.Subscription;
import com.bestinsurance.api.domain.SubscriptionId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface SubscriptionRepository extends CrudRepository<Subscription, SubscriptionId> {
    @Query(value = """
            select new com.bestinsurance.api.domain.StateSubscriptionRevenue(state.name, sum(sub.paidPrice), count(customer)) 
            from Subscription sub
            join sub.customer customer 
            join customer.address addr 
            join addr.state state
            group by state.stateId,state.name
            """)
    public List<StateSubscriptionRevenue> selectStatesRevenues();
}
