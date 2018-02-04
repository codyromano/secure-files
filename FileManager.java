import java.io.*;

public class FileManager {
  public static void handleCryptoRequest(File fileInput, String key,
    String action, String fileName) {

    try {
      // Create separate directories for "encrypt" and "decrypt" as needed
      File directory = new File(action);
      if (!directory.exists()) {
        directory.mkdirs();
      }

      String outputFileName = action + "/" + fileName;
      File outputFile = new File(outputFileName);

      switch (action) {
        case "encrypt":
          CryptoUtils.encrypt(key, fileInput, outputFile);
        break;
        case "decrypt":
          CryptoUtils.decrypt(key, fileInput, outputFile);
        break;
        default:
          System.out.println("Unknown action \"" + action + "\"");
        break;
      }

      if (outputFileName != null) {
        System.out.println("Created file " + outputFileName);
      }

    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      ex.printStackTrace();
    }
  }

  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("Must provide an action and a file name");
      return;
    }

    String action = args[0];
    String fileName = args[1];
    File fileInput = new File(fileName);

    if (!fileInput.exists()) {
      System.out.println("Cannot find the file \"" + fileName + "\"");
      return;
    }

    Console console = System.console();
    String key = new String(console.readPassword("Encryption passphrase: "));
    handleCryptoRequest(fileInput, key, action, fileInput.getName());
  }
}
