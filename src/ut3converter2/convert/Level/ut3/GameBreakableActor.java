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
public class GameBreakableActor extends UTActor{

    String otherdata="";
    public GameBreakableActor() {
    }

    public void AnalyseT3DData(String line)
    {
        parseOtherData(line);
    }

    public String toString()
    {
        String tmp="";
        tmp +="      Begin Actor Class=GameBreakableActor Name=GameBreakableActor_0 Archetype=GameBreakableActor\'GameFramework.Default__GameBreakableActor\'\n";
        tmp +="         Begin Object Class=StaticMeshComponent Name=StaticMeshComponent0 ObjName=StaticMeshComponent_21 Archetype=StaticMeshComponent\'GameFramework.Default__GameBreakableActor:StaticMeshComponent0\'\n";
        tmp +="            StaticMesh=StaticMesh\'HU_Deco.SM.Mesh.S_HU_Deco_SM_Barrel01\'\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            Name=\"StaticMeshComponent_21\"\n";
        tmp +="            ObjectArchetype=StaticMeshComponent\'GameFramework.Default__GameBreakableActor:StaticMeshComponent0\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=DynamicLightEnvironmentComponent Name=MyLightEnvironment ObjName=DynamicLightEnvironmentComponent_10 Archetype=DynamicLightEnvironmentComponent\'GameFramework.Default__GameBreakableActor:MyLightEnvironment\'\n";
        tmp +="            Name=\"DynamicLightEnvironmentComponent_10\"\n";
        tmp +="            ObjectArchetype=DynamicLightEnvironmentComponent\'GameFramework.Default__GameBreakableActor:MyLightEnvironment\'\n";
        tmp +="         End Object\n";
        tmp +="         StaticMeshComponent=StaticMeshComponent\'StaticMeshComponent_21\'\n";
        tmp +="         ReplicatedMesh=StaticMesh\'HU_Deco.SM.Mesh.S_HU_Deco_SM_Barrel01\'\n";
        tmp +="         Components(1)=StaticMeshComponent\'StaticMeshComponent_21\'\n";
        tmp += getOtherdata();
        tmp +="         CollisionComponent=StaticMeshComponent\'StaticMeshComponent_21\'\n";
        tmp +="         ObjectArchetype=GameBreakableActor\'GameFramework.Default__GameBreakableActor\'\n";
        tmp +="      End Actor\n";
        return tmp;
    }

}
