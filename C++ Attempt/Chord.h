#include "Note.h"
#include "string"
#include "vector"

#ifndef CHORD_H
#define CHORD_H

enum Chord_Purpose{
    TONIC, SUBDOMINANT, DOMINANT
}purpose;

enum Mode{
    IONIAN, DORIAN, PHRYGIAN, LYDIAN, MIXOLYDIAN, AEOLIAN, LOCRIAN, HARM_MINOR
}mode;


class Chord{

    std::vector<Note> * notes_in_chord;

    /*ie. Dmin is the II of C major. Number represents that.*/
    int roman_numeral;
    Chord_Purpose purpose;

    /*Follow Jazz symbol, like in musescore. i.e. C^7(b9) is C major seven with a
    flat ninth.*/
    std::string chord_name;
    
    /*Generates a chord from its name. Has an expected format. Voicing will be tricky.
    Start with base position voicings, then we can modify.
    
    Also RENAMES the chord based on given name. We want standardization here! 
    */
    static std::vector<Note> * generate_chord_notes(std::string name, int octave);

    public:
    /*Generates a list of strings that have the chord name of all possible next
    chords. Generally try not to use?*/
    std::vector<std::string> * possible_next_chords(tone root, Mode key, int steps_to_finish);
    std::vector<Note> * get_chord_notes(); //should be a deep copy
    /*What we want to use generally, good for generating based on what next.*/
    Chord * generate_chord(Note root, Mode key, Chord * preceding, Chord_Purpose needed_function);
    /*General chord constructor, has the most detail of all of them.*/
    Chord(std::string chord_name, Chord_Purpose puropose, int rom_num, int octave);
    /*Overload of general so you can set chords from a manual list of notes.*/
    Chord(std::string name, Chord_Purpose fucntion, int rom_num, const std::vector<Note> * notes_in_chord);
    /*Prints all relevant data to the chord to cout.*/
    Chord * print_chord_data();

   //NEEDS A COPY CONSTRUCTOR!!! 
};
#endif