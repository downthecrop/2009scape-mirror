package org.runite.jagex;

final class Class159 {

   static Class93 aClass93_2016 = new Class93(100);
   static int[] anIntArray2017 = new int[]{1, 2, 4, 8};
   static RSString aClass94_2018 = RSString.createRSString("Cabbage");
   static CacheIndex aClass153_2019;
   static int anInt2020 = 0;
   static int[] anIntArray2021 = new int[2];
   static int localPlayerCount = 0;
   static int anInt2023 = 0;
   static int anInt2024 = 0;


   static final Class12 method2193(int var0, int var1, int var2) {
      Class3_Sub2 var3 = Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2];
      if(var3 == null) {
         return null;
      } else {
         Class12 var4 = var3.aClass12_2230;
         var3.aClass12_2230 = null;
         return var4;
      }
   }

   static final boolean method2194(int var0) {
      try {
         if(Class3_Sub28_Sub11.aBoolean3641) {
            try {
               return !((Boolean)Class3_Sub13_Sub17.aClass94_3209.method1577(var0 + -2112, Class38.aClass87_665.anApplet1219)).booleanValue();
            } catch (Throwable var2) {
               ;
            }
         }

         return var0 != 255?true:true;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "vl.B(" + var0 + ')');
      }
   }

   static final void method2195(int var0, int var1) {
      try {
         short var2 = 256;
         if(var0 > var2) {
            var0 = var2;
         }

         if(~var0 < -11) {
            var0 = 10;
         }

         Class72.anInt1071 += var0 * 128;
         int var3;
         if(Class161.anIntArray2026.length < Class72.anInt1071) {
            Class72.anInt1071 -= Class161.anIntArray2026.length;
            var3 = (int)(12.0D * Math.random());
            Class3_Sub13_Sub10.method215((byte)-119, Class163_Sub2_Sub1.aClass109_Sub1Array4027[var3]);
         }

         var3 = var1;
         int var5 = (var2 + -var0) * 128;
         int var4 = 128 * var0;

         int var6;
         int var7;
         for(var6 = 0; ~var5 < ~var6; ++var6) {
            var7 = Class127.anIntArray1681[var3 - -var4] - var0 * Class161.anIntArray2026[-1 + Class161.anIntArray2026.length & Class72.anInt1071 + var3] / 6;
            if(0 > var7) {
               var7 = 0;
            }

            Class127.anIntArray1681[var3++] = var7;
         }

         int var8;
         int var9;
         for(var6 = var2 + -var0; var2 > var6; ++var6) {
            var7 = var6 * 128;

            for(var8 = 0; 128 > var8; ++var8) {
               var9 = (int)(100.0D * Math.random());
               if(-51 < ~var9 && ~var8 < -11 && ~var8 > -119) {
                  Class127.anIntArray1681[var8 + var7] = 255;
               } else {
                  Class127.anIntArray1681[var8 + var7] = 0;
               }
            }
         }

         for(var6 = 0; var2 + -var0 > var6; ++var6) {
            Class3_Sub28_Sub5.anIntArray3592[var6] = Class3_Sub28_Sub5.anIntArray3592[var6 - -var0];
         }

         for(var6 = var2 - var0; var2 > var6; ++var6) {
            Class3_Sub28_Sub5.anIntArray3592[var6] = (int)(Math.sin((double)Class1.anInt57 / 14.0D) * 16.0D + 14.0D * Math.sin((double)Class1.anInt57 / 15.0D) + 12.0D * Math.sin((double)Class1.anInt57 / 16.0D));
            ++Class1.anInt57;
         }

         Class132.anInt1740 += var0;
         var6 = (var0 - -(1 & Class44.anInt719)) / 2;
         if(~var6 < -1) {
            for(var7 = 0; Class132.anInt1740 > var7; ++var7) {
               var8 = 2 + (int)(124.0D * Math.random());
               var9 = (int)(128.0D * Math.random()) + 128;
               Class127.anIntArray1681[var8 - -(var9 << 7)] = 192;
            }

            Class132.anInt1740 = 0;

            int var10;
            for(var7 = 0; ~var2 < ~var7; ++var7) {
               var9 = var7 * 128;
               var8 = 0;

               for(var10 = -var6; ~var10 > -129; ++var10) {
                  if(128 > var6 + var10) {
                     var8 += Class127.anIntArray1681[var9 + (var10 - -var6)];
                  }

                  if(-1 >= ~(-1 + -var6 + var10)) {
                     var8 -= Class127.anIntArray1681[-var6 + -1 + var10 + var9];
                  }

                  if(0 <= var10) {
                     Class3_Sub30_Sub1.anIntArray3805[var10 + var9] = var8 / (1 + var6 * 2);
                  }
               }
            }

            for(var7 = 0; 128 > var7; ++var7) {
               var8 = 0;

               for(var9 = -var6; var2 > var9; ++var9) {
                  var10 = var9 * 128;
                  if(~(var9 + var6) > ~var2) {
                     var8 += Class3_Sub30_Sub1.anIntArray3805[var6 * 128 + (var7 - -var10)];
                  }

                  if(0 <= var9 - var6 - 1) {
                     var8 -= Class3_Sub30_Sub1.anIntArray3805[-((1 + var6) * 128) + (var7 - -var10)];
                  }

                  if(var9 >= 0) {
                     Class127.anIntArray1681[var10 + var7] = var8 / (var6 * 2 - -1);
                  }
               }
            }
         }

      } catch (RuntimeException var11) {
         throw Class44.method1067(var11, "vl.E(" + var0 + ',' + var1 + ')');
      }
   }

   static final void method2196(int var0) {
      try {
         Class3_Sub13_Sub34.aClass93_3412.method1523((byte)-107);
         if(var0 != 128) {
            anInt2024 = 111;
         }

         Class3_Sub13_Sub31.aClass93_3369.method1523((byte)-110);
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "vl.D(" + var0 + ')');
      }
   }

   public static void method2197(boolean var0) {
      try {
         if(var0) {
            anIntArray2021 = null;
            aClass94_2018 = null;
            anIntArray2017 = null;
            aClass153_2019 = null;
            aClass93_2016 = null;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "vl.C(" + var0 + ')');
      }
   }

}
