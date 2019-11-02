package org.runite.jagex;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public final class Class122 {

   private long aLong1649;
   private File aFile1650;
   private RandomAccessFile aRandomAccessFile1651;
   private long aLong1652;


   public final void method1737(byte var1, long var2) throws IOException {
      this.aRandomAccessFile1651.seek(var2);
      this.aLong1652 = var2;
      if(var1 != -10) {
         this.method1742(49);
      }

   }

   protected void finalize() throws Throwable {
      if(this.aRandomAccessFile1651 != null) {
         System.out.println("Warning! fileondisk " + this.aFile1650 + " not closed correctly using close(). Auto-closing instead. ");
         this.close(1);
      }

   }

   public final void method1738(int var1, byte[] var2, int var3, int var4) throws IOException {
      if(~(this.aLong1652 + (long)var3) < ~this.aLong1649) {
         this.aRandomAccessFile1651.seek(1L + this.aLong1649);
         this.aRandomAccessFile1651.write(1);
         throw new EOFException();
      } else {
         this.aRandomAccessFile1651.write(var2, var4, var3);
         this.aLong1652 += (long)var3;
         if(var1 < 105) {
            this.aLong1649 = -26L;
         }

      }
   }

   public final int method1739(int var1, int var2, int var3, byte[] var4) throws IOException {
      int var5 = this.aRandomAccessFile1651.read(var4, var1, var3);
      if(var5 > var2) {
         this.aLong1652 += (long)var5;
      }

      return var5;
   }

   public final void close(int var1) throws IOException {
      if(var1 != 1) {
         this.aFile1650 = null;
      }

      if(this.aRandomAccessFile1651 != null) {
         this.aRandomAccessFile1651.close();
         this.aRandomAccessFile1651 = null;
      }

   }

   public final long method1741(int var1) throws IOException {
      return var1 != -1?36L:this.aRandomAccessFile1651.length();
   }

   public final File method1742(int var1) {
      return var1 > -8?null:this.aFile1650;
   }

   public Class122(File var1, String var2, long var3) throws IOException {
      if(0L == ~var3) {
         var3 = Long.MAX_VALUE;
      }

      if(~var3 >= ~var1.length()) {
         var1.delete();
      }

      this.aRandomAccessFile1651 = new RandomAccessFile(var1, var2);
      this.aFile1650 = var1;
      this.aLong1649 = var3;
      this.aLong1652 = 0L;
      int var5 = this.aRandomAccessFile1651.read();
      if(~var5 != 0 && !var2.equals("r")) {
         this.aRandomAccessFile1651.seek(0L);
         this.aRandomAccessFile1651.write(var5);
      }

      this.aRandomAccessFile1651.seek(0L);
   }
}
