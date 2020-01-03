/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ut3converter2.convert.Level.ut3;

import ut3converter2.convert.Level.UTActor;

/**
 *
 * @author Hyperion
 */
public class Note extends UTActor{

    String text;
    
    public Note(String utclass) {
        text = "Unconverted object "+utclass;
    }
    
    @Override
    public String toString() {
        String tmp = "";
        tmp += "      Begin Actor Class=Note Name=Note_0 Archetype=Note\'Engine.Default__Note\'\n";
        tmp += "         Text=\"" + text + "\"\n";
        tmp += getOtherdata();
        tmp += "      End Actor\n";
        
        return tmp;
    }
    
}
