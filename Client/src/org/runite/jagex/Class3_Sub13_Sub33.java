package org.runite.jagex;

final class Class3_Sub13_Sub33 extends Class3_Sub13 {

   static byte[][][] aByteArrayArrayArray3390;
   static RSString[] aClass94Array3391;
   private Class75[] aClass75Array3392;
   static Class133[] aClass133Array3393 = new Class133[6];
   static RSString aClass94_3394 = RSString.createRSString("<col=ffb000>");
   static int anInt3395;
   static byte[] aByteArray3396;
   
   static volatile int anInt3398 = 0;
   static RSString aClass94_3399 = RSString.createRSString("<br>(X");
   private static RSString aClass94_3400 = RSString.createRSString("Take");
   static RSString aClass94_3401 = RSString.createRSString("Weiter");
static RSString aClass94_3397 = aClass94_3400;

   final int[] method154(int var1, byte var2) {
      try {
         int var4 = -75 % ((30 - var2) / 36);
         int[] var3 = this.aClass114_2382.method1709(-16409, var1);
         if(this.aClass114_2382.aBoolean1580) {
            this.method323(-60, this.aClass114_2382.method1710((byte)124));
         }

         return var3;
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "si.D(" + var1 + ',' + var2 + ')');
      }
   }

   static final int method322(boolean var0, byte var1) {
      try {
         return !var0?104:255 & var1;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "si.C(" + var0 + ',' + var1 + ')');
      }
   }

   private final void method323(int var1, int[][] var2) {
      try {
         int var4 = Class101.anInt1427;
         int var3 = Class113.anInt1559;
         Class3_Sub13_Sub13.method230(var2, true);
         Class58.method1196(0, 0, (byte)111, Class3_Sub20.anInt2487, RenderAnimationDefinition.anInt396);
         if(this.aClass75Array3392 != null) {
            for(int var5 = 0; this.aClass75Array3392.length > var5; ++var5) {
               Class75 var6 = this.aClass75Array3392[var5];
               int var7 = var6.anInt1101;
               int var8 = var6.anInt1104;
               if(var7 >= 0) {
                  if(~var8 > -1) {
                     var6.method1341(2, var3, var4);
                  } else {
                     var6.method1335(var4, var3, 4898);
                  }
               } else if(~var8 <= -1) {
                  var6.method1337(var4, true, var3);
               }
            }
         }

         if(var1 != -60) {
            method326((byte)-35, (RSString)null);
         }

      } catch (RuntimeException var9) {
         throw Class44.method1067(var9, "si.F(" + var1 + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

   final void method157(int var1, RSByteBuffer var2, boolean var3) {
      try {
         if(var1 == 0) {
            this.aClass75Array3392 = new Class75[var2.getByte((byte)-77)];

            for(int var4 = 0; ~this.aClass75Array3392.length < ~var4; ++var4) {
               int var5 = var2.getByte((byte)-44);
               if(var5 == 0) {
                  this.aClass75Array3392[var4] = Class8.method843(-5232, var2);
               } else if(-2 == ~var5) {
                  this.aClass75Array3392[var4] = Class3_Sub28_Sub2.method536((byte)54, var2);
               } else if(var5 != 2) {
                  if(3 == var5) {
                     this.aClass75Array3392[var4] = Class3_Sub19.method384(var2, (byte)80);
                  }
               } else {
                  this.aClass75Array3392[var4] = Class3_Sub22.method404((byte)-110, var2);
               }
            }
         } else if(1 == var1) {
            this.aBoolean2375 = var2.getByte((byte)-48) == 1;
         }

         if(!var3) {
            this.method323(124, (int[][])((int[][])null));
         }

      } catch (RuntimeException var7) {
         throw Class44.method1067(var7, "si.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   static final void method324(int var0, boolean var1) {
      try {
         Class92.setLightParams(Class92.defaultScreenColorRgb, (0.7F + (float)var0 * 0.1F) * 1.1523438F, 0.69921875F, 0.69921875F);
         Class92.setLightPosition(-50.0F, -60.0F, -50.0F);
         Class92.setFogValues(Class92.defaulFogColorRgb, 0);
         Class92.method1504();
         if(var1) {
            aByteArrayArrayArray3390 = (byte[][][])((byte[][][])null);
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "si.Q(" + var0 + ',' + var1 + ')');
      }
   }

   public static void method325(int var0) {
      try {
         aClass94_3401 = null;
         aByteArrayArrayArray3390 = (byte[][][])null;
         if(var0 == 0) {
            aByteArray3396 = null;
            aClass133Array3393 = null;
            aClass94_3400 = null;
            aClass94Array3391 = null;
            aClass94_3397 = null;
            aClass94_3399 = null;
            aClass94_3394 = null;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "si.E(" + var0 + ')');
      }
   }

   static final int method326(byte var0, RSString var1) {
      try {
         if(var0 <= 13) {
            aClass94_3399 = (RSString)null;
         }

         return var1.length(-43) + 1;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "si.O(" + var0 + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

   final int[][] method166(int var1, int var2) {
      try {
         if(var1 != -1) {
            return (int[][])((int[][])null);
         } else {
            int[][] var3 = this.aClass97_2376.method1594((byte)-117, var2);
            if(this.aClass97_2376.aBoolean1379) {
               int var4 = Class113.anInt1559;
               int var5 = Class101.anInt1427;
               int[][] var6 = new int[var5][var4];
               int[][][] var7 = this.aClass97_2376.method1589((byte)-56);
               this.method323(-60, var6);

               for(int var8 = 0; var8 < Class101.anInt1427; ++var8) {
                  int[] var9 = var6[var8];
                  int[][] var10 = var7[var8];
                  int[] var11 = var10[0];
                  int[] var12 = var10[1];
                  int[] var13 = var10[2];

                  for(int var14 = 0; ~var14 > ~Class113.anInt1559; ++var14) {
                     int var15 = var9[var14];
                     var13[var14] = Class3_Sub28_Sub15.method633(255, var15) << 4;
                     var12[var14] = Class3_Sub28_Sub15.method633(4080, var15 >> 4);
                     var11[var14] = Class3_Sub28_Sub15.method633(var15 >> 12, 4080);
                  }
               }
            }

            return var3;
         }
      } catch (RuntimeException var16) {
         throw Class44.method1067(var16, "si.T(" + var1 + ',' + var2 + ')');
      }
   }

   public Class3_Sub13_Sub33() {
      super(0, true);
   }

   static final void method327(int var0, int var1, byte var2) {
      try {
         if(var2 != 68) {
            aClass94_3397 = (RSString)null;
         }

         Class3_Sub28_Sub6 var3 = Class3_Sub24_Sub3.method466(var2 + -64, 12, var1);
         var3.g((byte)33);
         var3.anInt3598 = var0;
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "si.B(" + var0 + ',' + var1 + ',' + var2 + ')');
      }
   }

}
