/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools;

/**
 *
 * @author Hyperion
 */
public class TextureAlign {

    double scalefactorr = 1D;
    double dorigin[];
    double texu[];
    double texv[];
    double ps_x=1D;
    double ps_y=1D;
    double ps_z=1D;
    double ms_x=1D;
    double ms_y=1D;
    double ms_z=1D;
    int panu=0;
    int panv=0;

    public TextureAlign() {
        texu = new double[3];
        texv = new double[3];
    }

    public void reset()
    {
        ps_x = 1D;
        ps_y = 1D;
        ps_z = 1D;
        ms_x = 1D;
        ms_y = 1D;
        ms_z = 1D;
        
    }

    public double getScalefactorr() {
        return scalefactorr;
    }

    public void setScalefactorr(double scalefactorr) {
        this.scalefactorr = scalefactorr;
    }

    public void resetPan()
    {
        panu=0;
        panv=0;
    }
    public double[] getTexu() {
        return texu;
    }

    public double[] getTexv() {
        return texv;
    }

    public void setPs_x(double ps_x) {
        this.ps_x = ps_x;
    }

    public void setPs_y(double ps_y) {
        this.ps_y = ps_y;
    }

    public void setPs_z(double ps_z) {
        this.ps_z = ps_z;
    }

    public void setMs_x(double ms_x) {
        this.ms_x = ms_x;
    }

    public void setMs_y(double ms_y) {
        this.ms_y = ms_y;
    }

    public void setMs_z(double ms_z) {
        this.ms_z = ms_z;
    }

    public void updateUVMS()
    {
        texu[0]=(texu[0])/(ms_x);
        texu[1]=(texu[1])/(ms_y);
        texu[2]=(texu[2])/(ms_z);

        texv[0]=(texv[0])/(ms_x);
        texv[1]=(texv[1])/(ms_y);
        texv[2]=(texv[2])/(ms_z);
    }

    public void updateUVPSSF()
    {
        texu[0]=(texu[0])/(ps_x*scalefactorr);
        texu[1]=(texu[1])/(ps_y*scalefactorr);
        texu[2]=(texu[2])/(ps_z*scalefactorr);

        texv[0]=(texv[0])/(ps_x*scalefactorr);
        texv[1]=(texv[1])/(ps_y*scalefactorr);
        texv[2]=(texv[2])/(ps_z*scalefactorr);
    }

    public void updateUV(double pitch2,double yaw2,double roll2)
    {
        texu[0]=(texu[0])/ms_x;
        texu[1]=(texu[1])/ms_y;
        texu[2]=(texu[2])/ms_z;
        
        texv[0]=(texv[0])/ms_x;
        texv[1]=(texv[1])/ms_y;
        texv[2]=(texv[2])/ms_z;

        texu = Geom.getVecRotate(texu, pitch2, yaw2, roll2);
        texv = Geom.getVecRotate(texv, pitch2, yaw2, roll2);

        texu[0]=(texu[0])/(ps_x*scalefactorr);
        texu[1]=(texu[1])/(ps_y*scalefactorr);
        texu[2]=(texu[2])/(ps_z*scalefactorr);
        
        texv[0]=(texv[0])/(ps_x*scalefactorr);
        texv[1]=(texv[1])/(ps_y*scalefactorr);
        texv[2]=(texv[2])/(ps_z*scalefactorr);
    }
    
    public void setPanu(int panu) {
        this.panu = panu;
    }

    public void setPanv(int panv) {
        this.panv = panv;
    }

    public double[] getDorigin() {
        return dorigin;
    }

    
    public void setDorigin(double[] dorigin2) {
        
        //dorigin2[1] -= (panu/2D);
        this.dorigin = dorigin2;
        //dorigin[1] -= panv;
    }

    public void setTexu(double[] texu) {
        /*
        if(Math.abs(texu[0])<0.001){texu[0]=0D;}
        if(Math.abs(texu[1])<0.001){texu[1]=0D;}
        if(Math.abs(texu[2])<0.001){texu[2]=0D;}
         */

        this.texu = texu;

    }

    public void setTexv(double[] texv) {
        /*
        if(Math.abs(texv[0])<0.001){texv[0]=0D;}
        if(Math.abs(texv[1])<0.001){texv[1]=0D;}
        if(Math.abs(texv[2])<0.001){texv[2]=0D;}
         */
        this.texv = texv;
    }

    

}
