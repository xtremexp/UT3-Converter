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
public class UTPickupFactory extends UTActor{

    String otherdata="";
    float deltaz=38F;
    float loc_x=0F;
    float loc_y=0F;
    float loc_z=0F;
    public static int pickup_medium_health=0;
    public static int pickup_super_health=1;
    public static int pickup_vest=2;
    public static int pickup_thighpads=3;
    public static int pickup_belt=4;
    public static int pickup_udamage=5;
    
    int pickup_type;
    
    public UTPickupFactory(String curclass) {
        getType(curclass);
    }

    private void getType(String curclass)
    {
        if(curclass.equals("HealthCharger")||curclass.equals("NewHealthCharger"))
        {
            this.pickup_type = UTPickupFactory.pickup_medium_health;
        }
        else if(curclass.equals("SuperShieldCharger"))
        {
            this.pickup_type = UTPickupFactory.pickup_vest;
        }
        else if(curclass.equals("ShieldCharger"))
        {
            this.pickup_type = UTPickupFactory.pickup_thighpads;
        }
        else if(curclass.equals("SuperHealthCharger")||curclass.equals("NewSuperHealthCharger"))
        {
            this.pickup_type = UTPickupFactory.pickup_super_health;
        }
        else if(curclass.equals("UDamageCharger"))
        {
            this.pickup_type = UTPickupFactory.pickup_udamage;
        }
    }

    public void AnalyseT3DData(String line)
    {
        parseOtherData(line);
    }

    public String toString()
    {
        String tmp="";
        loc_z += deltaz;
        if(pickup_type==pickup_medium_health)
        {
            tmp +="      Begin Actor Class=UTPickupFactory_MediumHealth Name=UTPickupFactory_MediumHealth2 Archetype=UTPickupFactory_MediumHealth\'UTGame.Default__UTPickupFactory_MediumHealth\'\n";
            tmp +="         Tag=\"UTPickupFactory_MediumHealth\"\n";
            tmp += getOtherdata();
            tmp +="         Name=\"UTPickupFactory_MediumHealth2\"\n";
            tmp +="         ObjectArchetype=UTPickupFactory_MediumHealth\'UTGame.Default__UTPickupFactory_MediumHealth\'\n";
            tmp +="      End Actor\n";
        }
        else if(pickup_type==pickup_super_health)
        {
            tmp +="      Begin Actor Class=UTPickupFactory_SuperHealth Name=UTPickupFactory_SuperHealth0 Archetype=UTPickupFactory_SuperHealth\'UTGameContent.Default__UTPickupFactory_SuperHealth\'\n";
            tmp +="         Tag=\"UTPickupFactory_SuperHealth\"\n";
            tmp += getOtherdata();
            tmp +="         Name=\"UTPickupFactory_SuperHealth0\"\n";
            tmp +="         ObjectArchetype=UTPickupFactory_SuperHealth\'UTGameContent.Default__UTPickupFactory_SuperHealth\'\n";
            tmp +="      End Actor\n";
        }
        else if(pickup_type==pickup_vest)
        {
            tmp +="      Begin Actor Class=UTArmorPickup_Vest Name=UTArmorPickup_Vest_0 Archetype=UTArmorPickup_Vest\'UTGame.Default__UTArmorPickup_Vest\'\n";
            tmp +="         Tag=\"UTArmorPickup_Vest\"\n";
            tmp += getOtherdata();
            tmp +="         Name=\"UTArmorPickup_Vest_0\"\n";
            tmp +="         ObjectArchetype=UTArmorPickup_Vest\'UTGame.Default__UTArmorPickup_Vest\'\n";
            tmp +="      End Actor\n";
        }
        else if(pickup_type==pickup_thighpads)
        {
            tmp +="      Begin Actor Class=UTArmorPickup_Thighpads Name=UTArmorPickup_Thighpads_1 Archetype=UTArmorPickup_Thighpads\'UTGame.Default__UTArmorPickup_Thighpads\'\n";
            tmp +="         Tag=\"UTArmorPickup_Thighpads\"\n";
            tmp += getOtherdata();
            tmp +="         Name=\"UTArmorPickup_Thighpads_1\"\n";
            tmp +="         ObjectArchetype=UTArmorPickup_Thighpads\'UTGame.Default__UTArmorPickup_Thighpads\'\n";
            tmp +="      End Actor\n";
        }
        else if(pickup_type==pickup_belt)
        {
            tmp +="      Begin Actor Class=UTArmorPickup_ShieldBelt Name=UTArmorPickup_ShieldBelt_1 Archetype=UTArmorPickup_ShieldBelt\'UTGameContent.Default__UTArmorPickup_ShieldBelt\'\n";
            tmp +="         Tag=\"UTArmorPickup_ShieldBelt\"\n";
            tmp += getOtherdata();
            tmp +="         Name=\"UTArmorPickup_ShieldBelt_1\"\n";
            tmp +="         ObjectArchetype=UTArmorPickup_ShieldBelt\'UTGameContent.Default__UTArmorPickup_ShieldBelt\'\n";
            tmp +="      End Actor\n";
        }
        else if(pickup_type==pickup_udamage)
        {
            tmp +="      Begin Actor Class=UTPickupFactory_UDamage Name=UTPickupFactory_UDamage_1 Archetype=UTPickupFactory_UDamage\'UTGameContent.Default__UTPickupFactory_UDamage'\'\n";
            tmp +="         Tag=\"UTPickupFactory_UDamage\"\n";
            tmp += getOtherdata();
            tmp +="         Name=\"UTPickupFactory_UDamage_1\"\n";
            tmp +="         ObjectArchetype=UTPickupFactory_UDamage\'UTGameContent.Default__UTPickupFactory_UDamage\'\n";
            tmp +="      End Actor\n";
        }


        return tmp;
    }

}
