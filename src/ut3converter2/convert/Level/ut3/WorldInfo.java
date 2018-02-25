/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ut3converter2.convert.Level.ut3;

import java.util.ArrayList;
import ut3converter2.GlobalGametypes;
import ut3converter2.UTGames;
import ut3converter2.convert.Level.UTActor;
import ut3converter2.convert.MapConverter;

/**
 *
 * @author Hyperion
 */
public class WorldInfo extends UTActor {

    String author = "Unknown";
    String maptitle = "";
    String description;
    ArrayList gametypes;
    String recplmin = "6";
    String recplmax = "12";
    float stallz = 1000000.000000F;
    MapConverter mc;

    public WorldInfo(ArrayList gametypes,MapConverter mc) {
        this.gametypes = gametypes;
        this.mc = mc;
    }

    public WorldInfo(MapConverter mc) {
        gametypes = new ArrayList();
        gametypes.add(GlobalGametypes.dm);
        this.mc = mc;
    }

    public void AnalyseT3DData(String line) {
        String t[];
        if (line.contains("Author=")) {
            this.author = line.split("\"")[1];
        } //Unreal2:"	Title="" "
        else if (line.contains("Title=")) {
            t = line.split("\"");
            if (t.length == 2) {
                this.maptitle = t[1];
            }
        } //Unreal2: 	Description=LevelDesc'M03B5.Description'
        else if (line.contains("Description=")) {
            if (line.contains("\"")) {
                this.description = line.split("\"")[1];
            }
        } else if (line.contains("IdealPlayerCountMin=")) {
            this.recplmin = line.split("IdealPlayerCountMin=")[1];
        } else if (line.contains("IdealPlayerCountMax=")) {
            this.recplmax = line.split("IdealPlayerCountMax=")[1];
        }
    }

    public String toString() {
        String tmp = "";
        tmp += "      Begin Actor Class=Note Name=Note_0 Archetype=Note\'Engine.Default__Note\'\n";
        tmp += "         Text=\"" + getText() + "\"\n";
        tmp += "         Tag=\"Note\"\n";
        tmp += "         Location=(X=0.000000,Y=0.000000,Z=4000.000000)\n";
        tmp += "         DrawScale=4.000000\n";
        tmp += "      End Actor\n";

        tmp += "      Begin Actor Class=Note Name=Note_0 Archetype=Note\'Engine.Default__Note\'\n";
        tmp += "         Text=\"RecommendedMinPlayers:" + recplmin + " RecommendedMaxPlayers:" + recplmax + "\"\n";
        tmp += "         Tag=\"Note\"\n";
        tmp += "         Location=(X=0.000000,Y=0.000000,Z=3000.000000)\n";
        tmp += "         DrawScale=4.000000\n";
        tmp += "      End Actor\n";

        if (description != null) {
            tmp += "      Begin Actor Class=Note Name=Note_1 Archetype=Note\'Engine.Default__Note\'\n";
            tmp += "         Text=\"Description:" + description + "\"\n";
            tmp += "         Tag=\"Note\"\n";
            tmp += "         Location=(X=0.000000,Y=0.000000,Z=2000.000000)\n";
            tmp += "         DrawScale=4.000000\n";
            tmp += "      End Actor\n";
        }
        return tmp;
    }

    private String getText() {
        return UTGames.getUTGame(mc.getInput_utgame())+" to "+UTGames.getUTGame(mc.getOutput_utgame())+" Converted Map  - Original Title: " + maptitle + " Original Author: " + author;
    }

    private String getGameTypes(ArrayList gametypes) {
        String tmp = "";
        String gametypestr = "";
        for (int i = 0; i < gametypes.size(); i++) {
            gametypestr = gametypes.get(i).toString();
            if (gametypestr.equals(GlobalGametypes.dm)) {
                tmp += "    GameTypesSupportedOnThisMap(" + i + ")=Class'" + GameTypes.ut3dm_gm_class + "'\n";
            } else if (gametypestr.equals(GlobalGametypes.ctf)) {
                tmp += "    GameTypesSupportedOnThisMap(" + i + ")=Class'" + GameTypes.ut3ctf_gm_class + "'\n";
            } else if (gametypestr.equals(GlobalGametypes.vctf)) {
                tmp += "    GameTypesSupportedOnThisMap(" + i + ")=Class'" + GameTypes.ut3vctf_gm_class + "'\n";
            } else if (gametypestr.equals(GlobalGametypes.duel)) {
                tmp += "    GameTypesSupportedOnThisMap(" + i + ")=Class'" + GameTypes.ut3duel_gm_class + "'\n";
            } else if (gametypestr.equals(GlobalGametypes.ons)) {
                tmp += "    GameTypesSupportedOnThisMap(" + i + ")=Class'" + GameTypes.ut3war_gm_class + "'\n";
            }
        }
        return tmp;
    }
}
