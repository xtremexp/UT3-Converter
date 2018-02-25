/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.terrain;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ut3converter2.Installation;
import ut3converter2.Main;
import ut3converter2.T3DConvertor;


/**
 *
 * @author Hyperion
 */
public class TerBmpToTiff extends T3DConvertor{

    File termapfile = null;
    File outputfolder = null;

    public TerBmpToTiff(File termapfile,File outputfolder) {
        this.outputfolder = outputfolder;
        this.termapfile = termapfile;
        super.setTaskname("\t Converting heightmap terrain bitmap to .tiff ");
        super.setBShowLog(true);
    }

    public TerBmpToTiff(File termapfile) {
        this.outputfolder = termapfile.getParentFile().getParentFile();
        this.termapfile = termapfile;
        super.setTaskname("\t Converting heightmap terrain bitmap to .tiff ");
        super.setBShowLog(true);
    }

    public void ExportToTiff()
    {
        String cmd = "\""
                +Installation.getInstallDirectory(Main.class).getAbsolutePath()
                + File.separator+"bin"+File.separator+"g16convert"+File.separator+"g16convert\""
                + " \""+termapfile.getAbsolutePath() + "\""
                + " \""+outputfolder.getAbsolutePath()+File.separator+getOutputFile()+"\"";
        //System.out.println(cmd);

        if(isBShowLog()){System.out.println("\t Converting heightmap terrain bmp texture to tiff");}
        try {
            ArrayList log = new ArrayList();
            Runtime run = Runtime.getRuntime();
            Process pp = run.exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(pp.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                //System.out.println(line);
                log.add(line);
            }
            int exitVal = pp.waitFor();
            pp.exitValue();
            if (exitVal == 1) {
                String error = "";
                error = "g16convert.exe was unable to convert terrain map file";
                //if(isBShowLog()){System.out.println("... Error! (" + error + ")");}
                
            } else {
                //if(isBShowLog()){System.out.println( "... done!");}
            }
        } catch (IOException ex) {
            Logger.getLogger(TerBmpToTiff.class.getName()).log(Level.SEVERE, null, ex);
        }   catch (InterruptedException ex) {

        }
    }

    private String getOutputFile()
    {
        //Terrain.ground1.bmp
        String tmp=termapfile.getName();
        String tmp2[]=tmp.split("\\.");
        if(tmp2.length==2)
        {
            tmp=tmp2[0]+".tiff";
        }
        else if(tmp2.length==3)
        {
            tmp=tmp2[0]+"."+tmp2[1]+".tiff";
        }
        return tmp;
    }



}
