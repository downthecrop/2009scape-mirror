package com.alex.store;

import com.alex.store.FileReference;

import java.util.Arrays;

public class ArchiveReference {
   private int nameHash;
   private byte[] whirpool;
   private int crc;
   private int revision;
   private FileReference[] files;
   private int[] validFileIds;
   private boolean needsFilesSort;
   private boolean updatedRevision;

   public void updateRevision() {
      if(!this.updatedRevision) {
         ++this.revision;
         this.updatedRevision = true;
      }

   }

   public int getNameHash() {
      return this.nameHash;
   }

   public void setNameHash(int nameHash) {
      this.nameHash = nameHash;
   }

   public byte[] getWhirpool() {
      return this.whirpool;
   }

   public void setWhirpool(byte[] whirpool) {
      this.whirpool = whirpool;
   }

   public int getCRC() {
      return this.crc;
   }

   public void setCrc(int crc) {
      this.crc = crc;
   }

   public int getRevision() {
      return this.revision;
   }

   public FileReference[] getFiles() {
      return this.files;
   }

   public void setFiles(FileReference[] files) {
      this.files = files;
   }

   public void setRevision(int revision) {
      this.revision = revision;
   }

   public int[] getValidFileIds() {
      return this.validFileIds;
   }

   public void setValidFileIds(int[] validFileIds) {
      this.validFileIds = validFileIds;
   }

   public boolean isNeedsFilesSort() {
      return this.needsFilesSort;
   }

   public void setNeedsFilesSort(boolean needsFilesSort) {
      this.needsFilesSort = needsFilesSort;
   }

   public void removeFileReference(int fileId) {
      int[] newValidFileIds = new int[this.validFileIds.length - 1];
      int count = 0;
      int[] arr$ = this.validFileIds;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         int id = arr$[i$];
         if(id != fileId) {
            newValidFileIds[count++] = id;
         }
      }

      this.validFileIds = newValidFileIds;
      this.files[fileId] = null;
   }

   public void addEmptyFileReference(int fileId) {
      this.needsFilesSort = true;
      int[] newValidFileIds = Arrays.copyOf(this.validFileIds, this.validFileIds.length + 1);
      newValidFileIds[newValidFileIds.length - 1] = fileId;
      this.validFileIds = newValidFileIds;
      if(this.files.length <= fileId) {
         FileReference[] newFiles = (FileReference[])Arrays.copyOf(this.files, fileId + 1);
         newFiles[fileId] = new FileReference();
         this.files = newFiles;
      } else {
         this.files[fileId] = new FileReference();
      }

   }

   public void sortFiles() {
      Arrays.sort(this.validFileIds);
      this.needsFilesSort = false;
   }

   public void reset() {
      this.whirpool = null;
      this.updatedRevision = true;
      this.revision = 0;
      this.nameHash = 0;
      this.crc = 0;
      this.files = new FileReference[0];
      this.validFileIds = new int[0];
      this.needsFilesSort = false;
   }

   public void copyHeader(ArchiveReference fromReference) {
      this.setCrc(fromReference.getCRC());
      this.setNameHash(fromReference.getNameHash());
      this.setWhirpool(fromReference.getWhirpool());
      int[] validFiles = fromReference.getValidFileIds();
      this.setValidFileIds(Arrays.copyOf(validFiles, validFiles.length));
      FileReference[] files = fromReference.getFiles();
      this.setFiles((FileReference[])Arrays.copyOf(files, files.length));
   }
}
