package GUI;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Main extends JFrame implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private postTrackingSystem panel;


    public static void main(String[]args) {
        Main fr = new Main();
        fr.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        fr.setSize(845,715);
        fr.setVisible(true);
    }

    public Main() {
        super("Post tracking system");
        panel = new postTrackingSystem(this);
        add(panel);
        panel.setVisible(true);

    }


    public void actionPerformed(ActionEvent e) {

    }

}
