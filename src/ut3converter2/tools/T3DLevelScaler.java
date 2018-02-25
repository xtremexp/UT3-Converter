/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.vecmath.Vector3d;
import ut3converter2.T3DConvertor;

/**
 * Scale up or down whole map.
 * Used mainly for UT99 to UT2004 conversion (1.25X factor)
 * Uses DrawScale3D for staticmeshes
 * Updates Location value for all actors
 * Updates TextureU,V,Origin,PrePivot for brushes and volumes
 * Updates SoundRadius,LightRadius
 * @author Hyperion
 */
public class T3DLevelScaler extends T3DConvertor{

    String tskname="\n*** Scaling T3D Level File ";
    File t3dlevelin;
    File t3dlevelout;
    double scalefactorr=1D;
    boolean scalestaticmeshes=true;
    DecimalFormat df = new DecimalFormat("0.000000", new DecimalFormatSymbols(Locale.US));
    BufferedReader bfr;
    BufferedWriter bwr;

    public T3DLevelScaler(File t3dlevelin, File t3dlevelout,double scalefactorr) {
        this.t3dlevelin = t3dlevelin;
        this.t3dlevelout = t3dlevelout;
        this.scalefactorr = scalefactorr;
        super.setTaskname(tskname);
    }

    public boolean isScalestaticmeshes() {
        return scalestaticmeshes;
    }

    public void setScalestaticmeshes(boolean scalestaticmeshes) {
        this.scalestaticmeshes = scalestaticmeshes;
    }

    public void scale() throws FileNotFoundException, IOException
    {
        if(this.isBShowLog()){System.out.print(super.getTaskname()+" ("+scalefactorr+"X)");}
        


        bfr = FileDecoder.getBufferedReader(t3dlevelin, "Begin");
        bwr = new BufferedWriter(new FileWriter(t3dlevelout));
        
        String prvline="";
        int linenum=1;
        while((line=bfr.readLine())!=null)
        {
            try {
                analyzeline(line);
                prvline = line;
                linenum ++;
            } catch (Exception e) {
                System.out.println("Error while parsing data from "+t3dlevelin.getName()+" @line #"+linenum+":");
                System.out.println("\t  \""+prvline+"\"");
                System.out.println("\t->\""+line+"\"");
            }
        }

        bfr.close();
        bwr.close();
        if(this.isBShowLog()){System.out.println(" ... done!");}
        if(this.isBDispResult()){JOptionPane.showMessageDialog(null, "<html>T3D Level have been scaled "+scalefactorr+"X to:<br>"+t3dlevelout.getAbsolutePath());}
    }

    double dlocation[]=new double[3];
    double dorigin[];
    double dvertex[];
    double dtexu[];
    double dtexv[];
    double dprepivot[];
    double ddrawscale3d[]=new double[]{1D,1D,1D};
    Vector3d v3d;
    String line="";
    String curclass="";
    boolean isbrush=false;
    boolean isstaticmesh=false;

