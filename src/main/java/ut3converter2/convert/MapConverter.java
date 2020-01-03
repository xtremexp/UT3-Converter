/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert;

import java.awt.TrayIcon.MessageType;
import ut3converter2.*;
import java.io.File;
import java.util.ArrayList;
import ut3converter2.convert.Level.SupU2ToUT3Classes;
import ut3converter2.convert.Level.SupUT2k4ToUT3Classes;
import ut3converter2.convert.Level.SupUT99ToUT3Classes;
import ut3converter2.convert.Level.SupportedClasses;
import ut3converter2.convert.Level.ut2004.T3dU1UT99ToUT2k4;
import ut3converter2.convert.Level.ut3.PlayerStart;
import ut3converter2.convert.Level.ut3.T3dU2toUT3;
import ut3converter2.convert.Level.ut3.T3dUT2k4toUT3;
import ut3converter2.convert.Level.ut3.T3dUT99toUT3;
import ut3converter2.convert.Level.ut3.Terrain;
import ut3converter2.convert.Sound.SoundConverter;
import ut3converter2.convert.StaticMesh.SMT3DtoAse;
import ut3converter2.convert.Texture.TextureConverter;
import ut3converter2.convert.Texture.TextureReplacer;
import ut3converter2.errors.T3DLevelExportException;
import ut3converter2.export.LevelExporter;
import ut3converter2.export.StaticMeshesExporter;
import ut3converter2.export.SoundExporter;
import ut3converter2.export.T3DLevelToUTFiles;
import ut3converter2.export.TextureExporter;
import ut3converter2.export.UCExporter;
import ut3converter2.export.UTPackageTexConfig;
import ut3converter2.export.UTPackageTexExtractor;
import ut3converter2.identification.MusicIdentificator;
import ut3converter2.identification.SoundsIdentificator;
import ut3converter2.identification.StaticMeshesIdentificator;
import ut3converter2.identification.TexturesIdentificator;
import ut3converter2.ihm.map.GlobalInstructions;
import ut3converter2.tools.FileCleaner;
import ut3converter2.tools.FileMover;
import ut3converter2.tools.FileRenamer;
import ut3converter2.tools.T3DBrushTranformPerm;
import ut3converter2.tools.T3DLevelNameChanger;
import ut3converter2.tools.T3DLevelScaler;
import ut3converter2.tools.T3DLvlMoverPort;
import ut3converter2.tools.T3DUT99ASMaker;
import ut3converter2.tools.T3DUVTexUpdater;
import ut3converter2.tools.umc.UMCReader;

/**
 *
 * @author Hyperion
 */
public class MapConverter extends GlobalConverter implements Runnable {

    /**
     * Default staticmesh used when converting mover brush actor to InterpActor
     * as Brush->Ase conversion not yet ready.
     * UN_SimpleMeshes.TexPropCube_Dup
     * VH_Goliath.Mesh.S_VH_Goliath_Tankshell
     */
    public static final String defaultsm="NodeBuddies.3D_Icons.NodeBuddy__BASE_SHORT";
    //         U1
    public static final Object[] defumc_u1ut2k4 = new Object[]{true,new String[]{"U1ToUT2k4-Default.umc","U1ToUT2k4-Coop.umc"},"U1ToUT2k4-Default.umc"};
    public static final String defumcfile_u1ut2k4="U1-Default.umc";
    public static final String defpacnamedat_u1ut2k4="U1TexPackInfo.txt";

    public static final Object[] defumc_u1ut3=new Object[]{true,new String[]{"UT99ToUT3-Default.umc"},"UT99ToUT3-Default.umc"};
    public static final String defumcfile_u1ut3="UT99ToUT3-Default.umc";
    public static final String defpacnamedat_u1ut3="U1TexPackInfo.txt";

    //         UT99
    public static final Object[] defumc_ut99ut3=new Object[]{true,new String[]{"UT99ToUT3-Default.umc"},"UT99ToUT3-Default.umc"};
    public static final String defumcfile_ut99ut3="UT99ToUT3-Default.umc";
    public static final String defpacnamedat_ut99ut3="UT99TexPackInfo.txt";

    public static final Object[] defumc_ut99ut2k4=new Object[]{true,new String[]{"UT99ToUT2k4-Default.umc","UT99ToUT2k4-Coop.umc"},"UT99ToUT2k4-Default.umc"};
    public static final String defpacnamedat_ut99ut2k4="UT99TexPackInfo.txt";
    public static String defumcfile_ut99ut2k4="UT2004-Default.umc";

    //        DEUS EX
    public static final Object[] defumc_deut2k4=new Object[]{true,new String[]{"UT2004-Default.umc","UT2004-CoopDeuxEx.umc"},"UT2004-Default.umc"};
    public static final String defpacnamedat_deut2k4="DeusExTexPackInfo.txt";
    
