/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.halflife;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.vecmath.Vector3d;

/**
 *
 * @author Hyperion
 */
public class HlBrush {

    ArrayList al_polygon;
    Vector3d location;

    public HlBrush() {
        al_polygon = new ArrayList();
    }

    public void writeBrush(BufferedWriter bwr) throws IOException
    {
        HlPolygon hlp;
        this.location = getLocation();
        bwr.write(getHeaderBrush());
        for(int i=0;i<al_polygon.size();i++)
        {
            hlp = (HlPolygon) al_polygon.get(i);
            writePolygon(bwr, hlp);
        }

        bwr.write("      End PolyList\n");
        bwr.write("   End Brush\n");
        bwr.write("   Brush=Model'MyLevel.Model0'\n");
        bwr.write("End Actor\n");
    }

    /**
     * Begin Polygon Texture=wall03 Flags=524288
             Origin   -00192.000000,-00064.000000,-00128.000000
             Normal   -00001.000000,+00000.000000,+00000.000000
             TextureU +00000.000000,+00001.000000,+00000.000000
             TextureV +00000.000000,+00000.000000,-00001.000000
             Vertex   -00192.000000,-00064.000000,-00128.000000
             Vertex   -00192.000000,-00064.000000,+00128.000000
             Vertex   -00192.000000,+00064.000000,+00128.000000
             Vertex   -00192.000000,+00064.000000,-00128.000000
          End Polygon
     * @param bwr
     */
    private void writePolygon(BufferedWriter bwr,HlPolygon hlp) throws IOException
    {
        Vector3d vx;
        bwr.write("          Begin Polygon\n");
        bwr.write("             Origin   -00192.000000,-00064.000000,-00128.000000\n");
        bwr.write("             Normal   -00001.000000,+00000.000000,+00000.000000\n");
        bwr.write("             TextureU +00000.000000,+00001.000000,+00000.00000\n");
        bwr.write("             TextureV +00000.000000,+00000.000000,-00001.0000000\n");

        
        for(int i=0;i<hlp.alvertex.size();i++)
        {
            vx = (Vector3d) hlp.alvertex.get(i);
            bwr.write("             Vertex   "+(vx.getX()-location.x)+","+(vx.getY()-location.y)+","+(vx.getZ()-location.z)+"\n");
        }

        bwr.write("          End Polygon\n");
    }

    private Vector3d getLocation()
    {
        location = new Vector3d(0D, 0D, 0D);
        HlPolygon hlp;
        Vector3d vertex;
        double num_vert=this.al_polygon.size()*3D;
        /*
         * for (i=0; i<b->num_verts; i++)
      b->center.x += b->verts[i].x;
      b->center.y += b->verts[i].y;
      b->center.z += b->verts[i].z;
      */
        for(int i=0;i<this.al_polygon.size();i++)
        {
            hlp = (HlPolygon) al_polygon.get(i);
            for(int j=0;j<3;j++)
            {
                vertex = (Vector3d) hlp.alvertex.get(j);
                location.setX(location.getX()+vertex.getX());
                location.setY(location.getY()+vertex.getY());
                location.setZ(location.getZ()+vertex.getZ());
            }
        }

        location.setX(location.getX()/num_vert);
        location.setY(location.getY()/num_vert);
        location.setZ(location.getZ()/num_vert);

        return location;
    }

    private String getHeaderBrush()
    {
        String tmp="";
        tmp +="Begin Actor Class=Brush\n";
        tmp +="    MainScale=(SheerAxis=SHEER_ZX)\n";
        tmp +="    PostScale=(SheerAxis=SHEER_ZX)\n";
        tmp +="    CsgOper=CSG_Subtract\n";
        tmp +="    Location=(X="+location.x+",Y="+location.y+",Z="+location.z+")\n";
        tmp +="    Begin Brush Name=Model0\n";
        tmp +="        Begin PolyList\n";
        return tmp;
    }
    /**
     *   (  1901 -8192  8192 ) (  1901  8192  8192 ) (  1901  8192 -8192 ) chromebar 0 11 112 1 6.200000
     * @param line
     */
    public void parseBrushData(String line)
    {
        String t[];
        String t2[];
        Vector3d vertex=new Vector3d();


        line = line.replaceAll("\\(  ", "(");
        line = line.replaceAll(" \\)", ")");
        line = line.replaceAll("  ", " ");
        line = line.replaceAll("\\) \\(", "\\,");
        line = line.replaceAll(" \\(", "");
        line = line.replaceAll("\\)", ",");
        HlPolygon hlp = null;
        t = line.split("\\,"); //1901 -8192  8192 )

        String tmp;
        int k=0;

        hlp = new HlPolygon();
        for(int i=0;i<3;i++)
        {
             
             tmp = t[i];
             tmp = tmp.replaceAll("   ", " ");
             tmp = tmp.replaceAll("  ", " ");
             
             t2 = tmp.split("\\ ");
             if(t2[0].equals("")){k +=1;}
             vertex.setX(Double.valueOf(t2[k]));
             vertex.setY(Double.valueOf(t2[k+1]));
             vertex.setZ(Double.valueOf(t2[k+2]));

             //System.out.println(vertex);
             hlp.addVertex(new Vector3d(vertex.getX(), vertex.getY(), vertex.getZ()));
             k=0;
        }


        this.al_polygon.add(hlp);
        
    }

    private void addPolygon(HlPolygon hlp)
    {
        this.al_polygon.add(hlp);
    }

}
