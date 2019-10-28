package org.runite.jagex;

public final class Class5 {

   private static long aLong89;
   private static long aLong90;


   public static final synchronized long method830(byte var0) {
      long var1 = System.currentTimeMillis();
      if(~aLong90 < ~var1) {
         aLong89 += aLong90 + -var1;
      }

      if(var0 != -55) {
         method830((byte)-4);
      }

      aLong90 = var1;
      return aLong89 + var1;
   }
}
