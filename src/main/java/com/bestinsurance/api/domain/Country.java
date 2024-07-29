package com.bestinsurance.api.domain;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "countries")
public class Country implements DomainObject<UUID>{

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    private UUID countryId;

    @Column(nullable = false, length = 64)
    private String name;

    @Column
    private Integer population;

    @OneToMany(mappedBy = "country")
    private Set<State> countryStates;

    @OneToMany(mappedBy = "country")
    private Set<City> countryCities;

    @OneToMany(mappedBy = "country")
    private Set<Address> countryAddresses;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return countryId.equals(country.countryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryId);
    }

    public UUID getCountryId() {
        return countryId;
    }

    public void setCountryId(final UUID countryId) {
        this.countryId = countryId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(final Integer population) {
        this.population = population;
    }

    public Set<State> getCountryStates() {
        return countryStates;
    }

    public void setCountryStates(final Set<State> countryStates) {
        this.countryStates = countryStates;
    }

    public Set<City> getCountryCities() {
        return countryCities;
    }

    public void setCountryCities(final Set<City> countryCities) {
        this.countryCities = countryCities;
    }

    public Set<Address> getCountryAddresses() {
        return countryAddresses;
    }

    public void setCountryAddresses(final Set<Address> countryAddresses) {
        this.countryAddresses = this.countryAddresses;
    }

    @Override
    public void setId(UUID id) {
        this.setCountryId(id);
    }
}
