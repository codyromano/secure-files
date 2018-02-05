package com.securefiles.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import java.util.Arrays;
 
public class CryptoUtils {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";
 
    public static void encrypt(String salt, String key, File inputFile, File outputFile)
            throws CryptoException {
        doCrypto(salt, Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
    }
 
    public static void decrypt(String salt, String key, File inputFile, File outputFile)
            throws CryptoException {
        doCrypto(salt, Cipher.DECRYPT_MODE, key, inputFile, outputFile);
    }

    private static Key getSecretKey(String salt, String userKeyString) {
      byte[] key = null;

      try {
        // Hash the user's password
        key = (salt + userKeyString).getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-256");

        key = sha.digest(key);
        // Use only the first 128 bits
        key = Arrays.copyOf(key, 16);

      } catch (Exception ex) {
        System.out.println("Problem generating secret key");
        System.out.println(ex);
      }

      return new SecretKeySpec(key, ALGORITHM);
    }
 
    private static void doCrypto(String salt, int cipherMode, String key, File inputFile,
            File outputFile) throws CryptoException {
        try {
            Key secretKey = getSecretKey(salt, key);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);
             
            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);
             
            byte[] outputBytes = cipher.doFinal(inputBytes);
             
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);
             
            inputStream.close();
            outputStream.close();
             
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException ex) {
            throw new CryptoException("Error encrypting/decrypting file. Most likely an INVALID PASSWORD", ex);
        }
    }
}
