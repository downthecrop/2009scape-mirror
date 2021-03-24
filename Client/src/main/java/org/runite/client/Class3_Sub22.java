package org.runite.client;



import org.rs09.client.Linkable;
import org.rs09.client.config.GameConfig;

import javax.media.opengl.GL;


final class Class3_Sub22 extends Linkable {

   static int anInt2500;
   int anInt2501;
   int anInt2502;
   int anInt2503;
   Class166 aClass166_2504;
   static Class49 aClass49_2505 = new Class49();
   int anInt2506;
   Class3_Sub24_Sub1 aClass3_Sub24_Sub1_2507;
   int anInt2508;
   Class3_Sub12_Sub1 aClass3_Sub12_Sub1_2509;
   int anInt2510;
   int anInt2511;
   int anInt2512;
   int anInt2513;
   int anInt2514;
   int anInt2515;
   int anInt2516;
   int anInt2517;
   int anInt2519;
   int anInt2520;
   static byte[][] aByteArrayArray2521;
   int anInt2522;
   int anInt2523;
   Class3_Sub15 aClass3_Sub15_2527;


   static void method398(int var0, int var1, int var2, int var3, int var4, byte[][][] var5, int[] var6, int[] var7, int[] var8, int[] var9, int[] var10, int var11, byte var12, int var13, int var14) {
      if(var0 < 0) {
         var0 = 0;
      } else if(var0 >= Unsorted.anInt1234 * 128) {
         var0 = Unsorted.anInt1234 * 128 - 1;
      }

      if(var2 < 0) {
         var2 = 0;
      } else if(var2 >= Class3_Sub13_Sub15.anInt3179 * 128) {
         var2 = Class3_Sub13_Sub15.anInt3179 * 128 - 1;
      }

      Class60.anInt936 = Class51.anIntArray840[var3];
      Unsorted.anInt1037 = Class51.anIntArray851[var3];
      Class3_Sub13_Sub34.anInt3417 = Class51.anIntArray840[var4];
      Class145.anInt3153 = Class51.anIntArray851[var4];
      Class145.anInt2697 = var0;
      Unsorted.anInt3657 = var1;
      Class3_Sub13_Sub30.anInt3363 = var2;
      Class97.anInt1375 = var0 / 128;
      Class145.anInt3340 = var2 / 128;
      Class163_Sub1_Sub1.anInt4006 = Class97.anInt1375 - Class3_Sub13_Sub39.anInt3466;
      if(Class163_Sub1_Sub1.anInt4006 < 0) {
         Class163_Sub1_Sub1.anInt4006 = 0;
      }

      Unsorted.anInt3603 = Class145.anInt3340 - Class3_Sub13_Sub39.anInt3466;
      if(Unsorted.anInt3603 < 0) {
         Unsorted.anInt3603 = 0;
      }

      Unsorted.anInt67 = Class97.anInt1375 + Class3_Sub13_Sub39.anInt3466;
      if(Unsorted.anInt67 > Unsorted.anInt1234) {
         Unsorted.anInt67 = Unsorted.anInt1234;
      }

      Class126.anInt1665 = Class145.anInt3340 + Class3_Sub13_Sub39.anInt3466;
      if(Class126.anInt1665 > Class3_Sub13_Sub15.anInt3179) {
         Class126.anInt1665 = Class3_Sub13_Sub15.anInt3179;
      }

      short var15;
      if(HDToolKit.highDetail) {
         var15 = (short) GameConfig.RENDER_DISTANCE_VALUE;
      } else {
         var15 = 3500;
      }

      int var17;
      int var16;
      for(var16 = 0; var16 < Class3_Sub13_Sub39.anInt3466 + Class3_Sub13_Sub39.anInt3466 + 2; ++var16) {
         for(var17 = 0; var17 < Class3_Sub13_Sub39.anInt3466 + Class3_Sub13_Sub39.anInt3466 + 2; ++var17) {
            int var18 = (var16 - Class3_Sub13_Sub39.anInt3466 << 7) - (Class145.anInt2697 & 127);
            int var19 = (var17 - Class3_Sub13_Sub39.anInt3466 << 7) - (Class3_Sub13_Sub30.anInt3363 & 127);
            int var20 = Class97.anInt1375 - Class3_Sub13_Sub39.anInt3466 + var16;
            int var21 = Class145.anInt3340 - Class3_Sub13_Sub39.anInt3466 + var17;
            if(var20 >= 0 && var21 >= 0 && var20 < Unsorted.anInt1234 && var21 < Class3_Sub13_Sub15.anInt3179) {
               int var22;
               if(Unsorted.anIntArrayArrayArray3605 == null) {
                  var22 = Class58.anIntArrayArrayArray914[0][var20][var21] - Unsorted.anInt3657 + 128;
               } else {
                  var22 = Unsorted.anIntArrayArrayArray3605[0][var20][var21] - Unsorted.anInt3657 + 128;
               }

               int var23 = Class58.anIntArrayArrayArray914[3][var20][var21] - Unsorted.anInt3657 - 1000;
               Class49.aBooleanArrayArray814[var16][var17] = Class91.method1495(var18, var23, var22, var19, var15);
            } else {
               Class49.aBooleanArrayArray814[var16][var17] = false;
            }
         }
      }

      for(var16 = 0; var16 < Class3_Sub13_Sub39.anInt3466 + Class3_Sub13_Sub39.anInt3466 + 1; ++var16) {
         for(var17 = 0; var17 < Class3_Sub13_Sub39.anInt3466 + Class3_Sub13_Sub39.anInt3466 + 1; ++var17) {
            Class23.aBooleanArrayArray457[var16][var17] = Class49.aBooleanArrayArray814[var16][var17] || Class49.aBooleanArrayArray814[var16 + 1][var17] || Class49.aBooleanArrayArray814[var16][var17 + 1] || Class49.aBooleanArrayArray814[var16 + 1][var17 + 1];
         }
      }

      Class72.anIntArray3045 = var6;
      Unsorted.anIntArray1083 = var7;
      Class52.anIntArray859 = var8;
      Class75_Sub4.anIntArray2663 = var9;
      Unsorted.anIntArray39 = var10;
      Class72.method1294();
      if(Class166.aClass3_Sub2ArrayArrayArray2065 != null) {
         Class167.method2264(true);
         Class146.method2083(var0, var1, var2, null, 0, (byte)0, var13, var14);
         if(HDToolKit.highDetail) {
            Class3_Sub13_Sub17.aBoolean3207 = false;
            Class3_Sub28_Sub4.method551(0, 0);
            Class92.method1512(null);
            Class68.method1265();
         }

         Class167.method2264(false);
      }

      Class146.method2083(var0, var1, var2, var5, var11, var12, var13, var14);
   }

