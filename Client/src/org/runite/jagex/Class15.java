package org.runite.jagex;
import java.util.Date;

import org.runite.Configurations;

final class Class15 implements Runnable {

   static int anInt342;
   static RSString aClass94_343 = RSString.createRSString("blanc:");
   static short[][] aShortArrayArray344;
   volatile boolean aBoolean345 = false;
   static boolean aBoolean346;
   static int[] localNPCIndexes = new int['\u8000'];
   static int anInt348;
   static int anInt349;
   Signlink aClass87_350;
   static Class64 aClass64_351;
   volatile Class155[] aClass155Array352 = new Class155[2];
   volatile boolean aBoolean353 = false;


   static final boolean method888(int var0, ObjectDefinition var1, boolean var2, int var3, int var4, int var5, int var6) {
      try {
         Class2 var7 = Class3_Sub28_Sub6.c(var1.anInt1516, 0);
         if(var7.anInt64 == -1) {
            return true;
         } else {
            if(var1.aBoolean1537) {
               var6 += var1.anInt1478;
               var6 &= 3;
            } else {
               var6 = 0;
            }

            if(var2) {
               aClass64_351 = (Class64)null;
            }

            LDIndexedSprite var8 = var7.method77(var6, (byte)-111);
            if(var8 == null) {
               return false;
            } else {
               int var9 = var1.anInt1480;
               int var10 = var1.anInt1485;
               if(1 == (1 & var6)) {
                  var9 = var1.anInt1485;
                  var10 = var1.anInt1480;
               }

               int var11 = var8.anInt1469;
               int var12 = var8.anInt1467;
               if(var7.aBoolean69) {
                  var12 = 4 * var10;
                  var11 = 4 * var9;
               }

               if(~var7.anInt61 == -1) {
                  var8.method1677(var0 * 4 + 48, 48 + 4 * (-var10 + -var5 + 104), var11, var12);
               } else {
                  var8.method1669(48 + 4 * var0, 4 * (-var10 + -var5 + 104) + 48, var11, var12, var7.anInt61);
               }

               return true;
            }
         }
      } catch (RuntimeException var13) {
         throw Class44.method1067(var13, "cj.D(" + var0 + ',' + (var1 != null?"{...}":"null") + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ')');
      }
   }

