/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut2004;

/**
 *
 * @author Hyperion
 */
public class UT2k4GameTypes {

    public static String getUT2004Gametype(String utgametype)
    {
        if(utgametype.equals("Class'Botpack.CTFGame'"))
        {
            return "\"XGame.xCTFGame\"";
        }
        //Class'Botpack.Assault'
        else if(utgametype.equals("Class'Botpack.Assault'"))
        {
            return "\"UT2k4Assault.ASGameInfo\"";
        }
        else
        {
            return "\"XGame.xDeathMatch\"";
        }
    }

    public static boolean isAssault(String utgametype)
    {
        if(utgametype.equals("\"UT2k4Assault.ASGameInfo\""))
        {
            return true;
        }
        else if(utgametype.equals("Class'Botpack.Assault'"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
