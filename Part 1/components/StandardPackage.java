package components;

//Lior Liberov ID:315199059
//Yuval Avitan ID:319066007

/*StandardPackage - A class representing packages of varying weights over one kilogram.*/
public class StandardPackage extends Package {
	private double weight; //The weight of the package//
	
	//Builder that receives as priority arguments, sender and recipient addresses, package weight.//
	public StandardPackage(Priority priority,Address senderAddress,Address destinationAddress,double weight) {
		super(priority,senderAddress,destinationAddress);
		this.weight=weight;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "StandardPackage [weight=" + weight + ", toString()=" + super.toString() + "]";
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		StandardPackage other = (StandardPackage) obj;
		if (Double.doubleToLongBits(weight) != Double.doubleToLongBits(other.weight))
			return false;
		return true;
	}
	
	
	
	
}
