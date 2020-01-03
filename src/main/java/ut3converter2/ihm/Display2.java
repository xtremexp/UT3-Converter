/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Display2.java
 *
 * Created on 5 déc. 2008, 10:15:15
 */

package ut3converter2.ihm;

import com.jhlabs.image.BlurFilter;
import java.awt.CardLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;
import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.effect.BufferedImageOpEffect;
import org.jdesktop.jxlayer.plaf.ext.LockableUI;
import ut3converter2.Main;
import ut3converter2.RegistryReader;
import ut3converter2.convert.Level.ut2004.U2ToUT2k4T3DConv;
import ut3converter2.convert.U2ToUT3Conv;
import ut3converter2.convert.UT2k4ToUT3Conv;
import ut3converter2.convert.U1UT99ToUT2k4Conv;
import ut3converter2.UTGames;
import ut3converter2.convert.DeusExToUT2k4Conv;
import ut3converter2.convert.StaticMesh.BrushtoAse;
import ut3converter2.convert.StaticMesh.SMT3DtoAse;
import ut3converter2.convert.Mesh.UCModelConverter;
import ut3converter2.convert.Mesh.UModelConverter;
import ut3converter2.convert.U1UT99ToUT3Conv;
import ut3converter2.export.GlobalUCCExporter;
import ut3converter2.export.LevelExporter;
import ut3converter2.export.UModelExporter;
import ut3converter2.identification.TexturesIdentificator;
import ut3converter2.ihm.map.IHMMapConverter;
import ut3converter2.ihm.t3d.T3DLvlConvToUT2004;
import ut3converter2.tools.GlobalStats;
import ut3converter2.tools.SongsFinder;
import ut3converter2.tools.T3DLevelAnalyser;
import ut3converter2.tools.T3DLevelScaler;
import ut3converter2.tools.UTPackageDat;

/**
 *
 * @author Hyperion
 */
public class Display2 extends javax.swing.JFrame {

    public CardLayout cl;
    RedirectFrame rf;
    ArrayList al = new ArrayList();
    private JXLayer<JComponent> layer;
    private LockableUI blurUI = new LockableUI(new BufferedImageOpEffect(new BlurFilter()));
    public String lbltsksub="";
    public String lbltskmain="";
    public UT2k4ToUT3Conv mcut2004;
    public U2ToUT3Conv mcu2;
    public U1UT99ToUT2k4Conv mcut99;
    public int utgame;
    public int output_game;
    public GlobalStats gs;

    /** Creates new form Display2 */
    public Display2() {
        initComponents();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //jp2 = new JProgress3();
        //jp2.setVisible(true);
        if(Main.config.isFirstRun())
        {
            JOptionPane.showMessageDialog(this, "<html>Welcome to UT3 converter!<br>Please take some time to check the install paths<br>" +
                    "of the UT games detected you have in the following list:");
            autoDetectUTPaths();
            Main.config.setFirstRun("false");
        }
       try {
            //NewJInternalFrame njj = new NewJInternalFrame();
            rf = new RedirectFrame(true, true, "log.txt", 600, (int)screenSize.getHeight()-100, JInternalFrame.HIDE_ON_CLOSE);

        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Display2.class.getName()).log(Level.SEVERE, null, ex);
        }
            //njj.setLocation(300, 50);
            //njj.setVisible(true);

