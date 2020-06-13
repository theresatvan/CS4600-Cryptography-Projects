CS 4600-01 Project 1
Theresa Van

Compile all of the source code byt inputting each of the commands listed before running: <br> 
"javac RSA.java" <br>
"javac RSAGenKey.java" <br>
"javac RSAEncrypt.java" <br>
"javac RSADecrypt.java" <br>

To generate the key file input the following command: <br>
"java RSAGenKey KEYFILEPATH", where KEYFILEPATH is the file path to where you want to place your key file. <br>

To encrypt a file, input the following command: <br>
"javac RSAEncrypt SRCFILEPATH DESTFILEPATH KEYFILEPATH", where SRCFILEPATH is the file path to the source file you wish to encrypt, DESTFILEPATH is the file path you wish to create the encrypted message, and KEYFILEPATH is the key file path you wish to encrypt with.

To decrypt a file, input the following command: <br>
"javac RSADecrypt SRCFILEPATH DESTFILEPATH KEYFILEPATH, where SRCFILEPATH is the file path you wish to decrypt, DESTFILEPATH is the file path you wish to create the decrypted file, and KEYFILEPATH is the key file path you wish to decrypt with.
