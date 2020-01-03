/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut3;

/**
 *
 * @author Hyperion
 */
public class SpecPickupClasses {

    public static String ut3sp_udamage="UTPickupFactory_UDamage";
    public static String ut3sp_jumpboots="UTPickupFactory_JumpBoots";
    public static String ut3sp_invis="UTPickupFactory_Invisibility";

    public static String getUT3SpNmFromUT2k4(String ut2k4spclass)
    {
        if(ut2k4spclass.equals("UDamageCharger"))
        {
            return ut3sp_udamage;
        }
        return "";
    }
    
}
