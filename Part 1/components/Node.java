package components;

/*Represents the location of a package, can refer to branches and trucks. */
public interface Node {
	
	
	public void collectPackage(Package p); //A method that handles the collection / receipt of a package by the implementing department.//
	
	public void deliverPackage(Package p); //A method that handles the delivery of the package to the next person in the transfer chain.//
	
	public void work(); //A method that performs a work unit.//
	

}
