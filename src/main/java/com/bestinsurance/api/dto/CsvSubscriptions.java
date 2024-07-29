package com.bestinsurance.api.dto;

import com.opencsv.bean.CsvBindByName;

public class CsvSubscriptions {
    @CsvBindByName
    private String name;
    @CsvBindByName
    private String surname;
    @CsvBindByName
    private String email;
    @CsvBindByName
    private String birthDate;
    @CsvBindByName
    private String address;
    @CsvBindByName
    private String postalcode;
    @CsvBindByName
    private String stateName;
    @CsvBindByName
    private String cityName;
    @CsvBindByName
    private String paidPrice;
    @CsvBindByName
    private String policyName;
    @CsvBindByName
    private String startDate;
    @CsvBindByName
    private String endDate;

    public CsvSubscriptions(){}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPaidPrice() {
        return paidPrice;
    }

    public void setPaidPrice(String paidPrice) {
        this.paidPrice = paidPrice;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
