/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Sound;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ut3converter2.Installation;
import ut3converter2.Main;
import ut3converter2.convert.Texture.TextureConverter;
import ut3converter2.T3DConvertor;

/**
 *
 * @author Hyperion
 */
public class SoundConverter extends T3DConvertor {

    final String tskname="\n*** Conversion of wave sounds files to 44.1K sample rate ***";
    File inputfolder;
    File outputfolder;
    File sndfiles[];
    String samplerate="44100";
    static int mode_files=0;
    static int mode_folder=1;
    int curmode=1;

    public SoundConverter(File inputfolder) {
        if(inputfolder.isDirectory())
        {
            this.inputfolder = inputfolder;
            curmode=SoundConverter.mode_folder;
            setBShowLog(true);
            setTaskname(tskname);
        }
        else
        {
            curmode=SoundConverter.mode_files;
            sndfiles = new File[1];
            sndfiles[0] = inputfolder;
        }
        
    }

    public void setOutputfolder(File outputfolder) {
        this.outputfolder = outputfolder;
    }

    public File getInputfolder() {
        return inputfolder;
    }

    public void setInputfolder(File inputfolder) {
        this.inputfolder = inputfolder;
    }

    public void convertAll()
    {
        File f[];
        if(curmode==mode_folder)
        {
            f=inputfolder.listFiles();
        }
        else
        {
            f = sndfiles;
        }
        if(isBShowLog()){System.out.println(tskname);}
        for(int i=0;i<f.length;i++)
        {
            if(isBShowLog()){System.out.print((i+1)+"/"+(f.length)+"-"+f[i].getName());}
            convertTo(f[i]);
        }
        
    }

    public static void convertMusicFile(File inputmusicfile,File outputfolder)
    {
        File installfolder =Installation.getInstallDirectory(Main.class);
        System.out.print("*** Conversion of "+inputmusicfile.getName()+" music file ...");
        String cmd = installfolder.getAbsolutePath()+"\\bin\\sox\\sox.exe \""+inputmusicfile.getAbsolutePath()+"\" -2 \""+outputfolder.getAbsolutePath()+File.separator+inputmusicfile.getName().replaceAll("\\.ogg", ".wav")+"\"";
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
            System.out.println(" done!");
        } catch (IOException ex) {
            Logger.getLogger(TextureConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public  void convertTo(File inputwavfile)
    {
        File installfolder =Installation.getInstallDirectory(Main.class);
        String cmd = installfolder.getAbsolutePath()+"\\bin\\sox\\sox.exe \""+inputwavfile.getAbsolutePath()+"\" -r "+samplerate+" -2 \""+inputwavfile.getAbsolutePath().replaceAll(".wav", "-44k.wav")+"\"";
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
            if(isBShowLog()){System.out.println(" ... done!");}
        } catch (IOException ex) {
            Logger.getLogger(TextureConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
