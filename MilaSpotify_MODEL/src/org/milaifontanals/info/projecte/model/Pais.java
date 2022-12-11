package org.milaifontanals.info.projecte.model;

import java.util.Objects;

public class Pais {
    private String pai_codi;
    private String pai_nom;

    public Pais(String pai_codi, String pai_nom) {
        setPai_codi(pai_codi);
        setPai_nom(pai_nom);
    }

    public String getPai_codi() {
        return pai_codi;
    }

    public void setPai_codi(String pai_codi) {
        if(pai_codi == null || pai_codi.length()<=0 )
        {
            throw new RuntimeException("El codi del pais es obligatori");
        }
        this.pai_codi = pai_codi;
    }

    public String getPai_nom() {
        return pai_nom;
    }

    public void setPai_nom(String pai_nom) {
          if(pai_nom == null || pai_nom.length()<=0 )
        {
            throw new RuntimeException("El nom del pais es obligatori");
        }
        this.pai_nom = pai_nom;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.pai_codi);
        hash = 37 * hash + Objects.hashCode(this.pai_nom);
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
        final Pais other = (Pais) obj;
        if (!Objects.equals(this.pai_codi, other.pai_codi)) {
            return false;
        }
        if (!Objects.equals(this.pai_nom, other.pai_nom)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return  "Codi del pais=" + pai_codi + ", Nom del pais=" + pai_nom;
    }
    
    
    
}
