/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2;

import java.io.File;
import ut3converter2.export.UTPackageTexConfig;

/**
 * Describes any UTObject which belongs to:
 * a package
 * a group (optional)
 * and as a name.
 * @author Hyperion
 */
public class UTObject {

    String packagename;
    String groupname;
    String name;

    public UTObject(String packagename, String groupname, String name) {
        this.packagename = packagename;
        this.groupname = groupname;
        this.name = name;
    }

    /**
     * Creates UTObject with no group set.
     * @param packagename Name of the package it belongs to (without extension)
     * @param name Name of the UTObject
     */
    public UTObject(String packagename, String name) {
        this.packagename = packagename;
        this.name = name;
    }

    /**
     * Phobos2_cp.HardwareSkins.cp_rustbrace3shad
     * @param dataline
     */
    public UTObject(String dataline) {
        String t[]=dataline.split("\\.");
        if(t.length==1)
        {
            this.name = t[0];
        }
        else if(t.length==2)
        {
            this.packagename = t[0];
            this.name = t[1];
        }
        else if(t.length==3)
        {
            this.packagename = t[0];
            this.groupname = t[1];
            this.name = t[2];
        }
    }
    

    /**
     * Returns UTObject name of type=texture
     * in UT3 format Groupname_name_Mat
     * Used for UT2004 Staticmeshes mainly.
     * @return
     */
    public String getUT3TexName()
    {
        return this.groupname+"_"+this.name+"_Mat";
    }

    /**
     * ac.Group.Name->Group
     * @return ac.Group.Name->Group
     */
    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    /**
     * Return the group and the name of UTObject
     * @return Group.Name
     */
    public String getGroupAndName() {
        if(this.groupname!=null)
        {
            return this.groupname+"."+this.name;
        }
        else
        {
            return this.name;
        }

    }

    /**
     * Return the group and the name of UTObject
     * @return Package.Group.Name
     */
    public String getPacGroupAndName() {
        if(this.packagename==null){
            return this.name;
        } else {
            if(this.groupname!=null)
            {
                return this.packagename+"."+this.groupname+"."+this.name;
            }
            else
            {
                return this.packagename+"."+this.name;
            }
        }
    }

    public String getPacName(){
        if(this.packagename==null){
            return this.name;
        } else {
            return this.packagename+"."+this.name;
        }
    }

    /**
     *
     * @return Package.Group_Name
     */
    public String getPacGroupAndName2() {
        if(this.packagename==null){
            return this.name;
        } else {
            if(this.groupname!=null)
            {
                return this.packagename+"."+this.groupname+"_"+this.name;
            }
            else
            {
                return this.packagename+"."+this.name;
            }
        }
    }

    /**
     * Return the group and the name of UTObject
     * @return Group_Name
     */
    public String getGroupAndName2() {
        if(this.groupname!=null)
        {
            return this.groupname+"_"+this.name;
        }
        else
        {
            return this.name;
        }

    }



    /**
     * Pac.Group.Name->Name
     * @return Name of UTObject
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public UTPackageTexConfig toUTPackageTexConfig(File outputfolder,String[] textype_export,int export_mode,int utgame)
    {
        String texpath_root = Main.config.getUTxRootFolder(utgame).getPath()+File.separator;
        File ftex = new File(texpath_root + "Textures" + File.separator + this.getPackagename() + ".utx");
        File fsys = new File(texpath_root + "System" + File.separator + this.getPackagename() + ".u");
        File fmap = new File(texpath_root + "Maps" + File.separator + this.getPackagename() + "." +Main.config.getMapFileExtension(utgame));
        
        if(ftex.exists()){
            return new UTPackageTexConfig(ftex,outputfolder,textype_export,export_mode,utgame);
        }
        
        if(fsys.exists()){
            return new UTPackageTexConfig(fsys,outputfolder,textype_export,export_mode,utgame);
        }

        if(fmap.exists()){
            return new UTPackageTexConfig(fmap,outputfolder,textype_export,export_mode,utgame);
        }

        return null;
    }

}
