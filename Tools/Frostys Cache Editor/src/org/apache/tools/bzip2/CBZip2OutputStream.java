package org.apache.tools.bzip2;

import org.apache.tools.bzip2.BZip2Constants;
import org.apache.tools.bzip2.CRC;

import java.io.IOException;
import java.io.OutputStream;

public class CBZip2OutputStream extends OutputStream implements BZip2Constants {
   protected static final int SETMASK = 2097152;
   protected static final int CLEARMASK = -2097153;
   protected static final int GREATER_ICOST = 15;
   protected static final int LESSER_ICOST = 0;
   protected static final int SMALL_THRESH = 20;
   protected static final int DEPTH_THRESH = 10;
   protected static final int QSORT_STACK_SIZE = 1000;
   int last;
   int origPtr;
   int blockSize100k;
   boolean blockRandomised;
   int bytesOut;
   int bsBuff;
   int bsLive;
   CRC mCrc;
   private boolean[] inUse;
   private int nInUse;
   private char[] seqToUnseq;
   private char[] unseqToSeq;
   private char[] selector;
   private char[] selectorMtf;
   private char[] block;
   private int[] quadrant;
   private int[] zptr;
   private short[] szptr;
   private int[] ftab;
   private int nMTF;
   private int[] mtfFreq;
   private int workFactor;
   private int workDone;
   private int workLimit;
   private boolean firstAttempt;
   private int nBlocksRandomised;
   private int currentChar;
   private int runLength;
   boolean closed;
   private int blockCRC;
   private int combinedCRC;
   private int allowableBlockSize;
   private OutputStream bsStream;
   private int[] incs;

   private static void panic() {
      System.out.println("panic");
   }

   private void makeMaps() {
      this.nInUse = 0;

      for(int i = 0; i < 256; ++i) {
         if(this.inUse[i]) {
            this.seqToUnseq[this.nInUse] = (char)i;
            this.unseqToSeq[i] = (char)this.nInUse;
            ++this.nInUse;
         }
      }

   }

   protected static void hbMakeCodeLengths(char[] len, int[] freq, int alphaSize, int maxLen) {
      int[] heap = new int[260];
      int[] weight = new int[516];
      int[] parent = new int[516];

      int i;
      for(i = 0; i < alphaSize; ++i) {
         weight[i + 1] = (freq[i] == 0?1:freq[i]) << 8;
      }

      while(true) {
         int nNodes = alphaSize;
         int nHeap = 0;
         heap[0] = 0;
         weight[0] = 0;
         parent[0] = -2;

         int zz;
         int tmp;
         for(i = 1; i <= alphaSize; ++i) {
            parent[i] = -1;
            ++nHeap;
            heap[nHeap] = i;
            zz = nHeap;

            for(tmp = heap[nHeap]; weight[tmp] < weight[heap[zz >> 1]]; zz >>= 1) {
               heap[zz] = heap[zz >> 1];
            }

            heap[zz] = tmp;
         }

         if(nHeap >= 260) {
            panic();
         }

         while(nHeap > 1) {
            int tooLong = heap[1];
            heap[1] = heap[nHeap];
            --nHeap;
            boolean j = false;
            boolean k = false;
            boolean tmp1 = false;
            zz = 1;
            int var20 = heap[zz];

            while(true) {
               tmp = zz << 1;
               if(tmp > nHeap) {
                  break;
               }

               if(tmp < nHeap && weight[heap[tmp + 1]] < weight[heap[tmp]]) {
                  ++tmp;
               }

               if(weight[var20] < weight[heap[tmp]]) {
                  break;
               }

               heap[zz] = heap[tmp];
               zz = tmp;
            }

            heap[zz] = var20;
            int n2 = heap[1];
            heap[1] = heap[nHeap];
            --nHeap;
            j = false;
            k = false;
            tmp1 = false;
            zz = 1;
            var20 = heap[zz];

            while(true) {
               tmp = zz << 1;
               if(tmp > nHeap) {
                  break;
               }

               if(tmp < nHeap && weight[heap[tmp + 1]] < weight[heap[tmp]]) {
                  ++tmp;
               }

               if(weight[var20] < weight[heap[tmp]]) {
                  break;
               }

               heap[zz] = heap[tmp];
               zz = tmp;
            }

            heap[zz] = var20;
            ++nNodes;
            parent[tooLong] = parent[n2] = nNodes;
            weight[nNodes] = (weight[tooLong] & -256) + (weight[n2] & -256) | 1 + ((weight[tooLong] & 255) > (weight[n2] & 255)?weight[tooLong] & 255:weight[n2] & 255);
            parent[nNodes] = -1;
            ++nHeap;
            heap[nHeap] = nNodes;
            j = false;
            k = false;
            zz = nHeap;

            for(tmp = heap[nHeap]; weight[tmp] < weight[heap[zz >> 1]]; zz >>= 1) {
               heap[zz] = heap[zz >> 1];
            }

            heap[zz] = tmp;
         }

         if(nNodes >= 516) {
            panic();
         }

         boolean var18 = false;

         int var19;
         for(i = 1; i <= alphaSize; ++i) {
            var19 = 0;

            for(int var201 = i; parent[var201] >= 0; ++var19) {
               var201 = parent[var201];
            }

            len[i - 1] = (char)var19;
            if(var19 > maxLen) {
               var18 = true;
            }
         }

         if(!var18) {
            return;
         }

         for(i = 1; i < alphaSize; ++i) {
            var19 = weight[i] >> 8;
            var19 = 1 + var19 / 2;
            weight[i] = var19 << 8;
         }
      }
   }

