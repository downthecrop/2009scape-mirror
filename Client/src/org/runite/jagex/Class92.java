package org.runite.jagex;

import javax.media.opengl.GL;

final class Class92 {

   static float[] aFloatArray1312 = new float[4];
   private static int anInt1313 = -1;
   static int anInt1314;
   static int anInt1315;
   static int anInt1316 = 13156520;
   private static float aFloat1317 = -1.0F;
   private static float aFloat1318 = -1.0F;
   static float[] aFloatArray1319 = new float[4];
   private static float aFloat1320;
   private static float[] aFloatArray1321 = new float[4];
   static int anInt1322 = 16777215;
   private static int anInt1323 = -1;
   private static int anInt1324 = -1;


   static final void method1504() {
      GL var0 = HDToolKit.gl;
      var0.glLightfv(16384, 4611, aFloatArray1312, 0);
      var0.glLightfv(16385, 4611, aFloatArray1321, 0);
   }

   static final float method1505() {
      return aFloat1317;
   }

   static final void method1506(int var0, float var1, float var2, float var3) {
      if(anInt1313 != var0 || aFloat1320 != var1 || aFloat1317 != var2 || aFloat1318 != var3) {
         anInt1313 = var0;
         aFloat1320 = var1;
         aFloat1317 = var2;
         aFloat1318 = var3;
         javax.media.opengl.GL var4 = HDToolKit.gl;
         float var5 = (float)(var0 >> 16 & 255) / 255.0F;
         float var6 = (float)(var0 >> 8 & 255) / 255.0F;
         float var7 = (float)(var0 & 255) / 255.0F;
         float[] var8 = new float[]{var1 * var5, var1 * var6, var1 * var7, 1.0F};
         var4.glLightModelfv(2899, var8, 0);
         float[] var9 = new float[]{var2 * var5, var2 * var6, var2 * var7, 1.0F};
         var4.glLightfv(16384, 4609, var9, 0);
         float[] var10 = new float[]{-var3 * var5, -var3 * var6, -var3 * var7, 1.0F};
         var4.glLightfv(16385, 4609, var10, 0);
      }
   }

   public static void method1507() {
      aFloatArray1312 = null;
      aFloatArray1321 = null;
      aFloatArray1319 = null;
   }

   static final void method1508(int var0, int var1) {
      if(anInt1324 != var0 || anInt1323 != var1) {
         anInt1324 = var0;
         anInt1323 = var1;
         javax.media.opengl.GL var2 = HDToolKit.gl;
         byte var3 = 50;
         short var4 = 3584;
         aFloatArray1319[0] = (float)(var0 >> 16 & 255) / 255.0F;
         aFloatArray1319[1] = (float)(var0 >> 8 & 255) / 255.0F;
         aFloatArray1319[2] = (float)(var0 & 255) / 255.0F;
         var2.glFogi(2917, 9729);
         var2.glFogf(2914, 0.95F);
         var2.glHint(3156, 4353);
         int var5 = var4 / 2/* - 512 - var1*/;
         if(var5 < var3) {
            var5 = var3;
         }

         var2.glFogf(2915, (float)var5);
         var2.glFogf(2916, (float)(var4 - 256));
         var2.glFogfv(2918, aFloatArray1319, 0);
      }
   }

   static final void method1509(float var0, float var1, float var2) {
      if(aFloatArray1312[0] != var0 || aFloatArray1312[1] != var1 || aFloatArray1312[2] != var2) {
         aFloatArray1312[0] = var0;
         aFloatArray1312[1] = var1;
         aFloatArray1312[2] = var2;
         aFloatArray1321[0] = -var0;
         aFloatArray1321[1] = -var1;
         aFloatArray1321[2] = -var2;
         anInt1314 = (int)(var0 * 256.0F / var1);
         anInt1315 = (int)(var2 * 256.0F / var1);
      }
   }

   static final int method1510() {
      return anInt1313;
   }

   static final void method1511() {
      javax.media.opengl.GL var0 = HDToolKit.gl;
      var0.glColorMaterial(1028, 5634);
      var0.glEnable(2903);
      float[] var1 = new float[]{0.0F, 0.0F, 0.0F, 1.0F};
      var0.glLightfv(16384, 4608, var1, 0);
      var0.glEnable(16384);
      float[] var2 = new float[]{0.0F, 0.0F, 0.0F, 1.0F};
      var0.glLightfv(16385, 4608, var2, 0);
      var0.glEnable(16385);
      anInt1313 = -1;
      anInt1324 = -1;
      method1513();
   }

   static final void method1512(float[] var0) {
      if(var0 == null) {
         var0 = aFloatArray1319;
      }

      javax.media.opengl.GL var1 = HDToolKit.gl;
      var1.glFogfv(2918, var0, 0);
   }

   private static final void method1513() {
      method1506(anInt1322, 1.1523438F, 0.69921875F, 1.2F);
      method1509(-50.0F, -60.0F, -50.0F);
      method1508(anInt1316, 0);
   }

   static final float method1514() {
      return aFloat1320;
   }

}
