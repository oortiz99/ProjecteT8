package org.milaifontanals.info.projecte.vista;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class JRProductes {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Cal executar passant com a paràmetre la URL de connexió JDBC");
            return;
        }

        Utils.demanaUsuariContrasenya(args[0]);

        Connection con = null;
        try {
            con = DriverManager.getConnection(args[0], Utils.usuari, Utils.contrasenya);
            System.out.println("Connexió establerta");
            System.out.println("URL: "+args[0]);
            System.out.println("Usuari: "+Utils.usuari);
            // Compila el report
            JasperReport report = JasperCompileManager.compileReport("reportProductes.jrxml");
            System.out.println("Informe compilat");
            // Genera el report (sense paràmetres)
            JasperPrint print = JasperFillManager.fillReport(report, null, con);
            System.out.println("Informe generat");
            // Exporta informe a PDF (hi ha diverses opcions)
            JasperExportManager.exportReportToPdfFile(print, "Productes.pdf");
            System.out.println("Informe exportat a PDF");
            //visualitza l'informe directament pel visor
            JasperViewer.viewReport(print, false);
            System.out.println("Informe visualitzat pel visor d'informes");
        } catch (SQLException | JRException  ex) {
            System.out.println("Error mentre es genera informe!");
            Utils.infoError(ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                    con = null;
                }
            } catch (SQLException ex) {
                System.out.println("Error en tancar la connexió");
                Utils.infoError(ex);
            }
        }
    }

}
