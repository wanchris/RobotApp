package edu.toronto.csc301.warehouse;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import edu.toronto.csc301.grid.GridCell;
import edu.toronto.csc301.grid.IGrid;
import edu.toronto.csc301.robot.GridRobot;
import edu.toronto.csc301.robot.IGridRobot;
import edu.toronto.csc301.robot.IGridRobot.Direction;

public class PathPlanner implements IPathPlanner {

	public PathPlanner() {
		// Breadth first search for shortest path 
	}

	@Override
	public Entry<IGridRobot, Direction> nextStep(IWarehouse warehouse, Map<IGridRobot, GridCell> robot2dest) {
		// TODO Auto-generated method stub
		GridRobot curr = (GridRobot) robot2dest.keySet().iterator().next(); //the robot to move
		
		
		GridCell destination = robot2dest.get(curr); //the init location of robot
		GridCell starting = curr.getLocation(); //the desired destination of robot
		
		if (starting.x == destination.x && starting.y == destination.y) { 
			return null; 
		}
		
		Direction next = BFS(warehouse, starting, destination);
		
		Map.Entry<IGridRobot, Direction> entry =
			    new AbstractMap.SimpleEntry<IGridRobot, Direction>(robot2dest.keySet().iterator().next(), next);
		
		return entry; 
	}
	
	public Direction BFS(IWarehouse warehouse, GridCell start, GridCell end) {
		
		
		/* ArrayList<GridCell> path = new ArrayList<GridCell>(); 
		Map<GridCell, Boolean> visited = new HashMap<GridCell, Boolean>();
		Queue<ArrayList<GridCell>> q = new LinkedList<ArrayList<GridCell>>();
		Map<GridCell, GridCell> prev = new HashMap<GridCell, GridCell>();
		
		ArrayList<GridCell> first = new ArrayList<GridCell>(); 
		first.add(start); 
		q.add(first); 
		
		while (!q.isEmpty()) { 
			path = q.poll(); 
			GridCell previous = path.get(path.size() - 1);
			
			//Calculate all possible cells you can go to 
			
			
		
		}*/
		
		Map<GridCell, Boolean> vis = new HashMap<GridCell, Boolean>();

		Map<GridCell, GridCell> prev = new HashMap<GridCell, GridCell>();
		
		Set<GridCell> robotFilled = new HashSet<GridCell>(); 
		Iterator<IGridRobot> robots = warehouse.getRobots(); 
		while (robots.hasNext()) { 
			IGridRobot robot = robots.next(); 
			robotFilled.add(robot.getLocation()); 
		}		
		
		IGrid<Rack> plan = warehouse.getFloorPlan(); 
		
		LinkedList<GridCell> path = new LinkedList<GridCell>();
	    Queue<GridCell> q = new LinkedList<GridCell>();
	    GridCell current = start;
	    q.add(current);
	    vis.put(current, true);
	    
	    
	    while(!q.isEmpty()){
	        current = q.poll();
	        if (current.equals(end)){
	            break;
	        }else {
	        	
	        	ArrayList<GridCell> possibilities = new ArrayList<GridCell>();
	        	GridCell p1 = GridCell.at(current.x + 1, current.y); 
	        	if (!robotFilled.contains(p1) && plan.hasCell(p1)) {
	        		possibilities.add(p1); 	        		
	        	}
	        	GridCell p2 = GridCell.at(current.x - 1, current.y); 
	        	if (!robotFilled.contains(p2) && plan.hasCell(p2)) {
	        		possibilities.add(p2); 	        		
	        	}
	        	GridCell p3 = GridCell.at(current.x, current.y + 1); 
	        	if (!robotFilled.contains(p3) && plan.hasCell(p3)) {
	        		possibilities.add(p3); 	        		
	        	}
	        	GridCell p4 = GridCell.at(current.x, current.y - 1);	
	        	if (!robotFilled.contains(p4) && plan.hasCell(p4)) {
	        		possibilities.add(p4); 	        		
	        	}
	        	 	        	
	            for(GridCell pos: possibilities){
	            	
	                if(!vis.containsKey(pos)){
	                    q.add(pos);
	                    vis.put(pos, true);
	                    prev.put(pos, current);
	                }
	            }
	        }
	    }
	    if (!current.equals(end)){
	        System.out.println("can't reach destination");
	    }
	    for(GridCell cell = end; cell != null; cell = prev.get(cell)) {
	        path.add(cell);
	    }

	    Iterator<GridCell> p = path.descendingIterator(); 
	    
	    GridCell fir = p.next(); 
	    GridCell sec = p.next(); 
	    
	    if (fir.x == sec.x) { 
	    	//moved up
	    	if (fir.y == sec.y - 1) { 
		    	return Direction.NORTH;
		    }
	    	//moved down
		    else if (fir.y == sec.y + 1) { 
		    	return Direction.SOUTH; 
		    }
	    	
	    }
	    else if (fir.y == sec.y) { 
	    	//moved up
	    	if (fir.x == sec.x - 1) { 
		    	return Direction.EAST;
		    }
	    	//moved down
		    else if (fir.x == sec.x + 1) { 
		    	return Direction.WEST; 
		    }
	    }
				
		return null; 
	}
}