   public CBZip2OutputStream(OutputStream inStream) throws IOException {
      this(inStream, 9);
   }

   public CBZip2OutputStream(OutputStream inStream, int inBlockSize) throws IOException {
      this.mCrc = new CRC();
      this.inUse = new boolean[256];
      this.seqToUnseq = new char[256];
      this.unseqToSeq = new char[256];
      this.selector = new char[18002];
      this.selectorMtf = new char[18002];
      this.mtfFreq = new int[258];
      this.currentChar = -1;
      this.runLength = 0;
      this.closed = false;
      this.incs = new int[]{1, 4, 13, 40, 121, 364, 1093, 3280, 9841, 29524, 88573, 265720, 797161, 2391484};
      this.block = null;
      this.quadrant = null;
      this.zptr = null;
      this.ftab = null;
      this.bsSetStream(inStream);
      this.workFactor = 50;
      if(inBlockSize > 9) {
         inBlockSize = 9;
      }

      if(inBlockSize < 1) {
         inBlockSize = 1;
      }

      this.blockSize100k = inBlockSize;
      this.allocateCompressStructures();
      this.initialize();
      this.initBlock();
   }

   public void write(int bv) throws IOException {
      int b = (256 + bv) % 256;
      if(this.currentChar != -1) {
         if(this.currentChar == b) {
            ++this.runLength;
            if(this.runLength > 254) {
               this.writeRun();
               this.currentChar = -1;
               this.runLength = 0;
            }
         } else {
            this.writeRun();
            this.runLength = 1;
            this.currentChar = b;
         }
      } else {
         this.currentChar = b;
         ++this.runLength;
      }

   }

   private void writeRun() throws IOException {
      if(this.last < this.allowableBlockSize) {
         this.inUse[this.currentChar] = true;

         for(int i = 0; i < this.runLength; ++i) {
            this.mCrc.updateCRC((char)this.currentChar);
         }

         switch(this.runLength) {
         case 1:
            ++this.last;
            this.block[this.last + 1] = (char)this.currentChar;
            break;
         case 2:
            ++this.last;
            this.block[this.last + 1] = (char)this.currentChar;
            ++this.last;
            this.block[this.last + 1] = (char)this.currentChar;
            break;
         case 3:
            ++this.last;
            this.block[this.last + 1] = (char)this.currentChar;
            ++this.last;
            this.block[this.last + 1] = (char)this.currentChar;
            ++this.last;
            this.block[this.last + 1] = (char)this.currentChar;
            break;
         default:
            this.inUse[this.runLength - 4] = true;
            ++this.last;
            this.block[this.last + 1] = (char)this.currentChar;
            ++this.last;
            this.block[this.last + 1] = (char)this.currentChar;
            ++this.last;
            this.block[this.last + 1] = (char)this.currentChar;
            ++this.last;
            this.block[this.last + 1] = (char)this.currentChar;
            ++this.last;
            this.block[this.last + 1] = (char)(this.runLength - 4);
         }
      } else {
         this.endBlock();
         this.initBlock();
         this.writeRun();
      }

   }

   protected void finalize() throws Throwable {
      this.close();
      super.finalize();
   }

   public void close() throws IOException {
      if(!this.closed) {
         if(this.runLength > 0) {
            this.writeRun();
         }

         this.currentChar = -1;
         this.endBlock();
         this.endCompression();
         this.closed = true;
         super.close();
         this.bsStream.close();
      }

   }

   public void flush() throws IOException {
      super.flush();
      this.bsStream.flush();
   }

   private void initialize() throws IOException {
      this.bytesOut = 0;
      this.setnBlocksRandomised(0);
      this.bsPutUChar(104);
      this.bsPutUChar(48 + this.blockSize100k);
      this.combinedCRC = 0;
   }

   private void initBlock() {
      this.mCrc.initialiseCRC();
      this.last = -1;

      for(int i = 0; i < 256; ++i) {
         this.inUse[i] = false;
      }

      this.allowableBlockSize = 100000 * this.blockSize100k - 20;
   }

