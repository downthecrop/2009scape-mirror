package org.runite.jagex;

final class Class3_Sub19 extends Class3 {

   byte aByte2472;
   RSString aClass94_2473;
   static CacheIndex aClass153_2474;
   static int anInt2475 = 0;
   RSString aClass94_2476;
   static RSString aClass94_2477 = RSString.createRSString("<col=ffffff>");
   int anInt2478;
   static int anInt2479;


   static final Class75_Sub2 method384(RSByteBuffer var0, byte var1) {
      try {
         if(var1 <= 40) {
            aClass153_2474 = (CacheIndex)null;
         }

         return new Class75_Sub2(var0.getShort((byte)38), var0.getShort((byte)51), var0.getShort((byte)66), var0.getShort((byte)68), var0.getTriByte((byte)86), var0.getTriByte((byte)86), var0.getByte((byte)-85));
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "kl.C(" + (var0 != null?"{...}":"null") + ',' + var1 + ')');
      }
   }

   static final boolean method385(int var0, int var1) {
      try {
         int var2 = var0;
         byte var3 = 0;
         int var5;
         int var6;
         int var7;
         int var8;
         if(null == Class27.aClass3_Sub28_Sub16_Sub2_518) {
            if(!HDToolKit.highDetail && null != Class49.aClass3_Sub28_Sub16_812) {
               Class27.aClass3_Sub28_Sub16_Sub2_518 = (Class3_Sub28_Sub16_Sub2)Class49.aClass3_Sub28_Sub16_812;
            } else {
               Class27.aClass3_Sub28_Sub16_Sub2_518 = new Class3_Sub28_Sub16_Sub2(512, 512);
            }

            int[] var4 = Class27.aClass3_Sub28_Sub16_Sub2_518.anIntArray4081;
            var5 = var4.length;

            for(var6 = 0; ~var6 > ~var5; ++var6) {
               var4[var6] = 1;
            }

            for(var6 = var3 + 1; -1 + -var3 + 104 > var6; ++var6) {
               var7 = 4 * 512 * (var3 + 103 + -var6) + 24628;

               for(var8 = 1 + var2; ~var8 > ~(-var2 + 104 + -1); ++var8) {
                  if(-1 == ~(Class9.aByteArrayArrayArray113[var1][var8][var6] & 24)) {
                     IOHandler.method1469(var4, var7, 512, var1, var8, var6);
                  }

                  if(~var1 > -4 && 0 != (Class9.aByteArrayArrayArray113[1 + var1][var8][var6] & 8)) {
                     IOHandler.method1469(var4, var7, 512, var1 + 1, var8, var6);
                  }

                  var7 += 4;
               }
            }

            MouseListeningClass.anInt1924 = 0;

            for(var6 = 0; 104 > var6; ++var6) {
               for(var7 = 0; 104 > var7; ++var7) {
                  long var20 = Class3_Sub2.method104(WorldListCountry.localPlane, var6 + var2, var7 - -var3);
                  if(~var20 != -1L) {
                     ObjectDefinition var10 = Class162.getObjectDefinition(4, (int)(var20 >>> 32) & Integer.MAX_VALUE);
                     int var11 = var10.anInt1482;
                     int var12;
                     if(null != var10.anIntArray1524) {
                        for(var12 = 0; ~var10.anIntArray1524.length < ~var12; ++var12) {
                           if(-1 != var10.anIntArray1524[var12]) {
                              ObjectDefinition var13 = Class162.getObjectDefinition(4, var10.anIntArray1524[var12]);
                              if(0 <= var13.anInt1482) {
                                 var11 = var13.anInt1482;
                                 break;
                              }
                           }
                        }
                     }

                     if(~var11 <= -1) {
                        int var21 = var3 + var7;
                        var12 = var2 + var6;
                        if(22 != var11 && var11 != 29 && 34 != var11 && -37 != ~var11 && 46 != var11 && var11 != 47 && var11 != 48) {
                           int[][] var14 = Class86.aClass91Array1182[WorldListCountry.localPlane].anIntArrayArray1304;

                           for(int var15 = 0; -11 < ~var15; ++var15) {
                              int var16 = (int)(Math.random() * 4.0D);
                              if(var16 == 0 && var12 > 0 && ~(var6 - 3) > ~var12 && ~(var14[var12 - 1][var21] & 19661064) == -1) {
                                 --var12;
                              }

                              if(var16 == 1 && -104 < ~var12 && ~(3 + var6) < ~var12 && (var14[var12 - -1][var21] & 19661184) == 0) {
                                 ++var12;
                              }

                              if(2 == var16 && var21 > 0 && ~(-3 + var7) > ~var21 && 0 == (var14[var12][-1 + var21] & 19661058)) {
                                 --var21;
                              }

                              if(3 == var16 && var21 < 103 && ~var21 > ~(var7 - -3) && 0 == (19661088 & var14[var12][1 + var21])) {
                                 ++var21;
                              }
                           }
                        }

                        Class3_Sub28_Sub15.anIntArray3693[MouseListeningClass.anInt1924] = var10.objectId;
                        Class84.anIntArray1163[MouseListeningClass.anInt1924] = -var2 + var12;
                        Class3_Sub28_Sub7_Sub1.anIntArray4050[MouseListeningClass.anInt1924] = var21 + -var3;
                        ++MouseListeningClass.anInt1924;
                     }
                  }
               }
            }
         }

         Class27.aClass3_Sub28_Sub16_Sub2_518.method658();
         int var18 = (238 + (int)(20.0D * Math.random()) - 10 << 8) + (-10 + (int)(Math.random() * 20.0D) + 238 << 16) + (228 - -((int)(20.0D * Math.random())));
         var5 = (int)(Math.random() * 20.0D) + 238 + -10 << 16;

         for(var6 = 1; ~var6 > -104; ++var6) {
            for(var7 = 1; 103 > var7; ++var7) {
               if(0 == (Class9.aByteArrayArrayArray113[var1][var7 + var2][var6 - -var3] & 24) && !RSString.method1529(var3, var7, var18, var6, var5, var2, var1, true)) {
                  if(HDToolKit.highDetail) {
                     Class74.anIntArray1100 = null;
                  } else {
                     Class164_Sub1.aClass158_3009.method2182(0);
                  }

                  return false;
               }

               if(-4 < ~var1 && ~(Class9.aByteArrayArrayArray113[1 + var1][var7 + var2][var6 + var3] & 8) != -1 && !RSString.method1529(var3, var7, var18, var6, var5, var2, 1 + var1, true)) {
                  if(!HDToolKit.highDetail) {
                     Class164_Sub1.aClass158_3009.method2182(0);
                  } else {
                     Class74.anIntArray1100 = null;
                  }

                  return false;
               }
            }
         }

         if(HDToolKit.highDetail) {
            int[] var19 = Class27.aClass3_Sub28_Sub16_Sub2_518.anIntArray4081;
            var7 = var19.length;

            for(var8 = 0; var7 > var8; ++var8) {
               if(var19[var8] == 0) {
                  var19[var8] = 1;
               }
            }

            Class49.aClass3_Sub28_Sub16_812 = new Class3_Sub28_Sub16_Sub1(Class27.aClass3_Sub28_Sub16_Sub2_518);
         } else {
            Class49.aClass3_Sub28_Sub16_812 = Class27.aClass3_Sub28_Sub16_Sub2_518;
         }

         if(!HDToolKit.highDetail) {
            Class164_Sub1.aClass158_3009.method2182(0);
         } else {
            Class74.anIntArray1100 = null;
         }

         Class27.aClass3_Sub28_Sub16_Sub2_518 = null;
         return true;
      } catch (RuntimeException var17) {
         throw Class44.method1067(var17, "kl.B(" + var0 + ',' + var1 + ')');
      }
   }

   static final void method386(int var0, int var1) {
      try {
         Class3_Sub31.aClass93_2604.method1522(-128, var0);
         if(var1 <= -109) {
            Class27.aClass93_511.method1522(-126, var0);
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "kl.D(" + var0 + ',' + var1 + ')');
      }
   }

   public static void method387(int var0) {
      try {
         //int var1 = 41 % ((var0 - -70) / 32);
         aClass94_2477 = null;
         aClass153_2474 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "kl.A(" + var0 + ')');
      }
   }

}
