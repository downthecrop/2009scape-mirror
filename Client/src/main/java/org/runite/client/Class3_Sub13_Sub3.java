package org.runite.client;

import org.rs09.client.config.GameConfig;
import org.rs09.client.net.game.PacketDecoder;


import java.io.IOException;

public final class Class3_Sub13_Sub3 extends TextureOperation {

   private int anInt3047 = 4096;
   static KeyboardListener aClass148_3049 = new KeyboardListener();
   private boolean aBoolean3050 = true;


   static RSString bufferToString(byte[] bytes, int length, int offset) {
      try {
         RSString var4 = new RSString();
         var4.buffer = new byte[length];
         var4.length = 0;
         for (int var5 = offset; var5 < length + offset; ++var5) {
            if (bytes[var5] != 0) {
               var4.buffer[var4.length++] = bytes[var5];
            }
         }
         if (var4.toString().contains("RuneScape")) {
            var4 = RSString.parse(var4.toString().replace("RuneScape", GameConfig.SERVER_NAME));
         }
         if (var4.toString().contains("Jagex")) {
            var4 = RSString.parse(var4.toString().replace("Jagex", GameConfig.SERVER_NAME));
         }
         if (var4.toString().contains("Cearch")) {
            var4 = RSString.parse(var4.toString().replace("Cearch", "search"));
         }
         if (var4.toString().contains(">o")) {
            var4 = RSString.parse(var4.toString().replace(">o", "no"));
         }
         return var4;
      } catch (RuntimeException var6) {
         throw ClientErrorException.clientError(var6, "an.B(" + (bytes != null ? "{...}" : "null") + ',' + -4114 + ',' + length + ',' + offset + ')');
      }
   }

   public Class3_Sub13_Sub3() {
      super(1, false);
   }

