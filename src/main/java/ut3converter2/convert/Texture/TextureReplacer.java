/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ut3converter2.convert.Texture;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import ut3converter2.*;
import ut3converter2.convert.MapConverter;

/**
 * Analyse texture packages and try to find real textures for shaders,firetextures,...
 * @author Hyperion
 */
public class TextureReplacer extends T3DConvertor implements Serializable {

    String taskname = "\n**** Analysis of possible Textures replacements ******";
    HashSet<File> utxfiles = new HashSet<File>();
    ArrayList<Texture> altex = new ArrayList();
    ArrayList altexpacanalyzed = new ArrayList();
    HashMap<String, String> hmtex = new HashMap<String, String>();
    /**
     * Contains texture identificator + whole texture data (name,group,type,..)
     */
    HashMap<String, Texture> hmtex2 = new HashMap<String, Texture>();
    /**
     * Identifies inclusion of packages being analyzed
     */
    HashMap<Integer, String> hmpacname = new HashMap<Integer, String>();

    String corepackage = "none";
    String curpackage = "none";
    int curlevel=0;
    MapConverter mc;

    public TextureReplacer(MapConverter mc) {
        this.mc = mc;
    }

    public TextureReplacer() {
        super.setBShowLog(true);
    }

    public TextureReplacer(File[] utxfiles, MapConverter mc) {
        this.utxfiles.addAll(Arrays.asList(utxfiles));
        this.mc = mc;
    }

    public void setUtxfiles(File[] utxfiles) {
        this.utxfiles.addAll(Arrays.asList(utxfiles));
    }

    private boolean bHasAnalyzedPackage(String pacname) {
        return altexpacanalyzed.contains(pacname);
    }

    /**
     * Ice;Texture'textures.IceAlpha02';Shader
     * ->Shader_Ice;textures_IceAlpha02
     */
    public void getUT2004Replacement() {
        Texture tex;
        Texture linkedtex;
        Texture linkedtextofind;
        String identificator;


        for (Iterator<Texture> it = hmtex2.values().iterator(); it.hasNext();) {
            tex = it.next();
            identificator = tex.getIdentificator(); //H_E_L_Ltx.Walls.cp_plaingrit2->Walls.cp_plaingrit2


            if (tex.bHasLinkedTex()) //E.G: Shader texture linked to default texture
            {
                //System.out.println(tex.getIdentificator());
                //linkedtex = tex.getLinkedtexture();
                linkedtex = getTexWithIdentificator(tex.getLinkedtexture().getIdentificator());
                if (linkedtex != null) {

                    //shader.stonevertexfinal01->shader.stonevertexcombiner01
                    if (!linkedtex.bHasLinkedTex()) {
                        hmtex.put(identificator, linkedtex.getIdentificator());
                    } else {
                        //System.out.println(tex.getIdentificator()+"->"+linkedtex.getIdentificator());
                        //linkedtex = tex.getLinkedtexture();
                        linkedtex = getTexWithIdentificator(linkedtex.getLinkedtexture().getIdentificator());
                        //System.out.println("-->"+linkedtex.getIdentificator());
                        if (linkedtex != null) {
                            if (!linkedtex.bHasLinkedTex()) {
                                hmtex.put(identificator, linkedtex.getIdentificator());
                            } else {
                                //System.out.println("***"+tex.getIdentificator()+"->"+linkedtex.getIdentificator());
                                //linkedtex = linkedtex.getLinkedtexture();
                                linkedtex = getTexWithIdentificator(linkedtex.getLinkedtexture().getIdentificator());
                                if (linkedtex != null) {
                                    if (!linkedtex.bHasLinkedTex()) {
                                        hmtex.put(identificator, linkedtex.getIdentificator());
                                    } else {
                                        //System.out.println("*** ***"+tex.getIdentificator()+"->"+linkedtex.getIdentificator());
                                        //linkedtex = tex.getLinkedtexture();
                                        //linkedtextofind = getTexWithIdentificator(linkedtex.getLinkedtexture().getIdentificator());
                                    }
                                }
                            }
                        }
                    }

                } else {
                    //System.out.println("NO Linked TEX FOUND! for "+tex.getIdentificator());
                }
                //System.out.println(tex);
            }

        }

        HashMap<String,String> hmtmp=new HashMap();
        String t;
        UTObject uto;
        for (Iterator<String> it = hmtex.keySet().iterator(); it.hasNext();) {
            t = it.next();
            uto = new UTObject(t);
            hmtmp.put(uto.getGroupAndName().toLowerCase(), hmtex.get(t));
        }

        hmtex = hmtmp;
    }

    private Texture getTexWithIdentificator(String idtex) {
        Texture tex;
        if (hmtex2.containsKey(idtex)) {
            return hmtex2.get(idtex);
        } else {
            return null;
        }
    }

