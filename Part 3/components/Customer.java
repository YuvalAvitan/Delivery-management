package components;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * this class is for demonstrate a costumer that will active packages
 * according to the HW
 */
public class Customer implements Runnable{
	//fields:
	private static int countSN=1;
	final private int serialNumber;
	Address sender;
	ArrayList<Package> packages =  new ArrayList<Package>();
	ArrayList<Package> removePackages = new ArrayList<Package>();
	private static String trackingFileName = "tracking.txt";

	/**
	 * constructor gets an instance object from a mainOffice field
	 * gets the hub from that field and making a sender for
	 * the application to run
	 */
	public Customer() {
		MainOffice.getInstance();
		Hub hub = MainOffice.getHub();
		Random r = new Random();
		serialNumber = countSN++;
		sender = new Address(r.nextInt(hub.getBranches().size()), r.nextInt(999999)+100000);		
	}

	/**
	 * this function much like the last HW we did on a mainOffice
	 * creating 5 packages with threads that makes them sleep
	 * according to the HW
	 */
	public void createPackages() {
		for (int i = 0; i < 5; i++) {
			Random rand = new Random();
			int time = rand.nextInt(4) + 2;
			try {	
				Thread.sleep(time*1000);
				addPackage();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * the run function is override methode from the interface Runnable
	 * we use it to explicit the costumer how to behave in his thread
	 */
	@Override
	public void run() {
		createPackages();
		try {
			
			//TODO to read from the tracking.txt file to check if all the packages completed.
			
			while (packages.size() > 0) {
				Thread.sleep(10000);
				checkPackages();
				
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}


	/**
	 * this function is reading from the file tracking.txt we build earlier
	 * and according to the package's status that we read we updating the system
	 * and the function uses the lock, so the only one who can read the file is
	 * mai office (this is because of the design pattern called readWriteLock)
	 * @throws InterruptedException for the file that we read
	 */
	private synchronized void checkPackages() throws InterruptedException {
		try {
			MainOffice.getInstance();
			MainOffice.readLock.lock();
			Thread.sleep(3000);
			Scanner myReader = new Scanner(new File(trackingFileName));
			while (myReader.hasNextLine()) {
				String line = myReader.nextLine();
				String status = line.split("=")[1].split(",")[0];
				int package_id = Integer.valueOf(line.split("=")[2]);
				if (status.equals("DELIVERED")) {
					for (Package p : packages) {
						 if (p.getPackageID() == package_id) {
							 removePackages.add(p);
						}
					}
					for (Package p : removePackages) {
						packages.remove(p);
					}
					removePackages = new ArrayList<Package>();
				}
				
				
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			MainOffice.readLock.unlock();

		}
		
	}


	/**
	 * this function is exactly the same function from the HW1 and HW2
	 * taken from the class mainOffice and added to the package list
	 * a random package with all the detail randomly
	 */
	public void addPackage() {
		MainOffice.getInstance();
		Hub hub = MainOffice.getHub();
		Random r = new Random();
		Package p;
		Branch br;
		Priority priority=Priority.values()[r.nextInt(3)];
		Address dest = new Address(r.nextInt(hub.getBranches().size()), r.nextInt(999999)+100000);

		switch (r.nextInt(3)){
		case 0:
			p = new SmallPackage(priority,  sender, dest, r.nextBoolean() );
			br = hub.getBranches().get(sender.zip);
			br.addPackage(p);
			p.setBranch(br); 
			break;
		case 1:
			p = new StandardPackage(priority,  sender, dest, r.nextFloat()+(r.nextInt(9)+1));
			br = hub.getBranches().get(sender.zip); 
			br.addPackage(p);
			p.setBranch(br); 
			break;
		case 2:
			p=new NonStandardPackage(priority,  sender, dest,  r.nextInt(200), r.nextInt(500), r.nextInt(300));
			hub.addPackage(p);
			break;
		default:
			p=null;
			return;
		}
		packages.add(p);
		MainOffice.getInstance().getPackages().add(p);
		
	}
	
	
	
	
}
