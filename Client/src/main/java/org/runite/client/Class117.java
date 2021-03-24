package org.runite.client;

public final class Class117 {

   static int[] anIntArray1613 = new int[500];


   static void method1719(int var0) {
      try {
         if(Class143.gameStage != var0) {
            if(Class143.gameStage == 0) {
               Unsorted.method1517();
            }

            if(var0 == 40) {
               Class24.method951();
            }

            boolean var2 = var0 == 5 || var0 == 10 || var0 == 28;
            if(40 != var0 && null != Class163_Sub2_Sub1.aClass89_4012) {
               Class163_Sub2_Sub1.aClass89_4012.close();
               Class163_Sub2_Sub1.aClass89_4012 = null;
            }

            if(var0 == 25 || var0 == 28) {
               Class162.anInt2038 = 0;
               LinkableRSString.anInt2579 = 1;
               Class163_Sub2_Sub1.anInt4019 = 0;
               Client.anInt2275 = 1;
               Class40.anInt3293 = 0;
               Unsorted.method1250(102, true);
            }

            if(var0 == 25 || var0 == 10) {
               Class72.method1293();
            }

            if(var0 == 5) {
               Class108.method1656(CacheIndex.spritesIndex, (byte)-111);
            } else {
               Class3_Sub13_Sub17.method247((byte)-121);
            }

            boolean var3 = 5 == Class143.gameStage || Class143.gameStage == 10 || Class143.gameStage == 28;
            if(var3 == !var2) {
               if(var2) {
                  Class129.anInt1691 = KeyboardListener.loginThemeSongArchiveID;
                  if(Unsorted.anInt120 == 0) {
                     Unsorted.method882();
                  } else {
                     Unsorted.method2099(KeyboardListener.loginThemeSongArchiveID, CacheIndex.musicIndex, 255);
                  }

                  Class58.aJs5Worker_917.sendLoginState(false);
               } else {
                  Unsorted.method882();
                  Class58.aJs5Worker_917.sendLoginState(true);
               }
            }

            if(HDToolKit.highDetail && (25 == var0 || var0 == 28 || var0 == 40)) {
               HDToolKit.method1833();
            }

            Class143.gameStage = var0;
         }
      } catch (RuntimeException var4) {
         throw ClientErrorException.clientError(var4, "pl.C(" + var0 + ',' + 5 + ')');
      }
   }

   static void method1720(boolean var0, int var1) {
      try {
         Class129.anIntArray1695 = new int[104];
         Unsorted.anIntArray1138 = new int[104];
         Class85.anInt1174 = 99;
         MouseListeningClass.anIntArray1920 = new int[104];
         byte var2;
         if(var0) {
            var2 = 1;
         } else {
            var2 = 4;
         }

         Unsorted.aByteArrayArrayArray1328 = new byte[var2][104][104];
         Unsorted.anIntArray2469 = new int[104];
         Class38_Sub1.anIntArrayArrayArray2609 = new int[var2][105][var1];
         Class67.aByteArrayArrayArray1014 = new byte[var2][105][105];
         Class158_Sub1.aByteArrayArrayArray1828 = new byte[var2][104][104];
         Class3_Sub31.anIntArray2606 = new int[104];
         PacketParser.aByteArrayArrayArray81 = new byte[var2][104][104];
         Class3_Sub13_Sub36.aByteArrayArrayArray3430 = new byte[var2][104][104];
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "pl.B(" + var0 + ',' + var1 + ')');
      }
   }

   static Class3_Sub28_Sub16_Sub2 method1722(int var0) {
      try {
         int var1 = Class140_Sub7.anIntArray2931[0] * Unsorted.anIntArray3076[0];
         byte[] var2 = Class163_Sub1.aByteArrayArray2987[0];
         int[] var3 = new int[var1];

         for(int var4 = 0; var1 > var4; ++var4) {
            var3[var4] = Class3_Sub13_Sub38.spritePalette[Unsorted.bitwiseAnd(var2[var4], 255)];
         }

         Class3_Sub28_Sub16_Sub2 var6 = new Class3_Sub28_Sub16_Sub2(Class3_Sub15.anInt2426, Class133.anInt1748, Class164.anIntArray2048[0], Unsorted.anIntArray2591[0], Class140_Sub7.anIntArray2931[0], Unsorted.anIntArray3076[0], var3);
         Class39.method1035((byte)122);
         return var0 >= -51?(Class3_Sub28_Sub16_Sub2)null:var6;
      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "pl.A(" + var0 + ')');
      }
   }

}
