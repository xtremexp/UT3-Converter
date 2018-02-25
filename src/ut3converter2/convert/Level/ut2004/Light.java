/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut2004;

/**
 *
 * @author Hyperion
 */
public class Light {


    int inputgame;
    String otherdata="";
    
    public Light(int inputgame) {
        this.inputgame = inputgame;
    }

    public void setOtherdata(String otherdata) {
        this.otherdata = otherdata;
    }

    
    /*
     * Begin Actor Class=Light Name=Light84
    Group=None,Mothership
    Level=LevelInfo'MyLevel.LevelInfo0'
    Tag=Light
    Region=(Zone=LevelInfo'MyLevel.LevelInfo0',iLeaf=-1)
    Location=(X=-14176.582031,Y=16890.148438,Z=-1208.107056)
    Rotation=(Yaw=-32768)
    OldLocation=(X=-1183.320435,Y=53.900772,Z=-1008.107056)
    LightBrightness=255
    LightSaturation=183
    bSpecialLit=True
    Name=Light84
    End Actor
     * */
    public String toString()
    {
        String tmp="";
        tmp +="Begin Actor Class=Light\n";
        tmp +=otherdata+"\n";
        tmp +="End Actor\n";
        return tmp;

    }

    /**
     * Replaces obsolete UT/U1 light effects not working with UT2004 with working ones
     * @param line
     * @return
     */
    public static String UTLightEffect2UT2k4LE(String line)
    {
        //LightEffect=LE_WateryShimmer

        String oldle=line.split("\\=")[1];
        String newle="LE_None";
        if(oldle.equals("LE_WateryShimmer")){
            return line.replaceAll(oldle, newle);
        }
        else if(oldle.equals("LE_TorchWaver")){
            return line.replaceAll(oldle, newle);
        }
        else if(oldle.equals("LE_FireWaver")){
            return line.replaceAll(oldle, newle);
        }
        else if(oldle.equals("LE_CloudCast")){
            return line.replaceAll(oldle, newle);
        }
        //LE_Torchwaver
        return line;
    }
    public static String toString(String data)
    {
        String tmp="";
        tmp +="Begin Actor Class=Light\n";
        tmp +=data;
        tmp +="End Actor\n";
        return tmp;

    }



}
