/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut3;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import ut3converter2.UTGames;
import ut3converter2.UTObject;
import ut3converter2.convert.MapConverter;

/**
 * @TODO Use TerrainMaterial with TerrainLayer
 * @author Hyperion
 */
public class DecoLayer {

    /**
     * If densitymap tex has been found ...
     */
    boolean isvalid=false;
    File terfolder;
    /**
     * Associated TerrainLayerSetup name
     */
    String TLSName;

    String staticmesh;
    /**
     * Greyscale tga file
     */
    File densitymaptga;
    /**
     * Greyscale bitmap file.
     */
    File densitymapbmp;

    private float densitymult_min=1F;
    private float densitymult_max=1F;

    float density;
    
    private float scalex_min=1F;
    private float scalex_max=1F;
    private float scaley_min=1F;
    private float scaley_max=1F;
    private float scalez_min=1F;
    private float scalez_max=1F;

    float minscale=1F;
    float maxscale=1F;

    /**
     * Until which distance the foliages are rendered/displayed
     */
    float maxdrawradius=1024F;
    /**
     * From which distance the foliages are rendered
     */
    float mintransitionradius=0F;

    TerrainLayerSetup tls;

    final String min_ut2004="Min=";
    final String max_ut2004="Max=";
    final String min_u2="A=";
    final String max_u2="B=";
    String min;
    String max;
    MapConverter mc;

    /**
     *
     * @param linedata T3D Line
     * @param terfolder Folder where are stored terrain files (generally /UT-Terrain)
     * @param tlsname Name that will be used for TerrainLayerSetup actor
     */
    public DecoLayer(String linedata,String tlsname,MapConverter mc) {
        this.terfolder = mc.getTerfolder();
        tls = new TerrainLayerSetup(tlsname);
        tls.setMaterial(new TerrainMaterial(true));
        this.mc = mc;
        if(mc.getInput_utgame()==UTGames.UT2004)
        {
            min = min_ut2004;
            max = max_ut2004;
        }
        else if(mc.getInput_utgame()==UTGames.U2)
        {
            min = min_u2;
            max = max_u2;
        }
        analyseline(linedata);
    }

    public TerrainLayerSetup getTls() {
        return tls;
    }

    public void setTls(TerrainLayerSetup tls) {
        this.tls = tls;
    }

    public File getAlphaTgaFile()
    {
        return this.densitymaptga;
    }
    public float getDensity() {
        return density;
    }

    public float getMaxscale() {
        return maxscale;
    }

    public float getMinscale() {
        return minscale;
    }

    
    public File getDensitymapbmp() {
        return densitymapbmp;
    }

    public File getDensitymaptga() {
        return densitymaptga;
    }

    /**
     * cp_wasteland_mesh.DecoLayers.cp_rubble1_deco
     * ->mypackage.DecoLayers_cp_rubble1_deco
     * @param savpack
     * @return
     */
    public String getStaticmeshUT3(String savpack)
    {
        String t[] = this.staticmesh.split("\\.");
        if(t.length==2)
        {
            return savpack+"."+t[1];
        }
        else if(t.length==3)
        {
            return savpack+"."+t[1]+"_"+t[2];
        }
        return "None";
    }
    public String getStaticmesh() {
        return staticmesh;
    }