        rf.setLocation(0, 0);
        jDP.setSize(450, 350);
        jDP.add(rf);
    }


    

    //VCTF-ONP-Test
    private String getUT3PacOutputName(String mapname)
    {
        String tmp="";
        String t[] = mapname.split("-");
        if(t.length==2)
        {
            t[1] = t[1].replaceAll("\\]", "");
            t[1] = t[1].replaceAll("\\[", "");
            return t[1];
        }
        else if(t.length==3)
        {
            t[2] = t[2].replaceAll("\\]", "");
            t[2] = t[2].replaceAll("\\[", "");
            return t[1]+"-"+t[2];
        }
        return tmp;
    }

    private String getUT3OutputName(String utxxmapname)
    {
        String in = utxxmapname.split("\\-")[0];
        String t[] = utxxmapname.split("\\-");
        String core="";
        if(t.length==2)
        {
            core = t[1];
        }
        else if(t.length==3)
        {
            core = t[1]+"-"+t[2];
        }
        core = core.split("\\.")[0];
        core = core.replaceAll("\\]", "");
        core = core.replaceAll("\\[", "");
        if(in.equalsIgnoreCase("ons"))
        {
            return "WAR-"+core;
        }
        else if(in.equalsIgnoreCase("as"))
        {
            return "VCTF-"+core;
        }
        else if(in.equalsIgnoreCase("ctf"))
        {
            return "CTF-"+core;
        }
        else if(in.equalsIgnoreCase("vctf"))
        {
            return "VCTF-"+core;
        }
        else if(in.equalsIgnoreCase("dm"))
        {
            return "DM-"+core;
        }
        else if(in.equalsIgnoreCase("dm"))
        {
            return "TDM-"+core;
        }
        else
        {
            return "DM-"+core;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDP = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        JMnFile = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        jmnDEtoUT2k4 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMnU1Map = new javax.swing.JMenu();
        jMnU1toUT2k4 = new javax.swing.JMenuItem();
        jmnU1toUT3 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMnU1ExpT3D = new javax.swing.JMenuItem();
        jMnU2Map = new javax.swing.JMenu();
        jMenuItem17 = new javax.swing.JMenuItem();
        jmnU2toUT3 = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMnU2ExpT3D = new javax.swing.JMenuItem();
        jMnUTMap = new javax.swing.JMenu();
        jMnUT99UT2k4 = new javax.swing.JMenuItem();
        jMnUT99UT3 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMnUTExpT3d = new javax.swing.JMenuItem();
        jMnUT2k4Map = new javax.swing.JMenu();
        jMnUT2k4toUT3 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMnUT2k4ExpT3d = new javax.swing.JMenuItem();
        jMnUT3Map = new javax.swing.JMenu();
        jMnUT3ExpT3d = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMnExit = new javax.swing.JMenuItem();
        JMnT3DFile = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMnFilterT3DLevel = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMnScaleMap = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jmnT3DU1ToUT2k4 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu12 = new javax.swing.JMenu();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu9 = new javax.swing.JMenu();
        jmConvT3DSm2Ase = new javax.swing.JMenuItem();
        JMnUFile = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jmnExportUC = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        JMnOptions = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jchkShowOutput = new javax.swing.JCheckBoxMenuItem();
        JMnTools = new javax.swing.JMenu();
        jMenuItem15 = new javax.swing.JMenuItem();
        JMnHelp = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("UT3 Converter 2");
        setBackground(new java.awt.Color(102, 102, 255));
        getContentPane().add(jDP, java.awt.BorderLayout.CENTER);

        JMnFile.setText("File");

        jMenu1.setText("Deus Ex");

        jmnDEtoUT2k4.setText("Convert to UT2004 ...");
        jmnDEtoUT2k4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnDEtoUT2k4ActionPerformed(evt);
            }
        });
        jMenu1.add(jmnDEtoUT2k4);

        jMenuItem20.setText("Export T3D Level File ...");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem20);

        JMnFile.add(jMenu1);

        jMnU1Map.setText("Unreal 1 Map");

        jMnU1toUT2k4.setText("Convert to UT2004 ...");
        jMnU1toUT2k4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMnU1toUT2k4ActionPerformed(evt);
            }
        });
        jMnU1Map.add(jMnU1toUT2k4);

        jmnU1toUT3.setText("Convert to UT3 ...");
        jmnU1toUT3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnU1toUT3ActionPerformed(evt);
            }
        });
        jMnU1Map.add(jmnU1toUT3);
        jMnU1Map.add(jSeparator5);

        jMnU1ExpT3D.setText("Export T3D Level File ...");
        jMnU1ExpT3D.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMnU1ExpT3DActionPerformed(evt);
            }
        });
        jMnU1Map.add(jMnU1ExpT3D);

        JMnFile.add(jMnU1Map);

        jMnU2Map.setText("Unreal 2 Map");

        jMenuItem17.setText("Convert to UT2004 ...");
        jMenuItem17.setEnabled(false);
        jMnU2Map.add(jMenuItem17);

        jmnU2toUT3.setText("Convert to UT3 ...");
        jmnU2toUT3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnU2toUT3ActionPerformed(evt);
            }
        });
        jMnU2Map.add(jmnU2toUT3);
        jMnU2Map.add(jSeparator4);

        jMnU2ExpT3D.setText("Export T3D Level File ...");
        jMnU2ExpT3D.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMnU2ExpT3DActionPerformed(evt);
            }
        });
        jMnU2Map.add(jMnU2ExpT3D);

        JMnFile.add(jMnU2Map);

        jMnUTMap.setText("UT Map");

        jMnUT99UT2k4.setText("Convert to UT2004 ...");
        jMnUT99UT2k4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMnUT99UT2k4ActionPerformed(evt);
            }
        });
        jMnUTMap.add(jMnUT99UT2k4);

        jMnUT99UT3.setText("Convert to UT3 ...");
        jMnUT99UT3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMnUT99UT3ActionPerformed(evt);
            }
        });
        jMnUTMap.add(jMnUT99UT3);
        jMnUTMap.add(jSeparator2);

        jMnUTExpT3d.setText("Export to T3D Level file ...");
        jMnUTExpT3d.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMnUTExpT3dActionPerformed(evt);
            }
        });
        jMnUTMap.add(jMnUTExpT3d);

        JMnFile.add(jMnUTMap);

        jMnUT2k4Map.setText("UT2004 Map");

        jMnUT2k4toUT3.setText("Convert to UT3 ...");
        jMnUT2k4toUT3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMnUT2k4toUT3ActionPerformed(evt);
            }
        });
        jMnUT2k4Map.add(jMnUT2k4toUT3);
        jMnUT2k4Map.add(jSeparator3);

        jMnUT2k4ExpT3d.setText("Export T3D Level File ...");
        jMnUT2k4ExpT3d.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMnUT2k4ExpT3dActionPerformed(evt);
            }
        });
        jMnUT2k4Map.add(jMnUT2k4ExpT3d);

        JMnFile.add(jMnUT2k4Map);

        jMnUT3Map.setText("UT3 Map");

        jMnUT3ExpT3d.setText("Export T3D Level File ...");
        jMnUT3ExpT3d.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMnUT3ExpT3dActionPerformed(evt);
            }
        });
        jMnUT3Map.add(jMnUT3ExpT3d);

        JMnFile.add(jMnUT3Map);
        JMnFile.add(jSeparator1);

        jMnExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jMnExit.setText("Exit");
        jMnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMnExitActionPerformed(evt);
            }
        });
        JMnFile.add(jMnExit);

        jMenuBar1.add(JMnFile);

        JMnT3DFile.setText("T3D File");

        jMenu5.setText("T3D Level");
        jMenu5.setToolTipText("<html>A T3D Level is a text file that contains informations about all actors used in a Ux/Utx map<br>\nCan be found using File->Export in any Unreal Editor<br>\n</html>");

        jMnFilterT3DLevel.setText("Actor Filter");
        jMnFilterT3DLevel.setToolTipText("<html>Keeps only actors with specified name from T3D File.</html>");
        jMnFilterT3DLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMnFilterT3DLevelActionPerformed(evt);
            }
        });
        jMenu5.add(jMnFilterT3DLevel);

        jMenuItem6.setText("Actors Usage");
        jMenuItem6.setToolTipText("<html>Shows actors used in T3D map.</html>");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem6);

        jMnScaleMap.setText("Scale Map");
        jMnScaleMap.setToolTipText("Scales up or down any T3D Level file.");
        jMnScaleMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMnScaleMapActionPerformed(evt);
            }
        });
        jMenu5.add(jMnScaleMap);

        jMenuItem7.setText("Texture Usage");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem7);

        jMenu3.setText("U1/UT Map");
        jMenu3.setEnabled(false);

        jmnT3DU1ToUT2k4.setText("Convert to UT2004");
        jmnT3DU1ToUT2k4.setToolTipText("<html>\nConverts UT/U1 maps <br>\nfrom T3D level files\n</html>");
        jmnT3DU1ToUT2k4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnT3DU1ToUT2k4ActionPerformed(evt);
            }
        });
        jMenu3.add(jmnT3DU1ToUT2k4);

        jMenuItem9.setText("Musics Usage");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem9);

        jMenu5.add(jMenu3);

        jMenu12.setText("Unreal 2 Map");
        jMenu12.setEnabled(false);

        jMenuItem14.setText("Convert to UT2004");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem14);

        jMenu5.add(jMenu12);

        JMnT3DFile.add(jMenu5);

        jMenu6.setText("T3D Brush");

        jMenuItem4.setText("Convert to StaticMesh (.ase) ...");
        jMenuItem4.setToolTipText("<html>\nConverts T3D Brushes into StaticMeshes<br>\n</html>");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem4);

        JMnT3DFile.add(jMenu6);

        jMenu9.setText("T3D StaticMesh");

        jmConvT3DSm2Ase.setText("Convert to .ase (3D ASCII Scene)");
        jmConvT3DSm2Ase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmConvT3DSm2AseActionPerformed(evt);
            }
        });
        jMenu9.add(jmConvT3DSm2Ase);

        JMnT3DFile.add(jMenu9);

        jMenuBar1.add(JMnT3DFile);

        JMnUFile.setText("U File");
        JMnUFile.setEnabled(false);

        jMenuItem8.setText("Convert decos");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        JMnUFile.add(jMenuItem8);

        jMenuItem13.setText("Testons");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        JMnUFile.add(jMenuItem13);

        jmnExportUC.setText("Export uc files ...");
        jmnExportUC.setToolTipText("<html> Export Unreal Script files<br>\nfrom select .u file</html>");
        jmnExportUC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnExportUCActionPerformed(evt);
            }
        });
        JMnUFile.add(jmnExportUC);

        jMenuItem10.setText("Export 3D Model ...");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        JMnUFile.add(jMenuItem10);

        jMenuBar1.add(JMnUFile);

        JMnOptions.setText("Options");

        jMenuItem1.setText("Settings");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        JMnOptions.add(jMenuItem1);

        jchkShowOutput.setSelected(true);
        jchkShowOutput.setText("Show output window");
        jchkShowOutput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchkShowOutputActionPerformed(evt);
            }
        });
        JMnOptions.add(jchkShowOutput);

        jMenuBar1.add(JMnOptions);

        JMnTools.setText("Tools");

        jMenuItem15.setText("CreateDat");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        JMnTools.add(jMenuItem15);

        jMenuBar1.add(JMnTools);

        JMnHelp.setText("?");

        jMenuItem2.setText("Check updates ...");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        JMnHelp.add(jMenuItem2);

        jMenuItem11.setText("About");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        JMnHelp.add(jMenuItem11);

        jMenuBar1.add(JMnHelp);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMnExitActionPerformed

        System.exit(9);
}//GEN-LAST:event_jMnExitActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed

            Settings cp = new Settings(this, Main.config);
            cp.setLocationRelativeTo(null);
            cp.setVisible(true);

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    /**
     * Detect UT install paths at first start.
     */
    private void autoDetectUTPaths()
    {
                try {

            String tmp= RegistryReader.getUTxFolderPath(UTGames.U1);
            if(tmp!=null)
            {
                if(new File(tmp).exists()){ Main.config.setU1RootFolder(new File(tmp));}
            }

            tmp= RegistryReader.getUTxFolderPath(UTGames.UT99);
            if(tmp!=null)
            {
                if(new File(tmp).exists()){ Main.config.setUT99RootFolder(new File(tmp));}
            }
            tmp= RegistryReader.getUTxFolderPath(UTGames.UT2003);
            if(tmp!=null)
            {
                if(new File(tmp).exists()){ Main.config.setUT2003RootFolder(new File(tmp));}
            }
            tmp=RegistryReader.getUTxFolderPath(UTGames.UT2004);
            if(tmp!=null)
            {
                if(new File(tmp).exists()){ Main.config.setUT2004RootFolder(new File(tmp));}
            }
            tmp=RegistryReader.getUTxFolderPath(UTGames.DeusEx);
            if(tmp!=null)
            {
                if(new File(tmp).exists()){ Main.config.setDeuxExRootFolder(new File(tmp));}
            }

            Settings cp = new Settings(this, Main.config);
            //cp
            cp.setLocationRelativeTo(null);
            cp.setVisible(true);
            //cl.show(jxpcard, "settings");
        } catch (IOException ex) {
            Logger.getLogger(Display2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void jmnT3DU1ToUT2k4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnT3DU1ToUT2k4ActionPerformed
        //JMenuItem jmn = (JMenuItem) evt.getSource();

        //System.out.println(jmn.getClass().toString());
        File in = FileChooser2.getFile("Select UT/U1 File", "U1/UT T3D File", new String[]{"t3d"});
        if(in!=null)
        {
            T3DLvlConvToUT2004 tut = new T3DLvlConvToUT2004(this, in);
            tut.setLocationRelativeTo(null);
            tut.setVisible(true);
        }
}//GEN-LAST:event_jmnT3DU1ToUT2k4ActionPerformed

    private void jMnFilterT3DLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMnFilterT3DLevelActionPerformed

            File t3dfile = FileChooser2.getFile("Select T3D File", "T3D FIles", new String[]{"t3d"});
            if(t3dfile!=null)
            {
                JDActorFilter jda = new JDActorFilter(this, t3dfile);
                jda.setLocationRelativeTo(null);
                jda.setVisible(true);
                /*
                File outfile = new File(t3dfile.getParent() + "\\out.t3d");
                String classname = JOptionPane.showInputDialog("Class Name? (e.g.: \"Brush,Mover\")");
                if(classname!=null)
                {
                    T3DLevelClassFilter tf = new T3DLevelClassFilter(t3dfile, outfile, classname);
                    tf.filter();
                    JOptionPane.showMessageDialog(null,"Filtered to: "+outfile.getAbsolutePath());
                }*/
            }
            

    }//GEN-LAST:event_jMnFilterT3DLevelActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
            File t3dfiles[] = FileChooser2.getFiles("Select T3D File", "T3D FIles", new String[]{"t3d"});
            if(t3dfiles!=null)
            {
                T3DLevelAnalyser ta = null;
            try {
                ta = new T3DLevelAnalyser(t3dfiles);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Display2.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Display2.class.getName()).log(Level.SEVERE, null, ex);
            }
                JFT3DLvlAnalysis jf = new JFT3DLvlAnalysis(ta.getActorClassesUsed());
                jf.setLocationRelativeTo(null);
                jf.setVisible(true);
            }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed

        File t3dfile = FileChooser2.getFile("Select T3D File", "T3D FIles", new String[]{"t3d"});
            if(t3dfile!=null)
            {
                TexturesIdentificator ti = new TexturesIdentificator(new ArrayList(),null, TexturesIdentificator.textype_bmp);
            try {
                ti.identifyLvlTextures(t3dfile);
                //TODO RESTORE BACK
                //System.out.println(ti.getPackageNamesUsed());
            } catch (Exception ex) {
                Logger.getLogger(Display2.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed

        File f[] = FileChooser2.getFiles("Select T3D Files", "T3D Map File", new String[]{"t3d"});
        SongsFinder sf = new SongsFinder(f);
        try {
            sf.getSongUsed();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Display2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Display2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jchkShowOutputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchkShowOutputActionPerformed

        if(rf.isVisible())
        {
            rf.setVisible(false);
        }
        else
        {
            rf.setVisible(true);
        }
        
    }//GEN-LAST:event_jchkShowOutputActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed

        File t3dbrushfile = FileChooser2.getFile("Select T3D Brush file", "T3D Brush File.t3d", new String[]{"t3d"});
        if(t3dbrushfile!=null)
        {
            BrushtoAse b2a = new BrushtoAse(t3dbrushfile);
            try {
                b2a.loadBrushData();
                b2a.export2Ase();
                JOptionPane.showMessageDialog(this, "Done!");
            } catch (IOException ex) {
                Logger.getLogger(Display2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jmConvT3DSm2AseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmConvT3DSm2AseActionPerformed

        File[] t3dsmfile = FileChooser2.getFiles("Select T3D Sm files you want to convert", "T3D Staticmeshes files", new String[]{"t3d"});
        if(t3dsmfile!=null)
        {
            try {
                SMT3DtoAse smt = new SMT3DtoAse(t3dsmfile,true);
                smt.setBDispResult(true);
                smt.export2Ase();
            } catch (IOException ex) {
                Logger.getLogger(Display2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
}//GEN-LAST:event_jmConvT3DSm2AseActionPerformed

    private void jmnExportUCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnExportUCActionPerformed
        try {

            String inputgame = (String) JOptionPane.showInputDialog(null, "Select the UT input game", "Select game", JOptionPane.OK_CANCEL_OPTION, null,  UTGames.getAllGamesNames(),null);
            System.out.println(inputgame);
            File inputfile = FileChooser2.getFile("Select U File", "U File", new String[]{"u"});
            GlobalUCCExporter gue = new GlobalUCCExporter(inputfile, Main.config, Integer.valueOf(inputgame.split("-")[1]));
            gue.exportUCFilesTo(inputfile.getParentFile());
        } catch (IOException ex) {
            Logger.getLogger(Display2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Display2.class.getName()).log(Level.SEVERE, null, ex);
        }
}//GEN-LAST:event_jmnExportUCActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        try {

            File inputufile = FileChooser2.getFile("Select U File", "U File", new String[]{"u"});
            UModelExporter ume = new UModelExporter(inputufile);
            ume.ExportTo(new File("G:\\XXX\\"));
        } catch (IOException ex) {
            Logger.getLogger(Display2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Display2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed

        String decpacname = JOptionPane.showInputDialog("Deco Package Name?");
        File inputufiles[] = FileChooser2.getFiles("Select U File", "U File", new String[]{"u"});
        String inputgame = (String) JOptionPane.showInputDialog(null, "Select the UT input game", "Select game", JOptionPane.OK_CANCEL_OPTION, null,  UTGames.getAllGamesNames(),null);
        String outputgame = (String) JOptionPane.showInputDialog(null, "Select the UT output game", "Select game", JOptionPane.OK_CANCEL_OPTION, null,  UTGames.getAllGamesNames(),null);

        int ingamenum = Integer.valueOf(inputgame.split("\\-")[1]);
        int outgamenum = Integer.valueOf(outputgame.split("\\-")[1]);


            UModelConverter umc = new UModelConverter(inputufiles, decpacname, ingamenum, outgamenum);
            try {
                umc.convert();
            } catch (IOException ex) {
                Logger.getLogger(Display2.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Display2.class.getName()).log(Level.SEVERE, null, ex);
            }

        
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed

        File inputufiles[] = FileChooser2.getFiles("Select Unreal Files", "UC Deco Files", new String[]{"uc"});
        UCModelConverter ucmc = new UCModelConverter(inputufiles, new File("G:\\XXX\\"));
        try {
            ucmc.convert();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Display2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Display2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed

        File inputu2file = FileChooser2.getFile("Select Unreal2 T3D File", "U2 T3D File", new String[]{"t3d"});
        U2ToUT2k4T3DConv u2t = new U2ToUT2k4T3DConv(inputu2file);
        try {
            u2t.convert();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Display2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Display2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        try {

            About abt = new About(this, true, new Credits(),Main.numversion);
            abt.setVisible(true);
            //JOptionPane.showMessageDialog(null, "<html>UT3 Converter V"+Main.numversion+"<br> by Thomas \"Hyperion\" P.</html>");
        } catch (URISyntaxException ex) {
            Logger.getLogger(Display2.class.getName()).log(Level.SEVERE, null, ex);
        }
        //JOptionPane.showMessageDialog(null, "<html>UT3 Converter V"+Main.numversion+"<br> by Thomas \"Hyperion\" P.</html>");
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMnU1ExpT3DActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMnU1ExpT3DActionPerformed

        if(bHasValidUTFolder(UTGames.U1))
        {
            File u1file = FileChooser2.getFile("Select Unreal 1 Map", "U1 Map (.unr)", new String[]{"unr"},Main.config.getMapFolder(UTGames.U1));
            if(u1file!=null)
            {
                LevelExporter le = new LevelExporter(u1file,UTGames.U1);
                le.setBDispResult(true);
                le.setBShowLog(true);
                try {
                    le.exportT3DMap();
                } catch (Exception e) {
                }
            }
        }
        else
        {
            showMsgHasToSetSettings(UTGames.U1);
        }
        
}//GEN-LAST:event_jMnU1ExpT3DActionPerformed

    private void jMnU2ExpT3DActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMnU2ExpT3DActionPerformed

        if(bHasValidUTFolder(UTGames.U2))
        {
            File u1file = FileChooser2.getFile("Select Unreal 2 Map", "U2 Map (.un2)", new String[]{"un2"},Main.config.getMapFolder(UTGames.U2));
            if(u1file!=null)
            {
                LevelExporter le = new LevelExporter(u1file,UTGames.U2);
                le.setBDispResult(true);
                le.setBShowLog(true);
                try {
                    le.exportT3DMap();
                } catch (Exception e) {
                }
            }
        }
        else
        {
            showMsgHasToSetSettings(UTGames.U2);
        }
        
    }//GEN-LAST:event_jMnU2ExpT3DActionPerformed

    private void jMnUTExpT3dActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMnUTExpT3dActionPerformed

        if(bHasValidUTFolder(UTGames.UT99))
        {
            File u1file = FileChooser2.getFile("Select UT Map", "UT Map (.unr)", new String[]{"unr"},Main.config.getMapFolder(UTGames.UT99));
            if(u1file!=null)
            {
                LevelExporter le = new LevelExporter(u1file,UTGames.UT99);
                le.setBDispResult(true);
                le.setBShowLog(true);
                try {
                    le.exportT3DMap();
                } catch (Exception e) {
                }
            }
        }
        else
        {
            showMsgHasToSetSettings(UTGames.UT99);
        }
        
}//GEN-LAST:event_jMnUTExpT3dActionPerformed

    private void jMnUT2k4ExpT3dActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMnUT2k4ExpT3dActionPerformed
        if(bHasValidUTFolder(UTGames.UT2004))
        {
            File u1file = FileChooser2.getFile("Select UT2004 Map", "UT2004 Map (.ut2)", new String[]{"ut2"},Main.config.getUT2004MapFolder());
            if(u1file!=null)
            {
                LevelExporter le = new LevelExporter(u1file,UTGames.UT2004);
                le.setBDispResult(true);
                le.setBShowLog(true);
                try {
                    le.exportT3DMap();
                } catch (Exception e) {
                }
            }
        }
        else
        {
            showMsgHasToSetSettings(UTGames.UT2004);;
        }

    }//GEN-LAST:event_jMnUT2k4ExpT3dActionPerformed

    private void jMnUT2k4toUT3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMnUT2k4toUT3ActionPerformed
        
        if(bHasValidUTFolder(UTGames.UT2004))
        {
            File ut2004file = FileChooser2.getFile("Select UT2004 Map", "UT2004 Map (.ut2)", new String[]{"ut2"},Main.config.getUT2004MapFolder());
            if(ut2004file!=null)
            {
                UT2k4ToUT3Conv mc = new UT2k4ToUT3Conv(ut2004file, UTGames.UT2004, UTGames.UT3);
                new IHMMapConverter(mc);
            }
        }
        else
        {
            showMsgHasToSetSettings(UTGames.UT2004);
        }

    }//GEN-LAST:event_jMnUT2k4toUT3ActionPerformed

    private void jMnScaleMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMnScaleMapActionPerformed

        File t3dlevel = FileChooser2.getFile("Select T3D File", "T3D Level file", new String[]{"t3d"});
        if(t3dlevel!=null)
        {
            File t3dout = new File(t3dlevel.getParent()+"//scaledmap.t3d");
            double scalefactor = Double.valueOf(JOptionPane.showInputDialog("Scale Factor? (e.g: \"1.25\")"));
            T3DLevelScaler tlc = new T3DLevelScaler(t3dlevel, t3dout, scalefactor);
            tlc.setBShowLog(true);
            tlc.setBDispResult(true);
            try {
                tlc.scale();
            } catch (FileNotFoundException ex) {
                System.out.println(ex.getMessage());
                Logger.getLogger(Display2.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                Logger.getLogger(Display2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }//GEN-LAST:event_jMnScaleMapActionPerformed


    private void jMnUT99UT2k4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMnUT99UT2k4ActionPerformed

        if(bHasValidUTFolder(UTGames.UT99))
        {
            File ut99file = FileChooser2.getFile("Select UT99 Map", "UT99 Map (.unr)", new String[]{"unr"},Main.config.getMapFolder(UTGames.UT99));
            if(ut99file!=null)
            {
                U1UT99ToUT2k4Conv mc = new U1UT99ToUT2k4Conv(ut99file, UTGames.UT99, UTGames.UT2004);
                new IHMMapConverter(mc);
            }
        }
        else
        {
            showMsgHasToSetSettings(UTGames.UT99);
        }
    }//GEN-LAST:event_jMnUT99UT2k4ActionPerformed

    private void showMsgHasToSetSettings(int inputgame)
    {
        JOptionPane.showMessageDialog(null, "<html>You have invalid or unset root folder of your "
                +UTGames.getUTGame(inputgame)+" game!<br>You need to set it in Settings panel.</html>", "Error", JOptionPane.ERROR_MESSAGE);
        Settings cp = new Settings(this, Main.config);
        cp.setLocationRelativeTo(null);
        cp.setVisible(true);
    }

    private boolean bHasValidUTFolder(int inputgame)
    {
        File rootfolder;
        if(inputgame==UTGames.UT99)
        {
            rootfolder = Main.config.getUT99RootFolder();
            if(rootfolder!=null){if(!rootfolder.exists()){return false;}}
            else{return false;}
        }
        else if(inputgame==UTGames.U1)
        {
            rootfolder = Main.config.getU1RootFolder();
            if(rootfolder!=null){if(!rootfolder.exists()){return false;}}
            else{return false;}
        }
        else if(inputgame==UTGames.U2)
        {
            rootfolder = Main.config.getU2RootFolder();
            if(rootfolder!=null){if(!rootfolder.exists()){return false;}}
            else{return false;}
        }
        else if(inputgame==UTGames.UT2003)
        {
            rootfolder = Main.config.getUT2003RootFolder();
            if(rootfolder!=null){if(!rootfolder.exists()){return false;}}
            else{return false;}
        }
        else if(inputgame==UTGames.UT2004)
        {
            rootfolder = Main.config.getUT2004RootFolder();
            if(rootfolder!=null){if(!rootfolder.exists()){return false;}}
            else{return false;}
        }
        else if(inputgame==UTGames.DeusEx)
        {
            rootfolder = Main.config.getDeuxExRootFolder();
            System.out.println(rootfolder);
            if(rootfolder!=null){if(!rootfolder.exists()){return false;}}
            else{return false;}
        }

        return true;
    }

    
    
    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed

        File utxfiles[] = FileChooser2.getFiles("Select textures files", "Texture files (.utx)", new String[]{"utx"});
        File outputfolder = FileChooser2.getFolder("Select outputfolder");
        File datfileout = new File("C:\\Users\\Hyperion\\Documents\\Test\\myfile.txt");
        UTPackageDat upd = new UTPackageDat(utxfiles, outputfolder, datfileout);
        try {
            upd.createDatFile();
        } catch (IOException ex) {
            Logger.getLogger(Display2.class.getName()).log(Level.SEVERE, null, ex);
        }catch (InterruptedException ex) {
            Logger.getLogger(Display2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed

        if(bHasValidUTFolder(UTGames.DeusEx))
        {
            File u1file = FileChooser2.getFile("Select Deus Ex Map", "Deus Ex Map (.dx)", new String[]{"dx"},Main.config.getMapFolder(UTGames.DeusEx));
            if(u1file!=null)
            {
                LevelExporter le = new LevelExporter(u1file,UTGames.DeusEx);
                le.setBDispResult(true);
                le.setBShowLog(true);
                try {
                    le.exportT3DMap();
                } catch (Exception e) {
                }
            }
        }
        else
        {
            showMsgHasToSetSettings(UTGames.DeusEx);
        }
        
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jmnDEtoUT2k4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnDEtoUT2k4ActionPerformed

        if(bHasValidUTFolder(UTGames.DeusEx))
        {
            File deusexfile = FileChooser2.getFile("Select Deus Ex Map", "Deus Ex Map (.dx)", new String[]{"dx"},Main.config.getMapFolder(UTGames.DeusEx));
            if(deusexfile!=null)
            {
                DeusExToUT2k4Conv mc = new DeusExToUT2k4Conv(deusexfile, UTGames.DeusEx, UTGames.UT2004);
                new IHMMapConverter(mc);
            }
        }
        else
        {
            showMsgHasToSetSettings(UTGames.DeusEx);
        }
        
    }//GEN-LAST:event_jmnDEtoUT2k4ActionPerformed

    private void open(URI uri) {
        if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                try {
                        desktop.browse(uri);
                } catch (IOException e) {
                 
                }
        } else {
             
        }
    }
    
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            open(new URI("http://utforums.epicgames.com/showthread.php?p=25131566"));
        } catch (URISyntaxException ex) {
            Logger.getLogger(Display2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMnUT99UT3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMnUT99UT3ActionPerformed
        
        if(bHasValidUTFolder(UTGames.UT99))
        {
            File ut99file = FileChooser2.getFile("Select UT99 Map", "UT99 Map (.unr)", new String[]{"unr"},Main.config.getMapFolder(UTGames.UT99));
            if(ut99file!=null)
            {
                U1UT99ToUT3Conv mc = new U1UT99ToUT3Conv(ut99file, UTGames.UT99, UTGames.UT3);
                new IHMMapConverter(mc);
            }
        }
        else
        {
            showMsgHasToSetSettings(UTGames.UT99);
        }

    }//GEN-LAST:event_jMnUT99UT3ActionPerformed

    private void jMnU1toUT2k4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMnU1toUT2k4ActionPerformed
        if(bHasValidUTFolder(UTGames.U1))
        {
            File u1file = FileChooser2.getFile("Select "+UTGames.getUTGame(UTGames.U1)+" Map", UTGames.getUTGameShort(UTGames.U1)+" Map (.unr)", new String[]{"unr"},Main.config.getMapFolder(UTGames.U1));
            if(u1file!=null)
            {
                U1UT99ToUT2k4Conv mc = new U1UT99ToUT2k4Conv(u1file, UTGames.U1, UTGames.UT2004);
                new IHMMapConverter(mc);
            }
        }
        else
        {
            showMsgHasToSetSettings(UTGames.U1);
        }
    }//GEN-LAST:event_jMnU1toUT2k4ActionPerformed

    private void jmnU1toUT3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnU1toUT3ActionPerformed
        if(bHasValidUTFolder(UTGames.U1))
        {
            File u1file = FileChooser2.getFile("Select "+UTGames.getUTGame(UTGames.U1)+" Map", UTGames.getUTGameShort(UTGames.U1)+" Map (.unr)", new String[]{"unr"},Main.config.getMapFolder(UTGames.U1));
            if(u1file!=null)
            {
                U1UT99ToUT3Conv mc = new U1UT99ToUT3Conv(u1file, UTGames.U1, UTGames.UT3);
                new IHMMapConverter(mc);
            }
        }
        else
        {
            showMsgHasToSetSettings(UTGames.U1);
        }
    }//GEN-LAST:event_jmnU1toUT3ActionPerformed

    private void jmnU2toUT3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnU2toUT3ActionPerformed
        if(bHasValidUTFolder(UTGames.U2))
        {
            File u1file = FileChooser2.getFile("Select "+UTGames.getUTGame(UTGames.U2)+" Map", UTGames.getUTGameShort(UTGames.U2)+" Map (.un2)", new String[]{"un2"},Main.config.getMapFolder(UTGames.U2));
            if(u1file!=null)
            {
                U2ToUT3Conv mc = new U2ToUT3Conv(u1file, UTGames.U2, UTGames.UT3);
                new IHMMapConverter(mc);
            }
        }
        else
        {
            showMsgHasToSetSettings(UTGames.U2);
        }
    }//GEN-LAST:event_jmnU2toUT3ActionPerformed

    private void jMnUT3ExpT3dActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMnUT3ExpT3dActionPerformed
        
        if(bHasValidUTFolder(UTGames.UT3))
        {
            File u1file = FileChooser2.getFile("Select U3 Map", "UT3 Map (.ut3)", new String[]{"ut3"},Main.config.getUT3MapFolder());
            if(u1file!=null)
            {
                LevelExporter le = new LevelExporter(u1file,UTGames.UT3);
                le.setBDispResult(true);
                le.setBShowLog(true);
                try {
                    le.exportT3DMap();
                } catch (Exception e) {
                }
            }
        }
        else
        {
            showMsgHasToSetSettings(UTGames.UT2004);;
        }
    }//GEN-LAST:event_jMnUT3ExpT3dActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Display2().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu JMnFile;
    private javax.swing.JMenu JMnHelp;
    private javax.swing.JMenu JMnOptions;
    private javax.swing.JMenu JMnT3DFile;
    private javax.swing.JMenu JMnTools;
    private javax.swing.JMenu JMnUFile;
    public static javax.swing.JDesktopPane jDP;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu12;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuItem jMnExit;
    private javax.swing.JMenuItem jMnFilterT3DLevel;
    private javax.swing.JMenuItem jMnScaleMap;
    private javax.swing.JMenuItem jMnU1ExpT3D;
    private javax.swing.JMenu jMnU1Map;
    private javax.swing.JMenuItem jMnU1toUT2k4;
    private javax.swing.JMenuItem jMnU2ExpT3D;
    private javax.swing.JMenu jMnU2Map;
    private javax.swing.JMenuItem jMnUT2k4ExpT3d;
    private javax.swing.JMenu jMnUT2k4Map;
    private javax.swing.JMenuItem jMnUT2k4toUT3;
    private javax.swing.JMenuItem jMnUT3ExpT3d;
    private javax.swing.JMenu jMnUT3Map;
    private javax.swing.JMenuItem jMnUT99UT2k4;
    private javax.swing.JMenuItem jMnUT99UT3;
    private javax.swing.JMenuItem jMnUTExpT3d;
    private javax.swing.JMenu jMnUTMap;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JCheckBoxMenuItem jchkShowOutput;
    private javax.swing.JMenuItem jmConvT3DSm2Ase;
    private javax.swing.JMenuItem jmnDEtoUT2k4;
    private javax.swing.JMenuItem jmnExportUC;
    private javax.swing.JMenuItem jmnT3DU1ToUT2k4;
    private javax.swing.JMenuItem jmnU1toUT3;
    private javax.swing.JMenuItem jmnU2toUT3;
    // End of variables declaration//GEN-END:variables

}
