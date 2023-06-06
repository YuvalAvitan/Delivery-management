package components;

import java.util.ArrayList;
import java.util.Random;

public class Hub extends Branch{
	
	private ArrayList<Branch> branches=new ArrayList<Branch>();
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
	
	
	public synchronized void sendTruck(StandardTruck t) {
		synchronized(t) {
			t.notify();
		}
		t.setAvailable(false);
		t.setDestination(branches.get(currentIndex));
		t.load(this, t.getDestination(), Status.BRANCH_TRANSPORT);
		t.setTimeLeft(((new Random()).nextInt(10)+1)*10);
		t.initTime = t.getTimeLeft();
		System.out.println(t.getName() + " is on it's way to " + t.getDestination().getName() + ", time to arrive: "+t.getTimeLeft());	
		currentIndex=(currentIndex+1)%branches.size();
	}
	
	
	public synchronized void shipNonStandard(NonStandardTruck t) {
		for (Package p: listPackages) {
			if (p instanceof NonStandardPackage) {
						synchronized(t) {
							t.notify();
						}
						t.collectPackage(p);
						listPackages.remove(p);
						return;
			}
		}	
	}
	
	
	@Override
	public void work() {

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
			for (Truck t : listTrucks) {
				if (t.isAvailable()){
					if(t instanceof NonStandardTruck) {
						shipNonStandard((NonStandardTruck)t);
					}
					else {
						sendTruck((StandardTruck)t);
					}
				}	
			}
		}
	}


	public void removeBranch(int i) {
		Branch branch = getBranches().get(i);
		getBranches().remove(branch);
		System.out.println("\nRemoving "+ branch);
		for (int j = branch.getListTrucks().size() - 1; j >= 0; j--) {
			System.out.println("Removing "+ branch.getTrucks().get(j));
			branch.getTrucks().remove(j);
			Van.setCountID(Van.getCountID() - 1);
		}
		Branch.setCounter(Branch.getCounter() - 1);
		
	}



}
