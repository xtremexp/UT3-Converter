/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ut3converter2.convert.Level.UTActor;
import ut3converter2.convert.Level.ut3.AmbientSoundSimple;

/**
 *
 * In UT99 every actor can have ambientsound set.
 * That's why we need to create AmbientSound actor for each actor which have some sound set
 * @author Hyperion
 */
public class T3DUT99ASMaker {

    File t3dfilein;
    File t3dfileout;
    Float soundradiusfactor;

    String pacname="UT3Map";
    String sndname="";
    String otherdata="";
    Float soundradius=64F;
    float soundvolume=1F; //100
    float soundpitch=1F; //64

     /**
     * Used for UT99->UT3 conversion (not using group info in wav filename)
     */
    boolean buseut99sndname=false;

    boolean bhasambientsound=false;

    public T3DUT99ASMaker(File t3dfilein, File t3dfileout, Float soundradiusfactor,String pacname) {
        this.t3dfilein = t3dfilein;
        this.t3dfileout = t3dfileout;
        this.soundradiusfactor = soundradiusfactor;
        this.pacname = pacname;
    }

    public void convert()
    {
        AmbientSoundSimple ass;
        UTActor uta = new UTActor();
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(t3dfilein));
            BufferedWriter bwr = new BufferedWriter(new FileWriter(t3dfileout));
            String line="";
            String curclass="";

            while((line=bfr.readLine())!=null)
            {
                bwr.write(line+"\n");


                
                if(line.contains("Begin Actor"))
                {
                    curclass = UTActor.getActorClass(line);
                }
                if(!curclass.equals("Brush"))
                {
                    uta.parseOtherData(line);
                }
                if(line.contains(" AmbientSound=")) //Movers have MoveAmbientSound
                {
                    bhasambientsound = true;
                    getSndName(line);
                }
                else if(line.contains(" SoundRadius="))
                {
                    soundradius = Float.valueOf(line.split("\\=")[1]);
                }
                else if(line.contains(" SoundVolume="))
                {
                    soundvolume = Integer.valueOf(line.split("\\=")[1]);
                    soundvolume = soundvolume/255F;
                }

                //UT: SoundPitch: 0--64(default)-->128
                //UT3: SoundPitch: 0--1(default)-->2
                else if(line.contains("SoundPitch="))
                {
                    soundpitch = Float.valueOf(line.split("\\=")[1]); //42
                    soundpitch = soundpitch/64F;
                }
                else if(line.contains("End Actor"))
                {
                    if(!curclass.equals("AmbientSound")&&bhasambientsound)
                    {
                        ass = new AmbientSoundSimple(pacname, sndname);
                        ass.setSoundpitch(soundpitch);
                        ass.setSoundradius(soundradius);
                        ass.setSoundvolume(soundvolume);
                        ass.setOtherdata(uta.getOtherdata());
                        bwr.write(ass.toString());
                        bhasambientsound = false;
                    }
                    uta = new UTActor();
                }
            }
            bwr.close();
            bfr.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(T3DUT99ASMaker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(T3DUT99ASMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getSndName(String line)
    {
       String tmp;
       String tmp2[];
       //    AmbientSound=Sound'OutdoorAmbience.BThunder.caveambience3'
       //AmbientSound=Sound'AmbModern.Looping.alarm3'
       tmp = line.split("\\'")[1]; //AmbModern.Looping.alarm3
       tmp2 = tmp.split("\\.");
       if(tmp2.length==2)
       {
           sndname=tmp2[1]+"-44k";
       }
       else if(tmp2.length==3)
       {
           if(buseut99sndname){sndname=tmp2[2]+"-44k";}
           else{
               sndname=tmp2[1]+"_"+tmp2[2]+"-44k";
           }
       }
    }

    public void setBuseut99sndname(boolean buseut99sndname) {
        this.buseut99sndname = buseut99sndname;
    }

    public void setOtherdata(String otherdata) {
        this.otherdata = otherdata;
    }


}
