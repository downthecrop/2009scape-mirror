package org.runite.client;

final class PcmSound extends Sound {

   byte[] aByteArray3030;
   boolean aBoolean3031;
   int anInt3032;
   int anInt3033;
   int anInt3034;


   final PcmSound method151(Class157 var1) {
      this.aByteArray3030 = var1.method2173(this.aByteArray3030);
      this.anInt3034 = var1.method2177(this.anInt3034);
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
      this.anInt3034 = 22050;
      this.aByteArray3030 = var2;
      this.anInt3033 = var3;
      this.anInt3032 = var4;
   }

   PcmSound(int var1, byte[] var2, int var3, int var4, boolean var5) {
      this.anInt3034 = var1;
      this.aByteArray3030 = var2;
      this.anInt3033 = var3;
      this.anInt3032 = var4;
      this.aBoolean3031 = var5;
   }
}
