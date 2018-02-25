/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ut3converter2.ihm.t3d;

/**
 * Will be used to copy/paste from UT/UT2004 Editor to UT3 editor (only brushes and some actors (no SM))
 * @author Hyperion
 */
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Toolkit;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import ut3converter2.convert.Level.ut3.PointLight;

public final class T3DCopyPasteConvertor implements ClipboardOwner {

    int input_utgame;
    int output_utgame;
    
    public T3DCopyPasteConvertor() {
    }

    /**
     * Empty implementation of the ClipboardOwner interface.
     */
    public void lostOwnership(Clipboard aClipboard, Transferable aContents) {
        //do nothing
    }

    public boolean isT3DLevelData(String clipboard){
        String t[]=clipboard.split("\n");
        if(t[0].contains("Begin Map")){
            return true;
        } else {
            return false;
        }
    }
    /**
     * Place a String on the clipboard, and make this class the
     * owner of the Clipboard's contents.
     */
    public void setClipboardContents(String aString) {
        StringSelection stringSelection = new StringSelection(aString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, this);
    }

    /**
     * Get the String residing on the clipboard.
     *
     * @return any text found on the Clipboard; if none found, return an
     * empty String.
     */
    public String getClipboardContents() {
        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        //odd: the Object param of getContents is not currently used
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText =
                (contents != null)
                && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTransferableText) {
            try {
                result = (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException ex) {
                //highly unlikely since we are using a standard DataFlavor
                System.out.println(ex);
                ex.printStackTrace();
            } catch (IOException ex) {
                System.out.println(ex);
                ex.printStackTrace();
            }
        }
        return result;
    }

    PointLight pl=new PointLight(null);
    
    public String convertClipBoardToUT3(String inputstring){
        BufferedReader bfr = new BufferedReader(new StringReader(inputstring));
        String outputstring ="";
        boolean bcopyactordata=false;
        String curclass="";
        outputstring +="Begin Map\n";
        outputstring +="Begin Level NAME=PersistentLevel\n";
        
        String line = "";
        try {
            while ((line = bfr.readLine()) != null) {
                if(line.contains("Begin Actor")){
                    curclass = getActorClass(line);
                    if(curclass.equals("PathNode")){
                        bcopyactordata = true;
                        outputstring += line+"\n";
                    }
                } else if (line.contains("End Actor")){
                    if(curclass.equals("Light")){
                        outputstring += pl.toString(null);
                    }
                    if(bcopyactordata){
                        outputstring += line+"\n";
                        bcopyactordata = false;
                    }
                } else {
                    if(curclass.equals("Light")){
                        pl.AnalyseT3DData(line);
                    } else if(bcopyactordata){
                        outputstring += line+"\n";
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(T3DCopyPasteConvertor.class.getName()).log(Level.SEVERE, null, ex);
        }

        outputstring += "   End Level\n";
        outputstring += "Begin Surface\n";
        outputstring += "End Surface\n";
        outputstring += "End Map\n";
        return outputstring;
    }

    private String getActorClass(String line)
    {
        return (line.split("=")[1]).split(" ")[0];
    }
}
