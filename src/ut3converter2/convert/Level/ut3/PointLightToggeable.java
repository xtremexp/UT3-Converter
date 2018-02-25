/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut3;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 *
 * @author Hyperion
 */
public class PointLightToggeable {
     public int numptlgt=0;

    /**
     * Hue value
     */
    int hue=0;
    int saturation=255;
    int brightness=64;

    int blue=255;
    int green=255;
    int red=255;

    /**
     * Location of Point Light with axis X
     */
    float loc_x=0F;

    /**
     * Location of Point Light with axis Y
     */
    float loc_y=0F;

    /**
     * Location of Point Light with axis Z
     */
    float loc_z=0F;

    final int radiusfactor=22;
    float radius=1024F;
    String location="";
    /**
     * If true will check if original light (e.g.: Light actor in UT2004) has corona
     */
    boolean bcheckcoronas = false;

    /**
     * If true, means the original light has corona
     */
    boolean biscorona = false;

    public PointLightToggeable(boolean bcheckcoronas) {
        //this.bcheckcoronas = bcheckcoronas;
        this.bcheckcoronas = false;

    }

    /**
     * Parse data from original light actor.
     * @param line
     */
    public void AnalyseT3DData(String line)
    {
        if(line.contains(" Location="))
        {
            getLocation(line);
            location = line;
        }
        else if(line.contains("bCorona"))
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
            this.radius = radiusfactor*(int)radf;
        }
    }

    /**
     * Returns location of current action parsing data from line
     * @param line Current line of T3D Level file being analyzed containing Location info
     */
    private void getLocation(String line)
    {
        //    Location=(X=3864.000000,Y=-5920.000000,Z=-15776.000000)

        line = line.split("\\(")[1];
        line = line.replaceAll("\\)","");
        String fields[] = line.split("=");
        if(fields.length==4)
        {
            loc_x = Float.valueOf(fields[1].replaceAll(",Y",""));
            loc_y = Float.valueOf(fields[2].replaceAll(",Z",""));
            loc_z = Float.valueOf(fields[3]);
        }
        else if(fields.length==3)
        {
            //    Y 2213.000000,Z 151.000000
            if(!line.contains("X="))
            {
                loc_y = Float.valueOf(fields[1].replaceAll(",Z",""));
                loc_z = Float.valueOf(fields[2]);
            }
            //    X 2213.000000,Z 151.000000
            else if(!line.contains("Y="))
            {
                loc_x = Float.valueOf(fields[1].replaceAll(",Z",""));
                loc_z = Float.valueOf(fields[2]);
            }
            //    X 2213.000000,Y 151.000000
            else if(!line.contains("Z="))
            {
                loc_x = Float.valueOf(fields[1].replaceAll(",Y",""));
                loc_y = Float.valueOf(fields[2]);
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
        if((bcheckcoronas)&&(biscorona))
        {
            LensFlareSource.createLFS(bfr, location,LensFlareSource.lf_normallight);
        }
        HSBToRGB(hue,saturation,brightness);
        String tmp="";
        tmp+="      Begin Actor Class=PointLightToggleable Name=PointLightToggleable_"+numptlgt+" Archetype=PointLightToggleable\'Engine.Default__PointLightToggleable\'\n";
        tmp+="         Begin Object Class=DrawLightRadiusComponent Name=DrawLightRadius0 ObjName=DrawLightRadiusComponent_1729 Archetype=DrawLightRadiusComponent\'Engine.Default__PointLightToggleable:DrawLightRadius0\'\n";
        tmp+="            SphereRadius="+this.radius+"\n";
        tmp+="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp+="            Name=\"DrawLightRadiusComponent_1729\"\n";
        tmp+="            ObjectArchetype=DrawLightRadiusComponent\'Engine.Default__PointLightToggleable:DrawLightRadius0\'\n";
        tmp+="         End Object\n";
        tmp+="         Begin Object Class=PointLightComponent Name=PointLightComponent0 ObjName=PointLightComponent_2005 Archetype=PointLightComponent\'Engine.Default__PointLightToggleable:PointLightComponent0\'\n";
        tmp+="            Radius="+this.radius+"\n";
        tmp+="            CachedParentToWorld=(XPlane=(W=0.000000,X=1.000000,Y=0.000000,Z=0.000000),YPlane=(W=0.000000,X=0.000000,Y=1.000000,Z=-0.000000),ZPlane=(W=0.000000,X=-0.000000,Y=0.000000,Z=1.000000),WPlane=(W=1.000000,X=464.000000,Y=768.000000,Z=704.000000))\n";
        tmp+="            PreviewLightRadius=DrawLightRadiusComponent\'DrawLightRadiusComponent_1729\'\n";
        tmp+="            LightColor=(B="+String.valueOf(blue)+",G="+String.valueOf(green)+",R="+String.valueOf(red)+",A=0)\n";
        tmp+="            Name=\"PointLightComponent_2005\"\n";
        tmp+="            ObjectArchetype=PointLightComponent\'Engine.Default__PointLightToggleable:PointLightComponent0\'\n";
        tmp+="         End Object\n";
        tmp+="         Begin Object Class=SpriteComponent Name=Sprite ObjName=SpriteComponent_5043 Archetype=SpriteComponent\'Engine.Default__PointLightToggleable:Sprite\'\n";
        tmp+="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp+="            Name=\"SpriteComponent_5043\"\n";
        tmp+="            ObjectArchetype=SpriteComponent\'Engine.Default__PointLightToggleable:Sprite\'\n";
        tmp+="         End Object\n";
        tmp+="         LightComponent=PointLightComponent\'PointLightComponent_2005\'\n";
        tmp+="         Components(0)=SpriteComponent\'SpriteComponent_5043\'\n";
        tmp+="         Components(1)=DrawLightRadiusComponent\'DrawLightRadiusComponent_1729\'\n";
        tmp+="         Components(2)=PointLightComponent\'PointLightComponent_2005\'\n";
        tmp+="         Tag=\"PointLightToggleable\"\n";
        tmp+="         Location=(X="+loc_x+",Y="+loc_y+",Z="+loc_z+")\n";
        tmp+="         Name=\"PointLightToggleable_"+numptlgt+"\"\n";
        tmp+="         ObjectArchetype=PointLightToggleable\'Engine.Default__PointLightToggleable\'\n";
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
        //System.out.println("h:"+h+" ("+h/255F+") s:"+s+" ("+s/255F+") b:"+b+" ("+b/255F+")");
        h = h/255F;
        //if()
        s = (s-255F)*(-1F);
        s = s/255F;

        b += 130;
        if(b>255F){b=255F;}
        b = b/255F;

        int rgb = Color.HSBtoRGB(h, s, b);

        this.red = (rgb>>16)&0xFF;
        this.green = (rgb>>8)&0xFF;
        this.blue = rgb&0xFF;

        if((red==green)&&(green==blue))
        {
            if(b<190)
            {
                b += 40;
                rgb = Color.HSBtoRGB(h, s, b);
                this.red = (rgb>>16)&0xFF;
                this.green = (rgb>>8)&0xFF;
                this.blue = rgb&0xFF;
            }


        }
    }

}
