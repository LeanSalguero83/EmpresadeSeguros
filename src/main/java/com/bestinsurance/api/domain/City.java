package com.bestinsurance.api.domain;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "cities")
public class City implements DomainObject<UUID>{

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    private UUID cityId;

    @Column(nullable = false, length = 64)
    private String name;

    @Column
    private Integer population;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "state_id")
    private State state;

    @OneToMany(mappedBy = "city")
    private Set<Address> cityAddresses;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return cityId.equals(city.cityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityId);
    }

    public UUID getCityId() {
        return cityId;
    }

    public void setCityId(final UUID cityId) {
        this.cityId = cityId;
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

    public State getState() {
        return state;
    }

    public void setState(final State state) {
        this.state = state;
    }

    public Set<Address> getCityAddresses() {
        return cityAddresses;
    }

    public void setCityAddresses(final Set<Address> cityAddresses) {
        this.cityAddresses = cityAddresses;
    }


    @Override
    public void setId(UUID id) {
        this.setCityId(id);
    }
}
