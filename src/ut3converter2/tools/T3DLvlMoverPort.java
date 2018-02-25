/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import ut3converter2.convert.Level.SupportedClasses;
import ut3converter2.T3DConvertor;

/**
 * Converts movers from t3d file. Make Volume actor containing shape and SM mover.
 * Basically used to port U1/UT maps to UT2004/UT3.
 * @author Hyperion
 */
public class T3DLvlMoverPort extends T3DConvertor{

    String tskname="\n*** Converting UT99 movers in T3D Level to InterpActor(UT3 mover) and LightVolume actors ***";
    File t3dfile;
    File outputfile;
    /**
     * Default mesh rendered for the mover.
     */
    String defaultSM="2k4ChargerMeshes.ChargerMeshes.HealthChargerMESH-DS";
    boolean buseut99mode=false;

    public T3DLvlMoverPort(File t3dfile, File outputfile) {
        this.t3dfile = t3dfile;
        this.outputfile = outputfile;
        super.setTaskname(tskname);
    }

    public void setBuseut99mode(boolean buseut99mode) {
        this.buseut99mode = buseut99mode;
    }


    public void setDefaultSM(String defaultUT2k4SM) {
        this.defaultSM = defaultUT2k4SM;
    }

    public void convert() throws IOException
    {
        if(super.isBShowLog()){System.out.println(super.getTaskname());}
        if(super.isBShowLog()){System.out.println("Default Staticmesh: "+defaultSM);}
        if(super.isBShowLog()){System.out.print(t3dfile.getName()+"->"+outputfile.getAbsolutePath());}


        BufferedReader bfr = FileDecoder.getBufferedReader(t3dfile, "Begin");

        //BufferedWriter bwr = new BufferedWriter(new FileWriter(outputfile));
        BufferedWriter bwr = new BufferedWriter(new FileWriter(outputfile));
        
        String line="";
        String curclass="";
        String buffer="";
        String tmp2="";
        SupportedClasses sc = new SupportedClasses(new ArrayList());
        ArrayList alsupclass = new ArrayList();
        alsupclass.add("all");

        sc.setAlsupclasses(alsupclass);
        //ArrayList albanclass = new ArrayList();
        //albanclass.add("SniperRifle");
        //sc.setAlbanclasses(albanclass);

        boolean ismoverbrush=false;
        boolean ismover=false;
        boolean portclass=false;
        bwr.write("Begin Map\n");
        
        while((line=bfr.readLine())!=null)
        {
            
            if(line.contains("Begin Actor"))
            {
                curclass = getActorClass(line);
                portclass = sc.isSupportedClass(curclass);
                if(portclass)
                {
                  if(curclass.toLowerCase().contains("mover"))
                    {
                        //PhysicsVolume
                        if(!buseut99mode)
                        {
                          bwr.write(line.replaceAll(curclass, "Volume")+"\n");
                        }
                        else //Volume is abstract class in UT3 so has to use Volume
                        {
                          bwr.write(line.replaceAll(curclass, "LightVolume")+"\n");
                        }
                        ismover = true;
                    }
                    else
                    {
                        bwr.write(line+"\n");
                        ismover = false;
                    }
                }
                
            }
            else if(line.contains("End Actor"))
            {
                if(portclass)
                {
                    bwr.write(line+"\n");
                    if(ismover)
                    {
                        tmp2 = "Begin Actor Class="+curclass+" Name=Mover0\n";
                        tmp2 +="    DrawType=DT_StaticMesh\n";
                        tmp2 +="    StaticMesh=StaticMesh\'"+defaultSM+"\'\n";
                        tmp2 +="    DrawType=DT_StaticMesh\n";
                        tmp2 +="    bBlockKarma = True\n";
                        tmp2 +="    bStaticLighting = True\n";
                        bwr.write(tmp2+buffer+"\n");
                        bwr.write(line+"\n");
                    }
                }
                
                
                ismover = false;
                portclass = false;
                buffer ="";
                curclass="";
            }
            else if(ismover)
            {
                //buffer += line+"\n";
                if(line.contains("PrePivot")||line.contains("Rotation"))
                {
                    buffer += line+"\n";
                }
                else if(line.contains("Begin Brush"))
                {
                    ismoverbrush = true;
                    bwr.write(line+"\n");
                }
                else if(line.contains("End Brush"))
                {
                    ismoverbrush = false;
                    bwr.write(line+"\n");
                }
                else if(ismoverbrush)
                {
                    bwr.write(line+"\n");
                }
                else
                {
                    buffer += line+"\n";
                    bwr.write(line+"\n");
                }
                
            }
            else if(portclass)
            {
                bwr.write(line+"\n");
            }
        }
        bwr.write("End Map\n");
        bfr.close();
        bwr.close();
        if(super.isBShowLog()){System.out.println(" ... done!");}
    }
    private String getActorClass(String line)
    {
        return (line.split("=")[1]).split(" ")[0];
    }

}
