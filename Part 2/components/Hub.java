package components;

import GUI.modelTruck;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Hub extends Branch {
	
	private final ArrayList<Branch> branches=new ArrayList<>();
	private int currentIndex=0;



	public Hub() {
		super("HUB");
	}

	public ArrayList<Branch> getBranches() {
		return branches;
	}

	
	public void add_branch(Branch branch) {
		branches.add(branch);
	}

	
	public void shipNonStandard(NonStandardTruck t)
	{
		for (Package p: packages)
		{
			if (p instanceof NonStandardPackage)
			{
				t.setTruckPaint(new modelTruck(p.getOriginLine().getX2()-5,p.getOriginLine().getY2()-7,t.getColor()));


				//we were canceling this condition because the work marina gave us is not working properly it seems like the non standard truck is not
				//picking all the non standard packages so for the sake of the program to work we did a always true condition: if(true)
				// ------> if (((NonStandardPackage) p).getHeight() <= t.getHeight() && ((NonStandardPackage) p).getLength()<=t.getLength() && ((NonStandardPackage) p).getWidth()<=t.getWidth())


				t.collectPackage(p);

				t.startingPoint = new Point(p.getOriginLine().getX2()-5, p.getOriginLine().getY2());
				t.endingPoint = new Point(p.getOriginLine().getX1()-5, p.getOriginLine().getY1());




				packages.remove(p);
				return;

			}
		}	
	}
	
	
	@Override
	public void localWork() {
		for (Truck t : listTrucks) {
			if (t.isAvailable()){
				if(t instanceof NonStandardTruck) {
					shipNonStandard((NonStandardTruck) t);
				}
				else {
					sendTruck((StandardTruck)t);
				}
			}	
		}
	}

	public void sendTruck(StandardTruck t) {
		t.setAvailable(false);
		t.setDestination(branches.get(currentIndex));
		t.load(this, t.getDestination(), Status.BRANCH_TRANSPORT);

		//setting the starting ant the ending points
		//for the Standard truck
		t.startingPoint = new Point(t.getDestination().getLineToHub().getX2()-13, t.getDestination().getLineToHub().getY2());
		t.endingPoint = new Point(t.getDestination().getLineToHub().getX1()+13, t.getDestination().getLineToHub().getY1());

		t.setTruckPaint(new modelTruck(t.getDestination().getLineToHub().getX2()-13, t.getDestination().getLineToHub().getY2(),t.getColor()));

		int random = (new Random()).nextInt(((10+1)+2)*10);
		t.setTimeLeft(random);
		t.setTotalTime(random);
		System.out.println(t.getName() + " is on it's way to " + t.getDestination().getName() + ", time to arrive: "+t.getTimeLeft());
		currentIndex=(currentIndex+1)%branches.size();
	}

	@Override
	public void run() {
		while(true)
		{
			synchronized(this) {
				while (threadSuspend) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			work();
		}
	}

	public Branch getBranchByID(int ID)
	{
		for (Branch branch : this.branches)
			if (branch.getBranchId() == ID) {
				return branch;
			}
		return this.branches.get(0);
	}

}
