package components;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Branch implements Node, Runnable, Cloneable {
	private static int counter=0;
	private int branchId;
	private String branchName;
	protected ArrayList <Package> unsafeListPackages = new ArrayList<Package>();
	protected List<Package> listPackages = unsafeListPackages; //Collections.synchronizedList(unsafeListPackages);
	protected ArrayList <Truck> listTrucks = new ArrayList<Truck>();
	private Point hubPoint;
	private Point branchPoint;
	protected boolean threadSuspend = false;
	private List<Van> listener = new ArrayList<Van>();


	public Branch(Branch branch)
	{
		this.branchId = ++counter;
		this.branchName = branch.getName();
		this.unsafeListPackages = branch.getUnsafeListPackages();
		this.listPackages = branch.getPackages();
		this.listTrucks = branch.getListTrucks();
		this.hubPoint = branch.getHubPoint();
		this.branchPoint = branch.getBranchPoint();
		this.threadSuspend = branch.isThreadSuspend();
	}




	public Branch() {
		this("Branch "+counter);
	}
	
	public Branch(String branchName) {
		this.branchId=counter++;
		this.branchName=branchName;
		System.out.println("\nCreating "+ this);
	}
	
	public Branch(int branchId, String branchName, Package[] plist, Truck[] tlist, boolean threadSuspend) {
		this.branchId=counter++;
		this.branchName=branchName;
		addPackages(plist);
		addTrucks(tlist);
	}



	public synchronized List <Package> getPackages(){
		return this.listPackages;
	}
	
	public void printBranch() {
		System.out.println("\nBranch name: "+branchName);
		System.out.println("Packages list:");
		for (Package pack: listPackages)
			System.out.println(pack);
		System.out.println("Trucks list:");
		for (Truck trk: listTrucks)
			System.out.println(trk);
	}
	
	
	public synchronized void addPackage(Package pack) {
		listPackages.add(pack);
	}
	
	
	public ArrayList <Truck> getTrucks(){
		return this.listTrucks;
	}
	
	public void addTruck(Truck trk) {
		listTrucks.add(trk);
	}
	
	
	public Point getHubPoint() {
		return hubPoint;
	}
	
	public Point getBranchPoint() {
		return branchPoint;
	}
	
	public synchronized void addPackages(Package[] plist) {
		for (Package pack: plist)
			listPackages.add(pack);
	}
	
	
	public void addTrucks(Truck[] tlist) {
		for (Truck trk: tlist) {
			listTrucks.add(trk);
		}
	}

	
	public int getBranchId() {
		return branchId;
	}
	
	
	public String getName() {
		return branchName;
	}

	
	@Override
	public String toString() {
		return "Branch " + branchId + ", branch name:" + branchName + ", packages: " + listPackages.size()
				+ ", trucks: " + listTrucks.size();
	}

	
	@Override
	public synchronized void  collectPackage(Package p) {

		
		for (Truck v : listTrucks) {
			if (v.isAvailable()) {
				synchronized(v) {
					v.notify();
				}
				v.collectPackage(p);
				return;
			}
		}
	}

	@Override
	public synchronized void deliverPackage(Package p) {
		for (Truck v : listTrucks) {
			if (v.isAvailable()) {
				synchronized(v) {
					v.notify();
				}
				v.deliverPackage(p);
				return;
			}
		}	
	}

	@Override
	public void work() {	
		/*for (Package p: listPackages) {
			if (p.getStatus()==Status.CREATION) {
				collectPackage(p);
			}
			if (p.getStatus()==Status.DELIVERY) {
				deliverPackage(p);
			}
		}*/	
	}

	
	private boolean arePackagesInBranch() {
		for(Package p: listPackages) {
			if (p.getStatus() == Status.BRANCH_STORAGE)
				return true;
		}
		return false;
	}
	
	public void paintComponent(Graphics g, int y, int y2) {
		if (arePackagesInBranch())
			g.setColor(new Color(0,0,153));
		else
			g.setColor(new Color(51,204,255));
   		g.fillRect(20, y, 40, 30);
   		
   		g.setColor(new Color(0,102,0));
   		g.drawLine(60, y+15, 1120, y2);
   		branchPoint = new Point(60,y+15);
   		hubPoint = new Point(1120,y2);
	}

	@Override
	public void run() {
		while(true) {
		    synchronized(this) {
                while (threadSuspend)
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    }
			synchronized(this) {
				for (Package p: listPackages) {
						if (p.getStatus()==Status.CREATION) {
							collectPackage(p);
						}
						if (p.getStatus()==Status.DELIVERY) {
							deliverPackage(p);
						}
				}
			}
		}
	}
	
	public void addListener(Van van) {
		listener.add(van);
	}
	
	
	public synchronized void setSuspend() {
	   	threadSuspend = true;
	}

	public synchronized void setResume() {
	   	threadSuspend = false;
	   	notify();
	}


	public Branch clone(){
		return new Branch(this);
	}


	public static int getCounter() {
		return counter;
	}

	public static void setCounter(int counter) {
		Branch.counter = counter;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public ArrayList<Package> getUnsafeListPackages() {
		return unsafeListPackages;
	}

	public void setUnsafeListPackages(ArrayList<Package> unsafeListPackages) {
		this.unsafeListPackages = unsafeListPackages;
	}

	public List<Package> getListPackages() {
		return listPackages;
	}

	public void setListPackages(List<Package> listPackages) {
		this.listPackages = listPackages;
	}

	public ArrayList<Truck> getListTrucks() {
		return listTrucks;
	}

	public void setListTrucks(ArrayList<Truck> listTrucks) {
		this.listTrucks = listTrucks;
	}

	public void setHubPoint(Point hubPoint) {
		this.hubPoint = hubPoint;
	}

	public void setBranchPoint(Point branchPoint) {
		this.branchPoint = branchPoint;
	}

	public boolean isThreadSuspend() {
		return threadSuspend;
	}

	public void setThreadSuspend(boolean threadSuspend) {
		this.threadSuspend = threadSuspend;
	}
		
}
