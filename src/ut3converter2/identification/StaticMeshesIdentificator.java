/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.identification;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import ut3converter2.Configuration;
import ut3converter2.Main;
import ut3converter2.UTGames;
import ut3converter2.UTObject;
import ut3converter2.convert.MapConverter;
import ut3converter2.T3DConvertor;
import ut3converter2.tools.FileDecoder;

/**
 * Class for identifying StaticMeshes used in T3D Level file
 * @author Hyperion
 */
public class StaticMeshesIdentificator extends T3DConvertor {

    final String tskname="\n*** Identification of Staticmeshes used ***";
    File ut3t3dlevel;
    HashMap hmsmpack;
    ArrayList alpacsm;
    ArrayList alunexpsmfull;
    HashMap hmsm2pac;
    int utgame;
    boolean bshowlog=true;
    MapConverter mc;
    //public StaticMeshesIdentificator(File ut3t3dlevel,MapConverter mc) throws FileNotFoundException, IOException  {
    public StaticMeshesIdentificator(File ut3t3dlevel,int utgame) throws FileNotFoundException, IOException  {
        //public StaticMeshesIdentificator(File ut3t3dlevel,MapConverter mc) throws FileNotFoundException, IOException  {
        this.ut3t3dlevel = ut3t3dlevel;
        super.setTaskname(tskname);
        hmsmpack = new HashMap();
        hmsm2pac = new HashMap();
        alpacsm = new ArrayList();
        this.utgame = utgame;
        //this.utgame = mc.getInput_utgame();
    }

    public void identifyStaticMeshes() throws FileNotFoundException, IOException
    {
        String line="";
        String smpac[] = null;
        System.out.println("("+ut3t3dlevel.getName() + FileDecoder.getFileEncodingFormat(ut3t3dlevel, "Begin")+" encoding)");
        /*
        BufferedReader bfr = new BufferedReader(
                new InputStreamReader(
                new FileInputStream(ut3t3dlevel),FileDecoder.getFileEncodingFormat(ut3t3dlevel, "Begin")));
        */
        BufferedReader bfr = new BufferedReader(new FileReader(ut3t3dlevel));
        //BufferedReader bfr = FileDecoder.getBufferedReader(ut3t3dlevel, "Begin");
        ArrayList al = new ArrayList();
        try {
            Thread.sleep(100L);
        } catch (InterruptedException ex) {
            Logger.getLogger(StaticMeshesIdentificator.class.getName()).log(Level.SEVERE, null, ex);
        }

        if(super.isBShowLog()){System.out.print(super.getTaskname());}
        int count=0;

        
        while((line=bfr.readLine())!=null)
        {
            //    rain.Deco',StaticMesh=StaticMesh'TowerStatic.Rock.TowerDecoGrassA',Sc
            //    StaticMesh=StaticMesh'UNR.Plain.U'
            if(line.contains("StaticMesh=S"))
            {
                if(utgame==UTGames.UT2004)
                {
                    smpac=getSmPack(line);
                }
                //DecoLayers.....s.decolayer_02',StaticMesh=StaticMesh'Mission_05M.Vegetation.SwampPlantA02',Scal
                else if(utgame==UTGames.U2)
                {
                    smpac=getSmPackU2(line);
                }
                
                if(!hmsm2pac.containsKey(smpac[1]+".t3d"))
                {
                   hmsm2pac.put(smpac[1]+".t3d",smpac[0]);
                }
                if(!alpacsm.contains(smpac[1]+".t3d"))
                {
                    alpacsm.add(smpac[1]+".t3d");
                }
                if((!hmsmpack.containsKey(smpac[0]))&&(!smpac[0].equals("None")))
                {
                    al.clear();
                    al.add(smpac[1]);
                    hmsmpack.put(smpac[0], al);
                }
                else if (hmsmpack.containsKey(smpac[0]))
                {
                    al = (ArrayList) hmsmpack.get(smpac[0]);
                    al.add(smpac[1]);
                    hmsmpack.put(smpac[0], al);
                }

                count ++;
            }
        }
        bfr.close();
        
        if(super.isBShowLog())
        {
            System.out.println("... done!");
            System.out.println(getSMUsedHtml());
        }
        
    }

    public ArrayList getAlunexpsmfull() {
        return alunexpsmfull;
    }

    public void setAlunexpsmfull(ArrayList alunexpsmfull) {
        this.alunexpsmfull = alunexpsmfull;
    }

    public ArrayList getUnexportedSm(File inputfolder)
    {
        File f[] = inputfolder.listFiles();
        ArrayList al = new ArrayList();
        ArrayList al2 = new ArrayList();
        alunexpsmfull = new ArrayList();
        
        for(int i=0;i<f.length;i++)
        {
                al.add(f[i].getName().replaceAll(".t3d", ""));
        }

        String smname="";
        for (Iterator it = hmsm2pac.keySet().iterator(); it.hasNext();) {
            smname = it.next().toString();

            if(!al.contains(getSubOriginalSMName(smname)))
            {
                al2.add(new String[]{hmsm2pac.get(smname).toString(),getGroup(smname),getName(smname)});
                alunexpsmfull.add(getOriginalSMName(smname));
            }
        }
        
        //System.out.println(alunexpsmfull);
        return al2;
    }

