package com.fexco.currency;

import java.math.BigDecimal;

public interface ChargeCalculator {
	public String determineProcessingCharge(String customerId, String currency, BigDecimal charge);
}

