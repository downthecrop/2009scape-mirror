package org.runite.jagex;
import java.util.Random;

final class Class100 {

   private int anInt1404 = 0;
   static int anInt1405;
   int anInt1406;
   static int anInt1407;
   int anInt1408;
   
   static CacheIndex aClass153_1410;
   boolean aBoolean1411 = true;
   int anInt1412 = -1;
   static Class136 aClass136_1413 = new Class136();
   int anInt1414 = 128;
   private static RSString aClass94_1415 = RSString.createRSString("Loaded input handler");
   static int worldListArraySize;
   int anInt1417;
   int anInt1418;
static RSString aClass94_1409 = aClass94_1415;

   private final void method1600(int var1, byte var2) {
      try {
         double var3 = (double)(255 & var1 >> 16) / 256.0D;
         double var5 = (double)(255 & var1 >> 8) / 256.0D;
         double var9 = var3;
         double var7 = (double)(var1 & 255) / 256.0D;
         if(var5 < var3) {
            var9 = var5;
         }

         if(var7 < var9) {
            var9 = var7;
         }

         double var11 = var3;
         int var13 = 77 % ((var2 - 21) / 57);
         double var14 = 0.0D;
         if(var5 > var3) {
            var11 = var5;
         }

         if(var7 > var11) {
            var11 = var7;
         }

         double var16 = 0.0D;
         double var18 = (var11 + var9) / 2.0D;
         if(var9 != var11) {
            if(0.5D > var18) {
               var16 = (var11 - var9) / (var11 + var9);
            }

            if(var11 != var3) {
               if(var5 != var11) {
                  if(var7 == var11) {
                     var14 = 4.0D + (-var5 + var3) / (-var9 + var11);
                  }
               } else {
                  var14 = (var7 - var3) / (var11 - var9) + 2.0D;
               }
            } else {
               var14 = (-var7 + var5) / (-var9 + var11);
            }

            if(0.5D <= var18) {
               var16 = (var11 - var9) / (-var9 + (2.0D - var11));
            }
         }

         if(var18 > 0.5D) {
            this.anInt1418 = (int)(var16 * (-var18 + 1.0D) * 512.0D);
         } else {
            this.anInt1418 = (int)(var16 * var18 * 512.0D);
         }

         if(1 > this.anInt1418) {
            this.anInt1418 = 1;
         }

         this.anInt1406 = (int)(var16 * 256.0D);
         this.anInt1417 = (int)(256.0D * var18);
         if(~this.anInt1417 <= -1) {
            if(255 < this.anInt1417) {
               this.anInt1417 = 255;
            }
         } else {
            this.anInt1417 = 0;
         }

         var14 /= 6.0D;
         this.anInt1408 = (int)((double)this.anInt1418 * var14);
         if(-1 >= ~this.anInt1406) {
            if(this.anInt1406 > 255) {
               this.anInt1406 = 255;
            }
         } else {
            this.anInt1406 = 0;
         }

      } catch (RuntimeException var20) {
         throw Class44.method1067(var20, "ni.D(" + var1 + ',' + var2 + ')');
      }
   }

   final void method1601(int var1, RSByteBuffer var2, int var3) {
      try {
         while(true) {
            int var4 = var2.getByte((byte)-43);
            if(var4 == 0) {
               if(var3 != 255) {
                  anInt1407 = -8;
               }

               return;
            }

            this.method1604((byte)-52, var4, var2, var1);
         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "ni.F(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   static final int method1602(int var0, RSString var1) {
      try {
         if(Class119.aClass131_1624 != null && ~var1.length(var0 ^ -107) != -1) {
            for(int var2 = var0; ~Class119.aClass131_1624.anInt1720 < ~var2; ++var2) {
               if(Class119.aClass131_1624.aClass94Array1721[var2].method1560(Class3_Sub13_Sub16.aClass94_3192, true, Class3_Sub28_Sub10_Sub2.aClass94_4066).method1562((byte)-32, var1)) {
                  return var2;
               }
            }

            return -1;
         } else {
            return -1;
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ni.G(" + var0 + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

   static final int method1603(byte var0, int var1, Random var2) {
      try {
         if(~var1 >= -1) {
            throw new IllegalArgumentException();
         } else if(Class140_Sub6.method2021((byte)-115, var1)) {
            return (int)(((long)var2.nextInt() & 4294967295L) * (long)var1 >> 32);
         } else {
            int var3 = -((int)(4294967296L % (long)var1)) + Integer.MIN_VALUE;

            int var4;
            do {
               var4 = var2.nextInt();
            } while(var3 <= var4);

            int var5 = -101 % ((var0 - -52) / 33);
            return Class3_Sub13_Sub7.method201(var4, var1, -58);
         }
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "ni.C(" + var0 + ',' + var1 + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

   private final void method1604(byte var1, int var2, RSByteBuffer var3, int var4) {
      try {
         if(-2 != ~var2) {
            if(var2 != 2) {
               if(3 == var2) {
                  this.anInt1414 = var3.getShort(1);
               } else if(~var2 == -5) {
                  this.aBoolean1411 = false;
               }
            } else {
               this.anInt1412 = var3.getShort(1);
               if(-65536 == ~this.anInt1412) {
                  this.anInt1412 = -1;
               }
            }
         } else {
            this.anInt1404 = var3.getTriByte((byte)93);
            this.method1600(this.anInt1404, (byte)81);
         }

         if(var1 != -52) {
            anInt1407 = -121;
         }

      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "ni.E(" + var1 + ',' + var2 + ',' + (var3 != null?"{...}":"null") + ',' + var4 + ')');
      }
   }

   static final void method1605(int var0, RSString var1, int var2) {
      try {
         ++Class19.anInt421;
         Class3_Sub13_Sub1.outgoingBuffer.putOpcode(188);
         Class3_Sub13_Sub1.outgoingBuffer.putByteA(var2, var0 + -13326);
         if(var0 == 255) {
            Class3_Sub13_Sub1.outgoingBuffer.putLong(var1.toLong(-126), -2037491440);
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ni.B(" + var0 + ',' + (var1 != null?"{...}":"null") + ',' + var2 + ')');
      }
   }

   public static void method1606(byte var0) {
      try {
         aClass94_1415 = null;
         aClass94_1409 = null;
         int var1 = 86 % ((49 - var0) / 48);
         aClass153_1410 = null;
         aClass136_1413 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "ni.A(" + var0 + ')');
      }
   }

}
