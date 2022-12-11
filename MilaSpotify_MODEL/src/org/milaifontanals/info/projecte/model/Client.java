package org.milaifontanals.info.projecte.model;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {
     
    private int cli_id;
    private String cli_email;
    private String cli_nom;
    private String cli_cognoms;
    private Date cli_data_naixement;
    private String cli_c_postal;
    private String cli_dom1;
    private String cli_dom2;
    private String cli_poblacio;
    private Pais cli_pais;
    /*
    public Client(long cli_id, String cli_email, String cli_nom, String cli_cognoms, Date cli_data_naixement, String c_postal, String cli_dom1, String cli_dom2, String cli_poblacio, Pais cli_pais) {
        this.cli_id = cli_id;
        this.cli_email = cli_email;
        this.cli_nom = cli_nom;
        this.cli_cognoms = cli_cognoms;
        this.cli_data_naixement = cli_data_naixement;
        this.c_postal = c_postal;
        this.cli_dom1 = cli_dom1;
        this.cli_dom2 = cli_dom2;
        this.cli_poblacio = cli_poblacio;
        this.cli_pais = cli_pais;
    }*/

    public Client(String cli_email, String cli_nom, String cli_cognoms, Date cli_data_naixement, String c_postal, String cli_dom1, String cli_dom2, String cli_poblacio, Pais cli_pais) {
        setCli_email(cli_email);
        setCli_nom(cli_nom);
        setCli_cognoms(cli_cognoms);
        setCli_data_naixement(cli_data_naixement);
        setCli_c_postal(c_postal);
        setCli_dom1(cli_dom1);
        setCli_dom2(cli_dom2);
        setCli_poblacio(cli_poblacio);
        setCli_pais(cli_pais);
    }

    public Client(String cli_email, String cli_nom, String cli_cognoms, Date cli_data_naixement, String c_postal, String cli_dom1, String cli_poblacio, Pais cli_pais) {
        setCli_email(cli_email);
        setCli_nom(cli_nom);
        setCli_cognoms(cli_cognoms);
        setCli_data_naixement(cli_data_naixement);
        setCli_c_postal(c_postal);
        setCli_dom1(cli_dom1);
        setCli_poblacio(cli_poblacio);
        setCli_pais(cli_pais);
    }
    
    public void setCli_id(int cli_id) {
        if(cli_id<0)
        {
            throw new RuntimeException("El id del client no pot ser negatiu o 0");
        }
        this.cli_id = cli_id;
    }

    public void setCli_email(String cli_email) {
      
        if(cli_email == null || cli_email.length()<=0 || isValidEmail(cli_email)==false )
        {
            throw new RuntimeException("El email es obligatori i ha de tindre un format valid");
        }
        this.cli_email = cli_email;
    }

    public void setCli_nom(String cli_nom) {
        if(cli_nom == null || cli_nom.length()<=0 )
        {
            throw new RuntimeException("El nom es obligatori");
        }
        this.cli_nom = cli_nom;
    }

    public void setCli_cognoms(String cli_cognoms) {
        
        if(cli_cognoms == null || cli_cognoms.length()<=0)
        {
            throw new RuntimeException("Els cognoms son obligatoris");
        }
        this.cli_cognoms = cli_cognoms;
    }

    public void setCli_data_naixement(Date cli_data_naixement) {
        
        if(cli_data_naixement==null || cli_data_naixement.after(new Date()))
        {
            throw new RuntimeException("La data de naixement no pot ser null o futura");
        }
        this.cli_data_naixement = cli_data_naixement;
    }

    public void setCli_c_postal(String c_postal) { 
        if(c_postal == null || c_postal.length()<5)
        {
            throw new RuntimeException("El codi postal es obligatori i no pot tindre menys de 5 caracters");
        }
        this.cli_c_postal = c_postal;
    }

    public void setCli_dom1(String cli_dom1) {
        if(cli_dom1.length()<10 || cli_dom1.equals(""))
        {
            throw new RuntimeException("El dom1 es obligatori i no pot tindre menys de 10 caracters");
        }
        this.cli_dom1 = cli_dom1;
    }

    public void setCli_dom2(String cli_dom2) {
        this.cli_dom2 = cli_dom2;
    }

    public void setCli_poblacio(String cli_poblacio) {
        if(cli_poblacio  == null || cli_poblacio.length()<=0)
        this.cli_poblacio = cli_poblacio;
    }

    public void setCli_pais(Pais cli_pais) {
        if(cli_pais == null || cli_pais.getPai_codi().length()<=0)
        {
            throw new RuntimeException("El pais es obligatori");
        }
        this.cli_pais = cli_pais;
    }
    
    
    
      public int getCli_id() {
        return cli_id;
    }

    public String getCli_email() {
        return cli_email;
    }

    public String getCli_nom() {
        return cli_nom;
    }

    public String getCli_cognoms() {
        return cli_cognoms;
    }

    public Date getCli_data_naixement() {
        return cli_data_naixement;
    }

    public String getCli_c_postal() {
        return cli_c_postal;
    }

    public String getCli_dom1() {
        return cli_dom1;
    }

    public String getCli_dom2() {
        return cli_dom2;
    }

    public String getCli_poblacio() {
        return cli_poblacio;
    }

    public Pais getCli_pais() {
        return cli_pais;
    }
    
    

    private boolean isValidEmail(String email)
    {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(email);
        
        return m.matches();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (int) (this.cli_id ^ (this.cli_id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Client other = (Client) obj;
        if (this.cli_id != other.cli_id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return  cli_email + " - " + cli_nom;
    }  
    
}
