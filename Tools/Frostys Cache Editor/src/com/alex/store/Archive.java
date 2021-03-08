package com.alex.store;

import com.alex.io.InputStream;
import com.alex.io.OutputStream;
import com.alex.store.MainFile;
import com.alex.util.bzip2.BZip2Compressor;
import com.alex.util.bzip2.BZip2Decompressor;
import com.alex.util.crc32.CRC32HGenerator;
import com.alex.util.gzip.GZipCompressor;
import com.alex.util.gzip.GZipDecompressor;
import com.alex.util.whirlpool.Whirlpool;

public class Archive {
   private int id;
   private int revision;
   private int compression;
   private byte[] data;
   private int[] keys;

   protected Archive(int id, byte[] archive, int[] keys) {
      this.id = id;
      this.keys = keys;
      this.decompress(archive);
   }

   public Archive(int id, int compression, int revision, byte[] data) {
      this.id = id;
      this.compression = compression;
      this.revision = revision;
      this.data = data;
   }

   public byte[] compress() {
      OutputStream stream = new OutputStream();
      stream.writeByte(this.compression);
      byte[] compressedData1;
      switch(this.compression) {
      case 0:
         compressedData1 = this.data;
         stream.writeInt(this.data.length);
         break;
      case 1:
         Object compressed = null;
         compressedData1 = BZip2Compressor.compress(this.data);
         stream.writeInt(compressedData1.length);
         stream.writeInt(this.data.length);
      default:
         compressedData1 = GZipCompressor.compress(this.data);
         stream.writeInt(compressedData1.length);
         stream.writeInt(this.data.length);
      }

      stream.writeBytes(compressedData1);
      if(this.keys != null && this.keys.length == 4) {
         stream.encodeXTEA(this.keys, 5, stream.getOffset());
      }

      if(this.revision != -1) {
         stream.writeShort(this.revision);
      }

      byte[] compressed1 = new byte[stream.getOffset()];
      stream.setOffset(0);
      stream.getBytes(compressed1, 0, compressed1.length);
      return compressed1;
   }

   private void decompress(byte[] archive) {
      InputStream stream = new InputStream(archive);
      if(this.keys != null && this.keys.length == 4) {
         stream.decodeXTEA(this.keys);
      }

      this.compression = stream.readUnsignedByte();
      int compressedLength = stream.readInt();
      if(compressedLength >= 0 && compressedLength <= 1000000) {
         int length;
         switch(this.compression) {
         case 0:
            this.data = new byte[compressedLength];
            this.checkRevision(compressedLength, archive, stream.getOffset());
            stream.readBytes(this.data, 0, compressedLength);
            break;
         case 1:
            length = stream.readInt();
            if(length <= 0) {
               this.data = null;
            } else {
               this.data = new byte[length];
               this.checkRevision(compressedLength, archive, stream.getOffset());
               BZip2Decompressor.decompress(this.data, archive, compressedLength, 9);
            }
            break;
         default:
            length = stream.readInt();
            if(length > 0 && length <= 1000000000) {
               this.data = new byte[length];
               this.checkRevision(compressedLength, archive, stream.getOffset());
               if(!GZipDecompressor.decompress(stream, this.data)) {
                  this.data = null;
               }
            } else {
               this.data = null;
            }
         }

      } else {
         throw new RuntimeException("INVALID ARCHIVE HEADER");
      }
   }

   private void checkRevision(int compressedLength, byte[] archive, int o) {
      InputStream stream = new InputStream(archive);
      int offset = stream.getOffset();
      if(stream.getLength() - (compressedLength + o) >= 2) {
         stream.setOffset(stream.getLength() - 2);
         this.revision = stream.readUnsignedShort();
         stream.setOffset(offset);
      } else {
         this.revision = -1;
      }

   }

   public Object[] editNoRevision(byte[] data, MainFile mainFile) {
      this.data = data;
      if(this.compression == 1) {
         this.compression = 2;
      }

      byte[] compressed = this.compress();
      return !mainFile.putArchiveData(this.id, compressed)?null:new Object[]{Integer.valueOf(CRC32HGenerator.getHash(compressed)), Whirlpool.getHash(compressed, 0, compressed.length)};
   }

   public int getId() {
      return this.id;
   }

   public byte[] getData() {
      return this.data;
   }

   public int getDecompressedLength() {
      return this.data.length;
   }

   public int getRevision() {
      return this.revision;
   }

   public void setRevision(int revision) {
      this.revision = revision;
   }

   public int getCompression() {
      return this.compression;
   }

   public int[] getKeys() {
      return this.keys;
   }

   public void setKeys(int[] keys) {
      this.keys = keys;
   }
}
