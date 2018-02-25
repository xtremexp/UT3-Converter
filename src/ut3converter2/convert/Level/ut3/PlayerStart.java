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
public class PlayerStart extends UTActor{

    String otherdata="";
    String numteam="0";
    public static int mode_playerstart=0;
    public static int mode_teamplayerstart=1;
    public static int mode_warfareteamplayerstart=2;
    int mode=0;

    public PlayerStart(int mode) {
        this.mode = mode;
    }



    public void AnalyseT3DData(String line)
    {
        parseOtherData(line);
        if(line.contains("TeamNumber"))
        {
            this.numteam = line.split("\\=")[1];
            mode = mode_teamplayerstart;
        }
    }

    public String toString()
    {
        String tmp="";
        if(mode==PlayerStart.mode_teamplayerstart)
        {
            tmp +="\tBegin Actor Class=UTTeamPlayerStart Name=UTTeamPlayerStart_0 Archetype=UTTeamPlayerStart\'UTGame.Default__UTTeamPlayerStart\'\n";
            tmp +="\t\tTeamNumber="+numteam+"\n";
            tmp += getOtherdata();
            tmp +="\t\tName=\"UTTeamPlayerStart_0\"\n";
            tmp +="\t\tObjectArchetype=UTTeamPlayerStart\'UTGame.Default__UTTeamPlayerStart\'\n";
            tmp +="\tEnd Actor\n";
        }
        else if(mode==PlayerStart.mode_playerstart)
        {
            tmp +="\tBegin Actor Class=PlayerStart Name=PlayerStart_0 Archetype=PlayerStart\'Engine.Default__PlayerStart\'\n";
            tmp += getOtherdata();
            tmp +="\t\tName=\"PlayerStart_0\"\n";
            tmp +="\t\tObjectArchetype=PlayerStart\'Engine.Default__PlayerStart\'\n";
            tmp +="\tEnd Actor\n";
        }
        else if(mode==PlayerStart.mode_warfareteamplayerstart)
        {
            tmp +="\tBegin Actor Class=UTWarfarePlayerStart Name=UTWarfarePlayerStart_0 Archetype=UTWarfarePlayerStart\'UTGame.Default__UTWarfarePlayerStart\'\n";
            tmp += getOtherdata();
            tmp +="\t\tName=\"UTWarfarePlayerStart_0\"\n";
            tmp +="\t\tObjectArchetype=UTWarfarePlayerStart\'UTGame.Default__UTWarfarePlayerStart\'\n";
            tmp +="\tEnd Actor\n";
        }
        

        return tmp;
    }
}
