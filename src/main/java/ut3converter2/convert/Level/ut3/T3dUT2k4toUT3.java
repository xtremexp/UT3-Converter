 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ut3converter2.convert.Level.ut3;

import java.io.*;
import java.util.ArrayList;
import ut3converter2.T3DConvertor;
import ut3converter2.UTObject;
import ut3converter2.convert.Level.SupportedClasses;
import ut3converter2.convert.MapConverter;
import ut3converter2.convert.Texture.TextureReplacer;
import ut3converter2.convert.terrain.TerrainTiffReader;

/**
 * Convert UT2004 T3D Level file to UT3 T3D Level File
 * The operation consists in converting:
 * -actors
 * -terrain
 * @author Hyperion
 */
public class T3dUT2k4toUT3 extends T3DConvertor {

    final String tskname = "\n*** Conversion of UT2004 T3D Level to UT3 T3D Level ***";
    int uvscalefactor = 1;
    boolean bhasterrain = false;
    ArrayList altlsname = new ArrayList();
    ArrayList<Terrain> alterrains = new ArrayList();
    MapConverter mc = null;
    File ut2004t3dfile = null;
    File outputtmpfile = null;
    BufferedReader bfr;
    BufferedWriter bwr;
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
    PostProcessVolume ppv = null;
    StaticMesh sm = null;
    Terrain ter = null;
    UTPickupFactory utpf = null;
    UTWeaponPickupFactory utw = null;
    UTWeaponLocker utwl = null;
    UTOnsSpecialObj uts = null;
    UTOnsPowerCore utopc = null;
    UTOnsPowerNode utopn = null;
    UTVehicleFactory utv = null;
    UTVMinTurret utvm = null;
    UTVShockTurret utvst = null;
    UT3Ammo ut3a = null;
    WorldInfo wi = null;
    File outputfile;
    File tmpfile;
    String pacname = "";
    TextureReplacer tr;

    public T3dUT2k4toUT3(File ut2004t3dfile, File outputfile, MapConverter mc, String pacname) {
        this.ut2004t3dfile = ut2004t3dfile;
        this.mc = mc;
        this.sc = mc.getSc();
        this.pacname = pacname;
        this.uvscalefactor = mc.getBrushscalefactor();
        this.outputfile = outputfile;
        setTaskname(tskname);
        setBShowLog(true);
    }

    public void setTextureReplacement(TextureReplacer tr) {
        this.tr = tr;
    }

    private void addTerrainDetected(Terrain ter) {
        this.alterrains.add(ter);
    }

    public ArrayList<Terrain> getTerrainsDetected() {
        return this.alterrains;
    }

    public void convert() throws FileNotFoundException, IOException {
        System.out.println("UVScaleFactor: " + uvscalefactor);
        if (isBShowLog()) {
            System.out.println(tskname);
        }
        //String t3dfileformat = FileDecoder.getFileEncodingFormat(ut2004t3dfile, "Begin");
        //bfr = FileDecoder.getBufferedReader(ut2004t3dfile, "Begin");
        bfr = new BufferedReader(new FileReader(ut2004t3dfile));

        tmpfile = File.createTempFile("UT3T3D", "t3d");
        //bwr = new BufferedWriter(new FileWriter(tmpfile));
        bwr = new BufferedWriter(new FileWriter(tmpfile));
        /**
         * Current line of T3D File being analyzed
         */
        int linenumber = 0;
        String line = "";



        bwr.write("Begin Map\n");
        bwr.write("Begin Level NAME=PersistentLevel\n");
        wi = new WorldInfo(mc);

        //bwr.write(wi.toString());

        while ((line = bfr.readLine()) != null) {
            try {
                AnalyzeLine(line);
                linenumber++;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error while parsing data from T3D Level File:");
                System.out.println("-----> " + ut2004t3dfile.getAbsolutePath());
                System.out.println("Line #" + linenumber + " Original Line String:");
                System.out.println("\"" + line + "\"");
                //System.exit(9);
            }

        }

        bwr.write("   End Level\n");
        bwr.write("Begin Surface\n");
        bwr.write("End Surface\n");
        bwr.write("End Map\n");

        bwr.close();
        bfr.close();

        replaceClasses();
        if (isBShowLog()) {
            System.out.println(" ... done!");
        }
    }
    boolean banalyseline = false;
    String curclass = "";

