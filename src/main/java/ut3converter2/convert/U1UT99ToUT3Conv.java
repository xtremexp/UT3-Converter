/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert;

import java.awt.TrayIcon.MessageType;
import java.io.*;
import java.util.ArrayList;
import javax.swing.JLabel;
import ut3converter2.*;
import ut3converter2.convert.Level.ut3.T3dUT99toUT3;
import ut3converter2.errors.T3DLevelExportException;
import ut3converter2.export.*;
import ut3converter2.identification.*;
import ut3converter2.ihm.*;
import ut3converter2.ihm.map.*;
import ut3converter2.tools.*;
import ut3converter2.tools.umc.UMCReader;

/**
 *
 * @author Hyperion
 */
public class U1UT99ToUT3Conv extends MapConverter {

    public U1UT99ToUT3Conv() {
    }

    public U1UT99ToUT3Conv(File inputfile, int inputgame, int outputgame) {
        super(inputfile, inputgame, outputgame);
    }

    
    
    public void prepareUT99Tasks() throws FileNotFoundException, IOException
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

        //T3D TRANSFORM DONE IN T3DLevelToUTFiles
        //File tr1 = File.createTempFile(tmpfile_transformed, ".t3d");
        //tbt = new T3DBrushTranformPerm(new File(outputfolder.getAbsolutePath()+"//myLevel.t3d"), tr1);
        //altask.add(tbt);

        File tr2 = File.createTempFile(tmpfile_moverconv, ".t3d");
        
        tlmp = new T3DLvlMoverPort(new File(this.getLogfolder().getAbsolutePath()+File.separator+this.tmpfile_texuvupdated+".t3d"), tr2);
        tlmp.setDefaultSM(defaultsm);
        altask.add(tlmp);

        File tr3 = File.createTempFile(tmpfile_scaled, ".t3d");
        
        tls = new T3DLevelScaler(tr2, tr3, scalefactor);
        tls.setScalestaticmeshes(false);
        altask.add(tls);

        File tr4 = File.createTempFile(tmpfile_lvlnamechanged, ".t3d");
        
        tlnc = new T3DLevelNameChanger(tr3, tr4);
        altask.add(tlnc);

        File tr5 = File.createTempFile(tmpfile_asadd, ".t3d", getLogfolder());

        tasm = new T3DUT99ASMaker(tr4, tr5, 35F, this.getDefaultpackage());
        tasm.setBuseut99sndname(true);
        altask.add(tasm);

        File tr6 = File.createTempFile(tmpfile_umcparsed, ".t3d");
        
        umc = new UMCReader(defumcfile, tr5, tr6);
        altask.add(umc);

        if(this.input_utgame==UTGames.UT99){
            t3dut99ut3 = new T3dUT99toUT3(tr6, getFinalt3dfile(),this);
            altask.add(t3dut99ut3);
        } else if(this.input_utgame==UTGames.U1){
            t3dut99ut3 = new T3dUT99toUT3(tr6, getFinalt3dfile(),this);
            altask.add(t3dut99ut3);
        }
        

        fm = new FileMover(terfolder, texfolder);
        altask.add(fm);

        altask.add(new FileCleaner(sndfolder, FileCleaner.mode_special,"44k"));
        altask.add(new FileRenamer(sndfolder, ".", "_"));

