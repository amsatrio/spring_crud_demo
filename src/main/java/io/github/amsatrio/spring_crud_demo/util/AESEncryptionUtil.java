package io.github.amsatrio.spring_crud_demo.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AESEncryptionUtil {
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String AES_IV = "KEYFtYaCSpMD9wm5";

    /// Encrypt data using AES encryption with a provided key and IV
    public static String encrypt(String plaintext, String stringKey) throws Exception {
        SecretKey secretKey = new SecretKeySpec(stringKey.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(AES_IV.getBytes(StandardCharsets.UTF_8));

        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /// Decrypt data using AES decryption with a provided key and IV
    public static String decrypt(String ciphertext, String stringKey) throws Exception {
        SecretKey secretKey = new SecretKeySpec(stringKey.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(AES_IV.getBytes(StandardCharsets.UTF_8));

        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
