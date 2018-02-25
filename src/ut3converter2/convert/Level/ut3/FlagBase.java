/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut3;

import ut3converter2.tools.actor.Location;

/**
 *
 * @author Hyperion
 */
public class FlagBase {

    String otherdata="";
    public static int team_red=0;
    public static int team_blue=1;
    int curmode;
    
    float deltaz=18F;

    public FlagBase(String curclass) {
        if(curclass.equals("xRedFlagBase"))
        {
            curmode = FlagBase.team_red;
        }
        else if(curclass.equals("xBlueFlagBase"))
        {
            curmode = FlagBase.team_blue;
        }
    }


    public String toString()
    {
        String tmp="";

        if(curmode==FlagBase.team_red)
        {
            tmp +="      Begin Actor Class=UTCTFRedFlagBase Name=UTCTFRedFlagBase_0 Archetype=UTCTFRedFlagBase\'UTGameContent.Default__UTCTFRedFlagBase\'\n";
            tmp +="         Tag=\"UTCTFRedFlagBase\"\n";
            tmp +=otherdata;
            tmp +="         Name=\"UTCTFRedFlagBase_0\"\n";
            tmp +="         ObjectArchetype=UTCTFRedFlagBase\'UTGameContent.Default__UTCTFRedFlagBase\'\n";
            tmp +="      End Actor\n";
        }
        else if(curmode==FlagBase.team_blue)
        {
            tmp +="      Begin Actor Class=UTCTFBlueFlagBase Name=UTCTFBlueFlagBase_0 Archetype=UTCTFBlueFlagBase\'UTGameContent.Default__UTCTFBlueFlagBase\'\n";
            tmp +="         Tag=\"UTCTFBlueFlagBase\"\n";
            tmp +=otherdata;
            tmp +="         CollisionComponent=CylinderComponent\'CylinderComponent_961\'\n";
            tmp +="         Name=\"UTCTFBlueFlagBase_0\"\n";
            tmp +="         ObjectArchetype=UTCTFBlueFlagBase\'UTGameContent.Default__UTCTFBlueFlagBase\'\n";
            tmp +="      End Actor\n";
        }
        
        return tmp;
    }

    public void AnalyseT3DData(String line)
    {
        //    VehicleClass=Class'Onslaught.ONSRV'
        if(line.contains(" Location"))
        {
            otherdata += Location.getLocationStrWithUpdatedZ(line, deltaz)+"\n";
        }
        else if(line.contains("DrawScale3D"))
        {
            otherdata += line+"\n";
        }
        else if(line.contains("DrawScale="))
        {
            otherdata += line+"\n";
        }
        else if(line.contains("Rotation"))
        {
            otherdata += line+"\n";
        }
        //FOR UT99 Maps
        //"    Team=1"
        else if(line.contains("Team"))
        {
            if(line.split("Team=")[0].equals("0")) //RED
            {
                curmode = team_red;
            }
            else
            {
                curmode = team_blue;
            }
        }

    }
}
