package com.bestinsurance.api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public class PolicyCreation {
    @NotBlank
    @Size(max=16)
    private String name;
    @NotBlank
    private String description;
    @NotNull
    @Digits(integer = 5, fraction = 2)
    private BigDecimal price;
    @NotNull
    @NotEmpty
    private List<String> coveragesIds;

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

    public List<String> getCoveragesIds() {
        return coveragesIds;
    }

    public void setCoveragesIds(List<String> coveragesIds) {
        this.coveragesIds = coveragesIds;
    }
}
