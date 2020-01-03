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
import ut3converter2.convert.MapConverter;
import ut3converter2.T3DConvertor;

/**
 *
 * @author Hyperion
 */
public class SoundExporter extends T3DConvertor{

    String tskname="\n*** Export of wave files from sounds packages ***";
    File[] uaxfiles = null;
    File outputfolder = null;
    File uccfilepath = null;
    boolean bsetuaxfiles=true;

    public SoundExporter(File[] uaxfiles,MapConverter mc) {
        this.uaxfiles = uaxfiles;
        this.outputfolder = mc.getSndfolder();
        this.uccfilepath = mc.getUccfilepath();
        super.setTaskname(tskname);
        super.setBShowLog(true);
    }

    public boolean isBsetuaxfiles() {
        return bsetuaxfiles;
    }

    public void setBsetuaxfiles(boolean bsetuaxfiles) {
        this.bsetuaxfiles = bsetuaxfiles;
    }




    public void ExportToWav()
    {
        if(super.isBShowLog()){System.out.println(tskname);}
        for(int i=0;i<uaxfiles.length;i++)
        {
            if(super.isBShowLog()){System.out.print((i+1)+"/"+(uaxfiles.length)+"-"+uaxfiles[i].getName());}
            ExportSound(uaxfiles[i]);
        }
    }

    public File[] getUaxfiles() {
        return uaxfiles;
    }

    public void setUaxfiles(File[] uaxfiles) {
        this.uaxfiles = uaxfiles;
    }

     private File createBatFile(File uaxfile)
    {
        try {
            File fbat = File.createTempFile("SoundExporter", ".bat");
            BufferedWriter bwr = new BufferedWriter(new FileWriter(fbat));

            bwr.write("copy \""+uaxfile.getAbsolutePath()+"\" \""+uccfilepath.getParent()+"\"\n");


            bwr.write(uccfilepath.getAbsolutePath().split("\\\\")[0]+"\n");
            bwr.write("cd \""+uccfilepath.getParent()+"\"\n");
            bwr.write("ucc.exe batchexport "+uaxfile.getName()+" Sound wav \""+outputfolder.getAbsolutePath()+"\"\n");

            if(!uaxfile.getName().endsWith(".u"))
            {
                bwr.write("del "+uaxfile.getName());
            }
            
            
            bwr.close();
            return fbat;
        } catch (IOException ex) {
            Logger.getLogger(LevelExporter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private boolean ExportSound(File uaxfile)
    {
        //String cmd = uccfilepath.getAbsolutePath() + " batchexport \"" + uaxfile + "\" Sound wav " + outputfolder.getAbsolutePath();
        String cmd = "";
        if(!uaxfile.exists())
        {
            if(super.isBShowLog()){System.out.println(".... skipped (file doesn't exist)!");}
            return true;
        }

        try {

            ArrayList log = new ArrayList();

            Runtime run = Runtime.getRuntime();
            File f=createBatFile(uaxfile);
            Process pp = run.exec(f.getAbsolutePath());

            BufferedReader in =new BufferedReader(new InputStreamReader(pp.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                //System.out.println(line);
                log.add(line);
            }
            int exitVal = pp.waitFor();
            pp.exitValue();

            f.delete();
         if(exitVal==1)
         {
             String error = "";
             error = "<html>"+uccfilepath.getAbsolutePath()+" <br> was unable to extract the sound file.<br><i>";

             for(int i=0;i<log.size();i++)
             {
                 if(log.get(i).toString().contains("Failed"))
                 {
                     error += log.get(i).toString();
                 }
             }

             error += "</i></html>";
             if(super.isBShowLog()){System.out.println(".... Error!"+error);}
             JOptionPane.showMessageDialog(
                            null,
                            error,
                            "Error!",JOptionPane.ERROR_MESSAGE
                            );
             return false;
         }
         else
         {
             if(super.isBShowLog()){System.out.println(".... done!");}
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
