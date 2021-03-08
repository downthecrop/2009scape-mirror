package com.alex.loaders.images;

import com.alex.io.InputStream;
import com.alex.io.OutputStream;
import com.alex.store.Store;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public final class IndexedColorImageFile {
   private BufferedImage[] images;
   private int[] pallete;
   private int[][] pixelsIndexes;
   private byte[][] alpha;
   private boolean[] usesAlpha;
   private int biggestWidth;
   private int biggestHeight;

   public IndexedColorImageFile(BufferedImage... images) {
      this.images = images;
   }

   public IndexedColorImageFile(Store cache, int archiveId, int fileId) {
      this(cache, 8, archiveId, fileId);
   }

   public IndexedColorImageFile(Store cache, int idx, int archiveId, int fileId) {
      this.decodeArchive(cache, idx, archiveId, fileId);
   }

   public void decodeArchive(Store cache, int idx, int archiveId, int fileId) {
      byte[] data = cache.getIndexes()[idx].getFile(archiveId, fileId);
      if(data != null) {
         InputStream stream = new InputStream(data);
         stream.setOffset(data.length - 2);
         int count = stream.readUnsignedShort();
         this.images = new BufferedImage[count];
         this.pixelsIndexes = new int[this.images.length][];
         this.alpha = new byte[this.images.length][];
         this.usesAlpha = new boolean[this.images.length];
         int[] imagesMinX = new int[this.images.length];
         int[] imagesMinY = new int[this.images.length];
         int[] imagesWidth = new int[this.images.length];
         int[] imagesHeight = new int[this.images.length];
         stream.setOffset(data.length - 7 - this.images.length * 8);
         this.setBiggestWidth(stream.readShort());
         this.setBiggestHeight(stream.readShort());
         int palleteLength = (stream.readUnsignedByte() & 255) + 1;

         int i_20_;
         for(i_20_ = 0; i_20_ < this.images.length; ++i_20_) {
            imagesMinX[i_20_] = stream.readUnsignedShort();
         }

         for(i_20_ = 0; i_20_ < this.images.length; ++i_20_) {
            imagesMinY[i_20_] = stream.readUnsignedShort();
         }

         for(i_20_ = 0; i_20_ < this.images.length; ++i_20_) {
            imagesWidth[i_20_] = stream.readUnsignedShort();
         }

         for(i_20_ = 0; i_20_ < this.images.length; ++i_20_) {
            imagesHeight[i_20_] = stream.readUnsignedShort();
         }

         stream.setOffset(data.length - 7 - this.images.length * 8 - (palleteLength - 1) * 3);
         this.pallete = new int[palleteLength];

         for(i_20_ = 1; i_20_ < palleteLength; ++i_20_) {
            this.pallete[i_20_] = stream.read24BitInt();
            if(this.pallete[i_20_] == 0) {
               this.pallete[i_20_] = 1;
            }
         }

         stream.setOffset(0);

         for(i_20_ = 0; i_20_ < this.images.length; ++i_20_) {
            int pixelsIndexesLength = imagesWidth[i_20_] * imagesHeight[i_20_];
            this.pixelsIndexes[i_20_] = new int[pixelsIndexesLength];
            this.alpha[i_20_] = new byte[pixelsIndexesLength];
            int maskData = stream.readUnsignedByte();
            int i_31_;
            if((maskData & 2) == 0) {
               int var201;
               if((maskData & 1) == 0) {
                  for(var201 = 0; var201 < pixelsIndexesLength; ++var201) {
                     this.pixelsIndexes[i_20_][var201] = (byte)stream.readByte();
                  }
               } else {
                  for(var201 = 0; var201 < imagesWidth[i_20_]; ++var201) {
                     for(i_31_ = 0; i_31_ < imagesHeight[i_20_]; ++i_31_) {
                        this.pixelsIndexes[i_20_][var201 + i_31_ * imagesWidth[i_20_]] = (byte)stream.readByte();
                     }
                  }
               }
            } else {
               this.usesAlpha[i_20_] = true;
               boolean var20 = false;
               if((maskData & 1) == 0) {
                  for(i_31_ = 0; i_31_ < pixelsIndexesLength; ++i_31_) {
                     this.pixelsIndexes[i_20_][i_31_] = (byte)stream.readByte();
                  }

                  for(i_31_ = 0; i_31_ < pixelsIndexesLength; ++i_31_) {
                     byte var21 = this.alpha[i_20_][i_31_] = (byte)stream.readByte();
                     var20 |= var21 != -1;
                  }
               } else {
                  int var211;
                  for(i_31_ = 0; i_31_ < imagesWidth[i_20_]; ++i_31_) {
                     for(var211 = 0; var211 < imagesHeight[i_20_]; ++var211) {
                        this.pixelsIndexes[i_20_][i_31_ + var211 * imagesWidth[i_20_]] = stream.readByte();
                     }
                  }

                  for(i_31_ = 0; i_31_ < imagesWidth[i_20_]; ++i_31_) {
                     for(var211 = 0; var211 < imagesHeight[i_20_]; ++var211) {
                        byte i_33_ = this.alpha[i_20_][i_31_ + var211 * imagesWidth[i_20_]] = (byte)stream.readByte();
                        var20 |= i_33_ != -1;
                     }
                  }
               }

               if(!var20) {
                  this.alpha[i_20_] = null;
               }
            }

            this.images[i_20_] = this.getBufferedImage(imagesWidth[i_20_], imagesHeight[i_20_], this.pixelsIndexes[i_20_], this.alpha[i_20_], this.usesAlpha[i_20_]);
         }
      }

   }

   public BufferedImage getBufferedImage(int width, int height, int[] pixelsIndexes, byte[] extraPixels, boolean useExtraPixels) {
      if(width > 0 && height > 0) {
         BufferedImage image = new BufferedImage(width, height, 6);
         int[] rgbArray = new int[width * height];
         int i = 0;
         int i_43_ = 0;
         int i_46_;
         int i_47_;
         if(useExtraPixels && extraPixels != null) {
            for(i_46_ = 0; i_46_ < height; ++i_46_) {
               for(i_47_ = 0; i_47_ < width; ++i_47_) {
                  rgbArray[i_43_++] = extraPixels[i] << 24 | this.pallete[pixelsIndexes[i] & 255];
                  ++i;
               }
            }
         } else {
            for(i_46_ = 0; i_46_ < height; ++i_46_) {
               for(i_47_ = 0; i_47_ < width; ++i_47_) {
                  int i_48_ = this.pallete[pixelsIndexes[i++] & 255];
                  rgbArray[i_43_++] = i_48_ != 0?-16777216 | i_48_:0;
               }
            }
         }

         image.setRGB(0, 0, width, height, rgbArray, 0, width);
         image.flush();
         return image;
      } else {
         return null;
      }
   }

   public byte[] encodeFile() {
      if(this.pallete == null) {
         this.generatePallete();
      }

      OutputStream stream = new OutputStream();

      int container;
      int len$;
      int i$;
      for(container = 0; container < this.images.length; ++container) {
         len$ = 0;
         if(this.usesAlpha[container]) {
            len$ |= 2;
         }

         stream.writeByte(len$);

         for(i$ = 0; i$ < this.pixelsIndexes[container].length; ++i$) {
            stream.writeByte(this.pixelsIndexes[container][i$]);
         }

         if(this.usesAlpha[container]) {
            for(i$ = 0; i$ < this.alpha[container].length; ++i$) {
               stream.writeByte(this.alpha[container][i$]);
            }
         }
      }

      for(container = 0; container < this.pallete.length; ++container) {
         stream.write24BitInt(this.pallete[container]);
      }

      if(this.biggestWidth == 0 && this.biggestHeight == 0) {
         BufferedImage[] var7 = this.images;
         len$ = var7.length;

         for(i$ = 0; i$ < len$; ++i$) {
            BufferedImage image = var7[i$];
            if(image.getWidth() > this.biggestWidth) {
               this.biggestWidth = image.getWidth();
            }

            if(image.getHeight() > this.biggestHeight) {
               this.biggestHeight = image.getHeight();
            }
         }
      }

      stream.writeShort(this.biggestWidth);
      stream.writeShort(this.biggestHeight);
      stream.writeByte(this.pallete.length - 1);

      for(container = 0; container < this.images.length; ++container) {
         stream.writeShort(this.images[container].getMinX());
      }

      for(container = 0; container < this.images.length; ++container) {
         stream.writeShort(this.images[container].getMinY());
      }

      for(container = 0; container < this.images.length; ++container) {
         stream.writeShort(this.images[container].getWidth());
      }

      for(container = 0; container < this.images.length; ++container) {
         stream.writeShort(this.images[container].getHeight());
      }

      stream.writeShort(this.images.length);
      byte[] var71 = new byte[stream.getOffset()];
      stream.setOffset(0);
      stream.getBytes(var71, 0, var71.length);
      return var71;
   }

   public int getPalleteIndex(int rgb) {
      if(this.pallete == null) {
         this.pallete = new int[1];
      }

      for(int var3 = 0; var3 < this.pallete.length; ++var3) {
         if(this.pallete[var3] == rgb) {
            return var3;
         }
      }

      if(this.pallete.length == 256) {
         System.out.println("Pallete to big, please reduce images quality.");
         return 0;
      } else {
         int[] var31 = new int[this.pallete.length + 1];
         System.arraycopy(this.pallete, 0, var31, 0, this.pallete.length);
         var31[this.pallete.length] = rgb;
         this.pallete = var31;
         return this.pallete.length - 1;
      }
   }

   public int addImage(BufferedImage image) {
      BufferedImage[] newImages = (BufferedImage[])Arrays.copyOf(this.images, this.images.length + 1);
      newImages[this.images.length] = image;
      this.images = newImages;
      this.pallete = null;
      this.pixelsIndexes = null;
      this.alpha = null;
      this.usesAlpha = null;
      return this.images.length - 1;
   }

   public void replaceImage(BufferedImage image, int index) {
      this.images[index] = image;
      this.pallete = null;
      this.pixelsIndexes = null;
      this.alpha = null;
      this.usesAlpha = null;
   }

   public void generatePallete() {
      this.pixelsIndexes = new int[this.images.length][];
      this.alpha = new byte[this.images.length][];
      this.usesAlpha = new boolean[this.images.length];

      for(int index = 0; index < this.images.length; ++index) {
         BufferedImage image = this.images[index];
         int[] rgbArray = new int[image.getWidth() * image.getHeight()];
         image.getRGB(0, 0, image.getWidth(), image.getHeight(), rgbArray, 0, image.getWidth());
         this.pixelsIndexes[index] = new int[image.getWidth() * image.getHeight()];
         this.alpha[index] = new byte[image.getWidth() * image.getHeight()];

         for(int pixel = 0; pixel < this.pixelsIndexes[index].length; ++pixel) {
            int rgb = rgbArray[pixel];
            int medintrgb = this.convertToMediumInt(rgb);
            int i = this.getPalleteIndex(medintrgb);
            this.pixelsIndexes[index][pixel] = i;
            if(rgb >> 24 != 0) {
               this.alpha[index][pixel] = (byte)(rgb >> 24);
               this.usesAlpha[index] = true;
            }
         }
      }

   }

   public int convertToMediumInt(int rgb) {
      OutputStream out = new OutputStream(4);
      out.writeInt(rgb);
      InputStream stream = new InputStream(out.getBuffer());
      stream.setOffset(1);
      rgb = stream.read24BitInt();
      return rgb;
   }

   public BufferedImage[] getImages() {
      return this.images;
   }

   public int getBiggestWidth() {
      return this.biggestWidth;
   }

   public void setBiggestWidth(int biggestWidth) {
      this.biggestWidth = biggestWidth;
   }

   public int getBiggestHeight() {
      return this.biggestHeight;
   }

   public void setBiggestHeight(int biggestHeight) {
      this.biggestHeight = biggestHeight;
   }
}
