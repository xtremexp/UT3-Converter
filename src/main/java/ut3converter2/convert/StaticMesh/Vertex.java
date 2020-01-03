/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.StaticMesh;

/**
 * Vertex: 3 points + textures coordinates
 * @author Hyperion
 */
public class Vertex {
    
    float mvert1 = 0F;
    float mvert2 = 0F;
    float mvert3 = 0F;
    float tvert1 = 0F;
    float tvert2 = 0F;

    public Vertex()
    {

    }
    public Vertex(float mvert1,float mvert2,float mvert3,float tvert1,float tvert2) {
        this.mvert1 = mvert1;
        this.mvert2 = mvert2;
        this.mvert3 = mvert3;
        this.tvert1 = tvert1;
        this.tvert2 = tvert2;
    }

    public void setMvert1(float mvert1)
    {
        this.mvert1 = mvert1;
    }

    public void setMvert2(float mvert2)
    {
        this.mvert2 = mvert2;
    }

    public void setMvert3(float mvert3)
    {
        this.mvert3 = mvert3;
    }

    public void setTvert1(float tvert1)
    {
        this.tvert1 = tvert1;
    }

    public void setTvert2(float tvert2)
    {
        this.tvert2 = tvert2;
    }
    /** X */
    public float getMvert1() {
        return mvert1;
    }

    /** Y */
    public float getMvert2() {
        return mvert2;
    }

    /** Z */
    public float getMvert3() {
        return mvert3;
    }

    /** Texture coordinate X*/
    public float getTvert1() {
        return tvert1;
    }

    /** Texture coordinate Y*/
    public float getTvert2() {
        return tvert2;
    }

    

}
