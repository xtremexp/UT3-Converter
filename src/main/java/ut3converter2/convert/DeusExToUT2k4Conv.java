/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert;

import ut3converter2.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import ut3converter2.convert.Level.SupportedClasses;
import ut3converter2.convert.Level.ut2004.T3dU1UT99ToUT2k4;
import ut3converter2.convert.Level.ut3.T3dUT2k4toUT3;
import ut3converter2.convert.Sound.SoundConverter;
import ut3converter2.convert.Texture.TextureConverter;
import ut3converter2.export.LevelExporter;
import ut3converter2.export.SoundExporter;
import ut3converter2.export.TextureExporter;
import ut3converter2.identification.SoundsIdentificator;
import ut3converter2.identification.TexturesIdentificator;
import ut3converter2.ihm.Display2;
import ut3converter2.ihm.map.GlobalInstructions;
import ut3converter2.ihm.map.Instruction;
import ut3converter2.ihm.map.Instructions;
import ut3converter2.ihm.map.ShowResult;
import ut3converter2.ihm.myJXHyperlink;
import ut3converter2.tools.*;
import ut3converter2.tools.umc.UMCReader;

/**
 *
 * @author Hyperion
 */
public class DeusExToUT2k4Conv extends MapConverter{

    public static final int inputgame= UTGames.DeusEx;
    public static final int outputgame= UTGames.UT2004;

    TextureExporter televel;
    ArrayList alunsm;

    public DeusExToUT2k4Conv() {
    }

    public DeusExToUT2k4Conv(File inputfile, int inputgame, int outputgame) {
        super(inputfile, inputgame, outputgame);
    }

    public ArrayList getAlunsm() {
        return alunsm;
    }

    public void setAlunsm(ArrayList alunsm) {
        this.alunsm = alunsm;
    }


    public void convert() throws Exception
    {
        if(output_utgame==UTGames.UT2004)
        {
           prepareUT99Tasks();
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
        }
        else if(output_utgame==UTGames.UT3)
        {

        }


    }

    private GlobalInstructions getGInstructions()
    {
        GlobalInstructions gi = new GlobalInstructions();
        /*
         *         tmp +=" Import textures\n";
            tmp +="     Open Generic Browser\n";
            tmp +="     Select the Texture tab\n";
            tmp +="     Do \"File->Import\"\n";
            tmp +="     Select all ."+tc.getOutformat()+" textures only from: "+this.getTexfolder()+"\n";
            tmp +="     Set Package=\"myLevel\" and leave Group field blank\n";
            tmp +="     Press \"OK All\"\n";
         * */
        Instructions inst1 = new Instructions("Step 1");
        inst1.addInstruction(new Instruction(new JLabel("Open the UnrealEditor:"+Main.config.getUEDFilePath(DeusExToUT2k4Conv.outputgame))));
        inst1.addInstruction(new Instruction(new myJXHyperlink("Open the Textures Browser","/ut3converter2/ihm/images/ut2004/uedut2k4-texbr.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Do \"File->Import\"","/ut3converter2/ihm/images/ut2004/ut2k4uedteximpot.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Select all ."+tc.getOutformat()+" textures only from: "+this.getTexfolder(),"/ut3converter2/ihm/images/ut2004/ut2k4uedteximpot2.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Set Package=\"myLevel\" and leave Group field blank","/ut3converter2/ihm/images/importtexUT.png")));
        inst1.addInstruction(new Instruction(new JLabel("     Press \"OK All\"")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("For textures with alpha-layer","/ut3converter2/ihm/images/ut2004/ut2k4-translucy.png")));//
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Import the .bmp files into myLevel package from: "+this.getTexfolder(),"/ut3converter2/ihm/images/ut2004/ut2k4-importbmp.png")));
        inst1.addInstruction(new Instruction(new JLabel("     Select the imported bmp texture in texture browser")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Open up its properties","/ut3converter2/ihm/images/ut2004/ut2k4-texprop1.png")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Set bMasked=True and bTwoSided=True (if texture is two sided)","/ut3converter2/ihm/images/ut2004/ut2k4-texprop2.png")));
        inst1.addInstruction(new Instruction(new JLabel("     Close windows and go reselect texture in browser")));
        inst1.addInstruction(new Instruction(new myJXHyperlink("     Compress texture to DXT3 (will reduce map filesize)","/ut3converter2/ihm/images/ut2004/ut2k4-compressdxt3.png")));



        //ut2k4-texprop1
        Instructions inst2 = new Instructions("Step 2");
        inst2.addInstruction(new Instruction(new JLabel(" Import sounds")));
        inst2.addInstruction(new Instruction(new JLabel("     Select the Sounds tab")));
        inst2.addInstruction(new Instruction(new JLabel("     Do \"File->Import\"")));
        inst2.addInstruction(new Instruction(new myJXHyperlink("     Select all .wav sounds from: "+this.getSndfolder(),"/ut3converter2/ihm/images/ut2004/ut2k4uedimportsnds.png")));
        inst2.addInstruction(new Instruction(new myJXHyperlink("     Set Package=\"myLevel\" and leave Group field blank","/ut3converter2/ihm/images/ut2004/ut2k4uedimportsnds2.png")));
        inst2.addInstruction(new Instruction(new JLabel("     Press \"OK All\"")));
        //ut2k4uedimportsnds.png ut2k4uedimportT3d1

