package com.alex.tools.clientCacheUpdater;

import com.alex.store.Index;
import com.alex.store.Store;

import java.io.IOException;

public class CopyCache {
   public static void main(String[] args) throws IOException {
      Store cache = new Store("718/cache/");
      Store newCache = new Store("718/cacheCleaned/");

      for(int i = 0; i < cache.getIndexes().length; ++i) {
         Index index = cache.getIndexes()[i];
         newCache.addIndex(index.getTable().isNamed(), index.getTable().usesWhirpool(), 2);
         newCache.getIndexes()[i].packIndex(cache);
         newCache.getIndexes()[i].getTable().setRevision(cache.getIndexes()[i].getTable().getRevision());
         newCache.getIndexes()[i].rewriteTable();
      }

   }
}
