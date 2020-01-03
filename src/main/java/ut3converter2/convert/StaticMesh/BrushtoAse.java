/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ut3converter2.convert.StaticMesh;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Converts brushes directly to staticmeshes as .ase files.
 * Useful to port brush movers from UT/U1 game.
 * Experimental use only.
 * Texture alignment not supported yet.
 * INPUT: T3D (BRH) Brush File
 * OUTPUT: ASE StaticMesh File
 * @author Hyperion
 */
public class BrushtoAse {

    BufferedWriter bwr;
    File inputbrushfile;
    int numvertex = 0;
    int numtriangles;
    int numtex = 0;
    ArrayList<BrPolygon> alpolygon;
    ArrayList<BrTriangle> altriangles;
    HashMap hmtexnum;
    DecimalFormat df = new DecimalFormat("0.000000");

    public BrushtoAse(File inputbrushfile) {
        this.inputbrushfile = inputbrushfile;
        alpolygon = new ArrayList<BrPolygon>();
        hmtexnum = new HashMap();
    }

    public void loadBrushData() throws FileNotFoundException, IOException {
        BufferedReader bfr = new BufferedReader(new FileReader(inputbrushfile));
        String line = "";
        BrPolygon brp = new BrPolygon();
        altriangles = new ArrayList<BrTriangle>();

        while ((line = bfr.readLine()) != null) {
            if (line.contains("Begin Polygon")) {
                brp = new BrPolygon();
                brp.setTexture(getTexture(line));
            }
            if (line.contains("Origin")) {
                brp.setOrigin(getVector(line, "Origin"));
                //brp = new BrPolygon();
                //brp.setTexture(getTexture(line));
            } else if (line.contains("Vertex")) {
                brp.addVertex(new BrVertex(getVector(line, "Vertex"), brp.getOrigin()));
                numvertex++;
            } else if (line.contains("TextureU")) {
                brp.setBrtu(new BrTexU(getVector(line, "TextureU")));
            } else if (line.contains("TextureV")) {
                brp.setBrtv(new BrTexV(getVector(line, "TextureV")));
            } else if (line.contains("End Polygon")) {
                brp.convertToTriangles();
                updateTriangles(brp);
                alpolygon.add(brp);

            }
        }

        bfr.close();
        System.out.println(alpolygon.size() + " polygons");
        export2Ase();

    }

    private void updateTriangles(BrPolygon brp) {
        ArrayList al = brp.getAltriangles();

        BrTriangle brt = null;

        for (int i = 0; i < al.size(); i++) {

            brt = (BrTriangle) al.get(i);
            altriangles.add(brt);
        }
    }

    /**
     *  Begin Polygon Item=2DLoftTOP Texture=BASEIRM3 Link=0
     * @param line
     */
    private String getTexture(String line) {
        String texture = "";
        if (line.contains("Texture=")) {
            line = line.split("Texture=")[1];
            texture = line.split(" ")[0];
            if (!hmtexnum.containsKey(texture)) {
                hmtexnum.put(texture, numtex);
                numtex++;
            }
        }

        return texture;
    }

    /**
     *              Vertex   +00000.000000,-00032.000000,+00000.000000
     * @param line
     * @return
     */
    private double[] getVector(String line, String delimitor) {
        double d[] = new double[3];
        String tmp = "";

        tmp = line.split(delimitor + " ")[1];


        String tmp2[] = tmp.split("\\,");
        d[0] = Double.valueOf(tmp2[0]);
        d[1] = Double.valueOf(tmp2[1]);
        d[2] = Double.valueOf(tmp2[2]);

        return d;
    }

    private String getMaterialId(BrTriangle brt) {
        String texname = brt.getTexture();
        int num = -1;
        if (hmtexnum.containsKey(texname)) {
            num = Integer.valueOf(hmtexnum.get(texname).toString());
        }
        return String.valueOf(num);
    }