    //        UT2004
    public static final Object[] defumc_ut2k4ut3=new Object[]{false,new String[]{"UT2k4ToUT3-Default.umc"},"UT2k4ToUT3-Default.umc"};


    //        U2
    public static final Object[] defumc_u2ut3=new Object[]{false,new String[]{"UT2k4ToUT3-Default.umc"},"UT2k4ToUT3-Default.umc"};

    /**
     * Folder where immages are for instructions
     */
    public final static String IMAGE_FOLDER = "/images";

    public static final String defscalefac_u2ut2k4_ut3="1X";
    public static final String defscalefac_u1ut99_ut2004="1.25X";
    public static final String defscalefac_u1ut99_ut3="1.25X";

    public boolean canChangeUMC=true;
    public boolean canChangeScale=true;
    public boolean canChangePackage=true;
    /**
     * Allows to analyze texture package to guess package belongs from texname
     */
    public boolean canAddPackagetoAnalyze=true;

    public boolean delt3dfilesafterconvert=true;
    /**
     * "UT99TexPackInfo.txt"
     */
    String defpacnamedat;
    /**
     * new Object[]{true,new String[]{"UT99ToUT3-Default.umc"},"UT99ToUT3-Default.umc"};
     */
    Object[] defumc;
    String defumcfile;

    private static final int UTPKG_GRP_NAME_SYNTAXEXP = 0;
    private static final int UTPKG_NAME_SYNTAXEXP = 1;

    int syntax_uaxexp = UTPKG_GRP_NAME_SYNTAXEXP;
    int syntax_utxexp = UTPKG_GRP_NAME_SYNTAXEXP;
    int syntax_moverst3d = UTPKG_GRP_NAME_SYNTAXEXP;
    
    public String defscalefac="1X";
    public double scalefactor=1D;
    /**
     * Used when creating temporary file to transform permanently T3D Level
     */
    public final String tmpfile_transformed="T3DLvlTransformed";
    public final String tmpfile_moverconv="T3DLvlMoversConverted";
    public final String tmpfile_scaled="T3DLvlScaled";
    public final String tmpfile_lvlnamechanged="T3DLvlNameChanged";
    public final String tmpfile_umcparsed="T3DLvlUMCParsed";
    public final String tmpfile_texuvupdated="T3DLvlUVTexUpdated";
    /**
     * When it auto-created AmbientSound actors for any actor with AmbientSound field
     */
    public final String tmpfile_asadd="T3DLvlAmbientSoundsAdd";
    public final String tmpfile_ut3="UT3T3DLevel";
    String defaultpackage="Map";
    SupportedClasses sc;
    private File finalt3dfile;
    private String texfolderstr="UT-Textures";
    private String smfolderstr="UT-StaticMeshes";
    private String sndfolderstr="UT-Sounds";
    private String terfolderstr="UT-Terrain";
    private String musicfolderstr="UT-Music";
    private String classesfolderstr="UT-Classes";
    private String extrafolderstr="UT-Other";
    private String logfolderstr="Logs";
    private String defaultt3dlvlname = "myLevel.t3d";
    private String input_utmapname= "";
    private String output_conv_mapname = "";
    boolean bhasterrain=false;
    boolean bhasmusic=false;
    ArrayList unexportedtex;
    ArrayList unexportedsm;
    ArrayList altlsnames;
    int uvscalefactor=4;
    int numtotsmconv=0;
    int numtotsmnotconv=0;
    File t3dlvlfile;
    File utgamerootfolder;
    File uccfilepath;
    File texfolder;
    File smfolder;
    File extrafolder;
    File sndfolder;
    File terfolder;
    File classfolder;
    File logfolder;
    File musicfolder;
    int ps_mode=PlayerStart.mode_playerstart;
    int output_utgame;
    int input_utgame;

    T3DLevelToUTFiles tdltutf;
    ArrayList <Terrain>alterrainsdetected=new ArrayList();
    /**
     * Custom packages used for UT/U1 conversion to UT2004/UT3
     * Since can't identify directly textures from custom packages.
     */
    ArrayList <UTPackage>alutu1customtexpac=new ArrayList<UTPackage>();

    T3DUVTexUpdater tuvu;
    
