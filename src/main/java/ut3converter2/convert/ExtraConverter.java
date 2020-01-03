/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert;

import java.awt.CardLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import ut3converter2.Main;
import ut3converter2.UTGames;
import ut3converter2.export.StaticMeshesExporter;
import ut3converter2.export.TextureExporter;
import ut3converter2.identification.StaticMeshesIdentificator;
import ut3converter2.identification.TexturesIdentificator;
import ut3converter2.tools.FileCleaner;

/**
 *
 * @author Hyperion
 */
public class ExtraConverter {

    MapConverter mc;
    StaticMeshesExporter sme;
    StaticMeshesIdentificator smi;
    TexturesIdentificator ti;
    File smfiles[];
    String subtexfolderstr="Tex";
    File subtexfolder;

    public ExtraConverter(MapConverter mc) {
        this.mc = mc;
        this.subtexfolder = new File(mc.getExtrafolder().getAbsolutePath()+"//"+subtexfolderstr);
        if(!this.subtexfolder.exists())
        {
            this.subtexfolder.mkdir();
        }
    }

    public void Convert() throws FileNotFoundException, IOException
    {
        ArrayList altmp=new ArrayList();
        ArrayList altex=new ArrayList();
        ArrayList alt3dsm=new ArrayList();


        File f[]=mc.getExtrafolder().listFiles();
        for(int i=0;i<f.length;i++)
        {
            if(f[i].getName().endsWith(".usx"))
            {
                altmp.add(f[i]);
            }
            else if(f[i].getName().endsWith(".dds"))
            {
                altex.add(f[i]);
            }
        }
        this.smfiles = objectToFileArray(altmp);
        /*
        sme = new SMExporter(null, mc.getExtrafolder(), Main.config,UTGames.UT2004);
        sme.setUsxfiles(smfiles);
        sme.ExportToT3D();
        */
        f=mc.getExtrafolder().listFiles();
        altmp.clear();
        for(int i=0;i<f.length;i++)
        {
            if(f[i].getName().endsWith(".t3d"))
            {
                altmp.add(f[i]);
            }
        }
        this.smfiles = objectToFileArray(altmp);

        altmp = new ArrayList();
        for(int i=0;i<smfiles.length;i++)
        {
            altmp.add(new Object[]{smfiles[i],false});
        }
        //ti = new TexturesIdentificator(altmp,"dds");
        ti.identifyTextures();
        TextureExporter te = new TextureExporter(ti.getTexPackageFiles(Main.config, UTGames.UT2004), new String[]{"dds"}, mc);
        te.ExportTex(this.subtexfolder);

        /*
        FileCleaner fc = new FileCleaner(ti.getFileTex2Del(this.subtexfolder));
        fc.clean();
         * */
         
        this.smfiles = objectToFileArray(altex);
        te.setUtxfiles(smfiles);
        te.ExportTex();
        /*
        CardLayout cl = (CardLayout) Main.d2.jxpcard.getLayout();
        cl.show(Main.d2.jxpcard, "step1");*/
    }

    private File[] objectToFileArray(ArrayList al)
    {
        File f[] = new File[al.size()];

        for(int i=0;i<al.size();i++)
        {
            f[i] = (File) al.get(i);
        }

        return f;
    }

}
