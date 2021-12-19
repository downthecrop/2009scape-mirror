package org.runite.client;



import org.rs09.client.Node;


import javax.media.opengl.GL;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

final class Class3_Sub28_Sub18 extends Node {

   private int anInt3759;
   private final int anInt3760;
   static int anInt3764;
   static int anInt3765 = 100;
   private int anInt3767 = 0;
   static int[] anIntArray3768 = new int[100];
   static boolean aBoolean3769 = false;


   protected final void finalize() throws Throwable {
      try {
         if(this.anInt3759 != -1) {
            Class31.method985(this.anInt3759, this.anInt3767, this.anInt3760);
            this.anInt3759 = -1;
            this.anInt3767 = 0;
         }

         super.finalize();
      } catch (RuntimeException var2) {
         throw ClientErrorException.clientError(var2, "sd.finalize()");
      }
   }

   static void method709(int var0, int var1) {
      TileData var2 = TileData.aTileDataArrayArrayArray2638[0][var0][var1];

      for(int var3 = 0; var3 < 3; ++var3) {
         TileData var4 = TileData.aTileDataArrayArrayArray2638[var3][var0][var1] = TileData.aTileDataArrayArrayArray2638[var3 + 1][var0][var1];
         if(var4 != null) {
            --var4.anInt2244;

            for(int var5 = 0; var5 < var4.anInt2223; ++var5) {
               Class25 var6 = var4.aClass25Array2221[var5];
               if((var6.aLong498 >> 29 & 3L) == 2L && var6.anInt483 == var0 && var6.anInt478 == var1) {
                  --var6.anInt493;
               }
            }
         }
      }

      if(TileData.aTileDataArrayArrayArray2638[0][var0][var1] == null) {
         TileData.aTileDataArrayArrayArray2638[0][var0][var1] = new TileData(0, var0, var1);
      }

      TileData.aTileDataArrayArrayArray2638[0][var0][var1].aClass3_Sub2_2235 = var2;
      TileData.aTileDataArrayArrayArray2638[3][var0][var1] = null;
   }

//   static void method710() {
//      try {
//         Class44.aReferenceCache_725.clearSoftReferences();
//      } catch (RuntimeException var2) {
//         throw ClientErrorException.clientError(var2, "sd.C(" + (byte) 126 + ')');
//      }
//   }

   final void method712() {
      try {
         int var2 = Class27.method961();
         if((1 & var2) == 0) {
            HDToolKit.bindTexture2D(this.anInt3759);
         }

         if(0 == (var2 & 2)) {
            HDToolKit.method1856(0);
         }

         if((var2 & 4) == 0) {
            HDToolKit.method1847(0);
         }
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "sd.E(" + (byte) 6 + ')');
      }
   }

   Class3_Sub28_Sub18(int var1) {
      try {
         GL var2 = HDToolKit.gl;
         int[] var3 = new int[1];
         var2.glGenTextures(1, var3, 0);
         this.anInt3759 = var3[0];
         this.anInt3760 = Class31.anInt582;
         HDToolKit.bindTexture2D(this.anInt3759);
         int var4 = Class51.anIntArray834[var1];
         byte[] var5 = new byte[]{(byte)(var4 >> 16), (byte)(var4 >> 8), (byte)var4, (byte)-1};
         ByteBuffer var6 = ByteBuffer.wrap(var5);
         var2.glTexImage2D(3553, 0, 6408, 1, 1, 0, 6408, 5121, var6);
         var2.glTexParameteri(3553, 10241, 9729);
         var2.glTexParameteri(3553, 10240, 9729);
         Class31.anInt580 += var6.limit() - this.anInt3767;
         this.anInt3767 = var6.limit();
      } catch (RuntimeException var7) {
         throw ClientErrorException.clientError(var7, "sd.<init>(" + var1 + ')');
      }
   }

}
