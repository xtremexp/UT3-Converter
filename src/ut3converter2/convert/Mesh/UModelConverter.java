/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Mesh;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import ut3converter2.Main;
import ut3converter2.convert.Texture.TextureConverter;
import ut3converter2.export.GlobalUCCExporter;
import ut3converter2.export.TextureExporter;
import ut3converter2.export.UModelExporter;

/**
 *
 * @author Hyperion
 */
public class UModelConverter {

    File inputufiles[];
    String decopacname;
    static String vertmeshname="";
    static String texname2="";
    File uccfilepath;
    File rootgamein;
    File rootgameout;
    int inputgame;
    int outputgame;
    /**
     * Texture Folder: /UTRootFolder/DecoFolder/Textures
     */
    File texfolder;
    /**
     * Temporary class folder
     */
    File tempclfolder;
    /**
     * Class Folder: /UTRootFolder/DecoFolder/Class
     */
    File classfolder;

    public UModelConverter(File inputufiles[], String decopacname,int inputgame,int outputgame) {
        this.inputufiles = inputufiles;
        this.decopacname = decopacname;
        this.rootgamein = Main.config.getUTxRootFolder(inputgame);
        this.rootgameout = Main.config.getUTxRootFolder(outputgame);
        this.inputgame = inputgame;
        this.outputgame = outputgame;
        
    }


    public void convert() throws IOException, InterruptedException
    {
        File pacfolder = new File(rootgameout.getAbsoluteFile()+"\\"+decopacname);
        this.classfolder = new File(rootgameout.getAbsoluteFile()+"\\"+decopacname+"\\Classes\\");
        File modelsfolder = new File(rootgameout.getAbsoluteFile()+"\\"+decopacname+"\\Models\\");
        this.tempclfolder = new File(rootgameout.getAbsoluteFile()+"\\"+decopacname+"\\TempClasses\\");
        this.texfolder = new File(rootgameout.getAbsoluteFile()+"\\"+decopacname+"\\Textures\\");

        
        if(!pacfolder.exists()){pacfolder.mkdir();}
        if(!classfolder.exists()){classfolder.mkdir();}
        if(!modelsfolder.exists()){modelsfolder.mkdir();}
        if(!tempclfolder.exists()){tempclfolder.mkdir();}
        if(!texfolder.exists()){texfolder.mkdir();}



        for(int i=0;i<inputufiles.length;i++)
        {
            System.out.print("Extracting models from "+inputufiles[i]);
            //Extract 3d models to Models folder
            UModelExporter ume = new UModelExporter(inputufiles[i]);
            ume.ExportTo(modelsfolder);

            Thread.sleep(500L);
            //Extract uc files
            GlobalUCCExporter gue = new GlobalUCCExporter(inputufiles[i], rootgamein);
            //File tempfodler = File.createTempFile(decopacname, decopacname, pacfolder)
            gue.exportUCFilesTo(tempclfolder);
            System.out.println("... Done!");

            System.out.print("Exporting texture files ...");
            exportTexFilesFromUFile(inputufiles[i]);
            System.out.println("Done!");

            //convertBmpTextoPcxTex();
        }
        
        UpdateModelFiles();
        
    }

    private void convertBmpTextoPcxTex()
    {
        System.out.print("Converting texture files to PCX ...");
        TextureConverter tc = new TextureConverter(texfolder.listFiles(), "pcx");
        tc.useMergeAlpha();
        tc.convertAll();
        System.out.println("Done!");
    }
    private void UpdateModelFiles() throws FileNotFoundException, InterruptedException, IOException
    {
        //Update uc model files
        File ucmodfiles[] = tempclfolder.listFiles();
        File ucmodfin;
        System.out.print("Updating Model Files...");
        for(int i=0;i<ucmodfiles.length;i++)
        {
            //System.out.print(i+"/"+(ucmodfiles.length-1)+"->"+ucmodfiles[i].getName());
            ucmodfin = ucmodfiles[i];
            if(isADecoration(ucmodfin))
            {
                updateUCModelFile(ucmodfin,new File(this.classfolder.getAbsolutePath()+"\\"+ucmodfin.getName()));
            }
            else
            {
                ucmodfin.delete();
            }
            //System.out.println("Done!");
            Thread.sleep(30L);
        }
        tempclfolder.delete();
        System.out.println("Done!");
    }

    /**
     * #exec MESH SEQUENCE MESH=barrel1M SEQ=Still  STARTFRAME=0   NUMFRAMES=1
       #exec TEXTURE IMPORT NAME=jbarreli1 FILE=MODELS\barrel1.pcx GROUP=Skins LODSET=2
     * @param line
     */
    private static String parseExecLines(String line)
    {
        //#exec TEXTURE IMPORT NAME=Jearth1 FILE=MODELS\earth.pcx GROUP=Skins   LODSET=2
        if(line.contains("TEXTURE IMPORT"))
        {
            String texname = line.split("NAME=")[1].split("\\ ")[0];
            texname2 = texname; //Jearth1
            String texfilepath = line.split("FILE=")[1].split("\\ ")[0];
            String texfilename = texfilepath.split("\\\\")[1]; //earth.pcx
            line = line.replaceAll("\\\\"+texfilename.split("\\.")[0],"\\\\"+texname);
            line = line.replaceAll("MODELS","TEXTURES");
            line = line.replaceAll(".pcx", ".bmp");
            line = line.replaceAll(".PCX", ".bmp");
        }
        //#exec MESH IMPORT MESH=barrel1M ANIVFILE=MODELS\barrel1_a.3D DATAFILE=MODELS\barrel1_d.3D X=0 Y=0 Z=0
        else if(line.contains("MESH IMPORT"))
        {
            String meshname = line.split("MESH=")[1].split("\\ ")[0];
            vertmeshname = meshname;
            String mesh_aniv_filepath = line.split("ANIVFILE=")[1].split("\\ ")[0];
            String anivname = mesh_aniv_filepath.split("\\\\")[1].split("\\_a")[0];

            String mesh_data_filepath = line.split("DATAFILE=")[1].split("\\ ")[0];
            String dataname = mesh_data_filepath.split("\\\\")[1].split("\\_d")[0];

            line = line.replaceAll("\\\\"+anivname, "\\\\"+meshname);
        }
        else
        {
            return line;
        }
        return line;
    }


