package components;

import GUI.Line;
import GUI.modelTruck;
import GUI.sys;

import java.awt.*;
import java.util.ArrayList;

import static GUI.postTrackingSystem.numberOfBranches;
import static components.MainOffice.lock;
//import java.awt.Color;


public class Branch implements Node, Runnable,sys {
	private static int counter=-1;
	private int branchId;
	private String branchName;
	protected ArrayList <Package> packages = new ArrayList<Package>();
	protected ArrayList <Truck> listTrucks = new ArrayList<Truck>();

	public boolean threadSuspend = false;
	public boolean stop = false;
	public static Object collectLock = new Object();


	public ArrayList<Truck> getListTrucks() {
		return listTrucks;
	}
	public Color DARK_GREEN = new Color(0,102,0);
	public  Color LIGHT_BLUE = new Color(51,204,255);
	public Color color = LIGHT_BLUE;
	public ArrayList<Line> lines = new ArrayList<>();
	private Line lineToHub;
	public int x;
	public int y;
	public boolean run_flag = true;



	public Branch() {
		this("Branch "+counter);
	}
	
	public Branch(String branchName) {
		this.branchId=counter++;
		this.branchName=branchName;
		System.out.println("\nCreating "+ this);
	}
	
	public Branch(String branchName, Package[] plist, Truck[] tlist) {
		this.branchId=counter++;
		this.branchName=branchName;
		addPackages(plist);
		addTrucks(tlist);
	}
	
	
	public void printBranch() {
		System.out.println("\nBranch name: "+branchName);
		System.out.println("Packages list:");
		for (Package pack: packages)
			System.out.println(pack);
		System.out.println("Trucks list:");
		for (Truck trk: listTrucks)
			System.out.println(trk);
	}
	
	
	public void addPackage(Package pack) {

		packages.add(pack);

	}
	
	
	public void addTruck(Truck trk) {
		listTrucks.add(trk);
	}
	
	
	public void addPackages(Package[] plist) {
		for (Package pack: plist)
			packages.add(pack);

	}
	
	
	public void addTrucks(Truck[] tlist) {
		for (Truck trk: tlist)
			listTrucks.add(trk);
	}

	
	public int getBranchId() {
		return branchId;
	}
	
	
	public String getName() {
		return branchName;
	}

	
	@Override
	public String toString() {
		return "Branch " + branchId + ", branch name:" + branchName + ", packages: " + packages.size()
				+ ", trucks: " + listTrucks.size();
	}

	
	@Override
	public synchronized void collectPackage(Package p) {
		synchronized (MainOffice.lock) {

			for (Truck v : listTrucks) {
				if (v.isAvailable()) {
					v.collectPackage(p);
					v.setStartingPoint(new Point(p.getOriginLine().getX1()-5, p.getOriginLine().getY1()));
					v.setEndingPoint(new Point(p.getOriginLine().getX2()-5, p.getOriginLine().getY2()));

					v.setTruckPaint(new modelTruck(p.getOriginLine().getX1()-5, p.getOriginLine().getY1(),v.getColor()));

					MainOffice.lock.notifyAll();

					return;

				}
			}
		}
	}

	@Override
	public synchronized void deliverPackage(Package p)
	{
		synchronized (MainOffice.lock) {
			for (Truck v : listTrucks) {
				if (v.isAvailable()) {
					v.deliverPackage(p);
					return;
				}
			}
		}
	}

	@Override
	public void work() {
		synchronized (MainOffice.lock) {
			int pSize = packages.size();
			while (pSize > 0) {
				this.color = Color.blue;
				localWork();
				pSize --;
			}
			for (Truck t: listTrucks) {
				t.work();
			}
		}
	}
	
	
	public void localWork() {
		if (getPackages() != null) {
			for (Package p : getPackages()) {

				if (p.getStatus() == Status.CREATION) {
					collectPackage(p);
				}
				if (p.getStatus() == Status.BRANCH_STORAGE) {
					deliverPackage(p);
				}
			}

		}
	}
	
	
	public synchronized ArrayList<Package> getPackages() {
		return packages;
	}

	public void removePackage(Package p) {
		packages.remove(p);
	}


	public void drawBranch(Graphics g, int i)
	{
		this.x = 10;
		this.y = 350-numberOfBranches*30+i*60;
		g.setColor(color);
		g.fillRect(x, y, 40, 30);
	}


	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public synchronized int getSizePack()
	{
		return this.packages.size();
	}
	@Override
	public void run() {

		while (true) {
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

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				work();

				this.color = Color.blue;

			}
			if (packages.size() == 0) {
				this.color = this.LIGHT_BLUE;
			}
		}

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




	public ArrayList<Line> getLines() {
		return lines;
	}

	public void setLines(ArrayList<Line> lines) {
		this.lines = lines;
	}

	public Line getLineToHub() {
		return lineToHub;
	}

	public void setLineToHub(Line lineToHub) {
		this.lineToHub = lineToHub;
	}

	public void setListTrucks(ArrayList<Truck> listTrucks) {
		this.listTrucks = listTrucks;
	}
}
