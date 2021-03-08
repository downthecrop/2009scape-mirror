package com.editor.item;

import com.alex.loaders.items.ItemDefinitions;
import com.alex.store.Store;
import com.alex.utils.Utils;
import com.editor.Main;

import java.util.Iterator;

public class ItemDefDump {
   private static ItemDefinitions defs;
   private static Store STORE;

   public static void main(String[] args) {
      try {
         STORE = new Store("C:/Users/yvonne å christer/Dropbox/FCE/ItemDefDump/");
      } catch (IOException var2) {
         System.out.println("Cannot find cache location");
      }

      int id;
      if(Utils.getItemDefinitionsSize(STORE) > 30000) {
         for(id = 0; id < Utils.getItemDefinitionsSize(STORE) - 22314; ++id) {
            defs = ItemDefinitions.getItemDefinition(STORE, id);
            dump();
            Main.log("ItemDefDump", "Dumping Item " + defs.id);
         }
      } else {
         for(id = 0; id < Utils.getItemDefinitionsSize(STORE); ++id) {
            defs = ItemDefinitions.getItemDefinition(STORE, id);
            dump();
            Main.log("ItemDefDump", "Dumping Item " + defs.id);
         }
      }

   }

   public static void editorDump(String cache) {
      try {
         STORE = new Store(cache);
      } catch (IOException var2) {
         Main.log("ItemDefDump", "Cannot find cache location");
      }

      int id;
      if(Utils.getItemDefinitionsSize(STORE) > 30000) {
         for(id = 0; id < Utils.getItemDefinitionsSize(STORE) - 22314; ++id) {
            defs = ItemDefinitions.getItemDefinition(STORE, id);
            dump();
            Main.log("ItemDefDump", "Dumping Item " + defs.id);
         }
      } else {
         for(id = 0; id < Utils.getItemDefinitionsSize(STORE); ++id) {
            defs = ItemDefinitions.getItemDefinition(STORE, id);
            dump();
            Main.log("ItemDefDump", "Dumping Item " + defs.id);
         }
      }

   }

