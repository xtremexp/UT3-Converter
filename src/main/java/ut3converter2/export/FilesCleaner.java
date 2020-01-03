/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.export;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Hyperion
 */
public class FilesCleaner {


    ArrayList reffiles;
    File folder;

    public FilesCleaner(File[] reffiles,File folder) {
        this.folder = folder;
        this.reffiles = new ArrayList();

        for (int i = 0; i < reffiles.length; i++) {
            File file = reffiles[i];
            this.reffiles.add(file.getAbsolutePath());
        }
    }

    public void Clean()
    {
        File filesinfolder[] = new File[folder.listFiles().length];
        filesinfolder = folder.listFiles();
        for (int i = 0; i < filesinfolder.length; i++) {
            File file = filesinfolder[i];
            if(!reffiles.contains(file.getAbsolutePath()))
            {
                file.delete();
            }
        }
    }



}
