package org.runite.client;
import org.rs09.client.Node;
import org.rs09.client.data.HashTable;

import java.util.Calendar;

final class Class3_Sub28_Sub9 extends Node {

   int anInt3614;
   static Calendar aCalendar3616 = Calendar.getInstance();
   private int anInt3617;
   RSString aClass94_3619;
   static int anInt3620 = 0;
   static RSString aClass94_3621 = null;
   static int anInt3622 = 0;
   static int anInt3623 = 0;
   static int anInt3624;


   static SoftwareSprite method578() {
      try {
         int var1 = Unsorted.anIntArray3076[0] * Class140_Sub7.anIntArray2931[0];
         byte[] var2 = Class163_Sub1.aByteArrayArray2987[0];

         Object var3;
         if(Class45.aBooleanArray3272[0]) {
            byte[] var4 = Class163_Sub3.aByteArrayArray3005[0];
            int[] var5 = new int[var1];

            for(int var6 = 0; var6 < var1; ++var6) {
               var5[var6] = TextureOperation3.bitwiseOr(Unsorted.bitwiseAnd(var4[var6] << 24, -16777216), TextureOperation38.spritePalette[Unsorted.bitwiseAnd(255, var2[var6])]);
            }

            var3 = new Class3_Sub28_Sub16_Sub2_Sub1(Class3_Sub15.anInt2426, Class133.anInt1748, Class164.anIntArray2048[0], Unsorted.anIntArray2591[0], Class140_Sub7.anIntArray2931[0], Unsorted.anIntArray3076[0], var5);
         } else {
            int[] var8 = new int[var1];

            for(int var9 = 0; var9 < var1; ++var9) {
               var8[var9] = TextureOperation38.spritePalette[Unsorted.bitwiseAnd(var2[var9], 255)];
            }

            var3 = new SoftwareSprite(Class3_Sub15.anInt2426, Class133.anInt1748, Class164.anIntArray2048[0], Unsorted.anIntArray2591[0], Class140_Sub7.anIntArray2931[0], Unsorted.anIntArray3076[0], var8);
         }

         Class39.method1035((byte)127);
         return (SoftwareSprite)var3;
      } catch (RuntimeException var7) {
         throw ClientErrorException.clientError(var7, "hn.P(" + 115 + ')');
      }
   }

   private void method579(int var1, DataBuffer var2) {
      try {
         if(var1 == 1) {
            this.anInt3617 = var2.readUnsignedByte();
         } else if (var1 == 2) {
            this.anInt3614 = var2.readInt();
         } else if (var1 == 5) {
            this.aClass94_3619 = var2.readString();
         }

      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "hn.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + 0 + ')');
      }
   }

   static void method580(byte var0) {
      try {
         if(var0 != 80) {
            method582(88, 85, -8, true, 72, 12, 29, 96, 6, 57, -13, 15);
         }

         Class3_Sub2.aHashTable_2220 = new HashTable(32);
      } catch (RuntimeException var2) {
         throw ClientErrorException.clientError(var2, "hn.B(" + var0 + ')');
      }
   }

   static void method581(CacheIndex var0, CacheIndex var2) {
      try {
         FontType.smallFont = Unsorted.method1300(Sprites.p11FullSpriteArchive, var2, var0);
         if(HDToolKit.highDetail) {
            Class157.aClass3_Sub28_Sub17_Sub1_2000 = Class70.method1287(Sprites.p11FullSpriteArchive, var0, var2);
         } else {
            Class157.aClass3_Sub28_Sub17_Sub1_2000 = (Class3_Sub28_Sub17_Sub1) FontType.smallFont;
         }

         FontType.plainFont = Unsorted.method1300(Sprites.p12FullSpriteArchive, var2, var0);
         FontType.bold = Unsorted.method1300(Sprites.b12FullSpriteArchive, var2, var0);
      } catch (RuntimeException var4) {
         throw ClientErrorException.clientError(var4, "hn.D(" + (var0 != null?"{...}":"null") + ',' + 0 + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

   static boolean method582(int var0, int var1, int var2, boolean var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11) {
      try {
         return Class102.player.getSize() != var5?(Class102.player.getSize() <= 2?Class158_Sub1.method2191(var6, var4, var11, var10, var9, var2, var1, var3, var8, var0, var7):Class52.method1166(var10, var7, var9, var1, Class102.player.getSize(), var6, var8, var4, var11, var2, var3, var0)): Unsorted.method76(var7, var8, var4, var0, var10, var3, var2, var1, var6, var9, var11);
      } catch (RuntimeException var13) {
         throw ClientErrorException.clientError(var13, "hn.O(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ',' + var9 + ',' + var10 + ',' + var11 + ')');
      }
   }

   final void method583(DataBuffer var2) {
      try {
         while(true) {
            int var3 = var2.readUnsignedByte();
            if(var3 == 0) {
               return;
            }

            this.method579(var3, var2);
         }
      } catch (RuntimeException var4) {
         throw ClientErrorException.clientError(var4, "hn.C(" + 207 + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

   final boolean method585() {
      try {

         return this.anInt3617 == 115;
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "hn.E(" + 0 + ')');
      }
   }

}
