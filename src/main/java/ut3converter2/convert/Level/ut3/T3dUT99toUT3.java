/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut3;

import java.io.*;
import java.util.ArrayList;
import ut3converter2.T3DConvertor;
import ut3converter2.convert.Level.SupportedClasses;
import ut3converter2.convert.MapConverter;
import ut3converter2.convert.Texture.TextureReplacer;

/**
 *
 * @author Hyperion
 */
public class T3dUT99toUT3 extends T3DConvertor{

    int uvscalefactor=4;
    final String tskname="\n*** Conversion of UT99 T3D Level to UT3 T3D Level ***";
    
    boolean bhasterrain=false;
    ArrayList altlsname=new ArrayList();
    ArrayList <Terrain>alterrains=new ArrayList();
    MapConverter mc = null;
    File ut99t3dfile = null;
    File outputtmpfile = null;
    BufferedReader bfr;
    BufferedWriter bwr;

    
    Note note;
    SupportedClasses sc;
    AmbientSound as = null;
    AmbientSoundSimple abs = null;
    SkyLight dlt = null;
    BlockingVolume bv = null;
    Emitter em;
    FlagBase fb = null;
    GameBreakableActor gba = null;
    HeightFog hf = null;
    InterpActor ia = null;
    PathNode pn = null;
    PlayerStart ps = null;
    PointLight pl = null;
    PointLightToggeable plt = null;
    StaticMesh sm =null;
    Terrain ter = null;
    UTPickupFactory utpf = null;
    UTWeaponPickupFactory utw = null;
    UTWeaponLocker utwl = null;
    UTOnsSpecialObj uts = null;
    UTVehicleFactory utv = null;
    UTVMinTurret utvm = null;
    UTVShockTurret utvst = null;
    UT3Ammo ut3a = null;
    WorldInfo wi = null;
    File outputfile;
    File tmpfile;
    String pacname="";
    TextureReplacer tr;

    public T3dUT99toUT3(File ut99t3dfile,File outputfile, MapConverter mc){
        this.ut99t3dfile = ut99t3dfile;
        this.mc = mc;
        this.sc = mc.getSc();
        this.pacname = mc.getDefaultpackage();
        this.outputfile = outputfile;
        setTaskname(tskname);
        setBShowLog(true);
    }

    /*
    public T3dUT99toUT3(File ut99t3dfile,File outputfile, MapConverter mc,String pacname){
        this.ut99t3dfile = ut99t3dfile;
        this.mc = mc;
        this.sc = mc.getSc();
        this.pacname = pacname;
        this.uvscalefactor = mc.getBrushscalefactor();
        this.outputfile = outputfile;
        setTaskname(tskname);
        setBShowLog(true);
    }*/

    public void convert() throws FileNotFoundException, IOException, Exception
    {
        if(isBShowLog()){System.out.println(tskname);}

        bfr = new BufferedReader(new FileReader(ut99t3dfile));
        //bfr = FileDecoder.getBufferedReader(ut99t3dfile, "Begin");

        tmpfile = File.createTempFile("UT3T3D", "t3d");
        bwr = new BufferedWriter(new FileWriter(tmpfile));
        //bwr = new BufferedWriter(new FileWriter(outputfile));
        /**
         * Current line of T3D File being analyzed
         */
        int linenumber=0;
        String line="";

        //UT3 T3D Level Header file
        bwr.write("Begin Map\n");
        bwr.write("Begin Level NAME=PersistentLevel\n");
        wi = new WorldInfo(mc);

        //bwr.write(wi.toString());

        while((line=bfr.readLine())!=null)
        {
            try {
                AnalyzeLine(line);
                linenumber ++;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error while parsing data from T3D Level File "+ut99t3dfile.getName());
                System.out.println("Line #"+linenumber+" Original Line String:");
                System.out.println("\""+line+"\"");
                throw new Exception();
            }

        }

        bwr.write("   End Level\n");
        bwr.write("Begin Surface\n");
        bwr.write("End Surface\n");
        bwr.write("End Map\n");

        bwr.close();
        bfr.close();

        replaceClasses();
        if(isBShowLog()){System.out.println(" ... done!");}
    }

