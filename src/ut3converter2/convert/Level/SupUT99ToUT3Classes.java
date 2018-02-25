/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level;

import java.util.ArrayList;

/**
 * Set allowed Classes to be converted from UT99 to UT3.
 * This acts as a filter so prevents error or crash while importing t3d converted file into UT3 Editor
 * @author Hyperion
 */
public class SupUT99ToUT3Classes extends SupportedClasses{

    public SupUT99ToUT3Classes() {
        super(new ArrayList());
        addOthers();
        addHpPickups();
        addNavPoints();
        addVolumes();
        addWeapons();
        addAmmoPickups();
    }

    public SupUT99ToUT3Classes(ArrayList alsupclasses) {
        super(alsupclasses);
    }




    private void addOthers()
    {
        //Decoration
        super.addClass("Plant1");
        super.addClass("Plant2");
        super.addClass("Plant3");
        super.addClass("Plant4");
        super.addClass("Plant5");
        super.addClass("Plant6");
        super.addClass("Plant7");
        super.addClass("Tree1");
        super.addClass("Tree2");
        super.addClass("Tree3");
        super.addClass("Tree4");


        super.addClass("Light");
        //super.addClass("Emitter");
        super.addClass("ZoneInfo");
        super.addClass("WaterZone");
        super.addClass("LavaZone");
        super.addClass("Sunlight");
        super.addClass("StaticMeshActor");
        super.addClass("Mover");
        super.addClass("Trigger");
        super.addClass("TriggerLight");
        super.addClass("AmbientSound");
        super.addClass("AmbientSoundSimple");
        super.addClass("ut_jumpboots");
        super.addClass("ThighPads");
        super.addClass("BlockAll");
        super.addClass("Armor");
        super.addClass("UT_invisibility");
        super.addClass("UT_ShieldBelt");
        super.addClass("UT_Eightball");
        super.addClass("UDamage");
        super.addClass("FlagBase");
        //After using UMC
        super.addClass("UTPickupFactory_JumpBoots");
        super.addClass("UTPickupFactory_Invisibility");
        super.addClass("UTPickupFactory_UDamage");
        super.addClass("UTArmorPickup_ShieldBelt");
        //UTArmorPickup_ShieldBelt
    }

    private void addHpPickups()
    {
        super.addClass("MiniHealthPack");
        super.addClass("HealthCharger");
        super.addClass("HealthPack");
        super.addClass("NewHealthCharger");
        super.addClass("SuperHealthCharger");
        super.addClass("NewSuperHealthCharger");
        super.addClass("ShieldCharger");
        super.addClass("SuperShieldCharger");
        super.addClass("UDamageCharger");
        super.addClass("HealthVial");
        super.addClass("MedBox");
    }

    private void addWeapons()
    {
        super.addClass("xWeaponBase");
        super.addClass("WeaponLocker");
        super.addClass("ShockRifle");
        super.addClass("WarheadLauncher");
        super.addClass("ut_biorifle");
        super.addClass("UT_FlakCannon");
        super.addClass("SniperRifle");
        super.addClass("minigun2");
        super.addClass("ripper");
    }

    private void addVolumes()
    {
        super.addClass("Brush");
        super.addClass("BlockingVolume");
        super.addClass("PhysicsVolume");
        super.addClass("LavaVolume");
        super.addClass("xFallingVolume");
        super.addClass("WaterVolume");
        super.addClass("LightVolume"); //For UT3 Mover Conversion
        super.addClass("Volume"); //For UT3 Mover Conversion
        //super.addClass("DefaultPhysicsVolume");
        super.addClass("UTWaterVolume");
        super.addClass("UTLavaVolume");
        super.addClass("UTSlimeVolume");
        super.addClass("UTSpaceVolume");
        super.addClass("UTKillZVolume");
    }

    private void addAmmoPickups()
    {
        super.addClass("BulletBox");
        super.addClass("RocketPack");
        super.addClass("BladeHopper");
        super.addClass("bioammo");
        super.addClass("FlakAmmo");
        super.addClass("ShockCore");
        super.addClass("PAmmo"); 
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
        super.addClass("LiftExit");
        super.addClass("LiftCenter");
        super.addClass("DefensePoint");
    }

    
}
