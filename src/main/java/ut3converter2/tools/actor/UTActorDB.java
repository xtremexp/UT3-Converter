/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools.actor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import ut3converter2.UTGames;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
/**
 *
 * @author Hyperion
 */
public class UTActorDB {

    /**
     * UT class files (.uc)
     */
    File[] ucfiles;
    String xmlref_file="U2DB.xml";
    int utgame;
    Document doc;
    Element rootel;

    public UTActorDB(File[] ucfiles) {
        this.ucfiles = ucfiles;
    }

    public UTActorDB(File folder) {
        ucfiles = folder.listFiles();
    }

    private void createBDDFile() throws IOException
    {
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(xmlref_file)));
        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        bw.write("<"+UTGames.getUTGameShort(utgame)+">\n");

        bw.write("</"+UTGames.getUTGameShort(utgame)+">\n");
        bw.close();
    }

    public void makeDB() throws IOException, JDOMException
    {
        Element el;
        Element superel;
        File ucfile;
        if(!(new File(xmlref_file)).exists())
        {
            createBDDFile();
        }
        SAXBuilder sxb = new SAXBuilder();
        this.doc = sxb.build(new File(xmlref_file));
        rootel = this.doc.getRootElement();
        String classname="";
        String superclassname="";

        for(int j=0;j<10;j++)
        {
            System.out.println((j+1)+"/"+10+" Pass");
            for(int i=0;i<ucfiles.length;i++)
            {
                ucfile = ucfiles[i];
                classname = ucfile.getName().replaceAll(".uc","").toLowerCase();
                superclassname = getSuperClassName(ucfile).toLowerCase();

                
                el = getElementWithName(classname);
                if(el==null)
                {
                    el = new Element(classname);
                }

                superel = getElementWithName(superclassname);

                if(superel==null)
                {
                    //System.out.println(el.getName());
                    if(!ElementHasChild(rootel, el))
                    {
                        rootel.addContent(new Element(classname));
                    }

                }
                else
                {
                    if(!ElementHasChild(superel, el))
                    {
                        superel.addContent(new Element(el.getName()));
                    }
                    if(ElementHasChild(rootel,el))
                    {
                        rootel.removeContent(el);
                    }
                }
            }
        }
        
        save();
    }

    private boolean ElementHasChild(Element parentel,Element childel)
    {
        Iterator it = parentel.getDescendants(new ElementFilter(childel.getName()));
        if(it.hasNext())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    private Element getElementWithName(String classname)
    {
        Element el;

        Iterator it = rootel.getDescendants(new ElementFilter(classname));
        if(it.hasNext())
        {
            return (Element) it.next();
        }
        return null;
    }

    private String getSuperClassName(File f)
    {
        BufferedReader bfr = null;
        try {
            String line = "";
            bfr = new BufferedReader(new FileReader(f));
            int max=250;
            int count=0;
            while(((line=bfr.readLine())!=null)&&count<max)
            {
                //class AssaultRifleArena extends Arena;
                if(line.contains("extends"))
                {
                   return getSuperClassName(line);
                }
                count ++;
            }

        } catch (IOException ex) {
            Logger.getLogger(UTActorDB.class.getName()).log(Level.SEVERE, null, ex);
        }  finally {
            try {
                bfr.close();
            } catch (IOException ex) {
                Logger.getLogger(UTActorDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

    /**
     * class AdminManager extends Object within PlayerController;
     * class AssaultRifleArena extends Arena;
     * class NIU2 extends U2Mutator config(NIU2);
     * @param line
     */
    private String getSuperClassName(String line)
    {
        String t;
        t = line.split("extends ")[1];
        if(t.contains(" "))
        {
            String a[] = t.split("\\ ");
            t = a[0].split("\\;")[0];
        }
        else
        {
            t = t.split("\\;")[0];
        }


        return removeChar(t, 32);
    }


    private String removeChar(String line,int numchar)
    {
        String tmp="";
        int nchar=0;

        for(int i=0;i<line.length();i++)
        {
            nchar=(int)line.charAt(i);

            if(nchar!=numchar)
            {
                tmp += line.charAt(i);
            }
        }
        return tmp;
    }

    public void save()
    {
        try {
            XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
            sortie.output(doc, new FileOutputStream(xmlref_file));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }


}
