package org.runite.jagex;
import java.util.Calendar;

final class Class3_Sub28_Sub9 extends Node {

   int anInt3614;
   static int anInt3615;
   static Calendar aCalendar3616 = Calendar.getInstance();
   private int anInt3617;
   static int anInt3618;
   RSString aClass94_3619;
   static int anInt3620 = 0;
   static RSString aClass94_3621 = null;
   static int anInt3622 = 0;
   static int anInt3623 = 0;
   static int anInt3624;


   static final Class3_Sub28_Sub16_Sub2 method578(int var0) {
      try {
         int var1 = Class3_Sub13_Sub6.anIntArray3076[0] * Class140_Sub7.anIntArray2931[0];
         byte[] var2 = Class163_Sub1.aByteArrayArray2987[0];
         if(var0 != 115) {
            anInt3624 = 112;
         }

         Object var3;
         if(Class3_Sub13_Sub22.aBooleanArray3272[0]) {
            byte[] var4 = Class163_Sub3.aByteArrayArray3005[0];
            int[] var5 = new int[var1];

            for(int var6 = 0; var6 < var1; ++var6) {
               var5[var6] = Class3_Sub13_Sub29.bitwiseOr(Class3_Sub28_Sub15.method633(var4[var6] << 24, -16777216), Class3_Sub13_Sub38.spritePalette[Class3_Sub28_Sub15.method633(255, var2[var6])]);
            }

            var3 = new Class3_Sub28_Sub16_Sub2_Sub1(Class3_Sub15.anInt2426, Class133.anInt1748, Class164.anIntArray2048[0], RSByteBuffer.anIntArray2591[0], Class140_Sub7.anIntArray2931[0], Class3_Sub13_Sub6.anIntArray3076[0], var5);
         } else {
            int[] var8 = new int[var1];

            for(int var9 = 0; var9 < var1; ++var9) {
               var8[var9] = Class3_Sub13_Sub38.spritePalette[Class3_Sub28_Sub15.method633(var2[var9], 255)];
            }

            var3 = new Class3_Sub28_Sub16_Sub2(Class3_Sub15.anInt2426, Class133.anInt1748, Class164.anIntArray2048[0], RSByteBuffer.anIntArray2591[0], Class140_Sub7.anIntArray2931[0], Class3_Sub13_Sub6.anIntArray3076[0], var8);
         }

         Class39.method1035((byte)127);
         return (Class3_Sub28_Sub16_Sub2)var3;
      } catch (RuntimeException var7) {
         throw Class44.method1067(var7, "hn.P(" + var0 + ')');
      }
   }

   private final void method579(int var1, RSByteBuffer var2, int var3) {
      try {
         if(~var1 != -2) {
            if(var1 == 2) {
               this.anInt3614 = var2.getInt();
            } else if(-6 == ~var1) {
               this.aClass94_3619 = var2.getString();
            }
         } else {
            this.anInt3617 = var2.getByte((byte)-71);
         }

         if(var3 != 0) {
            method582(5, 31, 114, true, -67, 14, -33, -115, -101, -61, -25, -121);
         }

      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "hn.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   static final void method580(byte var0) {
      try {
         if(var0 != 80) {
            method582(88, 85, -8, true, 72, 12, 29, 96, 6, 57, -13, 15);
         }

         Class3_Sub2.aClass130_2220 = new Class130(32);
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "hn.B(" + var0 + ')');
      }
   }

   static final void method581(CacheIndex var0, int var1, CacheIndex var2) {
      try {
         Class3_Sub13.aClass3_Sub28_Sub17_2379 = Class73.method1300(0, Class96.anInt1352, (byte)124, var2, var0);
         if(!HDToolKit.highDetail) {
            Class157.aClass3_Sub28_Sub17_Sub1_2000 = (Class3_Sub28_Sub17_Sub1)Class3_Sub13.aClass3_Sub28_Sub17_2379;
         } else {
            Class157.aClass3_Sub28_Sub17_Sub1_2000 = Class70.method1287(Class96.anInt1352, 0, var0, var2, -1);
         }

         Class126.aClass3_Sub28_Sub17_1669 = Class73.method1300(var1, Class75_Sub2.anInt2643, (byte)125, var2, var0);
         Class168.aClass3_Sub28_Sub17_2096 = Class73.method1300(0, Class3_Sub13_Sub11.anInt3132, (byte)124, var2, var0);
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "hn.D(" + (var0 != null?"{...}":"null") + ',' + var1 + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

   static final boolean method582(int var0, int var1, int var2, boolean var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11) {
      try {
         return Class102.player.getSize((byte)114) != var5?(Class102.player.getSize((byte)114) <= 2?Class158_Sub1.method2191(var6, var4, var11, -1001, var10, var9, var2, var1, var3, var8, var0, var7):Class52.method1166(var10, (byte)34, var7, var9, var1, Class102.player.getSize((byte)114), var6, var8, var4, var11, var2, var3, var0)):Class2.method76(var7, var8, var4, var0, var10, var3, var2, var1, var6, var9, 127, var11);
      } catch (RuntimeException var13) {
         throw Class44.method1067(var13, "hn.O(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ',' + var9 + ',' + var10 + ',' + var11 + ')');
      }
   }

   final void method583(int var1, RSByteBuffer var2) {
      try {
         if(var1 == 207) {
            while(true) {
               int var3 = var2.getByte((byte)-96);
               if(var3 == 0) {
                  return;
               }

               this.method579(var3, var2, 0);
            }
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "hn.C(" + var1 + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

   public static void method584(int var0) {
      try {
         aCalendar3616 = null;
         if(var0 != 0) {
            method580((byte)-90);
         }

         aClass94_3621 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "hn.F(" + var0 + ')');
      }
   }

   final boolean method585(int var1) {
      try {
         if(var1 != 0) {
            aClass94_3621 = (RSString)null;
         }

         return this.anInt3617 == 115;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "hn.E(" + var1 + ')');
      }
   }

}
