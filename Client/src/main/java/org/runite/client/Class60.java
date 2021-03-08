package org.runite.client;

final class Class60 {

   static int anInt930;
   static int anInt932;
   static int anInt934;
   static int anInt936;

   static void method1207() {
      try {
         for(int var1 = 0; Class163.localNPCCount > var1; ++var1) {
            int var2 = Class15.localNPCIndexes[var1];
            NPC var3 = NPC.npcs[var2];
            if(null != var3) {
               Unsorted.method68(var3.definition.size, var3);
            }
         }

      } catch (RuntimeException var4) {
         throw ClientErrorException.clientError(var4, "ig.A(" + -72 + ')');
      }
   }

   static void method1208(byte var0, int var1) {
      try {
         if(-1 != var1) {
            if(Unsorted.aBooleanArray1703[var1]) {
               Unsorted.aClass153_3361.method2128(var1);
               if(null != GameObject.aClass11ArrayArray1834[var1]) {
                  boolean var2 = true;

                  for(int var3 = 0; GameObject.aClass11ArrayArray1834[var1].length > var3; ++var3) {
                     if(GameObject.aClass11ArrayArray1834[var1][var3] != null) {
                        if(GameObject.aClass11ArrayArray1834[var1][var3].type == 2) {
                           var2 = false;
                        } else {
                           GameObject.aClass11ArrayArray1834[var1][var3] = null;
                        }
                     }
                  }

                  if(var2) {
                     GameObject.aClass11ArrayArray1834[var1] = null;
                  }

                  Unsorted.aBooleanArray1703[var1] = false;
               }
            }
         }
      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "ig.B(" + var0 + ',' + var1 + ')');
      }
   }

}
