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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import ut3converter2.Main;
import ut3converter2.UTGames;
import ut3converter2.UTObject;
import ut3converter2.UTPackage;
import ut3converter2.convert.MapConverter;
import ut3converter2.T3DConvertor;

/**
 *
 * @author Hyperion
 */
public class SoundsIdentificator extends T3DConvertor{

    final String tskname="\n*** Identifications of sounds used ";
    File t3dlevelfile=null;
    ArrayList al;
    final static String SNDFORMAT="wav";

    MapConverter mc;
    /**
     * Set files that won't be deleted later ...
     */
    Set<String> set_texfilestokeep = new HashSet<String>();
    Set<String> set_sndpacused = new HashSet<String>();
    /**
     * Don't record group info while parsing sound data if true.
     * AmbientSound=Sound'U2AmbientA.Hum.Hum23'
     * ->AmbientSound=Sound'Hum23'
     */
    boolean useNameSndOnly=false;

    public SoundsIdentificator(File t3dlevelfile,File sndfolder,int inputgame) {
        this.t3dlevelfile = t3dlevelfile;
        super.setBShowLog(true);
    }

    public SoundsIdentificator(MapConverter mc) {
        this.mc = mc;
        this.t3dlevelfile = mc.getT3dlvlfile();
        super.setBShowLog(true);
    }

    /**
     * When exporting sounds with ucc command,
     * UT doesn't keep the group name in the filename (group.sndname.wav->sndwave.wav)
     * That's why we have to tell it to this class so it can identifies correctl files to del
     * @param bisutu1file False= not using UT/UT1 snd filename
     */
    public void useNameSndOnly(boolean bisutu1file) {
        this.useNameSndOnly = bisutu1file;
    }

    public void identifyLvlSounds() throws FileNotFoundException, IOException
    {
        if(isBShowLog()){System.out.print(tskname);}
         //           MoveAmbientSound=Sound'DoorsAnc.Other.chaindL5'
        String line="";
        String sndpac[];
        //BufferedReader bfr = FileDecoder.getBufferedReader(t3dlevelfile, "Begin");
        BufferedReader bfr = new BufferedReader(new FileReader(t3dlevelfile));
        UTObject uto;


            while((line=bfr.readLine())!=null)
            {
                try {
                    if((line.contains("Sound="))&&(!line.contains("SpawningSound"))&&(!line.contains("CollisionSound"))
                        &&(!line.contains("BreakingSound"))&&(!line.contains("=None")))
                {
                    uto = getSndPack(line);
                    if(uto!=null&&!uto.getName().equals("None")){

                        /*
                         * F:\jeux\U2\System>ucc batchexport ..\Sounds\AidaA.uax Sound wav ..\
                         * Loading package ..\Sounds\AidaA.uax...
                         * Exported Sound AidaA.AtlantisPickup.ImBringingTheAtlantisDown3 to ..\ImBringingTheAtlantisDown3.wav
                         */
                        if(mc.getInput_utgame()==UTGames.UT99||mc.getInput_utgame()==UTGames.U2){
                            set_texfilestokeep.add(uto.getName().toLowerCase()+"."+SNDFORMAT);
                        }//U1 with latest patch export as group.name.wav
                        else {
                            set_texfilestokeep.add(uto.getGroupAndName().toLowerCase()+"."+SNDFORMAT);
                        }
                        set_sndpacused.add(uto.getPackagename());
                    }
                    
                    

                }
                } catch (Exception e) {
                    e.printStackTrace();
                    //System.out.println("\n");
                    //Main.config.getMclog().warning("[SoundIdentification]-"+this.t3dlevelfile.getName()+"- \""+line+"\") "+e.getMessage());
                }
                //        Sound=Sound'SuperSmashTrialVic.KirbyVic'
                //        Sound=Sound'noxxsnd.metal.lift1act'
                 //       SpawningSound=PTSC_Random
                 //       CollisionSound=PTSC_Random used by Emitters
                 //	"    BreakingSound=None" used by BreakingGlass (Unreal 1)
                
            }
        
        if(isBShowLog()){System.out.println(" ... done!");}
    }

    /**
     * Get useless sound files to delete.
     * @param sndfolder
     * @return List of sounds files to delete
     */
    public File[] getFileSnd2Del()
    {
        File listwavfiles[] = mc.getSndfolder().listFiles();
        al = new ArrayList();

        //System.out.println(set_texfilestokeep);
        for(int i=0;i<listwavfiles.length;i++)
        {
            //System.out.println(listwavfiles[i]);
            if(!set_texfilestokeep.contains(listwavfiles[i].getName().toLowerCase())){
                al.add(listwavfiles[i]);
            } else {
                //System.out.println(listwavfiles[i].getName());
            }
        }
        
        File listfiltodel[] = new File[al.size()];
        for (int i = 0; i < al.size(); i++) {
            File fil = (File) al.get(i);
            listfiltodel[i]=fil;
 
        }
        return listfiltodel;
    }

    /*
     * //\UT-Sounds\Wood.wstartc2.wav with Unreal Gold patch 227f!!
     * When extracting sound files from UT99 packages it doesn't keep group info in filename.
     * So have to remove this info so we can correctly detect which sound files are really used for import into UT3
     * or UT2004 editor.
     * "General.md4loop.wav"->"md4loop.wav"
     */

    public Set<String> getPackageNamesUsed()
    {
        return set_sndpacused;
    }

    /**
     * From UT sound package name, retrieve the corresponding file (.uax or .u)
     * @param conf
     * @param utgame
     * @return
     */
    public File[] getSoundPackageFilesToExport()
    {

        ArrayList<File> alsndpacfiles = new ArrayList<File>();
        File ftmp;

        int i=0;
        UTPackage utp;
        String pacname;
        Iterator<String> it = getPackageNamesUsed().iterator();
        
        while(it.hasNext()){
            pacname = it.next();
            ftmp = new File(Main.config.getUTxRootFolder(mc.getInput_utgame())+File.separator+"Sounds"+File.separator+pacname+".uax");
            if(ftmp.exists()){
                alsndpacfiles.add(ftmp);
            }
            ftmp = new File(Main.config.getUTxRootFolder(mc.getInput_utgame())+File.separator+"System"+File.separator+pacname+".u");
            if(ftmp.exists()){
                alsndpacfiles.add(ftmp);
            }
        }

        File[] ff = new File[alsndpacfiles.size()];
        for (int j = 0; j < ff.length; j++) {
            ff[j] = alsndpacfiles.get(j);

        }

        return ff;
    }

    private UTObject getSndPack(String line)
    {
        return new UTObject(line.split("\\'")[1]);
    }


}
