package com.bestinsurance.api.rest;

import com.bestinsurance.api.domain.Customer;
import com.bestinsurance.api.domain.Policy;
import com.bestinsurance.api.domain.Subscription;
import com.bestinsurance.api.domain.SubscriptionId;
import com.bestinsurance.api.dto.*;
import com.bestinsurance.api.dto.mappers.CustomerViewMapper;
import com.bestinsurance.api.dto.mappers.DTOMapper;
import com.bestinsurance.api.dto.mappers.PolicyViewMapper;
import com.bestinsurance.api.services.CrudService;
import com.bestinsurance.api.services.CsvService;
import com.bestinsurance.api.services.SubcriptionService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Controller impelmenting the Subscription crud API
 * This class extends the AbstractCrudController because it has a composite id
 */
@RestController
@SecurityRequirement(name = "security_auth")
@RequestMapping("/subscriptions")
public class SubscriptionController extends AbstractCrudController<SubscriptionCreation, SubscriptionUpdate,
        SubscriptionView, Subscription, SubscriptionId> {

    private static final String ID_CUSTOMER = "idCustomer";
    private static final String ID_POLICY = "idPolicy";

    @Autowired
    private SubcriptionService subscriptionService;

    @Autowired
    private CsvService csvService;

    @GetMapping("/{"+ ID_CUSTOMER +"}/{" + ID_POLICY + "}")
    @Parameter(in = ParameterIn.PATH, name = ID_CUSTOMER, schema = @Schema(type="string"), required = true)
    @Parameter(in = ParameterIn.PATH, name = ID_POLICY, schema = @Schema(type="string"), required = true)
    @Override
    public SubscriptionView searchById(Map<String, String> idMap) {
        return super.searchById(idMap);
    }

    @PutMapping("/{"+ ID_CUSTOMER +"}/{" + ID_POLICY + "}")
    @Parameter(in = ParameterIn.PATH, name = ID_CUSTOMER, schema = @Schema(type="string"), required = true)
    @Parameter(in = ParameterIn.PATH, name = ID_POLICY, schema = @Schema(type="string"), required = true)
    @Override
    public SubscriptionView update(Map<String, String> id, SubscriptionUpdate updateDTO) {
        return super.update(id, updateDTO);
    }

    @DeleteMapping("/{"+ ID_CUSTOMER +"}/{" + ID_POLICY + "}")
    @Parameter(in = ParameterIn.PATH, name = ID_CUSTOMER, schema = @Schema(type="string"), required = true)
    @Parameter(in = ParameterIn.PATH, name = ID_POLICY, schema = @Schema(type="string"), required = true)
    @Override
    public void delete(Map<String, String> id) {
        super.delete(id);
    }

    @Override
    protected CrudService<Subscription, SubscriptionId> getService() {
        return this.subscriptionService;
    }

    @GetMapping("revenues")
    public List<SubscriptionRevenueView> getRevenuesByState() {
        try {
            return this.subscriptionService.computeRevenues().stream()
                    .map(x -> new SubscriptionRevenueView(x.stateName(),x.revenue(), x.customersCount())).toList();
        } catch (Exception e){
            logger.error("Error during getRevenuesByState: ", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String uploadFile(@RequestParam(value = "file") MultipartFile file) throws Exception {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<CsvSubscriptions> csvToBean = new CsvToBeanBuilder<CsvSubscriptions>(reader)
                    .withType(CsvSubscriptions.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(';')
                    .build();
            List<CsvSubscriptions> csvCustomers = csvToBean.parse();
            this.csvService.save(csvCustomers);
        } catch (Exception ex) {
            logger.error("Error during uploadFile: ", ex);
            throw ex;
        }
        return "File Uploaded Correctly";
    }

    @Override
    protected DTOMapper<SubscriptionCreation, Subscription> getCreateDtoMapper() {
        return (dto) -> {
            SubscriptionId id = new SubscriptionId();
            id.setCustomerId(UUID.fromString(dto.getCustomerId()));
            id.setPolicyId(UUID.fromString(dto.getPolicyId()));

            Subscription subscription = new Subscription();
            subscription.setId(id);
            subscription.setStartDate(dto.getStartDate());
            subscription.setEndDate(dto.getEndDate());
            subscription.setPaidPrice(dto.getPaidPrice());
            Customer customer = new Customer();
            customer.setCustomerId(id.getCustomerId());
            Policy policy = new Policy();
            policy.setPolicyId(id.getPolicyId());
            subscription.setCustomer(customer);
            subscription.setPolicy(policy);
            return subscription;

        };
    }

    @Override
    protected DTOMapper<SubscriptionUpdate, Subscription> getUpdateDtoMapper() {
        return (dto) -> {

            Subscription subscription = new Subscription();
            subscription.setStartDate(dto.getStartDate());
            subscription.setEndDate(dto.getEndDate());
            subscription.setPaidPrice(dto.getPaidPrice());

            return subscription;

        };
    }

    @Override
    protected DTOMapper<Subscription, SubscriptionView> getSearchDtoMapper() {
        return (entity) -> {
            SubscriptionView dto = new SubscriptionView();
            dto.setCreated(entity.getCreated());
            dto.setUpdated(entity.getUpdated());
            dto.setEndDate(entity.getEndDate());
            dto.setStartDate(entity.getStartDate());
            dto.setPolicy(new PolicyViewMapper().map(entity.getPolicy()));
            dto.setCustomer(new CustomerViewMapper().map(entity.getCustomer()));
            dto.setPaidPrice(entity.getPaidPrice());
            return dto;
        };
    }

    @Override
    protected DTOMapper<Map<String, String>, SubscriptionId> getIdMapper() {
        return (idMap) -> {
            SubscriptionId id = new SubscriptionId();
            id.setCustomerId(UUID.fromString(idMap.get(ID_CUSTOMER)));
            id.setPolicyId(UUID.fromString(idMap.get(ID_POLICY)));
            return id;
        };
    }
}
