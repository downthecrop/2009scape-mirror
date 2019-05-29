package org.runite.jagex;

final class Class46 {

   static CacheIndex aClass153_737;
   static RSString aClass94_738 = RSString.createRSString("Monde de jeu cr-B-B");
   static int anInt739;
   static int anInt740;
   static int anInt741;


   public static void method1085(int var0) {
      try {
         aClass94_738 = null;
         if(var0 == -1) {
            aClass153_737 = null;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "gl.B(" + var0 + ')');
      }
   }

   static final void method1086(CacheIndex var0, int var1) {
      try {
         Class45.aClass153_731 = var0;
         if(var1 != -6) {
            aClass153_737 = (CacheIndex)null;
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "gl.C(" + (var0 != null?"{...}":"null") + ',' + var1 + ')');
      }
   }

   static final void method1087(int var0, int var1) {
      try {
         Class3_Sub30_Sub1.method819(false);
         Class3_Sub13_Sub17.method252(8);
         if(var0 < 38) {
            anInt741 = 118;
         }

         int var2 = Class145.method2076(-37, var1).anInt556;
         if(var2 != 0) {
            int var3 = Class163_Sub1.anIntArray2985[var1];
            if(6 == var2) {
               Class41.anInt688 = var3;
            }

            if(-6 == ~var2) {
               Class66.anInt998 = var3;
            }

            if(-10 == ~var2) {
               Canvas_Sub1.anInt15 = var3;
            }

         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "gl.A(" + var0 + ',' + var1 + ')');
      }
   }

}
