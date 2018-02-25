/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ut3converter2.export;

import java.io.File;
import java.util.Arrays;
import java.util.TreeSet;
import ut3converter2.Main;
import ut3converter2.UTGames;
import ut3converter2.convert.MapConverter;

/**
 *
 * @author Hyperion
 */
public class UTPackageTexConfig {

    /**
     * Export tex file as Group.TexName.(bmp/tga)
     */
    boolean usegroupnametexfile = false;
    boolean usemergealpha=false;
    File utpackagetexfile;
    File outputfolder;
    TreeSet<String> textype_export  = new TreeSet<String>();
    int export_mode;
    int utgame;

    /**
     *
     * @param utpackagetexfile mytex.utx, mycorepackage.u
     * @param outputfolder
     * @param textype_export [.bmp,.tga] ...
     * @param export_mode UCC or UTXExtractor
     */
    public UTPackageTexConfig(File utpackagetexfile, File outputfolder, String[] textype_export, int export_mode, int utgame) {
        this.utpackagetexfile = utpackagetexfile;
        this.outputfolder = outputfolder;
        this.textype_export.addAll(Arrays.asList(textype_export));
        this.export_mode = export_mode;
        this.utgame = utgame;
        if(utgame==UTGames.U2||utgame==UTGames.UT2004)
        {
            usegroupnametexfile = true;
        }
    }

    public UTPackageTexConfig(File utmapfile,MapConverter mc){
        this.utpackagetexfile = utmapfile;
        this.outputfolder = mc.getTexfolder();
        this.textype_export.add("bmp");
        this.export_mode = UTPackageTexExtractor.mode_utxextractor;
        this.utgame = mc.getInput_utgame();
        if(utgame==UTGames.U2||utgame==UTGames.UT2004)
        {
            usegroupnametexfile = true;
        }
    }

    /**
     * ONLY USE IT TO EXPORT TEXTURES FROM MAP WITH UCCBATCHEXPORT
     * @param utmapfile
     * @param mc
     * @param exportmode
     */
    public UTPackageTexConfig(File utmapfile,MapConverter mc,int exportmode,String[] textypeexport){
        this.utpackagetexfile = utmapfile;
        if(exportmode==UTPackageTexExtractor.mode_uccbatchexport){
            this.outputfolder = mc.getTerfolder();
        } else {
            this.outputfolder = mc.getTexfolder();
        }
        
        this.textype_export.addAll(Arrays.asList(textypeexport));
        this.export_mode = exportmode;
        this.utgame = mc.getInput_utgame();
        if(utgame==UTGames.U2||utgame==UTGames.UT2004)
        {
            usegroupnametexfile = true;
        }
    }

    /*
    @Override
    public int hashCode(){
        return export_mode+textype_export.hashCode()+utpackagetexfile.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UTPackageTexConfig other = (UTPackageTexConfig) obj;
        if (this.utpackagetexfile != other.utpackagetexfile && (this.utpackagetexfile == null || !this.utpackagetexfile.equals(other.utpackagetexfile))) {
            return false;
        }
        if (!Arrays.deepEquals(this.textype_export, other.textype_export)) {
            return false;
        }
        if (this.export_mode != other.export_mode) {
            return false;
        }
        return true;
    }*/

    /**
     *
     * @param texturepackagefile E.g: Anubis.utx
     * @param mc MapConverter
     */
    public UTPackageTexConfig(String texturepackagefile,MapConverter mc){
        //E.G: C:\Games\UT
        String texpath_root = Main.config.getUTxRootFolder(mc.getInput_utgame()).getPath()+File.separator;
        File ftex = new File(texpath_root + "Textures" + File.separator + texturepackagefile + ".utx");
        File fsys = new File(texpath_root + "System" + File.separator + texturepackagefile + ".u");
        File fmap = new File(texpath_root + "Maps" + File.separator + texturepackagefile + Main.config.getMapFileExtension(utgame));

        if (ftex.exists()) {
            this.utpackagetexfile = ftex;
        } else if (fsys.exists()) {
            this.utpackagetexfile = fsys;
        } else if (fmap.exists()) {
            this.utpackagetexfile = fmap;
        }
        this.outputfolder = mc.getTexfolder();
        this.textype_export.add(".bmp");
        this.export_mode = UTPackageTexExtractor.mode_utxextractor;
        this.utgame = mc.getInput_utgame();
        if(utgame==UTGames.U2||utgame==UTGames.UT2004)
        {
            usegroupnametexfile = true;
        }
    }

    public String getInfo()
    {
        String tmp="";
        tmp += " -> "+File.separator+outputfolder.getName();
        tmp += " ";
        
        tmp += "[";
        for(int i=0;i<textype_export.size();i++)
        {
            tmp += textype_export.toArray()[i]+",";
        }
        tmp +="]";

        if (export_mode == UTPackageTexExtractor.mode_uccbatchexport) {
            tmp += "(UCC Mode)";
        } else {
            tmp += "(ExtractTextures Mode)";
        }
        
        return tmp;
    }


    public int getExport_mode() {
        return export_mode;
    }

    public File getOutputfolder() {
        return outputfolder;
    }

    public TreeSet<String> getTextype_export() {
        return textype_export;
    }

    /**
     * MyTex.utx or MyCoreTexPac.u
     * @return E.G.: C:\Test\MyTex.utx or C:\Test\MyCoreTexPac.u
     */
    public File getUtpackagetexfile() {
        return utpackagetexfile;
    }

    /**
     * UT and U1 don't have group info in T3D level file
     * @return
     */
    public boolean isUseGroupNameTexFile() {
        return usegroupnametexfile;
    }

    public String getPackageNameLowerCase(){
        return this.getUtpackagetexfile().getName().toLowerCase();
    }

    public boolean isUsemergealpha() {
        return usemergealpha;
    }

    public void setUsemergealpha(boolean usemergealpha) {
        this.usemergealpha = usemergealpha;
    }

    
}
