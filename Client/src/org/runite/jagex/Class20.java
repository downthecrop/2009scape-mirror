package org.runite.jagex;

final class Class20 {

   static RSString aClass94_434 = RSString.createRSString("Shift)2click ENABLED(Q");
   static short[][] aShortArrayArray435 = new short[][]{{(short)6554, (short)115, (short)10304, (short)28, (short)5702, (short)7756, (short)5681, (short)4510, (short)-31835, (short)22437, (short)2859, (short)-11339, (short)16, (short)5157, (short)10446, (short)3658, (short)-27314, (short)-21965, (short)472, (short)580, (short)784, (short)21966, (short)28950, (short)-15697, (short)-14002}, {(short)9104, (short)10275, (short)7595, (short)3610, (short)7975, (short)8526, (short)918, (short)-26734, (short)24466, (short)10145, (short)-6882, (short)5027, (short)1457, (short)16565, (short)-30545, (short)25486, (short)24, (short)5392, (short)10429, (short)3673, (short)-27335, (short)-21957, (short)192, (short)687, (short)412, (short)21821, (short)28835, (short)-15460, (short)-14019}, new short[0], new short[0], new short[0]};
   static RSString aClass94_436 = RSString.createRSString("Cache:");
   static int anInt437;
   static boolean aBoolean438 = false;
   static RSInterface aClass11_439;


   static final boolean method907(int var0, int var1, int var2, int var3, int var4, GameObject var5, int var6, long var7, boolean var9) {
      if(var5 == null) {
         return true;
      } else {
         int var10 = var1 - var4;
         int var11 = var2 - var4;
         int var12 = var1 + var4;
         int var13 = var2 + var4;
         if(var9) {
            if(var6 > 640 && var6 < 1408) {
               var13 += 128;
            }

            if(var6 > 1152 && var6 < 1920) {
               var12 += 128;
            }

            if(var6 > 1664 || var6 < 384) {
               var11 -= 128;
            }

            if(var6 > 128 && var6 < 896) {
               var10 -= 128;
            }
         }

         var10 /= 128;
         var11 /= 128;
         var12 /= 128;
         var13 /= 128;
         return Class56.method1189(var0, var10, var11, var12 - var10 + 1, var13 - var11 + 1, var1, var2, var3, var5, var6, true, var7);
      }
   }

