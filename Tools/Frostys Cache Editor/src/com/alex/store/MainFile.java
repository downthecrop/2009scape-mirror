package com.alex.store;

import com.alex.store.Archive;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public final class MainFile {
   public static final int IDX_BLOCK_LEN = 6;
   public static final int HEADER_LEN = 8;
   public static final int EXPANDED_HEADER_LEN = 10;
   public static final int BLOCK_LEN = 512;
   public static final int EXPANDED_BLOCK_LEN = 510;
   public static final int TOTAL_BLOCK_LEN = 520;
   private static final ByteBuffer tempBuffer = ByteBuffer.allocateDirect(520);
   private int id;
   private FileChannel index;
   private FileChannel data;
   private boolean newProtocol;

   protected MainFile(int id, RandomAccessFile data, RandomAccessFile index, boolean newProtocol) throws IOException {
      this.id = id;
      this.data = data.getChannel();
      this.index = index.getChannel();
      this.newProtocol = newProtocol;
   }

   public Archive getArchive(int id) {
      return this.getArchive(id, (int[])null);
   }

   public Archive getArchive(int id, int[] keys) {
      byte[] data = this.getArchiveData(id);
      return data == null?null:new Archive(id, data, keys);
   }

   public byte[] getArchiveData(int archiveId) {
      FileChannel var2 = this.data;
      FileChannel var3 = this.data;
      synchronized(this.data) {
         try {
            tempBuffer.position(0).limit(6);
            this.index.read(tempBuffer, (long)(archiveId * 6));
            tempBuffer.flip();
            int var16 = getMediumInt(tempBuffer);
            int block = getMediumInt(tempBuffer);
            Object var10000;
            byte[] var100001;
            if(var16 < 0) {
               var10000 = null;
               var100001 = (byte[])var10000;
               return var100001;
            } else if(block <= 0 || (long)block > this.data.size() / 520L) {
               var10000 = null;
               var100001 = (byte[])var10000;
               return var100001;
            } else {
               ByteBuffer fileBuffer = ByteBuffer.allocate(var16);
               int remaining = var16;
               int chunk = 0;
               int blockLen = this.newProtocol && archiveId > '\uffff'?510:512;
               int headerLen = this.newProtocol && archiveId > '\uffff'?10:8;

               while(remaining > 0) {
                  if(block == 0) {
                     System.out.println(archiveId + ", " + this.newProtocol);
                     var10000 = null;
                     var100001 = (byte[])var10000;
                     return var100001;
                  }

                  int blockSize = remaining > blockLen?blockLen:remaining;
                  tempBuffer.position(0).limit(blockSize + headerLen);
                  this.data.read(tempBuffer, (long)(block * 520));
                  tempBuffer.flip();
                  int currentFile;
                  int currentChunk;
                  int nextBlock;
                  int currentIndex;
                  if(this.newProtocol && archiveId > '\uffff') {
                     currentFile = tempBuffer.getInt();
                     currentChunk = tempBuffer.getShort() & '\uffff';
                     nextBlock = getMediumInt(tempBuffer);
                     currentIndex = tempBuffer.get() & 255;
                  } else {
                     currentFile = tempBuffer.getShort() & '\uffff';
                     currentChunk = tempBuffer.getShort() & '\uffff';
                     nextBlock = getMediumInt(tempBuffer);
                     currentIndex = tempBuffer.get() & 255;
                  }

                  if((archiveId == currentFile || archiveId > '\uffff') && chunk == currentChunk && this.id == currentIndex) {
                     if(nextBlock >= 0 && (long)nextBlock <= this.data.size() / 520L) {
                        fileBuffer.put(tempBuffer);
                        remaining -= blockSize;
                        block = nextBlock;
                        ++chunk;
                        continue;
                     }

                     var10000 = null;
                     var100001 = (byte[])var10000;
                     return var100001;
                  }

                  var10000 = null;
                  var100001 = (byte[])var10000;
                  return var100001;
               }

               byte[] var18 = (byte[])fileBuffer.flip().array();
               return var18;
            }
         } catch (Exception var181) {
            return null;
         }
      }
   }

   private static int getMediumInt(ByteBuffer buffer) {
      return (buffer.get() & 255) << 16 | (buffer.get() & 255) << 8 | buffer.get() & 255;
   }

   private static void putMediumInt(ByteBuffer buffer, int val) {
      buffer.put((byte)(val >> 16));
      buffer.put((byte)(val >> 8));
      buffer.put((byte)val);
   }

   public boolean putArchive(Archive archive) {
      return this.putArchiveData(archive.getId(), archive.getData());
   }

   public boolean putArchiveData(int id, byte[] archive) {
      ByteBuffer buffer = ByteBuffer.wrap(archive);
      boolean done = this.putArchiveData(id, buffer, archive.length, true);
      if(!done) {
         done = this.putArchiveData(id, buffer, archive.length, false);
      }

      return done;
   }

   public boolean putArchiveData(int archiveId, ByteBuffer archive, int size, boolean exists) {
      FileChannel var5 = this.data;
      FileChannel var6 = this.data;
      synchronized(this.data) {
         try {
            boolean var10000;
            int var16;
            boolean var100001;
            if(exists) {
               if((long)(archiveId * 6 + 6) > this.index.size()) {
                  var10000 = false;
                  var100001 = var10000;
                  return var100001;
               }

               tempBuffer.position(0).limit(6);
               this.index.read(tempBuffer, (long)(archiveId * 6));
               tempBuffer.flip().position(3);
               var16 = getMediumInt(tempBuffer);
               if(var16 <= 0 || (long)var16 > this.data.size() / 520L) {
                  var10000 = false;
                  var100001 = var10000;
                  return var100001;
               }
            } else {
               var16 = (int)(this.data.size() + 520L - 1L) / 520;
               if(var16 == 0) {
                  var16 = 1;
               }
            }

            tempBuffer.position(0);
            putMediumInt(tempBuffer, size);
            putMediumInt(tempBuffer, var16);
            tempBuffer.flip();
            this.index.write(tempBuffer, (long)(archiveId * 6));
            int remaining = size;
            int chunk = 0;
            int blockLen = this.newProtocol && archiveId > '\uffff'?510:512;

            for(int headerLen = this.newProtocol && archiveId > '\uffff'?10:8; remaining > 0; ++chunk) {
               int nextBlock = 0;
               int blockSize;
               if(exists) {
                  tempBuffer.position(0).limit(headerLen);
                  this.data.read(tempBuffer, (long)(var16 * 520));
                  tempBuffer.flip();
                  int currentChunk;
                  int currentIndex;
                  if(this.newProtocol && archiveId > '\uffff') {
                     blockSize = tempBuffer.getInt();
                     currentChunk = tempBuffer.getShort() & '\uffff';
                     nextBlock = getMediumInt(tempBuffer);
                     currentIndex = tempBuffer.get() & 255;
                  } else {
                     blockSize = tempBuffer.getShort() & '\uffff';
                     currentChunk = tempBuffer.getShort() & '\uffff';
                     nextBlock = getMediumInt(tempBuffer);
                     currentIndex = tempBuffer.get() & 255;
                  }

                  if(archiveId != blockSize && archiveId <= '\uffff' || chunk != currentChunk || this.id != currentIndex) {
                     var10000 = false;
                     var100001 = var10000;
                     return var100001;
                  }

                  if(nextBlock < 0 || (long)nextBlock > this.data.size() / 520L) {
                     var10000 = false;
                     var100001 = var10000;
                     return var100001;
                  }
               }

               if(nextBlock == 0) {
                  exists = false;
                  nextBlock = (int)((this.data.size() + 520L - 1L) / 520L);
                  if(nextBlock == 0) {
                     nextBlock = 1;
                  }

                  if(nextBlock == var16) {
                     ++nextBlock;
                  }
               }

               if(remaining <= blockLen) {
                  nextBlock = 0;
               }

               tempBuffer.position(0).limit(520);
               if(this.newProtocol && archiveId > '\uffff') {
                  tempBuffer.putInt(archiveId);
                  tempBuffer.putShort((short)chunk);
                  putMediumInt(tempBuffer, nextBlock);
                  tempBuffer.put((byte)this.id);
               } else {
                  tempBuffer.putShort((short)archiveId);
                  tempBuffer.putShort((short)chunk);
                  putMediumInt(tempBuffer, nextBlock);
                  tempBuffer.put((byte)this.id);
               }

               blockSize = remaining > blockLen?blockLen:remaining;
               archive.limit(archive.position() + blockSize);
               tempBuffer.put(archive);
               tempBuffer.flip();
               this.data.write(tempBuffer, (long)(var16 * 520));
               remaining -= blockSize;
               var16 = nextBlock;
            }

            var10000 = true;
            return var10000;
         } catch (Exception var17) {
            return false;
         }
      }
   }

   public int getId() {
      return this.id;
   }

   public int getArchivesCount() throws IOException {
      FileChannel var1 = this.index;
      FileChannel var2 = this.index;
      synchronized(this.index) {
         return (int)(this.index.size() / 6L);
      }
   }
}
