package com.bestinsurance.api.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "customers")
@EntityListeners(AuditingEntityListener.class)
public class Customer implements DomainObject<UUID>{

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    private UUID customerId;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(nullable = false, length = 64)
    private String surname;

    @Column(nullable = false, length = 320)
    private String email;

    @Column(length = 20)
    private String telephoneNumber;
    @Column
    private LocalDate birthDate;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime created;

    @LastModifiedDate
    @Column
    private OffsetDateTime updated;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", insertable = true)
    private Address address;

    @OneToMany(mappedBy = "customer")
    private Set<Subscription> customerSubscriptions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return customerId.equals(customer.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(final UUID customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(final String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }

    public Set<Subscription> getCustomerSubscriptions() {
        return customerSubscriptions;
    }

    public void setCustomerSubscriptions(final Set<Subscription> customerSubscriptions) {
        this.customerSubscriptions = customerSubscriptions;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    @Override
    public void setId(UUID id) {
        this.setCustomerId(id);
    }
}