    FileMover fm;
    T3DLevelNameChanger tlnc;
    TexturesIdentificator ti;
    SoundsIdentificator si;
    LevelExporter le;
    MusicIdentificator mi;
    SoundExporter se;
    SoundConverter sndc;
    StaticMeshesIdentificator smi;
    StaticMeshesExporter sme;
    SMT3DtoAse smt;
    T3DBrushTranformPerm tbt;
    T3DLevelScaler tls;
    T3DLvlMoverPort tlmp;
    T3dU1UT99ToUT2k4  t3du1ut99_tout2k4;
    T3dU2toUT3 t3du2ut3;
    T3dUT99toUT3 t3dut99ut3;
    T3dUT2k4toUT3 t3dut2k4ut3;
    T3DUT99ASMaker tasm;
    TextureExporter te;
    TextureConverter tc;
    TextureReplacer tr;
    UCExporter uce;
    UMCReader umc;
    UTPackageTexExtractor utpte;
    FileCleaner fc;
    FileRenamer fr;

    ArrayList altask=new ArrayList();
    GlobalInstructions gi;

    boolean stopconverting = false;
    
    public MapConverter(File inputfile,int inputgame,int outputgame) {
        input_mapfile = inputfile;
        input_utgame = inputgame;
        output_utgame = outputgame;
        this.input_utmapname = inputfile.getName().split("\\.")[0];


        this.defaultpackage = inputfile.getName().split("\\.")[0];
        this.uccfilepath = Main.config.getUCCFilePath(input_utgame);
        this.utgamerootfolder = Main.config.getUTxRootFolder(input_utgame);

        utpte= new UTPackageTexExtractor(this.input_utgame);

        if((input_utgame==UTGames.U1||input_utgame==UTGames.UT99)&&(output_utgame==UTGames.UT3)){
            this.setScalefactor(1.25D);
        }

        findpsmode();
        prepareFolders();
        guessFinalT3DFileName();

        this.t3dlvlfile = new File(super.getOutputfolder().getAbsolutePath()+File.separator+defaultt3dlvlname);
        //SET SUPPORTED CLASSES
        //SET UMC
        //     U1
        if(input_utgame==UTGames.U1){
            setSc(new SupUT99ToUT3Classes());
            this.canAddPackagetoAnalyze=true;
            if(outputgame==UTGames.UT2004){
                this.defumc = defumc_u1ut2k4;
                this.defumcfile = defumcfile_u1ut2k4;
                this.defpacnamedat = defpacnamedat_u1ut2k4;
                this.defscalefac = defscalefac_u1ut99_ut2004;
                this.canChangePackage = false;
            }
            if(outputgame==UTGames.UT3){
                this.defumc = defumc_u1ut3;
                this.defumcfile = defumcfile_u1ut3;
                this.defpacnamedat = defpacnamedat_u1ut3;
                this.defscalefac = defscalefac_u1ut99_ut3;
            }
        }

        //     UT99
        if(input_utgame==UTGames.UT99){
            setSc(new SupUT99ToUT3Classes());
            this.canAddPackagetoAnalyze=true;
            if(outputgame==UTGames.UT2004){
                this.defumc = defumc_ut99ut2k4;
                this.defumcfile = defumcfile_ut99ut2k4;
                this.defpacnamedat = defpacnamedat_ut99ut2k4;
                this.defscalefac = defscalefac_u1ut99_ut2004;
                this.canChangePackage = false;
            }
            if(outputgame==UTGames.UT3){
                this.defumc = defumc_ut99ut3;
                this.defumcfile = defumcfile_ut99ut3;
                this.defpacnamedat = defpacnamedat_ut99ut3;
                this.defscalefac = defscalefac_u1ut99_ut3;
            }
        }

        //   DEUS EX
        if(input_utgame==UTGames.DeusEx){
            setSc(new SupUT99ToUT3Classes());
            this.canAddPackagetoAnalyze=true;
            if(outputgame==UTGames.UT2004){
                this.defumc = defumc_deut2k4;
                this.defumcfile = defumcfile_ut99ut2k4;
                this.defpacnamedat = defpacnamedat_deut2k4;
                this.defscalefac = defscalefac_u1ut99_ut2004;
                this.canChangePackage = false;
            }
        }

        //    UT2004
        if(input_utgame==UTGames.UT2004){
            this.canAddPackagetoAnalyze=false;
            if(outputgame==UTGames.UT3){
                setSc(new SupUT2k4ToUT3Classes());
                this.defumc = defumc_ut99ut3;
                this.defscalefac = defscalefac_u2ut2k4_ut3;
                this.canChangeScale = false;
            }
        }

        //    U2
        if(input_utgame==UTGames.U2){
            this.canAddPackagetoAnalyze=false;
            
            if(outputgame==UTGames.UT3){
                setSc(new SupU2ToUT3Classes());
                this.defumc = defumc_u2ut3;
                this.defscalefac = defscalefac_u2ut2k4_ut3;
                this.canChangeScale = false;
            }
        }
    }



