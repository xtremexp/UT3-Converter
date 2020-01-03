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
import java.util.ArrayList;

/**
 *
 * @author Hyperion
 */
public class UCModelConverter {

    File ucmodelfiles[];
    File outputfolder;

    public UCModelConverter(File[] ucmodelfiles, File outputfolder) {
        this.ucmodelfiles = ucmodelfiles;
        this.outputfolder = outputfolder;
    }

    public void convert() throws FileNotFoundException, IOException
    {
        for(int i=0;i<ucmodelfiles.length;i++)
        {
            System.out.print(i+"/"+(ucmodelfiles.length-1)+"-"+ucmodelfiles[i]+" ...");
            convertUCMFile(ucmodelfiles[i]);
            System.out.println("Done!");
        }
    }

    private void convertUCMFile(File ucfile) throws FileNotFoundException, IOException
    {
        BufferedReader bfr = new BufferedReader(new FileReader(ucfile));
        BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(outputfolder.getAbsolutePath()+"\\"+ucfile.getName())));
        String line="";
        ArrayList altex = new ArrayList();
        String oldmesh="";
        String mesh="";
        String texname="";
        String oldtex="";
        String oldanivfile="";

        while((line=bfr.readLine())!=null)
        {
            //#exec MESH IMPORT MESH=barrel3M ANIVFILE=MODELS\barrel3_a.3D DATAFILE=MODELS\barrel3_d.3D X=0 Y=0 Z=0
            if(line.contains("#exec MESH IMPORT"))
            {
                //#exec MESH IMPORT MESH=barrel3M
                oldanivfile = line.split("ANIVFILE=MODELS\\\\")[1];
                oldanivfile = oldanivfile.split("\\ ")[0];
                oldanivfile = oldanivfile.split("\\_")[0]; //->barrel3
                
                mesh = (line.split("MESH=")[1]).split("\\ ")[0];
                //line = line.replaceAll(mesh.substring(0, mesh.length()-1)+"_", mesh+"_");
                line = line.replaceAll(oldanivfile+"_", oldanivfile+"M_");
                bwr.write(line+"\n");
            }
            //#exec TEXTURE IMPORT NAME=jbarrel3 FILE=MODELS\barrel3.pcx GROUP=Skins   LODSET=2
            else if(line.contains("#exec TEXTURE IMPORT"))
            {
                texname = (line.split("NAME=")[1]).split("\\ ")[0];
                altex.add(texname);
                oldtex = line.split("FILE=MODELS\\\\")[1];
                System.out.println(oldtex);
                oldtex = oldtex.split("\\ ")[0];
                line = line.replaceAll(oldtex, texname+".tga");
                bwr.write(line+"\n");
            }
            //Mesh=VertMesh'Barrel3'
            else if(line.contains("Mesh=VertMesh"))
            {
                //Barrel3'
                oldmesh = line.split("\'")[1];

                oldmesh = oldmesh.split("\'")[0];
                line = line.replaceAll(oldmesh, mesh);
                bwr.write(line+"\n");
                for(int i=0;i<altex.size();i++)
                {
                    bwr.write("     Skins("+i+")=Texture\'"+altex.get(i).toString()+"\'\n");
                }
                
            }
            else if(line.contains("Skin="))
            {

            }
            else
            {
                bwr.write(line+"\n");
            }
        }

        bfr.close();
        bwr.close();
    }
    
    
}


