package org.runite.jagex;

final class Class3_Sub1 extends Class3 {

   int anInt2202;
   static CacheIndex interfaceScriptsIndex;
   static int anInt2204;
   int anInt2205;
   private static RSString aClass94_2206 = RSString.createRSString("white:");
   static RSString aClass94_2207 = RSString.createRSString("brillant2:");
   static int anInt2208 = -1;
   static int[] anIntArray2209 = new int[]{2, 2, 4, 2, 1, 8, 4, 1, 4, 4, 2, 1, 1, 1, 4, 1};
   static RSString aClass94_2210 = aClass94_2206;
   static int localIndex = -1;
   static int anInt2212 = 0;
   static int[] anIntArray2213 = new int[]{16776960, 16711680, '\uff00', '\uffff', 16711935, 16777215};
   static RSString aClass94_2214 = aClass94_2206;


   static final void method90(int var0) {
      try {
         if(HDToolKit.highDetail) {
            if(!Class3_Sub13_Sub34.aBoolean3416) {
               Class3_Sub2[][][] var1 = Class75_Sub2.aClass3_Sub2ArrayArrayArray2638;
               if(var0 != 1) {
                  aClass94_2214 = (RSString)null;
               }

               for(int var2 = 0; var2 < var1.length; ++var2) {
                  Class3_Sub2[][] var3 = var1[var2];

                  for(int var4 = 0; ~var3.length < ~var4; ++var4) {
                     for(int var5 = 0; var3[var4].length > var5; ++var5) {
                        Class3_Sub2 var6 = var3[var4][var5];
                        if(var6 != null) {
                           Class140_Sub1_Sub1 var7;
                           if(var6.aClass12_2230 != null && var6.aClass12_2230.object instanceof Class140_Sub1_Sub1) {
                              var7 = (Class140_Sub1_Sub1)var6.aClass12_2230.object;
                              if(~(var6.aClass12_2230.aLong328 & Long.MIN_VALUE) == -1L) {
                                 var7.method1920(false, true, true, true, false, true, true);
                              } else {
                                 var7.method1920(true, true, true, true, true, true, true);
                              }
                           }

                           if(null != var6.aClass19_2233) {
                              if(var6.aClass19_2233.aClass140_429 instanceof Class140_Sub1_Sub1) {
                                 var7 = (Class140_Sub1_Sub1)var6.aClass19_2233.aClass140_429;
                                 if(0L == (var6.aClass19_2233.aLong428 & Long.MIN_VALUE)) {
                                    var7.method1920(false, true, true, true, false, true, true);
                                 } else {
                                    var7.method1920(true, true, true, true, true, true, true);
                                 }
                              }

                              if(var6.aClass19_2233.aClass140_423 instanceof Class140_Sub1_Sub1) {
                                 var7 = (Class140_Sub1_Sub1)var6.aClass19_2233.aClass140_423;
                                 if(-1L != ~(Long.MIN_VALUE & var6.aClass19_2233.aLong428)) {
                                    var7.method1920(true, true, true, true, true, true, true);
                                 } else {
                                    var7.method1920(false, true, true, true, false, true, true);
                                 }
                              }
                           }

                           if(var6.aClass70_2234 != null) {
                              if(var6.aClass70_2234.aClass140_1049 instanceof Class140_Sub1_Sub1) {
                                 var7 = (Class140_Sub1_Sub1)var6.aClass70_2234.aClass140_1049;
                                 if(-1L != ~(var6.aClass70_2234.aLong1048 & Long.MIN_VALUE)) {
                                    var7.method1920(true, true, true, true, true, true, true);
                                 } else {
                                    var7.method1920(false, true, true, true, false, true, true);
                                 }
                              }

                              if(var6.aClass70_2234.aClass140_1052 instanceof Class140_Sub1_Sub1) {
                                 var7 = (Class140_Sub1_Sub1)var6.aClass70_2234.aClass140_1052;
                                 if(~(Long.MIN_VALUE & var6.aClass70_2234.aLong1048) != -1L) {
                                    var7.method1920(true, true, true, true, true, true, true);
                                 } else {
                                    var7.method1920(false, true, true, true, false, true, true);
                                 }
                              }
                           }

                           for(int var10 = 0; ~var10 > ~var6.anInt2223; ++var10) {
                              if(var6.aClass25Array2221[var10].aClass140_479 instanceof Class140_Sub1_Sub1) {
                                 Class140_Sub1_Sub1 var8 = (Class140_Sub1_Sub1)var6.aClass25Array2221[var10].aClass140_479;
                                 if(-1L == ~(Long.MIN_VALUE & var6.aClass25Array2221[var10].aLong498)) {
                                    var8.method1920(false, true, true, true, false, true, true);
                                 } else {
                                    var8.method1920(true, true, true, true, true, true, true);
                                 }
                              }
                           }
                        }
                     }
                  }
               }

               Class3_Sub13_Sub34.aBoolean3416 = true;
            }
         }
      } catch (RuntimeException var9) {
         throw Class44.method1067(var9, "bf.D(" + var0 + ')');
      }
   }

