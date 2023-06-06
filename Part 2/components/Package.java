package components;

import GUI.Circle;
import GUI.Line;

import java.awt.*;
import java.util.ArrayList;


public abstract class Package{
	private static int countID=1000;
	final private int packageID;
	private Priority priority;
	private Status status;
	private Address senderAddress;
	private Address destinationAddress;
	private ArrayList<Tracking> tracking = new ArrayList<Tracking>();

	public Circle sendCircle;
	public Circle destCircle;
	public Line originLine;
	public Line destLine;

	
	
	public Package(Priority priority, Address senderAddress,Address destinationAdress) {
		packageID = countID++;
		this.priority=priority;
		this.status=Status.CREATION;
		this.senderAddress=senderAddress;
		this.destinationAddress=destinationAdress;
		tracking.add(new Tracking( MainOffice.getClock(), null, status));
	}

	public Circle getSendCircle() {
		return sendCircle;
	}

	public Circle getDestCircle() {
		return destCircle;
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

	
	public int getPackageID() {
		return packageID;
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

	public String getName() {
		return "package " + getPackageID(); 
	}
	
	
	public void setDestinationAddress(Address destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	//Marina's work
	public void addTracking(Node node, Status status) {
		tracking.add(new Tracking(MainOffice.getClock(), node, status));
	}
	
	
	public void addTracking(Tracking t) {
		tracking.add(t);
	}
	
	
	public ArrayList<Tracking> getTracking() {
		return tracking;
	}

	
	public void printTracking() {
		for (Tracking t: tracking)
			System.out.println(t);
	}
	
	
	public Branch getSenderBranch() {
		return MainOffice.getHub().getBranches().get(getSenderAddress().getZip());
	}
	
	
	public Branch getDestBranch() {
		return MainOffice.getHub().getBranches().get(getDestinationAddress().getZip());
	}
	
	
	@Override
	public String toString() {
		return "packageID=" + packageID + ", priority=" + priority + ", status=" + status + ", senderAddress=" + senderAddress + ", destinationAddress=" + destinationAddress;
	}

	public void addRecords(Status status, Node node) {
		setStatus(status);
		addTracking(node, status);
	}


	public void drawCircle(Graphics g)
	{
		this.sendCircle.drawCircle(g);
		this.destCircle.drawCircle(g);
	}

	public void drawLines(Graphics g)
	{
		this.originLine.drawLine(g);
		this.destLine.drawLine(g);
	}

	public void setSendCircle(Circle sendCircle) {
		this.sendCircle = sendCircle;
	}

	public void setDestCircle(Circle destCircle) {
		this.destCircle = destCircle;
	}

	public Line getOriginLine() {
		return originLine;
	}

	public void setOriginLine(Line hubLine) {
		this.originLine = hubLine;
	}

	public Line getDestLine() {
		return destLine;
	}

	public void setDestLine(Line destLine) {
		this.destLine = destLine;
	}
}
