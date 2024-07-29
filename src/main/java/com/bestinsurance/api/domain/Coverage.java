package com.bestinsurance.api.domain;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "coverages")
public class Coverage implements DomainObject<UUID>{

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    private UUID coverageId;

    @Column(nullable = false, length = 16)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToMany(mappedBy = "coverages")
    private Set<Policy> policies;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coverage coverage = (Coverage) o;
        return coverageId.equals(coverage.coverageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coverageId);
    }

    public UUID getCoverageId() {
        return coverageId;
    }

    public void setCoverageId(final UUID coverageId) {
        this.coverageId = coverageId;
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

    public Set<Policy> getPolicies() {
        return policies;
    }

    public void setPolicies(Set<Policy> policies) {
        this.policies = policies;
    }

    @Override
    public void setId(UUID id) {
        this.setCoverageId(id);
    }
}
