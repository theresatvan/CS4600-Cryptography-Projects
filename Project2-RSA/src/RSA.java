import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class RSA {
    private byte[] modulus;
    private byte[] publicKey;
    private byte[] privateKey;
    protected void getValuesFromKeyFile(String filepath) {
        try (FileInputStream fis = new FileInputStream(filepath)) {
            String lineSeparator = System.getProperty("line.separator");
            byte[] byteSeparator = lineSeparator.getBytes();

            List<byte[]> values = new ArrayList<byte[]>();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            boolean separatorDetected = false;

            int in;
            while ((in = fis.read()) != -1) {
                if (in == byteSeparator[0])
                    separatorDetected = true;
                if ((in == byteSeparator[1]) && separatorDetected) {
                    values.add(baos.toByteArray());
                    baos.reset();
                    separatorDetected = false;
                }
                else
                    baos.write(in);
            }
            this.modulus = Arrays.copyOfRange(values.get(0), 0, values.get(0).length - 1);
            this.publicKey = Arrays.copyOfRange(values.get(1), 0, values.get(1).length - 1);
            this.privateKey = Arrays.copyOfRange(values.get(2), 0, values.get(2).length - 1);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    protected byte[] getModulus() {
        return this.modulus;
    }

    protected byte[] getPublicKey() { return this.publicKey; }

    protected byte[] getPrivateKey() {
        return this.privateKey;
    }

    protected byte[] modAlgorithm(byte[] text, byte[] key, byte[] modulus) {
        BigInteger textBI = new BigInteger(text);
        BigInteger keyBI = new BigInteger(key);
        BigInteger modulusBI = new BigInteger(modulus);

        BigInteger resultBI = textBI.modPow(keyBI, modulusBI);

        return resultBI.toByteArray();
    }
}
