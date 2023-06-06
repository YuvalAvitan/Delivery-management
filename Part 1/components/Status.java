package components;

//Lior Liberov ID:315199059
//Yuval Avitan ID:319066007

public enum Status {
	CREATION,                //CREATION - Initial status of each created package.//
	COLLECTION,              //COLLECTION - This status a package receives when a transport vehicle is sent to pick it up from the sender's address.
	BRANCH_STORAGE,			 //Storage at the branch The package collected from a customer arrived at the sender's local branch.
	HUB_TRANSPORT,			 //Transfer to the sorting center - the package on the way from the local branch to the sorting center.
	HUB_STORAGE,			 //Storage at the sorting center - the package has arrived at the sorting center and is waiting to be transferred to the destination branch.//
	BRANCH_TRANSPORT,		 //Transfer to the destination branch The package on the way from the sorting center to the destination branch.//
	DELIVERY,				 //DELIVERY - The package has arrived at the destination branch and is ready for delivery to the end customer.//
	DISTRIBUTION,			 //DISTRIBUTION - The package on the way from the destination branch to the end customer.//
	DELIVERED 				 //DELIVERED - The package was delivered to the end customer.//

}
