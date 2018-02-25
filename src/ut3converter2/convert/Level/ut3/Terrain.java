/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut3;

import java.awt.TrayIcon.MessageType;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import ut3converter2.Main;
import ut3converter2.T3DConvertor;
import ut3converter2.UTGames;
import ut3converter2.UTObject;
import ut3converter2.convert.MapConverter;
import ut3converter2.convert.terrain.TerBmpToTiff;
import ut3converter2.convert.terrain.TerrainTiffReader;
import ut3converter2.tools.actor.Location;

/**
 *
 * @author Hyperion
 */
public class Terrain extends T3DConvertor{

    Location loc;
    String otherdata="";
    ArrayList <TerrainLayer>alterlayers = null;
    ArrayList <DecoLayer>aldecolayers = null;
    HashMap hmqvbitmap = null;
    File terfolder = null;
    File hmbmpfile=null;
    File hmtiff_file=null;
    String savpack="";
    String drawscale3dstr="       DrawScale3D=(X=64.000000,Y=64.000000,Z=64.000000)";
    float drawscale3d=1F;
    /**
     * Current Number of Decolayer analyzed
     */
    int numdecolayertlsnames=0;
    ArrayList <String[]>altlsnames;
    ArrayList<TerrainLayerSetup> altls;
    MapConverter mc;
    /**
     * Num terrain in T3D input file (first terrain data parsed in T3D file -> num = 0)
     */
    int numterrain;
    boolean isvalidterrain = true;

    public Terrain(MapConverter mc, int numterrain) {
        this.numterrain = numterrain;
        this.savpack = mc.getDefaultpackage();
        this.mc = mc;
        this.terfolder = mc.getTerfolder();
        alterlayers = new ArrayList();
        hmqvbitmap = new HashMap();
        aldecolayers = new ArrayList();
        altlsnames=new ArrayList();
        altls = new ArrayList<TerrainLayerSetup>();
        setBShowLog(true);
    }

    public ArrayList <TerrainLayer> getTerrainLayers()
    {
        return this.alterlayers;
    }
    
    public File getHmTiffFile()
    {
        return this.hmtiff_file;
    }
    
    public File getHmBmpFile()
    {
        return this.hmbmpfile;
    }

    public void AnalyseT3DData(String line)
    {
        //    TerrainMap=Texture'AS-Glacier.Terrain.ground1'
        //    TerrainMap=Texture'GraysonT.HeightMaps.M09A_v1a'
        //TODO Handle terrain heightmap in external package (not in myLevel)

        if (line.contains("TerrainMap=Texture")) {
            UTObject uto;
            uto = new UTObject(line.split("\\'")[1]);

            if (this.mc.getInput_utgame() == UTGames.U2) {
                hmbmpfile = new File(terfolder + File.separator + uto.getName() + ".bmp");
                hmtiff_file = new File(terfolder + File.separator + uto.getName() + ".tiff");
            } else if (this.mc.getInput_utgame() == UTGames.UT2004) {
                hmbmpfile = new File(terfolder + File.separator + uto.getGroupAndName() + ".bmp");
                hmtiff_file = new File(terfolder + File.separator + uto.getGroupAndName() + ".tiff");
            }

            TerBmpToTiff tbt = new TerBmpToTiff(hmbmpfile, terfolder);
            tbt.ExportToTiff();
        }
        //Unreal 2: "	(TAB)Layers(0)=(Texture=T ..."
        else if((line.contains(" Layer")||line.contains("\tLayers"))&&line.contains("AlphaMap="))
        {
            TerrainLayer tl = new TerrainLayer(line, this.mc);
            alterlayers.add(tl);
        }
        //    DecoLayers(0)=(ShowOnTerrain=1,DensityMap=Texture'ONS-Dria.Decolyaeralphalised1',StaticMesh=StaticMesh'cp_wasteland_mesh.DecoLayers.cp_rubble1_deco',
        else if(line.contains("DecoLayers")&&line.contains("DensityMap="))
        {
            DecoLayer dl = new DecoLayer(line,"TLSDecoL"+numdecolayertlsnames,mc);
            numdecolayertlsnames ++;
            aldecolayers.add(dl);
        }
        else if(line.contains("QuadVisibilityBitmap"))
        {
            //    QuadVisibilityBitmap(0)=-2147483648
            String num=(line.split("\\(")[1]).split("\\)")[0];
            hmqvbitmap.put(num, line.split("=")[1]);
        }
        else if(line.contains(" Location"))
        {
            loc = new Location(line);
            otherdata += line+"\n";
        }
        /*
        else if(line.contains("DrawScale"))
        {
            otherdata += line+"\n";
        }*/
        
        else if(line.contains("TerrainScale="))
        {
            drawscale3dstr = TerScale2DrawScale3D(line);
        }
        /*
        else if(line.contains("Rotation"))
        {
            otherdata += line+"\n";
        }*/
        else if(line.contains("ColLocation"))
        {
            otherdata += line+"\n";
        }
        else if(line.contains("PhysicsVolume"))
        {
            otherdata += line+"\n";
        }
    }

