package org.runite.jagex;

final class Class54 {

   static int anInt869;
   int[] anIntArray870;
   static RSString aClass94_871 = RSString.createRSString("hint_headicons");
   static int anInt872;
   RSString[] aClass94Array873;
   Class3_Sub28_Sub15 aClass3_Sub28_Sub15_874;
   static RSString aClass94_875 = RSString.createRSString("Benutzen");
   static int anInt876;
   int anInt877 = -1;
   static CacheIndex aClass153_878;


   static final void method1175(int var0, int var1) {
      try {
         Class3_Sub13_Sub30.anInt3362 = -1;
         Class82.anInt1150 = -1;
         if(var1 <= 55) {
            method1177(-67, 28L, (byte)76, (RSString)null, -45, (short)94, (RSString)null, -125);
         }

         Class3_Sub28_Sub1.anInt3536 = var0;
         Class3_Sub5.method117((byte)87);
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "hj.D(" + var0 + ',' + var1 + ')');
      }
   }

   static final boolean method1176(RSString var0, byte var1) {
      try {
         if(var0 != null) {
            for(int var2 = 0; Class8.anInt104 > var2; ++var2) {
               if(var0.equals(-121, Class70.aClass94Array1046[var2])) {
                  return true;
               }
            }

            if(var1 != -82) {
               aClass94_871 = (RSString)null;
            }

            if(var0.equals(var1 + -46, Class102.player.displayName)) {
               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "hj.A(" + (var0 != null?"{...}":"null") + ',' + var1 + ')');
      }
   }

   static final void method1177(int var0, long var1, byte var3, RSString var4, int var5, short var6, RSString var7, int var8) {
      try {
         if(var3 > -23) {
            method1177(-45, 37L, (byte)-37, (RSString)null, -16, (short)110, (RSString)null, -75);
         }

         if(!Class38_Sub1.aBoolean2615) {
            if(Class3_Sub13_Sub34.anInt3415 < 500) {
               Class140_Sub7.aClass94Array2935[Class3_Sub13_Sub34.anInt3415] = var7;
               Class163_Sub2_Sub1.aClass94Array4016[Class3_Sub13_Sub34.anInt3415] = var4;
               Class114.anIntArray1578[Class3_Sub13_Sub34.anInt3415] = ~var0 == 0?Class3_Sub28_Sub5.anInt3590:var0;
               Class3_Sub13_Sub7.aShortArray3095[Class3_Sub13_Sub34.anInt3415] = var6;
               Class3_Sub13_Sub22.aLongArray3271[Class3_Sub13_Sub34.anInt3415] = var1;
               Class117.anIntArray1613[Class3_Sub13_Sub34.anInt3415] = var5;
               Class27.anIntArray512[Class3_Sub13_Sub34.anInt3415] = var8;
               ++Class3_Sub13_Sub34.anInt3415;
            }

         }
      } catch (RuntimeException var10) {
         throw Class44.method1067(var10, "hj.C(" + var0 + ',' + var1 + ',' + var3 + ',' + (var4 != null?"{...}":"null") + ',' + var5 + ',' + var6 + ',' + (var7 != null?"{...}":"null") + ',' + var8 + ')');
      }
   }

   public static void method1178(byte var0) {
      try {
         int var1 = 103 / ((var0 - -13) / 52);
         aClass94_875 = null;
         aClass94_871 = null;
         aClass153_878 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "hj.B(" + var0 + ')');
      }
   }

}
