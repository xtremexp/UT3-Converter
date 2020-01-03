/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools;

import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;
import ut3converter2.Main;

/**
 *
 * @author Hyperion
 */
public class Geom {

    private static String brushnam="Brush8888";


    private static double radToDegree(double radian)
    {
        return radian*360D/(2*Math.PI);
    }

    public static double[] getAngles2(Vector3d va,Vector3d vb)
    {
        double cosx=0D;

        double tmp=0D;
        double tmp2=0D;

        //(2,3,4)
        //cos a = (XaXb+YaYb+ZaZb) / sqrt((Xa²+Ya²+Za²)(Xb²+Yb²+Zb² ))

        tmp = va.getX()*vb.getX()+va.getY()*vb.getY()+va.getZ()*vb.getZ();
        tmp2 = Math.sqrt((Math.pow(va.getX(), 2)+Math.pow(va.getY(), 2)+Math.pow(va.getZ(), 2))*(Math.pow(vb.getX(), 2)+Math.pow(vb.getY(), 2)+Math.pow(vb.getZ(), 2)));
        cosx = tmp/tmp2;

        System.out.println("X: "+cosx);
        System.out.println("X: "+radToDegree(Math.acos(cosx)));

        return new double[]{cosx};
    }

    /**
     * Returns the angles in radians
     * @param v3d
     */
    public static double[] getAngles(Vector3d va)
    {
        double cosx=0D;
        double cosy=0D;
        double cosz=0D;
        double tmp=0D;
        double tmp2=0D;
        Vector3d vb=new Vector3d(1D, 0D, 0D);
        //(2,3,4)
        //cos a = (XaXb+YaYb+ZaZb) / sqrt((Xa²+Ya²+Za²)(Xb²+Yb²+Zb² ))

        tmp = va.getX()*vb.getX()+va.getY()*vb.getY()+va.getZ()*vb.getZ();
        tmp2 = Math.sqrt((Math.pow(va.getX(), 2)+Math.pow(va.getY(), 2)+Math.pow(va.getZ(), 2))*(Math.pow(vb.getX(), 2)+Math.pow(vb.getY(), 2)+Math.pow(vb.getZ(), 2)));
        cosx = tmp/tmp2;

        vb=new Vector3d(0D, 1D, 0D);
        tmp = va.getX()*vb.getX()+va.getY()*vb.getY()+va.getZ()*vb.getZ();
        tmp2 = Math.sqrt((Math.pow(va.getX(), 2)+Math.pow(va.getY(), 2)+Math.pow(va.getZ(), 2))*(Math.pow(vb.getX(), 2)+Math.pow(vb.getY(), 2)+Math.pow(vb.getZ(), 2)));
        cosy = tmp/tmp2;

        vb=new Vector3d(0D, 0D, 1D);
        tmp = va.getX()*vb.getX()+va.getY()*vb.getY()+va.getZ()*vb.getZ();
        tmp2 = Math.sqrt((Math.pow(va.getX(), 2)+Math.pow(va.getY(), 2)+Math.pow(va.getZ(), 2))*(Math.pow(vb.getX(), 2)+Math.pow(vb.getY(), 2)+Math.pow(vb.getZ(), 2)));
        cosz = tmp/tmp2;

        //System.out.println("X: "+cosx+" Y: "+cosy+" Z: "+cosz);
        //System.out.println("X: "+radToDegree(Math.acos(cosx))+" Y: "+radToDegree(Math.acos(cosy))+" Z: "+radToDegree(Math.acos(cosz)));

        return new double[]{cosx,cosy,cosz};
    }

    public static double[] getScale(double tex_u[],double tex_v[])
    {
        double scalex = Math.pow(tex_u[0], 2)+Math.pow(tex_v[0], 2);
        double scaley = Math.pow(tex_u[1], 2)+Math.pow(tex_v[1], 2);
        double scalez = Math.pow(tex_u[2], 2)+Math.pow(tex_v[2], 2);
        return new double[]{1/scalex,1/scaley,1/scalez};
    }
    /**
     * Make rotate input vector with Yaw(Z),Pitch(Y) and Roll(X) unreal rotation values
     * in the YXZ UT Editor coordinate system.
     *    +00576.000000,+01088.000000,+00192.000000
     * -> -00192.000000,+00064.000000,+00192.000000
     * @param v3f
     * @param pitch2 Pitch in Unreal Value (65536 u.v. = 360°)
     * @param yaw2 Yaw
     * @param roll2 
     */
    public static double[] getVecRotate(Vector3d v3d,double pitch2,double yaw2,double roll2)
    {

        pitch2 = UnrealAngleToDegree(pitch2);
        yaw2 = UnrealAngleToDegree(yaw2);
        roll2 = UnrealAngleToDegree(roll2);

        double pitch = pitch2;
        double roll = roll2;
        double yaw = yaw2;

        double rot_x = ((roll)/360D)*2D*Math.PI; //Roll=Axis X with Unreal Editor
        double rot_y = (((pitch))/360D)*2D*Math.PI; //Pitch=Axis Y with Unreal Editor
        double rot_z = ((yaw)/360D)*2D*Math.PI; //Yaw=Axis Z with Unreal Editor

        
        double tmp[]= new double[]{v3d.getX(),v3d.getY(),v3d.getZ(),1D};
        Matrix4d m4d=getGlobalRotationMatrix(rot_x, rot_y, rot_z);

        tmp = getRot(tmp, m4d);
        /*
        if(Math.abs(tmp[0])<0.001){tmp[0]=0D;}
        if(Math.abs(tmp[1])<0.001){tmp[1]=0D;}
        if(Math.abs(tmp[2])<0.001){tmp[2]=0D;}
         * */
         

        return new double[]{tmp[0],tmp[1],tmp[2]};
    }

