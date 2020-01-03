/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import ut3converter2.Installation;
import ut3converter2.Main;

/**
 *
 * @author Hyperion
 */
public class FileReplacement {

    File conffile;
    File inputfile;
    File outputfile;
    TextReplacer trclass;
    TextReplacer trall;
    ArrayList alsingleclass;
    HashMap hmconv;
    HashMap hmweaptype;
    HashMap <String,HashMap>hmsinglecl;
    String tmp="";

    public FileReplacement(File conffile, File inputfile, File outputfile) throws FileNotFoundException, IOException {
        this.conffile = conffile;
        this.inputfile = inputfile;
        this.outputfile = outputfile;
        this.trall = new TextReplacer();
        this.trclass = new TextReplacer();
        hmconv = new HashMap();
        hmweaptype = new HashMap();
        alsingleclass = new ArrayList();
        hmsinglecl = new HashMap();
        loadHmCon();
    }

    private String getActorClass(String line)
    {
        return (line.split("=")[1]).split(" ")[0];
    }

    private String getActorName(String line)
    {
        return line.split("=")[2];
    }

    public void replace() throws FileNotFoundException, IOException
    {
        String line="";
        //String t3dfileformat = FileDecoder.getFileEncodingFormat(inputfile, "Begin");

        //BufferedReader bfr = FileDecoder.getBufferedReader(inputfile, "Begin");
        BufferedReader bfr = new BufferedReader(new FileReader(inputfile));
        BufferedWriter bwr = new BufferedWriter(new FileWriter(outputfile));

        //BufferedReader bfr = new BufferedReader(new FileReader(inputfile));
        //BufferedWriter bwr = new BufferedWriter(new FileWriter(outputfile));
        String t[];
        String curclass="";
        String curname="";
        boolean skip=false;
        boolean checksingleclass=false;
        try {
            //PackageUtils.getClasseNamesInPackage("UT3Converter2.jar","ut3converter.convert.Level.ut2004");
            Class cl[] = PackageUtils.getClasses("ut3converter2.convert.Level.ut2004");
            Method m[] = cl[0].getMethods();
            for(int i=0;i<m.length;i++)
            {
                System.out.println(m[i].getName());
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileReplacement.class.getName()).log(Level.SEVERE, null, ex);
        }

        while((line=bfr.readLine())!=null)
        {
            
            if(line.contains("Begin Brush"))
            {
                skip = true;
                bwr.write(line+"\n");
            }
            else if(line.contains("End Brush"))
            {
                skip = false;
                bwr.write(line+"\n");
            }
            else if(!skip)
            {
                if(line.contains("Sound=")||line.contains("=Class'"))
                {
                    line = replaceSound(line);
                }
                if(line.contains("Begin Actor"))
                {
                    checksingleclass = false;
                    curclass = getActorClass(line);
                    curname = getActorName(line);
                    if(alsingleclass.contains(curclass))
                    {
                        checksingleclass = true;
                    }

                    bwr.write(trclass.replace(line)+"\n");
                }
                else
                {
                    if(checksingleclass)
                    {
                        bwr.write(replaceLine(line, hmsinglecl.get(curclass)));
                    }
                    else
                    {
                        line = trall.replace(line);
                        bwr.write(line+"\n");
                    }
                }
            }
            else if(skip)
            {
                bwr.write(line+"\n");
            }
        }

        bwr.close();
        bfr.close();
    }


    private String replaceSound(String line)
    {
        String prefix=line.split("\\=")[0];
        if(!line.contains("\\'"))
        {
            return line;
        }
        else
        {
            line = line.split("\\'")[1];
            line = line.split("\\'")[0];
            String t[] = line.split("\\.");
            return "    "+prefix+"="+t[t.length-1];
        }
    }
    
    private String replaceLine(String line,HashMap hm)
    {
        String oldtext="";
        Iterator it = hm.keySet().iterator();
        boolean done=false;
        while((it.hasNext())&&(!done))
        {
            oldtext = it.next().toString();
            if(line.contains(oldtext))
            {
                done = true;
                return line.replaceAll(oldtext, hm.get(oldtext).toString()+"\n");
            }

        }
        return line+"\n";
    }

     private static HashMap loadHmCon2(File conffile2) throws FileNotFoundException, IOException
    {
        String line="";
        BufferedReader bfr = new BufferedReader(new FileReader(conffile2));
        String t[];
        HashMap hm = new HashMap();

        while((line=bfr.readLine())!=null)
        {
            if(!line.contains("#")&&line.length()>2)
            {
                if(line.contains(";"))
                {
                    t = split2(line);
                    hm.put(t[0],t[1]);
                }
                
            }
        }

        bfr.close();
        return hm;
    }



    private void loadHmCon() throws FileNotFoundException, IOException
    {
        String line="";
        BufferedReader bfr = new BufferedReader(new FileReader(conffile));
        String t[];
        String singleclass="";
        boolean bclasschange=false;
        boolean balllines=false;

        while((line=bfr.readLine())!=null)
        {
            if(!line.contains("#")&&line.length()>2)
            {
                if(line.contains("[ActorClass]"))
                {
                    bclasschange = true;
                }
                else if(line.contains("[All]"))
                {
                    balllines = true;
                }
                else if(line.contains("["))
                {
                    singleclass = line.split("\\[")[1];
                    singleclass = singleclass.split("\\]")[0];
                    alsingleclass.add(singleclass);
                    bclasschange = false;
                    balllines = false;
                }
                else
                {
                    if(line.contains(";"))
                    {
                        t = split1(line);
                        if(bclasschange)
                        {
                            trclass.add(t[0], t[1]);
                        }
                        if(balllines)
                        {
                            trall.add(t[0], t[1]);
                        }
                    }
                    else if(line.contains("-"))
                    {
                        t = splitDash2(line);
                        File a = new File(Installation.getInstallDirectory(Main.class).getAbsolutePath()+"//conf//"+t[1]);
                        if(a.exists())
                        {
                            hmsinglecl.put(singleclass,loadHmCon2(a));
                        }
                    }
                }
                
            }
        }

        bfr.close();
    }

    private static String[] split2(String line)
    {
        String t[] = line.split(";");

        if(t.length==2)
        {
            return new String[]{t[0], t[1]};
        }
        else if(t.length==1)
        {
            return new String[]{t[0], ""};
        }

        return new String[]{""};
    }

     private static String[] splitDash2(String line)
    {
        String t[] = line.split("-");

        if(t.length==2)
        {
            return new String[]{t[0], t[1]};
        }
        else if(t.length==1)
        {
            return new String[]{t[0], ""};
        }

        return new String[]{""};
    }

    private String[] split1(String line)
    {
        String t[] = line.split(";");

        if(t.length==2)
        {
            return new String[]{t[0], t[1]};
        }
        else if(t.length==1)
        {
            return new String[]{t[0], ""};
        }

        return new String[]{""};
    }

    
}