    private void AnalyzeLine(String line) throws IOException {

        if (line.contains("Begin Actor")) {
            curclass = getActorClass(line);
            //Main.d2.gs.addConvActor(curclass);
            if (sc.isSupportedClass(curclass)) {
                //System.out.println(line);
                banalyseline = true;

                if (curclass.equals("Light")) {
                    pl = new PointLight(mc,false);
                } else if (curclass.equals("TerrainInfo")) {
                    if (isBShowLog()) {
                        System.out.println("\n\t**** Terrain Data Analysis ****");
                    }
                    ter = new Terrain(mc, super.getNumterrain());
                    ter.AnalyseT3DData(line);
                    this.setBhasterrain(true);
                } else if (curclass.equals("StaticMeshActor")) {
                    sm = new StaticMesh(mc, tr);
                    sm.analyzeT3Ddata(line);
                } else if (curclass.equals("Mover")) {
                    ia = new InterpActor(mc);
                } else if (curclass.equals("AmbientSound")) {
                    abs = new AmbientSoundSimple(mc);
                    abs.setSoundradiusfactor(4.2F);
                } else if (curclass.equals("BlockingVolume")) {
                    bv = new BlockingVolume(true, bwr);
                    bv.AnalyseT3DData(line);
                }//xWeaponBase
                else if (curclass.equals("xWeaponBase")||curclass.equals("NewWeaponBase")) {
                    utw = new UTWeaponPickupFactory();
                    utw.AnalyseT3DData(line);
                } else if (curclass.equals("WeaponLocker")) {
                    utwl = new UTWeaponLocker();
                    utwl.AnalyseT3DData(line);
                } else if (curclass.contains("PlayerStart")) {
                    ps = new PlayerStart(mc.getPs_mode());
                } else if (curclass.contains("Charger")) {
                    utpf = new UTPickupFactory(curclass);
                } else if (curclass.contains("FlagBase")) {
                    fb = new FlagBase(curclass);
                } else if (curclass.equals("Sunlight")) {
                    dlt = new SkyLight();
                } else if (curclass.equals("MiniHealthPack")) {
                    bwr.write(line.replaceAll("MiniHealthPack", "UTPickupFactory_HealthVial") + "\n");
                } else if (curclass.equals("DECO_ExplodingBarrel")) {
                    gba = new GameBreakableActor();
                } else if (curclass.equals("UseObjective") || curclass.equals("ProximityObjective")) {
                    uts = new UTOnsSpecialObj();
                } else if (curclass.equals("ASVehicleFactory") || VehicleClasses.isVehicleFactory(curclass)) {
                    utv = new UTVehicleFactory(curclass);
                } else if (curclass.equals("ASVehicleFactory_MinigunTurret")) {
                    utvm = new UTVMinTurret();
                } else if (curclass.equals("ONSManualGunPawn")) {
                    utvst = new UTVShockTurret();
                } else if (curclass.equals("ONSPowerCoreBlue") || curclass.equals("ONSPowerCoreRed")) {
                    utopc = new UTOnsPowerCore();
                } else if (curclass.equals("ONSPowerNodeNeutral")) {
                    utopn = new UTOnsPowerNode();
                } else if (curclass.equals("Emitter")) {
                    em = new Emitter();
                } else if (curclass.equals("ZoneInfo")) {
                    if (hf == null) {
                        hf = new HeightFog(true);
                    } else {
                        hf = new HeightFog(false);
                    }
                } else if (curclass.contains("AmmoPickup")) {
                    ut3a = new UT3Ammo();
                    ut3a.AnalyseT3DData(line);
                    ut3a.setAmClassNm(AmmoClasses.getUT3AmmoNmFromUT2k4(curclass).split("\\.")[1]);
                } else if (curclass.equals("WaterVolume") || curclass.equals("LavaVolume")) {
                    ppv = new PostProcessVolume(curclass);
                    bwr.write(line + "\n");
                } else if (curclass.equals("LevelInfo")) {
                    wi = new WorldInfo(mc);
                } else {
                    bwr.write(line + "\n");
                }
            }
        } else if (line.contains("End Actor")) {
            if (banalyseline) {
                if (curclass.equals("Light")) {
                    bwr.write(pl.toString(bwr));
                } else if (curclass.equals("TerrainInfo")) {
                    ter.toString(bwr, new TerrainTiffReader(ter.getHmTiffFile(), pacname));
                    if(ter.isvalidterrain){
                        this.addTerrainDetected(ter);
                        for (int i = 0; i < ter.getAltlsnames().size(); i++) {
                            altlsname.add(ter.getAltlsnames().get(i));
                        }
                        super.incrementsNumTerrain();
                    }
                    //altlsname = ter.getAltlsnames();
                } else if (curclass.equals("StaticMeshActor")) {
                    bwr.write(sm.toString());
                } else if (curclass.equals("Mover")) {
                    bwr.write(ia.toString());
                } else if (curclass.equals("AmbientSound")) {
                    bwr.write(abs.toString());
                } else if (curclass.equals("DECO_ExplodingBarrel")) {
                    bwr.write(gba.toString());
                } else if (curclass.equals("BlockingVolume")) {
                    bwr.write(bv.toString());
                } else if (curclass.equals("xWeaponBase")||curclass.equals("NewWeaponBase")) {
                    bwr.write(utw.toString());
                } else if (curclass.equals("WeaponLocker")) {
                    bwr.write(utwl.toString());
                } else if (curclass.equals("Emitter")) {
                    bwr.write(em.toString());
                } else if (curclass.equals("UseObjective") || curclass.equals("ProximityObjective")) {
                    bwr.write(uts.toString());
                } else if (curclass.equals("ASVehicleFactory") || VehicleClasses.isVehicleFactory(curclass)) {
                    bwr.write(utv.toString());
                } else if (curclass.equals("ASVehicleFactory_MinigunTurret")) {
                    bwr.write(utvm.toString());
                } else if (curclass.equals("ONSManualGunPawn")) {
                    bwr.write(utvst.toString());
                } else if (curclass.contains("AmmoPickup")) {
                    bwr.write(ut3a.toString());
                } else if (curclass.contains("PlayerStart")) {
                    bwr.write(ps.toString());
                } else if (curclass.contains("FlagBase")) {
                    bwr.write(fb.toString());
                } else if (curclass.contains("Charger")) {
                    bwr.write(utpf.toString());
                } else if (curclass.equals("ONSPowerCoreBlue") || curclass.equals("ONSPowerCoreRed")) {
                    bwr.write(utopc.toString());
                } else if (curclass.equals("ONSPowerNodeNeutral")) {
                    bwr.write(utopn.toString());
                } else if (curclass.equals("Sunlight")) {
                    bwr.write(dlt.toString(bwr));
                } else if (curclass.equals("ZoneInfo")) {
                    bwr.write(hf.toString());
                } else if (curclass.equals("WaterVolume") || curclass.equals("LavaVolume")) {
                    bwr.write(line + "\n");
                    bwr.write(ppv.toString());
                } else if (curclass.equals("LevelInfo")) {
                    bwr.write(wi.toString());
                } else {
                    bwr.write(line + "\n");
                }
            }

            banalyseline = false;
        } else {
            if (banalyseline) {
                if (curclass.equals("Brush")) {
                    if (line.contains("TextureU")) {
                        bwr.write(UT3Brush.getNewTextureU(line, this.uvscalefactor) + "\n");
                    } else if (line.contains("TextureV")) {
                        bwr.write(UT3Brush.getNewTextureV(line, this.uvscalefactor) + "\n");
                    } //          Begin Polygon Item=Sheet Texture=AbaddonArchitecture.Grates.grt02GO_AlphaTwoSided Flags=264 Link=0
                    else if (line.contains("Texture=")) {
                        if (line.contains("Flags")) {
                            texname = (line.split("Texture=")[1]).split(" Flags=")[0];
                        } else {
                            texname = (line.split("Texture=")[1]).split(" Link=")[0];
                        }
                        UTObject uto = new UTObject(texname);


                        //texname: AbaddonArchitecture.Grates.grt02GO_AlphaTwoSided
                        //Begin Polygon Texture=DM-1on1-Aerowalk.Walls.wall1shader
                        //Begin Polygon Texture=onAerowalk.Walls.wall1shader
                        if (this.tr.hasTextureReplacement(uto.getGroupAndName().toLowerCase())) {
                            bwr.write(line.replaceAll(texname, tr.getTextureReplacement(uto.getGroupAndName().toLowerCase())) + "\n");
                        } else {
                            bwr.write(line + "\n");
                        }

                    } else if(line.contains("CsgOper")){
                        super.setBrushhasCsgOper(true);
                        bwr.write(line + "\n");
                    } else if(line.contains("Begin Brush")){
                        if(!super.isBrushhasCsgOper()){
                            bwr.write("  CsgOper=CSG_Add\n");
                        }
                        super.setBrushhasCsgOper(false);
                        bwr.write(line + "\n");
                    } else {
                        bwr.write(line + "\n");
                    }
                } else if (curclass.equals("Light")) {
                    pl.AnalyseT3DData(line);
                } else if (curclass.equals("StaticMeshActor")) {
                    sm.analyzeT3Ddata(line);
                } else if (curclass.equals("Mover")) {
                    ia.AnalyseT3DData(line);
                } else if (curclass.equals("AmbientSound")) {
                    abs.AnalyseT3DData(line);
                } else if (curclass.equals("BlockingVolume")) {
                    bv.AnalyseT3DData(line);
                } else if (curclass.contains("PlayerStart")) {
                    ps.AnalyseT3DData(line);
                } else if (curclass.contains("Charger")) {
                    utpf.AnalyseT3DData(line);
                } else if (curclass.contains("FlagBase")) {
                    fb.AnalyseT3DData(line);
                } else if (curclass.equals("MiniHealthPack")) {
                    bwr.write(line.replaceAll("MiniHealthPack", "UTPickupFactory_HealthVial") + "\n");
                } else if (curclass.equals("DECO_ExplodingBarrel")) {
                    gba.AnalyseT3DData(line);
                } else if (curclass.equals("xWeaponBase")||curclass.equals("NewWeaponBase")) {
                    utw.AnalyseT3DData(line);
                } else if (curclass.equals("WeaponLocker")) {
                    utwl.AnalyseT3DData(line);
                } else if (curclass.equals("UseObjective") || curclass.equals("ProximityObjective")) {
                    uts.AnalyseT3DData(line);
                } else if (curclass.equals("Emitter")) {
                    em.AnalyseT3DData(line);
                } else if (curclass.equals("ASVehicleFactory") || VehicleClasses.isVehicleFactory(curclass)) {
                    utv.AnalyseT3DData(line);
                } else if (curclass.equals("ASVehicleFactory_MinigunTurret")) {
                    utvm.AnalyseT3DData(line);
                } else if (curclass.equals("ONSManualGunPawn")) {
                    utvst.AnalyseT3DData(line);
                } else if (curclass.contains("AmmoPickup")) {
                    ut3a.AnalyseT3DData(line);
                } else if (curclass.equals("ONSPowerCoreBlue") || curclass.equals("ONSPowerCoreRed")) {
                    utopc.AnalyseT3DData(line);
                } else if (curclass.equals("ONSPowerNodeNeutral")) {
                    utopn.AnalyseT3DData(line);
                } else if (curclass.equals("TerrainInfo")) {
                    ter.AnalyseT3DData(line);
                } else if (curclass.equals("Sunlight")) {
                    dlt.AnalyseT3DData(line);
                } else if (curclass.equals("ZoneInfo")) {
                    hf.AnalyseT3DData(line);
                } else if (curclass.equals("WaterVolume") || curclass.equals("LavaVolume")) {
                    ppv.analyzeT3DData(line);
                    bwr.write(line + "\n");
                } else if (curclass.equals("LevelInfo")) {
                    wi.AnalyseT3DData(line);
                } else {
                    bwr.write(line + "\n");
                }
            }
        }
    }

