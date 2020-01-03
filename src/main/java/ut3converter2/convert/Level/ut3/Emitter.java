/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.convert.Level.ut3;

import ut3converter2.convert.Level.UTActor;

/**
 *
 * @author Hyperion
 */
public class Emitter extends UTActor{

    public static String em_type_smoke="Envy_Effects.Smoke.Effects.P_EFX_Smoke_Drift_Building_01";
    String em_type="";
    String otherdata="";
    
    public Emitter() {
        this.em_type = em_type_smoke;
    }

     public void AnalyseT3DData(String line)
    {
        parseOtherData(line);
    }


    public String toString()
    {
        String tmp="";
        tmp +="      Begin Actor Class=Emitter Name=Emitter_0 Archetype=Emitter\'Engine.Default__Emitter\'\n";
        tmp +="         Begin Object Class=ParticleSystemComponent Name=ParticleSystemComponent0 ObjName=ParticleSystemComponent_0 Archetype=ParticleSystemComponent\'Engine.Default__Emitter:ParticleSystemComponent0\'\n";
        tmp +="            Template=ParticleSystem\'"+em_type+"\'\n";
        tmp +="            LightingChannels=(bInitialized=True,Dynamic=True)\n";
        tmp +="            Name=\"ParticleSystemComponent_0\"\n";
        tmp +="            ObjectArchetype=ParticleSystemComponent\'Engine.Default__Emitter:ParticleSystemComponent0\'\n";
        tmp +="         End Object\n";
        tmp += getOtherdata();
        tmp +="         ObjectArchetype=Emitter\'Engine.Default__Emitter\'\n";
        tmp +="      End Actor\n";

        return tmp;
    }


}
