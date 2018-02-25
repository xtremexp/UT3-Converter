/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.halflife;

/**
 *
 * @author Hyperion
 */
public class HlFieldToUTField {

    private static final double scalefactor=1D;

    public static String toUT2004Field(String field,String value)
    {
        String tmp="";
        if(field.equals("_light"))
        {
            return parseLight(value);
        }
        else if(field.equals("origin"))
        {
            return parseLocation(value);
        }

        return tmp;
    }


    /**
     * "origin" "-224 792 -146"
     * ->    Location=(X=-14176.582031,Y=16890.148438,Z=-1208.107056)
     * @param locvalues
     * @return
     */
    private static String parseLocation(String locvalues)
    {
        String tmp="";
        String t[] = locvalues.split("\\ ");
        double x=Double.valueOf(t[0]);
        double y=Double.valueOf(t[1]);
        double z=Double.valueOf(t[2]);
        x *= scalefactor;
        y *= -1D;
        z *= scalefactor;
        return "    Location=(X="+x+",Y="+y+",Z="+z+")\n";
    }

    /**
     * "_light" "70 80 100"
     *  "_light" " 220 210 150 200"
     * ->
     * LightHue=70
     * LightBrightness=80
     * LightSaturation=100
     * @param lightval
     * @return
     */
    private static String parseLight(String lightvalues)
    {
        String tmp="";
        String t[] = lightvalues.split("\\ ");
        if(t.length==3)
        {
            tmp += "    LightHue="+t[0]+"\n";
            tmp += "    LightBrightness="+t[1]+"\n";
            tmp += "    LightSaturation="+t[2]+"\n";
            return tmp;
        }
        else if(t.length==4)
        {
            tmp += "    LightHue="+t[0]+"\n";
            tmp += "    LightBrightness="+t[1]+"\n";
            tmp += "    LightSaturation="+t[2]+"\n";
            return tmp;
        }
        else if(t.length==5)
        {
            tmp += "    LightHue="+t[1]+"\n";
            tmp += "    LightBrightness="+t[2]+"\n";
            tmp += "    LightSaturation="+t[3]+"\n";
            return tmp;
        }
        else
        {
            return "";
        }
    }

}
