package org.runite.jagex;

final class Class3_Sub13_Sub29 extends Class3_Sub13 {

   static int anInt3356;
   static RSString aClass94_3357 = RSString.createRSString("");
   static boolean disableGEBoxes = false;
   static int[] anIntArray3359 = new int[5];
   static RSString aClass94_3360 = RSString.createRSString("mem=");
   static CacheIndex aClass153_3361;


   final int[] method154(int var1, byte var2) {
      try {
         int[] var3 = this.aClass114_2382.method1709(-16409, var1);
         if(this.aClass114_2382.aBoolean1580) {
            Class76.method1359(var3, 0, Class113.anInt1559, Class163_Sub3.anIntArray2999[var1]);
         }

         int var4 = -123 % ((30 - var2) / 36);
         return var3;
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "qg.D(" + var1 + ',' + var2 + ')');
      }
   }

   static final void method304(int var0) {
      try {
         if(var0 != 6799) {
            method305((Signlink)null, (RSByteBuffer)null, -13, (byte)41);
         }

         Class3_Sub13_Sub34.aClass93_3412.method1524(3);
         Class3_Sub13_Sub31.aClass93_3369.method1524(3);
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "qg.F(" + var0 + ')');
      }
   }

   static final void method305(Signlink var0, RSByteBuffer var1, int var2, byte var3) {
      try {
         int var5 = 123 % ((var3 - -66) / 57);
         Class3_Sub8 var4 = new Class3_Sub8();
         var4.anInt2296 = var1.getByte((byte)-54);
         var4.anInt2305 = var1.getInt();
         var4.aClass64Array2298 = new Class64[var4.anInt2296];
         var4.anIntArray2300 = new int[var4.anInt2296];
         var4.aByteArrayArrayArray2302 = new byte[var4.anInt2296][][];
         var4.aClass64Array2303 = new Class64[var4.anInt2296];
         var4.anIntArray2301 = new int[var4.anInt2296];
         var4.anIntArray2299 = new int[var4.anInt2296];

         for(int var6 = 0; ~var6 > ~var4.anInt2296; ++var6) {
            try {
               int var7 = var1.getByte((byte)-50);
               String var8;
               String var9;
               int var10;
               if(~var7 != -1 && var7 != 1 && -3 != ~var7) {
                  if(-4 == ~var7 || -5 == ~var7) {
                     var8 = new String(var1.getString().method1568(0));
                     var9 = new String(var1.getString().method1568(0));
                     var10 = var1.getByte((byte)-113);
                     String[] var11 = new String[var10];

                     for(int var12 = 0; var10 > var12; ++var12) {
                        var11[var12] = new String(var1.getString().method1568(0));
                     }

                     byte[][] var21 = new byte[var10][];
                     int var14;
                     if(3 == var7) {
                        for(int var13 = 0; ~var10 < ~var13; ++var13) {
                           var14 = var1.getInt();
                           var21[var13] = new byte[var14];
                           var1.method764(0, var14, var21[var13], (byte)93);
                        }
                     }

                     var4.anIntArray2301[var6] = var7;
                     Class[] var22 = new Class[var10];

                     for(var14 = 0; var10 > var14; ++var14) {
                        var22[var14] = Class3_Sub13_Sub1.method170(6092, var11[var14]);
                     }

                     var4.aClass64Array2298[var6] = var0.method1443(Class3_Sub13_Sub1.method170(6092, var8), var22, -80, var9);
                     var4.aByteArrayArrayArray2302[var6] = var21;
                  }
               } else {
                  var8 = new String(var1.getString().method1568(0));
                  var10 = 0;
                  var9 = new String(var1.getString().method1568(0));
                  if(var7 == 1) {
                     var10 = var1.getInt();
                  }

                  var4.anIntArray2301[var6] = var7;
                  var4.anIntArray2299[var6] = var10;
                  var4.aClass64Array2303[var6] = var0.method1447(-41, var9, Class3_Sub13_Sub1.method170(6092, var8));
               }
            } catch (ClassNotFoundException var15) {
               var4.anIntArray2300[var6] = -1;
            } catch (SecurityException var16) {
               var4.anIntArray2300[var6] = -2;
            } catch (NullPointerException var17) {
               var4.anIntArray2300[var6] = -3;
            } catch (Exception var18) {
               var4.anIntArray2300[var6] = -4;
            } catch (Throwable var19) {
               var4.anIntArray2300[var6] = -5;
            }
         }

         Class3_Sub26.aClass61_2557.method1215(true, var4);
      } catch (RuntimeException var20) {
         throw Class44.method1067(var20, "qg.E(" + (var0 != null?"{...}":"null") + ',' + (var1 != null?"{...}":"null") + ',' + var2 + ',' + var3 + ')');
      }
   }

   static final void method306(int var0, boolean var1, int var2) {
      try {
         Class79 var3 = CS2Script.method378(var0, (byte)127);
         int var6 = var3.anInt1125;
         if(!var1) {
            int var5 = var3.anInt1123;
            int var4 = var3.anInt1128;
            int var7 = Class3_Sub6.anIntArray2288[var6 - var5];
            if(~var2 > -1 || ~var2 < ~var7) {
               var2 = 0;
            }

            var7 <<= var5;
            Class86.method1428(var4, 72, var7 & var2 << var5 | Class163_Sub1.anIntArray2985[var4] & ~var7);
         }
      } catch (RuntimeException var8) {
         throw Class44.method1067(var8, "qg.Q(" + var0 + ',' + var1 + ',' + var2 + ')');
      }
   }

   static final void method307(RSString[] var0, short[] var1, int var2) {
      try {
         Class3_Sub8.method127(var1, -1 + var0.length, var0, -909, 0);
         int var3 = 91 % ((var2 - -1) / 63);
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "qg.C(" + (var0 != null?"{...}":"null") + ',' + (var1 != null?"{...}":"null") + ',' + var2 + ')');
      }
   }

   static int bitwiseOr(int var0, int var1) {
      try {
         return var0 | var1;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "qg.R(" + var0 + ',' + var1 + ')');
      }
   }

   public static void method309(boolean var0) {
      try {
         anIntArray3359 = null;
         if(var0) {
            aClass153_3361 = null;
            aClass94_3357 = null;
            aClass94_3360 = null;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "qg.S(" + var0 + ')');
      }
   }

   public Class3_Sub13_Sub29() {
      super(0, true);
   }

   static final int method310(int var0, byte var1, int var2, int var3) {
      try {
         var0 &= 3;
         if(~var0 != -1) {
            if(var1 >= -17) {
               aClass94_3357 = (RSString)null;
            }

            return var0 == 1?7 + -var2:(~var0 == -3?-var3 + 7:var2);
         } else {
            return var3;
         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "qg.B(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   static final Class55 method311(int var0, int var1) {
      try {
         Class55 var2 = (Class55)Class41.aClass93_684.get((long)var0, (byte)121);
         if(var2 != null) {
            return var2;
         } else {
            byte[] var3 = Class3_Sub13_Sub19.aClass153_3227.getFile(33, (byte)-122, var0);
            if(var1 != 5) {
               aClass94_3357 = (RSString)null;
            }

            var2 = new Class55();
            if(var3 != null) {
               var2.method1182(new RSByteBuffer(var3), var0, (byte)85);
            }

            Class41.aClass93_684.put((byte)-112, var2, (long)var0);
            return var2;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "qg.O(" + var0 + ',' + var1 + ')');
      }
   }

}