    /**
     * Detects which packages are being used by the textures replacement.
     * @return ArrayList with textures package names.
     */
    public ArrayList<String> getPackagesAnalysed() {
        ArrayList al = new ArrayList();
        for (Iterator it = hmtex.keySet().iterator(); it.hasNext();) {
            String texid = it.next().toString();
            Texture tt = getTexWithIdentificator(texid);
            if (tt != null) {
                if (tt.getPackagename() != null) {
                    if (!al.contains(tt.getPackagename())) {
                        al.add(tt.getPackagename());
                    }
                }
            }
        }
        return al;
    }

    /**
     * Analyse texture  package that haven't been analyzed
     * @param texpacname Package filename (e.g.: BenTex01 (without .utx extension))
     */
    private void addPackageToAnalyze(String texpacname) {
        File f;
        try {
            if (!altexpacanalyzed.contains(texpacname)) {
                String rootfolderpath = Main.config.getUTxRootFolder(mc.getInput_utgame()).getAbsolutePath();

                f = new File(rootfolderpath + File.separator + "Textures" + File.separator + texpacname + ".utx");
                if (f.exists()) {
                        System.out.println("*** analyzing linked texture package: " + f.getName() + " ...");
                        altexpacanalyzed.add(f.getName().split("\\.")[0].toLowerCase());
                        analyseFile(f);
                }

                f = new File(rootfolderpath + "\\System\\" + texpacname + ".u");
                if (f.exists()) {
                        System.out.println("*** analyzing linked texture package: " + f.getName() + " ...");
                        altexpacanalyzed.add(f.getName().split("\\.")[0].toLowerCase());
                        analyseFile(f);
                }


                f = new File(rootfolderpath + File.separator+ "Maps" + File.separator+texpacname + Main.config.getMapFileExtension(mc.getInput_utgame()));
                if (f.exists()) {
                        System.out.println("*** analyzing linked texture package: " + f.getName() + " ...");
                        altexpacanalyzed.add(f.getName().split("\\.")[0].toLowerCase());
                        analyseFile(f);
                }
            }
        } catch (Exception e) {
            Main.config.getMclog().warning(e.getCause().getMessage());
        }
    }

    private void analyzeExtraPackages() {

        for (Iterator<Texture> it = hmtex2.values().iterator(); it.hasNext();) {
            Texture taa = it.next();
            if (taa.bHasLinkedTex()) {
                if (taa.getLinkedtexture().bHasPackageSet()) {
                    addPackageToAnalyze(taa.getLinkedtexture().getPackagename());
                }
            }
        }
    }

    /**
     *
     * @param texname Group.Name
     * @return
     */
    public boolean hasTextureReplacement(String texname) {	
        return hmtex.containsKey(texname.toLowerCase());
    }

    public String getTextureReplacement(String texname) {
        if (hmtex.containsKey(texname.toLowerCase())) {
            return hmtex.get(texname.toLowerCase()).toString();
        } else {
            return texname;
        }
    }

    public HashMap<String, String> getHMReplace() {
        return this.hmtex;
    }

    public void showTexReplacement() {
        String a = "";
        System.out.println(hmtex.size());
        for (Iterator it = hmtex.keySet().iterator(); it.hasNext();) {
            a = it.next().toString();
            System.out.println(a + "->" + hmtex.get(a).toString());
        }
    }

    public TextureReplacer(File utxfile) {
        this.utxfiles.add(utxfile);
        analyseAll();
    }

    /**
     * Returns a list of texture replacements for special textures
     * @param alpactex [hell.smallcloud2.bmp,avalon.cloudshader1.bmp,MyGroup.avalon.avalonmoon_1_2.bmp,...]
     * @return [MyGroup.ShaderTexReplacement,MyGroup.PanTexReplacement]
     */
    public ArrayList getNewNames(Set<String> set_texfiles) {

        ArrayList alnames = new ArrayList();
        UTObject uto;
        if (isBShowLog()) {
            System.out.println("\n*** Textures replacements: ***");
        }

        String t = "";
        String t2[];

        for (Iterator it = set_texfiles.iterator(); it.hasNext();) {
            t = (String) it.next();

            //Removes file extension .bmp,.dds
            t2 = t.split("\\.");
            if (t2.length == 2) {
                t = t2[0];
            }//defaulttexture
            else if (t2.length == 3) {
                t = t2[0] + "." + t2[1];
            }

            if (hmtex.containsKey(t.toLowerCase())) {
                //uto = new UTObject(hmtex.get(t.toLowerCase()));
                uto = new UTObject(hmtex.get(t.toLowerCase()));
                alnames.add(uto.getGroupAndName());
                if (isBShowLog()) {
                    System.out.println(t + " -> " + uto.getGroupAndName());
                }
            }
        }
        
        System.out.println(alnames.size()+" special textures will be replaced.");

        return alnames;
    }

