/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import ut3converter2.convert.Level.SupportedClasses;
import ut3converter2.convert.Level.ut3.*;
import ut3converter2.convert.MapConverter;
import ut3converter2.convert.Texture.TextureReplacer;
import ut3converter2.tools.ClassAnalyzer;

/**
 *
 * @author Hyperion
 */
public class T3DConvertor {


    ClassAnalyzer ca;
    SupportedClasses sc;

    //Generic actors
    AmbientSound as = null;
    AmbientSoundSimple abs = null;
    PlayerStart ps = null;
    PointLight pl = null;
    InterpActor ia = null;
    PathNode pn = null;


    SkyLight dlt = null;
    BlockingVolume bv = null;
    Emitter em;
    FlagBase fb = null;
    GameBreakableActor gba = null;
    HeightFog hf = null;
    
    
    
    PostProcessVolume ppv = null;
    
    UTPickupFactory utpf = null;
    UTWeaponPickupFactory utw = null;
    UTWeaponLocker utwl = null;

    // UT2004
    StaticMesh sm = null;
    Terrain ter = null;
    UTOnsSpecialObj uts = null;
    UTOnsPowerCore utopc = null;
    UTOnsPowerNode utopn = null;
    UTVehicleFactory utv = null;
    UTVMinTurret utvm = null;
    UTVShockTurret utvst = null;
    UT3Ammo ut3a = null;
    WorldInfo wi = null;
    /**
     * T3D file that will be converted
     */
    File t3din;
    BufferedReader bfr;

    /**
     * T3D file converted
     */
    File t3dout;
    BufferedWriter bwr;

    MapConverter mc;
    public boolean bhassuceeded=true;

    /**
     * If its true then the following tasks shouldn't be processed.
     * Particulary true when using LevelExporter for example.
     */
    boolean biscritical=false;
    boolean bDebugMode=false;
    boolean bShowLog=true;
    boolean bhasterrain = false;
    /**
     * Display pop-up window at the end of this task showing if task has suceeded or not
     */
    boolean bDispResult=false;
    String taskname="Task";
    int numtottasks=1;
    int curtasknum=0;
    long tasklenghtmsec;

    String curclass;
    int numterrain=0;

    TextureReplacer tr;
    boolean brushhasCsgOper=false;

    public T3DConvertor(File t3din, File t3dout, MapConverter mc){
        this.mc = mc;
        this.t3din = t3din;
        this.t3dout = t3dout;
    }
    
    public T3DConvertor() {
    }

    public boolean Convert(){
        File t3dtmpfile;
        int linenumber = 0;
        String line;
        
        System.out.println("\n*** Conversion of "+UTGames.getUTGameShort(mc.getInput_utgame())
                +" T3D Level to "+UTGames.getUTGameShort(mc.getOutput_utgame())+" T3D Level ***");

        if(t3din==null||(t3din!=null&&!t3din.exists())){
            System.out.println(" Error! - T3D input file does not exists!");
            return false;
        }
        
        try {
            t3dtmpfile = File.createTempFile("UT3T3D", "t3d");

            bfr = new BufferedReader(new FileReader(t3din));
            bwr = new BufferedWriter(new FileWriter(t3dout));

            writeUT3T3DHeader();

            while ((line = bfr.readLine()) != null) {
                AnalyzeLine(line);
                linenumber++;
            }
            writeUT3T3DHeader();
            
        } catch (Exception e) {
            return false;
        } finally {
            try {
                bwr.close();
                bfr.close();
            } catch (IOException ex) {
                Logger.getLogger(T3DConvertor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return true;
    }
    
    public boolean isBiscritical() {
        return biscritical;
    }

    public void setBiscritical(boolean biscritical) {
        this.biscritical = biscritical;
    }


    public boolean isBShowLog() {
        return bShowLog;
    }

    public void setBShowLog(boolean bShowLog) {
        this.bShowLog = bShowLog;
    }

    public int getCurtasknum() {
        return curtasknum;
    }

    public void setCurtasknum(int curtasknum) {
        this.curtasknum = curtasknum;
    }

    public int getNumtottasks() {
        return numtottasks;
    }

    public void setNumtottasks(int numtottasks) {
        this.numtottasks = numtottasks;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public boolean isBDispResult() {
        return bDispResult;
    }

    public void setBDispResult(boolean bDispResult) {
        this.bDispResult = bDispResult;
    }

    public long getTasklenghtmsec() {
        return tasklenghtmsec;
    }

    public void setTasklenghtmsec(long tasklenghtmsec) {
        this.tasklenghtmsec = tasklenghtmsec;
    }

    public boolean isBhassuceeded() {
        return bhassuceeded;
    }

    public void setBhassuceeded(boolean bhassuceeded) {
        this.bhassuceeded = bhassuceeded;
    }

    public int getNumterrain() {
        return numterrain;
    }

    public void setNumterrain(int numterrain) {
        this.numterrain = numterrain;
    }

    public void incrementsNumTerrain(){
        this.numterrain ++;
    }

    /**
     * Is CsgOper not set Add or Substract will cause import fail into UT3 editor!!!!
     * @return
     */
    public boolean isBrushhasCsgOper() {
        return brushhasCsgOper;
    }

    public void setBrushhasCsgOper(boolean brushhasCsgOper) {
        this.brushhasCsgOper = brushhasCsgOper;
    }

    private String getActorClass(String line) {
        return (line.split("=")[1]).split(" ")[0];
    }
    
    private void writeUT3T3DHeader() throws IOException{
        bwr.write("Begin Map\n");
        bwr.write("\tBegin Level NAME=PersistentLevel\n");
    }

    private void writeUT3T3DFooter() throws IOException{
        bwr.write("\tEnd Level\n");
        bwr.write("\tBegin Surface\n");
        bwr.write("\tEnd Surface\n");
        bwr.write("End Map\n");
    }

    boolean banalyseline = false;

    private void AnalyzeLine(String line) throws IOException {
        if (line.contains("Begin Actor")) {
            curclass = ca.getRootKnownConvertActor(getActorClass(line), this.mc.getInput_utgame());

            if (mc.getSc().isSupportedClass(curclass)) {
                banalyseline = true;
                InitializeActor(line);
            }
        }
        else if (line.contains("End Actor")) {
            if(banalyseline){WriteCurrentActor();}
            banalyseline = false;
        } 
        else if(banalyseline) {
            AnalyzeCurrentActor();
        }
    }

    private void AnalyzeCurrentActor() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void WriteCurrentActor() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void InitializeActor(String line) throws IOException {
        curclass = ca.getRootKnownConvertActor(getActorClass(line), this.mc.getInput_utgame());

            if (sc.isSupportedClass(curclass)) {
                banalyseline = true;
                if (curclass.equals("Light")) {
                    pl = new PointLight(mc, false);
                } else if (curclass.equals("TerrainInfo")) {
                    if (isBShowLog()) {
                        System.out.println("Terrain found! Parsing data ...");
                    }
                    ter = new Terrain(mc, numterrain);
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
                else if (curclass.equals("xWeaponBase")) {
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
                } else if (curclass.equals("Sunlight")||curclass.equals("SunLight")) {
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
                } else if (curclass.equals("ZoneInfo")||curclass.equals("U2ZoneInfo")) {
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
    }

    public boolean isBhasterrain() {
        return bhasterrain;
    }

    public void setBhasterrain(boolean bhasterrain) {
        this.bhasterrain = bhasterrain;
    }


    
    
}
