/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import ut3converter2.tools.actor.Location;

/**
 *
 * @TODO Change DrawScale3D to DrawScale for non volumic actors
 * @author Hyperion
 */
public class UTActor {

    String otherdata="";
    Location loc;
    Location coloc; //Colocation
    boolean usecolocation=false;

    /**
     * Get some important info about actors like location,rotation,drawscale,...
     * @param line T3D level line being analyzed
     */
    public void parseOtherData(String line)
    {
        if(line.contains(" Location=")||line.contains("\tLocation=")){loc = new Location(line);}
        else if(line.contains("DrawScale3D")){addOtherData(line);}
        else if(line.contains("DrawScale=")){addOtherData(line);}
        else if(line.contains("Rotation")){addOtherData(line);}
        else if(line.contains("PrePivot")){addOtherData(line);}
        else if(line.contains(" Group=")||line.contains("\tGroup=")){addOtherData(line);}
    }

    /**
     * Get some important info about actors like location,rotation,drawscale,...
     * @param line T3D level line being analyzed
     */
    public void parseOtherData(String line, boolean usecolocation)
    {
        this.usecolocation = usecolocation;
        if(line.contains(" Location=")||line.contains("\tLocation=")){
            if(!usecolocation){
                addOtherData(line);
            }
            loc = new Location(line);
        }
        else if(line.contains(" ColLocation=") || line.contains("\tColLocation=")|| line.contains("ColLocation="))
        {
            coloc = new Location(line);
        }
        else if(line.contains("DrawScale3D")){addOtherData(line);}
        else if(line.contains("DrawScale=")){addOtherData(line);}
        else if(line.contains("Rotation")){addOtherData(line);}
        else if(line.contains("PrePivot")){addOtherData(line);}
        else if(line.contains(" Group=")||line.contains("\tGroup=")){addOtherData(line);}
    }

    /**
     * Get some important info about actors like location,rotation,drawscale,...
     * Changed Z location value of this actor
     * @param deltaz Extra Z value for location
     * @param line T3D level line being analyzed
     */
    public void parseOtherData(String line,float deltaz)
    {
        if(line.contains(" Location=")||line.contains("\tLocation=")){
            loc = new Location(line, deltaz);
        }
        else if(line.contains("DrawScale3D")){addOtherData(line);}
        else if(line.contains("DrawScale=")){addOtherData(line);}
        else if(line.contains("Rotation")){addOtherData(line);}
        else if(line.contains(" Group=")||line.contains("\tGroup=")){addOtherData(line);}
    }

    private void addOtherData(String somedata)
    {
        this.otherdata += somedata+"\n";
    }

    public String getOtherdata() {

        if(usecolocation&&loc!=null&&coloc!=null){
            loc.setLoc_x(loc.getLoc_x()+coloc.getLoc_x());
            loc.setLoc_y(loc.getLoc_y()+coloc.getLoc_y());
            loc.setLoc_z(loc.getLoc_z()+coloc.getLoc_z());
        }

        if(loc!=null){
            addOtherData(loc.locToString());
        }
        
        return otherdata;
    }

    public void setOtherdata(String otherdata) {
        this.otherdata = otherdata;
    }

    
    public Location getLoc() {
        return loc;
    }

    public String formatValue(double value)
    {
        DecimalFormat df = new DecimalFormat("0.000000",new DecimalFormatSymbols(Locale.US));
        return df.format(value);
    }

    public static String getActorClass(String line)
    {
        return (line.split("=")[1]).split(" ")[0];
    }
    

    
}
