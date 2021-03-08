package org.runite.client;
import org.rs09.client.util.ArrayUtils;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;

final class Class30 {

   private long aLong563;
   private final byte[] aByteArray564;
   private int anInt566 = 0;
   private long aLong568;
   private long aLong569;
   private long aLong570;
   private long aLong571 = -1L;
   private final byte[] aByteArray572;
   private final RandomAccessFileWrapper aRandomAccessFileWrapper_573;
   static int[] anIntArray574 = new int[14];
   private int anInt575;
   private long aLong576 = -1L;
   static float aFloat578;
   static boolean loadedWorldList = false;


   private void method975(byte var1) throws IOException {
      try {
         if(-1L != this.aLong571) {
            if(this.aLong571 != this.aLong570) {
               this.aRandomAccessFileWrapper_573.seek(this.aLong571);
               this.aLong570 = this.aLong571;
            }

            this.aRandomAccessFileWrapper_573.write(this.aByteArray572, this.anInt566, 0);
            long var3 = -1L;
            if(this.aLong571 >= this.aLong576 && this.aLong571 < this.aLong576 + (long) this.anInt575) {
               var3 = this.aLong571;
            } else if(this.aLong571 <= this.aLong576 && this.aLong571 - -((long) this.anInt566) > this.aLong576) {
               var3 = this.aLong576;
            }

            this.aLong570 += (long)this.anInt566;
            if(this.aLong563 < this.aLong570) {
               this.aLong563 = this.aLong570;
            }

            long var5 = -1L;
            if(this.aLong576 < this.aLong571 - -((long)this.anInt566) && (long)this.anInt575 + this.aLong576 >= (long)this.anInt566 + this.aLong571) {
               var5 = this.aLong571 - -((long)this.anInt566);
            } else if(this.aLong571 < this.aLong576 - -((long) this.anInt575) && (long)this.anInt566 + this.aLong571 >= (long)this.anInt575 + this.aLong576) {
               var5 = (long)this.anInt575 + this.aLong576;
            }

            if(-1L < var3 && var3 < var5) {
               int var7 = (int)(-var3 + var5);
               ArrayUtils.arraycopy(this.aByteArray572, (int)(var3 - this.aLong571), this.aByteArray564, (int)(var3 + -this.aLong576), var7);
            }

            this.anInt566 = 0;
            this.aLong571 = -1L;
         }

      } catch (RuntimeException var8) {
         throw ClientErrorException.clientError(var8, "en.C(" + var1 + ')');
      }
   }

