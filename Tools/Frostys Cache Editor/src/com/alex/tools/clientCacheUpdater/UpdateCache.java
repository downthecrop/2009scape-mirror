package com.alex.tools.clientCacheUpdater;

import com.alex.loaders.images.IndexedColorImageFile;
import com.alex.loaders.items.ItemDefinitions;
import com.alex.store.Index;
import com.alex.store.Store;
import com.alex.tools.clientCacheUpdater.RSXteas;
import com.alex.tools.clientCacheUpdater.SpritesDumper;
import com.alex.utils.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class UpdateCache {
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

   public static void main6(String[] args) throws IOException {
      Store cache = new Store("cache667_2/", false);
      cache.getIndexes()[6].putFile(0, 0, getBytesFromFile(new File("0")));
   }

   public static void main5(String[] args) throws IOException {
      Store rscache = new Store("cache697/");
      Store cache = new Store("cache667_2/", false);
      boolean result = false;
      result = cache.getIndexes()[3].putArchive(320, rscache, false, false);
      System.out.println("Packed skill interface: 320, " + result);
      result = cache.getIndexes()[3].putArchive(679, rscache, false, false);
      System.out.println("Packed skill interface: 679, " + result);
      cache.getIndexes()[3].rewriteTable();
   }

   public static void main555(String[] args) throws IOException {
      Store cache = new Store("cache667_2/", false);
      Store originalCache = new Store("rscache/", false);
      cache.addIndex(false, false, 2);
      int[] arr$ = originalCache.getIndexes()[19].getTable().getValidArchiveIds();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         int i = arr$[i$];
         System.out.println(i);
         int[] arr$1 = originalCache.getIndexes()[19].getTable().getArchives()[i].getValidFileIds();
         int len$1 = arr$1.length;

         for(int i$1 = 0; i$1 < len$1; ++i$1) {
            int i2 = arr$1[i$1];

            try {
               cache.getIndexes()[37].putFile(i, i2, 2, originalCache.getIndexes()[19].getFile(i, i2), (int[])null, false, false, -1, -1);
            } catch (Throwable var12) {
               var12.printStackTrace();
            }
         }
      }

      cache.getIndexes()[37].rewriteTable();
   }

   public static void main77(String[] args) throws IOException {
      Store originalCache = new Store("cache667/", false);
      Store cache = new Store("cache667_2/", false);

      for(int i = 1610; i < 1616; ++i) {
         cache.getIndexes()[17].putFile(i >>> 8, i & 255, originalCache.getIndexes()[17].getFile(i >>> 8, i & 255));
      }

   }

   public static void packLogo(Store cache) throws IOException {
      short id = 2498;
      IndexedColorImageFile f = null;

      try {
         f = new IndexedColorImageFile(new BufferedImage[]{ImageIO.read(new File("bg/logo.png"))});
      } catch (IOException var81) {
         var81.printStackTrace();
      }

      byte[] data = f.encodeFile();
      cache.getIndexes()[8].putFile(id, 0, data);

      int i;
      for(i = 4139; i <= 4146; ++i) {
         try {
            cache.getIndexes()[8].putFile(i, 0, (new IndexedColorImageFile(new BufferedImage[]{ImageIO.read(new File("bg/" + i + ".gif"))})).encodeFile());
         } catch (IOException var7) {
            var7.printStackTrace();
         }
      }

      for(i = 0; i < 4; ++i) {
         int realid = 3769 + i;
         int var8 = 3769 + i;
         cache.getIndexes()[8].putFile(var8, 0, (new IndexedColorImageFile(new BufferedImage[]{ImageIO.read(new File("bg/" + realid + ".gif"))})).encodeFile());
         var8 = 3779 + i;
         cache.getIndexes()[8].putFile(var8, 0, (new IndexedColorImageFile(new BufferedImage[]{ImageIO.read(new File("bg/" + realid + ".gif"))})).encodeFile());
         var8 = 3783 + (i >= 2?i - 2:i + 2);
         cache.getIndexes()[8].putFile(var8, 0, (new IndexedColorImageFile(new BufferedImage[]{ImageIO.read(new File("bg/" + realid + ".gif"))})).encodeFile());
         var8 = 3769 + i;
         cache.getIndexes()[34].putFile(var8, 0, (new IndexedColorImageFile(new BufferedImage[]{ImageIO.read(new File("bg/" + realid + ".gif"))})).encodeFile());
         var8 = 3779 + i;
         cache.getIndexes()[34].putFile(var8, 0, (new IndexedColorImageFile(new BufferedImage[]{ImageIO.read(new File("bg/" + realid + ".gif"))})).encodeFile());
         var8 = 3783 + (i >= 2?i - 2:i + 2);
         cache.getIndexes()[34].putFile(var8, 0, (new IndexedColorImageFile(new BufferedImage[]{ImageIO.read(new File("bg/" + realid + ".gif"))})).encodeFile());
         var8 = 3769 + i;
         cache.getIndexes()[32].putFile(var8, 0, SpritesDumper.getImage(new File("bg/" + realid + ".png")));
         var8 = 3779 + i;
         cache.getIndexes()[32].putFile(var8, 0, SpritesDumper.getImage(new File("bg/" + realid + ".png")));
         var8 = 3783 + (i >= 2?i - 2:i + 2);
         cache.getIndexes()[32].putFile(var8, 0, SpritesDumper.getImage(new File("bg/" + realid + ".png")));
         System.out.println("added file: " + i);
      }

   }

   public static void packDonatorIcon(Store cache) {
      short id = 1455;
      IndexedColorImageFile f = null;

      try {
         f = new IndexedColorImageFile(cache, id, 0);
         BufferedImage var7 = ImageIO.read(new File("1455.png"));
         System.out.println("Added icon: " + f.addImage(var7) + ".");
         BufferedImage icon2 = ImageIO.read(new File("1455f.png"));
         System.out.println("Added icon2: " + f.addImage(icon2) + ".");
         BufferedImage icon3 = ImageIO.read(new File("crown_green.gif"));
         System.out.println("Added icon3: " + f.addImage(icon3) + ".");
         BufferedImage icon4 = ImageIO.read(new File("1455_11.png"));
         System.out.println("Added icon4: " + f.addImage(icon4) + ".");
      } catch (IOException var71) {
         var71.printStackTrace();
      }

      cache.getIndexes()[8].putFile(id, 0, f.encodeFile());
   }

   public static void packMatrixIcon(Store cache) {
      short id = 2173;
      IndexedColorImageFile f = null;

      try {
         f = new IndexedColorImageFile(new BufferedImage[]{ImageIO.read(new File("2173.png"))});
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      byte[] data = f.encodeFile();
      cache.getIndexes()[8].putFile(id, 0, data);
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
      int modelID = packCustomModel(cache, getBytesFromFile(new File("donatorCape.dat")));
      System.out.println("model id " + modelID);
      ItemDefinitions donatorCape = ItemDefinitions.getItemDefinition(cache, 9747);
      donatorCape.setName("Donator cape");
      donatorCape.femaleEquip1 = modelID;
      donatorCape.maleEquip1 = modelID;
      donatorCape.modelId = modelID;
      donatorCape.resetModelColors();
      short newId = 29999;
      System.out.println(cache.getIndexes()[19].putFile(newId >>> 8, 255 & newId, donatorCape.encode()));
   }

   public static void main(String[] args) throws IOException {
      boolean updateJustMaps = false;
      boolean addOldItems = true;
      Store rscache = new Store("cache697/");
      Store cache = new Store("cache667_2/", false);
      Store originalCache = new Store("cache667/", false);
      if(addOldItems) {
         cache.resetIndex(19, false, false, 2);
      }

      cache.resetIndex(7, false, false, 2);
      cache.getIndexes()[7].packIndex(originalCache);
      int regionId;
      int regionX;
      int regionY;
      boolean var20;
      int[] xteas;
      if(!updateJustMaps) {
         int var19;
         for(var19 = 0; var19 < cache.getIndexes().length; ++var19) {
            if(var19 != 3 && var19 != 5 && var19 != 12 && var19 != 33 && var19 != 30) {
               boolean var21 = cache.getIndexes()[var19].packIndex(rscache, true);
               System.out.println("Packed index archives: " + var19 + ", " + var21);
            }
         }

         System.out.println("Adding logo...");
         packLogo(cache);
         System.out.println("Adding donator icon...");
         packDonatorIcon(cache);
         System.out.println("Adding Matrix icon...");
         packMatrixIcon(cache);
         System.out.println("Adding Custom items...");
         packCustomItems(cache);
         int var22;
         int var17;
         if(addOldItems) {
            System.out.println("Adding back old item definitions...");
            short var24 = 30000;
            System.out.println(var24);
            var22 = Utils.getItemDefinitionsSize(originalCache);

            for(var17 = var24; var17 < var24 + var22; ++var17) {
               regionId = var17 - var24;
               cache.getIndexes()[19].putFile(var17 >>> 8, 255 & var17, 2, originalCache.getIndexes()[19].getFile(regionId >>> 8, 255 & regionId), (int[])null, false, false, -1, -1);
            }

            cache.getIndexes()[19].rewriteTable();
         }

         System.out.println("Recovering Client Script Maps...");
         int[] var211 = originalCache.getIndexes()[17].getTable().getValidArchiveIds();
         var22 = var211.length;

         for(var17 = 0; var17 < var22; ++var17) {
            int data = var211[var17];
            xteas = originalCache.getIndexes()[17].getTable().getArchives()[data].getValidFileIds();
            regionX = xteas.length;

            for(regionY = 0; regionY < regionX; ++regionY) {
               int name = xteas[regionY];
               if(!cache.getIndexes()[17].fileExists(data, name) || cache.getIndexes()[17].getFile(data, name).length == 1) {
                  cache.getIndexes()[17].putFile(data, name, originalCache.getIndexes()[17].getFile(data, name));
               }
            }
         }

         System.out.println("Recovering Bank Client Script Maps...");

         for(var19 = 1610; var19 < 1616; ++var19) {
            cache.getIndexes()[17].putFile(var19 >>> 8, var19 & 255, originalCache.getIndexes()[17].getFile(var19 >>> 8, var19 & 255));
         }

         System.out.println("Adding new interfaces...");

         for(var19 = cache.getIndexes()[3].getLastArchiveId() + 1; var19 <= rscache.getIndexes()[3].getLastArchiveId(); ++var19) {
            if(rscache.getIndexes()[3].archiveExists(var19)) {
               cache.getIndexes()[3].putArchive(var19, rscache, false, false);
            }
         }

         cache.getIndexes()[3].putArchive(320, rscache, false, false);
         cache.getIndexes()[3].putArchive(751, rscache, false, false);
         cache.getIndexes()[3].putArchive(1092, rscache, false, false);
         var20 = cache.getIndexes()[3].rewriteTable();
         cache.getIndexes()[8].rewriteTable();
         System.out.println("Packed new interfaces: " + var20);
      }

      Index var18 = cache.getIndexes()[5];
      Index var191 = rscache.getIndexes()[5];
      Index var201 = originalCache.getIndexes()[5];
      RSXteas.loadUnpackedXteas("old");
      System.out.println("Updating Maps.");

      for(regionId = 0; regionId < 30000; ++regionId) {
         regionX = (regionId >> 8) * 64;
         regionY = (regionId & 255) * 64;
         String var221 = "m" + (regionX >> 3) / 8 + "_" + (regionY >> 3) / 8;
         byte[] var23 = var191.getFile(var191.getArchiveId(var221));
         if(var23 == null) {
            var23 = var201.getFile(var201.getArchiveId(var221));
         }

         if(var23 != null) {
            var20 = addMapFile(var18, var221, var23);
            System.out.println(var221 + ", " + var20);
         }

         var221 = "um" + (regionX >> 3) / 8 + "_" + (regionY >> 3) / 8;
         var23 = var191.getFile(var191.getArchiveId(var221));
         if(var23 == null) {
            var23 = var201.getFile(var201.getArchiveId(var221));
         }

         if(var23 != null) {
            var20 = addMapFile(var18, var221, var23);
            System.out.println(var221 + ", " + var20);
         }

         xteas = RSXteas.getXteas(regionId);
         var221 = "l" + (regionX >> 3) / 8 + "_" + (regionY >> 3) / 8;
         var23 = var191.getFile(var191.getArchiveId(var221), 0, xteas);
         if(var23 != null) {
            var20 = addMapFile(var18, var221, var23);
            System.out.println(var221 + ", " + var20);
         }

         var221 = "ul" + (regionX >> 3) / 8 + "_" + (regionY >> 3) / 8;
         var23 = var191.getFile(var191.getArchiveId(var221), 0, xteas);
         if(var23 != null) {
            var20 = addMapFile(var18, var221, var23);
            System.out.println(var221 + ", " + var20);
         }

         var221 = "n" + (regionX >> 3) / 8 + "_" + (regionY >> 3) / 8;
         var23 = var191.getFile(var191.getArchiveId(var221), 0);
         if(var23 == null) {
            var23 = var201.getFile(var201.getArchiveId(var221), 0);
         }

         if(var23 != null) {
            var20 = addMapFile(var18, var221, var23);
            System.out.println(var221 + ", " + var20);
         }
      }

      var18.rewriteTable();
   }

   public static boolean addMapFile(Index index, String name, byte[] data) {
      int archiveId = index.getArchiveId(name);
      if(archiveId == -1) {
         archiveId = index.getTable().getValidArchiveIds().length;
      }

      return index.putFile(archiveId, 0, 2, data, (int[])null, false, false, Utils.getNameHash(name), -1);
   }
}
