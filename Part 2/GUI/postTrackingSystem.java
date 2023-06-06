package GUI;

import components.*;
import components.Package;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;

public class postTrackingSystem extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private Main frame;
    private JPanel p1;
    private JButton[]  buttons;
    private String[] names = {"Create system","Start","Stop","Resume","All packages info", "Branch info"};
    private static boolean flag = false;
    private static boolean flag1 = true;
    private int pnum = 0;
    private boolean started = false;
    public static int branchesNumber;
    private ArrayList<Line> lines = new ArrayList<Line>();
    public JPanel HubPanel;
    public int numberOfPackages;
    public int numberOfTrucks;
    static public int numberOfBranches;
    public JPanel packagesPanel[];
    public MainOffice mainOffice;
    public Graphics ourG;
    private boolean isTableVisible;
    private JScrollPane scrollPane;
    private BranchInfoDialog BranchInfoDialog;
    private boolean createdBranchTable = false;






    public postTrackingSystem(Main f) {
        frame = f;

        setBackground(new Color(255,255,255));

        //initialized the panel 1:
        p1=new JPanel();

        p1.setLayout(new GridLayout(1,7,0,0));
        p1.setBackground(new Color(0,150,255));

        //initialized the buttons
        buttons = new JButton[names.length];


        //adding the buttons to the main frame
        for(int i=0;i<names.length;i++) {
            buttons[i] = new JButton(names[i]);
            buttons[i].addActionListener(this);
            buttons[i].setBackground(Color.lightGray);
            p1.add(buttons[i]);
        }

        setLayout(new BorderLayout());
        add("South", p1);
        repaint();
    }


    public void createPostSystem(int numberOfBranches, int numberOfTrucks, int numberOfPackages) {

        this.numberOfPackages = numberOfPackages;
        this.numberOfBranches = numberOfBranches;
        this.numberOfTrucks = numberOfTrucks;

        branchesNumber = numberOfBranches;

        isTableVisible = false;

        this.mainOffice = null;
        this.HubPanel = null;
//        for(int i=0; i<this.numberOfBranches; i++)
//        {
//            this.mainOffice.getHub().getBranches().get(i).setListTrucks(null);
//        }
//        this.mainOffice.getHub().setBranches(null);
//        this.mainOffice.getHub().setListTrucks(null);
//        this.mainOffice.setHub(null);



        this.mainOffice = new MainOffice(numberOfBranches, numberOfTrucks, numberOfPackages);

        mainOffice.setPanel(this);
        started = false;

        this.HubPanel = new JPanel();
        this.setLayout(null);
        HubPanel.setBackground(this.mainOffice.getHub().getBranches().get(0).DARK_GREEN);
        HubPanel.setBounds(750,200,40,200);
        this.add(HubPanel);


        repaint();
    }


    /**
     * The function draws components into the container
     * @param g is for the model graphics to draw on the container
     */
    public void paintComponent(Graphics g) {
    
        this.ourG = g;
        super.paintComponent(g);
        if (mainOffice==null) return;

        //drawing the branches:
        for (int i = 0; i<numberOfBranches; i++) {
            this.mainOffice.getHub().getBranches().get(i).drawBranch(g, i);
        }

        //creating the lines between the hub to the branches:
        this.lines = new ArrayList<>();
        for(int i=0; i<numberOfBranches; i++)
        {
            //creating the line:
            Line l = new Line(this.mainOffice.getHub().getBranches().get(i).getX()+40,this.mainOffice.getHub().getBranches().get(i).getY()+15,HubPanel.getX(),(int) ( HubPanel.getY()+20*i-numberOfBranches*2 +30));

            //adding the line to the list of the lines:
            lines.add(l);

            //adding the line to the branch so i could use it later:
            this.mainOffice.getHub().getBranches().get(i).setLineToHub(l);
        }

        //drawing the lines between the hub and the branches
        g.setColor(this.mainOffice.getHub().getBranches().get(0).DARK_GREEN);
        for (int i=0; i<this.lines.size(); i++)
        {
            lines.get(i).drawLine(g);
        }

        //drawing the packages and the lines between the sender and dest and Hub
        if(flag)
        {
            this.mainOffice.drawPackages(g);
            for(int i = 0; i<this.mainOffice.getPackages().size(); i++)
            {
                if(this.mainOffice.getPackages().get(i) instanceof NonStandardPackage)
                {
                    g.setColor(Color.RED);
                    this.mainOffice.getPackages().get(i).drawLines(g);
                }
                else if(this.mainOffice.getPackages().get(i) instanceof StandardPackage || this.mainOffice.getPackages().get(i) instanceof SmallPackage)
                {
                    g.setColor(Color.BLUE);
                    this.mainOffice.getPackages().get(i).drawLines(g);
                }
            }
        }



        JTextField numberOfPackages;

        //drawing the trucks on the roads:
        if(this.mainOffice.getHub().getListTrucks() != null)
        {
            for (int i=0; i<this.mainOffice.getHub().getListTrucks().size(); i++)
            {
                if(this.mainOffice.getHub().getListTrucks().get(i).getTruckPaint() != null)
                {
                    //drawing the standard truck
                    this.mainOffice.getHub().getListTrucks().get(i).getTruckPaint().drawTruck(g);

                    //adding the JTextField to the truck
                    if(this.mainOffice.getHub().getListTrucks().get(i) instanceof StandardTruck && this.mainOffice.getHub().getListTrucks().get(i).getPackages() != null)
                    {
                        Truck t = this.mainOffice.getHub().getListTrucks().get(i);
                        if( ((StandardTruck) t).getDestination() instanceof Hub)
                        {
                            String packagesNum = String.valueOf(this.mainOffice.getHub().getListTrucks().get(i).pnum);
                            g.drawString(packagesNum, t.getTruckPaint().getX(), t.getTruckPaint().getY()-10);
                        }


                    }


                }
            }
        }
        if(this.mainOffice.getHub().getBranches() != null)
        {
            for(int i=0; i<this.mainOffice.getHub().getBranches().size(); i++)
            {
                ArrayList<Truck> trucksList = this.mainOffice.getHub().getBranches().get(i).getListTrucks();

                if(trucksList != null)
                {
                    for(int j = 0; j<trucksList.size(); j++)
                    {
                        if(trucksList.get(j).getTruckPaint() != null && trucksList.get(j).isAvailable() == false)
                        {
                            trucksList.get(j).getTruckPaint().drawTruck(g);
                        }
                    }
                }
            }
        }




        repaint();
    }

    public void add(){
        CreatePostSystemDialog dial = new CreatePostSystemDialog(frame,this,"Create post system");
        dial.setVisible(true);
    }


    public void start(Graphics g)
    {
        if (mainOffice == null || started) return;
        flag = true;
        this.mainOffice.drawPackages(g);
        started = true;
        Thread t = new Thread(mainOffice);
        t.start();


        MainOffice m = mainOffice;
        new Thread()
        {
            public void run()
            {

        		//mainOffice.play();
                while (true)
                {
                    m.getPanel().repaint();
                }
            }
        }.start();


        repaint();
    }




     public void stop() {
        if (mainOffice == null) return;
        mainOffice.setSuspend();
    }

    public void allPackagesInfo()
    {
        if (mainOffice == null) return;
        if(isTableVisible == false) {
            int counter = 0;
            String[] columnNames = {"Package ID", "Sender", "Destination","Priority","Status"};
            ArrayList<Branch> branches = mainOffice.getHub().getBranches();
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            if(branches != null)
            {
                for(Branch b: branches)
                {
                    ArrayList<Package> packages = b.getPackages();
                    for(Package p : packages)
                    {
                        counter++;
                        Object[] obj = {p.getPackageID(), p.getSenderAddress(), p.getDestinationAddress(), p.getPriority(), p.getStatus()};
                        tableModel.addRow(obj);
                    }
                }
            }

            JTable table = new JTable(tableModel);
            scrollPane = new JScrollPane(table);
            scrollPane.setSize(450,table.getRowHeight()*counter+22);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            add( scrollPane, BorderLayout.CENTER );

            isTableVisible = true;
        }
        else
            isTableVisible = false;

        scrollPane.setVisible(isTableVisible);
        repaint();
    }

    public void branchInfo()
    {
        if(createdBranchTable == false)
        {
            BranchInfoDialog = new BranchInfoDialog(this.frame,this.mainOffice,this,"Choose branch");
            this.createdBranchTable = true;
        }
        BranchInfoDialog.setVisible(true);
    }


    public synchronized void resume() {
        if (mainOffice == null) return;
        mainOffice.setResume();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttons[0])
            add();
        else if (e.getSource() == buttons[1])
            start(this.ourG);
        else if(e.getSource() == buttons[2])
            stop();
        else if(e.getSource() == buttons[3])
            resume();
        else if(e.getSource() == buttons[4])
            allPackagesInfo();
        else if(e.getSource() == buttons[5])
            branchInfo();
    }
}