package org.runite.jagex;

final class Class3_Sub28_Sub4 extends Node {

   static int[] anIntArray3565 = new int[32];
   private int[] anIntArray3566;
   int[] anIntArray3567;
   boolean aBoolean3568 = true;
   static int anInt3569;
   private int[][] anIntArrayArray3570;
   private RSString[] aClass94Array3571;
   static Class93 aClass93_3572 = new Class93(64);
   static RSString aClass94_3573 = RSString.createRSString(" )2> <col=00ffff>");
   static RSString aClass94_3574 = RSString.createRSString("titlebg");
  
   static RSString aClass94_3576 = RSString.createRSString("name_icons");
   static RSString aClass94_3577 = RSString.createRSString(": ");
   private static RSString aClass94_3578 = RSString.createRSString("Loaded title screen");
   static Class83 aClass83_3579;
 static RSString aClass94_3575 = aClass94_3578;

   final void method545(RSByteBuffer var1, int[] var2, boolean var3) {
      try {
         if(!var3) {
            if(this.anIntArray3566 != null) {
               for(int var4 = 0; ~this.anIntArray3566.length < ~var4 && ~var4 > ~var2.length; ++var4) {
                  int var5 = RuntimeException_Sub1.anIntArray2113[this.method550(89, var4)];
                  if(~var5 < -1) {
                     var1.method739(0, var5, (long)var2[var4]);
                  }
               }

            }
         }
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "cb.O(" + (var1 != null?"{...}":"null") + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   final void method546(RSByteBuffer var1, int var2) {
      try {
         while(true) {
            int var3 = var1.getByte((byte)-43);
            if(0 == var3) {
               if(var2 != -1) {
                  this.anIntArray3567 = (int[])null;
               }

               return;
            }

            this.method553(var1, var3, -14637);
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "cb.D(" + (var1 != null?"{...}":"null") + ',' + var2 + ')');
      }
   }

   public static void method547(int var0) {
      try {
         if(var0 != -2951) {
            aClass94_3576 = (RSString)null;
         }

         aClass94_3578 = null;
         aClass93_3572 = null;
         aClass94_3575 = null;
         aClass94_3574 = null;
         aClass94_3577 = null;
         aClass94_3576 = null;
         aClass94_3573 = null;
         aClass83_3579 = null;
         anIntArray3565 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "cb.F(" + var0 + ')');
      }
   }

   final void method548(int var1) {
      try {
         if(null != this.anIntArray3567) {
            for(int var2 = 0; ~this.anIntArray3567.length < ~var2; ++var2) {
               this.anIntArray3567[var2] = Class3_Sub13_Sub29.bitwiseOr(this.anIntArray3567[var2], '\u8000');
            }
         }

         if(var1 != 60) {
            this.method552(true);
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "cb.Q(" + var1 + ')');
      }
   }

   final int method549(int var1, int var2, int var3) {
      try {
         if(var1 > -99) {
            aClass94_3577 = (RSString)null;
         }

         return null != this.anIntArray3566 && var3 >= 0 && ~var3 >= ~this.anIntArray3566.length?(this.anIntArrayArray3570[var3] != null && -1 >= ~var2 && ~var2 >= ~this.anIntArrayArray3570[var3].length?this.anIntArrayArray3570[var3][var2]:-1):-1;
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "cb.P(" + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   final int method550(int var1, int var2) {
      try {
         if(null != this.anIntArray3566 && -1 >= ~var2 && var2 <= this.anIntArray3566.length) {
            if(var1 <= 33) {
               aClass94_3578 = (RSString)null;
            }

            return this.anIntArray3566[var2];
         } else {
            return -1;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "cb.S(" + var1 + ',' + var2 + ')');
      }
   }

   static final void method551(int var0, int var1, int var2) {
      try {
         if(var0 == 0) {
            if(4 == var2 && !Class128.aBoolean1685) {
               var2 = 2;
               var1 = 2;
            }

            if(~Class23.anInt453 == ~var2) {
               if(0 != var2 && var1 != Class3_Sub13_Sub21.anInt3263) {
                  Class2.anInterface5Array70[var2].method23(var1);
                  Class3_Sub13_Sub21.anInt3263 = var1;
               }
            } else {
               if(Class3_Sub13_Sub17.aBoolean3207) {
                  return;
               }

               if(~Class23.anInt453 != -1) {
                  Class2.anInterface5Array70[Class23.anInt453].method21();
               }

               if(-1 != ~var2) {
                  Interface5 var3 = Class2.anInterface5Array70[var2];
                  var3.method22();
                  var3.method23(var1);
               }

               Class23.anInt453 = var2;
               Class3_Sub13_Sub21.anInt3263 = var1;
            }

         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "cb.A(" + var0 + ',' + var1 + ',' + var2 + ')');
      }
   }

   final int method552(boolean var1) {
      try {
         return !var1?-22:(this.anIntArray3566 != null?this.anIntArray3566.length:0);
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "cb.E(" + var1 + ')');
      }
   }

   private final void method553(RSByteBuffer var1, int var2, int var3) {
      try {
         if(var3 != -14637) {
            this.method548(-20);
         }

         if(var2 != 1) {
            int var4;
            int var5;
            if(var2 != 2) {
               if(var2 != 3) {
                  if(var2 == 4) {
                     this.aBoolean3568 = false;
                  }
               } else {
                  var4 = var1.getByte((byte)-34);
                  this.anIntArray3566 = new int[var4];
                  this.anIntArrayArray3570 = new int[var4][];

                  for(var5 = 0; var4 > var5; ++var5) {
                     int var6 = var1.getShort(1);
                     this.anIntArray3566[var5] = var6;
                     this.anIntArrayArray3570[var5] = new int[Class155.anIntArray1976[var6]];

                     for(int var7 = 0; Class155.anIntArray1976[var6] > var7; ++var7) {
                        this.anIntArrayArray3570[var5][var7] = var1.getShort(1);
                     }
                  }
               }
            } else {
               var4 = var1.getByte((byte)-114);
               this.anIntArray3567 = new int[var4];

               for(var5 = 0; var5 < var4; ++var5) {
                  this.anIntArray3567[var5] = var1.getShort(1);
               }
            }
         } else {
            this.aClass94Array3571 = var1.getString().method1567(60, (byte)118);
         }

      } catch (RuntimeException var8) {
         throw Class44.method1067(var8, "cb.R(" + (var1 != null?"{...}":"null") + ',' + var2 + ',' + var3 + ')');
      }
   }

   final RSString method554(int var1) {
      try {
         if(var1 != -1) {
            return (RSString)null;
         } else {
            RSString var2 = Class47.method1090((byte)-118, 80);
            if(null == this.aClass94Array3571) {
               return Class3_Sub13_Sub29.aClass94_3357;
            } else {
               var2.method1533(this.aClass94Array3571[0], true);

               for(int var3 = 1; var3 < this.aClass94Array3571.length; ++var3) {
                  var2.method1533(GameShell.aClass94_9, true);
                  var2.method1533(this.aClass94Array3571[var3], true);
               }

               return var2.method1576((byte)90);
            }
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "cb.C(" + var1 + ')');
      }
   }

   final RSString method555(int var1, RSByteBuffer var2) {
      try {
         if(var1 != 28021) {
            this.anIntArrayArray3570 = (int[][])((int[][])null);
         }

         RSString var3 = Class47.method1090((byte)-125, 80);
         if(this.anIntArray3566 != null) {
            for(int var4 = 0; var4 < this.anIntArray3566.length; ++var4) {
               var3.method1533(this.aClass94Array3571[var4], true);
               var3.method1533(Class49.method1124(this.anIntArrayArray3570[var4], var2.method772(Class3_Sub1.anIntArray2209[this.anIntArray3566[var4]], var1 + -28021), this.anIntArray3566[var4], false), true);
            }
         }

         var3.method1533(this.aClass94Array3571[-1 + this.aClass94Array3571.length], true);
         return var3.method1576((byte)90);
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "cb.B(" + var1 + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

}
