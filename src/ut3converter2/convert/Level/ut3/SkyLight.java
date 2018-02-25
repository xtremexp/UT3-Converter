/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut3;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.IOException;
import ut3converter2.convert.Level.UTActor;

/**
 *
 * @author Hyperion
 */
public class SkyLight extends UTActor{


    public int numdirlgt=0;

    int hue=0;
    int saturation=255;
    int brightness=64;

    int blue=255;
    int green=255;
    int red=255;


    float radius=1024F;

    boolean bcheckcoronas = false;
    boolean biscorona = false;
    boolean bcreatelfsunlight=true;

    public SkyLight() {
    }

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
            this.radius = 20*(int)radf;
        }
    }



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
        //System.out.println(rgb);

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

        //System.out.println("R : " + red + " G : " + green + " B : " + blue);


    }

    public String toString(BufferedWriter bwr) throws IOException
    {
        String tmp="";

        HSBToRGB(hue,saturation,brightness);
        if(bcreatelfsunlight)
        {
            LensFlareSource.createLFS(bwr, this.getLoc().locToString(), LensFlareSource.lf_sunlight);
        }
        tmp += "      Begin Actor Class=SkyLight Name=SkyLight_2 Archetype=SkyLight\'Engine.Default__SkyLight\'\n";
        tmp += "         Begin Object Class=SkyLightComponent Name=SkyLightComponent0 ObjName=SkyLightComponent_2 Archetype=SkyLightComponent\'Engine.Default__SkyLight:SkyLightComponent0\'\n";
        tmp += "            LightColor=(B="+String.valueOf(blue)+",G="+String.valueOf(green)+",R="+String.valueOf(red)+",A=0)\n";
        tmp += "            Name=\"SkyLightComponent_2\"\n";
        tmp += "            ObjectArchetype=SkyLightComponent\'Engine.Default__SkyLight:SkyLightComponent0\'\n";
        tmp += "         End Object\n";
        tmp += "         Begin Object Class=SpriteComponent Name=Sprite ObjName=SpriteComponent_1490 Archetype=SpriteComponent\'Engine.Default__SkyLight:Sprite\'\n";
        tmp += "            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp += "            Name=\"SpriteComponent_1490\"\n";
        tmp += "            ObjectArchetype=SpriteComponent\'Engine.Default__SkyLight:Sprite\'\n";
        tmp += "         End Object\n";
        tmp += "         Begin Object Class=ArrowComponent Name=ArrowComponent0 ObjName=ArrowComponent_900 Archetype=ArrowComponent\'Engine.Default__SkyLight:ArrowComponent0\'\n";
        tmp += "            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp += "            Name=\"ArrowComponent_900\"\n";
        tmp += "            ObjectArchetype=ArrowComponent\'Engine.Default__SkyLight:ArrowComponent0\'\n";
        tmp += "         End Object\n";
        tmp += "         LightComponent=SkyLightComponent\'SkyLightComponent_2\'\n";
        tmp += "         Components(0)=SpriteComponent\'SpriteComponent_1490\'\n";
        tmp += "         Components(1)=SkyLightComponent\'SkyLightComponent_2\'\n";
        tmp += "         Components(2)=ArrowComponent\'ArrowComponent_900\'\n";
        tmp += getOtherdata();
        tmp += "         ObjectArchetype=SkyLight\'Engine.Default__SkyLight\'\n";
        tmp += "      End Actor\n";

        return tmp;
    }
}
