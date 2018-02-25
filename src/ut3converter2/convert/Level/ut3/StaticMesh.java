/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ut3converter2.convert.Level.ut3;

import java.util.ArrayList;
import ut3converter2.UTGames;
import ut3converter2.UTObject;
import ut3converter2.convert.Level.UTActor;
import ut3converter2.convert.MapConverter;
import ut3converter2.convert.Texture.TextureReplacer;

/**
 *
 * @author Hyperion
 */
public class StaticMesh extends UTActor {

    ArrayList almaterials = new ArrayList();
    boolean bUseTexReplacement = false;
    String numsm = "0";
    String tmp = "";
    float culldistance = 0F;
    String savpack = "";
    String smname = "";
    TextureReplacer tr;
    int inputgame;
    MapConverter mc;

    public StaticMesh(MapConverter mc, TextureReplacer tr) {
        this.savpack = mc.getDefaultpackage();
        this.tr = tr;
        this.inputgame = mc.getInput_utgame();
        this.mc = mc;
    }

    /**
     * Tries to find if Skin texture has a replacement.
     * Textures have replacement if there are special textures such as shaders,texpanner,...
     * @param line TexturePath
     */
    private void addMaterial(String line) {
        UTObject uto = new UTObject(line);
        if(tr.hasTextureReplacement(uto.getGroupAndName().toLowerCase()))
        {
            UTObject uto2 = new UTObject(tr.getTextureReplacement(uto.getGroupAndName().toLowerCase()));
            almaterials.add(uto2.getGroupAndName2()+ "_Mat");
        }
        else
        {
            almaterials.add(uto.getGroupAndName2()+ "_Mat");
        }
    }

    public void analyzeT3Ddata(String line) {

        //	ColLocation=(X=-190
        if(mc.getInput_utgame()==UTGames.U2){ //Adds ColLocation values to location
            //parseOtherData(line,true);
            parseOtherData(line);
        } else {
            parseOtherData(line);
        }


        if (line.contains("StaticMesh=StaticMesh")) {
            getSMName(line);
        } else if (line.contains(" CullDistance=")) {
            culldistance = Float.valueOf(line.split("\\=")[1]);
        } //Skins(0)=FinalBlend'myLevel.jwat1bl'
        else if (line.contains("Skins(")) {
            addMaterial(line.split("\\'")[1]);
        } //Begin Actor Class=StaticMeshActor Name=StaticMeshActor865
        else if (line.contains("Name=StaticMeshActor")) {
            this.numsm = line.split("Name=StaticMeshActor")[1];
        }
    }

    private void getSMName(String line) {
        String tmp;
        String tmp2[];
        //    StaticMesh=StaticMesh'BarrenHardware.Miscellaneous.MiscArchFront01BA'
        //->    StaticMesh=StaticMesh'UT3Map.Miscellaneous_MiscArchFront01BA'

        //    StaticMesh=StaticMesh'Rahnem_Glacier.Base.closed_box3'
        //        StaticMesh=StaticMesh'UT3Map.Base_closed_box3'
        tmp = line.split("\\'")[1]; //Rahnem_Glacier.Base.closed_box3

        tmp2 = tmp.split("\\.");
        if (tmp2.length == 2) {
            smname = tmp2[1];
        } else if (tmp2.length == 3) {
            if(inputgame==UTGames.U2)
            {
                smname = tmp2[2];
            }
            else if(inputgame==UTGames.UT2004)
            {
                smname = tmp2[1] + "_" + tmp2[2];
            }
        }
    }

    public String toString() {

        tmp += "      Begin Actor Class=StaticMeshActor Name=StaticMeshActor_" + numsm + " Archetype=StaticMeshActor\'Engine.Default__StaticMeshActor\'\n";
        tmp += "         Begin Object Class=StaticMeshComponent Name=StaticMeshComponent0 ObjName=StaticMeshComponent_14 Archetype=StaticMeshComponent\'Engine.Default__StaticMeshActor:StaticMeshComponent0\'\n";
        tmp += "            StaticMesh=StaticMesh\'" + savpack + "." + smname + "\'\n";
        //Materials(0)=Material'HU_Deco3.SM.Materials.M_HU_Deco_SM_FireHydrant01'-Original
        int j = almaterials.size();
        for (int i = 0; i < almaterials.size(); i++) {
            tmp += "            Materials(" + i + ")=Material\'" + savpack + "." + almaterials.get(j - 1).toString() + "\'\n";
            j--;
        }
        if (culldistance != 0F) {
            tmp += "         CullDistance=" + culldistance + "\n";
            tmp += "         CachedCullDistance=" + culldistance + "\n";
        }
        tmp += "            LightingChannels=(bInitialized=True,Static=True)\n";
        tmp += "            Name=\"StaticMeshComponent_14\"\n";
        tmp += "            ObjectArchetype=StaticMeshComponent\'Engine.Default__StaticMeshActor:StaticMeshComponent0\'\n";
        tmp += "         End Object\n";
        tmp += "         StaticMeshComponent=StaticMeshComponent\'StaticMeshComponent_14\'\n";
        tmp += "         Components(0)=StaticMeshComponent\'StaticMeshComponent_14\'\n";
        tmp += "         CollisionComponent=StaticMeshComponent\'StaticMeshComponent_14\'\n";
        tmp += "         ObjectArchetype=StaticMeshActor\'Engine.Default__StaticMeshActor\'\n";
        tmp += getOtherdata();
        tmp += "      End Actor\n";

        return tmp;
    }
}
