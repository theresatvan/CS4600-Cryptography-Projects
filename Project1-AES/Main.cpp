#include <iostream>
#include <iomanip>
#include <fstream>
#include <string.h>

using namespace std;

void alter(unsigned char *array) {
    for (int i = 0; i < 16; i++) {
        array[i] = 1;
    }
}

int main() {

    unsigned char array[16];
    string keyInput;
    char inputArray[keyInput.length() + 1];
    char *pos = inputArray;
    

    cout << "Enter: ";
    getline(cin, keyInput);

    strcpy(inputArray, keyInput.c_str());

    for (size_t i = 0; i < 16; i++) {
        sscanf(pos, "%2hhx", &array[i]);
        pos += 2;
    }
    
    for (int i = 0; i < 16; i++) {
        cout << setw(2) << setfill('0') << hex << (unsigned int)array[i] << " ";
    }

    return 0;
}