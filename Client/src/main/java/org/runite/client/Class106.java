package org.runite.client;

final class Class106 {

   static boolean aBoolean1441 = true;
   static int anInt1442 = 0;
   static int anInt1446 = 0;
   int anInt1447;
   int anInt1449;
   int anInt1450;
   static boolean paramUserUsingInternetExplorer = false;


   static void method1642(RSString var1) {
      try {
         if(null != PacketParser.aClass3_Sub19Array3694) {

             long var3 = var1.toLong();
            int var2 = 0;
            if(var3 != 0L) {
               while(PacketParser.aClass3_Sub19Array3694.length > var2 && var3 != PacketParser.aClass3_Sub19Array3694[var2].linkableKey) {
                  ++var2;
               }

               if(var2 < PacketParser.aClass3_Sub19Array3694.length && null != PacketParser.aClass3_Sub19Array3694[var2]) {
                  Class3_Sub13_Sub1.outgoingBuffer.putOpcode(162);
                  Class3_Sub13_Sub1.outgoingBuffer.writeLong(PacketParser.aClass3_Sub19Array3694[var2].linkableKey);
               }
            }
         }
      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "od.C(" + 3803 + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

}
