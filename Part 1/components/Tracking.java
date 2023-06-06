package components;


//Lior Liberov ID:315199059
//Yuval Avitan ID:319066007

/*The department represents a record in the package transfer history.
 *Each package contains a collection of records of this type,
 *each time the status of a package is changed a new record is added to the collection.
 *Each record includes the time the record was created, the point where the package is located and the status of the package.
 */


public class Tracking {
	private int time;        //time- Integer, value of the system clock as soon as the record is created.//
	private Node node;       //node- Package location - customer / branch / sorting center / transport vehicle. When the package is with the customer, this field value is null.

	private Status status;   //status- Package status as soon as the record is created//
	
	public Tracking(int time,Node node,Status status) {
		this.time=time;
		this.node=node;
		this.status=status;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Tracking [time=" + time + ", node=" + node + ", status=" + status + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tracking other = (Tracking) obj;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		if (status != other.status)
			return false;
		if (time != other.time)
			return false;
		return true;
	}
	
	

}
