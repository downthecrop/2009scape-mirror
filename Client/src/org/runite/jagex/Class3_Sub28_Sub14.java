package org.runite.jagex;

final class Class3_Sub28_Sub14 extends Node {

   static boolean aBoolean3668 = false;
   static byte[][] aByteArrayArray3669;
   static int anInt3670;
   static int anInt3671 = 0;
   static RSString aClass94_3672 = RSString.createRSString("");
   static RSString password = aClass94_3672;
   static boolean[] aBooleanArray3674 = new boolean[100];
   static RSString username = aClass94_3672;
   Class140_Sub7 aClass140_Sub7_3676;
   static int anInt3677;


   static final void method626(int var0) {
      try {
         if(null != RSByteBuffer.aClass3_Sub28_Sub3_2600) {
            if(Class140_Sub3.anInt2737 < 10) {
               if(!Class3_Sub13_Sub17.aClass153_3210.method2127((byte)-83, RSByteBuffer.aClass3_Sub28_Sub3_2600.aClass94_3561)) {
                  Class140_Sub3.anInt2737 = Class133.aClass153_1751.method2116(22813, RSByteBuffer.aClass3_Sub28_Sub3_2600.aClass94_3561) / 10;
                  return;
               }

               Class3_Sub13_Sub1.method169(var0 ^ 22166);
               Class140_Sub3.anInt2737 = 10;
            }

            if(~Class140_Sub3.anInt2737 == -11) {
               Class3_Sub13_Sub21.anInt3256 = RSByteBuffer.aClass3_Sub28_Sub3_2600.anInt3555 >> 6 << 6;
               Class2.anInt65 = RSByteBuffer.aClass3_Sub28_Sub3_2600.anInt3562 >> 6 << 6;
               Class108.anInt1460 = (RSByteBuffer.aClass3_Sub28_Sub3_2600.anInt3549 >> 6 << 6) - Class2.anInt65 + 64;
               Class23.anInt455 = 64 + (RSByteBuffer.aClass3_Sub28_Sub3_2600.anInt3559 >> 6 << 6) + -Class3_Sub13_Sub21.anInt3256;
               if(RSByteBuffer.aClass3_Sub28_Sub3_2600.anInt3563 != 37) {
                  if(~RSByteBuffer.aClass3_Sub28_Sub3_2600.anInt3563 != -51) {
                     if(75 == RSByteBuffer.aClass3_Sub28_Sub3_2600.anInt3563) {
                        Class44.aFloat727 = 6.0F;
                        NPC.aFloat3979 = 6.0F;
                     } else if(-101 != ~RSByteBuffer.aClass3_Sub28_Sub3_2600.anInt3563) {
                        if(200 == RSByteBuffer.aClass3_Sub28_Sub3_2600.anInt3563) {
                           Class44.aFloat727 = 16.0F;
                           NPC.aFloat3979 = 16.0F;
                        } else {
                           Class44.aFloat727 = 8.0F;
                           NPC.aFloat3979 = 8.0F;
                        }
                     } else {
                        Class44.aFloat727 = 8.0F;
                        NPC.aFloat3979 = 8.0F;
                     }
                  } else {
                     Class44.aFloat727 = 4.0F;
                     NPC.aFloat3979 = 4.0F;
                  }
               } else {
                  Class44.aFloat727 = 3.0F;
                  NPC.aFloat3979 = 3.0F;
               }

               int var1 = -Class3_Sub13_Sub21.anInt3256 + (Class102.player.anInt2819 >> 7) + Class131.anInt1716;
               var1 += -5 + (int)(Math.random() * 10.0D);
               int var2 = -Class82.anInt1152 + -(Class102.player.anInt2829 >> 7) + Class2.anInt65 + -1 + Class108.anInt1460;
               var2 += -5 + (int)(Math.random() * 10.0D);
               if(-1 >= ~var1 && ~Class23.anInt455 < ~var1 && 0 <= var2 && ~var2 > ~Class108.anInt1460) {
                  Class3_Sub28_Sub1.anInt3536 = var1;
                  Class3_Sub4.anInt2251 = var2;
               } else {
                  Class3_Sub4.anInt2251 = Class2.anInt65 - RSByteBuffer.aClass3_Sub28_Sub3_2600.anInt3556 * 64 + Class108.anInt1460 + -1;
                  Class3_Sub28_Sub1.anInt3536 = RSByteBuffer.aClass3_Sub28_Sub3_2600.anInt3558 * 64 + -Class3_Sub13_Sub21.anInt3256;
               }

               Class3_Sub5.method117((byte)87);
               Class84.anIntArray1161 = new int[1 + Class54.anInt869];
               int var4 = Class108.anInt1460 >> 6;
               int var3 = Class23.anInt455 >> 6;
               Class3_Sub10.aByteArrayArrayArray2339 = new byte[var3][var4][];
               int var5 = Class3_Sub13_Sub14.anInt3158 >> 2 << 10;
               Class36.aByteArrayArrayArray640 = new byte[var3][var4][];
               Class44.anIntArrayArrayArray720 = new int[var3][var4][];
               RenderAnimationDefinition.aByteArrayArrayArray383 = new byte[var3][var4][];
               Class146.anIntArrayArrayArray1903 = new int[var3][var4][];
               Class3_Sub13_Sub33.aByteArrayArrayArray3390 = new byte[var3][var4][];
               int var6 = AbstractIndexedSprite.anInt1463 >> 1;
               CS2Script.aByteArrayArrayArray2452 = new byte[var3][var4][];
               Class29.anIntArrayArrayArray558 = new int[var3][var4][];
               Class36.method1014(-120, var6, var5);
               Class140_Sub3.anInt2737 = 20;
            } else if(Class140_Sub3.anInt2737 != 20) {
               if(var0 != 64) {
                  method628(-40, -12, 40, (Player)null);
               }

               if(Class140_Sub3.anInt2737 == 30) {
                  Class163_Sub2.method2219(new RSByteBuffer(Class3_Sub13_Sub17.aClass153_3210.method2123(0, PacketParser.aClass94_85, RSByteBuffer.aClass3_Sub28_Sub3_2600.aClass94_3561)), false);
                  Class140_Sub3.anInt2737 = 40;
                  Class75_Sub4.method1355(true);
               } else if(-41 != ~Class140_Sub3.anInt2737) {
                  if(Class140_Sub3.anInt2737 != 50) {
                     if(-61 == ~Class140_Sub3.anInt2737) {
                        if(!Class3_Sub13_Sub17.aClass153_3210.method2135(RenderAnimationDefinition.method903(new RSString[]{RSByteBuffer.aClass3_Sub28_Sub3_2600.aClass94_3561, Class157.aClass94_2003}, (byte)-107), -106)) {
                           Class119.aClass131_1624 = new Class131(0);
                        } else {
                           if(!Class3_Sub13_Sub17.aClass153_3210.method2127((byte)-83, RenderAnimationDefinition.method903(new RSString[]{RSByteBuffer.aClass3_Sub28_Sub3_2600.aClass94_3561, Class157.aClass94_2003}, (byte)-71))) {
                              return;
                           }

                           Class119.aClass131_1624 = Class81.method1403(var0 + -120, RenderAnimationDefinition.method903(new RSString[]{RSByteBuffer.aClass3_Sub28_Sub3_2600.aClass94_3561, Class157.aClass94_2003}, (byte)-91), Class3_Sub13_Sub17.aClass153_3210);
                        }

                        Class140_Sub3.anInt2737 = 70;
                        Class75_Sub4.method1355(true);
                     } else if(Class140_Sub3.anInt2737 == 70) {
                        Class164_Sub2.aClass33_3019 = new Class33(11, true, Class3_Sub28_Sub12.aCanvas3648);
                        Class140_Sub3.anInt2737 = 73;
                        Class163_Sub1.method2210((byte)-90, true);
                        Class75_Sub4.method1355(true);
                     } else if(Class140_Sub3.anInt2737 != 73) {
                        if(~Class140_Sub3.anInt2737 == -77) {
                           Class99.aClass33_1399 = new Class33(14, true, Class3_Sub28_Sub12.aCanvas3648);
                           Class140_Sub3.anInt2737 = 79;
                           Class163_Sub1.method2210((byte)-90, true);
                           Class75_Sub4.method1355(true);
                        } else if(-80 == ~Class140_Sub3.anInt2737) {
                           Class75_Sub2.aClass33_2637 = new Class33(17, true, Class3_Sub28_Sub12.aCanvas3648);
                           Class140_Sub3.anInt2737 = 82;
                           Class163_Sub1.method2210((byte)-90, true);
                           Class75_Sub4.method1355(true);
                        } else if(~Class140_Sub3.anInt2737 == -83) {
                           Class119.aClass33_1626 = new Class33(19, true, Class3_Sub28_Sub12.aCanvas3648);
                           Class140_Sub3.anInt2737 = 85;
                           Class163_Sub1.method2210((byte)-90, true);
                           Class75_Sub4.method1355(true);
                        } else if(85 == Class140_Sub3.anInt2737) {
                           Class75_Sub2.aClass33_2648 = new Class33(22, true, Class3_Sub28_Sub12.aCanvas3648);
                           Class140_Sub3.anInt2737 = 88;
                           Class163_Sub1.method2210((byte)-90, true);
                           Class75_Sub4.method1355(true);
                        } else if(88 != Class140_Sub3.anInt2737) {
                           Class91.aClass33_1305 = new Class33(30, true, Class3_Sub28_Sub12.aCanvas3648);
                           Class140_Sub3.anInt2737 = 100;
                           Class163_Sub1.method2210((byte)-90, true);
                           Class75_Sub4.method1355(true);
                           System.gc();
                        } else {
                           Class161.aClass33_2034 = new Class33(26, true, Class3_Sub28_Sub12.aCanvas3648);
                           Class140_Sub3.anInt2737 = 91;
                           Class163_Sub1.method2210((byte)-90, true);
                           Class75_Sub4.method1355(true);
                        }
                     } else {
                        IOHandler.aClass33_1238 = new Class33(12, true, Class3_Sub28_Sub12.aCanvas3648);
                        Class140_Sub3.anInt2737 = 76;
                        Class163_Sub1.method2210((byte)-90, true);
                        Class75_Sub4.method1355(true);
                     }
                  } else {
                     Class96.method1587((byte)-83, new RSByteBuffer(Class3_Sub13_Sub17.aClass153_3210.method2123(var0 + -64, Class19.aClass94_422, RSByteBuffer.aClass3_Sub28_Sub3_2600.aClass94_3561)));
                     Class140_Sub3.anInt2737 = 60;
                     Class163_Sub1.method2210((byte)-90, true);
                     Class75_Sub4.method1355(true);
                  }
               } else {
                  Class3_Sub13_Sub34.method328(-21774, new RSByteBuffer(Class3_Sub13_Sub17.aClass153_3210.method2123(0, Class3_Sub13_Sub11.aClass94_3140, RSByteBuffer.aClass3_Sub28_Sub3_2600.aClass94_3561)));
                  Class140_Sub3.anInt2737 = 50;
                  Class75_Sub4.method1355(true);
               }
            } else {
               Class15.method889((byte)105, new RSByteBuffer(Class3_Sub13_Sub17.aClass153_3210.method2123(0, Class96.aClass94_1349, RSByteBuffer.aClass3_Sub28_Sub3_2600.aClass94_3561)));
               Class140_Sub3.anInt2737 = 30;
               Class163_Sub1.method2210((byte)-90, true);
               Class75_Sub4.method1355(true);
            }
         }
      } catch (RuntimeException var7) {
         throw Class44.method1067(var7, "pa.B(" + var0 + ')');
      }
   }

