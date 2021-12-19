package org.runite.client;

import org.rs09.client.Node;

final class Class3_Sub28_Sub2 extends Node {

   PositionedGraphicObject aPositionedGraphicObject_3545;

   static void method535(byte var0, int var1) {
      try {
         Unsorted.aFloatArray1934[0] = (float) Unsorted.bitwiseAnd(255, var1 >> 16) / 255.0F;
         Unsorted.aFloatArray1934[1] = (float) Unsorted.bitwiseAnd(var1 >> 8, 255) / 255.0F;
         Unsorted.aFloatArray1934[2] = (float) Unsorted.bitwiseAnd(255, var1) / 255.0F;
         Unsorted.method383(-32584, 3);
         Unsorted.method383(-32584, 4);
         if(var0 != 56) {
            method535((byte)127, 99);
         }

      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "bk.A(" + var0 + ',' + var1 + ')');
      }
   }

   static Class75_Sub3 method536(DataBuffer var1) {
      try {

          return new Class75_Sub3(var1.readSignedShort(), var1.readSignedShort(), var1.readSignedShort(), var1.readSignedShort(), var1.readSignedShort(), var1.readSignedShort(), var1.readSignedShort(), var1.readSignedShort(), var1.readMedium(), var1.readUnsignedByte());
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "bk.C(" + (byte) 54 + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

   Class3_Sub28_Sub2(PositionedGraphicObject var1) {
      try {
         this.aPositionedGraphicObject_3545 = var1;
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "bk.<init>(" + (var1 != null?"{...}":"null") + ')');
      }
   }

}
