/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.tools;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Hyperion
 */
public class TextReplacer {

    HashMap hmtr;
    ArrayList altr;

    public TextReplacer() {
        hmtr = new HashMap();
        altr = new ArrayList();
    }

    public void add(String oldtext,String newtext)
    {
        hmtr.put(oldtext, newtext);
        if(!altr.contains(oldtext)){altr.add(oldtext);}
    }


    private String[] hasOldText(String line)
    {
        String tmp="";

        for(int i=0;i<altr.size();i++)
        {
            tmp = altr.get(i).toString();
            if(line.contains(tmp))
            {
                i=altr.size();
                return new String[]{"1",tmp};
            }
        }

        return new String[]{"0",""};
    }
    
    public String replace(String line)
    {
        if(altr.size()==0)
        {
            return line;
        }
        else
        {
            String str[] = hasOldText(line);

            if(str[0].equals("1"))
            {
                return line.replaceAll(str[1], hmtr.get(str[1]).toString());
            }
            else
            {
                return line;
            }
        }
        
    }



}
