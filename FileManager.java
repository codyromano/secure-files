import java.io.*;
import java.util.Properties;

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

      String rootPath = Thread.currentThread().getContextClassLoader()
        .getResource("").getPath();
      String appConfigPath = rootPath + "fileManager.properties";

      Properties appProps = new Properties();
      appProps.load(new FileInputStream(appConfigPath));

      String salt = appProps.getProperty("salt");

      switch (action) {
        case "encrypt":
          CryptoUtils.encrypt(salt, key, fileInput, outputFile);
        break;
        case "decrypt":
          CryptoUtils.decrypt(salt, key, fileInput, outputFile);
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

  private static String getConfirmedUserInput(String labelText,
    String confirmLabelText) {
    Console console = System.console();
    String userInput = new String(console.readPassword(labelText));
    String confirmUserInput = new String(console.readPassword(confirmLabelText));

    if (userInput.equals(confirmUserInput)) {
      return userInput;
    }
    System.out.println("Confirmation failed. You entered different values.");
    return getConfirmedUserInput(labelText, confirmLabelText);
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

    String key = getConfirmedUserInput("Passphrase: ", "Confirm Passphrase: ");
    handleCryptoRequest(fileInput, key, action, fileInput.getName());
  }
}
