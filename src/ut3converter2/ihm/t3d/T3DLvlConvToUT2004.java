/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.ihm.t3d;

import java.awt.Frame;
import java.io.File;
import ut3converter2.convert.MapConverter;
import ut3converter2.convert.U1UT99ToUT2k4Conv;

/**
 *
 * @author Hyperion
 */
public class T3DLvlConvToUT2004 extends T3DLvlConvertor{

    //"UT2004-default.umc"
    final static String[] defaultumc = new String[]{"UT2004-Coop.umc"};
    final static String[] scalefactors = new String[]{"0.5","0.8","1 (Coop)","1.25 (Default)","1.5","2"};
    final static int defaultfactor=3;

    public T3DLvlConvToUT2004(Frame parent, File t3dlvl) {
        super(parent, t3dlvl, MapConverter.defumc_ut99ut2k4, T3DLvlConvToUT2004.scalefactors, T3DLvlConvToUT2004.defaultfactor);
    }


}
