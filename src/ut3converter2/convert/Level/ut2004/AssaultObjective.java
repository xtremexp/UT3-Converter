/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut2004;

import ut3converter2.convert.Level.UTActor;

/**
 *
 * @author Hyperion
 */
public class AssaultObjective extends UTActor{

    String otherdata="";
    String destruction_msg="Objective Disabled!";
    String objective_name="The objective";
    String endcamera_tag="None";
    String event="EndMap";
    String defprio="";
    final int type_proximityobj=0;
    final int type_destobj=1;
    int curtype=1;
    
    public void AnalyseT3DData(String line)
    {
        //    StaticMesh=StaticMesh'AS-7Co-TheEgyptianPyramid-Final.Movers.door1'
        if(line.contains("DefensePriority"))
        {
            defprio = line.split("\\=")[1];
            otherdata += line+"\n";
        }
        else if(line.contains("DestroyedMessage"))
        {
            this.destruction_msg = removeChar(line.split("\\=")[1],34);
        }
        else if(line.contains("EndCamTag"))
        {
            this.endcamera_tag = line.split("\\=")[1];
        }
        else if(line.contains("FortName"))
        {
            this.objective_name = removeChar(line.split("\\=")[1],34);
        }
        else if(line.toLowerCase().contains("btriggeronly=true"))
        {
            curtype = this.type_proximityobj;
        }
        else
        {
            otherdata += line+"\n";
        }
    }

    public String toString()
    {
        String tmp="";
        if(curtype==this.type_proximityobj)
        {
            tmp +="Begin Actor Class=ProximityObjective Name=ProximityObjective0\n";
        }
        else if(curtype==this.type_destobj)
        {
            tmp +="Begin Actor Class=DestroyableObjective Name=DestroyableObjective0\n";
        }
            tmp += otherdata;
            tmp +="    ObjectiveName=\""+objective_name+"\"\n";
            tmp +="    DestructionMessage=\""+destruction_msg+"\"\n";
            tmp +="    EndCameraTag=\""+this.endcamera_tag+"\"\n";
        if(curtype==this.type_proximityobj)
        {
            tmp +="    ObjectiveDescription=\"Touch "+objective_name+" to disable it.\"\n";
            tmp +="    Objective_Info_Attacker=\"Touch "+objective_name+"\"\n";
        }
        else if(curtype==this.type_proximityobj)
        {
            tmp +="    ObjectiveDescription=\"Destroy "+objective_name+" to disable it.\"\n";
            tmp +="    Objective_Info_Attacker=\"Destroy "+objective_name+"\"\n";
        }
            tmp +="    Objective_Info_Defender=\"Defend "+objective_name+"\"\n";
            tmp +="    bPathsChanged=True\n";
            tmp +="    bLightChanged=True\n";
            tmp +="    Level=LevelInfo\'myLevel.LevelInfo0\'\n";
            tmp +="    Region=(Zone=LevelInfo\'myLevel.LevelInfo0\',iLeaf=5,ZoneNumber=1)\n";
            tmp +="    Tag=\"ProximityObjective\"\n";
            tmp +="    PhysicsVolume=DefaultPhysicsVolume\'myLevel.DefaultPhysicsVolume0\'\n";
            tmp +="End Actor\n";

            if(defprio.equals(""))
            {
                tmp +="   Begin Actor Class=Trigger_ASRoundEnd Name=Trigger_ASRoundEnd0\n";
                tmp +="   Location=(X=0,Y=0.000000,Z=0.000000)\n";
                tmp +="   Tag="+event+"\n";
                tmp +="   End Actor\n";
            }
        return tmp;
    }

    private String removeChar(String line,int numchar)
    {
        String tmp="";
        int previouschar=-1;
        int nchar=0;

        for(int i=0;i<line.length();i++)
        {
            nchar=(int)line.charAt(i);
            if(nchar!=numchar)
            {
                tmp += line.charAt(i);
            }
        }
        return tmp;
    }

}
