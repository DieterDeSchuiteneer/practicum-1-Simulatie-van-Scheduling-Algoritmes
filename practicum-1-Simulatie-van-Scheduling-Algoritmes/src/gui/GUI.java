package gui;

import logic.Logic;

import javax.swing.*;
import java.awt.event.*;

public class GUI extends JDialog {
    private Logic logic;
    private JPanel contentPane;
    private JButton buttonRun;
    private JButton buttonCancel;
    private JButton buttonFileOpen;
    private JLabel textFieldFile;


    public GUI() {
        logic = new Logic();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonRun);

        buttonRun.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onRun();
            }
        });
        buttonFileOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onFileOpen();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onRun() {
        if(logic.getFile().equals(""))
        System.out.println("No file");
        else
            System.out.println("run");
    }

    private void onFileOpen() {
        JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(contentPane);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        GUI dialog = new GUI();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
