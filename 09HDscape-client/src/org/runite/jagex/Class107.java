package org.runite.jagex;

final class Class107 {

   static int currentChunkY;
   static RSInterface aClass11_1453;
   static RSString[] aClass94Array1454;
   static RSString aClass94_1455 = RSString.createRSString(" loggt sich aus)3");


   static final void method1645(CacheIndex var0, CacheIndex var1, byte var2) {
      try {
         Class148.aClass153_1916 = var1;
         int var4 = (int)(21.0D * Math.random()) - 10;
         Class54.aClass153_878 = var0;
         int var5 = (int)(21.0D * Math.random()) - 10;
         Class54.aClass153_878.getFileAmount(34, (byte)103);
         int var3 = (int)(Math.random() * 21.0D) + -10;
         int var6 = -20 + (int)(41.0D * Math.random());
         Class158.anInt2015 = var6 + var5;
         if(var2 == -67) {
            Class46.anInt740 = var4 + var6;
            Class102.anInt2136 = var6 + var3;
         }
      } catch (RuntimeException var7) {
         throw Class44.method1067(var7, "og.F(" + (var0 != null?"{...}":"null") + ',' + (var1 != null?"{...}":"null") + ',' + var2 + ')');
      }
   }

   public static void method1646(boolean var0) {
      try {
         aClass94_1455 = null;
         if(!var0) {
            aClass94_1455 = (RSString)null;
         }

         aClass94Array1454 = null;
         aClass11_1453 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "og.E(" + var0 + ')');
      }
   }

   static final void method1647(byte var0, int var1, int var2, Class140_Sub4 var3, int var4, int var5, int var6) {
      try {
         if(var0 != 122) {
            method1648((CacheIndex)null, 7);
         }

         Class118.method1724(var6, var2, var3.anInt2829, var5, var1, var3.anInt2819, (byte)-85, var4);
      } catch (RuntimeException var8) {
         throw Class44.method1067(var8, "og.A(" + var0 + ',' + var1 + ',' + var2 + ',' + (var3 != null?"{...}":"null") + ',' + var4 + ',' + var5 + ',' + var6 + ')');
      }
   }

   static final void method1648(CacheIndex var0, int var1) {
      try {
         Class101.aClass153_1420 = var0;
         if(var1 != 255) {
            currentChunkY = -56;
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "og.B(" + (var0 != null?"{...}":"null") + ',' + var1 + ')');
      }
   }

   static final void method1649(int var0, int var1) {
      try {
         if(var1 <= -65) {
            Class3_Sub28_Sub6 var2 = Class3_Sub24_Sub3.method466(4, 10, var0);
            var2.a(true);
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "og.C(" + var0 + ',' + var1 + ')');
      }
   }

   static final void method1650(int var0) {
      try {
         Class61.aClass93_939.method1523((byte)-113);
         if(var0 != 21) {
            aClass11_1453 = (RSInterface)null;
         }

      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "og.G(" + var0 + ')');
      }
   }

   static final int method1651(int var0, int var1, int var2) {
      try {
         int var3;
         if(~var2 > ~var1) {
            var3 = var2;
            var2 = var1;
            var1 = var3;
         }

         while(var1 != 0) {
            var3 = var2 % var1;
            var2 = var1;
            var1 = var3;
         }

         if(var0 != 19067) {
            aClass94Array1454 = (RSString[])null;
         }

         return var2;
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "og.D(" + var0 + ',' + var1 + ',' + var2 + ')');
      }
   }

}
