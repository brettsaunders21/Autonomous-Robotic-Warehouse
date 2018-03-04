package tests.routeplanning;

import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import lejos.geom.Point;
import routeplanning.Route;;

public class RouteTestSuccess {
	final static Logger logger = Logger.getLogger(RouteTestSuccess.class);
	
	/**/
	@Parameters
	public static Collection<Integer[]> getTestParameters() {
		return Arrays.asList(new Integer[][] {});
	}
	public RouteTestSuccess() {
	}

	@Test
	public void constructorTest() {
		logger.debug("");
	}
}