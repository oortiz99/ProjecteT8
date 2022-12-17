
package org.milaifontanals.info.projecte.persistencia;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.milaifontanals.info.projecte.model.Album;
import org.milaifontanals.info.projecte.model.Artista;
import org.milaifontanals.info.projecte.model.ArtistaIndividual;
import org.milaifontanals.info.projecte.model.Canso;
import org.milaifontanals.info.projecte.model.Client;
import org.milaifontanals.info.projecte.model.Estil;
import org.milaifontanals.info.projecte.model.Grup;
import org.milaifontanals.info.projecte.model.LlistaRep;
import org.milaifontanals.info.projecte.model.Pais;
import org.milaifontanals.info.projecte.model.Producte;
import org.milaifontanals.info.projecte.model.Reproduccio;
import org.milaifontanals.info.projecte.model.TipusArtista;
import org.milaifontanals.info.projecte.model.TipusProducte;


public class MilaSpotifyJDBC {

    private Connection conn;
    private PreparedStatement qAddReproduccio;
    private PreparedStatement qUpdateReproduccio;
    private PreparedStatement qDeleteReproduccio;
    private PreparedStatement qGetPais;
    private PreparedStatement qGetClient;
    private PreparedStatement qGetEstil;
     
    private PreparedStatement qGetProducte;
    private PreparedStatement qGetCanso;
    private PreparedStatement qGetAlbum;
    private PreparedStatement qGetReproduccions;
        
    private PreparedStatement qGetProductesFiltre;
    private PreparedStatement qGetCansonsAlbum;
    private PreparedStatement qGetProducteLlista;
    
    private PreparedStatement qAddProducte;
    private PreparedStatement qAddProducteCanco;
    private PreparedStatement qAddProducteAlbum;
    private PreparedStatement qAddProducteLlista;
    
   
    
    private PreparedStatement qDeleteProducte;
    private PreparedStatement qDelProducteCanco;
    private PreparedStatement qDelProducteAlbum;
    private PreparedStatement qDelProducteLlista;
    
     private PreparedStatement qAddAlbumContingut;
    private PreparedStatement qDelAlbumContingut;
    private PreparedStatement qAddLlistaContingut;
    private PreparedStatement qDelLlistaContingut;
    
     private PreparedStatement qUpdProducte;
    private PreparedStatement qUpdProducteCanco;
    private PreparedStatement qUpdProducteAlbum;
   
      private PreparedStatement qGetRefReproduccions;
    private PreparedStatement qGetAlbCon;
    private PreparedStatement qGetRefLlistaCon;
    
    
    
    
    private PreparedStatement qGetArtInd;
    private PreparedStatement qGetArtGrup;
    private PreparedStatement qGetArtistaFromId;
    
    private PreparedStatement qGetCansonsFiltreDialog;
    private PreparedStatement qGetProductesFiltreDialog;
    private DateFormat sdf =  new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
  

    public MilaSpotifyJDBC() throws MilaSpotifyException {
        this("milaspotify.properties");
    }

