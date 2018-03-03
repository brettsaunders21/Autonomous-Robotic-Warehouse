package tests.routeplanning;

import org.apache.log4j.Logger;
import org.junit.Test;
import lejos.geom.Point;
import routeplanning.Map;
import java.util.Arrays;
import java.util.Collection;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class AStarTestSuccess {
	final static Logger logger = Logger.getLogger(AStarTestSuccess.class);
	
	/**/
	@Parameters
	public static Collection<Integer[]> getTestParameters() {
		return Arrays.asList(new Integer[][] {});
	}
	
	/*Initialises each parameter for each test*/
	public AStarTestSuccess() {
	}

	@Test
	public void constructorTest() {
		logger.debug("");
	}
}