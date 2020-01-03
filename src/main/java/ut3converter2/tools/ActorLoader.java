/*
 * Reads .txt and saves as String.
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ut3converter2.Installation;
import ut3converter2.Main;

/**
 *
 * @author Hyperion
 */
public class ActorLoader {

    public static String getActor(String path)
    {
        File actorfile = new File(path);

        String tmp="";
        String line="";
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(actorfile));

            while((line=bfr.readLine())!=null)
            {
                tmp += line+"\n";
            }
            bfr.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ActorLoader.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex) {
            Logger.getLogger(ActorLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tmp;
    }

}
