package org.runite.jagex;

final class Class84 {

   static int[][] anIntArrayArray1160 = new int[104][104];
   static int[] anIntArray1161;
   static Class61 aClass61_1162 = new Class61();
   static int[] anIntArray1163 = new int[1000];
   static int anInt1164 = 0;
   static int anInt1165 = -1;


   static final void method1417(int var0) {
      try {
         if(var0 < 98) {
            method1418(55, (CacheIndex)null);
         }

         if(~Class143.loadingStage == -11 && HDToolKit.highDetail) {
            Class117.method1719(28, 5);
         }

         if(~Class143.loadingStage == -31) {
            Class117.method1719(25, 5);
         }

      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "lf.D(" + var0 + ')');
      }
   }

   static final void method1418(int var0, CacheIndex var1) {
      try {
         Class163_Sub2_Sub1.aClass109_Sub1Array4027 = Class3_Sub28_Sub13.method619((byte)65, NPC.anInt4001, var1);
         Class52.anIntArray861 = new int[256];

         int var2;
         for(var2 = 0; -4 < ~var2; ++var2) {
            int var4 = (Class131.anIntArray1729[1 + var2] & 16711680) >> 16;
            float var3 = (float)((Class131.anIntArray1729[var2] & 16711680) >> 16);
            float var6 = (float)(Class131.anIntArray1729[var2] >> 8 & 255);
            float var9 = (float)(Class131.anIntArray1729[var2] & 255);
            float var5 = ((float)var4 - var3) / 64.0F;
            int var7 = (Class131.anIntArray1729[var2 + 1] & '\uff00') >> 8;
            float var8 = (-var6 + (float)var7) / 64.0F;
            int var10 = Class131.anIntArray1729[var2 + 1] & 255;
            float var11 = ((float)var10 - var9) / 64.0F;

            for(int var12 = 0; -65 < ~var12; ++var12) {
               Class52.anIntArray861[var12 + 64 * var2] = Class3_Sub13_Sub29.bitwiseOr((int)var9, Class3_Sub13_Sub29.bitwiseOr((int)var6 << 8, (int)var3 << 16));
               var6 += var8;
               var9 += var11;
               var3 += var5;
            }
         }

         for(var2 = 192; var2 < 255; ++var2) {
            Class52.anIntArray861[var2] = Class131.anIntArray1729[3];
         }

         var2 = -35 / ((-51 - var0) / 42);
         Class161.anIntArray2026 = new int['\u8000'];
         OutputStream_Sub1.anIntArray49 = new int['\u8000'];
         Class3_Sub13_Sub10.method215((byte)-89, (LDIndexedSprite)null);
         Class3_Sub30_Sub1.anIntArray3805 = new int['\u8000'];
         Class127.anIntArray1681 = new int['\u8000'];
         Class97.aClass3_Sub28_Sub16_Sub2_1381 = new Class3_Sub28_Sub16_Sub2(128, 254);
      } catch (RuntimeException var13) {
         throw Class44.method1067(var13, "lf.E(" + var0 + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

   static final void method1419(int var0) {
      try {
         int var2 = Class3_Sub13_Sub33.anInt3395;
         int var3 = Class3_Sub28_Sub3.anInt3552;
         int var1 = AbstractIndexedSprite.anInt1462;
         int var5 = 6116423;
         int var4 = Class3_Sub28_Sub1.anInt3537;
         if(!HDToolKit.highDetail) {
            Class74.method1323(var1, var2, var3, var4, var5);
            Class74.method1323(1 + var1, 1 + var2, var3 + -2, 16, 0);
            Class74.method1311(1 + var1, var2 + 18, -2 + var3, -19 + var4, 0);
         } else {
            Class22.method934(var1, var2, var3, var4, var5);
            Class22.method934(1 + var1, 1 + var2, var3 + -2, 16, 0);
            Class22.method927(1 + var1, var2 + 18, var3 + -2, -19 + var4, 0);
         }

         Class168.aClass3_Sub28_Sub17_2096.method681(Class75_Sub4.aClass94_2667, var1 - -3, var2 + 14, var5, -1);
         int var7 = Class130.anInt1709;
         int var6 = Class126.anInt1676;
         if(var0 >= -113) {
            anIntArrayArray1160 = (int[][])((int[][])null);
         }

         for(int var8 = 0; ~Class3_Sub13_Sub34.anInt3415 < ~var8; ++var8) {
            int var9 = (-var8 + -1 + Class3_Sub13_Sub34.anInt3415) * 15 + var2 - -31;
            int var10 = 16777215;
            if(~var1 > ~var6 && ~var6 > ~(var1 - -var3) && -13 + var9 < var7 && ~var7 > ~(3 + var9)) {
               var10 = 16776960;
            }

            Class168.aClass3_Sub28_Sub17_2096.method681(RSByteBuffer.method802(var8, true), var1 - -3, var9, var10, 0);
         }

         Class69.method1282(AbstractIndexedSprite.anInt1462, (byte)107, Class3_Sub13_Sub33.anInt3395, Class3_Sub28_Sub1.anInt3537, Class3_Sub28_Sub3.anInt3552);
      } catch (RuntimeException var11) {
         throw Class44.method1067(var11, "lf.A(" + var0 + ')');
      }
   }

   static final void method1420(int var0, int var1, int var2, int var3, byte var4) {
      try {
         Class3_Sub28_Sub6 var5 = Class3_Sub24_Sub3.method466(4, 10, var0);
         var5.g((byte)33);
         var5.anInt3597 = var2;
         var5.anInt3598 = var3;
         var5.anInt3596 = var1;
         if(var4 >= -35) {
            anInt1165 = 86;
         }

      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "lf.C(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ')');
      }
   }

   static final int method1421(int var0) {
      try {
         if(var0 != -2) {
            method1422((byte)26);
         }

         return ((Class3_Sub13_Sub15.aBoolean3184?1:0) << 19) + (((Class38.aBoolean661?1:0) << 16) + ((!Class128.aBoolean1685?0:1) << 15) + ((!Class106.aBoolean1441?0:1) << 13) + ((Class140_Sub6.aBoolean2910?1:0) << 10) + ((Class3_Sub13_Sub22.aBoolean3275?1:0) << 9) + ((RSInterface.aBoolean236?1:0) << 7) + ((!Class25.aBoolean488?0:1) << 6) + ((Class148.aBoolean1905?1:0) << 5) + (((!Class3_Sub28_Sub13.aBoolean3665?0:1) << 3) + (Class3_Sub28_Sub10.anInt3625 & 7) - (-((!Class3_Sub28_Sub7.aBoolean3604?0:1) << 4) + -((WorldListEntry.aBoolean2623?1:0) << 8)) - (-(Class80.anInt1137 << 11 & 6144) + -((-1 == ~CS2Script.anInt2453?0:1) << 20) - (((~Class9.anInt120 != -1?1:0) << 21) + ((~Class14.anInt340 == -1?0:1) << 22)))) - -(Class127_Sub1.method1757() << 23));
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "lf.F(" + var0 + ')');
      }
   }

   public static void method1422(byte var0) {
      try {
         anIntArrayArray1160 = (int[][])null;
         anIntArray1161 = null;
         aClass61_1162 = null;
         anIntArray1163 = null;
         if(var0 != 24) {
            method1420(-74, 65, 51, 91, (byte)-26);
         }

      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "lf.B(" + var0 + ')');
      }
   }

}
