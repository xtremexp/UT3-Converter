/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.ihm;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import org.jdesktop.swingx.JXHyperlink;
import ut3converter2.ihm.map.ShowImage;

/**
 *
 * @author Hyperion
 */
public class myJXHyperlink extends JXHyperlink
    {
        String name;
        String uri;
        final Color def_forgrnd_color=Color.red;

        public myJXHyperlink(String name, String uri) {
            this.name = name;
            this.uri = uri;
            this.setText(name);
            //this.setToolTipText(uri);
            this.setForeground(def_forgrnd_color);
            this.setToolTipText("Click to see sample picture "+uri);
            final String uri2 = this.uri;
            this.addMouseListener(new MouseAdapter() {
              public void mousePressed(MouseEvent e) {
                  //System.out.println("X="+e.getX()+",Y="+e.getY());
              ShowImage si = new ShowImage(null, true, uri2,e.getXOnScreen()-15,e.getYOnScreen()-15);
                            si.setVisible(true);
              }
            });
        /*
            this.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    MouseEvent me = (MouseEvent)e.getSource();
                    ShowImage si = new ShowImage(null, true, uri2,me.getX(),me.getY());
                    si.setVisible(true);
                }
            });*/
        }

        private void open(URI uri) {
        if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                try {
                        desktop.browse(uri);
                } catch (IOException e) {
                }
        } else {
        }
    }
}