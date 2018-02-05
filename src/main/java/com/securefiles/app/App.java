package com.securefiles.app;

import java.io.*;
import java.util.Properties;

public class App {
  public static Properties getAppProperties() {
    Properties appProps = null;

    try {
      InputStream appConfigStream = App.class.getClassLoader().getResourceAsStream("fileManager.properties");
      appProps = new Properties();
      appProps.load(appConfigStream);

    } catch (Exception ex) {
      System.out.println("Problem reading fileManager properties");
      System.out.println(ex);
    }

    return appProps;
  }

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

      // TODO: Research better way of managing .properties file. It should
      // probably exist in a standalone folder, and there should be
      // a class for abstraction.
      Properties appProps = getAppProperties();
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