        Instructions inst3 = new Instructions("Step 3 - T3D Level Import");
        inst3.addInstruction(new Instruction(new JLabel("In main window:")));
        inst3.addInstruction(new Instruction(new myJXHyperlink("     do \"File\"->\"Import...\"","/ut3converter2/ihm/images/ut2004/ut2k4uedimportT3d1.png")));
        inst3.addInstruction(new Instruction(new myJXHyperlink("     Select the T3D file: "+super.getFinalt3dfile().getAbsolutePath(),"/ut3converter2/ihm/images/ut2004/ut2k4uedimportT3d2.png")));
        inst3.addInstruction(new Instruction(new myJXHyperlink("     Don't import into existing map","/ut3converter2/ihm/images/ut2004/ut2k4uedimportT3d3.png")));
        inst3.addInstruction(new Instruction(new JLabel("     Wait while the editor is importing actors ..")));
        inst3.addInstruction(new Instruction(new myJXHyperlink("     do \"Build\"->\"Rebuild Geometry\"","/ut3converter2/ihm/images/ut2004/ut2k4uedbuildgeom.png")));

        Instructions inst4 = new Instructions("Step 4 - Movers");
        inst4.addInstruction(new Instruction(new myJXHyperlink("For EACH volume actors in map DO:","/ut3converter2/ihm/images/ut2004/ut2k4-moverconv1.png")));
        inst4.addInstruction(new Instruction(new JLabel("     Right-click on it")));
        inst4.addInstruction(new Instruction(new myJXHyperlink("     Do: \"Convert\"->\"To StaticMesh\"","/ut3converter2/ihm/images/ut2004/ut2k4-moverconv2.png")));
        inst4.addInstruction(new Instruction(new myJXHyperlink("     Set Package=\"myLevel\",set Group (can be blank) and Name as you want","/ut3converter2/ihm/images/ut2004/ut2k4-moverconv3.png")));
        inst4.addInstruction(new Instruction(new JLabel("     Press \"OK\"")));
        inst4.addInstruction(new Instruction(new myJXHyperlink("     Select the nearest Mover actor (StaticMesh) near the volume actor","/ut3converter2/ihm/images/ut2004/ut2k4-selectedmesh.png")));
        inst4.addInstruction(new Instruction(new myJXHyperlink("     Open its properties (press F4)","/ut3converter2/ihm/images/ut2004/ut2k4-moverprops.png")));
        inst4.addInstruction(new Instruction(new myJXHyperlink("     Open the StaticMeshes browser","/ut3converter2/ihm/images/ut2004/ut2k4-smbrowser.png")));
        inst4.addInstruction(new Instruction(new myJXHyperlink("     Go to \"myLevel\" package and select the staticmesh you have just created","/ut3converter2/ihm/images/ut2004/ut2k4-mymeshinbr.png"))); //ut2k4-mymeshinbr
        inst4.addInstruction(new Instruction(new myJXHyperlink("     Go back to the Mover Properties window and click on field \"Display\"->\"StaticMesh\"","/ut3converter2/ihm/images/ut2004/ut2k4-replacesm.png")));
        inst4.addInstruction(new Instruction(new myJXHyperlink("     Press \"Use\", the mover has now the correct staticmesh in the map!","/ut3converter2/ihm/images/ut2004/ut2k4-wootmymover.png")));
        inst4.addInstruction(new Instruction(new JLabel("     Delete the volume actor")));

