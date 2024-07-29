package com.bestinsurance.api.dto;

import java.math.BigDecimal;

public record SubscriptionRevenueView(String stateName, BigDecimal revenue, Long customersCount) {
}
