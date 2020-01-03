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
public class UTOnsSpecialObj extends UTActor{

    String otherdata="";
    String objname="";

    public UTOnsSpecialObj() {
    }

    public void AnalyseT3DData(String line)
    {
        if(line.contains("ObjectiveName="))
        {
            this.objname = line.split("\"")[1];
        }
        parseOtherData(line);
    }

    public String toString()
    {
        String tmp="";
        tmp +="      Begin Actor Class=UTOnslaughtSpecialObjective Name=UTOnslaughtSpecialObjective_0 Archetype=UTOnslaughtSpecialObjective\'UTGame.Default__UTOnslaughtSpecialObjective\'\n";
        tmp +="         ObjectiveType=OBJ_Touch\n";
        tmp +="         bInitiallyActive=True\n";
        tmp +="         bTriggerOnceOnly=True\n";
        tmp +="         RequiredPawnClass=Class\'UTGame.UTPawn\'\n";
        tmp +="         bPathsChanged=True\n";
        tmp +="         CreationTime=3150.687012\n";
        tmp += getOtherdata();
        tmp +="         Name=\"UTOnslaughtSpecialObjective_0\"\n";
        tmp +="         ObjectArchetype=UTOnslaughtSpecialObjective\'UTGame.Default__UTOnslaughtSpecialObjective\'\n";
        tmp +="      End Actor\n";
        return tmp;
    }
}
