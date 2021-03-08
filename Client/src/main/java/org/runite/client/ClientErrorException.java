package org.runite.client;

final class ClientErrorException extends RuntimeException {

   static int[] anIntArray2113 = new int[]{2, 2, 4, 0, 1, 8, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0};
   static Class96[] aClass96Array2114 = new Class96[4];
   static int anInt2115 = -1;
   String aString2117;
   Throwable aThrowable2118;
   static RSString[] aClass94Array2119 = null;
   static int anInt2120 = 0;
   static String worldListHost;


   static void method2285(int var0, int var1, int var2, int var3, int var5) {
      try {
         LinkableRSString.anInt2587 = var1;
         Class3_Sub13_Sub8.anInt3103 = var5;
         Class140_Sub7.anInt2938 = var0;
         Unsorted.anInt144 = var3;
         Unsorted.anInt3695 = var2;
      } catch (RuntimeException var7) {
         throw clientError(var7, "ld.B(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + false + ',' + var5 + ')');
      }
   }

   ClientErrorException(Throwable var1, String var2) {
      this.aString2117 = var2;
      this.aThrowable2118 = var1;
   }

   static int method2287(int var0, byte var1) {
      try {
         return (var0 < 97 || var0 > 122) && (var0 < 224 || var0 > 254 || 247 == var0)?(var0 != 255 ?(var0 != 156?(var1 != 59?72:var0):140):159):var0 + -32;
      } catch (RuntimeException var3) {
         throw clientError(var3, "ld.C(" + var0 + ',' + var1 + ')');
      }
   }

    static ClientErrorException clientError(Throwable var0, String var1) {
        //var0.printStackTrace();
        //var0.printStackTrace(); Uncomment to be spammed when clan chat is used cuz clanchat breaks the text script.
        ClientErrorException var2;
        if(var0 instanceof ClientErrorException) {
            var2 = (ClientErrorException)var0;
            var2.aString2117 = var2.aString2117 + ' ' + var1;
        } else {
            var2 = new ClientErrorException(var0, var1);
        }

        return var2;
    }
}
