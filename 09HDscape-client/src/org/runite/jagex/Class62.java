package org.runite.jagex;

final class Class62 {

   static int anInt942;
   int[][] fileNameHashes;
   static int anInt944 = 0;
   int[] archiveCRCs;
   static RSString aClass94_946 = RSString.createRSString(")2");
   int validArchiveAmount;
   private static RSString aClass94_948 = RSString.createRSString("You can(Wt add yourself to your own friend list)3");
   Class69 aClass69_949;
   static int anInt950;
   static RSString aClass94_951 = RSString.createRSString("Interfaces charg-Bes");
   static int anInt952;
   int[] validArchiveIds;
   int[] archiveLengths;
   int[] archiveNameHash;
   int[] archiveFileLengths;
   static RSString aClass94_957 = aClass94_948;
   int[] archiveRevisions;
   int[][] validFileIds;
   int archiveAmount;
   int revision;
   Class69[] aClass69Array962;
   static int anInt963;
   int anInt964;


   public static void method1223(int var0) {
      try {
         aClass94_951 = null;
         aClass94_946 = null;
         if(var0 == 0) {
            aClass94_948 = null;
            aClass94_957 = null;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "ii.A(" + var0 + ')');
      }
   }

   static final void method1224(RSInterface var0, int var1, int var2, int var3) {
      try {
         if(0 != var0.aByte162) {
            if(var0.aByte162 != 1) {
               if(2 == var0.aByte162) {
                  var0.anInt210 = var2 - var0.anInt193 - var0.y;
               } else if(var0.aByte162 != 3) {
                  if(4 == var0.aByte162) {
                     var0.anInt210 = (var2 * var0.y >> 14) + (-var0.anInt193 + var2) / 2;
                  } else {
                     var0.anInt210 = -(var2 * var0.y >> 14) + -var0.anInt193 + var2;
                  }
               } else {
                  var0.anInt210 = var0.y * var2 >> 14;
               }
            } else {
               var0.anInt210 = (var2 - var0.anInt193) / 2 + var0.y;
            }
         } else {
            var0.anInt210 = var0.y;
         }

         if(0 == var0.aByte273) {
            var0.anInt306 = var0.x;
         } else if(~var0.aByte273 != -2) {
            if(var0.aByte273 == 2) {
               var0.anInt306 = -var0.x + -var0.anInt168 + var3;
            } else if(3 != var0.aByte273) {
               if(4 != var0.aByte273) {
                  var0.anInt306 = -(var3 * var0.x >> 14) + var3 + -var0.anInt168;
               } else {
                  var0.anInt306 = (var0.x * var3 >> 14) + (var3 - var0.anInt168) / 2;
               }
            } else {
               var0.anInt306 = var0.x * var3 >> 14;
            }
         } else {
            var0.anInt306 = var0.x + (var3 - var0.anInt168) / 2;
         }

         if(Class69.aBoolean1040 && (Client.method44(var0).anInt2205 != 0 || ~var0.type == -1)) {
            if(~var0.anInt210 > -1) {
               var0.anInt210 = 0;
            } else if(var0.anInt193 + var0.anInt210 > var2) {
               var0.anInt210 = var2 + -var0.anInt193;
            }

            if(0 > var0.anInt306) {
               var0.anInt306 = 0;
            } else if(var3 < var0.anInt306 - -var0.anInt168) {
               var0.anInt306 = var3 + -var0.anInt168;
            }
         }

         if(var1 != 23730) {
            method1223(19);
         }

      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "ii.B(" + (var0 != null?"{...}":"null") + ',' + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   static final void method1225(int var0) {
      try {
         MouseListeningClass var1 = Class3_Sub28_Sub7_Sub1.aClass149_4047;
         synchronized(var1) {
            if(var0 != 18074) {
               aClass94_946 = (RSString)null;
            }

            Class3_Sub13_Sub5.anInt3069 = GraphicDefinition.anInt549;
            Class126.anInt1676 = Class3_Sub21.anInt2493;
            Class130.anInt1709 = Class95.anInt1340;
            Class3_Sub28_Sub11.anInt3644 = Class140_Sub3.anInt2743;
            Class163_Sub1.anInt2993 = RenderAnimationDefinition.anInt362;
            ++Class3_Sub28_Sub7_Sub1.anInt4045;
            Class38_Sub1.anInt2614 = Class3_Sub13_Sub32.anInt3389;
            Class75.aLong1102 = Class140_Sub6.aLong2926;
            Class140_Sub3.anInt2743 = 0;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ii.D(" + var0 + ')');
      }
   }

   private final void method1226(int var1, byte[] var2) {
      try {
         RSByteBuffer buffer = new RSByteBuffer(Class3_Sub28_Sub13.method623((byte)-114, var2));
         int var4 = buffer.getByte((byte)-34);
         if(var4 != 5 && ~var4 != -7) {
            throw new RuntimeException();
         } else {
            if(~var4 <= -7) {
               this.revision = buffer.getInt();
            } else {
               this.revision = 0;
            }

            int info = buffer.getByte((byte)-127);
            int var6 = 0;
            this.validArchiveAmount = buffer.getShort(var1 ^ 3);
            int var7 = -1;
            this.validArchiveIds = new int[this.validArchiveAmount];

            int var8;
            for(var8 = 0; this.validArchiveAmount > var8; ++var8) {
               this.validArchiveIds[var8] = var6 += buffer.getShort(1);
               if(this.validArchiveIds[var8] > var7) {
                  var7 = this.validArchiveIds[var8];
               }
            }

            this.archiveAmount = var7 - -1;
            this.archiveRevisions = new int[this.archiveAmount];
            this.validFileIds = new int[this.archiveAmount][];
            this.archiveCRCs = new int[this.archiveAmount];
            this.archiveLengths = new int[this.archiveAmount];
            this.archiveFileLengths = new int[this.archiveAmount];
            if(~info != -1) {
               this.archiveNameHash = new int[this.archiveAmount];

               for(var8 = 0; ~var8 > ~this.archiveAmount; ++var8) {
                  this.archiveNameHash[var8] = -1;
               }

               for(var8 = 0; ~var8 > ~this.validArchiveAmount; ++var8) {
                  this.archiveNameHash[this.validArchiveIds[var8]] = buffer.getInt();
               }

               this.aClass69_949 = new Class69(this.archiveNameHash);
            }

            for(var8 = 0; ~this.validArchiveAmount < ~var8; ++var8) {
               this.archiveCRCs[this.validArchiveIds[var8]] = buffer.getInt();
            }

            for(var8 = 0; this.validArchiveAmount > var8; ++var8) {
               this.archiveRevisions[this.validArchiveIds[var8]] = buffer.getInt();
            }

            var8 = 0;
            if(var1 != 2) {
               aClass94_957 = (RSString)null;
            }

            while(this.validArchiveAmount > var8) {
               this.archiveFileLengths[this.validArchiveIds[var8]] = buffer.getShort(1);
               ++var8;
            }

            int var9;
            int var10;
            int var11;
            int var12;
            for(var8 = 0; ~var8 > ~this.validArchiveAmount; ++var8) {
               var6 = 0;
               var9 = this.validArchiveIds[var8];
               var10 = this.archiveFileLengths[var9];
               var11 = -1;
               this.validFileIds[var9] = new int[var10];

               for(var12 = 0; var10 > var12; ++var12) {
                  int var13 = this.validFileIds[var9][var12] = var6 += buffer.getShort(1);
                  if(var13 > var11) {
                     var11 = var13;
                  }
               }

               this.archiveLengths[var9] = var11 + 1;
               if(~(1 + var11) == ~var10) {
                  this.validFileIds[var9] = null;
               }
            }

            if(-1 != ~info) {
               this.aClass69Array962 = new Class69[var7 - -1];
               this.fileNameHashes = new int[1 + var7][];

               for(var8 = 0; var8 < this.validArchiveAmount; ++var8) {
                  var9 = this.validArchiveIds[var8];
                  var10 = this.archiveFileLengths[var9];
                  this.fileNameHashes[var9] = new int[this.archiveLengths[var9]];

                  for(var11 = 0; var11 < this.archiveLengths[var9]; ++var11) {
                     this.fileNameHashes[var9][var11] = -1;
                  }

                  for(var11 = 0; ~var11 > ~var10; ++var11) {
                     if(null != this.validFileIds[var9]) {
                        var12 = this.validFileIds[var9][var11];
                     } else {
                        var12 = var11;
                     }

                     this.fileNameHashes[var9][var12] = buffer.getInt();
                  }

                  this.aClass69Array962[var9] = new Class69(this.fileNameHashes[var9]);
               }
            }

         }
      } catch (RuntimeException var14) {
         throw Class44.method1067(var14, "ii.C(" + var1 + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

   Class62(byte[] var1, int var2) {
      try {
         this.anInt964 = Class38.method1026(var1, var1.length, false);
         if(var2 == this.anInt964) {
            this.method1226(2, var1);
         } else {
            throw new RuntimeException();
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ii.<init>(" + (var1 != null?"{...}":"null") + ',' + var2 + ')');
      }
   }

}
