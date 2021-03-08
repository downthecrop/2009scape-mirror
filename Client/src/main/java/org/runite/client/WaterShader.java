package org.runite.client;




import javax.media.opengl.GL;
import java.nio.ByteBuffer;


final class WaterShader implements ShaderInterface {

   static int anInt3285 = 128;
   private final float[] aFloatArray2190 = new float[4];
   private static boolean aBoolean2191 = false;
   private int anInt2192 = -1;
   private int anInt2193 = -1;


   public WaterShader() {
      if(HDToolKit.maxTextureUnits >= 2) {
         int[] textures = new int[1];
         byte[] pixels = new byte[8];
         int pixelsPos = 0;
         while (pixelsPos < 8) {
            pixels[pixelsPos] = (byte) (96 + ++pixelsPos * 159 / 8);
         }
//         for(int var3 = 0; var3 < 8; pixels[var3++] = (byte)(96 + var3 * 159 / 8)) {
//         }
         GL var4 = HDToolKit.gl;
         var4.glGenTextures(1, textures, 0);
         var4.glBindTexture(3552, textures[0]);//TEXTURE_1D
         //                TEXTURE_1D, level0, ALPHA, width8, border0, ALPHA, UNSIGNED_BYTE, pixels.
         var4.glTexImage1D(3552, 0, 6406, 8, 0, 6406, 5121, ByteBuffer.wrap(pixels));
         var4.glTexParameteri(3552, 10241, 9729);//TEXTURE_1D, TEXTURE_MIN_FILTER, LINEAR
         var4.glTexParameteri(3552, 10240, 9729);//TEXTURE_1D, TEXTURE_MAG_FILTER, LINEAR
         var4.glTexParameteri(3552, 10242, '\u812f');//TEXTURE_1D, TEXTURE_WRAP_S, CLAMP_TO_EDGE
         this.anInt2192 = textures[0];
         aBoolean2191 = HDToolKit.maxTextureUnits > 2 && HDToolKit.allows3DTextureMapping;
         this.method2251();
      }

   }


   private void method2251() {
      GL var1 = HDToolKit.gl;
      this.anInt2193 = var1.glGenLists(2);
      var1.glNewList(this.anInt2193, 4864);
      var1.glActiveTexture('\u84c1');
      if(aBoolean2191) {
         var1.glBindTexture('\u806f', Class88.anInt1228);
         var1.glTexEnvi(8960, '\u8571', 260);
         var1.glTexEnvi(8960, '\u8590', 768);
         var1.glTexEnvi(8960, '\u8572', 7681);
         var1.glTexEnvi(8960, '\u8588', '\u8578');
         var1.glTexGeni(8192, 9472, 9216);
         var1.glTexGeni(8194, 9472, 9216);
         var1.glTexGeni(8193, 9472, 9216);
         var1.glTexGeni(8195, 9472, 9217);
         var1.glTexGenfv(8195, 9473, new float[]{0.0F, 0.0F, 0.0F, 1.0F}, 0);
         var1.glEnable(3168);
         var1.glEnable(3169);
         var1.glEnable(3170);
         var1.glEnable(3171);
         var1.glEnable('\u806f');
         var1.glActiveTexture('\u84c2');
         var1.glTexEnvi(8960, 8704, '\u8570');
      }

      var1.glBindTexture(3552, this.anInt2192);
      var1.glTexEnvi(8960, '\u8571', '\u8575');
      var1.glTexEnvi(8960, '\u8580', '\u8576');
      var1.glTexEnvi(8960, '\u8582', 5890);
      var1.glTexEnvi(8960, '\u8572', 7681);
      var1.glTexEnvi(8960, '\u8588', '\u8578');
      var1.glTexGeni(8192, 9472, 9216);
      var1.glEnable(3552);
      var1.glEnable(3168);
      var1.glActiveTexture('\u84c0');
      var1.glEndList();
      var1.glNewList(this.anInt2193 + 1, 4864);
      var1.glActiveTexture('\u84c1');
      if(aBoolean2191) {
         var1.glTexEnvi(8960, '\u8571', 8448);
         var1.glTexEnvi(8960, '\u8590', 768);
         var1.glTexEnvi(8960, '\u8572', 8448);
         var1.glTexEnvi(8960, '\u8588', 5890);
         var1.glDisable(3168);
         var1.glDisable(3169);
         var1.glDisable(3170);
         var1.glDisable(3171);
         var1.glDisable('\u806f');
         var1.glActiveTexture('\u84c2');
         var1.glTexEnvi(8960, 8704, 8448);
      }

      var1.glTexEnvfv(8960, 8705, new float[]{0.0F, 1.0F, 0.0F, 1.0F}, 0);
      var1.glTexEnvi(8960, '\u8571', 8448);
      var1.glTexEnvi(8960, '\u8580', 5890);
      var1.glTexEnvi(8960, '\u8582', '\u8576');
      var1.glTexEnvi(8960, '\u8572', 8448);
      var1.glTexEnvi(8960, '\u8588', 5890);
      var1.glDisable(3552);
      var1.glDisable(3168);
      var1.glActiveTexture('\u84c0');
      var1.glEndList();
   }

