/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hyperion
 */
public class FileCopier {

    public static void copyFileFromTo(File inputfile,File outputfilecopy)
    {
        copy(inputfile, outputfilecopy);
    }
    
    public static void copyFile(File inputfile,String outputfilename)
    {
        copy(inputfile, new File(inputfile.getParent()+File.separator+outputfilename));
    }

    private static void copy(File inputfile2,File outputfile2)
    {
        if(inputfile2.exists())
        {
            try {
                BufferedReader bfr = new BufferedReader(new FileReader(inputfile2));
                BufferedWriter bwr = new BufferedWriter(new FileWriter(outputfile2));

                bfr.close();
                bwr.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FileCopier.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FileCopier.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
