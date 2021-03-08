package com.editor;

import com.alex.store.Store;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Utils {
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
         System.out.println("Failed packing model " + archiveId);
         return -1;
      }
   }

   public static int packCustomModel(Store cache, byte[] data, int modelId) {
      if(cache.getIndexes()[7].putFile(modelId, 0, data)) {
         return modelId;
      } else {
         System.out.println("Failed packing model " + modelId);
         return -1;
      }
   }
}
