package org.runite.jagex;

final class Class166 {

   int anInt2063;
   byte[] aByteArray2064;
   static Class3_Sub2[][][] aClass3_Sub2ArrayArrayArray2065;
   int anInt2066;
   int anInt2067;
   static int[] anIntArray2068 = new int[50];
   int anInt2069;
   static Class3_Sub28_Sub5[] aClass3_Sub28_Sub5Array2070 = new Class3_Sub28_Sub5[14];
   int anInt2071;
   static Class3_Sub28_Sub16[] aClass3_Sub28_Sub16Array2072;
   static int[] anIntArray2073 = new int[5];
   static RSString aClass94_2074 = RSString.createRSString("; version=1; path=)4; domain=");
   static RSString aClass94_2075 = RSString.createRSString("rect_debug=");
   byte[] aByteArray2076;
   int anInt2077;
   int anInt2078;
   static int anInt2079 = 0;
   static RSString aClass94_2080 = RSString.createRSString("(U2");
   static int anInt2081;


   public static void method2255(byte var0) {
      try {
         anIntArray2073 = null;
         aClass3_Sub2ArrayArrayArray2065 = (Class3_Sub2[][][])null;
         aClass94_2080 = null;
         aClass94_2075 = null;
         if(var0 >= -126) {
            aClass94_2080 = (RSString)null;
         }

         anIntArray2068 = null;
         aClass3_Sub28_Sub5Array2070 = null;
         aClass3_Sub28_Sub16Array2072 = null;
         aClass94_2074 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "wh.B(" + var0 + ')');
      }
   }

   static final boolean method2256(int var0, int var1, int var2, int var3) {
      if(!Class8.method846(var0, var1, var2)) {
         return false;
      } else {
         int var4 = var1 << 7;
         int var5 = var2 << 7;
         return Class3_Sub13_Sub37.method349(var4 + 1, Class44.anIntArrayArrayArray723[var0][var1][var2] + var3, var5 + 1) && Class3_Sub13_Sub37.method349(var4 + 128 - 1, Class44.anIntArrayArrayArray723[var0][var1 + 1][var2] + var3, var5 + 1) && Class3_Sub13_Sub37.method349(var4 + 128 - 1, Class44.anIntArrayArrayArray723[var0][var1 + 1][var2 + 1] + var3, var5 + 128 - 1) && Class3_Sub13_Sub37.method349(var4 + 1, Class44.anIntArrayArrayArray723[var0][var1][var2 + 1] + var3, var5 + 128 - 1);
      }
   }

   static final void method2257(int var0) {
      try {
         if(var0 < 60) {
            aClass3_Sub28_Sub16Array2072 = (Class3_Sub28_Sub16[])null;
         }

         Class163_Sub2_Sub1.aClass93_4015.method1524(3);
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "wh.F(" + var0 + ')');
      }
   }

   static final void method2258(int var0, int var1, RSString var2) {
      try {
         RSString var3 = var2.method1579(-17).method1545((byte)-50);
         boolean var4 = false;

         for(int var5 = var1; ~var5 > ~Class159.localPlayerCount; ++var5) {
            Player var6 = Class3_Sub13_Sub22.players[Class56.localPlayerIndexes[var5]];
            if(null != var6 && null != var6.displayName && var6.displayName.equals(-110, var3)) {
               var4 = true;
               Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var6.anIntArray2767[0], 1, 0, 2, var6.anIntArray2755[0], Class102.player.anIntArray2767[0]);
               if(1 == var0) {
                  ++ItemDefinition.anInt759;
                  Class3_Sub13_Sub1.outgoingBuffer.putOpcode(68);
                  Class3_Sub13_Sub1.outgoingBuffer.putLEShortA(Class56.localPlayerIndexes[var5]);
               } else if(4 != var0) {
                  if(5 != var0) {
                     if(~var0 != -7) {
                        if(~var0 == -8) {
                           ++Class20.anInt437;
                           Class3_Sub13_Sub1.outgoingBuffer.putOpcode(114);
                           Class3_Sub13_Sub1.outgoingBuffer.putLEShortA(Class56.localPlayerIndexes[var5]);
                        }
                     } else {
                        Class3_Sub13_Sub1.outgoingBuffer.putOpcode(133);
                        Class3_Sub13_Sub1.outgoingBuffer.putLEShort(-1, Class56.localPlayerIndexes[var5]);
                        ++Class3_Sub24_Sub4.anInt3517;
                     }
                  } else {
                     Class3_Sub13_Sub1.outgoingBuffer.putOpcode(4);
                     Class3_Sub13_Sub1.outgoingBuffer.putLEShort(var1 + -1, Class56.localPlayerIndexes[var5]);
                     ++IOHandler.anInt1240;
                  }
               } else {
                  ++KeyboardListener.anInt1910;
                  Class3_Sub13_Sub1.outgoingBuffer.putOpcode(180);
                  Class3_Sub13_Sub1.outgoingBuffer.putLEShortA(Class56.localPlayerIndexes[var5]);
               }
               break;
            }
         }

         if(!var4) {
            Class3_Sub30_Sub1.sendMessage(Class3_Sub28_Sub14.aClass94_3672, 0, RenderAnimationDefinition.method903(new RSString[]{Class43.aClass94_691, var3}, (byte)-77), -1);
         }

      } catch (RuntimeException var7) {
         throw Class44.method1067(var7, "wh.D(" + var0 + ',' + var1 + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

   static final AbstractIndexedSprite method2259(byte var0) {
      try {
         Object var1;
         if(HDToolKit.highDetail) {
            var1 = new HDIndexedSprite(Class3_Sub15.anInt2426, Class133.anInt1748, Class164.anIntArray2048[0], RSByteBuffer.anIntArray2591[0], Class140_Sub7.anIntArray2931[0], Class3_Sub13_Sub6.anIntArray3076[0], Class163_Sub1.aByteArrayArray2987[0], Class3_Sub13_Sub38.spritePalette);
         } else {
            var1 = new LDIndexedSprite(Class3_Sub15.anInt2426, Class133.anInt1748, Class164.anIntArray2048[0], RSByteBuffer.anIntArray2591[0], Class140_Sub7.anIntArray2931[0], Class3_Sub13_Sub6.anIntArray3076[0], Class163_Sub1.aByteArrayArray2987[0], Class3_Sub13_Sub38.spritePalette);
         }

         if(var0 != -40) {
            method2257(83);
         }

         Class39.method1035((byte)116);
         return (AbstractIndexedSprite)var1;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "wh.A(" + var0 + ')');
      }
   }

   static final void method2260(int var0, int var1) {
      try {
         Class3_Sub13_Sub34.aClass93_3412.method1522(var0 + 919, var1);
         if(var0 == -1045) {
            Class3_Sub13_Sub31.aClass93_3369.method1522(var0 ^ 1130, var1);
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "wh.E(" + var0 + ',' + var1 + ')');
      }
   }

}
