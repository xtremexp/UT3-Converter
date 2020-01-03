/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2;

import java.io.File;

/**
 *
 * @author Hyperion
 */
public class UTPackage {

    final static int TYPE_TEXTURE=0;
    final static int TYPE_STATICMESH=1;
    final static int TYPE_SYSTEM=2;
    final static int TYPE_ANIMATION=3;
    final static int TYPE_GLOBAL=4;

    String type;
    String packagename;
    String packageext;

    File path;
    int utgameparent;

    public UTPackage(String utpackage) {
        if(utpackage.contains(".")){
            packagename = utpackage.split("\\.")[0];
            packageext = utpackage.split("\\.")[1];
        } else {
            packagename = utpackage;
        }
    }

    public UTPackage(File packagepath) {
        path = packagepath;
        String temp = packagepath.getName();
        if(temp.contains(".")){
            packagename = temp.split("\\.")[0];
            packageext = temp.split("\\.")[1];
        } else {
            packagename = temp;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public File getPath() {
        return path;
    }

    public String getPackagename() {
        return packagename;
    }

    


}