    private String getSubOriginalSMName(String smname)
    {
        if(getGroup(smname).length()<2)
        {
            return new String(getName(smname));
        }
        else
        {
            return new String(getGroup(smname)+"."+getName(smname));
        }
    }

    private String getOriginalSMName(String smname)
    {
        if(getGroup(smname).length()<2)
        {
            return new String(hmsm2pac.get(smname).toString()+"."+getName(smname));
        }
        else
        {
            return new String(hmsm2pac.get(smname).toString()+"."+getGroup(smname)+"."+getName(smname));
        }
    }
    
    private String getGroup(String texname)
    {
        String tmp=removeExt(texname);
        String t[] = tmp.split("\\.");
        if(t.length==1)
        {
            return "";
        }
        else if(t.length==2)
        {
            return t[0];
        }
        return tmp;
    }

    private String getName(String texname)
    {
        String tmp=removeExt(texname);
        String t[] = tmp.split("\\.");
        if(t.length==1)
        {
            return t[0];
        }
        else if(t.length==2)
        {
            return t[1];
        }
        return tmp;
    }

    private String removeExt(String texname)
    {
        return texname.replaceAll(".t3d", "");
    }

    public File[] getFileSm2Del(File smfolder)
    {
        File listsmfiles[] = smfolder.listFiles();
        ArrayList al = new ArrayList();
       // System.out.println(alpacsm);
        for(int i=0;i<listsmfiles.length;i++)
        {
            //System.out.println(listtexfiles[i].getName());
            if(!alpacsm.contains(listsmfiles[i].getName()))
            {
               al.add(listsmfiles[i]);
            }
        }
        File listfiltodel[] = new File[al.size()];
        for (int i = 0; i < al.size(); i++) {
            File fil = (File) al.get(i);
            listfiltodel[i]=fil;
        }
        return listfiltodel;
    }

    private String[] getSmPackU2(String line)
    {
        //    StaticMesh=StaticMesh'Egypt_techmeshes_Epic.Deco.egypt_techpassage'
        //    StaticMesh=StaticMesh'Rahnem_Glacier.Base.closed_box3'
        ////DecoLayers.....s.decolayer_02',StaticMesh=StaticMesh'Mission_05M.Vegetation.SwampPlantA02',Scal
        UTObject uto = new UTObject((line.split("StaticMesh=")[1]).split("\\'")[1]);
        return new String[]{uto.getPackagename(),uto.getName()};
    }

    private String[] getSmPack(String line)
    {
        //    ... rain.Deco',StaticMesh=StaticMesh'TowerStatic.Rock.TowerDecoGrassA',Sc ...
        //    StaticMesh=StaticMesh'Rahnem_Glacier.Base.closed_box3'
        UTObject uto = new UTObject((line.split("StaticMesh=")[1]).split("\\'")[1]);
        return new String[]{uto.getPackagename(),uto.getGroupAndName()};
    }

    public Set getPackageNamesUsed()
    {
        return hmsmpack.keySet();
    }

    public String getSMUsedHtml()
    {
        String result = "Packages used:\n";
        Iterator it = hmsmpack.keySet().iterator();
        while(it.hasNext())
        {
            result += it.next().toString();
            if(it.hasNext())
            {
                result += ",";
            }

        }

        return result;
    }
    
    public File[] getFileSmFiles(Configuration conf,int utgame)
    {
        
        ArrayList<File> alf=new ArrayList<File>();
        if(utgame==UTGames.U1)
        {
            //No staticmeses for Unreal 1
        }
        else if(utgame==UTGames.U2)
        {
            int i=0;
            for (Iterator it = getPackageNamesUsed().iterator(); it.hasNext();) {
                String texpackname = it.next().toString();
                File f = new File(conf.getU2RootFolder()+"/StaticMeshes/"+texpackname+".usx");
                if(f.exists()){alf.add(f);}
                else
                {
                    f = new File(conf.getU2RootFolder()+"/System/"+texpackname+".u");
                    if(f.exists()){alf.add(f);}
                }
                
                i++;
            }
        }
        else if(utgame==UTGames.UT99)
        {
            //No staticmeshes for Unreal Tournament
        }
        else if(utgame==UTGames.UT2003)
        {
            int i=0;
            for (Iterator it = getPackageNamesUsed().iterator(); it.hasNext();) {
                String texpackname = it.next().toString();
                File f = new File(conf.getUT2003RootFolder()+"/StaticMeshes/"+texpackname+".usx");
                if(f.exists()){alf.add(f);}
                else
                {
                    f = new File(conf.getUT2003RootFolder()+"/System/"+texpackname+".u");
                    if(f.exists()){alf.add(f);}
                }
                i++;
            }
        }
        else if(utgame==UTGames.UT2004)
        {
            int i=0;
            for (Iterator it = getPackageNamesUsed().iterator(); it.hasNext();) {
                String texpackname = it.next().toString();
                File f = new File(conf.getUT2004RootFolder()+"/StaticMeshes/"+texpackname+".usx");

                if(f.exists()){alf.add(f);}
                else
                {
                    f = new File(conf.getUT2004RootFolder()+"/System/"+texpackname+".u");
                    if(f.exists()){alf.add(f);}
                }
                i++;
            }
        }

        File listex[]= new File[alf.size()];
        for(int i=0;i<listex.length;i++)
        {
            listex[i] = alf.get(i);
        }
        return listex;
    }



}
