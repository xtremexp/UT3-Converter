/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut2004;

/**
 *
 * @author Hyperion
 */
public class LevelInfo {

    String otherdata="";
    String gametype="\"XGame.xDeathMatch\"";
    String song="";

     public void AnalyseT3DData(String line)
    {
        //    StaticMesh=StaticMesh'AS-7Co-TheEgyptianPyramid-Final.Movers.door1'
        if(line.contains("DefaultGameType="))
        {
            gametype = UT2k4GameTypes.getUT2004Gametype(line.split("\\=")[1]);
        }
        //Song=Music'Foregone.Foregone'
        else if(line.contains("Song="))
        {
            song = (line.split("\\'")[1]).split("\\.")[0];
        }
        else
        {
            otherdata += line+"\n";
        }
    }

     public String toString()
    {
        String tmp="";

            tmp +="Begin Actor Class=LevelInfo Name=LevelInfo0\n";
            tmp +="    Song="+song+"\n";
            tmp +="    DefaultGameType="+gametype+"\n";

            tmp += otherdata;
            tmp +="End Actor\n";

            if(UT2k4GameTypes.isAssault(gametype))
            {

                tmp +="Begin Actor Class=PlayerSpawnManager Name=PlayerSpawnManager0\n";
                tmp +="    Location=(X=0.000000,Y=0.000000,Z=50.000000)\n";
                tmp +="End Actor\n";
                tmp +="Begin Actor Class=PlayerSpawnManager Name=PlayerSpawnManager1\n";
                tmp +="    Location=(X=0.000000,Y=0.000000,Z=100.000000)\n";
                tmp +="    AssaultTeam=EPSM_Defenders\n";
                tmp +="    PlayerStartTeam=1\n";
                tmp +="End Actor\n";
            }
        return tmp;
    }

}