   public static void method908(int var0) {
      try {
         aClass11_439 = null;
         if(var0 != 5157) {
            aClass94_434 = (RSString)null;
         }

         aClass94_436 = null;
         aClass94_434 = null;
         aShortArrayArray435 = (short[][])null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "dg.A(" + var0 + ')');
      }
   }

   static final void method909(int var0, RSInterface var1) {
      try {
         if(~Class3_Sub23.anInt2535 == ~var1.anInt204) {
            Class3_Sub28_Sub14.aBooleanArray3674[var1.anInt292] = true;
         }

         int var2 = -100 / ((var0 - 55) / 52);
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "dg.C(" + var0 + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

   static final void method910(int var0, int var1, int var2, int var3, int var4, int var5, Class91 var6) {
      try {
         boolean var9 = true;
         long var7 = 0L;
         if(var4 == 0) {
            var7 = Class157.method2174(var3, var2, var1);
         } else if(var4 != 1) {
            if(~var4 == -3) {
               var7 = Class3_Sub28_Sub5.method557(var3, var2, var1);
            } else if(3 == var4) {
               var7 = Class3_Sub2.method104(var3, var2, var1);
            }
         } else {
            var7 = Class80.method1395(var3, var2, var1);
         }

         boolean var10 = false;
         int var19 = (519128 & (int)var7) >> 14;
         if(var0 < -94) {
            boolean var11 = false;
            int var17 = (int)(var7 >>> 32) & Integer.MAX_VALUE;
            ObjectDefinition var12 = Class162.getObjectDefinition(4, var17);
            if(var12.method1690(28933)) {
               Class140_Sub6.method2020(var2, var12, (byte)-73, var1, var3);
            }

            int var18 = ((int)var7 & 4109484) >> 20;
            if(~var7 != -1L) {
               GameObject var13 = null;
               GameObject var14 = null;
               if(0 == var4) {
                  Class70 var15 = Class61.method1209(var3, var2, var1);
                  if(null != var15) {
                     var13 = var15.aClass140_1049;
                     var14 = var15.aClass140_1052;
                  }

                  if(-1 != ~var12.actionCount) {
                     var6.method1485(var18, var12.aBoolean1486, -104, var1, var19, var2);
                  }
               } else if(var4 != 1) {
                  if(2 == var4) {
                     Class25 var20 = Class163_Sub2.method2217(var3, var2, var1);
                     if(null != var20) {
                        var13 = var20.aClass140_479;
                     }

                     if(var12.actionCount != 0 && var12.anInt1480 + var2 < 104 && -105 < ~(var12.anInt1480 + var1) && 104 > var2 + var12.anInt1485 && ~(var1 + var12.anInt1485) > -105) {
                        var6.method1502(20851, var2, var12.anInt1480, var12.aBoolean1486, var18, var12.anInt1485, var1);
                     }
                  } else if(~var4 == -4) {
                     Class12 var22 = Class159.method2193(var3, var2, var1);
                     if(var22 != null) {
                        var13 = var22.object;
                     }

                     if(var12.actionCount == 1) {
                        var6.method1499(var1, (byte)-73, var2);
                     }
                  }
               } else {
                  Class19 var21 = Class39.method1037(var3, var2, var1);
                  if(var21 != null) {
                     var13 = var21.aClass140_429;
                     var14 = var21.aClass140_423;
                  }
               }

               if(HDToolKit.highDetail && var12.aBoolean1503) {
                  if(2 != var19) {
                     if(5 != var19) {
                        if(-7 == ~var19) {
                           if(var13 instanceof Class140_Sub3) {
                              ((Class140_Sub3)var13).method1960(-1);
                           } else {
                              Class8.method840(var12, (byte)-28, 8 * Class163_Sub3.anIntArray3007[var18], 4 - -var18, 8 * Class3_Sub13.anIntArray2386[var18], 4, var2, var1, var5);
                           }
                        } else if(-8 == ~var19) {
                           if(var13 instanceof Class140_Sub3) {
                              ((Class140_Sub3)var13).method1960(-1);
                           } else {
                              Class8.method840(var12, (byte)-120, 0, 4 - -(3 & 2 + var18), 0, 4, var2, var1, var5);
                           }
                        } else if(var19 == 8) {
                           if(!(var13 instanceof Class140_Sub3)) {
                              Class8.method840(var12, (byte)-45, Class163_Sub3.anIntArray3007[var18] * 8, var18 + 4, 8 * Class3_Sub13.anIntArray2386[var18], 4, var2, var1, var5);
                           } else {
                              ((Class140_Sub3)var13).method1960(-1);
                           }

                           if(var14 instanceof Class140_Sub3) {
                              ((Class140_Sub3)var14).method1960(-1);
                           } else {
                              Class8.method840(var12, (byte)-24, Class163_Sub3.anIntArray3007[var18] * 8, 4 - -(3 & 2 + var18), Class3_Sub13.anIntArray2386[var18] * 8, 4, var2, var1, var5);
                           }
                        } else if(11 != var19) {
                           if(var13 instanceof Class140_Sub3) {
                              ((Class140_Sub3)var13).method1960(-1);
                           } else {
                              Class8.method840(var12, (byte)-113, 0, var18, 0, var19, var2, var1, var5);
                           }
                        } else if(var13 instanceof Class140_Sub3) {
                           ((Class140_Sub3)var13).method1960(-1);
                        } else {
                           Class8.method840(var12, (byte)-115, 0, 4 + var18, 0, 10, var2, var1, var5);
                        }
                     } else if(var13 instanceof Class140_Sub3) {
                        ((Class140_Sub3)var13).method1960(-1);
                     } else {
                        Class8.method840(var12, (byte)-119, Class3_Sub24_Sub3.anIntArray3491[var18] * 8, var18, RenderAnimationDefinition.anIntArray356[var18] * 8, 4, var2, var1, var5);
                     }
                  } else {
                     if(var13 instanceof Class140_Sub3) {
                        ((Class140_Sub3)var13).method1960(-1);
                     } else {
                        Class8.method840(var12, (byte)-76, 0, var18 + 4, 0, var19, var2, var1, var5);
                     }

                     if(var14 instanceof Class140_Sub3) {
                        ((Class140_Sub3)var14).method1960(-1);
                     } else {
                        Class8.method840(var12, (byte)-100, 0, 3 & var18 - -1, 0, var19, var2, var1, var5);
                     }
                  }
               }
            }

         }
      } catch (RuntimeException var16) {
         throw Class44.method1067(var16, "dg.B(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + (var6 != null?"{...}":"null") + ')');
      }
   }

}
