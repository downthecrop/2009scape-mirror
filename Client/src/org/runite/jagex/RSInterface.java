package org.runite.jagex;

final class RSInterface {

   boolean hidden = false;
   Object[] anObjectArray156;
   boolean aBoolean157;
   static long aLong1489;
   Object[] anObjectArray158;
   Object[] anObjectArray159;
   int anInt160 = 1;
   Object[] anObjectArray161;
   byte aByte162 = 0;
   boolean aBoolean163;
   int anInt164 = 100;
   Object[] anObjectArray165;
   int y;
   boolean aBoolean167;
   int anInt168;
   short aShort169 = 3000;
   Object[] anObjectArray170;
   RSString[] aClass94Array171;
   RSString aClass94_172;
   RSString[] options;
   Object[] anObjectArray174;
   int[] anIntArray175;
   Object[] anObjectArray176;
   int width;
   boolean aBoolean178;
   int anInt179 = 0;
   Object[] anObjectArray180;
   boolean aBoolean181 = false;
   int anInt182 = 0;
   Object[] anObjectArray183;
   int anInt184;
   int[] anIntArray185;
   boolean aBoolean186 = false;
   int type;
   boolean aBoolean188 = false;
   int anInt189;
   int parentId;
   int anInt191 = -1;
   int anInt192;
   int anInt193 = 0;
   int anInt194 = 0;
   boolean aBoolean195;
   private int secondModelId;
   int[] anIntArray197;
   int secondAnimationId = -1;
   boolean aBoolean199;
   boolean aBoolean200;
   int itemId;
   int modelType;
   Object[] anObjectArray203;
   int anInt204;
   int anInt205 = 0;
   Object[] anObjectArray206;
   int[] anIntArray207;
   int anInt208 = 0;
   static RSString aClass94_209 = RSString.createRSString("event_opbase");
   int anInt210 = 0;
   int[] anIntArray211;
   int anInt212;
   int anInt213;
   int anInt214 = 0;
   boolean aBoolean215;
   int anInt216;
   Object[] anObjectArray217;
   int anInt218;
   boolean aBoolean219;
   Object[] anObjectArray220;
   Object[] anObjectArray221;
   int anInt222;
   int anInt223;
   int spriteArchiveId = -1;
   int anInt225;
   boolean aBoolean226 = false;
   boolean aBoolean227;
   int anInt228;
   Object[] anObjectArray229;
   int anInt230 = 0;
   byte[] aByteArray231;
   RSString aClass94_232;
   boolean usingScripts;
   int anInt234;
   Object[] anObjectArray235;
   static boolean aBoolean236 = true;
   int anInt237;
   int anInt238 = -1;
   Object[] anObjectArray239;
   int anInt240;
   byte aByte241;
   int anInt242;
   RSString aClass94_243;
   int height;
   RSString aClass94_245;
   static float aFloat246;
   int anInt247;
   Object[] anObjectArray248;
   int[] anIntArray249;
   int anInt250 = 1;
   static RSString aClass94_251 = null;
   int anInt252;
   int anInt253;
   int[] itemAmounts;
   int anInt255;
   Object[] anObjectArray256;
   Class3_Sub1 aClass3_Sub1_257;
   int anInt258;
   int anInt259;
   int anInt260;
   static long aLong261 = 0L;
   RSInterface[] aClass11Array262;
   byte[] aByteArray263;
   int anInt264;
   int anInt265;
   int anInt266;
   int anInt267;
   Object[] anObjectArray268;
   Object[] anObjectArray269;
   int anInt270;
   int anInt271;
   int[] anIntArray272;
   byte aByte273;
   int[] anIntArray274;
   int[] anIntArray275;
   Object[] anObjectArray276;
   RSString aClass94_277;
   static int anInt278 = -1;
   int anInt279;
   int anInt280;
   Object[] anObjectArray281;
   Object[] anObjectArray282;
   int anInt283;
   int anInt284;
   int anInt285;
   int[] anIntArray286;
   int anInt287;
   int anInt288;
   RSString aClass94_289;
   int anInt290;
   int[] anIntArray291;
   int anInt292;
   short aShort293;
   private int secondModelType;
   Object[] anObjectArray295;
   int anInt296;
   static RSString aClass94_297 = RSString.createRSString("Nehmen");
   int[][] childDataBuffers;
   int[] anIntArray299;
   int[] anIntArray300;
   int anInt301;
   RSInterface aClass11_302;
   Object[] anObjectArray303;
   byte aByte304;
   int animationId;
   int anInt306;
   int[] anIntArray307;
   int anInt308;
   boolean aBoolean309;
   int[] anIntArray310;
   int anInt311;
   int anInt312;
   Object[] anObjectArray313;
   Object[] anObjectArray314;
   Object[] anObjectArray315;
   int x;
   int[] itemIds;
   int anInt318;


