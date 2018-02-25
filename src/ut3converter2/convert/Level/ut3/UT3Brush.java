/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut3;

/**
 *
 * @author Hyperion
 */
public class UT3Brush {

    private int scalefactor=4;

    public UT3Brush() {
    }

    public UT3Brush(int scalefactor) {
        this.scalefactor = scalefactor;
    }

    public static String getNewTextureU(String line,int scalefactor)
    {
        float tx=0F;
        float ty=0F;
        float tz=0F;
        String tmp="";
        String tmp2[]=null;
        //             TextureU +00000.999998,+00000.000000,+00000.000000
        tmp = line.split("TextureU ")[1];
        tmp2 = tmp.split("\\,");
        tx = Float.valueOf(tmp2[0]);
        ty = Float.valueOf(tmp2[1]);
        tz = Float.valueOf(tmp2[2]);
        tx = tx/scalefactor;
        ty = ty/scalefactor;
        tz = tz/scalefactor;
        return "             TextureU "+String.valueOf(tx)+","+String.valueOf(ty)+","+String.valueOf(tz);
    }

    public static String getNewTextureV(String line,int scalefactor)
    {
        float tx=0F;
        float ty=0F;
        float tz=0F;
        String tmp="";
        String tmp2[]=null;
        //             TextureU +00000.999998,+00000.000000,+00000.000000
        tmp = line.split("TextureV ")[1];
        tmp2 = tmp.split("\\,");
        tx = Float.valueOf(tmp2[0]);
        ty = Float.valueOf(tmp2[1]);
        tz = Float.valueOf(tmp2[2]);
        tx = tx/scalefactor;
        ty = ty/scalefactor;
        tz = tz/scalefactor;
        return "             TextureV "+String.valueOf(tx)+","+String.valueOf(ty)+","+String.valueOf(tz);
    }
    
    public String toString()
    {
        return null;
    }

}
