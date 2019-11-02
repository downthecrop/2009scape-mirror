package org.runite.jagex;

final class Class164_Sub2_Sub1 extends Class164_Sub2 {

   private byte[] aByteArray4029;


   public Class164_Sub2_Sub1() {
      super(8, 5, 8, 8, 2, 0.1F, 0.55F, 3.0F);
   }

   final byte[] method2250(int var1, int var2, int var3) {
      this.aByteArray4029 = new byte[var1 * var2 * var3 * 2];
      this.method2230(-98, var1, var3, var2);
      return this.aByteArray4029;
   }

   final void method2244(int var1, byte var2) {
      int var3 = var1 * 2;
      int var4 = var2 & 255;
      this.aByteArray4029[var3++] = (byte)(3 * var4 >> 5);
      this.aByteArray4029[var3] = (byte)(var4 >> 2);
   }
}
