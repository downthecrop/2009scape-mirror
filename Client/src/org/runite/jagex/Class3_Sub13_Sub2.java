package org.runite.jagex;

final class Class3_Sub13_Sub2 extends Class3_Sub13 {

   static RSString aClass94_3042 = RSString.createRSString("<col=ff9040>");
   private int anInt3043 = 0;
   static float aFloat3044;
   static int[] anIntArray3045;
   private int anInt3046 = 4096;


   final void method157(int var1, RSByteBuffer var2, boolean var3) {
      try {
         if(0 == var1) {
            this.anInt3043 = var2.getShort(1);
         } else if(1 != var1) {
            if(2 == var1) {
               this.aBoolean2375 = var2.getByte((byte)-76) == 1;
            }
         } else {
            this.anInt3046 = var2.getShort(1);
         }

         if(!var3) {
            aClass94_3042 = (RSString)null;
         }

      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "aj.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   public static void method172(int var0) {
      try {
         anIntArray3045 = null;
         if(var0 == 11597) {
            aClass94_3042 = null;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "aj.B(" + var0 + ')');
      }
   }

   final int[][] method166(int var1, int var2) {
      try {
         if(var1 != -1) {
            method175(2, -7, -114, true, -24, 102, -125);
         }

         int[][] var3 = this.aClass97_2376.method1594((byte)-119, var2);
         if(this.aClass97_2376.aBoolean1379) {
            int[][] var4 = this.method162(var2, 0, (byte)-124);
            int[] var6 = var4[1];
            int[] var7 = var4[2];
            int[] var5 = var4[0];
            int[] var8 = var3[0];
            int[] var9 = var3[1];
            int[] var10 = var3[2];

            for(int var11 = 0; ~Class113.anInt1559 < ~var11; ++var11) {
               int var13 = var6[var11];
               int var12 = var5[var11];
               int var14 = var7[var11];
               if(~this.anInt3043 >= ~var12) {
                  if(var12 > this.anInt3046) {
                     var8[var11] = this.anInt3046;
                  } else {
                     var8[var11] = var12;
                  }
               } else {
                  var8[var11] = this.anInt3043;
               }

               if(this.anInt3043 > var13) {
                  var9[var11] = this.anInt3043;
               } else if(var13 <= this.anInt3046) {
                  var9[var11] = var13;
               } else {
                  var9[var11] = this.anInt3046;
               }

               if(var14 >= this.anInt3043) {
                  if(this.anInt3046 >= var14) {
                     var10[var11] = var14;
                  } else {
                     var10[var11] = this.anInt3046;
                  }
               } else {
                  var10[var11] = this.anInt3043;
               }
            }
         }

         return var3;
      } catch (RuntimeException var15) {
         throw Class44.method1067(var15, "aj.T(" + var1 + ',' + var2 + ')');
      }
   }

   static final int method173(byte var0, int var1, RSInterface var2) {
      try {
         if(var0 < 4) {
            method176(-50);
         }

         return !Client.method44(var2).method92(var1, (byte)-109) && null == var2.anObjectArray314?-1:(null != var2.anIntArray249 && ~var1 > ~var2.anIntArray249.length?var2.anIntArray249[var1]:-1);
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "aj.F(" + var0 + ',' + var1 + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

   static final void method174(int var0, int var1) {
      try {
         Class163_Sub2_Sub1.aClass93_4015.method1522(-127, var1);
         if(var0 != 4096) {
            method176(92);
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "aj.E(" + var0 + ',' + var1 + ')');
      }
   }

   static final void method175(int var0, int var1, int var2, boolean var3, int var4, int var5, int var6) {
      try {
         MouseListeningClass.method2091(var5, 4);
         int var8 = var5 - var1;
         int var7 = 0;
         if(var8 < 0) {
            var8 = 0;
         }

         int var10 = -var5;
         int var9 = var5;
         int var11 = var8;
         int var12 = -var8;
         int var14 = -1;
         int[] var15 = Class38.anIntArrayArray663[var2];
         int var17 = var6 - -var8;
         int var13 = -1;
         int var16 = -var8 + var6;
         Class3_Sub13_Sub23_Sub1.method282(var15, -var5 + var6, -40, var16, var0);
         Class3_Sub13_Sub23_Sub1.method282(var15, var16, -51, var17, var4);
         Class3_Sub13_Sub23_Sub1.method282(var15, var17, -41, var6 - -var5, var0);
         if(!var3) {
            anIntArray3045 = (int[])null;
         }

         while(~var7 > ~var9) {
            var14 += 2;
            var12 += var14;
            if(~var12 <= -1 && ~var11 <= -2) {
               GameObject.anIntArray1838[var11] = var7;
               --var11;
               var12 -= var11 << 1;
            }

            ++var7;
            var13 += 2;
            var10 += var13;
            int[] var19;
            int[] var18;
            int var21;
            int var20;
            int var23;
            int var22;
            int var24;
            if(~var10 <= -1) {
               --var9;
               var10 -= var9 << 1;
               if(~var8 < ~var9) {
                  var18 = Class38.anIntArrayArray663[var9 + var2];
                  var19 = Class38.anIntArrayArray663[-var9 + var2];
                  var22 = -var7 + var6;
                  var21 = var7 + var6;
                  var20 = GameObject.anIntArray1838[var9];
                  var24 = -var20 + var6;
                  var23 = var20 + var6;
                  Class3_Sub13_Sub23_Sub1.method282(var18, var22, -113, var24, var0);
                  Class3_Sub13_Sub23_Sub1.method282(var18, var24, 95, var23, var4);
                  Class3_Sub13_Sub23_Sub1.method282(var18, var23, 117, var21, var0);
                  Class3_Sub13_Sub23_Sub1.method282(var19, var22, 113, var24, var0);
                  Class3_Sub13_Sub23_Sub1.method282(var19, var24, -76, var23, var4);
                  Class3_Sub13_Sub23_Sub1.method282(var19, var23, -97, var21, var0);
               } else {
                  var18 = Class38.anIntArrayArray663[var2 + var9];
                  var19 = Class38.anIntArrayArray663[var2 - var9];
                  var20 = var7 + var6;
                  var21 = var6 + -var7;
                  Class3_Sub13_Sub23_Sub1.method282(var18, var21, 113, var20, var0);
                  Class3_Sub13_Sub23_Sub1.method282(var19, var21, -100, var20, var0);
               }
            }

            var18 = Class38.anIntArrayArray663[var2 - -var7];
            var19 = Class38.anIntArrayArray663[var2 - var7];
            var20 = var9 + var6;
            var21 = -var9 + var6;
            if(var8 <= var7) {
               Class3_Sub13_Sub23_Sub1.method282(var18, var21, 104, var20, var0);
               Class3_Sub13_Sub23_Sub1.method282(var19, var21, -127, var20, var0);
            } else {
               var22 = var7 <= var11?var11:GameObject.anIntArray1838[var7];
               var23 = var22 + var6;
               var24 = var6 + -var22;
               Class3_Sub13_Sub23_Sub1.method282(var18, var21, -94, var24, var0);
               Class3_Sub13_Sub23_Sub1.method282(var18, var24, 115, var23, var4);
               Class3_Sub13_Sub23_Sub1.method282(var18, var23, 110, var20, var0);
               Class3_Sub13_Sub23_Sub1.method282(var19, var21, -114, var24, var0);
               Class3_Sub13_Sub23_Sub1.method282(var19, var24, -79, var23, var4);
               Class3_Sub13_Sub23_Sub1.method282(var19, var23, 120, var20, var0);
            }
         }

      } catch (RuntimeException var25) {
         throw Class44.method1067(var25, "aj.C(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ')');
      }
   }

   public Class3_Sub13_Sub2() {
      super(1, false);
   }

   static final void method176(int var0) {
      try {
         int var1 = 0;
         if(var0 >= -111) {
            method176(40);
         }

         for(; ~Class95.anInt1344 < ~var1; ++var1) {
            Class29 var2 = Class145.method2076(-109, var1);
            if(null != var2 && ~var2.anInt556 == -1) {
               Class57.anIntArray898[var1] = 0;
               Class163_Sub1.anIntArray2985[var1] = 0;
            }
         }

         Class3_Sub28_Sub15.aClass130_3679 = new Class130(16);
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "aj.O(" + var0 + ')');
      }
   }

   final int[] method154(int var1, byte var2) {
      try {
         int var4 = -52 % ((var2 - 30) / 36);
         int[] var3 = this.aClass114_2382.method1709(-16409, var1);
         if(this.aClass114_2382.aBoolean1580) {
            int[] var5 = this.method152(0, var1, 32755);

            for(int var6 = 0; var6 < Class113.anInt1559; ++var6) {
               int var7 = var5[var6];
               if(this.anInt3043 > var7) {
                  var3[var6] = this.anInt3043;
               } else if(this.anInt3046 >= var7) {
                  var3[var6] = var7;
               } else {
                  var3[var6] = this.anInt3046;
               }
            }
         }

         return var3;
      } catch (RuntimeException var8) {
         throw Class44.method1067(var8, "aj.D(" + var1 + ',' + var2 + ')');
      }
   }

}
