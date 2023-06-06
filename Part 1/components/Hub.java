package components;

import java.util.ArrayList;
import java.util.Random;

//Hub - Describes a main branch.//

public class Hub extends Branch implements Node{
	private ArrayList<Branch> branches; //Collection of objects of all local branches.//
	
	public Hub() {
		super("HUB");
		
		branches=new ArrayList<Branch>();
		
		this.setBranchId(-1);
		
	}
	
    public void collectPackage(Package p) {
    	
    	
    	getListPackages().add(p);
    	
    	
		
	}
	
	public ArrayList<Branch> getBranches() {
		return branches;
	}

	public void setBranches(ArrayList<Branch> branches) {
		this.branches = branches;
	}

	public void deliverPackage(Package p) {
        
    	
      
    	getListPackages().remove(p);
    	
    	
		
	}
	
	public void work() {
	
			for(int i=0;i<this.getListTrucks().size();i++) {
				if(this.getListTrucks().get(i).isAvailable() && this.getListTrucks().get(i) instanceof StandardTruck){
					((StandardTruck) this.getListTrucks().get(i)).setDestination(this.getBranches().get(i));
					((StandardTruck) this.getListTrucks().get(i)).setAvailable(false);
				}
			}
	}

}
