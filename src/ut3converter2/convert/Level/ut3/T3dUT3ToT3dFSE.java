/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Hyperion
 */
public class T3dUT3ToT3dFSE {
    
    
    public static final String[] OBJECT_DENIED = new String[]{"ProscribedReachSpec", "ReachSpec", "Sequence", "SeqAct_Toggle", "SeqEvent_Touch", "SequenceFrame", "SequenceFrameWrapped", "SeqVar_Object"};
    public static final ArrayList<String> allowedClasses = new ArrayList<String>();
    
    public static HashSet<String> actors = new HashSet<String>();
    
    
    private static void initAllowedClasses(){
        
        allowedClasses.add("UTAmmo_LinkGun");
allowedClasses.add("WorldInfo");
allowedClasses.add("UTCTFRedFlagBase");
allowedClasses.add("UTArmorPickup_Vest");
allowedClasses.add("HeightFog");
allowedClasses.add("KActor");
allowedClasses.add("Level");
allowedClasses.add("UTVehicleFactory_HellBender");
allowedClasses.add("SkyLightComponent");
allowedClasses.add("SpriteComponent");
allowedClasses.add("PointLightComponent");
allowedClasses.add("StaticMeshActor");
allowedClasses.add("SkeletalMeshActor");
allowedClasses.add("InterpActor");
allowedClasses.add("AnimNodeSequence");
allowedClasses.add("SoundCue");
allowedClasses.add("SeqEvent_Touch");
allowedClasses.add("SkeletalMeshComponent");
//allowedClasses.add("Sequence");
allowedClasses.add("DirectionalLightComponent");
allowedClasses.add("CylinderComponent");
allowedClasses.add("AmbientSoundSimpleToggleable");
allowedClasses.add("UTJumpPad");
allowedClasses.add("SpeedTreeComponent");
allowedClasses.add("UTVehicleFactory_Scorpion");
        /*
DistributionFloatUniform
AmbientSound
UTMapInfo
HeightFogComponent
SeqAct_Toggle
UTPickupFactory_MediumHealth
RB_RadialForceActor
StaticMeshComponent
PostProcessVolume
SoundNodeAmbient
SeqEvent_LevelBeginning
SeqVar_Object
ReachSpec
UTArmorPickupLight
AudioComponent
SpeedTreeActor
AmbientSoundSimple
UTArmorPickup_ShieldBelt
ArrowComponent
UTAmmo_SniperRifle
UTParticleSystemComponent
UTDefensePoint
DynamicLightEnvironmentComponent*/
allowedClasses.add("Brush");
/*
Trigger
UTArmorPickup_Helmet
BlockingVolume
UTWeaponLockerPickupLight
UTTeamPlayerStart
UTWeaponLocker_Content
UTPickupFactory_JumpBoots
UTAmmo_FlakCannon
DrawSphereComponent
UTHealthPickupLight
UTCTFBlueFlagBase
BrushComponent
PathNode
SequenceFrameWrapped
UTAmmo_BioRifle_Content
ParticleSystemComponent
SkyLight
ProscribedReachSpec
PathRenderingComponent
Emitter
DrawSoundRadiusComponent
UTWeaponPickupFactory
UTAmmo_RocketLauncher
Level
UTPickupFactory_HealthVial
TerrainComponent
SequenceFrame
DirectionalLight
        
        */
    }
    
    private static boolean isAllowedObject(String line, int level){
        
        if(!line.startsWith("Begin Object Class=") || level > 2){
            return true;
        }
        

        //Begin Object Class=UTWeaponLockerPickupLight Name=UTWeaponLockerPickupLight_3 Ob
        String clazz = line.split(" Class=")[1].split(" Name=")[0];
        actors.add(clazz);
        
        return allowedClasses.contains(clazz);
        //System.out.println(clazz);
        /*
        for(String s : OBJECT_DENIED){
            if(s.equals(clazz)){
                return false;
            }
        }*/
        
        //return true;
    }
    
    public static void convert(Path inputPath, Path outputPath){
        
        initAllowedClasses();
        
        FileReader fr = null;
        FileWriter fw = null;
        BufferedReader bfr = null;
        BufferedWriter bwr = null;
        
        try {
            fr = new FileReader(inputPath.toFile());
            bfr = new BufferedReader(fr);
            fw = new FileWriter(outputPath.toFile());
            bwr = new BufferedWriter(fw);
            
            String line = "";
            String trimedLine = "";
            
            int level = 0;
            
            
            bwr.write("Begin Map\n");
            bwr.write("Begin Level\n");
            
            boolean isValidObject = true;
            
            /*
            Begin Object Class=Level Name=PersistentLevel ObjName=PersistentLevel Archetype=Level'Engine.Default__Level' // 1
                Begin Object Class=AmbientSound Name=AmbientSound_0 ObjName=AmbientSound_0 Archetype=AmbientSound'Engine.Default__AmbientSound' // 2
                   Begin Object Class=AudioComponent Name=AudioComponent0 ObjName=AudioComponent_130 Archetype=AudioComponent'Engine.Default__AmbientSound:AudioComponent0' //3
                      SoundCue=SoundCue'crows_02'
                      VolumeMultiplier=1.100000
                      Name="AudioComponent_130"
                      ObjectArchetype=AudioComponent'Engine.Default__AmbientSound:AudioComponent0'
                   End Object
                   Begin Objec
            */
            
            while((line = bfr.readLine()) != null){
             
                trimedLine = line.trim();
                
                
                
                if(trimedLine.startsWith("Begin Object")){
                    
                    level ++;
                    
                    if(!isValidObject){
                        continue;
                    }
                    
                    if(!isAllowedObject(trimedLine, level)){
                        isValidObject = false;
                        continue;
                    }
                    
                    if(!trimedLine.startsWith("Begin Object Class=Level")){
                        if(level == 2){
                            //bwr.write(line.replaceAll("Begin Object", "Begin Actor")+"\n");
                            bwr.write(line+"\n");
                        } else if(level == 3){
                            bwr.write(line+"\n");
                        }
                    }
                }else if(trimedLine.startsWith("End Object")){
                    
                    
                    
                    if(!isValidObject){
                        if(level == 2){
                            isValidObject = true;
                        }
                        level --;
                        continue;
                    }
                    
                    if(level == 2){
                        //bwr.write(line.split("End")[0]+"End Actor\n");
                        bwr.write(line+"\n");
                    } else if(level == 3) {
                        bwr.write(line+"\n");
                    }
                    level --;
                    
                } else {
                    if(isValidObject){
                        bwr.write(line+"\n");
                    }
                }
            }
            
            bwr.write("End Map\n");
            bwr.write("End Level\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            
            try {
                bfr.close();
                fr.close();
                bwr.close();
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        
        

    }
    
    public static void main(String args[]){
        
        File f = new File("E:\\JEUX\\UT3\\Binaries\\PersistentLevelOriginal.t3d");
        Path in = f.toPath();
        Path out = new File("E:\\JEUX\\UT3\\UTGame\\Published\\CookedPC\\CustomMaps\\atest.t3d").toPath();
        
        convert(in, out);
        
    }
}
