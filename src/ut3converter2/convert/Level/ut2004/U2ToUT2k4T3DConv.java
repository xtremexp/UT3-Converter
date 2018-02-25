/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut2004;

import java.io.*;
import ut3converter2.convert.Level.SupU2ToUT2k4Classes;

/**
 *
 * @author Hyperion
 */
public class U2ToUT2k4T3DConv {

    File u2t3dfile;

    public U2ToUT2k4T3DConv(File u2t3dfile) {
        this.u2t3dfile = u2t3dfile;
    }

    public void convert() throws FileNotFoundException, IOException
    {


        //BufferedReader bfr = FileDecoder.getBufferedReader(u2t3dfile, "Begin");
        BufferedReader bfr = new BufferedReader(new FileReader(u2t3dfile));
        BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(u2t3dfile.getParent()+"\\U2Map.t3d")));

        String line="";
        String curactor="";
        SupU2ToUT2k4Classes s2 = new SupU2ToUT2k4Classes();
        
        bwr.write("Begin Map\n");
        while((line=bfr.readLine())!=null)
        {
            if(line.contains("Begin Actor"))
            {
                curactor = getActorClass(line);
                if(s2.isSupportedClass(curactor))
                {
                    bwr.write(line+"\n");
                }
            }
            else
            {
                if(curactor.equals("Brush"))
                {
                    if(line.contains("UseReticleOnEvents")||line.contains("ProximityReticleOnEvents")||line.contains("PrimaryStaticLight"))
                    {

                    }
                    //Brush=Model'M08B.Model159'
                    else if(line.contains("Brush=Model"))
                    {
                        bwr.write("     Brush=Model'myLevel."+getNumModel(line)+"'\n");
                    }
                    else
                    {
                        bwr.write(line+"\n");
                    }
                }
                else if(s2.isSupportedClass(curactor))
                {
                    bwr.write(line+"\n");
                }
            }
        }
        bwr.write("End Map\n");
        bfr.close();
        bwr.close();
    }

    //Brush=Model'M08B.Model159'
    private String getNumModel(String line)
    {
        String nummod="";
        nummod = line.split("\\.")[1];
        return nummod.substring(0,nummod.length()-1);
    }

    private String getActorClass(String line)
    {
        return (line.split("=")[1]).split(" ")[0];
    }

}
