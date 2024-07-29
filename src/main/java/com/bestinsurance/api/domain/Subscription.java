package com.bestinsurance.api.domain;

import com.bestinsurance.api.domain.events.SubscriptionUpdated;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;


@Entity
@Table(name="subscriptions")
@EntityListeners(AuditingEntityListener.class)
public class Subscription implements Serializable, DomainObject<SubscriptionId> {
    private static final long serialVersionUID = 42L;
    @EmbeddedId
    private SubscriptionId id;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal paidPrice;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime created;

    @LastModifiedDate
    @Column
    private OffsetDateTime updated;

    @MapsId("policyId")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @MapsId("customerId")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    @DomainEvents
    public SubscriptionUpdated domainEvent() {
        return new SubscriptionUpdated(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", paidPrice=" + paidPrice +
                ", created=" + created +
                ", updated=" + updated +
                ", policy=" + policy +
                ", customer=" + customer +
                '}';
    }

    public void setId(SubscriptionId id) { this.id = id; }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(final LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(final LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getPaidPrice() {
        return paidPrice;
    }

    public void setPaidPrice(final BigDecimal paidPrice) {
        this.paidPrice = paidPrice;
    }

    public OffsetDateTime getCreated() {
        return created;
    }

    public void setCreated(final OffsetDateTime created) {
        this.created = created;
    }

    public OffsetDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(final OffsetDateTime updated) {
        this.updated = updated;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(final Policy policy) {
        this.policy = policy;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(final Customer customer) {
        this.customer = customer;
    }

}