   final void method854(int var1, int var2, byte var3) {
      try {
         if(this.anIntArray249 == null || ~this.anIntArray249.length >= ~var1) {
            int[] var4 = new int[1 + var1];
            if(this.anIntArray249 != null) {
               int var5;
               for(var5 = 0; this.anIntArray249.length > var5; ++var5) {
                  var4[var5] = this.anIntArray249[var5];
               }

               for(var5 = this.anIntArray249.length; ~var1 < ~var5; ++var5) {
                  var4[var5] = -1;
               }
            }

            this.anIntArray249 = var4;
         }

         this.anIntArray249[var1] = var2;
         if(var3 != 43) {
            this.anIntArray211 = (int[])null;
         }

      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "be.P(" + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   final boolean method855(int var1) {
      try {
         if(this.anIntArray207 != null) {
            return true;
         } else {
            LDIndexedSprite var2 = RSString.method1539(0, true, this.spriteArchiveId, Class12.aClass153_323);
            if(null == var2) {
               return false;
            } else {
               var2.method1675();
               this.anIntArray207 = new int[var2.anInt1468];
               this.anIntArray291 = new int[var2.anInt1468];
               int var3 = 0;

               while(~var3 > ~var2.anInt1468) {
                  int var4 = 0;
                  int var5 = var2.anInt1461;
                  int var6 = 0;

                  while(true) {
                     if(~var6 > ~var2.anInt1461) {
                        if(-1 == ~var2.aByteArray2674[var2.anInt1461 * var3 + var6]) {
                           ++var6;
                           continue;
                        }

                        var4 = var6;
                     }

                     for(var6 = var4; var2.anInt1461 > var6; ++var6) {
                        if(0 == var2.aByteArray2674[var3 * var2.anInt1461 + var6]) {
                           var5 = var6;
                           break;
                        }
                     }

                     this.anIntArray207[var3] = var4;
                     this.anIntArray291[var3] = var5 - var4;
                     ++var3;
                     break;
                  }
               }

               if(var1 != -30721) {
                  this.anInt205 = -68;
               }

               return true;
            }
         }
      } catch (RuntimeException var7) {
         throw Class44.method1067(var7, "be.G(" + var1 + ')');
      }
   }

   static final RSString method856(boolean var0) {
      try {
         if(!var0) {
            method869(127, -68);
         }

         RSString var1 = Class3_Sub28_Sub7_Sub1.aClass94_4052;
         RSString var2 = Class3_Sub28_Sub14.aClass94_3672;
         if(-1 != ~Class44.anInt718) {
            var1 = Player.aClass94_3971;
         }

         if(null != Class163_Sub2.aClass94_2996) {
            var2 = RenderAnimationDefinition.method903(new RSString[]{Class3_Sub28_Sub11.aClass94_3637, Class163_Sub2.aClass94_2996}, (byte)-64);
         }

         return RenderAnimationDefinition.method903(new RSString[]{Class30.aClass94_577, var1, Class3_Sub28_Sub7.aClass94_3601, Class72.method1298((byte)9, Class3_Sub20.language), Class151.aClass94_1932, Class72.method1298((byte)9, Class3_Sub26.anInt2554), var2, Class140_Sub3.aClass94_2735}, (byte)-61);
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "be.N(" + var0 + ')');
      }
   }

   final void method857(byte var1, RSString var2, int var3) {
      try {
         if(null == this.aClass94Array171 || ~this.aClass94Array171.length >= ~var3) {
            RSString[] var4 = new RSString[1 + var3];
            if(null != this.aClass94Array171) {
               for(int var5 = 0; ~this.aClass94Array171.length < ~var5; ++var5) {
                  var4[var5] = this.aClass94Array171[var5];
               }
            }

            this.aClass94Array171 = var4;
         }

         this.aClass94Array171[var3] = var2;
         int var7 = -124 % ((-10 - var1) / 60);
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "be.B(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   final void decodeNoScripts(int var1, RSByteBuffer var2) {
      try {
         if(var1 >= -94) {
            this.anInt214 = -74;
         }

         this.usingScripts = false;
         this.type = var2.getByte((byte)-33);
         this.anInt318 = var2.getByte((byte)-35);
         this.anInt189 = var2.getShort(1);
         this.x = var2.getShort((byte)100);
         this.y = var2.getShort((byte)109);
         this.width = var2.getShort(1);
         this.height = var2.getShort(1);
         this.aByte304 = 0;
         this.aByte241 = 0;
         this.aByte273 = 0;
         this.aByte162 = 0;
         this.anInt223 = var2.getByte((byte)-120);
         this.parentId = var2.getShort(1);
         if(~this.parentId != -65536) {
            this.parentId += -65536 & this.anInt279;
         } else {
            this.parentId = -1;
         }

         this.anInt212 = var2.getShort(1);
         if(-65536 == ~this.anInt212) {
            this.anInt212 = -1;
         }

         int var3 = var2.getByte((byte)-98);
         int var4;
         if(-1 > ~var3) {
            this.anIntArray307 = new int[var3];
            this.anIntArray275 = new int[var3];

            for(var4 = 0; ~var3 < ~var4; ++var4) {
               this.anIntArray275[var4] = var2.getByte((byte)-102);
               this.anIntArray307[var4] = var2.getShort(1);
            }
         }

         var4 = var2.getByte((byte)-46);
         int var5;
         int var6;
         int var7;
         if(-1 > ~var4) {
            this.childDataBuffers = new int[var4][];

            for(var5 = 0; ~var4 < ~var5; ++var5) {
               var6 = var2.getShort(1);
               this.childDataBuffers[var5] = new int[var6];

               for(var7 = 0; ~var7 > ~var6; ++var7) {
                  this.childDataBuffers[var5][var7] = var2.getShort(1);
                  if(~this.childDataBuffers[var5][var7] == -65536) {
                     this.childDataBuffers[var5][var7] = -1;
                  }
               }
            }
         }

         if(-1 == ~this.type) {
            this.anInt252 = var2.getShort(1);
            this.hidden = 1 == var2.getByte((byte)-67);
         }

         if(~this.type == -2) {
            var2.getShort(1);
            var2.getByte((byte)-67);
         }

         var5 = 0;
         if(~this.type == -3) {
            this.aByte241 = 3;
            this.itemIds = new int[this.width * this.height];
            this.itemAmounts = new int[this.height * this.width];
            this.aByte304 = 3;
            var6 = var2.getByte((byte)-58);
            var7 = var2.getByte((byte)-115);
            if(~var6 == -2) {
               var5 |= 268435456;
            }

            int var8 = var2.getByte((byte)-97);
            if(~var7 == -2) {
               var5 |= 1073741824;
            }

            if(1 == var8) {
               var5 |= Integer.MIN_VALUE;
            }

            int var9 = var2.getByte((byte)-102);
            if(var9 == 1) {
               var5 |= 536870912;
            }

            this.anInt285 = var2.getByte((byte)-125);
            this.anInt290 = var2.getByte((byte)-50);
            this.anIntArray300 = new int[20];
            this.anIntArray272 = new int[20];
            this.anIntArray197 = new int[20];

            int var10;
            for(var10 = 0; 20 > var10; ++var10) {
               int var11 = var2.getByte((byte)-48);
               if(var11 == 1) {
                  this.anIntArray272[var10] = var2.getShort((byte)110);
                  this.anIntArray300[var10] = var2.getShort((byte)58);
                  this.anIntArray197[var10] = var2.getInt();
               } else {
                  this.anIntArray197[var10] = -1;
               }
            }

            this.options = new RSString[5];

            for(var10 = 0; var10 < 5; ++var10) {
               RSString var14 = var2.getString();
               if(~var14.length(-28) < -1) {
                  this.options[var10] = var14;
                  var5 |= 1 << 23 - -var10;
               }
            }
         }

         if(3 == this.type) {
            this.aBoolean226 = 1 == var2.getByte((byte)-106);
         }

         if(this.type == 4 || 1 == this.type) {
            this.anInt194 = var2.getByte((byte)-31);
            this.anInt225 = var2.getByte((byte)-23);
            this.anInt205 = var2.getByte((byte)-35);
            this.anInt270 = var2.getShort(1);
            if(~this.anInt270 == -65536) {
               this.anInt270 = -1;
            }

            this.aBoolean215 = 1 == var2.getByte((byte)-114);
         }

         if(this.type == 4) {
            this.aClass94_232 = var2.getString();
            this.aClass94_172 = var2.getString();
         }

         if(this.type == 1 || this.type == 3 || 4 == this.type) {
            this.anInt218 = var2.getInt();
         }

         if(~this.type == -4 || ~this.type == -5) {
            this.anInt253 = var2.getInt();
            this.anInt228 = var2.getInt();
            this.anInt222 = var2.getInt();
         }

         if(-6 == ~this.type) {
            this.spriteArchiveId = var2.getInt();
            this.anInt296 = var2.getInt();
         }

         if(6 == this.type) {
            this.modelType = 1;
            this.itemId = var2.getShort(1);
            this.secondModelType = 1;
            if(this.itemId == '\uffff') {
               this.itemId = -1;
            }

            this.secondModelId = var2.getShort(1);
            if(this.secondModelId == '\uffff') {
               this.secondModelId = -1;
            }

            this.animationId = var2.getShort(1);
            if(~this.animationId == -65536) {
               this.animationId = -1;
            }

            this.secondAnimationId = var2.getShort(1);
            if('\uffff' == this.secondAnimationId) {
               this.secondAnimationId = -1;
            }

            this.anInt164 = var2.getShort(1);
            this.anInt182 = var2.getShort(1);
            this.anInt308 = var2.getShort(1);
         }

         if(7 == this.type) {
            this.aByte241 = 3;
            this.aByte304 = 3;
            this.itemIds = new int[this.height * this.width];
            this.itemAmounts = new int[this.width * this.height];
            this.anInt194 = var2.getByte((byte)-95);
            this.anInt270 = var2.getShort(1);
            if(~this.anInt270 == -65536) {
               this.anInt270 = -1;
            }

            this.aBoolean215 = ~var2.getByte((byte)-128) == -2;
            this.anInt218 = var2.getInt();
            this.anInt285 = var2.getShort((byte)31);
            this.anInt290 = var2.getShort((byte)83);
            var6 = var2.getByte((byte)-74);
            if(-2 == ~var6) {
               var5 |= 1073741824;
            }

            this.options = new RSString[5];

            for(var7 = 0; var7 < 5; ++var7) {
               RSString var13 = var2.getString();
               if(var13.length(-121) > 0) {
                  this.options[var7] = var13;
                  var5 |= 1 << 23 - -var7;
               }
            }
         }

         if(8 == this.type) {
            this.aClass94_232 = var2.getString();
         }

         if(-3 == ~this.anInt318 || ~this.type == -3) {
            this.aClass94_245 = var2.getString();
            this.aClass94_243 = var2.getString();
            var6 = 63 & var2.getShort(1);
            var5 |= var6 << 11;
         }

         if(this.anInt318 == 1 || this.anInt318 == 4 || -6 == ~this.anInt318 || this.anInt318 == 6) {
            this.aClass94_289 = var2.getString();
            if(this.aClass94_289.length(-33) == 0) {
               if(~this.anInt318 == -2) {
                  this.aClass94_289 = Class115.aClass94_1583;
               }

               if(-5 == ~this.anInt318) {
                  this.aClass94_289 = Class131.aClass94_1722;
               }

               if(5 == this.anInt318) {
                  this.aClass94_289 = Class131.aClass94_1722;
               }

               if(this.anInt318 == 6) {
                  this.aClass94_289 = Class60.aClass94_935;
               }
            }
         }

         if(-2 == ~this.anInt318 || -5 == ~this.anInt318 || -6 == ~this.anInt318) {
            var5 |= 4194304;
         }

         if(~this.anInt318 == -7) {
            var5 |= 1;
         }

         this.aClass3_Sub1_257 = new Class3_Sub1(var5, -1);
      } catch (RuntimeException var12) {
         throw Class44.method1067(var12, "be.M(" + var1 + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

   final Class3_Sub28_Sub16 method859(boolean var1, int var2) {
      try {
         GameShell.aBoolean6 = false;
         if(!var1) {
            return (Class3_Sub28_Sub16)null;
         } else if(~var2 <= -1 && var2 < this.anIntArray197.length) {
            int var3 = this.anIntArray197[var2];
            if(~var3 != 0) {
               Class3_Sub28_Sub16 var4 = (Class3_Sub28_Sub16)Class114.aClass93_1569.get((long)var3, (byte)121);
               if(var4 == null) {
                  var4 = Class3_Sub28_Sub11.method602(0, var3, (byte)-18, Class12.aClass153_323);
                  if(null != var4) {
                     Class114.aClass93_1569.put((byte)-126, var4, (long)var3);
                  } else {
                     GameShell.aBoolean6 = true;
                  }

                  return var4;
               } else {
                  return var4;
               }
            } else {
               return null;
            }
         } else {
            return null;
         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "be.I(" + var1 + ',' + var2 + ')');
      }
   }

   public static void method860(int var0) {
      try {
         aClass94_297 = null;
         aClass94_209 = null;
         if(var0 < 63) {
            method860(42);
         }

         aClass94_251 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "be.F(" + var0 + ')');
      }
   }

   static final int method861(int var0, int var1, int var2) {
      try {
         Class3_Sub25 var3 = (Class3_Sub25)Class3_Sub2.aClass130_2220.method1780((long)var0, 0);
         return null == var3?-1:(0 <= var2 && var2 < var3.anIntArray2547.length?(var1 < 39?-69:var3.anIntArray2547[var2]):-1);
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "be.J(" + var0 + ',' + var1 + ',' + var2 + ')');
      }
   }

   private final Object[] method862(int var1, RSByteBuffer var2) {
      try {
         if(var1 != -65536) {
            return (Object[])null;
         } else {
            int var3 = var2.getByte((byte)-103);
            if(-1 != ~var3) {
               Object[] var4 = new Object[var3];

               for(int var5 = 0; var3 > var5; ++var5) {
                  int var6 = var2.getByte((byte)-115);
                  if(0 != var6) {
                     if(-2 == ~var6) {
                        var4[var5] = var2.getString();
                     }
                  } else {
                     var4[var5] = new Integer(var2.getInt());
                  }
               }

               this.aBoolean195 = true;
               return var4;
            } else {
               return null;
            }
         }
      } catch (RuntimeException var7) {
         throw Class44.method1067(var7, "be.K(" + var1 + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

   private final int[] method863(RSByteBuffer var1, boolean var2) {
      try {
         int var3 = var1.getByte((byte)-125);
         if(-1 == ~var3) {
            return null;
         } else {
            int[] var4 = new int[var3];
            if(var2) {
               this.anInt312 = 20;
            }

            for(int var5 = 0; ~var5 > ~var3; ++var5) {
               var4[var5] = var1.getInt();
            }

            return var4;
         }
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "be.H(" + (var1 != null?"{...}":"null") + ',' + var2 + ')');
      }
   }

   final void method864(int var1, int var2, int var3) {
      try {
         int var4 = this.itemAmounts[var2];
         this.itemAmounts[var2] = this.itemAmounts[var1];
         if(var3 > -66) {
            this.decodeNoScripts(36, (RSByteBuffer)null);
         }

         this.itemAmounts[var1] = var4;
         var4 = this.itemIds[var2];
         this.itemIds[var2] = this.itemIds[var1];
         this.itemIds[var1] = var4;
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "be.L(" + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   final Model method865(int var1, AnimationDefinition var2, int var3, int var4, int var5, boolean var6, Class52 var7) {
      try {
         GameShell.aBoolean6 = false;
         int var8;
         int var9;
         if(var6) {
            var8 = this.secondModelType;
            var9 = this.secondModelId;
         } else {
            var9 = this.itemId;
            var8 = this.modelType;
         }

         if(var4 < 125) {
            return (Model)null;
         } else if(-1 != ~var8) {
            if(-2 == ~var8 && var9 == -1) {
               return null;
            } else {
               Model var10;
               if(1 == var8) {
                  var10 = (Model)Class3_Sub15.aClass93_2428.get((long)((var8 << 16) - -var9), (byte)121);
                  if(var10 == null) {
                     Model_Sub1 var18 = Model_Sub1.method2015(Class119.aClass153_1628, var9, 0);
                     if(var18 == null) {
                        GameShell.aBoolean6 = true;
                        return null;
                     }

                     var10 = var18.method2008(64, 768, -50, -10, -50);
                     Class3_Sub15.aClass93_2428.put((byte)-115, var10, (long)(var9 + (var8 << 16)));
                  }

                  if(var2 != null) {
                     var10 = var2.method2055(var10, (byte)119, var1, var5, var3);
                  }

                  return var10;
               } else if(var8 != 2) {
                  if(3 != var8) {
                     if(4 == var8) {
                        ItemDefinition var16 = Class38.getItemDefinition(var9, (byte)94);
                        Model var17 = var16.method1110(110, var1, var5, var2, 10, var3);
                        if(var17 != null) {
                           return var17;
                        } else {
                           GameShell.aBoolean6 = true;
                           return null;
                        }
                     } else if(var8 != 6) {
                        if(~var8 != -8) {
                           return null;
                        } else if(var7 != null) {
                           int var15 = this.itemId >>> 16;
                           int var11 = this.itemId & '\uffff';
                           int var12 = this.anInt265;
                           Model var13 = var7.method1157(var1, var12, var15, var5, var2, var3, var11, -2012759707);
                           if(var13 == null) {
                              GameShell.aBoolean6 = true;
                              return null;
                           } else {
                              return var13;
                           }
                        } else {
                           return null;
                        }
                     } else {
                        var10 = Node.method522(var9, 27112).method1476((Class145[])null, 0, (byte)-120, 0, var1, var5, var3, (AnimationDefinition)null, 0, var2);
                        if(null != var10) {
                           return var10;
                        } else {
                           GameShell.aBoolean6 = true;
                           return null;
                        }
                     }
                  } else if(null == var7) {
                     return null;
                  } else {
                     var10 = var7.method1167(var5, (byte)127, var2, var3, var1);
                     if(null == var10) {
                        GameShell.aBoolean6 = true;
                        return null;
                     } else {
                        return var10;
                     }
                  }
               } else {
                  var10 = Node.method522(var9, 27112).getChatModel(var2, var5, var1, 27, var3);
                  if(null != var10) {
                     return var10;
                  } else {
                     GameShell.aBoolean6 = true;
                     return null;
                  }
               }
            }
         } else {
            return null;
         }
      } catch (RuntimeException var14) {
         throw Class44.method1067(var14, "be.E(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + (var7 != null?"{...}":"null") + ')');
      }
   }

   final Class3_Sub28_Sub16 method866(byte var1, boolean var2) {
      try {
         GameShell.aBoolean6 = false;
         int archiveId;
         if(var2) {
            archiveId = this.anInt296;
         } else {
            archiveId = this.spriteArchiveId;
         }
         if(0 == ~archiveId) {
            return null;
         } else {
            long var4 = ((this.aBoolean178?1L:0L) << 38) + ((!this.aBoolean157?0L:1L) << 35) + (long)archiveId + ((long)this.anInt288 << 36) + ((this.aBoolean199?1L:0L) << 39) + ((long)this.anInt287 << 40);
            Class3_Sub28_Sub16 var6 = (Class3_Sub28_Sub16)Class114.aClass93_1569.get(var4, (byte)121);
            if(var6 != null) {
               return var6;
            } else {
               Class3_Sub28_Sub16_Sub2 var7;
               if(this.aBoolean157) {
                  var7 = Class3_Sub28_Sub7.method562(Class12.aClass153_323, 0, archiveId, (byte)39);
               } else {
                  var7 = Class40.method1043(0, Class12.aClass153_323, -3178, archiveId);
               }

               if(null == var7) {
                  GameShell.aBoolean6 = true;
                  return null;
               } else if(var1 != -113) {
                  return (Class3_Sub28_Sub16)null;
               } else {
                  if(this.aBoolean178) {
                     var7.method663();
                  }

                  if(this.aBoolean199) {
                     var7.method653();
                  }

                  if(this.anInt288 > 0) {
                     var7.method652(this.anInt288);
                  }

                  if(~this.anInt288 <= -2) {
                     var7.method657(1);
                  }

                  if(2 <= this.anInt288) {
                     var7.method657(16777215);
                  }

                  if(this.anInt287 != 0) {
                     var7.method668(this.anInt287);
                  }

                  Object var9;
                  if(HDToolKit.highDetail) {
                     if(!(var7 instanceof Class3_Sub28_Sub16_Sub2_Sub1)) {
                        var9 = new Class3_Sub28_Sub16_Sub1(var7);
                     } else {
                        var9 = new Class3_Sub28_Sub16_Sub1_Sub1(var7);
                     }
                  } else {
                     var9 = var7;
                  }

                  Class114.aClass93_1569.put((byte)-75, var9, var4);
                  return (Class3_Sub28_Sub16)var9;
               }
            }
         }
      } catch (RuntimeException var8) {
         throw Class44.method1067(var8, "be.O(" + var1 + ',' + var2 + ')');
      }
   }

   final void decodeScriptFormat(int var1, RSByteBuffer buffer) {
      try {
         this.usingScripts = true;
         ++buffer.index;
         this.type = buffer.getByte((byte)-120);
         if(-1 != ~(128 & this.type)) {
            this.type &= 127;
            buffer.getString();
         }

         this.anInt189 = buffer.getShort(var1 + 2);
         this.x = buffer.getShort((byte)66);
         this.y = buffer.getShort((byte)121);
         this.width = buffer.getShort(1);
         this.height = buffer.getShort(1);
         this.aByte304 = buffer.getByte();
         this.aByte241 = buffer.getByte();
         this.aByte273 = buffer.getByte();
         this.aByte162 = buffer.getByte();
         this.parentId = buffer.getShort(var1 ^ -2);
         if(-65536 == ~this.parentId) {
            this.parentId = -1;
         } else {
            this.parentId = (this.anInt279 & -65536) - -this.parentId;
         }

         this.hidden = -2 == ~buffer.getByte((byte)-40);
         if(~this.type == -1) {
            this.anInt240 = buffer.getShort(1);
            this.anInt252 = buffer.getShort(1);
            this.aBoolean219 = -2 == ~buffer.getByte((byte)-114);
         }

         int var3;
         if(~this.type == -6) {
            this.spriteArchiveId = buffer.getInt();
            this.anInt301 = buffer.getShort(1);
            var3 = buffer.getByte((byte)-39);
            this.aBoolean157 = -1 != ~(2 & var3);
            this.aBoolean186 = ~(1 & var3) != -1;
            this.anInt223 = buffer.getByte((byte)-82);
            this.anInt288 = buffer.getByte((byte)-86);
            this.anInt287 = buffer.getInt();
            this.aBoolean178 = ~buffer.getByte((byte)-93) == -2;
            this.aBoolean199 = 1 == buffer.getByte((byte)-60);
         }

         if(~this.type == -7) {
            this.modelType = 1;
            this.itemId = buffer.getShort(var1 + 2);
            if(~this.itemId == -65536) {
               this.itemId = -1;
            }

            this.anInt259 = buffer.getShort((byte)122);
            this.anInt230 = buffer.getShort((byte)32);
            this.anInt182 = buffer.getShort(1);
            this.anInt308 = buffer.getShort(1);
            this.anInt280 = buffer.getShort(1);
            this.anInt164 = buffer.getShort(1);
            this.animationId = buffer.getShort(1);
            if('\uffff' == this.animationId) {
               this.animationId = -1;
            }

            this.aBoolean181 = buffer.getByte((byte)-100) == 1;
            this.aShort293 = (short)buffer.getShort(1);
            this.aShort169 = (short)buffer.getShort(1);
            this.aBoolean309 = 1 == buffer.getByte((byte)-64);
            if(this.aByte304 != 0) {
               this.anInt184 = buffer.getShort(1);
            }

            if(this.aByte241 != 0) {
               this.anInt312 = buffer.getShort(1);
            }
         }

         if(~this.type == -5) {
            this.anInt270 = buffer.getShort(1);
            if(~this.anInt270 == -65536) {
               this.anInt270 = -1;
            }

            this.aClass94_232 = buffer.getString();
            this.anInt205 = buffer.getByte((byte)-124);
            this.anInt194 = buffer.getByte((byte)-75);
            this.anInt225 = buffer.getByte((byte)-38);
            this.aBoolean215 = buffer.getByte((byte)-51) == 1;
            this.anInt218 = buffer.getInt();
         }

         if(this.type == 3) {
            this.anInt218 = buffer.getInt();
            this.aBoolean226 = 1 == buffer.getByte((byte)-90);
            this.anInt223 = buffer.getByte((byte)-63);
         }

         if(-10 == ~this.type) {
            this.anInt250 = buffer.getByte((byte)-56);
            this.anInt218 = buffer.getInt();
            this.aBoolean167 = 1 == buffer.getByte((byte)-61);
         }

         var3 = buffer.getTriByte((byte)87);
         int var4 = buffer.getByte((byte)-91);
         int var5;
         if(var4 != 0) {
            this.anIntArray299 = new int[10];
            this.aByteArray263 = new byte[10];

            for(this.aByteArray231 = new byte[10]; ~var4 != -1; var4 = buffer.getByte((byte)-80)) {
               var5 = (var4 >> 4) - 1;
               var4 = buffer.getByte((byte)-63) | var4 << 8;
               var4 &= 4095;
               if(4095 == var4) {
                  this.anIntArray299[var5] = -1;
               } else {
                  this.anIntArray299[var5] = var4;
               }

               this.aByteArray263[var5] = buffer.getByte();
               this.aByteArray231[var5] = buffer.getByte();
            }
         }

         this.aClass94_277 = buffer.getString();
         var5 = buffer.getByte((byte)-80);
         int var6 = var5 & 15;
         int var8;
         if(0 < var6) {
            this.aClass94Array171 = new RSString[var6];

            for(var8 = 0; var6 > var8; ++var8) {
               this.aClass94Array171[var8] = buffer.getString();
            }
         }

         
         
         int var7 = var5 >> 4;
         if(var7 > 0) {
            var8 = buffer.getByte((byte)-93);
            this.anIntArray249 = new int[var8 + 1];

            for(int var9 = 0; var9 < this.anIntArray249.length; ++var9) {
               this.anIntArray249[var9] = -1;
            }

            this.anIntArray249[var8] = buffer.getShort(1);
         }

         if(1 < var7) {
            var8 = buffer.getByte((byte)-33);
            this.anIntArray249[var8] = buffer.getShort(1);
         }

         this.anInt214 = buffer.getByte((byte)-105);
         this.anInt179 = buffer.getByte((byte)-78);
         this.aBoolean200 = buffer.getByte((byte)-119) == 1;
         var8 = var1;
         this.aClass94_245 = buffer.getString();
         if(0 != Class3_Sub28_Sub15.method630((byte)-34, var3)) {
            var8 = buffer.getShort(1);
            this.anInt266 = buffer.getShort(1);
            if(-65536 == ~var8) {
               var8 = -1;
            }

            if('\uffff' == this.anInt266) {
               this.anInt266 = -1;
            }

            this.anInt238 = buffer.getShort(1);
            if(this.anInt238 == '\uffff') {
               this.anInt238 = -1;
            }
         }

         this.aClass3_Sub1_257 = new Class3_Sub1(var3, var8);
         this.anObjectArray159 = this.method862(-65536, buffer);
         this.anObjectArray248 = this.method862(var1 + -65535, buffer);
         this.anObjectArray281 = this.method862(-65536, buffer);
         this.anObjectArray303 = this.method862(var1 ^ '\uffff', buffer);
         this.anObjectArray203 = this.method862(-65536, buffer);
         this.anObjectArray282 = this.method862(var1 ^ '\uffff', buffer);
         this.anObjectArray174 = this.method862(var1 + -65535, buffer);
         this.anObjectArray158 = this.method862(-65536, buffer);//.?
         this.anObjectArray269 = this.method862(-65536, buffer);
         this.anObjectArray314 = this.method862(var1 ^ '\uffff', buffer);
         this.anObjectArray276 = this.method862(-65536, buffer);
         this.anObjectArray165 = this.method862(-65536, buffer);
         this.anObjectArray170 = this.method862(var1 ^ '\uffff', buffer);
         this.anObjectArray239 = this.method862(var1 ^ '\uffff', buffer);
         this.anObjectArray180 = this.method862(-65536, buffer);
         this.anObjectArray295 = this.method862(-65536, buffer);
         this.anObjectArray229 = this.method862(-65536, buffer);
         this.anObjectArray183 = this.method862(-65536, buffer);
         this.anObjectArray161 = this.method862(-65536, buffer);
         this.anObjectArray221 = this.method862(-65536, buffer);
         this.anIntArray286 = this.method863(buffer, false);
         this.anIntArray175 = this.method863(buffer, false);
         this.anIntArray274 = this.method863(buffer, false);
         this.anIntArray211 = this.method863(buffer, false);
         this.anIntArray185 = this.method863(buffer, false);
      } catch (RuntimeException var10) {
         throw Class44.method1067(var10, "be.C(" + var1 + ',' + (buffer != null?"{...}":"null") + ')');
      }
   }

   final Class3_Sub28_Sub17 method868(AbstractIndexedSprite[] var1, int var2) {
      try {
         GameShell.aBoolean6 = false;
         if(0 == ~this.anInt270) {
            return null;
         } else {
            Class3_Sub28_Sub17 var3 = (Class3_Sub28_Sub17)Class47.aClass93_743.get((long)this.anInt270, (byte)121);
            if(null != var3) {
               return var3;
            } else {
               var3 = Class73.method1300(var2, this.anInt270, (byte)127, Class12.aClass153_323, Class97.aClass153_1378);
               if(null == var3) {
                  GameShell.aBoolean6 = true;
               } else {
                  var3.method697(var1, (int[])null);
                  Class47.aClass93_743.put((byte)-77, var3, (long)this.anInt270);
               }

               return var3;
            }
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "be.A(" + (var1 != null?"{...}":"null") + ',' + var2 + ')');
      }
   }

   static final int method869(int var0, int var1) {
      try {
         return ~var1 != -16711936?(var0 < 97?-63:Class56.method1186(0, var1)):-1;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "be.D(" + var0 + ',' + var1 + ')');
      }
   }

   public RSInterface() {
      this.aClass94_243 = Class104.aClass94_2171;
      this.aBoolean163 = false;
      this.anInt225 = 0;
      this.anInt212 = -1;
      this.aBoolean167 = false;
      this.anInt266 = -1;
      this.aByte241 = 0;
      this.anInt252 = 0;
      this.aBoolean200 = false;
      this.aBoolean215 = false;
      this.anInt204 = -1;
      this.anInt260 = 1;
      this.anInt228 = 0;
      this.usingScripts = false;
      this.aClass3_Sub1_257 = Class158_Sub1.aClass3_Sub1_2980;
      this.anInt253 = 0;
      this.aClass94_232 = Class104.aClass94_2171;
      this.anInt168 = 0;
      this.anInt247 = 0;
      this.aBoolean219 = false;
      this.secondModelId = -1;
      this.parentId = -1;
      this.anInt216 = 1;
      this.anInt192 = -1;
      this.anInt222 = 0;
      this.anInt264 = 0;
      this.aClass94_277 = Class104.aClass94_2171;
      this.anInt284 = 0;
      this.width = 0;
      this.anInt285 = 0;
      this.anInt234 = -1;
      this.aBoolean157 = false;
      this.anInt184 = 0;
      this.anInt223 = 0;
      this.anInt258 = 0;
      this.aClass94_245 = Class104.aClass94_2171;
      this.anInt237 = 0;
      this.aClass94_172 = Class104.aClass94_2171;
      this.anInt288 = 0;
      this.anInt265 = -1;
      this.anInt242 = 0;
      this.anInt259 = 0;
      this.anInt290 = 0;
      this.height = 0;
      this.anInt279 = -1;
      this.anInt296 = -1;
      this.aByte273 = 0;
      this.anInt267 = 0;
      this.anInt270 = -1;
      this.anInt240 = 0;
      this.anInt255 = 0;
      this.aShort293 = 0;
      this.anInt301 = 0;
      this.animationId = -1;
      this.aClass94_289 = Class115.aClass94_1583;
      this.anInt280 = 0;
      this.anInt271 = 0;
      this.anInt292 = -1;
      this.anInt189 = 0;
      this.anInt287 = 0;
      this.aClass11_302 = null;
      this.anInt311 = 0;
      this.modelType = 1;
      this.aBoolean309 = false;
      this.aByte304 = 0;
      this.secondModelType = 1;
      this.anInt312 = 0;
      this.anInt308 = 0;
      this.aBoolean195 = false;
      this.x = 0;
      this.anInt306 = 0;
      this.y = 0;
      this.aBoolean227 = true;
      this.anInt283 = 0;
      this.anInt213 = 0;
      this.anInt218 = 0;
      this.anInt318 = 0;
   }

}
