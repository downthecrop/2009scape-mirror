package org.runite.jagex;

final class Class69 {

   static int anInt1037;
   static int anInt1038;
   static Class30 aClass30_1039;
   static boolean aBoolean1040 = false;
   private int[] anIntArray1041;
   static int anInt1042;
   static CacheIndex aClass153_1043;
   static RSString aClass94_1044 = RSString.createRSString("Titelbild geladen)3");


   final int method1280(int var1, int var2) {
      try {
         if(var2 != 1) {
            return -68;
         } else {
            int var3 = (this.anIntArray1041.length >> 1) + -1;
            int var4 = var3 & var1;

            while(true) {
               int var5 = this.anIntArray1041[1 + var4 + var4];
               if(var5 == -1) {
                  return -1;
               }

               if(var1 == this.anIntArray1041[var4 + var4]) {
                  return var5;
               }

               var4 = var4 - -1 & var3;
            }
         }
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "jg.D(" + var1 + ',' + var2 + ')');
      }
   }

   static final LDIndexedSprite[] method1281(int var0) {
      try {
         LDIndexedSprite[] var1 = new LDIndexedSprite[Class95.anInt1338];

         for(int var2 = var0; ~var2 > ~Class95.anInt1338; ++var2) {
            var1[var2] = new LDIndexedSprite(Class3_Sub15.anInt2426, Class133.anInt1748, Class164.anIntArray2048[var2], RSByteBuffer.anIntArray2591[var2], Class140_Sub7.anIntArray2931[var2], Class3_Sub13_Sub6.anIntArray3076[var2], Class163_Sub1.aByteArrayArray2987[var2], Class3_Sub13_Sub38.spritePalette);
         }

         Class39.method1035((byte)116);
         return var1;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "jg.A(" + var0 + ')');
      }
   }

   static final void method1282(int var0, byte var1, int var2, int var3, int var4) {
      try {
         int var5 = 0;

         for(int var6 = 58 / ((56 - var1) / 49); ~Class3_Sub28_Sub3.anInt3557 < ~var5; ++var5) {
            if(var0 < Class155.anIntArray1969[var5] - -Class3_Sub28_Sub18.anIntArray3768[var5] && var0 + var4 > Class155.anIntArray1969[var5] && ~(Player.anIntArray3954[var5] - -Class140_Sub4.anIntArray2794[var5]) < ~var2 && ~Player.anIntArray3954[var5] > ~(var3 + var2)) {
               Class163_Sub1_Sub1.aBooleanArray4008[var5] = true;
            }
         }

      } catch (RuntimeException var7) {
         throw Class44.method1067(var7, "jg.C(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ')');
      }
   }

   public static void method1283(byte var0) {
      try {
         aClass153_1043 = null;
         aClass30_1039 = null;
         aClass94_1044 = null;
         int var1 = -9 / ((var0 - 64) / 53);
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "jg.B(" + var0 + ')');
      }
   }

   Class69(int[] var1) {
      try {
         int var2;
         for(var2 = 1; (var1.length >> 1) + var1.length >= var2; var2 <<= 1) {
            ;
         }

         this.anIntArray1041 = new int[var2 + var2];

         int var3;
         for(var3 = 0; var2 + var2 > var3; ++var3) {
            this.anIntArray1041[var3] = -1;
         }

         int var4;
         for(var3 = 0; var1.length > var3; this.anIntArray1041[var4 - -var4 - -1] = var3++) {
            for(var4 = -1 + var2 & var1[var3]; 0 != ~this.anIntArray1041[1 + var4 - -var4]; var4 = -1 + var2 & 1 + var4) {
               ;
            }

            this.anIntArray1041[var4 + var4] = var1[var3];
         }

      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "jg.<init>(" + (var1 != null?"{...}":"null") + ')');
      }
   }

}
