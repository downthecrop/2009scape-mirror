package org.runite.client;

final class Class107 {

   static RSInterface aClass11_1453;
   static CacheIndex aClass153_878;


   static void method1645(CacheIndex var0, CacheIndex var1) {
      try {
         KeyboardListener.aClass153_1916 = var1;
         int var4 = (int)(21.0D * Math.random()) - 10;
         aClass153_878 = var0;
         int var5 = (int)(21.0D * Math.random()) - 10;
         aClass153_878.getFileAmount(34);
         int var3 = (int)(Math.random() * 21.0D) + -10;
         int var6 = -20 + (int)(41.0D * Math.random());
         Class158.anInt2015 = var6 + var5;
         Class46.anInt740 = var4 + var6;
         Class102.anInt2136 = var6 + var3;
      } catch (RuntimeException var7) {
         throw ClientErrorException.clientError(var7, "og.F(" + (var0 != null?"{...}":"null") + ',' + (var1 != null?"{...}":"null") + ',' + (byte) -67 + ')');
      }
   }

   static void method1647(int var1, int var2, Class140_Sub4 var3, int var4, int var5, int var6) {
      try {

         Unsorted.method1724(var6, var2, var3.anInt2829, var5, var1, var3.anInt2819, (byte)-85, var4);
      } catch (RuntimeException var8) {
         throw ClientErrorException.clientError(var8, "og.A(" + (byte) 122 + ',' + var1 + ',' + var2 + ',' + (var3 != null?"{...}":"null") + ',' + var4 + ',' + var5 + ',' + var6 + ')');
      }
   }

   static void method1648(CacheIndex var0) {
      try {
         Class101.aClass153_1420 = var0;

      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "og.B(" + (var0 != null?"{...}":"null") + ',' + 255 + ')');
      }
   }

   static void method1649(int var0, int var1) {
      try {
         if(var1 <= -65) {
            InterfaceWidget var2 = InterfaceWidget.getWidget(10, var0);
            var2.a();
         }
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "og.C(" + var0 + ',' + var1 + ')');
      }
   }

//   static void method1650() {
//      try {
//         Class61.aReferenceCache_939.clearSoftReferences();
//
//      } catch (RuntimeException var2) {
//         throw ClientErrorException.clientError(var2, "og.G(" + 21 + ')');
//      }
//   }

   static int method1651(int var1, int var2) {
      try {
         int var3;
         if(var1 > var2) {
            var3 = var2;
            var2 = var1;
            var1 = var3;
         }

         while(var1 != 0) {
            var3 = var2 % var1;
            var2 = var1;
            var1 = var3;
         }

         return var2;
      } catch (RuntimeException var4) {
         throw ClientErrorException.clientError(var4, "og.D(" + 19067 + ',' + var1 + ',' + var2 + ')');
      }
   }

}
