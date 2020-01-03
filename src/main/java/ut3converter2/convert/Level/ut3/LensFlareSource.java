/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut3;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 *
 * @author Hyperion
 */
public class LensFlareSource {


    public static String lf_sunlight="sunlight";
    public static String lf_normallight="normallight";
    static String lf_sl_pacname="FX_LensFlares.flares.LF_SunFlare_Torlan_01";
    static String lf_normal_pacname="FX_LensFlares.flares.LF_Anamorphic_Neutral_Wide_01";
    
    public static void createLFS(BufferedWriter bfr,String location,String lensflaretype) throws IOException
    {
        String tmp="";
        String pacname="";
        if(lensflaretype.equals(LensFlareSource.lf_normallight)){pacname=lf_normal_pacname;}
        else if(lensflaretype.equals(LensFlareSource.lf_sunlight)){pacname=lf_sl_pacname;}
        bfr.write("	Begin Actor Class=LensFlareSource Name=LensFlareSource_2 Archetype=LensFlareSource\'Engine.Default__LensFlareSource\'\n");
        bfr.write("         Begin Object Class=DrawLightConeComponent Name=DrawInnerCone0 ObjName=DrawLightConeComponent_4 Archetype=DrawLightConeComponent\'Engine.Default__LensFlareSource:DrawInnerCone0\'\n");
        bfr.write("            ConeRadius=0.000000\n");
        bfr.write("            ConeAngle=0.000000\n");
        bfr.write("            LightingChannels=(bInitialized=True,Dynamic=True)\n");
        bfr.write("            Name=\"DrawLightConeComponent_4\"\n");
        bfr.write("            ObjectArchetype=DrawLightConeComponent\'Engine.Default__LensFlareSource:DrawInnerCone0\'\n");
        bfr.write("         End Object\n");
        bfr.write("         Begin Object Class=DrawLightConeComponent Name=DrawOuterCone0 ObjName=DrawLightConeComponent_5 Archetype=DrawLightConeComponent\'Engine.Default__LensFlareSource:DrawOuterCone0\'\n");
        bfr.write("            ConeRadius=0.000000\n");
        bfr.write("            ConeAngle=0.000000\n");
        bfr.write("            LightingChannels=(bInitialized=True,Dynamic=True)\n");
        bfr.write("            Name=\"DrawLightConeComponent_5\"\n");
        bfr.write("            ObjectArchetype=DrawLightConeComponent\'Engine.Default__LensFlareSource:DrawOuterCone0\'\n");
        bfr.write("         End Object\n");
        bfr.write("         Begin Object Class=DrawLightRadiusComponent Name=DrawRadius0 ObjName=DrawLightRadiusComponent_211 Archetype=DrawLightRadiusComponent\'Engine.Default__LensFlareSource:DrawRadius0\'\n");
        bfr.write("            SphereRadius=0.000000\n");
        bfr.write("            LightingChannels=(bInitialized=True,Dynamic=True)\n");
        bfr.write("            Name=\"DrawLightRadiusComponent_211\"\n");
        bfr.write("            ObjectArchetype=DrawLightRadiusComponent\'Engine.Default__LensFlareSource:DrawRadius0\'\n");
        bfr.write("         End Object\n");
        bfr.write("         Begin Object Class=LensFlareComponent Name=LensFlareComponent0 ObjName=LensFlareComponent_2 Archetype=LensFlareComponent\'Engine.Default__LensFlareSource:LensFlareComponent0\'\n");
        bfr.write("            Template=LensFlare\'"+pacname+"\'\n");
        bfr.write("            PreviewInnerCone=DrawLightConeComponent\'DrawLightConeComponent_4\'\n");
        bfr.write("            PreviewOuterCone=DrawLightConeComponent\'DrawLightConeComponent_5\'\n");
        bfr.write("            PreviewRadius=DrawLightRadiusComponent\'DrawLightRadiusComponent_211\'\n");
        bfr.write("            DepthPriorityGroup=SDPG_Foreground\n");
        bfr.write("            LightingChannels=(bInitialized=True,Dynamic=True)\n");
        bfr.write("            Name=\"LensFlareComponent_2\"\n");
        bfr.write("            ObjectArchetype=LensFlareComponent\'Engine.Default__LensFlareSource:LensFlareComponent0\'\n");
        bfr.write("         End Object\n");
        bfr.write("         Begin Object Class=SpriteComponent Name=Sprite ObjName=SpriteComponent_1346 Archetype=SpriteComponent\'Engine.Default__LensFlareSource:Sprite\'\n");
        bfr.write("            LightingChannels=(bInitialized=True,Dynamic=True)\n");
        bfr.write("            Name=\"SpriteComponent_1346\"\n");
        bfr.write("            ObjectArchetype=SpriteComponent\'Engine.Default__LensFlareSource:Sprite\'\n");
        bfr.write("         End Object\n");
        bfr.write("         Begin Object Class=ArrowComponent Name=ArrowComponent0 ObjName=ArrowComponent_1222 Archetype=ArrowComponent\'Engine.Default__LensFlareSource:ArrowComponent0\'\n");
        bfr.write("            LightingChannels=(bInitialized=True,Dynamic=True)\n");
        bfr.write("            Name=\"ArrowComponent_1222\"\n");
        bfr.write("            ObjectArchetype=ArrowComponent\'Engine.Default__LensFlareSource:ArrowComponent0\'\n");
        bfr.write("         End Object\n");
        bfr.write("         LensFlareComp=LensFlareComponent\'LensFlareComponent_2\'\n");
        bfr.write("         Components(0)=SpriteComponent\'SpriteComponent_1346\'\n");
        bfr.write("         Components(1)=DrawLightConeComponent\'DrawLightConeComponent_4\'\n");
        bfr.write("         Components(2)=DrawLightConeComponent\'DrawLightConeComponent_5\'\n");
        bfr.write("         Components(3)=DrawLightRadiusComponent\'DrawLightRadiusComponent_211\'\n");
        bfr.write("         Components(4)=LensFlareComponent\'LensFlareComponent_2\'\n");
        bfr.write("         Components(5)=ArrowComponent\'ArrowComponent_1222\'\n");
        bfr.write("         NetUpdateFrequency=0.000000\n");
        bfr.write("         Tag=\"LensFlareSource\"\n");
        bfr.write(location+"\n");
        bfr.write("         Name=\"LensFlareSource_2\"\n");
        bfr.write("         ObjectArchetype=LensFlareSource\'Engine.Default__LensFlareSource\'\n");
        bfr.write("      End Actor\n");
    }
}
