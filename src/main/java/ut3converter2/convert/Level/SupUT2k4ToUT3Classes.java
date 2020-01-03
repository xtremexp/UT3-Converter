/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level;

import java.util.ArrayList;

/**
 *
 * @author Hyperion
 */
public class SupUT2k4ToUT3Classes extends SupportedClasses{


    boolean bhppickups = false;
    boolean bweapons = false;
    boolean bvolumes = false;
    boolean bammopickups = false;
    boolean bvehicles = false;
    boolean bnavpts = false;
    boolean bterrain = false;
    boolean bothers = false;

    public SupUT2k4ToUT3Classes() {
        super(new ArrayList());
        addOthers();
        addTerrain();
        addVehicles();
        addHpPickups();
        addNavPoints();
        addVolumes();
        addWeapons();
        addAmmoPickups();
    }

    public SupUT2k4ToUT3Classes(boolean bweap,boolean bvolumes,boolean bnavpts,boolean bter,boolean bothers,boolean bhppck,boolean bamm) {
        super(new ArrayList());
        if(bweap){addWeapons();}
        if(bvolumes){addVolumes();}
        if(bnavpts){addNavPoints();}
        if(bter){addTerrain();}
        if(bothers){addOthers();}
        if(bhppck){addHpPickups();}
        if(bamm){addVehicles();}
        addAmmoPickups();
    }


    private void addTerrain()
    {
        super.addClass("TerrainInfo");
    }


    private void addOthers()
    {
        super.addClass("Light");
        super.addClass("LevelInfo");
        super.addClass("Emitter");
        super.addClass("ZoneInfo");
        super.addClass("Sunlight");
        super.addClass("StaticMeshActor");
        super.addClass("Mover");
        super.addClass("AmbientSound");
        super.addClass("DECO_ExplodingBarrel");
    }

    private void addHpPickups()
    {
        super.addClass("MiniHealthPack");
        super.addClass("HealthCharger");
        super.addClass("NewHealthCharger");
        super.addClass("SuperHealthCharger");
        super.addClass("NewSuperHealthCharger");
        super.addClass("ShieldCharger");
        super.addClass("SuperShieldCharger");
        super.addClass("UDamageCharger");
    }

    private void addWeapons()
    {
        super.addClass("xWeaponBase");
        super.addClass("NewWeaponBase");
        super.addClass("WeaponLocker");
    }

    private void addVolumes()
    {
        super.addClass("Brush");
        super.addClass("BlockingVolume");
        //super.addClass("PhysicsVolume");
        super.addClass("LavaVolume");
        super.addClass("xFallingVolume");
        super.addClass("WaterVolume");
        super.addClass("LightVolume");
        //super.addClass("DefaultPhysicsVolume");
    }

    private void addAmmoPickups()
    {
        super.addClass("BioAmmoPickup");
        super.addClass("RocketAmmoPickup");
        super.addClass("FlakAmmoPickup");
        super.addClass("LinkAmmoPickup");
        super.addClass("ShockAmmoPickup");
        super.addClass("MinigunAmmoPickup");
        super.addClass("SniperAmmoPickup");
        super.addClass("ClassicSniperAmmoPickup");
        super.addClass("ONSAVRiLAmmoPickup");
    }

    private void addVehicles()
    {
        super.addClass("ASVehicleFactory");
        super.addClass("ASVehicleFactory_Turret");
        super.addClass("ASVehicleFactory_LinkTurret");
        super.addClass("ASVehicleFactory_MinigunTurret");
        super.addClass("ASVehicleFactory_IonCannon");
        //super.addClass("ASVehicleFactory_SentinelFloor");
        //super.addClass("ASVehicleFactory_SentinelCeiling");

        super.addClass("ONSPRVFactory");
        super.addClass("ONSRVFactory");
        super.addClass("ONSHoverCraftFactory");
        super.addClass("ONSAttackCraftFactory");
        super.addClass("ONSTankFactory");
        super.addClass("ONSMASFactory");
        super.addClass("ONSShockTankFactory");
        super.addClass("ONSBomberFactory");
    }

    private void addNavPoints()
    {
        super.addClass("UseObjective");
        super.addClass("ProximityObjective");
        super.addClass("PathNode");
        super.addClass("RoadPathNode");
        super.addClass("FlyingPathNode");
        super.addClass("UTJumpPad");
        super.addClass("PlayerStart");
        super.addClass("Teleporter");
        super.addClass("ONSPowerCoreBlue");
        super.addClass("ONSPowerCoreRed");
        super.addClass("ONSPowerNodeNeutral");
        super.addClass("xBlueFlagBase");
        super.addClass("xRedFlagBase");
    }

}
