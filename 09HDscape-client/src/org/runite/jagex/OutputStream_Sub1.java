package org.runite.jagex;
import java.io.IOException;
import java.io.OutputStream;

final class OutputStream_Sub1 extends OutputStream {

   static RSString[] aClass94Array45;
   static short aShort46 = 256;
   static boolean aBoolean47 = false;
   static int anInt48 = 2;
   static int[] anIntArray49;
   static RSString aClass94_50 = RSString.createRSString("<col=80ff00>");
   static RSString aClass94_51 = RSString.createRSString("; Expires=");


   static final short[] method65(int var0, short[] var1) {
      try {
         if(var0 != 23032) {
            return (short[])null;
         } else if(null != var1) {
            short[] var2 = new short[var1.length];
            Class76.method1361(var1, 0, var2, 0, var1.length);
            return var2;
         } else {
            return null;
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "vg.A(" + var0 + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

   static final void method66(RSString var0, int var1, int var2, byte var3, int var4) {
      try {
         RSInterface var5 = Class3_Sub28_Sub16.method638((byte)-19, var4, var1);
         if(null != var5) {
            if(var5.anObjectArray314 != null) {
               CS2Script var6 = new CS2Script();
               var6.arguments = var5.anObjectArray314;
               var6.aClass11_2449 = var5;
               var6.aClass94_2439 = var0;
               var6.anInt2445 = var2;
               Class43.method1065(1073376993, var6);
            }

            boolean var8 = true;
            if(0 < var5.anInt189) {
               var8 = Class3_Sub28_Sub19.method715(205, var5);
            }

            if(var8) {
               if(Client.method44(var5).method92(var2 - 1, (byte)-108)) {
                  if(1 == var2) {
                     Class3_Sub13_Sub1.outgoingBuffer.putOpcode(155);
                     Class3_Sub13_Sub1.outgoingBuffer.putInt(-120, var4);
                     ++Class61.anInt937;
                     Class3_Sub13_Sub1.outgoingBuffer.putShort(var1);
                  }

                  if(var3 < -7) {
                     if(-3 == ~var2) {
                        ++Class3_Sub13_Sub10.anInt3122;
                        Class3_Sub13_Sub1.outgoingBuffer.putOpcode(196);
                        Class3_Sub13_Sub1.outgoingBuffer.putInt(-122, var4);
                        Class3_Sub13_Sub1.outgoingBuffer.putShort(var1);
                     }

                     if(~var2 == -4) {
                        ++Class56.anInt889;
                        Class3_Sub13_Sub1.outgoingBuffer.putOpcode(124);
                        Class3_Sub13_Sub1.outgoingBuffer.putInt(-122, var4);
                        Class3_Sub13_Sub1.outgoingBuffer.putShort(var1);
                     }

                     if(var2 == 4) {
                        Class3_Sub13_Sub1.outgoingBuffer.putOpcode(199);
                        Class3_Sub13_Sub1.outgoingBuffer.putInt(-126, var4);
                        Class3_Sub13_Sub1.outgoingBuffer.putShort(var1);
                        ++Class148.anInt1909;
                     }

                     if(~var2 == -6) {
                        Class3_Sub13_Sub1.outgoingBuffer.putOpcode(234);
                        Class3_Sub13_Sub1.outgoingBuffer.putInt(-123, var4);
                        Class3_Sub13_Sub1.outgoingBuffer.putShort(var1);
                        ++Class166.anInt2081;
                     }

                     if(6 == var2) {
                        Class3_Sub13_Sub1.outgoingBuffer.putOpcode(168);
                        ++Class85.anInt1172;
                        Class3_Sub13_Sub1.outgoingBuffer.putInt(-120, var4);
                        Class3_Sub13_Sub1.outgoingBuffer.putShort(var1);
                     }

                     if(-8 == ~var2) {
                        ++Class40.anInt674;
                        Class3_Sub13_Sub1.outgoingBuffer.putOpcode(166);
                        Class3_Sub13_Sub1.outgoingBuffer.putInt(-123, var4);
                        Class3_Sub13_Sub1.outgoingBuffer.putShort(var1);
                     }

                     if(-9 == ~var2) {
                        Class3_Sub13_Sub1.outgoingBuffer.putOpcode(64);
                        ++Class57.anInt903;
                        Class3_Sub13_Sub1.outgoingBuffer.putInt(-127, var4);
                        Class3_Sub13_Sub1.outgoingBuffer.putShort(var1);
                     }

                     if(-10 == ~var2) {
                        ++Class85.anInt1166;
                        Class3_Sub13_Sub1.outgoingBuffer.putOpcode(53);
                        Class3_Sub13_Sub1.outgoingBuffer.putInt(-123, var4);
                        Class3_Sub13_Sub1.outgoingBuffer.putShort(var1);
                     }

                     if(~var2 == -11) {
                        ++Class91.anInt1307;
                        Class3_Sub13_Sub1.outgoingBuffer.putOpcode(9);
                        Class3_Sub13_Sub1.outgoingBuffer.putInt(-125, var4);
                        Class3_Sub13_Sub1.outgoingBuffer.putShort(var1);
                     }

                  }
               }
            }
         }
      } catch (RuntimeException var7) {
         throw Class44.method1067(var7, "vg.D(" + (var0 != null?"{...}":"null") + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ')');
      }
   }

   public static void method67(boolean var0) {
      try {
         aClass94_50 = null;
         aClass94Array45 = null;
         if(!var0) {
            aBoolean47 = true;
         }

         anIntArray49 = null;
         aClass94_51 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "vg.B(" + var0 + ')');
      }
   }

   static final void method68(int var0, int var1, Class140_Sub4 var2) {
      try {
         if(~Class44.anInt719 <= ~var2.anInt2800) {
            if(var2.anInt2790 >= Class44.anInt719) {
               Class168.method2270(var2, (byte)-56);
            } else {
               Class55.method1180((byte)-22, var2);
            }
         } else {
            Class140_Sub2.method1950(var2, true);
         }

         if(-129 < ~var2.anInt2819 || var2.anInt2829 < 128 || var2.anInt2819 >= 13184 || var2.anInt2829 >= 13184) {
            var2.anInt2771 = -1;
            var2.anInt2842 = -1;
            var2.anInt2800 = 0;
            var2.anInt2790 = 0;
            var2.anInt2819 = 128 * var2.anIntArray2767[0] - -(64 * var2.getSize((byte)114));
            var2.anInt2829 = var2.anIntArray2755[0] * 128 + var2.getSize((byte)114) * 64;
            var2.method1973(var1 + -2395);
         }

         if(var1 == 2279) {
            if(var2 == Class102.player && (var2.anInt2819 < 1536 || -1537 < ~var2.anInt2829 || -11777 >= ~var2.anInt2819 || var2.anInt2829 >= 11776)) {
               var2.anInt2842 = -1;
               var2.anInt2800 = 0;
               var2.anInt2790 = 0;
               var2.anInt2771 = -1;
               var2.anInt2819 = var2.anIntArray2767[0] * 128 + var2.getSize((byte)114) * 64;
               var2.anInt2829 = 128 * var2.anIntArray2755[0] + 64 * var2.getSize((byte)114);
               var2.method1973(-98);
            }

            Class17.method904(65536, var2);
            RenderAnimationDefinition.method900(var2, -11973);
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "vg.C(" + var0 + ',' + var1 + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

   public final void write(int var1) throws IOException {
      try {
         throw new IOException();
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "vg.write(" + var1 + ')');
      }
   }

}