    public static double[] getVecRotate(double d[],double pitch2,double yaw2,double roll2)
    {
        return getVecRotate(new Vector3d(d[0], d[1], d[2]), pitch2, yaw2, roll2);
    }

    private static Matrix4d getGlobalRotationMatrix(double rot_x,double rot_y,double rot_z)
    {
        Matrix4d m4d=new Matrix4d();
        //Verifiée
        Matrix4d m4d_x = new Matrix4d(
                1D, 0D, 0D, 0D,
                0D, Math.cos(rot_x),-Math.sin(rot_x), 0D,
                0D, Math.sin(rot_x),Math.cos(rot_x), 0D,
                0D, 0D, 0D, 1D);

        //Vérifiée
        Matrix4d m4d_y = new Matrix4d(
                Math.cos(rot_y), 0D, Math.sin(rot_y), 0D,
                0D, 1D, 0D, 0D,
                -Math.sin(rot_y), 0,Math.cos(rot_y), 0D,
                0D, 0D, 0D, 1D);

        //Verified-OK
        Matrix4d m4d_z = new Matrix4d(
                Math.cos(rot_z), Math.sin(rot_z), 0D, 0D,
                -Math.sin(rot_z), Math.cos(rot_z), 0D, 0D,
                0D, 0D,1D, 0D,
                0D, 0D, 0D, 1D);
        m4d_x = updateMatrix(m4d_x);
        m4d_y = updateMatrix(m4d_y);
        m4d_z = updateMatrix(m4d_z);

        m4d = m4d_x;
        m4d.mul(m4d_y);
        m4d.mul(m4d_z);

        return m4d;
    }

    /**
     * Scale up input vector such as vertice,location,prepivot.
     * If Scalefactor>1 then it scales up else scales down.
     * @param d Vector
     * @param scalefactor Scale factor
     * @return New scaled vector.
     */
    public static double[] scale(double d[],double scalefactor)
    {
        d[0] *= scalefactor;
        d[1] *= scalefactor;
        d[2] *= scalefactor;
        return d;
    }

    public static double[] updateDoubleZeroes(double d[])
    {
        for(int i=0;i<d.length;i++)
        {
            if(Math.abs(d[i])<0.001D)
            {
                d[i]=0D;
            }
        }
        return d;
    }
    /**
     * Replaces low values in matrix by zeroes.
     * @param m4d Input 4x4 matrix
     * @return Filtered matrix.
     */
    private static Matrix4d updateMatrix(Matrix4d m4d)
    {
        double tmp=0D;

        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                tmp = m4d.getElement(i, j);
                if(Math.abs(tmp)<0.01){m4d.setElement(i, j, 0D);}
            }
        }
        return m4d;
    }

    /**
     * Convert an unreal angle to degree.
     * 65536 Unreal Angle = 360°
     * @param yaw Unreal Angle
     * @return Unreal angle in degrees
     */
    public static double UnrealAngleToDegree(double yaw)
    {
        //System.out.println("Angle (Unreal):"+yaw);
        int num = (int)(yaw/65536D);
        if(yaw>=0)
        {
            yaw = yaw-num*65536D;
        }
        else if(yaw<0)
        {
            yaw = yaw+num*65536D;
        }
        

        //System.out.println(yaw+"->"+(yaw/65536D)*360D);
        return (yaw/65536D)*360D;
    }

    private static double[] getRot(double[] d2,Matrix4d m4d)
    {
        double d[] = new double []{d2[0],d2[1],d2[2],1D};
        
        double dx = m4d.m00*d[0]+m4d.m10*d[1]+m4d.m20*d[2]+m4d.m30*d[3];
        
        double dy = m4d.m01*d[0]+m4d.m11*d[1]+m4d.m21*d[2]+m4d.m31*d[3];
        double dz = m4d.m02*d[0]+m4d.m12*d[1]+m4d.m22*d[2]+m4d.m32*d[3];

        if(Math.abs(dx)<0.00001D){dx=0D;}
        if(Math.abs(dy)<0.00001D){dy=0D;}
        if(Math.abs(dz)<0.00001D){dz=0D;}
        

        
        return new double[]{dx,dy,dz};
    }

}
