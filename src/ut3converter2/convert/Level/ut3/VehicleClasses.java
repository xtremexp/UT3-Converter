/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ut3converter2.convert.Level.ut3;

/**
 *
 * @author Hyperion
 */
public class VehicleClasses {

    public static String ut3v_hellbender = "UTGameContent.UTVehicle_hellbender_content";
    public static String ut3v_scorpion = "UTGameContent.UTVehicle_scorpion_content";
    public static String ut3v_tank = "UTGameContent.UTVehicle_Goliath_Content";
    public static String ut3v_raptor = "UTGameContent.UTVehicle_raptor_content";
    public static String ut3v_cicada = "UTGameContent.UTVehicle_cicada_content";
    public static String ut3v_leviathan = "UTGameContent.UTVehicle_leviathan_content";
    public static String ut3v_spma = "UTGameContent.UTVehicle_spma_content";
    public static String ut3v_paladin = "UTGameContent.UTVehicle_paladin_content";
    public static String ut3v_manta = "UTGameContent.UTVehicle_Manta_Content";
    public static String ut3v_turminigun = "UTGameContent.UTVehicle_Manta_Content";

    public static String getUT3vhShortNameFrUT2k4(String ut2k4vhclass) {
        if (ut2k4vhclass.equals("Onslaught.ONSRV") || ut2k4vhclass.equals("ONSRVFactory")) {
            return "Scorpion";
        } else if (ut2k4vhclass.equals("Onslaught.ONSPRV") || ut2k4vhclass.equals("ONSPRVFactory")) {
            return "HellBender";
        } else if (ut2k4vhclass.equals("OnslaughtFull.ONSMobileAssaultStation") || ut2k4vhclass.equals("ONSMASFactory")) {
            return "Leviathan";
        } else if (ut2k4vhclass.equals("OnslaughtBP.ONSArtillery")) {
            return "SPMA";
        } else if (ut2k4vhclass.equals("OnslaughtBP.ONSShockTank")) {
            return "Paladin";
        } else if (ut2k4vhclass.equals("Onslaught.ONSHoverBike") || ut2k4vhclass.equals("ONSHoverCraftFactory")) {
            return "Manta";
        } else if (ut2k4vhclass.equals("Onslaught.ONSAttackCraft") || ut2k4vhclass.equals("ONSAttackCraftFactory")) {
            return "Raptor";
        } else if (ut2k4vhclass.equals("Onslaught.ONSHoverTank") || ut2k4vhclass.equals("ONSTankFactory")) {
            return "Goliath";
        } else if (ut2k4vhclass.equals("OnslaughtBP.ONSDualAttackCraft")) {
            return "Cicada";
        } else {
            return "None";
        }
    }

    public static boolean isVehicleFactory(String ut2k4vhclass) {
        if (ut2k4vhclass.equals("ONSAttackCraftFactory")) {
            return true;
        }
        if (ut2k4vhclass.equals("ONSDualAttackCraftFactory")) {
            return true;
        } else if (ut2k4vhclass.equals("ONSRVFactory")) {
            return true;
        } else if (ut2k4vhclass.equals("ONSPRVFactory")) {
            return true;
        } else if (ut2k4vhclass.equals("ONSTankFactory")) {
            return true;
        } else if (ut2k4vhclass.equals("ONSShockTankFactory")) {
            return true;
        } else if (ut2k4vhclass.equals("ONSMASFactory")) {
            return true;
        } else if (ut2k4vhclass.equals("ONSArtilleryFactory")) {
            return true;
        } else if (ut2k4vhclass.equals("ONSBomberFactory")) {
            return true;
        } else if (ut2k4vhclass.equals("ONSHoverCraftFactory")) {
            return true;
        } else {
            return false;
        }
    }

    public static String getUT3VhClassFromUT2k4(String ut2k4vhclass) {
        if (ut2k4vhclass.equals("Onslaught.ONSRV") || ut2k4vhclass.equals("ONSRVFactory")) {
            return ut3v_scorpion;
        } else if (ut2k4vhclass.equals("Onslaught.ONSPRV") || ut2k4vhclass.equals("ONSPRVFactory")) {
            return ut3v_hellbender;
        } else if (ut2k4vhclass.equals("OnslaughtFull.ONSMobileAssaultStation") || ut2k4vhclass.equals("ONSMASFactory")) {
            return ut3v_leviathan;
        } else if (ut2k4vhclass.equals("OnslaughtBP.ONSArtillery") || ut2k4vhclass.equals("OnslaughtBP.ONSArtilleryFactory")) {
            return ut3v_spma;
        } else if (ut2k4vhclass.equals("OnslaughtBP.ONSShockTank")) {
            return ut3v_paladin;
        } else if (ut2k4vhclass.equals("Onslaught.ONSHoverBike") || ut2k4vhclass.equals("ONSHoverCraftFactory")) {
            return ut3v_manta;
        } //ONSAttackCraftFactory
        else if (ut2k4vhclass.equals("Onslaught.ONSAttackCraft") || ut2k4vhclass.equals("ONSAttackCraftFactory")
                || ut2k4vhclass.equals("ONSDualAttackCraftFactory")) {
            return ut3v_raptor;
        } else if (ut2k4vhclass.equals("Onslaught.ONSHoverTank") || ut2k4vhclass.equals("ONSTankFactory")) {
            return ut3v_tank;
        } else if (ut2k4vhclass.equals("OnslaughtBP.ONSDualAttackCraft")) {
            return ut3v_cicada;
        } else {
            return "None";
        }
    }
}
