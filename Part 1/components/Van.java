package components;

//Lior Liberov ID:315199059
//Yuval Avitan ID:319066007

import java.util.ArrayList;



/*
 * Van - a vehicle that collects a package from the sender's address to the local branch and
 *  delivers the package from the destination branch to the recipient's address
 */
public class Van extends Truck implements Node {
	
	public Van() {
		super();
		System.out.print("Creating Van");
		System.out.println(this);
	}
	
	//Produces an object with data parameters.//
	public Van(String licensePlate,String truckModel) {
		super(licensePlate,truckModel);
	}
	
	public void collectPackage(Package p) {
		
		
		
		
	  if(p.getStatus()==Status.CREATION) {
		  p.setStatus(Status.COLLECTION);
		  p.addTracking(MainOffice.getClock(), this, Status.COLLECTION);
		
		  getPackages.add(p);
		
		  
	  }
	  
	  else if(p.getStatus()==Status.DELIVERY) {
		  p.setStatus(Status.DISTRIBUTION);
		  p.addTracking(MainOffice.getClock(), this, Status.DISTRIBUTION);
		  
		  getPackages.add(p);
		  
	  }
		
		
	}
	
	public void deliverPackage(Package p) {
		
		
		/*If the purpose of the trip was to collect a package from a sending customer (COLLECTION),
		 *  the package will be transferred from the vehicle to the branch, the status of the package will be updated,
		 *   a suitable registration will be added to the package tracking list, and a message will be printed.
		 */
		if(p.getStatus()==Status.COLLECTION) {
			p.setStatus(Status.BRANCH_STORAGE);
			p.addTracking(MainOffice.getClock(),MainOffice.getHub().getBranches().get(p.getSenderAddress().getZip()), Status.BRANCH_STORAGE);
			getPackages.remove(p);
			
			System.out.println("Van "+this.getTruckID()+" has collected package "+p.getPackageID()+" and arrived back to branch ");
			
			/*If the purpose of the trip was to deliver the package to the customer (DISTRIBUTION),
			 *  the package will be removed from the list of packages in the vehicle,
			 *   the status of the package and the transfer history will be updated accordingly 
			 *   and a notice will be printed that the package has been delivered to the customer.
			 */
		}
		else if(p.getStatus()==Status.DISTRIBUTION) {
			p.setStatus(Status.DELIVERED);
			p.addTracking(MainOffice.getClock(), this, Status.DISTRIBUTION);
			MainOffice.getHub().collectPackage(p);
            getPackages.remove(p);
			
			//If this is a small package with the option to send a confirmation of delivery activated, a message about sending a confirmation of delivery will be printed.//
			
			if(p instanceof SmallPackage && ((SmallPackage) p).isAcknowledge()) {
				System.out.println("Van "+this.getTruckID()+" has delivered package "+p.getPackageID()+" to the destination and acknowledged delivery");
			}
			else
				System.out.println("Van "+this.getTruckID()+" has delivered package "+p.getPackageID()+" to the destination");
				
			
			
		}
		
		
	}
	
	public void work() {
		if(!this.isAvailable()) {
			this.setTimeLeft(this.getTimeLeft()-1); //A vehicle that is in travel time reduces the time left to the end of the invention by 1.//
			if(this.getTimeLeft()==0) {//If after the reduction the time value is equal to zero, then the trip is over//				
				this.deliverPackage(this.getPackages().get(0));
				
			}
		}
	}
	
	
	
	
}
