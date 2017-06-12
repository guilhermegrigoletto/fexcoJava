package com.fexco.currency;

import java.math.BigDecimal;

public class CustomerCharge {
	private String customerId;
	private String currencyUsed;
	private BigDecimal chargePercent;
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCurrencyUsed() {
		return currencyUsed;
	}
	public void setCurrencyUsed(String currencyUsed) {
		this.currencyUsed = currencyUsed;
	}
	public BigDecimal getChargePercent() {
		return chargePercent;
	}
	public void setChargePercent(BigDecimal chargePercent) {
		this.chargePercent = chargePercent;
	}
	
}
