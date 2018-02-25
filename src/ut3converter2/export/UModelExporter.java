/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.export;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import ut3converter2.Installation;
import ut3converter2.Main;
import ut3converter2.tools.FileMover;

/**
 *
 * @author Hyperion
 */
public class UModelExporter {

    File inputufile;

    public UModelExporter(File inputufile) {
        this.inputufile = inputufile;
    }

    public void ExportTo(File outputfolder) throws IOException, InterruptedException
    {
        File installfolder =Installation.getInstallDirectory(Main.class);
        ArrayList al3dfiles = new ArrayList();
        String cmd = installfolder.getAbsolutePath()+"\\bin\\umodel\\umodel.exe -export "+"\""+inputufile.getAbsolutePath()+"\"";
        //System.out.println(cmd);

        try {

            ArrayList log = new ArrayList();

            Runtime run = Runtime.getRuntime();
            Process pp = run.exec(cmd);
            
            BufferedReader in =new BufferedReader(new InputStreamReader(pp.getInputStream()));
            String line;
            
            while ((line = in.readLine()) != null) {
               //System.out.println(line);
                
                if(line.contains("Exported VertMesh"))
                {
                    al3dfiles.add(new File(inputufile.getParentFile().getAbsolutePath()+"\\"+line.split("\\ ")[2]+".3d"));
                }
                
                log.add(line);
            }
            
            int exitVal = pp.waitFor();
            pp.exitValue();

         if(exitVal==1)
         {
            System.out.print(".... ERROR!");
         }
         else
         {
             System.out.println(".... done!");
         }
            
        } catch (IOException ex) {

        }

        Thread.sleep(500L);
        File fout;
        File fin;
        
        for(int i=0;i<al3dfiles.size();i++)
        {
            File f =(File) al3dfiles.get(i);

            fin = new File(installfolder.getAbsolutePath()+"\\"+f.getName().split("\\.")[0]+"_a.3d");
            fout = new File(outputfolder.getAbsolutePath()+"\\"+f.getName().split("\\.")[0]+"_a.3d");
            //System.out.println(fin.getAbsolutePath());
            //System.out.println("-->"+fout.getAbsolutePath());
            fin.renameTo(fout);

            fin = new File(installfolder.getAbsolutePath()+"\\"+f.getName().split("\\.")[0]+"_d.3d");
            fout = new File(outputfolder.getAbsolutePath()+"\\"+f.getName().split("\\.")[0]+"_d.3d");
            //System.out.println(f.getAbsolutePath());
            //System.out.println("-->"+fout.getAbsolutePath());
            fin.renameTo(fout);
            
        }
    }

}
