import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class RSAEncrypt extends RSA{
    public static void main(String[] args) throws IOException {
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
                byte[] message = getMessage(sourceFile);

                RSA rsa = new RSA();
                rsa.getValuesFromKeyFile(keyFilepath);
                byte[] modulus = rsa.getModulus();
                byte[] publicKey = rsa.getPublicKey();

                byte[] ciphertext = rsa.modAlgorithm(message, publicKey, modulus);

                File destFile = new File(destFilepath);
                writeCiphertext(destFile, ciphertext);
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

    private static byte[] getMessage(File sourceFile) {
        byte[] message = new byte[(int) sourceFile.length()];
        try {
            FileInputStream srcFileFIS = new FileInputStream(sourceFile);

            srcFileFIS.read(message);
            srcFileFIS.close();

        } catch (IOException io) {
            System.out.println(io.getMessage());
        }

        return message;
    }

    private static void writeCiphertext(File destFile, byte[] ciphertext) {
        try {
            FileOutputStream fos = new FileOutputStream(destFile);
            fos.write(ciphertext);
            fos.close();
        } catch (IOException io) {
            System.out.println(io.getMessage());
        }
    }
}
