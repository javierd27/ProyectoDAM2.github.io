/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import javax.help.*;
import java.net.URL;
import javax.swing.JComponent;
/**
 *
 * @author javier
 */
public class HoteliaHelp {
    private HelpSet helpSet;
    private HelpBroker helpBroker;
    private boolean initialized = false;
    
    public HoteliaHelp() {
        initializeHelp();
    }
    
    private void initializeHelp() {
        try {
            URL hsURL = HoteliaHelp.class.getResource("/resources/help/source/Hotelia.hs");
            
            if (hsURL == null) {
                System.err.println("Error: No se encontró el archivo Hotelia.hs");
                System.err.println("Ruta esperada: src/help/source/Hotelia.hs");
                return;
            }
            
            helpSet = new HelpSet(null, hsURL);
            helpBroker = helpSet.createHelpBroker();
            helpBroker.initPresentation();
            
            initialized = true;
            System.out.println("JavaHelp inicializado correctamente");
            
        } catch (Exception e) {
            System.err.println("Error al cargar JavaHelp: " + e.getMessage());
            e.printStackTrace();
            initialized = false;
        }
    }
    
    public void showHelp() {
        if (helpBroker != null && initialized) {
            try {
                helpBroker.setCurrentID("index");
                helpBroker.setDisplayed(true);
            } catch (BadIDException e) {
                System.err.println("Error:ID de ayuda no encontrado");
            }
        }
    }
    
    public void showHelp(String target) {
        if (helpBroker != null && initialized) {
            try {
                helpBroker.setCurrentID(target);
                helpBroker.setDisplayed(true);
            } catch (BadIDException e) {
                System.err.println("Error:ID de ayuda no encontrado: " + target);
            }
        }
    }
    
    public void enableHelpOnComponent(JComponent component, String target) {
        if (helpBroker != null && initialized) {
            try {
                helpBroker.enableHelpOnButton(component, target, helpSet);
            } catch (BadIDException e) {
                System.err.println("Error al vincular ayuda: " + e.getMessage());
            }
        }
    }
    
    public void enableF1Help(JComponent component, String target) {
        if (helpBroker != null && initialized) {
            try {
                helpBroker.enableHelpKey(component, target, helpSet);
            } catch (BadIDException e) {
                System.err.println("Error al vincular F1: " + e.getMessage());
            }
        }
    }
    
    public HelpBroker getHelpBroker() {
        return helpBroker;
    }
    
    public boolean isInitialized() {
        return initialized;
    }
}
