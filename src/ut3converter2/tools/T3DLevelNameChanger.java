/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import ut3converter2.T3DConvertor;

/**
 *
 * Changes level names into T3D Files
 * mainly used to remove bad chars such as [ or ]
 * This is also used to remove LevelName in some string such as:
 *     Brush=Model'AS-Mazon.Brush'
 * When importing is AS-Mazon is kept UnrealEd will crash as
 * it won't find the brush model.
 * In normal mode (should be used for UT2004 Ports):
 * Brush=Model'AS-Mazon.Brush'->Brush=Model'myLevel.Brush'
 * In specified mode (should be used for UT3 Ports as UT3 no longer recognize myLevel string):
 * Brush=Model'AS-Mazon.Brush'->Brush=Model'VCTF-MyMap.Brush'
 * @author Hyperion
 */
public class T3DLevelNameChanger extends T3DConvertor{

    String tskname="\n*** Replacing LevelName ";
    File inputt3dfile;
    File outputt3dfile;
    String inputstr;
    String outputstr;
    int curmode=0;
    String defaultlvlname="myLevel";
    public static int mode_normal=0;
    public static int mode_specified=1;

    public T3DLevelNameChanger(File inputt3dfile, File outputt3dfile, String inputstr, String outputstr) {

        this.inputt3dfile = inputt3dfile;
        this.outputt3dfile = outputt3dfile;
        this.inputstr = inputstr;
        this.outputstr = outputstr;
        this.curmode = T3DLevelNameChanger.mode_specified;
        super.setTaskname(this.tskname);
    }

    public T3DLevelNameChanger(File inputt3dfile, File outputt3dfile) {

        this.inputt3dfile = inputt3dfile;
        this.outputt3dfile = outputt3dfile;
        super.setTaskname(this.tskname);
    }

    public T3DLevelNameChanger(File inputt3dfile, File outputt3dfile,String deflvlname) {

        this.inputt3dfile = inputt3dfile;
        this.outputt3dfile = outputt3dfile;
        this.defaultlvlname = deflvlname;
        super.setTaskname(this.tskname);
    }

    public void setDefaultlvlname(String defaultlvlname) {
        this.defaultlvlname = defaultlvlname;
    }


    public File getOutputt3dfile() {
        return outputt3dfile;
    }

    public void replace() throws FileNotFoundException, IOException
    {
        if(super.isBShowLog()){System.out.print(super.getTaskname());}
        String line="";
        String levelprefix="";

        //BufferedReader bfr = FileDecoder.getBufferedReader(inputt3dfile, "Begin");
        BufferedReader bfr = new BufferedReader(new FileReader(inputt3dfile));
        BufferedWriter bwr = new BufferedWriter(new FileWriter(outputt3dfile));


        while((line=bfr.readLine())!=null)
        {
            if(curmode==mode_specified)
            {
                if(line.contains(inputstr))
                {
                    bwr.write(line.replace(inputstr, outputstr)+"\n");
                }
                else
                {
                    bwr.write(line+"\n");
                }
            }
            else if(curmode==mode_normal)
            {
                if(line.contains("'"))
                {
                    if(levelprefix.equals("")&&(line.contains("LevelSummary'")||line.contains("LevelInfo'")))
                    {
                        levelprefix = getLevelPrefix(line);
                        if(this.isBShowLog()){System.out.print(" (\""+levelprefix+"->\""+defaultlvlname+"\")");}
                    }
                    if(!levelprefix.equals("")){
                        bwr.write(line.replace(levelprefix, defaultlvlname)+"\n");
                    } else {
                        bwr.write(line+"\n");
                    }
                }
                else if(!levelprefix.equals(""))
                {
                    //          Begin Polygon Item=Step Texture=AS-TempleOfTrials-v4.Base_bas07BA_Mat
                    //AS-TempleOfTrials-v4
                    if(line.contains(levelprefix))
                    {
                        bwr.write(line.replace(levelprefix, defaultlvlname)+"\n");
                    }
                    else
                    {
                        bwr.write(line+"\n");
                    }
                }
                else
                {
                    bwr.write(line+"\n");
                }
            }
            
        }
        bwr.close();
        bfr.close();
        
        if(super.isBShowLog()){System.out.println(" ... done!");}
    }


    /**
     *     Region=(Zone=LevelInfo'AS-Mazon.LevelInfo0',iLeaf=-1)
     * @param line
     * @return
     */
    private String getLevelPrefix(String line)
    {
        return (line.split("\\'")[1]).split("\\.")[0];
    }
}
