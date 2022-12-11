package org.milaifontanals.info.projecte.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LlistaRep extends Producte {

    
    private List<Producte> items = new ArrayList();
    private int durada;
    
    public LlistaRep(String pro_titol, String pro_actiu, Estil pro_estil) {
        super(pro_titol, pro_actiu, pro_estil,TipusProducte.LLISTA_REP); 
       
    }

  

    public Iterator<Producte> getItems()
    {
        return items.iterator();
    }
    
    public int getNumItems(){
        return items.size();
    }
    
    public Producte getItem(int idx)
    {
        return items.get(idx);
    }
    
    public void addItem(Producte p)
    {
        if(p!=null && p.getPro_id()>0){
            items.add(p);
        }
    }
    
    public void removeItem(int idx)
    {
        items.remove(idx);
    }
    
    @Override
    public int getDurada() {
       durada = 0;
      for(Producte p : items)
      {
          durada+= p.getDurada();
      }
        return this.durada;
    }
    
    @Override
    public String toString() {
        return getPro_titol() + " - " + getPro_tipus();
    }
}
