package org.runite.jagex;

final class Class3_Sub13_Sub9 extends Class3_Sub13 {

   static int[] anIntArray3107;
   private int anInt3108 = 1;
   private int anInt3109 = 204;
   static short[] aShortArray3110 = new short[256];
   static int anInt3111 = 0;
   static Class36 aClass36_3112;
   private int anInt3113 = 1;
   static int anInt3114 = 0;
   static int[][] anIntArrayArray3115;


   static final int method208(int var0) {
      try {
         if(var0 > -22) {
            aShortArray3110 = (short[])null;
         }

         return 15;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "f.B(" + var0 + ')');
      }
   }

   public static void method209(byte var0) {
      try {
         aShortArray3110 = null;
         aClass36_3112 = null;
         if(var0 >= 30) {
            anIntArray3107 = null;
            anIntArrayArray3115 = (int[][])null;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "f.C(" + var0 + ')');
      }
   }

   public Class3_Sub13_Sub9() {
      super(0, true);
   }

   final void method157(int var1, RSByteBuffer var2, boolean var3) {
      try {
         if(var3) {
            if(~var1 != -1) {
               if(var1 == 1) {
                  this.anInt3113 = var2.getByte((byte)-118);
               } else if(var1 == 2) {
                  this.anInt3109 = var2.getShort(1);
               }
            } else {
               this.anInt3108 = var2.getByte((byte)-86);
            }

         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "f.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   final int[] method154(int var1, byte var2) {
      try {
         int var3 = -24 / ((var2 - 30) / 36);
         int[] var4 = this.aClass114_2382.method1709(-16409, var1);
         if(this.aClass114_2382.aBoolean1580) {
            for(int var5 = 0; Class113.anInt1559 > var5; ++var5) {
               int var6 = Class102.anIntArray2125[var5];
               int var7 = Class163_Sub3.anIntArray2999[var1];
               int var8 = this.anInt3108 * var6 >> 12;
               int var9 = var7 * this.anInt3113 >> 12;
               int var10 = this.anInt3108 * (var6 % (4096 / this.anInt3108));
               int var11 = var7 % (4096 / this.anInt3113) * this.anInt3113;
               if(~this.anInt3109 < ~var11) {
                  for(var8 -= var9; ~var8 > -1; var8 += 4) {
                     ;
                  }

                  while(3 < var8) {
                     var8 -= 4;
                  }

                  if(1 != var8) {
                     var4[var5] = 0;
                     continue;
                  }

                  if(~this.anInt3109 < ~var10) {
                     var4[var5] = 0;
                     continue;
                  }
               }

               if(var10 < this.anInt3109) {
                  for(var8 -= var9; 0 > var8; var8 += 4) {
                     ;
                  }

                  while(~var8 < -4) {
                     var8 -= 4;
                  }

                  if(var8 > 0) {
                     var4[var5] = 0;
                     continue;
                  }
               }

               var4[var5] = 4096;
            }
         }

         return var4;
      } catch (RuntimeException var12) {
         throw Class44.method1067(var12, "f.D(" + var1 + ',' + var2 + ')');
      }
   }

}
