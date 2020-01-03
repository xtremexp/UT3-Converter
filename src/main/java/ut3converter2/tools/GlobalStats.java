/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools;

import java.util.HashMap;
import java.util.Iterator;
import javax.swing.table.DefaultTableModel;
import ut3converter2.convert.Level.SupportedClasses;

/**
 *
 * @author Hyperion
 */
public class GlobalStats {

    float converttime=0F;
    HashMap conv_actorsall;
    HashMap unconv_actorsall;
    SupportedClasses sc;
    
    public GlobalStats(SupportedClasses sc) {
        this.sc = sc;
        conv_actorsall = new HashMap();
        unconv_actorsall = new HashMap();
    }

    public void addConvActor(String curclass)
    {
        int num=0;

        if(sc.isSupportedClass(curclass))
        {
            if(!conv_actorsall.containsKey(curclass))
            {
                conv_actorsall.put(curclass, 1);
            }
            else
            {
                num = Integer.valueOf(conv_actorsall.get(curclass).toString());
                num ++;
                conv_actorsall.put(curclass, num);
            }
        }
        else
        {
            if(!unconv_actorsall.containsKey(curclass))
            {
                unconv_actorsall.put(curclass, 1);
            }
            else
            {
                num = Integer.valueOf(unconv_actorsall.get(curclass).toString());
                num ++;
                unconv_actorsall.put(curclass, num);
            }
        }
    }

    public DefaultTableModel getDTMUnconvActors()
    {
        DefaultTableModel dtm = new DefaultTableModel(new String[]{"Actor","Num"}, 0);

        Iterator it = unconv_actorsall.keySet().iterator();
        System.out.println(unconv_actorsall.size());
        String tmp="";
        while(it.hasNext())
        {
            tmp  = it.next().toString();
            dtm.addRow(new String[]{tmp,unconv_actorsall.get(tmp).toString()});
        }

        System.out.println(dtm.getRowCount());
        return dtm;
    }
    

}