    public MapConverter(SupportedClasses sc) {
        this.sc = sc;
    }

    
    public MapConverter() {
    }

    
    private void guessFinalT3DFileName()
    {
        String tmp= input_mapfile.getName();
        String t[];
        if(tmp.contains("-"))
        {
            t = tmp.split("\\-");
            if(t.length>1)
            {
                tmp = t[1];
            }
        }
        if(tmp.contains("."))
        {
            t = tmp.split("\\.");
            tmp = t[0];
        }
        tmp = outputfolder.getAbsolutePath()+File.separator+tmp+UTGames.getUTGameShort(output_utgame)+".t3d";

        finalt3dfile = new File(tmp);

    }

    public File getFinalt3dfile() {
        return finalt3dfile;
    }



    public boolean isBhasmusic() {
        return bhasmusic;
    }

    public void setBhasmusic(boolean bhasmusic) {
        this.bhasmusic = bhasmusic;
    }

    public int getInput_utgame() {
        return input_utgame;
    }

    public void setInput_utgame(int input_utgame) {
        this.input_utgame = input_utgame;
    }

    public File getMusicfolder() {
        return musicfolder;
    }

    public void setMusicfolder(File musicfolder) {
        this.musicfolder = musicfolder;
    }

    public int getOutput_utgame() {
        return output_utgame;
    }

    public void setOutput_utgame(int output_utgame) {
        this.output_utgame = output_utgame;
    }

    public int getUvscalefactor() {
        return uvscalefactor;
    }

    public void setUvscalefactor(int uvscalefactor) {
        this.uvscalefactor = uvscalefactor;
    }

    
    private void findpsmode()
    {
        String in = input_utmapname.split("\\-")[0];
        if(in.equalsIgnoreCase("vctf")||in.equalsIgnoreCase("tdm")||in.equalsIgnoreCase("as")||in.equalsIgnoreCase("ctf"))
        {
            this.setPs_mode(PlayerStart.mode_teamplayerstart);
        }
        else if(in.equalsIgnoreCase("ons"))
        {
            this.setPs_mode(PlayerStart.mode_warfareteamplayerstart);
        }
        else
        {
            this.setPs_mode(PlayerStart.mode_playerstart);
        }
    }
    
    public int getPs_mode() {
        return ps_mode;
    }

    public void setPs_mode(int ps_mode) {
        this.ps_mode = ps_mode;
    }



    public String getInpututxxmapname() {
        return input_utmapname;
    }

    public void setInpututxxmapname(String inpututxxmapname) {
        this.input_utmapname = inpututxxmapname;
    }

    
    public String getOutputut3mapname() {
        return output_conv_mapname;
    }

    public void setOutputut3mapname(String outputut3mapname) {
        this.output_conv_mapname = outputut3mapname;
    }

    
    public ArrayList getAltlsnames() {
        return altlsnames;
    }

    public void setAltlsnames(ArrayList altlsnames) {
        this.altlsnames = altlsnames;
    }

    public void addTLSName(String[] tlsnames)
    {
        if(altlsnames==null){altlsnames=new ArrayList();}
        altlsnames.add(tlsnames);
    }
    public int getNumtotsmconv() {
        return numtotsmconv;
    }

    public void addNumtotsmnotconv() {
        this.numtotsmnotconv ++;
    }

   public void addNumtotsmconv() {
        this.numtotsmconv ++;
    }

    public void setNumtotsmconv(int numtotsmconv) {
        this.numtotsmconv = numtotsmconv;
    }

    public int getNumtotsmnotconv() {
        return numtotsmnotconv;
    }

    public void setNumtotsmnotconv(int numtotsmnotconv) {
        this.numtotsmnotconv = numtotsmnotconv;
    }

    public int getBrushscalefactor() {
        return uvscalefactor;
    }

    public void setBrushscalefactor(int brushscalefactor) {
        this.uvscalefactor = brushscalefactor;
    }

    public File getExtrafolder() {
        return extrafolder;
    }

    public void setExtrafolder(File extrafolder) {
        this.extrafolder = extrafolder;
    }

    public String getExtrafolderstr() {
        return extrafolderstr;
    }

    public void setExtrafolderstr(String extrafolderstr) {
        this.extrafolderstr = extrafolderstr;
    }

    public ArrayList getUnexportedsm() {
        return unexportedsm;
    }

    public void setUnexportedsm(ArrayList unexportedsm) {
        this.unexportedsm = unexportedsm;
    }

    
    public ArrayList getUnexportedtex() {
        return unexportedtex;
    }

    public void setUnexportedtex(ArrayList unexportedtex) {
        this.unexportedtex = unexportedtex;
    }

