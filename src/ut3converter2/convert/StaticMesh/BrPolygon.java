/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.StaticMesh;

import java.util.ArrayList;

/**
 *
 * @author Hyperion
 */
public class BrPolygon {

    String texture="";
    double origin[];
    ArrayList alvertex;
    ArrayList altriangles;
    BrTexU brtu;
    BrTexV brtv;

    public BrPolygon() {
        alvertex = new ArrayList();
        altriangles = new ArrayList();
        origin = new double[3];
    }

    public double[] getOrigin() {
        return origin;
    }


    public void setOrigin(double origin[])
    {
        this.origin = origin;
    }

    public double getOriginX()
    {
        return origin[0];
    }

    public double getOriginY()
    {
        return origin[1];
    }

    public double getOriginZ()
    {
        return origin[2];
    }
    /**
     *   Vertex   +00096.000000,-00071.500000,+00012.000031 - 0
         Vertex   +00096.000000,+00075.500000,+00012.000031 - 1
         Vertex   +00072.000000,+00100.000000,+00012.000031 - 2
         Vertex   -00072.000000,+00100.000000,+00012.000031 - 3
         Vertex   -00096.000000,+00075.500000,+00012.000031 - 4
         Vertex   -00096.000000,-00071.500000,+00012.000031 - 5
         Vertex   -00072.000000,-00096.000000,+00012.000031 - 6
         Vertex   +00072.000000,-00096.000000,+00012.000031 - 7

        Begin Triangle
        Texture aaa.Red
        SmoothingMask 0
        Vertex 0 -72.000000 -96.000000 12.000031 50.875000 -11.500000 - 6
        Vertex 1 -96.000000 -71.500000 12.000031 50.500000 -11.117188 - 5
        Vertex 2 96.000000 -71.500000 12.000031 53.500000 -11.117188 - 0 <---always first
        End Triangle
     */
    public void convertToTriangles()
    {
        BrVertex bv0;
        BrVertex bv1;
        BrVertex bv2=(BrVertex) alvertex.get(0);

        for(int i=0;i<alvertex.size()-2;i++)
        {
            bv0 = (BrVertex) alvertex.get(i+1);
            bv1 = (BrVertex) alvertex.get(i+2);

            altriangles.add(new BrTriangle(bv0, bv1, bv2,this.texture));
        }


    }

    public ArrayList getAltriangles() {
        return altriangles;
    }
    
    public ArrayList getAlvertex() {
        return alvertex;
    }

    public void setAlvertex(ArrayList alvertex) {
        this.alvertex = alvertex;
    }

    public BrTexU getBrtu() {
        return brtu;
    }

    public void setBrtu(BrTexU brtu) {
        this.brtu = brtu;
    }

    public BrTexV getBrtv() {
        return brtv;
    }

    public void setBrtv(BrTexV brtv) {
        this.brtv = brtv;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public void addVertex(BrVertex brv)
    {
            alvertex.add(brv);  
    }

}
