package com.fexco.currency;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
public class ChargeCalculatorImpl implements ChargeCalculator {
	
	@EJB
	private CustomerChargeDAO customerChargeDao;
	private RoundingMode roundingMode = RoundingMode.HALF_UP;
	

	public void setRoundingMode(RoundingMode roundingMode) {
		//default rounding is HALF_UP
		this.roundingMode = roundingMode;
	}
	
	public String determineProcessingCharge(String customerId, String currency, BigDecimal charge) {
		Integer minorUnitNumber = CustomerChargeDAO.findMinorUnitNumberBy(currency);
		BigDecimal chargePercent = getChargePercent(customerId, currency);
		BigDecimal processingCharge = charge.multiply(chargePercent).setScale(minorUnitNumber, roundingMode);
		
		return processingCharge.toString();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private BigDecimal getChargePercent(String customerId, String currency) {
		return customerChargeDao.findChargePercentBy(customerId, currency);
	}
	
}
