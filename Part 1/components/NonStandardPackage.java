package components;

//NonStandardPackage - Represents non-standard packages.//
public class NonStandardPackage extends Package {
	private int width;
	private int length;
	private int height;
	
	//Builder that accepts as arguments arguments, sender and recipient addresses, and package dimensions.//
	public NonStandardPackage(Priority priority,Address senderAddress,Address destinationAddress,int width,int length,int height) {
		super(priority,senderAddress,destinationAddress);
		this.width=width;
		this.length=length;
		this.height=height;
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

	@Override
	public String toString() {
		return "NonStandardPackage [width=" + width + ", length=" + length + ", height=" + height + ", toString()="
				+ super.toString() + "]";
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		NonStandardPackage other = (NonStandardPackage) obj;
		if (height != other.height)
			return false;
		if (length != other.length)
			return false;
		if (width != other.width)
			return false;
		return true;
	}
	
	

}
