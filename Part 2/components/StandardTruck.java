package components;

import GUI.modelTruck;
import GUI.sys;

import java.awt.*;
import java.util.Random;

public class StandardTruck extends Truck
{
	private int maxWeight;
	private Branch destination;

	public StandardTruck(){
		super();
		maxWeight=((new Random()).nextInt(2)+2)*100;
		System.out.println(this);
		setColor(Color.GREEN);
	}

	public StandardTruck(String licensePlate,String truckModel,int maxWeight) {
		super(licensePlate,truckModel);
		this.maxWeight=maxWeight;
		System.out.println(this);
		setColor(Color.GREEN);
	}
	
	public Branch getDestination() {
		return destination;
	}
	
	
	public void setDestination(Branch destination) {
		this.destination = destination;
	}
	

	public int getMaxWeight() {
		return maxWeight;
	}

	
	public void setMaxWeight(int maxWeight) {
		this.maxWeight = maxWeight;
	}

	@Override
	public String toString() {
		return "StandardTruck ["+ super.toString() +",maxWeight=" + maxWeight + "]";
	}
	
	//Marina's work:
	public  void unload () {
		synchronized (MainOffice.lock) {
			for (Package p : getPackages()) {
				deliverPackage(p);
			}
			Point temp = endingPoint;
			endingPoint = startingPoint;
			startingPoint = temp;
			getPackages().removeAll(getPackages());
			System.out.println(getName() + " unloaded packages at " + destination.getName());
		}
	}

	@Override
	public void deliverPackage(Package p) {
		synchronized (MainOffice.lock) {
			if (destination == MainOffice.getHub())
				p.addRecords(Status.HUB_STORAGE, destination);
			else
				p.addRecords(Status.DELIVERY, destination);
			destination.addPackage(p);
		}
	}

	//Marina's work:
	public void load (Branch sender, Branch dest, Status status) {
		synchronized (MainOffice.lock) {
			double totalWeight = 0;
			for (int i = 0; i < sender.getPackages().size(); i++) {
				Package p = sender.getPackages().get(i);
				if (p.getStatus() == Status.BRANCH_STORAGE || (p.getStatus() == Status.HUB_STORAGE && p.getDestBranch() == dest)) {
					System.out.println("before checking small package");
					if (p instanceof SmallPackage /*&& totalWeight + 1 <= maxWeight || totalWeight + ((StandardPackage) p).getWeight() <= maxWeight*/) {
						System.out.println("I've got " + getPackages().size() + " packages");
						getPackages().add(p);
						sender.removePackage(p);
						i--;
						p.addRecords(status, this);
					}
				}
			}
			Random num = new Random(); 
	        pnum = num.nextInt(3)+1;
			System.out.println(this.getName() + " loaded packages at " + sender.getName());
			setColor(Color.GREEN);
		}
	}
	

	public void work() {
		synchronized (MainOffice.lock)
		{
			if (!isAvailable()) {
				int packages = getPackages().size();

				setTimeLeft(getTimeLeft() - 1);

//				System.out.println("starting point  = (" + startingPoint.getX() + ",  "+ startingPoint.getY());
//				System.out.println("ending point  = (" + endingPoint.getX() + ",  "+ endingPoint.getY());
//
//
//				System.out.println("\n\n\ntime left = " + getTimeLeft() + ", total time = " + totalTime + "\n\n\n\n\n");

				double ratio = (double)getTimeLeft() / (double)totalTime;
//				System.out.println("ratio = " + ratio);

				if (startingPoint.getX() > endingPoint.getX())
					newX =  (int)(startingPoint.getX() - (startingPoint.getX() - endingPoint.getX()) * (1 - ratio));
				else
					newX =  (int)(startingPoint.getX() + (endingPoint.getX() - startingPoint.getX()) * (1 - ratio));


				if (getDestination() instanceof Hub) {
					if (startingPoint.getY() > endingPoint.getY())
						newY = (int) (startingPoint.getY() - (startingPoint.getY() - endingPoint.getY()) * (1 - ratio));
					else
						newY = (int) (startingPoint.getY() + (startingPoint.getY() - endingPoint.getY()) * (1 - ratio));
				}
				else{
					if (startingPoint.getY() > endingPoint.getY())
						newY = (int)(startingPoint.getY() + (startingPoint.getY() - endingPoint.getY()) * (1 - ratio));
					else
						newY = (int)(startingPoint.getY() - (startingPoint.getY() - endingPoint.getY()) * (1 - ratio));
				}

				this.setTruckPaint(new modelTruck(newX, newY, getColor()));
				//System.out.println(this.toString() + "  zxc - newx = " + newX + ", new y = " + newY);

				if (getTimeLeft() == 0)
				{
					System.out.println("StandardTruck " + getTruckID() + " arrived to " + destination.getName());
					unload();
					if (destination == MainOffice.getHub())
					{
						setAvailable(true);
						destination = null;
						this.setColor(new Color(49, 92, 4));
					}

					else
					{
						load(destination, MainOffice.getHub(), Status.HUB_TRANSPORT);
						//changes HW2:
						int random = ((new Random()).nextInt((6) + 1)*10);
						setTimeLeft(random);
						setTotalTime(random);


						destination = MainOffice.getHub();
						System.out.println(this.getName() + " is on it's way to the HUB, time to arrive: " + getTimeLeft());


						setColor(this.DARK_GREEN);

					}

				}
			}
		}
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
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			work();
		}
	}
}
