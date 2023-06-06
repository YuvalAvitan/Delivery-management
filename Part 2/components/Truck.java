package components;

import GUI.modelTruck;
import GUI.sys;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;


public abstract class Truck implements Node , Runnable, sys {
	private static int countID=2000;
	final private int truckID;
	private String licensePlate;
	private String truckModel;
	private boolean available=true;
	private int timeLeft=0;
	private ArrayList<Package> packages=new ArrayList<Package>();
	private Color color;
	public modelTruck truckPaint;
	public Color DARK_GREEN = new Color(0,102,0);
	public boolean threadSuspend = false;
	public boolean stop = false;
	public Point startingPoint, endingPoint;
	public int totalTime;
	public int pnum = 0;
	public int newY = 0, newX = 0;


	public Point getStartingPoint() {
		return startingPoint;
	}

	public void setStartingPoint(Point startingPoint) {
		this.startingPoint = startingPoint;
	}

	public Point getEndingPoint() {
		return endingPoint;
	}

	public void setEndingPoint(Point endingPoint) {
		this.endingPoint = endingPoint;
	}

	public int getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}

	//default random constructor
	public Truck() {
		truckID=countID++;
		Random r= new Random();
		licensePlate=(r.nextInt(900)+100)+"-"+(r.nextInt(90)+10)+"-"+(r.nextInt(900)+100);
		truckModel="M"+r.nextInt(5);
		System.out.print("Creating ");
	}

	
	public Truck(String licensePlate,String truckModel) {
		truckID=countID++;
		this.licensePlate=licensePlate;
		this.truckModel=truckModel;
		System.out.print("Creating ");
	}
	
	
	public ArrayList<Package> getPackages() {
		return packages;
	}


	public int getTimeLeft() {
		return timeLeft;
	}

	
	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}


	@Override
	public String toString() {
		return "truckID=" + truckID + ", licensePlate=" + licensePlate + ", truckModel=" + truckModel + ", available= " + available ;
	}


	@Override
	public synchronized void collectPackage(Package p) {
		//synchronized (MainOffice.lock) {
			setAvailable(false);
			//changes HW2:
			int time = (p.getSenderAddress().getStreet() % 10 + 1);
			this.setTimeLeft(time*10);
			this.setTotalTime(time*10);
			this.packages.add(p);
			p.setStatus(Status.COLLECTION);
			p.addTracking(new Tracking(MainOffice.getClock(), this, p.getStatus()));

			System.out.println(getName() + " is collecting package " + p.getPackageID() + ", time to arrive: " + getTimeLeft());
		}
	//}


	public boolean isAvailable() {
		synchronized (MainOffice.lock) {
			return available;
		}
	}
	

	public int getTruckID() {
		return truckID;
	}

	
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	
	public void addPackage(Package p) {
		this.packages.add(p);
	}
	
	
	public String getName() {
		return this.getClass().getSimpleName()+" "+ getTruckID();
	}

	/**
	 *
	 * @return truck for the get function
	 */
	public modelTruck getTruckPaint() {
		return truckPaint;
	}

	public void setTruckPaint(modelTruck truck) {
		this.truckPaint = truck;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}


	@Override
	public void setSuspend() {
		threadSuspend = true;
	}

	@Override
	public synchronized void setResume() {
		threadSuspend = false;
		notify();
	}

	@Override
	public void setStop() {
		stop = true;
	}

}
