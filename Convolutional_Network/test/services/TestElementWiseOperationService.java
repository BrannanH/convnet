package services;

import org.junit.Before;
import org.junit.Test;

import fundamentals.MDA;
import fundamentals.MDABuilder;

/**
 * This class tests the ElementWiseOperationService by constructing an Addition Service.
 * Only the methods defined in the abstract ElementWiseOperationService are tested here.
 * @author Brannan
 *
 */
public class TestElementWiseOperationService {
	
	MDA multiD1;
	MDA multiD2;
	MDA result;
	AdditionService additionService;
	
	@Before
	public void setUp(){
		additionService = new AdditionService();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDimensionCheck(){
		// Given
		multiD1 = new MDABuilder().withDimensions(2,2).build();
		multiD2 = new MDABuilder().withDimensions(1,2).build();
		
		// When
		additionService.operate(multiD1, multiD2);
		
		// Then expect exception
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDimensionLengthCheck(){
		// Given
		MDA multiD1 = new MDABuilder().withDimensions(2,2).build();
		MDA multiD2 = new MDABuilder().withDimensions(2,2,1).build();
		
		// When
		additionService.operate(multiD1, multiD2);
		
		// Then expect exception
	}

}
