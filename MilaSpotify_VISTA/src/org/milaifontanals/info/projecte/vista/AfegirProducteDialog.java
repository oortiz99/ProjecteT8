/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.milaifontanals.info.projecte.vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import org.milaifontanals.info.projecte.model.Artista;
import org.milaifontanals.info.projecte.model.Producte;
import org.milaifontanals.info.projecte.persistencia.MilaSpotifyException;
import org.milaifontanals.info.projecte.persistencia.MilaSpotifyJDBC;

/**
 *
 * @author pepelu
 */
public class AfegirProducteDialog extends JDialog
{
    private DefaultListModel llistaProductes;  
    private JList JListProductes;
    private String tipus = "";
    private EscoltadoraDeBotons eb =  new EscoltadoraDeBotons();
    private JTextField nomProducte;
    private JComboBox cmbArtistes;
    private DefaultListModel llistaProductesAfegir;
    private MilaSpotifyJDBC bd;
    private List<Artista> artistes = new ArrayList();
    private List<Producte> llistaProductesDelProd = new ArrayList();
    //Falta afegir a bd per aprofitar la mateixa connexio
    public AfegirProducteDialog(Frame aFrame,String tipus ,DefaultListModel llistaProductesAfegir,MilaSpotifyJDBC bd , List<Producte> llistaProductesDelProd)
    {
        super(aFrame,"Afegir Producte",true);
        this.tipus = tipus;  
        this.llistaProductesAfegir = llistaProductesAfegir;
        this.llistaProductesDelProd = llistaProductesDelProd;
        this.bd = bd;
        this.setLayout(new BorderLayout());   
        this.addWindowListener(new TancamentFinestra());
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        
        
        
        JPanel panel_dialeg = new JPanel();
        panel_dialeg.setLayout(new BoxLayout(panel_dialeg,BoxLayout.Y_AXIS)); 
        
        JPanel panel_cm_artistes = new JPanel();
        panel_cm_artistes.setLayout(new BoxLayout(panel_cm_artistes,BoxLayout.X_AXIS));
        
        JLabel lblCmbArt = new JLabel("Artista: ");
        cmbArtistes = new JComboBox();
        cmbArtistes.setPreferredSize(new Dimension(200,20));       
        cmbArtistes.setMaximumSize(cmbArtistes.getPreferredSize());
        
        try {
            artistes = bd.getArtistes();
        } catch (MilaSpotifyException ex) {
            JOptionPane.showMessageDialog(null,
            "Motiu: "+ex.getMessage(),
            "Error en mostrar els artistes en el dialeg",
            JOptionPane.ERROR_MESSAGE);
        }
        
        cmbArtistes.addItem("");
        for(Artista a: artistes)
        {
            cmbArtistes.addItem(a);
        }
        
        panel_cm_artistes.add(lblCmbArt);
        panel_cm_artistes.add(cmbArtistes);      
        panel_cm_artistes.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));        
        panel_dialeg.add(panel_cm_artistes);
        
        JPanel panel_nom_producte = new JPanel();
        panel_nom_producte.setLayout(new BoxLayout(panel_nom_producte,BoxLayout.X_AXIS));      
        JLabel lblNomProd = new JLabel("Nom producte: ");
        nomProducte = new JTextField("",20);  
        panel_nom_producte.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0)); 
        nomProducte.setMaximumSize(nomProducte.getPreferredSize());        
        panel_nom_producte.add(lblNomProd);
        panel_nom_producte.add(nomProducte);       
        panel_dialeg.add(panel_nom_producte);
             
        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.addActionListener(eb);
        panel_dialeg.add(btnFiltrar);
              
        JPanel panProd = new JPanel();
        panProd.setBorder(new EmptyBorder(10,10,0,0));
        panProd.setLayout(new BoxLayout(panProd,BoxLayout.X_AXIS));     
        JLabel labProductes = new JLabel("Productes:  ");
        llistaProductes = new DefaultListModel();
        JListProductes = new JList(llistaProductes);
        JListProductes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JListProductes.setLayoutOrientation(JList.VERTICAL);
        JListProductes.setVisibleRowCount(-1); 
        JListProductes.setPreferredSize(new Dimension(300, 200));
        JListProductes.setMinimumSize(JListProductes.getPreferredSize());
        JListProductes.setVisible(true);
        JScrollPane scrollJListUsuaris = new JScrollPane(JListProductes,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 
        scrollJListUsuaris.setMaximumSize(JListProductes.getPreferredSize());
        panProd.add(labProductes);
        panProd.add(scrollJListUsuaris);        
        panel_dialeg.add(panProd);     
        JButton btnAfegir = new JButton("Afegir");
        btnAfegir.addActionListener(eb);
             
        
        
        panel_dialeg.add(btnAfegir);     
        this.add(panel_dialeg);
        pack();
        setVisible(true);
        
    }
    
    class EscoltadoraDeBotons implements ActionListener 
    {

        public void actionPerformed(ActionEvent e) 
        {
            String opcio = e.getActionCommand();
            switch (opcio) 
            {
                case "Filtrar":        
                    String titol = null;
                    if(nomProducte.getText().length()>0)
                        titol = nomProducte.getText();
                    
                    int art_id = -1;
                    llistaProductes.clear();
                    if(cmbArtistes.getSelectedIndex()>0)
                    {
                        Artista a = (Artista)cmbArtistes.getSelectedItem();
                        art_id = a.getId();
                    }                 
                    if(tipus.equals("Àlbum"))
                    {
                        //filtrem cançons   
                        try 
                        {
                            List<Producte> cansons = bd.getCansonsFiltre(art_id, titol);
                            for(Producte p : cansons)
                            {
                                if(llistaProductesAfegir.contains(p)==false)
                                    llistaProductes.addElement(p);

                            }                          
                        } catch (MilaSpotifyException ex) {
                            JOptionPane.showMessageDialog(null,
                           "Motiu: "+ex.getMessage(),
                           "Error en recollir les cansons del filtre",
                           JOptionPane.ERROR_MESSAGE);
                        }
                    }else
                    {
                        try 
                        {
                            List<Producte> productes = bd.getProductesFiltreDialog(art_id,titol);     
                            for(Producte p : productes)
                            {
                                if(llistaProductesDelProd.contains(p)==false){}
                                    llistaProductes.addElement(p.getPro_titol());
                            }       
                                               
                        } catch (MilaSpotifyException ex) {
                            JOptionPane.showMessageDialog(null,
                           "Motiu: "+ex.getMessage(),
                           "Error en recollir les cansons i albums del filtre",
                           JOptionPane.ERROR_MESSAGE);
                        }
                                
                    }
                break;
                
                case "Afegir":
                   //retornem el id del producte a afegir
                   if(JListProductes.getSelectedIndex()<0) 
                   {
                        JOptionPane.showMessageDialog(null,
                        "Motiu: ",
                        "Per afegir un producte tens que seleccionar un",
                        JOptionPane.ERROR_MESSAGE);
                   }else
                   {
                       Producte p = (Producte)JListProductes.getSelectedValue();
                       llistaProductesAfegir.addElement(p.getPro_titol() +" "+p.getPro_tipus());
                       llistaProductesDelProd.add(p);
                       llistaProductes.removeElement(p);
                   }
                break;
            }   
        }
    }
    
    class TancamentFinestra extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            int n = JOptionPane.showConfirmDialog(e.getComponent(), "Vol tancar el dialeg?", "Avís",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (n == JOptionPane.YES_OPTION) {
                setVisible(false);
                dispose();
            }
        }

    }
    
}
