package com.alex.store;

import com.alex.io.InputStream;
import com.alex.io.OutputStream;
import com.alex.store.Archive;
import com.alex.store.ArchiveReference;
import com.alex.store.FileReference;
import com.alex.store.MainFile;

import java.util.Arrays;

public final class ReferenceTable {
   private Archive archive;
   private int revision;
   private boolean named;
   private boolean usesWhirpool;
   private ArchiveReference[] archives;
   private int[] validArchiveIds;
   private boolean updatedRevision;
   private boolean needsArchivesSort;

   protected ReferenceTable(Archive archive) {
      this.archive = archive;
      this.decodeHeader();
   }

   public void setKeys(int[] keys) {
      this.archive.setKeys(keys);
   }

   public int[] getKeys() {
      return this.archive.getKeys();
   }

   public void sortArchives() {
      Arrays.sort(this.validArchiveIds);
      this.needsArchivesSort = false;
   }

   public void addEmptyArchiveReference(int archiveId) {
      this.needsArchivesSort = true;
      int[] newValidArchiveIds = Arrays.copyOf(this.validArchiveIds, this.validArchiveIds.length + 1);
      newValidArchiveIds[newValidArchiveIds.length - 1] = archiveId;
      this.validArchiveIds = newValidArchiveIds;
      ArchiveReference reference;
      if(this.archives.length <= archiveId) {
         ArchiveReference[] newArchives = (ArchiveReference[])Arrays.copyOf(this.archives, archiveId + 1);
         reference = newArchives[archiveId] = new ArchiveReference();
         this.archives = newArchives;
      } else {
         reference = this.archives[archiveId] = new ArchiveReference();
      }

      reference.reset();
   }

   public void sortTable() {
      if(this.needsArchivesSort) {
         this.sortArchives();
      }

      for(int index = 0; index < this.validArchiveIds.length; ++index) {
         ArchiveReference archive = this.archives[this.validArchiveIds[index]];
         if(archive.isNeedsFilesSort()) {
            archive.sortFiles();
         }
      }

   }

   public Object[] encodeHeader(MainFile mainFile) {
      OutputStream stream = new OutputStream();
      int protocol = this.getProtocol();
      stream.writeByte(protocol);
      if(protocol >= 6) {
         stream.writeInt(this.revision);
      }

      stream.writeByte((this.named?1:0) | (this.usesWhirpool?2:0));
      if(protocol >= 7) {
         stream.writeBigSmart(this.validArchiveIds.length);
      } else {
         stream.writeShort(this.validArchiveIds.length);
      }

      int data;
      int archive;
      for(data = 0; data < this.validArchiveIds.length; ++data) {
         archive = this.validArchiveIds[data];
         if(data != 0) {
            archive -= this.validArchiveIds[data - 1];
         }

         if(protocol >= 7) {
            stream.writeBigSmart(archive);
         } else {
            stream.writeShort(archive);
         }
      }

      if(this.named) {
         for(data = 0; data < this.validArchiveIds.length; ++data) {
            stream.writeInt(this.archives[this.validArchiveIds[data]].getNameHash());
         }
      }

      if(this.usesWhirpool) {
         for(data = 0; data < this.validArchiveIds.length; ++data) {
            stream.writeBytes(this.archives[this.validArchiveIds[data]].getWhirpool());
         }
      }

      for(data = 0; data < this.validArchiveIds.length; ++data) {
         stream.writeInt(this.archives[this.validArchiveIds[data]].getCRC());
      }

      for(data = 0; data < this.validArchiveIds.length; ++data) {
         stream.writeInt(this.archives[this.validArchiveIds[data]].getRevision());
      }

      for(data = 0; data < this.validArchiveIds.length; ++data) {
         archive = this.archives[this.validArchiveIds[data]].getValidFileIds().length;
         if(protocol >= 7) {
            stream.writeBigSmart(archive);
         } else {
            stream.writeShort(archive);
         }
      }

      int index2;
      ArchiveReference var8;
      for(data = 0; data < this.validArchiveIds.length; ++data) {
         var8 = this.archives[this.validArchiveIds[data]];

         for(index2 = 0; index2 < var8.getValidFileIds().length; ++index2) {
            int var9 = var8.getValidFileIds()[index2];
            if(index2 != 0) {
               var9 -= var8.getValidFileIds()[index2 - 1];
            }

            if(protocol >= 7) {
               stream.writeBigSmart(var9);
            } else {
               stream.writeShort(var9);
            }
         }
      }

      if(this.named) {
         for(data = 0; data < this.validArchiveIds.length; ++data) {
            var8 = this.archives[this.validArchiveIds[data]];

            for(index2 = 0; index2 < var8.getValidFileIds().length; ++index2) {
               stream.writeInt(var8.getFiles()[var8.getValidFileIds()[index2]].getNameHash());
            }
         }
      }

      byte[] var91 = new byte[stream.getOffset()];
      stream.setOffset(0);
      stream.getBytes(var91, 0, var91.length);
      return this.archive.editNoRevision(var91, mainFile);
   }

