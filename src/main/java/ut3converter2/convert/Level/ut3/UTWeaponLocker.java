/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut3;

import java.util.ArrayList;
import ut3converter2.convert.Level.UTActor;

/**
 *
 * @author Hyperion
 */
public class UTWeaponLocker extends UTActor{


    String num="0";
    String otherdata="";
    ArrayList alwpclass;

    public UTWeaponLocker() {
        alwpclass = new ArrayList();
    }

    public String toString()
    {
        String tmp="";
        tmp +="      Begin Actor Class=UTWeaponLocker_Content Name=UTWeaponLocker_Content_"+num+" Archetype=UTWeaponLocker_Content\'UTGameContent.Default__UTWeaponLocker_Content\'\n";
        for(int i=0;i<alwpclass.size();i++)
        {
            tmp +="         Weapons("+i+")=(WeaponClass=Class\'"+alwpclass.get(i).toString()+"\')\n";
        }
        tmp +="         Tag=\"UTWeaponLocker_Content\"\n";
        tmp += getOtherdata();
        tmp +="         Name=\"UTWeaponLocker_Content_"+num+"\"\n";
        tmp +="         ObjectArchetype=UTWeaponLocker_Content\'UTGameContent.Default__UTWeaponLocker_Content\'\n";
        tmp +="      End Actor\n";
        return tmp;
    }
    public void AnalyseT3DData(String line)
    {
        parseOtherData(line,30F);
        
        if(line.contains("    Weapons("))
        {
            getWpClass(line);
        }
        else if(line.contains("Name=WeaponLocker"))
        {
            this.num = line.split("Name=WeaponLocker")[1];
        }
    }

    private void getWpClass(String line)
    {
          //  Weapons(0)=(WeaponClass=Class'XWeapons.LinkGun',ExtraAmmo=30)
        alwpclass.add(WeaponsClasses.getUT3WpClassFromUT2k4(line.split("\\'")[1]));

    }

}
