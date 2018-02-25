/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2;

import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import com.nilo.plaf.nimrod.NimRODTheme;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ut3converter2.convert.MapConverter;
import ut3converter2.ihm.Display2;
import ut3converter2.ihm.RedirectFrame;
import ut3converter2.ihm.t3d.T3DCopyPasteConvertor;
import ut3converter2.tools.T3DBrushTranformPerm;

/**
 * This is where UT3 Converter starts
 * @author Hyperion
 */
public class Main {

    public static String numversion="0.25d - 20120316";
    public static String maintitle="UT3 Converter II - "+Main.numversion;
    public static Configuration config;
    public static File installfolder;
    public static Display2 d2;
    public static RedirectFrame rf;
    public static MapConverter mc;
    public static T3DBrushTranformPerm t;

    public static void main(String[] args) {
        System.out.println(maintitle);
        start();
    }


    public static void start() {
        addSystemTray();

        installfolder = Installation.getInstallDirectory(Main.class);
        File configfile = new File(installfolder.getAbsolutePath() + "/config.xml");

        try {
            config = new Configuration(configfile);

            NimRODLookAndFeel nlf = new NimRODLookAndFeel();

            UIManager.setLookAndFeel(nlf);
            NimRODTheme nt = new NimRODTheme();
            nt.setPrimary1(new Color(84, 84, 192));
            nt.setPrimary2(new Color(94, 94, 202));
            nt.setPrimary3(new Color(104, 104, 212));
            nt.setSecondary1(new Color(25, 30, 39));
            nt.setSecondary2(new Color(35, 40, 49));
            nt.setSecondary3(new Color(45, 50, 59));
            nt.setBlack(new Color(255, 255, 255));
            nt.setWhite(new Color(73, 72, 84));
            nt.setMenuOpacity(195);
            nt.setFrameOpacity(180);
            NimRODLookAndFeel NimRODLF = new NimRODLookAndFeel();
            NimRODLookAndFeel.setCurrentTheme(nt);
            UIManager.setLookAndFeel(NimRODLF);

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    d2 = new Display2();
                    d2.setTitle(maintitle);
                    GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    d2.setExtendedState(d2.getExtendedState() | Display2.MAXIMIZED_BOTH);
                    d2.pack();
                    Display2.setDefaultLookAndFeelDecorated(true);
                    d2.setVisible(true);
                }
            });

            // WAS AN ATTEMPT TO COPY CONVERT PASTE T3D DATA DIRECTLY
            //checkClipBoard();



            


        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    /**
     * WAS AN ATTEMPT TO COPY CONVERT PASTE T3D DATA DIRECTLY from one editor to another.
     */
    public static void checkClipBoard(){
        T3DCopyPasteConvertor tt = new T3DCopyPasteConvertor();
        tt.setClipboardContents("");
        String bufferold="";
        String buffernew="";
        String convertedbuffer="";
        while(true){
            try {
                Thread.sleep(4000L);
                buffernew = tt.getClipboardContents();
                if(!buffernew.equals(bufferold)){
                    if(tt.isT3DLevelData(buffernew)){
                        trayIcon.displayMessage("Ready to paste to UT3 Editor!", "?", TrayIcon.MessageType.INFO);
                        buffernew = tt.convertClipBoardToUT3(buffernew);
                        tt.setClipboardContents(buffernew);
                    }
                    
                    bufferold = buffernew;
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    boolean systemtraysupported = false;
    public static TrayIcon trayIcon;

    public static void addSystemTray() {
        if (SystemTray.isSupported()) {

            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage("images/ut3conv.png"); // On récupère l'image qui nous servira d'icone

            PopupMenu popup = new PopupMenu(); // Notre menu (clic droit sur l'icone systray)
            MenuItem mi_quit = new MenuItem("Exit UT3 Converter");
            mi_quit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            
            MenuItem mi_about = new MenuItem("About ...");
            mi_about.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null,"<html>Author: XtremeXp<br>Version: "+numversion,"About",JOptionPane.INFORMATION_MESSAGE);
                }
            });

            //Menu displayMenu = new Menu("Set ScaleFactor");

            popup.add(mi_about);
            //popup.add(displayMenu);
            popup.addSeparator();
            popup.add(mi_quit);

            trayIcon = new TrayIcon(image, "UT3 Converter", popup); // Création de l'icone systray

            ActionListener actionListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    trayIcon.displayMessage("Java 6 new feature !",
                            "Le System Tray en action !",
                            TrayIcon.MessageType.ERROR);
                    d2.setVisible(!d2.isVisible());
                }
            };
            trayIcon.setImageAutoSize(true);
            //trayIcon.addActionListener(actionListener);

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }

            trayIcon.displayMessage("UT3 Converter started!",
                            "Version: "+numversion,
                            TrayIcon.MessageType.INFO);
        }
    }




}
