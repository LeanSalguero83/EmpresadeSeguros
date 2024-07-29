package com.bestinsurance.api.dto.mappers;

import com.bestinsurance.api.domain.Address;
import com.bestinsurance.api.domain.Customer;
import com.bestinsurance.api.dto.AddressView;
import com.bestinsurance.api.dto.CustomerView;

public class CustomerViewMapper implements DTOMapper<Customer, CustomerView> {
    @Override
    public CustomerView map(Customer customer) {
        CustomerView customerViewDTO = new CustomerView();
        customerViewDTO.setId(customer.getCustomerId().toString());
        customerViewDTO.setName(customer.getName());
        customerViewDTO.setSurname(customer.getSurname());
        customerViewDTO.setEmail(customer.getEmail());
        customerViewDTO.setCreated(customer.getCreated());
        customerViewDTO.setUpdated(customer.getUpdated());
        customerViewDTO.setBirthDate(customer.getBirthDate());
        AddressView addressDTO = new AddressView();
        addressDTO.setId(customer.getAddress().getAddressId().toString());
        addressDTO.setAddress(customer.getAddress().getAddress());
        addressDTO.setPostalCode(customer.getAddress().getPostalCode());
        Address address = customer.getAddress();
        addressDTO.setCountry(new AddressView.CountryView(address.getCountry().getCountryId().toString(),
                address.getCountry().getName()));
        addressDTO.setState(new AddressView.StateView(address.getState().getStateId().toString(),
                address.getState().getName()));
        addressDTO.setCity(new AddressView.CityView(address.getCity().getCityId().toString(), address.getCity().getName()));

        customerViewDTO.setAddress(addressDTO);
        return customerViewDTO;
    }
}
