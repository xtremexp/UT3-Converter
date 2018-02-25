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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Hyperion
 */
public class T3DLevelAnalyser {

    File t3dfiles[];
    HashMap hmactors;

    public T3DLevelAnalyser(File t3dfile) throws FileNotFoundException, IOException {
        this.t3dfiles = new File[1];
        this.t3dfiles[0] = t3dfile;
        loadActorClassesUsed();
    }

    public T3DLevelAnalyser(File[] t3dfiles) throws FileNotFoundException, IOException {
        this.t3dfiles = t3dfiles;
        loadActorClassesUsed();
    }

    public HashMap getHmactors() {
        return hmactors;
    }

    
    private void loadActorClassesUsed() throws FileNotFoundException, IOException
    {
        hmactors = new HashMap();

        int numactors=0;
        BufferedReader bfr;
        String line="";
        String curclass="";
        ArrayList altmp = new ArrayList();
        Object obj[];
        String filename="";
        File f;

        for(int i=0;i<t3dfiles.length;i++)
        {
            f = t3dfiles[i];
            bfr = new BufferedReader(new FileReader(f));
            while((line=bfr.readLine())!=null)
            {
                if(line.contains("Begin Actor"))
                {
                    curclass=getActorClass(line);
                    if(hmactors.containsKey(curclass))
                    {
                        obj = (Object[]) hmactors.get(curclass);
                        altmp = (ArrayList) obj[1];
                        numactors = Integer.valueOf(obj[0].toString());
                        numactors ++;
                        if(!altmp.contains(f.getName().split("\\.")[0]))
                        {
                            altmp.add(f.getName().split("\\.")[0]);
                        }
                        
                        hmactors.put(curclass, new Object[]{numactors,altmp});
                    }
                    else
                    {
                        altmp = new ArrayList();
                        altmp.add(f.getName().split("\\.")[0]);
                        hmactors.put(curclass, new Object[]{1,altmp});
                    }
                } else if(line.contains("Begin Terrain"))
                {
                    curclass="Terrain";
                    if(hmactors.containsKey(curclass))
                    {
                        obj = (Object[]) hmactors.get(curclass);
                        altmp = (ArrayList) obj[1];
                        numactors = Integer.valueOf(obj[0].toString());
                        numactors ++;
                        if(!altmp.contains(f.getName().split("\\.")[0]))
                        {
                            altmp.add(f.getName().split("\\.")[0]);
                        }

                        hmactors.put(curclass, new Object[]{numactors,altmp});
                    }
                    else
                    {
                        altmp = new ArrayList();
                        altmp.add(f.getName().split("\\.")[0]);
                        hmactors.put(curclass, new Object[]{1,altmp});
                    }
                }
            }
        }
        
    }
    public DefaultTableModel getActorClassesUsed()
    {
        DefaultTableModel dtm = new DefaultTableModel(new String[]{"Actor Name", "Count","Map"}, 0);
        Object obj[];
        ArrayList altmp;
            
        String actorname="";
        int numact=0;
        Iterator it = hmactors.keySet().iterator();
        while(it.hasNext())
        {
            actorname = it.next().toString();
            obj = (Object[]) hmactors.get(actorname);
            numact=Integer.valueOf(obj[0].toString());
            altmp = (ArrayList) obj[1];
            dtm.addRow(new Object[]{actorname,numact,altmp});
        }


        return dtm;
    }

    private String getActorClass(String line)
    {
        return (line.split("=")[1]).split(" ")[0];
    }

    public String getActor(int num) throws FileNotFoundException, IOException
    {
        BufferedReader bfr = new BufferedReader(new FileReader(t3dfiles[0]));
        String line="";
        int numactor=1;

        while((line=bfr.readLine())!=null)
        {
            if(line.contains("Begin Actor"))
            {
                numactor ++;
            }
            if(numactor==num)
            {
                return line;
            }
        }

        return "No";
    }
}
