/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.identification;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import ut3converter2.tools.FileDecoder;

/**
 * Get num of specific actors used in a map
 * @author Hyperion
 */
public class ActorsIdentificator {

    File t3dmapfile;
    HashMap hmstats;

    public ActorsIdentificator(File t3dmapfile) {
        this.t3dmapfile = t3dmapfile;
        hmstats = new HashMap();
        analyze();
    }

    public void analyze()
    {
        try {
            //BufferedReader bfr = FileDecoder.getBufferedReader(t3dmapfile, "Begin");
            BufferedReader bfr = new BufferedReader(new FileReader(t3dmapfile));
            String line="";
            String curclass="";
            int num=0;
            while((line=bfr.readLine())!=null)
            {
                if(line.contains("Begin Actor"))
                {
                    curclass = getActorClass(line);
                    if(hmstats.containsKey(curclass)==false)
                    {
                        hmstats.put(curclass, 1);
                    }
                    else
                    {
                        num = Integer.valueOf(hmstats.get(curclass).toString());
                        num ++;
                        hmstats.put(curclass, num);
                    }
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ActorsIdentificator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ActorsIdentificator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getActorsStats()
    {
        String curclass="";
        int num=0;
        System.out.println("Actors stats for:"+this.t3dmapfile.getAbsolutePath());
        for (Iterator it = hmstats.keySet().iterator(); it.hasNext();) {
            curclass = it.next().toString();
            num=Integer.valueOf(hmstats.get(curclass).toString());
            System.out.println(curclass+" - "+num);
        }
    }
    
    private String getActorClass(String line)
    {
        //Begin Actor Class=StaticMeshActor Name=StaticMeshActor8
        //Begin Actor Class=Brush Name=Brush282
        //Begin Actor Class=ScriptedTrigger Name=ScriptedTrigger13
        String actorclass="";
        //Begin Actor Class=ScriptedTrigger Name=ScriptedTrigger13
        //ScriptedTrigger Name=ScriptedTrigger13
        //ScriptedTrigger
        return (line.split("=")[1]).split(" ")[0];
    }


}
