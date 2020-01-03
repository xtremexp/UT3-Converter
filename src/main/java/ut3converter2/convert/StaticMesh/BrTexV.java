/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.StaticMesh;

/**
 *
 * @author Hyperion
 */
public class BrTexV {

    double texvx;
    double texvy;
    double texvz;

    public BrTexV(double texvx, double texvy, double texvz) {
        this.texvx = texvx;
        this.texvy = texvy;
        this.texvz = texvz;
    }

    public BrTexV(double d[]) {
        this.texvx = d[0];
        this.texvy = d[1];
        this.texvz = d[2];
    }

    public double getTexvx() {
        return texvx;
    }

    public void setTexvx(double texvx) {
        this.texvx = texvx;
    }

    public double getTexvy() {
        return texvy;
    }

    public void setTexvy(double texvy) {
        this.texvy = texvy;
    }

    public double getTexvz() {
        return texvz;
    }

    public void setTexvz(double texvz) {
        this.texvz = texvz;
    }


}
