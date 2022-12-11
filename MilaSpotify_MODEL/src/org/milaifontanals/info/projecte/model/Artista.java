package org.milaifontanals.info.projecte.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Artista {
    
    private int id;
    private String nom;
    private List<Canso> cansons_int = new ArrayList<Canso>();
    private TipusArtista tipus;

    @Override
    public String toString() {
        return  nom ;
    }

    public Artista(String nom, TipusArtista tipus) {
        setNom(nom);
        setTipus(tipus);
    }

    public Iterator<Canso> getCansonsInterpret()
    {
        return cansons_int.iterator();
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        if(nom == null || nom.length()==0)
        {
            throw new RuntimeException("El nom es obligatori");
        }
        this.nom = nom;
    }

    public TipusArtista getTipus() {
        return tipus;
    }

    public void setTipus(TipusArtista tipus) {
        if(tipus == null)
        {
            throw new RuntimeException("El tipus es obligatori");
        }
        this.tipus = tipus;
    }
    
    
    
}