   static int method2252() {
      return aBoolean2191?'\u84c2':'\u84c1';
   }

   static void method2253() {
      GL var0 = HDToolKit.gl;
      var0.glClientActiveTexture(method2252());
      var0.glDisableClientState('\u8078');
      var0.glClientActiveTexture('\u84c0');
   }

   public final void method22() {
      GL var1 = HDToolKit.gl;
      var1.glCallList(this.anInt2193);
   }

   static void method2254() {
      GL var0 = HDToolKit.gl;
      var0.glClientActiveTexture(method2252());
      var0.glEnableClientState('\u8078');
      var0.glClientActiveTexture('\u84c0');
   }

   public final int method24() {
      return 0;
   }

   public final void method21() {
      GL var1 = HDToolKit.gl;
      var1.glCallList(this.anInt2193 + 1);
   }

   public final void method23(int var1) {
      GL var2 = HDToolKit.gl;
      var2.glActiveTexture('\u84c1');
      if(!aBoolean2191 && var1 < 0) {
         var2.glDisable(3168);
      } else {
         var2.glPushMatrix();
         var2.glLoadIdentity();
         var2.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
         var2.glRotatef((float)Class140_Sub7.anInt2938 * 360.0F / 2048.0F, 1.0F, 0.0F, 0.0F);
         var2.glRotatef((float)Class3_Sub13_Sub8.anInt3103 * 360.0F / 2048.0F, 0.0F, 1.0F, 0.0F);
         var2.glTranslatef((float)(-Unsorted.anInt144), (float)(-Unsorted.anInt3695), (float)(-LinkableRSString.anInt2587));
         if(aBoolean2191) {
            this.aFloatArray2190[0] = 0.0010F;
            this.aFloatArray2190[1] = 9.0E-4F;
            this.aFloatArray2190[2] = 0.0F;
            this.aFloatArray2190[3] = 0.0F;
            var2.glTexGenfv(8192, 9474, this.aFloatArray2190, 0);
            this.aFloatArray2190[0] = 0.0F;
            this.aFloatArray2190[1] = 9.0E-4F;
            this.aFloatArray2190[2] = 0.0010F;
            this.aFloatArray2190[3] = 0.0F;
            var2.glTexGenfv(8193, 9474, this.aFloatArray2190, 0);
            this.aFloatArray2190[0] = 0.0F;
            this.aFloatArray2190[1] = 0.0F;
            this.aFloatArray2190[2] = 0.0F;
            this.aFloatArray2190[3] = (float)HDToolKit.anInt1791 * 0.0050F;
            var2.glTexGenfv(8194, 9474, this.aFloatArray2190, 0);
            var2.glActiveTexture('\u84c2');
         }

         var2.glTexEnvfv(8960, 8705, Class72.method1297(), 0);
         if(var1 >= 0) {
            this.aFloatArray2190[0] = 0.0F;
            this.aFloatArray2190[1] = 1.0F / (float) anInt3285;
            this.aFloatArray2190[2] = 0.0F;
            this.aFloatArray2190[3] = 1.0F * (float)var1 / (float) anInt3285;
            var2.glTexGenfv(8192, 9474, this.aFloatArray2190, 0);
            var2.glEnable(3168);
         } else {
            var2.glDisable(3168);
         }

         var2.glPopMatrix();
      }

      var2.glActiveTexture('\u84c0');
   }



}