    /** */
    //    DecoLayers(0)=(ShowOnTerrain=1,DensityMap=Texture'ONS-Dria.Decolyaeralphalised1',StaticMesh=StaticMesh'cp_wasteland_mesh.DecoLayers.cp_rubble1_deco',
    //FadeoutRadius=(Min=750.000000,Max=1500.000000)
    private void analyseline(String linedata)
    {
        String tmp[];
        if(linedata.contains("DensityMap="))
        {
            UTObject uto;

            //U2: "    DecoLayers(0)=(ShowOnTerrain=1,DensityMap=Texture'mm_marsh.DecoLayers.decolayer_02',St..."
            //UT2004:"    DecoLayers(0)=(ShowOnTerrain=1,DensityMap=Texture'ONS-Dria.Decolyaeralphalised1',StaticMesh=S"
            uto = new UTObject((linedata.split("DensityMap")[1]).split("\\'")[1]);
            if (mc.getInput_utgame() == UTGames.U2) {
                densitymaptga = new File(terfolder.getAbsolutePath() + File.separator + uto.getName() + ".tga");
                densitymapbmp = new File(terfolder.getAbsolutePath() + File.separator + uto.getName() + ".bmp");
            } else if (mc.getInput_utgame() == UTGames.UT2004) {
                densitymaptga = new File(terfolder.getAbsolutePath() + File.separator + uto.getGroupAndName() + ".tga");
                densitymapbmp = new File(terfolder.getAbsolutePath() + File.separator + uto.getGroupAndName() + ".bmp");
            }

            
        }
        if(linedata.contains("StaticMesh="))
        {
            staticmesh = linedata.split("StaticMesh=")[1];
            staticmesh = staticmesh.split("\\'")[1];
            this.tls.material.fm.setStaticmesh(staticmesh);
        }
        if(linedata.contains("DensityMultiplier"))
        {
            //DensityMultiplier=(Min=1.000000,Max=1.000000)
            //DensityMultiplier=(A=1.000000,B=1.000000) for Unreal 2
            String tmp2 = linedata.split("DensityMultiplier=")[1]; //Min=1.000000,Max=1.000000)
            tmp2 = tmp2.replaceAll("\\)","");
            String t[] = tmp2.split("\\,");

            densitymult_min = Float.valueOf(t[0].split(min)[1]);
            densitymult_max = Float.valueOf(t[1].split(max)[1]);

            density = (densitymult_min+densitymult_max)/2F;
            if(density<1){ //UT3 EDITOR DOESN'T HANDLE DENSITY <1 MIN = 1
                density = 1;
            }
            this.tls.material.fm.setDensity(density);
            density /= 4F; //@TODO Remove density factor
        }
        //0)),FadeoutRadius=(Min=750.000000,Max=1500.000000),De
        if(linedata.contains("FadeoutRadius"))
        {
            String t[];
            String tmp2 = linedata.split("FadeoutRadius=")[1]; //(Min=750.000000,Max=1500.000000),DensityMultiplier=
            tmp2 = tmp2.split("\\(")[1];
            tmp2 = tmp2.split("\\)")[0]; //Min=750.000000,Max=1500.000000
            t = tmp2.split("\\,");
            if(t.length==2)
            {
                mintransitionradius = Float.valueOf(t[0].split(min)[1]);
                this.tls.material.fm.setMintransitionradius(mintransitionradius);
                maxdrawradius = Float.valueOf(t[1].split(max)[1]);
                this.tls.material.fm.setMaxdrawradius(maxdrawradius);
            }
            else
            {
                mintransitionradius = Float.valueOf(tmp2.split(min)[1]);
                this.tls.material.fm.setMintransitionradius(mintransitionradius);
                maxdrawradius = Float.valueOf(tmp2.split(max)[1]);
                this.tls.material.fm.setMintransitionradius(maxdrawradius);
            }
        }
        if(linedata.contains("ScaleMultiplier"))
        {
            String t[];
            //ScaleMultiplier=(X=(Min=1.000000,Max=1.500000),Y=(Min=1.000000,Max=1.500000),Z=(Min=0.750000,Max=1.000000))
            String tmp2 = linedata.split("ScaleMultiplier=")[1];
            tmp2 = tmp2.split("X=")[1]; //(Min=1.000000,Max=1.500000),Y=(Min=1.00 .....
            tmp2 = tmp2.split("\\(")[1];
            tmp2 = tmp2.split("\\)")[0]; //Min=1.000000,Max=1.500000
            t = tmp2.split("\\,");
            scalex_min = Float.valueOf(t[0].split(min)[1]);
            scalex_max = Float.valueOf(t[1].split(max)[1]);

            tmp2 = linedata.split("Y=")[1]; //(Min=1.000000,Max=1.500000),Y=(Min=1.00 .....
            tmp2 = tmp2.split("\\(")[1];
            tmp2 = tmp2.split("\\)")[0]; //Min=1.000000,Max=1.500000
            t = tmp2.split("\\,");
            scaley_min = Float.valueOf(t[0].split(min)[1]);
            scaley_max = Float.valueOf(t[1].split(max)[1]);

            tmp2 = linedata.split("Z=")[1]; //(Min=1.000000,Max=1.500000),Y=(Min=1.00 .....
            tmp2 = tmp2.split("\\(")[1];
            tmp2 = tmp2.split("\\)")[0]; //Min=1.000000,Max=1.500000
            t = tmp2.split("\\,");
            scalez_min = Float.valueOf(t[0].split(min)[1]);
            scalez_max = Float.valueOf(t[1].split(max)[1]);

            minscale = (scalex_min+scaley_min+scalez_min)/3F;
            this.tls.material.fm.setMinscale(minscale);
            maxscale = (scalex_max+scaley_max+scalez_max)/3F;
            this.tls.material.fm.setMaxscale(maxscale);
        }
    }

