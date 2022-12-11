
package org.milaifontanals.info.projecte.model;

import java.util.Objects;

public class Estil {
    
    private int est_id = 0;
    private String est_nom;
    
    public Estil(int est_id, String est_nom) {
        setEst_id(est_id);
        setEst_nom(est_nom);
    }
 
    public Estil(String est_nom) {
        setEst_nom(est_nom);
    }
    public int getEst_id() {
        return est_id;
    }

    public void setEst_id(int est_id) {
        if(est_id<=0)
        {
            throw new RuntimeException("El id de estat es obligatori");
        }
        this.est_id = est_id;
    }
    
    public String getEst_nom() {
        return est_nom;
    }

    public void setEst_nom(String est_nom) {
        if(est_nom == null || est_nom.length()<=0)
        {
            throw new RuntimeException("El nom del estat es obligatori");
        }
        this.est_nom = est_nom;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + this.est_id;
        hash = 83 * hash + Objects.hashCode(this.est_nom);
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
        final Estil other = (Estil) obj;
        if (this.est_id != other.est_id) {
            return false;
        }
        if (!Objects.equals(this.est_nom, other.est_nom)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return est_nom ;
    }
    
    
    
}
