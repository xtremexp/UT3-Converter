/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut3;

import ut3converter2.UTGames;
import ut3converter2.convert.Level.UTActor;
import ut3converter2.convert.MapConverter;

/**
 *
 * @author Hyperion
 */
public class AmbientSoundSimple extends UTActor{

    float soundradiusfactor = 20F; //20 for UT->UT3,2 for UT2k4->UT3
    String pacname="UT3Map";
    String sndname="";
    String otherdata="";
    Float soundradius=64F;
    float soundvolume=1F; //100
    float soundpitch=1F; //64
    float soundpitchfactor = 0.6F;
    /**
     * Used for UT99->UT3 conversion (not using group info in wav filename)
     * Uses: MySoundPackage_Name-44k instead of MySoundPackage.Group_Name-44k
     */
    boolean buseut99sndname=false;
    MapConverter mc;

    public AmbientSoundSimple(MapConverter mc) {
        this.pacname = mc.getDefaultpackage();
        this.mc = mc;
        if(mc.getInput_utgame()==UTGames.U2){
            buseut99sndname = true;
        }
        this.soundradius *= (float)mc.getScalefactor();
        this.soundvolume *= (float)mc.getScalefactor();
    }
    


    public AmbientSoundSimple(String pacname,String sndname)
    {
        this.pacname = pacname;
        this.sndname = sndname;
    }
    
    public void formatPacnameToUT99()
    {
       String tmp2[];
       //    AmbientSound=Sound'OutdoorAmbience.BThunder.caveambience3'
       //AmbientSound=Sound'AmbModern.Looping.alarm3'

       tmp2 = sndname.split("\\.");
       if(tmp2.length==2)
       {
           sndname=tmp2[1]+"-44k";
       }
       else if(tmp2.length==3)
       {
           if(buseut99sndname){sndname=tmp2[2]+"-44k";}
           else{
               sndname=tmp2[1]+"_"+tmp2[2]+"-44k";
           }
       }

    }
    public void setSoundradiusfactor(float soundradiusfactor) {
        this.soundradiusfactor = soundradiusfactor;
    }

    


    



    public void AnalyseT3DData(String line)
    {
        parseOtherData(line);
        //    AmbientSound=Sound'OutdoorAmbience.BThunder.caveambience3'
        if(line.contains("AmbientSound="))
        {
            getSndName(line);
        }
        else if(line.contains(" SoundRadius=")||line.contains("\tSoundRadius="))
        {
            soundradius = Float.valueOf(line.split("\\=")[1]);
            soundradius *= (float)(soundradiusfactor*mc.getScalefactor());
        }
        else if(line.contains(" SoundVolume=")||line.contains("\tSoundVolume="))
        {
            soundvolume = Integer.valueOf(line.split("\\=")[1]);
            soundvolume = (float) ((soundvolume * mc.getScalefactor()) / 64F);
        }

        //UT: SoundPitch: 0--64(default)-->128
        //UT3: SoundPitch: 0--1(default)-->2
        else if(line.contains("SoundPitch="))
        {
            soundpitch = Float.valueOf(line.split("\\=")[1]); //42
            soundpitch = soundpitch/64F;
        }
    }

    /**
     * Used for UT99->UT3 conversion (not using group info in wav filename)
     * @param buseut99sndname If true, doesn't keep group info for wav files in AmbientSound info.
     */
    public void setBuseut99sndname(boolean buseut99sndname) {
        this.buseut99sndname = buseut99sndname;
    }

    private void getSndName(String line)
    {
       String tmp;
       String tmp2[];
       //    AmbientSound=Sound'OutdoorAmbience.BThunder.caveambience3'
       //AmbientSound=Sound'AmbModern.Looping.alarm3'
       tmp = line.split("\\'")[1]; //AmbModern.Looping.alarm3
       tmp2 = tmp.split("\\.");
       if(tmp2.length==2)
       {
           sndname=tmp2[1]+"-44k";
       }
       else if(tmp2.length==3)
       {
           if(buseut99sndname){sndname=tmp2[2]+"-44k";}
           else{
               sndname=tmp2[1]+"_"+tmp2[2]+"-44k";
           }
       }
    }

    public void setSoundpitch(float soundpitch) {
        this.soundpitch = soundpitch;
    }

    public void setSoundradius(Float soundradius) {
        this.soundradius = soundradius;
    }

    public void setSoundvolume(float soundvolume) {
        this.soundvolume = soundvolume;
    }