    private void exportTexFilesFromUFile(File ufile)
    {
        File f[]= new File[1];
        f[0] = ufile;
        TextureExporter te = new TextureExporter(f, new String[]{"pcx"}, texfolder, inputgame);
        te.setBDispResult(true);
        te.ExportTex();
    }

    /**
     * Analyse if the uc file is a deco or not
     * @param ucmodelfile UC File
     * @return
     */
    private static boolean isADecoration(File ucmodelfile) throws FileNotFoundException, IOException
    {
        BufferedReader bfr = new BufferedReader(new FileReader(ucmodelfile));
        String line="";
        while((line=bfr.readLine())!=null)
        {
            if(line.toLowerCase().contains("class")&&line.toLowerCase().contains("decoration"))
            {
                return true;
            }
            if(line.toLowerCase().contains("mesh=lodmesh"))
            {
                return true;
            }
        }
        return false;
    }

    private static void updateUCModelFile(File ucmodelfile,File fileout) throws FileNotFoundException, IOException
    {

        BufferedReader bfr = new BufferedReader(new FileReader(ucmodelfile));
        BufferedWriter bwr = new BufferedWriter(new FileWriter(fileout));

        texname2 ="";
        String line="";
        boolean bwrite=true;
        boolean bhasskin=false;
        boolean isdefaultprop=false;

        while((line=bfr.readLine())!=null)
        {
            
            if(bwrite)
            {

                if(line.contains("bMeshEnviroMap")||(line.contains("Texture="))||(line.contains("MESHMAP SETTEXTURE")))
                {

                }
                else if(line.contains("#exec"))
                {
                    bwr.write(parseExecLines(line)+"\n");
                }
                else if(line.toLowerCase().contains("defaultproperties"))
                {
                    isdefaultprop = true;
                    bwr.write(line+"\n");
                }
                //     Skin=Texture'BotPack.Skins.Jearth1'
                else if(line.contains("Skin="))
                {
                    String tex = line.split("\\'")[1]; //BotPack.Skins.Jearth1
                    String temp[]=tex.split("\\.");
                    String tmp2 = line.split("kin=")[1];
                    //System.out.println(tex+"->"+texname2);
                    bwr.write("     Skins(0)="+tmp2.replaceAll(tex, temp[temp.length-1])+"\n");
                    bhasskin = true;
                }
                else if(line.contains("MultiSkins"))
                {
                    bwr.write(line.replaceAll("MultiSkins", "Skins")+"\n");
                }
                else if(line.contains("MultiSkins"))
                {
                    bwr.write(line.replaceAll("MultiSkins", "Skins")+"\n");
                }
                //Fragments(0)=LodMesh'UnrealShare.wfrag1'
                else if(line.contains("LodMesh"))
                {
                    if(line.contains("Mesh="))
                    {
                        bwr.write("     Mesh=VertMesh'"+vertmeshname+"\'\n");
                    }
                    else
                    {
                        String aa = line.split("\\'")[1];
                        String tt[] = aa.split("\\.");
                        line = line.replaceAll("LodMesh", "VertMesh");
                        line = line.replaceAll(aa, tt[tt.length-1]);
                        bwr.write(line+"\n");
                    }
                    //bwr.write("     Mesh=VertMesh'"+ucmodelfile.getName().split(".uc")[0]+"\'\n");
                }
                else if(line.contains("}")&&(isdefaultprop))
                {
                    if(!bhasskin&&(!texname2.equals("")))
                    {
                        bwr.write("     Skins(0)=Texture'"+texname2+"'\n");
                    }
                    bwr.write(line+"\n");
                    isdefaultprop = false;
                }
                else if(line.contains("expands"))
                {
                    bwr.write(line.replaceAll("expands", "extends")+"\n");
                    bwrite = true;
                }
                else
                {
                    bwr.write(line+"\n");
                }
            }
            
        }

        bfr.close();
        bwr.close();

        ucmodelfile.delete();
        if(!bwrite)
        {
            fileout.delete();
        }
    }

    //#exec mesh import mesh=XidiaBashedDoor anivfile=Models\XidiaBashedDoor_a.3d datafile=Models\XidiaBashedDoor_d.3d x=0 y=0 z=0 mlod=0
    private String updatePath(String line)
    {
        String anivfileval=(line.split("anivfile=")[1]).split("\\ ")[0];
        String datafile=(line.split("datafile=")[1]).split("\\ ")[0];
        line = line.replace(anivfileval, "'"+anivfileval+"'");
        line = line.replace(datafile, "'"+datafile+"'");

        return line;
    }

    
    
}
