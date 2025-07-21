#ifndef NOTE_H
#define NOTE_H

typedef enum Tone{
    C, Db, D, Eb, E, F, Gb, G, Ab, A, Bb, B 
} tone;


struct Note{
    int octave;
    tone pitch;
};
#endif