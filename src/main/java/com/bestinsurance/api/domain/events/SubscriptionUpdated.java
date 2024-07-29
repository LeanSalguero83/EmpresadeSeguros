package com.bestinsurance.api.domain.events;

import com.bestinsurance.api.domain.Subscription;

import java.io.Serializable;

public class SubscriptionUpdated implements Serializable  {
    private static final long serialVersionUID = 42L;
    private Subscription subscription;

    public SubscriptionUpdated(Subscription subscription) {
        this.subscription = subscription;
    }

    public Subscription getSubscription() {
        return subscription;
    }
}
