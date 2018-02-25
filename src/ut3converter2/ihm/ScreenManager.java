package ut3converter2.ihm;

import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;

/**
 * Cette classe est un gestionnaire d'�cran et permet de changer la r�solution
 * de l'�cran ou de mettre le mode plein-�cran.
 * 
 * @author Julien CHABLE (webmaster@neogamedev.com)
 * @version 1.0 20/06/2004
 */
public class ScreenManager {

    // Notre device (abstraction de la carte graphique ...) graphique sur lequel
    // nous allons effectuer les changements de r�solution, le plein-�cran, ...
    private GraphicsDevice gd;

    /**
     * Constructeur par d�faut. Initialise la variable du GraphicsDevice.
     *  
     */
    public ScreenManager() {
        // On r�cup�re l'environnement graphique du syst�me d'exploitation,
        // celui-ci va nous permettre de r�cup�rer un certain nombre d'�l�ments
        // sur la configuration graphique du syst�me ...
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        // On r�cup�re le device par d�faut ... vous pouvez r�cup�rer l'ensemble
        // des devices du syst�me avec la m�thode getScreenDevices(), le centre
        // de l'�cran avec getCenterPoint(), ...
        gd = ge.getDefaultScreenDevice();
    }

    /**
     * Passage de la fen�tre en mode plein �cran. Changement de mode graphique (r�solution et profondeur). Regarder la
     * classe DisplayMode pour plus d'informations sur les r�solutions d'�cran.
     * 
     * @param window
     *            La fen�tre � passer en plein-�cran.
     * @param displayMode
     *            Le mode graphique � appliquer.
     */
    public void setFullScreen(Frame window, DisplayMode displayMode) {
        if (window == null) return;
        // On ne veut pas des bords de la fen�tre
        window.setUndecorated(true);
        // La fen�tre n'est pas redimensionnable
        window.setResizable(false);
        // Passage en mode plein-�cran
        if (gd.isFullScreenSupported()) gd.setFullScreenWindow(window);
        // Changement de r�solution
        if (displayMode != null && gd.isDisplayChangeSupported()) {
            try {
                // Affectation du mode graphique au device graphique.
                gd.setDisplayMode(displayMode);
            } catch (Exception e) {
                // Do nothing
            }
        }
    }

    /**
     * Obtenir la fen�tre en plein-�cran.
     */
    public Window getFullScreen() {
        return gd.getFullScreenWindow();
    }
    
    /**
     * Obtenir le GraphicsDevice par d�faut de la fen�tre.
     */
    /*public GraphicsDevice getGraphicsDevice(){
        return gd;
    }*/

    /**
     * Arr�te le mode plein-�cran pour revenir en mode fen�tr� ...
     */
    public void restoreFullScreen() {
        // On r�cup�re notre fen�tre en mode plein-�cran
        Window window = getFullScreen();
        //
        if (window != null) window.dispose();
        // On arr�te le plein-�cran.
        gd.setFullScreenWindow(null);
    }
}