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
public class UTVehicleFactory extends UTActor{


    String ut3vclass="None";
    String ut3vcl="";
    float deltaz=35F;


    public UTVehicleFactory(String vclass) {
        this.ut3vclass = VehicleClasses.getUT3VhClassFromUT2k4(vclass);
        this.ut3vcl = VehicleClasses.getUT3vhShortNameFrUT2k4(vclass);
    }

    public String toString()
    {
        String tmp="";
        tmp +="      Begin Actor Class=UTVehicleFactory_"+ut3vcl+" Name="+ut3vcl+"_0 Archetype="+ut3vcl+"\'UTGameContent.Default__"+ut3vcl+"\'\n";
        tmp +="         VehicleClass=Class\'"+ut3vclass+"\'\n";
        tmp +="         Tag=\""+ut3vcl+"\"\n";
        tmp += getOtherdata();
        tmp +="         CollisionComponent=CylinderComponent\'CylinderComponent_2\'\n";
        tmp +="         Name=\""+ut3vcl+"_0\"\n";
        tmp +="         ObjectArchetype=UTVehicleFactory_"+ut3vcl+"\'UTGameContent.Default__UTVehicleFactory_"+ut3vcl+"\'\n";
        tmp +="      End Actor\n";
        return tmp;
    }



    public void AnalyseT3DData(String line)
    {
        //    VehicleClass=Class'Onslaught.ONSRV'
        if(line.contains("VehicleClass="))
        {
            this.ut3vclass = VehicleClasses.getUT3VhClassFromUT2k4(line.split("\\'")[1]);
            this.ut3vcl = VehicleClasses.getUT3vhShortNameFrUT2k4(line.split("\\'")[1]);
        }
        parseOtherData(line,deltaz);
    }

}
