package services;

import org.junit.Before;
import org.junit.Test;

import fundamentals.MultiD;

/**
 * This class tests the ElementWiseOperationService by constructing an Addition Service.
 * Only the methods defined in the abstract ElementWiseOperationService are tested here.
 * @author Brannan
 *
 */
public class TestElementWiseOperationService {
	
	MultiD multiD1;
	MultiD multiD2;
	MultiD result;
	AdditionService additionService;
	
	@Before
	public void setUp(){
		additionService = new AdditionService();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDimensionCheck(){
		// Given
		multiD1 = new MultiD(2,2);
		multiD2 = new MultiD(1,2);
		
		// When
		additionService.operate(multiD1, multiD2);
		
		// Then expect exception
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDimensionLengthCheck(){
		// Given
		MultiD multiD1 = new MultiD(2,2);
		MultiD multiD2 = new MultiD(2,2,1);
		
		// When
		additionService.operate(multiD1, multiD2);
		
		// Then expect exception
	}

}