    /**    TerrainScale=(X=448.000000,Y=448.000000,Z=36.000000)
            Converti en            DrawScale3D=(X=448.000000,Y=448.000000,Z=36.000000)*/
    private String TerScale2DrawScale3D(String tsline)
    {
        String x="";
        String y="";
        String z="";
        String tmp="";
        //            DrawScale3D=(X=(X,Y=448.000000,Z=36.000000)
        //TerrainScale=(X=128.000000,Y=128.000000)
        tsline = removeChar(tsline, 40);
        tsline = removeChar(tsline, 41);
        tmp = tsline.replaceAll("TerrainScale=", "");

        String tmp2[] = tmp.split("\\,");
        if(tmp2.length==3)
        {
            x = tmp2[0].split("\\=")[1];
            y = tmp2[1].split("\\=")[1];
            z = tmp2[2].split("\\=")[1];
            return "            DrawScale3D=(X="+x+",Y="+y+",Z="+z+")\n";
        }
        else if(tmp2.length==2)
        {
            tmp = "";
            x="64";
            y="64";
            z="64";
            if(tmp2[0].contains("X"))
            {
                x = tmp2[0].split("\\=")[1];
            }
            else if(tmp2[0].contains("Y"))
            {
                y = tmp2[0].split("\\=")[1];
            }
            else if(tmp2[0].contains("Z"))
            {
                z = tmp2[0].split("\\=")[1];
            }

            if(tmp2[1].contains("X"))
            {
                x = tmp2[1].split("\\=")[1];
            }
            else if(tmp2[1].contains("Y"))
            {
                y = tmp2[1].split("\\=")[1];
            }
            else if(tmp2[1].contains("Z"))
            {
                z = tmp2[1].split("\\=")[1];
            }
            return "            DrawScale3D=(X="+x+",Y="+y+",Z="+z+")\n";
        }
        
        return "            DrawScale3D=(X="+x+",Y="+y+",Z="+z+")\n";
    }

