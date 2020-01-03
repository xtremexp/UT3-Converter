/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut3;

import ut3converter2.convert.Level.UTActor;

/**
 * PostProcessVolume
 * Used to simulate vision under water, lava, glime, ...
 * Adds some "foggy" display (blue,red,green,..)
 * @TODO Handle DistanceFogColor=(B=135,G=147,R=87)
 * @author Hyperion
 */
public class PostProcessVolume extends UTActor{

    String brushdata="";
    //PrePivot=(X=-15872.000000,Y=8296.000000,Z=-2680.000000)
    String prepivot="";
    String modelname;
    boolean isbrushdata=false;
    String classname;
    String DOF_FocusInnerRadius="256.000000";
    
    public PostProcessVolume(String classname) {
        this.classname = classname;
    }


    public void analyzeT3DData(String line)
    {
        if(!isbrushdata)
        {
            parseOtherData(line,10F);
        }
        //    Begin Brush Name=Model86
        if(line.contains("Begin Brush"))
        {
            isbrushdata = true;
            modelname = line.split("Name=")[1];
        }
        else if(line.contains("End Brush"))
        {
            isbrushdata = false;
            brushdata+= line+"\n";
        }
        //    DistanceFogStart=-50.000000
        else if(line.contains("DistanceFogStart"))
        {
            DOF_FocusInnerRadius = line.split("DistanceFogStart=")[1];
        }


        if(isbrushdata)
        {
            brushdata+= line+"\n";
        }
        else if(line.contains("PrePivot"))
        {
            prepivot = line;
        }
    }

    public String toString()
    {
        String tmp="";
        tmp +="      Begin Actor Class=PostProcessVolume Name=PostProcessVolume_0 Archetype=PostProcessVolume\'Engine.Default__PostProcessVolume\'\n";
        tmp +="       Settings=(bEnableDOF=True,DOF_MaxNearBlurAmount=0.250000,DOF_FocusInnerRadius="+DOF_FocusInnerRadius+",DOF_FocusDistance=128.000000,"+getSceneHightLights()+"))\n";
        tmp += brushdata;
        tmp +="       Brush=Model\'"+modelname+"\'\n";
        tmp +="       Tag=\"PostProcessVolume\"\n";
        tmp += getOtherdata();
        tmp += prepivot+"\n";
        tmp +="       Name=\"PostProcessVolume_0\"\n";
        tmp +="       ObjectArchetype=PostProcessVolume\'Engine.Default__PostProcessVolume\'\n";
        tmp +="      End Actor\n";
        return tmp;
    }

    private String getSceneHightLights()
    {
        if(this.classname.equals("WaterVolume"))
        {
            return getSceneHightLightsWaterVolume();
        }
        else if(this.classname.equals("LavaVolume"))
        {
            return getSceneHightLightsLavaVolume();
        }
        else if(this.classname.equals("SlimeVolume"))
        {
            return getSceneHightLightsSlimeVolume();
        }
        else
        {
            return getSceneHightLightsWaterVolume();
        }
    }
    private String getSceneHightLightsWaterVolume()
    {
        return "Scene_HighLights=(X=2.000000,Y=1.000000,Z=0.500000)";
    }

    private String getSceneHightLightsLavaVolume()
    {
        return "Scene_HighLights=(X=0.500000,Y=1.000000,Z=2.000000)";
    }

    private String getSceneHightLightsSlimeVolume()
    {
        return "Scene_HighLights=(X=6.000000,Y=1.000000,Z=8.000000)";
    }
}
