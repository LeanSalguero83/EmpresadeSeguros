package com.bestinsurance.api.domain;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class SubscriptionId implements Serializable {
    private static final long serialVersionUID = 42L;
    private UUID policyId;
    private UUID customerId;

    public UUID getPolicyId() {
        return policyId;
    }

    public void setPolicyId(UUID policyId) {
        this.policyId = policyId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionId that = (SubscriptionId) o;
        return policyId.equals(that.policyId) && customerId.equals(that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(policyId, customerId);
    }
}
