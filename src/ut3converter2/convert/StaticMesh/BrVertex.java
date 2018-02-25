/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.StaticMesh;

/**
 *
 * @author Hyperion
 */
public class BrVertex {

    double vx;
    double vy;
    double vz;

    public BrVertex(double vx, double vy, double vz, double[] polorigin) {
        this.vx = vx-polorigin[0];
        this.vy = vy-polorigin[1];
        this.vz = vz-polorigin[2];
    }

    public BrVertex(double d[], double[] polorigin)
    {
        this.vx = d[0]+polorigin[0];
        this.vy = d[1]+polorigin[1];
        this.vz = d[2]+polorigin[2];
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getVz() {
        return vz;
    }

    public void setVz(double vz) {
        this.vz = vz;
    }

    

}
