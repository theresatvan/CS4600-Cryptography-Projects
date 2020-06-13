CS 4600-01 Project 1
Theresa Van

There is only the source code in the zipped folder. To build the executable, input the following command into the command line:
"g++ AESEncrypt128.cpp AES.cpp -o AESEncrypt128 -D_GLIBCXX_USE_CXX11_ABI=0". To run the executable input the following command: 
"./AESEncrypt128.exe <file parameter>", where <file parameter> is the file you wish to encrypt. The program will then prompt
the user for a key. The key must be a 32-digit hex value. The encrypted file will be created in the same directory with the name
of the original file but a '.enc' extension.