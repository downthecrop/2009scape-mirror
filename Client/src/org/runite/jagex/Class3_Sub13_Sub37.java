package org.runite.jagex;
import java.math.BigInteger;

final class Class3_Sub13_Sub37 extends Class3_Sub13 {

   private int[][] anIntArrayArray3438;
   static int anInt3439;
   static Class3_Sub28_Sub17_Sub1 aClass3_Sub28_Sub17_Sub1_3440;
   static BigInteger aBigInteger3441 = new BigInteger("7162900525229798032761816791230527296329313291232324290237849263501208207972894053929065636522363163621000728841182238772712427862772219676577293600221789");
   static int anInt3442;
   private int[] anIntArray3443 = new int[257];


   final void method158(int var1) {
      try {
         if(var1 != 16251) {
            this.anIntArray3443 = (int[])null;
         }

         if(this.anIntArrayArray3438 == null) {
            this.method345(1, false);
         }

         this.method346(114);
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "um.P(" + var1 + ')');
      }
   }

   private final void method345(int var1, boolean var2) {
      try {
         if(~var1 != -1) {
            if(~var1 == -2) {
               this.anIntArrayArray3438 = new int[2][4];
               this.anIntArrayArray3438[0][1] = 0;
               this.anIntArrayArray3438[0][0] = 0;
               this.anIntArrayArray3438[1][0] = 4096;
               this.anIntArrayArray3438[0][3] = 0;
               this.anIntArrayArray3438[1][1] = 4096;
               this.anIntArrayArray3438[0][2] = 0;
               this.anIntArrayArray3438[1][2] = 4096;
               this.anIntArrayArray3438[1][3] = 4096;
            } else if(-3 == ~var1) {
               this.anIntArrayArray3438 = new int[8][4];
               this.anIntArrayArray3438[0][0] = 0;
               this.anIntArrayArray3438[1][0] = 2867;
               this.anIntArrayArray3438[2][0] = 3072;
               this.anIntArrayArray3438[0][2] = 2602;
               this.anIntArrayArray3438[3][0] = 3276;
               this.anIntArrayArray3438[0][3] = 2361;
               this.anIntArrayArray3438[1][3] = 1558;
               this.anIntArrayArray3438[4][0] = 3481;
               this.anIntArrayArray3438[5][0] = 3686;
               this.anIntArrayArray3438[2][3] = 1413;
               this.anIntArrayArray3438[3][3] = 947;
               this.anIntArrayArray3438[4][3] = 722;
               this.anIntArrayArray3438[6][0] = 3891;
               this.anIntArrayArray3438[1][2] = 1799;
               this.anIntArrayArray3438[7][0] = 4096;
               this.anIntArrayArray3438[5][3] = 1766;
               this.anIntArrayArray3438[2][2] = 1734;
               this.anIntArrayArray3438[3][2] = 1220;
               this.anIntArrayArray3438[4][2] = 963;
               this.anIntArrayArray3438[5][2] = 2152;
               this.anIntArrayArray3438[6][3] = 915;
               this.anIntArrayArray3438[7][3] = 1140;
               this.anIntArrayArray3438[0][1] = 2650;
               this.anIntArrayArray3438[6][2] = 1060;
               this.anIntArrayArray3438[1][1] = 2313;
               this.anIntArrayArray3438[2][1] = 2618;
               this.anIntArrayArray3438[3][1] = 2296;
               this.anIntArrayArray3438[4][1] = 2072;
               this.anIntArrayArray3438[7][2] = 1413;
               this.anIntArrayArray3438[5][1] = 2730;
               this.anIntArrayArray3438[6][1] = 2232;
               this.anIntArrayArray3438[7][1] = 1686;
            } else if(3 == var1) {
               this.anIntArrayArray3438 = new int[7][4];
               this.anIntArrayArray3438[0][0] = 0;
               this.anIntArrayArray3438[0][3] = 4096;
               this.anIntArrayArray3438[1][3] = 4096;
               this.anIntArrayArray3438[2][3] = 0;
               this.anIntArrayArray3438[1][0] = 663;
               this.anIntArrayArray3438[0][1] = 0;
               this.anIntArrayArray3438[3][3] = 0;
               this.anIntArrayArray3438[1][1] = 0;
               this.anIntArrayArray3438[2][0] = 1363;
               this.anIntArrayArray3438[2][1] = 0;
               this.anIntArrayArray3438[4][3] = 0;
               this.anIntArrayArray3438[5][3] = 4096;
               this.anIntArrayArray3438[6][3] = 4096;
               this.anIntArrayArray3438[3][0] = 2048;
               this.anIntArrayArray3438[4][0] = 2727;
               this.anIntArrayArray3438[5][0] = 3411;
               this.anIntArrayArray3438[6][0] = 4096;
               this.anIntArrayArray3438[3][1] = 4096;
               this.anIntArrayArray3438[4][1] = 4096;
               this.anIntArrayArray3438[5][1] = 4096;
               this.anIntArrayArray3438[6][1] = 0;
               this.anIntArrayArray3438[0][2] = 0;
               this.anIntArrayArray3438[1][2] = 4096;
               this.anIntArrayArray3438[2][2] = 4096;
               this.anIntArrayArray3438[3][2] = 4096;
               this.anIntArrayArray3438[4][2] = 0;
               this.anIntArrayArray3438[5][2] = 0;
               this.anIntArrayArray3438[6][2] = 0;
            } else if(4 == var1) {
               this.anIntArrayArray3438 = new int[6][4];
               this.anIntArrayArray3438[0][3] = 0;
               this.anIntArrayArray3438[0][0] = 0;
               this.anIntArrayArray3438[0][2] = 0;
               this.anIntArrayArray3438[1][0] = 1843;
               this.anIntArrayArray3438[1][2] = 0;
               this.anIntArrayArray3438[2][2] = 0;
               this.anIntArrayArray3438[1][3] = 1493;
               this.anIntArrayArray3438[2][3] = 2939;
               this.anIntArrayArray3438[3][3] = 3565;
               this.anIntArrayArray3438[3][2] = 1124;
               this.anIntArrayArray3438[4][3] = 4031;
               this.anIntArrayArray3438[0][1] = 0;
               this.anIntArrayArray3438[1][1] = 0;
               this.anIntArrayArray3438[5][3] = 4096;
               this.anIntArrayArray3438[4][2] = 3084;
               this.anIntArrayArray3438[2][0] = 2457;
               this.anIntArrayArray3438[2][1] = 0;
               this.anIntArrayArray3438[3][0] = 2781;
               this.anIntArrayArray3438[4][0] = 3481;
               this.anIntArrayArray3438[3][1] = 0;
               this.anIntArrayArray3438[4][1] = 546;
               this.anIntArrayArray3438[5][2] = 4096;
               this.anIntArrayArray3438[5][0] = 4096;
               this.anIntArrayArray3438[5][1] = 4096;
            } else if(var1 == 5) {
               this.anIntArrayArray3438 = new int[16][4];
               this.anIntArrayArray3438[0][3] = 321;
               this.anIntArrayArray3438[0][0] = 0;
               this.anIntArrayArray3438[0][2] = 192;
               this.anIntArrayArray3438[1][0] = 155;
               this.anIntArrayArray3438[1][3] = 562;
               this.anIntArrayArray3438[1][2] = 449;
               this.anIntArrayArray3438[2][0] = 389;
               this.anIntArrayArray3438[3][0] = 671;
               this.anIntArrayArray3438[2][2] = 690;
               this.anIntArrayArray3438[0][1] = 80;
               this.anIntArrayArray3438[1][1] = 321;
               this.anIntArrayArray3438[4][0] = 897;
               this.anIntArrayArray3438[3][2] = 995;
               this.anIntArrayArray3438[4][2] = 1397;
               this.anIntArrayArray3438[2][1] = 578;
               this.anIntArrayArray3438[2][3] = 803;
               this.anIntArrayArray3438[5][0] = 1175;
               this.anIntArrayArray3438[6][0] = 1368;
               this.anIntArrayArray3438[5][2] = 1429;
               this.anIntArrayArray3438[3][1] = 947;
               this.anIntArrayArray3438[7][0] = 1507;
               this.anIntArrayArray3438[4][1] = 1285;
               this.anIntArrayArray3438[6][2] = 1461;
               this.anIntArrayArray3438[8][0] = 1736;
               this.anIntArrayArray3438[3][3] = 1140;
               this.anIntArrayArray3438[9][0] = 2088;
               this.anIntArrayArray3438[7][2] = 1525;
               this.anIntArrayArray3438[4][3] = 1509;
               this.anIntArrayArray3438[5][1] = 1525;
               this.anIntArrayArray3438[6][1] = 1734;
               this.anIntArrayArray3438[5][3] = 1413;
               this.anIntArrayArray3438[8][2] = 1590;
               this.anIntArrayArray3438[10][0] = 2355;
               this.anIntArrayArray3438[9][2] = 2056;
               this.anIntArrayArray3438[7][1] = 1413;
               this.anIntArrayArray3438[11][0] = 2691;
               this.anIntArrayArray3438[12][0] = 3031;
               this.anIntArrayArray3438[6][3] = 1333;
               this.anIntArrayArray3438[10][2] = 2586;
               this.anIntArrayArray3438[11][2] = 3148;
               this.anIntArrayArray3438[13][0] = 3522;
               this.anIntArrayArray3438[14][0] = 3727;
               this.anIntArrayArray3438[7][3] = 1702;
               this.anIntArrayArray3438[8][1] = 1108;
               this.anIntArrayArray3438[9][1] = 1766;
               this.anIntArrayArray3438[10][1] = 2409;
               this.anIntArrayArray3438[15][0] = 4096;
               this.anIntArrayArray3438[12][2] = 3710;
               this.anIntArrayArray3438[11][1] = 3116;
               this.anIntArrayArray3438[13][2] = 3421;
               this.anIntArrayArray3438[12][1] = 3806;
               this.anIntArrayArray3438[13][1] = 3437;
               this.anIntArrayArray3438[14][1] = 3116;
               this.anIntArrayArray3438[15][1] = 2377;
               this.anIntArrayArray3438[8][3] = 2056;
               this.anIntArrayArray3438[9][3] = 2666;
               this.anIntArrayArray3438[14][2] = 3148;
               this.anIntArrayArray3438[15][2] = 2505;
               this.anIntArrayArray3438[10][3] = 3276;
               this.anIntArrayArray3438[11][3] = 3228;
               this.anIntArrayArray3438[12][3] = 3196;
               this.anIntArrayArray3438[13][3] = 3019;
               this.anIntArrayArray3438[14][3] = 3228;
               this.anIntArrayArray3438[15][3] = 2746;
            } else {
               if(var1 != 6) {
                  throw new RuntimeException("Invalid gradient preset");
               }

               this.anIntArrayArray3438 = new int[4][4];
               this.anIntArrayArray3438[0][3] = 0;
               this.anIntArrayArray3438[0][2] = 4096;
               this.anIntArrayArray3438[1][3] = 0;
               this.anIntArrayArray3438[0][1] = 0;
               this.anIntArrayArray3438[2][3] = 0;
               this.anIntArrayArray3438[3][3] = 0;
               this.anIntArrayArray3438[0][0] = 2048;
               this.anIntArrayArray3438[1][1] = 4096;
               this.anIntArrayArray3438[1][0] = 2867;
               this.anIntArrayArray3438[2][1] = 4096;
               this.anIntArrayArray3438[1][2] = 4096;
               this.anIntArrayArray3438[2][2] = 4096;
               this.anIntArrayArray3438[3][1] = 4096;
               this.anIntArrayArray3438[2][0] = 3276;
               this.anIntArrayArray3438[3][2] = 0;
               this.anIntArrayArray3438[3][0] = 4096;
            }
         }

         if(var2) {
            aClass3_Sub28_Sub17_Sub1_3440 = (Class3_Sub28_Sub17_Sub1)null;
         }

      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "um.B(" + var1 + ',' + var2 + ')');
      }
   }

   public Class3_Sub13_Sub37() {
      super(1, false);
   }

   final void method157(int var1, RSByteBuffer var2, boolean var3) {
      try {
         if(var3) {
            if(var1 == 0) {
               int var4 = var2.getByte((byte)-68);
               if(-1 != ~var4) {
                  this.method345(var4, false);
               } else {
                  this.anIntArrayArray3438 = new int[var2.getByte((byte)-126)][4];

                  for(int var5 = 0; ~this.anIntArrayArray3438.length < ~var5; ++var5) {
                     this.anIntArrayArray3438[var5][0] = var2.getShort(1);
                     this.anIntArrayArray3438[var5][1] = var2.getByte((byte)-81) << 4;
                     this.anIntArrayArray3438[var5][2] = var2.getByte((byte)-63) << 4;
                     this.anIntArrayArray3438[var5][3] = var2.getByte((byte)-28) << 4;
                  }
               }
            }

         }
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "um.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   private final void method346(int var1) {
      try {
         if(var1 <= 95) {
            method350((byte)-28, -113);
         }

         int var5 = this.anIntArrayArray3438.length;
         if(~var5 < -1) {
            for(int var6 = 0; var6 < 257; ++var6) {
               int var7 = 0;
               int var8 = var6 << 4;

               for(int var9 = 0; var5 > var9 && var8 >= this.anIntArrayArray3438[var9][0]; ++var9) {
                  ++var7;
               }

               int var2;
               int var3;
               int var4;
               int[] var14;
               if(var5 > var7) {
                  var14 = this.anIntArrayArray3438[var7];
                  if(var7 > 0) {
                     int[] var10 = this.anIntArrayArray3438[-1 + var7];
                     int var11 = (var8 - var10[0] << 12) / (var14[0] + -var10[0]);
                     int var12 = 4096 + -var11;
                     var4 = var10[3] * var12 + var14[3] * var11 >> 12;
                     var2 = var12 * var10[1] + var11 * var14[1] >> 12;
                     var3 = var12 * var10[2] + var11 * var14[2] >> 12;
                  } else {
                     var2 = var14[1];
                     var4 = var14[3];
                     var3 = var14[2];
                  }
               } else {
                  var14 = this.anIntArrayArray3438[var5 + -1];
                  var4 = var14[3];
                  var3 = var14[2];
                  var2 = var14[1];
               }

               var2 >>= 4;
               var3 >>= 4;
               if(var2 < 0) {
                  var2 = 0;
               } else if(~var2 < -256) {
                  var2 = 255;
               }

               if(0 > var3) {
                  var3 = 0;
               } else if(var3 > 255) {
                  var3 = 255;
               }

               var4 >>= 4;
               if(var4 >= 0) {
                  if(-256 > ~var4) {
                     var4 = 255;
                  }
               } else {
                  var4 = 0;
               }

               this.anIntArray3443[var6] = Class3_Sub13_Sub29.bitwiseOr(var4, Class3_Sub13_Sub29.bitwiseOr(var3 << 8, var2 << 16));
            }
         }

      } catch (RuntimeException var13) {
         throw Class44.method1067(var13, "um.E(" + var1 + ')');
      }
   }

   static final void method347(boolean var0) {
      try {
         WorldListEntry.anInt2622 = 0;
         if(!var0) {
            method347(true);
         }

         int var1 = Class131.anInt1716 + (Class102.player.anInt2819 >> 7);
         int var2 = (Class102.player.anInt2829 >> 7) - -Class82.anInt1152;
         if(-3054 >= ~var1 && -3157 <= ~var1 && -3057 >= ~var2 && var2 <= 3136) {
            WorldListEntry.anInt2622 = 1;
         }

         if(var1 >= 3072 && var1 <= 3118 && var2 >= 9492 && var2 <= 9535) {
            WorldListEntry.anInt2622 = 1;
         }

         if(~WorldListEntry.anInt2622 == -2 && -3140 >= ~var1 && -3200 <= ~var1 && var2 >= 3008 && 3062 >= var2) {
            WorldListEntry.anInt2622 = 0;
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "um.O(" + var0 + ')');
      }
   }

   final int[][] method166(int var1, int var2) {
      try {
         int[][] var3 = this.aClass97_2376.method1594((byte)90, var2);
         if(var1 != -1) {
            this.method157(-71, (RSByteBuffer)null, false);
         }

         if(this.aClass97_2376.aBoolean1379) {
            int[] var5 = this.method152(0, var2, 32755);
            int[] var7 = var3[1];
            int[] var6 = var3[0];
            int[] var8 = var3[2];

            for(int var9 = 0; Class113.anInt1559 > var9; ++var9) {
               int var4 = var5[var9] >> 4;
               if(-1 < ~var4) {
                  var4 = 0;
               }

               if(var4 > 256) {
                  var4 = 256;
               }

               var4 = this.anIntArray3443[var4];
               var6[var9] = Class3_Sub28_Sub15.method633(var4, 16711680) >> 12;
               var7[var9] = Class3_Sub28_Sub15.method633(4080, var4 >> 4);
               var8[var9] = Class3_Sub28_Sub15.method633(255, var4) << 4;
            }
         }

         return var3;
      } catch (RuntimeException var10) {
         throw Class44.method1067(var10, "um.T(" + var1 + ',' + var2 + ')');
      }
   }

   public static void method348(int var0) {
      try {
         if(var0 >= 7) {
            aClass3_Sub28_Sub17_Sub1_3440 = null;
            aBigInteger3441 = null;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "um.Q(" + var0 + ')');
      }
   }

   static final boolean method349(int var0, int var1, int var2) {
      for(int var3 = 0; var3 < Class126.anInt1672; ++var3) {
         Class113 var4 = Class145.aClass113Array1895[var3];
         int var5;
         int var6;
         int var7;
         int var8;
         int var9;
         if(var4.anInt1564 == 1) {
            var5 = var4.anInt1562 - var0;
            if(var5 > 0) {
               var6 = var4.anInt1560 + (var4.anInt1555 * var5 >> 8);
               var7 = var4.anInt1550 + (var4.anInt1551 * var5 >> 8);
               var8 = var4.anInt1544 + (var4.anInt1561 * var5 >> 8);
               var9 = var4.anInt1548 + (var4.anInt1565 * var5 >> 8);
               if(var2 >= var6 && var2 <= var7 && var1 >= var8 && var1 <= var9) {
                  return true;
               }
            }
         } else if(var4.anInt1564 == 2) {
            var5 = var0 - var4.anInt1562;
            if(var5 > 0) {
               var6 = var4.anInt1560 + (var4.anInt1555 * var5 >> 8);
               var7 = var4.anInt1550 + (var4.anInt1551 * var5 >> 8);
               var8 = var4.anInt1544 + (var4.anInt1561 * var5 >> 8);
               var9 = var4.anInt1548 + (var4.anInt1565 * var5 >> 8);
               if(var2 >= var6 && var2 <= var7 && var1 >= var8 && var1 <= var9) {
                  return true;
               }
            }
         } else if(var4.anInt1564 == 3) {
            var5 = var4.anInt1560 - var2;
            if(var5 > 0) {
               var6 = var4.anInt1562 + (var4.anInt1549 * var5 >> 8);
               var7 = var4.anInt1545 + (var4.anInt1557 * var5 >> 8);
               var8 = var4.anInt1544 + (var4.anInt1561 * var5 >> 8);
               var9 = var4.anInt1548 + (var4.anInt1565 * var5 >> 8);
               if(var0 >= var6 && var0 <= var7 && var1 >= var8 && var1 <= var9) {
                  return true;
               }
            }
         } else if(var4.anInt1564 == 4) {
            var5 = var2 - var4.anInt1560;
            if(var5 > 0) {
               var6 = var4.anInt1562 + (var4.anInt1549 * var5 >> 8);
               var7 = var4.anInt1545 + (var4.anInt1557 * var5 >> 8);
               var8 = var4.anInt1544 + (var4.anInt1561 * var5 >> 8);
               var9 = var4.anInt1548 + (var4.anInt1565 * var5 >> 8);
               if(var0 >= var6 && var0 <= var7 && var1 >= var8 && var1 <= var9) {
                  return true;
               }
            }
         } else if(var4.anInt1564 == 5) {
            var5 = var1 - var4.anInt1544;
            if(var5 > 0) {
               var6 = var4.anInt1562 + (var4.anInt1549 * var5 >> 8);
               var7 = var4.anInt1545 + (var4.anInt1557 * var5 >> 8);
               var8 = var4.anInt1560 + (var4.anInt1555 * var5 >> 8);
               var9 = var4.anInt1550 + (var4.anInt1551 * var5 >> 8);
               if(var0 >= var6 && var0 <= var7 && var2 >= var8 && var2 <= var9) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   static final Class168 method350(byte var0, int var1) {
      try {
         Class168 var2 = (Class168)Class163_Sub2_Sub1.aClass93_4015.get((long)var1, (byte)121);
         if(null == var2) {
            int var3 = 70 % ((var0 - 43) / 57);
            byte[] var4 = Class3_Sub28_Sub5.aClass153_3580.getFile(4, (byte)-122, var1);
            var2 = new Class168();
            if(var4 != null) {
               var2.method2274(24559, new RSByteBuffer(var4), var1);
            }

            Class163_Sub2_Sub1.aClass93_4015.put((byte)-109, var2, (long)var1);
            return var2;
         } else {
            return var2;
         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "um.C(" + var0 + ',' + var1 + ')');
      }
   }

}
