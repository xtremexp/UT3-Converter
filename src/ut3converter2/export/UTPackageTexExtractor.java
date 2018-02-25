/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.export;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ut3converter2.Main;
import ut3converter2.convert.MapConverter;
import ut3converter2.convert.Texture.TextureReplacer;

/**
 *
 * @author Hyperion
 */
public class UTPackageTexExtractor extends UTPackageExtractor{

    
    public final static int mode_uccbatchexport=0;
    public final static int mode_utxextractor=1;

    final static String textype_tga="tga";
    final static String textype_bmp="bmp";
    final static String textype_dds="dds";


    /**
     * Export tex file as Group.TexName.(bmp/tga)
     */
    boolean useut3filename=false;

    ArrayList<UTPackageTexConfig> al_utpactexexport=new ArrayList<UTPackageTexConfig>();

    /**
     *
     * @param alpacexp
     * @param utgame Ex: 0,1,2 (UT,UT2004,...) - Necessary to get UCC path
     */
    public UTPackageTexExtractor(ArrayList<UTPackageTexConfig> alpacexp,int utgame) {
        this.utgame = utgame;
        uccfilepath = Main.config.getUCCFilePath(this.utgame);
        for (int i = 0; i < alpacexp.size(); i++) {
            UTPackageTexConfig uTPackageTexConfig = alpacexp.get(i);
            mergeThisUTPacTexConfig(uTPackageTexConfig);
        }
        //this.al_utpactexexport = alpacexp;
    }

    public UTPackageTexExtractor(int utgame) {
        this.utgame = utgame;
        uccfilepath = Main.config.getUCCFilePath(this.utgame);
    }

    public void reset()
    {
        al_utpactexexport.clear();
    }


    /**
     *
     * @param utptc
     * @param utgame Ex: 0,1,2 (UT,UT2004,...) - Necessary to get UCC path
     */
    public UTPackageTexExtractor(UTPackageTexConfig utptc,int utgame)
    {
        mergeThisUTPacTexConfig(utptc);
        //al_utpactexexport.add(utptc);
        this.utgame = utgame;
        super.uccfilepath = Main.config.getUCCFilePath(utgame);
    }

    /**
     * Converts all files
     */
    public void ExportAll()
    {
        System.out.println("\n*** Exporting textures from UT Packages ***");
        for (int i = 0; i < al_utpactexexport.size(); i++) {
            UTPackageTexConfig uTPackageTexConfig = al_utpactexexport.get(i);
            System.out.print(
                    (i+1)+"/"+(al_utpactexexport.size())
                    +"-"+uTPackageTexConfig.getUtpackagetexfile().getName()
                    + uTPackageTexConfig.getInfo());
            ExportUTPacTexConf(uTPackageTexConfig);
            System.out.println(" ... Done!");
        }
    }

    public boolean bHasExportedFile(File ff)
    {
        for (int i = 0; i < al_utpactexexport.size(); i++) {
            UTPackageTexConfig uTPackageTexConfig = al_utpactexexport.get(i);
            if(uTPackageTexConfig.getUtpackagetexfile().getAbsolutePath().toLowerCase().equals(ff.getAbsolutePath().toLowerCase()))
            {
                return true;
            }
        }
        return false;
    }