    private void analyzeline(String line) throws IOException
    {
         if(isbrush)
            {
                //             Vertex   +03333.000000,+03333.000000,+00000.000000
                if(line.contains("Vertex"))
                {
                    v3d = getVector(line);
                    dvertex = Geom.scale(new double[]{v3d.getX(),v3d.getY(),v3d.getZ()}, scalefactorr);
                    dvertex = Geom.updateDoubleZeroes(dvertex);
                    bwr.write("             Vertex   "+formatValue(dvertex[0])+","+formatValue(dvertex[1])+","+formatValue(dvertex[2])+"\n");
                }
                else if(line.contains("Origin"))
                {
                    dorigin = Geom.scale(getOrigin(line),scalefactorr);
                    bwr.write("             Origin   "+formatValue(dorigin[0])+","+formatValue(dorigin[1])+","+formatValue(dorigin[2])+"\n");
                }
                else if(line.contains("TextureU"))
                {
                    dtexu = Geom.scale(getTextureU(line), 1/scalefactorr);
                    bwr.write("             TextureU   "+formatValue(dtexu[0])+","+formatValue(dtexu[1])+","+formatValue(dtexu[2])+"\n");
                }
                else if(line.contains("TextureV"))
                {
                    dtexv = Geom.scale(getTextureV(line), 1/scalefactorr);
                    bwr.write("             TextureV   "+formatValue(dtexv[0])+","+formatValue(dtexv[1])+","+formatValue(dtexv[2])+"\n");
                }
                else if(line.contains("End Actor"))
                {
                    isbrush = false;
                    bwr.write(line+"\n");
                }
                else
                {
                    checkOtherFields(bwr, line);
                }
            }
            else if(isstaticmesh)
            {
                //    DrawScale3D=(X=0.600000,Y=0.600000,Z=0.600000)
                if(line.contains("DrawScale3D"))
                {
                    ddrawscale3d = getDrawScale3D(line);
                }
                else if(line.contains("End Actor"))
                {
                    if(isScalestaticmeshes())
                    {
                        ddrawscale3d = Geom.scale(ddrawscale3d, scalefactorr);
                    }

                    bwr.write("    DrawScale3D=(X="+ddrawscale3d[0]+",Y="+ddrawscale3d[1]+",Z="+ddrawscale3d[2]+")\n");
                    isstaticmesh = false;
                    ddrawscale3d =new double[]{1D,1D,1D};
                    bwr.write(line+"\n");
                }
                else
                {
                    checkOtherFields(bwr, line);
                }

            }
            else if(line.contains("Begin Actor"))
            {
                curclass = getActorClass(line);
                bwr.write(line+"\n");

            }
            else if(line.contains("End Actor"))
            {
                isbrush = false;
                isstaticmesh = false;
                bwr.write(line+"\n");
            }
            else if(line.contains("Begin Brush"))
            {
                isbrush = true;
                bwr.write(line+"\n");
            }
            else if(line.contains("End Brush"))
            {
                isbrush = false;
                bwr.write(line+"\n");
            }
            else if(line.contains("DrawType=DT_StaticMesh"))
            {
                isstaticmesh=true;
            }
            else
            {
                checkOtherFields(bwr, line);
            }
    }
    int tmpint;
    private void checkOtherFields(BufferedWriter bwr,String line) throws IOException
    {

        double da[];
        String t[];
        double n=1D;

        if(line.contains(" Location="))
        {
            da =  Geom.scale(getLocation(line), scalefactorr);
            bwr.write("    Location=(X="+formatValue(da[0])+",Y="+formatValue(da[1])+",Z="+formatValue(da[2])+")\n");
        }
        else if(line.contains("ColLocation="))
        {
            da =  Geom.scale(getLocation(line), scalefactorr);
            bwr.write("    ColLocation=(X="+da[0]+",Y="+da[1]+",Z="+da[2]+")\n");
        }
        else if(line.contains("PrePivot="))
        {
            da = Geom.scale(getPrepivot(line),scalefactorr);
            bwr.write("    PrePivot=(X="+formatValue(da[0])+",Y="+formatValue(da[1])+",Z="+formatValue(da[2])+")\n");
        }
        else if(line.contains("LightRadius=")||line.contains("SoundRadius="))
        {
            t = line.split("\\=");
            n = Double.valueOf(t[1]);
            n *=scalefactorr;
            bwr.write(t[0]+"="+n+"\n");
        }
        //FOR MOVERS
        //BasePos=(X=1824.000000,Y=126.000732,Z=-4144.000000)
        else if(line.contains(" BasePos"))
        {
            da =  Geom.scale(getLocation(line), scalefactorr);
            bwr.write("    BasePos=(X="+da[0]+",Y="+da[1]+",Z="+da[2]+")\n");
        }
        //SavedPos=(X=-12345.000000,Y=-12345.000000,Z=-12345.000000)
        else if(line.contains(" SavedPos"))
        {
            da =  Geom.scale(getLocation(line), scalefactorr);
            bwr.write("    SavedPos=(X="+da[0]+",Y="+da[1]+",Z="+da[2]+")\n");
        }
        //KeyPos(1)=(Z=209.000000)
        else if(line.contains(" KeyPos"))
        {
            t = line.split("\\)\\=");
            Object obj[]=getKeyPosVal(line);
            if(obj.length==2){bwr.write(t[0]+")=("+obj[0]+"="+(Double.valueOf(obj[1].toString())*scalefactorr)+")\n");}
            else if(obj.length==4)
            {bwr.write(t[0]+")=("+obj[0]+"="+(Double.valueOf(obj[1].toString())*scalefactorr)+","+obj[2]+"="+(Double.valueOf(obj[3].toString())*scalefactorr)+")\n");}
            else if(obj.length==6)
            {bwr.write(t[0]+")=("+obj[0]+"="+(Double.valueOf(obj[1].toString())*scalefactorr)+","+
                     obj[2]+"="+(Double.valueOf(obj[3].toString())*scalefactorr)+","+
                     obj[4]+"="+(Double.valueOf(obj[5].toString())*scalefactorr)+"\n");}
        }
        /*
        else if(line.contains(" Radius="))
        {
            try {
                tmpint = Integer.valueOf(line.split("Radius=")[1]);
            } catch (Exception e) {
            }
            bwr.write(" Radius="+(int)(tmpint*scalefactorr)+"\n");
        }
         */
        else
        {
            bwr.write(line+"\n");
        }
    }

    /**
     * KeyPos(1)=(Z=544.000000)
     * KeyPos(1)=(Y=1.364868,Z=137.916260)
     * @param line
     * @return
     */
    private Object[] getKeyPosVal(String line)
    {
        double val=0D;
        Object obj[] = null;
        String t[];
        String t2[];
        line = line.split("\\(")[2];
        line = line.replaceAll("\\)","");
        t = line.split("\\,");
        if(t.length==1)
        {
            t2 = line.split("\\=");
            return new Object[]{t2[0],t2[1]};
        }
        else if(t.length==2)
        {
            obj = new Object[4];
            t2 = t[0].split("\\=");
            obj[0]=t2[0];
            obj[1]=t2[1];

            t2 = t[1].split("\\=");
            obj[2]=t2[0];
            obj[3]=t2[1];
            return obj;
        }
        else if(t.length==3)
        {
            obj = new Object[6];
            t2 = t[0].split("\\=");
            obj[0]=t2[0];
            obj[1]=t2[1];

            t2 = t[1].split("\\=");
            obj[2]=t2[0];
            obj[3]=t2[1];

            t2 = t[2].split("\\=");
            obj[4]=t2[0];
            obj[5]=t2[1];
            return obj;
        }

        return obj;
    }
    
    private double[] getDrawScale3D(String line)
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

    private String getActorClass(String line)
    {
        return (line.split("=")[1]).split(" ")[0];
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
     *              Vertex   +00000.000000,-00032.000000,+00000.000000
     * @param line
     * @return
     */
    private Vector3d getVector(String line)
    {
        //             Vertex 3333.0,3333.0,0.0
        Vector3d v3d = new Vector3d();
        String tmp=line.split("Vertex   ")[1];

        
        String tmp2[] = tmp.split("\\,");
        v3d.setX(Double.valueOf(tmp2[0]));
        v3d.setY(Double.valueOf(tmp2[1]));
        v3d.setZ(Double.valueOf(tmp2[2]));

        return v3d;
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

     private String formatValue(double d)
    {
        return df.format(d);
    }

}
