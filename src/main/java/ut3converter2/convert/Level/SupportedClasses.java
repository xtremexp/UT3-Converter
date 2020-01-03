/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level;

import java.util.ArrayList;

/**
 *
 * @author Hyperion
 */
public class SupportedClasses {

    ArrayList alsupclasses;
    ArrayList albanclasses;

    public SupportedClasses(ArrayList alsupclasses) {
        this.alsupclasses = alsupclasses;
        this.albanclasses = new ArrayList();
    }

    public SupportedClasses() {
        ArrayList al = new ArrayList();
        al.add("all");
        this.alsupclasses = al;
        this.albanclasses = new ArrayList();
    }

    public ArrayList getAlbanclasses() {
        return albanclasses;
    }

    public void setAlbanclasses(ArrayList albanclasses) {
        this.albanclasses = albanclasses;
    }

    
    public ArrayList getAlsupclasses() {
        return alsupclasses;
    }

    public void setAlsupclasses(ArrayList alsupclasses) {
        this.alsupclasses = alsupclasses;
    }

    public void addClass(String utxclass)
    {
        alsupclasses.add(utxclass.toLowerCase());
    }

    public void addBannedClass(String classname)
    {
        albanclasses.add(classname);
    }

    public boolean isSupportedClass(String classname)
    {
        if(albanclasses.contains(classname.toLowerCase()))
        {
            return false;
        }
        if(alsupclasses.contains("all"))
        {
            return true;
        }
        if(alsupclasses.contains(classname.toLowerCase()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
