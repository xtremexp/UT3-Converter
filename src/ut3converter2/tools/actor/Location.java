/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools.actor;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 *
 * @author Hyperion
 */
public class Location {

    Double loc_x=0D;
    Double loc_y=0D;
    Double loc_z=0D;

    public Location(String line) {
        getLocation(line);
    }

    public Location(String line, float zdelta) {
        getLocation(line);
        addExtraZ(zdelta);
    }

    public Double getLoc_x() {
        return loc_x;
    }

    public Double getLoc_y() {
        return loc_y;
    }

    public Double getLoc_z() {
        return loc_z;
    }

    
    /**
     * Return location with updated Z value
     * Used for some actor that needs some extra Z value (e.g.: HeightFog)
     * @param line T3D original data line containing location
     * @return Location data with updated Z value
     */
    public static String getLocationStrWithUpdatedZ(String line,float zdelta)
    {
        Location loc = new Location(line);
        loc.addExtraZ(zdelta);
        return loc.locToString();
    }


    public String locToString()
    {
        DecimalFormat df = new DecimalFormat("0.000000",new DecimalFormatSymbols(Locale.US));
        return "    Location=(X="+df.format(loc_x)+",Y="+df.format(loc_y)+",Z="+df.format(loc_z)+")";
    }

    public Location getLocationClass(String line)
    {
        return new Location(line);
    }
    /**
     * Returns location of current action parsing data from line
     * @param line Current line of T3D Level file being analyzed containing Location info
     */

    private void getLocation(String line)
    {

        // Location=(X=5632.000000,Z=384.000000)
        //    Location=(X=3864.000000,Y=-5920.000000,Z=-15776.000000)
        line = line.substring(line.indexOf("(")+1);
        line = line.replaceAll("\\)","");

        String fields[] = line.split(",");
        if(fields.length==3)
        {
            loc_x = Double.valueOf(fields[0].split("\\=")[1]);
            loc_y = Double.valueOf(fields[1].split("\\=")[1]);
            loc_z = Double.valueOf(fields[2].split("\\=")[1]);
        }
        else if(fields.length==2)
        {
            if(line.contains("X")&&line.contains("Y"))
            {
                    loc_x = Double.valueOf(fields[0].split("\\=")[1]);
                    loc_y = Double.valueOf(fields[1].split("\\=")[1]);
                    loc_z = 0D;
            }
            //    Location=(X=1280.000000,Y=2944.000000)
            else if(line.contains("X")&&line.contains("Z"))
            {
                    loc_x = Double.valueOf(fields[0].split("\\=")[1]);
                    loc_y = 0D;
                    loc_z = Double.valueOf(fields[1].split("\\=")[1]);
            }
            else if(line.contains("Y")&&line.contains("Z"))
            {
                    loc_x = 0D;
                    loc_y = Double.valueOf(fields[0].split("\\=")[1]);
                    loc_z = Double.valueOf(fields[1].split("\\=")[1]);
            }
        }
        else if(fields.length==1)
        {
            if(line.contains("X"))
            {
                    loc_x = Double.valueOf(fields[0].split("\\=")[1]);
            }
            //    Location=(X=1280.000000,Y=2944.000000)
            else if(line.contains("Y"))
            {
                    loc_y = Double.valueOf(fields[0].split("\\=")[1]);
            }
            else if(line.contains("Z"))
            {
                    loc_z = Double.valueOf(fields[0].split("\\=")[1]);
            }
        }
    }

    public void addExtraZ(float zdelta)
    {
        this.loc_z += zdelta;
    }

    public void setLoc_x(Double loc_x) {
        this.loc_x = loc_x;
    }

    public void setLoc_y(Double loc_y) {
        this.loc_y = loc_y;
    }

    public void setLoc_z(Double loc_z) {
        this.loc_z = loc_z;
    }

    
}
