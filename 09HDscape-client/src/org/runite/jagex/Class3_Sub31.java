package org.runite.jagex;

final class Class3_Sub31 extends Class3 {

   int anInt2602;
   int anInt2603;
   static Class93 aClass93_2604 = new Class93(64);
   static RSString aClass94_2605 = RSString.createRSString("::clientjs5drop");
   static int[] anIntArray2606;
   static int countryId;
   static RSString aClass94_2608 = RSString.createRSString(")4l=");


   static final void method820(int var0, int var1) {
      try {
         Class148.aClass93_1911.method1522(-127, var0);
         if(var1 == 64) {
            Class80.aClass93_1131.method1522(-126, var0);
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "wk.C(" + var0 + ',' + var1 + ')');
      }
   }

   public static void method821(int var0) {
      try {
         aClass93_2604 = null;
         anIntArray2606 = null;
         aClass94_2605 = null;
         aClass94_2608 = null;
         if(var0 != 26971) {
            method820(-51, -76);
         }

      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "wk.B(" + var0 + ')');
      }
   }

   static final RSString[] method822(int var0, RSString[] var1) {
      try {
         RSString[] var2 = new RSString[5];
         if(var0 != 19406) {
            method821(-26);
         }

         for(int var3 = 0; ~var3 > -6; ++var3) {
            var2[var3] = RenderAnimationDefinition.method903(new RSString[]{Class72.method1298((byte)9, var3), Class3_Sub28_Sub4.aClass94_3577}, (byte)-67);
            if(var1 != null && null != var1[var3]) {
               var2[var3] = RenderAnimationDefinition.method903(new RSString[]{var2[var3], var1[var3]}, (byte)-62);
            }
         }

         return var2;
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "wk.A(" + var0 + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

}
