/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UnsupportedLookAndFeelException;





/**
 * http://tanksoftware.com/juk/developer/src/com/
 *     tanksoftware/util/RedirectedFrame.java
 * A Java Swing class that captures output to the command line
 ** (eg, System.out.println)
 * RedirectedFrame
 * <p>
 * This class was downloaded from:
 * Java CodeGuru (http://codeguru.earthweb.com/java/articles/382.shtml) <br>
 * The origional author was Real Gagnon (real.gagnon@tactika.com);
 * William Denniss has edited the code, improving its customizability
 *
 * In breif, this class captures all output to the system and prints it in
 * a frame. You can choose weither or not you want to catch errors, log
 * them to a file and more.
 * For more details, read the constructor method description
 */


public class RedirectFrame extends JInternalFrame {

    // Class information
    public static final String PROGRAM_NAME = "Redirect Frame";
    public static final String VERSION_NUMBER = "1.1";
    public static final String DATE_UPDATED = "13 April 2001";
    public static final String AUTHOR =
       "Real Gagnon - edited by William Denniss";


    private boolean catchErrors;
    private boolean logFile;
    private String fileName;
    private int width;
    private int height;
    private int closeOperation;


    TextArea aTextArea = new TextArea();
    PrintStream aPrintStream  =
       new PrintStream(
         new FilteredStream(
           new ByteArrayOutputStream()));

    /** Creates a new RedirectFrame.
     *  From the moment it is created,
     *  all System.out messages and error messages (if requested)
     *  are diverted to this frame and appended to the log file
     *  (if requested)
     *
     * for example:
     *  RedirectedFrame outputFrame =
     *       new RedirectedFrame
                (false, false, null, 700, 600, JFrame.DO_NOTHING_ON_CLOSE);
     * this will create a new RedirectedFrame that doesn't catch errors,
     * nor logs to the file, with the dimentions 700x600 and it doesn't
     * close this frame can be toggled to visible, hidden by a controlling
     * class by(using the example) outputFrame.setVisible(true|false)
     *  @param catchErrors set this to true if you want the errors to
     *         also be caught
     *  @param logFile set this to true if you want the output logged
     *  @param fileName the name of the file it is to be logged to
     *  @param width the width of the frame
     *  @param height the height of the frame
     *  @param closeOperation the default close operation
     *        (this must be one of the WindowConstants)
     */
    public RedirectFrame
       (boolean catchErrors, boolean logFile, String fileName, int width,
         int height, int closeOperation) throws UnsupportedLookAndFeelException {

            this.catchErrors = catchErrors;
            this.logFile = logFile;
            this.fileName = fileName;
            this.width = width;
            this.height = height;
            this.setResizable(true);
            //this.setClosable(true);
            this.setMaximizable(true);
            this.setIconifiable(true);

           // this.setAlwaysOnTop(true);
            this.closeOperation = closeOperation;
            aTextArea.setForeground(Color.black);
            aTextArea.setBackground(Color.orange);
            Container c = getContentPane();
            setTitle("Output Log:");
            setSize(width, height);
            c.setLayout(new BorderLayout());
            c.add("Center", aTextArea);
            JPanel jp1 = new JPanel();
            jp1.setLayout(new FlowLayout());
            JButton jbtclear = new JButton("Clear log");
            jbtclear.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                aTextArea.setText("");
            }
        });
            JButton jbt = new JButton("Save log ...");
            //final String towrite=aTextArea.getText();


            jbt.addActionListener(new ActionListener() {

            

            @Override
            public void actionPerformed(ActionEvent e) {
                File f = FileChooser2.getFolder("Select the folder to save log file");
                if(f!=null)
                {
                    File outputfile = new File(f.getAbsolutePath()+"//ut3converterlog.txt");
                BufferedWriter bwr = null;
                try {
                    bwr = new BufferedWriter(new FileWriter(outputfile));
                } catch (IOException ex) {
                    Logger.getLogger(RedirectFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    bwr.write(aTextArea.getText());
                } catch (IOException ex) {
                    Logger.getLogger(RedirectFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    bwr.close();
                } catch (IOException ex) {
                    Logger.getLogger(RedirectFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                JOptionPane.showMessageDialog(null, "<html>Log saved to:<br>"+outputfile.getAbsolutePath());
                }
                
            }
        });
        jp1.add(jbt);
        jp1.add(jbtclear);
        c.add("South",jp1);
            displayLog();
            this.logFile = logFile;
            System.setOut(aPrintStream); // catches System.out messages
            if (catchErrors) {
                System.setErr(aPrintStream); // catches error messages
            }
            // set the default closing operation to the one given
            setDefaultCloseOperation(closeOperation);
            Toolkit tk = Toolkit.getDefaultToolkit();
            Image im = tk.getImage("myicon.gif");
            //setIconImage(im);
    }



    class FilteredStream extends FilterOutputStream {
        public FilteredStream(OutputStream aStream) {
            super(aStream);
          }

        public void write(byte b[]) throws IOException {
            String aString = new String(b);
            aTextArea.append(aString);
        }

        public void write(byte b[], int off, int len) throws IOException {
            String aString = new String(b , off , len);
            aTextArea.append(aString);
            if (logFile) {
                FileWriter aWriter = new FileWriter(fileName, true);
                aWriter.write(aString);
                aWriter.close();
            }
        }
    }

    private void displayLog() {
        Dimension dim = getToolkit().getScreenSize();
        Rectangle abounds = getBounds();
        Dimension dd = getSize();
        setLocation((dim.width - abounds.width) / 2,
                    (dim.height - abounds.height) / 2);
        setVisible(true);
        requestFocus();
    }

}

