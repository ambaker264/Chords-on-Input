#include "Note.h"
#include "Chord.h"
#include "Progression_Maker.h"
#include "assert.h"

int chord_builder_1_3_5_test();
int chord_constructor_copy_test();


/*Running Tests requires a main function, makes sense.*/
int main(){
    chord_builder_1_3_5_test();
    chord_constructor_copy_test();
    return 0;
}

//extremely basic constructor test.
int chord_builder_1_3_5_test(){
    //Checking some specific functions that I want to use.
    printf("---TEST 1---\nShould print C1, E1, G1\n");

    Chord test_chord = Chord("C", TONIC, 1, 1);

    //C1 chord, has C, E, G in it.
    
    return 0;
}

int chord_constructor_copy_test(){
    printf("---TEST 2---\nChecking that deep copies happen in chord constructor.\n");


    return 0;
}