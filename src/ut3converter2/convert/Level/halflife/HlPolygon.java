/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.halflife;

import java.util.ArrayList;
import javax.vecmath.Vector3d;

/**
 *
 * @author Hyperion
 */
public class HlPolygon {

    String texture;
    Vector3d origin;
    Vector3d normal;
    ArrayList alvertex;

    public HlPolygon() {
        alvertex = new ArrayList();
    }

    public void addVertex(Vector3d vertex)
    {
        alvertex.add(vertex);
    }

    

}
