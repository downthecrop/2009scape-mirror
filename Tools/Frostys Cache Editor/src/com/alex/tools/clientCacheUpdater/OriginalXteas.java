package com.alex.tools.clientCacheUpdater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public final class OriginalXteas {
   public static final HashMap mapContainersXteas = new HashMap();

   public static final int[] getXteas(int regionId) {
      return (int[])mapContainersXteas.get(Integer.valueOf(regionId));
   }

   public static void init() {
      loadUnpackedXteas();
   }

   public static final void delete() {
   }

   public static final void loadUnpackedXteas() {
      try {
         File var11 = new File("cache667_protected/keys");
         File[] xteasFiles = var11.listFiles();
         File[] arr$ = xteasFiles;
         int len$ = xteasFiles.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            File region = arr$[i$];
            String name = region.getName();
            if(!name.contains(".txt")) {
               region.delete();
            } else {
               short regionId = Short.parseShort(name.replace(".txt", ""));
               if(regionId <= 0) {
                  region.delete();
               } else {
                  BufferedReader in = new BufferedReader(new FileReader(region));
                  int[] xteas = new int[4];

                  for(int index = 0; index < 4; ++index) {
                     xteas[index] = Integer.parseInt(in.readLine());
                  }

                  mapContainersXteas.put(Integer.valueOf(regionId), xteas);
                  in.close();
               }
            }
         }
      } catch (IOException var111) {
         var111.printStackTrace();
      }

   }
}
