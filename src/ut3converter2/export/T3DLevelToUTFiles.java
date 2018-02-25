/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import ut3converter2.Main;
import ut3converter2.UTGames;
import ut3converter2.convert.MapConverter;
import ut3converter2.convert.Sound.SoundConverter;
import ut3converter2.convert.StaticMesh.SMT3DtoAse;
import ut3converter2.convert.Texture.TextureConverter;
import ut3converter2.convert.Texture.TextureReplacer;
import ut3converter2.identification.SoundsIdentificator;
import ut3converter2.identification.StaticMeshesIdentificator;
import ut3converter2.identification.TexturesIdentificator;
import ut3converter2.tools.T3DBrushTranformPerm;
import ut3converter2.tools.T3DUVTexUpdater;

/**
 *
 * @author Hyperion
 */
public class T3DLevelToUTFiles {

    MapConverter mc;
    File t3dlvl_utmapfile;
    Set<File> set_filestodel = new HashSet<File>();
    SoundConverter sc;
    SoundsIdentificator si;
    SoundExporter se;
    TexturesIdentificator ti;
    TextureReplacer tr;
    UTPackageTexExtractor utpte;
    StaticMeshesIdentificator smi;
    StaticMeshesExporter sme;
    T3DBrushTranformPerm t3dbt;
    File t3dtexuvupdatedf;
    File t3dtransformed;

    public T3DLevelToUTFiles(MapConverter mc, File t3dlvl_utmapfile) {
        this.mc = mc;
        ti = new TexturesIdentificator(mc);
        this.t3dlvl_utmapfile = t3dlvl_utmapfile;
    }

    public T3DLevelToUTFiles(MapConverter mc) {
        this.mc = mc;
        ti = new TexturesIdentificator(mc);
        this.t3dlvl_utmapfile = new File(mc.getOutputfolder().getAbsolutePath()+File.separator+"myLevel.t3d");
    }

