#ifndef NOTE_H
#define NOTE_H

typedef enum Tone{
    C = 0, Db = 1, D = 2, Eb = 3, E = 4, F = 5, Gb = 6, G = 7, Ab = 8, A = 9, 
    Bb = 10, B = 11 
} tone;


struct Note{
    int octave;
    tone pitch;
    /*Converts a note number to a pitch. Anything outside 0-11 will fail.*/
    static Tone convert_number_to_pitch(int number); 
    /*Compares notes by how high they are in relation to another note. If current
    object is lower, we get negative, if higher we get positive. If equal, 0.
    
    i.e. D1 as current object and E4 as other will return -38*/
    int compare_to(const Note * other);
};
#endif