package org.runite.jagex;
import java.lang.ref.SoftReference;

final class Class3_Sub28_Sub7_Sub2 extends Class3_Sub28_Sub7 {

   private SoftReference aSoftReference4053;


   final Object method567(boolean var1) {
      if(!var1) {
         this.method567(true);
      }

      return this.aSoftReference4053.get();
   }

   final boolean method568(int var1) {
      if(var1 != -22358) {
         this.aSoftReference4053 = (SoftReference)null;
      }

      return true;
   }

   Class3_Sub28_Sub7_Sub2(Object var1) {
      this.aSoftReference4053 = new SoftReference(var1);
   }
}
