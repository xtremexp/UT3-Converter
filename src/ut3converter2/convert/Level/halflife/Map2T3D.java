/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.halflife;

import java.io.*;
import ut3converter2.convert.Level.ut2004.Light;

/**
 *
 * Allows to port half-life map files to Unreal T3D files.
 * For testing purposes only.
 * @author Hyperion
 */
public class Map2T3D {

    File mapfile;
    File t3dfileout;
    final String act_brush="Brush";
    final String act_light="Light";

    public Map2T3D(File mapfile) {
        this.mapfile = mapfile;
        t3dfileout = new File(mapfile.getParent()+"\\"+mapfile.getName().split("\\.")[0]+".t3d");
    }

    public void convert() throws FileNotFoundException, IOException
    {
        HlBrush hlb = null;
        String curactor="";
        String fldsval[];
        boolean startconvert=false;
        int numact=0;
        String line="";
        String otherdata="";
        BufferedReader bfr = new BufferedReader(new FileReader(mapfile));
        BufferedWriter bwr = new BufferedWriter(new FileWriter(t3dfileout));
        

        bwr.write("Begin Map\n");

        while((line=bfr.readLine())!=null)
        {
            if(line.contains("{")) //Begin Actor
            {
                if(numact==1){startconvert=true;}
                numact++;
                if(startconvert)
                {
                    if(line.contains("brush"))
                    {
                        curactor=act_brush;
                        hlb = new HlBrush();
                    }
                }
            }
            else if(line.contains("}"))
            {
                if(curactor.equals(act_light))
                {
                    bwr.write(Light.toString(otherdata));
                }
                else if(curactor.equals(act_brush))
                {
                    //hlb.writeBrush(bwr);
                }

                otherdata="";
            }
            else
            {
                if(startconvert)
                {
                    if(line.contains("classname"))
                    {
                        curactor = getUTActorName(getClassName(line));
                    }
                    else if(!curactor.equals(act_brush)&&(line.contains("\"")))
                    {
                        fldsval = getFieldAndValue(line);
                        otherdata += HlFieldToUTField.toUT2004Field(fldsval[0], fldsval[1]);
                    }
                    else if(curactor.equals(act_brush))
                    {
                        hlb.parseBrushData(line);
                    }
                }
            }
        }
        bwr.write("End Map\n");
        bfr.close();
        bwr.close();
    }

    /**
     *  "style" "0"
     * @param line
     * @return
     */
    private String[] getFieldAndValue(String line)
    {
        String t[]=line.split("\\\"");
        return new String[]{t[1],t[3]};
    }
    /**
     *  "classname" "trigger_changelevel"
     * @param line
     * @return
     */
    private String getClassName(String line)
    {
        return line.split("\\\"")[3];
    }

    private String getUTActorName(String classname)
    {
        if(classname.equals("light")){return act_light;}
        else if(classname.equals("light_spot")){return act_light;}
        else{return "";}
    }
    
}
