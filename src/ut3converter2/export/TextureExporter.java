/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.export;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import ut3converter2.Configuration;
import ut3converter2.Installation;
import ut3converter2.Main;
import ut3converter2.convert.MapConverter;
import ut3converter2.UTGames;
import ut3converter2.T3DConvertor;

/**
 * Exports textures from a .utx package
 * @TODO Fix bad export for P8 textures with ExtractTextures program
 * @author Hyperion
 */
public class TextureExporter extends T3DConvertor{

    private final String dds = "dds";

    final String tskname="\n*** Export of textures from Unreal packages ";
    String formats[];
    File utxfiles[] = null;
    File outputfolder = null;
    File uccfilepath = null;
    boolean useut3filename=false;
    String ut3f="false";
    int tskmaxval=0;
    int curval=0;
    /**
     * Ucc.exe batchexport mytex texture ...
     */
    public static final int mode_ucc=0;
    /**
     * ExtractTexture.exe a.utx ..
     */
    public static final int mode_utxexp=1;
    int curmode = TextureExporter.mode_utxexp;
    
    public TextureExporter(File utxfiles[],File outputfolder,File uccfilepath) {
        this.utxfiles = utxfiles;
        this.outputfolder = outputfolder;
        this.uccfilepath = uccfilepath;
        super.setTaskname(tskname);
    }

    public TextureExporter() {
    }

    public TextureExporter(File utxfiles[],String formats[],MapConverter mc) {
        this.formats = formats;
        this.utxfiles = utxfiles;
        this.uccfilepath = mc.getUccfilepath();
        this.outputfolder = mc.getTexfolder();
        super.setTaskname(tskname);   
    }


     public TextureExporter(File utxfiles[],String formats[],File outputfolder,int utgame) {
        this.formats = formats;
        this.utxfiles = utxfiles;
        this.uccfilepath = Main.config.getUCCFilePath(utgame);
        this.outputfolder = outputfolder;
        super.setTaskname(tskname);
    }

    public TextureExporter(File utxfiles[],File outputfolder,String formats[],Configuration conf,int utgame) {
        this.formats = formats;
        this.utxfiles = utxfiles;
        this.uccfilepath = conf.getUCCFilePath(utgame);
        this.outputfolder = outputfolder;
        super.setTaskname(tskname);
    }

    public TextureExporter(File utxfiles[],String formats[],MapConverter mc,File outputfolder) {
        this.formats = formats;
        this.utxfiles = utxfiles;
        this.uccfilepath = mc.getUccfilepath();
        this.outputfolder = outputfolder;
        super.setTaskname(tskname);
    }



    public boolean bHasExportedFile(File f)
    {
        if(utxfiles==null)
        {
            return false;
        }
        for(int i=0;i<utxfiles.length;i++)
        {
            File fa = utxfiles[i];
            if(fa.getName().toLowerCase().equals(f.getName().toLowerCase()))
            {
                return true;
            }
        }
        return false;
    }


    public void removeFile(String filename)
    {
        ArrayList alfiles = new ArrayList();
        for(int i=0;i<utxfiles.length;i++)
        {
            if(!utxfiles[i].getName().equals(filename))
            {
                alfiles.add(utxfiles[i]);
            }
        }

        File f[] = new File[alfiles.size()];
        for(int i=0;i<alfiles.size();i++)
        {
            f[i]=(File)alfiles.get(i);
        }
        this.utxfiles = f;
    }

    public void setOutputFolder(File outputfolder)
    {
        this.outputfolder = outputfolder;
    }

    public int getCurmode() {
        return curmode;
    }

    /**
     * Use standard "ucc batchexport" mode or "ExtractTextures" program
     * @param curmode
     */
    public void setCurmode(int curmode) {
        this.curmode = curmode;
    }
    
    public TextureExporter(File utxsourcefile[],File outputfolder,Configuration config,int utgame) {
        this.utxfiles = utxsourcefile;
        this.outputfolder = outputfolder;
        if(!outputfolder.exists()){outputfolder.mkdir();}
        if(utgame==UTGames.U1){this.uccfilepath = new File(config.getU1RootFolder().getAbsolutePath()+"/System/UCC.exe");}
        else if(utgame==UTGames.U2){this.uccfilepath = new File(config.getU2RootFolder().getAbsolutePath()+"/System/UCC.exe");}
        else if(utgame==UTGames.UT2003){this.uccfilepath = new File(config.getUT2003RootFolder().getAbsolutePath()+"/System/UCC.exe");}
        else if(utgame==UTGames.UT2004){this.uccfilepath = new File(config.getUT2004RootFolder().getAbsolutePath()+"/System/UCC.exe");}
        else if(utgame==UTGames.UT99){this.uccfilepath = new File(config.getUT99RootFolder().getAbsolutePath()+"/System/UCC.exe");}
    }

