package org.milaifontanals.info.projecte.vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import org.milaifontanals.info.projecte.persistencia.MilaSpotifyException;
import org.milaifontanals.info.projecte.persistencia.MilaSpotifyJDBC;

public class MilaSpotifyCon extends JFrame{  
    private JFrame ui = new JFrame("MilaSpofity");   
    MilaSpotifyJDBC bd = null;    
    EscoltadorMenu em = new EscoltadorMenu();
    
     
    public MilaSpotifyCon()
    {       
        ui.setLayout(new BorderLayout());
     
        ui.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        ui.addWindowListener(new TancamentFinestra());
        initMenuBar();        
        ui.setPreferredSize(new Dimension(1000,800));
        ui.setResizable(false);
        ui.setVisible(true);     
        
        ui.pack();
       
    }
    
    private void initMenuBar()
    { 
     
        JMenuBar menuBar = new JMenuBar();
        //menu bar artistes
        JMenu menu_artistes = new JMenu("Artistes");
        menuBar.add(menu_artistes);   
        JMenuItem paisos = new JMenuItem("Països (TODO)");
        JMenuItem artistes_sub = new JMenuItem("Artistes (TODO)");   
        menu_artistes.add(paisos);
        menu_artistes.add(artistes_sub);
        
        //menu bar cataleg
        JMenu menu_cataleg = new JMenu("Cataleg");
        menuBar.add(menu_cataleg);
        JMenuItem menu_item_estils = new JMenuItem("Estils (TODO)");
        JMenuItem menu_item_productes = new JMenuItem("Productes");
        menu_item_productes.addActionListener(em);
        menu_cataleg.add(menu_item_estils);
        menu_cataleg.add(menu_item_productes);
        
        //menu bar clients
        JMenu menu_clients = new JMenu("Clients");
        menuBar.add(menu_clients);
        JMenuItem menu_cli_paisos = new JMenuItem("Països (TODO)");
        JMenuItem menu_cli_clients = new JMenuItem("Clients (TODO)");
        JMenuItem menu_cli_eliminar = new JMenuItem("Eliminar (TODO)");
        menu_clients.add(menu_cli_paisos);
        menu_clients.add(menu_cli_clients);
        menu_clients.add(menu_cli_eliminar);
        
        //menu bar reproduccions
        JMenu menu_repr = new JMenu("Reproduccions");
        JMenuItem item_repr = new JMenuItem("Gestio Reproduccions");
        
        menu_repr.add(item_repr);
        menuBar.add(menu_repr);
              
        //menu ajuda
        JMenu menu_ajuda = new JMenu("Ajuda");
        menuBar.add(menu_ajuda);
        JMenuItem menu_ajuda_manual = new JMenuItem("Manual");
        JMenuItem menu_ajuda_sobre = new JMenuItem("Sobre MilaSpotify");
        KeyStroke f1 = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
        menu_ajuda_sobre.setAccelerator(f1);      
        menu_ajuda.add(menu_ajuda_manual);
        menu_ajuda.add(menu_ajuda_sobre);         
        ui.setJMenuBar(menuBar);
    }
        
    class TancamentFinestra extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            int n = JOptionPane.showConfirmDialog(e.getComponent(), "Vol tancar el dialog?", "Avís",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (n == JOptionPane.YES_OPTION) {
            
                System.exit(0);
            }
        }

    }
    
    class EscoltadorMenu implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) {
           switch(e.getActionCommand())
           {
              
               case "Productes":
                    ui.getContentPane().removeAll();
                    ui.getContentPane().add(new PProductes(ui)); 
                    ui.revalidate();  
               break;
           }
        }
    }   
}
