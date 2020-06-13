/***************************************************************
 * author: Theresa Van
 * filename: RSAGenKey.java
 * purpose: Generate an RSA keypair and store it to a binary file.
 *      The file will contain n, e, and d in that order. e will
 *      be the value 2^16 + 1 = (65537). The program will take a
 *      single command line argument for the file path to which
 *      the key file should be stored.
 ***************************************************************/
import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.math.BigInteger;

public class RSAGenKey {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println(" Invalid command line argument.\nProgram terminated...");
        }
        else {
            String filepath = args[0];
            if (!isPathValid(filepath)) {
                System.out.println(" Invalid command line argument.\nProgram terminated...");
            }
            else {
                File keypairFile = new File(filepath);
                byte[] p = getLargePrime();
                byte[] q = getLargePrime();
                while(Arrays.equals(p, q)) {
                    q = getLargePrime();
                }

                byte[] modulus = multiplyByteArrays(p, q);
                byte[] phi = calculatePhi(p, q);
                byte[] publicKey = setPublicKey();
                byte[] privateKey = setPrivateKey(publicKey, phi);

                writeToKeyFile(keypairFile, modulus, publicKey, privateKey);
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

    private static byte[] getLargePrime() {
        Random random = new Random();
        byte[] prime = new byte[65];
        boolean positivePrime = false;

        do {
            random.nextBytes(prime);
            BigInteger primeBI = new BigInteger(prime);

            if ((primeBI .compareTo(BigInteger.ZERO) > 0) && (primeBI.isProbablePrime(1)) && (primeBI.compareTo(BigInteger.valueOf(2^513)) < 0))
                positivePrime = true;
        } while (!positivePrime);

        return prime;
    }

    private static byte[] multiplyByteArrays(byte[] a, byte[] b) {
        BigInteger aBI = new BigInteger(a);
        BigInteger bBI = new BigInteger(b);

        return aBI.multiply(bBI).toByteArray();
    }

    private static byte[] calculatePhi(byte[] a, byte[] b) {
        BigInteger aBI = new BigInteger(a);
        BigInteger bBI = new BigInteger(b);

        aBI = aBI.subtract(BigInteger.ONE);
        bBI = bBI.subtract(BigInteger.ONE);

        return aBI.multiply(bBI).toByteArray();
    }

    private static byte[] setPublicKey() {
        BigInteger bi = BigInteger.valueOf(65537);

        return bi.toByteArray();
    }

    private static byte[] setPrivateKey(byte[] e, byte[] phi) {
        BigInteger eBI = new BigInteger(e);
        BigInteger phiBI = new BigInteger(phi);
        BigInteger dBI = eBI.modInverse(phiBI);

        return dBI.toByteArray();
    }

    private static void writeToKeyFile(File keypairFile, byte[] n, byte[] e, byte[] d) {
        try {
            String lineSeparator = System.getProperty("line.separator");

            FileOutputStream fos = new FileOutputStream(keypairFile);
            fos.write(n);
            fos.write(lineSeparator.getBytes());
            fos.write(e);
            fos.write(lineSeparator.getBytes());
            fos.write(d);
            fos.write(lineSeparator.getBytes());
            fos.close();
        } catch (IOException io) {
            System.out.println(io.getMessage());
        }
    }
}