   public static void method91(byte var0) {
      try {
         anIntArray2213 = null;
         if(var0 <= 110) {
            method90(-74);
         }

         interfaceScriptsIndex = null;
         anIntArray2209 = null;
         aClass94_2206 = null;
         aClass94_2210 = null;
         aClass94_2207 = null;
         aClass94_2214 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "bf.P(" + var0 + ')');
      }
   }

   final boolean method92(int var1, byte var2) {
      try {
         int var3 = 13 / ((-60 - var2) / 46);
         return 0 != (this.anInt2205 >> 1 + var1 & 1);
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "bf.C(" + var1 + ',' + var2 + ')');
      }
   }

   final boolean method93(int var1) {
      try {
         return var1 != 572878952?true:0 != (572878952 & this.anInt2205) >> 29;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "bf.S(" + var1 + ')');
      }
   }

   final int method94(byte var1) {
      try {
         if(var1 != -74) {
            this.method96(90);
         }

         return this.anInt2205 >> 18 & 7;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "bf.A(" + var1 + ')');
      }
   }

   final boolean method95(int var1) {
      try {
         if(var1 != -13081) {
            anInt2212 = 71;
         }

         return ~(1 & this.anInt2205) != -1;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "bf.Q(" + var1 + ')');
      }
   }

   final boolean method96(int var1) {
      try {
         return var1 != -2063688673?false:(this.anInt2205 >> 31 & 1) != 0;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "bf.T(" + var1 + ')');
      }
   }

   final boolean method97(int var1) {
      try {
         return var1 != -20710?true:0 != (1 & this.anInt2205 >> 22);
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "bf.R(" + var1 + ')');
      }
   }

   Class3_Sub1(int var1, int var2) {
      try {
         this.anInt2202 = var2;
         this.anInt2205 = var1;
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "bf.<init>(" + var1 + ',' + var2 + ')');
      }
   }

   final boolean method98(boolean var1) {
      try {
         if(var1) {
            anIntArray2209 = (int[])null;
         }

         return -1 != ~(this.anInt2205 >> 21 & 1);
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "bf.E(" + var1 + ')');
      }
   }

   final boolean method99(int var1) {
      try {
         if(var1 != 31595) {
            interfaceScriptsIndex = (CacheIndex)null;
         }

         return -1 != ~((1738913629 & this.anInt2205) >> 30);
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "bf.F(" + var1 + ')');
      }
   }

   final boolean method100(byte var1) {
      try {
         return var1 != -9?true:(this.anInt2205 & 455226656) >> 28 != 0;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "bf.O(" + var1 + ')');
      }
   }

   final int method101(int var1) {
      try {
         if(var1 > -51) {
            anInt2208 = -42;
         }

         return Class3_Sub28_Sub15.method630((byte)-34, this.anInt2205);
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "bf.B(" + var1 + ')');
      }
   }

}