    public void export2Ase() throws IOException, IOException {

        File output = new File(inputbrushfile.getParent() + "/" + inputbrushfile.getName().replaceAll(".t3d", ".ase"));
        System.out.println(output.getAbsolutePath());
        bwr = new BufferedWriter(new FileWriter(output));
        bwr.write(getHeader());
        bwr.write(getMaterialList());
        getGeomObject();
        bwr.write("\t}\n");
        bwr.write("\t*MATERIAL_REF 0\n");
        bwr.write("}");
        bwr.close();
    }

    private void getGeomObject() throws IOException {
        String tmp = "";
        tmp += "*GEOMOBJECT {\n";
        tmp += "\t*MESH {\n";
        tmp += "\t\t*TIMEVALUE 0\n";
        tmp += "\t\t*MESH_NUMVERTEX " + altriangles.size() * 3 + "\n";
        tmp += "\t\t*MESH_NUMFACES " + altriangles.size() + "\n";
        bwr.write(tmp);
        getMeshVertexList();
        getMeshFaceList();
        getTVertList();
        getTFaceList();
    }

    private String getHeader() {
        String tmp = "";
        tmp += "*3DSMAX_ASCIIEXPORT\t200\n";
        tmp += "*COMMENT \"UT3 Converter : Brush To Ase Conversion -\"\n";
        tmp += "*COMMENT \"Input File: " + inputbrushfile.getAbsolutePath() + "\"\n";
        return tmp;
    }

    private String getMaterialList() {
        StringBuilder sb = new StringBuilder();

        sb.append("*MATERIAL_LIST {\n");
        sb.append("*MATERIAL_LIST {\n");
        sb.append("\t*MATERIAL_COUNT 1\n");
        sb.append("\t*MATERIAL 0 {\n");
        sb.append("\t\t*MATERIAL_NAME \"Default Material\"\n");
        sb.append("\t\t*MATERIAL_CLASS \"Multi/Sub-Object\"\n");
        sb.append("\t\t*MATERIAL_AMBIENT 1.0000\t1.0000\t1.0000\n");
        sb.append("\t\t*MATERIAL_DIFFUSE 1.0000\t1.0000\t1.0000\n");
        sb.append("\t\t*MATERIAL_SPECULAR 1.0000\t1.0000\t1.0000\n");
        sb.append("\t\t*NUMSUBMTLS ").append(hmtexnum.size()).append("\n");

        Iterator it = hmtexnum.keySet().iterator();

        int i = 0;
        String texname = "";
        while (it.hasNext()) {
            texname = it.next().toString();
            sb.append("\t\t*SUBMATERIAL ").append(i).append(" {\n");
            sb.append("\t\t\t*MATERIAL_NAME \"").append(texname).append("\"\n");
            sb.append("\t\t\t*MATERIAL_AMBIENT 1\t1\t1\n");
            sb.append("\t\t\t*MATERIAL_DIFFUSE 1\t1\t1\n");
            sb.append("\t\t\t*MATERIAL_SPECULAR 1\t1\t1\n");
            sb.append("\t\t\t*MAP_DIFFUSE {\n");
            sb.append("\t\t\t*MAP_CLASS \"Bitmap\"\n");
            sb.append("\t\t\t*BITMAP \"").append(texname).append("\"\n");
            sb.append("\t\t\t}\n");
            sb.append("\t\t}\n");
            i++;
        }

        sb.append("\t}\n");
        sb.append("}\n");
        return sb.toString();
    }

