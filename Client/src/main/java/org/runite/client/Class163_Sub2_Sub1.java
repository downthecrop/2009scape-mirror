package org.runite.client;
import org.rs09.client.net.Connection;
import org.rs09.client.data.ReferenceCache;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class Class163_Sub2_Sub1 extends Class163_Sub2 {

   static Connection aClass89_4012;
   static int anInt4014;
   static ReferenceCache aReferenceCache_4015 = new ReferenceCache(64);
   static RSString[] aClass94Array4016 = new RSString[500];
   static long[] aLongArray4017 = new long[100];
   static boolean paramObjectTagEnabled = false;
   static int anInt4019 = 0;
   static int anInt4020 = 0;
   static int anInt4021;
   static int[] anIntArray4025 = new int[32];
   static int anInt4026 = 0;
   static LDIndexedSprite[] aClass109_Sub1Array4027;


   static void method2220() {
      try {
          Class140_Sub4.aReferenceCache_2792.clear();
      } catch (RuntimeException var2) {
         throw ClientErrorException.clientError(var2, "t.H(" + 0 + ')');
      }
   }

   static void method2221(int var0, int var1, int var2, int var4, int var5, int var6) {
      try {
         int var8 = 0;

         for(Class96[] var7 = ClientErrorException.aClass96Array2114; var8 < var7.length; ++var8) {
            Class96 var9 = var7[var8];
            if(null != var9 && var9.anInt1360 == 2) {
               Unsorted.method1724(var0 >> 1, var5, (-Texture.anInt1152 + var9.anInt1347 << 7) - -var9.anInt1350, var9.anInt1353 * 2, var2 >> 1, var9.anInt1346 + (var9.anInt1356 + -Class131.anInt1716 << 7), (byte)-114, var4);
               if(-1 < Class32.anInt590 && Class44.anInt719 % 20 < 10) {
                  Class166.aAbstractSpriteArray2072[var9.anInt1351].drawAt(-12 + var1 + Class32.anInt590, -28 + var6 - -Texture.anInt2208);
               }
            }
         }

      } catch (RuntimeException var10) {
         throw ClientErrorException.clientError(var10, "t.E(" + var0 + ',' + var1 + ',' + var2 + ',' + true + ',' + var4 + ',' + var5 + ',' + var6 + ')');
      }
   }

   static void method2223(boolean var0, byte var1) {
      try {
         byte var3;
         byte[][] var4;
         if(HDToolKit.highDetail && var0) {
            var4 = Class40.aByteArrayArray3057;
            var3 = 1;
         } else {
            var3 = 4;
            var4 = Class3_Sub22.aByteArrayArray2521;
         }

         for(int var5 = 0; var5 < var3; ++var5) {
            Class58.method1194();

            for(int var6 = 0; var6 < 13; ++var6) {
               for(int var7 = 0; var7 < 13; ++var7) {
                  int var8 = ObjectDefinition.anIntArrayArrayArray1497[var5][var6][var7];
                  if(var8 != -1) {
                     int var9 = var8 >> 24 & 3;
                     if(!var0 || var9 == 0) {
                        int var10 = (6 & var8) >> 1;
                        int var11 = var8 >> 14 & 1023;
                        int var12 = 2047 & var8 >> 3;
                        int var13 = var12 / 8 + (var11 / 8 << 8);

                        for(int var14 = 0; Class3_Sub24_Sub3.anIntArray3494.length > var14; ++var14) {
                           if(var13 == Class3_Sub24_Sub3.anIntArray3494[var14] && var4[var14] != null) {
                              TextureOperation13.parseObjectMapping(AtmosphereParser.aClass91Array1182, var5, var4[var14], var9, var10, 8 * var6, var7 * 8, var0, (var11 & 7) * 8, 8 * (7 & var12));
                              break;
                           }
                        }
                     }
                  }
               }
            }
         }

      } catch (RuntimeException var15) {
         throw ClientErrorException.clientError(var15, "t.J(" + var0 + ',' + var1 + ')');
      }
   }

   static void method2224(long var1, int var3, int var4) {
      try {
         int var5 = (int)var1 >> 14 & 31;

         int var6 = (int)var1 >> 20 & 3;
         int var7 = (int)(var1 >>> 32) & Integer.MAX_VALUE;
         if (var5 == 10 || var5 == 11 || var5 == 22) {
            ObjectDefinition var8 = ObjectDefinition.getObjectDefinition(var7);
            int var9;
            int var10;
            if(var6 == 0 || var6 == 2) {
               var10 = var8.SizeY;
               var9 = var8.SizeX;
            } else {
               var10 = var8.SizeX;
               var9 = var8.SizeY;
            }

            int var11 = var8.WalkingFlag;
            if(var6 != 0) {
               var11 = (var11 << var6 & 15) - -(var11 >> -var6 + 4);
            }

            Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, var10, true, var11, 2, var4, var9, 0, 2, var3, Class102.player.anIntArray2767[0]);
         } else {
            Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], var6, 0, true, 0, 2, var4, 0, 1 + var5, 2, var3, Class102.player.anIntArray2767[0]);
         }

         Unsorted.anInt4062 = Class38_Sub1.anInt2614;
         Unsorted.anInt2958 = 0;
         Class36.anInt638 = 2;
         Class70.anInt1053 = Class163_Sub1.anInt2993;
      } catch (RuntimeException var12) {
         throw ClientErrorException.clientError(var12, "t.D(" + (byte) 39 + ',' + var1 + ',' + var3 + ',' + var4 + ')');
      }
   }

   static void method2226(BufferedDataStream var0, int var1, int var2) {
      try {
         if(var2 > -109) {
            method2226(null, 67, 54);
         }

         while(true) {
            Class3_Sub8 var3 = (Class3_Sub8)Class3_Sub26.aLinkedList_2557.method1222();
            if(var3 == null) {
               return;
            }

            boolean var4 = false;

            int var5;
            for(var5 = 0; var3.anInt2296 > var5; ++var5) {
               if(var3.aClass64Array2303[var5] != null) {
                  if(var3.aClass64Array2303[var5].anInt978 == 2) {
                     var3.anIntArray2300[var5] = -5;
                  }

                  if(var3.aClass64Array2303[var5].anInt978 == 0) {
                     var4 = true;
                  }
               }

               if(null != var3.aClass64Array2298[var5]) {
                  if(var3.aClass64Array2298[var5].anInt978 == 2) {
                     var3.anIntArray2300[var5] = -6;
                  }

                  if(var3.aClass64Array2298[var5].anInt978 == 0) {
                     var4 = true;
                  }
               }
            }

            if(var4) {
               return;
            }

            var0.putOpcode(var1);
            var0.writeByte(0);
            var5 = var0.index;
            var0.writeInt(var3.anInt2305);

            for(int var6 = 0; var6 < var3.anInt2296; ++var6) {
               if(var3.anIntArray2300[var6] == 0) {
                  try {
                     int var7 = var3.anIntArray2301[var6];
                     Field var8;
                     int var9;
                     if(var7 == 0) {
                        var8 = (Field)var3.aClass64Array2303[var6].anObject974;
                        var9 = var8.getInt(null);
                        var0.writeByte(0);
                        var0.writeInt(var9);
                     } else if (var7 == 1) {
                        var8 = (Field) var3.aClass64Array2303[var6].anObject974;
                        var8.setInt(null, var3.anIntArray2299[var6]);
                        var0.writeByte(0);
                     } else if (2 == var7) {
                        var8 = (Field) var3.aClass64Array2303[var6].anObject974;
                        var9 = var8.getModifiers();
                        var0.writeByte(0);
                        var0.writeInt(var9);
                     }

                     Method var26;
                     if(3 == var7) {
                        var26 = (Method)var3.aClass64Array2298[var6].anObject974;
                        byte[][] var27 = var3.aByteArrayArrayArray2302[var6];
                        Object[] var10 = new Object[var27.length];

                        for(int var11 = 0; var11 < var27.length; ++var11) {
                           ObjectInputStream var12 = new ObjectInputStream(new ByteArrayInputStream(var27[var11]));
                           var10[var11] = var12.readObject();
                        }

                        Object var28 = var26.invoke(null, var10);
                        if(var28 == null) {
                           var0.writeByte(0);
                        } else if(var28 instanceof Number) {
                           var0.writeByte(1);
                           var0.writeLong(((Number)var28).longValue());
                        } else if (var28 instanceof RSString) {
                           var0.writeByte(2);
                           var0.writeString((RSString) var28);
                        } else {
                           var0.writeByte(4);
                        }
                     } else if(var7 == 4) {
                        var26 = (Method)var3.aClass64Array2298[var6].anObject974;
                        var9 = var26.getModifiers();
                        var0.writeByte(0);
                        var0.writeInt(var9);
                     }
                  } catch (ClassNotFoundException var13) {
                     var0.writeByte(-10);
                  } catch (InvalidClassException var14) {
                     var0.writeByte(-11);
                  } catch (StreamCorruptedException var15) {
                     var0.writeByte(-12);
                  } catch (OptionalDataException var16) {
                     var0.writeByte(-13);
                  } catch (IllegalAccessException var17) {
                     var0.writeByte(-14);
                  } catch (IllegalArgumentException var18) {
                     var0.writeByte(-15);
                  } catch (InvocationTargetException var19) {
                     var0.writeByte(-16);
                  } catch (SecurityException var20) {
                     var0.writeByte(-17);
                  } catch (IOException var21) {
                     var0.writeByte(-18);
                  } catch (NullPointerException var22) {
                     var0.writeByte(-19);
                  } catch (Exception var23) {
                     var0.writeByte(-20);
                  } catch (Throwable var24) {
                     var0.writeByte(-21);
                  }
               } else {
                  var0.writeByte(var3.anIntArray2300[var6]);
               }
            }

            var0.writeCRC(var5);
            var0.method769(var0.index - var5);
            var3.unlink();
         }
      } catch (RuntimeException var25) {
         throw ClientErrorException.clientError(var25, "t.I(" + (var0 != null?"{...}":"null") + ',' + var1 + ',' + var2 + ')');
      }
   }

}
