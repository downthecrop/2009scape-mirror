package org.runite.client;
import org.rs09.client.rendering.Toolkit;
import org.rs09.client.data.NodeCache;

import java.io.DataInputStream;
import java.net.URL;
import java.util.Random;

final class Class49 {

   static AbstractSprite aAbstractSprite_812;
   static boolean[][] aBooleanArrayArray814;
   static int anInt815 = 0;
   static int anInt817;
   static NodeCache aClass47_818 = new NodeCache(64);
   static int anInt819 = 0;


   static void method1121(boolean var0, byte var1) {
      try {
         byte var2;
         byte[][] var3;
         if(HDToolKit.highDetail && var0) {
            var2 = 1;
            var3 = Class40.aByteArrayArray3669;
         } else {
            var3 = Class164_Sub2.aByteArrayArray3027;
            var2 = 4;
         }

         for(int var4 = 0; var2 > var4; ++var4) {
            Class58.method1194();

            for(int var5 = 0; var5 < 13; ++var5) {
               for(int var6 = 0; var6 < 13; ++var6) {
                  int var8 = ObjectDefinition.anIntArrayArrayArray1497[var4][var5][var6];
                  boolean var7 = false;
                  if(var8 != -1) {
                     int var9 = var8 >> 24 & 3;
                     if(!var0 || 0 == var9) {
                        int var12 = 2047 & var8 >> 3;
                        int var10 = var8 >> 1 & 3;
                        int var11 = 1023 & var8 >> 14;
                        int var13 = (var11 / 8 << 8) + var12 / 8;

                        for(int var14 = 0; var14 < Class3_Sub24_Sub3.anIntArray3494.length; ++var14) {
                           if(var13 == Class3_Sub24_Sub3.anIntArray3494[var14] && null != var3[var14]) {
                              Unsorted.method60(var10, 8 * var5, var4, AtmosphereParser.aClass91Array1182, var6 * 8, (byte)-100, var3[var14], var9, (var12 & 7) * 8, 8 * (var11 & 7), var0);
                              var7 = true;
                              break;
                           }
                        }
                     }
                  }

                  if(!var7) {
                     Class12.method870(var4, (byte)84, 8 * var6, var5 * 8, 8, 8);
                  }
               }
            }
         }

         if(var1 <= 49) {
            anInt817 = 32;
         }

      } catch (RuntimeException var15) {
         throw ClientErrorException.clientError(var15, "ha.E(" + var0 + ',' + var1 + ')');
      }
   }