   private void endBlock() throws IOException {
      this.blockCRC = this.mCrc.getFinalCRC();
      this.combinedCRC = this.combinedCRC << 1 | this.combinedCRC >>> 31;
      this.combinedCRC ^= this.blockCRC;
      this.doReversibleTransformation();
      this.bsPutUChar(49);
      this.bsPutUChar(65);
      this.bsPutUChar(89);
      this.bsPutUChar(38);
      this.bsPutUChar(83);
      this.bsPutUChar(89);
      this.bsPutint(this.blockCRC);
      if(this.blockRandomised) {
         this.bsW(1, 1);
         this.setnBlocksRandomised(this.getnBlocksRandomised() + 1);
      } else {
         this.bsW(1, 0);
      }

      this.moveToFrontCodeAndSend();
   }

   private void endCompression() throws IOException {
      this.bsPutUChar(23);
      this.bsPutUChar(114);
      this.bsPutUChar(69);
      this.bsPutUChar(56);
      this.bsPutUChar(80);
      this.bsPutUChar(144);
      this.bsPutint(this.combinedCRC);
      this.bsFinishedWithStream();
   }

   private void hbAssignCodes(int[] code, char[] length, int minLen, int maxLen, int alphaSize) {
      int vec = 0;

      for(int n = minLen; n <= maxLen; ++n) {
         for(int i = 0; i < alphaSize; ++i) {
            if(length[i] == n) {
               code[i] = vec++;
            }
         }

         vec <<= 1;
      }

   }

   private void bsSetStream(OutputStream f) {
      this.bsStream = f;
      this.bsLive = 0;
      this.bsBuff = 0;
      this.bytesOut = 0;
   }

   private void bsFinishedWithStream() throws IOException {
      while(this.bsLive > 0) {
         int ch = this.bsBuff >> 24;

         try {
            this.bsStream.write(ch);
         } catch (IOException var3) {
            throw var3;
         }

         this.bsBuff <<= 8;
         this.bsLive -= 8;
         ++this.bytesOut;
      }

   }

   private void bsW(int n, int v) throws IOException {
      while(this.bsLive >= 8) {
         int ch = this.bsBuff >> 24;

         try {
            this.bsStream.write(ch);
         } catch (IOException var5) {
            throw var5;
         }

         this.bsBuff <<= 8;
         this.bsLive -= 8;
         ++this.bytesOut;
      }

      this.bsBuff |= v << 32 - this.bsLive - n;
      this.bsLive += n;
   }

   private void bsPutUChar(int c) throws IOException {
      this.bsW(8, c);
   }

   private void bsPutint(int u) throws IOException {
      this.bsW(8, u >> 24 & 255);
      this.bsW(8, u >> 16 & 255);
      this.bsW(8, u >> 8 & 255);
      this.bsW(8, u & 255);
   }

   private void bsPutIntVS(int numBits, int c) throws IOException {
      this.bsW(numBits, c);
   }

