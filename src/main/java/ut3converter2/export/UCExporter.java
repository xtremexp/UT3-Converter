/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.export;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import ut3converter2.Main;
import ut3converter2.UTGames;
import ut3converter2.convert.MapConverter;
import ut3converter2.T3DConvertor;

/**
 * Export Unreal Classes (.uc) files from Unreal Paclages
 * @author Hyperion
 */
public class UCExporter extends T3DConvertor{

    File[] ufiles;
    File outputfolder;
    /**
     * @see UTGames
     */
    int utgame;


    /**
     *
     * @param ufiles Can be .u,.un2,.unr files ...
     * @param outputfolder
     * @param utgame
     */
    public UCExporter(File[] ufiles,File outputfolder,int utgame) {
        this.ufiles = ufiles;
        this.utgame = utgame;
        this.outputfolder = outputfolder;
    }

    /**
     *
     * @param ufile Unreal Package file - Can be system file .u or map files like .un2,.unr file ...
     * @param outputfolder Where the uc files will be exported to
     * @param utgame Num of UT Game @see UTGame
     */
    public UCExporter(File ufile,File outputfolder,int utgame) {
        File ftmp[]=new File[1];
        ftmp[0] = ufile;
        ufiles = ftmp;
        this.utgame = utgame;
        this.outputfolder = outputfolder;
    }

    public UCExporter(MapConverter mc){
        File ftmp[]=new File[1];
        ftmp[0] = mc.getInputfile();
        ufiles = ftmp;
        this.utgame = mc.getInput_utgame();
        this.outputfolder = mc.getOutputfolder();
    }
    /**
     * Exports all UC files from specified Unreal Package files
     */
    public void Export()
    {
        if(super.isBShowLog())
        {
            System.out.println("\n*** Extracting Unreal Class (.uc) files ****");
        }
        String filename;
        for(int i=0;i<ufiles.length;i++)
        {
            filename = ufiles[i].getName();
            System.out.print((i+1)+"/"+ufiles.length+"-"+filename);
            ExportUCFile(ufiles[i]);
            System.out.println(" ... Done!");
        }
    }

    /**
     * Export uc files for specifies unreal package file
     * @param ufile Unreal package files (.u,.unr,.un2,.ut2,...)
     */
    private void ExportUCFile(File ufile)
    {
        try {
            String cmd = Main.config.getUTxRootFolder(utgame).getAbsolutePath() + File.separator + "System" + File.separator + "ucc batchexport \"" + ufile.getAbsolutePath() + "\" class uc \"" + outputfolder.getAbsolutePath() + "\"";

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
        } catch (IOException ex) {
            Logger.getLogger(UCExporter.class.getName()).log(Level.SEVERE, null, ex);
        }catch (InterruptedException ex) {
            Logger.getLogger(UCExporter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
