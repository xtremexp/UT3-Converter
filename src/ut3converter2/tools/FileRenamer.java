/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools;

import java.io.File;
import ut3converter2.T3DConvertor;

/**
 *
 * @author Hyperion
 */
public class FileRenamer extends T3DConvertor{

    File inputfolder;
    String oldstr="";
    String newstr="";

    public FileRenamer(File inputfolder,String oldstr,String newstr) {
        this.inputfolder = inputfolder;
        this.oldstr = oldstr;
        this.newstr = newstr;
    }

    public void renameAll()
    {
        if(super.isBShowLog())
        {
            System.out.print("\n*** Renaming files in "+inputfolder.getAbsolutePath());
        };
        File f[] = inputfolder.listFiles();
        String tmp="";
        for(int i=0;i<f.length;i++)
        {
            tmp = f[i].getName().replaceAll("."+getExtension(f[i]),"");
            tmp = tmp.replaceAll("\\"+oldstr,newstr);
            File newfile = new File(f[i].getParent()+File.separator+tmp+"."+getExtension(f[i]));
            if(f[i].isFile())
            {
                f[i].renameTo(newfile);
            }
        }

        if(super.isBShowLog())
        {
            System.out.println("... Done!");
        }
    }

    private String getExtension(File f)
    {
        String t[]=f.getName().split("\\.");
        return t[t.length-1];
    }

    

}