     public String toString()
     {
         
         
         String tmp="";
            tmp +="      Begin Actor Class=AmbientSoundSimple Name=AmbientSoundSimple_1 Archetype=AmbientSoundSimple\'Engine.Default__AmbientSoundSimple\'\n";
            tmp +="         Begin Object Class=SoundCue Name=SoundCue_1 ObjName=SoundCue_1 Archetype=SoundCue\'Engine.Default__AmbientSoundSimple:SoundCue0\'\n";
            //tmp +="            FirstNode=SoundNodeAmbient\'SoundNodeAmbient_0\'\n";
            tmp +="            Duration=10000.000000\n";
            tmp +="            Name=\"SoundCue_1\"\n";
            tmp +="            ObjectArchetype=SoundCue\'Engine.Default__AmbientSoundSimple:SoundCue0\'\n";
            tmp +="         End Object\n";
            tmp +="         Begin Object Class=SoundNodeAmbient Name=SoundNodeAmbient_0 ObjName=SoundNodeAmbient_0 Archetype=SoundNodeAmbient\'Engine.Default__AmbientSoundSimple:SoundNodeAmbient0\'\n";
            tmp +="            Begin Object Class=DistributionFloatUniform Name=DistributionMinRadius ObjName=DistributionFloatUniform_0 Archetype=DistributionFloatUniform\'Engine.Default__AmbientSoundSimple:SoundNodeAmbient0.DistributionMinRadius\'\n";
            tmp +="               Min=0.000000\n";
            tmp +="               Max=0.000000\n";
            tmp +="               bIsDirty=False\n";
            tmp +="               Name=\"DistributionFloatUniform_0\"\n";
            tmp +="               ObjectArchetype=DistributionFloatUniform\'Engine.Default__AmbientSoundSimple:SoundNodeAmbient0.DistributionMinRadius\'\n";
            tmp +="            End Object\n";
            tmp +="            Begin Object Class=DistributionFloatUniform Name=DistributionMaxRadius ObjName=DistributionFloatUniform_1 Archetype=DistributionFloatUniform\'Engine.Default__AmbientSoundSimple:SoundNodeAmbient0.DistributionMaxRadius\'\n";
            tmp +="               Min=0\n";
            tmp +="               Max="+soundradius+"\n";
            tmp +="               bIsDirty=False\n";
            tmp +="               Name=\"DistributionFloatUniform_1\"\n";
            tmp +="               ObjectArchetype=DistributionFloatUniform\'Engine.Default__AmbientSoundSimple:SoundNodeAmbient0.DistributionMaxRadius\'\n";
            tmp +="            End Object\n";
            tmp +="            Begin Object Class=DistributionFloatUniform Name=DistributionLPFMinRadius ObjName=DistributionFloatUniform_2 Archetype=DistributionFloatUniform\'Engine.Default__AmbientSoundSimple:SoundNodeAmbient0.DistributionLPFMinRadius\'\n";
            tmp +="               bIsDirty=False\n";
            tmp +="               Name=\"DistributionFloatUniform_2\"\n";
            tmp +="               ObjectArchetype=DistributionFloatUniform\'Engine.Default__AmbientSoundSimple:SoundNodeAmbient0.DistributionLPFMinRadius\'\n";
            tmp +="            End Object\n";
            tmp +="            Begin Object Class=DistributionFloatUniform Name=DistributionLPFMaxRadius ObjName=DistributionFloatUniform_3 Archetype=DistributionFloatUniform\'Engine.Default__AmbientSoundSimple:SoundNodeAmbient0.DistributionLPFMaxRadius\'\n";
            tmp +="               bIsDirty=False\n";
            tmp +="               Name=\"DistributionFloatUniform_3\"\n";
            tmp +="               ObjectArchetype=DistributionFloatUniform\'Engine.Default__AmbientSoundSimple:SoundNodeAmbient0.DistributionLPFMaxRadius\'\n";
            tmp +="            End Object\n";
            tmp +="            Begin Object Class=DistributionFloatUniform Name=DistributionVolume ObjName=DistributionFloatUniform_4 Archetype=DistributionFloatUniform\'Engine.Default__AmbientSoundSimple:SoundNodeAmbient0.DistributionVolume\'\n";
            tmp +="               Min="+soundvolume+"\n";
            tmp +="               Max="+soundvolume+"\n";
            tmp +="               bIsDirty=False\n";
            tmp +="               Name=\"DistributionFloatUniform_4\"\n";
            tmp +="               ObjectArchetype=DistributionFloatUniform\'Engine.Default__AmbientSoundSimple:SoundNodeAmbient0.DistributionVolume\'\n";
            tmp +="            End Object\n";
            tmp +="            Begin Object Class=DistributionFloatUniform Name=DistributionPitch ObjName=DistributionFloatUniform_5 Archetype=DistributionFloatUniform\'Engine.Default__AmbientSoundSimple:SoundNodeAmbient0.DistributionPitch\'\n";
            tmp +="               Min="+soundpitch+"\n";
            tmp +="               Max="+soundpitch+"\n";
            tmp +="               bIsDirty=False\n";
            tmp +="               Name=\"DistributionFloatUniform_5\"\n";
            tmp +="               ObjectArchetype=DistributionFloatUniform\'Engine.Default__AmbientSoundSimple:SoundNodeAmbient0.DistributionPitch\'\n";
            tmp +="            End Object\n";
            tmp +="            MinRadius=(Distribution=DistributionFloatUniform\'DistributionFloatUniform_0\',LookupTable=(444.000000,444.000000,444.000000,444.000000,444.000000,444.000000))\n";
            tmp +="            MaxRadius=(Distribution=DistributionFloatUniform\'DistributionFloatUniform_1\',LookupTable=("+soundradius+","+soundradius+","+soundradius+","+soundradius+","+soundradius+","+soundradius+"))\n";
            tmp +="            LPFMinRadius=(Distribution=DistributionFloatUniform\'DistributionFloatUniform_2\')\n";
            tmp +="            LPFMaxRadius=(Distribution=DistributionFloatUniform\'DistributionFloatUniform_3\')\n";
            tmp +="            Wave=SoundNodeWave\'"+pacname+"."+sndname+"\'\n";
            tmp +="            VolumeModulation=(Distribution=DistributionFloatUniform\'DistributionFloatUniform_4\',LookupTable=("+soundvolume+","+soundvolume+","+soundvolume+","+soundvolume+","+soundvolume+","+soundvolume+"))\n";
            tmp +="            PitchModulation=(Distribution=DistributionFloatUniform\'DistributionFloatUniform_5\',LookupTable=("+soundpitch+","+soundpitch+","+soundpitch+","+soundpitch+","+soundpitch+","+soundpitch+"))\n";
            tmp +="            Name=\"SoundNodeAmbient_0\"\n";
            tmp +="            ObjectArchetype=SoundNodeAmbient\'Engine.Default__AmbientSoundSimple:SoundNodeAmbient0\'\n";
            tmp +="         End Object\n";
            tmp +="         Begin Object Class=DrawSoundRadiusComponent Name=DrawSoundRadius0 ObjName=DrawSoundRadiusComponent_0 Archetype=DrawSoundRadiusComponent\'Engine.Default__AmbientSoundSimple:DrawSoundRadius0\'\n";
            tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
            tmp +="            Name=\"DrawSoundRadiusComponent_0\"\n";
            tmp +="            ObjectArchetype=DrawSoundRadiusComponent\'Engine.Default__AmbientSoundSimple:DrawSoundRadius0\'\n";
            tmp +="         End Object\n";
            tmp +="         Begin Object Class=AudioComponent Name=AudioComponent0 ObjName=AudioComponent_6 Archetype=AudioComponent\'Engine.Default__AmbientSoundSimple:AudioComponent0\'\n";
            tmp +="            SoundCue=SoundCue\'SoundCue_1\'\n";
            tmp +="            PreviewSoundRadius=DrawSoundRadiusComponent\'DrawSoundRadiusComponent_0\'\n";
            tmp +="            Name=\"AudioComponent_6\"\n";
            tmp +="            ObjectArchetype=AudioComponent\'Engine.Default__AmbientSoundSimple:AudioComponent0\'\n";
            tmp +="         End Object\n";
            tmp +="         Begin Object Class=SpriteComponent Name=Sprite ObjName=SpriteComponent_11 Archetype=SpriteComponent\'Engine.Default__AmbientSoundSimple:Sprite\'\n";
            tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
            tmp +="            Name=\"SpriteComponent_11\"\n";
            tmp +="            ObjectArchetype=SpriteComponent\'Engine.Default__AmbientSoundSimple:Sprite\'\n";
            tmp +="         End Object\n";
            tmp +="         AmbientProperties=SoundNodeAmbient\'SoundNodeAmbient_0\'\n";
            tmp +="         SoundCueInstance=SoundCue\'SoundCue_1\'\n";
            tmp +="         SoundNodeInstance=SoundNodeAmbient\'SoundNodeAmbient_0\'\n";
            tmp +="         bIsPlaying=True\n";
            tmp +="         AudioComponent=AudioComponent\'AudioComponent_6\'\n";
            tmp +="         Components(0)=SpriteComponent\'SpriteComponent_11\'\n";
            tmp +="         Components(1)=AudioComponent\'AudioComponent_6\'\n";
            tmp +="         Components(2)=DrawSoundRadiusComponent\'DrawSoundRadiusComponent_0\'\n";
            tmp +="         Tag=\"AmbientSoundSimple\"\n";
            tmp += getOtherdata();
            tmp +="         Name=\"AmbientSoundSimple_1\"\n";
            tmp +="         ObjectArchetype=AmbientSoundSimple\'Engine.Default__AmbientSoundSimple\'\n";
            tmp +="      End Actor\n";
         return tmp;
     }
}
