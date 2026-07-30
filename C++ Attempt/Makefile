CC = g++
#note that we may need to specify a C++ version that we are using.
CPPFLAGS = -g -Wall -O0 -Wshadow -Wwrite-strings

.PHONY: clean

test: Chord.o Note.o Progression_Maker.o tests.o
	$(CC) -o test_executable Chord.o Note.o Progression_Maker.o tests.o

Note.o: Note.h Note.cpp
	$(CC) $(CPPFLAGS) -c Note.cpp

Chord.o: Note.h Chord.h Chord.cpp
	$(CC) $(CPPFLAGS) -c Chord.cpp

Progression_Maker.o: Note.h Chord.h Progression_Maker.cpp Progression_Maker.h
	$(CC) $(CPPFLAGS) -c Progression_Maker.cpp

tests.o: Note.h Chord.h Progression_Maker.h tests.cpp
	$(CC) $(CPPFLAGS) -c tests.cpp

clean:
	@echo "Removing all .o and executable files"
	rm -f *.o test