package components;

//Lior Liberov ID:315199059
//Yuval Avitan ID:319066007

/*
 * The address consists of two integers, separated by -.
 * The first number (zip) determines the branch to which the address belongs,
 * the second number (street) the street number.
 */
public class Address {
	private int zip;    //Branch number to which the address belongs.//
	private int street;//Street number, can have values ​​of only 6 digits.//
	public Address(int zip,int street) {
		this.zip=zip;
		this.street=street;
	}
	public int getZip() {
		return zip;
	}
	public void setZip(int zip) {
		this.zip = zip;
	}
	public int getStreet() {
		return street;
	}
	public void setStreet(int street) {
		this.street = street;
	}
	@Override
	public String toString() {
		return "Address [zip=" + zip + ", street=" + street + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		if (street != other.street)
			return false;
		if (zip != other.zip)
			return false;
		return true;
	}
	
	
}
