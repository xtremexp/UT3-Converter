/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut3;

/**
 *
 * @author Hyperion
 */
public class TerrainMaterial {

    public final String mappingtype_TMT_Auto="TMT_Auto";
    public final String mappingtype_TMT_XY="TMT_XY";
    public final String mappingtype_TMT_XZ="TMT_XZ";
    public final String mappingtype_TMT_YZ="TMT_YZ";
    public final String mappingtype_TMT_defaut=this.mappingtype_TMT_Auto;

    String TMName;
    /**
     * Mapping type for this terrain material
     */
    String mappingtype=this.mappingtype_TMT_Auto;
    Float mappingscale=4F;
    /**
     * Value bewteen 0 and 360 (degrees)
     */
    Float mappingrotation=0F;
    FoliageMesh fm;
    
    public TerrainMaterial(boolean bhasfoliage) {
        if(bhasfoliage)
        {
            fm = new FoliageMesh(null);
        }
    }



    public void addFoliageMesh()
    {
        
    }
    public Float getMappingscale() {
        return mappingscale;
    }

    public void setMappingscale(Float mappingscale) {
        this.mappingscale = mappingscale;
    }

    public String getMappingtype() {
        return mappingtype;
    }

    public void setMappingtype(String mappingtype) {
        this.mappingtype = mappingtype;
    }

    public Float getMappingrotation() {
        return mappingrotation;
    }

    public void setMappingrotation(Float mappingrotation) {
        this.mappingrotation = mappingrotation;
    }

    public FoliageMesh getFm() {
        return fm;
    }

    
    public class FoliageMesh
    {
        /**
         * E.G.: "StaticMesh'NodeBuddies.3D_Icons.NodeBuddy_AutoAdjust'"
         */
        String staticmesh;
        String material;
        float density=0;
        float maxdrawradius=1024F;
        float mintransitionradius=0F;
        float minscale=1F;
        float maxscale=1F;

        public FoliageMesh(String staticmesh) {
            this.staticmesh = staticmesh;
        }

        public float getDensity() {
            return density;
        }

        public void setDensity(float density) {
            this.density = density;
        }

        public float getMaxdrawradius() {
            return maxdrawradius;
        }

        public void setMaxdrawradius(float maxdrawradius) {
            this.maxdrawradius = maxdrawradius;
        }

        public float getMaxscale() {
            return maxscale;
        }

        public void setMaxscale(float maxscale) {
            this.maxscale = maxscale;
        }

        public float getMinscale() {
            return minscale;
        }

        public void setMinscale(float minscale) {
            this.minscale = minscale;
        }

        public float getMintransitionradius() {
            return mintransitionradius;
        }

        public void setMintransitionradius(float mintransitionradius) {
            this.mintransitionradius = mintransitionradius;
        }

        public String getStaticmesh() {
            return staticmesh;
        }

        public void setStaticmesh(String staticmesh) {
            this.staticmesh = staticmesh;
        }

        
    }

}