   private void sendMTFValues() throws IOException {
      char[][] len = new char[6][258];
      int nSelectors = 0;
      int alphaSize = this.nInUse + 2;

      int v;
      int t;
      for(t = 0; t < 6; ++t) {
         for(v = 0; v < alphaSize; ++v) {
            len[t][v] = 15;
         }
      }

      if(this.nMTF <= 0) {
         panic();
      }

      byte nGroups;
      if(this.nMTF < 200) {
         nGroups = 2;
      } else if(this.nMTF < 600) {
         nGroups = 3;
      } else if(this.nMTF < 1200) {
         nGroups = 4;
      } else if(this.nMTF < 2400) {
         nGroups = 5;
      } else {
         nGroups = 6;
      }

      int rfreq = nGroups;
      int fave = this.nMTF;

      int gs;
      int ge;
      int code;
      for(gs = 0; rfreq > 0; fave -= code) {
         int var29 = fave / rfreq;
         ge = gs - 1;

         for(code = 0; code < var29 && ge < alphaSize - 1; code += this.mtfFreq[ge]) {
            ++ge;
         }

         if(ge > gs && rfreq != nGroups && rfreq != 1 && (nGroups - rfreq) % 2 == 1) {
            code -= this.mtfFreq[ge];
            --ge;
         }

         for(v = 0; v < alphaSize; ++v) {
            if(v >= gs && v <= ge) {
               len[rfreq - 1][v] = 0;
            } else {
               len[rfreq - 1][v] = 15;
            }
         }

         --rfreq;
         gs = ge + 1;
      }

      int[][] var25 = new int[6][258];
      int[] var30 = new int[6];
      short[] var32 = new short[6];

      int i;
      int var291;
      for(int var31 = 0; var31 < 4; ++var31) {
         for(t = 0; t < nGroups; ++t) {
            var30[t] = 0;
         }

         for(t = 0; t < nGroups; ++t) {
            for(v = 0; v < alphaSize; ++v) {
               var25[t][v] = 0;
            }
         }

         nSelectors = 0;
         int var33 = 0;

         for(gs = 0; gs < this.nMTF; gs = ge + 1) {
            ge = gs + 50 - 1;
            if(ge >= this.nMTF) {
               ge = this.nMTF - 1;
            }

            for(t = 0; t < nGroups; ++t) {
               var32[t] = 0;
            }

            short var37;
            if(nGroups == 6) {
               short j = 0;
               short var39 = 0;
               short var36 = 0;
               short nBytes = 0;
               short selCtr = 0;
               var37 = 0;

               for(i = gs; i <= ge; ++i) {
                  short icv = this.szptr[i];
                  var37 = (short)(var37 + len[0][icv]);
                  selCtr = (short)(selCtr + len[1][icv]);
                  nBytes = (short)(nBytes + len[2][icv]);
                  var36 = (short)(var36 + len[3][icv]);
                  var39 = (short)(var39 + len[4][icv]);
                  j = (short)(j + len[5][icv]);
               }

               var32[0] = var37;
               var32[1] = selCtr;
               var32[2] = nBytes;
               var32[3] = var36;
               var32[4] = var39;
               var32[5] = j;
            } else {
               for(i = gs; i <= ge; ++i) {
                  var37 = this.szptr[i];

                  for(t = 0; t < nGroups; ++t) {
                     var32[t] = (short)(var32[t] + len[t][var37]);
                  }
               }
            }

            var291 = 999999999;
            int var301 = -1;

            for(t = 0; t < nGroups; ++t) {
               if(var32[t] < var291) {
                  var291 = var32[t];
                  var301 = t;
               }
            }

            var33 += var291;
            ++var30[var301];
            this.selector[nSelectors] = (char)var301;
            ++nSelectors;

            for(i = gs; i <= ge; ++i) {
               ++var25[var301][this.szptr[i]];
            }
         }

         for(t = 0; t < nGroups; ++t) {
            hbMakeCodeLengths(len[t], var25[t], alphaSize, 20);
         }
      }

      var25 = (int[][])null;
      Object var26 = null;
      Object var27 = null;
      if(nGroups >= 8) {
         panic();
      }

      if(nSelectors >= 32768 || nSelectors > 18002) {
         panic();
      }

      char[] var28 = new char[6];

      for(i = 0; i < nGroups; ++i) {
         var28[i] = (char)i;
      }

      char var331;
      char var34;
      for(i = 0; i < nSelectors; ++i) {
         char var311 = this.selector[i];
         var291 = 0;

         for(var34 = var28[var291]; var311 != var34; var28[var291] = var331) {
            ++var291;
            var331 = var34;
            var34 = var28[var291];
         }

         var28[0] = var34;
         this.selectorMtf[i] = (char)var291;
      }

      int[][] var321 = new int[6][258];

      for(t = 0; t < nGroups; ++t) {
         var331 = 32;
         var34 = 0;

         for(i = 0; i < alphaSize; ++i) {
            if(len[t][i] > var34) {
               var34 = len[t][i];
            }

            if(len[t][i] < var331) {
               var331 = len[t][i];
            }
         }

         if(var34 > 20) {
            panic();
         }

         if(var331 < 1) {
            panic();
         }

         this.hbAssignCodes(var321[t], len[t], var331, var34, alphaSize);
      }

      boolean[] var35 = new boolean[16];

      for(i = 0; i < 16; ++i) {
         var35[i] = false;

         for(var291 = 0; var291 < 16; ++var291) {
            if(this.inUse[i * 16 + var291]) {
               var35[i] = true;
            }
         }
      }

      int var371 = this.bytesOut;

      for(i = 0; i < 16; ++i) {
         if(var35[i]) {
            this.bsW(1, 1);
         } else {
            this.bsW(1, 0);
         }
      }

      for(i = 0; i < 16; ++i) {
         if(var35[i]) {
            for(var291 = 0; var291 < 16; ++var291) {
               if(this.inUse[i * 16 + var291]) {
                  this.bsW(1, 1);
               } else {
                  this.bsW(1, 0);
               }
            }
         }
      }

      var371 = this.bytesOut;
      this.bsW(3, nGroups);
      this.bsW(15, nSelectors);

      for(i = 0; i < nSelectors; ++i) {
         for(var291 = 0; var291 < this.selectorMtf[i]; ++var291) {
            this.bsW(1, 1);
         }

         this.bsW(1, 0);
      }

      var371 = this.bytesOut;

      int var361;
      for(t = 0; t < nGroups; ++t) {
         var361 = len[t][0];
         this.bsW(5, var361);

         for(i = 0; i < alphaSize; ++i) {
            while(var361 < len[t][i]) {
               this.bsW(2, 2);
               ++var361;
            }

            while(var361 > len[t][i]) {
               this.bsW(2, 3);
               --var361;
            }

            this.bsW(1, 0);
         }
      }

      var371 = this.bytesOut;
      var361 = 0;

      for(gs = 0; gs < this.nMTF; ++var361) {
         ge = gs + 50 - 1;
         if(ge >= this.nMTF) {
            ge = this.nMTF - 1;
         }

         for(i = gs; i <= ge; ++i) {
            this.bsW(len[this.selector[var361]][this.szptr[i]], var321[this.selector[var361]][this.szptr[i]]);
         }

         gs = ge + 1;
      }

      if(var361 != nSelectors) {
         panic();
      }

   }

