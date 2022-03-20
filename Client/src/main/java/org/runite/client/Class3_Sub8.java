package org.runite.client;

import org.rs09.client.Linkable;
import org.rs09.client.config.GameConfig;


import java.util.Objects;

final class Class3_Sub8 extends Linkable {

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
            TextCore.aString_2306 = null;
         }

         if(Unsorted.loadInterface(var2)) {
            Unsorted.method75(GameObject.interfaces1834[var2], var1);
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

}