    public ArrayList getAltlsname() {
        return altlsname;
    }

    private void replaceClasses() throws FileNotFoundException, IOException {

        //bfr = FileDecoder.getBufferedReader(tmpfile, "Begin");
        bfr = new BufferedReader(new FileReader(tmpfile));
        //bwr = new BufferedWriter(new FileWriter(outputfile));
        bwr = new BufferedWriter(new FileWriter(outputfile));
        int linenumber = 0;
        String line = "";
        if (isBShowLog()) {
            System.out.print("\n*** Updating texture names (texname->" + mc.getDefaultpackage() + ".texname_Mat)");
        }

        while ((line = bfr.readLine()) != null) {
            try {

                /*
                else if(line.contains(mc.getInpututxxmapname()))
                {
                line = line.replaceAll(mc.getInpututxxmapname(), mc.getOutputut3mapname());
                bwr.write(line+"\n");
                }*/
                //Texture=lavaskyX.ground.LavaPanAXX
                //->Texture=MAPNAME.lavaskyX_ground_LavaPanAXX_Mat
                if (line.contains(" Texture=")) {
                    bwr.write(replaceTextureName(line) + "\n");
                } else if (line.contains("LavaVolume")) {
                    line = line.replaceAll("LavaVolume", "UTLavaVolume");
                    bwr.write(line + "\n");
                } else if (line.contains("WaterVolume")) {
                    line = line.replaceAll("WaterVolume", "UTWaterVolume");
                    bwr.write(line + "\n");
                } else if (line.contains("xFallingVolume")) {
                    line = line.replaceAll("xFallingVolume", "UTKillZVolume");
                    bwr.write(line + "\n");
                } //    Brush=Model'Glacier.Model406' -> Brush=Model'Model406'
                else if (line.contains("Brush=Model")) {
                    String model = line.split("\\'")[1];
                    if (model.contains(".")) {
                        model = model.split("\\.")[1];
                    }
                    bwr.write("    Brush=Model'" + model + "'\n");
                } else if (line.equals("")) {
                } else if (line.contains("DefaultPhysicsVolume")) {
                    line = line.replaceAll("DefaultPhysicsVolume", "PhysicsVolume");
                    bwr.write(line + "\n");
                } //else if(line.contains("LevelInfo")||line.contains("Region")||line.contains("ColLocation")||(line.contains("Base")&&line.contains("TerrainInfo")))
                else if (line.contains(".TerrainInfo") || line.contains("=TerrainInfo")) {
                    line = line.replaceAll("TerrainInfo", "Terrain");
                    bwr.write(line + "\n");
                } else if (line.contains("FlyingPathNode")) {
                    line = line.replaceAll("FlyingPathNode", "VolumePathNode");
                    bwr.write(line + "\n");
                } else if (line.contains("HoverPathNode")) {
                    line = line.replaceAll("HoverPathNode", "VolumePathNode");
                    bwr.write(line + "\n");
                } else if (line.contains("RoadPathNode")) {
                    line = line.replaceAll("RoadPathNode", "PathNode");
                    bwr.write(line + "\n");
                } else {
                    bwr.write(line + "\n");
                }
                linenumber++;
            } catch (Exception e) {
                System.out.println("Error while replacing class actor names in T3D Level File:");
                System.out.println("File:" + tmpfile.getAbsolutePath());
                System.out.println("Line #" + linenumber + " Original Line String:");
                System.out.println("\"" + line + "\"");
                e.printStackTrace();
            }

        }
        if (isBShowLog()) {
            System.out.println(" ... done! ***");
        }
        bwr.close();
        bfr.close();
    }

