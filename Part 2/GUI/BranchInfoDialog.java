package GUI;

import components.Branch;
import components.MainOffice;
import components.Package;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;

public class BranchInfoDialog extends JDialog implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JPanel p1,p2;
    private JButton OK,Cancel;
    private int START_INDEX = 0;
    private MainOffice mainOffice;
    private JComboBox<String> branches;
    private boolean isTableVisible;
    private JScrollPane scrollPane;
    private JPanel mainPanel;
    final static String COMBO_BOX_CHANGED_COMMAND = "comboBoxChanged";

    public BranchInfoDialog(Main parent,MainOffice MO,JPanel panel,String title)
    {
        super(parent,title,true);
        this.mainOffice = MO;
        this.mainPanel = panel;

        isTableVisible = false;

        setSize(300,150);
        setBackground(new Color(255,255,255));
        p1 = new JPanel();
        p2 = new JPanel();

        p1.setLayout(new GridLayout(1,1,1,1));

        int NOB = this.mainOffice.getHub().getBranches().size();
        String branchesNames[] = new String[NOB+1];

        branchesNames[0] = "Sorting center";
        for(int i=1; i<branchesNames.length; i++)
        {
            branchesNames[i] = "Branch " + String.valueOf(i);
        }
        branches = new JComboBox<>(branchesNames);
        branches.setSelectedIndex(START_INDEX);
//        branches.addActionListener(this);
        branches.setBorder(BorderFactory.createEmptyBorder(7,10,0,10));
        p1.add(branches);


        p2.setLayout(new GridLayout(1,2,5,5));

        //creating the button 'OK'
        OK = new JButton("OK");

        //setting the action for this button
        OK.addActionListener(this);

        OK.setBackground(Color.lightGray);

        //adding the button:
        p2.add(OK);

        Cancel=new JButton("Cancel");
        Cancel.addActionListener(this);
        Cancel.setBackground(Color.lightGray);

        //adding the Cancel button
        p2.add(Cancel);

        setLayout(new BorderLayout());
        add("North" , p1);
        OK.setBounds(40, 100, 100, 60);
        Cancel.setBounds(40, 100, 100, 60);
        add("South",p2);

    }

    public void branchTable(int branchNumber)
    {
        if (mainOffice == null) return;
        if(isTableVisible == false) {
            int counter = 0;
            String[] columnNames = {"Package ID", "Sender", "Destination","Priority","Status"};
            Branch branch = mainOffice.getHub().getBranchByID(branchNumber);
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            if(branch != null)
            {
                ArrayList<Package> packages = branch.getPackages();
                for(Package p : packages)
                {
                    counter++;
                    Object[] obj = {p.getPackageID(), p.getSenderAddress(), p.getDestinationAddress(), p.getPriority(), p.getStatus()};
                    tableModel.addRow(obj);
                }
            }

            JTable table = new JTable(tableModel);
            scrollPane = new JScrollPane(table);
            scrollPane.setSize(450,table.getRowHeight()*counter+22);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            mainPanel.add( scrollPane, BorderLayout.CENTER );

            isTableVisible = true;
        }
        else
            isTableVisible = false;

        scrollPane.setVisible(isTableVisible);
        repaint();

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == OK)
        {
            int a = this.branches.getSelectedIndex();
            branchTable(a);
            setVisible(false);
        }
        else
            setVisible(false);
    }
}
