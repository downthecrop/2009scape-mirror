package com.editor.npc;

import com.alex.loaders.npcs.NPCDefinitions;
import com.alex.store.Store;
import com.alex.utils.Utils;
import com.editor.Main;

import java.util.Iterator;

public class NPCDefDump {
   private static NPCDefinitions defs;
   private static Store STORE;

   public static void main(String[] args) {
      try {
         STORE = new Store("C:/Users/Travis/Documents/rscd/data/");
      } catch (IOException var2) {
         System.out.println("Cannot find cache location");
      }

      int id;
      if(Utils.getNPCDefinitionsSize(STORE) > 30000) {
         for(id = 0; id < Utils.getNPCDefinitionsSize(STORE) - 18433; ++id) {
            defs = NPCDefinitions.getNPCDefinition(STORE, id);
            dump();
            Main.log("NPCDefDump", "Dumping NPC " + defs.id);
         }
      } else {
         for(id = 0; id < Utils.getNPCDefinitionsSize(STORE); ++id) {
            defs = NPCDefinitions.getNPCDefinition(STORE, id);
            dump();
            Main.log("NPCDefDump", "Dumping NPC " + defs.id);
         }
      }

   }

   public static void editorDump(String cache) {
      try {
         STORE = new Store(cache);
      } catch (IOException var2) {
         Main.log("NPCDefDump", "Cannot find cache location");
      }

      int id;
      if(Utils.getNPCDefinitionsSize(STORE) > 30000) {
         for(id = 0; id < Utils.getNPCDefinitionsSize(STORE) - 15615; ++id) {
            defs = NPCDefinitions.getNPCDefinition(STORE, id);
            dump();
            Main.log("NPCDefDump", "Dumping NPC " + defs.id);
         }
      } else {
         for(id = 0; id < Utils.getNPCDefinitionsSize(STORE); ++id) {
            defs = NPCDefinitions.getNPCDefinition(STORE, id);
            dump();
            Main.log("NPCDefDump", "Dumping NPC " + defs.id);
         }
      }

   }

   private static void dump() {
      File f = new File(System.getProperty("user.home") + "/FCE/npcs/");
      f.mkdirs();
      String lineSep = System.getProperty("line.separator");
      BufferedWriter writer = null;

      try {
         writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getProperty("user.home") + "/FCE/npcs/" + defs.id + ".txt"), "utf-8"));
         writer.write("name = " + defs.getName());
         writer.write(lineSep);
         writer.write("combat level = " + defs.getCombatLevel());
         writer.write(lineSep);
         writer.write("isVisibleOnMap = " + defs.isVisibleOnMap);
         writer.write(lineSep);
         writer.write("height = " + defs.npcHeight);
         writer.write(lineSep);
         writer.write("width = " + defs.npcWidth);
         writer.write(lineSep);
         writer.write("walk mask = " + defs.walkMask);
         writer.write(lineSep);
         writer.write("respawn direction = " + defs.respawnDirection);
         writer.write(lineSep);
         writer.write("render emote = " + defs.renderEmote);
         writer.write(lineSep);
         writer.write("model ids = " + getModelIds());
         writer.write(lineSep);
         writer.write("chat head model ids = " + getChatHeads());
         writer.write(lineSep);
         writer.write("options = " + getOpts());
         writer.write(lineSep);
         writer.write("model colors = " + getChangedModelColors());
         writer.write(lineSep);
         writer.write("texture colors = " + getChangedTextureColors());
         writer.write(lineSep);
         writer.write("unknown array1 = " + getUnknownArray1());
         writer.write(lineSep);
         writer.write("unknown array2 = " + getUnknownArray2());
         writer.write(lineSep);
         writer.write("unknown array3 = " + getUnknownArray3());
         writer.write(lineSep);
         writer.write("unknown array4 = " + getUnknownArray4());
         writer.write(lineSep);
         writer.write("unknown array5 = " + getUnknownArray5());
         writer.write(lineSep);
         writer.write("unknownBoolean1 = " + defs.unknownBoolean1);
         writer.write(lineSep);
         writer.write("unknwonBoolean2 = " + defs.unknownBoolean2);
         writer.write(lineSep);
         writer.write("unknownBoolean3 = " + defs.unknownBoolean3);
         writer.write(lineSep);
         writer.write("unknownBoolean4 = " + defs.unknownBoolean5);
         writer.write(lineSep);
         writer.write("unknownBoolean5 = " + defs.unknownBoolean4);
         writer.write(lineSep);
         writer.write("unknownBoolean6 = " + defs.unknownBoolean6);
         writer.write(lineSep);
         writer.write("unknownBoolean7 = " + defs.unknownBoolean7);
         writer.write(lineSep);
         writer.write("unknown int1 = " + defs.unknownInt1);
         writer.write(lineSep);
         writer.write("unknown int2 = " + defs.unknownInt2);
         writer.write(lineSep);
         writer.write("unknown int3 = " + defs.unknownInt3);
         writer.write(lineSep);
         writer.write("unknown int4 = " + defs.unknownInt4);
         writer.write(lineSep);
         writer.write("unknown int5 = " + defs.unknownInt5);
         writer.write(lineSep);
         writer.write("unknown int6 = " + defs.unknownInt6);
         writer.write(lineSep);
         writer.write("unknown int7 = " + defs.unknownInt7);
         writer.write(lineSep);
         writer.write("unknown int8 = " + defs.unknownInt8);
         writer.write(lineSep);
         writer.write("unknown int9 = " + defs.unknownInt9);
         writer.write(lineSep);
         writer.write("unknown int10 = " + defs.unknownInt10);
         writer.write(lineSep);
         writer.write("unknown int11 = " + defs.unknownInt11);
         writer.write(lineSep);
         writer.write("unknown int12 = " + defs.unknownInt12);
         writer.write(lineSep);
         writer.write("unknown int13 = " + defs.unknownInt13);
         writer.write(lineSep);
         writer.write("unknown int14 = " + defs.unknownInt14);
         writer.write(lineSep);
         writer.write("unknown int15 = " + defs.unknownInt15);
         writer.write(lineSep);
         writer.write("unknown int16 = " + defs.unknownInt16);
         writer.write(lineSep);
         writer.write("unknown int17 = " + defs.unknownInt17);
         writer.write(lineSep);
         writer.write("unknown int18 = " + defs.unknownInt18);
         writer.write(lineSep);
         writer.write("unknown int19 = " + defs.unknownInt19);
         writer.write(lineSep);
         writer.write("unknown int20 = " + defs.unknownInt20);
         writer.write(lineSep);
         writer.write("unknown int21 = " + defs.unknownInt21);
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
         Main.log("NPCDefDump", "Failed to export NPC Defs to .txt");
      } finally {
         try {
            writer.close();
         } catch (Exception var13) {
            ;
         }

      }

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

   public static String getModelIds() {
      String text = "";

      try {
         int[] e = defs.modelIds;
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

   public static String getOpts() {
      String text = "";
      String[] arr$ = defs.getOptions();
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

   public static String getChatHeads() {
      String text = "";

      try {
         int[] e = defs.npcChatHeads;
         int len$ = e.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            int id = e[i$];
            text = text + id + ";";
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
         int[] e = defs.unknownArray3[0];
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
}
