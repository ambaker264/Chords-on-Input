#include "Note.h"
#include "stdexcept"

Tone Note::convert_number_to_pitch(int number){
    if(number > 11 || number < 0){
        throw std::invalid_argument("Number is not between 0 and 11");
    }
    switch(number){
        case 0:
            return C;
        case 1:
            return Db;
         
    }
}