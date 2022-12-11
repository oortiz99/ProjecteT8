/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.info.projecte.vista;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.milaifontanals.info.projecte.persistencia.MilaSpotifyException;

/**
 *
 * @author Oscar
 */
class ImprimirJaspersReports {
    private String nomFitxerPropietats = null;
    private static final int BUFFER_SIZE = 4096;
    
    public ImprimirJaspersReports() throws MilaSpotifyException
    {
        nomFitxerPropietats = "jasper.properties";
    }
     
    public ImprimirJaspersReports(String nomFitxerPropietats) throws MilaSpotifyException
    {
        if(nomFitxerPropietats==null || nomFitxerPropietats.length()<=0){
            throw new MilaSpotifyException("El fitxer de propietats es obligatori per executar els reports!!");
        }
        this.nomFitxerPropietats = nomFitxerPropietats;
    }
      public void LlistaProductesFiltrats(String titol,List<String>tipus,String actiu)
    {
       
        try 
        {
            
            Properties props = new Properties();
            props.load(new FileInputStream(nomFitxerPropietats));
            String[] claus = {"urlJRS", "userJRS", "passwordJRS"};
            String[] valors = new String[3];
            for (int i = 0; i < claus.length; i++) {
                valors[i] = props.getProperty(claus[i]);
                if (valors[i] == null || valors[i].isEmpty()) {
                    throw new RuntimeException("L'arxiu " + nomFitxerPropietats + " no troba la clau " + claus[i]);
                }
            }
            String params = "";
            
             if(!tipus.isEmpty())
            {
                for(String t:tipus)
                {
                    params+="&paramTipus="+t;
                }
            
            }
            
      
            
            if(actiu!=null && actiu.length()>0)
            {
                params+="&paramActiu="+actiu;
            }
                  if(titol!=null && titol.length()>0)
            {
                params+="&paramNomProd="+titol;
            }
           
                     
            String url= valors[0]+valors[1]+"/folderReports/reportProductes.pdf?"+params;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            String autenticacio = Base64.
                    getEncoder().
                    encodeToString((valors[1] + ":" + valors[2]).getBytes());
            con.setRequestProperty("Authorization", "Basic " + autenticacio);
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String fileName = "";
                String disposition = con.getHeaderField("Content-Disposition");
                String contentType = con.getContentType();
                int contentLength = con.getContentLength();

                if (disposition != null) {
                
                    int index = disposition.indexOf("filename=");
                    if (index > 0) {
                        fileName = disposition.substring(index + 10,
                                disposition.length() - 1);
                    }
                } else {
                   
                    int posArguments = url.lastIndexOf("?");
                    if (posArguments == -1) { 
                        fileName = url.substring(url.lastIndexOf("/") + 1,
                                url.length());
                    } else { 
                        fileName = url.substring(url.lastIndexOf("/") + 1, posArguments);
                    }
                }

                System.out.println("Content-Type = " + contentType);
                System.out.println("Content-Disposition = " + disposition);
                System.out.println("Content-Length = " + contentLength);
                System.out.println("fileName = " + fileName);

             
                InputStream inputStream = con.getInputStream();
               
                FileOutputStream outputStream = new FileOutputStream(fileName);
                // Llegim i escrivim
                int bytesRead = -1;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();

                System.out.println("Arxiu descarregat");
               
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().open(new File(fileName));
                    } catch (IOException ex) {
                        System.out.println("No hi ha aplicacions disponibles per obrir el fitxer");
                    }
                }
            } else {
                System.out.println("Mètode 'GET' : " + url);
                System.out.println("Codi resposta: " + responseCode);
                System.out.println("Cap fitxer a descarregar");
            }
            con.disconnect();
        } catch (IOException ex) {
           
            ex.printStackTrace();
        }
    }
      
       public void LlistaReproduccionsProducte(int pro_id) throws MilaSpotifyException, JRException
    {
        Connection conn = null;
        try 
        {
            Properties props = new Properties();
            props.load(new FileInputStream(nomFitxerPropietats));
            String[] claus = {"url", "user", "password"};
            String[] valors = new String[3];
            for (int i = 0; i < claus.length; i++) {
                valors[i] = props.getProperty(claus[i]);
                if (valors[i] == null || valors[i].isEmpty()) {
                    throw new RuntimeException("L'arxiu " + nomFitxerPropietats + " no troba la clau " + claus[i]);
                }
            }
            conn = DriverManager.getConnection(valors[0], valors[1], valors[2]);
            conn.setAutoCommit(false);
         
            JasperReport report = JasperCompileManager.compileReport("reportReproduccions.jrxml");
            System.out.println("Informe compilat");
           
            Map<String, Object> parameters = new HashMap<String, Object>();
           
            parameters.put("paramProId", pro_id);
            
            JasperPrint print = JasperFillManager.fillReport(report, parameters, conn);
            System.out.println("Informe generat");
            
            JasperExportManager.exportReportToPdfFile(print, "ReproduccionsDelProducte.pdf");
            System.out.println("Informe exportat a PDF");
            
            JasperViewer.viewReport(print, false);
            System.out.println("Informe visualitzat pel visor d'informes");
            conn.close();      
        } catch (JRException | SQLException ex) {
            throw new MilaSpotifyException("Error al generar el report\n: "+ex.getMessage());
        } catch (IOException ex) {
            throw new MilaSpotifyException("Error , problemes en trobar el fitxer\n: "+ex.getMessage());
        }
    
    }

 
     
}
