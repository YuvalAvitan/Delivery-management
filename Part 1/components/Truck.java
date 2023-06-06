package components;

//Lior Liberov ID:315199059
//Yuval Avitan ID:319066007

import java.util.ArrayList;
import java.util.Random;

//Truck - a department that represents the vehicles for transporting packages.//
public class Truck {
	private static int truckC=2000;
	private int truckID; //Serial number of vehicle, vehicle numbering starts from 2000.//
	private String licensePlate;//Vehicle ID number//
	private String truckModel; //Model of vehicle//
	private boolean available; //Vehicle availability//
	private int timeLeft; //Time left until the end of the transport//
	private ArrayList<Package> packages; //List of packages for transportation that are in the vehicle//
	
	
	/*A random default builder that produces an object with a vehicle license plate and model at random.
	  A model consists of hundreds of M and a number between 0 and 4
	  A license plate consists of three numbers separated by a line, according to the pattern xxx-xx-xxx
	 */
	
	public Truck() {
		Random rand1=new Random();
		this.setTruckModel("M"+rand1.nextInt(4-0)+0);
		Random rand2=new Random();
		Random rand3=new Random();
		Random rand4=new Random();
		this.setLicensePlate(rand2.nextInt(999-100)+100+"-"+rand3.nextInt(99-10)+10+"-"+rand4.nextInt(999-100)+100);
		this.available=true;
		this.truckID=truckC++;
		this.packages=new ArrayList<Package>();
		
		
	}
	
	public int getTruckID() {
		return truckID;
	}

	public void setTruckID(int truckID) {
		this.truckID = truckID;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public String getTruckModel() {
		return truckModel;
	}

	public void setTruckModel(String truckModel) {
		this.truckModel = truckModel;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public int getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}

	public ArrayList<Package> getPackages() {
		return packages;
	}

	public void setPackages(ArrayList<Package> packages) {
		this.packages = packages;
	}

	//A builder who receives as arguments a number plate and model of the vehicle and produces an object.//
	public Truck(String licensePlate,String truckModel) {
		this.licensePlate=licensePlate;
		this.truckModel=truckModel;
		this.available=true;
	}

	@Override
	public String toString() {
		return "Truck [truckID=" + truckID + ", licensePlate=" + licensePlate + ", truckModel=" + truckModel
				+ ", available=" + available + ", timeLeft=" + timeLeft + ", packages=" + packages + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Truck other = (Truck) obj;
		if (available != other.available)
			return false;
		if (licensePlate == null) {
			if (other.licensePlate != null)
				return false;
		} else if (!licensePlate.equals(other.licensePlate))
			return false;
		if (packages == null) {
			if (other.packages != null)
				return false;
		} else if (!packages.equals(other.packages))
			return false;
		if (timeLeft != other.timeLeft)
			return false;
		if (truckID != other.truckID)
			return false;
		if (truckModel == null) {
			if (other.truckModel != null)
				return false;
		} else if (!truckModel.equals(other.truckModel))
			return false;
		return true;
	}
	
	public void work() {
		
	}
	
	

}