    /**
     *
     * @return Path/myLevel.t3d
     */
    public File getT3dlvlfile() {
        return t3dlvlfile;
    }

    public void setT3dlvlfile(File t3dlvlfile) {
        this.t3dlvlfile = t3dlvlfile;
    }

    public File getUccfilepath() {
        return uccfilepath;
    }

    public void setUccfilepath(File uccfilepath) {
        this.uccfilepath = uccfilepath;
    }

    public File getUtgamerootfolder() {
        return utgamerootfolder;
    }

    public void setUtgamerootfolder(File utgamerootfolder) {
        this.utgamerootfolder = utgamerootfolder;
    }

    
    public String getDefaultpackage() {
        return defaultpackage;
    }

    public void setDefaultpackage(String defaultpackage) {
        this.defaultpackage = defaultpackage;
    }



    public SupportedClasses getSc() {
        return sc;
    }

    public void setSc(SupportedClasses sc) {
        this.sc = sc;
    }

    
    public File getTerfolder() {
        return terfolder;
    }

    public void setTerfolder(File terfolder) {
        this.terfolder = terfolder;
    }

    public File getSmfolder() {
        return smfolder;
    }

    public void setSmfolder(File smfolder) {
        this.smfolder = smfolder;
    }

    public File getSndfolder() {
        return sndfolder;
    }

    public void setSndfolder(File sndfolder) {
        this.sndfolder = sndfolder;
    }

    public File getTexfolder() {
        return texfolder;
    }

    public void setTexfolder(File texfolder) {
        this.texfolder = texfolder;
    }

    public boolean isBhasterrain() {
        return bhasterrain;
    }

    public void setBhasterrain(boolean bhasterrain) {
        this.bhasterrain = bhasterrain;
    }

    
    public void makeAndCleanFolders(){

        Main.trayIcon.displayMessage("UT3 Converter", this.getInputfile().getName()+" is being converted ...", MessageType.WARNING);
        if(!this.outputfolder.exists()){
            this.outputfolder.mkdir();
        }
        /**
         * Delete T3D Files in output folder
         */
        File f[] = outputfolder.listFiles();
        if(f!=null){
            for(int i=0;i<f.length;i++)
            {
                if(f[i].getName().endsWith(".t3d")){f[i].delete();}
            }
        }


        File tmpfolder = smfolder;

        if((input_utgame==UTGames.UT2003)||(input_utgame==UTGames.UT2004))
        {
            //Delete alls files at StaticMesh folder
            if(!tmpfolder.exists()){tmpfolder.mkdir();}
            else
            {
                File listfiles[] = tmpfolder.listFiles();
                for(int i=0;i<listfiles.length;i++)
                {
                    listfiles[i].delete();
                }
            }


            //TODO convert umx files ...
            tmpfolder = musicfolder;
            if(!tmpfolder.exists()){tmpfolder.mkdir();}
            else
            {
                File listfiles[] = tmpfolder.listFiles();
                for(int i=0;i<listfiles.length;i++)
                {
                    listfiles[i].delete();
                }
            }
        }

        tmpfolder = terfolder;
        if(!tmpfolder.exists()){tmpfolder.mkdir();}
        else
        {
            File listfiles[] = tmpfolder.listFiles();
            for(int i=0;i<listfiles.length;i++)
            {
                listfiles[i].delete();
            }
        }

        tmpfolder = classfolder;
        if(!tmpfolder.exists()){tmpfolder.mkdir();}
        else
        {
            File listfiles[] = tmpfolder.listFiles();
            for(int i=0;i<listfiles.length;i++)
            {
                listfiles[i].delete();
            }
        }

        tmpfolder = texfolder;
        if(!tmpfolder.exists()){tmpfolder.mkdir();}
        else
        {
            File listfiles[] = tmpfolder.listFiles();
            for(int i=0;i<listfiles.length;i++)
            {
                listfiles[i].delete();
            }
        }

        tmpfolder = sndfolder;
        if(!tmpfolder.exists()){tmpfolder.mkdir();}
        else
        {
            File listfiles[] = tmpfolder.listFiles();
            for(int i=0;i<listfiles.length;i++)
            {
                listfiles[i].delete();
            }
        }


        tmpfolder = logfolder;
        if(!tmpfolder.exists()){tmpfolder.mkdir();}
        else
        {
            File listfiles[] = tmpfolder.listFiles();
            for(int i=0;i<listfiles.length;i++)
            {
                listfiles[i].delete();
            }
        }
    }
   public void prepareFolders()
   {
        this.outputfolder = guessOutputFolder(this.input_mapfile);
        this.texfolder = new File(outputfolder+File.separator+texfolderstr);
        this.smfolder = new File(outputfolder+File.separator+smfolderstr);
        this.sndfolder = new File(outputfolder+File.separator+sndfolderstr);
        this.terfolder = new File(outputfolder+File.separator+terfolderstr);
        this.extrafolder = new File(outputfolder+File.separator+extrafolderstr);
        this.musicfolder = new File(outputfolder+File.separator+musicfolderstr);
        this.classfolder = new File(outputfolder+File.separator+classesfolderstr);
        this.logfolder = new File(outputfolder+File.separator+logfolderstr);
   }

