package components;

import GUI.modelTruck;
import GUI.sys;

import java.awt.*;
import java.util.Random;

public class NonStandardTruck extends Truck
{
	private int width, length, height;
	private boolean backToHub = false;


	public NonStandardTruck() {
		super();
		Random r=new Random();
		width=(r.nextInt(3)+2)*100;
		length=(r.nextInt(6)+10)*100;
		height=(r.nextInt(2)+3)*100;
		this.setColor(Color.pink);
		System.out.println(this);
	}
	
	
	public NonStandardTruck(String licensePlate,String truckModel, int length, int width, int height) {
		super(licensePlate,truckModel);
		this.width=width;
		this.length=length;
		this.height=height;
		this.setColor(Color.pink);
		System.out.println(this);
	}

	
	public int getWidth() {
		return width;
	}

	
	public void setWidth(int width) {
		this.width = width;
	}

	
	public int getLength() {
		return length;
	}

	
	public void setLength(int length) {
		this.length = length;
	}

	
	public int getHeight() {
		return height;
	}

	
	public void setHeight(int height) {
		this.height = height;
	}
	
	
	@Override
	public void work() {
		synchronized (MainOffice.lock) {
			if (!this.isAvailable()) {


				double ratio = (double)getTimeLeft() / (double)totalTime;

				this.setTimeLeft(this.getTimeLeft() - 1);
				Package p = this.getPackages().get(0);

				if (p.getStatus() == Status.COLLECTION) {
					newX = (int) (startingPoint.getX() - (startingPoint.getX() - endingPoint.getX()) * (1 - ratio));
					newY = (int) (startingPoint.getY() - (startingPoint.getY() - endingPoint.getY()) * (1 - ratio));
					p.getSendCircle().setColor(Color.RED);
				}
				else if (p.getStatus() == Status.DISTRIBUTION)
				{
					newX = (int)startingPoint.getX();
					newY = (int) (startingPoint.getY() + (endingPoint.getY() - startingPoint.getY()) * (1 - ratio));
					p.getSendCircle().setColor(Color.pink);
					p.getDestCircle().setColor(Color.RED);
				}
				else if (p.getStatus() == Status.DELIVERED){
					if (!backToHub){
						newY = (int) (endingPoint.getY() - (endingPoint.getY() - startingPoint.getY()) * (1 - ratio));
						if (getTimeLeft() == 0){

							//if it finished the up line
							//we are setting the time for the truck to go back as a random number from 10 to 15
							//because the HW2 is not giving us specific time for doing that
							int random = (int)(Math.random() * ((15 - 10) + 1)) + 10;
							setTimeLeft(random);
							setTotalTime(random);
							backToHub = true;
							startingPoint = new Point(p.getOriginLine().getX1(), p.getOriginLine().getY1());
							endingPoint = new Point(p.getOriginLine().getX2(), p.getOriginLine().getY2());

						}
					}
					else {
						newX = (int) (startingPoint.getX() + (endingPoint.getX() - startingPoint.getX()) * (1 - ratio));
						newY = (int) (startingPoint.getY() + (endingPoint.getY() - startingPoint.getY()) * (1 - ratio));


						if (getTimeLeft() == 0) {

							setAvailable(true);
							setColor(Color.pink);
							setTruckPaint(new modelTruck(p.getOriginLine().getX2()-5,p.getOriginLine().getY2()-7,getColor()));
							this.getPackages().remove(p);
							return;
						}
					}
				}

				this.setTruckPaint(new modelTruck(newX, newY, getColor()));

				if (this.getTimeLeft() == 0)
				{
					if (p.getStatus() == Status.COLLECTION)
					{
						System.out.println(getName() + " has collected " + p.getName());
						deliverPackage(p);
						this.setColor(Color.RED);
						startingPoint = new Point(p.getDestLine().getX1()-5,p.getDestLine().getY1());
						endingPoint = new Point(p.getDestLine().getX2()-5,p.getDestLine().getY2());
					}
					else
					{
						p.addRecords(Status.DELIVERED, null);
						setTimeLeft(totalTime);
					}
				}
			}
		}
	}

	@Override
	public void deliverPackage (Package p)
	{
		synchronized (MainOffice.lock) {
			//change HW2:
			int time = (Math.abs(p.getDestinationAddress().getStreet() - p.getSenderAddress().getStreet()) % 10 + 1);

			this.setTimeLeft(time*10);
			this.setTotalTime(time*10);
			p.addRecords(Status.DISTRIBUTION, this);
			System.out.println(getName() + " is delivering " + p.getName() + ", time left: " + this.getTimeLeft());
			this.setColor(Color.pink);
		}
	}
	
	
	@Override
	public String toString() { return "NonStandardTruck ["+ super.toString() + ", length=" + length +  ", width=" + width + ", height=" + height + "]"; }

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

