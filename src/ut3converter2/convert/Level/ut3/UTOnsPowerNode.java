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
public class UTOnsPowerNode extends UTActor{

    String otherdata="";

    public void AnalyseT3DData(String line)
    {
        parseOtherData(line);
    }

    public String toString()
    {
        String tmp="";
        tmp +="	Begin Actor Class=UTOnslaughtPowernode_Content Name=UTOnslaughtPowernode_Content_0 Archetype=UTOnslaughtPowernode_Content\'UTGameContent.Default__UTOnslaughtPowernode_Content\'\n";
        tmp +="         Begin Object Class=SkeletalMeshComponent Name=SkeletalMeshComponent1 ObjName=SkeletalMeshComponent_50 Archetype=SkeletalMeshComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:SkeletalMeshComponent1\'\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            Name=\"SkeletalMeshComponent_50\"\n";
        tmp +="            ObjectArchetype=SkeletalMeshComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:SkeletalMeshComponent1\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=DynamicLightEnvironmentComponent Name=PowerNodeLightEnvironment ObjName=DynamicLightEnvironmentComponent_290 Archetype=DynamicLightEnvironmentComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:PowerNodeLightEnvironment\'\n";
        tmp +="            Name=\"DynamicLightEnvironmentComponent_290\"\n";
        tmp +="            ObjectArchetype=DynamicLightEnvironmentComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:PowerNodeLightEnvironment\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=StaticMeshComponent Name=StaticMeshComponent0 ObjName=StaticMeshComponent_3535 Archetype=StaticMeshComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:StaticMeshComponent0\'\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            Name=\"StaticMeshComponent_3535\"\n";
        tmp +="            ObjectArchetype=StaticMeshComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:StaticMeshComponent0\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=StaticMeshComponent Name=StaticMeshSpinner ObjName=StaticMeshComponent_3536 Archetype=StaticMeshComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:StaticMeshSpinner\'\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            TickGroup=TG_DuringAsyncWork\n";
        tmp +="            Name=\"StaticMeshComponent_3536\"\n";
        tmp +="            ObjectArchetype=StaticMeshComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:StaticMeshSpinner\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=CylinderComponent Name=CollisionCylinder2 ObjName=CylinderComponent_1682 Archetype=CylinderComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:CollisionCylinder2\'\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            Name=\"CylinderComponent_1682\"\n";
        tmp +="           ObjectArchetype=CylinderComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:CollisionCylinder2\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=UTParticleSystemComponent Name=AmbientEffectComponent ObjName=UTParticleSystemComponent_108 Archetype=UTParticleSystemComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:AmbientEffectComponent\'\n";
        tmp +="            bJustAttached=True\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            Name=\"UTParticleSystemComponent_108\"\n";
        tmp +="            ObjectArchetype=UTParticleSystemComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:AmbientEffectComponent\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=ParticleSystemComponent Name=CaptureSystem ObjName=ParticleSystemComponent_27 Archetype=ParticleSystemComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:CaptureSystem\'\n";
        tmp +="            bJustAttached=True\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            Name=\"ParticleSystemComponent_27\"\n";
        tmp +="            ObjectArchetype=ParticleSystemComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:CaptureSystem\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=ParticleSystemComponent Name=InvulnerableSystem ObjName=ParticleSystemComponent_28 Archetype=ParticleSystemComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:InvulnerableSystem\'\n";
        tmp +="            bJustAttached=True\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            Name=\"ParticleSystemComponent_28\"\n";
        tmp +="            ObjectArchetype=ParticleSystemComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:InvulnerableSystem\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=AudioComponent Name=OrbNearbySoundComponent ObjName=AudioComponent_68 Archetype=AudioComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:OrbNearbySoundComponent\'\n";
        tmp +="            Name=\"AudioComponent_68\"\n";
        tmp +="            ObjectArchetype=AudioComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:OrbNearbySoundComponent\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=StaticMeshComponent Name=NecrisCapturePipesLargeComp ObjName=StaticMeshComponent_3537 Archetype=StaticMeshComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:NecrisCapturePipesLargeComp\'\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            TickGroup=TG_DuringAsyncWork\n";
        tmp +="            Name=\"StaticMeshComponent_3537\"\n";
        tmp +="            ObjectArchetype=StaticMeshComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:NecrisCapturePipesLargeComp\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=StaticMeshComponent Name=NecrisCapturePipesSmallComp ObjName=StaticMeshComponent_3538 Archetype=StaticMeshComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:NecrisCapturePipesSmallComp\'\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            TickGroup=TG_DuringAsyncWork\n";
        tmp +="            Name=\"StaticMeshComponent_3538\"\n";
        tmp +="            ObjectArchetype=StaticMeshComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:NecrisCapturePipesSmallComp\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=UTParticleSystemComponent Name=NecrisCaptureComp ObjName=UTParticleSystemComponent_109 Archetype=UTParticleSystemComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:NecrisCaptureComp\'\n";
        tmp +="            bJustAttached=True\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            Name=\"UTParticleSystemComponent_109\"\n";
        tmp +="            ObjectArchetype=UTParticleSystemComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:NecrisCaptureComp\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=UTParticleSystemComponent Name=NecrisGoodPuddleComp ObjName=UTParticleSystemComponent_110 Archetype=UTParticleSystemComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:NecrisGoodPuddleComp\'\n";
        tmp +="            bJustAttached=True\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            Name=\"UTParticleSystemComponent_110\"\n";
        tmp +="            ObjectArchetype=UTParticleSystemComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:NecrisGoodPuddleComp\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=AudioComponent Name=AmbientComponent ObjName=AudioComponent_69 Archetype=AudioComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:AmbientComponent\'\n";
        tmp +="            Name=\"AudioComponent_69\"\n";
        tmp +="            ObjectArchetype=AudioComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:AmbientComponent\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=StaticMeshComponent Name=StaticMeshComponent9 ObjName=StaticMeshComponent_3539 Archetype=StaticMeshComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:StaticMeshComponent9\'\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            TickGroup=TG_DuringAsyncWork\n";
        tmp +="            Name=\"StaticMeshComponent_3539\"\n";
        tmp +="            ObjectArchetype=StaticMeshComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:StaticMeshComponent9\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=ParticleSystemComponent Name=ParticleSystemComponent1 ObjName=ParticleSystemComponent_29 Archetype=ParticleSystemComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:ParticleSystemComponent1\'\n";
        tmp +="            bJustAttached=True\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            Name=\"ParticleSystemComponent_29\"\n";
        tmp +="            ObjectArchetype=ParticleSystemComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:ParticleSystemComponent1\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=CylinderComponent Name=CollisionCylinder ObjName=CylinderComponent_1683 Archetype=CylinderComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:CollisionCylinder\'\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            Name=\"CylinderComponent_1683\"\n";
        tmp +="            ObjectArchetype=CylinderComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:CollisionCylinder\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=ArrowComponent Name=Arrow ObjName=ArrowComponent_3154 Archetype=ArrowComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:Arrow\'\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            Name=\"ArrowComponent_3154\"\n";
        tmp +="            ObjectArchetype=ArrowComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:Arrow\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=PathRenderingComponent Name=PathRenderer ObjName=PathRenderingComponent_3267 Archetype=PathRenderingComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:PathRenderer\'\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            Name=\"PathRenderingComponent_3267\"\n";
        tmp +="            ObjectArchetype=PathRenderingComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:PathRenderer\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=LinkRenderingComponent Name=LinkRenderer ObjName=LinkRenderingComponent_21 Archetype=LinkRenderingComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:LinkRenderer\'\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            Name=\"LinkRenderingComponent_21\"\n";
        tmp +="            ObjectArchetype=LinkRenderingComponent\'UTGameContent.Default__UTOnslaughtPowernode_Content:LinkRenderer\'\n";
        tmp +="         End Object\n";
        tmp +="         NodeBase=StaticMeshComponent\'StaticMeshComponent_3535\'\n";
        tmp +="         NodeBaseSpinner=StaticMeshComponent\'StaticMeshComponent_3536\'\n";
        tmp +="         EnergySphereCollision=CylinderComponent\'CylinderComponent_1682\'\n";
        tmp +="         OrbCaptureComponent=ParticleSystemComponent\'ParticleSystemComponent_27\'\n";
        tmp +="         InvulnerableToOrbEffect=ParticleSystemComponent\'ParticleSystemComponent_28\'\n";
        tmp +="         NecrisCapturePipesLarge=StaticMeshComponent\'StaticMeshComponent_3537\'\n";
        tmp +="         NecrisCapturePipesSmall=StaticMeshComponent\'StaticMeshComponent_3538\'\n";
        tmp +="         PSC_NecrisCapture=UTParticleSystemComponent\'UTParticleSystemComponent_109\'\n";
        tmp +="         PSC_NecrisGooPuddle=UTParticleSystemComponent\'UTParticleSystemComponent_110\'\n";
        tmp +="         PanelMesh=SkeletalMeshComponent\'SkeletalMeshComponent_50\'\n";
        tmp +="         NodeBeamEffect=StaticMeshComponent\'StaticMeshComponent_3539\'\n";
        tmp +="         bPathsChanged=True\n";
        tmp +="         CylinderComponent=CylinderComponent\'CylinderComponent_1683\'\n";
        tmp +="         Components(0)=ArrowComponent\'ArrowComponent_3154\'\n";
        tmp +="         Components(1)=CylinderComponent\'CylinderComponent_1683\'\n";
        tmp +="         Components(2)=PathRenderingComponent\'PathRenderingComponent_3267\'\n";
        tmp +="         Components(3)=LinkRenderingComponent\'LinkRenderingComponent_21\'\n";
        tmp +="         Components(6)=StaticMeshComponent\'StaticMeshComponent_3535\'\n";
        tmp +="         Components(7)=StaticMeshComponent\'StaticMeshComponent_3536\'\n";
        tmp +="         Components(8)=SkeletalMeshComponent\'SkeletalMeshComponent_50\'\n";
        tmp +="         Components(9)=CylinderComponent\'CylinderComponent_1682\'\n";
        tmp +="         Components(10)=UTParticleSystemComponent\'UTParticleSystemComponent_108\'\n";
        tmp +="         Components(11)=ParticleSystemComponent\'ParticleSystemComponent_29\'\n";
        tmp +="         Components(12)=StaticMeshComponent\'StaticMeshComponent_3539\'\n";
        tmp +="         Components(14)=ParticleSystemComponent\'ParticleSystemComponent_27\'\n";
        tmp +="         Components(15)=ParticleSystemComponent\'ParticleSystemComponent_28\'\n";
        tmp +="         Components(16)=UTParticleSystemComponent\'UTParticleSystemComponent_110\'\n";
        tmp +="         Components(17)=UTParticleSystemComponent\'UTParticleSystemComponent_109\'\n";
        tmp +="         Components(18)=StaticMeshComponent\'StaticMeshComponent_3537\'\n";
        tmp +="         Components(19)=StaticMeshComponent\'StaticMeshComponent_3538\'\n";
        tmp +="         CreationTime=15503.409180\n";
        tmp += getOtherdata();
        tmp +="         CollisionComponent=CylinderComponent\'CylinderComponent_1683\'\n";
        tmp +="         Name=\"UTOnslaughtPowernode_Content_0\"\n";
        tmp +="         ObjectArchetype=UTOnslaughtPowernode_Content\'UTGameContent.Default__UTOnslaughtPowernode_Content\'\n";
        tmp +="      End Actor\n";
        return tmp;
    }

}
