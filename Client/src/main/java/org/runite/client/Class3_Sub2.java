package org.runite.client;

import org.rs09.client.Linkable;
import org.rs09.client.data.HashTable;

final class Class3_Sub2 extends Linkable {

   static int anInt2218 = -1;
   static HashTable aHashTable_2220 = new HashTable(32);
   Class25[] aClass25Array2221 = new Class25[5];
   boolean aBoolean2222;
   int anInt2223;
   boolean aBoolean2225;
   Class35 aClass35_2226;
   int anInt2227;
   int anInt2228 = 0;
   int anInt2229;
   Class12 aClass12_2230;
   int anInt2231;
   int anInt2232;
   Class19 aClass19_2233;
   Class70 aClass70_2234;
   Class3_Sub2 aClass3_Sub2_2235;
   boolean aBoolean2236;
   int[] anIntArray2237 = new int[5];
   int anInt2238;
   int anInt2239;
   Class126 aClass126_2240;
   int anInt2241;
   int anInt2244;
   Class72 aClass72_2245;

   Class3_Sub2(int var1, int var2, int var3) {
      try {
         this.anInt2231 = var3;
         this.anInt2238 = this.anInt2244 = var1;
         this.anInt2239 = var2;
      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "bj.<init>(" + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   static void method103() {
      try {
         Client.aHashTable_2194.clear();
         Class81.aClass13_1139.clear();
         Class126.aClass13_1666.clear();
      } catch (RuntimeException var2) {
         throw ClientErrorException.clientError(var2, "bj.B(" + (byte) 24 + ')');
      }
   }

   static long method104(int var0, int var1, int var2) {
      Class3_Sub2 var3 = Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2];
      return var3 != null && var3.aClass12_2230 != null?var3.aClass12_2230.aLong328:0L;
   }

}
