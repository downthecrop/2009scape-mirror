package org.runite.jagex;
import java.awt.Frame;

final class Class3_Sub13_Sub10 extends Class3_Sub13 {

   static volatile boolean aBoolean3116 = true;
  
   static boolean[][] aBooleanArrayArray3118 = new boolean[][]{new boolean[0], {true, false, true}, {true, false, false, true}, {false, false, true, true}, {true, true, false}, {false, true, true}, {true, false, false, true}, {false, false, false, true, true}, {false, true, true}, {true, false, true, true, true}, {false, true, true, true, true}, {false, true, true, true, true, false}};
   static RSString aClass94_3119 = RSString.createRSString("vert:");
   private static RSString aClass94_3120 = RSString.createRSString("M");
   static Frame aFrame3121;
   static int anInt3122;
   static RSString aClass94_3123 = RSString.createRSString("::noclip");
   static RSString aClass94_3124 = aClass94_3120;
   static Signlink aClass87_3125;
   private int anInt3126 = 2048;
   private int anInt3127 = 3072;
   private int anInt3128 = 1024;
 static RSString aClass94_3117 = aClass94_3120;

   static final int method210(int var0, int var1, int var2, int var3) {
      try {
         if(~var2 == ~var3) {
            return var2;
         } else {
            int var4 = -var1 + 128;
            if(var0 != 18348) {
               method213(82, -103, 50, -59, (GameObject)null, 126L, (GameObject)null, (GameObject)null);
            }

            int var5 = var1 * ((-16711936 & var3) >>> 7) + var4 * ((-16711936 & var2) >>> 7) & -16711936;
            int var6 = var4 * (16711935 & var2) - -((var3 & 16711935) * var1) & -16711936;
            return var5 - -(var6 >> 7);
         }
      } catch (RuntimeException var7) {
         throw Class44.method1067(var7, "fh.O(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   final void method157(int var1, RSByteBuffer var2, boolean var3) {
      try {
         if(var1 != 0) {
            if(var1 == 1) {
               this.anInt3127 = var2.getShort(1);
            } else if(-3 == ~var1) {
               this.aBoolean2375 = var2.getByte((byte)-117) == 1;
            }
         } else {
            this.anInt3128 = var2.getShort(1);
         }

         if(!var3) {
            this.method157(56, (RSByteBuffer)null, true);
         }

      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "fh.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   public static void method211(int var0) {
      try {
         aClass94_3124 = null;
         aClass94_3117 = null;
         if(var0 != 1024) {
            aClass87_3125 = (Signlink)null;
         }

         aClass94_3120 = null;
         aClass94_3123 = null;
         aFrame3121 = null;
         aBooleanArrayArray3118 = (boolean[][])null;
         aClass94_3119 = null;
         aClass87_3125 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "fh.C(" + var0 + ')');
      }
   }

   final int[] method154(int var1, byte var2) {
      try {
         int var3 = 71 / ((var2 - 30) / 36);
         int[] var7 = this.aClass114_2382.method1709(-16409, var1);
         if(this.aClass114_2382.aBoolean1580) {
            int[] var4 = this.method152(0, var1, 32755);

            for(int var5 = 0; var5 < Class113.anInt1559; ++var5) {
               var7[var5] = this.anInt3128 - -(var4[var5] * this.anInt3126 >> 12);
            }
         }

         return var7;
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "fh.D(" + var1 + ',' + var2 + ')');
      }
   }

   static final void method212(long var0, int var2) {
      try {
         if(0L != var0) {
            for(int var3 = var2; ~var3 > ~Class3_Sub28_Sub5.anInt3591; ++var3) {
               if(Class114.aLongArray1574[var3] == var0) {
                  ++Class137.anInt1781;
                  --Class3_Sub28_Sub5.anInt3591;

                  for(int var4 = var3; ~Class3_Sub28_Sub5.anInt3591 < ~var4; ++var4) {
                     Class114.aLongArray1574[var4] = Class114.aLongArray1574[var4 + 1];
                     Class3_Sub13_Sub27.aClass94Array3341[var4] = Class3_Sub13_Sub27.aClass94Array3341[1 + var4];
                  }

                  Class110.anInt1472 = Class3_Sub13_Sub17.anInt3213;
                  Class3_Sub13_Sub1.outgoingBuffer.putOpcode(213);
                  Class3_Sub13_Sub1.outgoingBuffer.putLong(var0, -2037491440);
                  break;
               }
            }

         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "fh.E(" + var0 + ',' + var2 + ')');
      }
   }

   static final void method213(int var0, int var1, int var2, int var3, GameObject var4, long var5, GameObject var7, GameObject var8) {
      Class72 var9 = new Class72();
      var9.aClass140_1073 = var4;
      var9.anInt1078 = var1 * 128 + 64;
      var9.anInt1075 = var2 * 128 + 64;
      var9.anInt1068 = var3;
      var9.aLong1079 = var5;
      var9.aClass140_1067 = var7;
      var9.aClass140_1069 = var8;
      int var10 = 0;
      Class3_Sub2 var11 = Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2];
      if(var11 != null) {
         for(int var12 = 0; var12 < var11.anInt2223; ++var12) {
            Class25 var13 = var11.aClass25Array2221[var12];
            if((var13.aLong498 & 4194304L) == 4194304L) {
               int var14 = var13.aClass140_479.method1871();
               if(var14 != -32768 && var14 < var10) {
                  var10 = var14;
               }
            }
         }
      }

      var9.anInt1077 = -var10;
      if(Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2] == null) {
         Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2] = new Class3_Sub2(var0, var1, var2);
      }

      Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2].aClass72_2245 = var9;
   }

   static final void method214(GameObject var0, int var1, int var2, int var3, int var4, int var5) {
      boolean var6 = true;
      int var7 = var2;
      int var8 = var2 + var4;
      int var9 = var3 - 1;
      int var10 = var3 + var5;

      for(int var11 = var1; var11 <= var1 + 1; ++var11) {
         if(var11 != Class3_Sub17.anInt2456) {
            for(int var12 = var7; var12 <= var8; ++var12) {
               if(var12 >= 0 && var12 < IOHandler.anInt1234) {
                  for(int var13 = var9; var13 <= var10; ++var13) {
                     if(var13 >= 0 && var13 < Class3_Sub13_Sub15.anInt3179 && (!var6 || var12 >= var8 || var13 >= var10 || var13 < var3 && var12 != var2)) {
                        Class3_Sub2 var14 = Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var11][var12][var13];
                        if(var14 != null) {
                           int var15 = (Class44.anIntArrayArrayArray723[var11][var12][var13] + Class44.anIntArrayArrayArray723[var11][var12 + 1][var13] + Class44.anIntArrayArrayArray723[var11][var12][var13 + 1] + Class44.anIntArrayArrayArray723[var11][var12 + 1][var13 + 1]) / 4 - (Class44.anIntArrayArrayArray723[var1][var2][var3] + Class44.anIntArrayArrayArray723[var1][var2 + 1][var3] + Class44.anIntArrayArrayArray723[var1][var2][var3 + 1] + Class44.anIntArrayArrayArray723[var1][var2 + 1][var3 + 1]) / 4;
                           Class70 var16 = var14.aClass70_2234;
                           if(var16 != null) {
                              if(var16.aClass140_1049.method1865()) {
                                 var0.method1866(var16.aClass140_1049, (var12 - var2) * 128 + (1 - var4) * 64, var15, (var13 - var3) * 128 + (1 - var5) * 64, var6);
                              }

                              if(var16.aClass140_1052 != null && var16.aClass140_1052.method1865()) {
                                 var0.method1866(var16.aClass140_1052, (var12 - var2) * 128 + (1 - var4) * 64, var15, (var13 - var3) * 128 + (1 - var5) * 64, var6);
                              }
                           }

                           for(int var17 = 0; var17 < var14.anInt2223; ++var17) {
                              Class25 var18 = var14.aClass25Array2221[var17];
                              if(var18 != null && var18.aClass140_479.method1865() && (var12 == var18.anInt483 || var12 == var7) && (var13 == var18.anInt478 || var13 == var9)) {
                                 int var19 = var18.anInt495 - var18.anInt483 + 1;
                                 int var20 = var18.anInt481 - var18.anInt478 + 1;
                                 var0.method1866(var18.aClass140_479, (var18.anInt483 - var2) * 128 + (var19 - var4) * 64, var15, (var18.anInt478 - var3) * 128 + (var20 - var5) * 64, var6);
                              }
                           }
                        }
                     }
                  }
               }
            }

            --var7;
            var6 = false;
         }
      }

   }

   static final void method215(byte var0, LDIndexedSprite var1) {
      try {
         short var2 = 256;
         int var3 = 0;
         if(var0 >= -80) {
            aBoolean3116 = true;
         }

         while(Class161.anIntArray2026.length > var3) {
            Class161.anIntArray2026[var3] = 0;
            ++var3;
         }

         int var4;
         for(var3 = 0; -5001 < ~var3; ++var3) {
            var4 = (int)((double)var2 * Math.random() * 128.0D);
            Class161.anIntArray2026[var4] = (int)(Math.random() * 284.0D);
         }

         int var5;
         int var6;
         for(var3 = 0; -21 < ~var3; ++var3) {
            for(var4 = 1; ~(-1 + var2) < ~var4; ++var4) {
               for(var5 = 1; var5 < 127; ++var5) {
                  var6 = var5 - -(var4 << 7);
                  OutputStream_Sub1.anIntArray49[var6] = (Class161.anIntArray2026[128 + var6] + Class161.anIntArray2026[-1 + var6] + Class161.anIntArray2026[1 + var6] - -Class161.anIntArray2026[-128 + var6]) / 4;
               }
            }

            int[] var10 = Class161.anIntArray2026;
            Class161.anIntArray2026 = OutputStream_Sub1.anIntArray49;
            OutputStream_Sub1.anIntArray49 = var10;
         }

         if(var1 != null) {
            var3 = 0;

            for(var4 = 0; var1.anInt1468 > var4; ++var4) {
               for(var5 = 0; var1.anInt1461 > var5; ++var5) {
                  if(var1.aByteArray2674[var3++] != 0) {
                     var6 = var1.anInt1470 + var5 + 16;
                     int var7 = var1.anInt1464 + (var4 - -16);
                     int var8 = var6 - -(var7 << 7);
                     Class161.anIntArray2026[var8] = 0;
                  }
               }
            }
         }

      } catch (RuntimeException var9) {
         throw Class44.method1067(var9, "fh.F(" + var0 + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

   final int[][] method166(int var1, int var2) {
      try {
         int[][] var3 = this.aClass97_2376.method1594((byte)-118, var2);
         if(this.aClass97_2376.aBoolean1379) {
            int[][] var4 = this.method162(var2, 0, (byte)-50);
            int[] var6 = var4[1];
            int[] var7 = var4[2];
            int[] var5 = var4[0];
            int[] var8 = var3[0];
            int[] var9 = var3[1];
            int[] var10 = var3[2];

            for(int var11 = 0; ~var11 > ~Class113.anInt1559; ++var11) {
               var8[var11] = this.anInt3128 - -(this.anInt3126 * var5[var11] >> 12);
               var9[var11] = (this.anInt3126 * var6[var11] >> 12) + this.anInt3128;
               var10[var11] = this.anInt3128 + (this.anInt3126 * var7[var11] >> 12);
            }
         }

         if(var1 != -1) {
            method211(51);
         }

         return var3;
      } catch (RuntimeException var12) {
         throw Class44.method1067(var12, "fh.T(" + var1 + ',' + var2 + ')');
      }
   }

   final void method158(int var1) {
      try {
         if(var1 == 16251) {
            this.anInt3126 = this.anInt3127 - this.anInt3128;
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "fh.P(" + var1 + ')');
      }
   }

   public Class3_Sub13_Sub10() {
      super(1, false);
   }

   static final void method216(RSByteBuffer var0, int var1) {
      try {
         for(int var2 = 0; ~Class57.activeWorldListSize < ~var2; ++var2) {
            int var3 = var0.getSmart(true);
            int var4 = var0.getShort(1);
            if(~var4 == -65536) {
               var4 = -1;
            }

            if(null != Class117.worldList[var3]) {
               Class117.worldList[var3].anInt722 = var4;
            }
         }

         if(var1 != -14991) {
            aClass94_3123 = (RSString)null;
         }

      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "fh.Q(" + (var0 != null?"{...}":"null") + ',' + var1 + ')');
      }
   }

}