   final void method157(int var1, DataBuffer var2, boolean var3) {
      try {
         if (!var3) {
            CS2Script.aShort3052 = -37;
         }

         if (0 == var1) {
            this.anInt3047 = var2.readUnsignedShort();
         } else if (var1 == 1) {
            this.aBoolean3050 = var2.readUnsignedByte() == 1;
         }

      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "an.A(" + var1 + ',' + (var2 != null ? "{...}" : "null") + ',' + var3 + ')');
      }
   }

   static int method179(byte var0, int var1) {
      try {
         if (var0 == 92) {
            if (null != Class3_Sub15.activeConnection) {
               Class3_Sub15.activeConnection.close();
               Class3_Sub15.activeConnection = null;
            }

            ++Unsorted.anInt1088;
            if (Unsorted.anInt1088 > 4) {
               Class43.worldListStage = 0;
               Unsorted.anInt1088 = 0;
               return var1;
            } else {
               Class43.worldListStage = 0;
               if (Class123.anInt1658 == Client.anInt3773) {
                  Class123.anInt1658 = Class53.anInt867;
               } else {
                  Class123.anInt1658 = Client.anInt3773;
               }

               return -1;
            }
         } else {
            return 122;
         }
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "an.F(" + var0 + ',' + var1 + ')');
      }
   }

   static void method180(int var0, int var1, int var2) {
      try {
         int var3;
         if (Class113.anInt1559 != var2) {
            Class102.anIntArray2125 = new int[var2];

            for (var3 = 0; var3 < var2; ++var3) {
               Class102.anIntArray2125[var3] = (var3 << 12) / var2;
            }

            Class95.anInt1343 = 64 != var2 ? 4096 : 2048;
            RenderAnimationDefinition.anInt396 = -1 + var2;
            Class113.anInt1559 = var2;
         }

         if (Class101.anInt1427 != var1) {
            if (Class113.anInt1559 == var1) {
               Class163_Sub3.anIntArray2999 = Class102.anIntArray2125;
            } else {
               Class163_Sub3.anIntArray2999 = new int[var1];

               for (int var4 = 0; var4 < var1; ++var4) {
                  Class163_Sub3.anIntArray2999[var4] = (var4 << 12) / var1;
               }
            }

            Class101.anInt1427 = var1;
            Class3_Sub20.anInt2487 = var1 + -1;
         }

      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "an.S(" + var0 + ',' + var1 + ',' + var2 + ')');
      }
   }

   static boolean method181() {
      try {
//            return PacketParser.parseIncomingPackets();
         return PacketDecoder.INSTANCE.decodePacket();
      } catch (IOException var4) {
         Class3_Sub13_Sub13.breakClientConnection();
         return true;
      } catch (Exception var5) {
         String var2 = "T2 - " + Unsorted.incomingOpcode + "," + Class7.anInt2166 + "," + Class24.anInt469 + " - " + Unsorted.incomingPacketLength + "," + (Class131.anInt1716 - -Class102.player.anIntArray2767[0]) + "," + (Class102.player.anIntArray2755[0] + Class82.anInt1152) + " - ";

         for (int var3 = 0; var3 < Unsorted.incomingPacketLength && 50 > var3; ++var3) {
            var2 = var2 + GraphicDefinition.incomingBuffer.buffer[var3] + ",";
         }

         Class49.reportError(var2, var5, (byte) 108);
         Class167.method2269((byte) 46);
         return true;
      }
   }

   static void method182() {
      try {
         if (false) {
            method179((byte) 120, -73);
         }

         Class82.aReferenceCache_1146.clear();
         Class159.aReferenceCache_2016.clear();
      } catch (RuntimeException var2) {
         throw ClientErrorException.clientError(var2, "an.Q(" + true + ')');
      }
   }

   static void method183() {
      try {

         Unsorted.aReferenceCache_684.clear();
         Class163_Sub1.aReferenceCache_2984.clear();
      } catch (RuntimeException var2) {
         throw ClientErrorException.clientError(var2, "an.O(" + -108 + ')');
      }
   }

   static void method184(int var0, int var1, int var2, int var3, int var5, int var6) {
      try {
         int var11 = Class40.method1040(Class57.anInt902, var6, Class159.anInt2020);
         int var12 = Class40.method1040(Class57.anInt902, var0, Class159.anInt2020);
         int var13 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var3, Class101.anInt1425);
         int var14 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var2, Class101.anInt1425);

         int var7 = Class40.method1040(Class57.anInt902, var6 + var1, Class159.anInt2020);
         int var8 = Class40.method1040(Class57.anInt902, -var1 + var0, Class159.anInt2020);

         int var15;
         for (var15 = var11; var7 > var15; ++var15) {
            Class3_Sub13_Sub23_Sub1.method282(Class38.anIntArrayArray663[var15], var13, 127, var14, var5);
         }

         for (var15 = var12; var15 > var8; --var15) {
            Class3_Sub13_Sub23_Sub1.method282(Class38.anIntArrayArray663[var15], var13, -76, var14, var5);
         }

         int var9 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var1 + var3, Class101.anInt1425);
         int var10 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, -var1 + var2, Class101.anInt1425);

         for (var15 = var7; var8 >= var15; ++var15) {
            int[] var16 = Class38.anIntArrayArray663[var15];
            Class3_Sub13_Sub23_Sub1.method282(var16, var13, -59, var9, var5);
            Class3_Sub13_Sub23_Sub1.method282(var16, var10, 1 + -97, var14, var5);
         }

      } catch (RuntimeException var17) {
         throw ClientErrorException.clientError(var17, "an.R(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + 1 + ',' + var5 + ',' + var6 + ')');
      }
   }

   final int[][] method166(int var1, int var2) {
      try {
         int[][] var3 = this.aClass97_2376.method1594((byte) 58, var2);
         if (this.aClass97_2376.aBoolean1379) {
            int[] var4 = this.method152(0, Class3_Sub20.anInt2487 & var2 + -1, 32755);
            int[] var5 = this.method152(0, var2, 32755);
            int[] var6 = this.method152(0, 1 + var2 & Class3_Sub20.anInt2487, 32755);
            int[] var7 = var3[0];
            int[] var8 = var3[1];
            int[] var9 = var3[2];

            for (int var10 = 0; var10 < Class113.anInt1559; ++var10) {
               int var14 = this.anInt3047 * (-var4[var10] + var6[var10]);
               int var15 = this.anInt3047 * (-var5[RenderAnimationDefinition.anInt396 & -1 + var10] + var5[var10 + 1 & RenderAnimationDefinition.anInt396]);
               int var17 = var14 >> 12;
               int var16 = var15 >> 12;
               int var19 = var17 * var17 >> 12;
               int var18 = var16 * var16 >> 12;
               int var20 = (int) (Math.sqrt((double) ((float) (var18 + var19 - -4096) / 4096.0F)) * 4096.0D);
               int var11;
               int var12;
               int var13;
               if (0 == var20) {
                  var13 = 0;
                  var11 = 0;
                  var12 = 0;
               } else {
                  var13 = 16777216 / var20;
                  var12 = var14 / var20;
                  var11 = var15 / var20;
               }

               if (this.aBoolean3050) {
                  var12 = 2048 - -(var12 >> 1);
                  var13 = (var13 >> 1) + 2048;
                  var11 = (var11 >> 1) + 2048;
               }

               var7[var10] = var11;
               var8[var10] = var12;
               var9[var10] = var13;
            }
         }

         if (var1 != -1) {
            method180(-55, -63, -5);
         }

         return var3;
      } catch (RuntimeException var21) {
         throw ClientErrorException.clientError(var21, "an.T(" + var1 + ',' + var2 + ')');
      }
   }

}
