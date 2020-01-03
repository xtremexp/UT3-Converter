/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut3;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 *
 * @author Hyperion
 */
public class BlockingVolume {

    String otherdata="";
    boolean createbl = true;
    boolean bdirectwrite=false;
    BufferedWriter bwr;
    
    public BlockingVolume(boolean bdirectwrite,BufferedWriter bwr) {
        this.bdirectwrite = bdirectwrite;
        this.bwr = bwr;
    }

    public BlockingVolume()
    {

    }

    public void AnalyseT3DData(String line) throws IOException
    {
        if(bdirectwrite)
        {
            bwr.write(line+"\n");
        }
        else
        {
            //System.out.println(line);
            if(line.contains("BlockedClasses"))
            {
                this.createbl = false;
            }
            else
            {
                otherdata += line+"\n";
            }
        }
        
    }

    public String toString()
    {
        if(bdirectwrite)
        {
            return "      End Actor\n";
        }
        else
        {
            if(createbl)
            {
                return otherdata+"      End Actor\n";
            }
            else
            {
                return "";
            }
        }

        
    }



}
