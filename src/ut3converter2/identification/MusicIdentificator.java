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
import ut3converter2.Main;
import ut3converter2.convert.MapConverter;
import ut3converter2.UTGames;
import ut3converter2.convert.Sound.SoundConverter;
import ut3converter2.tools.FileDecoder;

/**
 *
 * @author Hyperion
 */
public class MusicIdentificator {

    boolean bAutoConvertMusic = true;
    MapConverter mc;
    File utmusicfolder;
    File outputfolder;
    File inputmusicfile;
    File utrootfolder;
    File t3dlevelfile;
    int inputgame;

    public MusicIdentificator(MapConverter mc){
        this.t3dlevelfile = mc.getT3dlvlfile();
        this.utmusicfolder = Main.config.getUTxRootFolder(mc.getInput_utgame());
        this.inputgame = mc.getInput_utgame();
        this.outputfolder = mc.getMusicfolder();
    }

    public MusicIdentificator(File t3dlevelfile, File utrootfolder, File outputfolder, int inputgame) {
        this.t3dlevelfile = t3dlevelfile;
        this.utmusicfolder = utrootfolder;
        this.inputgame = inputgame;
        this.outputfolder = outputfolder;
    }

    public void setBAutoConvertMusic(boolean bAutoConvertMusic) {
        this.bAutoConvertMusic = bAutoConvertMusic;
    }

    public boolean bHasMusic() {
        if (this.inputmusicfile != null) {
            if (this.inputmusicfile.exists()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void convert() {
        try {
            this.inputmusicfile = getOriginalMusicFile();
            if (this.inputmusicfile.exists()) {
                SoundConverter.convertMusicFile(inputmusicfile, outputfolder);
            }
        } catch (Exception e) {
        }
    }

    private File getOriginalMusicFile() throws FileNotFoundException, IOException {

        //BufferedReader bfr = FileDecoder.getBufferedReader(t3dlevelfile, "Begin");
        BufferedReader bfr = new BufferedReader(new FileReader(t3dlevelfile));

        String line = "";
        boolean bmusicfound = false;
        //Song="KR-Convoy"
        String songvalue = "";

        while (((line = bfr.readLine()) != null) && (!bmusicfound)) {
            if (line.contains(" Song=")) {
                if (line.split("Song=").length > 0) {
                    songvalue = line.split("Song=")[1];
                    songvalue = songvalue.replaceAll("\\\"", "");
                    if (inputgame == UTGames.UT2004 || inputgame == UTGames.U2) {
                        return new File(utmusicfolder.getAbsolutePath() + "\\Music\\" + songvalue + ".ogg");
                    }
                    bmusicfound = true;
                }

            }
        }
        return null;
    }
}
