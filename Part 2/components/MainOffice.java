package components;

import GUI.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class MainOffice implements Runnable,sys{
	private static int clock=0;
	private static Hub hub;
	private ArrayList<Package> packages=new ArrayList<>();

	private JPanel panel;
	private int numberOfPackages;
	public boolean threadSuspend = false;
	static public boolean stop = false;
	private ArrayList<sys> system;
	public Color color = Color.RED;
	public static Object lock = new Object();
	private int x;
	private int y1;
	private int y2;

	public MainOffice(int branches, int trucksForBranch) {
		addHub(trucksForBranch);
		addBranches(branches, trucksForBranch);
		System.out.println("\n\n========================== START ==========================");

	}

	/**
	 * adding new constructor to the HW2:
	 * @param branches
	 * @param trucksForBranch
	 * @param numberOfPackages
	 */

	public MainOffice(int branches, int trucksForBranch, int numberOfPackages) {
		addHub(trucksForBranch);
		addBranches(branches, trucksForBranch);
		this.numberOfPackages = numberOfPackages;

		this.x = 100;
		this.y1 = 50;
		this.y2 = 620;

		this.system = new ArrayList<>();
		this.system.add(this.hub);
		for(int i = 0; i<this.hub.getListTrucks().size(); i++)
		{
			this.system.add(this.hub.getListTrucks().get(i));
		}
		for(int i = 0; i<this.hub.getBranches().size(); i++ )
		{
			this.system.add(this.hub.getBranches().get(i));
			for(int j = 0; j<this.hub.getBranches().get(i).getListTrucks().size(); j++)
			{
				this.system.add(this.hub.getBranches().get(i).getListTrucks().get(j));
			}
		}

		System.out.println("\n\n========================== START ==========================");
	}
	
	public static Hub getHub() {
		return hub;
	}


	public static int getClock() {
		return clock;
	}

	
	public void play() {
		System.out.println("finally starting");
		int i = 0 ;
		try {
			while (i<60) {
				Thread.sleep(2000);

				tick();
				i++;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();

		
		}
		System.out.println("\n========================== STOP ==========================\n\n");
		printReport();
	}
	
	
	public void printReport() {
		for (Package p: packages) {
			System.out.println("\nTRACKING " +p);
			for (Tracking t: p.getTracking())
				System.out.println(t);
		}
	}
	
	public String clockString() {
		String s="";
		int minutes=clock/60;
		int seconds=clock%60;
		s+=(minutes<10) ? "0" + minutes : minutes;
		s+=":";
		s+=(seconds<10) ? "0" + seconds : seconds;
		return s;
	}
	
	
	public void tick() {
		System.out.println(clockString());
		/*if (clock%5==0)
			addPackage();*/
		hub.work();
		for (Branch b: hub.getBranches()) {
			b.work();
		}
		clock++;
	}
		
	
	public void addHub(int trucksForBranch) {
		hub=new Hub();
		for (int i=0; i<trucksForBranch; i++) {
			hub.addTruck(new StandardTruck());
		}
		hub.addTruck(new NonStandardTruck());
	}
	
	
	public void addBranches(int branches, int trucks) {
		for (int i=0; i<branches; i++) {
			Branch branch=new Branch();
			for (int j=0; j<trucks; j++) {
				branch.addTruck(new Van());
			}
			hub.add_branch(branch);		
		}
	}
	
	
	public void addPackage() {
		Random r = new Random();
		Package p;
		Priority priority=Priority.values()[r.nextInt(3)];
		Address sender = new Address(r.nextInt(hub.getBranches().size()), r.nextInt(999999)+100000);
		Address dest = new Address(r.nextInt(hub.getBranches().size()), r.nextInt(999999)+100000);

		switch (r.nextInt(3)){
		case 0:
			p = new SmallPackage(priority,  sender, dest, r.nextBoolean() );
			p.getSenderBranch().addPackage(p);
			break;
		case 1:
			p = new StandardPackage(priority,  sender, dest, r.nextFloat()+(r.nextInt(9)+1));
			p.getSenderBranch().addPackage(p);
			break;
		case 2:
			p=new NonStandardPackage(priority,  sender, dest,  r.nextInt(1000), r.nextInt(500), r.nextInt(400));
			hub.addPackage(p);
			break;
		default:
			p=null;
			return;
		}

		p.setSendCircle(new Circle(x,y1,15));
		p.setDestCircle(new Circle(x,y2,15));
		if(p instanceof NonStandardPackage)
		{
			p.setOriginLine(new Line(x,y1+15,775,200));
			p.setDestLine(new Line(x,y1+15,x,y2-15));
		}
		else if(p instanceof StandardPackage || p instanceof SmallPackage)
		{
			Line l1 = new Line(p.sendCircle.getCenterX(), p.sendCircle.getCenterY()+15,p.getSenderBranch().getX()+20,p.getSenderBranch().getY());
			Line l2 = new Line(p.getSenderBranch().getX()+20,p.getSenderBranch().getY()+30,p.destCircle.getCenterX(), p.destCircle.getCenterY()-15);

			//adding the lines to the package class:
			p.setOriginLine(l1);
			p.setDestLine(l2);

			//adding the lines to the branch class:
			p.getSenderBranch().getLines().add(l1);
			p.getSenderBranch().getLines().add(l2);

		}
		this.x = x + 70;

		this.packages.add(p);

	}

	public ArrayList<Package> getPackages() {
		return packages;
	}


	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	public void drawPackages(Graphics g)
	{
		for(int i = 0; i<this.getPackages().size(); i++)
		{
			this.getPackages().get(i).drawCircle(g);
		}
	}

	public JPanel getPanel() {
		return panel;
	}

	@Override
	public void run() {
		for(int i=0; i<this.system.size(); i++) {
			Thread t = new Thread((Runnable) system.get(i));
			t.start();
		}
		int counterPackages = 0;
		boolean flagGame = true;
		while (numberOfPackages > counterPackages)
		{
			try {
				Thread.sleep(5000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}

			//adding the packages to the system according to the dialog HW2
			addPackage();
			counterPackages++;

			synchronized(this) {
				while(threadSuspend)
				{
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}

	}

	public synchronized void goToSleep() throws InterruptedException {
		this.wait();
	}

	@Override
	public synchronized void setSuspend() {
		for(int i=0; i<this.system.size(); i++)
			system.get(i).setSuspend();
		threadSuspend = true;
	}

	@Override
	public synchronized void setResume() {
		for(int i=0; i<this.system.size(); i++)
			system.get(i).setResume();
		threadSuspend = false;
		notifyAll();
	}

	public void setStop() {
		setResume();
		for(int i=0; i<this.system.size(); i++)
				system.get(i).setStop();
		stop = true;
	}


	public static void setHub(Hub hub) {
		MainOffice.hub = hub;
	}


}