   public int getProtocol() {
      if(this.archives.length > '\uffff') {
         return 7;
      } else {
         for(int index = 0; index < this.validArchiveIds.length; ++index) {
            if(index > 0 && this.validArchiveIds[index] - this.validArchiveIds[index - 1] > '\uffff') {
               return 7;
            }

            if(this.archives[this.validArchiveIds[index]].getValidFileIds().length > '\uffff') {
               return 7;
            }
         }

         return this.revision == 0?5:6;
      }
   }

   public void setRevision(int revision) {
      this.updatedRevision = true;
      this.revision = revision;
   }

   public void updateRevision() {
      if(!this.updatedRevision) {
         ++this.revision;
         this.updatedRevision = true;
      }

   }

   private void decodeHeader() {
      InputStream stream = new InputStream(this.archive.getData());
      int protocol = stream.readUnsignedByte();
      if(protocol >= 5 && protocol <= 7) {
         if(protocol >= 6) {
            this.revision = stream.readInt();
         }

         int hash = stream.readUnsignedByte();
         this.named = (1 & hash) != 0;
         this.usesWhirpool = (2 & hash) != 0;
         int validArchivesCount = protocol >= 7?stream.readBigSmart():stream.readUnsignedShort();
         this.validArchiveIds = new int[validArchivesCount];
         int lastArchiveId = 0;
         int biggestArchiveId = 0;

         int index;
         int archive;
         for(index = 0; index < validArchivesCount; ++index) {
            archive = lastArchiveId += protocol >= 7?stream.readBigSmart():stream.readUnsignedShort();
            if(archive > biggestArchiveId) {
               biggestArchiveId = archive;
            }

            this.validArchiveIds[index] = archive;
         }

         this.archives = new ArchiveReference[biggestArchiveId + 1];

         for(index = 0; index < validArchivesCount; ++index) {
            this.archives[this.validArchiveIds[index]] = new ArchiveReference();
         }

         if(this.named) {
            for(index = 0; index < validArchivesCount; ++index) {
               this.archives[this.validArchiveIds[index]].setNameHash(stream.readInt());
            }
         }

         if(this.usesWhirpool) {
            for(index = 0; index < validArchivesCount; ++index) {
               byte[] index2 = new byte[64];
               stream.getBytes(index2, 0, 64);
               this.archives[this.validArchiveIds[index]].setWhirpool(index2);
            }
         }

         for(index = 0; index < validArchivesCount; ++index) {
            this.archives[this.validArchiveIds[index]].setCrc(stream.readInt());
         }

         for(index = 0; index < validArchivesCount; ++index) {
            this.archives[this.validArchiveIds[index]].setRevision(stream.readInt());
         }

         for(index = 0; index < validArchivesCount; ++index) {
            this.archives[this.validArchiveIds[index]].setValidFileIds(new int[protocol >= 7?stream.readBigSmart():stream.readUnsignedShort()]);
         }

         ArchiveReference var14;
         int var13;
         for(index = 0; index < validArchivesCount; ++index) {
            archive = 0;
            var13 = 0;
            var14 = this.archives[this.validArchiveIds[index]];

            int index21;
            for(index21 = 0; index21 < var14.getValidFileIds().length; ++index21) {
               int fileId = archive += protocol >= 7?stream.readBigSmart():stream.readUnsignedShort();
               if(fileId > var13) {
                  var13 = fileId;
               }

               var14.getValidFileIds()[index21] = fileId;
            }

            var14.setFiles(new FileReference[var13 + 1]);

            for(index21 = 0; index21 < var14.getValidFileIds().length; ++index21) {
               var14.getFiles()[var14.getValidFileIds()[index21]] = new FileReference();
            }
         }

         if(this.named) {
            for(index = 0; index < validArchivesCount; ++index) {
               var14 = this.archives[this.validArchiveIds[index]];

               for(var13 = 0; var13 < var14.getValidFileIds().length; ++var13) {
                  var14.getFiles()[var14.getValidFileIds()[var13]].setNameHash(stream.readInt());
               }
            }
         }

      } else {
         throw new RuntimeException("INVALID PROTOCOL");
      }
   }

   public int getRevision() {
      return this.revision;
   }

   public ArchiveReference[] getArchives() {
      return this.archives;
   }

   public int[] getValidArchiveIds() {
      return this.validArchiveIds;
   }

   public boolean isNamed() {
      return this.named;
   }

   public boolean usesWhirpool() {
      return this.usesWhirpool;
   }

   public int getCompression() {
      return this.archive.getCompression();
   }
}
