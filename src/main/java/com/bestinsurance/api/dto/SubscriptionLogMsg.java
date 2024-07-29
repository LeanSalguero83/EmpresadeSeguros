package com.bestinsurance.api.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;


public class SubscriptionLogMsg implements Serializable {
    private static final long serialVersionUID = 42L;
    private UUID policyId;
    private UUID customerId;
    private String customerName;
    private String customerSurname;
    private String customerEmail;
    private String customerTelephone;
    private String policyName;
    private LocalDate subscriptionStart;
    private LocalDate subscriptionEnd;


    @Override
    public String toString() {
        return "SubscriptionLogMsg{" +
                "policyId=" + policyId +
                ", customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", customerSurname='" + customerSurname + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", customerTelephone='" + customerTelephone + '\'' +
                ", policyName='" + policyName + '\'' +
                ", subscriptionStart=" + subscriptionStart +
                ", subscriptionEnd=" + subscriptionEnd +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionLogMsg that = (SubscriptionLogMsg) o;
        return policyId.equals(that.policyId) && customerId.equals(that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(policyId, customerId);
    }

    public UUID getPolicyId() {
        return policyId;
    }

    public void setPolicyId(UUID policyId) {
        this.policyId = policyId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerSurname() {
        return customerSurname;
    }

    public void setCustomerSurname(String customerSurname) {
        this.customerSurname = customerSurname;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerTelephone() {
        return customerTelephone;
    }

    public void setCustomerTelephone(String customerTelephone) {
        this.customerTelephone = customerTelephone;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public LocalDate getSubscriptionStart() {
        return subscriptionStart;
    }

    public void setSubscriptionStart(LocalDate subscriptionStart) {
        this.subscriptionStart = subscriptionStart;
    }

    public LocalDate getSubscriptionEnd() {
        return subscriptionEnd;
    }

    public void setSubscriptionEnd(LocalDate subscriptionEnd) {
        this.subscriptionEnd = subscriptionEnd;
    }

}
