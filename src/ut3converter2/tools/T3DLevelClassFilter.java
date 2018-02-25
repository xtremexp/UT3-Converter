/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Filter actors in T3D Level files.
 * @author Hyperion
 */
public class T3DLevelClassFilter {

    File t3dfile;
    File outputfile;
    String excludedclassnames;
    String includedclassnames;

    public final static int MODE_DEFAULT = 0;
    public final static int MODE_CLASSES = 1;
    public final static int MODE_NUMACTOR = 2;
    public final static int MODE_DELLINE_WITHEXP = 3;
    boolean bcopyafternumactor=false;
    int numcopyactorafter=0;
    int numtotalactors=0;
    int numclassescopied=0;
    int numclassesexcluded=0;

    int mode = MODE_DEFAULT;
    String linewithexptodel;
    /**
     * Actor class names (e.g.: "Light","Brush") to filter (won't be copied)
     */
    ArrayList<String> alclass_exclude;
    /**
     * Actor class names that will be copied
     */
    ArrayList<String> alclass_include;

    public T3DLevelClassFilter(File t3dfile, File outputfile,String excludedclasses,String includedclasses) {
        this.t3dfile = t3dfile;
        this.outputfile = outputfile;
        this.excludedclassnames=excludedclasses;
        this.includedclassnames=includedclasses;
        loadAlExcludedClasses();
        loadAlIncludedClasses();
        this.mode = MODE_CLASSES;
    }

    public T3DLevelClassFilter(File t3dfile, File outputfile,String linewithexptodel) {
        this.t3dfile = t3dfile;
        this.outputfile = outputfile;
        this.linewithexptodel = linewithexptodel;
        this.mode = MODE_DELLINE_WITHEXP;
    }

    public void setNumActorToCopyAfter(int numactor)
    {
        this.bcopyafternumactor = true;
        this.numcopyactorafter = numactor;
    }
    
    public int getNumtotalactors() {
        return numtotalactors;
    }

    public int getNumclassescopied() {
        return numclassescopied;
    }

    public int getNumclassesexcluded() {
        return numclassesexcluded;
    }


    private void loadAlExcludedClasses()
    {
        alclass_exclude = new ArrayList();
        String t[] = this.excludedclassnames.split("\\,");

        for (int i = 0; i < t.length; i++)
        {
            alclass_exclude.add(t[i]);
        }
    }

     private void loadAlIncludedClasses()
    {
        alclass_include = new ArrayList();
        String t[] = this.includedclassnames.split("\\,");

        for (int i = 0; i < t.length; i++)
        {
            alclass_include.add(t[i]);
        }
    }

    public void filter() throws FileNotFoundException, IOException
    {
        boolean copy=false;

        String line="";
        int numactor=1;
        BufferedReader bfr = FileDecoder.getBufferedReader(t3dfile, "Begin");
        BufferedWriter bwr = new BufferedWriter(new FileWriter(outputfile));

        //BufferedReader bfr = new BufferedReader(new FileReader(t3dfile));
        //BufferedWriter bwr = new BufferedWriter(new FileWriter(outputfile));
        String curclass="";
        
        String tmp="";

        bwr.write("Begin Map\n");

        while((line=bfr.readLine())!=null)
        {
            if(line.contains("Begin Map")||line.contains("End Map")){
                copy = false;
            }

            if(line.contains("Begin Actor")||line.contains("Begin Terrain"))
            {
                    numtotalactors ++;
                    if(line.contains("Class="))
                    {
                        curclass = getActorClass(line);
                    }

                    if(line.contains("Begin Terrain")){
                        curclass = "Terrain";
                    }
                    if(alclass_exclude==null){copy=true;}
            }
            else if(line.contains("End Actor")||line.equals("      End Terrain"))
            {
                copy = false;
            }

            if(mode==MODE_DELLINE_WITHEXP){
                if(!line.contains(linewithexptodel)){
                    bwr.write(line+"\n");
                }
            } else if(mode == MODE_CLASSES){
                if(alclass_include.contains(curclass)){
                    copy = true;
                    numclassescopied++;
                } else {
                    copy = false;
                    numclassesexcluded++;
                }
                if(copy){
                    bwr.write(line+"\n");
                }
            }

            else {
                
            }
            
        }

        bwr.write("End Map\n");
        bfr.close();
        bwr.flush();
        bwr.close();
        
    }

    private String getActorClass(String line)
    {
        return (line.split("=")[1]).split(" ")[0];
    }


}
