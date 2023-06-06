package components;
import java.util.ArrayList;

//Lior Liberov ID:315199059
//Yuval Avitan ID:319066007


/*A general type represents packages.*/

public class Package {
	private static int packageC=1000;
	private int packageID; //Package ID number. Package numbering starts from 1000.//
	private Priority priority;
	private Status status;//current status//
	private Address senderAddress;//sender's address//
	private Address destinationAddress;//Recipient's address//
	private ArrayList<Tracking> tracking; //Collection of records with transfer history. In creating the package is initialized as an empty collection and the first object relating to the creation of the package is added to it. In each of its transfer operations a package is added to this collection and another object.//
	
	public Package(Priority priority,Address senderAddress,Address destinationAddress) {	//A builder who accepts as arguments arguments, addresses the sender and receives a package.//
		this.priority=priority;
		this.senderAddress=senderAddress;
		this.destinationAddress=destinationAddress;
		this.packageID=packageC++;
		this.tracking=new ArrayList<Tracking>();
		this.status=Status.CREATION;
		
	}
	
	/*Receives an object of type Node and a Status object,
	creates and adds an object of the Tracking class to the collection collection in the class.*/
	
	public void addTracking(int time,Node node,Status status) {
		Tracking t1=new Tracking(time,node,status);
		this.tracking.add(t1);
		
	}
	
	/*Prints the records stored in the tracking collection, each object in a separate row*/
	
	public void printTracking() {
		for(int i=0;i<this.tracking.size();i++)
			System.out.println(tracking.get(i).toString());
	}

	public int getPackageID() {
		return packageID;
	}


	public void setPackageID(int packageID) {
		this.packageID = packageID;
	}


	public Priority getPriority() {
		return priority;
	}


	public void setPriority(Priority priority) {
		this.priority = priority;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}


	public Address getSenderAddress() {
		return senderAddress;
	}


	public void setSenderAddress(Address senderAddress) {
		this.senderAddress = senderAddress;
	}


	public Address getDestinationAddress() {
		return destinationAddress;
	}


	public void setDestinationAddress(Address destinationAddress) {
		this.destinationAddress = destinationAddress;
	}


	public ArrayList<Tracking> getTracking() {
		return tracking;
	}


	public void setTracking(ArrayList<Tracking> tracking) {
		this.tracking = tracking;
	}


	@Override
	public String toString() {
		return "Package [packageID=" + packageID + ", priority=" + priority + ", status=" + status + ", senderAddress="
				+ senderAddress + ", destinationAddress=" + destinationAddress + "]";
	}

	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Package other = (Package) obj;
		if (destinationAddress == null) {
			if (other.destinationAddress != null)
				return false;
		} else if (!destinationAddress.equals(other.destinationAddress))
			return false;
		if (packageID != other.packageID)
			return false;
		if (priority != other.priority)
			return false;
		if (senderAddress == null) {
			if (other.senderAddress != null)
				return false;
		} else if (!senderAddress.equals(other.senderAddress))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

}
