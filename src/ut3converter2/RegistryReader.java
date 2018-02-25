/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2;

import java.io.*;

/**
 * Used to auto-detect where unreal games have been installed using registry.
 * @author Hyperion
 */
public class RegistryReader {

    private static final String REGSTR_TOKEN = "REG_SZ";
    private static final String REGQUERY_UTIL = "reg query ";

    public static final String prefix_os_w7= "\"HKEY_CURRENT_USER\\";
    public static final String prefix_os_other= "\"HKEY_LOCAL_MACHINE\\";

    private static final String deusex_path=
            "SOFTWARE\\Unreal Technology\\Installed Apps\\Deus Ex\\";

    private static final String ut2003_path=
            "SOFTWARE\\Unreal Technology\\Installed Apps\\UT2003\\";

    private static final String u1_path=
            "SOFTWARE\\Unreal Technology\\Installed Apps\\Unreal\\";

    private static final String ut99_path=
            "SOFTWARE\\Unreal Technology\\Installed Apps\\UnrealTournament\\";

    private static final String ut3_path=
            "SOFTWARE\\Unreal Technology\\Installed Apps\\UT3\\";

    private static final String ut2004_path=
            "SOFTWARE\\Unreal Technology\\Installed Apps\\UT2004\\";

    public static String getUTxFolderPath(int utgame) throws IOException
    {
        String prefix;
        String osname=System.getProperty("os.name");
        if(osname.equals("Windows 7")||osname.equals("Windows Vista"))
        {
            prefix = prefix_os_w7;
        }
        else if(osname.equals("Windows XP"))
        {
            prefix = prefix_os_other;
        }
        else
        {
            prefix = prefix_os_other;
        }
        String a = null;
        if(utgame==UTGames.UT99)
        {
            a = getRegValue("Folder", prefix+ut99_path);
        }
        else if(utgame==UTGames.UT2003)
        {
            a = getRegValue("Folder", prefix+ut2003_path);
        }
        else if(utgame==UTGames.U1)
        {
            a = getRegValue("Folder", prefix+u1_path);
        }
        else if(utgame==UTGames.UT2004)
        {
            a = getRegValue("Folder", prefix+ut2004_path);
        }
        else if(utgame==UTGames.DeusEx)
        {
            a = getRegValue("Folder", prefix+deusex_path);
        }
        else if(utgame==UTGames.UT3)
        {
            //a = getRegValue("Folder", deusex_path);
        }
        return a;
    }
    
    public static String getRegValue(String key,String path) throws IOException
    {
        String a = getValuesFromPath(path);
        try {
            if(a!=null)
            {
                BufferedReader bfr = new BufferedReader(new StringReader(a));
                String b="";
                while((b=bfr.readLine())!=null)
                {
                    if (b.contains(key))
                    {
                        b = b.replaceAll("\\  ", "\\ ");
                        b = b.replaceAll("\\  ", "\\ ");
                        String t[];
                        t = b.split("REG_SZ ");
                        if(t.length>0)
                        {
                            return t[1];
                        }
                        else
                        {
                            return null;
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        
        return a;
    }
    public static String getValuesFromPath(String path)
    {
            try {
          Process process = Runtime.getRuntime().exec(REGQUERY_UTIL+path);
          //System.out.println(REGQUERY_UTIL+path);
          StreamReader reader = new StreamReader(process.getInputStream());

          reader.start();
          process.waitFor();
          reader.join();


          String result = reader.getResult();
          int p = result.indexOf(REGSTR_TOKEN);

          if (p == -1)
             return null;

          return result;
          //return result.substring(p + REGSTR_TOKEN.length()).trim();
        }
        catch (Exception e) {
          return null;
        }
    }

      static class StreamReader extends Thread {
    private InputStream is;
    private StringWriter sw;

    StreamReader(InputStream is) {
      this.is = is;
      sw = new StringWriter();
    }

    public void run() {
      try {
        int c;
        while ((c = is.read()) != -1)
          sw.write(c);
        }
        catch (IOException e) { ; }
      }

    String getResult() {
      return sw.toString();
    }
  }

      public static String getOsName() {
  String os = "";
  if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
    os = "windows";
  } else if (System.getProperty("os.name").toLowerCase().indexOf("linux") > -1) {
    os = "linux";
  } else if (System.getProperty("os.name").toLowerCase().indexOf("mac") > -1) {
    os = "mac";
  }
 
  return os;
}

}
