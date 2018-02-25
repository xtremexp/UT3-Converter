/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert;

import java.beans.PropertyVetoException;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import ut3converter2.ihm.Display2;
import ut3converter2.ihm.map.GlobalInstructions;
import ut3converter2.ihm.map.ShowResult;

/**
 *
 * @author Hyperion
 */
public class GlobalConverter {

    boolean bShowLog=false;
    boolean bDispResult=false;
    String taskname="Task";
    File input_mapfile=null;
    File outputfolder=null;

    public GlobalConverter(File inputfile,File outputfolder) {
        this.input_mapfile = inputfile;
        this.outputfolder = outputfolder;
    }

    public GlobalConverter() {
    }

    

    public File getInputfile() {
        return input_mapfile;
    }

    public void setInputfile(File inputfile) {
        this.input_mapfile = inputfile;
    }

    public File getOutputfolder() {
        return outputfolder;
    }

    public void setOutputfolder(File outputfolder) {
        this.outputfolder = outputfolder;
    }

    public boolean isBDispResult() {
        return bDispResult;
    }

    public void setBDispResult(boolean bDispResult) {
        this.bDispResult = bDispResult;
    }

    public boolean isBShowLog() {
        return bShowLog;
    }

    public void setBShowLog(boolean bShowLog) {
        this.bShowLog = bShowLog;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public void ShowResults(GlobalInstructions gi)
    {
        ShowResult sr = new ShowResult(null, true, gi);
        sr.setSize(400, 500);
        sr.setLocation(400, 0);
        sr.setVisible(true);
        sr.setResizable(true);
        try {
            sr.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(GlobalConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        //this.setClosable(true);
        sr.setMaximizable(true);
        sr.setIconifiable(true);
        Display2.jDP.add(sr);
        sr.pack();
        sr.repaint();
    }


}
