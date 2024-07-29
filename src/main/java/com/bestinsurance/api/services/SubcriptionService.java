package com.bestinsurance.api.services;

import com.bestinsurance.api.domain.StateSubscriptionRevenue;
import com.bestinsurance.api.domain.Subscription;
import com.bestinsurance.api.domain.SubscriptionId;
import com.bestinsurance.api.repos.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service class managing the Subscriptions.-.
 */
@Service
public class SubcriptionService extends AbstractCrudService<Subscription, SubscriptionId> {
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    protected CrudRepository<Subscription, SubscriptionId> getRepository() {
        return this.subscriptionRepository;
    }

    @Override
    protected void updatePreSave(Subscription fetchedObj, Subscription toSave) {
        super.updatePreSave(fetchedObj, toSave);
        toSave.setPolicy(fetchedObj.getPolicy());
        toSave.setCustomer(fetchedObj.getCustomer());
    }

    public List<StateSubscriptionRevenue> computeRevenues() {
        return this.subscriptionRepository.selectStatesRevenues();
    }
}
