package com.bestinsurance.api.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class PolicyView {
    private String id;

    private String name;

    private String description;

    private BigDecimal price;

    private OffsetDateTime created;

    private OffsetDateTime updated;

    private List<CoverageView> policiesCoverages;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public List<CoverageView> getPoliciesCoverages() {
        return policiesCoverages;
    }

    public void setPoliciesCoverages(List<CoverageView> policiesCoverages) {
        this.policiesCoverages = policiesCoverages;
    }
}