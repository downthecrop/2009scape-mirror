package com.alex.loaders.objects;

import com.alex.io.InputStream;
import com.alex.io.OutputStream;
import com.alex.store.Store;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ObjectDefinitions {
   private static final ConcurrentHashMap objectDefinitions = new ConcurrentHashMap();
   private short[] originalColors;
   int[] toObjectIds;
   static int anInt3832;
   int[] anIntArray3833 = null;
   public int anInt3834;
   int anInt3835;
   static int anInt3836;
   public byte aByte3837;
   int anInt3838 = -1;
   boolean aBoolean3839;
   public int anInt3840;
   public int anInt3841;
   static int anInt3842;
   static int anInt3843;
   int anInt3844;
   boolean aBoolean3845;
   public byte aByte3847;
   public boolean ignoreClipOnAlternativeRoute;
   int[] animations = null;
   private byte[] possibleTypes;
   private int[] anIntArray4534;
   private byte[] unknownArray4;
   private byte[] unknownArray3;
   private int cflag;
   public byte aByte3849;
   int anInt3850;
   int anInt3851;
   public boolean secondBool;
   public boolean aBoolean3853;
   int anInt3855;
   public boolean notCliped;
   int anInt3857;
   private byte[] aByteArray3858;
   int[] anIntArray3859;
   int anInt3860;
   public String[] options;
   int configFileId;
   private short[] modifiedColors;
   int anInt3865;
   boolean aBoolean3866;
   boolean aBoolean3867;
   private int[] anIntArray3869;
   boolean aBoolean3870;
   public int sizeY;
   boolean aBoolean3872;
   boolean aBoolean3873;
   public int thirdInt;
   public int anInt3875;
   public int objectAnimation;
   public int anInt3877;
   public int anInt3878;
   public int clipType;
   public int anInt3881;
   public int anInt3882;
   public int anInt3883;
   Object loader;
   public int anInt3889;
   public int sizeX;
   public boolean aBoolean3891;
   int anInt3892;
   public int secondInt;
   boolean aBoolean3894;
   boolean aBoolean3895;
   int anInt3896;
   int configId;
   private byte[] aByteArray3899;
   int anInt3900;
   public String name;
   public int anInt3902;
   int anInt3904;
   int anInt3905;
   boolean aBoolean3906;
   int[] anIntArray3908;
   public byte aByte3912;
   int anInt3913;
   public byte aByte3914;
   public int anInt3915;
   public int[][] modelIds;
   public int anInt3917;
   public boolean loaded;
   private short[] aShortArray3919;
   private short[] aShortArray3920;
   int anInt3921;
   private HashMap parameters;
   boolean aBoolean3923;
   boolean aBoolean3924;
   int anInt3925;
   public int id;
   public boolean aBool6886;
   static int anInt3846;
   public boolean projectileCliped;

   public ObjectDefinitions() {
      this.anInt3835 = -1;
      this.anInt3860 = -1;
      this.configFileId = -1;
      this.aBoolean3866 = false;
      this.anInt3851 = -1;
      this.anInt3865 = 255;
      this.aBoolean3845 = false;
      this.aBoolean3867 = false;
      this.anInt3850 = 0;
      this.anInt3844 = -1;
      this.anInt3881 = 0;
      this.anInt3857 = -1;
      this.aBoolean3872 = true;
      this.anInt3882 = -1;
      this.anInt3834 = 0;
      this.options = new String[5];
      this.anInt3875 = 0;
      this.aBoolean3839 = false;
      this.anIntArray3869 = null;
      this.sizeY = 1;
      this.thirdInt = -1;
      this.anInt3883 = 0;
      this.aBoolean3895 = true;
      this.anInt3840 = 0;
      this.aBoolean3870 = false;
      this.anInt3889 = 0;
      this.aBoolean3853 = true;
      this.secondBool = false;
      this.clipType = 2;
      this.projectileCliped = true;
      this.ignoreClipOnAlternativeRoute = false;
      this.anInt3855 = -1;
      this.anInt3878 = 0;
      this.anInt3904 = 0;
      this.sizeX = 1;
      this.objectAnimation = -1;
      this.aBoolean3891 = false;
      this.anInt3905 = 0;
      this.name = "null";
      this.anInt3913 = -1;
      this.aBoolean3906 = false;
      this.aBoolean3873 = false;
      this.aByte3914 = 0;
      this.anInt3915 = 0;
      this.anInt3900 = 0;
      this.secondInt = -1;
      this.aBoolean3894 = false;
      this.aByte3912 = 0;
      this.anInt3921 = 0;
      this.anInt3902 = 128;
      this.configId = -1;
      this.anInt3877 = 0;
      this.anInt3925 = 0;
      this.anInt3892 = 64;
      this.aBoolean3923 = false;
      this.aBoolean3924 = false;
      this.anInt3841 = 128;
      this.anInt3917 = 128;
   }

   public String getFirstOption() {
      return this.options != null && this.options.length >= 1?this.options[0]:"";
   }

   public String getSecondOption() {
      return this.options != null && this.options.length >= 2?this.options[1]:"";
   }

   public String getOption(int option) {
      return this.options != null && this.options.length >= option && option != 0?this.options[option - 1]:"";
   }

   public String getThirdOption() {
      return this.options != null && this.options.length >= 3?this.options[2]:"";
   }

   private static int getArchiveId(int i_0_) {
      return i_0_ >>> -1135990488;
   }

   public int getSizeX() {
      return this.sizeX;
   }

   public int getSizeY() {
      return this.sizeY;
   }

   private Object getValue(Field field) throws Throwable {
      field.setAccessible(true);
      Class type = field.getType();
      return type == int[][].class?Arrays.toString((int[][])field.get(this)):(type == int[].class?Arrays.toString((int[])field.get(this)):(type == byte[].class?Arrays.toString((byte[])field.get(this)):(type == short[].class?Arrays.toString((short[])field.get(this)):(type == double[].class?Arrays.toString((double[])field.get(this)):(type == float[].class?Arrays.toString((float[])field.get(this)):(type == Object[].class?Arrays.toString((Object[])field.get(this)):field.get(this)))))));
   }

   public boolean isProjectileCliped() {
      return this.projectileCliped;
   }

   public boolean containsOption(int i, String option) {
      return this.options != null && this.options[i] != null && this.options.length > i?this.options[i].equals(option):false;
   }

   public boolean containsOption(String o) {
      if(this.options == null) {
         return false;
      } else {
         String[] var5 = this.options;
         int var4 = this.options.length;

         for(int var3 = 0; var3 < var4; ++var3) {
            String option = var5[var3];
            if(option != null && option.equalsIgnoreCase(o)) {
               return true;
            }
         }

         return false;
      }
   }

   final void method3287() {
      if(this.secondInt == -1) {
         this.secondInt = 0;
         if(this.possibleTypes != null && this.possibleTypes.length == 1 && this.possibleTypes[0] == 10) {
            this.secondInt = 1;
         }

         for(int i_13_ = 0; i_13_ < 5; ++i_13_) {
            if(this.options[i_13_] != null) {
               this.secondInt = 1;
               break;
            }
         }
      }

      if(this.anInt3855 == -1) {
         this.anInt3855 = this.clipType != 0?1:0;
      }

   }

   public int getAccessBlockFlag() {
      return this.cflag;
   }

   private void readValues(InputStream stream, int opcode) {
      boolean aBoolean1162;
      int i_73_;
      int i_74_;
      int i_75_;
      if(opcode != 1 && opcode != 5) {
         if(opcode != 2) {
            if(opcode != 14) {
               if(opcode != 15) {
                  if(opcode == 17) {
                     this.projectileCliped = false;
                     this.clipType = 0;
                  } else if(opcode != 18) {
                     if(opcode == 19) {
                        this.secondInt = stream.readUnsignedByte();
                     } else if(opcode == 21) {
                        this.aByte3912 = 1;
                     } else if(opcode != 22) {
                        if(opcode != 23) {
                           if(opcode != 24) {
                              if(opcode == 27) {
                                 this.clipType = 1;
                              } else if(opcode == 28) {
                                 this.anInt3892 = stream.readUnsignedByte() << 2;
                              } else if(opcode != 29) {
                                 if(opcode != 39) {
                                    if(opcode >= 30 && opcode < 35) {
                                       this.options[-30 + opcode] = stream.readString();
                                    } else {
                                       int var8;
                                       if(opcode == 40) {
                                          var8 = stream.readUnsignedByte();
                                          this.originalColors = new short[var8];
                                          this.modifiedColors = new short[var8];

                                          for(i_73_ = 0; var8 > i_73_; ++i_73_) {
                                             this.originalColors[i_73_] = (short)stream.readUnsignedShort();
                                             this.modifiedColors[i_73_] = (short)stream.readUnsignedShort();
                                          }
                                       } else {
                                          short var9;
                                          byte var10;
                                          if(44 == opcode) {
                                             var9 = (short)stream.readUnsignedShort();
                                             i_73_ = 0;

                                             for(i_74_ = var9; i_74_ > 0; i_74_ >>= 1) {
                                                ++i_73_;
                                             }

                                             this.unknownArray3 = new byte[i_73_];
                                             var10 = 0;

                                             for(i_75_ = 0; i_75_ < i_73_; ++i_75_) {
                                                if((var9 & 1 << i_75_) > 0) {
                                                   this.unknownArray3[i_75_] = var10++;
                                                } else {
                                                   this.unknownArray3[i_75_] = -1;
                                                }
                                             }
                                          } else if(opcode == 45) {
                                             var9 = (short)stream.readUnsignedShort();
                                             i_73_ = 0;

                                             for(i_74_ = var9; i_74_ > 0; i_74_ >>= 1) {
                                                ++i_73_;
                                             }

                                             this.unknownArray4 = new byte[i_73_];
                                             var10 = 0;

                                             for(i_75_ = 0; i_75_ < i_73_; ++i_75_) {
                                                if((var9 & 1 << i_75_) > 0) {
                                                   this.unknownArray4[i_75_] = var10++;
                                                } else {
                                                   this.unknownArray4[i_75_] = -1;
                                                }
                                             }
                                          } else if(opcode != 41) {
                                             if(opcode != 42) {
                                                if(opcode != 62) {
                                                   if(opcode != 64) {
                                                      if(opcode == 65) {
                                                         this.anInt3902 = stream.readUnsignedShort();
                                                      } else if(opcode != 66) {
                                                         if(opcode != 67) {
                                                            if(opcode == 69) {
                                                               this.cflag = stream.readUnsignedByte();
                                                            } else if(opcode != 70) {
                                                               if(opcode == 71) {
                                                                  this.anInt3889 = stream.readShort() << 2;
                                                               } else if(opcode != 72) {
                                                                  if(opcode == 73) {
                                                                     this.secondBool = true;
                                                                  } else if(opcode == 74) {
                                                                     this.ignoreClipOnAlternativeRoute = true;
                                                                  } else if(opcode != 75) {
                                                                     if(opcode != 77 && opcode != 92) {
                                                                        if(opcode == 78) {
                                                                           this.anInt3860 = stream.readUnsignedShort();
                                                                           this.anInt3904 = stream.readUnsignedByte();
                                                                        } else if(opcode != 79) {
                                                                           if(opcode == 81) {
                                                                              this.aByte3912 = 2;
                                                                              this.anInt3882 = 256 * stream.readUnsignedByte();
                                                                           } else if(opcode != 82) {
                                                                              if(opcode == 88) {
                                                                                 this.aBoolean3853 = false;
                                                                              } else if(opcode != 89) {
                                                                                 if(opcode == 90) {
                                                                                    this.aBoolean3870 = true;
                                                                                 } else if(opcode != 91) {
                                                                                    if(opcode != 93) {
                                                                                       if(opcode == 94) {
                                                                                          this.aByte3912 = 4;
                                                                                       } else if(opcode != 95) {
                                                                                          if(opcode != 96) {
                                                                                             if(opcode == 97) {
                                                                                                this.aBoolean3866 = true;
                                                                                             } else if(opcode == 98) {
                                                                                                this.aBoolean3923 = true;
                                                                                             } else if(opcode == 99) {
                                                                                                this.anInt3857 = stream.readUnsignedByte();
                                                                                                this.anInt3835 = stream.readUnsignedShort();
                                                                                             } else if(opcode == 100) {
                                                                                                this.anInt3844 = stream.readUnsignedByte();
                                                                                                this.anInt3913 = stream.readUnsignedShort();
                                                                                             } else if(opcode != 101) {
                                                                                                if(opcode == 102) {
                                                                                                   this.anInt3838 = stream.readUnsignedShort();
                                                                                                } else if(opcode == 103) {
                                                                                                   this.thirdInt = 0;
                                                                                                } else if(opcode != 104) {
                                                                                                   if(opcode == 105) {
                                                                                                      this.aBoolean3906 = true;
                                                                                                   } else if(opcode == 106) {
                                                                                                      var8 = stream.readUnsignedByte();
                                                                                                      this.anIntArray3869 = new int[var8];
                                                                                                      this.animations = new int[var8];

                                                                                                      for(i_73_ = 0; i_73_ < var8; ++i_73_) {
                                                                                                         this.animations[i_73_] = stream.readBigSmart();
                                                                                                         i_74_ = stream.readUnsignedByte();
                                                                                                         this.anIntArray3869[i_73_] = i_74_;
                                                                                                         this.anInt3881 += i_74_;
                                                                                                      }
                                                                                                   } else if(opcode == 107) {
                                                                                                      this.anInt3851 = stream.readUnsignedShort();
                                                                                                   } else if(opcode >= 150 && opcode < 155) {
                                                                                                      this.options[opcode + -150] = stream.readString();
                                                                                                   } else if(opcode != 160) {
                                                                                                      if(opcode == 162) {
                                                                                                         this.aByte3912 = 3;
                                                                                                         this.anInt3882 = stream.readInt();
                                                                                                      } else if(opcode == 163) {
                                                                                                         this.aByte3847 = (byte)stream.readByte();
                                                                                                         this.aByte3849 = (byte)stream.readByte();
                                                                                                         this.aByte3837 = (byte)stream.readByte();
                                                                                                         this.aByte3914 = (byte)stream.readByte();
                                                                                                      } else if(opcode != 164) {
                                                                                                         if(opcode != 165) {
                                                                                                            if(opcode != 166) {
                                                                                                               if(opcode == 167) {
                                                                                                                  this.anInt3921 = stream.readUnsignedShort();
                                                                                                               } else if(opcode != 168) {
                                                                                                                  if(opcode == 169) {
                                                                                                                     this.aBoolean3845 = true;
                                                                                                                  } else if(opcode == 170) {
                                                                                                                     var8 = stream.readUnsignedSmart();
                                                                                                                  } else if(opcode == 171) {
                                                                                                                     var8 = stream.readUnsignedSmart();
                                                                                                                  } else if(opcode == 173) {
                                                                                                                     var8 = stream.readUnsignedShort();
                                                                                                                     i_73_ = stream.readUnsignedShort();
                                                                                                                  } else if(opcode == 177) {
                                                                                                                     aBoolean1162 = true;
                                                                                                                  } else if(opcode == 178) {
                                                                                                                     var8 = stream.readUnsignedByte();
                                                                                                                  } else if(opcode == 189) {
                                                                                                                     aBoolean1162 = true;
                                                                                                                  } else if(opcode >= 190 && opcode < 196) {
                                                                                                                     if(this.anIntArray4534 == null) {
                                                                                                                        this.anIntArray4534 = new int[6];
                                                                                                                        Arrays.fill(this.anIntArray4534, -1);
                                                                                                                     }

                                                                                                                     this.anIntArray4534[opcode - 190] = stream.readUnsignedShort();
                                                                                                                  } else if(opcode == 249) {
                                                                                                                     var8 = stream.readUnsignedByte();
                                                                                                                     if(this.parameters == null) {
                                                                                                                        this.parameters = new HashMap(var8);
                                                                                                                     }

                                                                                                                     for(i_73_ = 0; i_73_ < var8; ++i_73_) {
                                                                                                                        boolean var11 = stream.readUnsignedByte() == 1;
                                                                                                                        i_75_ = stream.read24BitInt();
                                                                                                                        if(!var11) {
                                                                                                                           this.parameters.put(Integer.valueOf(i_75_), Integer.valueOf(stream.readInt()));
                                                                                                                        } else {
                                                                                                                           this.parameters.put(Integer.valueOf(i_75_), stream.readString());
                                                                                                                        }
                                                                                                                     }
                                                                                                                  }
                                                                                                               } else {
                                                                                                                  this.aBoolean3894 = true;
                                                                                                               }
                                                                                                            } else {
                                                                                                               this.anInt3877 = stream.readShort();
                                                                                                            }
                                                                                                         } else {
                                                                                                            this.anInt3875 = stream.readShort();
                                                                                                         }
                                                                                                      } else {
                                                                                                         this.anInt3834 = stream.readShort();
                                                                                                      }
                                                                                                   } else {
                                                                                                      var8 = stream.readUnsignedByte();
                                                                                                      this.anIntArray3908 = new int[var8];

                                                                                                      for(i_73_ = 0; var8 > i_73_; ++i_73_) {
                                                                                                         this.anIntArray3908[i_73_] = stream.readUnsignedShort();
                                                                                                      }
                                                                                                   }
                                                                                                } else {
                                                                                                   this.anInt3865 = stream.readUnsignedByte();
                                                                                                }
                                                                                             } else {
                                                                                                this.anInt3850 = stream.readUnsignedByte();
                                                                                             }
                                                                                          } else {
                                                                                             this.aBoolean3924 = true;
                                                                                          }
                                                                                       } else {
                                                                                          this.aByte3912 = 5;
                                                                                          this.anInt3882 = stream.readShort();
                                                                                       }
                                                                                    } else {
                                                                                       this.aByte3912 = 3;
                                                                                       this.anInt3882 = stream.readUnsignedShort();
                                                                                    }
                                                                                 } else {
                                                                                    this.aBoolean3873 = true;
                                                                                 }
                                                                              } else {
                                                                                 this.aBoolean3895 = false;
                                                                              }
                                                                           } else {
                                                                              this.aBoolean3891 = true;
                                                                           }
                                                                        } else {
                                                                           this.anInt3900 = stream.readUnsignedShort();
                                                                           this.anInt3905 = stream.readUnsignedShort();
                                                                           this.anInt3904 = stream.readUnsignedByte();
                                                                           var8 = stream.readUnsignedByte();
                                                                           this.anIntArray3859 = new int[var8];

                                                                           for(i_73_ = 0; i_73_ < var8; ++i_73_) {
                                                                              this.anIntArray3859[i_73_] = stream.readUnsignedShort();
                                                                           }
                                                                        }
                                                                     } else {
                                                                        this.configFileId = stream.readUnsignedShort();
                                                                        if(this.configFileId == '\uffff') {
                                                                           this.configFileId = -1;
                                                                        }

                                                                        this.configId = stream.readUnsignedShort();
                                                                        if(this.configId == '\uffff') {
                                                                           this.configId = -1;
                                                                        }

                                                                        var8 = -1;
                                                                        if(opcode == 92) {
                                                                           var8 = stream.readBigSmart();
                                                                        }

                                                                        i_73_ = stream.readUnsignedByte();
                                                                        this.toObjectIds = new int[i_73_ - -2];

                                                                        for(i_74_ = 0; i_73_ >= i_74_; ++i_74_) {
                                                                           this.toObjectIds[i_74_] = stream.readBigSmart();
                                                                        }

                                                                        this.toObjectIds[i_73_ + 1] = var8;
                                                                     }
                                                                  } else {
                                                                     this.anInt3855 = stream.readUnsignedByte();
                                                                  }
                                                               } else {
                                                                  this.anInt3915 = stream.readShort() << 2;
                                                               }
                                                            } else {
                                                               this.anInt3883 = stream.readShort() << 2;
                                                            }
                                                         } else {
                                                            this.anInt3917 = stream.readUnsignedShort();
                                                         }
                                                      } else {
                                                         this.anInt3841 = stream.readUnsignedShort();
                                                      }
                                                   } else {
                                                      this.aBoolean3872 = false;
                                                   }
                                                } else {
                                                   this.aBoolean3839 = true;
                                                }
                                             } else {
                                                var8 = stream.readUnsignedByte();
                                                this.aByteArray3858 = new byte[var8];

                                                for(i_73_ = 0; i_73_ < var8; ++i_73_) {
                                                   this.aByteArray3858[i_73_] = (byte)stream.readByte();
                                                }
                                             }
                                          } else {
                                             var8 = stream.readUnsignedByte();
                                             this.aShortArray3920 = new short[var8];
                                             this.aShortArray3919 = new short[var8];

                                             for(i_73_ = 0; var8 > i_73_; ++i_73_) {
                                                this.aShortArray3920[i_73_] = (short)stream.readUnsignedShort();
                                                this.aShortArray3919[i_73_] = (short)stream.readUnsignedShort();
                                             }
                                          }
                                       }
                                    }
                                 } else {
                                    this.anInt3840 = stream.readByte() * 5;
                                 }
                              } else {
                                 this.anInt3878 = stream.readByte();
                              }
                           } else {
                              this.objectAnimation = stream.readBigSmart();
                           }
                        } else {
                           this.thirdInt = 1;
                        }
                     } else {
                        this.aBoolean3867 = true;
                     }
                  } else {
                     this.projectileCliped = false;
                  }
               } else {
                  this.sizeY = stream.readUnsignedByte();
               }
            } else {
               this.sizeX = stream.readUnsignedByte();
            }
         } else {
            this.name = stream.readString();
         }
      } else {
         aBoolean1162 = false;
         if(opcode == 5 && aBoolean1162) {
            this.skipReadModelIds(stream);
         }

         i_73_ = stream.readUnsignedByte();
         this.modelIds = new int[i_73_][];
         this.possibleTypes = new byte[i_73_];

         for(i_74_ = 0; i_74_ < i_73_; ++i_74_) {
            this.possibleTypes[i_74_] = (byte)stream.readByte();
            i_75_ = stream.readUnsignedByte();
            this.modelIds[i_74_] = new int[i_75_];

            for(int i_76_ = 0; i_75_ > i_76_; ++i_76_) {
               this.modelIds[i_74_][i_76_] = stream.readIntLE(); //fix
            }
         }

         if(opcode == 5 && !aBoolean1162) {
            this.skipReadModelIds(stream);
         }
      }

   }

   private void skipReadModelIds(InputStream stream) {
      int length = stream.readUnsignedByte();

      for(int index = 0; index < length; ++index) {
         stream.skip(1);
         int length2 = stream.readUnsignedByte();

         for(int i = 0; i < length2; ++i) {
            stream.readBigSmart();
         }
      }

   }

   private void readValueLoop(InputStream stream) {
      while(true) {
         int opcode = stream.readUnsignedByte();
         if(opcode == 0) {
            return;
         }

         this.readValues(stream, opcode);
      }
   }

   public ObjectDefinitions(Store cache, int i) {
      this.anInt3835 = -1;
      this.anInt3860 = -1;
      this.configFileId = -1;
      this.aBoolean3866 = false;
      this.anInt3851 = -1;
      this.anInt3865 = 255;
      this.aBoolean3845 = false;
      this.aBoolean3867 = false;
      this.anInt3850 = 0;
      this.anInt3844 = -1;
      this.anInt3881 = 0;
      this.anInt3857 = -1;
      this.aBoolean3872 = true;
      this.anInt3882 = -1;
      this.anInt3834 = 0;
      this.options = new String[5];
      this.anInt3875 = 0;
      this.aBoolean3839 = false;
      this.anIntArray3869 = null;
      this.sizeY = 1;
      this.thirdInt = -1;
      this.anInt3883 = 0;
      this.aBoolean3895 = true;
      this.anInt3840 = 0;
      this.aBoolean3870 = false;
      this.anInt3889 = 0;
      this.aBoolean3853 = true;
      this.secondBool = false;
      this.clipType = 2;
      this.projectileCliped = true;
      this.notCliped = false;
      this.anInt3855 = -1;
      this.anInt3878 = 0;
      this.anInt3904 = 0;
      this.sizeX = 1;
      this.objectAnimation = -1;
      this.aBoolean3891 = false;
      this.anInt3905 = 0;
      this.name = "null";
      this.anInt3913 = -1;
      this.aBoolean3906 = false;
      this.aBoolean3873 = false;
      this.aByte3914 = 0;
      this.anInt3915 = 0;
      this.anInt3900 = 0;
      this.secondInt = -1;
      this.aBoolean3894 = false;
      this.aByte3912 = 0;
      this.anInt3921 = 0;
      this.anInt3902 = 128;
      this.configId = -1;
      this.anInt3877 = 0;
      this.anInt3925 = 0;
      this.anInt3892 = 64;
      this.aBoolean3923 = false;
      this.aBoolean3924 = false;
      this.anInt3841 = 128;
      this.anInt3917 = 128;
   }

   public int getArchiveId() {
      return this.id >>> -1135990488;
   }

   public int getFileId() {
      return 0xff & this.id;
   }

   public static ObjectDefinitions getObjectDefinitions(int id, Store store) {
      ObjectDefinitions def = (ObjectDefinitions)objectDefinitions.get(Integer.valueOf(id));
      if(def == null) {
         def = new ObjectDefinitions();
         def.id = id;
         byte[] data = store.getIndexes()[16].getFile(getArchiveId(id), id & 0xff);
         if(data != null) {
            def.readValueLoop(new InputStream(data));
         }

         def.method3287();
         objectDefinitions.put(Integer.valueOf(id), def);
      }

      return def;
   }

   private void loadObjectDefinition(Store store) {
      byte[] data = store.getIndexes()[16].getFile(this.id >>> -1135990488, this.id & 0xff);
      if(data == null) {
         System.out.println("FAILED LOADING OBJECT " + this.id);
      } else {
         try {
            this.readOpcodeValues(new InputStream(data));
         } catch (RuntimeException var4) {
            var4.printStackTrace();
         }

         this.loaded = true;
      }

   }

   private void readOpcodeValues(InputStream stream) {
      while(true) {
         int opcode = stream.readUnsignedByte();
         if(opcode == 0) {
            return;
         }

         this.readValues(stream, opcode);
      }
   }

   public static ObjectDefinitions getObjectDefinition(Store cache, int itemId) {
      return getObjectDefinition(cache, itemId, true);
   }

   public static ObjectDefinitions getObjectDefinition(Store cache, int itemId, boolean load) {
      return new ObjectDefinitions(cache, itemId, load);
   }

   public ObjectDefinitions(Store cache, int id, boolean load) {
      this.id = id;
      this.setDefaultVariableValues();
      this.setDefaultOptions();
      if(load) {
         this.loadObjectDefinition(cache);
      }

   }

   private void setDefaultOptions() {
      this.options = new String[5];
   }

   private void setDefaultVariableValues() {
      this.name = name;
      this.sizeX = 1;
      this.sizeY = 1;
      this.projectileCliped = true;
      this.clipType = 2;
      this.objectAnimation = -1;
   }

   public int getClipType() {
      return this.clipType;
   }

   public static void clearObjectDefinitions() {
      objectDefinitions.clear();
   }

   public void printFields() {
      Field[] arr$ = this.getClass().getDeclaredFields();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Field field = arr$[i$];
         if((field.getModifiers() & 8) == 0) {
            try {
               System.out.println(field.getName() + ": " + this.getValue(field));
            } catch (Throwable var6) {
               var6.printStackTrace();
            }
         }
      }

      System.out.println("-- end of " + this.getClass().getSimpleName() + " fields --");
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public byte[] encode() {
      OutputStream stream = new OutputStream();
      stream.writeByte(1);
      int i_73_ = this.modelIds.length;
      this.modelIds = new int[i_73_][];
      this.aByteArray3899 = new byte[i_73_];

      int data;
      for(data = 0; data < i_73_; ++data) {
         stream.write128Byte(this.aByteArray3899[data]);
         int var6 = this.modelIds[data].length;
         this.modelIds[data] = new int[var6];

         for(int i_76_ = 0; var6 > i_76_; ++i_76_) {
            stream.writeBigSmart(this.modelIds[data][i_76_]);
         }
      }

      if(!this.name.equals("null")) {
         stream.writeByte(2);
         stream.writeString(this.name);
      }

      if(this.sizeX != 1) {
         stream.writeByte(14);
         stream.write128Byte(this.sizeX);
      }

      if(this.sizeY != 1) {
         stream.writeByte(15);
         stream.writeByte(this.sizeY);
      }

      if(this.objectAnimation != -1) {
         stream.writeByte(24);
         stream.writeBigSmart(this.objectAnimation);
      }

      for(data = 0; data < this.options.length; ++data) {
         if(this.options[data] != null && this.options[data] != "Hidden") {
            stream.writeByte(30 + data);
            stream.writeString(this.options[data]);
         }
      }

      if(this.originalColors != null && this.modifiedColors != null) {
         stream.writeByte(40);
         stream.writeByte(this.originalColors.length);

         for(data = 0; data < this.originalColors.length; ++data) {
            stream.writeShort(this.originalColors[data]);
            stream.writeShort(this.modifiedColors[data]);
         }
      }

      if(this.clipType == 0 && this.projectileCliped) {
         stream.writeByte(17);
      }

      if(this.projectileCliped) {
         stream.writeByte(18);
      }

      if(this.clipType == 1 || this.clipType == 2) {
         stream.writeByte(27);
      }

      stream.writeByte(0);
      byte[] var61 = new byte[stream.getOffset()];
      stream.setOffset(0);
      stream.getBytes(var61, 0, var61.length);
      return var61;
   }
}
