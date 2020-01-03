/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools.umc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import ut3converter2.Main;
import ut3converter2.T3DConvertor;
import ut3converter2.tools.ActorLoader;

/**
 *
 * @author Hyperion
 */
public class UMCReader extends T3DConvertor{

    String tskname="\n*** Updating actors in T3D Level File";
    final String actorname_change="ActorNameChange";
    final String actor_replace="ActorReplacement";
    final String line_replaceall="LineReplaceAll";
    final String line_skip="SkippedLines";

    final int mode_normalcopy=0;
    final int mode_actornamechange=1;
    final int mode_actorreplace=2;
    final int mode_linechange=3;
    final int mode_skipcheck_brush=10;
    final int mode_nocopy=100;

    ArrayList<String> al_skip;
    HashMap hm_anc;
    HashMap hm_ar;
    HashMap hm_lra;
    File umcfile;
    File t3dfile_in;
    File t3dfile_out;

    String coredata="";
    String installpath="";

    public UMCReader(String umcfile,File t3dfilein,File t3dfileout) throws FileNotFoundException, IOException {
        this.umcfile = new File(Main.installfolder.getAbsolutePath()+"/conf/"+umcfile);
        this.t3dfile_in = t3dfilein;
        this.t3dfile_out = t3dfileout;
        hm_anc = new HashMap();
        hm_ar = new HashMap();
        hm_lra = new HashMap();
        al_skip = new ArrayList<String>();
        this.installpath = Main.installfolder.getAbsolutePath();
        super.setTaskname(tskname);
        load();
    }

    public UMCReader(File umcfile,File t3dfilein,File t3dfileout) throws FileNotFoundException, IOException {
        this.umcfile = umcfile;
        this.t3dfile_in = t3dfilein;
        this.t3dfile_out = t3dfileout;
        hm_anc = new HashMap();
        hm_ar = new HashMap();
        hm_lra = new HashMap();
        al_skip = new ArrayList<String>();
        this.installpath = Main.installfolder.getAbsolutePath();
        super.setTaskname(tskname);
        load();
    }

    public void replace() throws FileNotFoundException, IOException
    {
        if(isBShowLog()){System.out.print(tskname+" ("+umcfile.getName()+")");}
        String line="";
        String curactorname="";

        BufferedReader bfr = new BufferedReader(new FileReader(t3dfile_in));
        BufferedWriter bwr = new BufferedWriter(new FileWriter(t3dfile_out));
        int cur_mode=mode_normalcopy;
        String tmp="";

        
        while((line=bfr.readLine())!=null)
        {
            if(line.contains("Begin Actor"))
            {
                cur_mode = mode_normalcopy;
                curactorname = getActorClass(line);
                if(curactorname.equals("Brush"))
                {
                    cur_mode = mode_skipcheck_brush;
                    bwr.write(line+"\n");
                }
                else
                {
                    //Chances actor name (Ex:  GiantRGasbag->GiantGasbag)
                    if((tmp=getReplacementAN(curactorname)).length()>2)
                    {
                        bwr.write(line.replaceAll(curactorname, tmp)+"\n");
                    }
                    //Replace the whole actor
                    else if(hm_ar.containsKey(curactorname))
                    {
                        cur_mode = mode_actorreplace;
                    }
                    //Nothing else just normal copy
                    else
                    {
                        bwr.write(line+"\n");
                    }
                }
            }
            else if(line.contains("End Actor"))
            {
                if(cur_mode==mode_actorreplace)
                {
                    tmp = hm_ar.get(curactorname).toString();
                    tmp = tmp.replace("$coredata", coredata);
                    bwr.write(tmp);
                    coredata="";
                    cur_mode=mode_normalcopy;
                }
                else
                {
                    bwr.write(line+"\n");
                }

                cur_mode = mode_normalcopy;
            }
            else
            {

                if(cur_mode!=mode_skipcheck_brush)
                {
                    if(cur_mode==mode_linechange)
                    {
                        cur_mode = mode_normalcopy;
                    }
                    //If something needs to be changed
                    if((tmp=getReplacementLine(line)).length()>1)
                    {
                        bwr.write(line.replaceAll(tmp, hm_lra.get(tmp).toString())+"\n");
                        cur_mode=mode_linechange; 
                    }

                    if(cur_mode==mode_actornamechange)
                    {
                        bwr.write(line.replaceAll(curactorname, hm_anc.get(curactorname).toString())+"\n");
                    }
                    else if(cur_mode==mode_actorreplace)
                    {
                        analyzeCoreData(line);
                    }
                    else if(cur_mode==mode_normalcopy)
                    {
                        if(!hasSkipline(line)){bwr.write(line+"\n");}
                    }
                    else if(cur_mode==mode_linechange)
                    {
                        bwr.write(line.replaceAll(tmp, hm_lra.get(tmp).toString())+"\n");
                    }
                }
                //For brushes actors
                else
                {
                    if(!hasSkipline(line)){bwr.write(line+"\n");}
                    
                }
                
            }
        }


        bfr.close();
        bwr.close();
        if(isBShowLog()){System.out.println(" ... done!");}
    }

