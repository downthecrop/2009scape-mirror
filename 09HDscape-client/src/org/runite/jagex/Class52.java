package org.runite.jagex;

final class Class52 {

   static RSString aClass94_852 = RSString.createRSString("(U4");
   static RSString aClass94_853 = RSString.createRSString("::tele ");
   int pnpcId;
   private long aLong855;
   static RSString aClass94_856 = RSString.createRSString("::wm0");
   private int[] lookInfo;
   private int renderAnim;
   static int[] anIntArray859;
   private long aLong860;
   static int[] anIntArray861;
   private int[] anIntArray862;
   private int[][] anIntArrayArray863;
   boolean aBoolean864;


   final Model method1157(int var1, int var2, int var3, int var4, AnimationDefinition var5, int var6, int var7, int var8) {
      try {
         if(var8 != -2012759707) {
            this.pnpcId = -32;
         }

         long var9 = (long)var3 | (long)(var7 << 16) | (long)var2 << 32;
         Model var11 = (Model)Class80.aClass93_1131.get(var9, (byte)121);
         if(null == var11) {
            Model_Sub1[] var12 = new Model_Sub1[3];
            int var13 = 0;
            if(!Class3_Sub13_Sub13.method231(var3, 0).method948(var8 ^ -2012744886) || !Class3_Sub13_Sub13.method231(var7, 0).method948(18991) || !Class3_Sub13_Sub13.method231(var2, 0).method948(18991)) {
               return null;
            }

            Model_Sub1 var14 = Class3_Sub13_Sub13.method231(var3, 0).method941(true);
            if(null != var14) {
               var12[var13++] = var14;
            }

            var14 = Class3_Sub13_Sub13.method231(var7, 0).method941(true);
            if(var14 != null) {
               var12[var13++] = var14;
            }

            var14 = Class3_Sub13_Sub13.method231(var2, 0).method941(true);
            if(var14 != null) {
               var12[var13++] = var14;
            }

            var14 = new Model_Sub1(var12, var13);

            for(int var15 = 0; ~var15 > -6; ++var15) {
               if(~this.anIntArray862[var15] > ~Class15.aShortArrayArray344[var15].length) {
                  var14.method2016(Class3_Sub25.aShortArray2548[var15], Class15.aShortArrayArray344[var15][this.anIntArray862[var15]]);
               }

               if(~Class101.aShortArrayArray1429[var15].length < ~this.anIntArray862[var15]) {
                  var14.method2016(Class91.aShortArray1311[var15], Class101.aShortArrayArray1429[var15][this.anIntArray862[var15]]);
               }
            }

            var11 = var14.method2008(64, 768, -50, -10, -50);
            Class80.aClass93_1131.put((byte)-111, var11, var9);
         }

         if(null != var5) {
            var11 = var5.method2055(var11, (byte)-86, var1, var4, var6);
         }

         return var11;
      } catch (RuntimeException var16) {
         throw Class44.method1067(var16, "hh.J(" + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + (var5 != null?"{...}":"null") + ',' + var6 + ',' + var7 + ',' + var8 + ')');
      }
   }

