/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.info.projecte.vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author Usuari
 */
public class Utils {

    static String usuari;
    static String contrasenya;
    
    static void infoError(Throwable aux) {
        do {
            if (aux.getMessage() != null) {
                System.out.println("\t" + aux.getMessage());
            }
            aux = aux.getCause();
        } while (aux != null);

    }

    static void demanaUsuariContrasenya(String desti) {
        final JFrame iFRAME = new JFrame();
        iFRAME.setAlwaysOnTop(true);

        BorderLayout layout = new BorderLayout();
        JPanel panel = new JPanel(layout);
        JPanel p = new JPanel(new BorderLayout(5, 5));
        JPanel labels = new JPanel(new GridLayout(0, 1, 2, 2));
        labels.add(new JLabel("Usuari:", SwingConstants.RIGHT));
        labels.add(new JLabel("Contrasenya:", SwingConstants.RIGHT));
        p.add(labels, BorderLayout.WEST);
        JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));

        JTextField userName = new JTextField();
        controls.add(userName);

        JPasswordField password = new JPasswordField();
        controls.add(password);
        p.add(controls, BorderLayout.CENTER);
        panel.add(p);
        JLabel baseLabel = new JLabel("<html><pre><font size=2>                  "
                + "Select <font color=red>Cancel</font> to quit</font></pre><br></html>");
        panel.add(baseLabel, BorderLayout.SOUTH);

        // Get Input from User. This will display 
        // your custom Input Dialog Box.
        int res = JOptionPane.showConfirmDialog(null, panel, "Credencials de connexió a "+desti,
                JOptionPane.OK_CANCEL_OPTION);

        // Process the result from our custom Input Dialog Box
        if (res == JOptionPane.OK_OPTION && !userName.getText().equals("")
                && !Arrays.toString(password.getPassword()).equals("")) {
            usuari = userName.getText();
            contrasenya = String.copyValueOf(password.getPassword());
        } else {
            // Do whatever you want when nothing is supplied
            // or User selectes the Cancel button or simply 
            // closes the Input Box Dialog.
            System.out.println("Programa avortat per l'usuari");
            System.exit(0);
        }
    }

}