   Class3_Sub28_Sub14(Class140_Sub7 var1) {
      try {
         this.aClass140_Sub7_3676 = var1;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "pa.<init>(" + (var1 != null?"{...}":"null") + ')');
      }
   }

   public static void method627(byte var0) {
      try {
         if(var0 <= -112) {
            username = null;
            password = null;
            aByteArrayArray3669 = (byte[][])null;
            aClass94_3672 = null;
            aBooleanArray3674 = null;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "pa.A(" + var0 + ')');
      }
   }

   static final void method628(int var0, int var1, int var2, Player var3) {
      try {
         if(var0 != 0) {
            aBooleanArray3674 = (boolean[])null;
         }

         if(~var2 == ~var3.anInt2771 && 0 != ~var2) {
            AnimationDefinition var4 = Client.getAnimationDefinition(var2, (byte)-20);
            int var5 = var4.anInt1845;
            if(1 == var5) {
               var3.anInt2828 = var1;
               var3.anInt2760 = 0;
               var3.anInt2776 = 1;
               var3.anInt2832 = 0;
               var3.anInt2773 = 0;
               IOHandler.method1470(var3.anInt2829, var4, var0 + 183921384, var3.anInt2819, Class102.player == var3, var3.anInt2832);
            }

            if(var5 == 2) {
               var3.anInt2773 = 0;
            }
         } else if(-1 == var2 || var3.anInt2771 == -1 || Client.getAnimationDefinition(var2, (byte)-20).anInt1857 >= Client.getAnimationDefinition(var3.anInt2771, (byte)-20).anInt1857) {
            var3.anInt2776 = 1;
            var3.anInt2832 = 0;
            var3.anInt2828 = var1;
            var3.anInt2811 = var3.anInt2816;
            var3.anInt2773 = 0;
            var3.anInt2760 = 0;
            var3.anInt2771 = var2;
            if(var3.anInt2771 != -1) {
               IOHandler.method1470(var3.anInt2829, Client.getAnimationDefinition(var3.anInt2771, (byte)-20), 183921384, var3.anInt2819, var3 == Class102.player, var3.anInt2832);
            }
         }

      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "pa.C(" + var0 + ',' + var1 + ',' + var2 + ',' + (var3 != null?"{...}":"null") + ')');
      }
   }

}
