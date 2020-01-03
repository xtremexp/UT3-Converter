/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ut3converter2.Main;
import ut3converter2.UTGames;
import ut3converter2.UTObject;
import ut3converter2.convert.MapConverter;
import ut3converter2.convert.Texture.TextureReplacer;
import ut3converter2.T3DConvertor;

/**
 * Updates UV values of brushes in T3D level files depending
 * on size of texture.
 * @author Hyperion
 */
public class T3DUVTexUpdater extends T3DConvertor{

    /**
     * 256 for UT->UT3 and 512 for UT2004->UT3
     */
    Double reftexsize=512D;
    public static final double reftex_utut3=512D;
    public static final double reftex_ut2k4ut3=512D;
    BufferedReader bfr;
    BufferedWriter bwr;
    File t3dinputfile;
    File t3doutputfile;
    TextureReplacer tr;
    /**
     * Where to find extracted textures used in level
     */
    File texfolder;

    /**
     * e.g: ".bmp,.pcx,..."
     */
    String[] texextension;

    public T3DUVTexUpdater(MapConverter mc,File t3doutputfile,String[] texextension){
        this.t3dinputfile = mc.getT3dlvlfile();
        this.t3doutputfile = t3doutputfile;
        this.texfolder = mc.getTexfolder();
        this.texextension = texextension;
        if(mc.getInput_utgame()==UTGames.UT99)
        {
            reftexsize = reftex_utut3;
        }
        if(mc.getInput_utgame()==UTGames.UT2004||mc.getInput_utgame()==UTGames.U2)
        {
            reftexsize = reftex_ut2k4ut3;
        }
    }
    
    public T3DUVTexUpdater(File t3dinputfile, File t3doutputfile,String[] texextension,MapConverter mc) {
        this.t3dinputfile = t3dinputfile;
        this.t3doutputfile = t3doutputfile;
        this.texfolder = mc.getTexfolder();
        this.texextension = texextension;
        if(mc.getInput_utgame()==UTGames.UT99)
        {
            reftexsize = reftex_utut3;
        }
        if(mc.getInput_utgame()==UTGames.UT2004||mc.getInput_utgame()==UTGames.U2)
        {
            reftexsize = reftex_ut2k4ut3;
        }
    }

    public void setRefTexSize(double d)
    {
        this.reftexsize = d;
    }
    
