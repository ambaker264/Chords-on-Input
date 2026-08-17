package ash_personal;

import org.jfugue.theory.Scale;

public enum ChordFunction {
    DOMINANT, SUBDOMINANT, TONIC, UNKNOWN;

    /**
     * Returns the chord's function based on common understanding of music theory.
     * i.e. GDOM7 is DOMINANT in C major. 
     * Only implemented using major and minor keys so far.
     * @param chord
     * @return
     */
    public static ChordFunction get_chord_function(ChordInContext chord){
        String rom_num = chord.get_rom_num();
        if(chord.get_key().getScale().equals(Scale.MAJOR)){
            switch(rom_num){
                case "I", "IMAJ7", "I6":
                    return ChordFunction.TONIC;
                case "bII7":
                    return ChordFunction.DOMINANT;
                case "ii", "ii7":
                    return ChordFunction.SUBDOMINANT;
                case "iii", "iii7":

                

                default:
                    return ChordFunction.UNKNOWN;
            }            

        }else if(chord.get_key().getScale().equals(Scale.MINOR)){

        }
        return null; //failure state
    }
}
