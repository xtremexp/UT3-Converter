/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut3;

import ut3converter2.convert.Level.UTActor;

/**
 * Base class for HeightFog actor in UT3
 * TODO BETTER RENDERING
 * @author Hyperion
 */
public class HeightFog extends UTActor{

    String otherdata="";
    boolean bcreat_hf=false;
    boolean bisfog=false;
    /**
     * Increases Z location so this actor is above most of level parts
     */
    float deltaz=10000F;
    float loc_x=0F;
    float loc_y=0F;
    float loc_z=0F;

    float density=0.00005F;
    float startdistance=1000F;
    float lightbrightness=0.9F;
    float ext_distance=100000000F;
    int blue=255;
    int green=255;
    int red=255;

    public HeightFog(boolean bcreatefog) {
        this.bcreat_hf = bcreatefog;
    }

    /*    AmbientBrightness=20
    AmbientHue=20
    AmbientSaturation=175
    DistanceFogColor=(B=66,G=60,R=49)
     * */
    public void AnalyseT3DData(String line)
    {
        //    StaticMesh=StaticMesh'AS-7Co-TheEgyptianPyramid-Final.Movers.door1'
        parseOtherData(line,deltaz);
        
        //DistanceFogColor=(B=66,G=60,R=49)
        if(line.contains("DistanceFogColor"))
        {
            getRGBValues(line);
        }
        else if(line.contains("DistanceFogStart"))
        {
            startdistance = Float.valueOf(line.split("\\=")[1])*0.8F;
        }
        else if(line.contains("DistanceFogEnd"))
        {
            ext_distance = Float.valueOf(line.split("\\=")[1])*0.9F;
        }
        else if(line.contains("bDistanceFog"))
        {
            String a = line.split("\\=")[1];
            if(a.equalsIgnoreCase("true"))
            {
                bisfog = true;
            }
            else
            {
                bisfog = false;
            }
        }
    }

    //DistanceFogColor=(B=66,G=60,R=49)
    /**
     * Retrieves Red Green Blue values from T3D level data line
     * @param line T3D level line data
     */
    private void getRGBValues(String line)
    {
        String tmp=line.split("DistanceFogColor=")[1];
        tmp = removeChar(tmp, 40);
        tmp = removeChar(tmp, 41);
        //DistanceFogColor=(B=64,R=255)
        String t[] = tmp.split("\\,");
        if(t.length==3)
        {
            blue = Integer.valueOf(t[0].split("\\=")[1]);
            green = Integer.valueOf(t[1].split("\\=")[1]);
            red = Integer.valueOf(t[2].split("\\=")[1]);
        }
        else if(t.length==2)
        {
            if(tmp.contains("B")&&(tmp.contains("R")))
            {
                blue = Integer.valueOf(t[0].split("\\=")[1]);
                red = Integer.valueOf(t[1].split("\\=")[1]);
            }
            else if(tmp.contains("B")&&(tmp.contains("G")))
            {
                blue = Integer.valueOf(t[0].split("\\=")[1]);
                green = Integer.valueOf(t[1].split("\\=")[1]);
            }
            else if(tmp.contains("G")&&(tmp.contains("R")))
            {
                green = Integer.valueOf(t[0].split("\\=")[1]);
                red = Integer.valueOf(t[1].split("\\=")[1]);
            }
        }
        else if(t.length==1)
        {
            if(tmp.contains("B")){blue = Integer.valueOf(t[0].split("\\=")[1]);}
            else if(tmp.contains("G")){green = Integer.valueOf(t[0].split("\\=")[1]);}
            else if(tmp.contains("R")){red = Integer.valueOf(t[0].split("\\=")[1]);}
        }
        
    }

    private String removeChar(String line,int numchar)
    {
        String tmp="";
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

    public String toString()
    {
        String tmp="";
        loc_z += 5000;
        if((bcreat_hf)&&(bisfog))
        {
            tmp +="      Begin Actor Class=HeightFog Name=HeightFog_0 Archetype=HeightFog\'Engine.Default__HeightFog\'\n";
            tmp +="         Begin Object Class=HeightFogComponent Name=HeightFogComponent0 ObjName=HeightFogComponent_0 Archetype=HeightFogComponent\'Engine.Default__HeightFog:HeightFogComponent0\'\n";
            tmp +="            Height="+loc_z+"\n";
            tmp +="            Density="+formatValue(density)+"\n";
            tmp +="            LightBrightness="+formatValue(lightbrightness)+"\n";
            tmp +="            ExtinctionDistance="+formatValue(ext_distance)+"\n";
            tmp +="            LightColor=(B="+blue+",G="+green+","+"R="+red+",A=0)\n";
            tmp +="            StartDistance="+startdistance+"\n";
            tmp +="            Name=\"HeightFogComponent_0\"\n";
            tmp +="            ObjectArchetype=HeightFogComponent\'Engine.Default__HeightFog:HeightFogComponent0\'\n";
            tmp +="         End Object\n";
            tmp +="         Begin Object Class=SpriteComponent Name=Sprite ObjName=SpriteComponent_12 Archetype=SpriteComponent\'Engine.Default__HeightFog:Sprite\'\n";
            tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
            tmp +="            Name=\"SpriteComponent_12\"\n";
            tmp +="            ObjectArchetype=SpriteComponent\'Engine.Default__HeightFog:Sprite\'\n";
            tmp +="         End Object\n";
            tmp +="         Component=HeightFogComponent\'HeightFogComponent_0\'\n";
            tmp +="         Components(0)=SpriteComponent\'SpriteComponent_12\'\n";
            tmp +="         Components(1)=HeightFogComponent\'HeightFogComponent_0\'\n";
            tmp += getOtherdata();
            tmp +="         ObjectArchetype=HeightFog\'Engine.Default__HeightFog\'\n";
            tmp +="      End Actor\n";
        }

        return tmp;
    }
}
