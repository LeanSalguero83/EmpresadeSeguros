package com.bestinsurance.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public class SubscriptionView {
    private PolicyView policy;
    private CustomerView customer;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal paidPrice;
    private OffsetDateTime created;
    private OffsetDateTime updated;

    public PolicyView getPolicy() {
        return policy;
    }

    public void setPolicy(PolicyView policy) {
        this.policy = policy;
    }

    public CustomerView getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerView customer) {
        this.customer = customer;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getPaidPrice() {
        return paidPrice;
    }

    public void setPaidPrice(BigDecimal paidPrice) {
        this.paidPrice = paidPrice;
    }

    public OffsetDateTime getCreated() {
        return created;
    }

    public void setCreated(OffsetDateTime created) {
        this.created = created;
    }

    public OffsetDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(OffsetDateTime updated) {
        this.updated = updated;
    }
}
