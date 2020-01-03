/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert;

import java.awt.TrayIcon.MessageType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JLabel;
import ut3converter2.Main;
import ut3converter2.convert.Level.ut3.DecoLayer;
import ut3converter2.convert.Level.ut3.T3dU2toUT3;
import ut3converter2.convert.Level.ut3.Terrain;
import ut3converter2.convert.Level.ut3.TerrainLayer;
import ut3converter2.export.LevelExporter;
import ut3converter2.export.T3DLevelToUTFiles;
import ut3converter2.export.TextureExporter;
import ut3converter2.export.UCExporter;
import ut3converter2.identification.MusicIdentificator;
import ut3converter2.ihm.map.GlobalInstructions;
import ut3converter2.ihm.map.Instruction;
import ut3converter2.ihm.map.Instructions;
import ut3converter2.ihm.myJTextField;
import ut3converter2.ihm.myJXHyperlink;
import ut3converter2.tools.FileCleaner;
import ut3converter2.tools.FileRenamer;
import ut3converter2.tools.T3DLevelNameChanger;
import ut3converter2.tools.umc.UMCReader;

/**
 *
 * @author Hyperion
 */
public class U2ToUT3Conv extends MapConverter{

    TextureExporter televel;
    ArrayList alunsm;

    public U2ToUT3Conv() {
    }

    public U2ToUT3Conv(File inputfile, int inputgame, int outputgame) {
        super(inputfile, inputgame, outputgame);
    }

    

    public void convert() throws Exception {
        makeAndCleanFolders();
        //Main.config.getMclog().log(Level.INFO, "Input Game: " + UTGames.getUTGame(this.input_utgame) + " Output Game: " + UTGames.getUTGame(this.output_utgame));
        //Main.config.getMclog().log(Level.INFO, "Map Conversion: "+this.getInputfile().getName());
        prepareTasks();
        System.out.println(getConversionInfo());
        processTasks();
        super.setBhasterrain(super.t3du2ut3.isBhasterrain());
        ShowResults(getGInstructions());
        writeInstructions();
        Main.trayIcon.displayMessage("UT3 Converter", "Conversion done!", MessageType.INFO);
        deleteEmptyFolders();
    }

    public void prepareTasks() throws FileNotFoundException, IOException
    {
        altask = new ArrayList();

        le = new LevelExporter(this);
        le.setBShowLog(true);
        altask.add(le);

        uce = new UCExporter(this);
        altask.add(uce);

        mi = new MusicIdentificator(this);
        altask.add(mi);

        tdltutf = new T3DLevelToUTFiles(this);
        altask.add(tdltutf);


        //TexUV Update in T3DLevelToUTFiles

        File tr1 = File.createTempFile(this.tmpfile_umcparsed, ".t3d",this.logfolder);

        File ftuv = new File(this.getLogfolder().getAbsolutePath()+File.separator+this.tmpfile_texuvupdated+".t3d");
        umc = new UMCReader("UT2k4ToUT3-Default.umc", ftuv, tr1);
        altask.add(umc);

        File tr2 = File.createTempFile("UT3T3DConverted", ".t3d",this.logfolder);
        t3du2ut3 = new T3dU2toUT3(tr1, tr2, this, this.defaultpackage);
        altask.add(t3du2ut3);

        altask.add(new T3DLevelNameChanger(tr2, super.getFinalt3dfile(),this.getDefaultpackage()));

        altask.add(new FileRenamer(texfolder, ".", "_"));
        altask.add(new FileCleaner(smfolder, ".t3d"));
        altask.add(new FileRenamer(smfolder, ".", "_"));
        altask.add(new FileCleaner(sndfolder, FileCleaner.mode_special,"44k"));
        altask.add(new FileRenamer(sndfolder, ".", "_"));

        addFilesToDelAfterConvert(tr1);
        addFilesToDelAfterConvert(tr2);
        addFilesToDelAfterConvert(ftuv);
    }

    public ArrayList getAlunsm() {
        return alunsm;
    }

    public void setAlunsm(ArrayList alunsm) {
        this.alunsm = alunsm;
    }

