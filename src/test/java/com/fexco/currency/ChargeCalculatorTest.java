package com.fexco.currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.math.BigDecimal;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ChargeCalculatorTest {
	
	private static EJBContainer ejbContainer;
	
	private ChargeCalculator calculator;
	
    @BeforeClass
    public static void createContainer() {
        ejbContainer = EJBContainer.createEJBContainer();
    }

    @Before
    public void beanLookup() throws NamingException {
        Object object = ejbContainer.getContext().lookup("java:global/currency/ChargeCalculatorImpl");
        
        calculator = (ChargeCalculator) object;
    }

    @AfterClass
    public static void closeContainer() {
        if (ejbContainer != null) {
            ejbContainer.close();
        }
    }

    @Test
    public void testCurrencyFilesArePresent() {
		assertTrue(new File("customer_charges.csv").exists());
		assertTrue(new File("currency_minor_units.csv").exists());
    }
    
	@Test
	public void testdetermineProcessingCharge() throws NamingException {

		assertNotNull(calculator);


		assertEquals("2.74", calculator.determineProcessingCharge("100000000","USD", new BigDecimal("100.00")));
		assertEquals("22.151", calculator.determineProcessingCharge("100000000","BHD", new BigDecimal("1234.567")));
		assertEquals("14980", calculator.determineProcessingCharge("788710417","JPY", new BigDecimal("1290118")));
		assertEquals("3.02", calculator.determineProcessingCharge("68458093","USD", new BigDecimal("200.00")));
	}
	
	@Test
	public void testRoundingUp() {
		assertEquals("1.73", calculator.determineProcessingCharge("863112134","BOB", new BigDecimal("100.00")));
	}
	
	@Test(expected=RuntimeException.class)
	public void testNonExistingClientShouldThrowException() {
		calculator.determineProcessingCharge("1234", "USD", new BigDecimal("2000.00"));
	}
	
	@Test(expected=RuntimeException.class)
	public void testNonExistingCurrencyShouldThrowException() {
		calculator.determineProcessingCharge("1234", "AAA", new BigDecimal("2000.00"));
	}
}
