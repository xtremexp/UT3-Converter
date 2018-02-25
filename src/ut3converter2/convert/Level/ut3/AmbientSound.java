/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut3;

import ut3converter2.convert.Level.UTActor;
import ut3converter2.convert.MapConverter;

/**
 *
 * @author Hyperion
 */
public class AmbientSound extends UTActor{

    String pacname="UT3Map";
    String sndname="";
    String otherdata="";
    Float soundradius=80F;
    int soundvolume=64;
    int soundpitch=0;

    public AmbientSound(MapConverter mc) {
        this.pacname = mc.getDefaultpackage();
        this.soundradius *= (float) mc.getScalefactor();
        this.soundvolume *= mc.getScalefactor();
    }

    public void AnalyseT3DData(String line)
    {
        parseOtherData(line);
        //    AmbientSound=Sound'OutdoorAmbience.BThunder.caveambience3'
        if(line.contains("AmbientSound="))
        {
            getSndName(line);
        }
        else if(line.contains("SoundRadius="))
        {
            soundradius = Float.valueOf(line.split("\\=")[1]);
        }
        else if(line.contains("SoundVolume="))
        {
            soundvolume = Integer.valueOf(line.split("\\=")[1]);
        }
        else if(line.contains("SoundPitch="))
        {
            soundpitch = Integer.valueOf(line.split("\\=")[1]);
        }
    }

   private void getSndName(String line)
    {
       String tmp;
       String tmp2[];
       //    AmbientSound=Sound'OutdoorAmbience.BThunder.caveambience3'
       tmp = line.split("\\'")[1]; //Rahnem_Glacier.Base.closed_box3'
       tmp2 = tmp.split("\\.");
       if(tmp2.length==2)
       {
           sndname=tmp2[1]+"Cue";
       }
       else if(tmp2.length==3)
       {
           sndname=tmp2[1]+"_"+tmp2[2]+"Cue";
       }
    }

    public String toString()
    {
        String tmp="";
        tmp +="      Begin Actor Class=AmbientSound Name=AmbientSound_0 Archetype=AmbientSound\'Engine.Default__AmbientSound\'\n";
        tmp +="         Begin Object Class=AudioComponent Name=AudioComponent0 ObjName=AudioComponent_2 Archetype=AudioComponent\'Engine.Default__AmbientSound:AudioComponent0\'\n";
        tmp +="            SoundCue=SoundCue\'"+pacname+"."+sndname+"\'\n";
        tmp +="            Name=\"AudioComponent_2\"\n";
        tmp +="            ObjectArchetype=AudioComponent\'Engine.Default__AmbientSound:AudioComponent0\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=SpriteComponent Name=Sprite ObjName=SpriteComponent_2 Archetype=SpriteComponent\'Engine.Default__AmbientSound:Sprite\'\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            Name=\"SpriteComponent_2\"\n";
        tmp +="            ObjectArchetype=SpriteComponent\'Engine.Default__AmbientSound:Sprite\'\n";
        tmp +="         End Object\n";
        tmp +="         bIsPlaying=True\n";
        tmp +="         AudioComponent=AudioComponent\'AudioComponent_2\'\n";
        tmp +="         Components(0)=SpriteComponent\'SpriteComponent_2\'\n";
        tmp +="         Components(1)=AudioComponent\'AudioComponent_2\'\n";
        tmp += getOtherdata();
        tmp +="         ObjectArchetype=AmbientSound\'Engine.Default__AmbientSound\'\n";
        tmp +="      End Actor\n";
        return tmp;
    }

}
