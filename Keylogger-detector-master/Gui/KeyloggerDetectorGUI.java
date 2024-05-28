package Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KeyloggerDetectorGUI extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JButton startButton, exitButton;
    private JCheckBox autoDeleteCheckBox;

    public  boolean startDetect = false;

    public boolean autoDelete = false;
    public KeyloggerDetectorGUI() {
        setTitle("Keylogger-detector-master");
        setSize(500, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("Welcome to use Keylogger-detector-master. Please click start button to detect.");
        panel.add(label);

        autoDeleteCheckBox = new JCheckBox("Auto Delete");
        panel.add(autoDeleteCheckBox);

        startButton = new JButton("Start");
        startButton.addActionListener(this);
        panel.add(startButton);

        exitButton = new JButton("Exit");
        exitButton.addActionListener(this);
        panel.add(exitButton);

        add(panel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            // Start monitoring
            startDetect = true;
            System.out.println("Start monitoring...");
        } else if (e.getSource() == exitButton) {
            // Exit application
            System.exit(0);
        }
        if (e.getSource() == autoDeleteCheckBox){
            if (autoDelete=false)
            {
                autoDelete=true;
            }else autoDelete=false;
        }
    }

}