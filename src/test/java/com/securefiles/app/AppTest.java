package com.securefiles.app;

import org.junit.jupiter.api.Test;
import java.util.Properties;
import java.lang.Exception;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AppTest {
  private App app;
  private static String TEST_FILE_ENCODING = "UTF-8";
  private static String PASS_PHRASE = "This is my pass phrase!";

  // TODO: Move this exception elsewhere
  private class CryptoTestIOException extends Exception {
    public CryptoTestIOException(String message) {
      super(message);
    }
  }

  /**
  * @todo Move to utils
  * @throws CryptoTestIOException if test files cannot be read or written in the filesystem
  */
  private File createTestFile(String fileName, String content) throws CryptoTestIOException {
    PrintWriter writer = null;

    try {
      writer = new PrintWriter(fileName, AppTest.TEST_FILE_ENCODING);
      writer.println(content);
    } catch (Exception ex) {
      throw new CryptoTestIOException("Error creating test file " + fileName + ". " +
        "Could not perform the necessary I/O operations.");
    } finally {
      if (writer != null) {
        writer.close();
      }
    }

    return new File(fileName);
  }

  @Test
  public void testGetAppProperties() {
    Properties testProperties = app.getAppProperties();
    assertNotNull(testProperties.get("salt"));
  }

  @Test
  public void testCreateFileWithEncryptedContents() throws CryptoTestIOException {
    // Create a plaintext file for testing
    String plainTextFileName = "secret.txt";
    String plainText = "My secret content!";

    File plainTextFile = createTestFile(plainTextFileName, plainText);

    // Encrypt the file
    String encryptedFilePath = app.handleCryptoRequest(
      plainTextFile,
      AppTest.PASS_PHRASE,
      App.ACTION_ENCRYPT,
      plainTextFileName
    );

    try {
      String encryptedContents = new String( Files.readAllBytes( Paths.get(encryptedFilePath)) );

      // Ensure the encrypted content is a non-empty string that differs from the plain text
      assertEquals(encryptedContents.length() > 0, true);
      assertEquals(encryptedContents.equals(plainText), false);

    } catch (IOException ex) {
      throw new CryptoTestIOException("Cannot read encrypted file at path " + encryptedFilePath);
    }
  }

  @Test
  public void testDecryptAndReadEncryptedFile() throws CryptoTestIOException {
    // TODO: Implement test
  }
}
