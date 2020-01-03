/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.StaticMesh;

/**
 * A Triangle in T3D format composed for 3 vertices and textures.
 * @author Hyperion
 */
public class T3DTriangle {

    String texture = "";
    int nummtlid = 0;
    Vertex v1;
    Vertex v2;
    Vertex v3;

    public T3DTriangle() {

    }
    
    public T3DTriangle(Vertex v1,Vertex v2,Vertex v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public void setV1(Vertex v1) {
        this.v1 = v1;
    }

    public void setV2(Vertex v2) {
        this.v2 = v2;
    }

    public void setV3(Vertex v3) {
        this.v3 = v3;
    }

    public Vertex getV1() {
        return v1;
    }

    public Vertex getV2() {
        return v2;
    }

    public Vertex getV3() {
        return v3;
    }

    public int getNummtlid() {
        return nummtlid;
    }

    public void setNummtlid(int nummtlid) {
        this.nummtlid = nummtlid;
    }

    
    



    

}