    public MilaSpotifyJDBC(String nomFitxerPropietats) throws MilaSpotifyException {
        sdf.setLenient(false);
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(nomFitxerPropietats));
            String[] claus = {"url", "user", "password"};
            String[] valors = new String[3];
            for (int i = 0; i < claus.length; i++) {
                valors[i] = props.getProperty(claus[i]);
                if (valors[i] == null || valors[i].isEmpty()) {
                    throw new RuntimeException("L'arxiu " + nomFitxerPropietats + " no troba la clau " + claus[i]);
                }
            }
            conn = DriverManager.getConnection(valors[0], valors[1], valors[2]);
            conn.setAutoCommit(false);
        } catch (IOException ex) {
            throw new RuntimeException("Problemes en recuperar l'arxiu de configuració " + nomFitxerPropietats + "\n" + ex.getMessage());
        } catch (SQLException ex) {
            throw new RuntimeException("No es pot establir la connexió.\n" + ex.getMessage());
        }
       
       
        String inst = null;
        try {
            inst = "insert into REPRODUCCIO (rep_id_client,rep_moment_temporal,rep_id_producte) values (?,?,?)";
            qAddReproduccio = conn.prepareStatement(inst);
            inst = "UPDATE REPRODUCCIO SET rep_id_client = ? , rep_id_producte = ? , rep_moment_temporal = ? WHERE rep_id_client = ? and rep_moment_temporal = ? ";
            qUpdateReproduccio = conn.prepareStatement(inst);
            inst = "DELETE FROM REPRODUCCIO WHERE rep_id_client = ? and rep_moment_temporal = ?";
            qDeleteReproduccio = conn.prepareStatement(inst);
            inst = "SELECT pai_codi_iso, pai_nom FROM Pais where pai_codi_iso = ?";
            qGetPais = conn.prepareStatement(inst);
            inst = "SELECT cli_email,cli_nom,cli_cognoms,cli_data_naixement,cli_c_postal,cli_dom1,cli_poblacio,cli_pais FROM CLIENT where cli_id = ?";
            qGetClient = conn.prepareStatement(inst);
            inst = "SELECT est_nom from ESTIL where est_id = ?";     
            qGetEstil = conn.prepareStatement(inst);
            inst = "SELECT pro_titol,pro_actiu,pro_estil,pro_tipus from PRODUCTE where pro_id = ?";
            qGetProducte = conn.prepareStatement(inst);
            inst = "SELECT can_durada,can_any_creacio from CANCO where can_id = ?";
            qGetCanso = conn.prepareStatement(inst);
            inst = "SELECT alb_any_creacio from ALBUM where alb_id = ?";
            qGetAlbum = conn.prepareStatement(inst);
            
            inst = "select * from reproduccio repr join producte pro on repr.rep_id_producte = pro.pro_id " +
            " where (? = -1 or repr.rep_id_client = ?) and (? is null or upper(pro.pro_titol) like ?) and " +
            " (? is null or repr.rep_moment_temporal >= ?) and (? is null or repr.rep_moment_temporal <= ?) order by repr.rep_moment_temporal desc";
            qGetReproduccions = conn.prepareStatement(inst);
            
            
      
            inst = "select * from producte " +
            "where (pro_tipus IN (?,?,?)) and (? is null or upper(pro_titol) like ?) and (? is null or pro_actiu = ?)";
            qGetProductesFiltre = conn.prepareStatement(inst);
             
            inst = "select p.pro_id,p.pro_titol,p.pro_estil,p.pro_tipus,p.pro_actiu from producte p " +
                    "join album_contingut ac on p.pro_id = ac.alc_id_canco " +
                    "where ac.alc_id_album = ?";
            qGetCansonsAlbum = conn.prepareStatement(inst);
            
            inst = "select p.pro_id,p.pro_titol,p.pro_estil,p.pro_tipus,p.pro_actiu "
                    + "from llista_contingut llc join producte p on p.pro_id = llc.LIC_ID_PRODUCTE "
                    + "where llc.LIC_ID_LLISTA = ?";
            qGetProducteLlista = conn.prepareStatement(inst);
            
            inst = "select *  from ART_IND where ind_id = ?";        
            qGetArtInd = conn.prepareStatement(inst);
            
            inst = "select * from ART_GRUP where GRP_ID = ?";
            qGetArtGrup = conn.prepareStatement(inst);
            
            inst = "select * from artista where art_id = ?";        
            qGetArtistaFromId = conn.prepareStatement(inst);
            
            inst = "select p.pro_id,p.pro_titol,p.pro_estil,p.pro_actiu from producte p join canco c on p.pro_id = c.can_id "
                    + "where (? is null or upper(p.pro_titol) like ?) and (? = -1 or c.can_interpret = ?) and pro_tipus = 'C' ";
            
            qGetCansonsFiltreDialog = conn.prepareStatement(inst);
            
            inst = "select p.pro_id , p.pro_tipus from producte p join canco c on p.pro_id = c.can_id "
                    + "where (? is null or upper(p.pro_titol) like ?) and (? = -1 or c.can_interpret = ?) and pro_tipus IN ('C','A')";
            qGetProductesFiltreDialog = conn.prepareStatement(inst);
            
            inst = "delete from producte where pro_id = ?";
            qDeleteProducte = conn.prepareStatement(inst);
            inst = "delete from canco where can_id = ?";
            qDelProducteCanco = conn.prepareStatement(inst);
            inst = "delete from album where alb_id = ?";
            qDelProducteAlbum = conn.prepareStatement(inst);
            inst = "delete from llista where lis_id = ?";
            qDelProducteLlista = conn.prepareStatement(inst);
             inst = "update producte set pro_titol = ? , pro_actiu = ? , pro_estil = ? , pro_tipus = ? where pro_id = ?";
            qUpdProducte = conn.prepareStatement(inst);          
            inst = "update canco set can_any_creacio = ? , can_interpret = ? , can_durada = ? where can_id = ?";
            qUpdProducteCanco = conn.prepareStatement(inst);
            inst = "update album set alb_any_creacio = ?  where alb_id = ? ";
            qUpdProducteAlbum = conn.prepareStatement(inst);
            
            inst = "insert into producte (pro_titol,pro_actiu,pro_estil,pro_tipus) values(?,?,?,?)";
            qAddProducte = conn.prepareStatement(inst,new String [] {"pro_id"});          
            inst = "insert into canco (can_id,can_any_creacio,can_interpret,can_durada) values(?,?,?,?)";
            qAddProducteCanco = conn.prepareStatement(inst);
            inst = "insert into album (alb_id,alb_any_creacio,alb_durada) values(?,?,0)";//per si acas , el valor per defecte = 0
            qAddProducteAlbum = conn.prepareStatement(inst);
            inst = "insert into llista (lis_id,lis_durada) values(?,0)";//mateix cas que el album
            qAddProducteLlista = conn.prepareStatement(inst);
            
            inst = "insert into llista_contingut(lic_id_llista,lic_id_producte,lic_pos) values (?,?,?)";
            qAddLlistaContingut = conn.prepareStatement(inst);
            inst = "insert into album_contingut (alc_id_album,alc_id_canco,alc_pos) values (?,?,?)";
            qAddAlbumContingut = conn.prepareStatement(inst);
          
            inst = "delete from album_contingut where alc_id_album = ?";
            qDelAlbumContingut = conn.prepareStatement(inst);
            inst = "delete from llista_contingut where lic_id_llista = ?";
            qDelLlistaContingut = conn.prepareStatement(inst);
            
            inst = "select count(*) as qtat from reproduccio where rep_id_producte = ?";
            qGetRefReproduccions = conn.prepareStatement(inst);
            
            inst = "select * from " +
            "album_contingut alc join producte p on p.pro_id = alc.alc_id_album " +
            "where alc_id_canco = ?";
            qGetAlbCon = conn.prepareStatement(inst);
            inst = "select * from llista_contingut llc " +
            "join producte p on p.pro_id = llc.lic_id_llista " +
            "where lic_id_producte = ?";
            qGetRefLlistaCon = conn.prepareStatement(inst);
            
        } catch (SQLException ex) {
            throw new MilaSpotifyException("No es pot crear el PreparedStatement:\n " + inst + "\n" + ex.getMessage());
        }
    }
    
    
    
    public List<Producte> getProductes() throws MilaSpotifyException
    {
        List<Producte> productes = new ArrayList<Producte>();
        Statement q = null;
         try {
            q = conn.createStatement();
            ResultSet rs = q.executeQuery("select * FROM PRODUCTE");
            while (rs.next()) {
                Producte p = getProducteFromId(rs.getInt("pro_id"));
                productes.add(p);
            }
            rs.close();
        } catch (SQLException ex) {
            throw new MilaSpotifyException("Error en intentar recuperar la llista de clients.\n" + ex.getMessage());
        } finally {
            if (q != null) {
                try {
                    q.close();
                } catch (SQLException ex) {
                    throw new MilaSpotifyException("Error en intentar tancar la sentència que ha recuperat la llista de productes.\n" + ex.getMessage());
                }
            }
        }
        return productes;
    }
    public List<Client> getClients() throws MilaSpotifyException
    {
        List<Client> clients = new ArrayList<Client>();
         Statement q = null;
         try {
            q = conn.createStatement();
            ResultSet rs = q.executeQuery("select * FROM Client where cli_id>0");
            while (rs.next()) {
                Client cli = getClientFromId(rs.getInt("cli_id"));
                clients.add(cli);
            }
            rs.close();
        } catch (SQLException ex) {
            throw new MilaSpotifyException("Error en intentar recuperar la llista de clients.\n" + ex.getMessage());
        } finally {
            if (q != null) {
                try {
                    q.close();
                } catch (SQLException ex) {
                    throw new MilaSpotifyException("Error en intentar tancar la sentència que ha recuperat la llista de productes.\n" + ex.getMessage());
                }
            }
        }
        return clients;
    }
    public List<Reproduccio> getReproduccions(Integer idClient,String nomProd,Date inici, Date fi) throws MilaSpotifyException
    {
        List<Reproduccio> reprs = new ArrayList<Reproduccio>();   
        if(idClient==null)
        {
            idClient = -1;
        }
        
        String aux = null;
        if(nomProd != null && !nomProd.isEmpty())
        {
           aux = "%"+nomProd.toUpperCase()+"%";
        }
        
        try 
        {
            qGetReproduccions.setInt(1,idClient);
            qGetReproduccions.setInt(2,idClient);
            
            qGetReproduccions.setString(3, aux);
            qGetReproduccions.setString(4, aux);
            
            qGetReproduccions.setDate(5,inici == null ? null : new java.sql.Date(inici.getTime()));
            qGetReproduccions.setDate(6,inici == null ? null : new java.sql.Date(inici.getTime()));
            
            qGetReproduccions.setDate(7,fi == null ? null : new java.sql.Date(fi.getTime()));
            qGetReproduccions.setDate(8,fi == null ? null : new java.sql.Date(fi.getTime()));
            
            ResultSet rs = qGetReproduccions.executeQuery();
            String executedQuery = rs.getStatement().toString();   
            while (rs.next()) {
                Client cli = getClientFromId(rs.getInt("rep_id_client"));
                Producte p = getProducteFromId(rs.getInt("rep_id_producte"));
       
                Date moment = rs.getDate("rep_moment_temporal"); 
              
                                    
                Reproduccio r = new Reproduccio(cli, p, moment);
                reprs.add(r);
            }
            rs.close();
        } catch (SQLException ex) {
            throw new MilaSpotifyException("Error en intentar recuperar la llista de reproduccions.\n" + ex.getMessage());         
        }
        
        return reprs;
    }
    
    public Producte getProducteFromId(int pro_id) throws MilaSpotifyException
    {
       Producte p = null;
       try
        {
           qGetProducte.setInt(1, pro_id);
           ResultSet rs = qGetProducte.executeQuery();
           while(rs.next())
           {
                if(rs.getString("pro_tipus").equals("C"))
                {
                   p = getCansoFromId(pro_id,rs.getString("pro_titol"),rs.getString("pro_actiu"),getEstilFromId(rs.getInt("pro_estil")));
                }
                else if(rs.getString("pro_tipus").equals("A"))
                {
                   p = getAlbumFromId(pro_id,rs.getString("pro_titol"),rs.getString("pro_actiu"),getEstilFromId(rs.getInt("pro_estil")));
                   //album
                }else
                { 
                   p = new LlistaRep(rs.getString("pro_titol"),rs.getString("pro_actiu"),getEstilFromId(rs.getInt("pro_estil")));
                   p.setPro_id(pro_id);
                    
                }
            }
        }catch(SQLException ex)
        {
            throw new MilaSpotifyException("Error en intentar recuperar el producte amb id "+ pro_id +".\n" + ex.getMessage());
        }
       return p;
    }    
    
    public Album getAlbumFromId(int pro_id,String pro_titol,String pro_actiu,Estil pro_estil) throws MilaSpotifyException
    {
        Album a = null;
        try 
        {
            qGetAlbum.setInt(1, pro_id);
            ResultSet rs = qGetAlbum.executeQuery();
            while (rs.next()) {
              
                int any_creacio = rs.getInt("alb_any_creacio");
                a = new Album(pro_titol,pro_actiu,pro_estil,any_creacio);
                a.setPro_id(pro_id);
            }
            rs.close();
        } catch (SQLException ex) 
        {
            throw new MilaSpotifyException("Error en intentar recuperar el client amb id "+ pro_id +".\n" + ex.getMessage());
        }
       return a;
    }
    
     public Artista getArtistaFromId(int art_id) throws MilaSpotifyException
    {
        
        Artista a = null;
        try {
            qGetArtistaFromId.setInt(1, art_id);
            ResultSet rs = qGetArtistaFromId.executeQuery();
            
            while(rs.next())
            {
                if(rs.getString("art_tipus").equals("I"))
                {
                    a = getArtistaIndFromId(rs.getInt("art_id"),rs.getString("art_nom"));
                }
                else if(rs.getString("art_tipus").equals("G"))
                {
                    a = getGrupFromId(rs.getInt("art_id"),rs.getString("art_nom"));
                }           
            }
            rs.close();
            return a;
        } catch (SQLException ex) {
           throw new MilaSpotifyException("Error en el getArtistaFromId: "+ex.getMessage());
        }
    }
     public ArtistaIndividual getArtistaIndFromId(int art_id,String nom) throws MilaSpotifyException
    {
        ArtistaIndividual a = null;
        try {
            qGetArtInd.setInt(1, art_id);
            
            ResultSet rs = qGetArtInd.executeQuery();
            
            while(rs.next())
            {
                a = new ArtistaIndividual(nom,rs.getDate("ind_data_naixement"),getPaisFromId(rs.getString("IND_NACIONALITAT")));
                a.setId(art_id);            
            }
            rs.close();
            return a;
        } catch (SQLException ex) {
            throw new MilaSpotifyException("Error en recollir el artista individual "+ex.getMessage());
        }
      
    }
    
    public Grup getGrupFromId(int art_id,String nom) throws MilaSpotifyException
    {
        Grup g = null;
        try {
            qGetArtGrup.setInt(1, art_id);
            
            ResultSet rs = qGetArtGrup.executeQuery();
            
            while(rs.next())
            {
                g = new Grup(nom,rs.getDate("grp_data_creacio"));
                g.setId(art_id);            
            }
            rs.close();
            return g;
        } catch (SQLException ex) {
            throw new MilaSpotifyException("Error en recollir el artista grupal:  "+ex.getMessage());
        }
    }
    
    public List<Producte> getCansonsFiltre(int art_id , String titol) throws MilaSpotifyException
    {
        List<Producte> cansons = new ArrayList();
        
        if(titol!=null && titol.length()>0)
        {
            titol = "%"+titol.toUpperCase()+"%";
        }
        try 
        {
            qGetCansonsFiltreDialog.setString(1, titol);
            qGetCansonsFiltreDialog.setString(2, titol);
            qGetCansonsFiltreDialog.setInt(3, art_id);
            qGetCansonsFiltreDialog.setInt(4, art_id);
            ResultSet rs =  qGetCansonsFiltreDialog.executeQuery();           
            while(rs.next())
            {
                cansons.add(getCansoFromId(rs.getInt("pro_id"), rs.getString("pro_titol"), rs.getString("pro_actiu"), getEstilFromId(rs.getInt("pro_estil"))));
            }
            rs.close();
            return cansons;
        } catch (SQLException ex) {
           throw new MilaSpotifyException("Error en el getCansonsFiltre: "+ex.getMessage());
        } 
    }
    
    public List<Producte> getProductesFiltreDialog(int art_id , String titol) throws MilaSpotifyException
    {
        List<Producte> productes = new ArrayList();
        try {
          
            
            qGetProductesFiltreDialog.setString(1, titol);
            qGetProductesFiltreDialog.setString(2, titol);
            qGetProductesFiltreDialog.setInt(3, art_id);
            qGetProductesFiltreDialog.setInt(4, art_id);
            ResultSet rs = qGetProductesFiltreDialog.executeQuery();
            while(rs.next())
            {
                Producte p = null;           
                if(rs.getString("pro_tipus").equals("C"))
                {
                   p = getCansoFromId(rs.getInt("pro_id"),rs.getString("pro_titol"),rs.getString("pro_actiu"),getEstilFromId(rs.getInt("pro_estil")));
                }
                else if(rs.getString("pro_tipus").equals("A"))
                {
                   p = getAlbumFromIdProd(rs.getInt("pro_id"),rs.getString("pro_titol"),rs.getString("pro_actiu"),getEstilFromId(rs.getInt("pro_estil")));
                }
                productes.add(p);
            }
            rs.close();  
            return productes;
        } catch (SQLException ex) {
            throw new MilaSpotifyException("Error en el getProductesFiltreDialog: "+ex.getMessage());
        }
    }
    
    public Canso getCansoFromId(int pro_id,String pro_titol,String pro_actiu,Estil pro_estil) throws MilaSpotifyException
     {
        Canso c = null;
        try 
        {
            qGetCanso.setInt(1, pro_id);
            ResultSet rs = qGetCanso.executeQuery();
            while (rs.next()) {
              
                int any_creacio = rs.getInt("can_any_creacio");
                int durada = rs.getInt("can_durada");
                c = new Canso(pro_titol,pro_actiu,pro_estil,any_creacio,durada);
                c.setPro_id(pro_id);
            }
            rs.close();
        } catch (SQLException ex) 
        {
            throw new MilaSpotifyException("Error en intentar recuperar el client amb id "+ pro_id +".\n" + ex.getMessage());
        }
        return c;
    }
    
    public Client getClientFromId(int cli_id) throws MilaSpotifyException
    {    
        Client c = null;       
        try 
        {
            qGetClient.setInt(1, cli_id);
            ResultSet rs = qGetClient.executeQuery();
            while (rs.next()) {
                //
                String cli_email = rs.getString("cli_email");
                String cli_nom = rs.getString("cli_nom");
                String cli_cognoms = rs.getString("cli_cognoms");
                Date cli_data_naixement = rs.getDate("cli_data_naixement");
                String cli_c_postal = rs.getString("cli_c_postal");
                String cli_dom1 = rs.getString("cli_dom1");
                String cli_poblacio = rs.getString("cli_poblacio");
                Pais cli_pais = getPaisFromId(rs.getString("cli_pais"));
                c = new Client(cli_email,cli_nom,cli_cognoms,cli_data_naixement,cli_c_postal,cli_dom1,cli_poblacio,cli_pais);
                c.setCli_id(cli_id);
            }
            rs.close();
        } catch (SQLException ex) 
        {
            throw new MilaSpotifyException("Error en intentar recuperar el client amb id "+ cli_id +".\n" + ex.getMessage());

        }
        return c;
    }
    
    public Pais getPaisFromId(String pai_cod) throws MilaSpotifyException
    {
        Pais p  = null;
        try {
            qGetPais.setString(1, pai_cod);
            ResultSet rs = qGetPais.executeQuery();
            while (rs.next()) 
            {
                p = new Pais(rs.getString("pai_codi_iso"),rs.getString("pai_nom"));
            }
            rs.close();
        } catch (SQLException ex) {
            throw new MilaSpotifyException("Error en intentar recuperar el pais.\n" + ex.getMessage());

       }    
        return p;
    }
   
    public Estil getEstilFromId(int est_id) throws MilaSpotifyException
    {
        Estil e = null;
        try {
            qGetEstil.setInt(1, est_id);
            ResultSet rs = qGetEstil.executeQuery();
            while (rs.next()) 
            {   
                e = new Estil(est_id,rs.getString("est_nom"));
            }
            rs.close();
        } catch (SQLException ex) 
        {
            throw new MilaSpotifyException("Error en intentar recuperar el pais.\n" + ex.getMessage());

        }
        return e;
    }
    
     public Album getAlbumFromIdProd(int pro_id,String pro_titol,String pro_actiu,Estil pro_estil) throws MilaSpotifyException
    {
        Album a = null;
        try 
        {
            qGetAlbum.setInt(1, pro_id);
            ResultSet rsAlbum = qGetAlbum.executeQuery();
            while (rsAlbum.next()) {
              
                int any_creacio = rsAlbum.getInt("alb_any_creacio");
                a = new Album(pro_titol,pro_actiu,pro_estil,any_creacio);
                a.setPro_id(pro_id);
                qGetCansonsAlbum.setInt(1, pro_id);
                ResultSet rsAlbumCanco = qGetCansonsAlbum.executeQuery();
                while(rsAlbumCanco.next())
                {
                  Canso c = getCansoFromId(rsAlbumCanco.getInt("pro_id"),rsAlbumCanco.getString("pro_titol"),rsAlbumCanco.getString("pro_actiu"),getEstilFromId(rsAlbumCanco.getInt("pro_estil")));
                  a.addCanso(c);
                }
                rsAlbumCanco.close();
            } 
           rsAlbum.close();
        } catch (SQLException ex) 
        {
            ex.printStackTrace();
            throw new MilaSpotifyException("Error en intentar recuperar el album amb id "+ pro_id +".\n" + ex.getMessage());
        }
       return a;
    }
     
    public List<Artista> getArtistes() throws MilaSpotifyException {
         List<Artista> artistes = new ArrayList();       
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * from artista");
            while(rs.next())
            {
                if(rs.getString("art_tipus").equals("I"))
                {
                    artistes.add(getArtistaIndFromId(rs.getInt("art_id"),rs.getString("art_nom")));
                }
                else if(rs.getString("art_tipus").equals("G"))
                {
                    artistes.add(getGrupFromId(rs.getInt("art_id"),rs.getString("art_nom")));
                }           
            }
            rs.close();
            return artistes;
        } catch (SQLException ex) {
            throw new MilaSpotifyException("Error en recollir els artistes: "+ex.getMessage());
        }
       
    }
    
     public List<Producte> getProductesFiltreMaster(String titol , String actiu , List<String> tipus) throws MilaSpotifyException
    {
        List<Producte> productes = new ArrayList(); 
        try 
        {
            if(titol!=null)
            {
                titol = "%"+titol.toUpperCase()+"%";
            }           
            int i = 1;
            if(tipus!=null && !tipus.isEmpty())
            {
                for(String t: tipus)
                {      
                   qGetProductesFiltre.setString(i,t);
                   i++;
                }
            }else
            {
              qGetProductesFiltre.setString(1,"C");
              qGetProductesFiltre.setString(2,"A");
              qGetProductesFiltre.setString(3,"L");
            }
      
            qGetProductesFiltre.setString(4, titol);
            qGetProductesFiltre.setString(5, titol);
            qGetProductesFiltre.setString(6, actiu);
            qGetProductesFiltre.setString(7, actiu);
            
            ResultSet rs = qGetProductesFiltre.executeQuery();
            while(rs.next())
            {
                Producte p = null;           
                if(rs.getString("pro_tipus").equals("C"))
                {
                   p = getCansoFromId(rs.getInt("pro_id"),rs.getString("pro_titol"),rs.getString("pro_actiu"),getEstilFromId(rs.getInt("pro_estil")));
                }
                else if(rs.getString("pro_tipus").equals("A"))
                {
                   p = getAlbumFromIdProd(rs.getInt("pro_id"),rs.getString("pro_titol"),rs.getString("pro_actiu"),getEstilFromId(rs.getInt("pro_estil")));
                }else
                {       
                   LlistaRep r = new LlistaRep(rs.getString("pro_titol"),rs.getString("pro_actiu"),getEstilFromId(rs.getInt("pro_estil")));
                   r.setPro_id(rs.getInt("pro_id"));
                   qGetProducteLlista.setInt(1,rs.getInt("pro_id"));                            
                   ResultSet rsll = qGetProducteLlista.executeQuery();                  
                   while(rsll.next())
                   {
                        Producte itemLlista = null;
                        if(rsll.getString("pro_tipus").equals("A"))
                        {
                           itemLlista = getAlbumFromIdProd(rsll.getInt("pro_id"),rsll.getString("pro_titol"),rsll.getString("pro_actiu"),getEstilFromId(rsll.getInt("pro_estil")));
                        }else
                        {
                           itemLlista =  getCansoFromId(rsll.getInt("pro_id"),rsll.getString("pro_titol"),rsll.getString("pro_actiu"),getEstilFromId(rsll.getInt("pro_estil")));
                        }                   
                        r.addItem(itemLlista);
                   }
                   rsll.close();
                   p = r;
                }
                productes.add(p);
            }
            rs.close();
            return productes;
        } catch (SQLException ex) {
           throw new MilaSpotifyException("Error en recollir els productes "+ex.getMessage());
        }           
    }
    
   
    public void addReproduccio(Reproduccio r) throws MilaSpotifyException {
        try 
        {
            java.sql.Date sqlDate = new java.sql.Date(r.getMoment_temporal().getTime());
            PreparedStatement existeix = conn.prepareStatement("select * from REPRODUCCIO where rep_moment_temporal = ? and rep_id_client = ?");
            existeix.setDate(1,sqlDate);   
            existeix.setInt(2,r.getClient().getCli_id());
            ResultSet rs = existeix.executeQuery();
            if(rs.next() == true)
            {
                throw new MilaSpotifyException("No pot existir un client amb dos reproduccions amb el mateix moment temporal");
            }
            else
            {
                qAddReproduccio.setInt(1, r.getClient().getCli_id());
                qAddReproduccio.setDate(2, sqlDate);
                qAddReproduccio.setInt(3, r.getProducte().getPro_id());
                qAddReproduccio.executeUpdate();
            }
           
        } catch (Exception ex) {
            throw new MilaSpotifyException("Error en intentar inserir la reproduccio "+ex.getMessage());
        }
    }

    public void updReproduccio(Reproduccio r,int old_c_id,Date oldDate) throws MilaSpotifyException {
        try 
        {
            java.sql.Date sqlDate = new java.sql.Date(r.getMoment_temporal().getTime());
            PreparedStatement existeix = conn.prepareStatement("select * from REPRODUCCIO where rep_moment_temporal = ? and rep_id_client = ?");
            existeix.setDate(1,sqlDate);   
            existeix.setInt(2,r.getClient().getCli_id());
            ResultSet rs = existeix.executeQuery();
            if(rs.next() == true)
            {        
                throw new MilaSpotifyException("No pot existir un client amb dos reproduccions amb el mateix moment temporal");
            }
            else
            {     
                qUpdateReproduccio.setInt(1, r.getClient().getCli_id());
                qUpdateReproduccio.setInt(2, r.getProducte().getPro_id());   
                qUpdateReproduccio.setDate(3, sqlDate);
                java.sql.Date sqlDate_old = new java.sql.Date(oldDate.getTime());     
                qUpdateReproduccio.setInt(4, old_c_id);
                qUpdateReproduccio.setDate(5, sqlDate_old);     
            }
            int row = qUpdateReproduccio.executeUpdate();   
            if(row<=0)
            {
                throw new MilaSpotifyException("s''han actualitzat "+row+" linies");
            }
            
        } catch (Exception ex) {
            throw new MilaSpotifyException("Error en intentar modificar la reproduccio " + "\n" + ex.getMessage());
        }
    }
    
    public void delReproduccio(int id_client , Date mom_temp) throws MilaSpotifyException {
        try {
           
            qDeleteReproduccio.setInt(1, id_client);
            java.sql.Date sqlDate = new java.sql.Date(mom_temp.getTime());
            qDeleteReproduccio.setDate(2, sqlDate);
           int rows = qDeleteReproduccio.executeUpdate();
           System.out.println(""+rows);
        } catch (SQLException ex) {
            throw new MilaSpotifyException("Error en intentar eliminar la reproduccio " + "\n" + ex.getMessage());
        }
    }
   
    public List<Estil> getEstils() throws MilaSpotifyException
    {
        List<Estil> estils = new ArrayList();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * from ESTIL");
            while(rs.next())
            {
                Estil e = new Estil(rs.getInt("est_id"),rs.getString("est_nom"));
                estils.add(e);
            }
            
            rs.close();
            return estils;
        } catch (SQLException ex) {
            throw new MilaSpotifyException("Error en recollir els estils: "+ex.getMessage());
        }
    }
    public void addProducte(Producte p) throws MilaSpotifyException
    {        
       try
        {
            qAddProducte.setString(1, p.getPro_titol());
            qAddProducte.setString(2, p.getPro_actiu()?"S":"N");
            qAddProducte.setInt(3,p.getPro_estil().getEst_id());
            switch(p.getPro_tipus())
            {
                case CANSO:
              
                    qAddProducte.setString(4,"C");
                    qAddProducte.executeUpdate();
                    ResultSet generatedKeysCan = qAddProducte.getGeneratedKeys();             
                    if (generatedKeysCan.next()) 
                    {
                       int can_id = generatedKeysCan.getInt(1);                     
                       Canso c = (Canso)p;
                       c.setPro_id(can_id);                   
                       qAddProducteCanco.setInt(1, can_id);
                       qAddProducteCanco.setInt(2, c.getAny_creacio());
                       if(c.getInterpret()==null)
                           qAddProducteCanco.setNull(3,java.sql.Types.NULL);
                       else
                           qAddProducteCanco.setInt(3, c.getInterpret().getId());
                       qAddProducteCanco.setInt(4,c.getDurada());
                       int rows = qAddProducteCanco.executeUpdate();
                       if(rows<=0)
                       {
                           throw new SQLException("Error en crear el producte, no hi ha cap fila afectada");
                       }
                    }
                    else 
                    {
                        
                        throw new SQLException("Error en crear el producte, no ha trobat la id del producte a insertat");
                    }
                                
                    break;
                case ALBUM:
                    Album a  = (Album)p;
                    qAddProducte.setString(4,"A");
                    qAddProducte.executeUpdate();
                    ResultSet generatedKeysAlb = qAddProducte.getGeneratedKeys();                 
                    if (generatedKeysAlb.next()) 
                    {
                      
                       int alb_id = generatedKeysAlb.getInt(1);
                       a.setPro_id(alb_id);
                       addProducteAlbum(a);
                    }
                    else 
                    {
                       
                        throw new SQLException("Error en crear el producte, no ha trobat la id del producte a insertat");
                    }
                break;
                case LLISTA_REP:
                    LlistaRep r = (LlistaRep)p;
                    qAddProducte.setString(4,"L");
                    qAddProducte.executeUpdate();
                    ResultSet generatedKeysLlista = qAddProducte.getGeneratedKeys();                 
                    if (generatedKeysLlista.next()) 
                    {
                       
                       int lis_id = generatedKeysLlista.getInt(1);
                       r.setPro_id(lis_id);
                       addProducteLlista(r);
                    }
                    else 
                    {
                       
                        throw new SQLException("Error en crear el producte, no ha trobat la id del producte a insertat");
                    }
                break;
            }
            validarCanvis();
        }catch(SQLException ex)
        {
            throw new MilaSpotifyException("Error en el addProducte: "+ex.getMessage());
        }
    }
      public void addProducteAlbum(Album a) throws MilaSpotifyException
    {
        try
        {
            qAddProducteAlbum.setInt(1,a.getPro_id());
            qAddProducteAlbum.setInt(2,a.getAny_creacio());
            qAddProducteAlbum.executeUpdate();
            
            if(a.getNumCansons()>0)
            {
                Iterator<Canso> ite = a.getCansons();
                int i = 1;
                while(ite.hasNext())
                {
                    Canso c = ite.next();
                    qAddAlbumContingut.setInt(1,a.getPro_id());
                    qAddAlbumContingut.setInt(2,c.getPro_id());
                    qAddAlbumContingut.setInt(3,i);
                    qAddAlbumContingut.executeUpdate();
                    i++;
                }
              
            }         
        }
        catch(SQLException ex)
        {
            throw new MilaSpotifyException("Error en afegir el producte tipus Album: "+ex.getMessage());
        }
    }
    public void addProducteLlista(LlistaRep r) throws MilaSpotifyException
    {
        try
        {     
            qAddProducteLlista.setInt(1, r.getPro_id());   
            qAddProducteLlista.executeUpdate();
            if(r.getNumItems()>0)
            {
                int i = 1;
                Iterator<Producte> ite = r.getItems();
                while(ite.hasNext())
                {
                    Producte p = ite.next();
                    qAddLlistaContingut.setInt(1,r.getPro_id());
                    qAddLlistaContingut.setInt(2,p.getPro_id());
                    qAddLlistaContingut.setInt(3,i);
                    qAddLlistaContingut.executeUpdate();   
                    i++;
                }
               
            }           
        }catch(SQLException ex)
        {
            throw new MilaSpotifyException("Error en afegir el producte tipus llista: "+ex.getMessage());
        
        }
    }
    
    public void updProducte(Producte p) throws MilaSpotifyException
    {
        try {
            qUpdProducte.setString(1, p.getPro_titol());
            qUpdProducte.setString(2,p.getPro_actiu()?"S":"N");
            qUpdProducte.setInt(3,p.getPro_estil().getEst_id());
            qUpdProducte.setInt(5,p.getPro_id());
            switch(p.getPro_tipus())
            {
                case CANSO:
                    qUpdProducte.setString(4,"C");
                    qUpdProducte.executeUpdate();
                    Canso c = (Canso)p;
                    
                    qUpdProducteCanco.setInt(1,c.getAny_creacio());
                    if(c.getInterpret()==null)
                    {
                        qUpdProducteCanco.setNull(2,java.sql.Types.NULL);
                    }            
                    else
                    {
                        qUpdProducteCanco.setInt(2,c.getInterpret().getId());
                    }
                          
                    qUpdProducteCanco.setInt(3,c.getDurada());
                    qUpdProducteCanco.setInt(4,c.getPro_id());
                    qUpdProducteCanco.executeUpdate();
                    break;
                case ALBUM:
                    qUpdProducte.setString(4,"A");
                    qUpdProducte.executeUpdate();
                    updProducteAlbum((Album)p);
                    break;
                case LLISTA_REP:
                    qUpdProducte.setString(4,"L");
                    qUpdProducte.executeUpdate();
                    updProducteLlista((LlistaRep)p);
                    break;
            }
            validarCanvis();
        } catch (SQLException ex) {
            throw new MilaSpotifyException("Error en el update producte: "+ex.getMessage());
        }
    }
     public void updProducteAlbum(Album a) throws MilaSpotifyException
    {
        try
        {
            
            qUpdProducteAlbum.setInt(1,a.getAny_creacio());
            qUpdProducteAlbum.setInt(2,a.getPro_id());
            qUpdProducteAlbum.executeUpdate();
            if(a.getNumCansons()>0)
            {
                //eliminem i afegim 
                qDelAlbumContingut.setInt(1, a.getPro_id());
                qDelAlbumContingut.executeUpdate();
                Iterator<Canso> ite = a.getCansons();
                int i = 1;
                while(ite.hasNext())
                {
                    Canso c = ite.next();
                    qAddAlbumContingut.setInt(1,a.getPro_id());
                    qAddAlbumContingut.setInt(2,c.getPro_id());
                    qAddAlbumContingut.setInt(3,i);
                    qAddAlbumContingut.executeUpdate();
                    i++;
                }        
            }else
            {
                qDelAlbumContingut.setInt(1, a.getPro_id());
                qDelAlbumContingut.executeUpdate();
            }         
        }
        catch(SQLException ex)
        {
            throw new MilaSpotifyException("Error en actualitzar el producte tipus Album: "+ex.getMessage());
        }
    }  
    public void updProducteLlista(LlistaRep r) throws MilaSpotifyException
    {
        try
        {     
            if(r.getNumItems()>0)
            {
                qDelLlistaContingut.setInt(1, r.getPro_id());
                qDelLlistaContingut.executeUpdate();
                int i = 1;
                Iterator<Producte> ite = r.getItems();
                while(ite.hasNext())
                {
                    Producte p = ite.next();
                    qAddLlistaContingut.setInt(1,r.getPro_id());
                    qAddLlistaContingut.setInt(2,p.getPro_id());
                    qAddLlistaContingut.setInt(3,i);
                    qAddLlistaContingut.executeUpdate();
                    i++;
                }
            }else
            {
                qDelLlistaContingut.setInt(1, r.getPro_id());
                qDelLlistaContingut.executeUpdate();
            }           
            
        }catch(SQLException ex)
        {
            throw new MilaSpotifyException("Error en actualitzar el producte tipus llista: "+ex.getMessage());
        
        }
    }    
    
    
    
    public void delProducte(Producte p) throws MilaSpotifyException
    {
       
        String err="";
        boolean noRef = true;
        try
        {      
            qGetRefReproduccions.setInt(1,p.getPro_id());
            ResultSet rsRefRep = qGetRefReproduccions.executeQuery();
            
            if(rsRefRep.next())
            {
                int qt = rsRefRep.getInt("qtat");
                if(qt>0)
                {
                    rsRefRep.close();
                    throw new MilaSpotifyException("aquest producte  te reproduccions , eliminales abans!!");
                }
              
            }
            
            switch(p.getPro_tipus())
            {
                case CANSO:
                    
                    //fer comprovacio si aquesta canço esta en un album o llista
                    qGetAlbCon.setInt(1,p.getPro_id());
                    ResultSet rsRefAlbCon = qGetAlbCon.executeQuery();

                    while(rsRefAlbCon.next())
                    {
                        noRef = false;
                        err+= rsRefAlbCon.getString("pro_titol")+"\n";
                        //te referencies amb algun album
                    }
                    rsRefAlbCon.close();
                    
                    qGetRefLlistaCon.setInt(1, p.getPro_id());
                    ResultSet rsRefLlistaCon = qGetRefLlistaCon.executeQuery();
                    while(rsRefLlistaCon.next())
                    {
                        noRef = false;
                        err+= rsRefLlistaCon.getString("pro_titol")+"\n";
                    }
                    rsRefLlistaCon.close();
                    
                    if(noRef==false)
                    {
                        throw new MilaSpotifyException("Error! Per eliminar la canço no ha de estar a productes: "+err);
                    }

                    qDelProducteCanco.setInt(1, p.getPro_id());
                    qDelProducteCanco.executeUpdate();
                    break;
                case ALBUM:
                 
                    qGetRefLlistaCon.setInt(1, p.getPro_id());
                    ResultSet resultllistaAlb = qGetRefLlistaCon.executeQuery();
                    while(resultllistaAlb.next())
                    {
                        noRef = false;
                        err+= resultllistaAlb.getString("pro_titol")+"\n";
                    }
                    resultllistaAlb.close();
                    
                    if(noRef==false)
                    {
                        throw new MilaSpotifyException("Error! Per eliminar el album ha de estar fora de les  llista/es: "+err);
                    }
                    
                    qDelAlbumContingut.setInt(1, p.getPro_id());
                    qDelAlbumContingut.executeUpdate();
                    
                    qDelProducteAlbum.setInt(1, p.getPro_id());
                    qDelProducteAlbum.executeUpdate();
                    break;

                case LLISTA_REP:
                    
                    qDelLlistaContingut.setInt(1,p.getPro_id());
                    qDelLlistaContingut.executeUpdate();
                    
                    qDelProducteLlista.setInt(1, p.getPro_id());
                    qDelProducteLlista.executeUpdate();
                    break;
            }
            qDeleteProducte.setInt(1, p.getPro_id());
            qDeleteProducte.executeUpdate();
            validarCanvis();
        }catch(SQLException ex)
        {
            throw new MilaSpotifyException("Error en el delete producte: "+ex.getMessage());
        }
    }
    
    
    
    
    
    //_____________________________________________________________________________________
    public void tancar() throws MilaSpotifyException
    {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new MilaSpotifyException("Error en fer rollback final.\n" + ex.getMessage());
            }
            try {
                conn.close();
            } catch (SQLException ex) {
                throw new MilaSpotifyException("Error en tancar la connexió.\n" + ex.getMessage());
            }
        }
    }  
    
    public void validarCanvis() throws MilaSpotifyException {
        try {
            conn.commit();
        } catch (SQLException ex) {
            throw new MilaSpotifyException("Error en validar els canvis: " + ex.getMessage());
        }
    }

    public void desferCanvis() throws MilaSpotifyException {
        try {
            conn.rollback();
        } catch (SQLException ex) {
            throw new MilaSpotifyException("Error en desfer els canvis: " + ex.getMessage());
        }
    }

  

    
   

  
}
