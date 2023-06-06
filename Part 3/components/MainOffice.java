package components;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JPanel;

public class MainOffice implements Runnable {
	private static MainOffice single_instance = null;
	private static int clock = 0;
	private static Hub hub;
	private ArrayList<Package> packages = new ArrayList<Package>();
	private JPanel panel;
	private int maxPackages;
	private boolean threadSuspend = false;
	/**
	 * fields that we added for HW3
	 * for example the ReadWriteLock mission
	 * and the name of the file that we need to write on
	 * and exc
	 */
	private static ArrayList<Thread> customers = new ArrayList<Thread>();
	private static String trackingFileName = "tracking.txt";
	private static File trackingFile = new File(trackingFileName);
	private final static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	public final static Lock readLock = readWriteLock.readLock();
	private final static Lock writeLock = readWriteLock.writeLock();
	private final int MAX_CUSTOMERS = 10;
	private ExecutorService pool;
	private int trucksForBranch;

	private static int trackingNumber = 1;
/**
 * This function helps us to implement the design template called "singleton" and thus returns the mainOffice instance and
 *  thus it helps us to do a single instance of mainOffice and no more
 * @return instance of the object mainOffice
 */
	public synchronized static MainOffice getInstance() {
		if (single_instance == null) {
			System.out.println("WARNING - we need to check that.");
			single_instance = new MainOffice();
		}
		return single_instance;
	}

	public MainOffice() {

	}

	public MainOffice(int branches, int trucksForBranch, JPanel panel, int maxPack) {
		
		this.panel = panel;
		this.maxPackages = maxPack;
		this.trucksForBranch = trucksForBranch;
		Branch.setCounter(0);
		addHub(trucksForBranch);
		addBranches(branches, trucksForBranch);
		single_instance = this;
		try {
			if (!trackingFile.createNewFile()) {
				trackingFile.delete();
				trackingFile.createNewFile();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("\n\n========================== START ==========================");
	}

	public static Hub getHub() {
		return hub;
	}

	public static int getClock() {
		return clock;
	}

	/**
	 * This function keeps a chronological record of new statuses for all packages together into a txt file called tracking.
	 * txt in fact the file will contain all the tracking records of the packages in the order they were created
		Each entry in a separate row and the rows are numbered in ascending order
		And in addition we made exclusive access to write to mainOffice by a design template called readWriteLock
	 * @param t is the tracking parameter we inserting to the file.
	 * @throws IOException is for java to check whether the file was created or we need to create it.
	 */
	
	@SuppressWarnings("resource")
	public synchronized static void writeTracking(Tracking t) throws IOException {
		try {
			writeLock.lock();
			BufferedWriter out = new BufferedWriter(new FileWriter(trackingFileName, true));
			out.write(trackingNumber++ + ". " + t.toString() + "\n");
			out.close();
		} catch (IOException e) {
			System.out.println("exception occurred" + e);
		} finally {
			writeLock.unlock();
		}
	}

	
	@Override
	public void run() {
		Thread hubThrad = new Thread(hub);
		hubThrad.start();
		for (Truck t : hub.listTrucks) {
			Thread trackThread = new Thread(t);
			trackThread.start();
		}
		for (Branch b : hub.getBranches()) {
			Thread branch = new Thread(b);
			for (Truck t : b.listTrucks) {
				Thread trackThread = new Thread(t);
				trackThread.start();
			}
			branch.start();
		}
		
		/**
		 * At the beginning of the run the head office creates 10 clients and runs them all.
		 * thread pool- The customer processes will work in parallel without unnecessary waiting for each other and 2 processes that will take care of everyone
		 */
		pool = Executors.newFixedThreadPool(2);
		for (int i = 0; i < 10; i++) {
			Customer customer = new Customer();
			Thread c = new Thread(customer);
			pool.execute(c);
		}
		while (true) {
			synchronized (this) {
				while (threadSuspend)
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			tick();
		}

	}

	public void printReport() {
		for (Package p : packages) {
			System.out.println("\nTRACKING " + p);
			for (Tracking t : p.getTracking())
				System.out.println(t);
		}
	}

	public String clockString() {
		String s = "";
		int minutes = clock / 60;
		int seconds = clock % 60;
		s += (minutes < 10) ? "0" + minutes : minutes;
		s += ":";
		s += (seconds < 10) ? "0" + seconds : seconds;
		return s;
	}

	public void tick() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clock++;
		
		
		panel.repaint();
	}

	public void branchWork(Branch b) {
		for (Truck t : b.listTrucks) {
			t.work();
		}
		b.work();
	}

	public void addHub(int trucksForBranch) {
		hub = new Hub();
		for (int i = 0; i < trucksForBranch; i++) {
			Truck t = new StandardTruck();
			hub.addTruck(t);
		}
		Truck t = new NonStandardTruck();
		hub.addTruck(t);
	}

	public void addBranches(int branches, int trucks) {
		for (int i = 0; i < branches; i++) {
			Branch branch = new Branch();
			for (int j = 0; j < trucks; j++) {
				branch.addTruck(new Van());
			}
			hub.add_branch(branch);
		}
	}

	public ArrayList<Package> getPackages() {
		return this.packages;
	}


	public synchronized void setSuspend() {
		threadSuspend = true;
		for (Truck t : hub.listTrucks) {
			t.setSuspend();
		}
		for (Branch b : hub.getBranches()) {
			for (Truck t : b.listTrucks) {
				t.setSuspend();
			}
			b.setSuspend();
		}
		hub.setSuspend();
	}

	public synchronized void setResume() {
		threadSuspend = false;
		notify();
		hub.setResume();
		for (Truck t : hub.listTrucks) {
			t.setResume();
		}
		for (Branch b : hub.getBranches()) {
			b.setResume();
			for (Truck t : b.listTrucks) {
				t.setResume();
				;
			}
		}
	}



	public void addBranch() {
		Branch branch = new Branch();
		for (int j = 0; j < trucksForBranch; j++) {
			branch.addTruck(new Van());
		}
		hub.add_branch(branch);	
		panel.repaint();
	}

	/**
	 * this function is for the design pattern called "memento"
	 * removing a branch that we added as a clone
	 * the branch was added in a middle of a run time
	 * and this function help us removing the exact branch that
	 * added inside the list we keep in the array list called "branches"
	 */
	public void removeBranch() {
		hub.removeBranch(hub.getBranches().size() - 1);
		panel.repaint();		
	}

}
