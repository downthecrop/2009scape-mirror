package org.runite.jagex;

final class Class93 {

   static int anInt1325;
   static RSString aClass94_1326 = RSString.createRSString(")2");
   private int anInt1327;
   static byte[][][] aByteArrayArrayArray1328;
   private NodeList aClass13_1329 = new NodeList();
   static int anInt1330;
   private int anInt1331;
   private Class130 aClass130_1332;


   final void put(byte var1, Object var2, long var3) {
      try {
         this.method1518(var3, (byte)-124);
         if(var1 > -72) {
            this.aClass130_1332 = (Class130)null;
         }

         if(-1 == ~this.anInt1327) {
            Class3_Sub28_Sub7 var5 = (Class3_Sub28_Sub7)this.aClass13_1329.method877(-1);
            var5.method86(-1024);
            var5.method524((byte)-107);
         } else {
            --this.anInt1327;
         }

         Class3_Sub28_Sub7_Sub1 var7 = new Class3_Sub28_Sub7_Sub1(var2);
         this.aClass130_1332.method1779(1, var7, var3);
         this.aClass13_1329.method879(var7, (byte)-19);
         var7.aLong2569 = 0L;
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "n.F(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   static final void method1516(int var0, int var1) {
      try {
         Class3_Sub28_Sub6 var2 = Class3_Sub24_Sub3.method466(4, 3, var0);
         var2.a(true);
         if(var1 < 87) {
            aClass94_1326 = (RSString)null;
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "n.L(" + var0 + ',' + var1 + ')');
      }
   }

  /** static final void method1517(byte var0) {
      try {
         Class139.aFontMetrics1822 = null;
         Class3_Sub13_Sub32.aFont3384 = null;
         if(var0 != -118) {
            method1516(64, 82);
         }

         Class129_Sub1.anImage2695 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "n.J(" + var0 + ')');
      }
   }**/
   
   static final void method1517(byte var0) {
	      try {
	         Class139.aFontMetrics1822 = null;
	         Class3_Sub13_Sub32.aFont3384 = null;
	         if(var0 != -118) {
	            method1516(64, 82);
	         }

	         //MillisTimer.anImage2695 = null;
	      } catch (RuntimeException var2) {
	         throw Class44.method1067(var2, "n.J(" + var0 + ')');
	      }
	   }

   final void method1518(long var1, byte var3) {
      try {
         Class3_Sub28_Sub7 var4 = (Class3_Sub28_Sub7)this.aClass130_1332.method1780(var1, 0);
         if(var3 == -124) {
            if(null != var4) {
               var4.method86(-1024);
               var4.method524((byte)-107);
               ++this.anInt1327;
            }

         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "n.C(" + var1 + ',' + var3 + ')');
      }
   }

   static int method1519(int var0, int var1) {
      try {
         return var0 ^ var1;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "n.E(" + var0 + ',' + var1 + ')');
      }
   }

   final int method1520(int var1) {
      try {
         if(var1 != -12623) {
            return -85;
         } else {
            int var2 = 0;

            for(Class3_Sub28_Sub7 var3 = (Class3_Sub28_Sub7)this.aClass13_1329.method876((byte)70); var3 != null; var3 = (Class3_Sub28_Sub7)this.aClass13_1329.method878(var1 + 12744)) {
               if(!var3.method568(-22358)) {
                  ++var2;
               }
            }

            return var2;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "n.D(" + var1 + ')');
      }
   }

