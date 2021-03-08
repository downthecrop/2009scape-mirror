package com.alex.util.gzip;

import com.alex.io.Stream;

import java.util.zip.Inflater;

public class GZipDecompressor {
   private static final Inflater inflaterInstance = new Inflater(true);

   public static final boolean decompress(Stream stream, byte[] data) {
      Inflater var2 = inflaterInstance;
      Inflater var3 = inflaterInstance;
      synchronized(inflaterInstance) {
         if(stream.getBuffer()[stream.getOffset()] == 31 && stream.getBuffer()[stream.getOffset() + 1] == -117) {
            try {
               inflaterInstance.setInput(stream.getBuffer(), stream.getOffset() + 10, -stream.getOffset() - 18 + stream.getBuffer().length);
               inflaterInstance.inflate(data);
            } catch (Exception var5) {
               inflaterInstance.reset();
               return false;
            }

            inflaterInstance.reset();
            return true;
         } else {
            return false;
         }
      }
   }
}
