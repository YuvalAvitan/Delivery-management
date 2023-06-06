package components;

import java.util.ArrayList;

/*Branch - describes a local branch. Maintains a list of packages stored at the branch or intended for collection from the sender's address to this branch,
 *  and a list of vehicles that collect the packages from the sending customers and deliver the packages to the receiving customers.
 */
public class Branch implements Node {
	private static int branchCount=0;
	private int branchId;
	private String branchName;
	private ArrayList<Truck> listTrucks; //A collection of vehicles belonging to this branch//
	private ArrayList<Package> listPackages;//A collection of packages that are in the branch and packages that must be collected are shipped by this branch.//
	
	//Default builder, calculates the serial number of the branch and creates the name of the branch, the two remaining fields are initialized to blank collections.//
	public Branch() {
		this.branchId=branchCount++;
		this.branchName="Branch "+this.branchId;
		listTrucks=new ArrayList<Truck>();
		listPackages=new ArrayList<Package>();
		
	}
	
	//A builder who gets a branch name, calculates the serial number of the branch//
	public Branch(String branchName) {
		this.branchName=branchName;
		
		this.branchId=(int)this.branchName.charAt(this.branchName.length()-1);
		
		listTrucks=new ArrayList<Truck>();
		listPackages=new ArrayList<Package>();
		
	}
	
	public void collectPackage(Package p) {
		
		
		getListPackages().add(p);
		
		
	}
	
	public void deliverPackage(Package p) {
		
		getListPackages().remove(p);
		
		
		
		
	}
	
	public void work() {
		int j=0;
		for(int i=0;i<this.getListPackages().size();i++) {
			if(this.getListPackages().get(i).getStatus()==Status.CREATION && this.getListTrucks().get(j).isAvailable()==true) {
				this.getListTrucks().get(j).setTimeLeft((((this.getListPackages().get(i).getSenderAddress().getStreet())/10)%10)+1); //The street number of the sender is divided by ten, for the remaining remainder one is added.//
				this.getListTrucks().get(j).setAvailable(false);
				((Van)this.getListTrucks().get(j)).collectPackage(this.getListPackages().get(i)); 
				this.getListPackages().get(i).addTracking(MainOffice.getClock(),null, Status.CREATION);//For each package that is in the branch, if it is in the status of waiting for collection from a customer, make an attempt to collect//
				System.out.println("Van "+this.getListTrucks().get(j).getTruckID()+" is collecting package "+this.getListPackages().get(i).getPackageID()+", time to arrive: "+this.getListTrucks().get(j).getTimeLeft());
			}
			else if(this.getListPackages().get(i).getStatus()==Status.DELIVERY && this.getListTrucks().get(j).isAvailable()==true) {
				this.getListTrucks().get(j).setTimeLeft((((this.getListPackages().get(i).getDestinationAddress().getStreet())/10)%10)+1);
				this.getListTrucks().get(j).setAvailable(false);
				this.getListPackages().get(i).setStatus(Status.DISTRIBUTION);
				this.getListPackages().get(i).addTracking(MainOffice.getClock(),(Van)this.getListTrucks().get(i), Status.DISTRIBUTION);
				((Van)this.getListTrucks().get(j)).collectPackage(this.getListPackages().get(i));
				this.deliverPackage(this.getListPackages().get(i));
				System.out.println("Van "+this.getListTrucks().get(j).getTruckID()+" is delivering package "+this.getListPackages().get(i).getPackageID()+", time left: "+this.getListTrucks().get(j).getTimeLeft());
			}	
			else if(this.getListPackages().get(i).getStatus()==Status.BRANCH_STORAGE && this.getListTrucks().get(j).isAvailable()==true) {
				((StandardTruck) MainOffice.getHub().getListTrucks().get(j)).collectPackage(this.getListPackages().get(i));
				
			}
		}
	}
	public int getBranchId() {
		return branchId;
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

	public ArrayList<Truck> getListTrucks() {
		return listTrucks;
	}

	public void setListTrucks(ArrayList<Truck> listTrucks) {
		this.listTrucks = listTrucks;
	}

	public ArrayList<Package> getListPackages() {
		return listPackages;
	}

	public void setListPackages(ArrayList<Package> listPackages) {
		this.listPackages = listPackages;
	}
	
	

}
