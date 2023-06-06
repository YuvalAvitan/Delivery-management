package GUI;


import java.awt.*;
import javax.swing.*;

import java.awt.event.*;

public class CreatePostSystemDialog extends JDialog  implements  ActionListener {
    private static final long serialVersionUID = 1L;
    private JPanel p1,p2;
    private JButton OK, Cancel;
    private JLabel lbl1, lbl2, lbl3;
    private JSlider slider1, slider2,slider3;
    private postTrackingSystem postSystem;

    public CreatePostSystemDialog(Main parent, postTrackingSystem pan, String title) {
        super(parent,title,true);
        postSystem = pan;

        setSize(600,400);

        setBackground(new Color(100,230,255));
        p1 = new JPanel();
        p2 = new JPanel();

        //layout label 1:
        p1.setLayout(new GridLayout(7,1,10,5));

        lbl1 = new JLabel("Number of branches",JLabel.CENTER);
        p1.add(lbl1);



        //creating the 1st slider:
        slider1 = new JSlider(1,10);
        slider1.setMajorTickSpacing(1);
        slider1.setMinorTickSpacing(1);
        slider1.setValue(5);
        slider1.setPaintTicks(true);
        slider1.setPaintLabels(true);

        //adding the 1st dialog to panel p1:
        p1.add(slider1);

        //label 2:
        lbl2 = new JLabel("Number of trucks per branch",JLabel.CENTER);

        //adding the
        p1.add(lbl2);

        //creating the 2nd slider:
        slider2 = new JSlider(1,10);
        slider2.setMajorTickSpacing(1);
        slider2.setValue(5);
        slider2.setMinorTickSpacing(1);
        slider2.setPaintTicks(true);
        slider2.setPaintLabels(true);

        //adding the 2nd slider to the panel
        p1.add(slider2);




        //label 3:
        lbl3 = new JLabel("Number of packages",JLabel.CENTER);

        p1.add(lbl3);

        //creating the 3rd slider:
        slider3 = new JSlider(2,20);
        slider3.setMajorTickSpacing(2);
        slider3.setValue(8);
        slider3.setMinorTickSpacing(1);
        slider3.setPaintTicks(true);
        slider3.setPaintLabels(true);

        //adding the 3rd dialog to panel p1:
        p1.add(slider3);




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
        add("South" , p2);
    }


    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == OK) {
            postSystem.createPostSystem(slider1.getValue(), slider2.getValue(), slider3.getValue());
            setVisible(false);
        }
        else
            setVisible(false);
    }
}
