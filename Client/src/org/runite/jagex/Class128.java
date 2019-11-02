package org.runite.jagex;
import java.util.Arrays;


final class Class128 {

   static int anInt1682 = 1;
   static Class93 aClass93_1683 = new Class93(64);
   private Node[] aClass3_Sub28Array1684;
   static boolean aBoolean1685 = true;
   private static RSString aClass94_1686 = RSString.createRSString("shake:");
   static RSString aClass94_1687 = RSString.createRSString("(Z");
   static RSString aClass94_1688 = aClass94_1686;
   static RSString aClass94_1689 = aClass94_1686;


   static final void method1760(int var0, byte var1, int var2) {
      try {
    	  if (WorldListCountry.localPlane > 3 || WorldListCountry.localPlane < 0) {
    		  System.err.println("Local plane " + WorldListCountry.localPlane + " is out of bounds - rendering log=" + Arrays.toString(Class163_Sub3.PLAYER_RENDER_LOG) + "!");
    		  WorldListCountry.localPlane %= 4;
    	  }
         Class61 var3 = Class3_Sub13_Sub22.aClass61ArrayArrayArray3273[WorldListCountry.localPlane][var2][var0];
         if(var3 != null) {
            if(var1 == 65) {
               int var4 = -99999999;
               Class3_Sub28_Sub14 var5 = null;

               Class3_Sub28_Sub14 var6;
               for(var6 = (Class3_Sub28_Sub14)var3.method1222(); null != var6; var6 = (Class3_Sub28_Sub14)var3.method1221()) {
                  ItemDefinition var7 = Class38.getItemDefinition(var6.aClass140_Sub7_3676.anInt2936, (byte)104);
                  int var8 = var7.value;
                  if(var7.stackingType == 1) {
                     var8 *= 1 + var6.aClass140_Sub7_3676.anInt2930;
                  }

                  if(var4 < var8) {
                     var4 = var8;
                     var5 = var6;
                  }
               }

               if(null != var5) {
                  var3.method1216(64, var5);
                  Class140_Sub7 var12 = null;
                  Class140_Sub7 var14 = null;

                  for(var6 = (Class3_Sub28_Sub14)var3.method1222(); var6 != null; var6 = (Class3_Sub28_Sub14)var3.method1221()) {
                     Class140_Sub7 var9 = var6.aClass140_Sub7_3676;
                     if(~var9.anInt2936 != ~var5.aClass140_Sub7_3676.anInt2936) {
                        if(null == var12) {
                           var12 = var9;
                        }

                        if(~var9.anInt2936 != ~var12.anInt2936 && null == var14) {
                           var14 = var9;
                        }
                     }
                  }

                  long var13 = (long)(1610612736 + (var0 << 7) + var2);
                  Class3_Sub13_Sub10.method213(WorldListCountry.localPlane, var2, var0, Class121.method1736(WorldListCountry.localPlane, 1, 64 + 128 * var2, 64 + var0 * 128), var5.aClass140_Sub7_3676, var13, var12, var14);
               } else {
                  ObjectDefinition.method1688(WorldListCountry.localPlane, var2, var0);
               }
            }
         } else {
            ObjectDefinition.method1688(WorldListCountry.localPlane, var2, var0);
         }
      } catch (RuntimeException var11) {
         throw Class44.method1067(var11, "rm.E(" + var0 + ',' + var1 + ',' + var2 + ')');
      }
   }

   public static void method1761(byte var0) {
      try {
         aClass94_1688 = null;
         aClass94_1686 = null;
         aClass94_1689 = null;
         aClass94_1687 = null;
         if(var0 < -46) {
            aClass93_1683 = null;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "rm.C(" + var0 + ')');
      }
   }

