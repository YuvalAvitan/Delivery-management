package program;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;

import javax.imageio.ImageIO;
import javax.swing.*;

import components.*;
import components.Package;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.concurrent.CyclicBarrier;

public class PostSystemPanel extends JPanel implements ActionListener {
   private static final long serialVersionUID = 1L;
   private Main frame;
   private JPanel p1;
   private JButton[] b_num;
   /**
    * Adding the buttons
	"Clone Branch", "Restore", "Report"
    */
   private String[] names = {"Create system","Start","Stop","Resume","All packages info","Branch info", "Clone Branch", "Restore", "Report"};
   private JScrollPane scrollPane;
   private JScrollPane cloneScrollPane;
   private boolean isTableVisible = false;
   private boolean isTable2Visible = false;
   private int colorInd = 0;
   private boolean started = false;
   private MainOffice game = null;
   private int packagesNumber;
   private int branchesNumber;
   private int trucksNumber;
   private MainOffice beforeClone;
   private int memento = 0;

   public PostSystemPanel(Main f) {
	    frame = f;
	    isTableVisible = false;
	    setBackground(new Color(255,255,255));
	    p1=new JPanel();
		p1.setLayout(new GridLayout(1,7,0,0));
		p1.setBackground(new Color(0,150,255));
		b_num=new JButton[names.length];
		
		for(int i=0;i<names.length;i++) {
		    b_num[i]=new JButton(names[i]);
		    b_num[i].addActionListener(this);
		    b_num[i].setBackground(Color.lightGray);
		    p1.add(b_num[i]);		
		}

		setLayout(new BorderLayout());
		add("South", p1);
   }	
   
   
   public void createNewPostSystem(int branches, int trucks, int packages) {
	   if (started) return;
	   game = new MainOffice(branches, trucks, this, packages);
	   packagesNumber = packages;
	   trucksNumber = trucks;
	   branchesNumber = branches;
	   
	   repaint();
   }
   

   public void paintComponent(Graphics g) {
	   	super.paintComponent(g);	
	   	   	
	   	if (game==null) return;
	   	
	   	Hub hub = game.getHub();
	   	ArrayList<Branch> branches = hub.getBranches();
	   	
	   	int offset = 403/(branchesNumber-1);
	   	int y=100;
	   	int y2=246;
	   	int offset2 = 140/(branchesNumber-1);
	   	
	   	g.setColor(new Color(0,102,0));
	   	g.fillRect(1120, 216, 40, 200);
	   	
	   	
	   	for (Branch br: branches) {
	   		br.paintComponent(g,y,y2);
	   		y+=offset;
	   		y2+=offset2;
	   	}
	   	
	   	
	   	int x = 150;
	   	int offset3 = (1154-300)/(packagesNumber-1);
	   	
	   	for (Package p: game.getPackages()) {
	   		p.paintComponent(g,x,offset);
	   		x+=offset3;
	   	}
	   	
	   	
		for (Branch br: branches) {
			for(Truck tr: br.getTrucks()) {
				tr.paintComponent(g);
			}
	   	}
		
		for(Truck tr: hub.getTrucks()) {
			tr.paintComponent(g);
		}
   	
   }
   
   
   
   public void setColorIndex(int ind) {
	   this.colorInd = ind;
	   repaint();
   }
   
   
   public void setBackgr(int num) {
	   switch(num) {
	   case 0:
		   setBackground(new Color(255,255,255));
		   break;
	   case 1:
		   setBackground(new Color(0,150,255));
		   break;

	   }
	   repaint();
   }
   
   
   
   public void add(){
	   CreatePostSystemlDialog dial = new CreatePostSystemlDialog(frame,this,"Create post system");
	   dial.setVisible(true);
   }   

   public void start() {
	   if (game==null || started) return;
	   Thread t = new Thread(game);
	   started = true;
	   t.start();
   }
   
	public void resume() {
		if (game == null) return;
		game.setResume();
   }

