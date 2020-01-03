/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.terrain;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Hyperion
 */
public class TerrainBmpReader {

    File bmpterfile=null;
    Image image = null;
    BufferedImage img = null;

    public TerrainBmpReader(File bmpterfile) {
        this.bmpterfile = bmpterfile;
        java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
        /** lecture de l'image : */
        image = toolkit.getImage(bmpterfile.getAbsolutePath());
        try {
            img = ImageIO.read(bmpterfile);
        } catch (IOException ex) {
            Logger.getLogger(TerrainBmpReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void getInfo()
    {
        System.out.println(img.getHeight()+" "+img.getWidth());
        getXvalues();
    }

    public void getXvalues()
    {
        int rgb=0;
        int r=0;
        int g=0;
        int b=0;
        int max=0;

        for(int x=0;x<img.getWidth();x++)
        {


            for(int y=0;y<img.getHeight();y++)
            {
                rgb=img.getRGB(x, y);
                r=((rgb>>16)&0xFF);
                g=((rgb>>8)&0xFF);
                b=(rgb&0xFF);
                if(r>max){max=r;}
                if((r!=255)||(g!=255)||(b!=255))
                {
                    System.out.println("X="+x+" "+rgb+"  R:"+((rgb>>16)&0xFF)+" G:"+((rgb>>8)&0xFF)+" B:"+(rgb&0xFF));
                }
            }
            
            
            
            //System.out.println("X="+x+" "+img.getRGB(x, y));
        }

        System.out.println("Max="+max);
    }

    public void getGreyInfo(BufferedImage img)
    {
        ColorConvertOp op = new ColorConvertOp(
                                             ColorSpace.getInstance(ColorSpace.CS_GRAY),
                                             null);
        BufferedImage imageGrise = op.filter(img,null);
        for(int x=0;x<imageGrise.getWidth();x++)
        {
            System.out.println("X="+x+" "+img.getRGB(x, 128));
        }
    }

    BufferedImage toBufferedImage(Image image) {
            /** On test si l'image n'est pas déja une instance de BufferedImage */
            if( image instanceof BufferedImage ) {
                    return( (BufferedImage)image );
            } else {
                    /** On s'assure que l'image est complètement chargée */
                    image = new ImageIcon(image).getImage();

                    /** On crée la nouvelle image */
                    BufferedImage bufferedImage = new BufferedImage(
                                image.getWidth(null),
                                image.getHeight(null),
                                BufferedImage.TYPE_INT_RGB );

                    Graphics g = bufferedImage.createGraphics();
                    g.drawImage(image,0,0,null);
                    g.dispose();

                    return( bufferedImage );
            }
    }

    public static void lol()
    {

    }


}
