package components;

import GUI.modelTruck;

import java.awt.*;

public class Van extends Truck {
	boolean not_done = false;

	public Van() {
		super();
		this.setColor(Color.BLUE);
		System.out.println(this);
	}

	public Van(String licensePlate,String truckModel) {
		super(licensePlate,truckModel);
		this.setColor(Color.BLUE);
		System.out.println(this);
	}

	@Override
	public String toString() {
		return "Van ["+ super.toString() + "]";
	}

	@Override
	public synchronized void deliverPackage(Package p) {
		synchronized (MainOffice.lock) {
			addPackage(p);
			setAvailable(false);
			//changes HW2:
			int time = (p.getDestinationAddress().getStreet() % 10 + 1)*10;
			this.setTimeLeft(time);
			setTotalTime(time);
			startingPoint = new Point(p.getDestLine().getX1(), p.getDestLine().getY1());
			endingPoint = new Point(p.getDestLine().getX2(), p.getDestLine().getY2());
			p.addRecords(Status.DISTRIBUTION, this);
			System.out.println(getName() + " is delivering " + p.getName() + ", time left: " + getTimeLeft());
			not_done = true;
		}
	}

	@Override
	public void work() {
		if (!isAvailable()) {
			setTimeLeft(getTimeLeft() - 1);
			double ratio = (double) getTimeLeft() / (double) totalTime;

			this.setTimeLeft(this.getTimeLeft() - 1);

			Package p = this.getPackages().get(0);

			if (p.getStatus() == Status.COLLECTION) {
				newX = (int) (startingPoint.getX() - (startingPoint.getX() - endingPoint.getX()) * (1 - ratio));
				newY = (int) (startingPoint.getY() + (endingPoint.getY() - startingPoint.getY()) * (1 - ratio));
			} else if (p.getStatus() == Status.DISTRIBUTION) {
				newX = (int) (startingPoint.getX() + (endingPoint.getX() - startingPoint.getX()) * (1 - ratio));
				newY = (int) (startingPoint.getY() + (endingPoint.getY() - startingPoint.getY()) * (1 - ratio));

			}
			this.setTruckPaint(new modelTruck(newX, newY, getColor()));

			if (this.getTimeLeft() == 0) {
				if (p.getStatus() == Status.COLLECTION) {

					System.out.println("collected -> branch storage");
					//initiate sender circle to color red
					p.getSendCircle().setColor(Color.RED);
					p.addRecords(Status.BRANCH_STORAGE, p.getSenderBranch());

					this.setAvailable(true);

					System.out.println(getName() + " has collected " + p.getName() + " and arrived back to " + p.getSenderBranch().getName());
				} else {
					p.addRecords(Status.DELIVERED, null);
					//initiate sender circle to color red
					p.getDestCircle().setColor(Color.RED);
					p.getSendCircle().setColor(Color.pink);
					p.getDestBranch().removePackage(p);
					System.out.println(getName() + " has delivered " + p.getName() + " to the destination");


					if (p instanceof SmallPackage && ((SmallPackage) p).isAcknowledge()) {
						System.out.println("Acknowledge sent for " + p.getName());
					}
					this.getPackages().removeAll(getPackages());
					this.setAvailable(true);
				}
			}
		}
	}

	@Override
	public void run() {
		while(true)
		{
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (getPackages().size() > 0) {
				synchronized (this) {
					while (threadSuspend) {
						try {
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

				for(int i=0; i<getPackages().size(); i++)
				{
					if(getPackages().get(i).getStatus() == Status.BRANCH_STORAGE)
					{

					}
				}
				work();
			}
		}
	}
}
