package org.milaifontanals.info.projecte.model;

import java.util.Date;
public class MembreGrup {
    
    
    private Date dataInici;
    private Date dataFinal;
    private ArtistaIndividual art_ind;

    public MembreGrup(Date dataInici, Date dataFinal, ArtistaIndividual art_ind) {
        setDataInici(dataInici);
        setDataFinal(dataFinal);
        setArt_ind(art_ind);
    }
     
    public Date getDataInici() {
        return dataInici;
    }

    public void setDataInici(Date dataInici) {
        if(dataInici== null || dataInici.after(new Date()))
        {
            throw new RuntimeException("La data inici es obligatori i no pot ser futura");
        }
        this.dataInici = dataInici;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        if(dataFinal!=null && dataFinal.compareTo(this.dataInici)<0)
        {
            throw new RuntimeException("La data final no pot ser anterior a la de inici");
        }
        this.dataFinal = dataFinal;
    }

    public ArtistaIndividual getArt_ind() {
        return art_ind;
    }

    public void setArt_ind(ArtistaIndividual art_ind) {
        if(art_ind == null || art_ind.getId()<=0)
        {
            throw new RuntimeException("El artista es obligatori");
        }
        this.art_ind = art_ind;
    }
    
    
    
    
}
