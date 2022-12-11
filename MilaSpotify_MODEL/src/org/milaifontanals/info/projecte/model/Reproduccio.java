package org.milaifontanals.info.projecte.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Reproduccio {
    private int rep_id_client;
    private int rep_id_PRODUCTE;
        
    private Client client;
    private Producte producte;
    private Date moment_temporal; 
    public Reproduccio(Client client, Producte producte, Date moment_temporal) {
        setClient(client);
        setProducte(producte);
        setMoment_temporal(moment_temporal);
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        if(client == null || client.getCli_id()==0)
        {
            throw new RuntimeException("El client es obligatori");
        }
        this.client = client;
    }

    public Producte getProducte() {
        return producte;
    }

    public void setProducte(Producte producte) {
        if(producte== null || producte.getPro_id()<=0)
        {
            throw new RuntimeException("El producte es obligatori");
        }
        this.producte = producte;
    }

    public Date getMoment_temporal() {
        return moment_temporal;
    }

    public void setMoment_temporal(Date moment_temporal) {
        if(moment_temporal == null && moment_temporal.compareTo(new Date())>0)
        {
            throw new RuntimeException("El moment temporal es obligatori i no pot ser futur");
        }
       
        this.moment_temporal = moment_temporal;
    }
       
    
      
}