   private void moveToFrontCodeAndSend() throws IOException {
      this.bsPutIntVS(24, this.origPtr);
      this.generateMTFValues();
      this.sendMTFValues();
   }

   private void simpleSort(int lo, int hi, int d) {
      int bigN = hi - lo + 1;
      if(bigN >= 2) {
         int hp;
         for(hp = 0; this.incs[hp] < bigN; ++hp) {
            ;
         }

         --hp;

         for(; hp >= 0; --hp) {
            int h = this.incs[hp];
            int i = lo + h;

            while(i <= hi) {
               int v = this.zptr[i];
               int j = i;

               while(this.fullGtU(this.zptr[j - h] + d, v + d)) {
                  this.zptr[j] = this.zptr[j - h];
                  j -= h;
                  if(j <= lo + h - 1) {
                     break;
                  }
               }

               this.zptr[j] = v;
               ++i;
               if(i > hi) {
                  break;
               }

               v = this.zptr[i];
               j = i;

               while(this.fullGtU(this.zptr[j - h] + d, v + d)) {
                  this.zptr[j] = this.zptr[j - h];
                  j -= h;
                  if(j <= lo + h - 1) {
                     break;
                  }
               }

               this.zptr[j] = v;
               ++i;
               if(i > hi) {
                  break;
               }

               v = this.zptr[i];
               j = i;

               while(this.fullGtU(this.zptr[j - h] + d, v + d)) {
                  this.zptr[j] = this.zptr[j - h];
                  j -= h;
                  if(j <= lo + h - 1) {
                     break;
                  }
               }

               this.zptr[j] = v;
               ++i;
               if(this.workDone > this.workLimit && this.firstAttempt) {
                  return;
               }
            }
         }
      }

   }

   private void vswap(int p1, int p2, int n) {
      for(boolean temp = false; n > 0; --n) {
         int var5 = this.zptr[p1];
         this.zptr[p1] = this.zptr[p2];
         this.zptr[p2] = var5;
         ++p1;
         ++p2;
      }

   }

   private char med3(char a, char b, char c) {
      if(a > b) {
         char t = a;
         a = b;
         b = t;
      }

      if(b > c) {
         b = c;
      }

      if(a > b) {
         b = a;
      }

      return b;
   }

