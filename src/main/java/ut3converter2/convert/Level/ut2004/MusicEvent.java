/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut2004;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import ut3converter2.Installation;
import ut3converter2.Main;
import ut3converter2.tools.ActorLoader;

/**
 *
 * @author Hyperion
 */
public class MusicEvent {

    static String actorpath="/conf/ut2004/actors/MusicEvent.txt";
    static String musicfilespath="/conf/MusicEventFiles.txt";
    static String otherdata="";
    static String song="";
    static String section="";
    static String name="MusicEvent";

    public static void analyzeData(String line)
    {
        if(line.contains("Song="))
        {
            song = line.split("\\=")[1];
        }
        else if(line.contains("Section="))
        {
            section = line.split("\\=")[1];
        }
        else
        {
            otherdata += line+"\n";
        }
    }

    public static void setName(String name)
    {

    }
    private static String getUT2004SongFile(String song,String section) throws FileNotFoundException, IOException
    {
        File musiceventfiles = new File(Installation.getInstallDirectory(Main.class).getAbsolutePath()+musicfilespath);
        BufferedReader bfr = new BufferedReader(new FileReader(musiceventfiles));
        String line="";
        String t[];

        while((line=bfr.readLine())!=null)
        {
            t = line.split("\\;");
            if(t[0].equals(song))
            {
                if(t[1].equals(section))
                {
                    return t[2];
                }
            }
        }

        return "UnknownSong";
    }

    public static void reset()
    {
        otherdata="";
        song="";
        section="";
        name="MusicEvent";

    }

    public static String getActor()
    {
        String actor="";
        try {
             actor = ActorLoader.getActor(actorpath);
            actor = actor.replaceAll("$song", getUT2004SongFile(MusicEvent.song, MusicEvent.section));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MusicEvent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MusicEvent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return actor;
    }

    

}
