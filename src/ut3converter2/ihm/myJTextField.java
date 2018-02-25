/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.ihm;

import java.awt.Color;
import javax.swing.JTextField;

/**
 *
 * @author Hyperion
 */
public class myJTextField extends JTextField{

    final Color defcolor=Color.GREEN;
    public myJTextField(String text,boolean benabled,boolean beditable) {
        super(text);
        super.setEnabled(benabled);
        super.setEditable(beditable);
    }

    public myJTextField(String text) {
        super(text);
        super.setEditable(false);
        super.setForeground(defcolor);
    }



}
