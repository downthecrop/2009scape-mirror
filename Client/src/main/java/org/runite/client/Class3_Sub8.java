package org.runite.client;

import org.rs09.client.Linkable;
import org.rs09.client.config.GameConfig;


import java.util.Objects;

final class Class3_Sub8 extends Linkable {

    static int[] anIntArray3083 = new int[50];
    int anInt2296;
   Class64[] aClass64Array2298;
   int[] anIntArray2299;
   int[] anIntArray2300;
   int[] anIntArray2301;
   byte[][][] aByteArrayArrayArray2302;
   Class64[] aClass64Array2303;
   int anInt2305;


   static void method124(int var0, int var1, int var2) {
      try {
         if(var0 <= 23) {
            TextCore.aClass94_2306 = null;
         }

         if(Unsorted.loadInterface(var2)) {
            Unsorted.method75(GameObject.aClass11ArrayArray1834[var2], var1);
         }
      } catch (RuntimeException var4) {
         throw ClientErrorException.clientError(var4, "ed.A(" + var0 + ',' + var1 + ',' + var2 + ')');
      }
   }

   static void method126(boolean var0, int var1, int var2, int var3, int var4) {
      try {
         if(!var0) {
            if(Class101.anInt1425 <= var4 && var4 <= Class3_Sub28_Sub18.anInt3765) {
               var2 = Class40.method1040(Class57.anInt902, var2, Class159.anInt2020);
               var1 = Class40.method1040(Class57.anInt902, var1, Class159.anInt2020);
               TextureOperation15.method244(var2, var4, var1, var3);
            }

         }
      } catch (RuntimeException var6) {
         throw ClientErrorException.clientError(var6, "ed.F(" + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ')');
      }
   }

   static void method127(short[] var0, int var1, RSString[] var2, int var4) {
      try {
         if(var4 < var1) {
            int var6 = var4;
            int var5 = (var4 - -var1) / 2;
            RSString var7 = var2[var5];
            var2[var5] = var2[var1];
            var2[var1] = var7;
            short var8 = var0[var5];
            var0[var5] = var0[var1];
            var0[var1] = var8;

            for(int var9 = var4; var9 < var1; ++var9) {
               if(var7 == null || null != var2[var9] && var2[var9].method1559(var7) < (var9 & 1)) {
                  RSString var10 = var2[var9];
                  var2[var9] = var2[var6];
                  var2[var6] = var10;
                  short var11 = var0[var9];
                  var0[var9] = var0[var6];
                  var0[var6++] = var11;
               }
            }

            var2[var1] = var2[var6];
            var2[var6] = var7;
            var0[var1] = var0[var6];
            var0[var6] = var8;
            method127(var0, -1 + var6, var2, var4);
            method127(var0, var1, var2, var6 - -1);
         }

      } catch (RuntimeException var12) {
         throw ClientErrorException.clientError(var12, "ed.E(" + (var0 != null?"{...}":"null") + ',' + var1 + ',' + (var2 != null?"{...}":"null") + ',' + -909 + ',' + var4 + ')');
      }
   }

   static void method128() {
      try {
         Class44.aReferenceCache_725.clear();

      } catch (RuntimeException var2) {
         throw ClientErrorException.clientError(var2, "ed.O(" + 2 + ')');
      }
   }

