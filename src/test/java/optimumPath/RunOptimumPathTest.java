package optimumPath;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import optimumPath.algorithms.AStar;
import optimumPath.common.Raster;
import optimumPath.object.Map;

public class RunOptimumPathTest {

	@Test
	public void test() {
		
		AStar start = new AStar(new Map());
		assertTrue(start.getSizeX() == 10);;
	}
	
	@Test
	public void testAStar() {
		Map testMap = new Map(10, 10, 10, 1.0);
		testMap.setRaster(0, 1, 2, Raster.OBSTACLE);
//		testMap.setRaster(2, 0, 0, Raster.OBSTACLE);
//		testMap.setRaster(0, 2, 0, Raster.OBSTACLE);
//		testMap.setRaster(2, 2, 0, Raster.OBSTACLE);
//		testMap.setRaster(0, 0, 2, Raster.OBSTACLE);
//		testMap.setRaster(2, 0, 2, Raster.OBSTACLE);
//		testMap.setRaster(0, 2, 2, Raster.OBSTACLE);
//		testMap.setRaster(2, 2, 2, Raster.OBSTACLE);
		
//		AStar start = new AStar(testMap);
//		Method method;
//		try {
//			method = start.getClass().getDeclaredMethod("checkPossibleTransition", Node.class, Node.class);
//			method.setAccessible(true);
//			
//			Node actualNode = new Node(1,1,1);
//			ArrayList<Node> neighboursList = new ArrayList<Node>();
//			for(int z = -1+actualNode.getZ(); z <= 1+actualNode.getZ(); z++)
//				for(int y = -1+actualNode.getY(); y <= 1+actualNode.getY(); y++)
//					for(int x = -1+actualNode.getX(); x <= 1+actualNode.getX(); x++)
//						if(!(z == actualNode.getZ() && y == actualNode.getY() && x == actualNode.getX()))
//							neighboursList.add(new Node(z, y, x));
//			
//
//			
//			//Node neighbour = new Node(0,2,2);
//			for(int i = 0; i < neighboursList.size(); i++) {
//				Node neighbour = neighboursList.get(i);
//				Object r = method.invoke(start, actualNode, neighbour);
//				String x = ((Integer)neighbour.getX()).toString();
//				String y = ((Integer)neighbour.getY()).toString();
//				String z = ((Integer)neighbour.getZ()).toString();
//				System.out.println("("+x+" "+y+" "+z+ ")" + " resut: " + r);
//				//assertTrue(((Boolean) r));
//			}
			
			//
			
			//
//		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//			e.printStackTrace();
//			fail(e.getMessage());
//		}
		
	}

}
