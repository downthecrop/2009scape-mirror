package org.runite.jagex;

final class Class12 {

   static float aFloat319;
   GameObject object;
   private static RSString aClass94_321 = RSString.createRSString("Checking for updates )2 ");
   static CacheIndex aClass153_322;
   static CacheIndex aClass153_323;
   int anInt324;
   static RSString aClass94_325 = RSString.createRSString(",Mcran)2titre charg-B");
   int anInt326;
   static RSString aClass94_327 = aClass94_321;
   long aLong328;
   boolean aBoolean329 = false;
   int anInt330;
   static RSString aClass94_331 = RSString.createRSString("(U1");


   static final void method870(int var0, byte var1, int var2, int var3, int var4, int var5) {
      try {
         int var6;
         int var7;
         for(var6 = var2; var4 + var2 >= var6; ++var6) {
            for(var7 = var3; var5 + var3 >= var7; ++var7) {
               if(-1 >= ~var7 && 104 > var7 && -1 >= ~var6 && 104 > var6) {
                  Class67.aByteArrayArrayArray1014[var0][var7][var6] = 127;
               }
            }
         }

         for(var6 = var2; var4 + var2 > var6; ++var6) {
            for(var7 = var3; ~(var3 + var5) < ~var7; ++var7) {
               if(~var7 <= -1 && ~var7 > -105 && ~var6 <= -1 && var6 < 104) {
                  Class44.anIntArrayArrayArray723[var0][var7][var6] = var0 <= 0?0:Class44.anIntArrayArrayArray723[var0 + -1][var7][var6];
               }
            }
         }

         if(0 < var3 && ~var3 > -105) {
            for(var6 = 1 + var2; ~(var2 + var4) < ~var6; ++var6) {
               if(-1 >= ~var6 && var6 < 104) {
                  Class44.anIntArrayArrayArray723[var0][var3][var6] = Class44.anIntArrayArrayArray723[var0][var3 - 1][var6];
               }
            }
         }

         if(~var2 < -1 && ~var2 > -105) {
            for(var6 = var3 + 1; ~(var3 - -var5) < ~var6; ++var6) {
               if(var6 >= 0 && 104 > var6) {
                  Class44.anIntArrayArrayArray723[var0][var6][var2] = Class44.anIntArrayArrayArray723[var0][var6][var2 + -1];
               }
            }
         }

         var6 = 56 % ((var1 - -18) / 50);
         if(var3 >= 0 && var2 >= 0 && var3 < 104 && ~var2 > -105) {
            if(var0 != 0) {
               if(~var3 < -1 && Class44.anIntArrayArrayArray723[-1 + var0][var3 + -1][var2] != Class44.anIntArrayArrayArray723[var0][-1 + var3][var2]) {
                  Class44.anIntArrayArrayArray723[var0][var3][var2] = Class44.anIntArrayArrayArray723[var0][-1 + var3][var2];
               } else if(0 < var2 && ~Class44.anIntArrayArrayArray723[var0][var3][-1 + var2] != ~Class44.anIntArrayArrayArray723[-1 + var0][var3][var2 + -1]) {
                  Class44.anIntArrayArrayArray723[var0][var3][var2] = Class44.anIntArrayArrayArray723[var0][var3][var2 - 1];
               } else if(-1 > ~var3 && -1 > ~var2 && ~Class44.anIntArrayArrayArray723[var0][-1 + var3][var2 - 1] != ~Class44.anIntArrayArrayArray723[var0 - 1][-1 + var3][var2 - 1]) {
                  Class44.anIntArrayArrayArray723[var0][var3][var2] = Class44.anIntArrayArrayArray723[var0][-1 + var3][var2 - 1];
               }
            } else if(0 < var3 && 0 != Class44.anIntArrayArrayArray723[var0][var3 + -1][var2]) {
               Class44.anIntArrayArrayArray723[var0][var3][var2] = Class44.anIntArrayArrayArray723[var0][var3 + -1][var2];
            } else if(-1 > ~var2 && ~Class44.anIntArrayArrayArray723[var0][var3][var2 - 1] != -1) {
               Class44.anIntArrayArrayArray723[var0][var3][var2] = Class44.anIntArrayArrayArray723[var0][var3][var2 + -1];
            } else if(var3 > 0 && 0 < var2 && Class44.anIntArrayArrayArray723[var0][var3 - 1][var2 + -1] != 0) {
               Class44.anIntArrayArrayArray723[var0][var3][var2] = Class44.anIntArrayArrayArray723[var0][var3 - 1][var2 + -1];
            }
         }

      } catch (RuntimeException var8) {
         throw Class44.method1067(var8, "bm.B(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ')');
      }
   }

   public static void method871(byte var0) {
      try {
         aClass94_321 = null;
         aClass94_327 = null;
         aClass94_331 = null;
         aClass153_322 = null;
         aClass153_323 = null;
         aClass94_325 = null;
         int var1 = -93 / ((2 - var0) / 50);
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "bm.A(" + var0 + ')');
      }
   }

   static final int method872(int var0, int var1, int var2) {
      try {
         Class3_Sub25 var3 = (Class3_Sub25)Class3_Sub2.aClass130_2220.method1780((long)var1, 0);
         return null == var3?0:(var0 >= ~var2 && var2 < var3.anIntArray2551.length?var3.anIntArray2551[var2]:0);
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "bm.C(" + var0 + ',' + var1 + ',' + var2 + ')');
      }
   }

   static final byte[] method873(byte var0, byte[] var1) {
      try {
         int var2 = var1.length;
         byte[] var3 = new byte[var2];
         if(var0 != 62) {
            return (byte[])null;
         } else {
            Class76.method1357(var1, 0, var3, 0, var2);
            return var3;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "bm.D(" + var0 + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

}
