package org.milaifontanals.info.projecte.vista;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import org.milaifontanals.info.projecte.model.Album;
import org.milaifontanals.info.projecte.model.Artista;
import org.milaifontanals.info.projecte.model.Canso;
import org.milaifontanals.info.projecte.model.Client;
import org.milaifontanals.info.projecte.model.Estil;
import org.milaifontanals.info.projecte.model.LlistaRep;
import org.milaifontanals.info.projecte.model.Producte;
import org.milaifontanals.info.projecte.model.Reproduccio;
import org.milaifontanals.info.projecte.model.TipusProducte;
import org.milaifontanals.info.projecte.persistencia.MilaSpotifyException;
import org.milaifontanals.info.projecte.persistencia.MilaSpotifyJDBC;

public class PProductes extends JPanel{
    MilaSpotifyJDBC bd = null;    
    EscoltadoraDeBotons eb = new EscoltadoraDeBotons();
    private SimpleDateFormat sdf =  new SimpleDateFormat("dd/MM/yyyy hh:mm:ss"); 
    private Frame parent;
    private JTable t_productes;
    private DefaultTableModel taulaModel;
    
    private JTextField txtFiltNom;
    private JRadioButton rbFiltreActiu;
    private JRadioButton rbFiltreInactiu;
    private JRadioButton rbTots;  
    private JCheckBox chkCanco;
    private JCheckBox chkAlbum;
    private JCheckBox chkLlista;
    
    private JRadioButton rbCrudEstatActiu;
    private JRadioButton rbCrudEstatInactiu;
    private JTextField crudNomProducte;
    private JComboBox cmbTipusProducte;
    private JComboBox cmbEstils;
    
    private JPanel panel_crud_canco;
    private JTextField txtCrudAnyCreacio;
    private JTextField txtCrudDurada;
    private JComboBox cmbCrudCanArtista;
    
    private JPanel panel_crud_album;
    
    private DefaultListModel llistaProductes; 
    private JList JListProductes;

    
    private List<Producte> mProductes = new ArrayList();
    private List<Producte> mProductesAlbLlis = new ArrayList();
    public PProductes(Frame parent)
    {
        sdf.setLenient(false);    
        this.parent = parent;
        initPanellProductes();
        
        try 
        {
            bd = new MilaSpotifyJDBC();
        } catch (MilaSpotifyException ex) {
            System.out.println("Error en crear la capa de persistencia: " + ex.getMessage());
            System.exit(1);
        }
        
        try 
        {
            mProductes = bd.getProductesFiltreMaster(null, null, null);
            posarInfoTaula(mProductes);
        } catch (MilaSpotifyException ex) {
            
            JOptionPane.showMessageDialog(null,
            "Motiu: "+ex.getMessage(),
            "Error en mostrar els productes",
            JOptionPane.ERROR_MESSAGE);
        }
        
        try {
            List<Estil> estils = bd.getEstils();
            for(Estil e : estils)
            {
                cmbEstils.addItem(e);
            }
        } catch (MilaSpotifyException ex) {
            JOptionPane.showMessageDialog(null,
            "Motiu: "+ex.getMessage(),
            "Error en mostrar els estils",
            JOptionPane.ERROR_MESSAGE);
        }
        
         try 
         {
            List<Artista> art = bd.getArtistes();
            cmbCrudCanArtista.addItem("");
            for(Artista a : art)
            {
                cmbCrudCanArtista.addItem(a);
            }
        } catch (MilaSpotifyException ex) {
            
           JOptionPane.showMessageDialog(null,
            "Motiu: "+ex.getMessage(),
            "Error en mostrar els artistes",
            JOptionPane.ERROR_MESSAGE);
        }
         
    }
    
    
    public void initPanellProductes()
    {
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        
        
        JPanel p_grid_repr = new JPanel(); 
        p_grid_repr.setLayout(new GridLayout(0, 2)); 
        Border b = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        p_grid_repr.setBorder(BorderFactory.createTitledBorder(b, "PRODUCTES"));
        
     
        JPanel panel_filtre_crud = new JPanel();  
        panel_filtre_crud.setLayout(new BoxLayout(panel_filtre_crud,BoxLayout.Y_AXIS));     
        
  
        JPanel panell_filtre_producte = new JPanel();  
        panell_filtre_producte.setLayout(new BoxLayout(panell_filtre_producte,BoxLayout.Y_AXIS));
        
        JLabel lab_filt = new JLabel("Filtres");  
        panell_filtre_producte.add(lab_filt);
          
        JPanel panel_filtre_nom = new JPanel();
        panel_filtre_nom.setLayout(new BoxLayout(panel_filtre_nom,BoxLayout.X_AXIS));
        JLabel lab_flt_nom = new JLabel("Filtra per nom: ");
        txtFiltNom = new JTextField("",20);
        txtFiltNom.setMaximumSize(txtFiltNom.getPreferredSize());
           JButton btnFiltre = new JButton("Filtre");
        btnFiltre.addActionListener(eb);
     
        JButton btnCleanFiltre = new JButton("Netejar Filtre");
        btnCleanFiltre.addActionListener(eb);
 
        panel_filtre_nom.add(lab_flt_nom);
        panel_filtre_nom.add(txtFiltNom);
        panel_filtre_nom.add(btnFiltre);
        panel_filtre_nom.add(btnCleanFiltre);
        panel_filtre_nom.setBorder(new EmptyBorder(10,10,10,10));
        
        panell_filtre_producte.add(panel_filtre_nom);
    
        
      
        JPanel panel_filtre_estat = new JPanel();
        panel_filtre_estat.setLayout(new BoxLayout(panel_filtre_estat,BoxLayout.X_AXIS));
    
        JLabel lEstat = new JLabel("Estat: ");
        panel_filtre_estat.add(lEstat);
        
      
        rbFiltreActiu = new JRadioButton("Actiu");
        rbFiltreInactiu = new JRadioButton("Inactiu");
        rbTots = new JRadioButton("Tots");
        
        rbTots.setSelected(true);
        
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbFiltreActiu);
        bg.add(rbFiltreInactiu);
        bg.add(rbTots);
        
