package com.bestinsurance.api.domain;


import jakarta.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "states")
public class State implements DomainObject<UUID>{

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    private UUID stateId;

    @Column(nullable = false, length = 64)
    private String name;

    @Column
    private Integer population;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @OneToMany(mappedBy = "state")
    private Set<City> stateCities;

    @OneToMany(mappedBy = "state")
    private Set<Address> stateAddresses;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return stateId.equals(state.stateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stateId);
    }

    public UUID getStateId() {
        return stateId;
    }

    public void setStateId(final UUID stateId) {
        this.stateId = stateId;
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

    public Country getCountry() {
        return country;
    }

    public void setCountry(final Country country) {
        this.country = country;
    }

    public Set<City> getStateCities() {
        return stateCities;
    }

    public void setStateCities(final Set<City> stateCities) {
        this.stateCities = stateCities;
    }

    public Set<Address> getStateAddresses() {
        return stateAddresses;
    }

    public void setStateAddresses(final Set<Address> stateAddresses) {
        this.stateAddresses = stateAddresses;
    }

    @Override
    public void setId(UUID id) {
        this.setStateId(id);
    }
}
