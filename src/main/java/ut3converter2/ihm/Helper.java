/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.ihm;

import javax.swing.JDialog;
import javax.swing.JLabel;

import static ut3converter2.convert.MapConverter.IMAGE_FOLDER;

/**
 *
 * @author Hyperion
 */
public class Helper extends JDialog
{

    public Helper(String image) {
        this.setLayout(new java.awt.BorderLayout());
        JLabel jlb = new JLabel();
        jlb.setIcon(new javax.swing.ImageIcon(getClass().getResource(IMAGE_FOLDER + "/"+image)));
        this.add("Center",jlb);
    }



}
