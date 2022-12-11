package org.milaifontanals.info.projecte.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Album extends Producte{

    private int any_creacio;
    private List<Canso> cansons = new ArrayList<Canso>();
    
    public Album(String pro_titol, String pro_actiu, Estil pro_estil,int anyCreacio)
    {
        super(pro_titol,pro_actiu,pro_estil,TipusProducte.ALBUM);
        setAny_creacio(anyCreacio);

    }

 

    public int getAny_creacio() {
        return any_creacio;
    }

    public void setAny_creacio(int any_creacio) {
        if(any_creacio<=0)
        {
            throw new RuntimeException("Any creacio del album es obligatori");
        }
        this.any_creacio = any_creacio;
    }

    
    public Iterator<Canso> getCansons()
    {
        return cansons.iterator();
    }
    
    public int getNumCansons()
    {
        return cansons.size();
    }
    
    public Canso getCanso(int idx){
        
        return cansons.get(idx);
    }
    
    public void removeCanso(int idx)
    {
        cansons.remove(idx);
    }
    
    public void addCanso(Canso c)
    {
        if(c!=null && c.getPro_id()>0)
        {
             cansons.add(c);
        }
       
    }
    
    @Override
    public int getDurada() {
       int durada = 0;
         
       for(Canso c : cansons)
       {
          durada+= c.getDurada();
       }
       
       return durada;
    }

    @Override
    public String toString() {
        return getPro_titol() + " - " + getPro_tipus();
    }
    
    
}
