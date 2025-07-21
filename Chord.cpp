#include "Chord.h"
#include "stdexcept"


Chord::Chord(std::string chord_name, Chord_Purpose purpose, int rom_num, int octave){
    this->roman_numeral = rom_num;
    this->purpose = purpose;
    this->chord_name = chord_name; //Hopefully this is memory safe?

    /*now we generate notes off of the chord name*/
    
    this->notes_in_chord = generate_chord_notes(chord_name, octave);
}


std::vector<Note> * Chord::get_chord_notes(){
    //Deep copy - down to the notes, then return.
    std::vector<Note> *out = new std::vector<Note>;
    for(Note current : *this->notes_in_chord){
        out->push_back(current);
    }
    return out;
}

std::vector<Note> * Chord::generate_chord_notes(std::string name, int octave){

    /*First is the base note of the chord. This gets added automatically. If we 
    have a slash chord (G/D), the slash will be added an octave lower than the root.*/

    Note to_add;
    enum Tone root;
    to_add.octave = octave;

    bool flat = false;

    switch(name[0]){

        case C:
        root = C;
        break;
        case D:
        if(name[1] == 'b'){
            root = Db;
            bool flat = true;
        }else{
            root = D;
        }
        break;
        case E:
        if(name[1] == 'b'){
            root = Eb;
            bool flat = true;
        }else{
            root = E;
        } 
        break;
        case F:
        root = F;
        break;
        case G:
        if(name[1] == 'b'){
            root = Gb;
            bool flat = true;
        }else{
            root = G;
        } 
        break;
        case A:
        if(name[1] == 'b'){
            root = Ab;
            bool flat = true;
        }else{
            root = A;
        } 
        break;
        case B:
        if(name[1] == 'b'){
            root = Bb;
            bool flat = true;
        }else{
            root = B;
        } 
        break;
        default:
        throw std::invalid_argument("Invalid Chord String, root is not a note.");
    }
    to_add.pitch = root;
}