        //addFilesToDelAfterConvert(tr1);
        addFilesToDelAfterConvert(tr2);
        addFilesToDelAfterConvert(tr3);
        addFilesToDelAfterConvert(tr4);
        addFilesToDelAfterConvert(tr5);
        addFilesToDelAfterConvert(tr6);


    }

    

    public void convert() throws Exception
    {
        try {
            makeAndCleanFolders();
            prepareUT99Tasks();
            System.out.println(getConversionInfo());
            processTasks();
            ShowResult sr = new ShowResult(null, true, getGInstructions());
            sr.setSize(400, 500);
            sr.setLocation(100, 100);
            sr.setVisible(true);
            sr.setResizable(true);
            //this.setClosable(true);
            sr.setMaximizable(true);
            sr.setIconifiable(true);
            Display2.jDP.add(sr);
            sr.pack();
            sr.repaint();
            Main.trayIcon.displayMessage("UT3 Converter", "Conversion done!", MessageType.INFO);
        } catch (Exception e) {
            if(e instanceof T3DLevelExportException){
                Main.trayIcon.displayMessage("UT3 Converter", "Conversion failed! Couldn't export T3D level.", MessageType.ERROR);
            } else {
                Main.trayIcon.displayMessage("UT3 Converter", "Conversion failed!", MessageType.ERROR);
            }
        }
        
        deleteEmptyFolders();
    }

    private GlobalInstructions getGInstructions()
    {
        GlobalInstructions gi = new GlobalInstructions();
        int numstep=1;
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
        //gi.addInstructions(inst1);
        numstep ++;
        
        inst1 = new Instructions("Step "+numstep+"-Sounds");
        inst1.addInstruction(new Instruction(new JLabel(" Import sounds:")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Open the Generic Browser",IMAGE_FOLDER + "/ut3/genericbrowser.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Do \"File->Import\"",IMAGE_FOLDER + "/ut3/genericbrowser-import.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Select all .wav sounds from: "+this.getSndfolder(),IMAGE_FOLDER + "/ut3/importsnds.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Set package name the same as the one you set with textures",IMAGE_FOLDER + "/ut3/importsndsoptions.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Set \"bAutoCreateCue\" to True",IMAGE_FOLDER + "/ut3/importsndsoptions.png")));
        inst1.addInstruction(new Instruction(new JLabel("     Press \"OK All\"")));
        inst1.addInstruction(new Instruction(new JLabel("     Save your package")));
        gi.addInstructions(inst1);
        numstep ++;

        inst1 = new Instructions("Step "+numstep+"-T3D Level Import");
        inst1.addInstruction(new Instruction(new JLabel("In main window:")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     do \"File\"->\"Import\"->\"Into existing map",IMAGE_FOLDER + "/ut3/importt3d.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Select the T3D file: "+super.getFinalt3dfile().getAbsolutePath(),IMAGE_FOLDER + "/ut3/importt3d2.png")));
        inst1.addInstruction(new Instruction(new JLabel("     Wait while the editor is importing level data ...")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     do \"Build\"->\"Geometry for Current Level\"",IMAGE_FOLDER + "/ut3/buildgeom.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     do \"Tools\"->\"Clean BSP Materials\"",IMAGE_FOLDER + "/ut3/cleanbspmaterials.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Right click on a surface of the imported level and select all surfaces",IMAGE_FOLDER + "/ut3/selectallsurfaces.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     If you see all level in black use unlit mode",IMAGE_FOLDER + "/ut3/unlitmode.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Check \"Accepts Lights\" only",IMAGE_FOLDER + "/ut3/setsurfacelightning.png")));
        inst1.addInstruction(new Instruction(new JLabel("     Rebuild geometry and you can now see lightened level!")));
        //
        gi.addInstructions(inst1);
        numstep ++;

        inst1 = new Instructions("Step "+numstep+" - Movers");
        inst1.addInstruction(new Instruction(new myJXHyperlink("For EACH volume actors in map DO:",IMAGE_FOLDER + "/ut3/ut3-moverconv1.png")));
        inst1.addInstruction(new Instruction(new JLabel("     Right-click on it")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Do: \"Convert to StaticMesh\"",IMAGE_FOLDER + "/ut3/ut3-moverconv2.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Set Package=\""+this.getDefaultpackage()+"\",set Group (can be empty) and Name as you want",IMAGE_FOLDER + "/ut3/ut3-moverconv3.png")));
        inst1.addInstruction(new Instruction(new JLabel("     Press \"OK\"")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Right-click on the nearest mover actor (red StaticMesh) near the volume actor",IMAGE_FOLDER + "/ut3/ut3-moverconv4.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Go to StaticMeshComponent",IMAGE_FOLDER + "/ut3/ut3-moverconv5.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Open the generic browser and browse for your StaticMesh created",IMAGE_FOLDER + "/ut3/genericbrowser.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Set the new mesh for the mover",IMAGE_FOLDER + "/ut3/ut3-moverconv6.png"))); //ut2k4-mymeshinbr
        inst1.addInstruction(new Instruction(new myJXHyperlink("     The mover has now the correct staticmesh in the map!",IMAGE_FOLDER + "/ut3/ut3-moverconv7.png")));
        inst1.addInstruction(new Instruction(new JLabel("     Delete the volume actor")));
        gi.addInstructions(inst1);

        return gi;
    }
}
