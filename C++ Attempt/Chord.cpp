#include "Chord.h"
#include "Note.h"
#include "stdexcept"

static std::vector<Note> * chord_add_interval(std::vector<Note> * to_modify, int interval);



Chord::Chord(std::string name, Chord_Purpose function, int rom_num, int octave){
    this->roman_numeral = rom_num;
    this->purpose = purpose;
    this->chord_name = name; //Hopefully this is memory safe?

    /*now we generate notes off of the chord name*/
    
    this->notes_in_chord = generate_chord_notes(name, octave);
}

Chord::Chord(std::string name, Chord_Purpose fucntion, int rom_num, 
const std::vector<Note> * notes_in_chord){
    this->roman_numeral = rom_num;
    this->purpose = purpose;
    this->chord_name = name;

    for(Note current : *notes_in_chord){
        this->notes_in_chord->push_back(current); //Check for copy safety!!!
    }


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

    out->push_back(to_add); //Passes by value, no pointers in Note so its fine.

    /*Now that we have the root, we need to add the 3rd, so that means another
    switch statement, check for major, minor, suspended. Only advance if found. 
    Lots and lots of checks here.*/

    //First we check if there is anything more to the chord.
    bool maj7 = false, augmented = false, flat_5 = false, diminished = false, 
    six_chord = false, seveth_exists = true, ninth_exists = false;

    if(++name_idx > name_len){
        /*This means we are simple 3 note major chord, root has been added so we 
        simply add the 3 and 5 and then ship.*/

        chord_add_interval(out, 4);
        chord_add_interval(out, 7);

        return out;
    }

    /*Now we are pretty sure we arent in root position, so we check for a 3rd or
    4th*/
    switch(name[name_idx]){
        case 'M':
            /*weirdass notation- CM7 is c major seven*/
        case 'm':
            /*Lots of things to check here. Cm7 is a C minor 7, but Cmaj or
            Cmin7 also work. Will need to update as we check further. May
            not be worth the work, maybe we parse the string to replace this.*/
            break;
        case 's':
            /*probably a suspended chord. need to check if it is sus2 or sus4 -
            other weird shit not tolerated*/

        case '^': //major 3rd
            chord_add_interval(out, 4);
            maj7 = true;
            break;
        case '-':
            //Minor chord, pretty simple.
            chord_add_interval(out, 3);
            break;
        case '+': //augmented
            chord_add_interval(out, 4);
            augmented = true;
            break;
        case '0': //half diminished - no C-7(b5) bs please, i don't wanna code that
            chord_add_interval(out, 3);
            flat_5 = true;
            break;
        case 'o': //diminished
            chord_add_interval(out, 3);
            flat_5 = true;
            diminished = true;
            break;
        case '6':
            chord_add_interval(out, 4);
            six_chord = true;
            seveth_exists = false;
            break;
        case '7':
            chord_add_interval(out, 4);
            break;
        case '5':
            //Power chord, no 3rd at all.
            seveth_exists = false;
            break;
        case '9':
            //Ninth implies all notes lower than it, including 7th.
            chord_add_interval(out, 4);
            ninth_exists = true;
            break;
        case '1':
            /*Check for 11th or 13th, but note to voice these properly we need
            to get rid of certain notes (like the 3rd). Not sure on that 
            implementation process, need to make a decision.*/
        default:
        throw std::invalid_argument("Unrecognizable chord symbol.");
    }

    //Now we handle the fifth. implied by the root with a couple exceptions.
    if(augmented){
        chord_add_interval(out, 8);
    }else if(flat_5){
        chord_add_interval(out, 6);
    }else{
        chord_add_interval(out, 7); //Perfect fifth, standard
    }

    /*Now we fully check for the existence of a seventh, and add it accordingly.*/


    /*We finish with extensions beyond the seventh. A number implies every note below it.*/

}

/*Helper functions for adding a note to a chord at a set interval from root. Will modify the
pointer passed to them. Note: the vector should already have the root inside,
this will be read by the function, we don't want a segfault.*/
static std::vector<Note> * chord_add_interval(std::vector<Note> * to_modify, int interval){ 
    Note * to_add = new Note();
    to_add->octave = to_modify->front().octave;
    Tone root = to_modify->front().pitch;
    int note_number = root + interval;
    if(note_number >= 12){
        note_number = note_number % 12;
        to_add->octave++;
    }
    to_add->pitch = Note::convert_number_to_pitch(note_number);
    to_modify->push_back(*to_add);
}

