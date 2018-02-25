/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.ihm;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Hyperion
 */
public class FileChooser2 {


    public static File getFolder(String title)
    {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setDialogTitle(title);

        int open = jfc.showOpenDialog(null);
        if(open==JFileChooser.APPROVE_OPTION)
        {
           return jfc.getSelectedFile();
        }
        else
        {
            return null;
        }

    }

    public static File getFile(String title, String extensionnames,String extensions[],File curfolder)
    {
        JFileChooser jfc = new JFileChooser();
        jfc.setCurrentDirectory(curfolder);
        FileFilter filter1 = new ExtensionFileFilter(extensionnames, extensions);
        jfc.setFileFilter(filter1);
        jfc.setDialogTitle(title);
        int open = jfc.showOpenDialog(null);
        if(open==JFileChooser.APPROVE_OPTION)
        {
           return jfc.getSelectedFile();
        }
        else
        {
            return null;
        }
    }

    public static File saveFile(String title)
    {
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogType(JFileChooser.SAVE_DIALOG);

        jfc.setDialogTitle(title);
        int open = jfc.showOpenDialog(null);
        if(open==JFileChooser.APPROVE_OPTION)
        {
           return jfc.getSelectedFile();
        }
        else
        {
            return null;
        }
    }

    public static File getFile(String title, String extensionnames,String extensions[])
    {
        JFileChooser jfc = new JFileChooser();
        FileFilter filter1 = new ExtensionFileFilter(extensionnames, extensions);
        jfc.setFileFilter(filter1);
        jfc.setDialogTitle(title);
        int open = jfc.showOpenDialog(null);
        if(open==JFileChooser.APPROVE_OPTION)
        {
           return jfc.getSelectedFile();
        }
        else
        {
            return null;
        }
    }

    public static File[] getFiles(String title, String extensionnames,String extensions[])
    {
        JFileChooser jfc = new JFileChooser();
        FileFilter filter1 = new ExtensionFileFilter(extensionnames, extensions);
        jfc.setFileFilter(filter1);
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.setDialogTitle(title);
        jfc.setMultiSelectionEnabled(true);
        int open = jfc.showOpenDialog(null);

        if(open==JFileChooser.APPROVE_OPTION)
        {
           return jfc.getSelectedFiles();
        }
        else
        {
            return null;
        }
    }

    public static File[] getFiles(String title, String extensionnames,String extensions[],File curfolder)
    {
        JFileChooser jfc = new JFileChooser();
        jfc.setCurrentDirectory(curfolder);
        FileFilter filter1 = new ExtensionFileFilter(extensionnames, extensions);
        jfc.setFileFilter(filter1);
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.setDialogTitle(title);
        jfc.setMultiSelectionEnabled(true);
        int open = jfc.showOpenDialog(null);

        if(open==JFileChooser.APPROVE_OPTION)
        {
           return jfc.getSelectedFiles();
        }
        else
        {
            return null;
        }
    }

}
