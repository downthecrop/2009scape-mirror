package org.runite.jagex;

final class RuntimeException_Sub1 extends RuntimeException {

   static int[] anIntArray2113 = new int[]{2, 2, 4, 0, 1, 8, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0};
   static Class96[] aClass96Array2114 = new Class96[4];
   static int anInt2115 = -1;
   static RSString aClass94_2116 = RSString.createRSString("Hidden)2use");
   String aString2117;
   Throwable aThrowable2118;
   static RSString[] aClass94Array2119 = null;
   static int anInt2120 = 0;
   static String worldListHost;


   static final void method2285(int var0, int var1, int var2, int var3, boolean var4, int var5) {
      try {
         Class3_Sub29.anInt2587 = var1;
         if(!var4) {
            Class3_Sub13_Sub8.anInt3103 = var5;
            Class140_Sub7.anInt2938 = var0;
            Class9.anInt144 = var3;
            Class3_Sub28_Sub15.anInt3695 = var2;
         }
      } catch (RuntimeException var7) {
         throw Class44.method1067(var7, "ld.B(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ')');
      }
   }

   static final int method2286(byte var0) {
      try {
         if(var0 != -5) {
            worldListHost = (String)null;
         }

         return 6;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "ld.A(" + var0 + ')');
      }
   }

   RuntimeException_Sub1(Throwable var1, String var2) {
      this.aString2117 = var2;
      this.aThrowable2118 = var1;
   }

   static final int method2287(int var0, byte var1) {
      try {
         return (-98 < ~var0 || ~var0 < -123) && (-225 < ~var0 || ~var0 < -255 || 247 == var0)?(-256 != ~var0?(var0 != 156?(var1 != 59?72:var0):140):159):var0 + -32;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ld.C(" + var0 + ',' + var1 + ')');
      }
   }

   public static void method2288(boolean var0) {
      anIntArray2113 = null;
      aClass94_2116 = null;
      aClass96Array2114 = null;
      aClass94Array2119 = null;
      if(var0) {
         method2287(-64, (byte)-87);
      }

      worldListHost = null;
   }

}
