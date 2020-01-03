/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import ut3converter2.convert.MapConverter;
import ut3converter2.identification.SoundsIdentificator;
import ut3converter2.identification.TexturesIdentificator;
import ut3converter2.T3DConvertor;

/**
 *
 * @author Hyperion
 */
public class FileCleaner extends T3DConvertor {

    File inputfolder;
    File files2del[];
    boolean bcleanzerosfiles = false;
    String extension;
    TexturesIdentificator ti;
    SoundsIdentificator si;
    MapConverter mc;
    String mode="";
    String fc="";
    public static String mode_zeros="cleanzeros";
    public static String mode_all="cleanall";
    public static String mode_ext="cleanfileswithext";
    public static String mode_special="cleanspecial";
    public static String mode_delfolder="delfolder";
    Object objidentificator;

    public FileCleaner(File inputfolder,boolean bcleanzerosfiles) {
        this.inputfolder = inputfolder;
        this.bcleanzerosfiles = bcleanzerosfiles;
    }

    public FileCleaner(File inputfolder,String extension)
    {
        this.inputfolder = inputfolder;
        this.extension = extension;
    }

    public FileCleaner(Object identificator)
    {
        this.objidentificator = identificator;
    }
    
    public FileCleaner(File files2clean[])
    {
        this.files2del = files2clean;
    }

    public FileCleaner(File inputfolder,String mode,String fc)
    {
        this.inputfolder = inputfolder;
        this.mode = mode;
        this.fc = fc;
    }

    public FileCleaner(TexturesIdentificator ti,MapConverter mc)
    {
        this.ti = ti;
        this.mc = mc;
    }

    public FileCleaner(SoundsIdentificator si,MapConverter mc)
    {
        this.si = si;
        this.mc = mc;
    }

    public SoundsIdentificator getSi() {
        return si;
    }

    public void setSi(SoundsIdentificator si) {
        this.si = si;
    }

    public TexturesIdentificator getTi() {
        return ti;
    }

    public File[] getFiles2del() {
        return files2del;
    }

    public void setFiles2del(File[] files2del) {
        this.files2del = files2del;
    }


    public void setTi(TexturesIdentificator ti) {
        this.ti = ti;
    }

    public void clean()
    {
        long filesizesaved=0L;
        File f[];
        if(objidentificator!=null)
        {
            if(objidentificator instanceof SoundsIdentificator)
            {
                files2del = ((SoundsIdentificator)objidentificator).getFileSnd2Del();
            }
            else if(objidentificator instanceof TexturesIdentificator)
            {
                files2del = ((TexturesIdentificator)objidentificator).getFileTex2Del();
            }
        }
        if(mode.equals(FileCleaner.mode_special))
        {
            if(super.isBShowLog()){System.out.print("\n*** Deleting files NOT containing \""+fc+"\" in filename");}
            f = inputfolder.listFiles();
            for(int i=0;i<f.length;i++)
            {
                if(!f[i].getName().contains(fc))
                {
                    f[i].delete();
                    filesizesaved += f[i].length();
                }
            }
            if(super.isBShowLog()){System.out.print(" ("+sizetoKBMB(filesizesaved)+" saved)");}
        }
        else if(mode.equals(FileCleaner.mode_delfolder))
        {
            if(super.isBShowLog()){System.out.print("\n*** Deleting folder "+inputfolder.getAbsolutePath());}
            inputfolder.delete();
            if(super.isBShowLog()){System.out.println(" ... Done!");}
        }
        else if(files2del!=null)
        {
            if(files2del.length>0)
            {
                if(super.isBShowLog()){System.out.print("\n*** Deleting "+files2del.length+" unused files in "+files2del[0].getParent());}
            }
            
            for(int i=0;i<files2del.length;i++)
            {filesizesaved += files2del[i].length();files2del[i].delete();}
            if(super.isBShowLog()){System.out.print(" ("+sizetoKBMB(filesizesaved)+" deleted)");}
        }
        else
        {
            if(bcleanzerosfiles)
            {
                if(super.isBShowLog()){System.out.print("\n*** Deleting 0KB filesize files in "+inputfolder.getParent());}
                f = inputfolder.listFiles();
                for(int i=0;i<f.length;i++)
                {
                    if(f[i].length()==0L)
                    {
                        f[i].delete();
                    }
                }
            }
            if(extension != null)
            {
                if(super.isBShowLog()){System.out.print("\n*** Deleting "+extension+" files in "+inputfolder.getName());}
                f = inputfolder.listFiles();
                for(int i=0;i<f.length;i++)
                {
                    if(f[i].getName().endsWith(extension))
                    {
                        filesizesaved += f[i].length();
                        f[i].delete();
                    }
                }
                if(super.isBShowLog()){System.out.print(" ("+sizetoKBMB(filesizesaved)+" saved)");}
            }

            
        }
        System.out.println(" ... done! ***");
    }

     public static String sizetoKBMB(long filesize)
     {
         double temp;
         DecimalFormat df = new DecimalFormat("0.00",new DecimalFormatSymbols(Locale.US));

         temp= filesize; //KB
         if((temp/(1024F*1024F*1024F))>1F)
         {
             temp = temp/(1024F*1024F*1024F);
             return df.format(temp)+" GB";
         }
         temp = filesize;
         if((temp/(1024F*1024F))>1F)
         {
             temp = temp/(1024F*1024F);
             return df.format(temp)+" MB";
         }
         temp = filesize;
         if((temp/(1024F))>1L)
         {
             temp = temp/1024F;
             return df.format(temp)+" KB";
         }
         else
         {
             return temp+" Bytes";
         }
     }

    public void setObjidentificator(Object objidentificator) {
        this.objidentificator = objidentificator;
    }

     
}
