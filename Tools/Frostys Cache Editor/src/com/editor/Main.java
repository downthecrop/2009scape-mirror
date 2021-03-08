package com.editor;

import com.editor.Console;
import com.editor.ToolSelection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class Main {
   public static void main(String[] args) {
      Console.redirectSystemStreams();
      (new Console()).setVisible(true);
      (new ToolSelection()).setVisible(true);
      log("Main", "Frosty's Cache Editor");
      log("Main", "Edited to support the #562 revision by Jens.");
   }

   public static void log(String className, String message) {
      System.out.println("[" + className + "]: " + message);
      printDebug(className, message);
   }

   private static void printDebug(String className, String message) {
      File f = new File(System.getProperty("user.home") + "/FCE/logs/");
      File f1 = new File(System.getProperty("user.home") + "/FCE/logs/" + 2 + 5 + 1 + 11 + ".txt");
      f.mkdirs();

      try {
         f1.createNewFile();
      } catch (IOException var10) {
         log("Main", "Could not create log file.");
      }

      String strFilePath = System.getProperty("user.home") + "/FCE/logs/" + 2 + 5 + 1 + 11 + ".txt";

      try {
         FileOutputStream var9 = new FileOutputStream(strFilePath, true);
         String strContent = new Date() + ": [" + className + "]: " + message;
         String lineSep = System.getProperty("line.separator");
         var9.write(strContent.getBytes());
         var9.write(lineSep.getBytes());
      } catch (FileNotFoundException var8) {
         log("Main", "FileNotFoundException : " + var8);
      } catch (IOException var91) {
         log("Main", "IOException : " + var91);
      }

   }
}
