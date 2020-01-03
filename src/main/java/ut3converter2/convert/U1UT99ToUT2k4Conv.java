/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert;

import java.awt.TrayIcon.MessageType;
import ut3converter2.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import ut3converter2.convert.Level.ut2004.T3dU1UT99ToUT2k4;
import ut3converter2.export.LevelExporter;
import ut3converter2.export.T3DLevelToUTFiles;
import ut3converter2.export.UCExporter;
import ut3converter2.identification.MusicIdentificator;
import ut3converter2.ihm.map.GlobalInstructions;
import ut3converter2.ihm.map.Instruction;
import ut3converter2.ihm.map.Instructions;
import ut3converter2.ihm.myJXHyperlink;
import ut3converter2.tools.*;
import ut3converter2.tools.umc.UMCReader;

/**
 *
 * @author Hyperion
 */
public class U1UT99ToUT2k4Conv extends MapConverter{
    
    ArrayList alunsm;

    public U1UT99ToUT2k4Conv() {
    }

    
    public U1UT99ToUT2k4Conv(File inputfile, int inputgame, int outputgame) {
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

        File tr1 = File.createTempFile(tmpfile_transformed, ".t3d");
        addFilesToDelAfterConvert(tr1);
        tbt = new T3DBrushTranformPerm(new File(outputfolder.getAbsolutePath()+"//myLevel.t3d"), tr1);
        altask.add(tbt);

        File tr2 = File.createTempFile(tmpfile_moverconv, ".t3d");
        addFilesToDelAfterConvert(tr2);
        tlmp = new T3DLvlMoverPort(tr1, tr2);
        altask.add(tlmp);

        File tr3 = File.createTempFile(tmpfile_scaled, ".t3d");
        addFilesToDelAfterConvert(tr3);
        tls = new T3DLevelScaler(tr2, tr3, scalefactor);
        tls.setScalestaticmeshes(false);
        altask.add(tls);

        File tr4 = File.createTempFile(tmpfile_lvlnamechanged, ".t3d");
        addFilesToDelAfterConvert(tr4);
        tlnc = new T3DLevelNameChanger(tr3, tr4);
        altask.add(tlnc);

        File tr5 = File.createTempFile(tmpfile_umcparsed, ".t3d");
        addFilesToDelAfterConvert(tr5);
        umc = new UMCReader(defumcfile, tr4, tr5);
        altask.add(umc);

        t3du1ut99_tout2k4 = new T3dU1UT99ToUT2k4(tr5,super.getFinalt3dfile(),this.input_utgame);
        t3du1ut99_tout2k4.setBShowLog(true);
        altask.add(t3du1ut99_tout2k4);

        fm = new FileMover(terfolder, texfolder);
        altask.add(fm);

        altask.add(new FileRenamer(sndfolder, ".", "_"));
    }
    


    public ArrayList getAlunsm() {
        return alunsm;
    }

    public void setAlunsm(ArrayList alunsm) {
        this.alunsm = alunsm;
    }


    public void Convert(){
        try {
            makeAndCleanFolders();
            prepareUT99Tasks();
            System.out.println(getConversionInfo());
            processTasks();
            ShowResults(getGInstructions());
            deleteEmptyFolders();
            Main.trayIcon.displayMessage("UT3 Converter", "Conversion done!", MessageType.INFO);
        } catch (Exception e) {
        }
        
    }

