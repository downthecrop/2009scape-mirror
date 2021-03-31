package org.runite.client;

class Class163_Sub2 extends Class163 {

   static RSString paramSettings = null;
   static Class30[] aClass30Array2998 = new Class30[29]; //TODO


   static Class25 method2217(int var0, int var1, int var2) {
      Class3_Sub2 var3 = Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2];
      if (var3 != null) {
         for (int var4 = 0; var4 < var3.anInt2223; ++var4) {
            Class25 var5 = var3.aClass25Array2221[var4];
            if ((var5.aLong498 >> 29 & 3L) == 2L && var5.anInt483 == var1 && var5.anInt478 == var2) {
               Class158.method2186(var5);
               return var5;
            }
         }

      }
      return null;
   }

}
