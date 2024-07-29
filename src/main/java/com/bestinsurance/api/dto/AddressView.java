package com.bestinsurance.api.dto;

public class AddressView  {
    private String id;
    private String address;
    private String postalCode;
    private CountryView country;
    private CityView city;
    private StateView state;

    public record CityView(String id, String name) {}
    public record CountryView(String id, String name) {}
    public record StateView(String id, String name) {}
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public CountryView getCountry() {
        return country;
    }

    public void setCountry(CountryView country) {
        this.country = country;
    }

    public CityView getCity() {
        return city;
    }

    public void setCity(CityView city) {
        this.city = city;
    }

    public StateView getState() {
        return state;
    }

    public void setState(StateView state) {
        this.state = state;
    }
}
