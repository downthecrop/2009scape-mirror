package org.runite.jagex;

final class Class3_Sub13_Sub39 extends Class3_Sub13 {

   private int[] anIntArray3457;
   static Class3_Sub28_Sub16[] aClass3_Sub28_Sub16Array3458;
   static int anInt3459;
   static int anInt3460 = 0;
   static byte[][] aByteArrayArray3461 = new byte[1000][];
   private int anInt3462 = 0;
   static int anInt3463;
   static int anInt3464 = 0;
   private short[] aShortArray3465 = new short[257];
   static int anInt3466;
   private int[] anIntArray3467;
   static boolean[][] aBooleanArrayArray3468 = new boolean[][]{{true, true, true}, {false, false}, {false, true}, {true, false}, {false, true, true}, {true, false, true}, {false, true, false}, {true, false, false}};
   private int[][] anIntArrayArray3469;


   private final void method352(int var1) {
      try {
         int var2 = this.anInt3462;
         int var3;
         int var4;
         int[] var5;
         int[] var6;
         int var7;
         int var8;
         int var9;
         int var10;
         if(2 != var2) {
            if(var2 == 1) {
               for(var2 = 0; -258 < ~var2; ++var2) {
                  var4 = var2 << 4;

                  for(var3 = 1; ~var3 > ~(-1 + this.anIntArrayArray3469.length) && ~this.anIntArrayArray3469[var3][0] >= ~var4; ++var3) {
                     ;
                  }

                  var5 = this.anIntArrayArray3469[-1 + var3];
                  var6 = this.anIntArrayArray3469[var3];
                  var7 = (var4 + -var5[0] << 12) / (-var5[0] + var6[0]);
                  var8 = 4096 + -Class75_Sub2.anIntArray2639[(8189 & var7) >> 5] >> 1;
                  var9 = -var8 + 4096;
                  var10 = var8 * var6[1] + var5[1] * var9 >> 12;
                  if(32767 <= ~var10) {
                     var10 = -32767;
                  }

                  if('\u8000' <= var10) {
                     var10 = 32767;
                  }

                  this.aShortArray3465[var2] = (short)var10;
               }
            } else {
               for(var2 = 0; ~var2 > -258; ++var2) {
                  var4 = var2 << 4;

                  for(var3 = 1; -1 + this.anIntArrayArray3469.length > var3 && ~this.anIntArrayArray3469[var3][0] >= ~var4; ++var3) {
                     ;
                  }

                  var6 = this.anIntArrayArray3469[var3];
                  var5 = this.anIntArrayArray3469[-1 + var3];
                  var7 = (-var5[0] + var4 << 12) / (-var5[0] + var6[0]);
                  var8 = -var7 + 4096;
                  var9 = var6[1] * var7 + var8 * var5[1] >> 12;
                  if(-32768 >= var9) {
                     var9 = -32767;
                  }

                  if('\u8000' <= var9) {
                     var9 = 32767;
                  }

                  this.aShortArray3465[var2] = (short)var9;
               }
            }
         } else {
            for(var2 = 0; -258 < ~var2; ++var2) {
               var4 = var2 << 4;

               for(var3 = 1; this.anIntArrayArray3469.length - 1 > var3 && this.anIntArrayArray3469[var3][0] <= var4; ++var3) {
                  ;
               }

               var6 = this.anIntArrayArray3469[var3];
               var5 = this.anIntArrayArray3469[var3 + -1];
               var7 = this.method356((byte)-106, var3 + -2)[1];
               var9 = var6[1];
               var8 = var5[1];
               int var15 = var9 - var7;
               var10 = this.method356((byte)-106, var3 + 1)[1];
               int var11 = (var4 + -var5[0] << 12) / (var6[0] + -var5[0]);
               int var13 = var10 + -var9 + -var7 + var8;
               int var12 = var11 * var11 >> 12;
               int var14 = var7 + (-var8 - var13);
               int var19 = var15 * var11 >> 12;
               int var18 = var12 * var14 >> 12;
               int var17 = var12 * (var11 * var13 >> 12) >> 12;
               int var20 = var8 + var18 + var17 + var19;
               if(var20 <= -32768) {
                  var20 = -32767;
               }

               if(~var20 <= -32769) {
                  var20 = 32767;
               }

               this.aShortArray3465[var2] = (short)var20;
            }
         }

         var2 = -74 % ((var1 - 50) / 33);
      } catch (RuntimeException var21) {
         throw Class44.method1067(var21, "wi.E(" + var1 + ')');
      }
   }

