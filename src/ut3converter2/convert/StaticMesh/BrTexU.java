/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.StaticMesh;

/**
 *
 * @author Hyperion
 */
public class BrTexU {

    double texux;
    double texuy;
    double texuz;

    public BrTexU(double texux, double texuy, double texuz) {
        this.texux = texux;
        this.texuy = texuy;
        this.texuz = texuz;
    }

    public BrTexU(double d[]) {
        this.texux = d[0];
        this.texuy = d[1];
        this.texuz = d[2];
    }

    public double getTexux() {
        return texux;
    }

    public void setTexux(double texux) {
        this.texux = texux;
    }

    public double getTexuy() {
        return texuy;
    }

    public void setTexuy(double texuy) {
        this.texuy = texuy;
    }

    public double getTexuz() {
        return texuz;
    }

    public void setTexuz(double texuz) {
        this.texuz = texuz;
    }

    
}
