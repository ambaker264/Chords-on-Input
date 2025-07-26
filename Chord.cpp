#include "Chord.h"
#include "stdexcept"

static std::vector<Note> * minor_chord_add_3(std::vector<Note> * to_modify);
static std::vector<Note> * major_chord_add_3(std::vector<Note> * to_modify, bool * maj7);



Chord::Chord(std::string name, Chord_Purpose function, int rom_num, int octave){
    this->roman_numeral = rom_num;
    this->purpose = purpose;
    this->chord_name = name; //Hopefully this is memory safe?

    /*now we generate notes off of the chord name*/
    
    this->notes_in_chord = generate_chord_notes(name, octave);
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

    std::vector<Note> * out = new std::vector<Note>;
    int name_idx = 0, name_len = name.length();

    Note to_add;
    enum Tone root;
    to_add.octave = octave;

    bool flat = false, one_char_name;

    //If just saying smth like: C or Cb, then name[1] will fail. Need a check.

    if(name_len < 2){
        one_char_name = true;
    }else{
        one_char_name = false;
    }

    switch(name[name_idx]){
        case 'C':
        root = C;
        break;
        case 'D':
        if(one_char_name && name[++name_idx] == 'b'){
            root = Db;
            flat = true;
        }else{
            root = D;
        }
        break;
        case 'E':
        if(one_char_name && name[++name_idx] == 'b'){
            root = Eb;
            flat = true;
        }else{
            root = E;
        } 
        break;
        case 'F':
        root = F;
        break;
        case 'G':
        if(one_char_name && name[++name_idx] == 'b'){
            root = Gb;
            flat = true;
        }else{
            root = G;
        } 
        break;
        case 'A':
        if(one_char_name && name[++name_idx] == 'b'){
            root = Ab;
            flat = true;
        }else{
            root = A;
        } 
        break;
        case 'B':
        if(one_char_name && name[++name_idx] == 'b'){
            root = Bb;
            flat = true;
        }else{
            root = B;
        } 
        break;
        default:
        throw std::invalid_argument("Invalid Chord String, root is not a note.");
    }
    to_add.pitch = root;

    out->push_back(to_add);

    /*Now that we have the root, we need to add the 3rd, so that means another
    switch statement, check for major, minor, suspended. Only advance if found. 
    Lots and lots of checks here.*/

    //First we check if there is anything more to the chord.
    bool maj7 = false;

    if(++name_idx > name_len){
        /*This means we are simple 3 note major chord, root has been added so we 
        simply add the 3 and 5 and then ship.*/

        


    }

    switch(name[name_idx]){
        case 'm':
            /*Lots of things to check here. Cm7 is a C minor 7, but Cmaj or
            Cmin7 also work. Will need to update as we check further.*/
            break;
        case '^':
            major_chord_add_3(out, &maj7);
            break;
        case '-':
            //Minor chord, pretty simple.
            minor_chord_add_3(out);
            break;
        default:
        throw std::invalid_argument("Unrecognizable chord symbol.");
    }

    //Be sure to add 1 to octave if the 3rd is lower than the root. Root Position!



}

/*Helper functions for adding thirds to major and minor chords. Will modify the
pointer passed to them. Note: the vector should already have the root inside,
this will be read by the function, we don't want a segfault.*/
static std::vector<Note> * minor_chord_add_3(std::vector<Note> * to_modify){ 
    Tone root = to_modify->front().pitch;
    int note_number; //Convert number to pitch function.
    //Minor third is 3 semitones up from the root.
    
    

}

static std::vector<Note> * major_chord_add_3(std::vector<Note> * to_modify, bool * maj7){

}
