/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut3;

import ut3converter2.convert.Level.UTActor;

/**
 *
 * @author Hyperion
 */
public class UTWeaponPickupFactory extends UTActor{

    String otherdata="";
    String wpclass="";

    float deltaz=38F;

    public UTWeaponPickupFactory() {
    }

    public UTWeaponPickupFactory(String wpclass) {
        this.wpclass = wpclass;
    }

    
    public void AnalyseT3DData(String line)
    {
        if(line.contains("WeaponType="))
        {
            getWpClass(line);
        }
        parseOtherData(line,deltaz);
    }


    public String toString()
    {
        String tmp="";
        tmp +="      Begin Actor Class=UTWeaponPickupFactory Name=UTWeaponPickupFactory_0 Archetype=UTWeaponPickupFactory\'UTGame.Default__UTWeaponPickupFactory\'\n";
        tmp +="         WeaponPickupClass=Class\'"+wpclass+"\'\n";
        tmp += getOtherdata();
        tmp +="         ObjectArchetype=UTWeaponPickupFactory'UTGame.Default__UTWeaponPickupFactory'\n";
        tmp +="      End Actor\n";
        return tmp;
    }
    
    private void getWpClass(String line)
    {
       wpclass = WeaponsClasses.getUT3WpClassFromUT2k4(line.split("\\'")[1]);
    }

}