   static final void method889(byte var0, RSByteBuffer var1) {
      try {
         if(var0 != 105) {
            localNPCIndexes = (int[])null;
         }

         int var3 = Class158.anInt2010 >> 1;
         int var2 = Class3_Sub13_Sub23_Sub1.anInt4034 >> 2 << 10;
         byte[][] var4 = new byte[Class23.anInt455][Class108.anInt1460];

         int var6;
         int var12;
         int var14;
         while(var1.index < var1.buffer.length) {
            int var7 = 0;
            var6 = 0;
            boolean var5 = false;
            if(~var1.getByte((byte)-99) == -2) {
               var6 = var1.getByte((byte)-56);
               var7 = var1.getByte((byte)-51);
               var5 = true;
            }

            int var8 = var1.getByte((byte)-125);
            int var9 = var1.getByte((byte)-30);
            int var10 = -Class3_Sub13_Sub21.anInt3256 + var8 * 64;
            int var11 = -1 + Class108.anInt1460 - var9 * 64 + Class2.anInt65;
            if(~var10 <= -1 && 0 <= -63 + var11 && Class23.anInt455 > var10 - -63 && ~Class108.anInt1460 < ~var11) {
               for(var12 = 0; var12 < 64; ++var12) {
                  byte[] var13 = var4[var10 - -var12];

                  for(var14 = 0; 64 > var14; ++var14) {
                     if(!var5 || var12 >= 8 * var6 && 8 + 8 * var6 > var12 && var14 >= var7 * 8 && var14 < 8 + 8 * var7) {
                        var13[var11 - var14] = var1.getByte();
                     }
                  }
               }
            } else if(!var5) {
               var1.index += 4096;
            } else {
               var1.index += 64;
            }
         }

         int var27 = Class23.anInt455;
         var6 = Class108.anInt1460;
         int[] var29 = new int[var6];
         int[] var28 = new int[var6];
         int[] var30 = new int[var6];
         int[] var32 = new int[var6];
         int[] var31 = new int[var6];

         for(var12 = -5; ~var12 > ~var27; ++var12) {
            int var15;
            int var35;
            for(int var34 = 0; ~var34 > ~var6; ++var34) {
               var14 = var12 + 5;
               if(var27 > var14) {
                  var15 = 255 & var4[var14][var34];
                  if(~var15 < -1) {
                     Class100 var16 = Class3_Sub28_Sub15.method629(true, var15 - 1);
                     var28[var34] += var16.anInt1408;
                     var29[var34] += var16.anInt1406;
                     var30[var34] += var16.anInt1417;
                     var32[var34] += var16.anInt1418;
                     ++var31[var34];
                  }
               }

               var15 = var12 + -5;
               if(-1 >= ~var15) {
                  var35 = var4[var15][var34] & 255;
                  if(0 < var35) {
                     Class100 var17 = Class3_Sub28_Sub15.method629(true, -1 + var35);
                     var28[var34] -= var17.anInt1408;
                     var29[var34] -= var17.anInt1406;
                     var30[var34] -= var17.anInt1417;
                     var32[var34] -= var17.anInt1418;
                     --var31[var34];
                  }
               }
            }

            if(~var12 <= -1) {
               int[][] var33 = Class146.anIntArrayArrayArray1903[var12 >> 6];
               var14 = 0;
               var15 = 0;
               int var36 = 0;
               int var18 = 0;
               var35 = 0;

               for(int var19 = -5; ~var19 > ~var6; ++var19) {
                  int var20 = var19 - -5;
                  if(var6 > var20) {
                     var18 += var31[var20];
                     var15 += var29[var20];
                     var35 += var30[var20];
                     var14 += var28[var20];
                     var36 += var32[var20];
                  }

                  int var21 = -5 + var19;
                  if(~var21 <= -1) {
                     var35 -= var30[var21];
                     var36 -= var32[var21];
                     var14 -= var28[var21];
                     var18 -= var31[var21];
                     var15 -= var29[var21];
                  }

                  if(var19 >= 0 && 0 < var18) {
                     int[] var22 = var33[var19 >> 6];
                     int var23 = var36 != 0?Class3_Sub8.method129(var35 / var18, 2, var15 / var18, var14 * 256 / var36):0;
                     if(var4[var12][var19] == 0) {
                        if(var22 != null) {
                           var22[Class3_Sub28_Sub15.method633(4032, var19 << 6) - -Class3_Sub28_Sub15.method633(var12, 63)] = 0;
                        }
                     } else {
                        if(var22 == null) {
                           var22 = var33[var19 >> 6] = new int[4096];
                        }

                        int var24 = var3 + (var23 & 127);
                        if(var24 < 0) {
                           var24 = 0;
                        } else if(var24 > 127) {
                           var24 = 127;
                        }

                        int var25 = var24 + (896 & var23) + (var23 + var2 & '\ufc00');
                        var22[Class3_Sub28_Sub15.method633(4032, var19 << 6) + Class3_Sub28_Sub15.method633(63, var12)] = Class51.anIntArray834[Class47.method1100(96, true, var25)];
                     }
                  }
               }
            }
         }

      } catch (RuntimeException var26) {
         throw Class44.method1067(var26, "cj.H(" + var0 + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

   static final Object method890(boolean var0, int var1, byte[] var2) {
      try {
         if(var2 == null) {
            return null;
         } else {
            if(var1 >= -67) {
               method891(19);
            }

            if(~var2.length < -137 && !Class45.aBoolean732) {
               try {
                  Class144 var3 = (Class144)Class.forName(Configurations.PACKAGE_JAGEX + ".Class144_Sub1").newInstance();
                  var3.method2066(400, var2);
                  return var3;
               } catch (Throwable var4) {
                  Class45.aBoolean732 = true;
               }
            }

            return var0?Class12.method873((byte)62, var2):var2;
         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "cj.E(" + var0 + ',' + var1 + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

   public final void run() {
      try {
         this.aBoolean353 = true;

         try {
            while(!this.aBoolean345) {
               for(int var1 = 0; ~var1 > -3; ++var1) {
                  Class155 var2 = this.aClass155Array352[var1];
                  if(var2 != null) {
                     var2.method2153((byte)-34);
                  }
               }

               Class3_Sub13_Sub34.method331(10L, 64);
               Class81.method1400(this.aClass87_350, (Object)null, -71);
            }
         } catch (Exception var7) {
            Class49.method1125((String)null, var7, (byte)111);
         } finally {
            this.aBoolean353 = false;
         }

      } catch (RuntimeException var9) {
         throw Class44.method1067(var9, "cj.run()");
      }
   }

   static final Class3_Sub28_Sub16[] method891(int var0) {
      try {
         Class3_Sub28_Sub16[] var1 = new Class3_Sub28_Sub16[Class95.anInt1338];
         if(var0 != -5) {
            method894(113L, (byte)48);
         }

         for(int var2 = 0; ~Class95.anInt1338 < ~var2; ++var2) {
            int var3 = Class140_Sub7.anIntArray2931[var2] * Class3_Sub13_Sub6.anIntArray3076[var2];
            byte[] var4 = Class163_Sub1.aByteArrayArray2987[var2];
            int[] var5 = new int[var3];

            for(int var6 = 0; var6 < var3; ++var6) {
               var5[var6] = Class3_Sub13_Sub38.spritePalette[Class3_Sub28_Sub15.method633(255, var4[var6])];
            }

            if(!HDToolKit.highDetail) {
               var1[var2] = new Class3_Sub28_Sub16_Sub2(Class3_Sub15.anInt2426, Class133.anInt1748, Class164.anIntArray2048[var2], RSByteBuffer.anIntArray2591[var2], Class140_Sub7.anIntArray2931[var2], Class3_Sub13_Sub6.anIntArray3076[var2], var5);
            } else {
               var1[var2] = new Class3_Sub28_Sub16_Sub1(Class3_Sub15.anInt2426, Class133.anInt1748, Class164.anIntArray2048[var2], RSByteBuffer.anIntArray2591[var2], Class140_Sub7.anIntArray2931[var2], Class3_Sub13_Sub6.anIntArray3076[var2], var5);
            }
         }

         Class39.method1035((byte)116);
         return var1;
      } catch (RuntimeException var7) {
         throw Class44.method1067(var7, "cj.C(" + var0 + ')');
      }
   }

   public static void method892(int var0) {
      try {
         aShortArrayArray344 = (short[][])null;
         aClass94_343 = null;
         aClass64_351 = null;
         if(var0 <= 75) {
            aBoolean346 = false;
         }

         localNPCIndexes = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "cj.G(" + var0 + ')');
      }
   }

   static final int method893(int var0, byte var1) {
      try {
         int var2 = -66 / ((67 - var1) / 41);
         return var0 & 255;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "cj.A(" + var0 + ',' + var1 + ')');
      }
   }

   static final RSString method894(long var0, byte var2) {
      try {
         Class3_Sub28_Sub5.aCalendar3581.setTime(new Date(var0));
         int var3 = Class3_Sub28_Sub5.aCalendar3581.get(7);
         int var4 = Class3_Sub28_Sub5.aCalendar3581.get(5);
         int var5 = Class3_Sub28_Sub5.aCalendar3581.get(2);
         if(var2 < 9) {
            method889((byte)7, (RSByteBuffer)null);
         }

         int var6 = Class3_Sub28_Sub5.aCalendar3581.get(1);
         int var7 = Class3_Sub28_Sub5.aCalendar3581.get(11);
         int var8 = Class3_Sub28_Sub5.aCalendar3581.get(12);
         int var9 = Class3_Sub28_Sub5.aCalendar3581.get(13);
         return RenderAnimationDefinition.method903(new RSString[]{Class3_Sub13_Sub31.aClass94Array3376[var3 + -1], Class3_Sub13_Sub12.aClass94_3145, Class72.method1298((byte)9, var4 / 10), Class72.method1298((byte)9, var4 % 10), Class161.aClass94_2025, NPC.aClass94Array3985[var5], Class161.aClass94_2025, Class72.method1298((byte)9, var6), Class24.aClass94_465, Class72.method1298((byte)9, var7 / 10), Class72.method1298((byte)9, var7 % 10), Class155.aClass94_1970, Class72.method1298((byte)9, var8 / 10), Class72.method1298((byte)9, var8 % 10), Class155.aClass94_1970, Class72.method1298((byte)9, var9 / 10), Class72.method1298((byte)9, var9 % 10), WorldListCountry.aClass94_500}, (byte)-96);
      } catch (RuntimeException var10) {
         throw Class44.method1067(var10, "cj.F(" + var0 + ',' + var2 + ')');
      }
   }

   static final int[][] method895(boolean var0, int var1, int var2, int var3, int var4, int var5, int var6, float var7, byte var8) {
      try {
         if(var8 >= -52) {
            method891(115);
         }

         int[][] var9 = new int[var3][var2];
         Class3_Sub13_Sub4 var10 = new Class3_Sub13_Sub4();
         var10.anInt3062 = (int)(var7 * 4096.0F);
         var10.anInt3058 = var1;
         var10.anInt3056 = var5;
         var10.aBoolean3065 = var0;
         var10.anInt3060 = var6;
         var10.method158(16251);
         Class3_Sub13_Sub3.method180(122, var3, var2);

         for(int var11 = 0; ~var3 < ~var11; ++var11) {
            var10.method186(true, var11, var9[var11]);
         }

         return var9;
      } catch (RuntimeException var12) {
         throw Class44.method1067(var12, "cj.B(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ')');
      }
   }

}
