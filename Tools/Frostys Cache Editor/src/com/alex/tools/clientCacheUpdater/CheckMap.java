package com.alex.tools.clientCacheUpdater;

import com.alex.store.Store;
import com.alex.utils.Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class CheckMap {
   public static void main(String[] args) throws IOException {
      Store cache = new Store("C:/Users/yvonne � christer/Dropbox/Source/data/562cache/", false, (int[][])null);
      double land = 0.0D;
      double map = 0.0D;

      for(int var11 = 0; var11 < 30000; ++var11) {
         int regionX = (var11 >> 8) * 64;
         int regionY = (var11 & 255) * 64;
         String name1 = "l" + (regionX >> 3) / 8 + "_" + (regionY >> 3) / 8;
         String name2 = "m" + (regionX >> 3) / 8 + "_" + (regionY >> 3) / 8;
         if(cache.getIndexes()[5].getArchiveId(name1) != -1) {
            ++land;
         }

         if(cache.getIndexes()[5].getArchiveId(name2) != -1) {
            ++map;
         }
      }

      System.out.println("land: " + land + ", newMaps: " + map);
      double var111 = land * 100.0D / map;
      System.out.println(var111 + "% complete!");
   }

   public static boolean passArchive(int regionId, Store store1, Store store2, String nameHash, int i, int[] keys1, int[] keys2) {
      if(keys2 != null) {
         System.out.println(keys2);
      }

      int archiveId = store1.getIndexes()[i].getArchiveId(nameHash);
      if(archiveId == -1) {
         return false;
      } else {
         int oldArchiveId = store2.getIndexes()[i].getArchiveId(nameHash);
         if(oldArchiveId == -1) {
            oldArchiveId = store2.getIndexes()[i].getLastArchiveId() + 1;
         }

         byte[] data = store1.getIndexes()[i].getFile(archiveId, 0, keys1);
         if(data == null) {
            return false;
         } else {
            try {
               boolean var13 = store2.getIndexes()[i].putFile(oldArchiveId, 0, 2, data, keys2, false, false, Utils.getNameHash(nameHash), -1);
               if(!var13) {
                  return false;
               } else {
                  int[] keys = writeKeys(regionId);
                  return store2.getIndexes()[i].encryptArchive(oldArchiveId, keys2, keys, false, false);
               }
            } catch (Error var12) {
               return false;
            } catch (Exception var131) {
               var131.printStackTrace();
               return false;
            }
         }
      }
   }

   public static int[] generateKeys() {
      int[] keys = new int[4];

      for(int index = 0; index < keys.length; ++index) {
         keys[index] = (new Random()).nextInt();
      }

      return keys;
   }

   public static int[] writeKeys(int regionId) throws IOException {
      BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Users/yvonne � christer/Desktop/LS/Xteas/xteas/742/" + regionId + ".txt"));
      int[] keys = generateKeys();

      for(int index = 0; index < keys.length; ++index) {
         writer.write("" + keys[index]);
         writer.newLine();
         writer.flush();
      }

      System.out.println("Region: " + regionId + ", " + Arrays.toString(keys));
      return keys;
   }
}
