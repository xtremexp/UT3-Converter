/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.ihm;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import org.jdesktop.swingx.JXHyperlink;

/**
 *
 * @author Hyperion
 */
public class Credits {


    ArrayList <credit>alcredits;
    ArrayList <thanks>althanks;

    class thanks
    {
        Component name;
        String description;

        public thanks(Component name,String description) {
            this.name = name;
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Component getName() {
            return name;
        }

        public void setName(Component name) {
            this.name = name;
        }

        

    }
    class credit
    {
        Component name;
        Component product;
        String version;
        URI productlink;

        public credit(Component name, Component product) {
            this.name = name;
            this.product = product;
        }



        
        public Component getName() {
            return name;
        }

        public void setName(Component name) {
            this.name = name;
        }

        public Component getProduct() {
            return product;
        }

        public void setProduct(Component product) {
            this.product = product;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        
        public URI getProductlink() {
            return productlink;
        }

        public void setProductlink(URI productlink) {
            this.productlink = productlink;
        }


        
    }

    class myJXHyperlink extends JXHyperlink
    {
        String name;
        String uri;

        public myJXHyperlink(String name, String uri) {
            this.name = name;
            this.uri = uri;
            this.setText(name);
            this.setToolTipText(uri);
            this.setForeground(Color.red);
            final String uri2 = this.uri;
            this.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        open(new URI(uri2));
                    } catch (URISyntaxException ex) {
                        Logger.getLogger(Credits.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
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
    
    public Credits() throws URISyntaxException {

        alcredits = new ArrayList<Credits.credit>();
        alcredits.add(new credit(new JLabel("Pierre-E Gougelet"), new myJXHyperlink("Nconvert v5.06", "http://www.xnview.com/")));
        alcredits.add(new credit(new JLabel("Alex Stewart"),new myJXHyperlink("UED Texture Toolkit v1.0", "http://www.foogod.com/UEdTexKit/")));
        alcredits.add(new credit(new JLabel("-"), new myJXHyperlink("Java Advanced Imaging", "https://jai.dev.java.net")));
        alcredits.add(new credit(new JLabel("Chris Bagwell"),new myJXHyperlink("Sox", "http://sox.sourceforge.net")));
        alcredits.add(new credit(new JLabel("Antonio Cordero Balcázar"), new myJXHyperlink("UTPackages 2.3", "http://www.acordero.org")));

        althanks = new ArrayList<thanks>();
        althanks.add(new thanks(new JLabel(".:..:"), "For teaching me basics of U1 to UT2004 conversion"));
        althanks.add(new thanks(new myJXHyperlink("Unreal Wiki", "http://wiki.beyondunreal.com/"), "The best source for digging into Unreal Technology"));
    }



     public URI getURI(Object obj)
    {
        return ((credit)obj).getProductlink();
    }

}
