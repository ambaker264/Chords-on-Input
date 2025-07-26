#ifndef NOTE_H
#define NOTE_H

typedef enum Tone{
    C, Db, D, Eb, E, F, Gb, G, Ab, A, Bb, B 
} tone;


struct Note{
    int octave;
    tone pitch;
    /*Converts a note number to a pitch. Anything outside 0-11 will fail.*/
    Tone convert_number_to_pitch(int number);
};
#endif