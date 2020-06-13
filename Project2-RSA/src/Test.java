import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;
import java.nio.*;

public class Test {
    public static void main(String[] args) throws IOException {
        String separator = System.getProperty("line.separator");
        byte[] lineSeparator = separator.getBytes();

        File keyFile = new File("key.dat");
        BigInteger p = BigInteger.valueOf(829);
        BigInteger q = BigInteger.valueOf(1697);
        BigInteger n = p.multiply(q);

        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = BigInteger.valueOf(619);

        BigInteger d = e.modInverse(phi);
        try {
            FileOutputStream fos = new FileOutputStream(keyFile);
            fos.write(n.toByteArray());
            fos.write(lineSeparator);
            fos.write(e.toByteArray());
            fos.write(lineSeparator);
            fos.write(d.toByteArray());
            fos.write(lineSeparator);
            fos.close();
        } catch (IOException io) {
            System.out.println(io.getMessage());
        }
        System.out.println("n: " + Arrays.toString(n.toByteArray()));
        System.out.println("e: " + Arrays.toString(e.toByteArray()));
        System.out.println("d: " + Arrays.toString(d.toByteArray()));
        System.out.println();

        List<byte[]> values = new ArrayList<byte[]>();
        try (FileInputStream fis = new FileInputStream(keyFile)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            boolean separatorDetected = false;

            int in;
            while ((in = fis.read()) != -1) {
                if (in == lineSeparator[0]) {
                    separatorDetected = true;
                }
                if ((in == lineSeparator[1]) && separatorDetected) {
                    values.add(baos.toByteArray());
                    baos.reset();
                    separatorDetected = false;
                }
                else
                    baos.write(in);
            }
        } catch (IOException io) {
            System.out.println(io.getMessage());
        }

        byte[] nB = Arrays.copyOfRange(values.get(0), 0, values.get(0).length - 1);
        byte[] eB = Arrays.copyOfRange(values.get(1), 0, values.get(1).length - 1);
        byte[] dB = Arrays.copyOfRange(values.get(2), 0, values.get(2).length - 1);

        System.out.println("n: " + Arrays.toString(nB));
        System.out.println("e: " + Arrays.toString(eB));
        System.out.println("d: " + Arrays.toString(dB));
        System.out.println();

        File messageFile = new File("message.txt");
        byte[] message = new byte[(int) messageFile.length()];
        FileInputStream srcFileFIS = new FileInputStream(messageFile);

        srcFileFIS.read(message);
        srcFileFIS.close();

        System.out.println(Arrays.toString(message));

        BigInteger messageBI = new BigInteger(message);
        BigInteger nBI = new BigInteger(nB);
        BigInteger eBI = new BigInteger(eB);
        BigInteger dBI = new BigInteger(dB);

        File outputFile = new File("output.txt");

        BigInteger cipherBI = messageBI.modPow(eBI, nBI);
        byte[] cipher = cipherBI.toByteArray();

        FileOutputStream fos = new FileOutputStream(outputFile);
        fos.write(cipher);
        fos.close();

        System.out.println(Arrays.toString(cipher) + "\n");

        byte[] ciphertext = new byte[(int) outputFile.length()];
        FileInputStream fisMess = new FileInputStream(outputFile);
        fisMess.read(ciphertext);
        fisMess.close();

        System.out.println(Arrays.toString(ciphertext));

        File decipherFile = new File("deciphered.txt");

        BigInteger cipherT = new BigInteger(ciphertext);
        BigInteger decryptBI = cipherT.modPow(dBI, nBI);

        byte[] decrypted = decryptBI.toByteArray();
        System.out.println("\n" + Arrays.toString(decrypted));

        FileOutputStream dfos = new FileOutputStream(decipherFile);
        dfos.write(message);
        dfos.close();
    }
}
