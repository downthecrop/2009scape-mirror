package org.runite.jagex;

final class Class77 {

   static RSString aClass94_1110 = RSString.createRSString("Attaquer");
   static int anInt1111;
   static Class52 aClass52_1112 = new Class52();


   static final LDIndexedSprite method1364(byte var0) {
      try {
         LDIndexedSprite var1 = new LDIndexedSprite(Class3_Sub15.anInt2426, Class133.anInt1748, Class164.anIntArray2048[0], RSByteBuffer.anIntArray2591[0], Class140_Sub7.anIntArray2931[0], Class3_Sub13_Sub6.anIntArray3076[0], Class163_Sub1.aByteArrayArray2987[0], Class3_Sub13_Sub38.spritePalette);
         if(var0 <= 55) {
            method1366(86, -88);
         }

         Class39.method1035((byte)127);
         return var1;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "kh.B(" + var0 + ')');
      }
   }

   public static void method1365(int var0) {
      try {
         aClass52_1112 = null;
         aClass94_1110 = null;
         if(var0 <= 96) {
            anInt1111 = 55;
         }

      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "kh.E(" + var0 + ')');
      }
   }

   static final void method1366(int var0, int var1) {
      try {
         if(var0 != 104) {
            method1367(114, 95, -80, (byte)-90, -2, 56);
         }

         Class158_Sub1.aClass93_2982.method1522(var0 ^ -22, var1);
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "kh.C(" + var0 + ',' + var1 + ')');
      }
   }

   static final void method1367(int var0, int var1, int var2, byte var3, int var4, int var5) {
      try {
         Class3_Sub13_Sub23_Sub1.method282(Class38.anIntArrayArray663[var2++], var5, 92, var1, var0);
         Class3_Sub13_Sub23_Sub1.method282(Class38.anIntArrayArray663[var4--], var5, 97, var1, var0);
         if(var3 >= 23) {
            for(int var6 = var2; ~var6 >= ~var4; ++var6) {
               int[] var7 = Class38.anIntArrayArray663[var6];
               var7[var5] = var7[var1] = var0;
            }

         }
      } catch (RuntimeException var8) {
         throw Class44.method1067(var8, "kh.A(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ')');
      }
   }

   static final void method1368(int var0) {
      try {
         ++Class163_Sub3.anInt3001;
         int var1 = -126 / ((-26 - var0) / 52);
         Class3_Sub13_Sub1.outgoingBuffer.putOpcode(104);
         Class3_Sub13_Sub1.outgoingBuffer.putLong(0L, -2037491440);
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "kh.D(" + var0 + ')');
      }
   }

}
