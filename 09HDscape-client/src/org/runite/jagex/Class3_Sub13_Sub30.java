package org.runite.jagex;

import org.runite.Properties;

final class Class3_Sub13_Sub30 extends Class3_Sub13 {

   static int anInt3362 = -1;
   static int anInt3363;
   static Class61 aClass61_3364 = new Class61();
   static int anInt3365;
   static long aLong3366;
   static int[] anIntArray3367 = new int[64];
   static RSString aClass94_3368 = RSString.createRSString(":clanreq:");
   
   
   /**
    * Gets the properly colored string for the combat level difference.
    * 
    * @param their 	
    * 			their combat level
    * @param my 	
    * 			my combat level 
    * @returns
    * 			the annoying RSString.
    */
   static final RSString getProperColor(int their, int my){
	   if((my - their) < 10){
		   return RSString.createRSString("<col=ff3000>");
	   } else if (my == their){
		   return RSString.createRSString("<col=ffff00>");
	   } else if((my - their) < 3 && (my - their) > 6){
		   return RSString.createRSString("<col=ff9040>");
	   } else if((my - their) > 1 && (my - their) < 3){
		   return RSString.createRSString("<col=c0ff00>");
	   } else if((my - their) > 3){
		   return RSString.createRSString("<col=40ff00>");
	   }
	   return RSString.createRSString("<col=00ff80>");
   }


   static final void method312(int var0, int var1, int var2, Player var3, int var4) {
      try {
         if(Class102.player != var3) {
            if(Class3_Sub13_Sub34.anInt3415 < 400) {
               RSString var5;
               if(-1 == ~var3.anInt3974) {
                  boolean var6 = true;
                  if(~Class102.player.anInt3970 != 0 && -1 != var3.anInt3970) {
                     int var7 = var3.COMBAT_LEVEL < Class102.player.COMBAT_LEVEL?Class102.player.COMBAT_LEVEL:var3.COMBAT_LEVEL;
                     int var8 = ~Class102.player.anInt3970 > ~var3.anInt3970?Class102.player.anInt3970:var3.anInt3970;
                     int var9 = 5 - -(var7 * 10 / 100) + var8;
                     int var10 = -var3.COMBAT_LEVEL + Class102.player.COMBAT_LEVEL;
                     if(0 > var10) {
                        var10 = -var10;
                     }

                     if(var9 < var10) {
                        var6 = false;
                     }
                  }

                  RSString levelEquals = Class158.anInt2014 != 1?Class27.aClass94_525:Class3_Sub22.aClass94_2526;
                  if(var3.COMBAT_LEVEL < var3.anInt3965) {
                     var5 = RenderAnimationDefinition.method903(new RSString[]{var3.getName(0), var6?Class72.method1295(var3.COMBAT_LEVEL, (byte)-73, Class102.player.COMBAT_LEVEL):Class3_Sub26.CONTEXT_MENU_COLOR , Class72.LEFT_PARENTHESES, levelEquals, Class72.method1298((byte)9, var3.COMBAT_LEVEL), Class40.aClass94_673, Class72.method1298((byte)9, var3.anInt3965 + -var3.COMBAT_LEVEL), Class3_Sub9.RIGHT_PARENTHESES}, (byte)-127);
                  } else {
                	 //here
                     var5 = RenderAnimationDefinition.method903(new RSString[]{var3.getName(0), var6 ? Class72.method1295(var3.COMBAT_LEVEL, (byte)-128, Class102.player.COMBAT_LEVEL) : getProperColor(var3.COMBAT_LEVEL, Class102.player.COMBAT_LEVEL), Class72.LEFT_PARENTHESES, levelEquals, Class72.method1298((byte)9, var3.COMBAT_LEVEL), Class3_Sub9.RIGHT_PARENTHESES}, (byte)-70);
                  }
               } else {
                  var5 = RenderAnimationDefinition.method903(new RSString[]{var3.getName(0), Class72.LEFT_PARENTHESES, Class27.aClass94_522, Class72.method1298((byte)9, var3.anInt3974), Class3_Sub9.RIGHT_PARENTHESES}, (byte)-119);
               }

               int var12;
               if(~Class164_Sub1.anInt3012 == -2) {
                  Class54.method1177(Class99.anInt1403, (long)var0, (byte)-80, RenderAnimationDefinition.method903(new RSString[]{RenderAnimationDefinition.aClass94_378, Class56.aClass94_892, var5}, (byte)-125), var4, (short)1, Class3_Sub13_Sub32.aClass94_3388, var2);
                  ++Class129_Sub1.anInt2684;
               } else if(!GameObject.aBoolean1837) {
                  for(var12 = 7; -1 >= ~var12; --var12) {
                     if(null != Class91.aClass94Array1299[var12]) {
                        ++Class55.anInt880;
                        short var14 = 0;
                        boolean var16 = false;
                        if(~Class158.anInt2014 == -1 && Class91.aClass94Array1299[var12].equals(-123, Class3_Sub13_Sub36.aClass94_3427)) {
                         //If other player level greater than my level, then right click to attack.
                           if(var3.COMBAT_LEVEL > Class102.player.COMBAT_LEVEL && !Properties.get().isInstantAttack()) {
                              var14 = 2000;//Var for right click higher level players
                           }
                           if(-1 != ~Class102.player.teamId && ~var3.teamId != -1) {
                              if(~Class102.player.teamId != ~var3.teamId) {
                                 var14 = 0;
                              } else {
                                 var14 = 2000;
                              }
                           }
                        } else if(Class1.aBooleanArray54[var12]) {
                           var14 = 2000;
                        }

                        short var15 = Class7.aShortArray2167[var12];
                        var15 += var14;
                        Class54.method1177(Class3_Sub13_Sub26.anIntArray3328[var12], (long)var0, (byte)-73, RenderAnimationDefinition.method903(new RSString[]{Class3_Sub26.CONTEXT_MENU_COLOR , var5}, (byte)-78), var4, var15, Class91.aClass94Array1299[var12], var2);
                     }
                  }
               } else if((8 & Class164.anInt2051) != 0) {
                  ++Class3_Sub13_Sub13.anInt3151;
                  Class54.method1177(Class144.anInt1887, (long)var0, (byte)-58, RenderAnimationDefinition.method903(new RSString[]{Class40.aClass94_676, Class56.aClass94_892, var5}, (byte)-116), var4, (short)15, Class3_Sub28_Sub9.aClass94_3621, var2);
               }

               if(var1 <= 0) {
                  aLong3366 = -79L;
               }

               for(var12 = 0; ~Class3_Sub13_Sub34.anInt3415 < ~var12; ++var12) {
                  if(~Class3_Sub13_Sub7.aShortArray3095[var12] == -61) {
                     Class163_Sub2_Sub1.aClass94Array4016[var12] = RenderAnimationDefinition.method903(new RSString[]{Class3_Sub26.CONTEXT_MENU_COLOR , var5}, (byte)-75);
                     break;
                  }
               }

            }
         }
      } catch (RuntimeException var11) {
         throw Class44.method1067(var11, "rj.C(" + var0 + ',' + var1 + ',' + var2 + ',' + (var3 != null?"{...}":"null") + ',' + var4 + ')');
      }
   }