    private GlobalInstructions getGInstructions()
    {
        GlobalInstructions gi = new GlobalInstructions();
        int numstep=1;
        Instructions inst1 = new Instructions("Step "+numstep+"-Textures");
        inst1.addInstruction(new Instruction(new JLabel("Open the UnrealEditor:"+Main.config.getUEDFilePath(output_utgame))));
        inst1.addInstruction(new Instruction(new myJXHyperlink("Open the Textures Browser",IMAGE_FOLDER + "/ut2004/uedut2k4-texbr.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Do \"File->Import\"",IMAGE_FOLDER + "/ut2004/ut2k4uedteximpot.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Select all .bmp textures only from: "+this.getTexfolder(),IMAGE_FOLDER + "/ut2004/ut2k4uedteximpot2.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Set Package=\"myLevel\" and leave Group field blank",IMAGE_FOLDER + "/importtexUT.png")));
        inst1.addInstruction(new Instruction(new JLabel("     Press \"OK All\"")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("For masked textures:",IMAGE_FOLDER + "/ut2004/ut2k4-translucy.png")));//
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Import the .bmp files into myLevel package from: "+this.getTexfolder(),IMAGE_FOLDER + "/ut2004/ut2k4-importbmp.png")));
        inst1.addInstruction(new Instruction(new JLabel("     Select the imported bmp texture in texture browser")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Open up its properties",IMAGE_FOLDER + "/ut2004/ut2k4-texprop1.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Set bMasked=True and bTwoSided=True (if texture is two sided)",IMAGE_FOLDER + "/ut2004/ut2k4-texprop2.png")));
        inst1.addInstruction(new Instruction(new JLabel("     Close windows and go reselect texture in browser")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Compress texture to DXT3 (will reduce map filesize)",IMAGE_FOLDER + "/ut2004/ut2k4-compressdxt3.png")));

        numstep ++;


        //ut2k4-texprop1
        Instructions inst2 = new Instructions("Step "+numstep+"-Sounds");
        inst2.addInstruction(new Instruction(new JLabel("     Select the Sounds tab")));
        inst2.addInstruction(new Instruction(new JLabel("     Do \"File->Import\"")));
        inst2.addInstruction(new Instruction(new myJXHyperlink("     Select all .wav sounds from: "+this.getSndfolder(),IMAGE_FOLDER + "/ut2004/ut2k4uedimportsnds.png")));
        inst2.addInstruction(new Instruction(new myJXHyperlink("     Set Package=\"myLevel\" and leave Group field blank",IMAGE_FOLDER + "/ut2004/ut2k4uedimportsnds2.png")));
        inst2.addInstruction(new Instruction(new JLabel("     Press \"OK All\"")));
        numstep ++;
        
        Instructions inst3 = new Instructions("Step "+numstep+"-T3D Level Import");
        inst3.addInstruction(new Instruction(new JLabel("In main window:")));
        inst3.addInstruction(new Instruction(new myJXHyperlink("     do \"File\"->\"Import...\"",IMAGE_FOLDER + "/ut2004/ut2k4uedimportT3d1.png")));
        inst3.addInstruction(new Instruction(new myJXHyperlink("     Select the T3D file: "+super.getFinalt3dfile().getAbsolutePath(),IMAGE_FOLDER + "/ut2004/ut2k4uedimportT3d2.png")));
        inst3.addInstruction(new Instruction(new myJXHyperlink("     Don't import into existing map",IMAGE_FOLDER + "/ut2004/ut2k4uedimportT3d3.png")));
        inst3.addInstruction(new Instruction(new JLabel("     Wait while the editor is importing actors ..")));
        inst3.addInstruction(new Instruction(new myJXHyperlink("     do \"Build\"->\"Rebuild Geometry\"",IMAGE_FOLDER + "/ut2004/ut2k4uedbuildgeom.png")));
        numstep ++;
        
        Instructions inst4 = new Instructions("Step "+numstep+" - Movers");
        inst4.addInstruction(new Instruction(new myJXHyperlink("For EACH volume actors in map DO:",IMAGE_FOLDER + "/ut2004/ut2k4-moverconv1.png")));
        inst4.addInstruction(new Instruction(new JLabel("     Right-click on it")));
        inst4.addInstruction(new Instruction(new myJXHyperlink("     Do: \"Convert\"->\"To StaticMesh\"",IMAGE_FOLDER + "/ut2004/ut2k4-moverconv2.png")));
        inst4.addInstruction(new Instruction(new myJXHyperlink("     Set Package=\"myLevel\",set Group (can be blank) and Name as you want",IMAGE_FOLDER + "/ut2004/ut2k4-moverconv3.png")));
        inst4.addInstruction(new Instruction(new JLabel("     Press \"OK\"")));
        inst4.addInstruction(new Instruction(new myJXHyperlink("     Select the nearest Mover actor (StaticMesh) near the volume actor",IMAGE_FOLDER + "/ut2004/ut2k4-selectedmesh.png")));
        inst4.addInstruction(new Instruction(new myJXHyperlink("     Open its properties (press F4)",IMAGE_FOLDER + "/ut2004/ut2k4-moverprops.png")));
        inst4.addInstruction(new Instruction(new myJXHyperlink("     Open the StaticMeshes browser",IMAGE_FOLDER + "/ut2004/ut2k4-smbrowser.png")));
        inst4.addInstruction(new Instruction(new myJXHyperlink("     Go to \"myLevel\" package and select the staticmesh you have just created",IMAGE_FOLDER + "/ut2004/ut2k4-mymeshinbr.png"))); //ut2k4-mymeshinbr
        inst4.addInstruction(new Instruction(new myJXHyperlink("     Go back to the Mover Properties window and click on field \"Display\"->\"StaticMesh\"",IMAGE_FOLDER + "/ut2004/ut2k4-replacesm.png")));
        inst4.addInstruction(new Instruction(new myJXHyperlink("     Press \"Use\", the mover has now the correct staticmesh in the map!",IMAGE_FOLDER + "/ut2004/ut2k4-wootmymover.png")));
        inst4.addInstruction(new Instruction(new JLabel("     Delete the volume actor")));

        //ut2k4-smbrowser
        gi.addInstructions(inst1);
        gi.addInstructions(inst2);
        gi.addInstructions(inst3);
        gi.addInstructions(inst4);
        return gi;
    }
    


    public String showInstructionsHTML()
    {
        String tmp="";
        tmp += "******** INSTRUCTIONS ***********\n";
        tmp +="Open the UnrealEditor:"+Main.config.getUEDFilePath(output_utgame)+"\n";
        tmp +=" Import textures\n";
            tmp +="     Open Generic Browser\n";
            tmp +="     Select the Texture tab\n";
            tmp +="     Do \"File->Import\"\n";
            tmp +="     Select all ."+tc.getOutformat()+" textures only from: "+this.getTexfolder()+"\n";
            tmp +="     Set Package=\"myLevel\" and leave Group field blank\n";
            tmp +="     Press \"OK All\"\n";


        return tmp;
    }


    private void checkErrors()
    {
        if(this.getUnexportedtex().size()>0)
        {
            DefaultTableModel dtm = new DefaultTableModel(new String[]{"Package","Group","Name"},0);
            String t[];
            for(int i=0;i<this.getUnexportedtex().size();i++)
            {
                t =    (String[]) this.getUnexportedtex().get(i);
                dtm.addRow(t);
            }
           // Main.d2.jXTableMisTex.setModel(dtm);
           // Main.d2.jXTableMisTex.setSortOrder(tskmaxval, SortOrder.ASCENDING);
        }
        if(this.getUnexportedsm().size()>0)
        {
            DefaultTableModel dtm = new DefaultTableModel(new String[]{"Package","Group","Name"},0);
            String t[];
            for(int i=0;i<this.getUnexportedsm().size();i++)
            {
                t =    (String[]) this.getUnexportedsm().get(i);
                dtm.addRow(t);
            }
           // Main.d2.jxtableMisSM.setModel(dtm);
           // Main.d2.jxtableMisSM.setSortOrder(tskmaxval, SortOrder.ASCENDING);
        }
        if(this.getAltlsnames()!=null)
        {
            DefaultTableModel dtm = new DefaultTableModel(new String[]{"Material","Terrain Layer Setup Name"},0);
            String t[];
            for(int i=0;i<this.getAltlsnames().size();i++)
            {
                t =    (String[]) this.getAltlsnames().get(i);
                dtm.addRow(t);
            }
           // Main.d2.jxtabtlsnames.setModel(dtm);
           // Main.d2.jxtabtlsnames.setSortOrder(tskmaxval, SortOrder.ASCENDING);
        }
        /*
        Main.d2.jtfinputextrafolder.setText(this.getExtrafolder().getAbsolutePath());
        Main.d2.jpgunconvsm.setMinimum(0);
        Main.d2.jpgunconvsm.setMaximum(this.getNumtotsmconv());
        Main.d2.jpgunconvsm.setValue(this.getNumtotsmconv()-this.getNumtotsmnotconv());
        */
    }



}
