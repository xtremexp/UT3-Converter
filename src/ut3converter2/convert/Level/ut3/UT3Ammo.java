/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut3;

import ut3converter2.convert.Level.UTActor;

/**
 *
 * @author Hyperion
 */
public class UT3Ammo extends UTActor{

    String amclassnm="";
    String otherdata="";

    public void setAmClassNm(String name)
    {
        this.amclassnm = name;
    }

    public void AnalyseT3DData(String line)
    {
        parseOtherData(line);
        if(line.contains("Begin Actor"));
        {
            String t[] = line.split("AmmoPickup");
            //this.num = t[t.length-1];
        }
    }

    public String toString()
    {
        String tmp="";
        tmp +=" Begin Actor Class="+amclassnm+" Name="+amclassnm+"_0 Archetype="+amclassnm+"\'UTGameContent.Default__"+amclassnm+"\'\n";
        tmp +="     bPathsChanged=True\n";
        tmp +="     Tag=\""+amclassnm+"\"\n";
        tmp += getOtherdata();
        tmp +="     Name=\""+amclassnm+"\"\n";
        tmp +="     ObjectArchetype="+amclassnm+"\'UTGameContent.Default__"+amclassnm+"\'\n";
        tmp +=" End Actor\n";
        return tmp;
    }
}
