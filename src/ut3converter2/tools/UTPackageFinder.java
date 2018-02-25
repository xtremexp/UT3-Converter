/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Returns package name from given texture name
 * May not be correct as there may be several textures with same name in several packages.
 * Used for UT and U1 maps that don't keep package info in T3D Level files ...
 * @author Hyperion
 */
public class UTPackageFinder {

    /**
     * 
     * @param packagedatfile
     * @return 
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static HashMap getPackages(File packagedatfile) throws FileNotFoundException, IOException
    {
        HashMap hm = new HashMap();
        String t[];
        String line="";
        BufferedReader bfr = new BufferedReader(new FileReader(packagedatfile));

        while((line=bfr.readLine())!=null)
        {
            t = line.split("\\:");
            if(t.length>1)
            {
                if(!hm.containsKey(t[0]))
                {
                    hm.put(t[0], t[1]);
                }
            }
        }
        return hm;
    }

}
