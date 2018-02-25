/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.ihm.map;

import javax.swing.JComponent;

/**
 *
 * @author Hyperion
 */
public class Instruction {

    String message;
    /**
     * jXLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ut3converter2/ihm/images/importtexUT.png")));
     * /ut3converter2/ihm/images/importtexUT.png
     */
    String image;
    JComponent instru_data;

    public Instruction(JComponent instru_data) {
        this.instru_data = instru_data;
    }

    public Instruction(String message) {
        this.message = message;
    }

    public Instruction(String message, String image) {
        this.message = message;
        this.image = image;
    }

    public JComponent getInstru_data() {
        return instru_data;
    }

    public boolean hasImage()
    {
        if(image==null){return false;}else{return true;}
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    
    
}