   final int[] method154(int var1, byte var2) {
      try {
         int var3 = -119 / ((30 - var2) / 36);
         int[] var8 = this.aClass114_2382.method1709(-16409, var1);
         if(this.aClass114_2382.aBoolean1580) {
            int[] var5 = this.method152(0, var1, 32755);

            for(int var6 = 0; ~Class113.anInt1559 < ~var6; ++var6) {
               int var4 = var5[var6] >> 4;
               if(0 > var4) {
                  var4 = 0;
               }

               if(256 < var4) {
                  var4 = 256;
               }

               var8[var6] = this.aShortArray3465[var4];
            }
         }

         return var8;
      } catch (RuntimeException var7) {
         throw Class44.method1067(var7, "wi.D(" + var1 + ',' + var2 + ')');
      }
   }

   public Class3_Sub13_Sub39() {
      super(1, true);
   }

   static final boolean method353(int var0, int var1) {
      try {
         if(var1 > var0) {
            return false;
         } else {
            int var2 = Class3_Sub13_Sub7.aShortArray3095[var0];
            if(var2 >= 2000) {
               var2 -= 2000;
            }

            return ~var2 == -1004;
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "wi.Q(" + var0 + ',' + var1 + ')');
      }
   }

   static final void method354(int var0, int var1) {
      try {
         Class3_Sub13_Sub30.anInt3362 = -1;
         if(var0 >= -121) {
            anInt3466 = -58;
         }

         Class3_Sub13_Sub30.anInt3362 = -1;
         Class3_Sub4.anInt2251 = var1;
         Class3_Sub5.method117((byte)87);
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "wi.B(" + var0 + ',' + var1 + ')');
      }
   }

   final void method158(int var1) {
      try {
         if(null == this.anIntArrayArray3469) {
            this.anIntArrayArray3469 = new int[][]{{0, 0}, {4096, 4096}};
         }

         if(this.anIntArrayArray3469.length < 2) {
            throw new RuntimeException("Curve operation requires at least two markers");
         } else {
            if(2 == this.anInt3462) {
               this.method355(true);
            }

            if(var1 != 16251) {
               anInt3463 = 22;
            }

            Class8.method844((byte)-9);
            this.method352(-11);
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "wi.P(" + var1 + ')');
      }
   }

   private final void method355(boolean var1) {
      try {
         int[] var2 = this.anIntArrayArray3469[0];
         if(!var1) {
            this.anIntArray3467 = (int[])null;
         }

         int[] var3 = this.anIntArrayArray3469[1];
         int[] var4 = this.anIntArrayArray3469[this.anIntArrayArray3469.length + -2];
         int[] var5 = this.anIntArrayArray3469[-1 + this.anIntArrayArray3469.length];
         this.anIntArray3457 = new int[]{var2[0] - (var3[0] + -var2[0]), -var3[1] + var2[1] + var2[1]};
         this.anIntArray3467 = new int[]{var4[0] + var4[0] + -var5[0], -var5[1] - -var4[1] + var4[1]};
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "wi.C(" + var1 + ')');
      }
   }

   final void method157(int var1, RSByteBuffer var2, boolean var3) {
      try {
         if(0 == var1) {
            this.anInt3462 = var2.getByte((byte)-31);
            this.anIntArrayArray3469 = new int[var2.getByte((byte)-52)][2];

            for(int var4 = 0; ~this.anIntArrayArray3469.length < ~var4; ++var4) {
               this.anIntArrayArray3469[var4][0] = var2.getShort(1);
               this.anIntArrayArray3469[var4][1] = var2.getShort(1);
            }
         }

         if(!var3) {
            this.anInt3462 = -6;
         }

      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "wi.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   private final int[] method356(byte var1, int var2) {
      try {
         if(~var2 <= -1) {
            if(~var2 <= ~this.anIntArrayArray3469.length) {
               return this.anIntArray3467;
            } else {
               if(var1 != -106) {
                  this.aShortArray3465 = (short[])null;
               }

               return this.anIntArrayArray3469[var2];
            }
         } else {
            return this.anIntArray3457;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "wi.F(" + var1 + ',' + var2 + ')');
      }
   }

   public static void method357(boolean var0) {
      try {
         aByteArrayArray3461 = (byte[][])null;
         aClass3_Sub28_Sub16Array3458 = null;
         if(var0) {
            method353(-71, 100);
         }

         aBooleanArrayArray3468 = (boolean[][])null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "wi.O(" + var0 + ')');
      }
   }

}