   static void method400(long var0) {
      try {
         if((long) 0 != var0) {
            Class3_Sub13_Sub1.outgoingBuffer.putOpcode(104);
            Class3_Sub13_Sub1.outgoingBuffer.writeLong(var0);
         }
      } catch (RuntimeException var4) {
         throw ClientErrorException.clientError(var4, "mf.F(" + var0 + ',' + 0 + ')');
      }
   }

   final void method401() {
      try {
         this.aClass166_2504 = null;
         this.aClass3_Sub12_Sub1_2509 = null;
         this.aClass3_Sub24_Sub1_2507 = null;
         this.aClass3_Sub15_2527 = null;

      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "mf.A(" + 221 + ')');
      }
   }

   static void method403() {
      GL var0 = HDToolKit.gl;
      var0.glDisableClientState('\u8076');
      HDToolKit.method1837(false);
      var0.glDisable(2929);
      var0.glPushAttrib(128);
      var0.glFogf(2915, 3072.0F);
      HDToolKit.depthBufferWritingDisabled();

      for(int var1 = 0; var1 < Client.aClass3_Sub11ArrayArray2199[0].length; ++var1) {
         Class3_Sub11 var2 = Client.aClass3_Sub11ArrayArray2199[0][var1];
         if(var2.anInt2351 >= 0 && Class51.anInterface2_838.method18(var2.anInt2351, 255) == 4) {
            var0.glColor4fv(Class114.method1705(var2.anInt2355, 0), 0);
            float var3 = 201.5F - (var2.aBoolean2364?1.0F:0.5F);
            var2.method149(Class75_Sub2.aClass3_Sub2ArrayArrayArray2638, var3, true);
         }
      }

      var0.glEnableClientState('\u8076');
      HDToolKit.method1846();
      var0.glEnable(2929);
      var0.glPopAttrib();
      HDToolKit.method1830();
   }

   static Class75_Sub4 method404(DataBuffer var1) {
      try {
         return new Class75_Sub4(var1.readSignedShort(), var1.readSignedShort(), var1.readSignedShort(), var1.readSignedShort(), var1.readMedium(), var1.readMedium(), var1.readUnsignedByte());
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "mf.C(" + (byte) -110 + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

}
