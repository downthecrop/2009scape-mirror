package com.alex.loaders.interfaces;

import com.alex.io.InputStream;
import com.alex.loaders.interfaces.IComponent;
import com.alex.store.Store;
import com.alex.utils.Utils;

import java.awt.image.FilteredImageSource;
import java.awt.image.ReplicateScaleFilter;
import java.io.IOException;

public class Interface {
   public int id;
   public Store cache;
   public IComponent[] components;
   public JComponent[] jcomponents;

   public static void main(String[] args) throws IOException {
      Store rscache = new Store("cache/");

      for(int i = 0; i < Utils.getInterfaceDefinitionsSize(rscache); ++i) {
         try {
            new Interface(i, rscache);
         } catch (Throwable var4) {
            ;
         }
      }

   }

   public Interface(int id, Store cache) {
      this(id, cache, true);
   }

   public Interface(int id, Store cache, boolean load) {
      this.id = id;
      this.cache = cache;
      if(load) {
         this.getComponents();
      }

   }

   public void draw(JComponent parent) {
   }

   public Image resizeImage(Image image, int width, int height, Component c) {
      ReplicateScaleFilter replicate = new ReplicateScaleFilter(width, height);
      FilteredImageSource prod = new FilteredImageSource(image.getSource(), replicate);
      return c.createImage(prod);
   }

   public void getComponents() {
      if(Utils.getInterfaceDefinitionsSize(this.cache) <= this.id) {
         throw new RuntimeException("Invalid interface id.");
      } else {
         this.components = new IComponent[Utils.getInterfaceDefinitionsComponentsSize(this.cache, this.id)];

         for(int componentId = 0; componentId < this.components.length; ++componentId) {
            this.components[componentId] = new IComponent();
            this.components[componentId].hash = this.id << 16 | componentId;
            byte[] data = this.cache.getIndexes()[3].getFile(this.id, componentId);
            if(data == null) {
               throw new RuntimeException("Interface " + this.id + ", component " + componentId + " data is null.");
            }

            System.out.println("Interface: " + this.id + " - ComponentId: " + componentId);
            if(data[0] != -1) {
               this.components[componentId].decodeNoscriptsFormat(new InputStream(data));
            } else {
               this.components[componentId].decodeScriptsFormat(new InputStream(data));
            }
         }

      }
   }
}
