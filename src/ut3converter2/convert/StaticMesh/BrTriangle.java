/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.StaticMesh;

/**
 *
 * @author Hyperion
 */
public class BrTriangle {

    BrVertex bv1;
    BrVertex bv2;
    BrVertex bv3;
    String texture="";

    public BrTriangle(BrVertex bv1, BrVertex bv2, BrVertex bv3) {
        this.bv1 = bv1;
        this.bv2 = bv2;
        this.bv3 = bv3;
    }

    public BrTriangle(BrVertex bv1, BrVertex bv2, BrVertex bv3,String texture) {
        this.bv1 = bv1;
        this.bv2 = bv2;
        this.bv3 = bv3;
        this.texture = texture;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public BrVertex getBv1() {
        return bv1;
    }

    public void setBv1(BrVertex bv1) {
        this.bv1 = bv1;
    }

    public BrVertex getBv2() {
        return bv2;
    }

    public void setBv2(BrVertex bv2) {
        this.bv2 = bv2;
    }

    public BrVertex getBv3() {
        return bv3;
    }

    public void setBv3(BrVertex bv3) {
        this.bv3 = bv3;
    }

    
    
    

    
}