    public File[] getUtxfiles() {
        return utxfiles;
    }

    public void setUtxfiles(File[] utxfiles) {
        this.utxfiles = utxfiles;
    }



    public void ExportTex()
    {
        if(super.isBShowLog()){System.out.println(tskname+" to "+this.outputfolder.getAbsolutePath());}
        for(int i=0;i<utxfiles.length;i++)
        {
            for(int j=0;j<formats.length;j++)
            {
                if(super.isBShowLog()){System.out.print((i+1)+"/"+(utxfiles.length)+"-"+utxfiles[i].getName()+" ("+formats[j]+")");}
                ExportSingleTex(utxfiles[i],formats[j]);
            }
        }
    }

    public void ExportTex(File outputfolder)
    {
        this.outputfolder = outputfolder;
        //Main.d2.jp2.subvaluemax = utxfiles.length*formats.length;
        //Main.d2.lbltskmain = "Exporting textures to "+this.outputfolder.getAbsolutePath()+" ("+Arrays.toString(formats)+")";
        for(int i=0;i<utxfiles.length;i++)
        {
            for(int j=0;j<formats.length;j++)
            {
                ExportSingleTex(utxfiles[i],formats[j]);
            }
        }
    }

    public int getTskmaxval() {
        return tskmaxval;
    }

    public void setTskmaxval(int tskmaxval) {
        this.tskmaxval = tskmaxval;
    }

    public int getCurval() {
        return curval;
    }

    public void setCurval(int curval) {
        this.curval = curval;
    }

    public boolean isUseut3filename() {
        return useut3filename;
    }

    public void setUseut3filename(boolean useut3filename) {
        this.useut3filename = useut3filename;
    }

    private File createBatFile(File utxfile,String format)
    {
        try {
            File fbat = File.createTempFile("TextureExporter", ".bat");
            BufferedWriter bwr = new BufferedWriter(new FileWriter(fbat));

            bwr.write("copy \""+utxfile.getAbsolutePath()+"\" \""+uccfilepath.getParent()+"\"\n");


            bwr.write(uccfilepath.getAbsolutePath().split("\\\\")[0]+"\n");
            bwr.write("cd \""+uccfilepath.getParent()+"\"\n");
            bwr.write("ucc.exe batchexport "+utxfile.getName()+" Texture "+format+"\""+outputfolder.getAbsolutePath()+"\"\n");
            bwr.write("del "+utxfile.getName());

            bwr.close();
            return fbat;
        } catch (IOException ex) {
            Logger.getLogger(LevelExporter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private boolean ExportSingleTex(File utxfile,String format)
    {
        if(useut3filename)
        {
            ut3f="true";
        }

        File rootfolder = Installation.getInstallDirectory(Main.class);
        File utxextractor = new File(rootfolder.getAbsolutePath()+"\\bin\\utxextractor\\ExtractTextures.exe");
        String cmd="";
        if(this.curmode==TextureExporter.mode_ucc)
        {
            cmd = createBatFile(utxfile, format).getAbsolutePath();
            //cmd = uccfilepath.getAbsolutePath() + " batchexport \"" + utxfile + "\" Texture "+format+" " + outputfolder.getAbsolutePath();
        }
        else if(this.curmode==TextureExporter.mode_utxexp)
        {
            cmd = "\""+utxextractor.getAbsolutePath()+"\" \""+utxfile + "\" \""+ outputfolder.getAbsolutePath()+"\"\\ "+ut3f+"";
        }
        

        try {

            ArrayList log = new ArrayList();

            Runtime run = Runtime.getRuntime();
            Process pp = run.exec(cmd);
            
            BufferedReader in =new BufferedReader(new InputStreamReader(pp.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                log.add(line);
                //System.out.println("***"+line);
            }
            int exitVal = pp.waitFor();
            pp.exitValue();
            
         if(exitVal==1)
         {
             String error = "";
             error = "<html>"+uccfilepath.getAbsolutePath()+" <br> was unable to extract the texture file.<br><i>";
             
             for(int i=0;i<log.size();i++)
             {
                 if(log.get(i).toString().contains("Failed"))
                 {
                     error += log.get(i).toString();
                 }
             }
             
             error += "</i></html>";
             /*
             JOptionPane.showMessageDialog(
                            null,
                            error,
                            "Error!",JOptionPane.ERROR_MESSAGE
                            );
              * */
             if(super.isBShowLog()){
                 System.out.println("... Error! ("+error+")");
             }
             
             return false;
         }
         else
         {
             if(super.isBShowLog()){
                 System.out.println(" ... done!");
             }
             return true;
         }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),"Error!",JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (InterruptedException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),"Error!",JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    


    

}
