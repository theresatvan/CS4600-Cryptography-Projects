import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.*;
import java.nio.*;

public class Read {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("decrypted.txt");
        byte[] message = Files.readAllBytes(path);

        String str = new String(message, StandardCharsets.UTF_8);
        System.out.println(str);
    }
}
