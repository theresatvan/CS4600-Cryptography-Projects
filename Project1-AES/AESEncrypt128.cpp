#include "AES.h"
#include <iostream>
#include <iomanip>
#include <string.h>
#include <stdio.h>

using namespace std;

int main(int argc, char *argv[]) {
    unsigned char key[16];
    string src_filepath(argv[1]);
    string keyInput;
    char inputArray[keyInput.length() + 1];
    char *pos = inputArray;
    bool validInput = false;
    
    do {
    cout << " Enter 128-bit key (32-digit hex value): ";
    getline(cin, keyInput);

    if (keyInput.length() != 32) {
        cout << "Invalid key input." << endl;
    }
    else {
        if (keyInput.find_first_not_of("0123456789abcdefABCDEF") != string::npos) {
            cout << "Non-hexadecimal input." << endl;
        }
        else {
            validInput = true;
            strcpy(inputArray, keyInput.c_str());

        for (size_t i = 0; i < 16; i++) {
            sscanf(pos, "%2hhx", &key[i]);
            pos += 2;
        }

            AES aes;

            aes.encrypt(src_filepath, key);

        }
    }
    } while (!validInput);

    return 0;
}