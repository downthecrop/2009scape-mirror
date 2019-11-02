package org.runite.jagex;

final class Class164_Sub1_Sub1 extends Class164_Sub1 {

   private byte[] aByteArray4028;


   final void method2242(int var1, byte var2) {
      int var3 = var1 * 2;
      var2 = (byte)(127 + ((var2 & 255) >> 1));
      this.aByteArray4028[var3++] = var2;
      this.aByteArray4028[var3] = var2;
   }

   public Class164_Sub1_Sub1() {
      super(12, 5, 16, 2, 2, 0.45F);
   }

   final byte[] method2243(int var1, int var2, int var3) {
      this.aByteArray4028 = new byte[var1 * var2 * var3 * 2];
      this.method2230(-95, var1, var3, var2);
      return this.aByteArray4028;
   }
}
