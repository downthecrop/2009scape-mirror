package org.runite.client;
import java.util.Random;

final class TextureOperation15 extends TextureOperation {

   private int anInt3191 = 2;
   private int anInt3193 = 2048;
   private int anInt3194 = 1;
   private byte[] aByteArray3195 = new byte[512];
   private int anInt3197 = 0;
   static int anInt3198 = 0;
   private short[] aShortArray3200 = new short[512];
   private int anInt3203 = 5;
   private int anInt3204 = 5;


   private void method242() {
      try {
         Random var2 = new Random(this.anInt3197);
         this.aShortArray3200 = new short[512];
          if(0 < this.anInt3193) {
             for(int var3 = 0; 512 > var3; ++var3) {
                this.aShortArray3200[var3] = (short) TextureOperation.method1603((byte)23, this.anInt3193, var2);
             }
          }

      } catch (RuntimeException var4) {
         throw ClientErrorException.clientError(var4, "hm.C(" + (byte) 37 + ')');
      }
   }

   final void postDecode() {
      try {
         this.aByteArray3195 = Class49.method1123(this.anInt3197);
         this.method242();
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "hm.P(" + ')');
      }
   }

   static int compareEnteredLanguageArgument(RSString var0) {
      try {

          for(int var2 = 0; TextureOperation4.aClass94Array3238.length > var2; ++var2) {
            if(TextureOperation4.aClass94Array3238[var2].equalsStringIgnoreCase(var0)) {
               return var2;
            }
         }

         return -1;
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "hm.F(" + (var0 != null?"{...}":"null") + ',' + (byte) 13 + ')');
      }
   }

   final int[] method154(int var1, byte var2) {
      try {
         int[] var3 = this.aClass114_2382.method1709(var1);
         if(this.aClass114_2382.aBoolean1580) {
            int var5 = this.anInt3204 * Class163_Sub3.anIntArray2999[var1] + 2048;
            int var6 = var5 >> 12;
            int var7 = var6 - -1;

            for(int var15 = 0; var15 < Class113.anInt1559; ++var15) {
               TextureOperation36.anInt3422 = Integer.MAX_VALUE;
               KeyboardListener.anInt1914 = Integer.MAX_VALUE;
               Unsorted.anInt1042 = Integer.MAX_VALUE;
               Class3_Sub28_Sub5.anInt3589 = Integer.MAX_VALUE;
               int var16 = this.anInt3203 * Class102.anIntArray2125[var15] + 2048;
               int var17 = var16 >> 12;
               int var18 = 1 + var17;

               int var19;
               for(int var9 = var6 - 1; var7 >= var9; ++var9) {
                  int var13 = 255 & this.aByteArray3195[(var9 >= this.anInt3204 ?-this.anInt3204 + var9:var9) & 0xFF];

                  for(int var8 = var17 + -1; var18 >= var8; ++var8) {
                     int var14 = (255 & this.aByteArray3195[(var8 >= this.anInt3203 ?-this.anInt3203 + var8:var8) + var13 & 0xFF]) * 2;
                     int var10 = -(var8 << 12) - (this.aShortArray3200[var14++] - var16);
                     int var11 = var5 - (this.aShortArray3200[var14] + (var9 << 12));
                     var19 = this.anInt3194;
                     int var12;
                     if(var19 == 1) {
                        var12 = var11 * var11 + var10 * var10 >> 12;
                     } else if (3 == var19) {
                         var10 = var10 < 0 ? -var10 : var10;
                         var11 = var11 >= 0 ? var11 : -var11;
                         var12 = var11 >= var10 ? var11 : var10;
                     } else if (4 == var19) {
                         var10 = (int) (Math.sqrt((float) (0 > var10 ? -var10 : var10) / 4096.0F) * 4096.0D);
                         var11 = (int) (Math.sqrt((float) (var11 >= 0 ? var11 : -var11) / 4096.0F) * 4096.0D);
                         var12 = var11 + var10;
                         var12 = var12 * var12 >> 12;
                     } else if (var19 == 5) {
                         var10 *= var10;
                         var11 *= var11;
                         var12 = (int) (Math.sqrt(Math.sqrt((float) (var11 + var10) / 1.6777216E7F)) * 4096.0D);
                     } else if (2 == var19) {
                         var12 = (var10 >= 0 ? var10 : -var10) - -(var11 < 0 ? -var11 : var11);
                     } else {
                         var12 = (int) (4096.0D * Math.sqrt((float) (var11 * var11 + var10 * var10) / 1.6777216E7F));
                     }

                     if(var12 >= Class3_Sub28_Sub5.anInt3589) {
                        if(Unsorted.anInt1042 > var12) {
                           TextureOperation36.anInt3422 = KeyboardListener.anInt1914;
                           KeyboardListener.anInt1914 = Unsorted.anInt1042;
                           Unsorted.anInt1042 = var12;
                        } else if(KeyboardListener.anInt1914 <= var12) {
                           if(var12 < TextureOperation36.anInt3422) {
                              TextureOperation36.anInt3422 = var12;
                           }
                        } else {
                           TextureOperation36.anInt3422 = KeyboardListener.anInt1914;
                           KeyboardListener.anInt1914 = var12;
                        }
                     } else {
                        TextureOperation36.anInt3422 = KeyboardListener.anInt1914;
                        KeyboardListener.anInt1914 = Unsorted.anInt1042;
                        Unsorted.anInt1042 = Class3_Sub28_Sub5.anInt3589;
                        Class3_Sub28_Sub5.anInt3589 = var12;
                     }
                  }
               }

               var19 = this.anInt3191;
               if(var19 == 0) {
                  var3[var15] = Class3_Sub28_Sub5.anInt3589;
               } else if(var19 == 1) {
                  var3[var15] = Unsorted.anInt1042;
               } else if (var19 == 3) {
                   var3[var15] = KeyboardListener.anInt1914;
               } else if (var19 == 4) {
                   var3[var15] = TextureOperation36.anInt3422;
               } else if (var19 == 2) {
                   var3[var15] = Unsorted.anInt1042 + -Class3_Sub28_Sub5.anInt3589;
               }
            }
         }

         return var3;
      } catch (RuntimeException var20) {
         throw ClientErrorException.clientError(var20, "hm.D(" + var1 + ',' + var2 + ')');
      }
   }

   static void method244(int var1, int var2, int var3, int var4) {
      try {
         int var5;
         if(var3 >= var1) {
            for(var5 = var1; var5 < var3; ++var5) {
               Class38.anIntArrayArray663[var5][var2] = var4;
            }
         } else {
            for(var5 = var3; var1 > var5; ++var5) {
               Class38.anIntArrayArray663[var5][var2] = var4;
            }
         }

      } catch (RuntimeException var6) {
         throw ClientErrorException.clientError(var6, "hm.E(" + 2 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ')');
      }
   }

   final void decode(int var1, DataBuffer var2) {
      try {
         if(var1 == 0) {
            this.anInt3203 = this.anInt3204 = var2.readUnsignedByte();
         } else if(var1 == 1) {
            this.anInt3197 = var2.readUnsignedByte();
         } else if(2 == var1) {
            this.anInt3193 = var2.readUnsignedShort();
         } else if (var1 == 3) {
             this.anInt3191 = var2.readUnsignedByte();
         } else if (var1 == 4) {
             this.anInt3194 = var2.readUnsignedByte();
         } else if (var1 == 5) {
             this.anInt3203 = var2.readUnsignedByte();
         } else if (var1 == 6) {
             this.anInt3204 = var2.readUnsignedByte();
         }

      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "hm.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + true + ')');
      }
   }

   public TextureOperation15() {
      super(0, true);
   }

}
