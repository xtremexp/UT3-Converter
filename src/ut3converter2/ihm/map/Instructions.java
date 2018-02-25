/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.ihm.map;

import java.util.ArrayList;

/**
 *
 * @author Hyperion
 */
public class Instructions {

    String title;
    ArrayList <Instruction>alinst;

    public Instructions(ArrayList<Instruction> alinst) {
        this.alinst = alinst;
    }

    public Instructions(String title, ArrayList<Instruction> alinst) {
        this.title = title;
        this.alinst = alinst;
    }

    public Instructions(Instruction inst) {
        alinst = new ArrayList();
        alinst.add(inst);
    }

    public Instructions(String title) {
        this.title = title;
        alinst = new ArrayList();
    }

    
    public Instructions() {
        alinst = new ArrayList();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Instruction> getAlinst() {
        return alinst;
    }

    public boolean hasTitle()
    {
        if(title==null){return false;}else{return true;}
    }

    public void addInstruction(Instruction inst)
    {
        this.alinst.add(inst);
    }
    

}
