package com.bestinsurance.api.jpa;

import com.bestinsurance.api.domain.Coverage;
import com.bestinsurance.api.domain.Policy;
import com.bestinsurance.api.repos.CoverageRepository;
import com.bestinsurance.api.repos.PolicyRepository;
import com.bestinsurance.api.repos.SubscriptionRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static com.bestinsurance.api.jpa.PersistenceEntitiesUtil.*;
@Disabled
public abstract class AbstractPolicyInitializedTest {
    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private CoverageRepository coverageRepository;

    protected List<Coverage> coverages = new ArrayList<>();

    @BeforeAll
    public void initDB() {
        this.cleanDB();
        for (int i = 0; i < 10; i++) {
            char namePrefix = (char)('A' + i);
            policyRepository.save(
                    instancePolicy(namePrefix + " Policy", namePrefix + " Policy Description", new BigDecimal(100))
            );
            policyRepository.save(
                    instancePolicy(namePrefix + " Policy Double", namePrefix + " Policy Description", new BigDecimal(100))
            );
            policyRepository.save(
                    instancePolicy(namePrefix + " Policy s", namePrefix + " Policy Description", new BigDecimal(150))
            );
            policyRepository.save(
                    instancePolicy(namePrefix + " Policy z", namePrefix + " Policy Description", new BigDecimal(200))
            );
            policyRepository.save(
                    instancePolicy(namePrefix + " Policy k", namePrefix + " Policy Description", new BigDecimal(300))
            );
        }
        for (int i = 0; i < 5; i++) {
            coverages.add(coverageRepository.save(instanceCoverage("coverage" + i, "description test")));
        }
    }

    @AfterAll
    public void cleanDB() {
        subscriptionRepository.deleteAll();
        policyRepository.deleteAll();
        coverageRepository.deleteAll();
    }



    protected void printall(Iterable<Policy> all) {
        all.forEach(x-> System.out.println("{id: <generatedUUID>, name: '" + x.getName() + "', desc: '" + x.getDescription() + "', price: " + x.getPrice() + "}"));
    }
}
