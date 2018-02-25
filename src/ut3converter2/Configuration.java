/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2;

import java.io.*;
import java.util.logging.*;
import javax.swing.JOptionPane;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Saves user unreal game paths.
 * @author Hyperion
 */
public class Configuration {

    File configxmlfile = null;
    Logger mclog = Logger.getLogger("log.txt");
    Document doc;
    Element rootelement;
    final static String folder_el="install_folders";
    final String relativefolder_utxextractor=File.separator+"bin"+File.separator+"utxextractor"+File.separator+"ExtractTextures.exe";
    private final String rootfolder = "rootfolder";

    public Configuration(File configxmlfile) {
        try {
            FileHandler fh = new FileHandler("log.html");
            fh.setFormatter(new myFormatter());
            mclog.addHandler(fh);
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.configxmlfile = configxmlfile;
        if(!configxmlfile.exists())
        {
            try {
                createConfigFile();
            } catch (IOException ex) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        SAXBuilder sxb = new SAXBuilder();
        try {
            this.doc = sxb.build(configxmlfile);
        } catch (JDOMException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.rootelement = doc.getRootElement();
    }


    /**
     * Returns all folders for the install paths of UT Games
     * @return
     */
    private Element getInstallFoldersEl()
    {
        return rootelement.getChild(Configuration.folder_el);
    }

    private void createConfigFile() throws IOException
    {
        BufferedWriter bw = new BufferedWriter(new FileWriter(configxmlfile));
        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        bw.write("<root>\n");
        bw.write("    <isfirstrun value=\"true\" />\n");
    bw.write("  <"+folder_el+">\n");
        bw.write("    <"+UTGames.DE_name_short+" rootfolder=\"none\" />\n");
        bw.write("    <"+UTGames.U1_name_short+" rootfolder=\"none\" />\n");
        bw.write("    <"+UTGames.U2_name_short+" rootfolder=\"none\" />\n");
        bw.write("    <"+UTGames.UT99_name_short+" rootfolder=\"none\"/>\n");
        bw.write("    <"+UTGames.UT2003_name_short+" rootfolder=\"none\" />\n");
        bw.write("    <"+UTGames.UT2004_name_short+" rootfolder=\"none\" />\n");
        bw.write("    <"+UTGames.UT3_name_short+" rootfolder=\"none\" />\n");
        bw.write("    <lastfile value=\"none\" />\n");
        bw.write("    <lastoutputfolder value=\"none\" />\n");
        bw.write("  </"+folder_el+">\n");
        bw.write("</root>\n");
        bw.close();
    }

    public File getLastOutputFolder()
    {
        return new File(rootelement.getChild("lastoutputfolder").getAttributeValue("value"));
    }

    public void setLastOutputFolder(File outputfolder)
    {
        rootelement.getChild("lastoutputfolder").setAttribute("value", outputfolder.getAbsolutePath());
        save();
    }

    public boolean isFirstRun()
    {
        String val = rootelement.getChild("isfirstrun").getAttributeValue("value");
        if(val.equals("false")){return false;}else{return true;}
    }

    public void setFirstRun(String isfirstrun)
    {
        rootelement.getChild("isfirstrun").setAttribute("value", isfirstrun);
        save();
    }
    
    public File getLastFile()
    {
        return new File(rootelement.getChild("lastfile").getAttributeValue("value"));
    }

    public void setLastFile(File lastfile)
    {
        rootelement.getChild("lastfile").setAttribute("value", lastfile.getAbsolutePath());
        save();
    }

    public void setDeuxExRootFolder(File deusexrootfolder)
    {
        getInstallFoldersEl().getChild(UTGames.DE_name_short).setAttribute(rootfolder, deusexrootfolder.getAbsolutePath());
        save();
    }

    public File getDeuxExRootFolder()
    {
        return new File(getInstallFoldersEl().getChild(UTGames.DE_name_short).getAttributeValue(rootfolder));
    }
    
    public File getU1RootFolder()
    {
        return new File(getInstallFoldersEl().getChild(UTGames.U1_name_short).getAttributeValue(rootfolder));
    }
    
    public void setU1RootFolder(File u1rootfolder)
    {
        getInstallFoldersEl().getChild(UTGames.U1_name_short).setAttribute(rootfolder, u1rootfolder.getAbsolutePath());
        save();
    }

    public File getUT3RootFolder()
    {
        return new File(getInstallFoldersEl().getChild(UTGames.UT3_name_short).getAttributeValue(rootfolder));
    }

    public void setUT3RootFolder(File u2rootfolder)
    {
        getInstallFoldersEl().getChild(UTGames.UT3_name_short).setAttribute(rootfolder, u2rootfolder.getAbsolutePath());
        save();
    }

    public File getU2RootFolder()
    {
        return new File(getInstallFoldersEl().getChild(UTGames.U2_name_short).getAttributeValue(rootfolder));
    }
    
    public void setU2RootFolder(File u2rootfolder)
    {
        getInstallFoldersEl().getChild(UTGames.U2_name_short).setAttribute(rootfolder, u2rootfolder.getAbsolutePath());
        save();
    }
    
    public File getUT99RootFolder()
    {
        //Element el = getInstallFoldersEl();
        //System.out.println(el.getChildren().size());
        return new File(getInstallFoldersEl().getChild(UTGames.UT99_name_short).getAttributeValue(rootfolder));
    }
    
    public void setUT99RootFolder(File ut99rootfolder)
    {
        if(ut99rootfolder!=null)
        {
            getInstallFoldersEl().getChild(UTGames.UT99_name_short).setAttribute(rootfolder, ut99rootfolder.getAbsolutePath());
            save();
        }
    }

    
    public File getUT2003RootFolder()
    {
        return new File(getInstallFoldersEl().getChild(UTGames.UT2003_name_short).getAttributeValue(rootfolder));
    }
    

    
    public void setUT2003RootFolder(File ut2003rootfolder)
    {
        getInstallFoldersEl().getChild(UTGames.UT2003_name_short).setAttribute(rootfolder, ut2003rootfolder.getAbsolutePath());
        save();
    }
    
    public File getUT2004RootFolder()
    {
        return new File(getInstallFoldersEl().getChild(UTGames.UT2004_name_short).getAttributeValue(rootfolder));
    }

    public File getUT2004MapFolder()
    {
        return new File(getUT2004RootFolder().getAbsolutePath()+"//Maps");
    }
    
    public File getUT3MapFolder()
    {
        return new File(getUT3RootFolder().getAbsolutePath()+"//UTGame//Published//CookedPC//CustomMaps");
    }

    
    public void setUT2004RootFolder(File ut2004rootfolder)
    {
        if(ut2004rootfolder!=null)
        {
            getInstallFoldersEl().getChild(UTGames.UT2004_name_short).setAttribute(rootfolder, ut2004rootfolder.getAbsolutePath());
            save();
        }
        
        
    }


    String texpath=File.separator+"Textures";
    
    public File getUTxTexFolder(int utgame)
    {
        if(utgame==UTGames.UT99)
        {
            return new File(getUT99RootFolder().getAbsolutePath()+texpath);
        }
        else if(utgame==UTGames.UT2004)
        {
            return new File(getUT2004RootFolder().getAbsolutePath()+texpath);
        }
        else if(utgame==UTGames.DeusEx)
        {
            return new File(getDeuxExRootFolder().getAbsolutePath()+texpath);
        }
        else if(utgame==UTGames.UT2003)
        {
            return new File(getUT2003RootFolder().getAbsolutePath()+texpath);
        }
        else if(utgame==UTGames.U1)
        {
            return new File(getU1RootFolder().getAbsolutePath()+texpath);
        }
        else if(utgame==UTGames.U2)
        {
            return new File(getU2RootFolder().getAbsolutePath()+texpath);
        }
        else
        {
            return null;
        }
    }

    public File getUTxRootFolder(int utgame)
    {
        if(utgame==UTGames.UT99)
        {
            return getUT99RootFolder();
        }
        else if(utgame==UTGames.UT2004)
        {
            return getUT2004RootFolder();
        }
        else if(utgame==UTGames.DeusEx)
        {
            return getDeuxExRootFolder();
        }
        else if(utgame==UTGames.UT2003)
        {
            return getUT2003RootFolder();
        }
        else if(utgame==UTGames.U1)
        {
            return getU1RootFolder();
        }
        else if(utgame==UTGames.U2)
        {
            return getU2RootFolder();
        }
        else
        {
            return null;
        }
    }

    public File getMapFolder(int utgame)
    {
        if(utgame==UTGames.UT99)
        {
            return new File(getUT99RootFolder().getAbsolutePath()+"\\Maps");
        }
        else if(utgame==UTGames.UT2004)
        {
            return new File(getUT2004RootFolder().getAbsolutePath()+"\\Maps");
        }
        else if(utgame==UTGames.UT2004)
        {
            return new File(getUT2003RootFolder().getAbsolutePath()+"\\Maps");
        }
        else if(utgame==UTGames.U2)
        {
            return new File(getU2RootFolder().getAbsolutePath()+"\\Maps");
        }
        else if(utgame==UTGames.U1)
        {
            return new File(getU1RootFolder().getAbsolutePath()+"\\Maps");
        }
        else if(utgame==UTGames.DeusEx)
        {
            return new File(getDeuxExRootFolder().getAbsolutePath()+"\\Maps");
        }
        else if(utgame==UTGames.UT3)
        {
            return null;
            //return new File(getU1RootFolder().getAbsolutePath()+"\\System\\UnrealED.exe");
        }
        else
        {
            return null;
        }
    }

    public File getUEDFilePath(int utgame)
    {
        if(utgame==UTGames.UT99)
        {
            return new File(getUT99RootFolder().getAbsolutePath()+"\\System\\UnrealED.exe");
        }
        else if(utgame==UTGames.UT2004)
        {
            return new File(getUT2004RootFolder().getAbsolutePath()+"\\System\\UnrealED.exe");
        }
        else if(utgame==UTGames.UT2004)
        {
            return new File(getUT2003RootFolder().getAbsolutePath()+"\\System\\UnrealED.exe");
        }
        else if(utgame==UTGames.U2)
        {
            return new File(getU2RootFolder().getAbsolutePath()+"\\System\\UnrealED.exe");
        }
        else if(utgame==UTGames.U1)
        {
            return new File(getU1RootFolder().getAbsolutePath()+"\\System\\UnrealED.exe");
        }
        else if(utgame==UTGames.DeusEx)
        {
            return new File(getDeuxExRootFolder().getAbsolutePath()+"\\System\\UnrealED.exe");
        }
        else if(utgame==UTGames.UT3)
        {
            return null;
            //return new File(getU1RootFolder().getAbsolutePath()+"\\System\\UnrealED.exe");
        }
        else
        {
            return null;
        }
    }

    public File getUCCFilePath(int utgame)
    {
        if(utgame==UTGames.UT99)
        {
            return new File(getUT99RootFolder().getAbsolutePath()+"\\System\\ucc.exe");
        }
        else if(utgame==UTGames.UT2003)
        {
            return new File(getUT2003RootFolder().getAbsolutePath()+"\\System\\ucc.exe");
        }
        else if(utgame==UTGames.UT2004)
        {
            return new File(getUT2004RootFolder().getAbsolutePath()+"\\System\\ucc.exe");
        }
        else if(utgame==UTGames.UT3)
        {
            return new File(getUT3RootFolder().getAbsolutePath()+"\\Binaries\\ut3.com");
        }
        else if(utgame==UTGames.U2)
        {
            return new File(getU2RootFolder().getAbsolutePath()+"\\System\\ucc.exe");
        }
        else if(utgame==UTGames.U1)
        {
            return new File(getU1RootFolder().getAbsolutePath()+"\\System\\ucc.exe");
        }
        else if(utgame==UTGames.DeusEx)
        {
            return new File(getDeuxExRootFolder().getAbsolutePath()+"\\System\\ucc.exe");
        }
        else
        {
            return null;
        }
    }

    public void save()
    {
        try {
            XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
            sortie.output(doc, new FileOutputStream(configxmlfile));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public File getUT3ConverterRootFolder()
    {
        return Installation.getInstallDirectory(Main.class);
    }
    
    public File getUTXExtractorFilePath()
    {
        return new File(getUT3ConverterRootFolder().getAbsolutePath()+relativefolder_utxextractor);
    }

    public String getMapFileExtension(int utgame){
        switch (utgame){
            case UTGames.U1:
                return "unr";
            case UTGames.UT99:
                return "unr";
            case UTGames.U2:
                return "un2";
            case UTGames.UT2003:
                return "ut2";
            case UTGames.UT2004:
                return "ut2";
            case UTGames.UT3:
                return "ut3";
            default:
                return null;
        }
    }

    public Logger getMclog() {
        return mclog;
    }

    class myFormatter extends java.util.logging.Formatter {
        // formatage d’un enregistrement

        public String format(LogRecord record) {
            StringBuffer s = new StringBuffer(1000);
            s.append("<tr><td>" + record.getLevel() + "</td>");
            s.append("<td>" + record.getMessage() + "</td></tr>\n");
            return s.toString();
        }
        // entête du fichier de log

        public String getHead(Handler h) {
            return "<html>\n<body>\n<table>\n";
        }
        // fin du fichier de log

        public String getTail(Handler h) {
            return "</table>\n</body>\n</html>\n";
        }
    }

    
}