    /**     Base=StaticMeshActor'WAR-Dria.StaticMeshActor469'
    ->     Base=StaticMeshActor'WAR-Dria.StaticMeshActor_469'
     */
    private String updateBase(String linebase) {
        String a = "";
        int numindex = 0;

        for (int i = 0; i < linebase.length(); i++) {
            try {
                Integer.valueOf(String.valueOf(linebase.charAt(i)));
                numindex = i;
                i = linebase.length();
            } catch (Exception e) {
            }
        }

        return linebase.substring(0, numindex) + "_" + linebase.substring(numindex, linebase.length());
    }
    /**
     *
     * @param line
     * @return
     */
    String tmp = "";
    String texname = "";
    String newtexname = "";

    /**
     *    Begin Polygon Texture=DM-1on1-Aerowalk.Walls.wall10shader
     * -> Begin Polygon Texture=onAerowalk.Walls_wall10shader_Mat
     * @param line
     * @return
     */
    private String replaceTextureName(String line) {
        //          Begin Polygon Texture=lavaskyX.ground.LavaPanAXX Object=d

        tmp = "";
        texname = "";
        newtexname = "";

        tmp = line.split("Texture=")[1];
        tmp = tmp.replaceAll(" ", ":");
        tmp = tmp.split(":")[0];

        UTObject uto = new UTObject(tmp);
        texname = tmp;

        newtexname = mc.getDefaultpackage() + "." + uto.getGroupAndName2() + "_Mat";
        return line.replace(texname, newtexname);
    }

    private String getActorClass(String line) {
        return (line.split("=")[1]).split(" ")[0];
    }

    public boolean isBhasterrain() {
        return bhasterrain;
    }

    public void setBhasterrain(boolean bhasterrain) {
        this.bhasterrain = bhasterrain;
    }

    public int getUvscalefactor() {
        return uvscalefactor;
    }

    public void setUvscalefactor(int uvscalefactor) {
        this.uvscalefactor = uvscalefactor;
    }

    public ArrayList<Terrain> getAlterrains() {
        return alterrains;
    }

    
}