    boolean banalyseline=false;
    String curclass="";
    /**
     * Must not copy the first brush or else UT3 editor will crash
     */
    boolean bcopy=false;
    
    private void AnalyzeLine(String line) throws IOException
    {

        if(line.contains("Begin Actor"))
            {
                curclass = getActorClass(line);
                //Main.d2.gs.addConvActor(curclass);
                banalyseline=true; // XX TODO DEL??
                
                if(sc.isSupportedClass(curclass))
                {
                    //System.out.println(line);
                    banalyseline=true;

                    if(curclass.equals("Light"))
                    {
                        //@TODO MAKE UT3 PACKAGE WITH NEW LENSFLARE NOT THE CRAPPY ONES BY DEFAULT
                        pl = new PointLight(mc,false);
                    }
                    else if(curclass.equals("TriggerLight"))
                    {
                        plt = new PointLightToggeable(true);
                    }
                    else if(curclass.equals("Mover"))
                    {
                        ia = new InterpActor(mc);
                    }
                    else if(curclass.equals("AmbientSound"))
                    {
                        //abs = new AmbientSoundSimple(mc,28);
                        abs = new AmbientSoundSimple(mc);
                        abs.setBuseut99sndname(true);
                    }
                    else if(curclass.equals("BlockingVolume"))
                    {
                        bv = new BlockingVolume(true, bwr);
                        bv.AnalyseT3DData(line);
                    }//xWeaponBase
                    else if(curclass.equals("xWeaponBase")||curclass.equals("minigun2")||curclass.equals("SniperRifle")
                            ||curclass.equals("ut_biorifle")||curclass.equals("WarheadLauncher")||
                            curclass.equals("ripper")||curclass.equals("ShockRifle")||curclass.equals("UT_Eightball")
                            || curclass.equals("UT_FlakCannon"))
                    {
                        utw = new UTWeaponPickupFactory(WeaponsClasses.getUT3WpClassFromUT99(curclass));
                        utw.AnalyseT3DData(line);
                    }
                    else if(curclass.equals("WeaponLocker"))
                    {
                        utwl = new UTWeaponLocker();
                        utwl.AnalyseT3DData(line);
                    }
                    else if(curclass.contains("PlayerStart"))
                    {
                        ps = new PlayerStart(PlayerStart.mode_playerstart);
                    }
                    else if(curclass.contains("Charger"))
                    {
                        utpf = new UTPickupFactory(curclass);
                    }
                    else if(curclass.contains("FlagBase"))
                    {
                        fb = new FlagBase(curclass);
                    }
                    else if(curclass.equals("Sunlight"))
                    {
                        dlt = new SkyLight();
                    }
                    else if(curclass.equals("MiniHealthPack"))
                    {
                        bwr.write(line.replaceAll("MiniHealthPack", "UTPickupFactory_HealthVial")+"\n");
                    }
                    else if(curclass.equals("UT_ShieldBelt")){
                        bwr.write(line.replaceAll("UT_ShieldBelt", "UTArmorPickup_ShieldBelt")+"\n");
                    }
                    else if(curclass.equals("HealthPack")){
                        bwr.write(line.replaceAll("HealthPack", "UTPickupFactory_SuperHealth")+"\n");
                    }
                    
                    else if(curclass.equals("MedBox"))
                    {
                        bwr.write(line.replaceAll("MedBox", "UTPickupFactory_MediumHealth")+"\n");
                    }
                    else if(curclass.equals("DefensePoint"))
                    {
                        bwr.write(line.replaceAll("DefensePoint", "UTDefensePoint")+"\n");
                    }
                    else if(curclass.equals("HealthVial"))
                    {
                        bwr.write(line.replaceAll("HealthVial", "UTPickupFactory_HealthVial")+"\n");
                    }
                    else if(curclass.equals("DECO_ExplodingBarrel"))
                    {
                        gba = new GameBreakableActor();
                    }
                    else if(curclass.equals("UseObjective")||curclass.equals("ProximityObjective"))
                    {
                        uts = new UTOnsSpecialObj();
                    }
                    else if(curclass.equals("Emitter"))
                    {
                        em = new Emitter();
                    }
                    else if(curclass.equals("LevelInfo"))
                    {
                        wi = new WorldInfo(mc);
                    }
                    else if(curclass.equals("ZoneInfo"))
                    {
                        if(hf==null)
                        {
                            hf = new HeightFog(true);
                        }
                        else
                        {
                            hf = new HeightFog(false);
                        }
                    }
                    else if(curclass.contains("AmmoPickup")||curclass.contains("BulletBox")||curclass.contains("RocketPack")
                            ||curclass.contains("BladeHopper")||curclass.contains("bioammo")||curclass.contains("ShockCore")
                            ||curclass.contains("PAmmo") || curclass.contains("FlakAmmo"))
                    {
                        ut3a = new UT3Ammo();
                        ut3a.AnalyseT3DData(line);
                        ut3a.setAmClassNm(AmmoClasses.getUT3AmmoNmFromUT99(curclass).split("\\.")[1]);
                    }
                    else if(curclass.equals("Brush")&&(!bcopy))
                    {
                        banalyseline = false;
                    }
                    else
                    {
                        bwr.write(line+"\n");
                    }
                } else {
                    note = new Note(curclass);
                }
            }
            else if(line.contains("End Actor"))
            {
                
                if(banalyseline)
                {
                    if(curclass.equals("Light"))
                    {
                        bwr.write(pl.toString(bwr));
                    }
                    else if(curclass.equals("TriggerLight"))
                    {
                        bwr.write(plt.toString(bwr));
                    }
                    else if(curclass.equals("Mover"))
                    {
                        bwr.write(ia.toString());
                    }
                    else if(curclass.equals("AmbientSound"))
                    {
                        bwr.write(abs.toString());
                    }
                    else if(curclass.equals("DECO_ExplodingBarrel"))
                    {
                        bwr.write(gba.toString());
                    }
                    else if(curclass.equals("BlockingVolume"))
                    {
                        bwr.write(bv.toString());
                    }
                    else if(curclass.equals("xWeaponBase")||curclass.equals("minigun2")||curclass.equals("SniperRifle")
                            ||curclass.equals("ut_biorifle")||curclass.equals("WarheadLauncher")||
                            curclass.equals("ShockRifle")||curclass.equals("ripper")||curclass.equals("UT_Eightball")
                            || curclass.equals("UT_FlakCannon"))
                    {
                        bwr.write(utw.toString());
                    }
                    else if(curclass.equals("WeaponLocker"))
                    {
                        bwr.write(utwl.toString());
                    }
                    else if(curclass.equals("Emitter"))
                    {
                        bwr.write(em.toString());
                    }
                    else if(curclass.equals("UseObjective")||curclass.equals("ProximityObjective"))
                    {
                        bwr.write(uts.toString());
                    }
                    else if(curclass.contains("AmmoPickup")||curclass.contains("BulletBox")||curclass.contains("RocketPack")
                            ||curclass.contains("BladeHopper")||curclass.contains("bioammo")||curclass.contains("ShockCore")
                            ||curclass.contains("PAmmo") || curclass.contains("FlakAmmo"))
                    {
                        bwr.write(ut3a.toString());
                    }
                    else if(curclass.contains("PlayerStart"))
                    {
                        bwr.write(ps.toString());
                    }
                    else if(curclass.contains("FlagBase"))
                    {
                        bwr.write(fb.toString());
                    }
                    else if(curclass.contains("Charger"))
                    {
                        bwr.write(utpf.toString());
                    }
                    else if(curclass.equals("Sunlight"))
                    {
                        bwr.write(dlt.toString(bwr));
                    }
                    else if(curclass.equals("ZoneInfo"))
                    {
                        bwr.write(hf.toString());
                    }
                    else if(curclass.equals("LevelInfo"))
                    {
                        bwr.write(wi.toString());
                    }
                    else if(!sc.isSupportedClass(curclass)){
                        bwr.write(note.toString());
                    }
                    else
                    {
                        bwr.write(line+"\n");
                    }
                }
                if(curclass.equals("Brush"))
                {
                    bcopy = true;
                }
                banalyseline=false;
            }
            else
            {
                if(banalyseline)
                {
                    if(curclass.equals("Brush")||curclass.equals("LightVolume"))
                    {
                        if(line.contains("TextureU"))
                        {
                            bwr.write(UT3Brush.getNewTextureU(line, this.uvscalefactor)+"\n");
                        }
                        else if(line.contains("TextureV"))
                        {
                            bwr.write(UT3Brush.getNewTextureV(line, this.uvscalefactor)+"\n");
                        }
                        else if(line.contains("TempScale"))
                        {

                        }
                        else
                        {
                            bwr.write(line+"\n");
                        }
                    }
                    else if(curclass.equals("Light"))
                    {
                        pl.AnalyseT3DData(line);
                    }
                    else if(curclass.equals("TriggerLight"))
                    {
                        plt.AnalyseT3DData(line);
                    }
                    else if(curclass.equals("Mover"))
                    {
                        ia.AnalyseT3DData(line);
                    }
                    else if(curclass.equals("AmbientSound"))
                    {
                        abs.AnalyseT3DData(line);
                    }
                    else if(curclass.equals("BlockingVolume"))
                    {
                        bv.AnalyseT3DData(line);
                    }
                    else if(curclass.contains("PlayerStart"))
                    {
                        ps.AnalyseT3DData(line);
                    }
                    else if(curclass.contains("Charger"))
                    {
                       utpf.AnalyseT3DData(line);
                    }
                    else if(curclass.contains("FlagBase"))
                    {
                        fb.AnalyseT3DData(line);
                    }
                    else if(curclass.equals("MiniHealthPack"))
                    {
                        bwr.write(line.replaceAll("MiniHealthPack", "UTPickupFactory_HealthVial")+"\n");
                    }
                    else if(curclass.equals("HealthVial"))
                    {
                        bwr.write(line.replaceAll("HealthVial", "UTPickupFactory_HealthVial")+"\n");
                    }
                    else if(curclass.equals("MedBox"))
                    {
                        bwr.write(line.replaceAll("MedBox", "UTPickupFactory_MediumHealth")+"\n");
                    }
                    else if(curclass.equals("DECO_ExplodingBarrel"))
                    {
                        gba.AnalyseT3DData(line);
                    }
                    else if(curclass.equals("xWeaponBase")||curclass.equals("minigun2")||curclass.equals("SniperRifle")
                            ||curclass.equals("ut_biorifle")||curclass.equals("WarheadLauncher")||curclass.equals("ShockRifle")
                            ||curclass.equals("ripper")||curclass.equals("UT_Eightball")
                            || curclass.equals("UT_FlakCannon"))
                    {
                        utw.AnalyseT3DData(line);
                    }
                    else if(curclass.equals("WeaponLocker"))
                    {
                        utwl.AnalyseT3DData(line);
                    }
                    else if(curclass.equals("UseObjective")||curclass.equals("ProximityObjective"))
                    {
                        uts.AnalyseT3DData(line);
                    }
                    else if(curclass.equals("Emitter"))
                    {
                        em.AnalyseT3DData(line);
                    }
                    else if(curclass.contains("AmmoPickup")||curclass.equals("BulletBox")||curclass.equals("RocketPack")
                            ||curclass.equals("BladeHopper")||curclass.equals("bioammo")||curclass.equals("ShockCore")
                            ||curclass.equals("PAmmo") || curclass.contains("FlakAmmo"))
                    {
                        ut3a.AnalyseT3DData(line);
                    }
                    else if(curclass.equals("TerrainInfo"))
                    {
                        ter.AnalyseT3DData(line);
                    }
                    else if(curclass.equals("Sunlight"))
                    {
                        dlt.AnalyseT3DData(line);
                    }
                    else if(curclass.equals("ZoneInfo"))
                    {
                        hf.AnalyseT3DData(line);
                    }
                    else if(curclass.equals("LevelInfo"))
                    {
                        wi.AnalyseT3DData(line);
                    }
                    else if(!sc.isSupportedClass(curclass)){
                        note.parseOtherData(line);
                    }
                    else
                    {
                        bwr.write(line+"\n");
                    }
                }
            }
    }


