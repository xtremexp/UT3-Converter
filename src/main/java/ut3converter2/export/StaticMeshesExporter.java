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
import ut3converter2.Main;
import ut3converter2.convert.MapConverter;
import ut3converter2.T3DConvertor;

/**
 * @TODO StaticMesh exporter with delphi
 * @author Hyperion
 */
public class StaticMeshesExporter extends T3DConvertor{

    final String tskname="\n*** Export of staticmeshes from UT packages (.usx,.ut2,.u) to T3D staticmeshes files (.t3d) ***";
    private File usxfiles[]=new File[0];
    private File uccfilepath;
    private File outputfolder;

    public StaticMeshesExporter(File usxfiles[],File outputfolder,File uccfilepath) {
        this.usxfiles = usxfiles;
        this.uccfilepath = uccfilepath;
        this.outputfolder = outputfolder;
        super.setTaskname(tskname);
    }

    public StaticMeshesExporter(File usxsourcefiles[],File outputfolder,int utgame) {
        this.usxfiles = usxsourcefiles;
        this.outputfolder = outputfolder;
        if(!outputfolder.exists()){outputfolder.mkdir();}
        this.uccfilepath = Main.config.getUCCFilePath(utgame);
        super.setTaskname(tskname);
    }

    public void addFileToExport(File smfile){
        File usxfilestmp[]=new File[usxfiles.length+1];
        for (int i = 0; i < usxfiles.length; i++) {
            usxfilestmp[i] = usxfiles[i];
        }
        usxfilestmp[usxfiles.length]=smfile;
        usxfiles = usxfilestmp;
    }
    public File[] getUsxfiles() {
        return usxfiles;
    }

    public void setUsxfiles(File[] usxfiles) {
        this.usxfiles = usxfiles;
    }

    public void setUsxfiles(File[] usxfiles,MapConverter mc) {

        File f[] = new File[usxfiles.length+1];
        for (int i = 0; i < usxfiles.length; i++) {
            f[i] = usxfiles[i];
        }
        f[usxfiles.length]=mc.getInputfile();
        this.usxfiles = f;
    }

    public void removeFile(String filename)
    {
        ArrayList alfiles = new ArrayList();
        for(int i=0;i<usxfiles.length;i++)
        {
            if(!usxfiles[i].getName().equals(filename))
            {
                alfiles.add(usxfiles[i]);
            }
        }

        File f[] = new File[alfiles.size()];
        for(int i=0;i<alfiles.size();i++)
        {
            f[i]=(File)alfiles.get(i);
        }
        this.usxfiles = f;
    }
    
    public void ExportToT3D()
    {
        //Main.d2.jp2.subvaluemax = usxfiles.length;
        if(super.isBShowLog()){System.out.println(super.getTaskname());}

        for(int i=0;i<usxfiles.length;i++)
        {
            if(super.isBShowLog()){System.out.print((i+1)+"/"+usxfiles.length+"-"+usxfiles[i].getName());}
            ExportSingleFileToT3D(usxfiles[i]);
        }
    }

    /**
     * Create some .bat file
     * @param uaxfile
     * @return
     */
     private File createBatFile(File uaxfile)
    {
        try {
            File fbat = File.createTempFile("SoundExporter", ".bat");
            BufferedWriter bwr = new BufferedWriter(new FileWriter(fbat));

            
            bwr.write("copy \""+uaxfile.getAbsolutePath()+"\" \""+uccfilepath.getParent()+"\"\n");


            bwr.write(uccfilepath.getAbsolutePath().split("\\\\")[0]+"\n");
            bwr.write("cd \""+uccfilepath.getParent()+"\"\n");
            bwr.write("ucc.exe batchexport "+uaxfile.getName()+" StaticMesh t3d \""+outputfolder.getAbsolutePath()+"\"\n");

            //Do not delete .u file in /System folder if source file to export is .u file
            //Or else will delete it (so won't be able to convert!)
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
    
    private boolean ExportSingleFileToT3D(File usxfile)
    {
        try {
            File f=createBatFile(usxfile);
            String cmd = f.getAbsolutePath();
            //String cmd = uccfilepath.getAbsolutePath() + " batchexport \"" + usxfile + "\" StaticMesh t3d " + outputfolder.getAbsolutePath();
            ArrayList log = new ArrayList();
            Runtime run = Runtime.getRuntime();
            Process pp = run.exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(pp.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                log.add(line);
            }
            int exitVal = pp.waitFor();
            pp.exitValue();
            f.delete();
            if(exitVal==1)
         {
             String error = "";

             for(int i=0;i<log.size();i++)
             {
                 if(log.get(i).toString().contains("Failed"))
                 {
                     error += log.get(i).toString();
                 }
                 else if(log.get(i).toString().contains("Can't"))
                 {
                     error += log.get(i).toString();
                 }
             }

             /*
             JOptionPane.showMessageDialog(
                            null,
                            error,
                            "Error!",JOptionPane.ERROR_MESSAGE
                            );
              * */
             System.out.println("... Error! ("+error+")");
             return false;
             }
             else
             {
                 System.out.println(".... done!");
                 return true;
             }
        } catch (IOException ex) {
            Logger.getLogger(StaticMeshesExporter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex)
        {
            System.out.println("");
        }
        return false;
    }


}
