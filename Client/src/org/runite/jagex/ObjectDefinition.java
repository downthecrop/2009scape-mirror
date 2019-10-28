package org.runite.jagex;

final class ObjectDefinition {

   private short[] aShortArray1476;
   private short[] aShortArray1477;
   int anInt1478;
   private int anInt1479;
   int anInt1480 = 1;
   private int anInt1481;
   int anInt1482;
   boolean aBoolean1483 = false;
   int anInt1484;
   int anInt1485 = 1;
   boolean aBoolean1486;
   private int[] configuration;
   private int anInt1488;
   private int anInt1489;
   static boolean[] aBooleanArray1490 = new boolean[112];
   boolean aBoolean1491;
   boolean aBoolean1492;
   int anInt1493;
   private int anInt1494 = 0;
   private short[] aShortArray1495;
   private int anInt1496;
   static int[][][] anIntArrayArrayArray1497 = new int[4][13][13];
   boolean aBoolean1498;
   RSString[] options;
   private short aShort1500;
   private Class130 aClass130_1501;
   boolean aBoolean1502 = false;
   boolean aBoolean1503;
   RSString name;
   private byte aByte1505;
   private short[] aShortArray1506;
   boolean aBoolean1507;
   static RSString aClass94_1508 = RSString.createRSString("Choisir une option");
   static RSString aClass94_1509 = RSString.createRSString("Chargement des textures )2 ");
   boolean aBoolean1510;
   private int anInt1511;
   int anInt1512;
   private byte[] aByteArray1513;
   static int worldId = 1;
   int anInt1515;
   int anInt1516;
   int anInt1517;
   int anInt1518;
   private int[] models;
   int anInt1520;
   static int anInt1521 = 0;
   int anInt1522;
   static RSString aClass94_1523 = RSString.createRSString("Chargement en cours)3 Veuillez patienter)3");
   int[] anIntArray1524;
   boolean aBoolean1525;
   private int anInt1526;
   int objectId;
   int anInt1528;
   int anInt1529;
   boolean aBoolean1530;
   int animationId;
   private int anInt1532;
   int anInt1533;
   private int anInt1534;
   static short aShort1535 = 320;
   private boolean aBoolean1536;
   boolean aBoolean1537;
   int actionCount;
   int[] anIntArray1539;
   int anInt1540;
   private boolean aBoolean1541;
   boolean aBoolean1542;