    private String removeChar(String line,int numchar)
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
        }
        return tmp;
    }
    


    public void toString(BufferedWriter bwr,TerrainTiffReader ttr)
    {
        if(!ttr.hasHeightMapData()){
            setIsvalidterrain(false);
            Main.trayIcon.displayMessage("Terrain data error", "Terrain heighmap image not found!", MessageType.WARNING);
            System.out.println("\tConversion skipped (Error! - No heightmap detected!)");
        } else {
            try {
                String tmp = "";
                bwr.write("      Begin Terrain Class=Terrain Name=Terrain_"+numterrain+"\n");

                bwr.write("         Begin TerrainActor Class=Terrain Name=Terrain_"+numterrain+"\n");
                //addTerrainComponents(bwr, ttr);
                bwr.write("            Begin Object Class=SpriteComponent Name=Sprite ObjName=SpriteComponent_4 Archetype=SpriteComponent\'Engine.Default__Terrain:Sprite\'\n");
                bwr.write("               LightingChannels=(bInitialized=True,Dynamic=True)\n");
                bwr.write("               Name=\"SpriteComponent_4\"\n");
                bwr.write("               ObjectArchetype=SpriteComponent\'Engine.Default__Terrain:Sprite\'\n");
                bwr.write("            End Object\n");
                bwr.write("            Components(0)=SpriteComponent\'SpriteComponent_4\'\n");
                bwr.write("            Tag=\"Terrain"+numterrain+"\"\n");
                bwr.write(otherdata);
                bwr.write(drawscale3dstr);
                bwr.write("            Name=\"Terrain_"+numterrain+"\"\n");
                bwr.write("            ObjectArchetype=Terrain\'Engine.Default__Terrain\'\n");
                bwr.write("         End TerrainActor\n");

                bwr.write("         Begin TerrainActorMembers\n");
                bwr.write("            MaxTesselationLevel=1\n");
                bwr.write("            TesselationDistanceScale=1.000000\n");
                bwr.write("            CollisionTesselationLevel=4\n");
                if(ttr.hasHeightMapData()){
                    bwr.write("            NumPatchesX=" + String.valueOf(ttr.getWidth()) + "\n");
                    bwr.write("            NumPatchesY=" + String.valueOf(ttr.getHeight()) + "\n");
                }
                bwr.write("            MaxComponentSize=16\n");
                bwr.write("            StaticLightingResolution=4\n");
                bwr.write("            bIsOverridingLightResolution=0\n");
                bwr.write("            bCastShadow=1\n");
                bwr.write("            bForceDirectLightMap=1\n");
                bwr.write("            bCastDynamicShadow=1\n");
                bwr.write("            bBlockRigidBody=1\n");
                bwr.write("            bAcceptsDynamicLights=1\n");
                bwr.write("            LightingChannels=5\n");
                bwr.write("         End TerrainActorMembers\n");

                if(ttr.hasHeightMapData()){
                    bwr.write("         Begin TerrainHeight	Exporter=TerrainHeightMapExporterTextT3D\n");
                    bwr.write("            Count=" + ttr.getCountUT3() + "	Width=" + ttr.getWidthUT3() + "	Height=" + ttr.getHeightUT3() + "\n");
                    addHeightMap(bwr, ttr);
                    bwr.write("         End TerrainHeight\n");
                    addTerrainInfoData(bwr, ttr); //Visibility bitmap
                }

                addTerrainLayerData(bwr);
                //if(aldecolayers.size()>0){addTerrainDecoLayerData(bwr);}

                addTerrainAlphaMapData(bwr);
                bwr.write("      End Terrain\n");
            } catch (IOException ex) {
                Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void addHeightMap(BufferedWriter bwr,TerrainTiffReader ttr) throws IOException
    {
        String tmp="";

        ArrayList<String> alhmdata = ttr.getHeightMapData();

        for(int i=0;i<alhmdata.size();i++)
        {
            if((ismultiple(i+1, 8))||(i==(alhmdata.size()-1)))
            {
                bwr.write(tmp+alhmdata.get(i).toString()+"\n");
                tmp = "";
            }
            else
            {
                tmp += alhmdata.get(i).toString()+"\t";
            }
        }
    }

    /**
     * Visibility terrain info. Set which terrain squares are rendered.
     * @param bwr
     * @param ttr
     * @throws IOException
     */
    public void addTerrainInfoData(BufferedWriter bwr,TerrainTiffReader ttr) throws IOException
    {
        int numline = (int)(ttr.getRealCount()/32F);
        long value=0;
        int numvalbinary=0;
        int numadded=0;
        String tmp="";
        String visstr="";
        ArrayList<Integer> alvismap=new ArrayList<Integer>();


        bwr.write("         Begin TerrainInfoData\n");
        bwr.write("            Count="+ttr.getCountUT3()+"\n");


        for(int i=0;i<numline;i++)
        {
            if(hmqvbitmap.containsKey(String.valueOf(i)))
            {
                value=Integer.valueOf(hmqvbitmap.get(String.valueOf(i)).toString());
                //All squares are rendered
                //Write 32 zeroes 0 0 0 0 0 0 ...
                if(value==-1)
                {
                    for (int j = 0; j < 32; j++) {
                        alvismap.add(0);
                        numadded++;
                        if(ismultiple(numadded, ttr.getWidth()))
                        {
                            alvismap.add(0);
                        }
                    }
                }
                else
                {
                    if(value<0){value++;}
                    visstr = getVisStringValues(value);
                    //1073150975-> 11111 11111 01111 10110 11111 11111 00(real)-> 11111 11111 01101 11110 11111 11111 (00)(calc)

                    tmp="";
                    visstr = visstr.replaceAll("-","");
                    for(int k=0;k<(32-visstr.length());k++)
                    {
                        tmp +="0";
                    }
                    visstr = tmp+visstr;


                    //System.out.println(value+"->"+visstr+" "+visstr.length());

                    //for(int j=(visstr.length()-1);j>=0;j--)
                    if(value>0)
                    {
                        //for(int j=0;j<visstr.length();j++)
                        for(int j=(visstr.length()-1);j>=0;j--)
                        {
                            numvalbinary = visstr.charAt(j);
                            //Values are inverted from UT2k4 to UT3 0(48)->1; 1(49)->0
                            if(numvalbinary==48)
                            {
                                alvismap.add(1);
                                numadded ++;
                                if(ismultiple(numadded, ttr.getWidth()))
                                {
                                    alvismap.add(0);
                                }
                            }
                            else
                            {
                                alvismap.add(0);
                                numadded ++;
                                if(ismultiple(numadded, ttr.getWidth()))
                                {
                                    alvismap.add(0);
                                }
                            }
                        }
                    }
                    else if(value<0)
                    {
                        for(int j=(visstr.length()-1);j>=0;j--)
                        {
                            numvalbinary = visstr.charAt(j);
                            //Values are inverted from UT2k4 to UT3 0(48)->1; 1(49)->0
                            if(numvalbinary==48)
                            {
                                alvismap.add(0);
                                numadded ++;
                                if(ismultiple(numadded, ttr.getWidth()))
                                {
                                    alvismap.add(0);
                                }
                            }
                            else
                            {
                                alvismap.add(1);
                                numadded ++;
                                if(ismultiple(numadded, ttr.getWidth()))
                                {
                                    alvismap.add(0);
                                }
                            }
                        }
                    }

                }
            }
            else    //Write 32 ones 1 1 1 1 1 1 ...
            {
                for (int j = 0; j < 32; j++) {
                    alvismap.add(1);
                    numadded ++;
                    if(ismultiple(numadded, ttr.getWidth()))
                    {
                        alvismap.add(0);
                    }
                    }
            }
        }

        //Adds extra line
        for(int x=0;x<(ttr.getWidth()+1);x++)
        {
                alvismap.add(0);
        }
        tmp ="";

        for (int i = 0; i < alvismap.size(); i++) {
            if((ismultiple(i+1, 8))||(i==(alvismap.size()-1)))
            {
                bwr.write(tmp+alvismap.get(i)+"\n");
                tmp = new String("");
            }
            else
            {
                tmp += alvismap.get(i)+"\t";
            }
        }

        bwr.write("         End TerrainInfoData\n");

    }

    private void addTLSSetup(String tlsname,String texture)
    {
        this.altlsnames.add(new String[]{tlsname,texture});
    }

    public ArrayList getAltlsnames() {
        return altlsnames;
    }

    private void addTerrainDecoLayerData(BufferedWriter bwr) throws IOException
    {
        DecoLayer dl=null;

        bwr.write("         Begin TerrainDecoLayerData\n");
        bwr.write("            Count="+aldecolayers.size()+"\n");
        
        for(int i=0;i<aldecolayers.size();i++)
        {
            dl = aldecolayers.get(i);
        bwr.write("            Begin TerrainDecoLayer Index="+i+" Name=T"+numterrain+"_TDL"+i+"\n");
        bwr.write("               DecorationCount=1\n");
        bwr.write("               Begin TerrainDecoration Index=0\n");
        bwr.write("                  Begin Object Class=StaticMeshComponentFactory Name=StaticMeshComponentFactory_3 ObjName=StaticMeshComponentFactory_3 Archetype=StaticMeshComponentFactory\'Engine.Default__StaticMeshComponentFactory\'\n");
        bwr.write("                     StaticMesh=StaticMesh\'"+dl.getStaticmeshUT3(savpack)+"\'\n");
        bwr.write("                     Name=\"StaticMeshComponentFactory_3\"\n");
        bwr.write("                     ObjectArchetype=StaticMeshComponentFactory\'Engine.Default__StaticMeshComponentFactory\'\n");
        bwr.write("                  End Object\n");
        bwr.write("                  MinScale="+dl.getMinscale()+"\n");
        bwr.write("                  MaxScale="+dl.getMaxscale()+"\n");
        bwr.write("                  Density="+dl.getDensity()+"\n");
        bwr.write("                  SlopeRotationBlend=0.000000\n");
        bwr.write("                  RandSeed=7\n");
        //Apparently not needed
        /*
        bwr.write("                  Begin TerrainDecorationInstanceArray Count=1\n");
        bwr.write("                     Begin TerrainDecorationInstance Index=0\n");
        bwr.write("                        Begin Object Class=StaticMeshComponent Name=StaticMeshComponent_7321 ObjName=StaticMeshComponent_7321 Archetype=StaticMeshComponent\'Engine.Default__StaticMeshComponent\'\n");
        bwr.write("                           StaticMesh=StaticMesh\'"+dl.getStaticmesh()+"\'\n");
        bwr.write("                           LightingChannels=(bInitialized=True,Static=True)\n");
        bwr.write("                           Scale3D=(X=0.003876,Y=0.003876,Z=0.003876)\n");
        bwr.write("                           Name=\"StaticMeshComponent_7321\"\n");
        bwr.write("                           ObjectArchetype=StaticMeshComponent\'Engine.Default__StaticMeshComponent\'\n");
        bwr.write("                        End Object\n");
        bwr.write("                        X=1.589570\n");
        bwr.write("                        Y=2.177422\n");
        bwr.write("                        Scale=0.992229\n");
        bwr.write("                        Yaw=19383\n");
        bwr.write("                     End TerrainDecorationInstance\n");
        bwr.write("                  End TerrainDecorationInstanceArray\n");
        */
        bwr.write("               End TerrainDecoration\n");
        bwr.write("               AlphaMapIndex="+(alterlayers.size()+i)+"\n");
        bwr.write("            End TerrainDecoLayer\n");
        bwr.write("         End TerrainDecoLayerData\n");
        }
    }

    /**
     * Adds Terrain Alpha map data (for texture layers and deco/foliage)
     * @param bwr
     * @throws IOException
     */
    private void addTerrainAlphaMapData(BufferedWriter bwr) throws IOException
    {
        TerrainLayer tl=null;
        DecoLayer dl=null;
        bwr.write("         Begin TerrainAlphaMapData\n");
        bwr.write("            Count="+(alterlayers.size()+aldecolayers.size())+"\n");

        for(int i=0;i<alterlayers.size();i++)
        {
            tl = (TerrainLayer)alterlayers.get(i);
            tl.writeTerrainLayer(bwr, tl.getAlphaMapTgaFile(),i);
        }

        for(int i=0;i<aldecolayers.size();i++)
        {
            dl = aldecolayers.get(i);
            dl.writeDecoLayerAlphaValues(bwr, dl.getAlphaTgaFile(),(alterlayers.size()+i));
        }
        bwr.write("         End TerrainAlphaMapData\n");
    }

    private void addTerrainLayerData(BufferedWriter bwr) throws IOException
    {
        TerrainLayer tl=null;
        DecoLayer dl=null;
        int alphamapindex=0;
        bwr.write("         Begin TerrainLayerData\n");
        bwr.write("            Count="+(alterlayers.size()+aldecolayers.size())+"\n");
        
        for(int i=0;i<alterlayers.size();i++)
        {
            tl = (TerrainLayer)alterlayers.get(i);
            this.addTLSSetup(tl.getTLSName(), tl.getTLSTexName());
        bwr.write("            Begin TerrainLayer Index="+i+" Name="+tl.getTLSName()+"\n");
        bwr.write("               Begin TerrainLayerSetup Name="+savpack+"."+tl.getTLSName()+"\n");
        bwr.write("               End TerrainLayerSetup\n");
            if(tl.almapexist())
            {
        bwr.write("               AlphaMapIndex="+alphamapindex+"\n");
                alphamapindex ++;
                //if(tl.getAlphaMapBmpFile().exists())
                //{
                  //  tl.getAlphaMapBmpFile().delete();
                //}
                //TextureConverter tc = new TextureConverter(tl.getAlphaMapTgaFile(), "bmp",0); //0= files mode
                //tc.useMergeAlpha();
                //tc.convertAll();
            }
            else
            {
        bwr.write("               AlphaMapIndex=-1\n");
            }
        bwr.write("               Highlighted=0\n");
        bwr.write("               Hidden=0\n");
        bwr.write("            End TerrainLayer\n");
        }
        if(aldecolayers.size()>0)
        {
            for(int i=0;i<aldecolayers.size();i++)
            {
                dl = (DecoLayer)aldecolayers.get(i);
                this.addTLSSetup(dl.getTLSName(), tl.getTLSTexName());
            bwr.write("            Begin TerrainLayer Index="+alphamapindex+" Name="+dl.getTls().getTlsname()+"\n");
            bwr.write("               Begin TerrainLayerSetup Name="+savpack+"."+dl.getTls().getTlsname()+"\n");
            bwr.write("               End TerrainLayerSetup\n");
                if(dl.almapexist())
                {
            bwr.write("               AlphaMapIndex="+alphamapindex+"\n");
                    alphamapindex ++;
                    //if(tl.getAlphaMapBmpFile().exists())
                    //{
                      //  tl.getAlphaMapBmpFile().delete();
                    //}
                    //TextureConverter tc = new TextureConverter(tl.getAlphaMapTgaFile(), "bmp",0); //0= files mode
                    //tc.useMergeAlpha();
                    //tc.convertAll();
                }
                else
                {
            bwr.write("               AlphaMapIndex=-1\n");
                }
            bwr.write("               Highlighted=0\n");
            bwr.write("               Hidden=0\n");
            bwr.write("            End TerrainLayer\n");
            }
        }
        bwr.write("         End TerrainLayerData\n");
        
    }

    /**
     * For decoration? (staticmeshes?)
     * @param bwr
     * @param ttr
     * @throws IOException
     */
    public void addTerrainComponents(BufferedWriter bwr,TerrainTiffReader ttr) throws IOException
    {
        int numtercomp = (int)((ttr.getHeight()/64F)*(ttr.getWidth()/64F));
        int numbasex=0;
        int numbasey=0;
        int counter=1;

        for(int x=0;x<(int)(ttr.getWidth()/64F);x++)
        {
            for(int y=0;y<(int)(ttr.getHeight()/64F);y++)
            {
                numbasex = x*64;
                numbasey= y*64;
                bwr.write("            Begin Object Class=TerrainComponent Name=TerrainComponent_"+counter+" ObjName=TerrainComponent_"+counter+" Archetype=TerrainComponent\'Engine.Default__TerrainComponent\'\n");
                if(x!=0)
                {
                    bwr.write("               SectionBaseX="+String.valueOf(numbasex)+"\n");
                }
                if(y!=0)
                {
                    bwr.write("               SectionBaseY="+String.valueOf(numbasey)+"\n");
                }
                bwr.write("               SectionSizeX=16\n");
                bwr.write("               SectionSizeY=16\n");
                bwr.write("               TrueSectionSizeX=64\n");
                bwr.write("               TrueSectionSizeY=64\n");
                bwr.write("               bForceDirectLightMap=True\n");
                bwr.write("               LightingChannels=(bInitialized=True,Static=True)\n");
                bwr.write("               Name=\"TerrainComponent_"+counter+"\"\n");
                bwr.write("               ObjectArchetype=TerrainComponent\'Engine.Default__TerrainComponent\'\n");
                bwr.write("            End Object\n");
                counter++;
            }
        }
        
    }


    

    
    /** */
    private String getVisStringValues(long value)
    {
        return Long.toString(value, 2);
    }
    
    private boolean ismultiple(int a, int b)
    {
        if (a%b==0){return true;}else{ return false;}
    }

    public ArrayList<DecoLayer> getAldecolayers() {
        return aldecolayers;
    }

    public boolean isIsvalidterrain() {
        return isvalidterrain;
    }

    public void setIsvalidterrain(boolean isvalidterrain) {
        this.isvalidterrain = isvalidterrain;
    }

    

}