    public void run() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    ArrayList<File> alfilestodel=new ArrayList<File>();
    public void addFilesToDelAfterConvert(File f)
    {
        if(f!=null){
            alfilestodel.add(f);
        }
    }

    /**
     * Delete unused files.
     * Typically it deletes temporary t3d files created while converting
     */
    public void delUnusedFilesAfterConvert()
    {
        long filesizetmp=0L;
        File ftodel;
        if(alfilestodel.size()>0&&delt3dfilesafterconvert)
        {
            System.out.println("\n**** Deleting temporary created files  *****");
            for(int i=0;i<alfilestodel.size();i++)
            {
                ftodel = alfilestodel.get(i);
                if(ftodel.exists()){
                    System.out.print((i+1)+"/"+alfilestodel.size()+"-"+ftodel.getName()+" ... ");
                    filesizetmp = ftodel.length();
                    boolean bdel = ftodel.delete();
                    if(bdel)
                    {
                        System.out.println("Done! ("+FileCleaner.sizetoKBMB(filesizetmp)+" deleted)");
                    }
                    else
                    {
                        System.out.println("Failed! (maybe file is in use by another process)");
                    }
                }
            }
        }
    }

    public File getLogfolder() {
        return logfolder;
    }

    public void setLogfolder(File logfolder) {
        this.logfolder = logfolder;
    }

