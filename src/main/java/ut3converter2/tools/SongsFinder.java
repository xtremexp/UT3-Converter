/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Retrieve MusicFiles used by T3D Maps
 * @author Hyperion
 */
public class SongsFinder {

    File f[];
    ArrayList alsongs;

    public SongsFinder(File[] f) {
        this.f = f;
    }

    public void getSongUsed() throws FileNotFoundException, IOException
    {
        File t3dmapfile;
        BufferedReader bfr ;
        String line="";
        alsongs = new ArrayList();
        String song;

        for(int i=0;i<f.length;i++)
        {
            t3dmapfile = f[i];
            bfr = new BufferedReader(new FileReader(t3dmapfile));

            while((line=bfr.readLine())!=null)
            {
                if(line.contains("Song="))
                {
                    song = getSong(line);

                    if(!alsongs.contains(song))
                    {
                        alsongs.add(song);
                    }
                }
            }
        }
        displaySongList();
    }

    private void displaySongList()
    {
        for(int i=0;i<alsongs.size();i++)
        {
            System.out.println(alsongs.get(i).toString());
        }
    }
    /**
     *     Song="UB-Kran32"
     * @param line
     * @return
     */
    private String getSong(String line)
    {
        //Song=Music'Fifth.Fifth'
        String tmp[] = line.split("\\\'");
        if(tmp.length>1)
        {
            line = tmp[1];
            line = line.split("\\\'")[0];
        }
        
        return line;
    }

}
