/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut2004;

/**
 *
 * @author Hyperion
 */
public class FlagBase {

    String otherdata="";
    int curteam=0;
    final int team_red=0;
    final int team_blue=1;

    public void AnalyseT3DData(String line)
    {
        if(line.contains("Team=1"))
        {
            curteam=1;
        }
        else
        {
            otherdata += line+"\n";
        }
    }

    public String toString()
    {
        String tmp="";
        if(curteam==team_red)
        {
            tmp +="Begin Actor Class=xRedFlagBase Name=xRedFlagBase0\n";
        }
        else if(curteam==team_blue)
        {
            tmp +="Begin Actor Class=xBlueFlagBase Name=xBlueFlagBase0\n";
        }
            tmp += otherdata;
            tmp +="End Actor\n";

        return tmp;
    }
}
