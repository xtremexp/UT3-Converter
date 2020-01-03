/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools;

import java.io.File;

/**
 *
 * @author Hyperion
 */
public class FileMover {


    File inputfolder;
    File outputfolder;

    public FileMover(File inputfolder, File outputfolder) {
        this.inputfolder = inputfolder;
        this.outputfolder = outputfolder;
    }

    

    public void move()
    {
        File f[] = inputfolder.listFiles();

        for(int i=0;i<f.length;i++)
        {
            f[i].renameTo(new File(outputfolder.getAbsolutePath()+"\\"+f[i].getName()));
        }
    }

    

}
