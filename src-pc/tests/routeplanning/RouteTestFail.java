package tests.routeplanning;

import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import lejos.geom.Point;
import routeplanning.Route;

public class RouteTestFail {
	final static Logger logger = Logger.getLogger(RouteTestFail.class);
	
	/**/
	@Parameters
	public static Collection<Integer[]> getTestParameters() {
		return Arrays.asList(new Integer[][] {});
	}
	
	/*Initialises each parameter for each test*/
	public RouteTestFail() {
	}

	@Test
	public void constructorTest() {
		logger.debug("");
	}
}