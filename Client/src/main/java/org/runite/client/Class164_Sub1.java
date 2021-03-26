package org.runite.client;

class Class164_Sub1 extends Class164 {

   private int anInt3010;
   static int anInt3012 = 0;
   private final int[] anIntArray3014;
   private byte[] aByteArray3015;
   private int anInt3016;


   final void method2237(int var1, int var2) {
      try {
         this.anInt3010 += var1 * this.anIntArray3014[var2] >> 12;

      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "vd.H(" + var1 + ',' + var2 + ',' + -20975 + ')');
      }
   }

   static void method2238(int var0, int var1, int var2, int var3, int var5) {
      try {
         Class75.anInt1105 = var2;
         Class157.anInt1996 = var1;
         Class163_Sub2_Sub1.anInt4014 = var5;
         MouseListeningClass.anInt1923 = var3;
         GraphicDefinition.anInt529 = var0;

         if(Class163_Sub2_Sub1.anInt4014 >= 100) {
            int var6 = 64 + 128 * MouseListeningClass.anInt1923;
            int var7 = 64 + Class157.anInt1996 * 128;
            int var8 = Class121.method1736(WorldListCountry.localPlane, 1, var6, var7) - GraphicDefinition.anInt529;
            int var10 = var8 + -Class7.anInt2162;
            int var9 = var6 + -NPC.anInt3995;
            int var11 = -Class77.anInt1111 + var7;
            int var12 = (int)Math.sqrt(var11 * var11 + var9 * var9);
            Class139.anInt1823 = 2047 & (int)(Math.atan2(var10, var12) * 325.949D);
            TextureOperation28.anInt3315 = 2047 & (int)(Math.atan2(var9, var11) * -325.949D);
            if(128 > Class139.anInt1823) {
               Class139.anInt1823 = 128;
            }

            if(383 < Class139.anInt1823) {
               Class139.anInt1823 = 383;
            }
         }

         Class133.anInt1753 = 2;
      } catch (RuntimeException var13) {
         throw ClientErrorException.clientError(var13, "vd.F(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + (byte) -21 + ',' + var5 + ')');
      }
   }

   static boolean method2239(int var0, int var1, int var2, int var3) {
      if(Class8.method846(var0, var1, var2)) {
         int var4 = var1 << 7;
         int var5 = var2 << 7;
         int var6 = Class44.anIntArrayArrayArray723[var0][var1][var2] - 1;
         int var7 = var6 - 120;
         int var8 = var6 - 230;
         int var9 = var6 - 238;
         if(var3 < 16) {
            if(var3 == 1) {
               if(var4 > Class145.anInt2697) {
                  if(!TextureOperation10.method349(var4, var6, var5)) {
                     return true;
                  }

                  if(!TextureOperation10.method349(var4, var6, var5 + 128)) {
                     return true;
                  }
               }

               if(var0 > 0) {
                  if(!TextureOperation10.method349(var4, var7, var5)) {
                     return true;
                  }

                  if(!TextureOperation10.method349(var4, var7, var5 + 128)) {
                     return true;
                  }
               }

               if(!TextureOperation10.method349(var4, var8, var5)) {
                  return true;
               }

               return !TextureOperation10.method349(var4, var8, var5 + 128);
            }

            if(var3 == 2) {
               if(var5 < TextureOperation13.anInt3363) {
                  if(!TextureOperation10.method349(var4, var6, var5 + 128)) {
                     return true;
                  }

                  if(!TextureOperation10.method349(var4 + 128, var6, var5 + 128)) {
                     return true;
                  }
               }

               if(var0 > 0) {
                  if(!TextureOperation10.method349(var4, var7, var5 + 128)) {
                     return true;
                  }

                  if(!TextureOperation10.method349(var4 + 128, var7, var5 + 128)) {
                     return true;
                  }
               }

               if(!TextureOperation10.method349(var4, var8, var5 + 128)) {
                  return true;
               }

               return !TextureOperation10.method349(var4 + 128, var8, var5 + 128);
            }

            if(var3 == 4) {
               if(var4 < Class145.anInt2697) {
                  if(!TextureOperation10.method349(var4 + 128, var6, var5)) {
                     return true;
                  }

                  if(!TextureOperation10.method349(var4 + 128, var6, var5 + 128)) {
                     return true;
                  }
               }

               if(var0 > 0) {
                  if(!TextureOperation10.method349(var4 + 128, var7, var5)) {
                     return true;
                  }

                  if(!TextureOperation10.method349(var4 + 128, var7, var5 + 128)) {
                     return true;
                  }
               }

               if(!TextureOperation10.method349(var4 + 128, var8, var5)) {
                  return true;
               }

               return !TextureOperation10.method349(var4 + 128, var8, var5 + 128);
            }

            if(var3 == 8) {
               if(var5 > TextureOperation13.anInt3363) {
                  if(!TextureOperation10.method349(var4, var6, var5)) {
                     return true;
                  }

                  if(!TextureOperation10.method349(var4 + 128, var6, var5)) {
                     return true;
                  }
               }

               if(var0 > 0) {
                  if(!TextureOperation10.method349(var4, var7, var5)) {
                     return true;
                  }

                  if(!TextureOperation10.method349(var4 + 128, var7, var5)) {
                     return true;
                  }
               }

               if(!TextureOperation10.method349(var4, var8, var5)) {
                  return true;
               }

               return !TextureOperation10.method349(var4 + 128, var8, var5);
            }
         }

         return !TextureOperation10.method349(var4 + 64, var9, var5 + 64) || (var3 == 16 ? !TextureOperation10.method349(var4, var8, var5 + 128) : (var3 == 32 ? !TextureOperation10.method349(var4 + 128, var8, var5 + 128) : (var3 == 64 ? !TextureOperation10.method349(var4 + 128, var8, var5) : (var3 == 128 && !TextureOperation10.method349(var4, var8, var5)))));
      } else {
         return true;
      }
   }

   static void method2241(byte var0, boolean var1) {
      try {
         Class3_Sub9 var3;
         for(var3 = (Class3_Sub9) Unsorted.aLinkedList_78.method1222(); var3 != null; var3 = (Class3_Sub9) Unsorted.aLinkedList_78.method1221()) {
            if(null != var3.aClass3_Sub24_Sub1_2312) {
               Class3_Sub26.aClass3_Sub24_Sub2_2563.method461(var3.aClass3_Sub24_Sub1_2312);
               var3.aClass3_Sub24_Sub1_2312 = null;
            }

            if(var3.aClass3_Sub24_Sub1_2315 != null) {
               Class3_Sub26.aClass3_Sub24_Sub2_2563.method461(var3.aClass3_Sub24_Sub1_2315);
               var3.aClass3_Sub24_Sub1_2315 = null;
            }

            var3.unlink();
         }

         if(var1) {
            for(var3 = (Class3_Sub9) Unsorted.aLinkedList_1242.method1222(); null != var3; var3 = (Class3_Sub9) Unsorted.aLinkedList_1242.method1221()) {
               if(null != var3.aClass3_Sub24_Sub1_2312) {
                  Class3_Sub26.aClass3_Sub24_Sub2_2563.method461(var3.aClass3_Sub24_Sub1_2312);
                  var3.aClass3_Sub24_Sub1_2312 = null;
               }

               var3.unlink();
            }

            for(var3 = (Class3_Sub9) Unsorted.aHashTable_4046.first(); null != var3; var3 = (Class3_Sub9) Unsorted.aHashTable_4046.next()) {
               if(null != var3.aClass3_Sub24_Sub1_2312) {
                  Class3_Sub26.aClass3_Sub24_Sub2_2563.method461(var3.aClass3_Sub24_Sub1_2312);
                  var3.aClass3_Sub24_Sub1_2312 = null;
               }

               var3.unlink();
            }
         }

      } catch (RuntimeException var4) {
         throw ClientErrorException.clientError(var4, "vd.G(" + var0 + ',' + var1 + ')');
      }
   }

   final void method2233() {
      try {
         this.anInt3016 = 0;
         this.anInt3010 = 0;
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "vd.C(" + -949697716 + ')');
      }
   }

   Class164_Sub1() {
      super(12, 16, 2);

      try {
         this.anIntArray3014 = new int[this.anInt2062];

         for(int var7 = 0; var7 < this.anInt2062; ++var7) {
            this.anIntArray3014[var7] = (short)((int)(Math.pow((float) 0.45, var7) * 4096.0D));
         }

      } catch (RuntimeException var8) {
         throw ClientErrorException.clientError(var8, "vd.<init>(" + 12 + ',' + 5 + ',' + 16 + ',' + 2 + ',' + 2 + ',' + (float) 0.45 + ')');
      }
   }

   void method2242(int var1, byte var2) {
      try {
         this.aByteArray3015[this.anInt3016++] = (byte)(127 + (Unsorted.bitwiseAnd(var2, 255) >> 1));
      } catch (RuntimeException var4) {
         throw ClientErrorException.clientError(var4, "vd.B(" + var1 + ',' + var2 + ')');
      }
   }

   final void method2231(byte var1) {
      try {
         this.anInt3010 = Math.abs(this.anInt3010);
         if(var1 != -92) {
            this.method2231((byte)-112);
         }

         if(this.anInt3010 >= 4096) {
            this.anInt3010 = 4095;
         }

         this.method2242(this.anInt3016++, (byte)(this.anInt3010 >> 4));
         this.anInt3010 = 0;
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "vd.A(" + var1 + ')');
      }
   }

}