    private GlobalInstructions getGInstructions()
    {
        gi = new GlobalInstructions();
        Instructions inst0 = new Instructions("Summary");
        inst0.addInstruction(new Instruction(new JLabel("UT2004 input file: "+this.getInpututxxmapname())));
        //gi.addInstructions(inst0);
        int numstep=1;
        //newmapsub
        Instructions inst1 = new Instructions("Step "+numstep+"-Textures");
        inst1.addInstruction(new Instruction(new JLabel("Open the Unreal Editor:"+Main.config.getUEDFilePath(this.output_utgame))));
        inst1.addInstruction(new Instruction(new JLabel("Create new map in subtractive mode:")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Do \"File->New\"->Geometry Style=Subtractive",IMAGE_FOLDER + "/ut3/newmapsub.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("Open the Generic Browser",IMAGE_FOLDER + "/ut3/genericbrowser.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Do \"File->Import\"",IMAGE_FOLDER + "/ut3/genericbrowser-import.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Select all .psd textures only from: "+this.getTexfolder(),IMAGE_FOLDER + "/ut3/importtgatex.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Set Package name=\""+this.getDefaultpackage()+"\" and leave Group field blank",IMAGE_FOLDER + "/ut3/importtexoptions.png")));
        inst1.addInstruction(new Instruction(new JLabel("     Set \"Create material?\" to True")));
        inst1.addInstruction(new Instruction(new JLabel("     Press \"OK All\" and wait while textures are being imported (can take about 5 min)")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("Select in the generic browser the new package you have just created ",IMAGE_FOLDER + "/ut3/savetexpackage.png")));//
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Do \"File->Save\"",IMAGE_FOLDER + "/ut3/savetexpackage.png")));//
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Save the .upk file wherever you want",IMAGE_FOLDER + "/ut3/savetexpackage2.png")));//
        gi.addInstructions(inst1);
        numstep ++;

        inst1 = new Instructions("Step "+numstep+"-Alpha Textures");
        inst1.addInstruction(new Instruction(new myJXHyperlink(" For textures with alpha layer (e.g.: trees,plants,..):",IMAGE_FOLDER + "/ut3/alphabadgood.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Go to its properties (double click)",IMAGE_FOLDER + "/ut3/setalphatex.png")));
        inst1.addInstruction(new Instruction(new JLabel("     Link alpha channel to Opacity")));
        inst1.addInstruction(new Instruction(new JLabel("     Set BlendMode = Blend_Translucent")));
        inst1.addInstruction(new Instruction(new JLabel("     Close window and save changes.")));
        gi.addInstructions(inst1);
        numstep ++;

        inst1 = new Instructions("Step "+numstep+"-StaticMeshes");
        inst1.addInstruction(new Instruction(new JLabel(" Import staticmeshes files:")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Open the Generic Browser",IMAGE_FOLDER + "/ut3/genericbrowser.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Do \"File->Import\"",IMAGE_FOLDER + "/ut3/genericbrowser-import.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Select all .ase staticmeshes only from: "+this.getSmfolder(),IMAGE_FOLDER + "/ut3/importsm.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Set package name the same as the one you set with textures",IMAGE_FOLDER + "/ut3/importsmoptions.png")));
        inst1.addInstruction(new Instruction(new JLabel("     Press \"OK All\"")));
        gi.addInstructions(inst1);
        numstep ++;

        Instructions inst2 = new Instructions("Step "+numstep+"-Sounds");
        inst2.addInstruction(new Instruction(new JLabel(" Import sounds:")));
        inst2.addInstruction(new Instruction(new myJXHyperlink("     Open the Generic Browser",IMAGE_FOLDER + "/ut3/genericbrowser.png")));
        inst2.addInstruction(new Instruction(new myJXHyperlink("     Do \"File->Import\"",IMAGE_FOLDER + "/ut3/genericbrowser-import.png")));
        inst2.addInstruction(new Instruction(new myJXHyperlink("     Select all .wav sounds from: "+this.getSndfolder(),IMAGE_FOLDER + "/ut3/importsnds.png")));
        inst2.addInstruction(new Instruction(new myJXHyperlink("     Set package name the same as the one you set with textures",IMAGE_FOLDER + "/ut3/importsndsoptions.png")));
        inst2.addInstruction(new Instruction(new myJXHyperlink("     Set \"bAutoCreateCue\" to True",IMAGE_FOLDER + "/ut3/importsndsoptions.png")));
        inst2.addInstruction(new Instruction(new JLabel("     Press \"OK All\"")));
        inst2.addInstruction(new Instruction(new JLabel("     Save your package")));
        gi.addInstructions(inst2);
        numstep ++;


        //this.
        if(t3du2ut3.isBhasterrain())
        {
            //newfile
            Instructions inst4;
            ArrayList al = t3du2ut3.getAltlsname();
            for(int i=0;i<alterrainsdetected.size();i++)
            {
                Terrain ter = alterrainsdetected.get(i);
                ArrayList <TerrainLayer>alterlayers = ter.getTerrainLayers();
                for(int j=0;j<alterlayers.size();j++)
                {
                    TerrainLayer tl = alterlayers.get(j);

                    inst4 = new Instructions("Step "+numstep+"- Terrain "+(j+1)+"("+j+"/"+alterlayers.size()+")");
                    inst4.addInstruction(new Instruction(new myJXHyperlink("     Open the Generic Browser",IMAGE_FOLDER + "/ut3/genericbrowser.png")));
                    inst4.addInstruction(new Instruction(new myJXHyperlink("     Do \"File->New\",select Factory=\"TerrainMaterial\"",IMAGE_FOLDER + "/ut3/newfile.png")));
                    inst4.addInstruction(new Instruction(new myJXHyperlink("     Set \"Package\" name the same as the one you set with textures,set \"Name\" as you want",IMAGE_FOLDER + "/ut3/newtermat.png")));
                    inst4.addInstruction(new Instruction(new myJXHyperlink("     Go to the terrain material properties you have created in your package (use \"list view\" to find it quicker)",IMAGE_FOLDER + "/ut3/newtermat.png")));
                    inst4.addInstruction(new Instruction(new myJTextField("     Set \"MappingScale\" value="+tl.getGlobalUVScale())));
                    String mappingtype = tl.getTls().getMaterial().getMappingtype();
                    if(!mappingtype.equals(tl.getTls().getMaterial().mappingtype_TMT_Auto))
                    {
                        inst4.addInstruction(new Instruction(new JLabel("     Set MappingType= \""+mappingtype)));
                    }
                    inst4.addInstruction(new Instruction(new myJXHyperlink("     Select the material: \""+tl.getTLSTexName()+"\" in the generic browser",IMAGE_FOLDER + "/ut3/settermap.png")));
                    inst4.addInstruction(new Instruction(new myJXHyperlink("     In the terrain material properties set Material to: \""+tl.getTLSTexName()+"\"",IMAGE_FOLDER + "/ut3/settermap.png")));
                    inst4.addInstruction(new Instruction(new myJXHyperlink("     Do \"File->New\",select Factory=\"TerrainLayerSetup",IMAGE_FOLDER + "/ut3/newtls.png")));
                    inst4.addInstruction(new Instruction(new myJTextField("     Set \"Name\"= \""+tl.getTLSName()+"\" and press \"OK\"")));
                    inst4.addInstruction(new Instruction(new JLabel("     Go to its properties (Generic browser,your package ..)")));
                    inst4.addInstruction(new Instruction(new JLabel("     Expand its properties (green cross+)")));
                    inst4.addInstruction(new Instruction(new JLabel("     Select the TerrainMaterial created in the generic browser")));
                    inst4.addInstruction(new Instruction(new myJXHyperlink("     Set Material value in TerrainLayerSetup properties to this terrain material",IMAGE_FOLDER + "/ut3/settls2.png")));
                    gi.addInstructions(inst4);
                }

                ArrayList <DecoLayer>aldecolayers = ter.getAldecolayers();
                for(int j=0;j<aldecolayers.size();j++)
                {
                    DecoLayer dl = aldecolayers.get(j);
                    inst4 = new Instructions("Step "+numstep+"- Terrain Foliage "+(j+1)+"("+j+"/"+aldecolayers.size()+")");
                    inst4.addInstruction(new Instruction(new myJXHyperlink("     Open the Generic Browser",IMAGE_FOLDER + "/ut3/genericbrowser.png")));
                    inst4.addInstruction(new Instruction(new myJXHyperlink("     Do \"File->New\",select Factory=\"TerrainMaterial\"",IMAGE_FOLDER + "/ut3/newfile.png")));
                    inst4.addInstruction(new Instruction(new myJXHyperlink("     Set \"Package\" name the same as the one you set with textures,set \"Name\" as you want",IMAGE_FOLDER + "/ut3/newtermat.png")));
                    inst4.addInstruction(new Instruction(new myJXHyperlink("     Go to the terrain material properties you have created in your package (use \"list view\" to find it quicker)",IMAGE_FOLDER + "/ut3/newtermat.png")));
                    inst4.addInstruction(new Instruction(new myJXHyperlink("     Add some foliage Mesh",IMAGE_FOLDER + "/ut3/tmfoliageadd.png")));
                    inst4.addInstruction(new Instruction(new myJTextField("     Select staticmesh "+dl.getTls().getMaterial().getFm().getStaticmesh()+" from generic browser and apply it")));
                    inst4.addInstruction(new Instruction(new myJTextField("     Set density= "+dl.getTls().getMaterial().getFm().getDensity()
                            +" Set MinTransitionRadius= "+dl.getTls().getMaterial().getFm().getMintransitionradius()
                            +" Set MaxDrawRadius= "+dl.getTls().getMaterial().getFm().getMaxdrawradius())));
                    inst4.addInstruction(new Instruction(new myJTextField("     Set MinScale= "+dl.getTls().getMaterial().getFm().getMinscale()
                            +" Set MaxScale= "+dl.getTls().getMaterial().getFm().getMaxscale())));
                    //+" Set MaxDrawRadius= "+dl.getTls().getMaterial().getFm().getMaxdrawradius()
                    inst4.addInstruction(new Instruction(new myJTextField("     Set \"MappingScale\" value="+dl.getTls().getMaterial().getMappingscale())));
                    inst4.addInstruction(new Instruction(new myJXHyperlink("     Do \"File->New\",select Factory=\"TerrainLayerSetup",IMAGE_FOLDER + "/ut3/newtls.png")));
                    inst4.addInstruction(new Instruction(new myJTextField("     Set \"Name\"= \""+dl.getTls().getTlsname()+"\" and press \"OK\"")));
                    inst4.addInstruction(new Instruction(new JLabel("     Go to its properties (Generic browser,your package ..)")));
                    inst4.addInstruction(new Instruction(new JLabel("     Expand its properties (green cross+)")));
                    inst4.addInstruction(new Instruction(new JLabel("     Select the TerrainMaterial created in the generic browser")));
                    inst4.addInstruction(new Instruction(new myJXHyperlink("     Set Material value in TerrainLayerSetup properties to this terrain material",IMAGE_FOLDER + "/ut3/settls2.png")));
                    gi.addInstructions(inst4);
                }
            }


            numstep ++;
        }
        Instructions inst3 = new Instructions("Step "+numstep+"- Level");
        inst3.addInstruction(new Instruction(new JLabel("In main window:")));
        inst3.addInstruction(new Instruction(new myJXHyperlink("     do \"File\"->\"Import\"->\"Into Existing Map ...\"",IMAGE_FOLDER + "/ut3/importt3d.png")));
        inst3.addInstruction(new Instruction(new myJXHyperlink("     Select the T3D file: "+super.getFinalt3dfile().getAbsolutePath(),IMAGE_FOLDER + "/ut3/importt3d2.png")));
        inst3.addInstruction(new Instruction(new JLabel("     Click on \"Open\" button")));
        inst3.addInstruction(new Instruction(new JLabel("     Wait while the editor is importing actors ..")));
        inst3.addInstruction(new Instruction(new JLabel("     Save your map")));
        inst3.addInstruction(new Instruction(new myJXHyperlink("     do \"Build\"->\"Build All.\" (this can take up to 30-45 min!)",IMAGE_FOLDER + "/ut3/buildgeom.png")));
        inst3.addInstruction(new Instruction(new JLabel("     Save your map")));
        gi.addInstructions(inst3);

        return gi;
    }
}
