/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Texture;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ut3converter2.Installation;
import ut3converter2.Main;
import ut3converter2.UTGames;
import ut3converter2.convert.MapConverter;
import ut3converter2.T3DConvertor;

/**
 * Converts textures to specified format
 * @author Hyperion
 */
public class TextureConverter extends T3DConvertor implements Runnable{

    String tskname="\n*** Conversion of textures ***";
    File inputfiles[];
    String extracmdparameter="";
    String outformat="psd";
    public static int mode_files=0;
    public static int mode_folder=1;
    File inputfolder;
    int curmode=0;
    boolean deletesourcefiles=false;

    public TextureConverter(File inputfiles[]) {
        this.inputfiles = inputfiles;
        super.setTaskname(tskname);
        super.setBShowLog(true);
    }

    /**
     * Converts all textures in /Textures folder
     * to DDS (UT2004) or PSD (UT3,U2) format
     * @param mc
     */
    public TextureConverter(MapConverter mc){
        this.inputfiles = mc.getTexfolder().listFiles();
        super.setTaskname(tskname);
        if(mc.getOutput_utgame()==UTGames.UT2003||mc.getOutput_utgame()==UTGames.UT2004){
            useMergeAlpha();
            this.outformat = "dds";
        } else if(mc.getOutput_utgame()==UTGames.UT3){
            this.outformat = "psd";
        }
        
    }
    public TextureConverter(File inputfiles[],String outformat) {
        this.inputfiles = inputfiles;
        this.outformat = outformat;
        super.setTaskname(tskname);
        super.setBShowLog(true);
    }

    /**
     * Converts textures to another format
     * @param inputfile File or folder
     * @param outformat Output format texture (e.g: bmp,dds,pcx,...)
     * @param curmode If mode = mode_files then convert one single file else convert all files in folder.
     */
    public TextureConverter(File inputfile,String outformat,int curmode) {
        this.curmode = curmode;
        if(this.curmode==mode_files)
        {
            this.inputfiles = new File[1];
            inputfiles[0] = inputfile;
        }
        else if(this.curmode==mode_folder)
        {
            this.inputfolder=inputfile;
        }
        
        this.outformat = outformat;
        super.setTaskname(tskname);
        super.setBShowLog(true);
    }

    public void setDeleteSourceFilesAfterConversion(boolean deletesourcefiles) {
        this.deletesourcefiles = deletesourcefiles;
    }

    
    /**
     * Uses "-merge_alpha" extra cmd parameter used for terrain alpha textures (UT2004)
     */
    public void useMergeAlpha()
    {
        extracmdparameter +=" -merge_alpha ";
    }
    
    public String getOutformat() {
        return outformat;
    }

    public void setOutformat(String outformat) {
        this.outformat = outformat;
    }

    /**
     * Converts all textures files set.
     */
    public void convertAll()
    {
        //Main.d2.lbltskmain = "Converting textures to psd format";
        String outname="";
        if(super.isBShowLog()){System.out.println(super.getTaskname());}
        if(curmode==mode_folder)
        {
            inputfiles = inputfolder.listFiles();
        }
        for(int i=0;i<inputfiles.length;i++)
        {
            outname = inputfiles[i].getName();
            String t[]=outname.split("\\.");
            if(t.length==2){outname=t[0]+"."+outformat;}
            else if(t.length==3){outname=t[0]+"."+t[1]+"."+outformat;}
            if(super.isBShowLog()){System.out.print((i+1)+"/"+(inputfiles.length)+"-"+inputfiles[i].getName()+"->"+outname);}
            convertFile(inputfiles[i]);
            if(super.isBShowLog()){System.out.println(" ... done!");}
        }

        if(deletesourcefiles)
        {
            for(int i=0;i<inputfiles.length;i++)
            {
                inputfiles[i].delete();
            }
        }
    }


    public void convertFile(File inputtexfile)
    {
        File installfolder =Installation.getInstallDirectory(Main.class);
        //-ctype abgr -merge_alpha
        String cmd = installfolder.getAbsolutePath()+"\\bin\\nconvert\\nconvert.exe -32bits "+extracmdparameter+" -out "+this.outformat+" \""+inputtexfile.getAbsolutePath()+"\"";
        //System.out.println(cmd);
        Runtime run = Runtime.getRuntime();
        try {
            Process pp = run.exec(cmd);
            BufferedReader in =new BufferedReader(new InputStreamReader(pp.getInputStream()));
            String line;
            ArrayList allog = new ArrayList();
            while ((line = in.readLine()) != null) {
                //System.out.println(line);
                allog.add(line);
            }
            //System.out.println("... done!");
        } catch (IOException ex) {
            Logger.getLogger(TextureConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {
        this.convertAll();
    }


}
