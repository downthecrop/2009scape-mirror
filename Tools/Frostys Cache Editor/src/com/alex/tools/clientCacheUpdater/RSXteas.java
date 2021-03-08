package com.alex.tools.clientCacheUpdater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public final class RSXteas {
   public static final HashMap mapContainersXteas = new HashMap();

   public static final int[] getXteas(int regionId) {
      return (int[])mapContainersXteas.get(Integer.valueOf(regionId));
   }

   public static final void loadUnpackedXteas(String location) {
      try {
         File var12 = new File(location);
         File[] xteasFiles = var12.listFiles();
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
      } catch (Exception var121) {
         var121.printStackTrace();
      }

   }
}
