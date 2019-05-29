package org.runite.jagex;

final class CacheIndex {

   static int anInt1944;
   private boolean aBoolean1945;
   private boolean aBoolean1946;
   private Class151 aClass151_1947;
   static CacheIndex aClass153_1948;
   private Class62 aClass62_1949 = null;
   static int anInt1950;
   static boolean aBoolean1951 = false;
   private Object[][] anObjectArrayArray1952;
   static int anInt1953;
   private Object[] files;


   final boolean method2113(byte var1) {
      try {
         if(!this.method2122(3)) {
            return false;
         } else {
            if(var1 <= 15) {
               this.method2113((byte)39);
            }

            boolean var2 = true;

            for(int var3 = 0; ~var3 > ~this.aClass62_1949.validArchiveIds.length; ++var3) {
               int var4 = this.aClass62_1949.validArchiveIds[var3];
               if(null == this.files[var4]) {
                  this.method2134(false, var4);
                  if(null == this.files[var4]) {
                     var2 = false;
                  }
               }
            }

            return var2;
         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "ve.IA(" + var1 + ')');
      }
   }

   private final int method2114(int var1, int var2) {
      try {
         if(!this.isValidArchive(false, var1)) {
            return 0;
         } else {
            if(var2 != 0) {
               this.method2115(36, false, true);
            }

            return this.files[var1] != null?100:this.aClass151_1947.method2097(var1, '\uffff');
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ve.J(" + var1 + ',' + var2 + ')');
      }
   }

   final void method2115(int var1, boolean var2, boolean var3) {
      try {
         int var4 = 9 / ((var1 - 35) / 44);
         if(this.method2122(3)) {
            if(var2) {
               this.aClass62_1949.archiveNameHash = null;
               this.aClass62_1949.aClass69_949 = null;
            }

            if(var3) {
               this.aClass62_1949.aClass69Array962 = null;
               this.aClass62_1949.fileNameHashes = (int[][])null;
            }

         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "ve.R(" + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   final int method2116(int var1, RSString var2) {
      try {
         if(!this.method2122(var1 + -22810)) {
            return 0;
         } else {
            var2 = var2.method1534(-98);
            if(var1 != 22813) {
               return -12;
            } else {
               int var3 = this.aClass62_1949.aClass69_949.method1280(var2.method1574(false), 1);
               return this.method2114(var3, 0);
            }
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ve.P(" + var1 + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

   final boolean method2117(int var1, int var2) {
      try {
         if(var1 >= -88) {
            this.aBoolean1945 = true;
         }

         if(this.isValidArchive(false, var2)) {
            if(null == this.files[var2]) {
               this.method2134(false, var2);
               return null != this.files[var2];
            } else {
               return true;
            }
         } else {
            return false;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ve.GA(" + var1 + ',' + var2 + ')');
      }
   }

   final int getCRCValue(byte var1) {
      try {
         if(var1 >= -116) {
            this.method2115(-97, true, true);
         }

         if(this.method2122(3)) {
            return this.aClass62_1949.anInt964;
         } else {
            throw new IllegalStateException("");
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ve.T(" + var1 + ')');
      }
   }

   public static void method2119(int var0) {
      try {
         if(var0 != 100) {
            method2143((byte)-45, -91, 7, -83, 24);
         }

         aClass153_1948 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "ve.AA(" + var0 + ')');
      }
   }

   final int getArchiveForName(RSString name, byte var2) {
      try {
         if(this.method2122(3)) {
            name = name.method1534(var2 ^ 124);
            int var3 = this.aClass62_1949.aClass69_949.method1280(name.method1574(false), var2 ^ -29);
            return this.isValidArchive(false, var3)?(var2 != -30?87:var3):-1;
         } else {
            return -1;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ve.EA(" + (name != null?"{...}":"null") + ',' + var2 + ')');
      }
   }

   final int method2121(int var1) {
      try {
         if(!this.method2122(3)) {
            return -1;
         } else {
            if(var1 != 0) {
               this.method2139(45, 104, -3);
            }

            return this.aClass62_1949.archiveLengths.length;
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ve.D(" + var1 + ')');
      }
   }

   private final boolean method2122(int var1) {
      try {
         if(var1 != 3) {
            this.method2137((byte)-93);
         }

         if(this.aClass62_1949 == null) {
            this.aClass62_1949 = this.aClass151_1947.method2094(0);
            if(null == this.aClass62_1949) {
               return false;
            }

            this.anObjectArrayArray1952 = new Object[this.aClass62_1949.archiveAmount][];
            this.files = new Object[this.aClass62_1949.archiveAmount];
         }

         return true;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ve.W(" + var1 + ')');
      }
   }

   final byte[] method2123(int var1, RSString var2, RSString var3) {
      try {
         if(!this.method2122(var1 ^ 3)) {
            return null;
         } else {
            var3 = var3.method1534(-98);
            var2 = var2.method1534(-98);
            int var4 = this.aClass62_1949.aClass69_949.method1280(var3.method1574(false), 1);
            if(var1 != 0) {
               this.aClass62_1949 = (Class62)null;
            }

            if(this.isValidArchive(false, var4)) {
               int var5 = this.aClass62_1949.aClass69Array962[var4].method1280(var2.method1574(false), 1);
               return this.getFile(var4, (byte)-122, var5);
            } else {
               return null;
            }
         }
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "ve.C(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + (var3 != null?"{...}":"null") + ')');
      }
   }

   final void method2124(int var1, RSString var2) {
      try {
         if(this.method2122(3)) {
            var2 = var2.method1534(-98);
            int var3 = this.aClass62_1949.aClass69_949.method1280(var2.method1574(false), 1);
            this.method2131(119, var3);
            int var4 = 123 % ((var1 - -60) / 62);
         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "ve.V(" + var1 + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

   final boolean method2125(RSString var1, byte var2, RSString var3) {
      try {
         if(!this.method2122(var2 + -113)) {
            return false;
         } else {
            var3 = var3.method1534(var2 + -214);
            var1 = var1.method1534(-98);
            int var4 = this.aClass62_1949.aClass69_949.method1280(var3.method1574(false), 1);
            if(this.isValidArchive(false, var4)) {
               int var5 = this.aClass62_1949.aClass69Array962[var4].method1280(var1.method1574(false), 1);
               if(var2 != 116) {
                  this.aBoolean1945 = true;
               }

               return this.method2129((byte)70, var5, var4);
            } else {
               return false;
            }
         }
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "ve.DA(" + (var1 != null?"{...}":"null") + ',' + var2 + ',' + (var3 != null?"{...}":"null") + ')');
      }
   }

   final byte[] getFile(int archive, int[] xteaKeys, int var3, int file) {
      try {
         if(!this.method2139(archive, 0, file)) {
            return null;
         } else {
            if(this.anObjectArrayArray1952[archive] == null || this.anObjectArrayArray1952[archive][file] == null) {
               boolean var5 = this.method2132(archive, false, xteaKeys);
               if(!var5) {
                  this.method2134(false, archive);
                  var5 = this.method2132(archive, false, xteaKeys);
                  if(!var5) {
                     return null;
                  }
               }
            }

            if(var3 < 35) {
               return (byte[])null;
            } else {
               byte[] var7 = NPC.method1985(-119, this.anObjectArrayArray1952[archive][file], false);
               if(this.aBoolean1946) {
                  this.anObjectArrayArray1952[archive][file] = null;
                  if(this.aClass62_1949.archiveLengths[archive] == 1) {
                     this.anObjectArrayArray1952[archive] = null;
                  }
               }

               return var7;
            }
         }
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "ve.BA(" + archive + ',' + (xteaKeys != null?"{...}":"null") + ',' + var3 + ',' + file + ')');
      }
   }

   final boolean method2127(byte var1, RSString var2) {
      try {
         if(this.method2122(3)) {
            var2 = var2.method1534(-98);
            int var3 = this.aClass62_1949.aClass69_949.method1280(var2.method1574(false), var1 + 84);
            return var1 != -83?true:this.method2117(-104, var3);
         } else {
            return false;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ve.O(" + var1 + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

   final void method2128(int var1, int var2) {
      try {
         if(var1 == 7561) {
            if(this.isValidArchive(false, var2)) {
               if(null != this.anObjectArrayArray1952) {
                  this.anObjectArrayArray1952[var2] = null;
               }

            }
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ve.B(" + var1 + ',' + var2 + ')');
      }
   }

   final boolean method2129(byte var1, int var2, int var3) {
      try {
         int var4 = 78 / ((-10 - var1) / 59);
         if(this.method2139(var3, 0, var2)) {
            if(this.anObjectArrayArray1952[var3] != null && null != this.anObjectArrayArray1952[var3][var2]) {
               return true;
            } else if(this.files[var3] == null) {
               this.method2134(false, var3);
               return this.files[var3] != null;
            } else {
               return true;
            }
         } else {
            return false;
         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "ve.FA(" + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   private final boolean isValidArchive(boolean var1, int archiveId) {
      try {
         if(var1) {
            aBoolean1951 = false;
         }

         if(this.method2122(3)) {
            if(archiveId >= 0 && this.aClass62_1949.archiveLengths.length > archiveId && ~this.aClass62_1949.archiveLengths[archiveId] != -1) {
               return true;
            } else if(!Class134.aBoolean1765) {
               return false;
            } else {
               throw new IllegalArgumentException(Integer.toString(archiveId));
            }
         } else {
            return false;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ve.E(" + var1 + ',' + archiveId + ')');
      }
   }

   private final void method2131(int var1, int var2) {
      try {
         this.aClass151_1947.method2095(var2, 127);
         if(var1 < 101) {
            this.method2132(53, false, (int[])null);
         }

      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ve.L(" + var1 + ',' + var2 + ')');
      }
   }

   private final boolean method2132(int archive, boolean var2, int[] xteaKeys) {
      try {
         if(!this.isValidArchive(var2, archive)) {
            return false;
         } else if(this.files[archive] == null) {
            return false;
         } else {
            int[] var5 = this.aClass62_1949.validFileIds[archive];
            int var4 = this.aClass62_1949.archiveFileLengths[archive];
            if(this.anObjectArrayArray1952[archive] == null) {
               this.anObjectArrayArray1952[archive] = new Object[this.aClass62_1949.archiveLengths[archive]];
            }

            boolean var7 = true;
            Object[] var6 = this.anObjectArrayArray1952[archive];

            for(int var8 = 0; var8 < var4; ++var8) {
               int var9;
               if(var5 == null) {
                  var9 = var8;
               } else {
                  var9 = var5[var8];
               }

               if(var6[var9] == null) {
                  var7 = false;
                  break;
               }
            }

            if(var7) {
               return true;
            } else {
               byte[] var21;
               if(null != xteaKeys && (xteaKeys[0] != 0 || 0 != xteaKeys[1] || 0 != xteaKeys[2] || xteaKeys[3] != 0)) {
                  var21 = NPC.method1985(-124, this.files[archive], true);
                  RSByteBuffer var22 = new RSByteBuffer(var21);
                  var22.method770(xteaKeys, 120, 5, var22.buffer.length);
               } else {
                  var21 = NPC.method1985(-128, this.files[archive], false);
               }

               byte[] var23;
               try {
                  var23 = Class3_Sub28_Sub13.method623((byte)-125, var21);
               } catch (Throwable var19) {
                  throw Class44.method1067(var19, "T3 - " + (xteaKeys != null) + "," + archive + "," + var21.length + "," + Class38.method1026(var21, var21.length, false) + "," + Class38.method1026(var21, var21.length - 2, false) + "," + this.aClass62_1949.archiveCRCs[archive] + "," + this.aClass62_1949.anInt964);
               }

               if(this.aBoolean1945) {
                  this.files[archive] = null;
               }

               int var10;
               if(var4 > 1) {
                  var10 = var23.length;
                  --var10;
                  int var11 = 255 & var23[var10];
                  var10 -= var4 * var11 * 4;
                  RSByteBuffer var12 = new RSByteBuffer(var23);
                  var12.index = var10;
                  int[] var13 = new int[var4];

                  int var15;
                  int var16;
                  for(int var14 = 0; ~var14 > ~var11; ++var14) {
                     var15 = 0;

                     for(var16 = 0; ~var4 < ~var16; ++var16) {
                        var15 += var12.getInt();
                        if(null == var5) {
                           ;
                        }

                        var13[var16] += var15;
                     }
                  }

                  byte[][] var24 = new byte[var4][];

                  for(var15 = 0; ~var15 > ~var4; ++var15) {
                     var24[var15] = new byte[var13[var15]];
                     var13[var15] = 0;
                  }

                  var12.index = var10;
                  var15 = 0;

                  int var17;
                  for(var16 = 0; ~var16 > ~var11; ++var16) {
                     var17 = 0;

                     for(int var18 = 0; ~var4 < ~var18; ++var18) {
                        var17 += var12.getInt();
                        Class76.method1357(var23, var15, var24[var18], var13[var18], var17);
                        var15 += var17;
                        var13[var18] += var17;
                     }
                  }

                  for(var16 = 0; ~var4 < ~var16; ++var16) {
                     if(var5 != null) {
                        var17 = var5[var16];
                     } else {
                        var17 = var16;
                     }

                     if(this.aBoolean1946) {
                        var6[var17] = var24[var16];
                     } else {
                        var6[var17] = Class15.method890(false, -114, var24[var16]);
                     }
                  }
               } else {
                  if(null != var5) {
                     var10 = var5[0];
                  } else {
                     var10 = 0;
                  }

                  if(!this.aBoolean1946) {
                     var6[var10] = Class15.method890(false, -80, var23);
                  } else {
                     var6[var10] = var23;
                  }
               }

               return true;
            }
         }
      } catch (RuntimeException var20) {
         throw Class44.method1067(var20, "ve.I(" + archive + ',' + var2 + ',' + (xteaKeys != null?"{...}":"null") + ')');
      }
   }

   final byte[] getFile(int archive, byte var2, int file) {
      try {
         if(var2 != -122) {
            this.method2134(false, 93);
         }

         return this.getFile(archive, (int[])null, 52, file);
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "ve.M(" + archive + ',' + var2 + ',' + file + ')');
      }
   }

   private final void method2134(boolean var1, int var2) {
      try {
         if(!this.aBoolean1945) {
            this.files[var2] = Class15.method890(false, -101, this.aClass151_1947.method2098(var2, 0));
         } else {
            this.files[var2] = this.aClass151_1947.method2098(var2, 0);
         }

         if(var1) {
            this.aBoolean1945 = false;
         }

      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ve.F(" + var1 + ',' + var2 + ')');
      }
   }

   final boolean method2135(RSString var1, int var2) {
      try {
         if(var2 >= -103) {
            return false;
         } else if(this.method2122(3)) {
            var1 = var1.method1534(-98);
            int var3 = this.aClass62_1949.aClass69_949.method1280(var1.method1574(false), 1);
            return ~var3 <= -1;
         } else {
            return false;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ve.S(" + (var1 != null?"{...}":"null") + ',' + var2 + ')');
      }
   }

   final int method2136(byte var1) {
      try {
         if(var1 > -121) {
            anInt1950 = -3;
         }

         if(!this.method2122(3)) {
            return 0;
         } else {
            int var2 = 0;
            int var3 = 0;

            int var4;
            for(var4 = 0; var4 < this.files.length; ++var4) {
               if(0 < this.aClass62_1949.archiveFileLengths[var4]) {
                  var2 += 100;
                  var3 += this.method2114(var4, 0);
               }
            }

            if(var2 != 0) {
               var4 = var3 * 100 / var2;
               return var4;
            } else {
               return 100;
            }
         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "ve.N(" + var1 + ')');
      }
   }

   final void method2137(byte var1) {
      try {
         if(var1 != 56) {
            this.anObjectArrayArray1952 = (Object[][])((Object[][])null);
         }

         if(this.anObjectArrayArray1952 != null) {
            for(int var2 = 0; this.anObjectArrayArray1952.length > var2; ++var2) {
               this.anObjectArrayArray1952[var2] = null;
            }
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ve.Q(" + var1 + ')');
      }
   }

   final byte[] method2138(int var1, int var2) {
      try {
         if(!this.method2122(var2 ^ 3)) {
            return null;
         } else if(~this.aClass62_1949.archiveLengths.length == -2) {
            return this.getFile(0, (byte)-122, var1);
         } else if(!this.isValidArchive(false, var1)) {
            return null;
         } else if(~this.aClass62_1949.archiveLengths[var1] != -2) {
            if(var2 != 0) {
               return (byte[])null;
            } else {
               throw new RuntimeException();
            }
         } else {
            return this.getFile(var1, (byte)-122, 0);
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ve.HA(" + var1 + ',' + var2 + ')');
      }
   }

   private final boolean method2139(int archive, int var2, int file) {
      try {
         if(this.method2122(3)) {
            if(var2 <= archive && -1 >= ~file && ~this.aClass62_1949.archiveLengths.length < ~archive && this.aClass62_1949.archiveLengths[archive] > file) {
               return true;
            } else if(!Class134.aBoolean1765) {
               return false;
            } else {
               throw new IllegalArgumentException(archive + "," + file);
            }
         } else {
            return false;
         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "ve.K(" + archive + ',' + var2 + ',' + file + ')');
      }
   }

   final byte[] method2140(int file, int archive, int var3) {
      try {
         if(!this.method2139(archive, var3 + var3, file)) {
            return null;
         } else {
            if(this.anObjectArrayArray1952[archive] == null || null == this.anObjectArrayArray1952[archive][file]) {
               boolean var4 = this.method2132(archive, false, (int[])null);
               if(!var4) {
                  this.method2134(false, archive);
                  var4 = this.method2132(archive, false, (int[])null);
                  if(!var4) {
                     return null;
                  }
               }
            }

            byte[] var6 = NPC.method1985(-126, this.anObjectArrayArray1952[archive][file], false);
            return var6;
         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "ve.CA(" + file + ',' + archive + ',' + var3 + ')');
      }
   }

   final int[] getFileIds(byte var1, int archiveId) {
      try {
         if(var1 != -128) {
            anInt1953 = -69;
         }

         if(!this.isValidArchive(false, archiveId)) {
            return null;
         } else {
            int[] var3 = this.aClass62_1949.validFileIds[archiveId];
            if(null == var3) {
               var3 = new int[this.aClass62_1949.archiveFileLengths[archiveId]];

               for(int var4 = 0; var3.length > var4; var3[var4] = var4++) {
                  ;
               }
            }

            return var3;
         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "ve.G(" + var1 + ',' + archiveId + ')');
      }
   }

   CacheIndex(Class151 var1, boolean var2, boolean var3) {
      try {
         this.aClass151_1947 = var1;
         this.aBoolean1945 = var2;
         this.aBoolean1946 = var3;
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "ve.<init>(" + (var1 != null?"{...}":"null") + ',' + var2 + ',' + var3 + ')');
      }
   }

   final int getFileAmount(int archiveId, byte var2) {
      try {
         if(!this.isValidArchive(false, archiveId)) {
            return 0;
         } else {
            if(var2 <= 60) {
               this.method2122(32);
            }

            return this.aClass62_1949.archiveLengths[archiveId];
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ve.H(" + archiveId + ',' + var2 + ')');
      }
   }

   static final void method2143(byte var0, int var1, int var2, int var3, int var4) {
      try {
         Class3_Sub28_Sub6 var5 = Class3_Sub24_Sub3.method466(4, 8, var2);
         var5.g((byte)33);
         var5.anInt3596 = var1;
         if(var0 >= -120) {
            anInt1950 = -14;
         }

         var5.anInt3598 = var4;
         var5.anInt3597 = var3;
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "ve.U(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ')');
      }
   }

   final boolean method2144(int var1, int archiveId) {
      try {
         if(!this.method2122(3)) {
            return false;
         } else if(~this.aClass62_1949.archiveLengths.length == -2) {
            return this.method2129((byte)86, archiveId, 0);
         } else if(this.isValidArchive(false, archiveId)) {
            if(1 == this.aClass62_1949.archiveLengths[archiveId]) {
               return this.method2129((byte)109, 0, archiveId);
            } else if(var1 != 0) {
               return false;
            } else {
               throw new RuntimeException();
            }
         } else {
            return false;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ve.A(" + var1 + ',' + archiveId + ')');
      }
   }

}
