/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Allows to detect file encoding.
 * Used mainly for reading T3D level files.
 * Most of them are in UTF-8 or UTF16 format
 * but sometimes it has other kind of formats
 * @author Hyperion
 */
public class FileDecoder {

    /**
     * Reads first line of file in default read format (UTF-16)
     * if expression is not found in line then try with other Charset
     * such as UTF-16LE, ...
     * @param f File
     * @param expressioncheck String to be found in line (e.g: "Begin Map" in t3d level files)
     * @return Encoding format (UTF-16,UTF-16LE,UTF-16BE or null if unknown)
     */
    public static String getFileEncodingFormat(File f,String expressioncheck) throws FileNotFoundException, IOException
    {
        String line="";
        BufferedReader bfr = new BufferedReader(new FileReader(f));
        if(bfr.readLine().contains(expressioncheck))
        {
            return "UTF-16";
        }
        else
        {
            bfr.close();
            bfr = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-16LE"));
            if(bfr.readLine().contains(expressioncheck))
            {
                return "UTF-16LE";
            }

            bfr.close();
            bfr = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-16BE"));
            if(bfr.readLine().contains(expressioncheck))
            {
                return "UTF-16BE";
            }
        }

        return null;
    }

    public static BufferedReader getBufferedReader(File f,String exp) throws FileNotFoundException, IOException
    {
        return new BufferedReader(new FileReader(f));
        /*
        String encoding = getFileEncodingFormat(f, exp);
        if(encoding.equals("UTF-16")||encoding.equals("UTF-8"))
        {
            return new BufferedReader(new FileReader(f));
        }
        else
        {
            System.out.println(encoding);
            //TODO SUPPORT FOR OTHER ENCODING
            /*
                    BufferedReader bfr = new BufferedReader(
                new InputStreamReader(
                new FileInputStream(t3dlevelfile),FileDecoder.getFileEncodingFormat(t3dlevelfile, "Begin")));
             * */
            //return null;
        //}
    
    }

}
