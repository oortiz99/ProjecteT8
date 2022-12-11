package org.milaifontanals.info.projecte.model;

import java.util.Date;

public class ArtistaIndividual extends Artista{
    
    private Date dataNaixement;
    private Pais pais;

    public ArtistaIndividual(String nom,TipusArtista tipus,Date dataNaix,Pais pais) {
        super(nom,TipusArtista.INDIVIDUAL);
        setDataNaixement(dataNaix);
    }

    public ArtistaIndividual(String nom, Date dataNaix, Pais paisFromId) {
        super(nom,TipusArtista.INDIVIDUAL);
        setDataNaixement(dataNaix);
    }



    

    public Date getDataNaixement() {
        return dataNaixement;
    }

    public void setDataNaixement(Date dataNaixement) {
        if(dataNaixement==null || dataNaixement.after(new Date()))
        {
            throw new RuntimeException("La data naixement es obligatori i no pot ser futura");
        }
        this.dataNaixement = dataNaixement;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        if(pais==null || pais.getPai_codi().length()==0)
        {
            throw new RuntimeException("El pais es obligatori i ha de existir");
        }
        this.pais = pais;
    }

 
    
    
    
}
