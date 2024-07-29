package com.bestinsurance.api.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "policies")
@EntityListeners(AuditingEntityListener.class)
public class Policy implements DomainObject<UUID> {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    private UUID policyId;

    @Column(nullable = false, length = 16, unique = true)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal price;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime created;

    @LastModifiedDate
    @Column
    private OffsetDateTime updated;

    @OneToMany(mappedBy = "policy")
    private Set<Subscription> policySubscriptions;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "policies_coverages",
            joinColumns = @JoinColumn(name = "policy_id"),
            inverseJoinColumns = @JoinColumn(name = "coverage_id")
    )
    private Set<Coverage> coverages;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Policy policy = (Policy) o;
        return policyId.equals(policy.policyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(policyId);
    }

    public UUID getPolicyId() {
        return policyId;
    }

    public void setPolicyId(final UUID policyId) {
        this.policyId = policyId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
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

    public Set<Subscription> getPolicySubscriptions() {
        return policySubscriptions;
    }

    public void setPolicySubscriptions(final Set<Subscription> policySubscriptions) {
        this.policySubscriptions = policySubscriptions;
    }

    public Set<Coverage> getCoverages() {
        return coverages;
    }

    public void setCoverages(Set<Coverage> coverages) {
        this.coverages = coverages;
    }

    @Override
    public void setId(UUID id) {
        this.setPolicyId(id);
    }
}