    private void ExportUTPacTexConf(UTPackageTexConfig utptc)
    {
        ArrayList log = new ArrayList();
        Runtime run = Runtime.getRuntime();
        String exe_cmd="";
        String use_grouptexname;
        //TODO REMOVE USEUT3FILENAME
        if(utptc.isUseGroupNameTexFile()){use_grouptexname="true";}else{use_grouptexname="false";}
        if(utptc.getExport_mode()==UTPackageTexExtractor.mode_uccbatchexport)
        {
            //Make bat
            exe_cmd = createBatFile(utptc.getUtpackagetexfile(),utptc.getOutputfolder(), utptc.getTextype_export().toArray()).getAbsolutePath();
        }
        //USES UtxExtractor.exe - Exports only as bmp files
        else if(utptc.getExport_mode()==UTPackageTexExtractor.mode_utxextractor)
        {
            exe_cmd = Main.config.getUTXExtractorFilePath().getAbsolutePath()
                    +" \""+utptc.getUtpackagetexfile().getAbsolutePath()+"\""
                    +" \""+utptc.getOutputfolder().getAbsolutePath()+"\""
                    +" "+use_grouptexname;
        }

        try {
            Process pp = run.exec(exe_cmd);
            BufferedReader in =new BufferedReader(new InputStreamReader(pp.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                log.add(line);
                //System.out.println("***"+line);
            }
            int exitVal = pp.waitFor();
            pp.exitValue();
        } catch (Exception e) {
            
        }
        if(utptc.getExport_mode()==UTPackageTexExtractor.mode_uccbatchexport){
            new File(exe_cmd).delete();
        }

    }

    private File createBatFile(File utxfile,File outputfolder,Object[] texformats)
    {
        try {
            File fbat = File.createTempFile("TextureExporter", ".bat");
            BufferedWriter bwr = new BufferedWriter(new FileWriter(fbat));

            bwr.write("copy \""+utxfile.getAbsolutePath()+"\" \""+uccfilepath.getParent()+"\"\n");
            bwr.write(uccfilepath.getAbsolutePath().split("\\\\")[0]+"\n");
            bwr.write("cd \""+uccfilepath.getParent()+"\"\n");
            
            for(int i=0;i<texformats.length;i++)
            {
                bwr.write("ucc.exe batchexport "+utxfile.getName()+" Texture "+texformats[i].toString()+" \""+outputfolder.getAbsolutePath()+"\"\n");
            }

            if(!utxfile.getName().endsWith(".u"))
            {
                bwr.write("del "+utxfile.getName());
            }
            

            bwr.close();
            return fbat;
        } catch (IOException ex) {
            Logger.getLogger(LevelExporter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void setAl_utpactexexport(ArrayList<UTPackageTexConfig> al_utpactexexport) {
        this.al_utpactexexport = al_utpactexexport;
    }

    public void addUTTexPackage_toExport(UTPackageTexConfig utptc)
    {
        mergeThisUTPacTexConfig(utptc);
        //this.al_utpactexexport.add(utptc);
    }

    public void addUTTexPackages_toExport(ArrayList<UTPackageTexConfig> utptc)
    {
        for (int i = 0; i < utptc.size(); i++) {
            UTPackageTexConfig uTPackageTexConfig = utptc.get(i);
            mergeThisUTPacTexConfig(uTPackageTexConfig);
            //this.al_utpactexexport.add(uTPackageTexConfig);
        }
    }

    /**
     * Creates UTPackageTexConfig for each package tex found with texture replacer
     * and merge them with the current list. If some package is ever set for export then
     * it won't be added.
     * @param tr TextureReplacer
     * @param mc MapConverter
     */
    public void mergeUTTexPackagesFromTexReplacer(TextureReplacer tr,MapConverter mc){
        ArrayList<String> myal = tr.getPackagesAnalysed();
        for (int i = 0; i < myal.size(); i++) {
            String uttex_package = myal.get(i);
            UTPackageTexConfig utptc = new UTPackageTexConfig(uttex_package, mc);
            mergeThisUTPacTexConfig(utptc);
        }
    }

    /**
     * Tells if it ever has this package to export
     * @param utptc UTPackageTexConfig
     */
    private void mergeThisUTPacTexConfig(UTPackageTexConfig utptc) {

        boolean addutptc = true;

        for (int i = 0; i < al_utpactexexport.size(); i++) {
            UTPackageTexConfig uTPackageTexConfig = al_utpactexexport.get(i);
            if (!uTPackageTexConfig.getPackageNameLowerCase().equals(utptc.getPackageNameLowerCase())) {
                addutptc |= true;
            } else {
                if (utptc.getExport_mode() == uTPackageTexConfig.getExport_mode()) {
                    addutptc &= false;
                } else {
                    addutptc |= true;
                }
            }
        }
        if(addutptc){
            this.al_utpactexexport.add(utptc);
        }
    }



}
