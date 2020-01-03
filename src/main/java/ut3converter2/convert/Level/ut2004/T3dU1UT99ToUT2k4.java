/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut2004;

import java.io.*;
import ut3converter2.T3DConvertor;
import ut3converter2.UTGames;
import ut3converter2.UTObject;
import ut3converter2.convert.Level.SupportedClasses;

/**
 * Converts UT/U1 T3D Files to UT2004 T3D File
 * Used mainly for specific actors that requires detailling data parsing such as FortStandard
 * ..
 * @author Hyperion
 */
public class T3dU1UT99ToUT2k4 extends T3DConvertor{

    String tskname="*** Converting T3D Level Actor deeply ***";
    File t3dfilein;
    File t3dfileout;
    SupportedClasses sc;
    float soundradiusratio = 16F;
    float soundvolratio = 1.2F;
    int inputgame=UTGames.U1;
    /**
     * Sound=Sound'UnrealI.Queen.yell2Q'
     * ->Sound=Sound'yell2Q'
     */
    boolean usesimplename_snds=true;

    public T3dU1UT99ToUT2k4(File t3dfilein, File t3dfileout, int inputgame) {
        this.t3dfilein = t3dfilein;
        this.t3dfileout = t3dfileout;
        this.inputgame = inputgame;
        this.setTaskname(tskname);
    }

    public void setUsesimplename_snds(boolean usesimplename_snds) {
        this.usesimplename_snds = usesimplename_snds;
    }

    public void convert() throws FileNotFoundException, IOException
    {
        if(isBShowLog()){System.out.println(tskname);}

        //BufferedReader bfr = FileDecoder.getBufferedReader(t3dfilein, "Begin");
        String tmp="";
        BufferedReader bfr = new BufferedReader(new FileReader(t3dfilein));
        BufferedWriter bwr = new BufferedWriter(new FileWriter(t3dfileout));
        //BufferedWriter bwr = new BufferedWriter(new FileWriter(t3dfileout));
        String line="";
        boolean bupdatetransientrad=false;
        AssaultObjective ao = null;
        FlagBase fb=null;
        LevelInfo li=null;
        String curclass="";

        while((line=bfr.readLine())!=null)
        {
            if(line.contains("Begin Actor"))
            {
                curclass = getActorClass(line);
                if(curclass.equals("FortStandard")){ao = new AssaultObjective();}
                else if(curclass.equals("FlagBase")){fb = new FlagBase();}
                else if(curclass.equals("LevelInfo")){li = new LevelInfo();}
                else
                {
                    bwr.write(line+"\n");
                }
            }
            else if(line.contains("End Actor"))
            {
                if(curclass.equals("FortStandard")){bwr.write(ao.toString());}
                if(curclass.equals("FlagBase")){bwr.write(fb.toString());}
                else if(curclass.equals("LevelInfo")){bwr.write(li.toString());}
                else if(bupdatetransientrad)
                {
                    bwr.write("    TransientSoundRadius=0.1\n");
                    bwr.write(line+"\n");
                    bupdatetransientrad = false;
                }
                else {bwr.write(line+"\n");}
            }
            //LE_FireWaver
            //LE_WateryShimmer
            else if(line.contains("bHiddenEd"))
            {

            }
            else
            {
                if(curclass.equals("FortStandard")){ao.AnalyseT3DData(line);}
                else if(curclass.equals("FlagBase")){fb.AnalyseT3DData(line);}
                else if(curclass.equals("LevelInfo")){li.AnalyseT3DData(line);}
                else if(!curclass.equals("Brush"))
                {
                    //    Sound=Sound'DDay.(All).motive2'
                    if(line.contains("LightEffect"))
                    {
                        line = Light.UTLightEffect2UT2k4LE(line);
                    }
                    if(line.contains("Sound="))
                    {
                        line = line.replace(")","");
                        line = line.replace("(", ""); 
                    }
                    if(line.contains("AmbientSound="))
                    {
                        
                        //    AmbientSound=Sound'AmbAncient.Looping.afire3'
                        //With Unreal 1 it exports sounds with Group.Name.wav format
                        //So have to rename to Group_Name
                        //    AmbientSound=Sound'ambmodern.Looping.bigelec7'
                        if(inputgame==UTGames.U1)
                        {
                            line = removePacAndMergeGroupName(line);
                        }
                        else //UT
                        {
                            line = removePacAndGroup(line);
                        }
                        
                    }
                    //    SoundRadius=128
                    //    SoundVolume=160
                    if(curclass.contains("AmbientSound")) //Also for DynamicAmbientSound
                    {
                        if(line.contains("SoundRadius="))
                        {
                            line = line.split("\\=")[0]+"="+(int)(Float.valueOf(line.split("\\=")[1])*soundradiusratio);
                            bupdatetransientrad = true;
                        }
                    }
                    bwr.write(line+"\n");
                }
                else {bwr.write(line+"\n");}
            }
        }

        bfr.close();
        bwr.close();   
    }


    /**
     * AmbientSound=Sound'Ambient.Sound.Ocean'
     * -->AmbientSound=Sound'Ocean'
     * @param line
     * @return
     */
    private String removePacAndGroup(String line)
    {
        String buffer[] = line.split("\\'");
        String t[]=buffer[1].split("\\.");
        return buffer[0]+"'"+t[t.length-1]+"'";
    }

    /**
     * AmbientSound=Sound'Ambient.Sound.Ocean'
     * -->AmbientSound=Sound'Sound_Ocean'
     * @param line
     * @return
     */
    private String removePacAndMergeGroupName(String line)
    {
        UTObject uto = new UTObject(line.split("\\'")[1]);
        String buffer[] = line.split("\\'"); //Ambient.Sound.Ocean
        return buffer[0]+"'"+uto.getGroupAndName2()+"'";
    }

    private String getActorClass(String line)
    {
        return (line.split("=")[1]).split(" ")[0];
    }

}
