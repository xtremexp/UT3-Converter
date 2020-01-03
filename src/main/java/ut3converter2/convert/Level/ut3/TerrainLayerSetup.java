/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut3;

/**
 *
 * @author Hyperion
 */
public class TerrainLayerSetup {

    TerrainMaterial material;
    String tlsname;
    String tlstexname;

    public TerrainLayerSetup() {
    }

    public TerrainLayerSetup(String tlsname) {
        this.tlsname = tlsname;
    }

    
    
    public TerrainLayerSetup(TerrainMaterial material) {
        this.material = material;
    }

    public TerrainMaterial getMaterial() {
        return material;
    }

    public void setMaterial(TerrainMaterial material) {
        this.material = material;
    }

    public String getTlsname() {
        return tlsname;
    }

    public void setTlsname(String tlsname) {
        this.tlsname = tlsname;
    }

    public String getTlstexname() {
        return tlstexname;
    }

    public void setTlstexname(String tlstexname) {
        this.tlstexname = tlstexname;
    }

    
    



}
