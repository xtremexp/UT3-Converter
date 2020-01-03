/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ut3converter2.tools;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import ut3converter2.UTGames;

/**
 *
 * @author Hyperion
 */
public class ClassAnalyzer {

    String[] sm_rootclasses;
    Element rootel;
    Document doc;

    /**
     *
     * @param xml_dbclassfile Xml UTx classes database file (e.g: U2ClassesDB.xml)
     */
    public ClassAnalyzer(File xml_dbclassfile) {
        sm_rootclasses = new String[0];
        try {
            SAXBuilder sxb = new SAXBuilder();
            this.doc = sxb.build(xml_dbclassfile);
            rootel = this.doc.getRootElement();
        } catch (JDOMException ex) {
            Logger.getLogger(ClassAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClassAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setStaticMeshClasses(String[] smclasses) {
        this.sm_rootclasses = smclasses;
    }

    public boolean utclassIsStaticMesh(String utclass) {
        return classBelongsToClass(utclass, sm_rootclasses);
    }

    /**
     *
     * @param utclass
     * @param utparentclass (e.g: StaticMesh)
     * @return True if current class is child of specified class
     */
    public boolean classBelongsToClass(String utclass, String[] utparentclass) {

        Element el = getElementWithName(utclass.toLowerCase());
        Element elparent;
        if (el == null) {
            return false;
        } else {
            for (int i = 0; i < utparentclass.length; i++) {
                elparent = getElementWithName(utparentclass[i].toLowerCase());
                if (elparent == null) {
                    return false;
                } else {
                    if (UTParentClassHasUTClassChild(utclass.toLowerCase(), elparent)) {
                        return true;
                    }
                }
            }
        }


        return false;
    }

    private boolean UTParentClassHasUTClassChild(String utclass,Element parent_utclass_el)
    {
        Iterator it = parent_utclass_el.getDescendants(new ElementFilter(utclass));
        if(it.hasNext())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    private Element getElementWithName(String classname) {
        Element el;

        Iterator it = rootel.getDescendants(new ElementFilter(classname.toLowerCase()));
        if (it.hasNext()) {
            return (Element) it.next();
        }
        return null;
    }

    HashMap<String,String> hmactors = new HashMap<String, String>();

    /**
     * Returns known convertable actor name
     * (Ex: U2ZoneInfo->ZoneInfo (for Unreal 2)
     * @param actorname Eg: U2ZoneInfo
     * @param utgame Eg: 1,2,3 (see UTGames)
     * @return Eg: ZoneInfo
     * @see UTGames
     */
    public String getRootKnownConvertActor(String actorname,int utgame){
        if(hmactors.containsKey(actorname)){
            return hmactors.get(actorname);
        }

        if(classBelongsToClass(actorname, new String[]{"Light"})){
            hmactors.put(actorname, "Light");
            return "Light";
        }
        if(classBelongsToClass(actorname, new String[]{"ZoneInfo"})){
            hmactors.put(actorname, "ZoneInfo");
            return "ZoneInfo";
        }

        if(classBelongsToClass(actorname, new String[]{"Mover"})){
            hmactors.put(actorname, "Mover");
            return "Mover";
        }

        if(classBelongsToClass(actorname, new String[]{"Trigger"})){
            hmactors.put(actorname, "Trigger");
            return "Trigger";
        }

        if(utgame==UTGames.U2){
            if(classBelongsToClass(actorname, new String[]{"Decoration"})){
                hmactors.put(actorname, "StaticMeshActor");
                return "StaticMeshActor";
            }
            
            if(actorname.equals("ResizableStaticMesh")){
                return "StaticMeshActor";
            }
        }
        
        hmactors.put(actorname, actorname);
        return actorname;
    }
}