   private void qSort3(int loSt, int hiSt, int dSt) {
      CBZip2OutputStream.StackElem[] stack = new CBZip2OutputStream.StackElem[1000];

      int temp;
      for(temp = 0; temp < 1000; ++temp) {
         stack[temp] = new CBZip2OutputStream.StackElem((CBZip2OutputStream.StackElem)null);
      }

      byte sp = 0;
      stack[sp].ll = loSt;
      stack[sp].hh = hiSt;
      stack[sp].dd = dSt;
      int var17 = sp + 1;

      while(true) {
         label55:
         while(var17 > 0) {
            if(var17 >= 1000) {
               panic();
            }

            --var17;
            int lo = stack[var17].ll;
            int hi = stack[var17].hh;
            int d = stack[var17].dd;
            if(hi - lo >= 20 && d <= 10) {
               char med = this.med3(this.block[this.zptr[lo] + d + 1], this.block[this.zptr[hi] + d + 1], this.block[this.zptr[lo + hi >> 1] + d + 1]);
               int ltLo = lo;
               int unLo = lo;
               int gtHi = hi;
               int unHi = hi;

               while(true) {
                  while(true) {
                     int n;
                     boolean var18;
                     if(unLo <= unHi) {
                        n = this.block[this.zptr[unLo] + d + 1] - med;
                        if(n == 0) {
                           var18 = false;
                           temp = this.zptr[unLo];
                           this.zptr[unLo] = this.zptr[ltLo];
                           this.zptr[ltLo] = temp;
                           ++ltLo;
                           ++unLo;
                           continue;
                        }

                        if(n <= 0) {
                           ++unLo;
                           continue;
                        }
                     }

                     while(unLo <= unHi) {
                        n = this.block[this.zptr[unHi] + d + 1] - med;
                        if(n == 0) {
                           var18 = false;
                           temp = this.zptr[unHi];
                           this.zptr[unHi] = this.zptr[gtHi];
                           this.zptr[gtHi] = temp;
                           --gtHi;
                           --unHi;
                        } else {
                           if(n < 0) {
                              break;
                           }

                           --unHi;
                        }
                     }

                     if(unLo > unHi) {
                        if(gtHi < ltLo) {
                           stack[var17].ll = lo;
                           stack[var17].hh = hi;
                           stack[var17].dd = d + 1;
                           ++var17;
                        } else {
                           n = ltLo - lo < unLo - ltLo?ltLo - lo:unLo - ltLo;
                           this.vswap(lo, unLo - n, n);
                           int m = hi - gtHi < gtHi - unHi?hi - gtHi:gtHi - unHi;
                           this.vswap(unLo, hi - m + 1, m);
                           n = lo + unLo - ltLo - 1;
                           m = hi - (gtHi - unHi) + 1;
                           stack[var17].ll = lo;
                           stack[var17].hh = n;
                           stack[var17].dd = d;
                           ++var17;
                           stack[var17].ll = n + 1;
                           stack[var17].hh = m - 1;
                           stack[var17].dd = d + 1;
                           ++var17;
                           stack[var17].ll = m;
                           stack[var17].hh = hi;
                           stack[var17].dd = d;
                           ++var17;
                        }
                        continue label55;
                     }

                     var18 = false;
                     temp = this.zptr[unLo];
                     this.zptr[unLo] = this.zptr[unHi];
                     this.zptr[unHi] = temp;
                     ++unLo;
                     --unHi;
                  }
               }
            } else {
               this.simpleSort(lo, hi, d);
               if(this.workDone > this.workLimit && this.firstAttempt) {
                  return;
               }
            }
         }

         return;
      }
   }

   private void mainSort() {
      int[] runningOrder = new int[256];
      int[] copy = new int[256];
      boolean[] bigDone = new boolean[256];

      int i;
      for(i = 0; i < 20; ++i) {
         this.block[this.last + i + 2] = this.block[i % (this.last + 1) + 1];
      }

      for(i = 0; i <= this.last + 20; ++i) {
         this.quadrant[i] = 0;
      }

      this.block[0] = this.block[this.last + 1];
      if(this.last < 4000) {
         for(i = 0; i <= this.last; this.zptr[i] = i++) {
            ;
         }

         this.firstAttempt = false;
         this.workDone = this.workLimit = 0;
         this.simpleSort(0, this.last, 0);
      } else {
         int numQSorted = 0;

         for(i = 0; i <= 255; ++i) {
            bigDone[i] = false;
         }

         for(i = 0; i <= 65536; ++i) {
            this.ftab[i] = 0;
         }

         char c1 = this.block[0];

         char c2;
         for(i = 0; i <= this.last; ++i) {
            c2 = this.block[i + 1];
            ++this.ftab[(c1 << 8) + c2];
            c1 = c2;
         }

         for(i = 1; i <= 65536; ++i) {
            this.ftab[i] += this.ftab[i - 1];
         }

         c1 = this.block[1];

         int j;
         for(i = 0; i < this.last; this.zptr[this.ftab[j]] = i++) {
            c2 = this.block[i + 2];
            j = (c1 << 8) + c2;
            c1 = c2;
            --this.ftab[j];
         }

         j = (this.block[this.last + 1] << 8) + this.block[1];
         --this.ftab[j];
         this.zptr[this.ftab[j]] = this.last;

         for(i = 0; i <= 255; runningOrder[i] = i++) {
            ;
         }

         int bbSize = 1;

         do {
            bbSize = 3 * bbSize + 1;
         } while(bbSize <= 256);

         int bbStart;
         do {
            bbSize /= 3;

            for(i = bbSize; i <= 255; ++i) {
               bbStart = runningOrder[i];
               j = i;

               while(this.ftab[runningOrder[j - bbSize] + 1 << 8] - this.ftab[runningOrder[j - bbSize] << 8] > this.ftab[bbStart + 1 << 8] - this.ftab[bbStart << 8]) {
                  runningOrder[j] = runningOrder[j - bbSize];
                  j -= bbSize;
                  if(j <= bbSize - 1) {
                     break;
                  }
               }

               runningOrder[j] = bbStart;
            }
         } while(bbSize != 1);

         for(i = 0; i <= 255; ++i) {
            int ss = runningOrder[i];

            int shifts;
            for(j = 0; j <= 255; ++j) {
               shifts = (ss << 8) + j;
               if((this.ftab[shifts] & 2097152) != 2097152) {
                  bbStart = this.ftab[shifts] & -2097153;
                  bbSize = (this.ftab[shifts + 1] & -2097153) - 1;
                  if(bbSize > bbStart) {
                     this.qSort3(bbStart, bbSize, 2);
                     numQSorted += bbSize - bbStart + 1;
                     if(this.workDone > this.workLimit && this.firstAttempt) {
                        return;
                     }
                  }

                  this.ftab[shifts] |= 2097152;
               }
            }

            bigDone[ss] = true;
            if(i < 255) {
               bbStart = this.ftab[ss << 8] & -2097153;
               bbSize = (this.ftab[ss + 1 << 8] & -2097153) - bbStart;

               for(shifts = 0; bbSize >> shifts > '\ufffe'; ++shifts) {
                  ;
               }

               for(j = 0; j < bbSize; ++j) {
                  int a2update = this.zptr[bbStart + j];
                  int qVal = j >> shifts;
                  this.quadrant[a2update] = qVal;
                  if(a2update < 20) {
                     this.quadrant[a2update + this.last + 1] = qVal;
                  }
               }

               if(bbSize - 1 >> shifts > '\uffff') {
                  panic();
               }
            }

            for(j = 0; j <= 255; ++j) {
               copy[j] = this.ftab[(j << 8) + ss] & -2097153;
            }

            for(j = this.ftab[ss << 8] & -2097153; j < (this.ftab[ss + 1 << 8] & -2097153); ++j) {
               c1 = this.block[this.zptr[j]];
               if(!bigDone[c1]) {
                  this.zptr[copy[c1]] = this.zptr[j] == 0?this.last:this.zptr[j] - 1;
                  ++copy[c1];
               }
            }

            for(j = 0; j <= 255; ++j) {
               this.ftab[(j << 8) + ss] |= 2097152;
            }
         }
      }

   }

