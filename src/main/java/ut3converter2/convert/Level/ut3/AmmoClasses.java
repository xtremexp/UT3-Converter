/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut3;

/**
 *
 * @author Hyperion
 */
public class AmmoClasses {

    public static String ut3am_avril="UTGame.UTAmmo_AVRiL";
    public static String ut3am_bio="UTGame.UTAmmo_BioRifle_Content";
    public static String ut3am_enforcer="UTGame.UTAmmo_Enforcer";
    public static String ut3am_flak="UTGame.UTAmmo_FlakCannon";
    public static String ut3am_linkgun="UTGame.UTAmmo_LinkGun";
    public static String ut3am_rocket="UTGame.UTAmmo_RocketLauncher";
    public static String ut3am_shock="UTGame.UTAmmo_ShockRifle";
    public static String ut3am_sniper="UTGame.UTAmmo_SniperRifle";
    public static String ut3am_minigun="UTGame.UTAmmo_Stinger";

    

       
    public static String getUT3AmmoNmFromUT99(String ut99amclass)
    {
        if(ut99amclass.equals("BulletBox"))
        {
            return ut3am_sniper;
        }
        else if(ut99amclass.equals("bioammo"))
        {
            return ut3am_bio;
        }
        else if(ut99amclass.equals("RocketPack"))
        {
            return ut3am_rocket;
        }
        else if(ut99amclass.equals("PAmmo"))
        {
            return ut3am_linkgun;
        }
        else if(ut99amclass.equals("ShockCore"))
        {
            return ut3am_shock;
        }
        else if(ut99amclass.equals("ClassicSniperAmmoPickup"))
        {
            return ut3am_sniper;
        }
        else if(ut99amclass.equals("FlakAmmoPickup") || ut99amclass.equals("FlakAmmo"))
        {
            return ut3am_flak;
        }
        else if(ut99amclass.equals("ONSAVRiLAmmoPickup"))
        {
            return ut3am_avril;
        }
        else if(ut99amclass.equals("SniperAmmoPickup"))
        {
            return ut3am_sniper;
        }
        else if(ut99amclass.equals("BladeHopper"))
        {
            return ut3am_minigun;
        }
        return "";
    }

    public static String getUT3AmmoNmFromUT2k4(String ut2k4amclass)
    {
        if(ut2k4amclass.equals("BioAmmoPickup"))
        {
            return ut3am_bio;
        }
        else if(ut2k4amclass.equals("RocketAmmoPickup"))
        {
            return ut3am_rocket;
        }
        else if(ut2k4amclass.equals("LinkAmmoPickup"))
        {
            return ut3am_linkgun;
        }
        else if(ut2k4amclass.equals("ShockAmmoPickup"))
        {
            return ut3am_shock;
        }
        else if(ut2k4amclass.equals("ClassicSniperAmmoPickup"))
        {
            return ut3am_sniper;
        }
        else if(ut2k4amclass.equals("FlakAmmoPickup"))
        {
            return ut3am_flak;
        }
        else if(ut2k4amclass.equals("ONSAVRiLAmmoPickup"))
        {
            return ut3am_avril;
        }
        else if(ut2k4amclass.equals("SniperAmmoPickup"))
        {
            return ut3am_sniper;
        }
        else if(ut2k4amclass.equals("MinigunAmmoPickup"))
        {
            return ut3am_minigun;
        }
        return "";
    }

}
