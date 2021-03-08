package com.editor.item;

import com.alex.loaders.items.ItemDefinitions;
import com.alex.store.Store;
import com.alex.utils.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ItemListDumper {
   private static Store STORE;

   public static void main(String[] args) {
      try {
         STORE = new Store("C:/Users/Travis/Documents/rscd/data/");
         new ItemListDumper();
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public ItemListDumper() throws IOException {
      File file = new File("C:/Users/Travis/Documents/781 ItemList.txt");
      if(file.exists()) {
         file.delete();
      } else {
         file.createNewFile();
      }

      BufferedWriter writer = new BufferedWriter(new FileWriter(file));
      writer.append("//Version = 781\n");
      writer.flush();

      for(int id = 0; id < Utils.getItemDefinitionsSize(STORE); ++id) {
         ItemDefinitions def = ItemDefinitions.getItemDefinition(STORE, id);
         if(!def.getName().equals("null")) {
            writer.append(id + " - " + def.getName());
            writer.newLine();
            writer.flush();
         }
      }

      writer.close();
   }

   public static int convertInt(String str) {
      try {
         int var2 = Integer.parseInt(str);
         return var2;
      } catch (NumberFormatException var21) {
         return 0;
      }
   }
}
