/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;
import javax.vecmath.Vector3d;

/**
 *
 * @author Hyperion
 */
public class Polygon {

    ArrayList alvertex;
    double normal[];
    double origin[];
    String otherdata="";
    double rotation[]; //Pitch-Yaw-Roll
    double scalefactorr = 1D;
    double ms_scalex=1D;
    double ms_scaley=1D;
    double ms_scalez=1D;
    double ps_scalex=1D;
    double ps_scaley=1D;
    double ps_scalez=1D;
    boolean revertvertexorder=false;
    String panln="";
    TextureAlign ta;
    DecimalFormat df = new DecimalFormat("0.000000", new DecimalFormatSymbols(Locale.US));

    public Polygon() {
        normal = new double[3];
        origin = new double[3];
        rotation  = new double[3];
        alvertex = new ArrayList();
        ta = new TextureAlign();
    }

    public void setNormal(double[] normal) {
        this.normal = normal;
    }

    public void setRotation(double[] rotation) {
        this.rotation = rotation;
    }

    public void setPanln(String panln) {
        this.panln = panln;
    }

    public void setTa(TextureAlign ta) {
        this.ta = ta;
    }

    public void setMs_scalex(double ms_scalex) {
        this.ms_scalex = ms_scalex;
    }

    public void setMs_scaley(double ms_scaley) {
        this.ms_scaley = ms_scaley;
    }

    public void setMs_scalez(double ms_scalez) {
        this.ms_scalez = ms_scalez;
    }

    public void setScalex(double scalex) {
        this.ps_scalex = scalex;
    }

    public void setScaley(double scaley) {
        this.ps_scaley = scaley;
    }

    public void setScalez(double scalez) {
        this.ps_scalez = scalez;
    }

    public void setOrder(boolean inverted)
    {
        this.revertvertexorder = inverted;
    }
    
    public void setOrigin(double origin[])
    {
        this.origin = origin;
    }
    
    public void addExtraData(String line)
    {
        otherdata += line+"\n";
    }

    public TextureAlign getTa() {
        return ta;
    }
    
    public void addVertex(double vx,double vy,double vz)
    {
        alvertex.add(new double[]{vx,vy,vz});
    }

    public void clearVertex()
    {
        otherdata = "";
        this.alvertex.clear();
        panln="";
    }

    private double[] updateMS(double d[])
    {
        d[0] = d[0]*ms_scalex;
        d[1] = d[1]*ms_scaley;
        d[2] = d[2]*ms_scalez;;
        return d;
    }

    private double[] updatePS(double d[])
    {
        d[0] = d[0]*ps_scalex;
        d[1] = d[1]*ps_scaley;
        d[2] = d[2]*ps_scalez;;
        return d;
    }

    private double[] updateRot(double d[])
    {
        d = Geom.getVecRotate(new Vector3d(d[0], d[1], d[2]), rotation[0], rotation[1], rotation[2]);
        return d;
    }

    private double[] updateMSRot(double d[])
    {
        d[0] = d[0]*ms_scalex;
        d[1] = d[1]*ms_scaley;
        d[2] = d[2]*ms_scalez;
        d = Geom.getVecRotate(new Vector3d(d[0], d[1], d[2]), rotation[0], rotation[1], rotation[2]);
        return d;
    }


    private double[] updatePSRot(double d[])
    {
        d = Geom.getVecRotate(new Vector3d(d[0], d[1], d[2]), rotation[0], rotation[1], rotation[2]);
        d[0] = d[0]*ps_scalex;
        d[1] = d[1]*ps_scaley;
        d[2] = d[2]*ps_scalez;

        return d;
    }

    /**
     * Scales  input vector with MainScale values then make it rotate with Rotation values set
     * of current brush then scales it with PostScale values.
     * @param d Input Vector
     * @return Vector scaled and rotated.
     */
    private double[] updateMSPSRot(double d[])
    {
        d[0] = d[0]*ms_scalex;
        d[1] = d[1]*ms_scaley;
        d[2] = d[2]*ms_scalez;
        d = Geom.getVecRotate(new Vector3d(d[0], d[1], d[2]), rotation[0], rotation[1], rotation[2]);
        d[0] = d[0]*ps_scalex;
        d[1] = d[1]*ps_scaley;
        d[2] = d[2]*ps_scalez;

        return d;
    }

   private double getScaleU(double tex_u[],double uangles[])
    {
        double tmp=1D;
        for(int i=0;i<3;i++)
        {
            
            if(Math.abs(uangles[i])<0.01D)
            {
                uangles[i]=0D;
            }
            if(uangles[i]!=0D)
            {
                //tmp = Math.abs(uangles[i]/tex_u[i]);
                tmp = uangles[i]/tex_u[i];
            }
        }
        return tmp;
    }

    private double getScaleV(double tex_v[],double vangles[])
    {
        double tmp=0D;
        for(int i=0;i<3;i++)
        {
            if(Math.abs(vangles[i])<0.01D)
            {
                vangles[i]=0D;
            }
            if(vangles[i]!=0D)
            {
                return vangles[i]/tex_v[i];
                //return Math.abs(vangles[i]/tex_v[i]);
            }
        }
        return 1;
    }

