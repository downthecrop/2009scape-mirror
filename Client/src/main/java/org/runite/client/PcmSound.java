package org.runite.client;

public final class PcmSound extends Sound {

   public byte[] samples;
   boolean aBoolean3031;
   int anInt3032;
   int anInt3033;
   public int frequency;


   final PcmSound method151(Class157 var1) {
      this.samples = var1.method2173(this.samples);
      this.frequency = var1.method2177(this.frequency);
      if(this.anInt3033 == this.anInt3032) {
         this.anInt3033 = this.anInt3032 = var1.method2178(this.anInt3033);
      } else {
         this.anInt3033 = var1.method2178(this.anInt3033);
         this.anInt3032 = var1.method2178(this.anInt3032);
         if(this.anInt3033 == this.anInt3032) {
            --this.anInt3033;
         }
      }

      return this;
   }

   PcmSound(byte[] var2, int var3, int var4) {
      this.frequency = 22050;
      this.samples = var2;
      this.anInt3033 = var3;
      this.anInt3032 = var4;
   }

   PcmSound(int var1, byte[] var2, int var3, int var4, boolean var5) {
      this.frequency = var1;
      this.samples = var2;
      this.anInt3033 = var3;
      this.anInt3032 = var4;
      this.aBoolean3031 = var5;
   }
}
