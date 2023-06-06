package components;

import java.util.ArrayList;
import java.util.Random;

/*MainOffice - A department object manages the entire system,
 *  operates a clock, the branches and vehicles,
 *  creates the packages and transfers them to the appropriate branches.
 */

public class MainOffice {
	private static int clock=0;//Initialized to zero, each time preceded by one. Represents the amount of beats that have passed since the system was started.//
	private static Hub hub;//An object of a sorting center, containing all the active branches in the game.//
	private ArrayList<Package> packages; //A collection of all the packages that exist in the system//
	
	public MainOffice(int branches,int trucksForBranch) {
		MainOffice.hub=new Hub();
		packages=new ArrayList<Package>();
		
		
		
		for(int i=0;i<trucksForBranch;i++) {
			MainOffice.hub.getListTrucks().add(new StandardTruck());
		}
		
		
		
		
		
		for(int i=0;i<branches;i++) {
			MainOffice.getHub().getBranches().add(new Branch());
		}
		
		
		
		for(int i=0;i<MainOffice.hub.getBranches().size();i++) {
			
			for(int j=0;j<trucksForBranch;j++) {
				MainOffice.hub.getBranches().get(i).getListTrucks().add(new Van());
			}
			
		}
			
		
	}
	
	//Accepts as argument the number of beats that the system will perform and activates the beat (tick) this number of times.//
	public void play(int playTime) {
		System.out.println("=====================================================================START==============================================================");
		for(int i=0;i<playTime;i++)
			tick();
		System.out.println("=====================================================================STOP==============================================================");
		this.printReport();
		
	}
	
	//Prints a follow-up report for all packages in the system//
	public void printReport() {
		
		for(int i=0;i<this.getPackages().size();i++)
			for(int j=0;j<this.getPackages().get(i).getTracking().size();j++)
				System.out.println("TRACKING"+this.packages.get(i).getTracking().get(j).toString());
		
	}
	
	//Prints the value of the watch according to format//
	public String clockString() {
		if(MainOffice.clock<10)
			return "00:0"+MainOffice.clock;
		else if(MainOffice.clock<60 && MainOffice.clock>=10)
			return "00:"+MainOffice.clock;
		else if(MainOffice.clock>=60 && MainOffice.clock<600 && MainOffice.clock%60<10)
			return "0"+MainOffice.clock/60+":0"+MainOffice.clock%60;
		else if(MainOffice.clock>=60 && MainOffice.clock<600 && MainOffice.clock%60>=10)
			return "0"+MainOffice.clock/60+":"+MainOffice.clock%60;
		else if(MainOffice.clock>=600 && MainOffice.clock%60<10)
			return MainOffice.clock/60+":0"+MainOffice.clock%60;
		else 
			return MainOffice.clock/60+":"+MainOffice.clock%60;
		
		
	}
	
	public void tick() {
		
		System.out.println(this.clockString());
		
		if(MainOffice.clock%5==0) //Every 5 beats a random new package is created//
			this.addPackage();
		
		
		
		MainOffice.clock++;
		
		
		MainOffice.getHub().work();
		

		for(int i=0;i<MainOffice.getHub().getListTrucks().size();i++) {
			MainOffice.getHub().getListTrucks().get(i).work();
		} 
		for(int i=0;i<MainOffice.hub.getBranches().size();i++) {
			MainOffice.hub.getBranches().get(i).work();
			for(int j=0;j<MainOffice.hub.getBranches().get(i).getListTrucks().size();j++)
		         MainOffice.hub.getBranches().get(i).getListTrucks().get(j).work();
	    }
		
		

	}
	
	//Guerrilla package type (small / standard / non-standard), priority, sender and recipient addresses//
	public void addPackage() {
		Random type=new Random();
		Random prio=new Random();
		Random zip1=new Random();
		Random zip2=new Random();
		Random street1=new Random();
		Random street2=new Random();
		Random acknowlegde=new Random();
		Random weight=new Random();
		Random height=new Random();
		Random width=new Random();
		Random length=new Random();
			                                      
		
		int typeInt,prioInt,zipInt,streetInt,heightInt,widthInt,lengthInt,zipInt2,streetInt2;
		double weightDbl;
		boolean acknowledgeBool;
		
		typeInt=type.nextInt(3-1)+1;
		
		prioInt=prio.nextInt(Priority.class.getEnumConstants().length-0)+0;
		
		zipInt=zip1.nextInt((MainOffice.hub.getBranches().size()-1)-0)+0;
		
		streetInt=street1.nextInt(999999-100000)+100000;
		
        zipInt2=zip2.nextInt((MainOffice.hub.getBranches().size()-1)-0)+0;
		
		streetInt2=street2.nextInt(999999-100000)+100000;
		
		//Small or standard packages are delivered to a local Branch//
		
		if(typeInt==1) { 
			acknowledgeBool=acknowlegde.nextBoolean();
			SmallPackage p=new SmallPackage(Priority.class.getEnumConstants()[prioInt],new Address(zipInt,streetInt),new Address(zipInt2,streetInt2),acknowledgeBool);
			for(int i=0;i<MainOffice.hub.getBranches().size();i++) {
				if(MainOffice.hub.getBranches().get(i).getBranchId()==zipInt) {
					MainOffice.hub.getBranches().get(i).collectPackage(p);
				}
			}
			this.getPackages().add(p);
			System.out.println("Creating "+p.toString());
		}
		
		else if(typeInt==2) {
			weightDbl=weight.nextDouble()*(10-1)+1;
			
			StandardPackage p=new StandardPackage(Priority.class.getEnumConstants()[prioInt],new Address(zipInt,streetInt),new Address(zipInt2,streetInt2),weightDbl);
			
			for(int i=0;i<MainOffice.hub.getBranches().size();i++) {
				if(MainOffice.hub.getBranches().get(i).getBranchId()==zipInt) {
					MainOffice.hub.getBranches().get(i).collectPackage(p);
				}
			}
			this.getPackages().add(p);
			System.out.println("Creating "+p.toString());
		}
		
		//And non-standard packages are delivered to a sorting center.//
		else if(typeInt==3) {
			heightInt=height.nextInt(400-1)+1;
			widthInt=width.nextInt(500-1)+1;
			lengthInt=length.nextInt(1000-1)+1;
			
			NonStandardPackage p=new NonStandardPackage(Priority.class.getEnumConstants()[prioInt],new Address(zipInt,streetInt),new Address(zipInt2,streetInt2),widthInt,lengthInt,heightInt);
			
			MainOffice.hub.collectPackage(p);
			this.getPackages().add(p);
			System.out.println("Creating "+p.toString());
			
		}
		else System.out.println("ERROR: typeInt isnt 1-3");
		
		
		
			
			
		
	}

	public static int getClock() {
		return clock;
	}

	public static void setClock(int clock) {
		MainOffice.clock = clock;
	}

	public static Hub getHub() {
		return MainOffice.hub;
	}

	public void setHub(Hub hub) {
		MainOffice.hub = hub;
	}

	public ArrayList<Package> getPackages() {
		return packages;
	}

	public void setPackages(ArrayList<Package> packages) {
		this.packages = packages;
	}

	
	

}