    public double[] getOriginOffsetPan(double tex_u[],double tex_v[],double pan_u,double pan_v)
    {
        double uangles[] = Geom.getAngles(new Vector3d(tex_u[0], tex_u[1], tex_u[2]));
        double vangles[] = Geom.getAngles(new Vector3d(tex_v[0], tex_v[1], tex_v[2]));
        double scaleu= getScaleU(tex_u,uangles);
        double scalev = getScaleV(tex_v,vangles);

        scaleu=scalefactorr/scaleu;
        scalev=scalefactorr/scalev;
        

        double d[] = new double[]{pan_u*uangles[0]/scaleu,pan_u*uangles[1]/scaleu,pan_u*uangles[2]/scaleu,pan_v*vangles[0]/scalev,pan_v*vangles[1]/scalev,pan_v*vangles[2]/scalev};

        return d;
    }

    /**
     * Update texture origin coordinate depending of PanU,PanV
     * and texture scale factors TextureU and TextureV
     * @param origin2 Input texture coordinate
     * @param texture_u TextureU values double[3]
     * @param texture_v TextureV values double[3]
     * @return Update coordinates.
     */
    private double[] updateOrigin(double[] origin2,double[] texture_u,double[] texture_v,double[] normal)
    {
        double delta[] = new double[3];
        double offset[] = getOriginOffsetPan(texture_u, texture_v, ta.panu, ta.panv);

        delta[0] = (offset[0]+offset[3]);
        delta[1] = (offset[1]+offset[4]);
        delta[2] = (offset[2]+offset[5]);


        origin2[0] -= delta[0];
        origin2[1] -= delta[1];
        origin2[2] -= delta[2];

        return origin2;
    }

    public void setScalefactorr(double scalefactorr) {
        this.scalefactorr = scalefactorr;
    }

    private double[] updateNormal(double normal[])
    {
        double pitch=0D;
        double yaw=0D;
        double roll=0D;

        normal = updateRot(normal);

        normal[0] *= ms_scalex/Math.abs(ms_scalex);
        normal[1] *= ms_scaley/Math.abs(ms_scaley);
        normal[2] *= ms_scalez/Math.abs(ms_scalez);
        
        return normal;
    }
    /**
     * Writes polygon with updated vertices (scale,rotation) and texture scale,position.
     * @param bwr FileWrite
     * @throws java.io.IOException
     */
    public void writePolygon(BufferedWriter bwr) throws IOException
    {
        double dver[];
        double dtexu[];
        double dtexv[];


        ta.updateUV(rotation[0],rotation[1],rotation[2]); //Updates Texture Scale
        dtexu = ta.getTexu();
        dtexv = ta.getTexv();
        normal = updateNormal(normal);
        
        if(revertvertexorder)
        {
            origin = updateMSPSRot(origin);
            origin = updateOrigin(origin, dtexu, dtexv,normal); //Using PanU and PanV to get correct texture position
            
            Geom.scale(origin, scalefactorr);


            bwr.write("             Origin   "+formatValue(origin[0])+","+formatValue(origin[1])+","+formatValue(origin[2])+"\n");
            bwr.write("             Normal   "+formatValue(normal[0])+","+formatValue(normal[1])+","+formatValue(normal[2])+"\n");
            bwr.write("             TextureU   "+formatValue(dtexu[0])+","+formatValue(dtexu[1])+","+formatValue(dtexu[2])+"\n");
            bwr.write("             TextureV   "+formatValue(dtexv[0])+","+formatValue(dtexv[1])+","+formatValue(dtexv[2])+"\n");
            for(int i=(alvertex.size()-1);i>=0;i--)
            {
                dver = (double[]) alvertex.get(i);
                //System.out.println("             Vertex "+dver[0]*xfac+","+dver[1]*yfac+","+dver[2]*zfac+"\n");
                bwr.write("             Vertex   "+formatValue(dver[0])+","+formatValue(dver[1])+","+formatValue(dver[2])+"\n");
            }
        }
        else
        {
            origin = updateMSPSRot(origin);
            origin = updateOrigin(origin, dtexu, dtexv,normal);
            Geom.scale(origin, scalefactorr);

            bwr.write("             Origin   "+formatValue(origin[0])+","+formatValue(origin[1])+","+formatValue(origin[2])+"\n");
            bwr.write("             Normal   "+formatValue(normal[0])+","+formatValue(normal[1])+","+formatValue(normal[2])+"\n");
            bwr.write("             TextureU   "+formatValue(dtexu[0])+","+formatValue(dtexu[1])+","+formatValue(dtexu[2])+"\n");
            bwr.write("             TextureV   "+formatValue(dtexv[0])+","+formatValue(dtexv[1])+","+formatValue(dtexv[2])+"\n");
            for(int i=0;i<alvertex.size();i++)
            {
                dver = (double[]) alvertex.get(i);
                bwr.write("             Vertex   "+formatValue(dver[0])+","+formatValue(dver[1])+","+formatValue(dver[2])+"\n");
            }
        }
    }

    private String formatValue(double d)
    { 
        return df.format(d);
    }
}
