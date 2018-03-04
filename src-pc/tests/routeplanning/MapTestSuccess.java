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
public class MapTestSuccess {
	final static Logger logger = Logger.getLogger(MapTestSuccess.class);
	public int width;
	public int height;
	public int pointx;
	public int pointy;

	
	/*Creates a map and with a single point wall at coordinates*/
	@Parameters
	public static Collection<Integer[]> getTestParameters() {
		return Arrays.asList(new Integer[][] {
				{ 2, 1, 2, 1 }, //  width, height, obstructionX, obstructionY
				{ 2, 1, 0, 0 }, //  width, height, obstructionX, obstructionY
				{ 4, 3, 1, 0 }, //  width, height, obstructionX, obstructionY
		});
	}
	
	/*Initialises each parameter for each test*/
	public MapTestSuccess(int width, int height, int pointx, int pointy) {
		this.width = width;
		this.height = height;
		this.pointx = pointx;
		this.pointy = pointy;
	}

	@Test
	public void constructorTest() {
		logger.debug("w: "+width+" h: "+height+" p: ("+pointx+","+pointy+")");
		new Map(width, height, new Point[] { new Point(pointx, pointy) });
	}
}