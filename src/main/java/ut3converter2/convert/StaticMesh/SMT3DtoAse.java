/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.StaticMesh;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import ut3converter2.Main;
import ut3converter2.T3DConvertor;
import ut3converter2.UTObject;
import ut3converter2.convert.Texture.TextureReplacer;

/**
 * Converts T3D UT2004 Staticmeshes to .ase (Ascii Scene Exporter)
 * @author Hyperion
 */
public class SMT3DtoAse extends T3DConvertor implements Runnable{

    String taskname="\n*** Conversion of T3D Staticmeshes files to ASE Format ***";
    File inputfile;
    File inputfolder;
    File[] t3dfiles;
    ArrayList altriangles;
    ArrayList altexlist;
    ArrayList altexlistfull;
    BufferedWriter bwr;
    boolean bHasTextureReplacement=false;
    TextureReplacer tr;
    boolean showresult = false;
    int cur_mode=0;
    boolean bshowlog=false;
    static int mode_folder=0;
    static int mode_files=1;
    String texsuffix="";
    String texprefix="";
    boolean isUT3Mesh=false;
    /**
     * Delete T3D SM source files after conversion
     */
    boolean deletet3dfiles=true;
    DecimalFormat df = new DecimalFormat("0.000000");


    public SMT3DtoAse(File inputfolder,boolean bshowlog) {
        this.inputfolder = inputfolder;
        super.setBShowLog(bshowlog);
        super.setTaskname(taskname);
        cur_mode = 0;
    }



    public SMT3DtoAse(File[] t3dfiles,boolean bshowlog) {
        this.t3dfiles = t3dfiles;
        super.setBShowLog(bshowlog);
        super.setTaskname(taskname);
        cur_mode = 1;
    }

    public boolean isBHasTextureReplacement() {
        return bHasTextureReplacement;
    }

    public void setBHasTextureReplacement(boolean bHasTextureReplacement) {
        this.bHasTextureReplacement = bHasTextureReplacement;
    }

    /**
     * Adds a TextureReplacer used to replace some special textures (shaders,...) with normal ones.
     * @param tr
     */
    public void setTr(TextureReplacer tr) {
        this.tr = tr;
    }


    /**
     * Export all T3D staticmeshes files to ASE files.
     * @throws java.io.IOException
     */
    public void export2Ase() throws IOException
    {
        File f[];
        if(super.isBShowLog()){System.out.println(taskname);}
        if(cur_mode==mode_folder)
        {
            f = inputfolder.listFiles();
            //super.setNumtottasks(f.length);
            
            for(int i=0;i<f.length;i++)
            {
                inputfile = f[i];
                if(inputfile.getName().endsWith(".t3d"))
                {
                    
                    File output = new File(f[i].getParent() + "/" + f[i].getName().replaceAll(".t3d", ".ase"));

                    if(super.isBShowLog()){System.out.print((i+1)+"/"+f.length+"-"+inputfile.getName()+" -> "+output.getName());}
                    bwr = new BufferedWriter(new FileWriter(output));
                    getT3DTriangles();
                    bwr.write(getHeader());
                    bwr.write(getMaterialList());
                    getGeomObject();
                    bwr.write("\t}\n");
                    bwr.write("\t*MATERIAL_REF 0\n");
                    bwr.write("}");
                    bwr.close();
                    if(super.isBShowLog()){System.out.println(" ... done!");}
                }
            }
            if(super.isBDispResult())
            {
                JOptionPane.showMessageDialog(null, "<html>"+f.length+" T3D Staticmeshes files have been converted to:<br>"+inputfolder.getParent());
            }
        }
        else if(cur_mode==mode_files)
        {
            f = t3dfiles;
            super.setNumtottasks(f.length);
            for(int i=0;i<f.length;i++)
            {
                inputfile = f[i];
                if(inputfile.getName().endsWith(".t3d"))
                {
                    File output = new File(f[i].getParent() + "/" + f[i].getName().replaceAll(".t3d", ".ase"));
                    if(super.isBShowLog()){System.out.print(i+"/"+(f.length-1)+"-"+inputfile.getName()+" ->"+output.getName()+" ...");}
                    bwr = new BufferedWriter(new FileWriter(output));
                    getT3DTriangles();
                    bwr.write(getHeader());
                    bwr.write(getMaterialList());
                    getGeomObject();
                    bwr.write("\t}\n");
                    bwr.write("\t*MATERIAL_REF 0\n");
                    bwr.write("}");
                    bwr.close();
                    if(super.isBShowLog()){System.out.println("... done!");}
                }
            }
            if(super.isBDispResult())
            {
                JOptionPane.showMessageDialog(null, "<html>"+f.length+" T3D Staticmeshes files have been converted to:<br>"+t3dfiles[0].getParent());
            }
        }
        if(deletet3dfiles){
            for (int i = 0; i < t3dfiles.length; i++) {
                t3dfiles[i].delete();
            }
        }
    }

