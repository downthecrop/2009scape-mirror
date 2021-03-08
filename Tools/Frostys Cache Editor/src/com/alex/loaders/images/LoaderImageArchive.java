package com.alex.loaders.images;

import com.alex.store.Store;

public class LoaderImageArchive {
   private byte[] data;

   public LoaderImageArchive(byte[] data) {
      this.data = data;
   }

   public LoaderImageArchive(Store cache, int archiveId) {
      this(cache, 32, archiveId, 0);
   }

   private LoaderImageArchive(Store cache, int idx, int archiveId, int fileId) {
      this.decodeArchive(cache, idx, archiveId, fileId);
   }

   private void decodeArchive(Store cache, int idx, int archiveId, int fileId) {
      byte[] data = cache.getIndexes()[idx].getFile(archiveId, fileId);
      if(data != null) {
         this.data = data;
      }

   }

   public Image getImage() {
      return Toolkit.getDefaultToolkit().createImage(this.data);
   }

   public byte[] getImageData() {
      return this.data;
   }
}
