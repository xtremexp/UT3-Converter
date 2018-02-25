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
public class UTVShockTurret extends UTActor{

    String otherdata="";

    public void AnalyseT3DData(String line)
    {
        parseOtherData(line);
    }

    public String toString()
    {
        String tmp="";
        tmp +="      Begin Actor Class=UTVehicleFactory_ShieldedTurret_Shock Name=UTVehicleFactory_ShieldedTurret_Shock_0 Archetype=UTVehicleFactory_ShieldedTurret_Shock'UTGameContent.Default__UTVehicleFactory_ShieldedTurret_Shock\'\n";
        tmp +="         VehicleClass=Class\'UTGameContent.UTVehicle_ShieldedTurret_Shock\'\n";
        tmp +="         bPathsChanged=True\n";
        tmp +="         Tag=\"UTVehicleFactory_ShieldedTurret_Shock\"\n";
        tmp += getOtherdata();
        tmp +="         CollisionComponent=CylinderComponent\'CylinderComponent_1766\'\n";
        tmp +="         Name=\"UTVehicleFactory_ShieldedTurret_Shock_0\"\n";
        tmp +="         ObjectArchetype=UTVehicleFactory_ShieldedTurret_Shock'UTGameContent.Default__UTVehicleFactory_ShieldedTurret_Shock\'\n";
        tmp +="      End Actor\n";
        return tmp;
    }

}
