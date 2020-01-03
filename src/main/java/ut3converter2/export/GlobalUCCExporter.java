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
import ut3converter2.Configuration;
import ut3converter2.UTGames;

/**
 *
 * @author Hyperion
 */
public class GlobalUCCExporter {

    File uccfilepath;
    File inputfiles[];
    final String exp_classuc="Class uc";
    final String exp_texture="Texture pcx";

    public GlobalUCCExporter(File inputfile,File rootgamepath) {
        this.inputfiles = new File[1];
        inputfiles[0] = inputfile;
        this.uccfilepath = new File(rootgamepath.getAbsolutePath()+"\\System\\ucc.exe");
    }

     public GlobalUCCExporter(File inputfiles[],File rootgamepath) {
        this.inputfiles = inputfiles;
        this.uccfilepath = new File(rootgamepath.getAbsolutePath()+"\\System\\ucc.exe");
    }


     public GlobalUCCExporter(File inputfiles[],Configuration conf,int utgame) {
        this.inputfiles = inputfiles;
        if(utgame==UTGames.UT2004)
        {
            this.uccfilepath = new File(conf.getUT2004RootFolder().getAbsolutePath()+"//System//UCC.exe");
        }
        else if(utgame==UTGames.UT99)
        {
            this.uccfilepath = new File(conf.getUT99RootFolder().getAbsolutePath()+"//System//UCC.exe");
        }
    }

    public GlobalUCCExporter(File inputfile,Configuration conf,int utgame) {
        this.inputfiles = new File[1];
        inputfiles[0] = inputfile;
        if(utgame==UTGames.UT2004)
        {
            this.uccfilepath = new File(conf.getUT2004RootFolder().getAbsolutePath()+"//System//UCC.exe");
        }
        else if(utgame==UTGames.UT99)
        {
            this.uccfilepath = new File(conf.getUT99RootFolder().getAbsolutePath()+"//System//UCC.exe");
        }
    }

    public boolean exportTexFileTo(File inputfile,File outputfolder) throws InterruptedException
    {
        String cmd = uccfilepath.getAbsolutePath() + " batchexport \"" + inputfile + "\" "+exp_texture+" \"" + outputfolder.getAbsolutePath()+"\"";
        //System.out.println(cmd);
        try {

            ArrayList log = new ArrayList();

            Runtime run = Runtime.getRuntime();
            Process pp = run.exec(cmd);

            BufferedReader in =new BufferedReader(new InputStreamReader(pp.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                //System.out.println(line);
                //log.add(line);
            }
            int exitVal = pp.waitFor();
            pp.exitValue();

         if(exitVal==1)
         {
             return false;
         }
         else
         {
            return true;
         }
        } catch (IOException ex) {
            return false;
        }
    }
    public void exportTexFilesTo(File outputfolder) throws IOException, InterruptedException
    {
        boolean bsucess=false;
        for(int i=0;i<inputfiles.length;i++)
        {
            System.out.print(i+"/"+(inputfiles.length-1)+"-Exporting \""+inputfiles[i].getName()+"\" ...");
            bsucess = exportTexFileTo(inputfiles[i], outputfolder);
            if(bsucess){System.out.println("Done!");}else{System.out.println("ERROR!");}
        }
    }

    public void exportUCFileTo(File inputfile,File outputfolder) throws IOException, InterruptedException
    {
        String cmd = uccfilepath.getAbsolutePath() + " batchexport \"" + inputfile + "\" "+exp_classuc+" \"" + outputfolder.getAbsolutePath()+"\"";
        System.out.println(cmd);
        try {

            ArrayList log = new ArrayList();

            Runtime run = Runtime.getRuntime();
            Process pp = run.exec(cmd);

            BufferedReader in =new BufferedReader(new InputStreamReader(pp.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                //System.out.println(line);
                log.add(line);
            }
            int exitVal = pp.waitFor();
            pp.exitValue();

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



         }
         else
         {
             System.out.print(".... done!");
         }
        } catch (IOException ex) {

        }
    }
    public void exportUCFilesTo(File outputfolder) throws IOException, InterruptedException
    {
        for(int i=0;i<inputfiles.length;i++)
        {
            exportUCFileTo(inputfiles[i], outputfolder);
        }
    }



}
