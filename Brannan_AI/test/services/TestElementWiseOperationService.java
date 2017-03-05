package services;

import org.junit.Before;
import org.junit.Test;

import fundamentals.MultiD;

/**
 * This class tests the ElementWiseOperationService by constructing an Addition Service, but only testing
 * the methods which have an implementation attached to them.
 * @author Brannan
 *
 */
public class TestElementWiseOperationService {
	
	MultiD<Double> multiD1;
	MultiD<Double> multiD2;
	MultiD<Double> result;
	AdditionService additionService;
	
	@Before
	public void setUp(){
		additionService = new AdditionService();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDimensionCheck(){
		// Given
		multiD1 = new MultiD<>(2,2);
		multiD2 = new MultiD<>(1,2);
		
		// When
		additionService.operate(multiD1, multiD2);
		
		// Then expect exception
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDimensionLengthCheck(){
		// Given
		MultiD<Double> multiD1 = new MultiD<>(2,2);
		MultiD<Double> multiD2 = new MultiD<>(2,2,1);
		
		// When
		additionService.operate(multiD1, multiD2);
		
		// Then expect exception
	}

}