    boolean bhasExportedSnds=false;
    public void processTasks() throws Exception
    {
        Object obj;
        for(int i=0;i<altask.size();i++)
        {
            obj = altask.get(i);
            if(obj instanceof T3dU1UT99ToUT2k4)
            {
                t3du1ut99_tout2k4 = (T3dU1UT99ToUT2k4) obj;
                t3du1ut99_tout2k4.convert();
            }
            else if(obj instanceof LevelExporter)
            {
                le = (LevelExporter) obj;
                le.exportT3DMap();
            }
            else if(obj instanceof FileCleaner)
            {
                fc = (FileCleaner) obj;
                if(fc.getTi()!=null)
                {
                    fc.setFiles2del(ti.getFileTex2Del());
                    fc.setTi(null);
                }
                if(fc.getSi()!=null)
                {
                    fc.setSi(si);

                    fc.setFiles2del(si.getFileSnd2Del());
                }
                fc.clean();
            }
            else if(obj instanceof FileMover)
            {
                fm = (FileMover) obj;
                fm.move();
            }
            else if(obj instanceof FileRenamer)
            {
                fr = (FileRenamer) obj;
                fr.renameAll();
            }
            else if(obj instanceof MusicIdentificator)
            {
                mi = (MusicIdentificator) obj;
                mi.convert();
                setBhasmusic(mi.bHasMusic());
            }
            else if(obj instanceof SoundsIdentificator)
            {
                si = (SoundsIdentificator) obj;
                if(input_utgame==UTGames.U2)
                {
                    si.useNameSndOnly(true);
                }
                si.identifyLvlSounds();
            }
            else if(obj instanceof SoundConverter)
            {
                sndc = (SoundConverter) obj;
                sndc.convertAll();
            }
            else if(obj instanceof StaticMeshesIdentificator)
            {
                smi =           (StaticMeshesIdentificator) obj;
                smi.identifyStaticMeshes();
            }
            else if(obj instanceof StaticMeshesExporter)
            {
                sme = (StaticMeshesExporter) obj;
                sme.setUsxfiles(smi.getFileSmFiles(Main.config, this.input_utgame),this);
                sme.removeFile(input_mapfile.getName().split("\\.")+".usx"); //Removes export Map.utx
                sme.ExportToT3D();
                if(this.unexportedsm==null)
                {
                   this.unexportedsm = smi.getUnexportedSm(this.smfolder);
                }
                fc = new FileCleaner(smi.getFileSm2Del(smfolder));
                fc.clean();
                File f[] = this.getSmfolder().listFiles();
                for(int j=0;j<f.length;j++)
                {
                    ti.addFile(f[j], false);
                }
            }
            else if(obj instanceof SMT3DtoAse)
            {
                smt = (SMT3DtoAse) obj;
                if(smt.isBHasTextureReplacement())
                {
                    smt.setTr(tr);
                }
                smt.export2Ase();
            }
            else if(obj instanceof T3DBrushTranformPerm)
            {
                tbt = (T3DBrushTranformPerm) obj;
                tbt.convert();
            }
            else if(obj instanceof T3DLvlMoverPort)
            {
                tlmp = (T3DLvlMoverPort) obj;
                tlmp.convert();
            }
            else if(obj instanceof T3DLevelScaler)
            {
                tls = (T3DLevelScaler) obj;
                tls.scale();
            }
            else if(obj instanceof T3DLevelNameChanger)
            {
                tlnc = (T3DLevelNameChanger) obj;
                tlnc.replace();
            }
            else if(obj instanceof T3dU2toUT3)
            {
                t3du2ut3 = (T3dU2toUT3) obj;
                t3du2ut3.setTextureReplacement(tr);
                t3du2ut3.convert();
                this.alterrainsdetected = t3du2ut3.getTerrainsDetected();
                setBhasterrain(t3du2ut3.isBhasterrain());
            }
            else if(obj instanceof T3dUT99toUT3)
            {
                t3dut99ut3 = (T3dUT99toUT3) obj;
                t3dut99ut3.convert();
            }
            else if(obj instanceof T3dUT2k4toUT3)
            {
                t3dut2k4ut3 = (T3dUT2k4toUT3) obj;
                t3dut2k4ut3.setTextureReplacement(tr);
                t3dut2k4ut3.convert();
                this.alterrainsdetected = t3dut2k4ut3.getTerrainsDetected();
                setBhasterrain(t3dut2k4ut3.isBhasterrain());
            }
            else if(obj instanceof T3DUT99ASMaker)
            {
                tasm = (T3DUT99ASMaker) obj;
                tasm.convert();
            }
            else if(obj instanceof T3DUVTexUpdater)
            {
                tuvu = (T3DUVTexUpdater) obj;
                tuvu.convert();
            }
            else if(obj instanceof TextureConverter)
            {
                tc = (TextureConverter) obj;
                tc.convertAll();
            }
            else if(obj instanceof TextureExporter)
            {

                te = (TextureExporter) obj;
                if(te.getUtxfiles()==null)
                {
                  te.setUtxfiles(ti.getTexPackageFiles(Main.config, this.input_utgame));
                }
                te.ExportTex();
                if(this.unexportedtex==null)
                {
                   //this.unexportedtex = ti.getUnexportedTex(this.texfolder);
                }
            }
            else if(obj instanceof TexturesIdentificator)
            {
                ti = (TexturesIdentificator) obj;
                ti.identifyTextures();
            }
            else if(obj instanceof TextureReplacer)
            {
                tr =  (TextureReplacer) obj;
                tr.setUtxfiles(ti.getTexPackageFiles(Main.config, this.input_utgame));
                tr.analyseAll();
                ti.addIdentifiedTextures(tr.getNewNames(ti.getSet_texfilestokeep()));
                ArrayList al = tr.getPackagesAnalysed();
                for(int j=0;j<al.size();j++)
                {
                    String pacname = al.get(j).toString();
                    File ff = new File(Main.config.getUTxRootFolder(this.input_utgame).getAbsolutePath()+File.separator+"Textures"+File.separator+pacname+".utx");
                    if(!utpte.bHasExportedFile(ff))
                    {
                        utpte.addUTTexPackage_toExport(
                                new UTPackageTexConfig(
                                ff,
                                outputfolder,
                                new String[]{"bmp"},
                                UTPackageTexExtractor.mode_utxextractor,
                                this.input_utgame)
                                );
                    }
                }
            }
            else if(obj instanceof UCExporter)
            {
                uce = (UCExporter) obj;
                uce.Export();
            }
            else if(obj instanceof UMCReader)
            {
                umc = (UMCReader) obj;
                umc.replace();
            }
            else if(obj instanceof UTPackageTexExtractor)
            {
                utpte = (UTPackageTexExtractor) obj;
                utpte.addUTTexPackages_toExport(ti.getAl_utxexport2());
                utpte.ExportAll();
            }
            //UTPackageTexExtractor
            else if(obj instanceof SoundExporter)
            {
                se = (SoundExporter) obj;
                if((si!=null)&&!bhasExportedSnds)
                {
                    //se.setUaxfiles(si.getSoundPackageFilesToExport(Main.config, this.input_utgame));
                    se.ExportToWav();
                    bhasExportedSnds = true;
                }
            }
            else if(obj instanceof T3DLevelToUTFiles)
            {
                tdltutf = (T3DLevelToUTFiles) obj;
                tdltutf.ConvertUTRessources();
                //For replacing material names in T3D level in staticmeshes later ...
                this.tr = tdltutf.getTr();
            }
            else
            {
                System.out.println("No task found for object: "+obj);
            }
        }
        delUnusedFilesAfterConvert();
        fc = new FileCleaner(this.getTerfolder().listFiles());
        fc.clean();

        fc = new FileCleaner(this.getTexfolder(), ".bmp");
        fc.clean();
    }