    public void analyseAll() {
        if (isBShowLog()) {
            System.out.println(taskname);
        }

        for (int i = 0; i < utxfiles.size(); i++) {
            try {
                if (isBShowLog()) {
                    System.out.println((i+1) + "/" + (utxfiles.size() ) + "-" + ((File)utxfiles.toArray()[i]).getName());
                }
                hmpacname.clear();
                File file = ((File)utxfiles.toArray()[i]);
                curlevel = -1;
                if(altexpacanalyzed.contains(file.getName().split("\\.")[0].toLowerCase())){
                    if (isBShowLog()) {
                        System.out.println(" ... Skipped (ever analyzed)");
                    }
                } else {
                    altexpacanalyzed.add(file.getName().split("\\.")[0].toLowerCase());
                    this.corepackage=file.getName().split("\\.")[0].toLowerCase();
                    analyseFile(file);
                }
            } catch (IOException ex) {
                Logger.getLogger(TextureReplacer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(TextureReplacer.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (isBShowLog()) {
                //System.out.println(" ... done!");
            }
        }

        if (mc.getInput_utgame() == UTGames.UT2004 || mc.getInput_utgame() == UTGames.U2) {
            getUT2004Replacement();
        }
        if (isBShowLog()) {
            System.out.println(" ... Done!");
        }
    }

    private void analyseFile(File utxfile) throws IOException, InterruptedException {
        String cmd = Installation.getInstallDirectory(Main.class).getAbsolutePath() + File.separator + "bin" + File.separator + "utxextractor" + File.separator + "UtxAnalyser.exe \"" + utxfile.getAbsolutePath() + "\"";
        //ArrayList log = new ArrayList();

        Runtime run = Runtime.getRuntime();
        Process pp = run.exec(cmd);

        BufferedReader in = new BufferedReader(new InputStreamReader(pp.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            //log.add(line);
            analyseLine(line);
        }
        int exitVal = pp.waitFor();
        pp.exitValue();
        //System.out.println(getTexWithIdentificator("shader.stonevertexfinal01"));
        //System.out.println(getTexWithIdentificator("shader.stonevertexcombiner01"));

        /*
        for (Iterator<Texture> it = hmtex2.values().iterator(); it.hasNext();) {
        Texture t =  it.next();
        System.out.println(t);
        }*/
        //System.out.println(" done!");
    }

    /**
     * StoneShiny;Texture'textures.Stone01';Shader
    Gurder03;Texture'textures.Gurder01';Shader
    Ice;Texture'textures.IceAlpha02';Shader
    SnowDark01;Texture'EmitterTextures.MultiFrame.smokelight_a';Shader
    SnowyBranch02;Texture'textures.BranchSnowy01';Shader
    BranchSway01;Texture'textures.BranchSnowy01';Shader;TexOscillator
     * @param line
     */
    private void analyseLine(String line) {
        String t[];
        Texture t2;
        String groupname;
        String texname;
        String newtexname = "-";
        String textype;
        line = line.toLowerCase();

        //SnowDark01;Texture'EmitterTextures.MultiFrame.smokelight_a';Shader
        //System.out.println(line);
        if (line.contains("file:")) {
            curlevel ++;
            File f = new File(line.split("file:")[1]);
            this.curpackage = f.getName().split("\\.")[0].toLowerCase();
            hmpacname.put(curlevel, f.getName().split("\\.")[0].toLowerCase());
        }
        else if(line.contains("--end--"))
        {
            curlevel --;
            this.curpackage = hmpacname.get(curlevel);
        }
        //Avalon,cloudshader1,Texture'Avalon.AvalonScene1',Shader
        //Avalon,AvalonScene1,-,Texture
        else if (line.contains(","))
        {
            t = line.split("\\,");
            groupname = t[0].toLowerCase();
            if (groupname.equals("nogroup")) {
                groupname = null;
            }
            texname = t[1].toLowerCase();
            newtexname = t[2].toLowerCase();
            //Texture'EmitterTextures.MultiFrame.smokelight_a'
            //->EmitterTextures.MultiFrame.smokelight_a
            if (newtexname.equals("-")) {
                newtexname = null;
            } else if (newtexname.contains("'")) {
                newtexname = newtexname.split("\\'")[1];
            }
            textype = t[3].toLowerCase();
            //Group,TexName;NewTexName;TextureType
            t2 = new Texture(texname, groupname, this.curpackage, newtexname, textype);
            /*
            if(line.contains("cloudshader1")){
                System.out.println(line);
            }*/
            hmtex2.put(t2.getIdentificator(), t2);
            //altex.add(new Texture(texname, groupname, this.curpackage, newtexname, textype));
        }
        analyzeExtraPackages();
        updateTextures();
        updateTextures();
        updateTextures();

    }

    private void updateTextures() {
        Texture tex2;
        HashMap<String, Texture> hmtmp = new HashMap<String, Texture>();

        // Collection col=hmtex2.values();
        for (Iterator<Texture> it = hmtex2.values().iterator(); it.hasNext();) {
            tex2 = it.next();

            if (!tex2.bHasTexTypeofLinkedTexSet()) {
                Texture t = getLinkedTexType(tex2.getLinkedtexture());

                if (t != null) {
                    tex2.setLinkedTex(t);
                }
            }
            hmtmp.put(tex2.getIdentificator(), tex2);
        }
        //hmtex2 = new HashMap<String, Texture>();
        hmtex2 = hmtmp;
    }

    private Texture getLinkedTexType(Texture tex2) {
        Texture tex3 = null;

        if (hmtex2.containsKey(tex2.getIdentificator())) {
            return hmtex2.get(tex2.getIdentificator());
        } else {
            return null;
        }
    }

    /**
     * Ice;Texture'textures.IceAlpha02';Shader;Shader
     */
    class Texture {

        String id;
        String name;
        String group;
        String packagename;
        String newname;
        String textype;
        Texture newtexture;

        public Texture(String name, String group, String packagename) {
            this.name = name;
            this.group = group;
            this.packagename = packagename;
            this.id = getIdentificator();
        }

        public Texture(String name, String group, String packagename, String newname, String textype) {
            this.name = name;
            this.group = group;
            this.packagename = packagename;
            if (newname == null) {
                this.newtexture = null;
            } else {
                this.newtexture = getNewTexture(newname);
            }
            this.textype = textype;
            this.id = getIdentificator();
        }

        /**
         * EmitterTextures.MultiFrame.smokelight_a
         * @param newtexname
         */
        private Texture getNewTexture(String newtexname) {
            Texture tex = null;
            String t[] = newtexname.split("\\.");
            if (t.length == 3) {
                tex = new Texture(t[2], t[1], t[0]);
            } else if (t.length == 2) {
                tex = new Texture(t[1], t[0], this.packagename);
            }
            return tex;
        }

        public String getPackagename() {
            return packagename;
        }

        public void setLinkedTex(Texture newtexture) {
            this.newtexture = newtexture;
        }

        public boolean bHasLinkedTex() {
            if (newtexture == null) {
                return false;
            } else {
                return true;
            }
        }

        public Texture getLinkedtexture() {
            return newtexture;
        }

        public boolean bHasTexTypeofLinkedTexSet() {
            if (this.getLinkedtexture() == null) {
                return true;
            } else {
                if (this.getLinkedtexture().textype == null) {
                    return false;
                } else {
                    return true;
                }
            }
        }

        public boolean bHasTexTypeSet() {
            if (this.textype == null) {
                return false;
            } else {
                return true;
            }
        }

        /**
         * 
         * @return
         */
        public String getIdentificator() {
            if (this.bHasGroup()) {
                return (this.packagename+"."+this.group + "." + this.name).toLowerCase();
            } else {
                return (this.packagename+"."+this.name).toLowerCase();
            }
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIdentificatorWithPacName() {
            if (this.bHasGroup()) {
                if (this.bHasPackageSet()) {
                    return (this.packagename + "." + this.group + "." + this.name).toLowerCase();
                } else {
                    return (this.group + "." + this.name).toLowerCase();
                }
            } else {
                if (this.bHasPackageSet()) {
                    return (this.packagename + "." + this.name).toLowerCase();
                } else {
                    return this.name.toLowerCase();
                }

            }
        }

        public void setTextype(String textype) {
            this.textype = textype;
        }

        public String getTextype() {
            return textype;
        }

        public String toString() {
            if (this.bHasLinkedTex()) {
                return id + " *** Package:" + this.packagename + " Name:" + name + " Group:" + group + " Type:" + textype + " --> LinkedTex: (" + this.getLinkedtexture().getIdentificatorWithPacName() + ",Type:" + this.getLinkedtexture().getTextype() + ")";
            } else {
                return id + " *** Package:" + this.packagename + " Name:" + name + " Group:" + group + " Type:" + textype + " --> LinkedTex: (None)";
            }

        }

        public boolean bHasPackageSet() {
            if (packagename == null) {
                return false;
            } else {
                return true;
            }
        }

        public boolean bHasGroup() {
            if (group == null) {
                return false;
            } else {
                return true;
            }
        }

        public Texture(String name, String newnameunfilter) {
            this.name = name;
            this.newname = filternewname(newnameunfilter);
        }

        public String getGroup() {
            return group;
        }

        public String getName() {
            return name;
        }

        public String getNewname() {
            return newname;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public void setNewname(String newname) {
            this.newname = newname;
        }

        private String filternewname(String newnameunfiltered) {
            return newnameunfiltered;
        }
    }
}