   static int method129(int var0, int var2, int var3) {
      try {

         if(var0 <= 243) {
            if(var0 > 217) {
               var2 >>= 3;
            } else if(var0 <= 192) {
               if(179 < var0) {
                  var2 >>= 1;
               }
            } else {
               var2 >>= 2;
            }
         } else {
            var2 >>= 4;
         }

         return (var0 >> 1) + (var2 >> 5 << 7) + (var3 >> 2 << 10);
      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "ed.D(" + var0 + ',' + 2 + ',' + var2 + ',' + var3 + ')');
      }
   }

   static WorldListEntry getWorld(int var0, int index) {
      try {
         if(Class30.loadedWorldList && Class53.worldListOffset <= index && WorldListEntry.worldListArraySize >= index) {
            GameConfig.WORLD = index;
            return WorldListEntry.worldList[index - Class53.worldListOffset];
         } else {
            return null;
         }
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "ed.P(" + var0 + ',' + index + ')');
      }
   }

   static void method132() {
      try {
         for(int var1 = 0; var1 < Class113.anInt1552; ++var1) {
            --Unsorted.anIntArray2157[var1];
            if(Unsorted.anIntArray2157[var1] < -10) {
               --Class113.anInt1552;

               for(int var2 = var1; var2 < Class113.anInt1552; ++var2) {
                  Class3_Sub25.anIntArray2550[var2] = Class3_Sub25.anIntArray2550[var2 - -1];
                  Class102.aClass135Array2131[var2] = Class102.aClass135Array2131[var2 + 1];
                  Class166.anIntArray2068[var2] = Class166.anIntArray2068[1 + var2];
                  Unsorted.anIntArray2157[var2] = Unsorted.anIntArray2157[1 + var2];
                  anIntArray3083[var2] = anIntArray3083[var2 - -1];
               }

               --var1;
            } else {
               Class135 var11 = Class102.aClass135Array2131[var1];
               if(null == var11) {
                  var11 = Class135.method1811(CacheIndex.soundFXIndex, Class3_Sub25.anIntArray2550[var1], 0);
                  if(null == var11) {
                     continue;
                  }

                  Unsorted.anIntArray2157[var1] += var11.method1813();
                  Class102.aClass135Array2131[var1] = var11;
               }

               if(0 > Unsorted.anIntArray2157[var1]) {
                  int var3;
                  if(anIntArray3083[var1] == 0) {
                     var3 = CS2Script.anInt2453;
                  } else {
                     int var4 = 128 * (255 & anIntArray3083[var1]);
                     int var7 = anIntArray3083[var1] >> 8 & 0xFF;
                     int var5 = 255 & anIntArray3083[var1] >> 16;
                     int var8 = -Class102.player.anInt2829 + 64 + 128 * var7;
                     if(var8 < 0) {
                        var8 = -var8;
                     }

                     int var6 = -Class102.player.anInt2819 + 64 + var5 * 128;
                     if(0 > var6) {
                        var6 = -var6;
                     }

                     int var9 = -128 + var6 + var8;
                     if(var9 > var4) {
                        Unsorted.anIntArray2157[var1] = -100;
                        continue;
                     }

                     if(var9 < 0) {
                        var9 = 0;
                     }

                     var3 = Sprites.anInt340 * (var4 + -var9) / var4;
                  }

                  if(var3 > 0) {
                     Class3_Sub12_Sub1 var12 = var11.method1812().method151(Class27.aClass157_524);
                     Class3_Sub24_Sub1 var13 = Class3_Sub24_Sub1.method437(var12, var3);
                     Objects.requireNonNull(var13).method429(Class166.anIntArray2068[var1] + -1);
                     Class3_Sub26.aClass3_Sub24_Sub2_2563.method457(var13);
                  }

                  Unsorted.anIntArray2157[var1] = -100;
               }
            }
         }

         if((byte) -92 != -92) {
            method126(true, 36, 42, 14, 51);
         }

         if(Class83.aBoolean1158 && Class79.method1391(-1)) {
            if(0 != Unsorted.anInt120 && Class129.anInt1691 != -1) {
               Class70.method1285(CacheIndex.musicIndex, Class129.anInt1691, Unsorted.anInt120);
            }

            Class83.aBoolean1158 = false;
         } else if(Unsorted.anInt120 != 0 && Class129.anInt1691 != -1 && Class79.method1391((byte) -92 + 91)) {
            TextureOperation12.outgoingBuffer.putOpcode(137);
            TextureOperation12.outgoingBuffer.writeInt(Class129.anInt1691);
            Class129.anInt1691 = -1;
         }

      } catch (RuntimeException var10) {
         throw ClientErrorException.clientError(var10, "ed.C(" + (byte) -92 + ')');
      }
   }

}
