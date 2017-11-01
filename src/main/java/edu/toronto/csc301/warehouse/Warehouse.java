package edu.toronto.csc301.warehouse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

import edu.toronto.csc301.grid.GridCell;
import edu.toronto.csc301.grid.IGrid;
import edu.toronto.csc301.robot.GridRobot;
import edu.toronto.csc301.robot.IGridRobot;
import edu.toronto.csc301.robot.IGridRobot.Direction;
import edu.toronto.csc301.robot.IGridRobot.StepListener;

public class Warehouse implements IWarehouse {
	
	private IGrid<Rack> fPlan; 
	private Map<IGridRobot, GridCell> robots; 
	private Map<IGridRobot, Direction> mRobots; 
	private ArrayList<Consumer<IWarehouse>> listeners; 

	public Warehouse(IGrid<Rack> floorplan) throws NullPointerException {
		// TODO Auto-generated constructor stub
		if (floorplan == null) { 
			throw new NullPointerException("Floor plan cannot be null"); 
		}
		this.fPlan = floorplan; 
		this.robots = new HashMap<IGridRobot, GridCell>(); 
		this.mRobots = new HashMap<IGridRobot, Direction>(); 
		
		this.listeners = new ArrayList<Consumer<IWarehouse>>(); 
		
	}

	@Override
	public IGrid<Rack> getFloorPlan() {
		return this.fPlan; 
	}

	@Override
	public IGridRobot addRobot(GridCell initialLocation) throws IllegalArgumentException {
		if (fPlan.hasCell(initialLocation) == false) { 
			throw new IllegalArgumentException("Robot is off the grid."); 
		}
		
		GridRobot robot = new GridRobot(initialLocation);
		if (robots.containsValue(initialLocation)) { 
			throw new IllegalArgumentException("Robot already exists."); 
		}
		this.robots.put((IGridRobot) robot, initialLocation); 
		
		Consumer<IWarehouse> curr; 
		Iterator<Consumer<IWarehouse>> i = this.listeners.iterator(); 

		while (i.hasNext()) { 
			curr = i.next(); 
			curr.accept(this);
		}
		//look at both calls to accept listener in robot 
		//figure out what each does
		//search up what consumer does 
		//keep track of every time robot listener increments
		//need to have listeners, warehouse listen to robots
		IncrementListener r = new IncrementListener();
		robot.startListening(r);
		
		return (IGridRobot) robot; 
	}

	@Override
	public Iterator<IGridRobot> getRobots() {
		return robots.keySet().iterator(); 
	}

	@Override
	public Map<IGridRobot, Direction> getRobotsInMotion() {
		return Collections.unmodifiableMap(this.mRobots); 
	}
	
	public void addRobot(IGridRobot robot, Direction direction) { 
		this.mRobots.put(robot, direction); 
	}
	
	public void delRobot(IGridRobot robot, Direction direction) { 
		this.mRobots.remove(robot, direction); 
	}

	@Override
	public void subscribe(Consumer<IWarehouse> observer) {
		this.listeners.add(observer); 
	}

	@Override
	public void unsubscribe(Consumer<IWarehouse> observer) {
		this.listeners.remove(observer); 

	}
	public class IncrementListener implements StepListener {

		public IncrementListener() {
			
		}

		@Override
		public void onStepStart(IGridRobot robot, Direction direction) {
			Warehouse.this.addRobot(robot, direction); 
			
			Consumer<IWarehouse> curr; 
			Iterator<Consumer<IWarehouse>> i = listeners.iterator(); 
			while (i.hasNext()) { 
				curr = i.next(); 
				curr.accept(Warehouse.this);
			}
		}

		@Override
		public void onStepEnd(IGridRobot robot, Direction direction) {
			Warehouse.this.delRobot(robot, direction); 
			
			Consumer<IWarehouse> curr; 
			Iterator<Consumer<IWarehouse>> i = listeners.iterator(); 
			while (i.hasNext()) { 
				curr = i.next(); 
				curr.accept(Warehouse.this);
			}

		}

	}
}