    public void ConvertUTRessources(){
        try {
            IdentifyExportAndDelUnused();
            ConvertSMSndAndTex();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(T3DLevelToUTFiles.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(T3DLevelToUTFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private void IdentifyExportAndDelUnused() throws FileNotFoundException, IOException{
        IdentifyExportAndDelUnusedSounds();
        if((mc.getInput_utgame()==UTGames.U2)||(mc.getInput_utgame()==UTGames.UT2003)||(mc.getInput_utgame()==UTGames.UT2004)){
            IdentifyExportAndDelUnusedStaticMeshes();
        } else {
            
        }
        
        IdentifyExportAndDelUnusedTextures();
    }

    private void ConvertSMSndAndTex() throws IOException{
        if((mc.getInput_utgame()==UTGames.U2)||(mc.getInput_utgame()==UTGames.UT2003)||(mc.getInput_utgame()==UTGames.UT2004)){
            this.ConvertStaticMeshes();
        }
        this.ConvertTextures();
        this.ConvertSounds(); //Only if output game = UT3
    }

    private void IdentifyExportAndDelUnusedStaticMeshes() throws FileNotFoundException, IOException{
        //IDENTIFY
        smi = new StaticMeshesIdentificator(t3dlvl_utmapfile, mc.getInput_utgame());
        smi.identifyStaticMeshes();

        //EXPORTS
        sme = new StaticMeshesExporter(smi.getFileSmFiles(Main.config, mc.getInput_utgame()), mc.getSmfolder(), mc.getInput_utgame());
        sme.addFileToExport(mc.getInputfile()); //Adds Input Map to export SM
        sme.ExportToT3D();
        
        //DELETE UNUSED FILES
        addFilestoDelNow(smi.getFileSm2Del(mc.getSmfolder()));
        ti.addT3DFilesToIdentifyTex(mc.getSmfolder().listFiles(), TexturesIdentificator.T3DTYPE_STATICMESH);
    }

    private void IdentifyExportAndDelUnusedTextures() throws FileNotFoundException, IOException{
        //IDENTIFY
        ti.addT3DFilesToIdentifyTex(new File[]{new File(mc.getOutputfolder().getAbsolutePath()+File.separator+"myLevel.t3d")}, TexturesIdentificator.T3DTYPE_LEVEL);
        if(mc.hasCustomUTTexPacToAnalyze()){
            ti.addUTTexInfoToHMUTPack(mc.getAlutu1customtexpac());
        }
        ti.identifyTextures();
        //System.out.println(ti.getIdentifiedTexFilesToKeep());
        tr = new TextureReplacer(ti.getTexPackageFiles(Main.config, mc.getInput_utgame()), mc);
        tr.analyseAll();
        ti.addIdentifiedTextures(tr.getNewNames(ti.getSet_texfilestokeep()));

        //EXPORTS
            //ADDS Identified textures from TexturesIdentificator
            utpte = new UTPackageTexExtractor(ti.getAl_utxexport2(), mc.getInput_utgame());
        
            //ADDS Identified textures from TextureReplacer
            utpte.mergeUTTexPackagesFromTexReplacer(tr, mc);
        
            //ADDS All textures from Level to export to /Textures folder
                //Exports with normal extractor
                utpte.addUTTexPackage_toExport(new UTPackageTexConfig(mc.getInputfile(), mc));
                //Exports with UCC (for terrains mainly)
                utpte.addUTTexPackage_toExport(new UTPackageTexConfig(mc.getInputfile(), mc, UTPackageTexExtractor.mode_uccbatchexport, new String[]{"bmp","tga"}));
                    //new String[]{".bmp"};

            utpte.ExportAll();
            
            //UV UPDATE before textures will be deleted :/
            if(mc.getOutput_utgame()==UTGames.UT3){
                t3dtexuvupdatedf = new File(mc.getLogfolder().getAbsolutePath()+File.separator+mc.tmpfile_texuvupdated+".t3d");

                if(mc.getInput_utgame()==UTGames.U1||mc.getInput_utgame()==UTGames.UT99){
                    t3dtransformed = new File(mc.getLogfolder().getAbsolutePath()+File.separator+mc.tmpfile_transformed+".t3d");
                    t3dbt = new T3DBrushTranformPerm(mc.getT3dlvlfile(), t3dtransformed);
                    t3dbt.convert();

                    T3DUVTexUpdater tuvu = new T3DUVTexUpdater(t3dtransformed, t3dtexuvupdatedf, new String[]{".bmp"},mc);
                    tuvu.setTr(tr);
                    tuvu.convert();
                } else {

                    T3DUVTexUpdater tuvu = new T3DUVTexUpdater(mc, t3dtexuvupdatedf, new String[]{".bmp"});
                    tuvu.setTr(tr);
                    tuvu.convert();
                }
                
            }
            
            //Moves terrain alpha textures to /TerrainFolder
            moveTerrainFiles();
        //DELETE LATER (we need them for TexUV Update ...)
        addFilestoDelNow(ti.getFileTex2Del());
    }

    private void ConvertStaticMeshes() throws IOException{
        SMT3DtoAse smt3dtoase = new SMT3DtoAse(mc.getSmfolder().listFiles(), false);
        smt3dtoase.setBHasTextureReplacement(true);
        smt3dtoase.setTr(tr);
        smt3dtoase.setIsUT3Mesh(true);
        smt3dtoase.setTexprefix(mc.getDefaultpackage()+".");
        smt3dtoase.setDeleteSourceFilesAfterConversion(true);
        smt3dtoase.export2Ase();
    }

    private void ConvertTextures(){
        TextureConverter tc = new TextureConverter(mc);
        tc.setDeleteSourceFilesAfterConversion(true);
        tc.convertAll();
    }

    private void addFilestoDelNow(File[] filestodelnow){
        for (int i = 0; i < filestodelnow.length; i++) {
            File file = filestodelnow[i];
            file.delete();
        }
    }
    private void addFilestoDelLater(File[] filestodellater){
        for (int i = 0; i < filestodellater.length; i++) {
            File file = filestodellater[i];
            set_filestodel.add(file);
        }
    }

    public TexturesIdentificator getTi() {
        return ti;
    }

    public TextureReplacer getTr() {
        return tr;
    }

    private void moveTerrainFiles(){
        File texfiletomove;
        File newtexfile;
        for (String texname : ti.getSet_texfilestertomove()) {
            texfiletomove = new File(mc.getTexfolder().getAbsolutePath()+File.separator+texname);
            newtexfile = new File(mc.getTerfolder()+File.separator+texname);
            texfiletomove.renameTo(newtexfile);
        }
    }

    public void deleteFiles(){
        Iterator<File> itf = set_filestodel.iterator();
        while(itf.hasNext()){
            itf.next().delete();
        }
    }

    public File getT3dtexuvupdatedf() {
        return t3dtexuvupdatedf;
    }

    private void IdentifyExportAndDelUnusedSounds() throws FileNotFoundException, IOException{
        //IDENTIFY SOUNDS
        si = new SoundsIdentificator(mc);
        si.identifyLvlSounds();

        //EXPORT SOUNDS
        se = new SoundExporter(si.getSoundPackageFilesToExport(), mc);
        se.ExportToWav();

        //DELETE UNUSED SOUNDS
        addFilestoDelNow(si.getFileSnd2Del());
    }

    private void ConvertSounds(){
        if(mc.getOutput_utgame()==UTGames.UT3){
            sc = new SoundConverter(mc.getSndfolder());
            sc.convertAll();
        }
    }
    
}
