package org.runite.jagex;

final class Class3_Sub6 extends Class3 {


   private static RSString aClass94_2286 = RSString.createRSString("wishes to trade with you)3");
   static byte[][] aByteArrayArray2287;
   static int[] anIntArray2288 = new int[32];
   byte[] aByteArray2289;
   static int anInt2290;
   static int anInt2291;
   static RSString aClass94_2285 = aClass94_2286;

   public static void method118(int var0) {
      try {
         anIntArray2288 = null;
         aByteArrayArray2287 = (byte[][])null;
         aClass94_2285 = null;
         if(var0 != 2) {
            method119((float[])null, 91);
         }

         aClass94_2286 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "ea.A(" + var0 + ')');
      }
   }

   static final float[] method119(float[] var0, int var1) {
      try {
         if(var0 != null) {
            float[] var2 = new float[var0.length];
            Class76.method1360(var0, var1, var2, 0, var0.length);
            return var2;
         } else {
            return null;
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ea.B(" + (var0 != null?"{...}":"null") + ',' + var1 + ')');
      }
   }

   Class3_Sub6(byte[] var1) {
      try {
         this.aByteArray2289 = var1;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ea.<init>(" + (var1 != null?"{...}":"null") + ')');
      }
   }

   static {
      int var0 = 2;

      for(int var1 = 0; var1 < 32; ++var1) {
         anIntArray2288[var1] = -1 + var0;
         var0 += var0;
      }

      anInt2291 = 1;
   }
}
