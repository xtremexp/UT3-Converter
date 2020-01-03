/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.vecmath.Vector3d;
import ut3converter2.T3DConvertor;

/**
 * Make the transform permanently task also available from Unreal Editor.
 * Used for U1 and UT maps.
 * Allows to scale up/down brushes as well
 *
 * @author Hyperion
 */
public class T3DBrushTranformPerm extends T3DConvertor{

    /**
     * UT/U1 T3D Map file
     */
    String tskname="\n*** Transforming permanently T3D Level brushes ***";
    File t3dfile;
    File outputfile;

    double scalefactorr = 1D;
    double pitch=0D;
    double yaw=0D;
    double roll=0D;
    double ms_scalex=1D;
    double ms_scaley=1D;
    double ms_scalez=1D;
    double ps_scalex=1D;
    double ps_scaley=1D;
    double ps_scalez=1D;
    /**
     * For testing purpose only
     */
    String numbrush="Brush463";

    public String tmp="";

    public T3DBrushTranformPerm(File t3dfile,File outputfile) {
        this.t3dfile = t3dfile;
        this.outputfile = outputfile;
        super.setTaskname(tskname);
    }

    public void convert() throws IOException
    {
        if(super.isBShowLog())
        {
            System.out.println(super.getTaskname());
            System.out.println("Scale factor: "+scalefactorr);
            System.out.print(t3dfile.getName()+"->"+outputfile.getName());
        }


        //BufferedReader bfr = FileDecoder.getBufferedReader(t3dfile, "Begin");
        BufferedReader bfr = new BufferedReader(new FileReader(t3dfile));
        BufferedWriter bwr = new BufferedWriter(new FileWriter(outputfile));
        
        String line="";
        double dlocation[]=new double[3];
        double dorigin[];
        double dprepivot[];
        double dpan[] = new double[2];
        double dtexu[];
        double dtexv[] = null;
        Vector3d vtmp = new Vector3d();
        String curclass="";
        boolean isbrush=false;
        boolean ispolygon=false;
        boolean hasrotation=false;
        ArrayList alvertex = new ArrayList();
        Vector3d v3d;
        Polygon pol = new Polygon();
        TextureAlign ta = new TextureAlign();
        double newvec[];
        tmp="";

        while((line=bfr.readLine())!=null)
        {
            if(isbrush)
            {
                if(line.contains("End Actor"))
                {
                    hasrotation=false;
                    ps_scalex = 1D;
                    ps_scaley = 1D;
                    ps_scalez = 1D;
                    ms_scalex = 1D;
                    ms_scaley = 1D;
                    ms_scalez = 1D;
                    pitch=0D;
                    yaw=0D;
                    roll=0D;
                    dpan[0]=0D;
                    dpan[1]=0D;
                    
                    pol = new Polygon();
                    ta = new TextureAlign();
                    ta.reset();
                    isbrush = false;
                    bwr.write(line+"\n");
                }
                //    MainScale=(Scale=(Y=-1.000000),SheerAxis=SHEER_ZX)
                //PostScale=(Scale=(X=50.000000,Y=73.000000),SheerAxis=SHEER_ZX)
                else if(line.contains("MainScale="))
                {
                    if(line.split("\\=")[1].contains("Scale"))
                    {
                        getMS_Scale(line);
                        /*
                        if(curclass.toLowerCase().contains("mover"))
                        {
                            bwr.write("    SomeScale="+this.ms_scalex+";"+this.ms_scaley+";"+this.ms_scalez+"\n");
                            this.ms_scalex = 1D;
                            this.ms_scaley = 1D;
                            this.ms_scalez = 1D;
                        }*/
                        //else
                        //{
                            pol.setMs_scalex(this.ms_scalex);
                            pol.setMs_scaley(this.ms_scaley);
                            pol.setMs_scalez(this.ms_scalez);
                            ta.setMs_x(this.ms_scalex);
                            ta.setMs_y(this.ms_scaley);
                            ta.setMs_z(this.ms_scalez);
                        //}
                    }
                }
                else if(line.contains(" Location="))
                {
                    dlocation = getLocation(line);
                    Geom.scale(dlocation, scalefactorr);
                    dlocation = toZeroValues(dlocation);
                    bwr.write("     Location=(X="+dlocation[0]+",Y="+dlocation[1]+",Z="+dlocation[2]+")\n");
                }
                else if(line.contains("PostScale="))
                {
                    if(line.split("\\=")[1].contains("Scale"))
                    {
                        getScale(line);
                        ta.setPs_x(this.ps_scalex);
                        ta.setPs_y(this.ps_scaley);
                        ta.setPs_z(this.ps_scalez);
                        pol.setScalex(this.ps_scalex);
                        pol.setScaley(this.ps_scaley);
                        pol.setScalez(this.ps_scalez);
                    }
                        //bwr.write("    MainScale=(SheerAxis=SHEER_ZX)\n");
                }
                else if(line.contains("Vertex"))
                {
                    if(!hasrotation)
                    {
                        v3d = getVector(line);
                        pol.addVertex(v3d.getX()*ps_scalex*scalefactorr*ms_scalex, v3d.getY()*ps_scaley*scalefactorr*ms_scaley, v3d.getZ()*ps_scalez*scalefactorr*ms_scalez);
                    }
                    else
                    {
                        v3d = getVector(line);
                        v3d = lowValuesZero(v3d);
                        v3d = updateMSVertex(v3d); //MainScale
                        newvec = Geom.getVecRotate(v3d, pitch, yaw, roll);
                        pol.addVertex(newvec[0]*ps_scalex*scalefactorr, newvec[1]*ps_scaley*scalefactorr, newvec[2]*ps_scalez*scalefactorr);
                    }
                }
                else if(line.contains("Begin Polygon"))
                {
                    ispolygon = true;
                    pol.setScalefactorr(scalefactorr);
                    ta.setScalefactorr(scalefactorr);
                    bwr.write(line+"\n");
                    pol.clearVertex();
                }
                else if(line.contains("TextureU"))
                {
                    dtexu = getTextureU(line);
                    
                    vtmp = new Vector3d(dtexu[0], dtexu[1], dtexu[2]);
                    
                    //updateDoublePSDiv(dtexu);
                    ta.setTexu(dtexu);
                }
                else if(line.contains("TextureV"))
                {
                    dtexv = getTextureV(line);
                    vtmp = new Vector3d(dtexv[0], dtexv[1], dtexv[2]);
                    ta.setTexv(dtexv);
                }
                else if(line.contains("Pan "))
                {
                    dpan = getPan(line);
                    ta.setPanu((int)dpan[0]);
                    ta.setPanv((int)dpan[1]);
                }
                else if(line.contains("Rotation="))
                {
                    getRotation(line);
                    pol.setRotation(new double[]{pitch,yaw,roll});
                    hasrotation = true;
                }
                else if(line.contains("End Polygon"))
                {
                    if(ms_scalex*ms_scaley*ms_scalez<0)
                    {
                        pol.setOrder(true);
                    }
                    else
                    {
                        pol.setOrder(false);
                    }
                    pol.setTa(ta);

                    pol.writePolygon(bwr);
                    bwr.write("          End Polygon\n");
                    alvertex.clear();
                    ta.resetPan();
                    pol.ta.resetPan();
                    ispolygon = false;
                }
                else if(line.contains("PrePivot="))
                {
                    dprepivot = getPrepivot(line);
                    Geom.scale(dprepivot, scalefactorr);
                    dprepivot = updateDouble(dprepivot);

                    dprepivot = Geom.getVecRotate(new Vector3d(dprepivot[0], dprepivot[1],dprepivot[2]), pitch, yaw, roll);

                    bwr.write("    PrePivot=(X="+dprepivot[0]*ps_scalex+",Y="+dprepivot[1]*ps_scaley+",Z="+dprepivot[2]*ps_scalez+")\n");
                }
                else if(line.contains("Origin"))
                {
                    dorigin = getOrigin(line);
                    //System.out.println("Origin In: "+dorigin[0]+" "+dorigin[1]+" "+dorigin[2]);
                    ta.setDorigin(dorigin);
                    //dorigin = Geom.getVecRotate(new Vector3d(dorigin[0], dorigin[1],dorigin[2]), pitch, yaw, roll);
                    pol.setOrigin(dorigin);
                    //dorigin = getOrigin(line);
                    //bwr.write("             Origin   "+dorigin[0]*ps_scalex+","+dorigin[1]*ps_scaley+","+dorigin[2]*ps_scalez+"\n");
                }
                else if(line.contains("Normal"))
                {
                    pol.setNormal(getNormal(line));
                }
                else
                {
                    if(!ispolygon)
                    {
                        bwr.write(line+"\n");
                    }
                    else
                    {
                        pol.addExtraData(line);
                    }

                }
            }
            else if(line.contains("Begin Actor"))
            {
                curclass = getActorClass(line);
                //tmp = line;
                if(curclass.equals("Brush")||curclass.toLowerCase().contains("mover"))
                {
                    bwr.write(line+"\n");
                    isbrush=true;
                }
                else
                {
                    bwr.write(line+"\n");
                    isbrush=false;
                }
                
            }
            else
            {
                //tmp += line+"\n";
                bwr.write(line+"\n");
            }
            
        }

        bfr.close();
        bwr.close();
        if(super.isBShowLog()){System.out.println(" ... done!");}
    }

