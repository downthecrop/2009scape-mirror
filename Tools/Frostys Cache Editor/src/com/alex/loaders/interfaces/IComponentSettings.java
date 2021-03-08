package com.alex.loaders.interfaces;

public final class IComponentSettings {
   public int settingsHash;
   public int defaultHash;

   public IComponentSettings(int settingsHash, int defaultHash) {
      this.settingsHash = settingsHash;
      this.defaultHash = defaultHash;
   }
}
