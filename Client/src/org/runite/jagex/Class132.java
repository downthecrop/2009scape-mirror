package org.runite.jagex;

final class Class132 {

   static int anInt1734 = 0;
   static CacheIndex aClass153_1735;
   static int anInt1736;
   static int anInt1737 = 1;
   static RSString aClass94_1738 = RSString.createRSString("<col=ffff00>");
   static RSString[] aClass94Array1739 = new RSString[1000];
   static int anInt1740 = 0;
   static int anInt1741;
   static CacheIndex libIndex;


   static final void method1798(int var0, Class3_Sub4 var1) {
      try {
         long var2 = 0L;
         int var4 = -1;
         if(var0 <= 17) {
            anInt1740 = -43;
         }

         int var5 = 0;
         if(-1 == ~var1.anInt2263) {
            var2 = Class157.method2174(var1.anInt2250, var1.anInt2264, var1.anInt2248);
         }

         int var6 = 0;
         if(-2 == ~var1.anInt2263) {
            var2 = Class80.method1395(var1.anInt2250, var1.anInt2264, var1.anInt2248);
         }

         if(var1.anInt2263 == 2) {
            var2 = Class3_Sub28_Sub5.method557(var1.anInt2250, var1.anInt2264, var1.anInt2248);
         }

         if(~var1.anInt2263 == -4) {
            var2 = Class3_Sub2.method104(var1.anInt2250, var1.anInt2264, var1.anInt2248);
         }

         if(var2 != 0L) {
            var4 = Integer.MAX_VALUE & (int)(var2 >>> 32);
            var6 = (int)var2 >> 20 & 3;
            var5 = ((int)var2 & 516214) >> 14;
         }

         var1.anInt2254 = var4;
         var1.anInt2253 = var5;
         var1.anInt2257 = var6;
      } catch (RuntimeException var7) {
         throw Class44.method1067(var7, "sf.B(" + var0 + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

   static final void method1799(byte var0, CacheIndex var1) {
      try {
         Class3_Sub13_Sub7.aClass153_3098 = var1;
         int var2 = 113 / ((1 - var0) / 63);
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "sf.C(" + var0 + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

   public static void method1800(byte var0) {
      try {
         aClass94Array1739 = null;
         aClass94_1738 = null;
         aClass153_1735 = null;
         if(var0 <= 52) {
            aClass94Array1739 = (RSString[])null;
         }

      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "sf.A(" + var0 + ')');
      }
   }

   static final void method1801(byte var0) {
      try {
         if(var0 >= -94) {
            method1799((byte)-90, (CacheIndex)null);
         }

         int var1 = Class168.aClass3_Sub28_Sub17_2096.method682(Class75_Sub4.aClass94_2667);

         int var2;
         int var3;
         for(var2 = 0; Class3_Sub13_Sub34.anInt3415 > var2; ++var2) {
            var3 = Class168.aClass3_Sub28_Sub17_2096.method682(RSByteBuffer.method802(var2, true));
            if(var3 > var1) {
               var1 = var3;
            }
         }

         var2 = 15 * Class3_Sub13_Sub34.anInt3415 + 21;
         int var4 = Class38_Sub1.anInt2612;
         var1 += 8;
         var3 = NPCDefinition.anInt1297 + -(var1 / 2);
         if(~(var4 + var2) < ~Class140_Sub7.anInt2934) {
            var4 = Class140_Sub7.anInt2934 + -var2;
         }

         if(Class23.anInt454 < var3 + var1) {
            var3 = -var1 + Class23.anInt454;
         }

         if(-1 < ~var3) {
            var3 = 0;
         }

         if(~var4 > -1) {
            var4 = 0;
         }

         if(-2 == ~Class3_Sub28_Sub13.anInt3660) {
            if(~NPCDefinition.anInt1297 == ~Class3_Sub13_Sub39.anInt3460 && ~Class168.anInt2099 == ~Class38_Sub1.anInt2612) {
               Class3_Sub28_Sub1.anInt3537 = Class3_Sub13_Sub34.anInt3415 * 15 - -(!CacheIndex.aBoolean1951?22:26);
               Class3_Sub28_Sub13.anInt3660 = 0;
               Class3_Sub13_Sub33.anInt3395 = var4;
               AbstractIndexedSprite.anInt1462 = var3;
               Class38_Sub1.aBoolean2615 = true;
               Class3_Sub28_Sub3.anInt3552 = var1;
            }
         } else if(~NPCDefinition.anInt1297 == ~Class163_Sub1.anInt2993 && ~Class38_Sub1.anInt2612 == ~Class38_Sub1.anInt2614) {
            AbstractIndexedSprite.anInt1462 = var3;
            Class3_Sub28_Sub13.anInt3660 = 0;
            Class3_Sub28_Sub3.anInt3552 = var1;
            Class3_Sub13_Sub33.anInt3395 = var4;
            Class3_Sub28_Sub1.anInt3537 = (CacheIndex.aBoolean1951?26:22) + Class3_Sub13_Sub34.anInt3415 * 15;
            Class38_Sub1.aBoolean2615 = true;
         } else {
            Class168.anInt2099 = Class38_Sub1.anInt2614;
            Class3_Sub13_Sub39.anInt3460 = Class163_Sub1.anInt2993;
            Class3_Sub28_Sub13.anInt3660 = 1;
         }

      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "sf.D(" + var0 + ')');
      }
   }

}
