/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut3;

import ut3converter2.UTGames;
import ut3converter2.UTObject;
import ut3converter2.convert.Level.UTActor;
import ut3converter2.convert.MapConverter;

/**
 * Base class of Movers.
 * @author Hyperion
 */
public class InterpActor extends UTActor{

    String otherdata="";
    String defaultsm="HU_Floor2.SM.Mesh.S_HU_Floor_SM_WalkwaySetA_256";
    String smname=null;
    String savpack="UT3Map";
    MapConverter mc;
    UTObject moversmuto;
    int outputgame=0;

    public InterpActor(MapConverter mc) {
        this.savpack = mc.getDefaultpackage();
        this.mc = mc;
        this.defaultsm = MapConverter.getDefaultsm();
    }

    public void AnalyseT3DData(String line)
    {
        parseOtherData(line);
        //    StaticMesh=StaticMesh'AS-7Co-TheEgyptianPyramid-Final.Movers.door1'
        if(line.contains("StaticMesh=StaticMesh"))
        {
            moversmuto = new UTObject(line.split("\\'")[1]);
        }
    }



    public String toString()
    {
        String tmp="";
        tmp +="      Begin Actor Class=InterpActor Name=InterpActor_0 Archetype=InterpActor\'Engine.Default__InterpActor\'\n";
        tmp +="         Begin Object Class=StaticMeshComponent Name=StaticMeshComponent0 ObjName=StaticMeshComponent_0 Archetype=StaticMeshComponent\'Engine.Default__InterpActor:StaticMeshComponent0\'\n";
        //When no staticmesh set (mainly when using UT99 to UT3 conversion)
        if(moversmuto==null){
                    tmp +="            StaticMesh=StaticMesh\'"+defaultsm+"\'\n";

        }
        else{
            if(mc.getInput_utgame()==UTGames.U2){
                tmp +="            StaticMesh=StaticMesh\'"+savpack+"."+moversmuto.getName()+"\'\n";
            } else if(mc.getInput_utgame()==UTGames.UT2004){
                tmp +="            StaticMesh=StaticMesh\'"+savpack+"."+moversmuto.getGroupAndName2()+"\'\n";
            }
        }

        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            BlockRigidBody=True\n";
        tmp +="            Name=\"StaticMeshComponent_0\"\n";
        tmp +="            ObjectArchetype=StaticMeshComponent\'Engine.Default__InterpActor:StaticMeshComponent0\'\n";
        tmp +="         End Object\n";
        tmp +="         Begin Object Class=DynamicLightEnvironmentComponent Name=MyLightEnvironment ObjName=DynamicLightEnvironmentComponent_0 Archetype=DynamicLightEnvironmentComponent\'Engine.Default__InterpActor:MyLightEnvironment\'\n";
        tmp +="            Name=\"DynamicLightEnvironmentComponent_0\"\n";
        tmp +="            ObjectArchetype=DynamicLightEnvironmentComponent\'Engine.Default__InterpActor:MyLightEnvironment\'\n";
        tmp +="         End Object\n";
        tmp +="         StaticMeshComponent=StaticMeshComponent\'StaticMeshComponent_0\'\n";
        tmp +="         Components(1)=StaticMeshComponent\'StaticMeshComponent_0\'\n";
        tmp +="         bCollideActors=True\n";
        tmp +="         bBlockActors=True\n";
        tmp +="         bPathColliding=False\n";
        tmp += getOtherdata();
        tmp +="         CollisionComponent=StaticMeshComponent\'StaticMeshComponent_0\'\n";
        tmp +="         ObjectArchetype=InterpActor\'Engine.Default__InterpActor\'\n";
        tmp +="      End Actor\n";
        return tmp;
    }

}
