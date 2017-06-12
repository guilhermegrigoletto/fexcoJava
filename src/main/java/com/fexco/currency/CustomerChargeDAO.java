package com.fexco.currency;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.ejb.Stateless;

@Stateless
public class CustomerChargeDAO {
	
	private static final String FILE_DELIMITER = ",";
	private static final String CUSTOMER_CHARGES_FILE = "customer_charges.csv";
	private List<CustomerCharge> loadedCustomerCharges = new ArrayList<CustomerCharge>();
	
	private static final String DELIMITER = ",";
	private static final String MINOR_UNITS_FILE = "currency_minor_units.csv";
	private final static Map<String, Integer> currencyMinorUnits = new HashMap<String, Integer>(); 
	
	static {
        InputStream inputStream = null;
        Scanner scanner = null;
		try {
			inputStream = new FileInputStream(MINOR_UNITS_FILE);
	        scanner = new Scanner(inputStream);
	        while(scanner.hasNext()) {
	        	String lineRead = scanner.next();
	        	String[] currencySplit = lineRead.split(DELIMITER);
	        	currencyMinorUnits.put(currencySplit[0], Integer.parseInt(currencySplit[1]));
	        }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(inputStream != null){
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(scanner != null) {
				scanner.close();
			}
		}
	}
	
	public BigDecimal findChargePercentBy(String customerId, String currency) {
        loadCustomerCharges();
        return findCustomerCharge(customerId, currency);
        	
	}
	
	public static Integer findMinorUnitNumberBy(String currency) {
		return currencyMinorUnits.get(currency);
	}

	private BigDecimal findCustomerCharge(String customerId, String currency) {
		BigDecimal chargePercent = null;
		for(int i = 0 ; i < loadedCustomerCharges.size(); i++) {
        	CustomerCharge transaction = loadedCustomerCharges.get(i);
        	if(transaction.getCurrencyUsed().equalsIgnoreCase(currency)  &&  transaction.getCustomerId().equals(customerId) ) {
        		chargePercent = transaction.getChargePercent();
        		break;
        	}
        }
        
        if(chargePercent == null)
        	throw new RuntimeException("No customer charge found for customer ID: " + customerId + " and currency: " + currency);
		return chargePercent;
	}

	private void loadCustomerCharges() {
		InputStream inputStreamReader = null;
        Scanner scanner = null;
		try {
			inputStreamReader = new FileInputStream(CUSTOMER_CHARGES_FILE);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("customer_charges file was not found");
		}
        scanner = new Scanner(inputStreamReader);
        
        while (scanner.hasNext()) {
        	CustomerCharge transaction = new CustomerCharge();
        	String lineRead = scanner.nextLine();
        	String[] currencySplit = lineRead.split(FILE_DELIMITER);
        	transaction.setCustomerId(currencySplit[0]);
        	transaction.setCurrencyUsed(currencySplit[1]);
        	transaction.setChargePercent(new BigDecimal(currencySplit[2]));
        	loadedCustomerCharges.add(transaction);
        }
        
        if(scanner != null)
        	scanner.close();
	}
	

}
