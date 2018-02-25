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
public class UTOnsPowerCore extends UTActor{

    String otherdata="";

    public void AnalyseT3DData(String line)
    {
        parseOtherData(line);
    }

    public String toString()
    {
        String tmp="";
        tmp +="     Begin Actor Class=UTOnslaughtPowerCore_Content Name=UTOnslaughtPowerCore_Content_0 Archetype=UTOnslaughtPowerCore_Content\'UTGameContent.Default__UTOnslaughtPowerCore_Content\'\n";
        tmp +="         Begin Object Class=SkeletalMeshComponent Name=CoreBaseMesh ObjName=SkeletalMeshComponent_12 Archetype=SkeletalMeshComponent\'UTGameContent.Default__UTOnslaughtPowerCore_Content:CoreBaseMesh\'\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            Name=\"SkeletalMeshComponent_12\"\n";
        tmp +="            ObjectArchetype=SkeletalMeshComponent\'UTGameContent.Default__UTOnslaughtPowerCore_Content:CoreBaseMesh\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=DynamicLightEnvironmentComponent Name=PowerCoreLightEnvironment ObjName=DynamicLightEnvironmentComponent_103 Archetype=DynamicLightEnvironmentComponent\'UTGameContent.Default__UTOnslaughtPowerCore_Content:PowerCoreLightEnvironment\'\n";
        tmp +="            Name=\"DynamicLightEnvironmentComponent_103\"\n";
        tmp +="            ObjectArchetype=DynamicLightEnvironmentComponent\'UTGameContent.Default__UTOnslaughtPowerCore_Content:PowerCoreLightEnvironment\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=UTParticleSystemComponent Name=ParticleComponent3 ObjName=UTParticleSystemComponent_6 Archetype=UTParticleSystemComponent\'UTGameContent.Default__UTOnslaughtPowerCore_Content:ParticleComponent3\'\n";
        tmp +="            bJustAttached=True\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            Name=\"UTParticleSystemComponent_6\"\n";
        tmp +="            ObjectArchetype=UTParticleSystemComponent\'UTGameContent.Default__UTOnslaughtPowerCore_Content:ParticleComponent3\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=StaticMeshComponent Name=StaticMeshComponent_199 ObjName=StaticMeshComponent_199 Archetype=StaticMeshComponent\'Engine.Default__StaticMeshComponent\'\n";
        tmp +="            Begin Object Class=MaterialInstanceConstant Name=MaterialInstanceConstant_39 ObjName=MaterialInstanceConstant_39 Archetype=MaterialInstanceConstant\'Engine.Default__MaterialInstanceConstant\'\n";
        tmp +="               Parent=Material\'GP_Onslaught.Materials.M_GP_Ons_GlowBlob\'\n";
        tmp +="               Name=\"MaterialInstanceConstant_39\"\n";
        tmp +="               ObjectArchetype=MaterialInstanceConstant\'Engine.Default__MaterialInstanceConstant\'\n";
        tmp +="            End Object\n";
        tmp +="            StaticMesh=StaticMesh\'GP_Onslaught.Mesh.S_GP_Ons_Core_Blob\'\n";
        tmp +="            Materials(0)=MaterialInstanceConstant\'MaterialInstanceConstant_39\'\n";
        tmp +="            bAcceptsDecals=False\n";
        tmp +="            CastShadow=False\n";
        tmp +="            bAcceptsLights=False\n";
        tmp +="            CollideActors=False\n";
        tmp +="            BlockActors=False\n";
        tmp +="            BlockZeroExtent=False\n";
        tmp +="            BlockNonZeroExtent=False\n";
        tmp +="            BlockRigidBody=False\n";
        tmp +="            Name=\"StaticMeshComponent_199\"\n";
        tmp +="            ObjectArchetype=StaticMeshComponent\'Engine.Default__StaticMeshComponent\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=StaticMeshComponent Name=StaticMeshComponent_200 ObjName=StaticMeshComponent_200 Archetype=StaticMeshComponent\'Engine.Default__StaticMeshComponent\'\n";
        tmp +="            Begin Object Class=MaterialInstanceConstant Name=MaterialInstanceConstant_40 ObjName=MaterialInstanceConstant_40 Archetype=MaterialInstanceConstant\'Engine.Default__MaterialInstanceConstant\'\n";
        tmp +="               Parent=Material\'GP_Onslaught.Materials.M_GP_Ons_GlowBlob\'\n";
        tmp +="               Name=\"MaterialInstanceConstant_40\"\n";
        tmp +="               ObjectArchetype=MaterialInstanceConstant\'Engine.Default__MaterialInstanceConstant\'\n";
        tmp +="            End Object\n";
        tmp +="            StaticMesh=StaticMesh\'GP_Onslaught.Mesh.S_GP_Ons_Core_Blob\'\n";
        tmp +="            Materials(0)=MaterialInstanceConstant\'MaterialInstanceConstant_40\'\n";
        tmp +="            bAcceptsDecals=False\n";
        tmp +="            CastShadow=False\n";
        tmp +="            bAcceptsLights=False\n";
        tmp +="            CollideActors=False\n";
        tmp +="            BlockActors=False\n";
        tmp +="            BlockZeroExtent=False\n";
        tmp +="            BlockNonZeroExtent=False\n";
        tmp +="            BlockRigidBody=False\n";
        tmp +="            Name=\"StaticMeshComponent_200\"\n";
        tmp +="            ObjectArchetype=StaticMeshComponent\'Engine.Default__StaticMeshComponent\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=PointLightComponent Name=LightComponent0 ObjName=PointLightComponent_5 Archetype=PointLightComponent\'UTGameContent.Default__UTOnslaughtPowerCore_Content:LightComponent0\'\n";
        tmp +="            CachedParentToWorld=(XPlane=(W=0.000000,X=0.600000,Y=0.000000,Z=0.000000),YPlane=(W=0.000000,X=0.000000,Y=0.600000,Z=-0.000000),ZPlane=(W=0.000000,X=-0.000000,Y=0.000000,Z=0.600000),WPlane=(W=1.000000,X=7584.000000,Y=5664.000000,Z=576.000000))\n";
        tmp +="            LightGuid=(A=-1259561208,B=1123172599,C=-967665782,D=-1536746344)\n";
        tmp +="            LightmapGuid=(A=-1590186287,B=1133966168,C=-1811447423,D=-212667001)\n";
        tmp +="            Name=\"PointLightComponent_5\"\n";
        tmp +="            ObjectArchetype=PointLightComponent\'UTGameContent.Default__UTOnslaughtPowerCore_Content:LightComponent0\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=AudioComponent Name=AmbientComponent ObjName=AudioComponent_8 Archetype=AudioComponent\'UTGameContent.Default__UTOnslaughtPowerCore_Content:AmbientComponent\'\n";
        tmp +="            Name=\"AudioComponent_8\"\n";
        tmp +="            ObjectArchetype=AudioComponent\'UTGameContent.Default__UTOnslaughtPowerCore_Content:AmbientComponent\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=StaticMeshComponent Name=StaticMeshComponent9 ObjName=StaticMeshComponent_198 Archetype=StaticMeshComponent\'UTGameContent.Default__UTOnslaughtPowerCore_Content:StaticMeshComponent9\'\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            TickGroup=TG_DuringAsyncWork\n";
        tmp +="            Name=\"StaticMeshComponent_198\"\n";
        tmp +="            ObjectArchetype=StaticMeshComponent\'UTGameContent.Default__UTOnslaughtPowerCore_Content:StaticMeshComponent9\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=CylinderComponent Name=CollisionCylinder ObjName=CylinderComponent_4 Archetype=CylinderComponent\'UTGameContent.Default__UTOnslaughtPowerCore_Content:CollisionCylinder\'\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            Name=\"CylinderComponent_4\"\n";
        tmp +="            ObjectArchetype=CylinderComponent\'UTGameContent.Default__UTOnslaughtPowerCore_Content:CollisionCylinder\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=ArrowComponent Name=Arrow ObjName=ArrowComponent_621 Archetype=ArrowComponent\'UTGameContent.Default__UTOnslaughtPowerCore_Content:Arrow\'\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            Name=\"ArrowComponent_621\"\n";
        tmp +="            ObjectArchetype=ArrowComponent\'UTGameContent.Default__UTOnslaughtPowerCore_Content:Arrow\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=PathRenderingComponent Name=PathRenderer ObjName=PathRenderingComponent_696 Archetype=PathRenderingComponent\'UTGameContent.Default__UTOnslaughtPowerCore_Content:PathRenderer\'\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            Name=\"PathRenderingComponent_696\"\n";
        tmp +="            ObjectArchetype=PathRenderingComponent\'UTGameContent.Default__UTOnslaughtPowerCore_Content:PathRenderer\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=LinkRenderingComponent Name=LinkRenderer ObjName=LinkRenderingComponent_0 Archetype=LinkRenderingComponent\'UTGameContent.Default__UTOnslaughtPowerCore_Content:LinkRenderer\'\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            Name=\"LinkRenderingComponent_0\"\n";
        tmp +="            ObjectArchetype=LinkRenderingComponent\'UTGameContent.Default__UTOnslaughtPowerCore_Content:LinkRenderer\'\n";
        tmp +="         End Object\n";
        tmp +="         BaseMesh=SkeletalMeshComponent\'SkeletalMeshComponent_12\'\n";
        tmp +="         InnerCoreEffect=UTParticleSystemComponent\'UTParticleSystemComponent_6\'\n";
        tmp +="         EnergyEffectLight=PointLightComponent\'PointLightComponent_5\'\n";
        tmp +="         PanelMesh=SkeletalMeshComponent\'SkeletalMeshComponent_12\'\n";
        tmp +="         NodeBeamEffect=StaticMeshComponent\'StaticMeshComponent_198\'\n";
        tmp +="         bPathsChanged=True\n";
        tmp +="         CylinderComponent=CylinderComponent\'CylinderComponent_4\'\n";
        tmp +="         Components(0)=ArrowComponent\'ArrowComponent_621\'\n";
        tmp +="         Components(1)=CylinderComponent\'CylinderComponent_4\'\n";
        tmp +="         Components(2)=PathRenderingComponent\'PathRenderingComponent_696\'\n";
        tmp +="         Components(3)=LinkRenderingComponent\'LinkRenderingComponent_0\'\n";
        tmp +="         Components(6)=SkeletalMeshComponent\'SkeletalMeshComponent_12\'\n";
        tmp +="         Components(7)=UTParticleSystemComponent\'UTParticleSystemComponent_6\'\n";
        tmp +="         Components(8)=PointLightComponent\'PointLightComponent_5\'\n";
        tmp +="         Components(9)=StaticMeshComponent\'StaticMeshComponent_198\'\n";
        tmp += getOtherdata();
        tmp +="         CollisionComponent=SkeletalMeshComponent\'SkeletalMeshComponent_12\'\n";
        tmp +="         Name=\"UTOnslaughtPowerCore_Content_0\"\n";
        tmp +="         ObjectArchetype=UTOnslaughtPowerCore_Content\'UTGameContent.Default__UTOnslaughtPowerCore_Content\'\n";
        tmp +="      End Actor\n";
        return tmp;
    }

}