        panel_filtre_estat.add(rbFiltreActiu);
        panel_filtre_estat.add(rbFiltreInactiu);
        panel_filtre_estat.add(rbTots);
        
        panel_filtre_estat.setBorder(BorderFactory.createEmptyBorder(0, 10, 0 ,0));
        panell_filtre_producte.add(panel_filtre_estat);
      
        JPanel panel_filtre_tipus = new JPanel();
        panel_filtre_tipus.setLayout(new BoxLayout(panel_filtre_tipus,BoxLayout.X_AXIS));
        
        JLabel lFiltreTipus = new JLabel("Tipus: ");
        
        panel_filtre_tipus.add(lFiltreTipus);
        
        chkCanco = new JCheckBox("Canço");  
        chkAlbum = new JCheckBox("Àlbum");  
        chkLlista = new JCheckBox("Llista");  
        
        chkCanco.setSelected(true);
        chkAlbum.setSelected(true);
        chkLlista.setSelected(true);
        
        panel_filtre_tipus.add(chkCanco);
        panel_filtre_tipus.add(chkAlbum);
        panel_filtre_tipus.add(chkLlista);
         
        panel_filtre_tipus.setBorder(BorderFactory.createEmptyBorder(0, 10, 0 ,0));
        panell_filtre_producte.add(panel_filtre_tipus);
        panel_filtre_crud.add(panell_filtre_producte);
        JPanel panel_crud_producte = new JPanel();
        panel_crud_producte.setLayout(new BoxLayout(panel_crud_producte,BoxLayout.Y_AXIS));
       
        JLabel lCrudProd = new JLabel("CRUD");
        panel_crud_producte.add(lCrudProd);
        
        JPanel panel_cmb_tipus = new JPanel();
        panel_cmb_tipus.setLayout(new BoxLayout(panel_cmb_tipus,BoxLayout.Y_AXIS));
        
        JLabel lCmbTipus = new JLabel("Selecciona el tipus de producte: ");
        panel_cmb_tipus.add(lCmbTipus);
        
        cmbTipusProducte = new JComboBox();
        cmbTipusProducte.addItem("Canço");
        cmbTipusProducte.addItem("Àlbum");
        cmbTipusProducte.addItem("Llista");
        
        cmbTipusProducte.setPreferredSize(new Dimension(200,20));   
        cmbTipusProducte.setMaximumSize(cmbTipusProducte.getPreferredSize());     
        
