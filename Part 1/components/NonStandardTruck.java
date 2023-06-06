package components;

//Lior Liberov ID:315199059
//Yuval Avitan ID:319066007

import java.util.ArrayList;
import java.util.Random;

//NonStandardTruck - a vehicle for transporting packages of non-standard size. All vehicles of this type are present//
public class NonStandardTruck extends Truck implements Node{
	private int width;
	private int length;
	private int height;
	
	//A default builder that produces an object with a vehicle ID number and model at random//
	public NonStandardTruck() {
		super();
		Random r1=new Random();
		this.height=r1.nextInt(400-1)+1;
		this.length=r1.nextInt(1000-1)+1;
		this.width=r1.nextInt(500-1)+1;
		System.out.print("Creating NonStandardTruck");
		System.out.println(this);
		
		
		
		
	}
	
	//Builder that accepts arguments: license plate number, model of the vehicle and maximum length / width / height of cargo that the vehicle can//
	public NonStandardTruck(String licensePlate,String truckModel,int length,int width,int height) {
		super(licensePlate,truckModel);
		this.length=length;
		this.width=width;
		this.height=height;
		
		
	}
	
	public void collectPackage(Package p) {
		getPackages().add(p);
		
		
	}
	
	public void deliverPackage(Package p) {
        getPackages().remove(p);
		
		
	}
	
	public void work() {
		if(!this.isAvailable()) {
			this.setTimeLeft(this.getTimeLeft()-1);//A vehicle found during a trip reduces the time left to the end of the trip (timeLeft) by 1.//
			if(this.getTimeLeft()==0) {
				for(int i=0;i<this.getPackages().size();i++) {
					if(this.getPackages().get(i).getStatus()==Status.COLLECTION) { // //If the purpose of the trip was to collect a package from a sending customer (COLLECTION), the vehicle will go to the delivery.//
						this.setTimeLeft(((Math.abs(this.getPackages().get(i).getSenderAddress().getStreet()-this.getPackages().get(i).getSenderAddress().getStreet())/10)%10)+1);
						this.getPackages().get(i).setStatus(Status.DISTRIBUTION);
						this.getPackages().get(i).addTracking(MainOffice.getClock(), this, Status.DISTRIBUTION);
						System.out.println("NonStandardTruck "+this.getTruckID()+" is delivering package "+this.getPackages().get(i).getPackageID()+",time left: "+this.getTimeLeft());
					}
					else if(this.getPackages().get(i).getStatus()==Status.DISTRIBUTION) { //If the purpose of the trip was to deliver the package to the customer (DISTRIBUTION), the package will be removed from the list of packages in the vehicle, the status of the package and the transfer history will be updated accordingly and a notice will be printed that the package was delivered to the customer.//
						this.getPackages().get(i).setStatus(Status.DELIVERED);
						this.getPackages().get(i).addTracking(MainOffice.getClock(), null,Status.DELIVERED);
						System.out.println("NonStandardTruck "+this.getTruckID()+" has delivered package "+this.getPackages().get(i).getPackageID()+" to the destination");
						this.deliverPackage(this.getPackages().get(i));
						this.setAvailable(true); //The vehicle switches to "free" mode.//
					}
				}
			}
		}
		
		
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