   public static void method1521(int var0) {
      try {
         aByteArrayArrayArray1328 = (byte[][][])null;
         aClass94_1326 = null;
         if(var0 != 3101) {
            method1516(99, -14);
         }

      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "n.A(" + var0 + ')');
      }
   }

   final void method1522(int var1, int var2) {
      try {
         if(null != Class3_Sub28_Sub20.aClass118_3794) {
            for(Class3_Sub28_Sub7 var3 = (Class3_Sub28_Sub7)this.aClass13_1329.method876((byte)42); null != var3; var3 = (Class3_Sub28_Sub7)this.aClass13_1329.method878(-48)) {
               if(!var3.method568(-22358)) {
                  if(++var3.aLong2569 > (long)var2) {
                     Class3_Sub28_Sub7 var4 = Class3_Sub28_Sub20.aClass118_3794.method1725(var3, -108);
                     this.aClass130_1332.method1779(1, var4, var3.aLong71);
                     Class45.method1084(var3, var4, (byte)121);
                     var3.method86(-1024);
                     var3.method524((byte)-107);
                  }
               } else if(null == var3.method567(true)) {
                  var3.method86(-1024);
                  var3.method524((byte)-107);
                  ++this.anInt1327;
               }
            }
         }

         if(var1 > -124) {
            this.method1518(24L, (byte)-18);
         }

      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "n.G(" + var1 + ',' + var2 + ')');
      }
   }

   final void method1523(byte var1) {
      try {
         for(Class3_Sub28_Sub7 var2 = (Class3_Sub28_Sub7)this.aClass13_1329.method876((byte)52); var2 != null; var2 = (Class3_Sub28_Sub7)this.aClass13_1329.method878(-43)) {
            if(var2.method568(-22358)) {
               var2.method86(-1024);
               var2.method524((byte)-107);
               ++this.anInt1327;
            }
         }

         if(var1 > -98) {
            this.anInt1331 = 56;
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "n.B(" + var1 + ')');
      }
   }

   final void method1524(int var1) {
      try {
         if(var1 == 3) {
            this.aClass13_1329.method883(17126);
            this.aClass130_1332.method1773(114);
            this.anInt1327 = this.anInt1331;
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "n.H(" + var1 + ')');
      }
   }

   Class93(int var1) {
      try {
         this.anInt1331 = var1;

         int var2;
         for(var2 = 1; var2 + var2 < var1; var2 += var2) {
            ;
         }

         this.anInt1327 = var1;
         this.aClass130_1332 = new Class130(var2);
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "n.<init>(" + var1 + ')');
      }
   }

   static final void method1525(int var0, int var1, int var2, int var3, int var4, int var5) {
      try {
         int var6 = Class40.method1040(Class57.anInt902, var3, (byte)0, Class159.anInt2020);
         int var7 = Class40.method1040(Class57.anInt902, var5, (byte)0, Class159.anInt2020);
         int var8 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var4, (byte)0, Class101.anInt1425);
         if(var0 != 3074) {
            method1516(75, -7);
         }

         int var9 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var2, (byte)0, Class101.anInt1425);

         for(int var10 = var6; var10 <= var7; ++var10) {
            Class3_Sub13_Sub23_Sub1.method282(Class38.anIntArrayArray663[var10], var8, var0 + -2974, var9, var1);
         }

      } catch (RuntimeException var11) {
         throw Class44.method1067(var11, "n.I(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ')');
      }
   }

   final Object get(long key, byte var3) {
      try {
         Class3_Sub28_Sub7 var4 = (Class3_Sub28_Sub7)this.aClass130_1332.method1780(key, var3 + -121);
         if(var3 != 121) {
            return (Object)null;
         } else if(null == var4) {
            return null;
         } else {
            Object var5 = var4.method567(true);
            if(var5 != null) {
               if(var4.method568(-22358)) {
                  Class3_Sub28_Sub7_Sub1 var6 = new Class3_Sub28_Sub7_Sub1(var5);
                  this.aClass130_1332.method1779(1, var6, var4.aLong71);
                  this.aClass13_1329.method879(var6, (byte)84);
                  var6.aLong2569 = 0L;
                  var4.method86(-1024);
                  var4.method524((byte)-107);
               } else {
                  this.aClass13_1329.method879(var4, (byte)-127);
                  var4.aLong2569 = 0L;
               }

               return var5;
            } else {
               var4.method86(-1024);
               var4.method524((byte)-107);
               ++this.anInt1327;
               return null;
            }
         }
      } catch (RuntimeException var7) {
         throw Class44.method1067(var7, "n.K(" + key + ',' + var3 + ')');
      }
   }

}
