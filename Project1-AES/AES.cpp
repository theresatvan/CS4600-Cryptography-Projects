#include "AES.h"

void AES::padBlock(unsigned char *buffer, int n) {
    int remainingSpace = 16 - n;

    for (int i = n - 1; i < 16; i++) {
        buffer[i] = remainingSpace;
    }
}

void AES::xorCalc(unsigned char *result, unsigned char *factor) {
    for (size_t i = 0; i < sizeof(result); i++) {
        result[i] = result[i] ^ factor[i];
    }
}

void AES::gFunction(unsigned char *word, unsigned char rcoef) {
    unsigned char switchWord = word[0];
    word[0] = word[1];
    word[1] = word[2];
    word[2] = word[3];
    word[3] = switchWord;

    for (int i = 0; i < 4; i++) {
        word[i] = sbox[(unsigned int) word[i]];
    }

    word[0] = word[0] ^ rcoef;
}

void AES::keyTransform(unsigned char *key, int round) {
    unsigned char word1[4], word2[4], word3[4], word4[4], gword[4];
    copy(key, key + 4, word1);
    copy(key + 4, key + 8, word2);
    copy(key + 8, key + 12, word3);
    copy(key + 12, key + 16, word4);
    copy(key + 12, key + 16, gword);

    gFunction(gword, rcon[round]);

    xorCalc(word4, word3);
    xorCalc(word3, word2);
    xorCalc(word2, word1);
    xorCalc(word1, gword);

    copy(word1, word1 + 4, key);
    copy(word2, word2 + 4, key + 4);
    copy(word3, word3 + 4, key + 8);
    copy(word4, word4 + 4, key + 12);
}

unsigned char ** AES::getKeySchedule(unsigned char *key) {
    unsigned char **key_schedule = new unsigned char *[11];
    unsigned char key_whitening[16];
    copy(key, key + 16, key_whitening);

    key_schedule[0] = new unsigned char[16];
    copy(key_whitening, key_whitening + 16, key_schedule[0]);

    for (int i = 1; i < 11; i++) {
        key_schedule[i] = new unsigned char[16];
        keyTransform(key_whitening, i);
        copy(key_schedule[i], key_schedule[i] + 16, key_whitening);
    }

    return key_schedule;
}

void AES::keyAddition(unsigned char *key, unsigned char *buffer) {
    xorCalc(buffer, key);
}

void AES::byteSubstitution(unsigned char *buffer) {
    for (int i = 0; i < 16; i++) {
        buffer[i] = sbox[(int)buffer[i]];
    }
}

void AES::shiftRows(unsigned char *buffer) {
    unsigned char temp[16];

    temp[0] = buffer[0];
    temp[1] = buffer[5];
    temp[2] = buffer[10];
    temp[3] = buffer[15];
    temp[4] = buffer[4];
    temp[5] = buffer[9];
    temp[6] = buffer[14];
    temp[7] = buffer[3];
    temp[8] = buffer[8];
    temp[9] = buffer[13];
    temp[10] = buffer[2];
    temp[11] = buffer[7];
    temp[12] = buffer[12];
    temp[13] = buffer[1];
    temp[14] = buffer[6];
    temp[15] = buffer[11];
}

void AES::mixColumns(unsigned char *input) {
    unsigned char tmp[16];
    int i;
    for (i = 0; i < 4; ++i) {
        tmp[(i << 2) + 0] = (unsigned char) (mul2[input[(i << 2) + 0]] ^ mul_3[input[(i << 2) + 1]] ^ input[(i << 2) + 2] ^ input[(i << 2) + 3]);
        tmp[(i << 2) + 1] = (unsigned char) (input[(i << 2) + 0] ^ mul2[input[(i << 2) + 1]] ^ mul_3[input[(i << 2) + 2]] ^ input[(i << 2) + 3]);
        tmp[(i << 2) + 2] = (unsigned char) (input[(i << 2) + 0] ^ input[(i << 2) + 1] ^ mul2[input[(i << 2) + 2]] ^ mul_3[input[(i << 2) + 3]]);
        tmp[(i << 2) + 3] = (unsigned char) (mul_3[input[(i << 2) + 0]] ^ input[(i << 2) + 1] ^ input[(i << 2) + 2] ^ mul2[input[(i << 2) + 3]]);
    }

    for (i = 0; i < 16; ++i)
        input[i] = tmp[i];
}

void AES::encrypt(string src_filepath, unsigned char *key) {
    string dst_filepath = src_filepath.substr(0, src_filepath.find_last_of('.')) + ".enc";

    unsigned char **key_schedule = getKeySchedule(key);

    ifstream src_file_descriptor(src_filepath.c_str(), ios::binary);
    ofstream dst_file_descriptor(dst_filepath.c_str(), ios::binary);

    unsigned char f_buffer[16];
    bool padding = false;
    
    while (!src_file_descriptor.eof()) {
        src_file_descriptor.read((char *)&f_buffer, 16);

        int bytes_read = src_file_descriptor.gcount();

        if (bytes_read < 16) {
            padBlock(f_buffer, bytes_read);
            padding = true;
        }
        
        keyAddition(key_schedule[0], f_buffer);

        for (int i = 1; i <= 9; i++) {
            byteSubstitution(f_buffer);
            shiftRows(f_buffer);
            mixColumns(f_buffer);
            keyAddition(key_schedule[i], f_buffer);
        }

        byteSubstitution(f_buffer);
        shiftRows(f_buffer);
        keyAddition(key_schedule[10], f_buffer);

        dst_file_descriptor.write((char *)&f_buffer, 16);
    }


    if (!padding) {
        padBlock(f_buffer, 0);

        keyAddition(key_schedule[0], f_buffer);

        for (int i = 1; i <= 9; i++) {
            byteSubstitution(f_buffer);
            shiftRows(f_buffer);
            mixColumns(f_buffer);
            keyAddition(key_schedule[i], f_buffer);
        }

        byteSubstitution(f_buffer);
        shiftRows(f_buffer);
        keyAddition(key_schedule[10], f_buffer);

        dst_file_descriptor.write((char *)&f_buffer, 16);
    }

    for (int i = 0; i < 11; i++) {
        delete[] key_schedule[i];
    }
    delete[] key_schedule;

    src_file_descriptor.close();
    dst_file_descriptor.close();
}