   static final void method313(byte var0) {
      try {
         if(var0 <= 51) {
            method312(77, -52, -42, (Player)null, 120);
         }

         Class166.method2257(96);
         Class3_Sub8.method128(2);
         Class163_Sub2_Sub1.method2220(0);
         Class3_Sub10.method139(69);
         Class3_Sub26.method512((byte)-108);
         Class104.method1626((byte)-128);
         Class3_Sub13_Sub3.method182(true);
         Class145.method2077(true);
         Class25.method959(0);
         Class3_Sub28_Sub19.method716(14073);
         Class3_Sub15.method370((byte)-73);
         Class3_Sub13_Sub29.method304(6799);
         Class40.method1045(-19761);
         Class3_Sub13_Sub3.method183(-108);
         Class3_Sub29.method727(91);
         Class3_Sub21.method397((byte)-41);
         if(~Class3_Sub13_Sub13.anInt3148 != -1) {
            for(int var1 = 0; ~Class3_Sub6.aByteArrayArray2287.length < ~var1; ++var1) {
               Class3_Sub6.aByteArrayArray2287[var1] = null;
            }

            Class56.anInt893 = 0;
         }

         Class108.method1659(16712751);
         Class3_Sub10.method142(true);
         Class80.aClass93_1135.method1524(3);
         if(!HDToolKit.highDetail) {
            ((Class102)Class51.anInterface2_838).method1618(0);
         }

         Class56.aClass47_885.method1101(2);
         Class75_Sub3.aClass153_2660.method2137((byte)56);
         Class3_Sub28_Sub19.aClass153_3772.method2137((byte)56);
         Class140_Sub3.aClass153_2727.method2137((byte)56);
         Class146.aClass153_1902.method2137((byte)56);
         Class3_Sub13_Sub6.aClass153_3077.method2137((byte)56);
         Class75_Sub2.aClass153_2645.method2137((byte)56);
         Class159.aClass153_2019.method2137((byte)56);
         Class140_Sub6.spritesCacheIndex.method2137((byte)56);
         Class3_Sub13_Sub25.aClass153_3304.method2137((byte)56);
         Node.aClass153_2573.method2137((byte)56);
         Class3_Sub1.interfaceScriptsIndex.method2137((byte)56);
         Class3_Sub13_Sub11.aClass93_3130.method1524(3);
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "rj.E(" + var0 + ')');
      }
   }

   private final int method314(int var1, int var2, int var3) {
      try {
         if(var2 != 7001) {
            this.method314(-83, 92, 48);
         }

         int var4 = var3 - -(57 * var1);
         var4 ^= var4 << 1;
         return 4096 + -((var4 * (var4 * var4 * 15731 - -789221) - -1376312589 & Integer.MAX_VALUE) / 262144);
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "rj.O(" + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   public Class3_Sub13_Sub30() {
      super(0, true);
   }

   public static void method315(int var0) {
      try {
         aClass61_3364 = null;
         anIntArray3367 = null;
         if(var0 != -15028) {
            aClass61_3364 = (Class61)null;
         }

         aClass94_3368 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "rj.B(" + var0 + ')');
      }
   }

   final int[] method154(int var1, byte var2) {
      try {
         int var4 = 104 % ((30 - var2) / 36);
         int[] var3 = this.aClass114_2382.method1709(-16409, var1);
         if(this.aClass114_2382.aBoolean1580) {
            int var5 = Class163_Sub3.anIntArray2999[var1];

            for(int var6 = 0; var6 < Class113.anInt1559; ++var6) {
               var3[var6] = this.method314(var5, 7001, Class102.anIntArray2125[var6]) % 4096;
            }
         }

         return var3;
      } catch (RuntimeException var7) {
         throw Class44.method1067(var7, "rj.D(" + var1 + ',' + var2 + ')');
      }
   }

   static final void parseObjectMapping(Class91[] var0, int var1, byte[] var2, int var3, int var4, int var5, int var6, boolean var7, int var8, int var9, byte var10) {
      try {
         if(var10 == -54) {
            int var12 = -1;
            RSByteBuffer var11 = new RSByteBuffer(var2);

            while(true) {
               int var13 = var11.method773((byte)-127);
               if(~var13 == -1) {
                  return;
               }

               var12 += var13;
               int var14 = 0;

               while(true) {
                  int var15 = var11.getSmart(true);
                  if(~var15 == -1) {
                     break;
                  }

                  var14 += -1 + var15;
                  int var16 = 63 & var14;
                  int var17 = var14 >> 6 & 63;
                  int var18 = var14 >> 12;
                  int var19 = var11.getByte((byte)-51);
                  int var20 = var19 >> 2;
                  int var21 = 3 & var19;
                  if(~var3 == ~var18 && ~var17 <= ~var8 && var17 < 8 + var8 && var9 <= var16 && 8 + var9 > var16) {
                     ObjectDefinition var22 = Class162.getObjectDefinition(4, var12);
                     int var23 = Class3_Sub7.method121(var16 & 7, var4, var21, var22.anInt1485, var22.anInt1480, 1, 7 & var17) + var5;
                     int var24 = GameObject.method1863(var22.anInt1480, var4, (byte)126, var22.anInt1485, 7 & var17, var21, 7 & var16) + var6;
                     if(~var23 < -1 && var24 > 0 && -104 < ~var23 && var24 < 103) {
                        Class91 var25 = null;
                        if(!var7) {
                           int var26 = var1;
                           if(2 == (Class9.aByteArrayArrayArray113[1][var23][var24] & 2)) {
                              var26 = var1 - 1;
                           }

                           if(var26 >= 0) {
                              var25 = var0[var26];
                           }
                        }

                        Class110.method1683(var1, !var7, var1, var7, var25, var12, var20, var23, (byte)50, var24, 3 & var21 + var4);
                     }
                  }
               }
            }
         }
      } catch (RuntimeException var27) {
         throw Class44.method1067(var27, "rj.F(" + (var0 != null?"{...}":"null") + ',' + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ',' + var9 + ',' + var10 + ')');
      }
   }

}
