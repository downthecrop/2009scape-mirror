package org.runite.jagex;

final class Class129_Sub2 extends Class129 {

   private long aLong2698 = System.nanoTime();


   final void method1770(int var1) {
      int var2 = 37 / ((-82 - var1) / 42);
      this.aLong2698 = System.nanoTime();
   }

   final int method1767(int var1, int var2, int var3) {
      if(var1 != -1) {
         return 37;
      } else {
         long var4 = 1000000L * (long)var2;
         long var6 = this.aLong2698 - System.nanoTime();
         if(~var4 < ~var6) {
            var6 = var4;
         }

         Class3_Sub13_Sub34.method331(var6 / 1000000L, 64);
         int var10 = 0;

         long var8;
         for(var8 = System.nanoTime(); var10 < 10 && (-2 < ~var10 || this.aLong2698 < var8); this.aLong2698 += 1000000L * (long)var3) {
            ++var10;
         }

         if(var8 > this.aLong2698) {
            this.aLong2698 = var8;
         }

         return var10;
      }
   }

}
