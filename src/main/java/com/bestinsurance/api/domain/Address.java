package com.bestinsurance.api.domain;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;


@Entity
@Table(name = "addresses")
public class Address implements DomainObject<UUID>{

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    private UUID addressId;

    @Column(nullable = false, length = 128)
    private String address;

    @Column(length = 16)
    private String postalCode;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false, insertable = true)
    private Country country;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false, insertable = true)
    private City city;

    @ManyToOne
    @JoinColumn(name = "state_id", insertable = true)
    private State state;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return addressId.equals(address.addressId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressId);
    }

    @Override
    public void setId(UUID id) {
        this.setAddressId(id);
    }

    public UUID getAddressId() {
        return addressId;
    }

    public void setAddressId(final UUID addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(final Country country) {
        this.country = country;
    }

    public City getCity() {
        return city;
    }

    public void setCity(final City city) {
        this.city = city;
    }

    public State getState() {
        return state;
    }

    public void setState(final State state) {
        this.state = state;
    }
}
