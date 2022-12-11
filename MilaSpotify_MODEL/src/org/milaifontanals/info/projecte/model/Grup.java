package org.milaifontanals.info.projecte.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Grup extends Artista{

    private Date dataCreacio;
    private List<MembreGrup> membres = new ArrayList();
            
    public Grup(String nom,Date dataCreacio) {
        super(nom, TipusArtista.GRUPAL);
        setDataCreacio(dataCreacio);
    }

    public Date getDataCreacio() {
        return dataCreacio;
    }

    public void setDataCreacio(Date dataCreacio) {
        if(dataCreacio == null || dataCreacio.after(new Date()))
        {
            throw new RuntimeException("La data de creacio es obligatoria i no pot ser futura");
        }
        this.dataCreacio = dataCreacio;
    }
    
    
    public List<ArtistaIndividual> getMembres()
    {
        List<ArtistaIndividual> mem = new ArrayList();
        
        for(MembreGrup m : this.membres)
        {
            mem.add(m.getArt_ind());
        }
        
        return mem;
    }
    
    
    public void addMembre(ArtistaIndividual artista,Date dataInici, Date dataFi)
    {
        MembreGrup m = new MembreGrup(dataInici,dataFi,artista);
        this.membres.add(m);
    }
    
    public void eliminarMembre(int idx)
    {
        this.membres.remove(idx);
    }
    
}
