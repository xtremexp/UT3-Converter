/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.terrain;

import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codecimpl.TIFFImageDecoder;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Read heightmap terrain values from .tiff files and update them with UT3(ter_scale_z)
 * @author Hyperion
 */
public class TerrainTiffReader {

    File tertifffile=null;
    RenderedImage timg = null;
    float ter_scale_z=0.5F;
    String savpack="";

    public TerrainTiffReader(File tertifffile,String savpack) {
        this.tertifffile = tertifffile;
        this.savpack =savpack;

        if(this.tertifffile!=null&&this.tertifffile.exists()&&this.tertifffile.length()>0L){
            FileSeekableStream seekableStream;
            try {
                seekableStream = new FileSeekableStream(tertifffile);
                TIFFImageDecoder tid = new TIFFImageDecoder(seekableStream, null);
                timg = tid.decodeAsRenderedImage();
                try {
                    timg.getWidth();
                } catch (Exception e) {
                    System.out.println("Invalid terrain heighmap texture "+tertifffile.getName());
                    tertifffile = null;
                }
            } catch (IOException ex) {
                Logger.getLogger(TerrainTiffReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean hasHeightMapData(){
        if(this.tertifffile==null){
            return false;
        } else {
            if(!this.tertifffile.exists()){
                return false;
            }
            return true;
        }
    }
    public RenderedImage getRImg()
    {
        return this.timg;
    }
    
    public String getWidthUT3()
    {
        return String.valueOf(timg.getWidth()+1);
    }

    public int getWidth()
    {
        return timg.getWidth();
    }

    public int getHeight()
    {
        return timg.getHeight();
    }

    public int getRealCount()
    {
        return timg.getHeight()*timg.getWidth();
    }

    /** Get the total number of patches*/
    public int getCount()
    {
        return (timg.getHeight()+1)*(timg.getWidth()+1);
    }

    public String getHeightUT3()
    {
        return String.valueOf(timg.getHeight()+1);
    }

    public String getCountUT3()
    {
        return String.valueOf((timg.getHeight()+1)*(timg.getWidth()+1));
    }


    public void read2()
    {
        Raster rs = timg.getTile(0, 0);
        int a[] = null;
        for(int x=0;x<rs.getWidth();x++)
        {
            System.out.println("X="+x+" "+rs.getPixel(x, 90, a)[0]);
        }
    }



    /** Decrease height of mountains to fix with UT3 terrain standards*/
    private int getUpdatedHMValues(int hmvalue,float scalefactor)
    {
        int defaultval=32768;
        if(hmvalue<defaultval)
        {
            //System.out.println(hmvalue+"->"+(hmvalue + (int)((Math.abs(hmvalue-defaultval))*scalefactor)));
            return new Integer(defaultval - (int)((Math.abs(defaultval-hmvalue))*scalefactor));
        }
        else if(hmvalue>defaultval)
        {
            //System.out.println(hmvalue+"->"+(hmvalue - (int)((Math.abs(hmvalue-defaultval))*scalefactor)));
            return new Integer(defaultval + (int)((Math.abs(hmvalue-defaultval))*scalefactor));
        }
        else
        {
            return new Integer(defaultval);
        }

    }

    /** Returns all height map data of current data into an ArrayList*/
    public ArrayList<String> getHeightMapData()
    {
        ArrayList al = new ArrayList();
        Raster rs = timg.getTile(0, 0);
        int a[] = null;
        int max=0;
        int tmp=0;
        for(int y=0;y<rs.getWidth();y++)
        {
            for(int x=0;x<rs.getHeight();x++)
            {
                //al.add("x:"+x+"y:"+y+"-"+String.valueOf(rs.getPixel(x, y, a)[0]));
                //al.add(String.valueOf(rs.getPixel(x, y, a)[0]));
                al.add(String.valueOf(getUpdatedHMValues(rs.getPixel(x, y, a)[0], ter_scale_z)));
                //Adds extra terrain point (e.g.: UT2004: 256 width-> UT3: 257 width)
                if(x==(rs.getWidth()-1))
                {
                    al.add(String.valueOf(getUpdatedHMValues(rs.getPixel(x, y, a)[0], ter_scale_z)));
                }
            }

        }

        for(int i=0;i<(rs.getWidth()+1);i++)
        {
            al.add("32768");
        }

        return al;
    }


}
