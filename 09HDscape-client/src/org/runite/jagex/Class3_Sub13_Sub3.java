package org.runite.jagex;
import org.runite.GameLaunch;

import java.io.IOException;

final class Class3_Sub13_Sub3 extends Class3_Sub13 {

   private int anInt3047 = 4096;
   private static RSString aClass94_3048 = RSString.createRSString("Hidden");
   static Class148 aClass148_3049 = new Class148();
   private boolean aBoolean3050 = true;
   static RSString aClass94_3051 = aClass94_3048;
   static short aShort3052 = 205;
   static RSString aClass94_3053 = RSString.createRSString("Lade Schrifts-=tze )2 ");


   public static void method177(byte var0) {
      try {
         aClass94_3053 = null;
         aClass94_3051 = null;
         aClass94_3048 = null;
         if(var0 != 119) {
            aShort3052 = 109;
         }

         aClass148_3049 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "an.E(" + var0 + ')');
      }
   }

   static final RSString method178(byte[] var0, int var1, int var2, int var3) {
      try {
         RSString var4 = new RSString();
         var4.byteArray = new byte[var2];
         var4.length = 0;
         if(var1 != -4114) {
            aClass94_3053 = (RSString)null;
         }

         for(int var5 = var3; ~(var2 + var3) < ~var5; ++var5) {
            if(-1 != ~var0[var5]) {
               var4.byteArray[var4.length++] = var0[var5];
            }
         }
         if (var4.toString().contains("RuneScape")) {
        	 var4 = RSString.createRSString(var4.toString().replace("RuneScape", GameLaunch.SETTINGS.getName()));
         }
         if (var4.toString().contains("Jagex")) {
        	 var4 = RSString.createRSString(var4.toString().replace("Jagex", GameLaunch.SETTINGS.getName()));
         }
         return var4;
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "an.B(" + (var0 != null?"{...}":"null") + ',' + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   public Class3_Sub13_Sub3() {
      super(1, false);
   }

   final void method157(int var1, RSByteBuffer var2, boolean var3) {
      try {
         if(!var3) {
            aShort3052 = -37;
         }

         if(0 == var1) {
            this.anInt3047 = var2.getShort(1);
         } else if(-2 == ~var1) {
            this.aBoolean3050 = var2.getByte((byte)-88) == 1;
         }

      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "an.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   static final int method179(byte var0, int var1) {
      try {
         if(var0 != 92) {
            return 122;
         } else {
            if(null != Class3_Sub15.aClass89_2429) {
               Class3_Sub15.aClass89_2429.close(14821);
               Class3_Sub15.aClass89_2429 = null;
            }

            ++Class73.anInt1088;
            if(~Class73.anInt1088 < -5) {
               Class43.anInt692 = 0;
               Class73.anInt1088 = 0;
               return var1;
            } else {
               Class43.anInt692 = 0;
               if(Class123.anInt1658 != Class3_Sub28_Sub19.anInt3773) {
                  Class123.anInt1658 = Class3_Sub28_Sub19.anInt3773;
               } else {
                  Class123.anInt1658 = Class53.anInt867;
               }

               return -1;
            }
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "an.F(" + var0 + ',' + var1 + ')');
      }
   }

   static final void method180(int var0, int var1, int var2) {
      try {
         int var3;
         if(Class113.anInt1559 != var2) {
            Class102.anIntArray2125 = new int[var2];

            for(var3 = 0; ~var2 < ~var3; ++var3) {
               Class102.anIntArray2125[var3] = (var3 << 12) / var2;
            }

            Class95.anInt1343 = 64 != var2?4096:2048;
            RenderAnimationDefinition.anInt396 = -1 + var2;
            Class113.anInt1559 = var2;
         }

         var3 = 111 / ((56 - var0) / 38);
         if(Class101.anInt1427 != var1) {
            if(Class113.anInt1559 != var1) {
               Class163_Sub3.anIntArray2999 = new int[var1];

               for(int var4 = 0; var4 < var1; ++var4) {
                  Class163_Sub3.anIntArray2999[var4] = (var4 << 12) / var1;
               }
            } else {
               Class163_Sub3.anIntArray2999 = Class102.anIntArray2125;
            }

            Class101.anInt1427 = var1;
            Class3_Sub20.anInt2487 = var1 + -1;
         }

      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "an.S(" + var0 + ',' + var1 + ',' + var2 + ')');
      }
   }

   static final boolean method181(int var0) {
      try {
         if(var0 != -15450) {
            method182(false);
         }

         try {
            return PacketParser.parseIncomingPackets((byte)-83);
         } catch (IOException var4) {
            Class3_Sub13_Sub24.method289(false);
            return true;
         } catch (Exception var5) {
            String var2 = "T2 - " + RSString.incomingOpcode + "," + Class7.anInt2166 + "," + Class24.anInt469 + " - " + Class130.incomingPacketLength + "," + (Class131.anInt1716 - -Class102.player.anIntArray2767[0]) + "," + (Class102.player.anIntArray2755[0] + Class82.anInt1152) + " - ";

            for(int var3 = 0; var3 < Class130.incomingPacketLength && 50 > var3; ++var3) {
               var2 = var2 + GraphicDefinition.incomingBuffer.buffer[var3] + ",";
            }

            Class49.method1125(var2, var5, (byte)108);
            Class167.method2269((byte)46);
            return true;
         }
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "an.C(" + var0 + ')');
      }
   }

   static final void method182(boolean var0) {
      try {
         if(!var0) {
            method179((byte)120, -73);
         }

         Class82.aClass93_1146.method1524(3);
         Class159.aClass93_2016.method1524(3);
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "an.Q(" + var0 + ')');
      }
   }

   static final void method183(int var0) {
      try {
         if(var0 > -91) {
            aClass148_3049 = (Class148)null;
         }

         Class41.aClass93_684.method1524(3);
         Class163_Sub1.aClass93_2984.method1524(3);
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "an.O(" + var0 + ')');
      }
   }

   static final void method184(int var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      try {
         int var11 = Class40.method1040(Class57.anInt902, var6, (byte)0, Class159.anInt2020);
         int var12 = Class40.method1040(Class57.anInt902, var0, (byte)0, Class159.anInt2020);
         int var13 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var3, (byte)0, Class101.anInt1425);
         int var14 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var2, (byte)0, Class101.anInt1425);
         if(var4 != 1) {
            method183(-1);
         }

         int var7 = Class40.method1040(Class57.anInt902, var6 + var1, (byte)0, Class159.anInt2020);
         int var8 = Class40.method1040(Class57.anInt902, -var1 + var0, (byte)0, Class159.anInt2020);

         int var15;
         for(var15 = var11; var7 > var15; ++var15) {
            Class3_Sub13_Sub23_Sub1.method282(Class38.anIntArrayArray663[var15], var13, 127, var14, var5);
         }

         for(var15 = var12; ~var8 > ~var15; --var15) {
            Class3_Sub13_Sub23_Sub1.method282(Class38.anIntArrayArray663[var15], var13, -76, var14, var5);
         }

         int var9 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var1 + var3, (byte)0, Class101.anInt1425);
         int var10 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, -var1 + var2, (byte)0, Class101.anInt1425);

         for(var15 = var7; ~var15 >= ~var8; ++var15) {
            int[] var16 = Class38.anIntArrayArray663[var15];
            Class3_Sub13_Sub23_Sub1.method282(var16, var13, -59, var9, var5);
            Class3_Sub13_Sub23_Sub1.method282(var16, var10, var4 + -97, var14, var5);
         }

      } catch (RuntimeException var17) {
         throw Class44.method1067(var17, "an.R(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ')');
      }
   }

   final int[][] method166(int var1, int var2) {
      try {
         int[][] var3 = this.aClass97_2376.method1594((byte)58, var2);
         if(this.aClass97_2376.aBoolean1379) {
            int[] var4 = this.method152(0, Class3_Sub20.anInt2487 & var2 + -1, 32755);
            int[] var5 = this.method152(0, var2, 32755);
            int[] var6 = this.method152(0, 1 + var2 & Class3_Sub20.anInt2487, 32755);
            int[] var7 = var3[0];
            int[] var8 = var3[1];
            int[] var9 = var3[2];

            for(int var10 = 0; var10 < Class113.anInt1559; ++var10) {
               int var14 = this.anInt3047 * (-var4[var10] + var6[var10]);
               int var15 = this.anInt3047 * (-var5[RenderAnimationDefinition.anInt396 & -1 + var10] + var5[var10 + 1 & RenderAnimationDefinition.anInt396]);
               int var17 = var14 >> 12;
               int var16 = var15 >> 12;
               int var19 = var17 * var17 >> 12;
               int var18 = var16 * var16 >> 12;
               int var20 = (int)(Math.sqrt((double)((float)(var18 + var19 - -4096) / 4096.0F)) * 4096.0D);
               int var11;
               int var12;
               int var13;
               if(0 == var20) {
                  var13 = 0;
                  var11 = 0;
                  var12 = 0;
               } else {
                  var13 = 16777216 / var20;
                  var12 = var14 / var20;
                  var11 = var15 / var20;
               }

               if(this.aBoolean3050) {
                  var12 = 2048 - -(var12 >> 1);
                  var13 = (var13 >> 1) + 2048;
                  var11 = (var11 >> 1) + 2048;
               }

               var7[var10] = var11;
               var8[var10] = var12;
               var9[var10] = var13;
            }
         }

         if(var1 != -1) {
            method180(-55, -63, -5);
         }

         return var3;
      } catch (RuntimeException var21) {
         throw Class44.method1067(var21, "an.T(" + var1 + ',' + var2 + ')');
      }
   }

}