   public static void dump() {
      File f = new File(System.getProperty("user.home") + "/FCE/items/");
      f.mkdirs();
      String lineSep = System.getProperty("line.separator");
      BufferedWriter writer = null;

      try {
         writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getProperty("user.home") + "/FCE/items/" + defs.id + ".txt"), "utf-8"));
         writer.write("name = " + defs.getName());
         writer.write(lineSep);
         writer.write("value = " + defs.getValue());
         writer.write(lineSep);
         writer.write("team id = " + defs.getTeamId());
         writer.write(lineSep);
         writer.write("members only = " + String.valueOf(defs.isMembersOnly()));
         writer.write(lineSep);
         writer.write("equip slot = " + defs.getEquipSlot());
         writer.write(lineSep);
         writer.write("equip type = " + defs.getEquipType());
         writer.write(lineSep);
         writer.write("stack ids = " + getStackIDs());
         writer.write(lineSep);
         writer.write("stack amounts = " + getStackAmts());
         writer.write(lineSep);
         writer.write("stackable = " + defs.getStackable());
         writer.write(lineSep);
         writer.write("inv model zoom = " + defs.getInvModelZoom());
         writer.write(lineSep);
         writer.write("model rotation 1 = " + defs.getModelRotation1());
         writer.write(lineSep);
         writer.write("model rotation 2 = " + defs.getModelRotation2());
         writer.write(lineSep);
         writer.write("model offset 1 = " + defs.getModelOffset1());
         writer.write(lineSep);
         writer.write("model offset 2 = " + defs.getModelOffset2());
         writer.write(lineSep);
         writer.write("inv model id = " + defs.getInvModelId());
         writer.write(lineSep);
         writer.write("male equip model id 1 = " + defs.getMaleEquipModelId1());
         writer.write(lineSep);
         writer.write("female equip model id 1 = " + defs.getFemaleEquipModelId1());
         writer.write(lineSep);
         writer.write("male equip model id 2 = " + defs.getMaleEquipModelId2());
         writer.write(lineSep);
         writer.write("female equip model id 2 = " + defs.getFemaleEquipModelId2());
         writer.write(lineSep);
         writer.write("male equip model id 3 = " + defs.getMaleEquipModelId3());
         writer.write(lineSep);
         writer.write("female equip model id 3 = " + defs.getFemaleEquipModelId3());
         writer.write(lineSep);
         writer.write("inventory options = " + getInventoryOpts());
         writer.write(lineSep);
         writer.write("ground options = " + getGroundOpts());
         writer.write(lineSep);
         writer.write("changed model colors = " + getChangedModelColors());
         writer.write(lineSep);
         writer.write("changed texture colors = " + getChangedTextureColors());
         writer.write(lineSep);
         writer.write("switch note item id = " + defs.switchNoteItemId);
         writer.write(lineSep);
         writer.write("noted item id = " + defs.notedItemId);
         writer.write(lineSep);
         writer.write("unnoted = " + String.valueOf(defs.isUnnoted()));
         writer.write(lineSep);
         writer.write("switch lend item id = " + defs.getSwitchLendItemId());
         writer.write(lineSep);
         writer.write("lended item id = " + defs.getLendedItemId());
         writer.write(lineSep);
         writer.write("unknownArray1 = " + getUnknownArray1());
         writer.write(lineSep);
         writer.write("unknownArray2 = " + getUnknownArray2());
         writer.write(lineSep);
         writer.write("unknownArray3 = " + getUnknownArray3());
         writer.write(lineSep);
         writer.write("unknownArray4 = " + getUnknownArray4());
         writer.write(lineSep);
         writer.write("unknownArray5 = " + getUnknownArray5());
         writer.write(lineSep);
         writer.write("unknownArray6 = " + getUnknownArray6());
         writer.write(lineSep);
         writer.write("unknownInt1 = " + defs.unknownInt1);
         writer.write(lineSep);
         writer.write("unknownInt2 = " + defs.unknownInt2);
         writer.write(lineSep);
         writer.write("unknownInt3 = " + defs.unknownInt3);
         writer.write(lineSep);
         writer.write("unknownInt4 = " + defs.unknownInt4);
         writer.write(lineSep);
         writer.write("unknownInt5 = " + defs.unknownInt5);
         writer.write(lineSep);
         writer.write("unknownInt6 = " + defs.unknownInt6);
         writer.write(lineSep);
         writer.write("unknownInt7 = " + defs.unknownInt7);
         writer.write(lineSep);
         writer.write("unknownInt8 = " + defs.unknownInt8);
         writer.write(lineSep);
         writer.write("unknownInt9 = " + defs.unknownInt9);
         writer.write(lineSep);
         writer.write("unknownInt10 = " + defs.unknownInt10);
         writer.write(lineSep);
         writer.write("unknownInt11 = " + defs.unknownInt11);
         writer.write(lineSep);
         writer.write("unknownInt12 = " + defs.unknownInt12);
         writer.write(lineSep);
         writer.write("unknownInt13 = " + defs.unknownInt13);
         writer.write(lineSep);
         writer.write("unknownInt14 = " + defs.unknownInt14);
         writer.write(lineSep);
         writer.write("unknownInt15 = " + defs.unknownInt15);
         writer.write(lineSep);
         writer.write("unknownInt16 = " + defs.unknownInt16);
         writer.write(lineSep);
         writer.write("unknownInt17 = " + defs.unknownInt17);
         writer.write(lineSep);
         writer.write("unknownInt18 = " + defs.unknownInt18);
         writer.write(lineSep);
         writer.write("unknownInt19 = " + defs.unknownInt19);
         writer.write(lineSep);
         writer.write("unknownInt20 = " + defs.unknownInt20);
         writer.write(lineSep);
         writer.write("unknownInt21 = " + defs.unknownInt21);
         writer.write(lineSep);
         writer.write("unknownInt22 = " + defs.unknownInt22);
         writer.write(lineSep);
         writer.write("unknownInt23 = " + defs.unknownInt23);
         writer.write(lineSep);
         writer.write("Clientscripts");
         writer.write(lineSep);
         if(defs.clientScriptData != null) {
            Iterator var14 = defs.clientScriptData.keySet().iterator();

            while(var14.hasNext()) {
               int key = ((Integer)var14.next()).intValue();
               Object value = defs.clientScriptData.get(Integer.valueOf(key));
               writer.write("KEY: " + key + ", VALUE: " + value);
               writer.write(lineSep);
            }
         }
      } catch (IOException var141) {
         Main.log("ItemEditor", "Failed to export Item Defs to .txt");
      } finally {
         try {
            writer.close();
         } catch (Exception var13) {
            ;
         }

      }

   }

   public static String getInventoryOpts() {
      String text = "";
      String[] arr$ = defs.getInventoryOptions();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String option = arr$[i$];
         text = text + (option == null?"null":option) + ";";
      }

      return text;
   }

   public static String getGroundOpts() {
      String text = "";
      String[] arr$ = defs.getGroundOptions();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String option = arr$[i$];
         text = text + (option == null?"null":option) + ";";
      }

      return text;
   }

   public static String getChangedModelColors() {
      String text = "";
      if(defs.originalModelColors != null) {
         for(int i = 0; i < defs.originalModelColors.length; ++i) {
            text = text + defs.originalModelColors[i] + "=" + defs.modifiedModelColors[i] + ";";
         }
      }

      return text;
   }

   public static String getChangedTextureColors() {
      String text = "";
      if(defs.originalTextureColors != null) {
         for(int i = 0; i < defs.originalTextureColors.length; ++i) {
            text = text + defs.originalTextureColors[i] + "=" + defs.modifiedTextureColors[i] + ";";
         }
      }

      return text;
   }

   public static String getStackIDs() {
      String text = "";

      try {
         int[] e = defs.getStackIds();
         int len$ = e.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            int index = e[i$];
            text = text + index + ";";
         }
      } catch (Exception var5) {
         ;
      }

      return text;
   }

   public static String getClientScripts() {
      String text = "";
      String lineSep = System.getProperty("line.separator");
      if(defs.clientScriptData != null) {
         for(Iterator i$ = defs.clientScriptData.keySet().iterator(); i$.hasNext(); text = text + lineSep) {
            int key = ((Integer)i$.next()).intValue();
            Object value = defs.clientScriptData.get(Integer.valueOf(key));
            text = text + "KEY: " + key + ", VALUE: " + value;
         }
      }

      return text;
   }

   public static String getStackAmts() {
      String text = "";

      try {
         int[] e = defs.getStackAmounts();
         int len$ = e.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            int index = e[i$];
            text = text + index + ";";
         }
      } catch (Exception var5) {
         ;
      }

      return text;
   }

   public static String getUnknownArray1() {
      String text = "";

      try {
         byte[] e = defs.unknownArray1;
         int len$ = e.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            byte index = e[i$];
            text = text + index + ";";
         }
      } catch (Exception var5) {
         ;
      }

      return text;
   }

   public static String getUnknownArray2() {
      String text = "";

      try {
         int[] e = defs.unknownArray2;
         int len$ = e.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            int index = e[i$];
            text = text + index + ";";
         }
      } catch (Exception var5) {
         ;
      }

      return text;
   }

   public static String getUnknownArray3() {
      String text = "";

      try {
         byte[] e = defs.unknownArray3;
         int len$ = e.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            byte index = e[i$];
            text = text + index + ";";
         }
      } catch (Exception var5) {
         ;
      }

      return text;
   }

   public static String getUnknownArray4() {
      String text = "";

      try {
         int[] e = defs.unknownArray4;
         int len$ = e.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            int index = e[i$];
            text = text + index + ";";
         }
      } catch (Exception var5) {
         ;
      }

      return text;
   }

   public static String getUnknownArray5() {
      String text = "";

      try {
         int[] e = defs.unknownArray5;
         int len$ = e.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            int index = e[i$];
            text = text + index + ";";
         }
      } catch (Exception var5) {
         ;
      }

      return text;
   }

   public static String getUnknownArray6() {
      String text = "";

      try {
         byte[] e = defs.unknownArray6;
         int len$ = e.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            byte index = e[i$];
            text = text + index + ";";
         }
      } catch (Exception var5) {
         ;
      }

      return text;
   }
}