    public void addUTCustomPackages(File[] utcustompac){
        for (int i = 0; i < utcustompac.length; i++) {
            alutu1customtexpac.add(new UTPackage(utcustompac[i]));
        }
    }

    public boolean hasCustomUTTexPacToAnalyze(){
        if(alutu1customtexpac.size()>0){
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<UTPackage> getAlutu1customtexpac() {
        return alutu1customtexpac;
    }

    public double getScalefactor() {
        return scalefactor;
    }

    public void setScalefactor(double scalefactor) {
        this.scalefactor = scalefactor;
    }

    public String getDefumcfile() {
        return defumcfile;
    }

    public void setDefumcfile(String defumcfile) {
        this.defumcfile = defumcfile;
    }

    public Object[] getDefumc() {
        return defumc;
    }

    public void setDefumc(Object[] defumc) {
        this.defumc = defumc;
    }

    public String getDefscalefac() {
        return defscalefac;
    }

    public void setDefscalefac(String defscalefac) {
        this.defscalefac = defscalefac;
    }

    public String getDefpacnamedat() {
        return defpacnamedat;
    }

    public void setDefpacnamedat(String defpacnamedat) {
        this.defpacnamedat = defpacnamedat;
    }

    public File guessOutputFolder(File inputmap)
    {
        String tmp= inputmap.getName();
        String t[];
        if(tmp.contains("-"))
        {
            t = tmp.split("\\-");
            if(t.length>1)
            {
                tmp = t[1];
            }
        }
        if(tmp.contains("."))
        {
            t = tmp.split("\\.");
            tmp = t[0];
        }
        if(tmp.length()>15){tmp = tmp.substring(0, 15);}
        tmp = Installation.getProgramFolder().getAbsolutePath()+File.separator+"ConvertedMaps"+File.separator+tmp;
        File f = new File(Installation.getProgramFolder().getAbsolutePath()+File.separator+"ConvertedMaps");
        if(!f.exists())
        {
            f.mkdirs();
        }
        return new File(tmp);
    }
    
    public String getConversionInfo(){
        String tmp="";
        tmp = "* * * * * * * * * * C.O.N.V.E.R.S.I.O.N.   I.N.F.O. * * * * * * * * * *";
        tmp += "\nInput Map: "+this.input_mapfile.getName();
        tmp += "\nInput Game: "+UTGames.getUTGame(input_utgame);
        tmp += "\nOutput Game: "+UTGames.getUTGame(output_utgame);
        tmp += "\nOutput T3D File: "+getFinalt3dfile().getName();
        tmp += "\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n";
        return tmp;
    }

    public void deleteEmptyFolders(){
        if(logfolder!=null&logfolder.exists()&&logfolder.listFiles().length==0){
            logfolder.delete();
        }
        if(smfolder!=null&smfolder.exists()&&smfolder.listFiles().length==0){
            smfolder.delete();
        }
        if(terfolder!=null&terfolder.exists()&&terfolder.listFiles().length==0){
            terfolder.delete();
        }
        if(extrafolder!=null&extrafolder.exists()&&extrafolder.listFiles().length==0){
            extrafolder.delete();
        }
        if(classfolder!=null&classfolder.exists()&&classfolder.listFiles().length==0){
            classfolder.delete();
        }
    }

    public boolean isCanChangePackage() {
        return canChangePackage;
    }

    public boolean isCanChangeScale() {
        return canChangeScale;
    }

    public boolean isCanChangeUMC() {
        return canChangeUMC;
    }

    public boolean isCanAddPackagetoAnalyze() {
        return canAddPackagetoAnalyze;
    }

    public boolean isDelt3dfilesafterconvert() {
        return delt3dfilesafterconvert;
    }

    public void setDelt3dfilesafterconvert(boolean delt3dfilesafterconvert) {
        this.delt3dfilesafterconvert = delt3dfilesafterconvert;
    }

    public GlobalInstructions getGi() {
        return gi;
    }

    public void setGi(GlobalInstructions gi) {
        this.gi = gi;
    }


    /**
     * Writes instructions to file
     */
    public void writeInstructions(){
        if(gi!=null&&outputfolder!=null){
            File finst = new File(outputfolder.getAbsolutePath()+"//Instructions.txt");
            gi.writeToFile(finst);
        }
    }

    public static String getDefaultsm() {
        return defaultsm;
    }

    

}
