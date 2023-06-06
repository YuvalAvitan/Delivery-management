package components;

//Lior Liberov ID:315199059
//Yuval Avitan ID:319066007

public class StandardTruck extends Truck implements Node {
	
/*- A vehicle for transferring packages from the sorting center to the branches and back.
 *  All vehicles of this type are in the sorting center.
 */
	
	private int maxWeight; //Maximum weight that a vehicle can carry.//
	private Branch destination; //Target branch / sorting center//
	
	
	//A default constructor that produces an object with a vehicle license plate number and model at random.//
	public StandardTruck() {
		super();
		maxWeight = 2 + (int)(Math.random() * ((6 - 2) + 1));
		maxWeight *= 100;
		System.out.print("Creating StandardTruck");
		System.out.println(this);
		
	}
	
	//Builder that accepts as arguments: license plate number, vehicle model and maximum weight.//
	public StandardTruck(String license, String model, int maxWeight) {
		super(license,model);
		this.maxWeight=maxWeight;
		
	}
	

	public int getMaxWeight() {
		return maxWeight;
	}

	public Branch getDestination() {
		return destination;
	}

	public void setDestination(Branch destination) {
		this.destination = destination;
	}
	


	public void setMaxWeight(int maxWeight) {
		this.maxWeight = maxWeight;
	}

	@Override
	public String toString() {
		return super.toString() + " ,maxWeight=" + maxWeight + "]";
	}


	public void collectPackage(Package p) {
		if(p.getStatus() == Status.BRANCH_STORAGE) {
			p.setStatus(Status.HUB_TRANSPORT);
			p.addTracking(MainOffice.getClock(),this, Status.HUB_TRANSPORT);
			getPackages().add(p);
		}
		if(p.getStatus() == Status.HUB_STORAGE) {
			p.setStatus(Status.BRANCH_TRANSPORT);
			p.addTracking(MainOffice.getClock(),this, Status.BRANCH_TRANSPORT);
			getPackages().add(p);
		}
	}
	
	
	public void deliverPackage(Package p) {
		if(p.getStatus() == Status.HUB_TRANSPORT) {
			p.setStatus(Status.HUB_STORAGE);
			p.addTracking(MainOffice.getClock(),destination, Status.HUB_TRANSPORT);
			destination.collectPackage(p);
			getPackages().remove(p);
		}
		if(p.getStatus() == Status.BRANCH_TRANSPORT) {
			destination.collectPackage(p);
			getPackages().remove(p);
		}
	}
	
	
	@Override
	public void work() {
		if(!this.isAvailable()) {
			if(getTimeLeft() != 0)
				setTimeLeft(getTimeLeft() - 1); //A vehicle found during a trip reduces the time left to the end of the trip (timeLeft) by 1.//
			else {
				System.out.println("StandardTruck " + getTruckID() + " arrived to " + destination.getBranchName());
				if(!destination.getListTrucks().contains(this))
					destination.getListTrucks().add(this);
				for(int i=0;i<getPackages().size();i++) {
					deliverPackage(getPackages().get(i));
				}
				System.out.println("StandardTruck " + getTruckID() + " unloaded packages at " + destination.getBranchName());
				if(destination instanceof Hub )
					setAvailable(true);
				else {
					for(int i=0;i<destination.getListPackages().size();i++) {
						if(!(destination.getListPackages().get(i) instanceof NonStandardPackage) 
								&& (destination.getListPackages().get(i).getStatus() == Status.BRANCH_STORAGE || destination.getListPackages().get(i).getStatus() == Status.HUB_STORAGE)) {
							int weight = 0;
							if(destination.getListPackages().get(i) instanceof SmallPackage)
								weight += 1;
							if(destination.getListPackages().get(i) instanceof StandardPackage)
								weight += ((StandardPackage)destination.getListPackages().get(i)).getWeight();
							if(weight  <= getMaxWeight()) {
								collectPackage(destination.getListPackages().get(i));
								destination.getListPackages().remove(i);
							}
						}
					}
					System.out.println("StandardTruck " + getTruckID() + " loaded packages at " + destination.getBranchName());
					if(!(destination instanceof Hub))
						destination.getListTrucks().remove(this);
					int timeLeft = 1 + (int)(Math.random() * ((6 - 1) + 1)); //A new travel time will be drawn (value between 1 and 6) and will be updated in the relevant field. 
					setTimeLeft(timeLeft);
					System.out.println("StandardTruck " + getTruckID() + " is on it's way to the HUB, time to arrive: " + (getTimeLeft() + 1));
				}
			}
		}
	}

}
