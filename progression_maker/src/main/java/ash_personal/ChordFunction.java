package ash_personal;

import org.jfugue.theory.Scale;

public enum ChordFunction {
    TONIC(0), SUBDOMINANT(1), DOMINANT(2), UNKNOWN(3);

    private final int value;

    private ChordFunction(int value){
        this.value = value;
    }

    public int get_value(){
        return this.value;
    }

    /**
     * Returns the chord's function based on common understanding of music theory.
     * i.e. GDOM7 is DOMINANT in C major. 
     * Only implemented using major and minor keys so far, with only diatonic chords.
     * @param chord
     * @return
     */
    public static ChordFunction get_chord_function(ChordInContext chord){
        String rom_num = chord.get_rom_num();
        if(chord.get_key().getScale().equals(Scale.MAJOR)){
            return switch (rom_num) {
                case "I", "IMAJ7", "I6" -> ChordFunction.TONIC;
                case "bII7" -> ChordFunction.DOMINANT;
                case "ii", "ii7, iv, iv7" -> ChordFunction.SUBDOMINANT;
                case "iii", "iii7" -> ChordFunction.TONIC;
                case "IVMAJ7", "IV" -> ChordFunction.SUBDOMINANT;
                case "V", "V7" -> ChordFunction.DOMINANT;
                case "vi", "vi7" -> ChordFunction.TONIC;
                case "VIIDIM7", "bvii7" -> ChordFunction.DOMINANT;
                default -> ChordFunction.UNKNOWN;
            };
        } else if(chord.get_key().getScale().equals(Scale.MINOR)){
            return switch(rom_num){
                case "i", "iii"-> ChordFunction.TONIC;
                case "iv", "IIDIM7", "IIDIM", "VI", "VIMAJ7" -> ChordFunction.SUBDOMINANT;
                case "bVIMAJ7", "bVI" -> ChordFunction.SUBDOMINANT;
                case "v", "V7", "v7", "VIIMAJ7", "VII", "VIIDIM7", "VIIDIM" -> ChordFunction.DOMINANT;
                default -> ChordFunction.UNKNOWN;
            };
        }
        return null; //failure state
    }
}
