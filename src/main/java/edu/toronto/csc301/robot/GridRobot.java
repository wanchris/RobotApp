package edu.toronto.csc301.robot;

import java.util.ArrayList;
import java.util.Iterator;

import edu.toronto.csc301.grid.GridCell;

public class GridRobot implements IGridRobot {
	
	private GridCell location; 
	private ArrayList<StepListener> listeners; 
	
	public GridRobot(GridCell location) throws NullPointerException {
		// TODO Auto-generated constructor stub
		if (location == null) { 
			throw new NullPointerException("Location cannot be null."); 
		}
		this.location = location; 
		this.listeners = new ArrayList<StepListener>(); 
	}

	@Override
	public GridCell getLocation() {
		return this.location; 
	}

	@Override
	public void step(Direction direction) {
		int currx = this.location.x; 
		int curry = this.location.y; 
		
		StepListener curr; 
		Iterator<StepListener> l = listeners.iterator(); 
		while (l.hasNext()) { 
			curr = l.next(); 
			curr.onStepStart(this, direction);
		}
		
		if (direction == Direction.NORTH) { 
			this.location = GridCell.at(currx, curry + 1); 
		}
		else if (direction == Direction.EAST) { 
			this.location = GridCell.at(currx + 1, curry); 
		}
		else if (direction == Direction.SOUTH) { 
			this.location = GridCell.at(currx, curry - 1); 	
		}
		else if (direction == Direction.WEST) { 
			this.location = GridCell.at(currx - 1, curry); 	 
		}
		
		l = listeners.iterator(); 
		
		while (l.hasNext()) { 
			curr = l.next(); 
			curr.onStepEnd(this, direction);
		}		

	}

	@Override
	public void startListening(StepListener listener) {
		listeners.add(listener); 
		//notify warehouse it moves 
	}

	@Override
	public void stopListening(StepListener listener) {
		listeners.remove(listener); 
	}

}
