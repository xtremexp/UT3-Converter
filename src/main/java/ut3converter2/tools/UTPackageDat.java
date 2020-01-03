/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import ut3converter2.Installation;
import ut3converter2.Main;
import ut3converter2.UTPackage;
import ut3converter2.export.TextureExporter;
import ut3converter2.T3DConvertor;

/**
 *
 * @author Hyperion
 */
public class UTPackageDat extends T3DConvertor {

    String tskname="\n*** Associating new texnames to packagenames ***";
    File utxfiles[];
    File outputfolder;
    File datfileout;

    public UTPackageDat(File utxfiles[], File outputfolder, File datfileout) {
        this.utxfiles = utxfiles;
        this.outputfolder = outputfolder;
        this.datfileout = datfileout;
        super.setTaskname(tskname);
    }

    public UTPackageDat(ArrayList<UTPackage> alpac, File outputfolder, File datfileout) {
        utxfiles = new File[alpac.size()];
        for (int i = 0; i < alpac.size(); i++) {
            UTPackage uTPackage = alpac.get(i);
            utxfiles[i] = uTPackage.getPath();
        }
        this.outputfolder = outputfolder;
        this.datfileout = datfileout;
        super.setTaskname(tskname);
    }

    public void createDatFile() throws IOException, InterruptedException
    {
        File utxfile;
        FileCleaner fc;
        File exprtfiles[];
        File a[]=new File[1];
        TextureExporter te;
        BufferedWriter bwr = new BufferedWriter(new FileWriter(datfileout));
        File fgtn=new File(Installation.getInstallDirectory(Main.class).getAbsolutePath()+"//bin/gtn/GetTexturesNames.exe");
        if(isBShowLog()){System.out.println(getTaskname());}
        String cmd ="";
        String texname="";
        String newtexname="";
        String t[];
        String out="";

        for(int i=0;i<utxfiles.length;i++)
        {
            utxfile = utxfiles[i];
            cmd = "\""+fgtn.getAbsolutePath()+"\" \""+utxfile+"\"";
            if(isBShowLog()){System.out.print((i+1)+"/"+utxfiles.length+"-"+utxfile.getName()+" ...");}
            a[0]=utxfile;
            try {

            ArrayList log = new ArrayList();

            Runtime run = Runtime.getRuntime();
            Process pp = run.exec(cmd);

            BufferedReader in =new BufferedReader(new InputStreamReader(pp.getInputStream()));
            String line;

            // Rmetl
            //Swater4a;Texture'water6'
            //:water6Swater4a:RainFX
            //Swater4a
            while ((line = in.readLine()) != null) {
                //System.out.print(line);
                if(line.contains(";"))
                {
                    //Swater4a;Texture'water6'
                    texname = line.split("\\;")[0];
                    t = line.split("\\;");
                    out = texname.toLowerCase()+":"+utxfile.getName().split("\\.")[0]+":"+t[1].split("\\'")[1];
                    //System.out.println("->"+out);
                    bwr.write(out+"\n");
                }
                else
                {
                    out = line.toLowerCase()+":"+utxfile.getName().split("\\.")[0];
                    //System.out.println("->"+out);
                    bwr.write(out+"\n");
                }
                log.add(line);
            }
            int exitVal = pp.waitFor();
            pp.exitValue();

         if(exitVal==1)
         {
             String error = "";


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

         }
         else
         {
             if(super.isBShowLog()){
                 System.out.println(" ... done!");
             }

         }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),"Error!",JOptionPane.ERROR_MESSAGE);

        } catch (InterruptedException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),"Error!",JOptionPane.ERROR_MESSAGE);

        }
            /*
            te = new TextureExporter(a, outputfolder, new String[]{"pcx"}, Main.config, utgame);
            te.ExportTex();

            exprtfiles = outputfolder.listFiles();
            for(int j=0;j<exprtfiles.length;j++)
            {
                bwr.write((exprtfiles[j].getName().split("\\.")[0]).toLowerCase()+":"+utxfile.getName().split("\\.")[0]+"\n");
            }
            fc = new FileCleaner(exprtfiles);
            fc.clean();
            Thread.sleep(20L);
            if(isBShowLog()){System.out.println("done !");}
             * */
        }

        bwr.close();
    }


}
