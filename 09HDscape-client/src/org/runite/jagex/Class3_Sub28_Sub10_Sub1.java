package org.runite.jagex;
import java.awt.Frame;

final class Class3_Sub28_Sub10_Sub1 extends Class3_Sub28_Sub10 {

   static int anInt4054;
   private static RSString aClass94_4055 = RSString.createRSString(" has logged in)3");
   Class41 aClass41_4056;
   static RSString aClass94_4057 = RSString.createRSString("Mem:");
   static RSString aClass94_4058 = aClass94_4055;
   byte[] aByteArray4059;
   static Class25[] aClass25Array4060;
   int anInt4061;
   static int anInt4062 = 0;
   static boolean aBoolean4063 = false;


   final byte[] method587(boolean var1) {
      try {
         if(var1) {
            aBoolean4063 = true;
         }

         if(!this.aBoolean3632) {
            return this.aByteArray4059;
         } else {
            throw new RuntimeException();
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "c.E(" + var1 + ')');
      }
   }

   static final boolean method591(int var0) {
      try {
         Class148 var1 = Class3_Sub13_Sub3.aClass148_3049;
         synchronized(var1) {
            if(~Class134.anInt1762 != ~Class3_Sub28_Sub9.anInt3620) {
               Class3_Sub28_Sub9.anInt3624 = Class129.anIntArray1693[Class3_Sub28_Sub9.anInt3620];
               Class3_Sub13_Sub27.anInt3342 = Class155.anIntArray1978[Class3_Sub28_Sub9.anInt3620];
               Class3_Sub28_Sub9.anInt3620 = 1 + Class3_Sub28_Sub9.anInt3620 & 127;
               if(var0 < 58) {
                  method591(68);
               }

               return true;
            } else {
               return false;
            }
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "c.F(" + var0 + ')');
      }
   }

   public static void method592(byte var0) {
      try {
         aClass25Array4060 = null;
         aClass94_4057 = null;
         if(var0 > 33) {
            aClass94_4058 = null;
            aClass94_4055 = null;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "c.P(" + var0 + ')');
      }
   }

   static final void method593(Frame var0, boolean var1, Signlink var2) {
      try {
         if(!var1) {
            method594(110, 94);
         }

         while(true) {
            Class64 var3 = var2.method1436(var0, 86);

            while(-1 == ~var3.anInt978) {
               Class3_Sub13_Sub34.method331(10L, 64);
            }

            if(1 == var3.anInt978) {
               var0.setVisible(false);
               var0.dispose();
               return;
            }

            Class3_Sub13_Sub34.method331(100L, 64);
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "c.R(" + (var0 != null?"{...}":"null") + ',' + var1 + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

   final int method586(boolean var1) {
      try {
         if(var1) {
            this.aClass41_4056 = (Class41)null;
         }

         return this.aBoolean3632?0:100;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "c.A(" + var1 + ')');
      }
   }

   static final void method594(int var0, int var1) {
      try {
         if(var0 < 83) {
            method592((byte)122);
         }

         Class128.aClass93_1683.method1522(-127, var1);
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "c.Q(" + var0 + ',' + var1 + ')');
      }
   }

   static final void method595(int var0, int var1) {
      try {
         if(var1 > 108) {
            Class41.aClass93_684.method1522(-128, var0);
            Class163_Sub1.aClass93_2984.method1522(-125, var0);
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "c.O(" + var0 + ',' + var1 + ')');
      }
   }

}
