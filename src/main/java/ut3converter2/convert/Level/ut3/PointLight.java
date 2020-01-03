/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut3;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ut3converter2.convert.Level.UTActor;
import ut3converter2.convert.MapConverter;
import ut3converter2.tools.HSLColor;

/**
 * Base class for PointLight actors in UT3
 * @author Hyperion
 */
public class PointLight extends UTActor{

    public int numptlgt=0;

    /**
     * Default Hue value
     */
    int hue=0;
    /**
     * Default Saturation value
     */
    int saturation=255;
    /**
     * Default Brightness value
     */
    int brightness=64;
    
    int blue=255;
    int green=255;
    int red=255;


    final int radiusfactor=20;
    float radius=1024F;

    /**
     * If true will check if original light (e.g.: Light actor in UT2004) has corona
     */
    boolean bcheckcoronas = false;

    /**
     * If true, means the original light has corona
     */
    boolean biscorona = false;
    MapConverter mc;

    public PointLight(MapConverter mc,boolean bcheckcoronas) {
        this.bcheckcoronas = bcheckcoronas;
        this.mc = mc;
        this.radius *= mc.getScalefactor();
        resetColors();

    }

    public PointLight(MapConverter mc) {
        this.mc = mc;
        resetColors();
    }

    public PointLight() {
        resetColors();
    }

    

    private void resetColors(){
        hue = 0;
        saturation = 255;
        brightness = 64;
    }
    /**
     * Parse data from original light actor.
     * @param line
     */
    public void AnalyseT3DData(String line)
    {
        parseOtherData(line);

        if(line.contains("bCorona"))
        {
            if(line.split("\\=")[1].equalsIgnoreCase("true"))
            {
                biscorona = true;
            }
        }
        else if(line.contains("LightHue="))
        {
            float huef = Float.valueOf(line.split("=")[1]);
            hue = (int)huef;
        }
        else if(line.contains("LightSaturation="))
        {
            float satf = Float.valueOf(line.split("=")[1]);
            saturation = (int)satf;
        }
        else if(line.contains("LightBrightness="))
        {
            float brgf = Float.valueOf(line.split("=")[1]);
            brightness = (int)brgf;
        }
        else if(line.contains("LightRadius="))
        {
            float radf = Float.valueOf(line.split("=")[1]);
            this.radius = radiusfactor * (int) radf;
            if(mc!=null){
                this.radius *= mc.getScalefactor();
            }
        }
    }


    //    LightHue=9
    //    LightSaturation=126
    //    LightBrightness=200.000000
    /**
     * Export Light actor data as String for T3D Level files
     * @param bfr Writter used if corona conversion set to true
     * @return
     * @throws IOException
     */
    public String toString(BufferedWriter bfr) throws IOException
    {
        if(bfr!=null&&(bcheckcoronas)&&(biscorona))
        {
            LensFlareSource.createLFS(bfr, this.getLoc().locToString(),LensFlareSource.lf_normallight);
        }
        HSBToRGB(hue,saturation,brightness);
        String tmp="";
        tmp+="      Begin Actor Class=PointLight Name=PointLight_"+numptlgt+" Archetype=PointLight'Engine.Default__PointLight'\n";
        tmp+="         Begin Object Class=DrawLightRadiusComponent Name=DrawLightRadius0 ObjName=DrawLightRadiusComponent_0 Archetype=DrawLightRadiusComponent'Engine.Default__PointLight:DrawLightRadius0'\n";
        tmp+="            SphereRadius="+this.radius+"\n";
        tmp+="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp+="            Name=\"DrawLightRadiusComponent_0\"\n";
        tmp+="            ObjectArchetype=DrawLightRadiusComponent'Engine.Default__PointLight:DrawLightRadius0'\n";
        tmp+="         End Object\n";
        tmp+="         Begin Object Class=PointLightComponent Name=PointLightComponent0 ObjName=PointLightComponent_2 Archetype=PointLightComponent'Engine.Default__PointLight:PointLightComponent0'\n";
        tmp+="            Radius="+this.radius+"\n";
        //tmp+="            CachedParentToWorld=(XPlane=(W=0.000000,X=1.000000,Y=0.000000,Z=0.000000),YPlane=(W=0.000000,X=0.000000,Y=1.000000,Z=-0.000000),ZPlane=(W=0.000000,X=-0.000000,Y=0.000000,Z=1.000000),WPlane=(W=1.000000,X="+loc_x+",Y="+loc_y+",Z="+loc_z+"))\n";
        tmp+="            PreviewLightRadius=DrawLightRadiusComponent'DrawLightRadiusComponent_0'\n";
        tmp+="            LightColor=(B="+String.valueOf(blue)+",G="+String.valueOf(green)+",R="+String.valueOf(red)+",A=0)\n";
        tmp+="            bHasLightEverBeenBuiltIntoLightMap=True\n";
        tmp+="            Name=\"PointLightComponent_2\"\n";
        tmp+="            ObjectArchetype=PointLightComponent'Engine.Default__PointLight:PointLightComponent0'\n";
        tmp+="         End Object\n";
        tmp+="         Begin Object Class=SpriteComponent Name=Sprite ObjName=SpriteComponent_1 Archetype=SpriteComponent'Engine.Default__PointLight:Sprite'\n";
        tmp+="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp+="            Name=\"SpriteComponent_1\"\n";
        tmp+="            ObjectArchetype=SpriteComponent'Engine.Default__PointLight:Sprite'\n";
        tmp+="         End Object\n";
        tmp+="         LightComponent=PointLightComponent'PointLightComponent_2'\n";
        tmp+="         Components(0)=SpriteComponent'SpriteComponent_1'\n";
        tmp+="         Components(1)=DrawLightRadiusComponent'DrawLightRadiusComponent_0'\n";
        tmp+="         Components(2)=PointLightComponent'PointLightComponent_2'\n";
        tmp+="         Tag=\"PointLight\"\n";
        tmp+=getOtherdata();
        tmp+="         ObjectArchetype=PointLight'Engine.Default__PointLight'v\n";
        tmp+="      End Actor\n";

        numptlgt ++;
        return tmp;
    }


    /**
     * Convert Hue Saturation Brightness values from old UT2004 lightning system
     * to Red Blue Green values with new UT3 lightning system
     * @param h Hue, float Range: 0->255
     * @param s Saturation, float Range: 0->255
     * @param b Brightness, float Range: 0->255
     */
    private void HSBToRGB(float h,float s,float b)
    {
        //U1/UT/UT2004
        // 0--->Brightness--->255 ===> 0--->Saturation--->1
        // 0--->Hue--->255 ===> 0--->Hue--->1
        // 0--->Saturation--->255 ===>

        //UT3
        // 0--->Blue--->255
        // 0--->Green--->255
        // 0--->Red--->255

        //Seems always a bit brighter in UT/UT2004
        s *=1.2F;//INCREASES BRIGHTNESS BY 20%
        if(s>255F){s=255F;}
        if(b>255F){b=255F;}
        
        h = h*360F/255F;
        s = s*100F/255F;
        b = b*100F/255F;
        //IT SEEMS THAT EPIC SWAPPED BRIGHTNESS AND SATURATION!!!
        Color convcolor = HSLColor.toRGB(h, b, s, 1f);

        //System.out.println("Color.HSBtoRGB("+h+","+ s+","+ b+")");
        //int rgb = Color.HSBtoRGB(h, s, b);

        this.red = convcolor.getRed();
        this.blue = convcolor.getBlue();
        this.green = convcolor.getGreen();

    }
}
