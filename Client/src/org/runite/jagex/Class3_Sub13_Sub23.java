package org.runite.jagex;

class Class3_Sub13_Sub23 extends Class3_Sub13 {

   private int anInt3278 = -1;
   static RSString aClass94_3279 = RSString.createRSString("Lade)3)3)3");
   int anInt3280;
   static RSString aClass94_3281 = RSString.createRSString("sch-Utteln:");
   
   int anInt3283;
   int[] anIntArray3284;
   static int anInt3285 = 128;
   private static RSString aClass94_3286 = RSString.createRSString("Loading )2 please wait)3");
   static int itemDefinitionSize;
   static int[] anIntArray3288 = new int[]{4, 4, 1, 2, 6, 4, 2, 49, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
   static RSString aClass94_3289 = RSString.createRSString("::fps ");
static RSString aClass94_3282 = aClass94_3286;

   static final int method275(int var0, int var1, int var2, int var3, int var4) {
      try {
         if(var3 <= 8) {
            anIntArray3288 = (int[])null;
         }

         int var5 = -Class51.anIntArray851[1024 * var2 / var4] + 65536 >> 1;
         return (var0 * (-var5 + 65536) >> 16) + (var1 * var5 >> 16);
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "nh.CA(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ')');
      }
   }

   static final void method276(int var0, int var1, int var2, int var3, GameObject var4, long var5, boolean var7) {
      if(var4 != null) {
         Class12 var8 = new Class12();
         var8.object = var4;
         var8.anInt324 = var1 * 128 + 64;
         var8.anInt330 = var2 * 128 + 64;
         var8.anInt326 = var3;
         var8.aLong328 = var5;
         var8.aBoolean329 = var7;
         if(Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2] == null) {
            Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2] = new Class3_Sub2(var0, var1, var2);
         }

         Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2].aClass12_2230 = var8;
      }
   }

   public static void method277(byte var0) {
      try {
         aClass94_3281 = null;
         aClass94_3286 = null;
         if(var0 >= -11) {
            itemDefinitionSize = -68;
         }

         aClass94_3282 = null;
         aClass94_3289 = null;
         anIntArray3288 = null;
         aClass94_3279 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "nh.DA(" + var0 + ')');
      }
   }

   static final boolean handleWorldListUpdate(int var0, byte[] buf) {
      try {
         if(var0 != 4) {
            return false;
         } else {
            RSByteBuffer buffer = new RSByteBuffer(buf);
            int opcode = buffer.getByte((byte)-67);
            //System.out.println(opcode);
            if(1 != opcode) {
               return false;
            } else {
               boolean updated = ~buffer.getByte((byte)-70) == -2;
               if(updated) {
                  Class53.parseWorldList(buffer, var0 ^ -84);
               }

               Class3_Sub13_Sub10.method216(buffer, -14991);
               return true;
            }
         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "nh.AA(" + var0 + ',' + (buf != null?"{...}":"null") + ')');
      }
   }

   final boolean method279(int var1) {
      try {
         int var2 = 62 / ((var1 - 2) / 62);
         if(null == this.anIntArray3284) {
            if(-1 < ~this.anInt3278) {
               return false;
            } else {
               Class3_Sub28_Sub16_Sub2 var3 = ~Class126.anInt1668 > -1?RSString.method1537(Class104.aClass153_2172, this.anInt3278, false):Class40.method1043(this.anInt3278, Class104.aClass153_2172, -3178, Class126.anInt1668);
               var3.method665();
               this.anInt3283 = var3.anInt3696;
               this.anInt3280 = var3.anInt3707;
               this.anIntArray3284 = var3.anIntArray4081;
               return true;
            }
         } else {
            return true;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "nh.FA(" + var1 + ')');
      }
   }

   final int method159(int var1) {
      try {
         return var1 != 4?40:this.anInt3278;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "nh.GA(" + var1 + ')');
      }
   }

   public Class3_Sub13_Sub23() {
      super(0, false);
   }

   static final void method280(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12) {
      try {
         Class3_Sub5 var13 = new Class3_Sub5();
         if(var10 == -745213428) {
            var13.anInt2284 = var6;
            var13.anInt2283 = var3;
            var13.anInt2266 = var1;
            var13.anInt2279 = var5;
            var13.anInt2273 = var2;
            var13.anInt2271 = var8;
            var13.anInt2277 = var11;
            var13.anInt2282 = var4;
            var13.anInt2270 = var12;
            var13.anInt2268 = var7;
            var13.anInt2272 = var0;
            var13.anInt2278 = var9;
            Class3_Sub18.aClass61_2468.method1215(true, var13);
         }
      } catch (RuntimeException var14) {
         throw Class44.method1067(var14, "nh.V(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ',' + var9 + ',' + var10 + ',' + var11 + ',' + var12 + ')');
      }
   }

   int[][] method166(int var1, int var2) {
      try {
         if(var1 != -1) {
            this.method159(32);
         }

         int[][] var3 = this.aClass97_2376.method1594((byte)65, var2);
         if(this.aClass97_2376.aBoolean1379 && this.method279(-113)) {
            int[] var4 = var3[0];
            int[] var5 = var3[1];
            int[] var6 = var3[2];
            int var7 = (~this.anInt3283 == ~Class101.anInt1427?var2:this.anInt3283 * var2 / Class101.anInt1427) * this.anInt3280;
            int var8;
            int var9;
            if(Class113.anInt1559 == this.anInt3280) {
               for(var8 = 0; ~Class113.anInt1559 < ~var8; ++var8) {
                  var9 = this.anIntArray3284[var7++];
                  var6[var8] = Class3_Sub28_Sub15.method633(255, var9) << 4;
                  var5[var8] = Class3_Sub28_Sub15.method633('\uff00', var9) >> 4;
                  var4[var8] = Class3_Sub28_Sub15.method633(var9, 16711680) >> 12;
               }
            } else {
               for(var8 = 0; Class113.anInt1559 > var8; ++var8) {
                  var9 = this.anInt3280 * var8 / Class113.anInt1559;
                  int var10 = this.anIntArray3284[var7 - -var9];
                  var6[var8] = Class3_Sub28_Sub15.method633(var10 << 4, 4080);
                  var5[var8] = Class3_Sub28_Sub15.method633(var10, '\uff00') >> 4;
                  var4[var8] = Class3_Sub28_Sub15.method633(var10 >> 12, 4080);
               }
            }
         }

         return var3;
      } catch (RuntimeException var11) {
         throw Class44.method1067(var11, "nh.T(" + var1 + ',' + var2 + ')');
      }
   }

   final void method157(int var1, RSByteBuffer var2, boolean var3) {
      try {
         if(var1 == 0) {
            this.anInt3278 = var2.getShort(1);
         }

         if(!var3) {
            method276(115, 107, 22, 20, (GameObject)null, 87L, false);
         }

      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "nh.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   static final void method281(byte var0, int var1, int var2) {
      try {
         Class57.anIntArray898[var2] = var1;
         if(var0 != 99) {
            aClass94_3279 = (RSString)null;
         }

         Class3_Sub7 var3 = (Class3_Sub7)Class3_Sub28_Sub15.aClass130_3679.method1780((long)var2, 0);
         if(null != var3) {
            if(var3.aLong2295 != 4611686018427387905L) {
               var3.aLong2295 = Class5.method830((byte)-55) + 500L | 4611686018427387904L;
            }
         } else {
            var3 = new Class3_Sub7(4611686018427387905L);
            Class3_Sub28_Sub15.aClass130_3679.method1779(1, var3, (long)var2);
         }

      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "nh.W(" + var0 + ',' + var1 + ',' + var2 + ')');
      }
   }

   final void method161(byte var1) {
      try {
         super.method161(var1);
         this.anIntArray3284 = null;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "nh.BA(" + var1 + ')');
      }
   }

}
