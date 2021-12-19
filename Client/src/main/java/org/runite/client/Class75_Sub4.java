package org.runite.client;

import java.util.Objects;

public final class Class75_Sub4 extends Class75 {

   static int[] anIntArray2663;
   static int[] anIntArray2664;
   private final int anInt2666;
   public static int[] incomingPacketSizes = new int[]{-1, 0, 8, 0, 2, 0, 0, 0, 0, 12, 0, 1, 0, 3, 7, 0, 15, 6, 0, 0, 4, 7, -2, -1, 2, 0, 2, 8, 0, 0, 0, 0, -2, 5, 0, 0, 8, 3, 6, 0, 0, 0, -1, 0, -1, 0, 0, 6, -2, 0, 12, 0, 0, 0, -1, -2, 10, 0, 0, 0, 3, 0, -1, 0, 0, 5, 6, 0, 0, 8, -1, -1, 0, 8, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 6, 2, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 5, 0, 0, -2, 0, 0, 0, 0, 0, 12, 2, 0, -2, -2, 20, 0, 0, 10, 0, 15, 0, -1, 0, 8, -2, 0, 0, 0, 8, 0, 12, 0, 0, 7, 0, 0, 0, 0, 0, -1, -1, 0, 4, 5, 0, 0, 0, 6, 0, 0, 0, 0, 8, 9, 0, 0, 0, 2, -1, 0, -2, 0, 4, 14, 0, 0, 0, 24, 0, -2, 5, 0, 0, 0, 10, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, 0, 2, 1, 0, 0, 2, -1, 1, 0, 0, 0, 0, 14, 0, 0, 0, 0, 10, 5, 0, 0, 0, 0, 0, -2, 0, 0, 9, 0, 0, 8, 0, 0, 0, 0, -2, 6, 0, 0, 0, -2, 0, 3, 0, 1, 7, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 3, 0, 0};
   private final int anInt2669;
   static int anInt2670 = 0;
   private final int anInt2671;
   private final int anInt2672;


   final void method1337(int var1, boolean var2, int var3) {
      try {
         int var5 = var3 * this.anInt2666 >> 12;
         int var7 = this.anInt2669 * var1 >> 12;
         int var4 = this.anInt2671 * var3 >> 12;
         int var6 = this.anInt2672 * var1 >> 12;
         if(var2) {
            Class145.method2072(this.anInt1104, var4, var6, var5, var7, this.anInt1106);
         }
      } catch (RuntimeException var8) {
         throw ClientErrorException.clientError(var8, "ta.E(" + var1 + ',' + ',' + var3 + ')');
      }
   }

   Class75_Sub4(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      super(var5, var6, var7);

      try {
         this.anInt2672 = var2;
         this.anInt2666 = var3;
         this.anInt2671 = var1;
         this.anInt2669 = var4;
      } catch (RuntimeException var9) {
         throw ClientErrorException.clientError(var9, "ta.<init>(" + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ')');
      }
   }

   final void method1341(int var2, int var3) {
      try {
         int var4 = this.anInt2671 * var2 >> 12;
         int var5 = var2 * this.anInt2666 >> 12;
         int var6 = var3 * this.anInt2672 >> 12;
         int var7 = var3 * this.anInt2669 >> 12;
         LinkableRSString.method730(var4, this.anInt1101, var7, var5, var6);

      } catch (RuntimeException var8) {
         throw ClientErrorException.clientError(var8, "ta.A(" + 2 + ',' + var2 + ',' + var3 + ')');
      }
   }

   static boolean method1351(CacheIndex var0, int var1, int archiveId) {
      try {
         byte[] var4 = var0.getFile(archiveId, var1);
         if(var4 == null) {
            return false;
         } else {
            Class45.method1082(var4, 98);
            return true;
         }
      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "ta.N(" + (var0 != null?"{...}":"null") + ',' + var1 + ',' + archiveId + ',' + -30901 + ')');
      }
   }

   final void method1335(int var1, int var2, int var3) {
      try {
         if(var3 == 4898) {
            int var4 = var2 * this.anInt2671 >> 12;
            int var6 = this.anInt2672 * var1 >> 12;
            int var5 = var2 * this.anInt2666 >> 12;
            int var7 = this.anInt2669 * var1 >> 12;
            TextureOperation21.method194(this.anInt1106, var7, this.anInt1101, this.anInt1104, var6, var5, var4);
         }
      } catch (RuntimeException var8) {
         throw ClientErrorException.clientError(var8, "ta.D(" + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   static void method1352(int var0, boolean var1, int var3, int var4) {
      try {
         if(Unsorted.loadInterface(var3)) {
            Class158.method2183(-1, var1, var4, var0, GameObject.interfaces1834[var3]);
         }
      } catch (RuntimeException var6) {
         throw ClientErrorException.clientError(var6, "ta.K(" + var0 + ',' + var1 + ',' + -1 + ',' + var3 + ',' + var4 + ')');
      }
   }

   static void method1353(SoftwareSprite[] var0, CacheIndex var2) {
      try {
         GroundItemLink.worldmapIndex_3210 = var2;
         GameObject.aSoftwareSpriteArray1839 = var0;
         Class3_Sub24_Sub4.aBooleanArray3503 = new boolean[GameObject.aSoftwareSpriteArray1839.length];
         Class134.aLinkedList_1758.clear();
         int var3 = GroundItemLink.worldmapIndex_3210.getArchiveForName(TextCore.aString_2304);
         int[] var4 = GroundItemLink.worldmapIndex_3210.getFileIds(var3);

         for(int var5 = 0; var5 < Objects.requireNonNull(var4).length; ++var5) {
            Class134.aLinkedList_1758.pushBack(Class124.method1747(new DataBuffer(GroundItemLink.worldmapIndex_3210.getFile(var3, var4[var5]))));
         }

      } catch (RuntimeException var6) {
         throw ClientErrorException.clientError(var6, "ta.B(" + (var0 != null?"{...}":"null") + ',' + -11931 + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

   static void method1354(int var0, int var1, int var3, int var4) {
      try {
         if(var4 >= Class159.anInt2020 && var4 <= Class57.anInt902) {
            var0 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var0, Class101.anInt1425);
            var3 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var3, Class101.anInt1425);
            TextureOperation14.method320(var1, var4, var3, (byte)-123, var0);
         }

      } catch (RuntimeException var6) {
         throw ClientErrorException.clientError(var6, "ta.L(" + var0 + ',' + var1 + ',' + true + ',' + var3 + ',' + var4 + ')');
      }
   }

   static void method1355() {
      try {
         Class3_Sub25.aClass129_2552.method1770();

         int var1;
         for(var1 = 0; var1 < 32; ++var1) {
            Class163_Sub1.aLongArray2986[var1] = 0L;
         }

         for(var1 = 0; var1 < 32; ++var1) {
            Class134.aLongArray1766[var1] = 0L;
         }

         Class133.anInt1754 = 0;
      } catch (RuntimeException var2) {
         throw ClientErrorException.clientError(var2, "ta.O(" + true + ')');
      }
   }

}
