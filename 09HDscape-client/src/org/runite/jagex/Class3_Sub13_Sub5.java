package org.runite.jagex;

final class Class3_Sub13_Sub5 extends Class3_Sub13 {

   static int anInt3068 = 0;
   static int anInt3069 = 0;
   static int anInt3070 = 0;
   static int anInt3071 = 0;
   static int anInt3072 = -1;


   final int[][] method166(int var1, int var2) {
      try {
         if(var1 != -1) {
            anInt3069 = 67;
         }

         int[][] var3 = this.aClass97_2376.method1594((byte)7, var2);
         if(this.aClass97_2376.aBoolean1379) {
            int[] var4 = this.method152(2, var2, 32755);
            int[][] var5 = this.method162(var2, 0, (byte)-119);
            int[][] var6 = this.method162(var2, 1, (byte)-107);
            int[] var9 = var3[2];
            int[] var8 = var3[1];
            int[] var10 = var5[0];
            int[] var11 = var5[1];
            int[] var7 = var3[0];
            int[] var13 = var6[0];
            int[] var12 = var5[2];
            int[] var15 = var6[2];
            int[] var14 = var6[1];

            for(int var16 = 0; ~Class113.anInt1559 < ~var16; ++var16) {
               int var17 = var4[var16];
               if(var17 != 4096) {
                  if(0 != var17) {
                     int var18 = -var17 + 4096;
                     var7[var16] = var18 * var13[var16] + var17 * var10[var16] >> 12;
                     var8[var16] = var18 * var14[var16] + var11[var16] * var17 >> 12;
                     var9[var16] = var15[var16] * var18 + var12[var16] * var17 >> 12;
                  } else {
                     var7[var16] = var13[var16];
                     var8[var16] = var14[var16];
                     var9[var16] = var15[var16];
                  }
               } else {
                  var7[var16] = var10[var16];
                  var8[var16] = var11[var16];
                  var9[var16] = var12[var16];
               }
            }
         }

         return var3;
      } catch (RuntimeException var19) {
         throw Class44.method1067(var19, "bl.T(" + var1 + ',' + var2 + ')');
      }
   }

   public Class3_Sub13_Sub5() {
      super(3, false);
   }

   final void method157(int var1, RSByteBuffer var2, boolean var3) {
      try {
         if(var1 == 0) {
            this.aBoolean2375 = var2.getByte((byte)-91) == 1;
         }

         if(!var3) {
            this.method157(118, (RSByteBuffer)null, true);
         }

      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "bl.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   static final void method194(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      try {
         if(~Class101.anInt1425 >= ~var7 && Class3_Sub28_Sub18.anInt3765 >= var6 && var4 >= Class159.anInt2020 && var1 <= Class57.anInt902) {
            Class3_Sub13_Sub19.method262(var3, 119, var4, var1, var2, var0, var6, var7);
         } else {
            Class143.method2062(var6, var2, var5 + -4185, var1, var0, var3, var4, var7);
         }

         if(var5 == 4096) {
            ;
         }
      } catch (RuntimeException var9) {
         throw Class44.method1067(var9, "bl.B(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ')');
      }
   }

   final int[] method154(int var1, byte var2) {
      try {
         int[] var3 = this.aClass114_2382.method1709(-16409, var1);
         int var4 = 51 / ((30 - var2) / 36);
         if(this.aClass114_2382.aBoolean1580) {
            int[] var5 = this.method152(0, var1, 32755);
            int[] var6 = this.method152(1, var1, 32755);
            int[] var7 = this.method152(2, var1, 32755);

            for(int var8 = 0; ~Class113.anInt1559 < ~var8; ++var8) {
               int var9 = var7[var8];
               if(4096 == var9) {
                  var3[var8] = var5[var8];
               } else if(~var9 != -1) {
                  var3[var8] = var9 * var5[var8] - -((-var9 + 4096) * var6[var8]) >> 12;
               } else {
                  var3[var8] = var6[var8];
               }
            }
         }

         return var3;
      } catch (RuntimeException var10) {
         throw Class44.method1067(var10, "bl.D(" + var1 + ',' + var2 + ')');
      }
   }

}