    private String getActorClass(String line)
    {
        return (line.split("=")[1]).split(" ")[0];
    }

    /**
     * Changes very low values near to zero, to zero
     * Avoid some import errors in UT3 editor
     * e.g.:     Location=(X=160.0,Y=-9.999999999999999E-6,Z=320.0)
     * ->fail import of data location
     * @param d
     * @return
     */
    private double[] toZeroValues(double d[])
    {
        for(int i=0;i<d.length;i++)
        {
            if(Math.abs(d[i])<0.0001){d[i]=0D;}
        }
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

    private double[] updateDouble(double d[])
    {
        d[0] = d[0]*ms_scalex;
        d[1] = d[1]*ms_scaley;
        d[2] = d[2]*ms_scalez;
        return new double[]{d[0],d[1],d[2]};
    }

    private double[] updateDoublePSDiv(double d[])
    {
        d[0] = d[0]/ps_scalex;
        d[1] = d[1]/ps_scaley;
        d[2] = d[2]/ps_scalez;
        return new double[]{d[0],d[1],d[2]};
    }

    private double[] updateDoubleDiv(double d[])
    {
        d[0] = d[0]/(ms_scalex);
        d[1] = d[1]/(ms_scaley);
        d[2] = d[2]/(ms_scalez);
        return new double[]{d[0],d[1],d[2]};
    }

    private Vector3d lowValuesZero(Vector3d v3d)
    {
        if(Math.abs(v3d.getX())<0.01){v3d.setX(0D);}
        if(Math.abs(v3d.getY())<0.01){v3d.setY(0D);}
        if(Math.abs(v3d.getZ())<0.01){v3d.setZ(0D);}

        return v3d;
    }

    private Vector3d updateMSVertex(Vector3d v3d)
    {
        v3d.setX(v3d.getX()*ms_scalex);
        v3d.setY(v3d.getY()*ms_scaley);
        v3d.setZ(v3d.getZ()*ms_scalez);
        return v3d;
    }

    private double[] getLocation(String line)
    {
        double d[] = new double[3];
        d[0] =0D;
        d[1] =0D;
        d[2] =0D;
        // Location=(X=5632.000000,Z=384.000000)
        //    Location=(X=3864.000000,Y=-5920.000000,Z=-15776.000000)
        line = line.substring(line.indexOf("(")+1);
        line = line.replaceAll("\\)","");

        String fields[] = line.split(",");
        if(fields.length==3)
        {
            d[0] = Double.valueOf(fields[0].split("\\=")[1]);
            d[1] = Double.valueOf(fields[1].split("\\=")[1]);
            d[2] = Double.valueOf(fields[2].split("\\=")[1]);
        }
        else if(fields.length==2)
        {
            if(line.contains("X")&&line.contains("Y"))
            {
                    d[0] = Double.valueOf(fields[0].split("\\=")[1]);
                    d[1] = Double.valueOf(fields[1].split("\\=")[1]);
                    d[2] = 0D;
            }
            //    Location=(X=1280.000000,Y=2944.000000)
            else if(line.contains("X")&&line.contains("Z"))
            {
                    d[0] = Double.valueOf(fields[0].split("\\=")[1]);
                    d[1] = 0D;
                    d[2] = Double.valueOf(fields[1].split("\\=")[1]);
            }
            else if(line.contains("Y")&&line.contains("Z"))
            {
                    d[0] = 0D;
                    d[1] = Double.valueOf(fields[0].split("\\=")[1]);
                    d[2] = Double.valueOf(fields[1].split("\\=")[1]);
            }
        }
        else if(fields.length==1)
        {
            if(line.contains("X"))
            {
                    d[0] = Double.valueOf(fields[0].split("\\=")[1]);
                    d[1] = 0D;
                    d[2] = 0D;
            }
            //    Location=(X=1280.000000,Y=2944.000000)
            else if(line.contains("Y"))
            {
                    d[0] = 0D;
                    d[1] = Double.valueOf(fields[0].split("\\=")[1]);
                    d[2] = 0D;
            }
            else if(line.contains("Z"))
            {
                    d[0] = 0D;
                    d[1] = 0D;
                    d[2] = Double.valueOf(fields[0].split("\\=")[1]);
            }
        }
        

        return new double[]{d[0],d[1],d[2]};
    }

    /**
     * PrePivot=(X=64.000000,Y=64.000000)
     * @param line
     * @return
     */
    private double[] getPrepivot(String line)
    {
        String tmp="";
        double x=0D;
        double y=0D;
        double z=0D;

        tmp = line.split("PrePivot=")[1];
        tmp = removeChar(tmp, 40);
        tmp = removeChar(tmp, 41);

        String tmp2[]=tmp.split("\\,");

        for(int i=0;i<tmp2.length;i++)
        {
            tmp = tmp2[i];
            if(tmp.contains("X"))
            {
                x = Double.valueOf(tmp.split("\\=")[1]);
            }
            else if(tmp.contains("Y"))
            {
                y = Double.valueOf(tmp.split("\\=")[1]);
            }
            else if(tmp.contains("Z"))
            {
                z = Double.valueOf(tmp.split("\\=")[1]);
            }
        }

        return new double[]{x,y,z};
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

    private double[] getNormal(String line)
    {
        double d[]=new double[3];
        String tmp=line.split("Normal   ")[1];
        String tmp2[] = tmp.split("\\,");
        d[0]= Double.valueOf(tmp2[0]);
        d[1]= Double.valueOf(tmp2[1]);
        d[2]= Double.valueOf(tmp2[2]);
        return d;
    }

    /**
     * Origin   -00064.000000,-00064.000000,+00000.000000
     * @param line
     */
    private double[] getOrigin(String line)
    {
        double d[]=new double[3];
        String tmp=line.split("Origin   ")[1];
        String tmp2[] = tmp.split("\\,");
        d[0]= Double.valueOf(tmp2[0]);
        d[1]= Double.valueOf(tmp2[1]);
        d[2]= Double.valueOf(tmp2[2]);
        return d;
    }
    /**
     * PostScale=(Scale=(X=50.000000,Y=73.000000),SheerAxis=SHEER_ZX)
     * PostScale=:Scale=:X=50.000000,Y=73.000000:,SheerAxis=SHEER_ZX:
     * @param line
     */
    private void getScale(String line)
    {
        String tmp="";
        String tmp2[];
        tmp = replaceChar(line, 40, ':');
        tmp = replaceChar(tmp, 41, ':');
        tmp = replaceChar(tmp, 44, ':');
        tmp2 = tmp.split("\\:");
        for(int i=0;i<tmp2.length;i++)
        {
            tmp = tmp2[i];
            if(tmp.contains("X="))
            {
                this.ps_scalex = Double.valueOf(tmp.split("\\=")[1]);
            }
            else if(tmp.contains("Y="))
            {
                this.ps_scaley = Double.valueOf(tmp.split("\\=")[1]);
            }
            else if(tmp.contains("Z="))
            {
                this.ps_scalez = Double.valueOf(tmp.split("\\=")[1]);
            }
        }
        //System.out.println(this.ps_scalex+" "+ps_scaley+" "+ps_scalez);
    }

    /**
     * Gets MainScale values
     * PostScale=(Scale=(X=50.000000,Y=73.000000),SheerAxis=SHEER_ZX)
     * PostScale=:Scale=:X=50.000000,Y=73.000000:,SheerAxis=SHEER_ZX:
     * @param line Line being read from t3d file.
     */
    private void getMS_Scale(String line)
    {
        String tmp="";
        String tmp2[];
        tmp = replaceChar(line, 40, ':');
        tmp = replaceChar(tmp, 41, ':');
        tmp = replaceChar(tmp, 44, ':');
        tmp2 = tmp.split("\\:");
        for(int i=0;i<tmp2.length;i++)
        {
            tmp = tmp2[i];
            if(tmp.contains("X="))
            {
                this.ms_scalex = Double.valueOf(tmp.split("\\=")[1]);
            }
            else if(tmp.contains("Y="))
            {
                this.ms_scaley = Double.valueOf(tmp.split("\\=")[1]);
            }
            else if(tmp.contains("Z="))
            {
                this.ms_scalez = Double.valueOf(tmp.split("\\=")[1]);
            }
        }
        //System.out.println(this.ps_scalex+" "+ps_scaley+" "+ps_scalez);
    }
    /**
     * Gets rotation values (Pitch(Y),Yaw(Z) and Roll(X)) from current brush
     *     Rotation=(Yaw=16384,Roll=-16384)
     * @param line
     */
    private void getRotation(String line)
    {
        String tmp="";
        tmp = line.split("Rotation=")[1];
        tmp = removeChar(tmp, 40);
        tmp = removeChar(tmp, 41);

        String tmp2[]=tmp.split("\\,");

        for(int i=0;i<tmp2.length;i++)
        {
            tmp = tmp2[i];
            if(tmp.contains("Yaw"))
            {
                yaw = Double.valueOf(tmp.split("\\=")[1]);
            }
            else if(tmp.contains("Roll"))
            {
                roll = Double.valueOf(tmp.split("\\=")[1]);
            }
            else if(tmp.contains("Pitch"))
            {
                pitch = Double.valueOf(tmp.split("\\=")[1]);
            }
        }
    }

    /**
     * Remove a char from a string
     * @param line
     * @param numchar Char to be removed (int)
     * @return String without char specified
     */
    private String removeChar(String line,int numchar)
    {
        String tmp="";
        int previouschar=-1;
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

    /**
     *              Vertex   +00000.000000,-00032.000000,+00000.000000
     * @param line
     * @return
     */
    private Vector3d getVector(String line)
    {
        Vector3d v3d = new Vector3d();
        String tmp=line.split("Vertex   ")[1];
        String tmp2[] = tmp.split("\\,");
        v3d.setX(Double.valueOf(tmp2[0]));
        v3d.setY(Double.valueOf(tmp2[1]));
        v3d.setZ(Double.valueOf(tmp2[2]));

        return v3d;
    }
}