 	public void stop() {
 		if (game == null) return;
	    game.setSuspend();
 	}

 	
    public void info() {
 	   if (game == null || !started) return;
	   if(isTable2Visible == true) {
		   scrollPane.setVisible(false);
		   isTable2Visible = false;
	   }
 	   if(isTableVisible == false) {
 			 int i=0;
 			 String[] columnNames = {"Package ID", "Sender", "Destination", "Priority", "Staus"};
 			 ArrayList<Package> packages = game.getPackages();
 			 String [][] data = new String[packages.size()][columnNames.length];
 			 for(Package p : packages) {
 		    	  data[i][0] = ""+p.getPackageID();
 		    	  data[i][1] = ""+p.getSenderAddress();
 		    	  data[i][2] = ""+p.getDestinationAddress();
 		    	  data[i][3] = ""+p.getPriority();
 		    	  data[i][4] = ""+p.getStatus();
 		    	  i++;
 			 }
 			 JTable table = new JTable(data, columnNames);
 		     scrollPane = new JScrollPane(table);
 		     scrollPane.setSize(450,table.getRowHeight()*(packages.size())+24);
 		     add( scrollPane, BorderLayout.CENTER );
 		     isTableVisible = true;
 	   }
 	   else
 		   isTableVisible = false;
 	   
 	   scrollPane.setVisible(isTableVisible);
       repaint();
    }
    
   
   public void branchInfo() {
	   if (game == null || !started) return;
   
	   if(scrollPane!=null) scrollPane.setVisible(false);
	   isTableVisible = false;
	   isTable2Visible = false;
	   String[] branchesStrs = new String[game.getHub().getBranches().size()+1];
	   branchesStrs[0] = "Sorting center";
	   for(int i=1; i<branchesStrs.length; i++)
		   branchesStrs[i] = "Branch "+i;
	   JComboBox cb = new JComboBox(branchesStrs);
	   String[] options = { "OK", "Cancel" };
	   String title = "Choose branch";
	   int selection = JOptionPane.showOptionDialog(null, cb, title,
	        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
	        options[0]);

	   if (selection==1) return;
	   //System.out.println(cb.getSelectedIndex());
	   if(isTable2Visible == false) {
			 int i=0;
			 String[] columnNames = {"Package ID", "Sender", "Destination", "Priority", "Staus"};
			 Branch branch;
			 List<Package> packages = null;
			 int size=0;
			 if(cb.getSelectedIndex()==0) {
				 packages = game.getHub().getPackages();
				 size = packages.size();
			 }
			 else {
				 packages = game.getHub().getBranches().get(cb.getSelectedIndex()-1).getPackages();
				 size = packages.size();
				 int diff = 0;
				 for(Package p : packages) {
					 if (p.getStatus()==Status.BRANCH_STORAGE) {
						 diff++;
					 }
				 }
				 size = size - diff/2;
			 }
			 String [][] data = new String[size][columnNames.length];
			 for(Package p : packages) {
				 boolean flag = false;
				 for(int j=0; j<i; j++)
					 if (data[j][0].equals(""+p.getPackageID())) {
						 flag = true;
						 break;
					 }
				 if (flag) continue;
		    	 data[i][0] = ""+p.getPackageID();
		    	 data[i][1] = ""+p.getSenderAddress();
		    	 data[i][2] = ""+p.getDestinationAddress();
		    	 data[i][3] = ""+p.getPriority();
		    	 data[i][4] = ""+p.getStatus();
		    	 i++;
			 }
			 JTable table = new JTable(data, columnNames);
		     scrollPane = new JScrollPane(table);
		     scrollPane.setSize(450,table.getRowHeight()*(size)+24);
		     add( scrollPane, BorderLayout.CENTER );
		     isTable2Visible = true;
	   }
	   else
		   isTable2Visible = false;
	   
	   scrollPane.setVisible(isTable2Visible);
       repaint();
   }


	/**
	 * this function is for the memento we clone a branch from the list of branches
	 * according to the user's choice if branch 1 clone branch 1 and exc
	 */
	public void cloneBranch(){
	   if (game == null || started) return;

//	   if(cloneScrollPane != null) cloneScrollPane.setVisible(false);
	   String[] branchesStrs = new String[game.getHub().getBranches().size()+1];
	   branchesStrs[0] = "Sorting center";
	   for(int i=1; i<branchesStrs.length; i++)
		   branchesStrs[i] = "Branch "+i;
	   JComboBox cb = new JComboBox(branchesStrs);
	   String[] options = { "Clone", "Cancel" };
	   String title = "Choose branch to clone";
	   int selection = JOptionPane.showOptionDialog(null, cb, title,
	        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
	        options[0]);
	   
	   if (selection == 0) {
		   memento++;
		   branchesNumber++;
		   game.addBranch();
	   }
	   else {
		   return;
	   }
   }

	/**
	 * the function restore is to restore to last time we did clone to a branch
	 * we know where we added the branch we know everything about it
	 * so we could just easily remove it from the system and restore
	 * the system to last checkpoint
	 */
	private void restore() {
		if (memento != 0) {
			game.removeBranch();
			branchesNumber--;
			memento--;
		}
	}

	/**
	 * this function is opening the file tracking.txt we build
	 * the path for the file need to be changed because its work
	 * only on my computer
	 * so change the path i wrote: "C:\Users\noam5\Desktop\HW3\tracking.txt"
	 * to where your project is in your computer
	 * @throws IOException
	 */
	private void report() throws IOException {
   	if (game == null || !started) return;

	try
	{
		//constructor of file class having file as argument
		File file = new File("C:\\Users\\noam5\\Desktop\\HW3\\tracking.txt");
		//check if Desktop is supported by Platform or not
		if(!Desktop.isDesktopSupported())
		{
			System.out.println("not supported");
			return;
		}
		Desktop desktop = Desktop.getDesktop();
		//checks file exists or not
		if(file.exists())
			//opens the specified file
			desktop.open(file);
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
}

   
   public void destroy(){  	        
      System.exit(0);
   }
   
   
   public void actionPerformed(ActionEvent e) {
	if(e.getSource() == b_num[0]) 
		add();
	else if(e.getSource() == b_num[1]) 
		start();
	else if(e.getSource() == b_num[2])  
		stop();
	else if(e.getSource() == b_num[3])  
		resume(); 
	else if(e.getSource() == b_num[4])  
		info();
	else if(e.getSource() == b_num[5])  
		branchInfo();
	else if(e.getSource() == b_num[6])
		cloneBranch();
	else if(e.getSource() == b_num[7])
		restore();
	else if(e.getSource() == b_num[8]) {
		try {
			report();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
   }

}