        //ut2k4-smbrowser
        gi.addInstructions(inst1);
        gi.addInstructions(inst2);
        gi.addInstructions(inst3);
        gi.addInstructions(inst4);
        return gi;
    }

    public void prepareUT99Tasks() throws FileNotFoundException, IOException
    {
        File installfolder =Installation.getInstallDirectory(Main.class);
        File utpac=new File(installfolder.getAbsolutePath()+"//conf//"+defpacnamedat);
        altask = new ArrayList();

        le = new LevelExporter(input_mapfile, outputfolder, uccfilepath);
        le.setBShowLog(true);
        altask.add(le);



        ArrayList altmp = new ArrayList();
        altmp.add(new Object[]{t3dlvlfile,true});
        ti = new TexturesIdentificator(altmp,this.getTexfolder(),TexturesIdentificator.textype_bmp,utpac,this);
        ti.setBShowLog(true);
        altask.add(ti);

        te = new TextureExporter(null,new String []{"dds"}, this);
        te.setBShowLog(true);
        altask.add(te);


        altask.add(new TextureExporter(new File[]{input_mapfile},new String []{"pcx","bmp"}, this,terfolder));



        altask.add(new FileCleaner(ti, this));


        altask.add(new FileCleaner(terfolder, true));

        tc = new TextureConverter(texfolder,"dds",TextureConverter.mode_folder);
        tc.setBShowLog(true);
        altask.add(tc);


        altask.add(new TextureConverter(terfolder,"dds",TextureConverter.mode_folder));


        //altask.add(new FileCleaner(texfolder, ".bmp"));



        altask.add(new SoundsIdentificator(t3dlvlfile,this.getSndfolder(),this.input_utgame));

        altask.add(new SoundExporter(null, this));


        altask.add(new FileCleaner(si, this));

        File tr1 = File.createTempFile("Transformed", ".t3d");
        addFilesToDelAfterConvert(tr1);
        tbt = new T3DBrushTranformPerm(new File(outputfolder.getAbsolutePath()+"//myLevel.t3d"), tr1);
        altask.add(tbt);

        File tr2 = File.createTempFile("MoverSM", ".t3d");
        addFilesToDelAfterConvert(tr2);
        tlmp = new T3DLvlMoverPort(tr1, tr2);
        altask.add(tlmp);

        File tr3 = File.createTempFile("T3DScaled", ".t3d");
        addFilesToDelAfterConvert(tr3);
        tls = new T3DLevelScaler(tr2, tr3, scalefactor);
        tls.setScalestaticmeshes(false);
        altask.add(tls);

        File tr4 = File.createTempFile("T3DLevelNameCh", ".t3d");
        addFilesToDelAfterConvert(tr4);
        tlnc = new T3DLevelNameChanger(tr3, tr4);
        altask.add(tlnc);

        File tr5 = File.createTempFile("T3DLevelNameCh", ".t3d");
        addFilesToDelAfterConvert(tr5);
        umc = new UMCReader(defumcfile, tr4, tr5);

        umc = new UMCReader(defumcfile, tr4, tr5);
        altask.add(umc);

        
        t3du1ut99_tout2k4 = new T3dU1UT99ToUT2k4(tr5,super.getFinalt3dfile(),this.input_utgame);
        t3du1ut99_tout2k4.setBShowLog(true);
        altask.add(t3du1ut99_tout2k4);

        FileMover fm = new FileMover(terfolder, texfolder);
        altask.add(fm);

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

    public void processTasks() throws Exception, FileNotFoundException, IOException
    {
        Object obj;
        for(int i=0;i<altask.size();i++)
        {


            obj = altask.get(i);
            if(obj instanceof LevelExporter)
            {
                le = (LevelExporter) obj;
                le.exportT3DMap();
            }
            else if(obj instanceof TexturesIdentificator)
            {
                ti = (TexturesIdentificator) obj;
                ti.identifyTextures();
            }
            else if(obj instanceof TextureExporter)
            {

                te = (TextureExporter) obj;
                if(te.getUtxfiles()==null)
                {
                  te.setUtxfiles(ti.getTexPackageFiles(Main.config, DeusExToUT2k4Conv.inputgame));
                }
                te.ExportTex();
                if(this.unexportedtex==null)
                {
                   //this.unexportedtex = ti.getUnexportedTex(this.texfolder);
                }

            }
            else if(obj instanceof TextureConverter)
            {
                tc = (TextureConverter) obj;
                tc.convertAll();
            }
            else if(obj instanceof FileCleaner)
            {
                fc = (FileCleaner) obj;
                if(fc.getTi()!=null){fc.setFiles2del(ti.getFileTex2Del());fc.setTi(null);}
                if(fc.getSi()!=null)
                {
                    fc.setFiles2del(si.getFileSnd2Del());
                }
                fc.clean();
            }
            else if(obj instanceof FileRenamer)
            {
                fr = (FileRenamer) obj;
                fr.renameAll();
            }
            else if(obj instanceof T3dUT2k4toUT3)
            {
                T3dUT2k4toUT3 t3d = (T3dUT2k4toUT3) obj;
                t3d.convert();
            }
            else if(obj instanceof SoundsIdentificator)
            {
                si = (SoundsIdentificator) obj;
                si.identifyLvlSounds();
            }
            else if(obj instanceof SoundConverter)
            {
                sndc = (SoundConverter) obj;
                sndc.convertAll();
            }
            else if(obj instanceof FileMover)
            {
                fm = (FileMover) obj;
                fm.move();
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
            else if(obj instanceof UMCReader)
            {
                umc = (UMCReader) obj;
                umc.replace();
            }
            else if(obj instanceof T3dU1UT99ToUT2k4)
            {
                t3du1ut99_tout2k4 = (T3dU1UT99ToUT2k4) obj;
                t3du1ut99_tout2k4.convert();
            }
            else if(obj instanceof SoundExporter)
            {
                se = (SoundExporter) obj;
                if(si!=null)
                {
                   // se.setUaxfiles(si.getSoundPackageFilesToExport(Main.config, DeusExToUT2k4Conv.inputgame));
                    se.ExportToWav();
                }
            }
        }
        delUnusedFilesAfterConvert();
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


    public void run()
    {
        try {
            this.convert();
        } catch (Exception ex) {
            Logger.getLogger(U1UT99ToUT2k4Conv.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}

