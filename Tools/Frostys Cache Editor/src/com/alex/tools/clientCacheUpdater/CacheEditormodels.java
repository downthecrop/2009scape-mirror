package com.alex.tools.clientCacheUpdater;

import com.alex.loaders.items.ItemDefinitions;
import com.alex.store.Store;
import com.alex.utils.Utils;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CacheEditormodels {
   public static byte[] getBytesFromFile(File file) throws IOException {
      FileInputStream is = new FileInputStream(file);
      long length = file.length();
      if(length > 2147483647L) {
         ;
      }

      byte[] bytes = new byte[(int)length];
      int offset = 0;

      int numRead1;
      for(boolean numRead = false; offset < bytes.length && (numRead1 = is.read(bytes, offset, bytes.length - offset)) >= 0; offset += numRead1) {
         ;
      }

      if(offset < bytes.length) {
         throw new IOException("Could not completely read file " + file.getName());
      } else {
         is.close();
         return bytes;
      }
   }

   public static int packCustomModel(Store cache, byte[] data) {
      int archiveId = cache.getIndexes()[7].getLastArchiveId() + 1;
      if(cache.getIndexes()[7].putFile(archiveId, 0, data)) {
         return archiveId;
      } else {
         System.out.println("Failing packing model " + archiveId);
         return -1;
      }
   }

   public static void packCustomItems(Store cache) throws IOException {
      int modelID = packCustomModel(cache, getBytesFromFile(new File("pkcapefinalb.dat")));
      ItemDefinitions pkCape = ItemDefinitions.getItemDefinition(cache, 9747);
      pkCape.setName("PK Cape");
      pkCape.femaleEquip1 = modelID;
      pkCape.maleEquip1 = modelID;
      pkCape.modelId = modelID;
      pkCape.resetModelColors();
      packCustomItem(cache, 30000, pkCape);
   }

   public static void packCustomItem(Store cache, int id, ItemDefinitions def) {
      cache.getIndexes()[19].putFile(id >>> 8, 255 & id, def.encode());
   }

   public static void divideBackgrounds() throws IOException {
      BufferedImage background = ImageIO.read(new File("718/sprites/bg.jpg"));
      int id = 4139;
      int sx = background.getWidth() / 4;
      int sy = background.getHeight() / 2;

      int y;
      for(int var8 = 0; var8 < 2; ++var8) {
         for(y = 0; y < 4; ++y) {
            BufferedImage var9 = background.getSubimage(y * sx, var8 * sy, sx, sy);
            ImageIO.write(var9, "gif", new File("718/sprites/bg/" + id++ + ".gif"));
         }
      }

      BufferedImage var81 = ImageIO.read(new File("718/sprites/load.png"));
      id = 3769;
      sx = var81.getWidth() / 2;
      sy = var81.getHeight() / 2;

      for(y = 0; y < 2; ++y) {
         for(int var91 = 0; var91 < 2; ++var91) {
            BufferedImage part = var81.getSubimage(var91 * sx, y * sy, sx, sy);
            ImageIO.write(part, "png", new File("718/sprites/load/" + id + ".png"));
            ImageIO.write(part, "gif", new File("718/sprites/load/" + id++ + ".gif"));
         }
      }

   }

   public static byte[] getImage(File file) throws IOException {
      ImageOutputStream stream = ImageIO.createImageOutputStream(file);
      byte[] data = new byte[(int)stream.length()];
      stream.read(data);
      return data;
   }

   public static void main(String[] args) throws IOException {
      boolean beta = false;
      boolean addNewItemDefinitions = false;
      boolean divideBackgrounds = false;
      if(divideBackgrounds) {
         divideBackgrounds();
      }

      Store rscache = new Store(beta?"718/rsCacheBeta/":"718/rscache/");
      Store cache = new Store(beta?"718/cacheBeta/":"718/cache/");
      cache.resetIndex(7, false, false, 2);

      boolean result;
      for(int var13 = 0; var13 < cache.getIndexes().length; ++var13) {
         if(var13 != 3 && var13 != 5 && var13 != 12) {
            result = cache.getIndexes()[var13].packIndex(rscache, true);
            System.out.println("Packed index archives: " + var13 + ", " + result);
         }
      }

      if(addNewItemDefinitions) {
         System.out.println("Packing old item definitions...");
         Store var12 = new Store("cache667/", false);
         short currentSize = 30000;
         int oldSize = Utils.getItemDefinitionsSize(var12);

         for(int i = currentSize; i < currentSize + oldSize; ++i) {
            int oldItemId = i - currentSize;
            cache.getIndexes()[19].putFile(i >>> 8, 255 & i, 2, var12.getIndexes()[19].getFile(oldItemId >>> 8, 255 & oldItemId), (int[])null, false, false, -1, -1);
         }

         result = cache.getIndexes()[19].rewriteTable();
         System.out.println("Packed old item definitions: " + result);
      }

   }
}