    private void getMeshVertexList() throws IOException {
        String tmp = "";
        BrTriangle brt;
        BrVertex v1;
        BrVertex v2;
        BrVertex v3;

        bwr.write("\t\t*MESH_VERTEX_LIST {\n");

        for (int i = 0; i < altriangles.size(); i++) {
            brt = (BrTriangle) altriangles.get(i);
            v1 = brt.getBv1();
            v2 = brt.getBv2();
            v3 = brt.getBv3();

            tmp = "\t\t\t*MESH_VERTEX    " + (i * 3) + "\t" + df.format(v1.getVx()) + "\t" + df.format(v1.getVy() * (1F)) + "\t" + df.format(v1.getVz()) + "\n";
            tmp += "\t\t\t*MESH_VERTEX    " + (i * 3 + 1) + "\t" + df.format(v2.getVx()) + "\t" + df.format(v2.getVy() * (1F)) + "\t" + df.format(v2.getVz()) + "\n";
            tmp += "\t\t\t*MESH_VERTEX    " + (i * 3 + 2) + "\t" + df.format(v3.getVx()) + "\t" + df.format(v3.getVy() * (1F)) + "\t" + df.format(v3.getVz()) + "\n";
            bwr.write(tmp);
        }

        bwr.write("\t\t}\n");
    }

    private void getMeshFaceList() throws IOException {
        /*
         *MESH_FACE_LIST {
         *MESH_FACE 0: A: 0 B: 1 C: 2 AB: 1 BC: 0 CA: 1 *MESH_SMOOTHING 0 *MESH_MTLID 0
         *MESH_FACE 1: A: 3 B: 4 C: 5 AB: 1 BC: 0 CA: 1 *MESH_SMOOTHING 0 *MESH_MTLID 0

         */
        bwr.write("\t\t*MESH_FACE_LIST {\n");

        BrTriangle brt;

        for (int i = 0; i < altriangles.size(); i++) {
            brt = (BrTriangle) altriangles.get(i);
            bwr.write("\t\t\t*MESH_FACE " + i + ": A: " + (i * 3) + " B: " + ((i * 3) + 1) + " C: " + ((i * 3) + 2) + " AB:    1 BC:    0 CA:    1\t *MESH_SMOOTHING 1 \t*MESH_MTLID " + getMaterialId(brt) + "\n");
        }
        bwr.write("\t\t}\n");

    }

    private void getTVertList() throws IOException {
        bwr.write("\t\t*MESH_NUMTVERTEX " + altriangles.size() * 3 + "\n");
        bwr.write("\t\t*MESH_TVERTLIST {\n");
        String tmp;

        /*MESH_TVERT 0  0.968749  0.96875  1*/

        for (int i = 0; i < altriangles.size(); i++) {
            /*
            t3d = (T3DTriangle) altriangles.get(i);
            v1 = t3d.getV1();
            v2 = t3d.getV2();
            v3 = t3d.getV3();*/
            /*
             * tmp =  "              *MESH_TVERT "+(i*3)+"  "+v1.getTvert1()+" "+v1.getTvert2()*(-1F)+" 1\n";
            tmp += "              *MESH_TVERT "+((i*3)+1)+"  "+v2.getTvert1()+" "+v2.getTvert2()*(-1F)+" 1\n";
            tmp += "              *MESH_TVERT "+((i*3)+2)+"  "+v3.getTvert1()+" "+v3.getTvert2()*(-1F)+" 1\n";*/
            tmp = "\t\t\t*MESH_TVERT " + (i * 3) + "\t-20\t10\t-1\n";
            tmp += "\t\t\t*MESH_TVERT " + ((i * 3) + 1) + "\t-20\t10\t-1\n";
            tmp += "\t\t\t*MESH_TVERT " + ((i * 3) + 2) + "\t-20\t10\t-1\n";
            bwr.write(tmp);
        }
        bwr.write("\t\t}\n");

    }

    private void getTFaceList() throws IOException {
        bwr.write("\t\t*MESH_NUMTVFACES " + altriangles.size() + "\n");
        bwr.write("\t\t*MESH_TFACELIST {\n");
        for (int i = 0; i < altriangles.size(); i++) {
            bwr.write("\t\t\t*MESH_TFACE " + i + "\t" + (i * 3) + "\t" + ((i * 3) + 1) + "\t" + ((i * 3) + 2) + "\n");
        }
        bwr.write("\t\t}\n");

    }
}
