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
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import ut3converter2.Main;
import ut3converter2.convert.MapConverter;
import ut3converter2.T3DConvertor;
import ut3converter2.UTGames;
import ut3converter2.errors.T3DLevelExportException;

/**
 * Exports an UT map to t3d
 * @author Hyperion
 */
public class LevelExporter extends T3DConvertor{

    final static String BATCH_PROGRAM = "ucc.exe";
    final static String BATCH_PROGRAM_UT3 = "ut3.com";
    
    String taskname="*** Export of Unreal map to T3D Level File ***";
    boolean biscritical=true;
    final String defaultt3dnameout="myLevel.t3d";
    File utmap;
    File outputfolder;
    File uccfilepath;
    boolean renamefile;
    MapConverter mc;
    int game;

    public LevelExporter(MapConverter mc){
        this.mc = mc;
        this.utmap = mc.getInputfile();
        this.outputfolder = mc.getOutputfolder();
        this.uccfilepath = Main.config.getUCCFilePath(mc.getInput_utgame());
    }

    public LevelExporter(File utmap,File outputfolder,File uccfilepath) {
        this.utmap = utmap;
        this.outputfolder = outputfolder;
        this.uccfilepath = uccfilepath;
        super.setBiscritical(biscritical);
    }

    public LevelExporter(File utmap,int utgame) {
        this.game = utgame;
        this.utmap = utmap;
        this.outputfolder = utmap.getParentFile();
        this.uccfilepath = Main.config.getUCCFilePath(utgame);
        super.setBiscritical(biscritical);
    }

    public LevelExporter(File utmap,File outputfolder,int utgame) {
        this.game = utgame;
        this.utmap = utmap;
        this.outputfolder = outputfolder;
        this.uccfilepath = Main.config.getUCCFilePath(utgame);
        super.setBiscritical(biscritical);
    }


    private String getCoreFileName(File file)
    {
        String filename = file.getName();
        return filename.substring(0, filename.length()-4);
    }

    public void setRenameFile(boolean nrename)
    {
        this.renamefile = nrename;
    }
    private void renameT3DFile()
    {
        File mapnamefile = new File(outputfolder.getParent()+"//"+getCoreFileName(utmap)+".t3d");
        File f = new File(outputfolder.getAbsolutePath()+"//myLevel.t3d");
        System.out.println(mapnamefile.getAbsolutePath()+"->"+f.getAbsolutePath());
        f.renameTo(mapnamefile);
    }

    private File createBatFile()
    {
        BufferedWriter bwr = null;

        try {
            File fbat = File.createTempFile("LevelExporter", ".bat");
            bwr = new BufferedWriter(new FileWriter(fbat));
            bwr.write("copy \""+utmap.getAbsolutePath()+"\" \""+uccfilepath.getParent()+"\"\n");
            bwr.write(uccfilepath.getAbsolutePath().split("\\\\")[0]+"\n");
            bwr.write("cd \""+uccfilepath.getParent()+"\"\n");
            
            if(game == UTGames.UT3){
                bwr.write(BATCH_PROGRAM_UT3+" batchexport \""+utmap.getAbsolutePath()+"\" Level t3d \n");
            } else {
                bwr.write(BATCH_PROGRAM+" batchexport "+utmap.getName()+" Level t3d \""+outputfolder.getAbsolutePath()+"\"\n");
            }
            
            bwr.write("del "+utmap.getName());
            bwr.close();
            return fbat;
        } catch (IOException ex) {
            Logger.getLogger(LevelExporter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            if(bwr != null){
                try {
                    bwr.close();
                } catch (IOException ex) {
                    Logger.getLogger(LevelExporter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public boolean exportT3DMap() throws T3DLevelExportException
    {
        boolean bhaserror=false;
        String missingpackage= null;
        if(super.isBShowLog())
        {
            System.out.println(taskname);
            System.out.print(utmap.getName()+"->"+defaultt3dnameout);
        }
        try {
            String cmd = "";
            File fbat = createBatFile();
            ArrayList log = new ArrayList();
            Runtime run = Runtime.getRuntime();
            Process pp;
            pp = run.exec(fbat.getAbsolutePath());

            BufferedReader in =new BufferedReader(new InputStreamReader(pp.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                //System.out.println(line);
                log.add(line);
            }
            int exitVal = pp.waitFor();
            pp.exitValue();
            fbat.delete();
            
            

             String error = "";
             error = "<html>"+uccfilepath.getAbsolutePath()+" <br> was unable to extract map to .t3d file.<br><i>";
             String tmp="";
             for(int i=0;i<log.size();i++)
             {
                 tmp = log.get(i).toString();
                 if(tmp.contains("Failed"))
                 {
                     missingpackage = tmp.split("\\'")[2];
                     bhaserror = true;
                     error += tmp;
                 } else if(tmp.contains("Commandlet batchexport not found")){
                     bhaserror = true;
                     error += tmp;
                     if(mc != null && mc.getInput_utgame() == UTGames.U1){
                         error += "\nDownload and install latest patch for Unreal from www.oldunreal.com  ";
                     }
                     
                 }
             }

             if(bhaserror)
             {

                 //System.out.println("\nError!:"+cmd);
                 if(missingpackage != null){
                    error += "</i><br><h1>You need "+missingpackage+" (.uax;.utx;.u;.usx) file to convert map!</h1></html>";
                 }

                 JOptionPane.showMessageDialog(
                                null,
                                error,
                                "Error!",JOptionPane.ERROR_MESSAGE
                                );
                if(super.isBShowLog())
                {
                    System.out.println(".. Error! (Missing file:"+missingpackage+" (.uax;.utx;.u))");
                }
                throw new T3DLevelExportException("UCC T3D Export failure.");
                //return false;
             }
             else
             {
                 pp.destroy();
                if(renamefile)
                {
                    renameT3DFile();
                }
                
                if(this.game == UTGames.UT3){
                    // T3D files generated to /<root folder>/Binaries
                    File t3dFile = new File(uccfilepath.getParent()+"//PersistentLevel.t3d");
                    System.out.println(t3dFile.getAbsolutePath()+"-"+t3dFile.exists());
                    
                    File newT3dFile = new File(uccfilepath.getParent()+"//"+utmap.getName().split("\\.")[0]+".t3d");

                    Files.move(t3dFile.toPath(), newT3dFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }

                if(super.isBShowLog())
                {
                    System.out.println(".. done!");
                }
                if(super.isBDispResult())
                {
                    JOptionPane.showMessageDialog(null,"<html>"+ utmap.getName()+" has been sucessfully exported to <br>"+outputfolder);
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
