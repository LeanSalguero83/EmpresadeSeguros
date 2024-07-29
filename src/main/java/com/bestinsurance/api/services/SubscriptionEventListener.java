package com.bestinsurance.api.services;

import com.bestinsurance.api.domain.Subscription;
import com.bestinsurance.api.domain.events.SubscriptionUpdated;
import com.bestinsurance.api.dto.SubscriptionLogMsg;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class SubscriptionEventListener {
    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${eventlistener.queue.name}")
    private String queueName;

    @Value("${eventlistener.enabled:true}")
    private Boolean enabled;

    private static final ObjectMapper om = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionEventListener.class);

    public SubscriptionEventListener() {
        om.registerModule(new JavaTimeModule());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = SubscriptionUpdated.class)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void subscriptionEventListener(SubscriptionUpdated event){
        if (enabled) {
            Subscription subscription = event.getSubscription();
            SubscriptionLogMsg log = new SubscriptionLogMsg();
            log.setCustomerId(subscription.getCustomer().getCustomerId());
            log.setPolicyId(subscription.getPolicy().getPolicyId());
            log.setCustomerName(subscription.getCustomer().getName());
            log.setCustomerSurname(subscription.getCustomer().getSurname());
            log.setCustomerEmail(subscription.getCustomer().getEmail());
            log.setCustomerTelephone(subscription.getCustomer().getTelephoneNumber());
            log.setPolicyName(subscription.getPolicy().getName());
            log.setSubscriptionStart(subscription.getStartDate());
            log.setSubscriptionEnd(subscription.getEndDate());
            try {
                String objmsg = om.writeValueAsString(log);
                this.jmsTemplate.convertAndSend(queueName, objmsg);
            } catch (Exception e) {
                logger.error("Error during jms notification", e);
            }
        } else {
            logger.debug("Subscription event listener disabled");
        }
    }
}
