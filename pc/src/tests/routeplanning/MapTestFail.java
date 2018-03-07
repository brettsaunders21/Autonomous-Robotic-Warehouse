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

/**
 * @author ladderackroyd
 * @author Lewis Ackroyd
 * */

@RunWith(value = Parameterized.class)
public class MapTestFail {
	private final static Logger logger = Logger.getLogger(MapTestFail.class);
	public int width;
	public int height;
	public int pointx;
	public int pointy;

	
	/*Creates a map and with a single point wall at coordinates*/
	@Parameters
	public static Collection<Integer[]> getTestParameters() {
		return Arrays.asList(new Integer[][] {
				{ 2, 1, 4, 5 }, //  width, height, obstructionX, obstructionY
				{ 1, 1, 2, 1 }, //  width, height, obstructionX, obstructionY
				{ 1, 3, 1, -1 }, //  width, height, obstructionX, obstructionY
				{ 2, 1, 2, 1 },	// width, height, obstructionX, obstructionY
				{ 0, 0, 0, 0 }, // width, height, obstructionX, obstructionY
		});
	}
	
	/*Initialises each parameter for each test*/
	public MapTestFail(int width, int height, int pointx, int pointy) {
		this.width = width;
		this.height = height;
		this.pointx = pointx;
		this.pointy = pointy;
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void constructorTest() {
		logger.debug("w: "+width+" h: "+height+" p: ("+pointx+","+pointy+")");
		Map.createTestMap(width, height, new Point[] { new Point(pointx, pointy) });
	}
}