   final boolean method1684(int var1, int var2) {
      try {
         int var3 = -1 % ((31 - var1) / 41);
         if(this.configuration != null) {
            for(int var7 = 0; ~this.configuration.length < ~var7; ++var7) {
               if(~var2 == ~this.configuration[var7]) {
                  return Class69.aClass153_1043.method2129((byte)72, 0, this.models[var7] & '\uffff');
               }
            }

            return true;
         } else if(null == this.models) {
            return true;
         } else if(-11 != ~var2) {
            return true;
         } else {
            boolean var4 = true;

            for(int var5 = 0; this.models.length > var5; ++var5) {
               var4 &= Class69.aClass153_1043.method2129((byte)71, 0, '\uffff' & this.models[var5]);
            }

            return var4;
         }
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "pb.H(" + var1 + ',' + var2 + ')');
      }
   }

   final ObjectDefinition method1685(int var1) {
      try {
         if(var1 != 0) {
            this.method1697(-92, 83, (LDIndexedSprite)null, -13, (AnimationDefinition)null, 18, (int[][])((int[][])null), true, 114, 123, (int[][])((int[][])null), 118, 85, -116);
         }

         int var2 = -1;
         if(this.anInt1526 != -1) {
            var2 = NPCDefinition.method1484(64835055, this.anInt1526);
         } else if(this.anInt1532 != -1) {
            var2 = Class163_Sub1.anIntArray2985[this.anInt1532];
         }

         if(~var2 <= -1 && ~var2 > ~(this.anIntArray1524.length - 1) && ~this.anIntArray1524[var2] != 0) {
            return Class162.getObjectDefinition(4, this.anIntArray1524[var2]);
         } else {
            int var3 = this.anIntArray1524[-1 + this.anIntArray1524.length];
            return ~var3 == 0?null:Class162.getObjectDefinition(4, var3);
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "pb.C(" + var1 + ')');
      }
   }

   private final Model_Sub1 method1686(int var1, int var2, int var3) {
      try {
         Model_Sub1 var4 = null;
         boolean var5 = this.aBoolean1536;
         if(-3 == ~var2 && 3 < var1) {
            var5 = !var5;
         }

         int var6;
         int var7;
         if(null == this.configuration) {
            if(-11 != ~var2) {
               return null;
            }

            if(this.models == null) {
               return null;
            }

            var6 = this.models.length;

            for(var7 = 0; ~var7 > ~var6; ++var7) {
               int var8 = this.models[var7];
               if(var5) {
                  var8 += 65536;
               }

               var4 = (Model_Sub1)Class99.aClass93_1401.get((long)var8, (byte)121);
               if(var4 == null) {
                  var4 = Model_Sub1.method2015(Class69.aClass153_1043, var8 & '\uffff', 0);
                  if(var4 == null) {
                     return null;
                  }

                  if(var5) {
                     var4.method2002();
                  }

                  Class99.aClass93_1401.put((byte)-91, var4, (long)var8);
               }

               if(1 < var6) {
                  Class164.aClass140_Sub5Array2058[var7] = var4;
               }
            }

            if(~var6 < -2) {
               var4 = new Model_Sub1(Class164.aClass140_Sub5Array2058, var6);
            }
         } else {
            var6 = -1;

            for(var7 = 0; this.configuration.length > var7; ++var7) {
               if(var2 == this.configuration[var7]) {
                  var6 = var7;
                  break;
               }
            }

            if(~var6 == 0) {
               return null;
            }

            var7 = this.models[var6];
            if(var5) {
               var7 += 65536;
            }

            var4 = (Model_Sub1)Class99.aClass93_1401.get((long)var7, (byte)121);
            if(null == var4) {
               var4 = Model_Sub1.method2015(Class69.aClass153_1043, var7 & '\uffff', 0);
               if(null == var4) {
                  return null;
               }

               if(var5) {
                  var4.method2002();
               }

               Class99.aClass93_1401.put((byte)-122, var4, (long)var7);
            }
         }

         boolean var11;
         if(128 == this.anInt1479 && ~this.anInt1488 == -129 && 128 == this.anInt1481) {
            var11 = false;
         } else {
            var11 = true;
         }

         boolean var12;
         if(this.anInt1496 == 0 && -1 == ~this.anInt1511 && 0 == this.anInt1534) {
            var12 = false;
         } else {
            var12 = true;
         }

         Model_Sub1 var13 = new Model_Sub1(var4, var3 == ~var1 && !var11 && !var12, this.aShortArray1477 == null, null == this.aShortArray1476, true);
         if(~var2 == -5 && var1 > 3) {
            var13.method2011(256);
            var13.method2001(45, 0, -45);
         }

         var1 &= 3;
         if(-2 != ~var1) {
            if(-3 != ~var1) {
               if(3 == var1) {
                  var13.method2018();
               }
            } else {
               var13.method1989();
            }
         } else {
            var13.method1991();
         }

         int var9;
         if(null != this.aShortArray1477) {
            for(var9 = 0; this.aShortArray1477.length > var9; ++var9) {
               if(null != this.aByteArray1513 && this.aByteArray1513.length > var9) {
                  var13.method2016(this.aShortArray1477[var9], Class3_Sub13_Sub9.aShortArray3110[255 & this.aByteArray1513[var9]]);
               } else {
                  var13.method2016(this.aShortArray1477[var9], this.aShortArray1506[var9]);
               }
            }
         }

         if(this.aShortArray1476 != null) {
            for(var9 = 0; this.aShortArray1476.length > var9; ++var9) {
               var13.method1998(this.aShortArray1476[var9], this.aShortArray1495[var9]);
            }
         }

         if(var11) {
            var13.method1994(this.anInt1479, this.anInt1488, this.anInt1481);
         }

         if(var12) {
            var13.method2001(this.anInt1496, this.anInt1511, this.anInt1534);
         }

         return var13;
      } catch (RuntimeException var10) {
         throw Class44.method1067(var10, "pb.O(" + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   public static void method1687(int var0) {
      try {
         aClass94_1508 = null;
         aClass94_1523 = null;
         anIntArrayArrayArray1497 = (int[][][])null;
         aBooleanArray1490 = null;
         aClass94_1509 = null;
         if(var0 != -11) {
            anInt1521 = -96;
         }

      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "pb.B(" + var0 + ')');
      }
   }

   static final Class72 method1688(int var0, int var1, int var2) {
      Class3_Sub2 var3 = Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2];
      if(var3 == null) {
         return null;
      } else {
         Class72 var4 = var3.aClass72_2245;
         var3.aClass72_2245 = null;
         return var4;
      }
   }

   final void method1689(int var1) {
      try {
         if(this.anInt1529 == -1) {
            this.anInt1529 = 0;
            if(null != this.models && (null == this.configuration || -11 == ~this.configuration[0])) {
               this.anInt1529 = 1;
            }

            for(int var2 = 0; var2 < 5; ++var2) {
               if(this.options[var2] != null) {
                  this.anInt1529 = 1;
                  break;
               }
            }
         }

         if(var1 != -2116) {
            this.method1692(67, (RSByteBuffer)null);
         }

         if(-1 == this.anInt1540) {
            this.anInt1540 = ~this.actionCount != -1?1:0;
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "pb.D(" + var1 + ')');
      }
   }

   final boolean method1690(int var1) {
      try {
         if(this.anIntArray1524 != null) {
            if(var1 != 28933) {
               this.method1696(34, 54, (int[][])((int[][])null), 55, 80, (int[][])((int[][])null), true, (LDIndexedSprite)null, (byte)127, true, -38);
            }

            for(int var2 = 0; ~var2 > ~this.anIntArray1524.length; ++var2) {
               if(0 != ~this.anIntArray1524[var2]) {
                  ObjectDefinition var3 = Class162.getObjectDefinition(var1 + -28929, this.anIntArray1524[var2]);
                  if(0 != ~var3.anInt1512 || var3.anIntArray1539 != null) {
                     return true;
                  }
               }
            }

            return false;
         } else {
            return this.anInt1512 != -1 || this.anIntArray1539 != null;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "pb.F(" + var1 + ')');
      }
   }

   final int method1691(int var1, int var2, byte var3) {
      try {
         if(var3 <= 76) {
            return -40;
         } else if(this.aClass130_1501 == null) {
            return var1;
         } else {
            Class3_Sub18 var4 = (Class3_Sub18)this.aClass130_1501.method1780((long)var2, 0);
            return var4 != null?var4.anInt2467:var1;
         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "pb.N(" + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   final void method1692(int var1, RSByteBuffer var2) {
      try {
         while(true) {
            int var3 = var2.getByte((byte)-89);
            if(-1 == ~var3) {
               if(var1 != 6219) {
                  this.method1696(105, -55, (int[][])((int[][])null), -39, 71, (int[][])((int[][])null), true, (LDIndexedSprite)null, (byte)-117, false, -25);
               }

               return;
            }

            this.parseOpcode(var2, var3, -80);
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "pb.G(" + var1 + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

   private final void parseOpcode(RSByteBuffer buffer, int opcode, int var3) {
      try {
         if(var3 != -80) {
            this.anInt1522 = -122;
         }

         int var4;
         int var5;
         if(1 == opcode) {
            var4 = buffer.getByte((byte)-77);
            if(~var4 < -1) {
               if(this.models != null && !Class47.aBoolean742) {
                  buffer.index += var4 * 3;
               } else {
                  this.configuration = new int[var4];
                  this.models = new int[var4];

                  for(var5 = 0; var4 > var5; ++var5) {
                     this.models[var5] = buffer.getShort(Class93.method1519(var3, -79));
                     this.configuration[var5] = buffer.getByte((byte)-75);
                  }
               }
            }
         } else if(-3 == ~opcode) {
            this.name = buffer.getString();
         } else if(-6 != ~opcode) {
            if(~opcode == -15) {
               this.anInt1480 = buffer.getByte((byte)-124);
            } else if(-16 != ~opcode) {
               if(~opcode != -18) {
                  if(18 != opcode) {
                     if(opcode != 19) {
                        if(~opcode != -22) {
                           if(~opcode != -23) {
                              if(-24 != ~opcode) {
                                 if(~opcode == -25) {
                                    this.animationId = buffer.getShort(var3 ^ -79);
                                    if(~this.animationId == -65536) {
                                       this.animationId = -1;
                                    }
                                 } else if(-28 != ~opcode) {
                                    if(28 == opcode) {
                                       this.anInt1528 = buffer.getByte((byte)-112);
                                    } else if(-30 != ~opcode) {
                                       if(39 == opcode) {
                                          this.anInt1489 = buffer.getByte() * 5;
                                       } else if(~opcode <= -31 && -36 < ~opcode) {
                                          this.options[opcode - 30] = buffer.getString();
                                          if(this.options[-30 + opcode].equals(-112, Class3_Sub13_Sub3.aClass94_3051)) {
                                             this.options[-30 + opcode] = null;
                                          }
                                       } else if(opcode == 40) {
                                          var4 = buffer.getByte((byte)-27);
                                          this.aShortArray1477 = new short[var4];
                                          this.aShortArray1506 = new short[var4];

                                          for(var5 = 0; var5 < var4; ++var5) {
                                             this.aShortArray1477[var5] = (short)buffer.getShort(1);
                                             this.aShortArray1506[var5] = (short)buffer.getShort(1);
                                          }
                                       } else if(opcode != 41) {
                                          if(opcode == 42) {
                                             var4 = buffer.getByte((byte)-118);
                                             this.aByteArray1513 = new byte[var4];

                                             for(var5 = 0; ~var5 > ~var4; ++var5) {
                                                this.aByteArray1513[var5] = buffer.getByte();
                                             }
                                          } else if(opcode != 60) {
                                             if(~opcode == -63) {
                                                this.aBoolean1536 = true;
                                             } else if(-65 != ~opcode) {
                                                if(~opcode == -66) {
                                                   this.anInt1479 = buffer.getShort(1);
                                                } else if(-67 == ~opcode) {
                                                   this.anInt1488 = buffer.getShort(1);
                                                } else if(-68 == ~opcode) {
                                                   this.anInt1481 = buffer.getShort(var3 + 81);
                                                } else if(opcode != 69) {
                                                   if(70 == opcode) {
                                                      this.anInt1496 = buffer.getShort((byte)123);
                                                   } else if(71 == opcode) {
                                                      this.anInt1511 = buffer.getShort((byte)94);
                                                   } else if(72 == opcode) {
                                                      this.anInt1534 = buffer.getShort((byte)76);
                                                   } else if(opcode != 73) {
                                                      if(opcode != 74) {
                                                         if(75 == opcode) {
                                                            this.anInt1540 = buffer.getByte((byte)-66);
                                                         } else if(~opcode != -78 && -93 != ~opcode) {
                                                            if(78 != opcode) {
                                                               if(~opcode != -80) {
                                                                  if(81 != opcode) {
                                                                     if(~opcode != -83) {
                                                                        if(opcode != 88) {
                                                                           if(-90 != ~opcode) {
                                                                              if(90 != opcode) {
                                                                                 if(opcode == 91) {
                                                                                    this.aBoolean1491 = true;
                                                                                 } else if(-94 == ~opcode) {
                                                                                    this.aByte1505 = 3;
                                                                                    this.aShort1500 = (short)buffer.getShort(1);
                                                                                 } else if(opcode != 94) {
                                                                                    if(~opcode == -96) {
                                                                                       this.aByte1505 = 5;
                                                                                    } else if(~opcode != -97) {
                                                                                       if(~opcode == -98) {
                                                                                          this.aBoolean1537 = true;
                                                                                       } else if(opcode == 98) {
                                                                                          this.aBoolean1510 = true;
                                                                                       } else if(~opcode != -100) {
                                                                                          if(-101 == ~opcode) {
                                                                                             this.anInt1520 = buffer.getByte((byte)-27);
                                                                                             this.anInt1522 = buffer.getShort(var3 ^ -79);
                                                                                          } else if(~opcode != -102) {
                                                                                             if(opcode != 102) {
                                                                                                if(249 == opcode) {
                                                                                                   var4 = buffer.getByte((byte)-88);
                                                                                                   if(null == this.aClass130_1501) {
                                                                                                      var5 = Class95.method1585((byte)83, var4);
                                                                                                      this.aClass130_1501 = new Class130(var5);
                                                                                                   }

                                                                                                   for(var5 = 0; var4 > var5; ++var5) {
                                                                                                      boolean var10 = -2 == ~buffer.getByte((byte)-84);
                                                                                                      int var7 = buffer.getTriByte((byte)122);
                                                                                                      Object var8;
                                                                                                      if(!var10) {
                                                                                                         var8 = new Class3_Sub18(buffer.getInt());
                                                                                                      } else {
                                                                                                         var8 = new Class3_Sub29(buffer.getString());
                                                                                                      }

                                                                                                      this.aClass130_1501.method1779(var3 ^ -79, (Class3)var8, (long)var7);
                                                                                                   }
                                                                                                }
                                                                                             } else {
                                                                                                this.anInt1516 = buffer.getShort(1);
                                                                                             }
                                                                                          } else {
                                                                                             this.anInt1478 = buffer.getByte((byte)-114);
                                                                                          }
                                                                                       } else {
                                                                                          this.anInt1493 = buffer.getByte((byte)-115);
                                                                                          this.anInt1517 = buffer.getShort(1);
                                                                                       }
                                                                                    } else {
                                                                                       this.aBoolean1507 = true;
                                                                                    }
                                                                                 } else {
                                                                                    this.aByte1505 = 4;
                                                                                 }
                                                                              } else {
                                                                                 this.aBoolean1502 = true;
                                                                              }
                                                                           } else {
                                                                              this.aBoolean1492 = false;
                                                                           }
                                                                        } else {
                                                                           this.aBoolean1503 = false;
                                                                        }
                                                                     } else {
                                                                        this.aBoolean1530 = true;
                                                                     }
                                                                  } else {
                                                                     this.aByte1505 = 2;
                                                                     this.aShort1500 = (short)(256 * buffer.getByte((byte)-52));
                                                                  }
                                                               } else {
                                                                  this.anInt1518 = buffer.getShort(1);
                                                                  this.anInt1515 = buffer.getShort(1);
                                                                  this.anInt1484 = buffer.getByte((byte)-100);
                                                                  var4 = buffer.getByte((byte)-95);
                                                                  this.anIntArray1539 = new int[var4];

                                                                  for(var5 = 0; ~var4 < ~var5; ++var5) {
                                                                     this.anIntArray1539[var5] = buffer.getShort(1);
                                                                  }
                                                               }
                                                            } else {
                                                               this.anInt1512 = buffer.getShort(var3 + 81);
                                                               this.anInt1484 = buffer.getByte((byte)-90);
                                                            }
                                                         } else {
                                                            var4 = -1;
                                                            this.anInt1526 = buffer.getShort(1);
                                                            if('\uffff' == this.anInt1526) {
                                                               this.anInt1526 = -1;
                                                            }

                                                            this.anInt1532 = buffer.getShort(1);
                                                            if('\uffff' == this.anInt1532) {
                                                               this.anInt1532 = -1;
                                                            }

                                                            if(92 == opcode) {
                                                               var4 = buffer.getShort(1);
                                                               if(var4 == '\uffff') {
                                                                  var4 = -1;
                                                               }
                                                            }

                                                            var5 = buffer.getByte((byte)-66);
                                                            this.anIntArray1524 = new int[var5 - -2];

                                                            for(int var6 = 0; var5 >= var6; ++var6) {
                                                               this.anIntArray1524[var6] = buffer.getShort(1);
                                                               if('\uffff' == this.anIntArray1524[var6]) {
                                                                  this.anIntArray1524[var6] = -1;
                                                               }
                                                            }

                                                            this.anIntArray1524[1 + var5] = var4;
                                                         }
                                                      } else {
                                                         this.aBoolean1498 = true;
                                                      }
                                                   } else {
                                                      this.aBoolean1483 = true;
                                                   }
                                                } else {
                                                   this.anInt1533 = buffer.getByte((byte)-55);
                                                }
                                             } else {
                                                this.aBoolean1525 = false;
                                             }
                                          } else {
                                             this.anInt1482 = buffer.getShort(var3 ^ -79);
                                          }
                                       } else {
                                          var4 = buffer.getByte((byte)-79);
                                          this.aShortArray1495 = new short[var4];
                                          this.aShortArray1476 = new short[var4];

                                          for(var5 = 0; ~var5 > ~var4; ++var5) {
                                             this.aShortArray1476[var5] = (short)buffer.getShort(1);
                                             this.aShortArray1495[var5] = (short)buffer.getShort(Class93.method1519(var3, -79));
                                          }
                                       }
                                    } else {
                                       this.anInt1494 = buffer.getByte();
                                    }
                                 } else {
                                    this.actionCount = 1;
                                 }
                              } else {
                                 this.aBoolean1542 = true;
                              }
                           } else {
                              this.aBoolean1541 = true;
                           }
                        } else {
                           this.aByte1505 = 1;
                        }
                     } else {
                        this.anInt1529 = buffer.getByte((byte)-79);
                     }
                  } else {
                     this.aBoolean1486 = false;
                  }
               } else {
                  this.actionCount = 0;
                  this.aBoolean1486 = false;
               }
            } else {
               this.anInt1485 = buffer.getByte((byte)-42);
            }
         } else {
            var4 = buffer.getByte((byte)-62);
            if(~var4 < -1) {
               if(null != this.models && !Class47.aBoolean742) {
                  buffer.index += var4 * 2;
               } else {
                  this.models = new int[var4];
                  this.configuration = null;

                  for(var5 = 0; var4 > var5; ++var5) {
                     this.models[var5] = buffer.getShort(1);
                  }
               }
            }
         }

      } catch (RuntimeException var9) {
         throw Class44.method1067(var9, "pb.K(" + (buffer != null?"{...}":"null") + ',' + opcode + ',' + var3 + ')');
      }
   }

   final boolean hasModels(boolean var1) {
      try {
         if(null == this.models) {
            return true;
         } else {
            boolean var2 = true;

            for(int var3 = 0; ~this.models.length < ~var3; ++var3) {
               var2 &= Class69.aClass153_1043.method2129((byte)64, 0, '\uffff' & this.models[var3]);
            }

            return var1?true:var2;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "pb.I(" + var1 + ')');
      }
   }

   private final Class140_Sub1_Sub1 method1695(int var1, boolean var2, boolean var3, int var4) {
      try {
         int var6 = this.anInt1494 + 64;
         int var7 = 5 * this.anInt1489 + 768;
         Class140_Sub1_Sub1 var5 = null;
         int var8;
         int var12;
         if(this.configuration != null) {
            var8 = -1;

            int var9;
            for(var9 = 0; ~var9 > ~this.configuration.length; ++var9) {
               if(~this.configuration[var9] == ~var4) {
                  var8 = var9;
                  break;
               }
            }

            if(0 == ~var8) {
               return null;
            }

            var9 = this.models[var8];
            if(var2) {
               var9 += 65536;
            }

            var5 = (Class140_Sub1_Sub1)Class99.aClass93_1401.get((long)var9, (byte)121);
            if(null == var5) {
               Model_Sub1 var10 = Model_Sub1.method2015(Class69.aClass153_1043, '\uffff' & var9, 0);
               if(null == var10) {
                  return null;
               }

               var5 = new Class140_Sub1_Sub1(var10, var6, var7, var2);
               Class99.aClass93_1401.put((byte)-101, var5, (long)var9);
            }
         } else {
            if(var4 != 10) {
               return null;
            }

            if(this.models == null) {
               return null;
            }

            var8 = this.models.length;
            if(-1 == ~var8) {
               return null;
            }

            long var16 = 0L;

            for(int var11 = 0; ~var8 < ~var11; ++var11) {
               var16 = (long)this.models[var11] + var16 * 67783L;
            }

            if(var2) {
               var16 = ~var16;
            }

            var5 = (Class140_Sub1_Sub1)Class99.aClass93_1401.get(var16, (byte)121);
            if(null == var5) {
               Model_Sub1 var17 = null;

               for(var12 = 0; ~var8 < ~var12; ++var12) {
                  var17 = Model_Sub1.method2015(Class69.aClass153_1043, this.models[var12] & '\uffff', 0);
                  if(null == var17) {
                     return null;
                  }

                  if(var8 > 1) {
                     Class164.aClass140_Sub5Array2058[var12] = var17;
                  }
               }

               if(1 < var8) {
                  var17 = new Model_Sub1(Class164.aClass140_Sub5Array2058, var8);
               }

               var5 = new Class140_Sub1_Sub1(var17, var6, var7, var2);
               Class99.aClass93_1401.put((byte)-75, var5, var16);
            }
         }

         boolean var14 = this.aBoolean1536;
         if(~var4 == -3 && var1 > 3) {
            var14 = !var14;
         }

         boolean var15 = 128 == this.anInt1488 && -1 == ~this.anInt1511;
         boolean var18 = -1 == ~var1 && 128 == this.anInt1479 && ~this.anInt1481 == -129 && ~this.anInt1496 == -1 && this.anInt1534 == 0 && !var14;
         Class140_Sub1_Sub1 var19 = var5.method1926(var18, var15, this.aShortArray1477 == null, true, ~var5.method1903() == ~var6, -1 == ~var1 && !var14, var3, ~var7 == ~var5.method1924(), true, !var14, this.aShortArray1476 == null);
         if(var14) {
            var19.method1931();
         }

         if(var4 == 4 && 3 < var1) {
            var19.method1932(256);
            var19.method1897(45, 0, -45);
         }

         var1 &= 3;
         if(1 == var1) {
            var19.method1925();
         } else if(~var1 != -3) {
            if(~var1 == -4) {
               var19.method1902();
            }
         } else {
            var19.method1911();
         }

         if(null != this.aShortArray1477) {
            for(var12 = 0; ~this.aShortArray1477.length < ~var12; ++var12) {
               var19.method1918(this.aShortArray1477[var12], this.aShortArray1506[var12]);
            }
         }

         if(null != this.aShortArray1476) {
            for(var12 = 0; ~this.aShortArray1476.length < ~var12; ++var12) {
               var19.method1916(this.aShortArray1476[var12], this.aShortArray1495[var12]);
            }
         }

         if(this.anInt1479 != 128 || -129 != ~this.anInt1488 || ~this.anInt1481 != -129) {
            var19.resize(this.anInt1479, this.anInt1488, this.anInt1481);
         }

         if(-1 != ~this.anInt1496 || this.anInt1511 != 0 || 0 != this.anInt1534) {
            var19.method1897(this.anInt1496, this.anInt1511, this.anInt1534);
         }

         if(var6 != var19.method1903()) {
            var19.method1914(var6);
         }

         if(var19.method1924() != var7) {
            var19.method1909(var7);
         }

         return var19;
      } catch (RuntimeException var13) {
         throw Class44.method1067(var13, "pb.L(" + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ')');
      }
   }

   final Class136 method1696(int var1, int var2, int[][] var3, int var4, int var5, int[][] var6, boolean var7, LDIndexedSprite var8, byte var9, boolean var10, int var11) {
      try {
         if(var9 >= -5) {
            return (Class136)null;
         } else {
            long var12;
            if(!HDToolKit.highDetail) {
               if(this.configuration != null) {
                  var12 = (long)((var4 << 3) + ((this.objectId << 10) - -var1));
               } else {
                  var12 = (long)((this.objectId << 10) + var1);
               }

               boolean var20;
               if(var7 && this.aBoolean1541) {
                  var12 |= Long.MIN_VALUE;
                  var20 = true;
               } else {
                  var20 = false;
               }

               Object var22 = (GameObject)Class3_Sub28_Sub7_Sub1.aClass93_4051.get(var12, (byte)121);
               if(null == var22) {
                  Model_Sub1 var21 = this.method1686(var1, var4, -1);
                  if(var21 == null) {
                     Class100.aClass136_1413.aClass140_1777 = null;
                     return Class100.aClass136_1413;
                  }

                  var21.method2010();
                  if(-11 == ~var4 && var1 > 3) {
                     var21.method2011(256);
                  }

                  if(!var20) {
                     var22 = new Class140_Sub1_Sub2(var21, 64 - -this.anInt1494, 5 * this.anInt1489 + 768, -50, -10, -50);
                  } else {
                     var21.aShort2879 = (short)(64 + this.anInt1494);
                     var22 = var21;
                     var21.aShort2876 = (short)(768 + 5 * this.anInt1489);
                     var21.method1997();
                  }

                  Class3_Sub28_Sub7_Sub1.aClass93_4051.put((byte)-89, var22, var12);
               }

               if(var20) {
                  var22 = ((Model_Sub1)var22).method2004();
               }

               if(0 != this.aByte1505) {
                  if(!(var22 instanceof Class140_Sub1_Sub2)) {
                     if(var22 instanceof Model_Sub1) {
                        var22 = ((Model_Sub1)var22).method1999(this.aByte1505, this.aShort1500, var3, var6, var2, var5, var11, true, false);
                     }
                  } else {
                     var22 = ((Class140_Sub1_Sub2)var22).method1941(this.aByte1505, this.aShort1500, var3, var6, var2, var5, var11, true);
                  }
               }

               Class100.aClass136_1413.aClass140_1777 = (GameObject)var22;
               return Class100.aClass136_1413;
            } else {
               if(null != this.configuration) {
                  var12 = (long)(var1 + (this.objectId << 10) - -(var4 << 3));
               } else {
                  var12 = (long)((this.objectId << 10) + var1);
               }

               Class136 var16 = (Class136)Class3_Sub28_Sub7_Sub1.aClass93_4051.get(var12, (byte)121);
               Class140_Sub1_Sub1 var14;
               LDIndexedSprite var15;
               if(null == var16) {
                  var14 = this.method1695(var1, false, true, var4);
                  if(null == var14) {
                     Class100.aClass136_1413.aClass140_1777 = null;
                     Class100.aClass136_1413.aClass109_Sub1_1770 = null;
                     return Class100.aClass136_1413;
                  }

                  if(~var4 == -11 && var1 > 3) {
                     var14.method1876(256);
                  }

                  if(var10) {
                     var15 = var14.method1933(var8);
                  } else {
                     var15 = null;
                  }

                  var16 = new Class136();
                  var16.aClass140_1777 = var14;
                  var16.aClass109_Sub1_1770 = var15;
                  Class3_Sub28_Sub7_Sub1.aClass93_4051.put((byte)-93, var16, var12);
               } else {
                  var14 = (Class140_Sub1_Sub1)var16.aClass140_1777;
                  var15 = var16.aClass109_Sub1_1770;
               }

               boolean var17 = this.aBoolean1541 & var7;
               Class140_Sub1_Sub1 var18 = var14.method1926(3 != this.aByte1505, ~this.aByte1505 == -1, true, true, true, true, !var17, true, true, true, true);
               if(~this.aByte1505 != -1) {
                  var18.method1919(this.aByte1505, this.aShort1500, var14, var3, var6, var2, var5, var11);
               }

               var18.method1920(~this.anInt1529 == -1 && !this.aBoolean1510, true, true, true, -1 == ~this.anInt1529, true, false);
               Class100.aClass136_1413.aClass140_1777 = var18;
               var18.aBoolean3809 = var17;
               Class100.aClass136_1413.aClass109_Sub1_1770 = var15;
               return Class100.aClass136_1413;
            }
         }
      } catch (RuntimeException var19) {
         throw Class44.method1067(var19, "pb.A(" + var1 + ',' + var2 + ',' + (var3 != null?"{...}":"null") + ',' + var4 + ',' + var5 + ',' + (var6 != null?"{...}":"null") + ',' + var7 + ',' + (var8 != null?"{...}":"null") + ',' + var9 + ',' + var10 + ',' + var11 + ')');
      }
   }

   final Class136 method1697(int var1, int var2, LDIndexedSprite var3, int var4, AnimationDefinition var5, int var6, int[][] var7, boolean var8, int var9, int var10, int[][] var11, int var12, int var13, int var14) {
      try {
         if(var10 != 8308) {
            this.hasModels(false);
         }

         long var15;
         if(HDToolKit.highDetail) {
            if(this.configuration != null) {
               var15 = (long)((var13 << 3) + ((this.objectId << 10) - -var6));
            } else {
               var15 = (long)(var6 + (this.objectId << 10));
            }

            Class140_Sub1_Sub1 var23 = (Class140_Sub1_Sub1)Class154.aClass93_1965.get(var15, (byte)121);
            if(var23 == null) {
               var23 = this.method1695(var6, true, true, var13);
               if(null == var23) {
                  return null;
               }

               var23.method1908();
               var23.method1920(false, false, false, true, false, false, true);
               Class154.aClass93_1965.put((byte)-75, var23, var15);
            }

            boolean var19 = false;
            Class140_Sub1_Sub1 var22 = var23;
            if(null != var5) {
               var22 = (Class140_Sub1_Sub1)var5.method2056(var12, var9, var14, var6, var23, 3);
               var19 = true;
            }

            if(~var13 == -11 && 3 < var6) {
               if(!var19) {
                  var22 = (Class140_Sub1_Sub1)var22.method1890(true, true, true);
                  var19 = true;
               }

               var22.method1876(256);
            }

            if(var8) {
               Class100.aClass136_1413.aClass109_Sub1_1770 = var22.method1933(var3);
            } else {
               Class100.aClass136_1413.aClass109_Sub1_1770 = null;
            }

            if(this.aByte1505 != 0) {
               if(!var19) {
                  var19 = true;
                  var22 = (Class140_Sub1_Sub1)var22.method1890(true, true, true);
               }

               var22.method1919(this.aByte1505, this.aShort1500, var23, var7, var11, var2, var4, var1);
            }

            Class100.aClass136_1413.aClass140_1777 = var22;
            return Class100.aClass136_1413;
         } else {
            if(this.configuration == null) {
               var15 = (long)((this.objectId << 10) + var6);
            } else {
               var15 = (long)(var6 + (this.objectId << 10) + (var13 << 3));
            }

            Class140_Sub1_Sub2 var17 = (Class140_Sub1_Sub2)Class154.aClass93_1965.get(var15, (byte)121);
            if(var17 == null) {
               Model_Sub1 var18 = this.method1686(var6, var13, -1);
               if(var18 == null) {
                  return null;
               }

               var17 = new Class140_Sub1_Sub2(var18, 64 + this.anInt1494, this.anInt1489 * 5 + 768, -50, -10, -50);
               Class154.aClass93_1965.put((byte)-94, var17, var15);
            }

            boolean var21 = false;
            if(var5 != null) {
               var21 = true;
               var17 = (Class140_Sub1_Sub2)var5.method2054(19749, var9, var12, var17, var6, var14);
            }

            if(-11 == ~var13 && var6 > 3) {
               if(!var21) {
                  var21 = true;
                  var17 = (Class140_Sub1_Sub2)var17.method1890(true, true, true);
               }

               var17.method1876(256);
            }

            if(this.aByte1505 != 0) {
               if(!var21) {
                  var17 = (Class140_Sub1_Sub2)var17.method1890(true, true, true);
                  var21 = true;
               }

               var17 = var17.method1941(this.aByte1505, this.aShort1500, var7, var11, var2, var4, var1, false);
            }

            Class100.aClass136_1413.aClass140_1777 = var17;
            return Class100.aClass136_1413;
         }
      } catch (RuntimeException var20) {
         throw Class44.method1067(var20, "pb.M(" + var1 + ',' + var2 + ',' + (var3 != null?"{...}":"null") + ',' + var4 + ',' + (var5 != null?"{...}":"null") + ',' + var6 + ',' + (var7 != null?"{...}":"null") + ',' + var8 + ',' + var9 + ',' + var10 + ',' + (var11 != null?"{...}":"null") + ',' + var12 + ',' + var13 + ',' + var14 + ')');
      }
   }

   final RSString method1698(RSString var1, int var2, int var3) {
      try {
         if(var2 != -23085) {
            method1688(108, -11, 57);
         }

         if(null != this.aClass130_1501) {
            Class3_Sub29 var4 = (Class3_Sub29)this.aClass130_1501.method1780((long)var3, 0);
            return var4 == null?var1:var4.aClass94_2586;
         } else {
            return var1;
         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "pb.E(" + (var1 != null?"{...}":"null") + ',' + var2 + ',' + var3 + ')');
      }
   }

   public ObjectDefinition() {
      this.name = Class3_Sub13_Sub13.aClass94_3150;
      this.aBoolean1503 = true;
      this.anInt1493 = -1;
      this.anInt1515 = 0;
      this.anInt1516 = -1;
      this.aByte1505 = 0;
      this.aBoolean1491 = false;
      this.anInt1517 = -1;
      this.anInt1496 = 0;
      this.anInt1518 = 0;
      this.anInt1482 = -1;
      this.aBoolean1510 = false;
      this.anInt1520 = -1;
      this.aShort1500 = -1;
      this.anInt1481 = 128;
      this.options = new RSString[5];
      this.anInt1479 = 128;
      this.aBoolean1492 = true;
      this.anInt1488 = 128;
      this.aBoolean1498 = false;
      this.anInt1529 = -1;
      this.aBoolean1530 = false;
      this.aBoolean1525 = true;
      this.anInt1532 = -1;
      this.anInt1522 = -1;
      this.anInt1533 = 0;
      this.aBoolean1486 = true;
      this.anInt1534 = 0;
      this.anInt1478 = 0;
      this.anInt1528 = 16;
      this.aBoolean1537 = false;
      this.anInt1511 = 0;
      this.anInt1484 = 0;
      this.anInt1489 = 0;
      this.animationId = -1;
      this.aBoolean1507 = false;
      this.anInt1512 = -1;
      this.actionCount = 2;
      this.aBoolean1536 = false;
      this.anInt1526 = -1;
      this.anInt1540 = -1;
      this.aBoolean1541 = false;
      this.aBoolean1542 = false;
   }

}
