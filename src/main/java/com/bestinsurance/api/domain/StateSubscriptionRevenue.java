package com.bestinsurance.api.domain;

import java.math.BigDecimal;

public record StateSubscriptionRevenue(String stateName, BigDecimal revenue, Long customersCount) {
}
