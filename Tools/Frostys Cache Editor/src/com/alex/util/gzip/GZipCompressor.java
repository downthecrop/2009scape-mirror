package com.alex.util.gzip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class GZipCompressor {
   public static final byte[] compress(byte[] data) {
      ByteArrayOutputStream compressedBytes = new ByteArrayOutputStream();

      try {
         GZIPOutputStream var3 = new GZIPOutputStream(compressedBytes);
         var3.write(data);
         var3.finish();
         var3.close();
         return compressedBytes.toByteArray();
      } catch (IOException var31) {
         var31.printStackTrace();
         return null;
      }
   }
}
