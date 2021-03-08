package com.alex.tools.clientCacheUpdater;

import com.alex.store.Index;
import com.alex.store.Store;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class ProtectCache {
   public static void main(String[] args) throws IOException {
      boolean encryptMaps = true;
      boolean encryptTables = false;
      Store cache = new Store("718/cacheEncrypted/");
      if(encryptMaps) {
         Store var13 = new Store("718/rscache/");
         Index var14 = cache.getIndexes()[5];
         Index rsIndex = var13.getIndexes()[5];

         for(int regionId = 0; regionId < 25000; ++regionId) {
            int regionX = (regionId >> 8) * 64;
            int regionY = (regionId & 255) * 64;
            int[] keys1 = null;
            String name = "l" + (regionX >> 3) / 8 + "_" + (regionY >> 3) / 8;
            int archiveId;
            if(rsIndex.getFile(rsIndex.getArchiveId(name), 0) == null) {
               archiveId = var14.getArchiveId(name);
               if(archiveId != -1) {
                  keys1 = writeKeys(regionId);
                  if(!var14.encryptArchive(archiveId, (int[])null, keys1, false, false)) {
                     throw new RuntimeException("FAIL");
                  }
               }
            }

            name = "ul" + (regionX >> 3) / 8 + "_" + (regionY >> 3) / 8;
            if(rsIndex.getFile(rsIndex.getArchiveId(name), 0) == null) {
               archiveId = var14.getArchiveId(name);
               if(archiveId != -1) {
                  if(keys1 == null) {
                     keys1 = writeKeys(regionId);
                  }

                  if(!var14.encryptArchive(archiveId, (int[])null, keys1, false, false)) {
                     throw new RuntimeException("FAIL");
                  }
               }
            }
         }

         var14.rewriteTable();
      }

      if(encryptTables) {
         int[][] var131 = new int[cache.getIndexes().length][];

         int var141;
         for(var141 = 0; var141 < var131.length; ++var141) {
            var131[var141] = generateKeys();
            if(cache.getIndexes()[var141] != null) {
               System.out.println("encrypting idx table: " + var141);
               cache.getIndexes()[var141].setKeys(var131[var141]);
               cache.getIndexes()[var141].rewriteTable();
            }
         }

         for(var141 = 0; var141 < var131.length; ++var141) {
            System.out.println(Arrays.toString(var131[var141]));
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
      BufferedWriter writer = new BufferedWriter(new FileWriter("718/maps/unpacked/" + regionId + ".txt"));
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