   static RSInterface method1122(RSInterface var1) {
      try {

         RSInterface var2 = Client.method42(var1);
         if(null == var2) {
            var2 = var1.aClass11_302;
         }

         return var2;
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "ha.J(" + 0 + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

   static byte[] method1123(int var0, int var1) {
      try {
         Class3_Sub28_Sub8 var2 = (Class3_Sub28_Sub8)Class25.aClass47_480.get((long)var1);
         if(null == var2) {
            Random var4 = new Random((long)var1);
            byte[] var3 = new byte[512];

            int var5;
            for(var5 = 0; var5 < 255; ++var5) {
               var3[var5] = (byte)var5;
            }

            for(var5 = 0; var5 < 255; ++var5) {
               int var6 = 255 - var5;
               int var7 = Class3_Sub13.method1603((byte)125, var6, var4);
               byte var8 = var3[var7];
               var3[var7] = var3[var6];
               var3[var6] = var3[511 + -var5] = var8;
            }

            var2 = new Class3_Sub28_Sub8(var3);
            Class25.aClass47_480.put((long)var1, var2);
         }

         return var0 != 16711935?(byte[])null:var2.aByteArray3612;
      } catch (RuntimeException var9) {
         throw ClientErrorException.clientError(var9, "ha.B(" + var0 + ',' + var1 + ')');
      }
   }

   static RSString method1124(int[] var0, long var1, int var3) {
      try {
         if(Class58.anInterface4_915 != null) {
            RSString var5 = Class58.anInterface4_915.method20(var3, var0, 4936, var1);
            if(var5 != null) {
               return var5;
            }
         }

         return Class3_Sub28_Sub12.method612(var1);
      } catch (RuntimeException var6) {
         throw ClientErrorException.clientError(var6, "ha.H(" + (var0 != null?"{...}":"null") + ',' + var1 + ',' + var3 + ',' + false + ')');
      }
   }

   static void method1125(String var0, Throwable var1, byte var2) {
      if(var2 <= 100) {
         aClass47_818 = (NodeCache)null;
      }

      try {
         String var3 = "";
         if(null != var1) {
            var3 = Class53.method1172(var1);
         }

         if(var0 != null) {
            if(null != var1) {
               var3 = var3 + " | ";
            }

            var3 = var3 + var0;
         }

         Class7.method831(var3);
         var3 = InterfaceWidget.a(":", "%3a", var3);
         var3 = InterfaceWidget.a("@", "%40", var3);
         var3 = InterfaceWidget.a("&", "%26", var3);
         var3 = InterfaceWidget.a("#", "%23", var3);
         if(Class3_Sub13_Sub10.aClass87_3125.applet == null) {
            return;
         }

         Class64 var4 = Class3_Sub13_Sub10.aClass87_3125.method1439(false, new URL(Class3_Sub13_Sub10.aClass87_3125.applet.getCodeBase(), "clienterror.ws?c=" + GameShell.anInt4033 + "&u=" + PacketParser.aLong3202 + "&v1=" + Signlink.javaVendor + "&v2=" + Signlink.javaVersion + "&e=" + var3));

         while(var4.anInt978 == 0) {
            TimeUtils.sleep(1L);
         }

         if(var4.anInt978 == 1) {
            DataInputStream var5 = (DataInputStream)var4.anObject974;
            var5.read();
            var5.close();
         }
      } catch (Exception var6) {
      }

   }

   static void method1126(int var1) {
      try {
         InterfaceWidget var2 = InterfaceWidget.getWidget(2, var1);
         var2.a();
      } catch (RuntimeException var4) {
         throw ClientErrorException.clientError(var4, "ha.I(" + -94 + ',' + var1 + ')');
      }
   }

   static void method1127(int var0) {
      try {
         if(!Class38_Sub1.aBoolean2615) {
            if(Unsorted.anInt3660 != 0) {
               NPCDefinition.anInt1297 = Class3_Sub13_Sub39.anInt3460;
               Class38_Sub1.anInt2612 = Class168.anInt2099;
            } else if(Unsorted.anInt3644 == 0) {
               NPCDefinition.anInt1297 = Class126.anInt1676;
               Class38_Sub1.anInt2612 = Unsorted.anInt1709;
            } else {
               NPCDefinition.anInt1297 = Class163_Sub1.anInt2993;
               Class38_Sub1.anInt2612 = Class38_Sub1.anInt2614;
            }

            Unsorted.menuOptionCount = 1;
            Class140_Sub7.aClass94Array2935[0] = TextCore.HasCancel;
            Class163_Sub2_Sub1.aClass94Array4016[0] = TextCore.aClass94_3672;
            Class3_Sub13_Sub7.aShortArray3095[0] = 1005;
            Class114.anIntArray1578[0] = Class3_Sub28_Sub5.anInt3590;
         }

         if(Class3_Sub28_Sub12.anInt3655 != -1) {
            Class52.method1160(-113, Class3_Sub28_Sub12.anInt3655);
         }

         if(var0 == 0) {
            int var1;
            for(var1 = 0; var1 < Class3_Sub28_Sub3.anInt3557; ++var1) {
               if(Unsorted.aBooleanArray3674[var1]) {
                  Class163_Sub1_Sub1.aBooleanArray4008[var1] = true;
               }

               Unsorted.aBooleanArray1712[var1] = Unsorted.aBooleanArray3674[var1];
               Unsorted.aBooleanArray3674[var1] = false;
            }

            AbstractSprite.aClass11_3708 = null;
            Class53.anInt865 = -1;
            Unsorted.anInt2567 = -1;
            Class99.aClass11_1402 = null;
            if(HDToolKit.highDetail) {
               Unsorted.aBoolean47 = true;
            }

            Class3_Sub23.anInt2535 = Class44.anInt719;
            if(Class3_Sub28_Sub12.anInt3655 != -1) {
               Class3_Sub28_Sub3.anInt3557 = 0;
               Class8.method841();
            }

            if(HDToolKit.highDetail) {
               Class22.resetClipping();
            } else {
               Class74.resetClipping();
            }

            Class168.method2278(var0 + 122);
            if(Class38_Sub1.aBoolean2615) {
               if(Unsorted.aBoolean1951) {
                  WorldListEntry.buildWorldListInterface();
               } else {
                  Class84.rightClickContextMenuDraw();
               }
            } else if(null == AbstractSprite.aClass11_3708) {
               if(Class53.anInt865 != -1) {
                  Class24.method950((RSInterface)null, -86, Unsorted.anInt2567, Class53.anInt865);
               }
            } else {
               Class24.method950(AbstractSprite.aClass11_3708, -120, ClientErrorException.anInt2115, Class3_Sub13_Sub23_Sub1.anInt4041);
            }

            var1 = Class38_Sub1.aBoolean2615?-1:Class3_Sub13_Sub35.method335(var0 + 16859);
            if(var1 == -1) {
               var1 = Class161.anInt2027;
            }

            Class3_Sub13_Sub13.method229(var1);
            if(CS2Script.anInt2440 == 1) {
               CS2Script.anInt2440 = 2;
            }

            if(1 == ObjectDefinition.anInt1521) {
               ObjectDefinition.anInt1521 = 2;
            }

            if(Client.rectDebugInt == 3) {
               for(int var2 = 0; Class3_Sub28_Sub3.anInt3557 > var2; ++var2) {
                  if(!Unsorted.aBooleanArray1712[var2]) {
                     if(Class163_Sub1_Sub1.aBooleanArray4008[var2]) {
                           Toolkit.getActiveToolkit().fillRect(Class155.anIntArray1969[var2], Player.anIntArray3954[var2], Class3_Sub28_Sub18.anIntArray3768[var2], Class140_Sub4.anIntArray2794[var2], 16711680, 128);
                     }
                  } else {
                     Toolkit.getActiveToolkit().fillRect(Class155.anIntArray1969[var2], Player.anIntArray3954[var2], Class3_Sub28_Sub18.anIntArray3768[var2], Class140_Sub4.anIntArray2794[var2], 16711935, 128);
                  }
               }
            }

            Class61.method1214(Class106.anInt1446, Class102.player.anInt2819, Class102.player.anInt2829, WorldListCountry.localPlane);
            Class106.anInt1446 = 0;
         }
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "ha.G(" + var0 + ')');
      }
   }

   static void method1129(int var0, int var1, int var2, int var4) {
      try {
         int var5 = 0;
         int var6 = var2;
         int var7 = -var2;
         int var8 = -1;
         int var9 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var2 + var4, Class101.anInt1425);
         int var10 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var4 - var2, Class101.anInt1425);
         Class3_Sub13_Sub23_Sub1.method282(Class38.anIntArrayArray663[var1], var10, -98, var9, var0);

         while(var6 > var5) {
            var8 += 2;
            var7 += var8;
            int var11;
            int var12;
            int var13;
            int var14;
            if(var7 > 0) {
               --var6;
               var11 = -var6 + var1;
               var7 -= var6 << 1;
               var12 = var1 + var6;
               if(var12 >= Class159.anInt2020 && var11 <= Class57.anInt902) {
                  var13 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var4 - -var5, Class101.anInt1425);
                  var14 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, -var5 + var4, Class101.anInt1425);
                  if(Class57.anInt902 >= var12) {
                     Class3_Sub13_Sub23_Sub1.method282(Class38.anIntArrayArray663[var12], var14, 111, var13, var0);
                  }

                  if(Class159.anInt2020 <= var11) {
                     Class3_Sub13_Sub23_Sub1.method282(Class38.anIntArrayArray663[var11], var14, -84, var13, var0);
                  }
               }
            }

            ++var5;
            var11 = -var5 + var1;
            var12 = var5 + var1;
            if(var12 >= Class159.anInt2020 && var11 <= Class57.anInt902) {
               var13 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var4 - -var6, Class101.anInt1425);
               var14 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, -var6 + var4, Class101.anInt1425);
               if(var12 <= Class57.anInt902) {
                  Class3_Sub13_Sub23_Sub1.method282(Class38.anIntArrayArray663[var12], var14, 90, var13, var0);
               }

               if(var11 >= Class159.anInt2020) {
                  Class3_Sub13_Sub23_Sub1.method282(Class38.anIntArrayArray663[var11], var14, -103, var13, var0);
               }
            }
         }

      } catch (RuntimeException var15) {
         throw ClientErrorException.clientError(var15, "ha.A(" + var0 + ',' + var1 + ',' + var2 + ',' + 0 + ',' + var4 + ')');
      }
   }

}