    /**
     * Convert all T3D staticmeshes from specified folder.
     * @param outputfolder Folder where to find staticmeshes
     * @throws java.io.IOException
     */
    public void export2Ase(File outputfolder) throws IOException
    {
        File f[] = inputfolder.listFiles();
        super.setNumtottasks(f.length);
        for(int i=0;i<f.length;i++)
        {
            if(f[i].getName().endsWith(".t3d"))
            {
                File output = new File(outputfolder+"/"+f[i].getName().replaceAll(".t3d", ".ase"));
                bwr = new BufferedWriter(new FileWriter(output));
                getT3DTriangles();
                bwr.write(getHeader());
                bwr.write(getMaterialList());
                getGeomObject();
                bwr.write("\t}\n");
                bwr.write("\t*MATERIAL_REF 0\n");
                bwr.write("}");
                bwr.close();
            }
        }
    }

    /**Read T3D File and identify triangles used */
    private void getT3DTriangles()
    {
        altriangles = new ArrayList();
        altexlist = new ArrayList();
        altexlistfull = new ArrayList();
        String ta[];
        String tmp = "";
        String line = "";
        Float f=1F;
        String linesplit[];
        T3DTriangle t3dtri = null;
        int numtri = 0;
        int numvert = 0;
        Vertex vx = null;


        try {
            BufferedReader bfr = new BufferedReader(new FileReader(inputfile));
            while((line=bfr.readLine())!=null)
            {
                if(line.contains("Begin Triangle"))
                {
                    t3dtri = new T3DTriangle();
                }
                //        Texture BenTex01.Shader.SnowyBranch02
                else if(line.contains("Texture"))
                {
                    line = line.split("Texture ")[1]; //BenTex01.Shader.SnowyBranch02
                    line = line.toLowerCase(); //bentex01.shader.snowybranch02
                    
                    
                    if((bHasTextureReplacement)&&line.split("\\.").length>1)
                    {
                        UTObject uto = new UTObject(line);
                        if(tr.hasTextureReplacement(uto.getGroupAndName()))
                        {
                            //System.out.println(tmp+"->"+tr.getTextureReplacement(tmp));
                            line = tr.getTextureReplacement(uto.getGroupAndName()); //martint.atlantis.skinfrontbig
                            uto = new UTObject(line);
                            tmp = uto.getGroupAndName2();
                            if(isUT3Mesh){tmp += "_Mat";}
                            //martint_atlantis_skinfrontbig_Mat
                        }
                        else
                        {
                            tmp = getTexName(line);
                        }
                    }
                    else
                    {
                        tmp = getTexName(line);
                    }

                    //tmp = texprefix+tmp;
                    line = replaceChar(line, 46, '_');
                    tmp = replaceChar(tmp, 46, '_'); // '.'->'_'
                    if(!altexlistfull.contains(line))
                    {
                        altexlistfull.add(line);
                    }
                    //tmp = line.split("\\ ")[1];
                    
                    t3dtri.setTexture(tmp);
                    if(!altexlist.contains(tmp))
                    {
                        altexlist.add(tmp);
                    }
                    t3dtri.setNummtlid(altexlist.indexOf(tmp));
                }
                //        Vertex 0 -62.000000 1.000000 -62.000000 0.968749 -0.968750
                else if(line.contains("Vertex"))
                {
                    line = line.substring(line.indexOf("x"));
                    linesplit = line.split("\\ ");
                    vx = new Vertex();
                    //vx = new Vertex(Float.valueOf(linesplit[2]), Float.valueOf(linesplit[3]), Float.valueOf(linesplit[4]), Float.valueOf(linesplit[5]), Float.valueOf(linesplit[6]));
                    try {f = Float.valueOf(linesplit[2]);
                        vx.setMvert1(f);
                    } catch (NumberFormatException e) {
                        vx.setMvert1(1F);
                    }
                    try {f = Float.valueOf(linesplit[3]);
                        vx.setMvert2(f);
                    } catch (NumberFormatException e) {
                        vx.setMvert2(1F);
                    }
                    try {f = Float.valueOf(linesplit[4]);
                        vx.setMvert3(f);
                    } catch (NumberFormatException e) {
                        vx.setMvert3(1F);
                    }
                    try {f = Float.valueOf(linesplit[5]);
                        vx.setTvert1(f);
                    } catch (NumberFormatException e) {
                        vx.setTvert1(1F);
                    }
                    try {f = Float.valueOf(linesplit[6]);
                        vx.setTvert2(f);
                    } catch (NumberFormatException e) {
                        vx.setTvert2(1F);
                    }
                    
                    
                    if(numvert==0)
                    {
                        t3dtri.setV1(vx);
                    }
                    else if(numvert==1)
                    {
                        t3dtri.setV2(vx);
                    }
                    else if(numvert ==2)
                    {
                        t3dtri.setV3(vx);
                    }
                    numvert ++;
                }
                else if(line.contains("End Triangle"))
                {
                    numtri ++;
                    numvert =0;
                    //System.out.println(t3dtri.getTexture());
                    altriangles.add(t3dtri);
                }
            }
            bfr.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SMT3DtoAse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SMT3DtoAse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isIsUT3Mesh() {
        return isUT3Mesh;
    }

    public void setIsUT3Mesh(boolean isUT3Mesh) {
        this.isUT3Mesh = isUT3Mesh;
    }


    private String getTexName(String line)
    {
        //        Texture Egypt_tech_Epic.Shaders.passage2048unlit
        //-->     Shaders_passage2048unlit_Mat
        //textures.branchsnowy01
        //        Texture None

        //line = line.split("\\ ")[1];
        String tmp[] = line.split("\\.");
        if(line.equals("none"))
        {
            return line;
        }
        if(tmp.length==2)
        {
            if(isUT3Mesh)
            {
                return tmp[1]+"_Mat";
            }
            else{return tmp[1]+texsuffix;}
            
        }
        else if(tmp.length==3)
        {
            if(isUT3Mesh)
            {
                return tmp[1]+"_"+tmp[2]+"_Mat";
            }
            else{return tmp[2]+texsuffix;}
            //
            
        }
        return null;
    }
    private String getHeader()
    {
        String tmp = "";
        tmp += "ASE - 3D Studio Max Ascii Export Format\n";
        tmp += "*3DSMAX_ASCIIEXPORT 200\n";
        tmp += "*COMMENT \"UT3 Converter "+Main.numversion+" : T3D To Ase Conversion -\"\n";
        tmp += "*COMMENT \"Input File:"+inputfile.getAbsolutePath()+"\"\n";
        tmp += "*COMMENT \"List of textures used -\"\n";
        for(int i=0;i<altexlistfull.size();i++)
        {
            tmp += "*COMMENT \""+altexlistfull.get(i).toString()+"\"\n";
        }
        return tmp;
    }
    
    private String getMaterialList()
    {
        String tmp = "";
        tmp += "*MATERIAL_LIST {\n";
        tmp += "\t*MATERIAL_COUNT 1\n";
        tmp += "\t*MATERIAL 0 {\n";
        tmp += "\t\t*MATERIAL_NAME \"Default Material\"\n";
        tmp += "\t\t*MATERIAL_CLASS \"Multi/Sub-Object\"\n";
        tmp += "\t\t*MATERIAL_AMBIENT 1.0000   1.0000   1.0000\n";
        tmp += "\t\t*MATERIAL_DIFFUSE 1.0000   1.0000   1.0000\n";
        tmp += "\t\t*MATERIAL_SPECULAR 1.0000   1.0000   1.0000\n";
        tmp += "\t\t*NUMSUBMTLS "+altexlist.size()+"\n";
        
        for(int i=0;i<altexlist.size();i++)
        {
        tmp += "\t\t*SUBMATERIAL "+i+" {\n";
        tmp += "\t\t\t*MATERIAL_NAME \""+altexlist.get(i).toString()+"\"\n";
        tmp += "\t\t\t*MATERIAL_AMBIENT 1  1  1\n";
        tmp += "\t\t\t*MATERIAL_DIFFUSE 1  1  1\n";
        tmp += "\t\t\t*MATERIAL_SPECULAR 1  1  1\n";
        tmp += "\t\t\t*MAP_DIFFUSE {\n";
        tmp += "\t\t\t\t*MAP_NAME \"Map #"+i+"\"\n";
        tmp += "\t\t\t\t*MAP_CLASS \"Bitmap\"\n";
        tmp += "\t\t\t\t*MAP_SUBNO 1\n";
        tmp += "\t\t\t\t*MAP_AMOUNT 1\n";
        tmp += "\t\t\t\t*BITMAP \""+altexlist.get(i).toString()+"\"\n";
        tmp += "\t\t\t\t*MAP_TYPE Screen\n";
        tmp += "\t\t\t\t*UVW_U_OFFSET 0.000000\n";
        tmp += "\t\t\t\t*UVW_V_OFFSET 0.000000\n";
        tmp += "\t\t\t\t*UVW_U_TILING 1.000000\n";
        tmp += "\t\t\t\t*UVW_V_TILING 1.000000\n";
        tmp += "\t\t\t\t*UVW_ANGLE 0.000000\n";
        tmp += "\t\t\t\t*UVW_BLUR 1.000000\n";
        tmp += "\t\t\t\t*UVW_BLUR_OFFSET 0.000000\n";
        tmp += "\t\t\t\t*UVW_NOUSE_AMT 1.000000\n";
        tmp += "\t\t\t\t*UVW_NOISE_SIZE 1.000000\n";
        tmp += "\t\t\t\t*UVW_NOISE_LEVEL 1\n";
        tmp += "\t\t\t\t*UVW_NOISE_PHASE 0.000000\n";
        tmp += "\t\t\t\t*BITMAP_FILTER Pyramidal\n";
        tmp += "\t\t\t}\n";
        tmp += "\t\t}\n";
        }
        tmp += "\t}\n";
        tmp += "}\n";
        return tmp;
    }
    
    private void getGeomObject() throws IOException
    {
        String tmp = "";
        tmp += "*GEOMOBJECT {\n";
        tmp += "\t*MESH {\n";
        tmp += "\t\t*TIMEVALUE 0\n";
        tmp += "\t\t*MESH_NUMVERTEX "+altriangles.size()*3+"\n";
        tmp += "\t\t*MESH_NUMFACES "+altriangles.size()+"\n";
        bwr.write(tmp);
        getMeshVertexList();
        getMeshFaceList();
        getTVertList();
        getTFaceList();
    }

    private void getMeshVertexList() throws IOException
    {
        String tmp = "";
        T3DTriangle t3d;
        Vertex v1;
        Vertex v2;
        Vertex v3;
        
        bwr.write("\t\t*MESH_VERTEX_LIST {\n");
        for(int i=0;i<altriangles.size();i++)
        {
            t3d = (T3DTriangle) altriangles.get(i);
            v1 = t3d.getV1();
            v2 = t3d.getV2();
            v3 = t3d.getV3();
            tmp =  "\t\t\t*MESH_VERTEX    "+(i*3)+"\t"+df.format(v1.getMvert1())+"\t"+df.format(v1.getMvert2()*(-1F))+"\t"+df.format(v1.getMvert3())+"\n";
            tmp += "\t\t\t*MESH_VERTEX    "+(i*3+1)+"\t"+df.format(v2.getMvert1())+"\t"+df.format(v2.getMvert2()*(-1F))+"\t"+df.format(v2.getMvert3())+"\n";
            tmp += "\t\t\t*MESH_VERTEX    "+(i*3+2)+"\t"+df.format(v3.getMvert1())+"\t"+df.format(v3.getMvert2()*(-1F))+"\t"+df.format(v3.getMvert3())+"\n";
            bwr.write(tmp);
        }
        bwr.write("\t\t}\n");
    }
    
    private void getMeshFaceList() throws IOException
    {
        T3DTriangle t3d;
        /*
         * 
         *MESH_FACE_LIST {
               *MESH_FACE 0: A: 0 B: 1 C: 2 AB: 1 BC: 0 CA: 1 *MESH_SMOOTHING 0 *MESH_MTLID 0
               *MESH_FACE 1: A: 3 B: 4 C: 5 AB: 1 BC: 0 CA: 1 *MESH_SMOOTHING 0 *MESH_MTLID 0

         */
        bwr.write("\t\t*MESH_FACE_LIST {\n");
        for(int i=0;i<altriangles.size();i++)
        {
            t3d = (T3DTriangle) altriangles.get(i);
            bwr.write("\t\t\t*MESH_FACE "+i+": A: "+(i*3)+" B: "+((i*3)+1)+" C: "+((i*3)+2)+" AB: 1 BC: 0 CA: 1\t *MESH_SMOOTHING 1 \t*MESH_MTLID "+t3d.getNummtlid()+"\n");
        }
        bwr.write("\t\t}\n");
        
    }
    
    private void getTVertList() throws IOException
    {
        bwr.write("\t\t*MESH_NUMTVERTEX "+altriangles.size()*3+"\n");
        bwr.write("\t\t*MESH_TVERTLIST {\n");
        String tmp;
        T3DTriangle t3d;
        Vertex v1;
        Vertex v2;
        Vertex v3;
        /*MESH_TVERT 0  0.968749  0.96875  1*/

        for(int i=0;i<altriangles.size();i++)
        {
            t3d = (T3DTriangle) altriangles.get(i);
            v1 = t3d.getV1();
            v2 = t3d.getV2();
            v3 = t3d.getV3();
            tmp =  "\t\t\t*MESH_TVERT "+(i*3)+"\t"+v1.getTvert1()+"\t"+v1.getTvert2()*(-1F)+"\t1\n";
            tmp += "\t\t\t*MESH_TVERT "+((i*3)+1)+"\t"+v2.getTvert1()+"\t"+v2.getTvert2()*(-1F)+"\t1\n";
            tmp += "\t\t\t*MESH_TVERT "+((i*3)+2)+"\t"+v3.getTvert1()+"\t"+v3.getTvert2()*(-1F)+"\t1\n";
            bwr.write(tmp);
        }
        bwr.write("\t\t}\n");
    }

    private void getTFaceList() throws IOException
    {
        bwr.write("\t\t*MESH_NUMTVFACES "+altriangles.size()+"\n");
        bwr.write("\t\t*MESH_TFACELIST {\n");
        for(int i=0;i<altriangles.size();i++)
        {
            bwr.write("\t\t\t*MESH_TFACE "+i+"\t"+(i*3)+"\t"+((i*3)+1)+"\t"+((i*3)+2)+" \n");
        }
        bwr.write("\t\t}\n");

    }

    private String replaceChar(String line,int numchar,char newchar)
    {
        String tmp="";
        int nchar=0;

        for(int i=0;i<line.length();i++)
        {
            nchar=(int)line.charAt(i);
            if(nchar!=numchar)
            {
                tmp += line.charAt(i);
            }
            else
            {
                tmp += String.valueOf(newchar);
            }
        }
        return tmp;
    }

    public void setDeleteSourceFilesAfterConversion(boolean deletet3dfiles) {
        this.deletet3dfiles = deletet3dfiles;
    }
    
    public void run() {
        try {
            this.export2Ase();
        } catch (IOException ex) {
            Logger.getLogger(SMT3DtoAse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Name of default package+"."
     * @param texprefix "myPackage."
     */
    public void setTexprefix(String texprefix) {
        this.texprefix = texprefix;
    }


}