        cmbTipusProducte.addItemListener(new ItemListener() 
        {
            @Override
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) 
                {
                    Object item = event.getItem();
                    // Aqui cambiarem els panells
                    System.out.println(""+item.toString());
                    if(item.toString().equals("Canço"))
                    {
                       
                        panel_crud_album.setVisible(false);
                        panel_crud_canco.setVisible(true);
                        txtCrudDurada.setEnabled(true);
                        txtCrudAnyCreacio.setVisible(true);
                    }
                    else if(item.toString().equals("Àlbum"))
                    {
                        panel_crud_canco.setVisible(false);  
                        panel_crud_album.setVisible(true);
                        txtCrudAnyCreacio.setEnabled(true);
                        txtCrudAnyCreacio.setVisible(true);
                        txtCrudDurada.setEnabled(false);
                    }else
                    {
                        panel_crud_canco.setVisible(false);
                        panel_crud_album.setVisible(true);
                        txtCrudDurada.setEnabled(false);
                        txtCrudAnyCreacio.setEnabled(false);
                      
                    }
                }
            }     
        });
        panel_cmb_tipus.add(cmbTipusProducte);
        panel_cmb_tipus.setBorder(new EmptyBorder(10,10,0,0));
        panel_crud_producte.add(panel_cmb_tipus);
                        
        JPanel panel_crud_titol = new JPanel();
        panel_crud_titol.setLayout(new BoxLayout(panel_crud_titol,BoxLayout.X_AXIS));
        JLabel lTitolCrud = new JLabel("Titol: ");
        panel_crud_titol.add(lTitolCrud);    
        crudNomProducte = new JTextField("",20);
        crudNomProducte.setMaximumSize(crudNomProducte.getPreferredSize());
        
        panel_crud_titol.add(crudNomProducte);
        panel_crud_titol.setBorder(new EmptyBorder(10,10,0,0));
        panel_crud_producte.add(panel_crud_titol);
        JPanel panel_crud_actiu = new JPanel();
        panel_crud_actiu.setLayout(new BoxLayout(panel_crud_actiu,BoxLayout.X_AXIS));
        
        JLabel lActiuCrud = new JLabel("Estat: ");    
        rbCrudEstatActiu = new JRadioButton("Actiu");
        rbCrudEstatInactiu = new JRadioButton("Inactiu");
   
        ButtonGroup bgCrud = new ButtonGroup();
        bgCrud.add(rbCrudEstatActiu);
        bgCrud.add(rbCrudEstatInactiu);
        
        panel_crud_actiu.add(lActiuCrud);
        panel_crud_actiu.add(rbCrudEstatActiu);
        panel_crud_actiu.add(rbCrudEstatInactiu);
 
        panel_crud_actiu.setBorder(new EmptyBorder(10,10,0,0));
        
        JPanel panel_crud_estils = new JPanel();
        panel_crud_estils.setBorder(new EmptyBorder(0,20,0,0));
        
        
        panel_crud_estils.setLayout(new BoxLayout(panel_crud_estils,BoxLayout.X_AXIS));
        JLabel lEstilsCrud = new JLabel("Estil: "); 
        panel_crud_estils.add(lEstilsCrud); 
        cmbEstils = new JComboBox();
        cmbEstils.setPreferredSize(new Dimension(150,20));   
        cmbEstils.setMaximumSize(cmbEstils.getPreferredSize());     
        panel_crud_estils.add(cmbEstils);
        
        panel_crud_actiu.add(panel_crud_estils);
        
        panel_crud_producte.add(panel_crud_actiu);
      
        JPanel panel_crud_can_any_creacio = new JPanel();
        panel_crud_can_any_creacio.setLayout(new BoxLayout(panel_crud_can_any_creacio,BoxLayout.X_AXIS));
        
        JLabel lblAnyCreacio = new JLabel("Any Creacio: ");
        txtCrudAnyCreacio = new JTextField("",10);
        txtCrudAnyCreacio.setMaximumSize(txtFiltNom.getPreferredSize());
        panel_crud_can_any_creacio.add(lblAnyCreacio);
        panel_crud_can_any_creacio.add(txtCrudAnyCreacio);
        panel_crud_can_any_creacio.setBorder(BorderFactory.createEmptyBorder(10, 10, 0 ,0));
        
        JPanel panel_crud_can_durada = new JPanel();
        panel_crud_can_durada.setLayout(new BoxLayout(panel_crud_can_durada,BoxLayout.X_AXIS)); 
        JLabel lblCrudCanDurada = new JLabel("Durada :  ");
        txtCrudDurada = new JTextField("",10);
        txtCrudDurada.setMaximumSize(txtCrudDurada.getPreferredSize());
        panel_crud_can_durada.add(lblCrudCanDurada);
        panel_crud_can_durada.add(txtCrudDurada);  
        panel_crud_can_durada.setBorder(BorderFactory.createEmptyBorder(0, 10, 0 ,0));
        
        panel_crud_can_any_creacio.add(panel_crud_can_durada);  
        panel_crud_producte.add(panel_crud_can_any_creacio);
           
     
        panel_crud_canco = new JPanel();
        panel_crud_canco.setLayout(new BoxLayout(panel_crud_canco,BoxLayout.Y_AXIS));
        panel_crud_canco.setVisible(true);
       
        JPanel panel_crud_can_artistes = new JPanel();
        panel_crud_can_artistes.setLayout(new BoxLayout(panel_crud_can_artistes,BoxLayout.X_AXIS));
          
        JLabel lblCrudCanArtista = new JLabel("Artista: ");
        cmbCrudCanArtista = new JComboBox();
        cmbCrudCanArtista.setPreferredSize(new Dimension(200,20));   
        cmbCrudCanArtista.setMaximumSize(cmbTipusProducte.getPreferredSize()); 
             
        
        
        panel_crud_can_artistes.add(lblCrudCanArtista);
        panel_crud_can_artistes.add(cmbCrudCanArtista);
        panel_crud_can_artistes.setBorder(BorderFactory.createEmptyBorder(20, 10, 0 ,0));
        
        panel_crud_canco.add(panel_crud_can_artistes);
        panel_crud_producte.add(panel_crud_canco);
     
        panel_crud_album = new JPanel();
        panel_crud_album.setVisible(false);
        panel_crud_album.setLayout(new BoxLayout(panel_crud_album,BoxLayout.Y_AXIS));
        
        JPanel panProd = new JPanel();
        panProd.setBorder(new EmptyBorder(10,10,0,0));
        panProd.setLayout(new BoxLayout(panProd,BoxLayout.Y_AXIS));     
        JLabel labProductes = new JLabel("Productes asignats:   ");
        llistaProductes = new DefaultListModel();
        JListProductes = new JList(llistaProductes);
        JListProductes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JListProductes.setLayoutOrientation(JList.VERTICAL);
        JListProductes.setVisibleRowCount(-1); 
        JListProductes.setPreferredSize(new Dimension(800, 200));
        JListProductes.setMinimumSize(JListProductes.getPreferredSize());
        JListProductes.setVisible(true);
        JScrollPane scrollJListUsuaris = new JScrollPane(JListProductes,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 
        scrollJListUsuaris.setMaximumSize(JListProductes.getPreferredSize());
        panProd.add(labProductes);
        panProd.add(scrollJListUsuaris);   
    
        panel_crud_album.add(panProd);
        
        JPanel panel_btn_add_del_prod = new JPanel();
        panel_btn_add_del_prod.setBorder(BorderFactory.createEmptyBorder(10, 10, 0 ,0));
        panel_btn_add_del_prod.setLayout(new BoxLayout(panel_btn_add_del_prod,BoxLayout.X_AXIS));
        
           
        panel_crud_album.add(panel_btn_add_del_prod);     
        panel_crud_producte.add(panel_crud_album);
        
        JPanel panel_btn_crud = new JPanel();
        panel_btn_crud.setLayout(new BoxLayout(panel_btn_crud,BoxLayout.X_AXIS));
        JButton btnEliminarProducteDe = new JButton("-");
        btnEliminarProducteDe.addActionListener(eb);
        JButton btnAfegirProducteA = new JButton("+");
        btnAfegirProducteA.addActionListener(eb);
        panel_btn_add_del_prod.add(btnEliminarProducteDe);   
        panel_btn_add_del_prod.add(btnAfegirProducteA);     
        JButton btnCrearProducte = new JButton("Crear");
        JButton btnGuardarProducte = new JButton("Guardar");
        JButton btnEliminarProducte = new JButton("Eliminar");
        JButton btnImprimirJasper = new JButton("Imprimir");
       
        
        panel_btn_add_del_prod.add(btnAfegirProducteA);    
        
        
        
       
        String header_taula[] = {"Titol","Estil","Tipus","Estat"};    
        taulaModel = new DefaultTableModel(0, header_taula.length)
        {          
            @Override
            public boolean isCellEditable(int row, int column) 
            {
               return false;
            }
        } ;
        taulaModel.setColumnIdentifiers(header_taula);
        
        t_productes = new JTable(taulaModel);
        t_productes.setCellSelectionEnabled(false);
        t_productes.setRowSelectionAllowed(true);
         
        //cambiar informacio
        t_productes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = t_productes.getSelectedRow();
                if(index>=0)
                {
                    Producte p = mProductes.get(index);
                    crudNomProducte.setText(p.getPro_titol());
                    cmbEstils.setSelectedItem(p.getPro_estil());
                    mProductesAlbLlis = new ArrayList();
                    llistaProductes.clear();
                    if(p.getPro_actiu()==true)
                    {
                        rbCrudEstatActiu.setSelected(true);          
                    }else
                    {
                        rbCrudEstatInactiu.setSelected(true);
                    }                
                  
                    if(p.getPro_tipus() == TipusProducte.CANSO)
                    {
                        cmbTipusProducte.setSelectedIndex(0);         
                        Canso c = (Canso)p;
                        txtCrudDurada.setText(""+c.getDurada());
                        txtCrudAnyCreacio.setText(""+c.getAny_creacio());
                        
                        cmbCrudCanArtista.setSelectedItem(c.getInterpret());
                     
                    }else if(p.getPro_tipus() == TipusProducte.ALBUM )
                    {
                        cmbTipusProducte.setSelectedIndex(1);
                        Album a = (Album)p;
                        txtCrudDurada.setText(""+a.getDurada());
                        txtCrudAnyCreacio.setText(""+a.getAny_creacio());
                        
                        Iterator<Canso> ite = a.getCansons();
                        while(ite.hasNext())
                        {
                            Producte can = ite.next();
                            mProductesAlbLlis.add(can);
                            llistaProductes.addElement(can.getPro_titol());
                        }
                        
                    }else
                    {
                        LlistaRep r = (LlistaRep)p;
                        cmbTipusProducte.setSelectedIndex(2);
                        txtCrudDurada.setText(""+r.getDurada());
                        Iterator<Producte> ite = r.getItems();
                        while(ite.hasNext())
                        {
                            Producte pLl = ite.next();
                            mProductesAlbLlis.add(pLl);
                            llistaProductes.addElement(pLl.getPro_titol() + " "+ pLl.getPro_tipus());
                        }
                    }                  
                }
            }
        });
        JScrollPane sp=new JScrollPane(t_productes);         
        p_grid_repr.add(panel_filtre_crud);
       
        
        btnCrearProducte.addActionListener(eb);
        btnGuardarProducte.addActionListener(eb);
        btnEliminarProducte.addActionListener(eb);
        btnImprimirJasper.addActionListener(eb);
      
        panel_btn_crud.add(btnCrearProducte);
        panel_btn_crud.add(btnGuardarProducte);
        panel_btn_crud.add(btnEliminarProducte);
        panel_btn_crud.add(btnImprimirJasper);
   
        panel_btn_crud.setBorder(BorderFactory.createEmptyBorder(10, 10, 0 ,0));
        panel_crud_producte.add(panel_btn_crud);
        
        panel_filtre_crud.add(panel_crud_producte);
        p_grid_repr.add(sp);
        this.add(p_grid_repr);
    }
    
    
    private void posarInfoTaula(List<Producte> productes)
    {           
        for (Producte p : productes) {
            Object[] o = new Object[4];                                           
            o[0] = p.getPro_titol();
            o[1] = p.getPro_estil().getEst_nom();
            o[2] = p.getPro_tipus();
            o[3] = p.getPro_actiu()?"S":"N";
            taulaModel.addRow(o);
        }
        
    }
    
    
    
   

    
    class EscoltadoraDeBotons implements ActionListener 
    {

        public void actionPerformed(ActionEvent e) 
        {
            String opcio = e.getActionCommand();
           // Persona p;

            switch (opcio) 
            {
                case "Filtre":              
                    String actiu = "";
                    String titol = "";
                    List<String> tipus = new ArrayList();
                    
                    if(rbFiltreActiu.isSelected())
                    {
                      actiu = "S";
                    }else if(rbFiltreInactiu.isSelected())
                    {
                      actiu = "N";
                    }else
                    {
                      actiu = null;
                    }
                    
                    if(txtFiltNom.getText().length()<=0)
                        titol = null;
                    else
                        titol = txtFiltNom.getText();
                    
                    if(chkCanco.isSelected())
                    {
                        tipus.add("C");
                    }else
                    {
                        tipus.add("");
                    }
                    if(chkAlbum.isSelected())
                    {
                        tipus.add("A");
                    }else
                    {
                        tipus.add("");
                    }
                    if(chkLlista.isSelected())
                    {
                        tipus.add("L");
                    }else
                    {
                        tipus.add("");
                    }
                    
                    List<Producte> prods = new ArrayList();               
                    taulaModel.setRowCount(0);       
                    try 
                    {
                        mProductes = bd.getProductesFiltreMaster(titol, actiu, tipus);
                        posarInfoTaula(mProductes);
                    } catch (MilaSpotifyException ex) {
                        
                    }
                
                break;

                
                case "Netejar Filtre":
                    txtFiltNom.setText("");
                    rbTots.setSelected(true);
                    chkCanco.setSelected(true);
                    chkAlbum.setSelected(true);
                    chkLlista.setSelected(true);
                break;

                case "Crear":
                    
                {
                    try {
                        CrearPoducte();
                    } catch (MilaSpotifyException ex) {
                      
                    }
                }
                    
                break;

                
                case "Eliminar":
                    
                {
                    try {
                        EliminarProducte();
                    } catch (MilaSpotifyException ex) {
                       
                    }
                }
                break;


                case "Guardar":
                {
                    try {
                        GuardarPoducte();
                    } catch (MilaSpotifyException ex) {
                       
                    }
                }
                break;

                
                case "Imprimir":
                      Object[] options = {"Llistat de productes","Reproduccions del producte seleccionat"};
                    String s = (String)JOptionPane.showInputDialog(
                    parent,
                    "Selecciona una opcio:\n",
                    "Imprimir Jasper",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    "Llistat de productes");
                    if ((s != null) && (s.length() > 0)) 
                    {
                        if(s.equals("Reproduccions del producte seleccionat"))
                        {
                            if(t_productes.getSelectedRow()<0)
                            {
                                JOptionPane.showMessageDialog(null,
                                "Motiu: Eps, tens que seleccionar un producte",
                                "Error en imprimir el jasper",
                                JOptionPane.ERROR_MESSAGE);
                            }else
                            {
                                try
                                {
                                    Producte p = mProductes.get(t_productes.getSelectedRow());
                                    ImprimirJaspersReports ij = new ImprimirJaspersReports("jasper.properties");
                                    ij.LlistaReproduccionsProducte(p.getPro_id());
                                } catch (MilaSpotifyException ex) {
                                    JOptionPane.showMessageDialog(null,
                                    "Motiu: "+ex.getMessage(),
                                    "Error en imprimir el jasper",
                                    JOptionPane.ERROR_MESSAGE);
                                } catch (JRException ex) {
                                  
                                }   
                            }
                        }else if(s.equals("Llistat de productes"))
                        {
                            //agafem els parametres del filtre
                            String actiuImp = "";
                            String titolImp = "";
                            List<String> tipusImp = new ArrayList();
                    
                            if(rbFiltreActiu.isSelected())
                            {
                              actiuImp = "S";
                            }else if(rbFiltreInactiu.isSelected())
                            {
                              actiuImp = "N";
                            }else
                            {
                              actiuImp = null;
                            }

                            if(txtFiltNom.getText().length()<=0)
                                titolImp = null;
                            else
                                titolImp = txtFiltNom.getText();

                            if(chkCanco.isSelected())
                            {
                                tipusImp.add("C");
                            }
                            
                            if(chkAlbum.isSelected())
                            {
                                tipusImp.add("A");
                            }
                            
                            if(chkLlista.isSelected())
                            {
                                tipusImp.add("L");
                            }

                            ImprimirJaspersReports ij = null;
                            try {
                                ij = new ImprimirJaspersReports("jasper.properties");
                                ij.LlistaProductesFiltrats(titolImp, tipusImp, actiuImp);
                            } catch (MilaSpotifyException ex) 
                            {
                               JOptionPane.showMessageDialog(null,
                                "Motiu: "+ex.getMessage(),
                                "Error en imprimir el jasper",
                                JOptionPane.ERROR_MESSAGE);
                            }
                         
                        }                
                    }
                break;
                
                case "-":
                {
                    try {
                        deleteRep();
                    } catch (MilaSpotifyException ex) {
                        
                    }
                }
                break;

                
                case "+":
                Addrepalbllist();
                break;
            }   
        }

        private void GuardarPoducte() throws MilaSpotifyException {
            try
            {
                if(t_productes.getSelectedRow()<0)
                {
                    
                }else
                {
                    String errUpd = "";
                    boolean UpdateCorrecte = true;
                    int id_prod_selec = mProductes.get(t_productes.getSelectedRow()).getPro_id();
                    
                    
                    if(crudNomProducte.getText().length()<=0)
                    {
                        errUpd += "Error! El titol es obligatori\n";
                        UpdateCorrecte = false;
                    }
                    
                    if(rbCrudEstatActiu.isSelected()==false && rbCrudEstatInactiu.isSelected()==false)
                    {
                        errUpd+= "Error! Tens que seleccionar el estat del producte\n";
                        UpdateCorrecte = false;
                    }
                    
                    if(UpdateCorrecte == false)
                    {
                        throw new MilaSpotifyException(errUpd);
                    }else
                    {
                        
                        String tipusProd = (String)cmbTipusProducte.getSelectedItem();
                        Estil estilProd = (Estil)cmbEstils.getSelectedItem();
                        String titProd = crudNomProducte.getText();
                        String prodEstat = "";
                        if(rbCrudEstatActiu.isSelected())
                        {
                            prodEstat = "S";
                        }
                        else if(rbCrudEstatInactiu.isSelected())
                        {
                            prodEstat = "N";
                        }
                        switch(tipusProd)
                        {
                            case "Canço":
                                GuardarCanco(errUpd, UpdateCorrecte, titProd, prodEstat, estilProd, id_prod_selec);
                                
                                break;
                            case "Àlbum":
                                GuardarAlbum(errUpd, UpdateCorrecte, titProd, prodEstat, estilProd, id_prod_selec);
                                break;
                            case "Llista":
                                guardarllista(titProd, prodEstat, estilProd, id_prod_selec);
                                break;
                        }
                    }
                }
            }catch(MilaSpotifyException ex)
            {
                JOptionPane.showMessageDialog(null,
                        "Motiu: "+ex.getMessage(),
                        "Error en guardar el producte",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        private void CrearPoducte() throws MilaSpotifyException {
            String err = "";
            boolean correcte = true;
            try
            {
                if(crudNomProducte.getText().length()<=0)
                {
                    err += "Error! El titol es obligatori\n";
                    correcte = false;
                }
                
                if(rbCrudEstatActiu.isSelected()==false && rbCrudEstatInactiu.isSelected()==false)
                {
                    err+= "Error! Tens que seleccionar el estat del producte\n";
                    correcte = false;
                }
                
                if(correcte == false){
                    throw new MilaSpotifyException(err);
                }
                else
                {
                    String tipusProd = (String)cmbTipusProducte.getSelectedItem();
                    Estil estilProd = (Estil)cmbEstils.getSelectedItem();
                    String titProd = crudNomProducte.getText();
                    String prodEstat = "";
                    if(rbCrudEstatActiu.isSelected())
                    {
                        prodEstat = "S";
                    }else if(rbCrudEstatInactiu.isSelected())
                    {
                        prodEstat = "N";
                    }
                    
                    switch(tipusProd)
                    {
                        case "Canço":
                            CrearCanco(err, correcte, titProd, prodEstat, estilProd);
                            
                            break;
                        case "Àlbum":
                            
                            CrearAlbum(err, correcte, titProd, prodEstat, estilProd);
                            break;
                        case "Llista":
                            crearllistaRep(titProd, prodEstat, estilProd);
                            break;
                    }
                    
                }
            }
            catch(MilaSpotifyException ex)
            {
                //cualsevol error
                JOptionPane.showMessageDialog(null,
                        "Motiu: "+ex.getMessage(),
                        "Error en crear el producte",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        private void EliminarProducte() throws MilaSpotifyException {
            int index = t_productes.getSelectedRow();
            if(index<0)
            {
                JOptionPane.showMessageDialog(null,
                        "Motiu: No hi ha cap producte seleccionat",
                        "Error en eliminar el producte",
                        JOptionPane.ERROR_MESSAGE);
            }else
            {
                Producte p = mProductes.get(index);
                if(llistaProductes.size()>0)
                {
                    JOptionPane.showMessageDialog(null,
                            "Motiu: No pots eliminar un producte que tingui productes associats",
                            "Error en eliminar el producte",
                            JOptionPane.ERROR_MESSAGE);
                }else
                {
                    
                    int n = JOptionPane.showConfirmDialog(getRootPane(),"Vol eliminar aquesta reproduccio?", "Avís",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(n == JOptionPane.YES_OPTION)
                    {
                        
                        try
                        {
                            bd.delProducte(p);
                            taulaModel.removeRow(index);
                            mProductes.remove(p);
                            JOptionPane.showMessageDialog(null,
                                    "El producte s''ha eliminat correctament",
                                    "Eliminacio efectuada correctament",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } catch (MilaSpotifyException ex) {
                            JOptionPane.showMessageDialog(null,
                                    "Motiu: "+ex.getMessage(),
                                    "Error al eliminar el prodcte",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        
                    }
                    
                }
            }
        }

        private void Addrepalbllist() {
            String t = (String)cmbTipusProducte.getSelectedItem();
            AfegirProducteDialog p = new AfegirProducteDialog(parent,t,llistaProductes,bd,mProductesAlbLlis);
        }

        private void deleteRep() throws MilaSpotifyException {
            int index;
            index = JListProductes.getSelectedIndex();
            if(index<=0)
            {
                JOptionPane.showMessageDialog(null,
                        "Motiu: No hi ha cap producte seleccionat",
                        "Error en treure el producte",
                        JOptionPane.ERROR_MESSAGE);
            }else
            {
                
                
                Producte p = mProductesAlbLlis.get(index);
                int n = JOptionPane.showConfirmDialog(getRootPane(),"Vol treure aquest producte?", "Avís",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(n == JOptionPane.YES_OPTION)
                {
                    
                    JOptionPane.showMessageDialog(null,
                            "El producte s''ha tret correctament",
                            "El producte s''ha tret correctament",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }

        private void crearllistaRep(String titProd, String prodEstat, Estil estilProd) throws MilaSpotifyException {
            LlistaRep r = new LlistaRep(titProd,prodEstat,estilProd);
            if(mProductesAlbLlis.size()>0)
            {
                for(Producte p: mProductesAlbLlis)
                {
                    r.addItem(p);
                }
            }
            bd.addProducte(r);
            Object[] o = new Object[4];
            o[0] = r.getPro_titol();
            o[1] = r.getPro_estil().getEst_nom();
            o[2] = r.getPro_tipus();
            o[3] = r.getPro_actiu()?"S":"N";
            taulaModel.addRow(o);
            mProductes.add(r);
        }

        private void CrearAlbum(String err, boolean correcte, String titProd, String prodEstat, Estil estilProd) throws MilaSpotifyException {
            int anyCreacioAlb = 0;
            if(txtCrudAnyCreacio.getText().length()<=0)
            {
                err += "Error! el any de creacio del album es obligatori \n";
                correcte = false;
            }else
            {
                try
                {
                    anyCreacioAlb = Integer.parseInt(txtCrudAnyCreacio.getText());
                    if(anyCreacioAlb<1900)
                    {
                        err += "Error! L'any de creacio no pot ser mes petit que l'any 1900";
                        correcte = false;
                    }
                } catch (NumberFormatException nfe)
                {
                    err+="Error! el any de creacio ha de ser un numero! \n";
                    correcte = false;
                }
            }
            if(correcte==false)
            {
                throw new MilaSpotifyException(err);
            }
            else
            {
                Album alb = new Album(titProd,prodEstat,estilProd,anyCreacioAlb);
                
                if(mProductesAlbLlis.size()>0)
                {
                    for(Producte p: mProductesAlbLlis)
                    {
                        alb.addCanso((Canso)p);
                    }
                    
                }
                bd.addProducte(alb);
                Object[] o = new Object[4];
                o[0] = alb.getPro_titol();
                o[1] = alb.getPro_estil().getEst_nom();
                o[2] = alb.getPro_tipus();
                o[3] = alb.getPro_actiu()?"S":"N";
                taulaModel.addRow(o);
                mProductes.add(alb);
            }
        }

        private void CrearCanco(String err, boolean correcte, String titProd, String prodEstat, Estil estilProd) throws MilaSpotifyException {
            int anyCreacioCan = 0;
            int duradaCan = 0;
            if(txtCrudAnyCreacio.getText().length()<=0)
            {
                err += "Error! el any de creacio de la canço es obligatori \n";
                correcte = false;
            }else
            {
                try
                {
                    anyCreacioCan = Integer.parseInt(txtCrudAnyCreacio.getText());
                    if(anyCreacioCan<1900)
                    {
                        err += "Error! L'any de creacio no pot ser mes petit que l'any 1900";
                        correcte = false;
                    }
                } catch (NumberFormatException nfe)
                {
                    err+="Error! el any de creacio ha de ser un numero! \n";
                    correcte = false;
                }
            }
            
            if(txtCrudDurada.getText().length()<=0)
            {
                err += "Error! La durada de la canço es obligatori \n";
                correcte = false;
            }else
            {
                try
                {
                    duradaCan = Integer.parseInt(txtCrudDurada.getText());
                    if(duradaCan<=100)
                    {
                        
                        err+= "Error! la durada no pot ser mes petit o igual que 100";
                    }
                } catch (NumberFormatException nfe)
                {
                    err+="Error! La durada de la canco te que ser un numeric \n";
                    correcte = false;
                }
            }
            
            if(correcte == false)
            {
                throw new MilaSpotifyException(err);
            }else
            {
                Canso c = null;
                
                if(cmbCrudCanArtista.getSelectedIndex()>0)
                {
                    Artista a = (Artista)cmbCrudCanArtista.getSelectedItem();
                    c = new Canso(titProd,prodEstat,estilProd,anyCreacioCan,duradaCan,a);
                    
                }else
                {
                    c = new Canso(titProd,prodEstat,estilProd,anyCreacioCan,duradaCan);
                }
             
                bd.addProducte(c);
                Object[] o = new Object[4];
                o[0] = c.getPro_titol();
                o[1] = c.getPro_estil().getEst_nom();
                o[2] = c.getPro_tipus();
                o[3] = c.getPro_actiu()?"S":"N";
                taulaModel.addRow(o);
                mProductes.add(c);
            }
        }

        private void GuardarCanco(String errUpd, boolean correcteUpd, String titProd, String prodEstat, Estil estilProd, int id_prod_selec) throws MilaSpotifyException {
            boolean correcte;
            int anyCreacioCan = 0;
            int duradaCan = 0;
            if(txtCrudAnyCreacio.getText().length()<=0)
            {
                errUpd += "Error! el any de creacio de la canço es obligatori \n";
                correcte = false;
            }else
            {
                try
                {
                    anyCreacioCan = Integer.parseInt(txtCrudAnyCreacio.getText());
                    if(anyCreacioCan<1900)
                    {
                        errUpd += "Error! L'any de creacio no pot ser mes petit que l'any 1900";
                        correcteUpd = false;
                    }
                } catch (NumberFormatException nfe)
                {
                    errUpd+="Error! el any de creacio ha de ser un numero! \n";
                    correcteUpd = false;
                }
            }
            if(txtCrudDurada.getText().length()<=0)
            {
                errUpd += "Error! La durada de la canço es obligatori \n";
                correcteUpd = false;
            }else
            {
                try
                {
                    duradaCan = Integer.parseInt(txtCrudDurada.getText());
                    if(duradaCan<=100)
                    {
                        
                        correcteUpd = false;
                        errUpd+= "Error! la durada no pot ser mes petit o igual que 100";
                    }
                } catch (NumberFormatException nfe)
                {
                    errUpd+="Error! La durada de la canco te que ser un numeric \n";
                    correcte = false;
                }
            }
            if(correcteUpd == false)
            {
                throw new MilaSpotifyException(errUpd);
            }else
            {
                Canso c = null;
                
                if(cmbCrudCanArtista.getSelectedIndex()>0)
                {
                    Artista a = (Artista)cmbCrudCanArtista.getSelectedItem();
                    c = new Canso(titProd,prodEstat,estilProd,anyCreacioCan,duradaCan,a);
                }else
                {
                    c = new Canso(titProd,prodEstat,estilProd,anyCreacioCan,duradaCan);
                }
                
                c.setPro_id(id_prod_selec);
                
                bd.updProducte(c);
                mProductes.set(mProductes.indexOf(c), c);
                taulaModel.setValueAt(c.getPro_titol(),t_productes.getSelectedRow(), 0);
                taulaModel.setValueAt(c.getPro_estil().getEst_nom(),t_productes.getSelectedRow(), 1);
                taulaModel.setValueAt(c.getPro_tipus(),t_productes.getSelectedRow(), 2);
                taulaModel.setValueAt(c.getPro_actiu()?"S":"N",t_productes.getSelectedRow(), 3);
                
            }
        }

        private void GuardarAlbum(String errUpd, boolean correcteUpd, String titProd, String prodEstat, Estil estilProd, int id_prod_selec) throws MilaSpotifyException {
            int anyCreacioAlb = 0;
            if(txtCrudAnyCreacio.getText().length()<=0)
            {
                errUpd += "Error! el any de creacio del album es obligatori \n";
                correcteUpd = false;
            }else
            {
                try
                {
                    anyCreacioAlb = Integer.parseInt(txtCrudAnyCreacio.getText());
                    if(anyCreacioAlb<1900)
                    {
                        errUpd += "Error! L'any de creacio no pot ser mes petit que l'any 1900";
                        correcteUpd = false;
                    }
                } catch (NumberFormatException nfe)
                {
                    errUpd+="Error! el any de creacio ha de ser un numero! \n";
                    correcteUpd = false;
                }
            }
            if(correcteUpd==false)
            {
                throw new MilaSpotifyException(errUpd);
            }
            else
            {
                Album alb = new Album(titProd,prodEstat,estilProd,anyCreacioAlb);
                if(mProductesAlbLlis.size()>0)
                {
                    for(Producte p: mProductesAlbLlis)
                    {
                        alb.addCanso((Canso)p);
                    }
                }
                alb.setPro_id(id_prod_selec);
                bd.updProducte(alb);
                mProductes.set(mProductes.indexOf(alb), alb);
                taulaModel.setValueAt(alb.getPro_titol(),t_productes.getSelectedRow(), 0);
                taulaModel.setValueAt(alb.getPro_estil().getEst_nom(),t_productes.getSelectedRow(), 1);
                taulaModel.setValueAt(alb.getPro_tipus(),t_productes.getSelectedRow(), 2);
                taulaModel.setValueAt(alb.getPro_actiu()?"S":"N",t_productes.getSelectedRow(), 3);
            }
        }

        private void guardarllista(String titProd, String prodEstat, Estil estilProd, int id_prod_selec) throws MilaSpotifyException {
            LlistaRep r = new LlistaRep(titProd,prodEstat,estilProd);
            if(mProductesAlbLlis.size()>0)
            {
                for(Producte p: mProductesAlbLlis)
                {
                    r.addItem(p);
                }
            }
            r.setPro_id(id_prod_selec);
            bd.updProducte(r);
            mProductes.set(mProductes.indexOf(r), r);
            taulaModel.setValueAt(r.getPro_titol(),t_productes.getSelectedRow(), 0);
            taulaModel.setValueAt(r.getPro_estil().getEst_nom(),t_productes.getSelectedRow(), 1);
            taulaModel.setValueAt(r.getPro_tipus(),t_productes.getSelectedRow(), 2);
            taulaModel.setValueAt(r.getPro_actiu()?"S":"N",t_productes.getSelectedRow(), 3);
        }
    }
        
}