   static final void method1762(long var0, byte var2) {
      try {
         try {
            int var3 = 30 / ((var2 - -86) / 36);
            Thread.sleep(var0);
         } catch (InterruptedException var4) {
            ;
         }

      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "rm.B(" + var0 + ',' + var2 + ')');
      }
   }

   static final Model method1763(boolean var0, int var1, int var2, int var3, int var4, Model var5, int var6) {
      try {
         long var7 = (long)var3;
         Model var9 = (Model)Class61.aClass93_939.get(var7, (byte)121);
         if(var9 == null) {
            Model_Sub1 var10 = Model_Sub1.method2015(Class159.aClass153_2019, var3, 0);
            if(var10 == null) {
               return null;
            }

            var9 = var10.method2008(64, 768, -50, -10, -50);
            Class61.aClass93_939.put((byte)-95, var9, var7);
         }

         int var17 = var5.method1884();
         int var11 = var5.method1883();
         int var12 = var5.method1898();
         int var13 = var5.method1872();
         var9 = var9.method1882(var0, true, true);
         if(var1 != 0) {
            var9.method1876(var1);
         }

         int var15;
         if(HDToolKit.highDetail) {
            Class140_Sub1_Sub1 var14 = (Class140_Sub1_Sub1)var9;
            if(var6 != Class121.method1736(WorldListCountry.localPlane, 1, var4 + var17, var2 + var12) || var6 != Class121.method1736(WorldListCountry.localPlane, 1, var4 - -var11, var13 + var2)) {
               for(var15 = 0; ~var15 > ~var14.anInt3823; ++var15) {
                  var14.anIntArray3845[var15] += Class121.method1736(WorldListCountry.localPlane, 1, var14.anIntArray3822[var15] + var4, var14.anIntArray3848[var15] + var2) - var6;
               }

               var14.aClass121_3839.aBoolean1640 = false;
               var14.aClass6_3835.aBoolean98 = false;
            }
         } else {
            Class140_Sub1_Sub2 var18 = (Class140_Sub1_Sub2)var9;
            if(var6 != Class121.method1736(WorldListCountry.localPlane, 1, var17 + var4, var12 + var2) || var6 != Class121.method1736(WorldListCountry.localPlane, 1, var4 - -var11, var13 + var2)) {
               for(var15 = 0; var18.anInt3891 > var15; ++var15) {
                  var18.anIntArray3883[var15] += Class121.method1736(WorldListCountry.localPlane, 1, var4 + var18.anIntArray3885[var15], var18.anIntArray3895[var15] + var2) - var6;
               }

               var18.aBoolean3897 = false;
            }
         }

         return var9;
      } catch (RuntimeException var16) {
         throw Class44.method1067(var16, "rm.D(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + (var5 != null?"{...}":"null") + ',' + var6 + ')');
      }
   }

   static final void method1764(int var0, int var1, int var2) {
      for(int var3 = 0; var3 < Class3_Sub17.anInt2456; ++var3) {
         for(int var4 = 0; var4 < IOHandler.anInt1234; ++var4) {
            for(int var5 = 0; var5 < Class3_Sub13_Sub15.anInt3179; ++var5) {
               Class3_Sub2 var6 = Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var3][var4][var5];
               if(var6 != null) {
                  Class70 var7 = var6.aClass70_2234;
                  if(var7 != null && var7.aClass140_1049.method1865()) {
                     Class3_Sub13_Sub10.method214(var7.aClass140_1049, var3, var4, var5, 1, 1);
                     if(var7.aClass140_1052 != null && var7.aClass140_1052.method1865()) {
                        Class3_Sub13_Sub10.method214(var7.aClass140_1052, var3, var4, var5, 1, 1);
                        var7.aClass140_1049.method1866(var7.aClass140_1052, 0, 0, 0, false);
                        var7.aClass140_1052 = var7.aClass140_1052.method1861(var0, var1, var2);
                     }

                     var7.aClass140_1049 = var7.aClass140_1049.method1861(var0, var1, var2);
                  }

                  for(int var8 = 0; var8 < var6.anInt2223; ++var8) {
                     Class25 var9 = var6.aClass25Array2221[var8];
                     if(var9 != null && var9.aClass140_479.method1865()) {
                        Class3_Sub13_Sub10.method214(var9.aClass140_479, var3, var4, var5, var9.anInt495 - var9.anInt483 + 1, var9.anInt481 - var9.anInt478 + 1);
                        var9.aClass140_479 = var9.aClass140_479.method1861(var0, var1, var2);
                     }
                  }

                  Class12 var10 = var6.aClass12_2230;
                  if(var10 != null && var10.object.method1865()) {
                     Class155.method2162(var10.object, var3, var4, var5);
                     var10.object = var10.object.method1861(var0, var1, var2);
                  }
               }
            }
         }
      }

   }

   Class128(int var1) {
      try {
         this.aClass3_Sub28Array1684 = new Node[var1];

         for(int var2 = 0; ~var2 > ~var1; ++var2) {
            Node var3 = this.aClass3_Sub28Array1684[var2] = new Node();
            var3.aClass3_Sub28_2570 = var3;
            var3.aClass3_Sub28_2578 = var3;
         }

      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "rm.<init>(" + var1 + ')');
      }
   }

}