   private final void method1158(int var1) {
      try {
         long var2 = this.aLong860;
         this.aLong860 = -1L;
         long[] var4 = Class120.aLongArray1631;
         this.aLong860 = var4[(int)(255L & ((long)(this.renderAnim >> 8) ^ this.aLong860))] ^ this.aLong860 >>> 8;
         this.aLong860 = var4[(int)(255L & (this.aLong860 ^ (long)this.renderAnim))] ^ this.aLong860 >>> 8;

         int var5;
         for(var5 = 0; 12 > var5; ++var5) {
            this.aLong860 = this.aLong860 >>> 8 ^ var4[(int)((this.aLong860 ^ (long)(this.lookInfo[var5] >> 24)) & 255L)];
            this.aLong860 = this.aLong860 >>> 8 ^ var4[(int)(255L & (this.aLong860 ^ (long)(this.lookInfo[var5] >> 16)))];
            this.aLong860 = var4[(int)(255L & ((long)(this.lookInfo[var5] >> 8) ^ this.aLong860))] ^ this.aLong860 >>> 8;
            this.aLong860 = this.aLong860 >>> 8 ^ var4[(int)((this.aLong860 ^ (long)this.lookInfo[var5]) & 255L)];
         }

         if(var1 != 459557008) {
            this.anIntArray862 = (int[])null;
         }

         for(var5 = 0; var5 < 5; ++var5) {
            this.aLong860 = var4[(int)(((long)this.anIntArray862[var5] ^ this.aLong860) & 255L)] ^ this.aLong860 >>> 8;
         }

         this.aLong860 = var4[(int)(((long)(this.aBoolean864?1:0) ^ this.aLong860) & 255L)] ^ this.aLong860 >>> 8;
         if(-1L != ~var2 && this.aLong860 != var2) {
            Class148.aClass93_1911.method1518(var2, (byte)-124);
         }

      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "hh.K(" + var1 + ')');
      }
   }

   final void method1159(boolean var1, boolean var2) {
      try {
         if(!var2) {
            anIntArray859 = (int[])null;
         }

         this.aBoolean864 = var1;
         this.method1158(459557008);
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "hh.A(" + var1 + ',' + var2 + ')');
      }
   }

   static final void method1160(int var0, int var1) {
      try {
         if(Canvas_Sub2.loadInterface(var1, 104)) {
            if(var0 > -100) {
               method1168(52);
            }

            Class67.method1260(23206, -1, GameObject.aClass11ArrayArray1834[var1]);
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "hh.B(" + var0 + ',' + var1 + ')');
      }
   }

   final void method1161(int[] var1, int var2, boolean var3, int var4, int[] look, int render) {
      try {
         if(render != this.renderAnim) {
            this.renderAnim = render;
            this.anIntArrayArray863 = (int[][])null;
         }

         if(null == look) {
            look = new int[12];

            for(int var7 = 0; -9 < ~var7; ++var7) {
               for(int var8 = 0; Class25.anInt497 > var8; ++var8) {
                  Class24 var9 = Class3_Sub13_Sub13.method231(var8, 0);
                  if(null != var9 && !var9.aBoolean476 && ~var9.anInt466 == ~(!var3?Class3_Sub26.anIntArray2559[var7]:Class3_Sub13_Sub19.anIntArray3228[var7])) {
                     look[Class163.anIntArray2043[var7]] = Class3_Sub13_Sub29.bitwiseOr(Integer.MIN_VALUE, var8);
                     break;
                  }
               }
            }
         }

         this.pnpcId = var2;
         this.aBoolean864 = var3;
         if(var4 == 0) {
            this.anIntArray862 = var1;
            this.lookInfo = look;
            this.method1158(var4 + 459557008);
         }
      } catch (RuntimeException var10) {
         throw Class44.method1067(var10, "hh.G(" + (var1 != null?"{...}":"null") + ',' + var2 + ',' + var3 + ',' + var4 + ',' + (look != null?"{...}":"null") + ',' + render + ')');
      }
   }

   final void method1162(int var1, boolean var2, int var3) {
      try {
         this.anIntArray862[var1] = var3;
         this.method1158(459557008);
         if(var2) {
            this.method1159(false, false);
         }

      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "hh.L(" + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   final int method1163(int var1) {
      try {
         if(var1 != -24861) {
            anIntArray859 = (int[])null;
         }

         return this.pnpcId != -1?305419896 + Node.method522(this.pnpcId, var1 + '\ucb05').npcId:(this.lookInfo[8] << 10) + ((this.anIntArray862[0] << 25) - -(this.anIntArray862[4] << 20)) - (-(this.lookInfo[0] << 15) - ((this.lookInfo[11] << 5) + this.lookInfo[1]));
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "hh.E(" + var1 + ')');
      }
   }

   final void method1164(int var1, int var2, int var3) {
      try {
         int var4 = Class163.anIntArray2043[var1];
         if(-1 != ~this.lookInfo[var4]) {
            if(Class3_Sub13_Sub13.method231(var2, var3) != null) {
               this.lookInfo[var4] = Class3_Sub13_Sub29.bitwiseOr(var2, Integer.MIN_VALUE);
               this.method1158(459557008);
            }
         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "hh.I(" + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   final Model method1165(Class145[] var1, int var2, AnimationDefinition var3, AnimationDefinition var4, int var5, int var6, int var7, int var8, boolean var9, int var10, int var11) {
      try {
         int var12 = 102 % ((var7 - -39) / 61);
         if(0 != ~this.pnpcId) {
            return Node.method522(this.pnpcId, 27112).method1476(var1, var6, (byte)-128, var11, var2, var8, var10, var3, var5, var4);
         } else {
            int[] var15 = this.lookInfo;
            long var13 = this.aLong860;
            if(var4 != null && (~var4.anInt1854 <= -1 || ~var4.anInt1849 <= -1)) {
               var15 = new int[12];

               for(int var16 = 0; 12 > var16; ++var16) {
                  var15[var16] = this.lookInfo[var16];
               }

               if(0 <= var4.anInt1854) {
                  if(-65536 == ~var4.anInt1854) {
                     var13 ^= -4294967296L;
                     var15[5] = 0;
                  } else {
                     var15[5] = Class3_Sub13_Sub29.bitwiseOr(1073741824, var4.anInt1854);
                     var13 ^= (long)var15[5] << 32;
                  }
               }

               if(~var4.anInt1849 <= -1) {
                  if(-65536 != ~var4.anInt1849) {
                     var15[3] = Class3_Sub13_Sub29.bitwiseOr(1073741824, var4.anInt1849);
                     var13 ^= (long)var15[3];
                  } else {
                     var15[3] = 0;
                     var13 ^= 4294967295L;
                  }
               }
            }

            Model var37 = (Model)Class148.aClass93_1911.get(var13, (byte)121);
            boolean var17;
            int var23;
            int var22;
            int var25;
            int var24;
            int frame;
            int var29;
            int var28;
            int var34;
            int var32;
            int var33;
            int var45;
            if(var37 == null) {
               var17 = false;

               int var19;
               for(int var18 = 0; -13 < ~var18; ++var18) {
                  var19 = var15[var18];
                  if(~(var19 & 1073741824) != -1) {
                     if(!Class38.getItemDefinition(1073741823 & var19, (byte)92).method1108((byte)95, this.aBoolean864)) {
                        var17 = true;
                     }
                  } else if(~(var19 & Integer.MIN_VALUE) != -1 && !Class3_Sub13_Sub13.method231(1073741823 & var19, 0).method942(101)) {
                     var17 = true;
                  }
               }

               if(var17) {
                  if(this.aLong855 != -1L) {
                     var37 = (Model)Class148.aClass93_1911.get(this.aLong855, (byte)121);
                  }

                  if(null == var37) {
                     return null;
                  }
               }

               if(null == var37) {
                  Model_Sub1[] var39 = new Model_Sub1[12];

                  int var20;
                  for(var19 = 0; ~var19 > -13; ++var19) {
                     var20 = var15[var19];
                     Model_Sub1 var21;
                     if(~(var20 & 1073741824) == -1) {
                        if(-1 != ~(Integer.MIN_VALUE & var20)) {
                           var21 = Class3_Sub13_Sub13.method231(var20 & 1073741823, 0).method947((byte)-26);
                           if(null != var21) {
                              var39[var19] = var21;
                           }
                        }
                     } else {
                        var21 = Class38.getItemDefinition(var20 & 1073741823, (byte)115).method1117(this.aBoolean864, 80);
                        if(null != var21) {
                           var39[var19] = var21;
                        }
                     }
                  }

                  RenderAnimationDefinition var40 = null;
                  if(this.renderAnim != -1) {
                     var40 = Class3_Sub10.getRenderAnimationDefinition(false, this.renderAnim);
                  }

                  if(var40 != null && null != var40.anIntArrayArray359) {
                     for(var20 = 0; var20 < var40.anIntArrayArray359.length; ++var20) {
                        if(null != var40.anIntArrayArray359[var20] && null != var39[var20]) {
                           var45 = var40.anIntArrayArray359[var20][0];
                           var22 = var40.anIntArrayArray359[var20][1];
                           var23 = var40.anIntArrayArray359[var20][2];
                           var25 = var40.anIntArrayArray359[var20][4];
                           var24 = var40.anIntArrayArray359[var20][3];
                           frame = var40.anIntArrayArray359[var20][5];
                           if(null == this.anIntArrayArray863) {
                              this.anIntArrayArray863 = new int[var40.anIntArrayArray359.length][];
                           }

                           if(this.anIntArrayArray863[var20] == null) {
                              int[] var27 = this.anIntArrayArray863[var20] = new int[15];
                              if(~var24 == -1 && -1 == ~var25 && frame == 0) {
                                 var27[12] = -var45;
                                 var27[13] = -var22;
                                 var27[0] = var27[4] = var27[8] = '\u8000';
                                 var27[14] = -var23;
                              } else {
                                 var28 = Class51.anIntArray851[var24] >> 1;
                                 var29 = Class51.anIntArray840[var24] >> 1;
                                 int var30 = Class51.anIntArray851[var25] >> 1;
                                 int var31 = Class51.anIntArray840[var25] >> 1;
                                 var32 = Class51.anIntArray851[frame] >> 1;
                                 var33 = Class51.anIntArray840[frame] >> 1;
                                 var27[4] = var28 * var32 + 16384 >> 15;
                                 var27[5] = -var29;
                                 var27[3] = 16384 + var33 * var28 >> 15;
                                 var27[2] = 16384 + var28 * var31 >> 15;
                                 var27[8] = var30 * var28 - -16384 >> 15;
                                 int var35 = 16384 + var33 * var29 >> 15;
                                 var27[0] = var31 * var35 + var32 * var30 - -16384 >> 15;
                                 var27[14] = 16384 + var27[8] * -var23 + -var22 * var27[5] + var27[2] * -var45 >> 15;
                                 var27[6] = var30 * var35 + (var32 * -var31 - -16384) >> 15;
                                 var34 = 16384 + var32 * var29 >> 15;
                                 var27[7] = 16384 + -var33 * -var31 + var34 * var30 >> 15;
                                 var27[1] = var31 * var34 + (var30 * -var33 - -16384) >> 15;
                                 var27[12] = -var22 * var27[3] + var27[0] * -var45 + -var23 * var27[6] - -16384 >> 15;
                                 var27[13] = 16384 + -var22 * var27[4] + var27[1] * -var45 + -var23 * var27[7] >> 15;
                              }

                              var27[9] = var45;
                              var27[11] = var23;
                              var27[10] = var22;
                           }

                           if(0 != var24 || ~var25 != -1 || ~frame != -1) {
                              var39[var20].method2013(var24, var25, frame);
                           }

                           if(var45 != 0 || 0 != var22 || 0 != var23) {
                              var39[var20].method2001(var45, var22, var23);
                           }
                        }
                     }
                  }

                  Model_Sub1 var43 = new Model_Sub1(var39, var39.length);

                  for(var45 = 0; 5 > var45; ++var45) {
                     if(Class15.aShortArrayArray344[var45].length > this.anIntArray862[var45]) {
                        var43.method2016(Class3_Sub25.aShortArray2548[var45], Class15.aShortArrayArray344[var45][this.anIntArray862[var45]]);
                     }

                     if(Class101.aShortArrayArray1429[var45].length > this.anIntArray862[var45]) {
                        var43.method2016(Class91.aShortArray1311[var45], Class101.aShortArrayArray1429[var45][this.anIntArray862[var45]]);
                     }
                  }

                  var37 = var43.method2008(64, 850, -30, -50, -30);
                  if(HDToolKit.highDetail) {
                     ((Class140_Sub1_Sub1)var37).method1920(false, false, true, true, false, false, true);
                  }

                  if(var9) {
                     Class148.aClass93_1911.put((byte)-115, var37, var13);
                     this.aLong855 = var13;
                  }
               }
            }

            var17 = false;
            boolean var38 = false;
            var45 = var1 != null?var1.length:0;
            boolean var42 = false;
            boolean var44 = false;

            int var47;
            for(var22 = 0; ~var45 < ~var22; ++var22) {
               if(var1[var22] != null) {
                  AnimationDefinition var41 = Client.getAnimationDefinition(var1[var22].animationId, (byte)-20);
                  if(var41.frames != null) {
                     var17 = true;
                     Class123.aClass142Array1654[var22] = var41;
                     var24 = var1[var22].anInt1893;
                     var25 = var1[var22].anInt1891;
                     frame = var41.frames[var24];
                     Class166.aClass3_Sub28_Sub5Array2070[var22] = Class3_Sub9.method133(frame >>> 16, 0);
                     frame &= '\uffff';
                     GameObject.anIntArray1833[var22] = frame;
                     if(Class166.aClass3_Sub28_Sub5Array2070[var22] != null) {
                        var42 |= Class166.aClass3_Sub28_Sub5Array2070[var22].method561(frame, (byte)119);
                        var38 |= Class166.aClass3_Sub28_Sub5Array2070[var22].method559(1317095745, frame);
                        var44 |= var41.aBoolean1848;
                     }

                     if((var41.aBoolean1846 || Class3_Sub26.aBoolean2558) && 0 != ~var25 && var25 < var41.frames.length) {
                        Class154.anIntArray1960[var22] = var41.duration[var24];
                        Class3_Sub13_Sub11.anIntArray3139[var22] = var1[var22].anInt1897;
                        var47 = var41.frames[var25];
                        Class75.aClass3_Sub28_Sub5Array1103[var22] = Class3_Sub9.method133(var47 >>> 16, 0);
                        var47 &= '\uffff';
                        Class127.anIntArray1679[var22] = var47;
                        if(null != Class75.aClass3_Sub28_Sub5Array1103[var22]) {
                           var42 |= Class75.aClass3_Sub28_Sub5Array1103[var22].method561(var47, (byte)117);
                           var38 |= Class75.aClass3_Sub28_Sub5Array1103[var22].method559(1317095745, var47);
                        }
                     } else {
                        Class154.anIntArray1960[var22] = 0;
                        Class3_Sub13_Sub11.anIntArray3139[var22] = 0;
                        Class75.aClass3_Sub28_Sub5Array1103[var22] = null;
                        Class127.anIntArray1679[var22] = -1;
                     }
                  }
               }
            }

            if(!var17 && null == var4 && null == var3) {
               return var37;
            } else {
               var22 = -1;
               var23 = -1;
               var24 = 0;
               Class3_Sub28_Sub5 var48 = null;
               Class3_Sub28_Sub5 var46 = null;
               if(null != var4) {
                  var22 = var4.frames[var10];
                  var47 = var22 >>> 16;
                  var46 = Class3_Sub9.method133(var47, 0);
                  var22 &= '\uffff';
                  if(var46 != null) {
                     var42 |= var46.method561(var22, (byte)124);
                     var38 |= var46.method559(1317095745, var22);
                     var44 |= var4.aBoolean1848;
                  }

                  if((var4.aBoolean1846 || Class3_Sub26.aBoolean2558) && var2 != -1 && var4.frames.length > var2) {
                     var23 = var4.frames[var2];
                     var28 = var23 >>> 16;
                     var23 &= '\uffff';
                     var24 = var4.duration[var10];
                     if(~var47 != ~var28) {
                        var48 = Class3_Sub9.method133(var23 >>> 16, 0);
                     } else {
                        var48 = var46;
                     }

                     if(null != var48) {
                        var42 |= var48.method561(var23, (byte)122);
                        var38 |= var48.method559(1317095745, var23);
                     }
                  }
               }

               var47 = -1;
               var28 = -1;
               Class3_Sub28_Sub5 var49 = null;
               Class3_Sub28_Sub5 var50 = null;
               var29 = 0;
               if(var3 != null) {
                  var47 = var3.frames[var11];
                  var32 = var47 >>> 16;
                  var47 &= '\uffff';
                  var49 = Class3_Sub9.method133(var32, 0);
                  if(null != var49) {
                     var42 |= var49.method561(var47, (byte)123);
                     var38 |= var49.method559(1317095745, var47);
                     var44 |= var3.aBoolean1848;
                  }

                  if((var3.aBoolean1846 || Class3_Sub26.aBoolean2558) && 0 != ~var6 && var3.frames.length > var6) {
                     var29 = var3.duration[var11];
                     var28 = var3.frames[var6];
                     var33 = var28 >>> 16;
                     var28 &= '\uffff';
                     if(~var32 != ~var33) {
                        var50 = Class3_Sub9.method133(var28 >>> 16, 0);
                     } else {
                        var50 = var49;
                     }

                     if(null != var50) {
                        var42 |= var50.method561(var28, (byte)122);
                        var38 |= var50.method559(1317095745, var28);
                     }
                  }
               }

               Model var51 = var37.method1894(!var38, !var42, !var44);
               var33 = 0;

               for(var34 = 1; var33 < var45; var34 <<= 1) {
                  if(Class166.aClass3_Sub28_Sub5Array2070[var33] != null) {
                     var51.method1887(Class166.aClass3_Sub28_Sub5Array2070[var33], GameObject.anIntArray1833[var33], Class75.aClass3_Sub28_Sub5Array1103[var33], Class127.anIntArray1679[var33], Class3_Sub13_Sub11.anIntArray3139[var33] + -1, Class154.anIntArray1960[var33], var34, Class123.aClass142Array1654[var33].aBoolean1848, this.anIntArrayArray863[var33]);
                  }

                  ++var33;
               }

               if(null != var46 && null != var49) {
                  var51.method1892(var46, var22, var48, var23, var8 - 1, var24, var49, var47, var50, var28, var5 + -1, var29, var4.aBooleanArray1855, var4.aBoolean1848 | var3.aBoolean1848);
               } else if(var46 == null) {
                  if(null != var49) {
                     var51.method1880(var49, var47, var50, var28, var5 - 1, var29, var3.aBoolean1848);
                  }
               } else {
                  var51.method1880(var46, var22, var48, var23, var8 + -1, var24, var4.aBoolean1848);
               }

               for(var33 = 0; var33 < var45; ++var33) {
                  Class166.aClass3_Sub28_Sub5Array2070[var33] = null;
                  Class75.aClass3_Sub28_Sub5Array1103[var33] = null;
                  Class123.aClass142Array1654[var33] = null;
               }

               return var51;
            }
         }
      } catch (RuntimeException var36) {
         throw Class44.method1067(var36, "hh.D(" + (var1 != null?"{...}":"null") + ',' + var2 + ',' + (var3 != null?"{...}":"null") + ',' + (var4 != null?"{...}":"null") + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ',' + var9 + ',' + var10 + ',' + var11 + ')');
      }
   }

   static final boolean method1166(int var0, byte var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, boolean var11, int var12) {
      try {
         int var13;
         int var14;
         for(var13 = 0; ~var13 > -105; ++var13) {
            for(var14 = 0; var14 < 104; ++var14) {
               Class84.anIntArrayArray1160[var13][var14] = 0;
               Class97.anIntArrayArray1373[var13][var14] = 99999999;
            }
         }

         var13 = var9;
         var14 = var12;
         Class84.anIntArrayArray1160[var9][var12] = 99;
         Class97.anIntArrayArray1373[var9][var12] = 0;
         byte var15 = 0;
         if(var1 != 34) {
            return true;
         } else {
            Class3_Sub13_Sub38.anIntArray3456[var15] = var9;
            int var28 = var15 + 1;
            Class45.anIntArray729[var15] = var12;
            int var16 = 0;
            boolean var17 = false;
            int[][] var18 = Class86.aClass91Array1182[WorldListCountry.localPlane].anIntArrayArray1304;

            int var19;
            int var20;
            label410:
            while(~var16 != ~var28) {
               var13 = Class3_Sub13_Sub38.anIntArray3456[var16];
               var14 = Class45.anIntArray729[var16];
               var16 = 1 + var16 & 4095;
               if(~var6 == ~var13 && ~var14 == ~var0) {
                  var17 = true;
                  break;
               }

               if(0 != var7) {
                  if((var7 < 5 || 10 == var7) && Class86.aClass91Array1182[WorldListCountry.localPlane].method1488(var0, var13, false, var14, var6, var7 + -1, var5, var4)) {
                     var17 = true;
                     break;
                  }

                  if(var7 < 10 && Class86.aClass91Array1182[WorldListCountry.localPlane].method1492(var0, -1 + var7, var6, var14, var5, var4, var13, 95)) {
                     var17 = true;
                     break;
                  }
               }

               if(var2 != 0 && -1 != ~var10 && Class86.aClass91Array1182[WorldListCountry.localPlane].method1498(true, var6, var14, var13, var5, var2, var8, var0, var10)) {
                  var17 = true;
                  break;
               }

               var19 = 1 + Class97.anIntArrayArray1373[var13][var14];
               if(var13 > 0 && Class84.anIntArrayArray1160[-1 + var13][var14] == 0 && (var18[var13 + -1][var14] & 19661070) == 0 && 0 == (19661112 & var18[-1 + var13][-1 + var5 + var14])) {
                  var20 = 1;

                  while(true) {
                     if(-1 + var5 <= var20) {
                        Class3_Sub13_Sub38.anIntArray3456[var28] = -1 + var13;
                        Class45.anIntArray729[var28] = var14;
                        Class84.anIntArrayArray1160[-1 + var13][var14] = 2;
                        var28 = 4095 & 1 + var28;
                        Class97.anIntArrayArray1373[var13 + -1][var14] = var19;
                        break;
                     }

                     if(-1 != ~(19661118 & var18[-1 + var13][var14 + var20])) {
                        break;
                     }

                     ++var20;
                  }
               }

               if(-103 < ~var13 && ~Class84.anIntArrayArray1160[1 + var13][var14] == -1 && (19661187 & var18[var13 + var5][var14]) == 0 && ~(19661280 & var18[var5 + var13][var14 + var5 + -1]) == -1) {
                  var20 = 1;

                  while(true) {
                     if(var20 >= -1 + var5) {
                        Class3_Sub13_Sub38.anIntArray3456[var28] = var13 + 1;
                        Class45.anIntArray729[var28] = var14;
                        Class84.anIntArrayArray1160[var13 + 1][var14] = 8;
                        Class97.anIntArrayArray1373[var13 + 1][var14] = var19;
                        var28 = 4095 & var28 - -1;
                        break;
                     }

                     if(~(var18[var5 + var13][var14 + var20] & 19661283) != -1) {
                        break;
                     }

                     ++var20;
                  }
               }

               if(-1 > ~var14 && 0 == Class84.anIntArrayArray1160[var13][-1 + var14] && (19661070 & var18[var13][-1 + var14]) == 0 && 0 == (19661187 & var18[-1 + var5 + var13][var14 + -1])) {
                  var20 = 1;

                  while(true) {
                     if(-1 + var5 <= var20) {
                        Class3_Sub13_Sub38.anIntArray3456[var28] = var13;
                        Class45.anIntArray729[var28] = -1 + var14;
                        Class84.anIntArrayArray1160[var13][-1 + var14] = 1;
                        var28 = 4095 & 1 + var28;
                        Class97.anIntArrayArray1373[var13][-1 + var14] = var19;
                        break;
                     }

                     if((var18[var13 + var20][var14 + -1] & 19661199) != 0) {
                        break;
                     }

                     ++var20;
                  }
               }

               if(102 > var14 && ~Class84.anIntArrayArray1160[var13][1 + var14] == -1 && ~(var18[var13][var14 + var5] & 19661112) == -1 && -1 == ~(19661280 & var18[-1 + var13 + var5][var5 + var14])) {
                  var20 = 1;

                  while(true) {
                     if(~var20 <= ~(var5 + -1)) {
                        Class3_Sub13_Sub38.anIntArray3456[var28] = var13;
                        Class45.anIntArray729[var28] = var14 + 1;
                        Class84.anIntArrayArray1160[var13][1 + var14] = 4;
                        Class97.anIntArrayArray1373[var13][1 + var14] = var19;
                        var28 = 4095 & var28 + 1;
                        break;
                     }

                     if(-1 != ~(19661304 & var18[var13 - -var20][var5 + var14])) {
                        break;
                     }

                     ++var20;
                  }
               }

               if(var13 > 0 && -1 > ~var14 && -1 == ~Class84.anIntArrayArray1160[var13 + -1][var14 + -1] && ~(var18[var13 + -1][-1 + var5 + -1 + var14] & 19661112) == -1 && 0 == (19661070 & var18[-1 + var13][var14 - 1]) && ~(var18[var5 + -1 + (var13 - 1)][-1 + var14] & 19661187) == -1) {
                  var20 = 1;

                  while(true) {
                     if(var5 - 1 <= var20) {
                        Class3_Sub13_Sub38.anIntArray3456[var28] = var13 - 1;
                        Class45.anIntArray729[var28] = -1 + var14;
                        var28 = 4095 & var28 + 1;
                        Class84.anIntArrayArray1160[var13 + -1][-1 + var14] = 3;
                        Class97.anIntArrayArray1373[-1 + var13][var14 + -1] = var19;
                        break;
                     }

                     if((var18[var13 - 1][var14 - 1 + var20] & 19661118) != 0 || 0 != (19661199 & var18[var20 + -1 + var13][-1 + var14])) {
                        break;
                     }

                     ++var20;
                  }
               }

               if(102 > var13 && ~var14 < -1 && -1 == ~Class84.anIntArrayArray1160[1 + var13][-1 + var14] && -1 == ~(19661070 & var18[1 + var13][-1 + var14]) && (var18[var5 + var13][-1 + var14] & 19661187) == 0 && (var18[var13 - -var5][-1 + var14 + var5 + -1] & 19661280) == 0) {
                  var20 = 1;

                  while(true) {
                     if(~var20 <= ~(-1 + var5)) {
                        Class3_Sub13_Sub38.anIntArray3456[var28] = 1 + var13;
                        Class45.anIntArray729[var28] = -1 + var14;
                        var28 = 1 + var28 & 4095;
                        Class84.anIntArrayArray1160[var13 - -1][-1 + var14] = 9;
                        Class97.anIntArrayArray1373[1 + var13][-1 + var14] = var19;
                        break;
                     }

                     if(-1 != ~(19661283 & var18[var13 + var5][var14 - (1 + -var20)]) || ~(19661199 & var18[var20 + (var13 - -1)][-1 + var14]) != -1) {
                        break;
                     }

                     ++var20;
                  }
               }

               if(~var13 < -1 && 102 > var14 && -1 == ~Class84.anIntArrayArray1160[-1 + var13][var14 - -1] && 0 == (19661070 & var18[var13 - 1][1 + var14]) && (19661112 & var18[-1 + var13][var14 + var5]) == 0 && 0 == (19661280 & var18[var13][var14 + var5])) {
                  var20 = 1;

                  while(true) {
                     if(-1 + var5 <= var20) {
                        Class3_Sub13_Sub38.anIntArray3456[var28] = var13 - 1;
                        Class45.anIntArray729[var28] = var14 + 1;
                        var28 = 4095 & var28 - -1;
                        Class84.anIntArrayArray1160[-1 + var13][1 + var14] = 6;
                        Class97.anIntArrayArray1373[-1 + var13][1 + var14] = var19;
                        break;
                     }

                     if((var18[var13 - 1][var14 - -1 - -var20] & 19661118) != 0 || ~(var18[var20 + -1 + var13][var5 + var14] & 19661304) != -1) {
                        break;
                     }

                     ++var20;
                  }
               }

               if(~var13 > -103 && -103 < ~var14 && Class84.anIntArrayArray1160[var13 - -1][1 + var14] == 0 && 0 == (19661112 & var18[var13 + 1][var14 + var5]) && 0 == (var18[var13 - -var5][var14 + var5] & 19661280) && ~(19661187 & var18[var5 + var13][1 + var14]) == -1) {
                  for(var20 = 1; var20 < -1 + var5; ++var20) {
                     if(-1 != ~(var18[var20 + var13 - -1][var14 - -var5] & 19661304) || 0 != (var18[var5 + var13][var20 + (var14 - -1)] & 19661283)) {
                        continue label410;
                     }
                  }

                  Class3_Sub13_Sub38.anIntArray3456[var28] = 1 + var13;
                  Class45.anIntArray729[var28] = var14 - -1;
                  Class84.anIntArrayArray1160[1 + var13][1 + var14] = 12;
                  Class97.anIntArrayArray1373[1 + var13][1 + var14] = var19;
                  var28 = 1 + var28 & 4095;
               }
            }

            Class129.anInt1692 = 0;
            if(!var17) {
               if(!var11) {
                  return false;
               }

               var19 = 1000;
               var20 = 100;
               byte var21 = 10;

               for(int var22 = var6 - var21; var22 <= var21 + var6; ++var22) {
                  for(int var23 = -var21 + var0; ~var23 >= ~(var0 - -var21); ++var23) {
                     if(var22 >= 0 && -1 >= ~var23 && ~var22 > -105 && ~var23 > -105 && -101 < ~Class97.anIntArrayArray1373[var22][var23]) {
                        int var24 = 0;
                        if(~var6 < ~var22) {
                           var24 = -var22 + var6;
                        } else if(~(var6 - (-var2 - -1)) > ~var22) {
                           var24 = -var2 + -var6 - -1 + var22;
                        }

                        int var25 = 0;
                        if(~var23 > ~var0) {
                           var25 = -var23 + var0;
                        } else if(~(var0 + var10 + -1) > ~var23) {
                           var25 = var23 + 1 + -var0 + -var10;
                        }

                        int var26 = var24 * var24 + var25 * var25;
                        if(~var26 > ~var19 || var26 == var19 && ~var20 < ~Class97.anIntArrayArray1373[var22][var23]) {
                           var20 = Class97.anIntArrayArray1373[var22][var23];
                           var13 = var22;
                           var19 = var26;
                           var14 = var23;
                        }
                     }
                  }
               }

               if(var19 == 1000) {
                  return false;
               }

               if(~var13 == ~var9 && var12 == var14) {
                  return false;
               }

               Class129.anInt1692 = 1;
            }

            byte var29 = 0;
            Class3_Sub13_Sub38.anIntArray3456[var29] = var13;
            var16 = var29 + 1;
            Class45.anIntArray729[var29] = var14;

            for(var19 = var20 = Class84.anIntArrayArray1160[var13][var14]; ~var13 != ~var9 || ~var12 != ~var14; var19 = Class84.anIntArrayArray1160[var13][var14]) {
               if(~var20 != ~var19) {
                  Class3_Sub13_Sub38.anIntArray3456[var16] = var13;
                  var20 = var19;
                  Class45.anIntArray729[var16++] = var14;
               }

               if(-1 == ~(2 & var19)) {
                  if(0 != (8 & var19)) {
                     --var13;
                  }
               } else {
                  ++var13;
               }

               if(~(var19 & 1) != -1) {
                  ++var14;
               } else if(-1 != ~(var19 & 4)) {
                  --var14;
               }
            }

            if(0 >= var16) {
               if(var3 == 1) {
                  return false;
               } else {
                  return true;
               }
            } else {
               Class3_Sub13_Sub27.method299(93, var16, var3);
               return true;
            }
         }
      } catch (RuntimeException var27) {
         throw Class44.method1067(var27, "hh.C(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ',' + var9 + ',' + var10 + ',' + var11 + ',' + var12 + ')');
      }
   }

   final Model method1167(int var1, byte var2, AnimationDefinition var3, int var4, int var5) {
      try {
         if(0 == ~this.pnpcId) {
            Model var6 = (Model)Class80.aClass93_1131.get(this.aLong860, (byte)121);
            if(var2 < 122) {
               this.anIntArray862 = (int[])null;
            }

            if(var6 == null) {
               boolean var7 = false;

               int var9;
               for(int var8 = 0; var8 < 12; ++var8) {
                  var9 = this.lookInfo[var8];
                  if(0 == (1073741824 & var9)) {
                     if(~(var9 & Integer.MIN_VALUE) != -1 && !Class3_Sub13_Sub13.method231(var9 & 1073741823, 0).method948(18991)) {
                        var7 = true;
                     }
                  } else if(!Class38.getItemDefinition(1073741823 & var9, (byte)127).method1102(this.aBoolean864, false)) {
                     var7 = true;
                  }
               }

               if(var7) {
                  return null;
               }

               Model_Sub1[] var14 = new Model_Sub1[12];
               var9 = 0;

               int var11;
               for(int var10 = 0; ~var10 > -13; ++var10) {
                  var11 = this.lookInfo[var10];
                  Model_Sub1 var12;
                  if(~(1073741824 & var11) == -1) {
                     if(0 != (Integer.MIN_VALUE & var11)) {
                        var12 = Class3_Sub13_Sub13.method231(1073741823 & var11, 0).method941(true);
                        if(null != var12) {
                           var14[var9++] = var12;
                        }
                     }
                  } else {
                     var12 = Class38.getItemDefinition(var11 & 1073741823, (byte)89).method1116(this.aBoolean864, (byte)-109);
                     if(var12 != null) {
                        var14[var9++] = var12;
                     }
                  }
               }

               Model_Sub1 var15 = new Model_Sub1(var14, var9);

               for(var11 = 0; ~var11 > -6; ++var11) {
                  if(~Class15.aShortArrayArray344[var11].length < ~this.anIntArray862[var11]) {
                     var15.method2016(Class3_Sub25.aShortArray2548[var11], Class15.aShortArrayArray344[var11][this.anIntArray862[var11]]);
                  }

                  if(Class101.aShortArrayArray1429[var11].length > this.anIntArray862[var11]) {
                     var15.method2016(Class91.aShortArray1311[var11], Class101.aShortArrayArray1429[var11][this.anIntArray862[var11]]);
                  }
               }

               var6 = var15.method2008(64, 768, -50, -10, -50);
               Class80.aClass93_1131.put((byte)-102, var6, this.aLong860);
            }

            if(var3 != null) {
               var6 = var3.method2055(var6, (byte)120, var5, var1, var4);
            }

            return var6;
         } else {
            return Node.method522(this.pnpcId, 27112).getChatModel(var3, var1, var5, -109, var4);
         }
      } catch (RuntimeException var13) {
         throw Class44.method1067(var13, "hh.F(" + var1 + ',' + var2 + ',' + (var3 != null?"{...}":"null") + ',' + var4 + ',' + var5 + ')');
      }
   }

   public static void method1168(int var0) {
      try {
         aClass94_853 = null;
         aClass94_852 = null;
         if(var0 == 8160) {
            anIntArray859 = null;
            aClass94_856 = null;
            anIntArray861 = null;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "hh.H(" + var0 + ')');
      }
   }

}
