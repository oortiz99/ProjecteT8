
package org.milaifontanals.info.projecte.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Canso extends Producte{
   
    private int any_creacio;
    private int durada;
    private Artista interpret;


    private List<Artista> autors = new ArrayList();
    
    public Canso(String pro_titol, String pro_actiu, Estil pro_estil,int anyCreacio,int durada)
    {
        super(pro_titol,pro_actiu,pro_estil,TipusProducte.CANSO);
        setAny_creacio(anyCreacio);
        setDurada(durada);
    }

    public Canso(String titProd, String prodEstat, Estil estilProd, int anyCreacioCan, int duradaCan, Artista a) {
        
          super(titProd,prodEstat,estilProd,TipusProducte.CANSO);
        setAny_creacio(anyCreacioCan);
        setDurada(durada);
    }

    

   
    

    public void setAny_creacio(int any_creacio) {
        if(any_creacio<=0)
        {
            throw new RuntimeException("Any creacio es obligatori");
        }
        this.any_creacio = any_creacio;
    }

   
    public void setDurada(int durada) {
        if(durada<=0)
        {
            throw new RuntimeException("La durada ha de ser mes gran de 0");
        }
        this.durada = durada;
    }
    
    public void setInterpret(Artista interpret) {
        this.interpret = interpret;
    }
    
    public int getAny_creacio()
    {
        return this.any_creacio;
    }

    public Artista getInterpret() {
        return interpret;
    }
   
    public Iterator<Artista> getAutors()
    {
        return autors.iterator();
    }
    
    @Override
    public int getDurada() {
        return this.durada;
    }
    
     
}
