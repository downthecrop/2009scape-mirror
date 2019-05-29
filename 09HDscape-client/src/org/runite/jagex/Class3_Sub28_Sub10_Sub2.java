package org.runite.jagex;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Insets;

final class Class3_Sub28_Sub10_Sub2 extends Class3_Sub28_Sub10 {

   byte aByte4064;
   static int anInt4065;
   static RSString aClass94_4066 = RSString.createRSString("<br>");
   int anInt4067;
   static boolean aBoolean4068 = true;
   RSByteBuffer aClass3_Sub30_4069;
   static Class3_Sub2[][][] aClass3_Sub2ArrayArrayArray4070;
  
   private static RSString aClass94_4072 = RSString.createRSString(" from your friend list first)3");
   static int anInt4073;
 static RSString aClass94_4071 = aClass94_4072;

   final int method586(boolean var1) {
      try {
         return var1?92:(this.aClass3_Sub30_4069 == null?0:this.aClass3_Sub30_4069.index * 100 / (-this.aByte4064 + this.aClass3_Sub30_4069.buffer.length));
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "pm.A(" + var1 + ')');
      }
   }

   static final Class106[] method596(int var0, Signlink var1) {
      try {
         if(!var1.method1432(false)) {
            return new Class106[0];
         } else {
            Class64 var2 = var1.method1453((byte)8);

            while(0 == var2.anInt978) {
               Class3_Sub13_Sub34.method331(10L, 64);
            }

            if(2 == var2.anInt978) {
               return new Class106[0];
            } else {
               int[] var3 = (int[])((int[])var2.anObject974);
               Class106[] var4 = new Class106[var3.length >> 2];

               for(int var5 = 0; ~var4.length < ~var5; ++var5) {
                  Class106 var6 = new Class106();
                  var4[var5] = var6;
                  var6.anInt1447 = var3[var5 << 2];
                  var6.anInt1449 = var3[(var5 << 2) + 1];
                  var6.anInt1450 = var3[(var5 << 2) - -2];
                  var6.anInt1448 = var3[(var5 << 2) - -3];
               }

               if(var0 != 10) {
                  method597((byte)-74);
               }

               return var4;
            }
         }
      } catch (RuntimeException var7) {
         throw Class44.method1067(var7, "pm.P(" + var0 + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

   public static void method597(byte var0) {
      try {
         aClass94_4072 = null;
         aClass3_Sub2ArrayArrayArray4070 = (Class3_Sub2[][][])null;
         aClass94_4066 = null;
         if(var0 < 91) {
            aClass3_Sub2ArrayArrayArray4070 = (Class3_Sub2[][][])((Class3_Sub2[][][])null);
         }

         aClass94_4071 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "pm.O(" + var0 + ')');
      }
   }
   
   static final void method598(boolean var0, int var1, boolean var2, int var3, boolean var4, int var5, int var6) {
	      try {
	         if(var2) {
	            HDToolKit.method1842();
	         }

	         if(null != Class3_Sub13_Sub10.aFrame3121 && (3 != var1 || ~var5 != ~Class3_Sub13.anInt2378 || ~var6 != ~Class3_Sub13_Sub5.anInt3071)) {
	            Class3_Sub28_Sub10_Sub1.method593(Class3_Sub13_Sub10.aFrame3121, true, Class38.aClass87_665);
	            Class3_Sub13_Sub10.aFrame3121 = null;
	         }

	         if(3 == var1 && null == Class3_Sub13_Sub10.aFrame3121) {
	            Class3_Sub13_Sub10.aFrame3121 = Class99.method1597(2, 0, 0, var6, var5, Class38.aClass87_665);
	            if(null != Class3_Sub13_Sub10.aFrame3121) {
	               Class3_Sub13_Sub5.anInt3071 = var6;
	               Class3_Sub13.anInt2378 = var5;
	               Class119.method1730(Class38.aClass87_665, (byte)14);
	            }
	         }

	         if(~var1 == -4 && Class3_Sub13_Sub10.aFrame3121 == null) {
	            method598(true, anInt2577, true, var3, var4, -1, -1);
	         } else {
	            Object var7;
	            if(null == Class3_Sub13_Sub10.aFrame3121) {
	               if(null != GameShell.frame) {
	                  var7 = GameShell.frame;
	               } else {
	                  var7 = Class38.aClass87_665.anApplet1219;
	               }
	            } else {
	               var7 = Class3_Sub13_Sub10.aFrame3121;
	            }

	            Class3_Sub9.anInt2334 = ((Container)var7).getSize().width;
	            Class70.anInt1047 = ((Container)var7).getSize().height;
	            Insets var8;
	            if(GameShell.frame == var7) {
	               var8 = GameShell.frame.getInsets();
	               Class3_Sub9.anInt2334 -= var8.right + var8.left;
	               Class70.anInt1047 -= var8.bottom + var8.top;
	            }

	            if(var4) {
	               aClass94_4071 = (RSString)null;
	            }

	            if(-3 >= ~var1) {
	               Class23.anInt454 = Class3_Sub9.anInt2334;
	               Class140_Sub7.anInt2934 = Class70.anInt1047;
	               Class84.anInt1164 = 0;
	               Class106.anInt1442 = 0;
	            } else {
	               Class106.anInt1442 = 0;
	               Class84.anInt1164 = (Class3_Sub9.anInt2334 + -765) / 2;
	               Class23.anInt454 = 765;
	               Class140_Sub7.anInt2934 = 503;
	            }

	            if(!var0) {
	               if(HDToolKit.highDetail) {
	                  HDToolKit.method1854(Class23.anInt454, Class140_Sub7.anInt2934);
	               }

	               Class3_Sub28_Sub12.aCanvas3648.setSize(Class23.anInt454, Class140_Sub7.anInt2934);
	               if(GameShell.frame == var7) {
	                  var8 = GameShell.frame.getInsets();
	                  Class3_Sub28_Sub12.aCanvas3648.setLocation(var8.left - -Class84.anInt1164, var8.top + Class106.anInt1442);
	               } else {
	                  Class3_Sub28_Sub12.aCanvas3648.setLocation(Class84.anInt1164, Class106.anInt1442);
	               }
	            } else {
	               Class163_Sub1_Sub1.method2215(Class3_Sub28_Sub12.aCanvas3648, -9320);
	               Class130.method1783(4, Class3_Sub28_Sub12.aCanvas3648);
	               if(null != Class38.aClass146_668) {
	                  Class38.aClass146_668.method2082(false, Class3_Sub28_Sub12.aCanvas3648);
	               }

	               Class126.aClient1671.method30((byte)97);
	               Class3_Sub13_Sub4.method193((byte)97, Class3_Sub28_Sub12.aCanvas3648);
	               ItemDefinition.method1119(Class3_Sub28_Sub12.aCanvas3648, var4);
	               if(Class38.aClass146_668 != null) {
	                  Class38.aClass146_668.method2084(Class3_Sub28_Sub12.aCanvas3648, -103);
	               }
	            }

	            if(0 == var1 && -1 > ~var3) {
	               HDToolKit.method1834(Class3_Sub28_Sub12.aCanvas3648);
	            }

	            if(var2 && var1 > 0) {
	               Class3_Sub28_Sub12.aCanvas3648.setIgnoreRepaint(true);
	               if(!GameShell.aBoolean11) {
	                  Class32.method995();
	                  Class164_Sub1.aClass158_3009 = null;
	                  Class164_Sub1.aClass158_3009 = Class3_Sub13_Sub23_Sub1.method285(Class140_Sub7.anInt2934, Class23.anInt454, true, Class3_Sub28_Sub12.aCanvas3648);
	                  Class74.method1320();
	                  if(5 != Class143.loadingStage) {
	                     Class3_Sub13.method164((byte)-20, false, Class3_Sub13_Sub23.aClass94_3282);
	                  } else {
	                     Class3_Sub23.method406((byte)122, true, Class168.aClass3_Sub28_Sub17_2096);
	                  }

	                  try {
	                     Graphics var11 = Class3_Sub28_Sub12.aCanvas3648.getGraphics();
	                     Class164_Sub1.aClass158_3009.method2179(0, 0, var11, 0);
	                  } catch (Exception var9) {
	                     ;
	                  }

	                  Class80.method1396(-1);
	                  if(-1 != ~var3) {
	                     Class164_Sub1.aClass158_3009 = null;
	                  } else {
	                     Class164_Sub1.aClass158_3009 = Class3_Sub13_Sub23_Sub1.method285(503, 765, true, Class3_Sub28_Sub12.aCanvas3648);
	                  }

	                  Class64 var13 = Class38.aClass87_665.method1444(-43, Class126.aClient1671.getClass());

	                  while(~var13.anInt978 == -1) {
	                     Class3_Sub13_Sub34.method331(100L, 64);
	                  }

	                  if(1 == var13.anInt978) {
	                     GameShell.aBoolean11 = true;
	                  }
	               }

	               if(GameShell.aBoolean11) {
	                  HDToolKit.method1853(Class3_Sub28_Sub12.aCanvas3648, 2 * Class3_Sub28_Sub14.anInt3671);
	               }
	            }

	            if(!HDToolKit.highDetail && 0 < var1) {
	               method598(true, 0, true, var3, false, -1, -1);
	            } else {
	               if(~var1 < -1 && -1 == ~var3) {
	                  Class17.aThread409.setPriority(5);
	                  Class164_Sub1.aClass158_3009 = null;
	                  Class140_Sub1_Sub2.method1935();
	                  ((Class102)Class51.anInterface2_838).method1619(200, -1);
	                  if(Class106.aBoolean1441) {
	                     Class51.method1137(0.7F);
	                  }

	                  Class3_Sub13.method165(-7878);
	               } else if(0 == var1 && var3 > 0) {
	                  Class17.aThread409.setPriority(1);
	                  Class164_Sub1.aClass158_3009 = Class3_Sub13_Sub23_Sub1.method285(503, 765, true, Class3_Sub28_Sub12.aCanvas3648);
	                  Class140_Sub1_Sub2.method1938();
	                  Class127_Sub1.method1756();
	                  ((Class102)Class51.anInterface2_838).method1619(20, -1);
	                  if(Class106.aBoolean1441) {
	                     if(1 == Class3_Sub28_Sub10.anInt3625) {
	                        Class51.method1137(0.9F);
	                     }

	                     if(-3 == ~Class3_Sub28_Sub10.anInt3625) {
	                        Class51.method1137(0.8F);
	                     }

	                     if(3 == Class3_Sub28_Sub10.anInt3625) {
	                        Class51.method1137(0.7F);
	                     }

	                     if(-5 == ~Class3_Sub28_Sub10.anInt3625) {
	                        Class51.method1137(0.6F);
	                     }
	                  }

	                  Class3_Sub11.method144();
	                  Class3_Sub13.method165(-7878);
	               }

	               Class47.aBoolean742 = !NPC.method1986(89);
	               if(var2) {
	                  Class3_Sub20.method389(false);
	               }

	               if(~var1 <= -3) {
	                  Class3_Sub15.aBoolean2427 = true;
	               } else {
	                  Class3_Sub15.aBoolean2427 = false;
	               }

	               if(-1 != Class3_Sub28_Sub12.anInt3655) {
	                  Class124.method1746(true, (byte)-107);
	               }

	               if(null != Class3_Sub15.aClass89_2429 && (~Class143.loadingStage == -31 || Class143.loadingStage == 25)) {
	                  Class3_Sub13_Sub8.method204(-3);
	               }

	               for(int var12 = 0; var12 < 100; ++var12) {
	                  Class3_Sub28_Sub14.aBooleanArray3674[var12] = true;
	               }

	               Class3_Sub13_Sub10.aBoolean3116 = true;
	            }
	         }
	      } catch (RuntimeException var10) {
	         throw Class44.method1067(var10, "pm.F(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ')');
	      }
	   }

   final byte[] method587(boolean var1) {
      try {
         if(!this.aBoolean3632 && ~this.aClass3_Sub30_4069.index <= ~(-this.aByte4064 + this.aClass3_Sub30_4069.buffer.length)) {
            if(var1) {
               this.method586(false);
            }

            return this.aClass3_Sub30_4069.buffer;
         } else {
            throw new RuntimeException();
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "pm.E(" + var1 + ')');
      }
   }

}