    public void writeDecoLayerAlphaValues(BufferedWriter bwr,File bmpfile,int numindex) throws IOException
    {
        ArrayList al = new ArrayList();
        if(densitymaptga.exists()){
            al = getAlphaValuesFromTga(densitymaptga);
        } else if(densitymapbmp.exists()){
            al = getAlphaValuesFromTga(densitymapbmp);
        } else {
            System.out.println("\t DecoLayer data tex file not found: \""+densitymapbmp);
            System.out.println("\t DecoLayer data tex file not found: \""+densitymaptga);
            isvalid = false;
        }
         
        String tmp="";


        bwr.write("            Begin AlphaMap Index="+numindex+" Count="+al.size()+"\n");

        for(int i=0;i<al.size();i++)
        {
            if((ismultiple(i+1, 8))||(i==(al.size()-1)))
            {
                bwr.write(tmp+al.get(i).toString()+"\n");
                tmp = new String("");
            }
            else
            {
                tmp += al.get(i).toString()+"\t";
            }
        }
        //addExtraLine(bwr, ImageIO.read(bmpfile));
        bwr.write("            End AlphaMap\n");
    }

    private boolean ismultiple(int a, int b)
    {
        if (a%b==0){return true;}else{ return false;}
    }

    /**
     *
     * @param dl_densitymap_tertexfile (E.G: "Decolyaeralphalised1.bmp or Decolyaeralphalised1.tga"
     * @return Density Map values
     */
    public ArrayList<String> getAlphaValuesFromTga(File dl_densitymap_tertexfile)
    {
        System.out.println("\t Converting Decolayer data from \""+dl_densitymap_tertexfile.getParentFile().getName()+File.separator+dl_densitymap_tertexfile.getName()+"\"");
        ArrayList alvalues = new ArrayList();
        try {
            int value=0;
            Color col = null;
            int width=0;
            Iterator it = ImageIO.getImageReadersByFormatName(dl_densitymap_tertexfile.getName().split("\\.")[1]);
            while(it.hasNext())
            {
                ImageReader im = (ImageReader) it.next();
                //ImageIn
                im.setInput(ImageIO.createImageInputStream(dl_densitymap_tertexfile));
                BufferedImage bf = im.readTile(0, 0, 0);
                width = bf.getWidth();
                //for(int y=0;y<bf.getHeight();y++)
                for(int y=(bf.getWidth()-1);y>=0;y--)
                //for(int x=0;x<bf.getWidth();x++)
                {
                    //for(int y=0;y<bf.getHeight();y++)
                    //for(int x=(bf.getWidth()-1);x>=0;x--)
                    for(int x=0;x<bf.getWidth();x++)
                    {
                        value = bf.getRGB(x, y);
                        col = new Color(value,true);
                        value = col.getAlpha();
                        //value = Math.abs(value-255);
                        alvalues.add(String.valueOf(value));
                        if(x==(bf.getWidth()-1))
                        //if(x==0)
                        {
                            alvalues.add("128");
                        }
                    }
                }
            }

            for(int x=0;x<(width+1);x++)
            {
                    alvalues.add("128");
            }

        } catch (IOException ex) {
            Logger.getLogger(TerrainLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(" ... Done!");
        return alvalues;
    }

    public ArrayList getAlphaValues(File bmpterfile)
    {
        ArrayList alvalues = new ArrayList();
        try {
            BufferedImage img = ImageIO.read(bmpterfile);
            int rgb=0;
            int r=0;
            int g=0;
            int b=0;
            int a=0;
            boolean invertvalues=true;
            boolean isdone=false;
            int maxa=0;
            int mina=999999999;

            for(int y=0;y<img.getHeight();y++)
            {
                for(int x=0;x<img.getWidth();x++)
                {
                    rgb=img.getRGB(x, y);

                    a=(rgb>>16)&0xFF;

                    if(a>maxa){maxa=a;}
                    if(a<mina){mina=a;}

                    a = a-127;
                    a = a*2;
                    if(a>255){a=255;}
                    a=Math.abs(a-255);


                    alvalues.add(String.valueOf(a));
                    if(x==(img.getWidth()-1))
                    {
                        alvalues.add(String.valueOf(a));
                    }
                }
            }
            for(int x=0;x<img.getWidth();x++)
            {
                    alvalues.add("0");
            }

        } catch (IOException ex) {
            Logger.getLogger(TerrainLayer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return alvalues;
    }

    public String getTLSName() {
        return TLSName;
    }

    public void setTLSName(String TLSName) {
        this.TLSName = TLSName;
    }

    public boolean almapexist()
    {
        if(densitymaptga==null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean isIsvalid() {
        return isvalid;
    }

    public void setIsvalid(boolean isvalid) {
        this.isvalid = isvalid;
    }

    
}
