/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut3;

/**
 *
 * @author Hyperion
 */
public class WeaponsClasses {

    public static String ut3w_shock="UTGame.UTWeap_ShockRifle";
    public static String ut3w_avril="UTGame.UTWeap_Avril_Content";
    public static String ut3w_biorifle="UTGame.UTWeap_BioRifle_Content";
    public static String ut3w_flak="UTGame.UTWeap_FlakCannon";
    public static String ut3w_linkgun="UTGame.UTWeap_LinkGun";
    public static String ut3w_redeemer="UTGame.UTWeap_Redeemer_Content";
    public static String ut3w_rocket="UTGame.UTWeap_RocketLauncher";
    public static String ut3w_sniper="UTGame.UTWeap_SniperRifle";
    public static String ut3w_stinger="UTGame.UTWeap_Stinger";

    public static String getUT3WpClassFromUT99(String ut99wpclass)
    {
        if(ut99wpclass.equals("ShockRifle"))
        {
            return ut3w_shock;
        }
        else if(ut99wpclass.equals("WarheadLauncher"))
        {
            return ut3w_redeemer;
        }
        else if(ut99wpclass.equals("Onslaught.ONSAVRiL"))
        {
            return ut3w_avril;
        }
        else if(ut99wpclass.equals("XWeapons.LinkGun"))
        {
            return ut3w_avril;
        }
        else if(ut99wpclass.equals("ut_biorifle"))
        {
            return ut3w_biorifle;
        }
        else if(ut99wpclass.equals("UT_FlakCannon"))
        {
            return ut3w_flak;
        }
        else if(ut99wpclass.equals("XWeapons.ShockRifle"))
        {
            return ut3w_linkgun;
        }
        else if(ut99wpclass.equals("XWeapons.ShockRifle")||ut99wpclass.equals("OnslaughtFull.ONSPainter"))
        {
            return ut3w_redeemer;
        }
        else if(ut99wpclass.equals("UT_Eightball"))
        {
            return ut3w_rocket;
        }
        else if(ut99wpclass.equals("SniperRifle")||ut99wpclass.equals("XWeapons.SniperRifle"))
        {
            return ut3w_sniper;
        }
        else if(ut99wpclass.equals("minigun2")||ut99wpclass.equals("ripper"))
        {
            return ut3w_stinger;
        }
        else if(ut99wpclass.equals("Onslaught.ONSGrenadeLauncher"))
        {
            return ut3w_stinger;
        }
        return "";
    }

    public static String getUT3WpClassFromUT2k4(String ut2k4wpclass)
    {
        if(ut2k4wpclass.equals("XWeapons.ShockRifle"))
        {
            return ut3w_shock;
        }
        else if(ut2k4wpclass.equals("XWeapons.Redeemer"))
        {
            return ut3w_redeemer;
        }
        else if(ut2k4wpclass.equals("Onslaught.ONSAVRiL"))
        {
            return ut3w_avril;
        }
        else if(ut2k4wpclass.equals("XWeapons.LinkGun"))
        {
            return ut3w_avril;
        }
        else if(ut2k4wpclass.equals("XWeapons.BioRifle"))
        {
            return ut3w_biorifle;
        }
        else if(ut2k4wpclass.equals("XWeapons.FlakCannon"))
        {
            return ut3w_flak;
        }
        else if(ut2k4wpclass.equals("XWeapons.ShockRifle"))
        {
            return ut3w_linkgun;
        }
        else if(ut2k4wpclass.equals("XWeapons.ShockRifle")||ut2k4wpclass.equals("OnslaughtFull.ONSPainter"))
        {
            return ut3w_redeemer;
        }
        else if(ut2k4wpclass.equals("XWeapons.RocketLauncher"))
        {
            return ut3w_rocket;
        }
        else if(ut2k4wpclass.equals("UTClassic.ClassicSniperRifle")||ut2k4wpclass.equals("XWeapons.SniperRifle"))
        {
            return ut3w_sniper;
        }
        else if(ut2k4wpclass.equals("XWeapons.Minigun"))
        {
            return ut3w_stinger;
        }
        else if(ut2k4wpclass.equals("Onslaught.ONSGrenadeLauncher"))
        {
            return ut3w_stinger;
        }
        return "";
    }
}