    private boolean hasSkipline(String line)
    {
        if(al_skip==null){return false;}
        else
        {
            for(int i=0;i<al_skip.size();i++)
            {
                if(line.contains(al_skip.get(i)))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private String getReplacementLine(String line)
    {
        Iterator it = hm_lra.keySet().iterator();
        String word="";

        while (it.hasNext())
        {
            word = it.next().toString();
            if(line.contains(word))
            {
                return word;
            }
        }
        return "";
    }

    private String getReplacementAN(String actorname)
    {
        if(hm_anc.containsKey(actorname))
        {
            return hm_anc.get(actorname).toString();
        }
        else
        {
           return "";
        }

    }
    
    private void analyzeCoreData(String line)
    {
        if(line.contains(" Location="))
        {
            this.coredata +=line+"\n";
        }
        else if(line.contains("Rotation="))
        {
            this.coredata +=line+"\n";
        }
        else if(line.contains("DrawScale="))
        {
            this.coredata +=line+"\n";
        }
        else if(line.contains("Tag="))
        {
            this.coredata +=line+"\n";
        }
        else if(line.contains("Name="))
        {
            this.coredata +=line+"\n";
        }
        else if(line.contains("PrePivot="))
        {
            this.coredata +=line+"\n";
        }
    }

    private void load() throws FileNotFoundException, IOException
    {
        String cur_section="";
        String line="";
        BufferedReader bfr = new BufferedReader(new FileReader(umcfile));

        while((line=bfr.readLine())!=null)
        {
            if(isSection(line)){cur_section=getSectionName(line);}
            else if(isValidLine(line))
            {
                if(cur_section.equals(this.actorname_change))
                {
                    toHMActorName(line);
                }
                else if(cur_section.equals(this.line_replaceall))
                {
                    toHMReplaceAll(line);
                }
                else if(cur_section.equals(this.actor_replace))
                {
                    toHMActorReplace(line);
                }
                else if(cur_section.equals(this.line_skip))
                {
                    if((line.length()>=2)&&(!line.contains("#")))
                    {
                        al_skip.add(line);
                    }
                }
            }
        }
        //System.out.println(al_skip);
    }

    private void toHMActorReplace(String line)
    {
        String t[] = line.split("\\;");
        if((t.length==2)&&(!line.contains("#")))
        {
            hm_ar.put(t[0], ActorLoader.getActor(installpath+t[1]));
        }

    }

    private void toHMReplaceAll(String line)
    {
        String t[] = line.split("\\;");
        if((!line.contains("#")))
        {
            if((t.length==2)&&(!line.contains("#")))
            {
                hm_lra.put(t[0], t[1]);
            }
            else if(t.length==1)
            {
                hm_lra.put(t[0], "");
            }
        }
        
    }

    private void toHMActorName(String line)
    {
        String t[] = line.split("\\;");
        if((t.length==2)&&(!line.contains("#")))
        {
            hm_anc.put(t[0], t[1]);
        }
    }
    
    private boolean isValidLine(String line)
    {
        if((line.length()>2)&&(!line.contains("#")))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private String getSectionName(String line)
    {
        line = line.split("\\[")[1];
        return line.split("\\]")[0];
    }

    private boolean isSection(String line)
    {
        if(line.length()>2)
        {
            if(line.contains("[")&&line.contains("]"))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
        
    }

    private String getActorClass(String line)
    {
        return (line.split("=")[1]).split(" ")[0];
    }

}
