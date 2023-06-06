package components;

//Lior Liberov ID:315199059
//Yuval Avitan ID:319066007

/*SmallPackage - Represents small packages.*/
public class SmallPackage extends Package {
	private boolean acknowledge;//Receiver value true if the package requires delivery confirmation after delivery to the recipient.//
	
	//A builder who accepts as arguments and priority addresses sends and receives the package, and if delivery confirmation is required.//
	public SmallPackage(Priority priority,Address senderAddress,Address destinationAddress,boolean acknowledge) {
		super(priority,senderAddress,destinationAddress);
		this.acknowledge=acknowledge;
		
	}

	public boolean isAcknowledge() {
		return acknowledge;
	}

	public void setAcknowledge(boolean acknowledge) {
		this.acknowledge = acknowledge;
	}

	@Override
	public String toString() {
		return "SmallPackage [acknowledge=" + acknowledge + ", toString()=" + super.toString() + "]";
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SmallPackage other = (SmallPackage) obj;
		if (acknowledge != other.acknowledge)
			return false;
		return true;
	}
	
	
	
	
	
}
