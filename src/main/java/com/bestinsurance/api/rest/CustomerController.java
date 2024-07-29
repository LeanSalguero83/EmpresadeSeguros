package com.bestinsurance.api.rest;

import com.bestinsurance.api.domain.*;
import com.bestinsurance.api.dto.CustomerCreation;
import com.bestinsurance.api.dto.CustomerUpdate;
import com.bestinsurance.api.dto.CustomerView;
import com.bestinsurance.api.dto.mappers.CustomerViewMapper;
import com.bestinsurance.api.dto.mappers.DTOMapper;
import com.bestinsurance.api.services.CrudService;
import com.bestinsurance.api.services.CustomerService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/**
 * Controller impelmenting the Customers crud API
 */
@RestController
@SecurityRequirement(name = "security_auth")
@RequestMapping("/customers")
public class CustomerController extends AbstractSimpleIdCrudController<CustomerCreation, CustomerUpdate, CustomerView, Customer> {
    public final static String NAME = "name";
    public final static String SURNAME = "surname";
    public final static String EMAIL = "email";
    public final static String AGE = "age";
    public final static String AGE_FROM = "ageFrom";
    public final static String AGE_TO = "ageTo";
    public final static String ORDER_BY = "orderBy";
    public final static String ORDER_DIRECTION = "orderDirection";
    public final static String PAGE_NUMBER = "pageNumber";
    public final static String PAGE_SIZE = "pageSize";
    @Autowired
    private CustomerService customerService;

    @Override
    protected CrudService<Customer, UUID> getService() {
        return this.customerService;
    }

