/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2;

/**
 *
 * @author Hyperion
 */
public class UTGames {

    public static final int UT99 = 0;
    public static final int U1 = 1;
    public static final int U2 = 2;
    public static final int UT2003 = 3;
    public static final int UT2004 = 4;
    public static final int UT3 = 5;
    public static final int DeusEx = 9;
    public static final int HalfLife = 10;

    public static final String UT99_name = "Unreal Tournament";
    public static final String U1_name = "Unreal 1";
    public static final String U2_name = "Unreal 2";
    public static final String UT2003_name = "Unreal Tournament 2003";
    public static final String UT2004_name = "Unreal Tournament 2004";
    public static final String UT3_name = "Unreal Tournament 3";
    public static final String DE_name = "Deus Ex";
    public static final String HL_name = "Half Life";

    public static final String UT99_name_short = "UT";
    public static final String U1_name_short = "U1";
    public static final String U2_name_short = "U2";
    public static final String UT2003_name_short = "UT2K3";
    public static final String UT2004_name_short = "UT2K4";
    public static final String UT3_name_short = "UT3";
    public static final String DE_name_short = "DE";
    public static final String HL_name_short = "HL";

    public static String[] getOutputUTGames(int input_utgame)
    {
        if((input_utgame==UTGames.U1)||(input_utgame==UTGames.UT99))
        {
            return new String[]{"Unreal Tournament 2004 -"+UTGames.UT2004,"Unreal Tournament 3 -"+UTGames.UT3};
        }
        else if((input_utgame==UTGames.U2)||(input_utgame==UTGames.UT2004))
        {
            return new String[]{"Unreal Tournament 3 -"+UTGames.UT3};
        }
        else
        {
            return new String[]{"-999"};
        }
    }

    public static String getUTGame(int numgame)
    {
        switch(numgame){
            case UT99:
                return UT99_name;
            case UT2003:
                return UT2003_name;
            case UT2004:
                return UT2004_name;
            case U1:
                return U1_name;
            case U2:
                return U2_name;
            case UT3:
                return UT3_name;
            case HalfLife:
                return HL_name;
            default:
                return "Game";
        }
    }
    public static String getUTGameShort(int numgame)
    {
        switch(numgame){
            case UT99:
                return UT99_name_short;
            case UT2003:
                return UT2003_name_short;
            case UT2004:
                return UT2004_name_short;
            case U1:
                return U1_name_short;
            case U2:
                return U2_name_short;
            case UT3:
                return UT3_name_short;
            case HalfLife:
                return HL_name_short;
            default:
                return "Game";
        }

    }
    public static String[] getAllGamesNames()
    {
        return new String[]{"UT99-"+UT99,"Unreal 1-"+U1,"Unreal 2-"+U2,"Unreal Tournament 2004-"+UT2004};
    }
}
