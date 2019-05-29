package org.runite.jagex;

final class Class3_Sub13_Sub31 extends Class3_Sub13 {

   static Class93 aClass93_3369 = new Class93(64);
   static RSString aClass94_3370 = RSString.createRSString("hitmarks");
   private static RSString aClass94_3371 = RSString.createRSString("Sat");
   private static RSString aClass94_3372 = RSString.createRSString("Mon");
   static Class3_Sub28_Sub16[] aClass3_Sub28_Sub16Array3373;
   private static RSString aClass94_3374 = RSString.createRSString("Fri");
   static int anInt3375 = 0;
   static int anInt3377 = 7759444;
   private static RSString aClass94_3378 = RSString.createRSString("Wed");
   private static RSString aClass94_3379 = RSString.createRSString("Thu");
   private static RSString aClass94_3380 = RSString.createRSString("Tue");
   private static RSString aClass94_3381 = RSString.createRSString("Sun");
   static RSString aClass94_3382 = RSString.createRSString("(U0a )2 in: ");
   static RSString[] aClass94Array3376 = new RSString[]{aClass94_3381, aClass94_3372, aClass94_3380, aClass94_3378, aClass94_3379, aClass94_3374, aClass94_3371};

   public static void method317(int var0) {
      try {
         aClass94Array3376 = null;
         aClass94_3378 = null;
         aClass94_3374 = null;
         aClass94_3382 = null;
         aClass94_3381 = null;
         aClass94_3372 = null;
         aClass94_3380 = null;
         if(var0 != 7759444) {
            method317(72);
         }

         aClass93_3369 = null;
         aClass94_3379 = null;
         aClass3_Sub28_Sub16Array3373 = null;
         aClass94_3370 = null;
         aClass94_3371 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "rl.C(" + var0 + ')');
      }
   }

   final int[] method154(int var1, byte var2) {
      try {
         int var3 = -96 / ((var2 - 30) / 36);
         return Class102.anIntArray2125;
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "rl.D(" + var1 + ',' + var2 + ')');
      }
   }

   public Class3_Sub13_Sub31() {
      super(0, true);
   }

   static final void method318(int var0) {
      try {
         Class3_Sub4 var1 = (Class3_Sub4)Class3_Sub13_Sub6.aClass61_3075.method1222();
         if(var0 != 7759444) {
            aClass94_3379 = (RSString)null;
         }

         for(; null != var1; var1 = (Class3_Sub4)Class3_Sub13_Sub6.aClass61_3075.method1221()) {
            if(var1.anInt2259 != -1) {
               var1.method86(-1024);
            } else {
               var1.anInt2261 = 0;
               Class132.method1798(56, var1);
            }
         }

      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "rl.B(" + var0 + ')');
      }
   }

}
