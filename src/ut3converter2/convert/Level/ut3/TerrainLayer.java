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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import ut3converter2.Main;
import ut3converter2.T3DConvertor;
import ut3converter2.UTGames;
import ut3converter2.UTObject;
import ut3converter2.convert.MapConverter;


/**
 *
 * @author Hyperion
 */
public class TerrainLayer extends T3DConvertor {

    String texture="";
    String alphamap="";
    String TLSTexName="";
    String TLSName="";
    float UScale=1f;
    float VScale=1f;
    float globalUVScale=1f;
    File alphamaptgafile=null;
    File alphamapbmpfile=null;
    File terfolder = null;
    ArrayList altlsnames=new ArrayList();
    TerrainLayerSetup tls;
    MapConverter mc;

    public TerrainLayer(String linedata,MapConverter mc) {
        this.mc = mc;
        this.terfolder = mc.getTerfolder();
        tls = new TerrainLayerSetup();
        tls.setMaterial(new TerrainMaterial(false));
        if(linedata.length()>2)
        {
            analyseLine(linedata);
        }
        super.setBShowLog(true);
        
    }

    public float getGlobalUVScale() {
        return globalUVScale;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public String getTLSTexName() {
        return TLSTexName;
    }

    public void setTLSTexName(String TLSTexName) {
        this.TLSTexName = TLSTexName;
    }

    public File getAlphaMapBmpFile()
    {
        return this.alphamapbmpfile;
    }

    public File getAlphaMapTgaFile()
    {
        return this.alphamaptgafile;
    }

    public boolean almapexist()
    {
        if(alphamaptgafile==null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public String getTLSName()
    {
        return this.TLSName;
    }

    private void analyseLine(String linedata)
    {

       String tmp[];

        //Layers(3)=(Texture=Texture'AlleriaTerrain.ground.sand02AL',AlphaMap=Texture'AS-Glacier.Terrain.ground_earth',UScale=1.500000,VScale=1.500000,TerrainMatrix=(XPlane=(X=0.002604),YPlane=(Y=0.002604),ZPlane=(Z=1.000000),WPlane=(W=1.000000,X=0.001299,Y=0.001299,Z=33180.730469)))
        //Layers(0)=(Texture=Texture'BenTropical01.Texture.DeepSand01',AlphaMap=Texture'ONS-IslandHop.Terrain.BaseDirt',UScale=6.000000,VScale=6.000000

        if(linedata.contains("Texture="))
        {
            texture = linedata.split("Texture=")[1];
            texture = texture.split("\\'")[1];
        }
        //    Layers(5)=(AlphaMap=Texture'ONS-DriaLite.Terraintexalphalayerdetailsand',UScale=1.000000,VScale=1.000000)
        if(linedata.contains("VScale="))
        {
            tmp = linedata.split("VScale=");
            this.VScale = Float.valueOf(tmp[1].split("\\,")[0].replaceAll("\\)","")); //UScale=1.000000,VScale=1.000000) rare case ..
        }
        if(linedata.contains("UScale="))
        {
            tmp = linedata.split("UScale=");
            this.UScale = Float.valueOf(tmp[1].split("\\,")[0].replaceAll("\\)",""));
        }
        this.globalUVScale = (Math.abs(this.UScale)+Math.abs(VScale))/2F;
        DecimalFormat df = new DecimalFormat("0.00");
        String s = df.format(this.globalUVScale);
        this.globalUVScale = Float.valueOf(s.replaceAll("\\,", "\\."));
        this.tls.material.setMappingscale(this.globalUVScale);
        
        //Z,LayerRotation=(Pitch=16384),T
        //Z,LayerRotation=(Roll=-16384),T
        if(linedata.contains("LayerRotation="))
        {
            //@TODO handle LayerRotation
        }

        //,TextureMapAxis=TEXMAPAXIS_XZ,L
        if(linedata.contains("TextureMapAxis="))
        {
            tmp = linedata.split("TextureMapAxis="); //TEXMAPAXIS_XZ,L
            this.tls.material.setMappingtype(tmp[1].split("\\,")[0]);
        }
        //AlphaMap=Texture'ONS-IslandHop.Terrain.BaseDirt'
        if(linedata.contains("AlphaMap="))
        {
            UTObject uto;
            alphamap = linedata.split("AlphaMap=")[1];
            uto = new UTObject(alphamap.split("\\'")[1]);

            if (this.mc.getInput_utgame() == UTGames.U2) {
                alphamap = uto.getName();
                TLSName = "TLS" + uto.getName();
            } else if (this.mc.getInput_utgame() == UTGames.UT2004) {
                alphamap = uto.getGroupAndName();
                TLSName = "TLS" + uto.getName();
            }

            this.tls.setTlstexname(getTLSTextureName(texture));
            this.setTLSTexName(getTLSTextureName(texture));
            File a = new File(terfolder.getAbsolutePath()+File.separator+alphamap+".tga");
            if(a.exists()){alphamaptgafile = a;}
            alphamapbmpfile = new File(terfolder.getAbsolutePath()+File.separator+alphamap+".bmp");
            //if(isBShowLog()){System.out.println("\t ");}
            //System.out.println(alphamapbmpfile+" "+alphamaptgafile);
        }
       
    }

    public ArrayList getAlphaValuesFromTga(File tgaterfile)
    {
        System.out.println("\t Converting TerrainLayer data from \""+tgaterfile.getParentFile().getName()+File.separator+tgaterfile.getName()+"\"");
        ArrayList alvalues = new ArrayList();
        try {
            int value=0;
            Color col = null;
            int width=0;
            Iterator it = ImageIO.getImageReadersByFormatName("tga");
            while(it.hasNext())
            {
                ImageReader im = (ImageReader) it.next();
                //ImageIn
                im.setInput(ImageIO.createImageInputStream(tgaterfile));
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

        return alvalues;
    }

    public ArrayList getAlphaValues(File bmpterfile)
    {
        ArrayList alvalues = new ArrayList();
        try {
            BufferedImage img = ImageIO.read(bmpterfile);
            int rgb=0;
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
                    alvalues.add("128");
            }

        } catch (IOException ex) {
            Logger.getLogger(TerrainLayer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return alvalues;
    }

    public void writeTerrainLayer(BufferedWriter bwr,File bmpfile,int numindex) throws IOException
    {
        //ArrayList al = getAlphaValues(bmpfile);
        ArrayList al = getAlphaValuesFromTga(bmpfile);
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
        if(Main.d2.utgame==UTGames.UT2004)
        {
            this.tls.setTlsname(TLSName);
            Main.d2.mcut2004.addTLSName(new String[]{getTLSTextureName(texture),TLSName});
        }
        else if(Main.d2.utgame==UTGames.U2)
        {
            this.tls.setTlsname(TLSName);
            Main.d2.mcu2.addTLSName(new String[]{getTLSTextureName(texture),TLSName});
        }
        
        //System.out.println("Texture:"+getTLSTextureName(texture)+" TLS:"+TLSName);
    }

    private String getTLSTextureName(String texture)
    {
        String tmp[] = texture.split("\\.");
        if(tmp.length==2)
        {
            return tmp[1]+"_Mat";
        }
        else if(tmp.length==3)
        {
            return tmp[1]+"_"+tmp[2]+"_Mat";
        }
        return "";
    }
    
    private boolean ismultiple(int a, int b)
    {
        if (a%b==0){return true;}else{ return false;}
    }

    public TerrainLayerSetup getTls() {
        return tls;
    }

    public void setTls(TerrainLayerSetup tls) {
        this.tls = tls;
    }
    

    

}
