#include "Note.h"
#include "stdexcept"

Tone Note::convert_number_to_pitch(int number){
    switch(number){
        case 0:
            return C;
        case 1:
            return Db;
        case 2:
            return D;
        case 3:
            return Eb;
        case 4:
            return E;
        case 5:
            return F;
        case 6:
            return Gb;
        case 7:
            return G;
        case 8:
            return Ab;
        case 9:
            return A;
        case 10:
            return Bb;
        case 11:
            return B;
        default:
            throw std::invalid_argument("Number is not between 0 and 11"); 
    }
    return C; //Code will never get here, just a catch for syntax.
}

int Note::compare_to(const Note * other){
    int current_obj_val, other_val;
    current_obj_val = (this->octave * 12) + this->pitch;
    other_val = (other->octave * 12) + other->pitch;
    return current_obj_val - other_val;
}