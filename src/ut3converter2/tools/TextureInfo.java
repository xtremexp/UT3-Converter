/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools;

import com.sun.media.imageioimpl.plugins.pcx.PCXImageReader;
import com.sun.media.imageioimpl.plugins.pcx.PCXImageReaderSpi;
import com.sun.media.jai.codec.ImageDecodeParam;
import com.sun.media.jai.codecimpl.BMPImageDecoder;
import com.sun.opengl.util.texture.spi.DDSImage;
import java.awt.Dimension;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Some tool to give information about texture file
 * @author Hyperion
 */
public class TextureInfo {

    /**
     * Try to detect texture format and give dimension
     * @param ftex
     * @return
     */
    public static Dimension getTexSize(File ftex)
    {
        Dimension dim = new Dimension();

        if(ftex.getName().endsWith(".bmp"))
        {
            try {
                BMPImageDecoder bid = new BMPImageDecoder(new FileInputStream(ftex), new ImageDecodeParam() {

                });
                RenderedImage img = bid.decodeAsRenderedImage();
                dim.setSize(img.getWidth(), img.getHeight());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(TextureInfo.class.getName()).log(Level.SEVERE, null, ex);
            }catch (IOException ex) {
                Logger.getLogger(TextureInfo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(ftex.getName().endsWith(".dds"))
        {
            try {
                DDSImage ddsi = DDSImage.read(ftex);
                dim.setSize(ddsi.getWidth(), ddsi.getHeight());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(TextureInfo.class.getName()).log(Level.SEVERE, null, ex);
            }catch (IOException ex) {
                Logger.getLogger(TextureInfo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        PCXImageReader pcxir = new PCXImageReader(new PCXImageReaderSpi());
        PCXImageReaderSpi xc;

        return dim;
    }

}