    @Parameter(in = ParameterIn.QUERY, name = NAME, schema = @Schema(type="string"), required = false)
    @Parameter(in = ParameterIn.QUERY, name = SURNAME, schema = @Schema(type="string"), required = false)
    @Parameter(in = ParameterIn.QUERY, name = EMAIL, schema = @Schema(type="string"), required = false)
    @Parameter(in = ParameterIn.QUERY, name = AGE_FROM, schema = @Schema(type="number"), required = false)
    @Parameter(in = ParameterIn.QUERY, name = AGE_TO, schema = @Schema(type="number"), required = false)
    @Parameter(in = ParameterIn.QUERY, name = ORDER_BY, schema = @Schema(type="string" , allowableValues = {NAME, SURNAME, EMAIL, AGE}), required = false)
    @Parameter(in = ParameterIn.QUERY, name = ORDER_DIRECTION, schema = @Schema(type="string", allowableValues = {"ASC", "DESC"}), required = false)
    @Parameter(in = ParameterIn.QUERY, name = PAGE_NUMBER, schema = @Schema(type="number"), required = false)
    @Parameter(in = ParameterIn.QUERY, name = PAGE_SIZE, schema = @Schema(type="number"), required = false)
    @Override
    public List<CustomerView> all(Map<String, String> filters) {
        try {
            Integer ageFrom = filters.get(AGE_FROM) == null ? null : Integer.valueOf(filters.get(AGE_FROM));
            Integer ageTo = filters.get(AGE_TO) == null ? null : Integer.valueOf(filters.get(AGE_TO));
            if (!(ageFrom == null && ageTo == null) && (ageFrom == null || ageTo == null)) {
                throw new IllegalArgumentException("When searching by age, ageFrom and ageTo are mandatory");
            }
            if (ageFrom != null && ageFrom > ageTo) { //check on ageTo doesn't make sense because here the only null case is both dates null, so checking ageFrom is enough
                throw new IllegalArgumentException("Not valid: ageFrom > ageTo");
            }
            String orderByParam = filters.get(ORDER_BY);
            CustomerService.OrderBy orderBy;
            if (orderByParam != null) {
                if (AGE.equalsIgnoreCase(orderByParam)) {
                    orderBy = CustomerService.OrderBy.birthDate;
                } else {
                    orderBy = CustomerService.OrderBy.valueOf(orderByParam.toLowerCase());
                }
            } else {
                orderBy = CustomerService.OrderBy.valueOf(NAME.toLowerCase());
            }

            CustomerService.OrderDirection orderDirection = filters.get(ORDER_DIRECTION) == null ? null :
                    CustomerService.OrderDirection.valueOf(filters.get(ORDER_DIRECTION).toUpperCase());

            Integer pageNumber = filters.get(PAGE_NUMBER) == null ? null : Integer.valueOf(filters.get(PAGE_NUMBER));
            Integer pageSize = filters.get(PAGE_SIZE) == null ? 10 : Integer.valueOf(filters.get(PAGE_SIZE));
            return customerService.findAllWithFilters(filters.get(NAME), filters.get(SURNAME),
                            filters.get(EMAIL), ageFrom, ageTo, orderBy, orderDirection, pageSize, pageNumber).stream()
                    .map(this.getSearchDtoMapper()::map).toList();
        } catch (NumberFormatException nfe) {
            logger.error("Error during search: ", nfe);
            throw new IllegalArgumentException("Check the integer parameters value");
        } catch (Exception e){
            logger.error("Error during search: ", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Secured({"ROLE_FRONT_OFFICE", "ROLE_ADMIN"}) //Non-final values are not allowed :(
    @GetMapping("/policy/{" + ID + "}")
    @Parameter(in = ParameterIn.PATH, name = ID, schema = @Schema(type="string"), required = true)
    public List<CustomerView> searchByPolicy(@PathVariable Map<String, String> id) {
        try {
            return this.customerService.findByPolicy(this.getIdMapper().map(id)).stream()
                    .map(this.getSearchDtoMapper()::map).toList();
        } catch (Exception e){
            logger.error("Error during searchByPolicy: ", e);
            throw new RuntimeException(e.getMessage());
        }
    }
    @Secured({"ROLE_FRONT_OFFICE", "ROLE_ADMIN"})
    @GetMapping("/coverage/{" + ID + "}")
    @Parameter(in = ParameterIn.PATH, name = ID, schema = @Schema(type="string"), required = true)
    public List<CustomerView> searchByCoverage(@PathVariable Map<String, String> id) {
        try {
            return this.customerService.findByCoverage(this.getIdMapper().map(id)).stream()
                    .map(this.getSearchDtoMapper()::map).toList();
        } catch (Exception e){
            logger.error("Error during searchByCoverage: ", e);
            throw new RuntimeException(e.getMessage());
        }
    }
    @Secured({"ROLE_FRONT_OFFICE", "ROLE_ADMIN"})
    @GetMapping("/subscriptions/discountedPrice")
    public List<CustomerView> searchWithDiscount() {
        try {
            return this.customerService.findWithDiscount().stream()
                    .map(this.getSearchDtoMapper()::map).toList();
        } catch (Exception e){
            logger.error("Error during searchWithDiscount: ", e);
            throw new RuntimeException(e.getMessage());
        }
    }
    @Secured({"ROLE_FRONT_OFFICE", "ROLE_ADMIN"})
    @GetMapping("/subscriptions")
    public List<CustomerView> searchWithSubscriptionBetween(
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd") LocalDate endDate) {
        try {
            return this.customerService.findWithSubscriptionBetween(startDate, endDate).stream()
                    .map(this.getSearchDtoMapper()::map).toList();
        } catch (Exception e){
            logger.error("Error during searchWithSubscriptionBetween: ", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected DTOMapper<CustomerCreation, Customer> getCreateDtoMapper() {
        return (customerCreationDTO) -> {
            Customer customer = new Customer();
            customer.setName(customerCreationDTO.getName());
            customer.setSurname(customerCreationDTO.getSurname());
            customer.setEmail(customerCreationDTO.getEmail());
            customer.setBirthDate(customerCreationDTO.getBirthDate());
            if (!StringUtils.isBlank(customerCreationDTO.getAddress())) {
                Address address = new Address();
                address.setAddress(customerCreationDTO.getAddress());
                address.setPostalCode(customerCreationDTO.getPostalCode());
                Country country = new Country();
                country.setCountryId(UUID.fromString(customerCreationDTO.getIdCountry()));
                address.setCountry(country);
                if (!StringUtils.isBlank(customerCreationDTO.getIdState())) {
                    State state = new State();
                    state.setStateId(UUID.fromString(customerCreationDTO.getIdState()));
                    address.setState(state);
                }
                City city = new City();
                city.setCityId(UUID.fromString(customerCreationDTO.getIdCity()));
                address.setCity(city);

                customer.setAddress(address);
            }
            return customer;
        };
    }

    @Override
    protected DTOMapper<CustomerUpdate, Customer> getUpdateDtoMapper() {
        return (customerUpdateDTO) -> {
            Customer customer = new Customer();
            customer.setEmail(customerUpdateDTO.getEmail());
            if (!StringUtils.isBlank(customerUpdateDTO.getAddress())) {
                Address address = new Address();
                address.setAddress(customerUpdateDTO.getAddress());
                address.setPostalCode(customerUpdateDTO.getPostalCode());
                Country country = new Country();
                country.setCountryId(UUID.fromString(customerUpdateDTO.getIdCountry()));
                address.setCountry(country);
                if (!StringUtils.isBlank(customerUpdateDTO.getIdState())) {
                    State state = new State();
                    state.setStateId(UUID.fromString(customerUpdateDTO.getIdState()));
                    address.setState(state);
                }
                City city = new City();
                city.setCityId(UUID.fromString(customerUpdateDTO.getIdCity()));
                address.setCity(city);

                customer.setAddress(address);
            }
            return customer;
        };
    }

    @Override
    protected DTOMapper<Customer, CustomerView> getSearchDtoMapper() {
        return new CustomerViewMapper();
    }

}
