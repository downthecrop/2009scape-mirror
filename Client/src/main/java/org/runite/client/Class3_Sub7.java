package org.runite.client;

import org.rs09.client.Linkable;

final class Class3_Sub7 extends Linkable {

   static int[] anIntArray2292 = new int[1000];
   static int anInt2294;
   long aLong2295;


   static int method121(int var0, int var1, int var2, int var3, int var4, int var6) {
      try {
         if((var2 & 1) == 1) {
            int var7 = var4;
            var4 = var3;
            var3 = var7;
         }

         var1 &= 3;
         return 0 != var1?(var1 != 1 ?(var1 != 2?-var0 + 7 + 1 + -var3:-var6 + (7 - (var4 + -1))):var0):var6;
      } catch (RuntimeException var8) {
         throw ClientErrorException.clientError(var8, "eb.A(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + 1 + ',' + var6 + ')');
      }
   }

   static void method122(int var0) {
      try {
         GameObject.aClass11ArrayArray1834 = new RSInterface[Unsorted.aClass153_3361.method2121()][];
         Unsorted.aBooleanArray1703 = new boolean[Unsorted.aClass153_3361.method2121()];
      } catch (RuntimeException var2) {
         throw ClientErrorException.clientError(var2, "eb.D(" + var0 + ')');
      }
   }

   static RSString itemStackColor(int var0, int var1) {
      try {
         if(100000 > var1) {
            return RSString.stringCombiner(new RSString[]{ColorCore.DefaultStackColor, RSString.stringAnimator(var1), TextCore.aClass94_1076});
         } else {
            if(var0 != 1000) {
               itemStackColor(-54, 54);
            }

            return var1 >= 10000000? RSString.stringCombiner(new RSString[]{ColorCore.MillionStackColor, RSString.stringAnimator(var1 / 1000000), TextCore.MillionM, TextCore.aClass94_1076}): RSString.stringCombiner(new RSString[]{ColorCore.ThousandStackColor, RSString.stringAnimator(var1 / 1000), TextCore.ThousandK, TextCore.aClass94_1076});
         }
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "eb.C(" + var0 + ',' + var1 + ')');
      }
   }

   public Class3_Sub7() {}

   Class3_Sub7(long var1) {
      try {
         this.aLong2295 = var1;
      } catch (RuntimeException var4) {
         throw ClientErrorException.clientError(var4, "eb.<init>(" + var1 + ')');
      }
   }

}
