/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.ihm.map;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import ut3converter2.ihm.myJTextField;
import ut3converter2.ihm.myJXHyperlink;

/**
 *
 * @author Hyperion
 */
public class GlobalInstructions {

    String title;
    ArrayList <Instructions>alglinst;

    public GlobalInstructions(ArrayList<Instructions> alglinst) {
        this.alglinst = alglinst;
    }

    public GlobalInstructions() {
        alglinst = new ArrayList<Instructions>();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Instructions> getAlglinst() {
        return alglinst;
    }


    public boolean hasTitle()
    {
        if(title==null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public int getSize()
    {
        return alglinst.size();
    }

    public void addInstructions(Instructions insts)
    {
        this.alglinst.add(insts);
    }

    public void writeToFile(File f){
        BufferedWriter bwr = null;
        FileWriter fw = null;

        try {
            fw = new FileWriter(f);
            bwr = new BufferedWriter(fw);

            Instructions insts;
            Instruction inst;

            for(int i=0;i<this.alglinst.size();i++){

                insts = alglinst.get(i);
                bwr.write("\n"+insts.title+"\n");

                for(int j=0;j<insts.getAlinst().size();j++){
                    inst = insts.getAlinst().get(j);

                    if(inst.getInstru_data() instanceof JLabel){
                        bwr.write(((JLabel)inst.getInstru_data()).getText()+"\n");
                    } else if(inst.getInstru_data() instanceof myJXHyperlink){
                        bwr.write(((myJXHyperlink)inst.getInstru_data()).getText()+"\n");
                    } else if(inst.getInstru_data() instanceof myJTextField){
                        bwr.write(((myJTextField)inst.getInstru_data()).getText()+"\n");
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(GlobalInstructions.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bwr.close();
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(GlobalInstructions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