   final long method976(int var1) {
      try {
         if(var1 != 0) {
            this.method976(19);
         }

         return this.aLong568;
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "en.J(" + var1 + ')');
      }
   }

   private File method977() {
      try {

          return this.aRandomAccessFileWrapper_573.getPath();
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "en.D(" + 281669816 + ')');
      }
   }

   final void method978(int var1, byte[] var2, int var3) throws IOException {
      try {
         try {
            if(var1 - -var3 > var2.length) {
               throw new ArrayIndexOutOfBoundsException(var3 + var1 - var2.length);
            }

            if(-1L != this.aLong571 && this.aLong569 >= this.aLong571 && (long)this.anInt566 + this.aLong571 >= (long)var3 + this.aLong569) {
               ArrayUtils.arraycopy(this.aByteArray572, (int)(-this.aLong571 + this.aLong569), var2, var1, var3);
               this.aLong569 += (long)var3;
               return;
            }

            long var5 = this.aLong569;
            int var8 = var3;
            int var9;
            if(this.aLong576 <= this.aLong569 && this.aLong576 + (long)this.anInt575 > this.aLong569) {
               var9 = (int)((long)this.anInt575 - this.aLong569 + this.aLong576);
               if(var3 < var9) {
                  var9 = var3;
               }

               ArrayUtils.arraycopy(this.aByteArray564, (int)(this.aLong569 - this.aLong576), var2, var1, var9);
               var1 += var9;
               var3 -= var9;
               this.aLong569 += (long)var9;
            }

            if(var3 > this.aByteArray564.length) {
               this.aRandomAccessFileWrapper_573.seek(this.aLong569);

               for(this.aLong570 = this.aLong569; 0 < var3; this.aLong569 += (long)var9) {
                  var9 = this.aRandomAccessFileWrapper_573.read(var2, var1, var3, 0);
                  if(var9 == -1) {
                     break;
                  }

                  this.aLong570 += (long)var9;
                  var3 -= var9;
                  var1 += var9;
               }
            } else if(var3 > 0) {
               this.method981();
               var9 = var3;
               if(var3 > this.anInt575) {
                  var9 = this.anInt575;
               }

               ArrayUtils.arraycopy(this.aByteArray564, 0, var2, var1, var9);
               var3 -= var9;
               var1 += var9;
               this.aLong569 += (long)var9;
            }

            if(-1L != this.aLong571) {
               if(this.aLong569 < this.aLong571 && var3 > 0) {
                  var9 = (int)(-this.aLong569 + this.aLong571) + var1;
                  if(var9 > var1 - -var3) {
                     var9 = var1 + var3;
                  }

                  while(var1 < var9) {
                     var2[var1++] = 0;
                     ++this.aLong569;
                     --var3;
                  }
               }

               long var16 = -1L;
               long var11 = -1L;
               if((long)this.anInt566 + this.aLong571 > var5 && (long)var8 + var5 >= (long)this.anInt566 + this.aLong571) {
                  var11 = (long)this.anInt566 + this.aLong571;
               } else if((long) var8 + var5 > this.aLong571 && (long) var8 + var5 <= (long) this.anInt566 + this.aLong571) {
                  var11 = (long)var8 + var5;
               }

               if(this.aLong571 >= var5 && this.aLong571 < var5 - -((long) var8)) {
                  var16 = this.aLong571;
               } else if(this.aLong571 <= var5 && var5 < (long)this.anInt566 + this.aLong571) {
                  var16 = var5;
               }

               if(-1L < var16 && var11 > var16) {
                  int var13 = (int)(var11 + -var16);
                  ArrayUtils.arraycopy(this.aByteArray572, (int)(var16 + -this.aLong571), var2, (int)(var16 + -var5) + var1, var13);
                  if(this.aLong569 < var11) {
                     var3 = (int)((long)var3 - (-this.aLong569 + var11));
                     this.aLong569 = var11;
                  }
               }
            }
         } catch (IOException var14) {
            this.aLong570 = -1L;
            throw var14;
         }

         if(0 < var3) {
            throw new EOFException();
         }
      } catch (RuntimeException var15) {
         throw ClientErrorException.clientError(var15, "en.F(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ',' + 0 + ')');
      }
   }

   static void method979(int var0, int var1, int var2) {
      try {
         RSString var4 = RSString.stringCombiner(new RSString[]{TextCore.aClass94_853, RSString.stringAnimator(var2), TextCore.aClass94_3268, RSString.stringAnimator(var0 >> 6), TextCore.aClass94_3268, RSString.stringAnimator(var1 >> 6), TextCore.aClass94_3268, RSString.stringAnimator(var0 & 63), TextCore.aClass94_3268, RSString.stringAnimator(63 & var1)});

          ClientCommands.ClientCommands(var4);
      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "en.I(" + var0 + ',' + var1 + ',' + var2 + ',' + (byte) -4 + ')');
      }
   }

   final void method980() throws IOException {
      try {
         this.method975((byte)-75);
         this.aRandomAccessFileWrapper_573.close();

      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "en.K(" + false + ')');
      }
   }

   private void method981() throws IOException {
      try {
         this.anInt575 = 0;

          if(this.aLong570 != this.aLong569) {
            this.aRandomAccessFileWrapper_573.seek(this.aLong569);
            this.aLong570 = this.aLong569;
         }

         int var3;
         for(this.aLong576 = this.aLong569; this.aByteArray564.length > this.anInt575; this.anInt575 += var3) {
            int var2 = this.aByteArray564.length + -this.anInt575;
            if(var2 > 200000000) {
               var2 = 200000000;
            }

            var3 = this.aRandomAccessFileWrapper_573.read(this.aByteArray564, this.anInt575, var2, 0);
            if(var3 == -1) {
               break;
            }

            this.aLong570 += (long)var3;
         }

      } catch (RuntimeException var4) {
         throw ClientErrorException.clientError(var4, "en.A(" + 4393 + ')');
      }
   }

   final void method982(byte[] var2) throws IOException {
      try {

          this.method978(0, var2, var2.length);
      } catch (RuntimeException var4) {
         throw ClientErrorException.clientError(var4, "en.B(" + false + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

   final void method983(byte[] var1, int var2, int var3, int var4) throws IOException {
      try {
         try {
            if((long) var4 + this.aLong569 > this.aLong568) {
               this.aLong568 = (long)var4 + this.aLong569;
            }

            if(this.aLong571 != -1 && (this.aLong571 > this.aLong569 || (long) this.anInt566 + this.aLong571 < this.aLong569)) {
               this.method975((byte)124);
            }

            if(this.aLong571 != -1 && (long)this.aByteArray572.length + this.aLong571 < (long)var4 + this.aLong569) {
               int var5 = (int)((long)this.aByteArray572.length - this.aLong569 + this.aLong571);
               var4 -= var5;
               ArrayUtils.arraycopy(var1, var2, this.aByteArray572, (int)(this.aLong569 + -this.aLong571), var5);
               this.aLong569 += (long)var5;
               this.anInt566 = this.aByteArray572.length;
               this.method975((byte)93);
               var2 += var5;
            }

            if(var4 > this.aByteArray572.length) {
               if(this.aLong570 != this.aLong569) {
                  this.aRandomAccessFileWrapper_573.seek(this.aLong569);
                  this.aLong570 = this.aLong569;
               }

               this.aRandomAccessFileWrapper_573.write(var1, var4, var2);
               long var12 = -1L;
               if(this.aLong576 <= this.aLong569 && this.aLong569 < (long) this.anInt575 + this.aLong576) {
                  var12 = this.aLong569;
               } else if(this.aLong576 >= this.aLong569 && this.aLong576 < (long) var4 + this.aLong569) {
                  var12 = this.aLong576;
               }

               this.aLong570 += (long)var4;
               long var7 = -1L;
               if(this.aLong563 < this.aLong570) {
                  this.aLong563 = this.aLong570;
               }

               if(this.aLong569 + (long)var4 > this.aLong576 && this.aLong576 - -((long) this.anInt575) >= this.aLong569 - -((long) var4)) {
                  var7 = (long)var4 + this.aLong569;
               } else if(this.aLong569 < this.aLong576 + (long) this.anInt575 && this.aLong569 + (long)var4 >= (long)this.anInt575 + this.aLong576) {
                  var7 = (long)this.anInt575 + this.aLong576;
               }

               if(var12 > -1 && var7 > var12) {
                  int var9 = (int)(-var12 + var7);
                  ArrayUtils.arraycopy(var1, (int)(-this.aLong569 + var12 + (long)var2), this.aByteArray564, (int)(-this.aLong576 + var12), var9);
               }

               this.aLong569 += (long)var4;
               return;
            }

            if(0 < var4) {
               if(this.aLong571 == -1L) {
                  this.aLong571 = this.aLong569;
               }

               ArrayUtils.arraycopy(var1, var2, this.aByteArray572, (int)(this.aLong569 + -this.aLong571), var4);
               this.aLong569 += (long)var4;
               if(-this.aLong571 + this.aLong569 > (long) this.anInt566) {
                  this.anInt566 = (int)(-this.aLong571 + this.aLong569);
               }

               return;
            }
         } catch (IOException var10) {
            this.aLong570 = -1L;
            throw var10;
         }

         if(var3 != -903171152) {
            this.aLong563 = -28L;
         }

      } catch (RuntimeException var11) {
         throw ClientErrorException.clientError(var11, "en.H(" + (var1 != null?"{...}":"null") + ',' + var2 + ',' + var3 + ',' + var4 + ')');
      }
   }

   final void method984(int var1, long var2) throws IOException {
      try {
         if(var2 >= 0L) {
            this.aLong569 = var2;
            if(var1 > -6) {
               this.aLong569 = 89L;
            }

         } else {
            throw new IOException("Invalid seek to " + var2 + " in file " + this.method977());
         }
      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "en.E(" + var1 + ',' + var2 + ')');
      }
   }

   Class30(RandomAccessFileWrapper var1, int var2) throws IOException {
      try {
         this.aRandomAccessFileWrapper_573 = var1;
         this.aLong568 = this.aLong563 = var1.getLength();
         this.aByteArray572 = new byte[0];
         this.aByteArray564 = new byte[var2];
         this.aLong569 = 0L;
      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "en.<init>(" + (var1 != null?"{...}":"null") + ',' + var2 + ',' + 0 + ')');
      }
   }

}
