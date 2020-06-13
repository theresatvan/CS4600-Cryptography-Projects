import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class RSADecrypt extends RSA {
    public static void main(String[] args) {
        if (args.length != 3)
            System.out.println(" Invalid command line argument.\nProgram terminated...");
        else {
            String sourceFilepath = args[0];
            String destFilepath = args[1];
            String keyFilepath = args[2];

            if ((!isPathValid(sourceFilepath)) || (!isPathValid(destFilepath)) || (!isPathValid(keyFilepath)))
                System.out.println(" Invalid command line argument.\nProgram terminated...");

            else {
                File sourceFile = new File(sourceFilepath);
                byte[] ciphertext = getCiphertext(sourceFile);

                RSA rsa = new RSA();
                rsa.getValuesFromKeyFile(keyFilepath);
                byte[] modulus = rsa.getModulus();
                byte[] privateKey = rsa.getPrivateKey();

                byte[] message = rsa.modAlgorithm(ciphertext, privateKey, modulus);

                File destFile = new File(destFilepath);
                writeMessage(destFile, message);
            }
        }
    }

    private static boolean isPathValid(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException ex) {
            return false;
        }
        return true;
    }

    private static byte[] getCiphertext(File sourceFile) {
        byte[] ciphertext = new byte[(int) sourceFile.length()];
        try {
            FileInputStream srcFileFIS = new FileInputStream(sourceFile);

            srcFileFIS.read(ciphertext);
            srcFileFIS.close();

        } catch (IOException io) {
            System.out.println(io.getMessage());
        }

        return ciphertext;
    }

    private static void writeMessage(File destFile, byte[] message) {
        try {
            FileOutputStream fos = new FileOutputStream(destFile);
            fos.write(message);
            fos.close();
        } catch (IOException io) {
            System.out.println(io.getMessage());
        }
    }
}