    public void convert()
    {
        if(super.isBShowLog())
        {
            System.out.print("\n*** Converting UT99/2k4 UV texture data to UT3");
        }
        
        int linenumber=0;
        try {
            bfr = new BufferedReader(new FileReader(t3dinputfile));
            bwr = new BufferedWriter(new FileWriter(t3doutputfile));

            while((line=bfr.readLine())!=null)
            {
                try {
                    AnalyzeLine(line);
                    linenumber ++;
                } catch (Exception e) {
                    System.out.println("Error while parsing data from T3D Level File "+t3dinputfile.getName());
                    System.out.println("Line #"+linenumber+" Original Line String:");
                    System.out.println("\""+line+"\"");
                    e.printStackTrace();
                    //System.exit(9);
                }
                
            }
            bfr.close();
            bwr.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(T3DUVTexUpdater.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(T3DUVTexUpdater.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(super.isBShowLog()){System.out.println(" ... done! ***");}
    }

    String curclass = "";
        String line;
        String texname=null;
        double dpan[] = new double[2];
        Dimension texdim=null;
        File ftex;
        boolean skipupdate=false;
        boolean bhaspan=false;

    private void AnalyzeLine(String line) throws IOException
    {
        if(line.contains("Begin Actor"))
                {
                    curclass = getActorClass(line);
                    bwr.write(line+"\n");
                }
                else if(line.contains("End Actor"))
                {
                    bwr.write(line+"\n");
                }
                else
                {
                    if(curclass.equals("Brush")||curclass.equals("Mover"))
                    {
                        if(line.contains("Texture="))
                        {
                            //          Begin Polygon Item=Outer Texture=inxmars0 Flags=1605900 Link=12
                            //Begin Polygon Item=OUTSIDE Texture=Outwal2 Flags=32 Link=2
                            //Begin Polygon Item=OUTSIDE Texture=Outwal2 Link=3
                            //Begin Polygon Texture=HumanoidArchitecture.Floors.flr03HA Link=1
                            if(line.contains("Flags"))
                            {
                                texname = (line.split("Texture=")[1]).split(" Flags=")[0];
                            }
                            else
                            {
                                texname = (line.split("Texture=")[1]).split(" Link=")[0];
                            }

                            ftex = getTexFile(texname);
                            if(ftex!=null)
                            {
                                if(ftex.exists())
                                {
                                    texdim = TextureInfo.getTexSize(ftex);
                                } else {
                                    System.out.println(ftex);
                                }
                            }
                            else{
                                skipupdate=true;
                                //Main.config.getMclog().warning("No tex file found at: "+ftex.getAbsolutePath());
                            }

                            //skipupdate=true;
                            bwr.write(line+"\n");
                        }
                        else if(line.contains("TextureU"))
                        {
                            if((texname!=null)&&(skipupdate==false))
                            {
                                bwr.write(getUpdatedUValue(line, texdim)+"\n");
                            }
                            else{bwr.write(line+"\n");}
                        }
                        else if(line.contains("TextureV"))
                        {
                           if((texname!=null)&&(skipupdate==false)){ bwr.write(getUpdatedVValue(line, texdim)+"\n");}
                           else{bwr.write(line+"\n");}
                           if((texdim!=null)&&(bhaspan==true))
                           {
                               dpan[0] *= reftexsize/texdim.getWidth();
                               dpan[1] *= reftexsize/texdim.getHeight();
                               bwr.write("             Pan      U="+dpan[0]+" V="+dpan[1]+"\n");
                               bhaspan=false;
                           }


                        }
                        else if(line.contains(" Pan"))
                        {
                            bhaspan=true;
                            dpan = getPan(line);
                        }
                        else if(line.contains("End Polygon")){bwr.write(line+"\n");texname=null;skipupdate=false;}
                        else
                        {
                            bwr.write(line+"\n");
                        }
                    }
                    else{bwr.write(line+"\n");}
                }
    }

    /**
     * 
     * @param line T3DLine of polygon of brush
     * @param texdim Dimensions of current texture being applied to polygon
     * @return Updated U value
     */
    double dtexu[];
    
    private String getUpdatedUValue(String line,Dimension texdim)
    {
        //Double factor = 256D/max;
        Double factor = reftexsize/texdim.getWidth(); //=256/128=2

        //                  TextureU -00000.221152,+00000.021782,+00000.000000
        //->                TextureU -00000.442305,+00000.043563,+00000.000000
        //128x256
        if(texdim.getHeight()==texdim.getWidth())
        {
            factor = reftexsize/texdim.getHeight();
        }
        dtexu = getTextureU(line);

        dtexu[0]*=factor;
        dtexu[1]*=factor;
        dtexu[2]*=factor;

        
        return "             TextureU   "+dtexu[0]+","+dtexu[1]+","+dtexu[2];
    }

    private String getUpdatedVValue(String line,Dimension texdim)
    {
        //Double factor = 256D/max;
        Double factor = reftexsize/texdim.getHeight();
        if(texdim.getHeight()==texdim.getWidth())
        {
            factor = reftexsize/texdim.getHeight();
        }
        dtexv = getTextureV(line);

        dtexv[0]*=factor;
        dtexv[1]*=factor;
        dtexv[2]*=factor;

        return "             TextureV   "+dtexv[0]+","+dtexv[1]+","+dtexv[2];
    }

            /**
     *              TextureU +00000.316228,-00000.948683,+00000.000000
     * @param line
     */
    private double[] getTextureU(String line)
    {
        double d[]=new double[3];
        String tmp=line.split("TextureU")[1];
        String tmp2[] = tmp.split("\\,");
        d[0]= Double.valueOf(tmp2[0]);
        d[1]= Double.valueOf(tmp2[1]);
        d[2]= Double.valueOf(tmp2[2]);
        return d;
    }

        /**
     *              TextureU +00000.316228,-00000.948683,+00000.000000
     * @param line
     */
    double dtexv[];
    private double[] getTextureV(String line)
    {
        double d[]=new double[3];
        String tmp=line.split("TextureV")[1];
        String tmp2[] = tmp.split("\\,");
        d[0]= Double.valueOf(tmp2[0]);
        d[1]= Double.valueOf(tmp2[1]);
        d[2]= Double.valueOf(tmp2[2]);
        return d;
    }

        /**
     * Pan      U=10 V=61
     * @param line
     * @return
     */
    private double[] getPan(String line)
    {
        double d[]=new double[3];

        String tmp =line.split("U=")[1];


        d[0] = Double.valueOf(tmp.split("\\ ")[0]);
        d[1] = Double.valueOf(tmp.split("V=")[1]);

        return d;
    }

    /**
     * Get filename from texname.
     * Checks if it has a texturereplacement.
     * @param texname
     * @return
     */
    private File getTexFile(String texname)
    {
        File f;
        UTObject uto;
        //Updates texnames for UT2004 exported tex
        //HumanoidArchitecture2.Borders.bdr02bHA
        //->Borders.bdr02bHA (.dds)
        String oldtex=texname;
        if(true)
        {
            uto = new UTObject(texname);
            texname = uto.getGroupAndName();
            if(tr!=null){
                //"Walls.wall1shader" --TR--> "dm-1on1-aerowalk.walls.ex_wall_01_d" --UTObject--> "walls.ex_wall_01_d"
                //tr.hasTextureReplacement(Group.Name)=NewPac.NewGroup.NewName
                if(tr.hasTextureReplacement(texname)){
                    texname = new UTObject(tr.getTextureReplacement(texname)).getGroupAndName();
                }
            }
        }

        for(int i=0;i<texextension.length;i++)
        {
            f = new File(this.texfolder.getAbsolutePath()+File.separator+texname+texextension[i]);

            if(f.exists())
            {
                if(f.length()!=0L)
                {
                    return f;
                }
            }
        }
        return null;
    }

    private String getActorClass(String line)
    {
        return (line.split("=")[1]).split(" ")[0];
    }

    public TextureReplacer getTr() {
        return tr;
    }

    public void setTr(TextureReplacer tr) {
        this.tr = tr;
    }

    
    
}
