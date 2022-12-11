package org.milaifontanals.info.projecte.model;

import java.util.Date;

public abstract class Producte {
    private int pro_id;
    private String pro_titol;
    private boolean pro_actiu;
    private Estil pro_estil;
    private TipusProducte pro_tipus;
    
    
    public Producte(String pro_titol, String pro_actiu, Estil pro_estil, TipusProducte pro_tipus) {
       
        setPro_titol(pro_titol);
        setPro_actiu(pro_actiu);
        setPro_estil(pro_estil);
        setPro_tipus(pro_tipus);
    }

    /*
     public Producte(String pro_titol, String pro_actiu, Estil pro_estil, TipusProducte pro_tipus) {
        setPro_titol(pro_titol);
        setPro_actiu(pro_actiu);
        setPro_estil(pro_estil);
        setPro_tipus(pro_tipus);
    }*/
     
     
    public int getPro_id() {
        return pro_id;
    }
    
    public void setPro_id(int pro_id) {
        if(pro_id<=0)
        {
            throw new RuntimeException("El id no pot ser negatiu");
        }
        this.pro_id = pro_id;
    }
    
    public String getPro_titol() {
        return pro_titol;
    }

    public void setPro_titol(String pro_titol) {
        if(pro_titol == null || pro_titol.length()<=0 )
        {
            throw new RuntimeException("El titol del producte es obligatori");
        }
        this.pro_titol = pro_titol;
    }

    public boolean getPro_actiu() {
        return pro_actiu;
    }

    public void setPro_actiu(String pro_actiu) {
        
        if(pro_actiu.equals("S"))
        {
            this.pro_actiu = true;
        }else if(pro_actiu.equals("N"))
        {
            this.pro_actiu = false;
        }
        else
        {
            throw new RuntimeException("el valor actiu nomes pot ser S o N");
        }
    }

    public Estil getPro_estil() {
        return pro_estil;
    }

    public void setPro_estil(Estil pro_estil) {
        if(pro_estil == null || pro_estil.getEst_id()==0)
        {
            throw new RuntimeException("el estil es obligatori");
        }
        this.pro_estil = pro_estil;
    }

    public TipusProducte getPro_tipus() {
        return pro_tipus;
    }

    public void setPro_tipus(TipusProducte pro_tipus) {
        if(pro_tipus == null)
        {
            throw new RuntimeException("El tipus es obligatori");
        }
        this.pro_tipus = pro_tipus;
    }
    
    public abstract int getDurada();

    @Override
    public String toString() {
        return  pro_titol + " - " + pro_tipus;
    }

    
    
    
}