   private void randomiseBlock() {
      int rNToGo = 0;
      int rTPos = 0;

      int i;
      for(i = 0; i < 256; ++i) {
         this.inUse[i] = false;
      }

      for(i = 0; i <= this.last; ++i) {
         if(rNToGo == 0) {
            rNToGo = (char)rNums[rTPos];
            ++rTPos;
            if(rTPos == 512) {
               rTPos = 0;
            }
         }

         --rNToGo;
         this.block[i + 1] = (char)(this.block[i + 1] ^ (rNToGo == 1?1:0));
         this.block[i + 1] = (char)(this.block[i + 1] & 255);
         this.inUse[this.block[i + 1]] = true;
      }

   }

   private void doReversibleTransformation() {
      this.workLimit = this.workFactor * this.last;
      this.workDone = 0;
      this.blockRandomised = false;
      this.firstAttempt = true;
      this.mainSort();
      if(this.workDone > this.workLimit && this.firstAttempt) {
         this.randomiseBlock();
         this.workLimit = this.workDone = 0;
         this.blockRandomised = true;
         this.firstAttempt = false;
         this.mainSort();
      }

      this.origPtr = -1;

      for(int i = 0; i <= this.last; ++i) {
         if(this.zptr[i] == 0) {
            this.origPtr = i;
            break;
         }
      }

      if(this.origPtr == -1) {
         panic();
      }

   }

