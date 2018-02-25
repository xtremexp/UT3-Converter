/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.identification;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import ut3converter2.Configuration;
import ut3converter2.Main;
import ut3converter2.UTGames;
import ut3converter2.UTObject;
import ut3converter2.UTPackage;
import ut3converter2.convert.Level.UTActor;
import ut3converter2.convert.MapConverter;
import ut3converter2.export.UTPackageTexConfig;
import ut3converter2.export.UTPackageTexExtractor;
import ut3converter2.T3DConvertor;
import ut3converter2.tools.UTPackageDat;
import ut3converter2.tools.UTPackageFinder;

/**
 * Identifies textures used by T3D staticmeshes files or T3D level Files
 * @author Hyperion
 */
public class TexturesIdentificator extends T3DConvertor{

    public final static String T3DTYPE_LEVEL="t3dlevel";
    public final static String T3DTYPE_STATICMESH="t3dsm";
    public final static File UT99DATA_TEXFILE =
            new File(Main.installfolder.getAbsolutePath() + File.separator + "conf" + File.separator + "UT99TexPackInfo.txt");
    public final static File U1DATA_TEXFILE =
            new File(Main.installfolder.getAbsolutePath() + File.separator + "conf" + File.separator + "U1TexPackInfo.txt");

    final String tskname = "\n*** Identification of textures used ***";
    ArrayList alt3dlvlsmfiles;
    /** Key: Texture package, Value: Arraylist of "group_+tex name"*/



    HashMap<String,String> hmutpack;
    public HashSet<String> alpactex = new HashSet<String>();
    boolean t3dlvlfile = true;
    boolean bisutu1file=false;
    File texfolder;
    ArrayList al;

    Set<String> set_utxexport = new HashSet<String>();
    ArrayList<UTPackageTexConfig> al_utxexport2 = new ArrayList<UTPackageTexConfig>();
    Set<String> set_texfilestertomove = new HashSet<String>();
    Set<String> set_texfilestokeep = new HashSet<String>();
    MapConverter mc;
    File futxtmpdat;
    String[] textype;
    public static final String[] textype_bmp = new String[]{"bmp"};
    public static final String[] textype_tga = new String[]{"tga"};
    public static final String[] textype_dds = new String[]{"dds"};
    public static final String[] textype_all = new String[]{"dds","tga","bmp"};

    public TexturesIdentificator(MapConverter mc) {
        alt3dlvlsmfiles = new ArrayList();
        this.mc = mc;
        futxtmpdat=new File(mc.getLogfolder().getAbsolutePath()+File.separator+"uttextmp.data");
        this.textype = textype_bmp;
        this.texfolder = mc.getTexfolder();
        try {
            if(mc.getInput_utgame()==UTGames.U1){
                    hmutpack = UTPackageFinder.getPackages(U1DATA_TEXFILE);
            } else if(mc.getInput_utgame()==UTGames.UT99){
                    hmutpack = UTPackageFinder.getPackages(UT99DATA_TEXFILE);
            }
        } catch (Exception e) {
        }
        
    }

    /**
     *
     * @param filestoidentify
     * @param t3dfiletype T3D Level or T3D Staticmesh
     */
    public void addT3DFilesToIdentifyTex(File[] filestoidentify,String t3dfiletype){
        boolean ist3dlvlfile = false;
        if(t3dfiletype.equals(TexturesIdentificator.T3DTYPE_LEVEL)){
            ist3dlvlfile = true;
        }
        for (int i = 0; i < filestoidentify.length; i++) {
            File file = filestoidentify[i];
            alt3dlvlsmfiles.add(new Object[]{file,ist3dlvlfile});
        }
    }

    /**
     *
     * @param alfiles List of files to analyse.
     * @param format Specify what output format the textures will be exported.
     */
    public TexturesIdentificator(ArrayList alfiles,File texfolder,String[] textype) {
        this.alt3dlvlsmfiles = alfiles;
        this.textype = textype;
        super.setTaskname(tskname);
        this.texfolder = texfolder;
        al = new ArrayList();
    }