     private void replaceClasses() throws FileNotFoundException, IOException
    {

         bfr = new BufferedReader(new FileReader(tmpfile));
        //bfr = FileDecoder.getBufferedReader(tmpfile, "Begin");
        //bwr = new BufferedWriter(new FileWriter(outputfile));
        bwr = new BufferedWriter(new FileWriter(outputfile));
        int linenumber=0;
        String line="";
        if(isBShowLog()){
            System.out.print("\n*** Updating texture names (texname->"+mc.getDefaultpackage()+".texname_Mat)");
        }

        while((line=bfr.readLine())!=null)
        {
            try {
                /*if(line.contains("LevelInfo")||line.contains("Paths")||line.contains("ColLocation")||(line.contains("Base="))||
                    (line.contains("PhysicsVolume"))||(line.contains("nextNavigationPoint"))||(line.contains("CollisionRadius"))
                    ||(line.contains("ForcedPaths"))||(line.contains("CollisionHeight"))||line.contains("LiftTag=")
                    ||line.contains("OldLocation")||line.contains("InventorySpot")||line.contains("previousPath"))
                *///{
                    //line = line.replaceAll("LevelInfo", "WorldInfo_"); super.addClass("Teleporter");
                    //bwr.write(line+"\n");
                //}

                /*else if(line.contains("Region=")||(line.contains("MainScale"))||(line.contains("PostScale")))
                {

                }*/
                /*
                else if(line.contains(mc.getInpututxxmapname()))
                {
                     line = line.replaceAll(mc.getInpututxxmapname(), mc.getOutputut3mapname());
                     bwr.write(line+"\n");
                }*/
                //Texture=lavaskyX.ground.LavaPanAXX
                //->Texture=MAPNAME.lavaskyX_ground_LavaPanAXX_Mat
                if(line.contains(" Texture="))
                {
                    bwr.write(replaceTextureName(line)+"\n");
                }
                else if(line.contains("Brush=Model"))
                {
                    String model = line.split("\\'")[1];
                    if(model.contains("."))
                    {
                        model = model.split("\\.")[1];
                    }
                    bwr.write("    Brush=Model'"+model+"'\n");
                }
                else if(line.equals("")){}
                else if(line.contains("DefaultPhysicsVolume"))
                {
                    line = line.replaceAll("DefaultPhysicsVolume", "PhysicsVolume");
                    bwr.write(line+"\n");
                }
                //else if(line.contains("LevelInfo")||line.contains("Region")||line.contains("ColLocation")||(line.contains("Base")&&line.contains("TerrainInfo")))
                else if(line.contains(".TerrainInfo")||line.contains("=TerrainInfo"))
                {
                    line = line.replaceAll("TerrainInfo", "Terrain");
                    bwr.write(line+"\n");
                }
                else
                {
                    bwr.write(line+"\n");
                }
                linenumber ++;
            } catch (Exception e) {
                System.out.println("Error while replacing class actor names in T3D Level File:");
                System.out.println("File:"+tmpfile.getAbsolutePath());
                System.out.println("Line #"+linenumber+" Original Line String:");
                System.out.println("\""+line+"\"");
                e.printStackTrace();
            }

        }
        if(isBShowLog()){System.out.println(" ... done! ***");}
        bwr.close();
        bfr.close();
    }

          /**
      *
      * @param line
      * @return
      */
    String tmp="";
    String texname="";
    String newtexname="";
    private  String replaceTextureName(String line)
    {
        //          Begin Polygon Texture=lavaskyX.ground.LavaPanAXX Object=d
        tmp="";
        texname="";
        newtexname="";

        tmp = line.split("Texture=")[1];

        tmp = tmp.replaceAll(" ", ":");
        tmp = tmp.split(":")[0];
        texname = tmp;
        String a[]=texname.split("\\.");
        if(a.length==2)
        {
            tmp=a[1];
        }
        else if(a.length==3)
        {
            tmp=a[1]+"."+a[2];
        }


        newtexname = mc.getDefaultpackage()+"."+tmp.replaceAll("\\.","\\_")+"_Mat";
        //newtexname = tmp.replaceAll("\\.","_")+"_Mat";
        return line.replace(texname, newtexname);
    }
    
    private String getActorClass(String line)
    {
        return (line.split("=")[1]).split(" ")[0];
    }
    
}