   private boolean fullGtU(int i1, int i2) {
      char c1 = this.block[i1 + 1];
      char c2 = this.block[i2 + 1];
      if(c1 != c2) {
         return c1 > c2;
      } else {
         ++i1;
         ++i2;
         c1 = this.block[i1 + 1];
         c2 = this.block[i2 + 1];
         if(c1 != c2) {
            return c1 > c2;
         } else {
            ++i1;
            ++i2;
            c1 = this.block[i1 + 1];
            c2 = this.block[i2 + 1];
            if(c1 != c2) {
               return c1 > c2;
            } else {
               ++i1;
               ++i2;
               c1 = this.block[i1 + 1];
               c2 = this.block[i2 + 1];
               if(c1 != c2) {
                  return c1 > c2;
               } else {
                  ++i1;
                  ++i2;
                  c1 = this.block[i1 + 1];
                  c2 = this.block[i2 + 1];
                  if(c1 != c2) {
                     return c1 > c2;
                  } else {
                     ++i1;
                     ++i2;
                     c1 = this.block[i1 + 1];
                     c2 = this.block[i2 + 1];
                     if(c1 != c2) {
                        return c1 > c2;
                     } else {
                        ++i1;
                        ++i2;
                        int k = this.last + 1;

                        do {
                           c1 = this.block[i1 + 1];
                           c2 = this.block[i2 + 1];
                           if(c1 != c2) {
                              return c1 > c2;
                           }

                           int s1 = this.quadrant[i1];
                           int s2 = this.quadrant[i2];
                           if(s1 != s2) {
                              if(s1 > s2) {
                                 return true;
                              }

                              return false;
                           }

                           ++i1;
                           ++i2;
                           c1 = this.block[i1 + 1];
                           c2 = this.block[i2 + 1];
                           if(c1 != c2) {
                              if(c1 > c2) {
                                 return true;
                              }

                              return false;
                           }

                           s1 = this.quadrant[i1];
                           s2 = this.quadrant[i2];
                           if(s1 != s2) {
                              if(s1 > s2) {
                                 return true;
                              }

                              return false;
                           }

                           ++i1;
                           ++i2;
                           c1 = this.block[i1 + 1];
                           c2 = this.block[i2 + 1];
                           if(c1 != c2) {
                              if(c1 > c2) {
                                 return true;
                              }

                              return false;
                           }

                           s1 = this.quadrant[i1];
                           s2 = this.quadrant[i2];
                           if(s1 != s2) {
                              if(s1 > s2) {
                                 return true;
                              }

                              return false;
                           }

                           ++i1;
                           ++i2;
                           c1 = this.block[i1 + 1];
                           c2 = this.block[i2 + 1];
                           if(c1 != c2) {
                              if(c1 > c2) {
                                 return true;
                              }

                              return false;
                           }

                           s1 = this.quadrant[i1];
                           s2 = this.quadrant[i2];
                           if(s1 != s2) {
                              if(s1 > s2) {
                                 return true;
                              }

                              return false;
                           }

                           ++i1;
                           ++i2;
                           if(i1 > this.last) {
                              i1 -= this.last;
                              --i1;
                           }

                           if(i2 > this.last) {
                              i2 -= this.last;
                              --i2;
                           }

                           k -= 4;
                           ++this.workDone;
                        } while(k >= 0);

                        return false;
                     }
                  }
               }
            }
         }
      }
   }

   private void allocateCompressStructures() {
      int n = 100000 * this.blockSize100k;
      this.block = new char[n + 1 + 20];
      this.quadrant = new int[n + 20];
      this.zptr = new int[n];
      this.ftab = new int[65537];
      if(this.block != null && this.quadrant != null && this.zptr != null && this.ftab == null) {
         ;
      }

      this.szptr = new short[2 * n];
   }

   private void generateMTFValues() {
      char[] yy = new char[256];
      this.makeMaps();
      int EOB = this.nInUse + 1;

      int i;
      for(i = 0; i <= EOB; ++i) {
         this.mtfFreq[i] = 0;
      }

      int wr = 0;
      int zPend = 0;

      for(i = 0; i < this.nInUse; ++i) {
         yy[i] = (char)i;
      }

      for(i = 0; i <= this.last; ++i) {
         char ll_i = this.unseqToSeq[this.block[this.zptr[i]]];
         int j = 0;

         char tmp;
         char tmp2;
         for(tmp = yy[j]; ll_i != tmp; yy[j] = tmp2) {
            ++j;
            tmp2 = tmp;
            tmp = yy[j];
         }

         yy[0] = tmp;
         if(j == 0) {
            ++zPend;
         } else {
            if(zPend > 0) {
               --zPend;

               while(true) {
                  switch(zPend % 2) {
                  case 0:
                     this.szptr[wr] = 0;
                     ++wr;
                     ++this.mtfFreq[0];
                     break;
                  case 1:
                     this.szptr[wr] = 1;
                     ++wr;
                     ++this.mtfFreq[1];
                  }

                  if(zPend < 2) {
                     zPend = 0;
                     break;
                  }

                  zPend = (zPend - 2) / 2;
               }
            }

            this.szptr[wr] = (short)(j + 1);
            ++wr;
            ++this.mtfFreq[j + 1];
         }
      }

      if(zPend > 0) {
         --zPend;

         while(true) {
            switch(zPend % 2) {
            case 0:
               this.szptr[wr] = 0;
               ++wr;
               ++this.mtfFreq[0];
               break;
            case 1:
               this.szptr[wr] = 1;
               ++wr;
               ++this.mtfFreq[1];
            }

            if(zPend < 2) {
               break;
            }

            zPend = (zPend - 2) / 2;
         }
      }

      this.szptr[wr] = (short)EOB;
      ++wr;
      ++this.mtfFreq[EOB];
      this.nMTF = wr;
   }

   public int getnBlocksRandomised() {
      return this.nBlocksRandomised;
   }

   public void setnBlocksRandomised(int nBlocksRandomised) {
      this.nBlocksRandomised = nBlocksRandomised;
   }

   private static class StackElem {
      int ll;
      int hh;
      int dd;

      private StackElem() {
      }

      StackElem(CBZip2OutputStream.SyntheticClass_1 x0) {
         this();
      }

      // $FF: synthetic method
      StackElem(CBZip2OutputStream.StackElem var1) {
         this();
      }
   }

   static class SyntheticClass_1 {
   }
}