    /**
     *
     * @param alfiles List of files to analyse.
     * @param format Specify what output format the textures will be exported.
     */
    public TexturesIdentificator(ArrayList alfiles,File texfolder,MapConverter conv) {
        this.alt3dlvlsmfiles = alfiles;
        this.mc = conv;
        this.textype = textype_bmp;
        super.setTaskname(tskname);
        this.texfolder = texfolder;
        al = new ArrayList();
        if(this.mc.getInput_utgame()==UTGames.U1||this.mc.getInput_utgame()==UTGames.UT99)
        {
            bisutu1file = true;
        }
    }

    /**
     * 
     * @param alfiles List of files to analyse.
     * @param format Specify what output format the textures will be exported.
     * @param ut99packinfo File that contains TextureName to PackageName data.
     */
    public TexturesIdentificator(ArrayList alfiles,File texfolder,String[] textype,File ut99packinfo,MapConverter conv) {
        this.mc = conv;
        this.alt3dlvlsmfiles = alfiles;
        this.textype = textype;
        this.texfolder = texfolder;
        super.setTaskname(tskname);
        al = new ArrayList();
        bisutu1file = true;
        try {
            hmutpack = UTPackageFinder.getPackages(ut99packinfo);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TexturesIdentificator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TexturesIdentificator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Adds a file to analyse for texturing info.
     * @param f File to analyse (T3D Level file or T3D staticmesh)
     * @param ist3dlvlfile If its a T3D Level file.
     */
    public void addFile(File f,boolean ist3dlvlfile)
    {
        this.alt3dlvlsmfiles.add(new Object[]{f,ist3dlvlfile});
    }

    /**
     * Identifies textures used in brushes and staticmeshes and terrain.
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public void identifyTextures() {

        try {
                    Object obj[];
                    File x;
                    Boolean ist3dlvlfile;
                    if (super.isBShowLog()) {
                        System.out.println(tskname);
                    }
                    if (alt3dlvlsmfiles != null) {
                        for (int i = 0; i < alt3dlvlsmfiles.size(); i++) {
                            obj = (Object[]) alt3dlvlsmfiles.get(i);
                            x = (File) obj[0];
                            if (super.isBShowLog()) {
                                System.out.print((i + 1) + "/" + (alt3dlvlsmfiles.size()) + "-" + x.getName());
                            }
                            ist3dlvlfile = (Boolean) obj[1];
                            if (ist3dlvlfile) {
                                if (super.isBShowLog()) {
                                    System.out.print(" (T3D Level)");
                                }
                                identifyLvlTextures(x);
                            } else {
                                if (super.isBShowLog()) {
                                    System.out.print(" (T3D Staticmesh)");
                                }
                                identifySmTextures(x);
                            }
                            if (super.isBShowLog()) {
                                System.out.println("... done!");

                            }
                        }
                    }
        } catch (Exception e) {
        }

    }

    /**
     * Adds texture replacements data so  FileCleaner will not delete the texture files
     * @param altex
     */
    public void addIdentifiedTextures(ArrayList altex)
    {
        String a="";

        for (int i = 0; i < altex.size(); i++) {
            a = (String) altex.get(i);
            set_texfilestokeep.add(a+".bmp");
        }
    }
    

    public void identifyLvlTextures(File t3dlvlfile) throws FileNotFoundException, IOException, Exception
    {

        //           Begin Polygon Texture=BarrensArchitecture.Base.base01BO Link=0
        //           Begin Polygon Texture=BarrensArchitecture.Base.bas03BA
        //        Texture=Texture'AW-2004Particles.Fire.MuchSmoke2t'
        //         Skins(0)=FinalBlend'myLevel.jwat1bl'
        String line="";
        BufferedReader bfr = new BufferedReader(new FileReader(t3dlvlfile));
        UTObject uto;
        UTPackageTexConfig utptc = null;
        String curclass="";
        String utxpackage;
        String texfile;
        
        while((line=bfr.readLine())!=null)
        {
            try {
                if(line.contains("Begin Actor"))
                {
                    curclass = UTActor.getActorClass(line);
                }

                if(curclass.contains("Terrain"))
                {
                    AnalyzeTerrainTextures(line);
                }

                //	TerrainMap=Texture'GraysonT.HeightMaps.M09A_v1a'
                //	TerrainMap=Texture'M10_Avalon.terrain_maps.island1'
                //    Skins(0)=Shader'CinemaT.Avalon.cloudshader1'
                //	Layers(0)=(Texture=Texture'ScottT.Generic.RockTerr_U08A677_',Al
                //Texture=DM-1on1-Aerowalk.Misc.eX_q2_01d_d_egg1 Link=0
                else if(line.contains("Texture=")||line.contains("Skins(")||line.contains("TerrainMap=Texture"))
                {
                    uto = getUTOUsedFromT3DLvl(line);
                    texfile = uto.getGroupAndName();


                    //F:\jeux\U2\System>ucc batchexport ..\Maps\MM_marsh.un2 Texture bmp ..\
                    //Loading package ..\Maps\MM_marsh.un2...
                    //Exported Texture MM_marsh.DecoLayers.fern_01 to ..\fern_01.bmp
                    set_utxexport.add(uto.getPackagename());



                    if (true)
                    {
                        utptc = uto.toUTPackageTexConfig(mc.getTexfolder(), this.textype, UTPackageTexExtractor.mode_utxextractor , this.mc.getInput_utgame());
                        if(!alpactex.contains(uto.getPackagename())&&utptc!=null)
                        {
                            al_utxexport2.add(utptc);
                        }
                        alpactex.add(uto.getPackagename());
                        if(utptc!=null){
                            for(int i=0;i<utptc.getTextype_export().size();i++)
                            {
                                set_texfilestokeep.add(texfile.toLowerCase()+"."+utptc.getTextype_export().toArray()[i]);
                            }
                        } else {
                            set_texfilestokeep.add(texfile.toLowerCase()+".bmp");
                        }
                        
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                throw  e;
                //Main.config.getMclog().warning("[TextureIdentification]-"+t3dlvlfile.getName()+"- \""+line+"\") "+e.getMessage());
            }
            
        }
    }

    private void AnalyzeTerrainTextures(String line)
    {
        UTPackageTexConfig utptc;
        UTObject uto = null;
        //U2: TerrainMap=Texture'SulferonT.HeightMaps.FlatPlainofSulfer'
        if(line.contains("TerrainMap"))
        {
            uto = new UTObject((line.split("TerrainMap")[1]).split("\\'")[1]);
            if(!uto.getPackagename().equals("myLevel"))
            {
                utptc = uto.toUTPackageTexConfig(mc.getTerfolder(), new String[]{"bmp"}, UTPackageTexExtractor.mode_uccbatchexport , mc.getInput_utgame());
                updateTexFilesToKeep(uto, mc.getInput_utgame(), ".bmp",UTPackageTexExtractor.mode_uccbatchexport);

                updateTexFilesTerToMove(uto, mc.getInput_utgame(), ".bmp",UTPackageTexExtractor.mode_uccbatchexport);
                updateTexFilesTerToMove(uto, mc.getInput_utgame(), ".tga",UTPackageTexExtractor.mode_uccbatchexport);

                //if(!alpactex.contains(uto.getPackagename())&&utptc!=null)
                addUTPTC(utptc);
                /*
                if(!al_utxexport2.contains(utptc))
                {
                    al_utxexport2.add(utptc);
                }*/
                alpactex.add(uto.getPackagename());
            }
        }
        //U2:"	Layers(5)=(Texture=Texture'ScottT.Generic.grssflor_umms700',AlphaMap=Texture'mm_marsh.alphamaps.swamp_alpha_005',US ..;"
        //UT2k4:     Layers(5)=(AlphaMap=Texture'ONS-Dria.Terraintexalphalayerdetailsand',UScale=1.000000,VScale=1.000000) (no tex!!)
        else if(line.contains("Layers")&&line.contains("AlphaMap")&&line.contains("Texture="))
        {
            uto = new UTObject((line.split("Texture=")[1]).split("\\'")[1]);
            utptc = uto.toUTPackageTexConfig(mc.getTexfolder(), new String[]{"bmp"}, UTPackageTexExtractor.mode_utxextractor , mc.getInput_utgame());
            updateTexFilesToKeep(uto, mc.getInput_utgame(), ".bmp",UTPackageTexExtractor.mode_utxextractor);
            
            //if(!alpactex.contains(uto.getPackagename())&&utptc!=null)
            addUTPTC(utptc);
            alpactex.add(uto.getPackagename());

            uto = new UTObject((line.split("AlphaMap=")[1]).split("\\'")[1]);
            utptc = uto.toUTPackageTexConfig(mc.getTerfolder(), new String[]{"tga"}, UTPackageTexExtractor.mode_uccbatchexport , mc.getInput_utgame());
            updateTexFilesToKeep(uto, mc.getInput_utgame(), ".tga",UTPackageTexExtractor.mode_uccbatchexport);
            updateTexFilesTerToMove(uto, mc.getInput_utgame(), ".tga",UTPackageTexExtractor.mode_uccbatchexport);

            //if(!alpactex.contains(uto.getPackagename())&&utptc!=null)
            addUTPTC(utptc);
            alpactex.add(uto.getPackagename());
        }
        //U2:"     DecoLayers(0)=(ShowOnTerrain=1,DensityMap=Texture'mm_marsh.DecoLayers.decolayer_02',St"
        //Always read layers with TGA files
        else if(line.contains("DensityMap")&&line.contains("DecoLayer"))
        {
            uto = new UTObject((line.split("DensityMap")[1]).split("\\'")[1]);
            utptc = uto.toUTPackageTexConfig(mc.getTerfolder(), new String[]{"tga"}, UTPackageTexExtractor.mode_uccbatchexport , mc.getInput_utgame());
            updateTexFilesToKeep(uto, mc.getInput_utgame(), ".tga",UTPackageTexExtractor.mode_uccbatchexport);
            updateTexFilesTerToMove(uto, mc.getInput_utgame(), ".tga",UTPackageTexExtractor.mode_uccbatchexport);
            updateTexFilesTerToMove(uto, mc.getInput_utgame(), ".bmp",UTPackageTexExtractor.mode_uccbatchexport);

            //if(!alpactex.contains(uto.getPackagename())&&utptc!=null)
            addUTPTC(utptc);
            alpactex.add(uto.getPackagename());
        }
    }



    /**
     *
     * @param uto
     * @param inputgame
     * @param exportmode UCCBatchexport or utxextractor
     * @param texfileextension Ex: .tga,.bmp,...
     * @see UTPackageTexExtractor
     */
    private void updateTexFilesToKeep(UTObject uto,int inputgame,String texfileextension,int exportmode)
    {
        if(exportmode == UTPackageTexExtractor.mode_utxextractor)
        {
            set_texfilestokeep.add(uto.getGroupAndName().toLowerCase()+texfileextension);
        }
        else
        {
            if((inputgame==UTGames.U2)||(inputgame==UTGames.U1)||(inputgame==UTGames.UT99))
            {
                set_texfilestokeep.add(uto.getName().toLowerCase()+texfileextension);
            }
            else
            {
                set_texfilestokeep.add(uto.getGroupAndName().toLowerCase()+texfileextension);
            }
        }
        
    }

    private void updateTexFilesTerToMove(UTObject uto,int inputgame,String texfileextension,int exportmode)
    {
        if(exportmode == UTPackageTexExtractor.mode_utxextractor)
        {
            set_texfilestertomove.add(uto.getGroupAndName().toLowerCase()+texfileextension);
        }
        else
        {
            if((inputgame==UTGames.U2)||(inputgame==UTGames.U1)||(inputgame==UTGames.UT99))
            {
                set_texfilestertomove.add(uto.getName().toLowerCase()+texfileextension);
            }
            else
            {
                set_texfilestertomove.add(uto.getGroupAndName().toLowerCase()+texfileextension);
            }
        }

    }
    
    /** Identify textures used throught T3D Staticmeshes files */
    public void identifySmTextures(File t3dsmfile) throws IOException
    {
        try {

        String texfile;
        String line="";
        BufferedReader bfr = new BufferedReader(new FileReader(t3dsmfile));
        UTObject uto;
        String utxpackage;
        UTPackageTexConfig utptc;
        //Skins(0)=FinalBlend'myLevel.jwat1bl'
        //        Texture 2K4TrophyTEX.trophyTEXTURES.TrophyENVreflection
        
        while((line=bfr.readLine())!=null)
        {
            //Texture None
            if(line.contains("Texture ")&&!line.contains("Texture None"))
            {
                uto = getUTOUsedFromT3DSM(line);
                texfile = uto.getGroupAndName();

                set_utxexport.add(uto.getPackagename());
                utptc = uto.toUTPackageTexConfig(mc.getTexfolder(), this.textype, UTPackageTexExtractor.mode_utxextractor , this.mc.getInput_utgame());

                if(!alpactex.contains(uto.getPackagename())&&utptc!=null)
                {
                    al_utxexport2.add(utptc);
                }
                alpactex.add(uto.getPackagename());


                for(int i=0;i<utptc.getTextype_export().size();i++)
                {
                    set_texfilestokeep.add(texfile.toLowerCase()+"."+utptc.getTextype_export().toArray()[i]);
                }
            }
        }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TexturesIdentificator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashSet getAlpactex() {
        return alpactex;
    }

    public Set<String> getIdentifiedTexFilesToKeep(){
        return this.set_texfilestokeep;
    }

    public File[] getFileTex2Del()
    {
        File listtexfiles[] = texfolder.listFiles();
        ArrayList al2 = new ArrayList();

        //.out.println(set_texfilestokeep);

        for(int i=0;i<listtexfiles.length;i++)
        {
            //System.out.println(listtexfiles[i].getName());
            if(!set_texfilestokeep.contains(listtexfiles[i].getName().toLowerCase()))
            {
               al2.add(listtexfiles[i]);
            }
        }
        File listfiltodel[] = new File[al2.size()];
        for (int i = 0; i < al2.size(); i++) {
            File fil = (File) al2.get(i);
            listfiltodel[i]=fil;
        }
        return listfiltodel;
    }


    private File[] getFileTex(ArrayList<File> alfil)
    {
        File f[] = new File[alfil.size()];
        for (int i = 0; i < alfil.size(); i++) {
            File file = alfil.get(i);
            f[i] = file;
        }
        return f;
    }

    /**
     * Return the packages files associated with texture package (can be .utx tex file or .u system file)
     * @param conf
     * @param utgame Number of UTGame
     * @return
     */
    public File[] getTexPackageFiles(Configuration conf,int utgame)
    {
        File listex[]=new File[al_utxexport2.size()];
        File f;

        for (int i = 0; i < al_utxexport2.size(); i++) {
            UTPackageTexConfig utptc = al_utxexport2.get(i);
            listex[i] = utptc.getUtpackagetexfile();
        }
        return listex;
    }
 
    /**
     * 
     * @param line
     * @return
     */
    private UTObject getUTOUsedFromT3DLvl(String line)
    {

        String tmp = "";
        String pacname = "";
        //        Texture'AW-2004Particles.Fire.MuchSmoke2t'
        //        WallTexture=WetTexture'TCrystal.taryd2'


        //        Texture=Texture'AW-2004Particles.Fire.MuchSmoke2t'
        //  Layers(0)=(Texture=Texture'AlleriaTerrain.ground.snowcliff04AL'
        //Skins(0)=Shader'Phobos2_cp.HardwareSkins.cp_rustbrace3shad'
        //    Skins(0)=Shader'CinemaT.Avalon.cloudshader1'
        if(line.contains("Texture'")||line.contains("Skins("))
        {
            tmp = line.split("\\'")[1];
            if((mc.getInput_utgame()==UTGames.U1)||(mc.getInput_utgame()==UTGames.UT99)){
                pacname = hmutpack.get(tmp.toLowerCase());
                if(pacname==null){pacname=mc.getInputfile().getName().split("\\.")[0];}
                return new UTObject(pacname, tmp);
            } else {
                return new UTObject(tmp);
            }
        }

        //          Begin Polygon Item=Sheet Texture=dom-goose.Lost-banners.underwater-dark Flags=264
        //          Begin Polygon Texture=Wrcka4 Link=0
        else
        {
            tmp = line.split("Texture=")[1]; //Shiptech.Bases.bas01CS Link
            tmp = tmp.split(" ")[0]; //Shiptech.Bases.bas01CS
            if((mc.getInput_utgame()==UTGames.U1)||(mc.getInput_utgame()==UTGames.UT99)){
                pacname = hmutpack.get(tmp.toLowerCase());
                if(pacname==null){pacname=mc.getInputfile().getName().split("\\.")[0];}
                return new UTObject(pacname, tmp);
            }
            return new UTObject(tmp);
        }
    }

    private UTObject getUTOUsedFromT3DSM(String line)
    {
        //        Texture 2K4TrophyTEX.trophyTEXTURES.TrophyENVreflection
        String tmp = line.split("Texture ")[1];
        UTObject uto = new UTObject(tmp);
        return uto;
    }


     public String getTexUsed()
    {
        String result = "Texture packages used:\n";
        Iterator it = set_utxexport.iterator();
        while(it.hasNext())
        {
            if(!it.hasNext())
            {
                result += it.next().toString();
            }
            else
            {
                result += it.next().toString()+",";
            }
        }

        return result;
    }

    public ArrayList<UTPackageTexConfig> getAl_utxexport2() {
        return al_utxexport2;
    }

    /**
     * [HeightMaps_M09A_v1a.bmp,M09A_v1a.tga,...]
     * @return
     */
    public Set<String> getSet_texfilestokeep() {
        return set_texfilestokeep;
    }

    public Set<String> getSet_texfilestertomove() {
        return set_texfilestertomove;
    }

    public void addUTTexInfoToHMUTPack(ArrayList<UTPackage> altexpackages){
        try {
           UTPackageDat utpd= 
                new UTPackageDat(altexpackages, mc.getLogfolder(), new File(mc.getLogfolder().getAbsolutePath()+File.separator+"uttextmp.data"));
            utpd.createDatFile();
            hmutpack.putAll(UTPackageFinder.getPackages(futxtmpdat)); 
        } catch (Exception e) {
            Main.config.getMclog().warning("Error while identifying package data.");
        }
    }

    /**
     * 
     * @return <mytexname,packageitbelongsto>
     */
    public HashMap<String, String> getHmutpack() {
        return hmutpack;
    }

    private void addUTPTC(UTPackageTexConfig utptc){

        if(utptc==null){
            return;
        }

        if(al_utxexport2.isEmpty()){
            al_utxexport2.add(utptc);
            return;
        }

        UTPackageTexConfig utptctmp;

        utptctmp = hasUTPTCWithPacNameAndExpMode(utptc.getPackageNameLowerCase(), utptc.getExport_mode());
        if(utptctmp==null){
            al_utxexport2.add(utptc);
        } else {
            utptctmp.getTextype_export().addAll(utptc.getTextype_export());
        }
    }

    private UTPackageTexConfig hasUTPTCWithPacNameAndExpMode(String texpacname, int exportmode){

        UTPackageTexConfig utptctmp;

        for(int i=0;i<al_utxexport2.size();i++){
            utptctmp = al_utxexport2.get(i);
            if(utptctmp.getPackageNameLowerCase().equals(texpacname.toLowerCase())&&(utptctmp.getExport_mode()==exportmode)){
                return utptctmp;
            }

        }

        return null;
    }


    

}
