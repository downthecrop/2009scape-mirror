package org.runite.jagex;

import java.util.Random;

final class Class3_Sub13_Sub7 extends Class3_Sub13 {

   private int anInt3085 = 10;
   private int anInt3086 = 0;
   static RSInterface aClass11_3087 = null;
   static Random aRandom3088 = new Random();
   private int[] anIntArray3089;
   static int anInt3090;
   private int[] anIntArray3091;
   private int anInt3093 = 2048;
   static boolean aBoolean3094 = false;
   static short[] aShortArray3095 = new short[500];
   private static RSString aClass94_3096 = RSString.createRSString("Close");
   static RSString aClass94_3097 = aClass94_3096;
   static CacheIndex aClass153_3098;
   static Class3_Sub28_Sub16 aClass3_Sub28_Sub16_3099;


   final void method158(int var1) {
      try {
         if(var1 == 16251) {
            this.method202(1);
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "d.P(" + var1 + ')');
      }
   }

   public static void method200(byte var0) {
      try {
         aClass94_3097 = null;
         aShortArray3095 = null;
         if(var0 != 122) {
            method201(62, -109, 127);
         }

         GameShell.frame = null;
         aRandom3088 = null;
         aClass94_3096 = null;
         aClass3_Sub28_Sub16_3099 = null;
         aClass11_3087 = null;
         aClass153_3098 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "d.E(" + var0 + ')');
      }
   }

   static final int method201(int var0, int var1, int var2) {
      try {
         int var3 = var1 + -1 & var0 >> 31;
         int var4 = -95 % ((33 - var2) / 54);
         return var3 + (var0 + (var0 >>> 31)) % var1;
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "d.C(" + var0 + ',' + var1 + ',' + var2 + ')');
      }
   }

   private final void method202(int var1) {
      try {
         if(var1 == 1) {
            int var2 = 0;
            this.anIntArray3091 = new int[this.anInt3085 + 1];
            int var3 = 4096 / this.anInt3085;
            this.anIntArray3089 = new int[this.anInt3085 + 1];
            int var4 = this.anInt3093 * var3 >> 12;

            for(int var5 = 0; this.anInt3085 > var5; ++var5) {
               this.anIntArray3089[var5] = var2;
               this.anIntArray3091[var5] = var2 + var4;
               var2 += var3;
            }

            this.anIntArray3089[this.anInt3085] = 4096;
            this.anIntArray3091[this.anInt3085] = this.anIntArray3091[0] + 4096;
         }
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "d.B(" + var1 + ')');
      }
   }

   final void method157(int var1, RSByteBuffer var2, boolean var3) {
      try {
         if(~var1 == -1) {
            this.anInt3085 = var2.getByte((byte)-118);
         } else if(-2 != ~var1) {
            if(-3 == ~var1) {
               this.anInt3086 = var2.getByte((byte)-81);
            }
         } else {
            this.anInt3093 = var2.getShort(1);
         }

         if(!var3) {
            this.anIntArray3089 = (int[])null;
         }

      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "d.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   public Class3_Sub13_Sub7() {
      super(0, true);
   }

   final int[] method154(int var1, byte var2) {
      try {
         int var4 = -12 / ((30 - var2) / 36);
         int[] var3 = this.aClass114_2382.method1709(-16409, var1);
         if(this.aClass114_2382.aBoolean1580) {
            int var5 = Class163_Sub3.anIntArray2999[var1];
            int var7;
            if(0 == this.anInt3086) {
               short var6 = 0;

               for(var7 = 0; this.anInt3085 > var7; ++var7) {
                  if(~this.anIntArray3089[var7] >= ~var5 && ~var5 > ~this.anIntArray3089[var7 - -1]) {
                     if(var5 < this.anIntArray3091[var7]) {
                        var6 = 4096;
                     }
                     break;
                  }
               }

               Class76.method1359(var3, 0, Class113.anInt1559, var6);
            } else {
               for(int var12 = 0; ~var12 > ~Class113.anInt1559; ++var12) {
                  int var9 = Class102.anIntArray2125[var12];
                  var7 = 0;
                  int var10 = this.anInt3086;
                  if(var10 == 1) {
                     var7 = var9;
                  } else if(var10 != 2) {
                     if(~var10 == -4) {
                        var7 = (-var5 + var9 >> 1) + 2048;
                     }
                  } else {
                     var7 = (var9 + var5 + -4096 >> 1) + 2048;
                  }

                  short var8 = 0;

                  for(var10 = 0; var10 < this.anInt3085; ++var10) {
                     if(~this.anIntArray3089[var10] >= ~var7 && ~var7 > ~this.anIntArray3089[var10 - -1]) {
                        if(~var7 > ~this.anIntArray3091[var10]) {
                           var8 = 4096;
                        }
                        break;
                     }
                  }

                  var3[var12] = var8;
               }
            }
         }

         return var3;
      } catch (RuntimeException var11) {
         throw Class44.method1067(var11, "d.D(" + var1 + ',' + var2 + ')');
      }
   }

}
