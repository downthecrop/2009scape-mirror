package org.runite.client;

public final class Class23 {

   static int anInt453 = 0;
   public static int canvasWidth;
   static int anInt455;
   static boolean[][] aBooleanArrayArray457;


   static void method938(int var0, int var1, int var2, int var3, int var4, int var5, int var7, int var8) {
      try {
         int var9 = var2 - var7;
         int var10 = var3 - var8;
         int var11 = (-var1 + var0 << 16) / var9;
         int var12 = (-var5 + var4 << 16) / var10;
         Class136.method1814(var1, var3, var2, var12, var7, var11, var8, var5);
      } catch (RuntimeException var13) {
         throw ClientErrorException.clientError(var13, "dl.B(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + 1 + ',' + var7 + ',' + var8 + ')');
      }
   }

   static DataBuffer method939() {
      try {
         DataBuffer var1 = new DataBuffer(34);
         var1.writeByte(11);
         var1.writeByte(Unsorted.anInt3625);
         var1.writeByte(!Unsorted.aBoolean3665?0:1);
         var1.writeByte(Unsorted.aBoolean3604?1:0);
         var1.writeByte(KeyboardListener.aBoolean1905?1:0);
         var1.writeByte(Class25.aBoolean488?1:0);
         var1.writeByte(!RSInterface.aBoolean236?0:1);
         var1.writeByte(!WorldListEntry.aBoolean2623?0:1);
         var1.writeByte(Class3_Sub13_Sub22.aBoolean3275?1:0);
         var1.writeByte(!Class140_Sub6.aBoolean2910?0:1);
         var1.writeByte(Unsorted.anInt1137);
         var1.writeByte(!Class106.aBoolean1441?0:1);
         var1.writeByte(Class128.aBoolean1685?1:0);
         var1.writeByte(Class38.aBoolean661?1:0);
         var1.writeByte(Class3_Sub28_Sub9.anInt3622);
         var1.writeByte(!Class3_Sub13_Sub15.aBoolean3184?0:1);
         var1.writeByte(CS2Script.anInt2453);
         var1.writeByte(Unsorted.anInt120);
         var1.writeByte(Sprites.anInt340);
         var1.writeShort(TextureOperation.anInt2378);
         var1.writeShort(Unsorted.anInt3071);
         var1.writeByte(Class127_Sub1.method1757());
         var1.writeInt(Unsorted.anInt2148);
         var1.writeByte(Unsorted.anInt2577);
         var1.writeByte(Unsorted.aBoolean2146?1:0);
         var1.writeByte(!Class15.aBoolean346?0:1);
         var1.writeByte(Class3_Sub20.anInt2488);
         var1.writeByte(Unsorted.aBoolean1080?1:0);
         var1.writeByte(Class163_Sub3.aBoolean3004?1:0);
         return var1;
      } catch (RuntimeException var2) {
         throw ClientErrorException.clientError(var2, "dl.C(" + (byte) -116 + ')');
      }
   }

   static void method940(int var0, int var1) {
      try {
         if(var0 >= 101) {
            MouseListeningClass var2 = Unsorted.aClass149_4047;
            synchronized(var2) {
               Unsorted.anInt4045 = var1;
            }
         }
      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "dl.D(" + var0 + ',' + var1 + ')');
      }
   }

}
