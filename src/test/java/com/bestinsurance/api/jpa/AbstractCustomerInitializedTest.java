package com.bestinsurance.api.jpa;

import com.bestinsurance.api.domain.City;
import com.bestinsurance.api.domain.Customer;
import com.bestinsurance.api.repos.CityRepository;
import com.bestinsurance.api.repos.CustomerRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.UUID;

import static com.bestinsurance.api.jpa.PersistenceEntitiesUtil.*;
@Disabled
public abstract class AbstractCustomerInitializedTest {
    /*
     * The ids in this array matches with the ids in the src/main/resources/db/changelog/cities_data.csv directory
     * that are automatically loaded in the db by liquibase
     */
    public static String[] cityIds = {"45576d7c-8d84-4422-9440-19ef80fa16f3",
            "91f360d5-811b-417c-a202-f5ba4b34b895",
            "144b05b6-ebf6-43a8-836d-0998c2c20a3c",
            "74716a04-d538-4441-84bf-7c41470778ca",
            "eb5e9505-8580-4857-9195-6bee0324ac0f"};

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CityRepository cityRepository;

    /**
     * Initializes the database with:
     * 50 total customers
     * The customers are grouped by 5, with name, surname and email starting with incremental letter A, B, C, etc.
     * For each letter there are customers living in different cities (cityIds[]) and different ages (ages[])
     * At every age correspond the same city, because the arrays are accessed with the same index j
     */
    @BeforeAll
    public void initializeDB() {
        this.cleanDB();
        LocalDate[] ages = {LocalDate.now().minusYears(20),
                LocalDate.now().minusYears(40),
                LocalDate.now().minusYears(50),
                LocalDate.now().minusYears(60),
                LocalDate.now().minusYears(70)};
        for (int i = 0; i < 10; i++) {
            char namePrefix = (char)('A' + i);
            for(int j = 0; j< cityIds.length; j++) { //CREATE 5 customers for each prefix
                save(instanceCustomer(namePrefix + "customerName", namePrefix + "customerSurname", namePrefix + "N" + j + "@customer.com",
                        ages[j], instanceAddress("street test " + 1, "12345" + i, findCity(cityIds[j]))));
            }
        }
    }

    @AfterAll
    public void cleanDB() {
        customerRepository.deleteAll();
    }

    private Customer save(Customer c) {
        return customerRepository.save(c);
    }



    private City findCity(String cityId) {
        return cityRepository.findById(UUID.fromString(cityId)).get();
    }
}
