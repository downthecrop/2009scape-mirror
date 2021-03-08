package com.alex.tools.clientCacheUpdater;

import com.alex.loaders.items.ItemDefinitions;
import com.alex.store.Index;
import com.alex.store.Store;
import com.alex.tools.clientCacheUpdater.RSXteas;
import com.alex.utils.Utils;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CacheEditor {
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
      int regionId;
      for(regionId = 0; regionId < cache.getIndexes().length; ++regionId) {
         if(regionId != 3 && regionId != 5 && regionId != 12) {
            result = cache.getIndexes()[regionId].packIndex(rscache, true);
            System.out.println("Packed index archives: " + regionId + ", " + result);
         }
      }

      int regionY;
      if(addNewItemDefinitions) {
         System.out.println("Packing old item definitions...");
         Store var14 = new Store("cache667/", false);
         short var15 = 30000;
         regionY = Utils.getItemDefinitionsSize(var14);

         for(int data = var15; data < var15 + regionY; ++data) {
            int var16 = data - var15;
            cache.getIndexes()[19].putFile(data >>> 8, 255 & data, 2, var14.getIndexes()[19].getFile(var16 >>> 8, 255 & var16), (int[])null, false, false, -1, -1);
         }

         result = cache.getIndexes()[19].rewriteTable();
         System.out.println("Packed old item definitions: " + result);
      }

      System.out.println("Adding new interfaces...");

      for(regionId = cache.getIndexes()[3].getLastArchiveId() + 1; regionId <= rscache.getIndexes()[3].getLastArchiveId(); ++regionId) {
         if(regionId != 548 && regionId != 746 && rscache.getIndexes()[3].archiveExists(regionId)) {
            cache.getIndexes()[3].putArchive(regionId, rscache, false, false);
         }
      }

      result = cache.getIndexes()[3].rewriteTable();
      System.out.println("Packed new interfaces: " + result);
      RSXteas.loadUnpackedXteas("old");
      System.out.println("Updating Maps.");

      for(regionId = 0; regionId < 30000; ++regionId) {
         int var13 = (regionId >> 8) * 64;
         regionY = (regionId & 255) * 64;
         String var141 = "m" + (var13 >> 3) / 8 + "_" + (regionY >> 3) / 8;
         byte[] var151 = rscache.getIndexes()[5].getFile(rscache.getIndexes()[5].getArchiveId(var141));
         if(var151 != null) {
            result = addMapFile(cache.getIndexes()[5], var141, var151);
            System.out.println(var141 + ", " + result);
         }

         var141 = "um" + (var13 >> 3) / 8 + "_" + (regionY >> 3) / 8;
         var151 = rscache.getIndexes()[5].getFile(rscache.getIndexes()[5].getArchiveId(var141));
         if(var151 != null) {
            result = addMapFile(cache.getIndexes()[5], var141, var151);
            System.out.println(var141 + ", " + result);
         }

         int[] var161 = RSXteas.getXteas(regionId);
         var141 = "l" + (var13 >> 3) / 8 + "_" + (regionY >> 3) / 8;
         var151 = rscache.getIndexes()[5].getFile(rscache.getIndexes()[5].getArchiveId(var141), 0, var161);
         if(var151 != null) {
            result = addMapFile(cache.getIndexes()[5], var141, var151);
            System.out.println(var141 + ", " + result);
         }

         var141 = "ul" + (var13 >> 3) / 8 + "_" + (regionY >> 3) / 8;
         var151 = rscache.getIndexes()[5].getFile(rscache.getIndexes()[5].getArchiveId(var141), 0, var161);
         if(var151 != null) {
            result = addMapFile(cache.getIndexes()[5], var141, var151);
            System.out.println(var141 + ", " + result);
         }

         var141 = "n" + (var13 >> 3) / 8 + "_" + (regionY >> 3) / 8;
         var151 = rscache.getIndexes()[5].getFile(rscache.getIndexes()[5].getArchiveId(var141), 0);
         if(var151 != null) {
            result = addMapFile(cache.getIndexes()[5], var141, var151);
            System.out.println(var141 + ", " + result);
         }
      }

      result = cache.getIndexes()[5].rewriteTable();
      System.out.println("Updated maps: " + result);
   }

   public static boolean addMapFile(Index index, String name, byte[] data) {
      int archiveId = index.getArchiveId(name);
      if(archiveId == -1) {
         archiveId = index.getTable().getValidArchiveIds().length;
      }

      return index.putFile(archiveId, 0, 2, data, (int[])null, false, false, Utils.getNameHash(name), -1);
   }
}
