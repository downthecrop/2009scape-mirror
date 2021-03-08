package com.alex.loaders.npcs;

import com.alex.io.InputStream;
import com.alex.io.OutputStream;
import com.alex.store.Store;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public final class NPCDefinitions implements Cloneable {
   private static final ConcurrentHashMap npcDefinitions = new ConcurrentHashMap();
   public boolean loaded;
   public int id;
   //public HashMap parameters;
   public int unknownInt13;
   public int unknownInt6;
   public int unknownInt15;
   public byte respawnDirection;
   public int size = 1;
   public int[][] unknownArray3;
   public boolean unknownBoolean2;
   public int unknownInt9;
   public int unknownInt4;
   public int[] unknownArray2;
   public int unknownInt7;
   public int renderEmote;
   public boolean unknownBoolean5 = false;
   public int unknownInt20;
   public byte unknownByte1;
   public boolean unknownBoolean3;
   public int unknownInt3;
   public byte unknownByte2;
   public boolean unknownBoolean6;
   public boolean unknownBoolean4;
   public int[] originalModelColors;
   public int combatLevel;
   public byte[] unknownArray1;
   public short unknownShort1;
   public boolean unknownBoolean1;
   public int npcHeight;
   public String name;
   public int[] modifiedTextureColors;
   public byte walkMask;
   public int[] modelIds;
   public int unknownInt1;
   public int unknownInt21;
   public int unknownInt11;
   public int unknownInt17;
   public int unknownInt14;
   public int unknownInt12;
   public int unknownInt8;
   public int headIcons;
   public int unknownInt19;
   public int[] originalTextureColors;
   public int[][] anIntArrayArray882;
   public int unknownInt10;
   public int[] unknownArray4;
   public int unknownInt5;
   public int unknownInt16;
   public boolean isVisibleOnMap;
   public int[] npcChatHeads;
   public short unknownShort2;
   public String[] options;
   public int[] modifiedModelColors;
   public int unknownInt2;
   public int npcWidth;
   public int npcId;
   public int unknownInt18;
   public boolean unknownBoolean7;
   public int[] unknownArray5;
   public HashMap<Integer, Object> clientScriptData;
   public int anInt833;
   public int anInt836;
   public int anInt837;
   public int[][] anIntArrayArray840;
   public boolean aBoolean841;
   public int anInt842;
   public int bConfig;
   public int[] transformTo;
   public int anInt846;
   public boolean aBoolean849 = false;
   public int anInt850;
   public byte aByte851;
   public boolean aBoolean852;
   public int anInt853;
   public byte aByte854;
   public boolean aBoolean856;
   public boolean aBoolean857;
   public short[] aShortArray859;
   public byte[] aByteArray861;
   public short aShort862;
   public boolean aBoolean863;
   public int anInt864;
   public short[] aShortArray866;
   public int anInt869;
   public int anInt870;
   public int anInt871;
   public int anInt872;
   public int anInt874;
   public int anInt875;
   public int anInt876;
   public int anInt879;
   public short[] aShortArray880;
   public int anInt884;
   public int[] anIntArray885;
   public int config;
   public int anInt889;
   public int[] anIntArray892;
   public short aShort894;
   public short[] aShortArray896;
   public int anInt897;
   public int anInt899;
   public int anInt901;
   public boolean aBoolean3190;
   private byte[] aByteArray1293;
   private byte[] aByteArray12930;
   private int[] anIntArray2930;

   public static final NPCDefinitions getNPCDefinitions(int id, Store store) {
      NPCDefinitions def = (NPCDefinitions)npcDefinitions.get(Integer.valueOf(id));
      if(def == null) {
         def = new NPCDefinitions(id);
         def.method694();
         byte[] data = store.getIndexes()[18].getFile(id >>> 134238215, id & 127);
         if(data != null) {
            def.readValueLoop(new InputStream(data));
         }

         npcDefinitions.put(Integer.valueOf(id), def);
      }

      return def;
   }

   public static NPCDefinitions getNPCDefinition(Store cache, int npcId) {
      return getNPCDefinition(cache, npcId, true);
   }

   public static NPCDefinitions getNPCDefinition(Store cache, int npcId, boolean load) {
      return new NPCDefinitions(cache, npcId, load);
   }

   public NPCDefinitions(Store cache, int id, boolean load) {
      this.id = id;
      this.setDefaultVariableValues();
      this.setDefaultOptions();
      if(load) {
         this.loadNPCDefinition(cache);
      }

   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   private void setDefaultOptions() {
      this.options = new String[]{"Talk-to", null, null, null, null};
   }

   private void setDefaultVariableValues() {
      this.name = "null";
      this.combatLevel = 0;
      this.isVisibleOnMap = true;
      this.renderEmote = -1;
      this.respawnDirection = 7;
      //this.size = 1;
      this.unknownInt9 = -1;
      this.unknownInt4 = -1;
      this.unknownInt15 = -1;
      this.unknownInt7 = -1;
      this.unknownInt3 = 32;
      this.unknownInt6 = -1;
      this.unknownInt1 = 0;
      this.walkMask = 0;
      this.unknownInt20 = 255;
      this.unknownInt11 = -1;
      this.unknownBoolean3 = true;
      this.unknownShort1 = 0;
      this.unknownInt8 = -1;
      this.unknownByte1 = -96;
      this.unknownInt12 = 0;
      this.unknownInt17 = -1;
      this.unknownBoolean4 = true;
      this.unknownInt21 = -1;
      this.unknownInt14 = -1;
      this.unknownInt13 = -1;
      this.npcHeight = 128;
      this.headIcons = -1;
      this.unknownBoolean6 = false;
      this.unknownInt5 = -1;
      this.unknownByte2 = -16;
      this.unknownBoolean1 = false;
      this.unknownInt16 = -1;
      this.unknownInt10 = -1;
      this.unknownBoolean2 = true;
      this.unknownInt19 = -1;
      this.npcWidth = 128;
      this.unknownShort2 = 0;
      this.unknownInt2 = 0;
      this.unknownInt18 = -1;
   }

   private void loadNPCDefinition(Store cache) {
      byte[] data = cache.getIndexes()[18].getFile(this.getArchiveId(), this.getFileId());
      if(data != null) {
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

   public int getArchiveId() {
      return this.id >>> 134238215;
   }

   public int getFileId() {
      return 127 & this.id;
   }

   public void write(Store store) {
      store.getIndexes()[18].putFile(this.getArchiveId(), this.getFileId(), this.encode());
   }

   public void method694() {
      if(this.modelIds == null) {
         this.modelIds = new int[0];
      }

   }

   private void readValueLoop(InputStream stream) {
      while(true) {
         int opcode = stream.readUnsignedByte();
         if(opcode == 0) {
            return;
         }

         this.readValues2(stream, opcode);
      }
   }
   
   private void readValues2(InputStream stream, int opcode) {
		if (opcode != 1) {
			if (opcode == 2)
				name = stream.readString();
			else if ((opcode ^ 0xffffffff) != -13) {
				if (opcode >= 30 && (opcode ^ 0xffffffff) > -36) {
					options[opcode - 30] = stream.readString();
					if (options[-30 + opcode].equalsIgnoreCase("Hidden"))
						options[-30 + opcode] = null;
				} else if ((opcode ^ 0xffffffff) != -41) {
					if (opcode == 41) {
						int i = stream.readUnsignedByte();
						aShortArray880 = new short[i];
						aShortArray866 = new short[i];
						for (int i_54_ = 0; (i_54_ ^ 0xffffffff) > (i ^ 0xffffffff); i_54_++) {
							aShortArray880[i_54_] = (short) stream.readUnsignedShort();
							aShortArray866[i_54_] = (short) stream.readUnsignedShort();
						}
					} else if (opcode == 44) {
						int i_24_ = (short) stream.readUnsignedShort();
						int i_25_ = 0;
						for (int i_26_ = i_24_; i_26_ > 0; i_26_ >>= 1)
							i_25_++;
						// aByteArray12930 = new byte[i_25_];
						byte i_27_ = 0;
						for (int i_28_ = 0; i_28_ < i_25_; i_28_++) {
							if ((i_24_ & 1 << i_28_) > 0) {
								// aByteArray12930[i_28_] = i_27_;
								i_27_++;
							}
							// aByteArray12930[i_28_] = (byte) -1;
						}
					} else if (45 == opcode) {
						int i_29_ = (short) stream.readUnsignedShort();
						int i_30_ = 0;
						for (int i_31_ = i_29_; i_31_ > 0; i_31_ >>= 1)
							i_30_++;
						// aByteArray1293 = new byte[i_30_];
						byte i_32_ = 0;
						for (int i_33_ = 0; i_33_ < i_30_; i_33_++) {
							if ((i_29_ & 1 << i_33_) > 0) {
								// aByteArray1293[i_33_] = i_32_;
								i_32_++;
							}
							// aByteArray1293[i_33_] = (byte) -1;
						}
					} else if ((opcode ^ 0xffffffff) == -43) {
						int i = stream.readUnsignedByte();
						aByteArray861 = new byte[i];
						for (int i_55_ = 0; i > i_55_; i_55_++)
							aByteArray861[i_55_] = (byte) stream.readByte();
					} else if ((opcode ^ 0xffffffff) != -61) {
						if (opcode == 93)
							isVisibleOnMap = false;
						else if ((opcode ^ 0xffffffff) == -96)
							combatLevel = stream.readUnsignedShort();
						else if (opcode != 97) {
							if ((opcode ^ 0xffffffff) == -99)
								anInt899 = stream.readUnsignedShort();
							else if ((opcode ^ 0xffffffff) == -100)
								aBoolean863 = true;
							else if (opcode == 100)
								anInt869 = stream.readByte();
							else if ((opcode ^ 0xffffffff) == -102)
								anInt897 = stream.readByte() * 5;
							else if ((opcode ^ 0xffffffff) == -103)
								headIcons = stream.readUnsignedShort();
							else if (opcode != 103) {
								if (opcode == 106 || opcode == 118) {
									bConfig = stream.readUnsignedShort();
									if (bConfig == 65535)
										bConfig = -1;
									config = stream.readUnsignedShort();
									if (config == 65535)
										config = -1;
									int i = -1;
									if ((opcode ^ 0xffffffff) == -119) {
										i = stream.readUnsignedShort();
										if ((i ^ 0xffffffff) == -65536)
											i = -1;
									}
									int i_56_ = stream.readUnsignedByte();
									transformTo = new int[2 + i_56_];
									for (int i_57_ = 0; i_56_ >= i_57_; i_57_++) {
										transformTo[i_57_] = stream.readUnsignedShort();
										if (transformTo[i_57_] == 65535)
											transformTo[i_57_] = -1;
									}
									transformTo[i_56_ - -1] = i;
								} else if ((opcode ^ 0xffffffff) != -108) {
									if ((opcode ^ 0xffffffff) == -110)
										aBoolean852 = false;
									else if ((opcode ^ 0xffffffff) != -112) {
										if (opcode != 113) {
											if (opcode == 114) {
												aByte851 = (byte) (stream.readByte());
												aByte854 = (byte) (stream.readByte());
											} else if (opcode == 115) {
												stream.readUnsignedByte();
												stream.readUnsignedByte();
											} else if ((opcode ^ 0xffffffff) != -120) {
												if (opcode != 121) {
													if ((opcode ^ 0xffffffff) != -123) {
														if (opcode == 123)
															anInt846 = (stream.readUnsignedShort());
														else if (opcode != 125) {
															if (opcode == 127)
																renderEmote = (stream.readUnsignedShort());
															else if ((opcode ^ 0xffffffff) == -129)
																stream.readUnsignedByte();
															else if (opcode != 134) {
																if (opcode == 135) {
																	anInt833 = stream.readUnsignedByte();
																	anInt874 = stream.readUnsignedShort();
																} else if (opcode != 136) {
																	if (opcode != 137) {
																		if (opcode != 138) {
																			if ((opcode ^ 0xffffffff) != -140) {
																				if (opcode == 140)
																					anInt850 = stream.readUnsignedByte();
																				else if (opcode == 141)
																					aBoolean849 = true;
																				else if ((opcode ^ 0xffffffff) != -143) {
																					if (opcode == 143)
																						aBoolean856 = true;
																					else if ((opcode ^ 0xffffffff) <= -151 && opcode < 155) {
																						options[opcode - 150] = stream.readString();
																						if (options[opcode - 150].equalsIgnoreCase("Hidden"))
																							options[opcode + -150] = null;
																					} else if ((opcode ^ 0xffffffff) == -161) {
																						int i = stream.readUnsignedByte();
																						anIntArray885 = new int[i];
																						for (int i_58_ = 0; i > i_58_; i_58_++)
																							anIntArray885[i_58_] = stream.readUnsignedShort();

																						// all
																						// added
																						// after
																						// here
																					} else if (opcode == 155) {
																						int aByte821 = stream.readByte();
																						int aByte824 = stream.readByte();
																						int aByte843 = stream.readByte();
																						int aByte855 = stream.readByte();
																					} else if (opcode == 158) {
																						byte aByte833 = (byte) 1;
																					} else if (opcode == 159) {
																						byte aByte833 = (byte) 0;
																					} else if (opcode == 162) { // added
																						// opcode
																						// aBoolean3190
																						// =
																						// true;
																					} else if (opcode == 163) { // added
																						// opcode
																						int anInt864 = stream.readUnsignedByte();
																					} else if (opcode == 164) {
																						int anInt848 = stream.readUnsignedShort();
																						int anInt837 = stream.readUnsignedShort();
																					} else if (opcode == 165) {
																						int anInt847 = stream.readUnsignedByte();
																					} else if (opcode == 168) {
																						int anInt828 = stream.readUnsignedByte();
																					} else if (opcode >= 170 && opcode < 176) {
																						// if
																						// (null
																						// ==
																						// anIntArray2930)
																						// {
																						// anIntArray2930
																						// =
																						// new
																						// int[6];
																						// Arrays.fill(anIntArray2930,
																						// -1);
																						// }
																						int i_44_ = (short) stream.readUnsignedShort();
																						if (i_44_ == 65535)
																							i_44_ = -1;
																						// anIntArray2930[opcode
																						// -
																						// 170]
																						// =
																						// i_44_;
																					} else if (opcode == 12) {
																						size = stream.readUnsignedByte();
																					} else if (opcode == 249) {
																						int i = stream.readUnsignedByte();
																						if (clientScriptData == null) {
																							clientScriptData = new HashMap<Integer, Object>(i);
																						}
																						for (int i_60_ = 0; i > i_60_; i_60_++) {
																							boolean stringInstance = stream.readUnsignedByte() == 1;
																							int key = stream.read24BitInt();
																							Object value;
																							if (stringInstance)
																								value = stream.readString();
																							else
																								value = stream.readInt();
																							clientScriptData.put(key, value);
																						}
																					}
																				} else
																					anInt870 = stream.readUnsignedShort();
																			} else
																				anInt879 = stream.readBigSmart();
																		} else
																			anInt901 = stream.readBigSmart();
																	} else
																		anInt872 = stream.readUnsignedShort();
																} else {
																	anInt837 = stream.readUnsignedByte();
																	anInt889 = stream.readUnsignedShort();
																}
															} else {
																anInt876 = (stream.readUnsignedShort());
																if (anInt876 == 65535)
																	anInt876 = -1;
																anInt842 = (stream.readUnsignedShort());
																if (anInt842 == 65535)
																	anInt842 = -1;
																anInt884 = (stream.readUnsignedShort());
																if ((anInt884 ^ 0xffffffff) == -65536)
																	anInt884 = -1;
																anInt871 = (stream.readUnsignedShort());
																if ((anInt871 ^ 0xffffffff) == -65536)
																	anInt871 = -1;
																anInt875 = (stream.readUnsignedByte());
															}
														} else
															respawnDirection = (byte) (stream.readByte());
													} else
														anInt836 = (stream.readBigSmart());
												} else {
													anIntArrayArray840 = (new int[modelIds.length][]);
													int i = (stream.readUnsignedByte());
													for (int i_62_ = 0; ((i_62_ ^ 0xffffffff) > (i ^ 0xffffffff)); i_62_++) {
														int i_63_ = (stream.readUnsignedByte());
														int[] is = (anIntArrayArray840[i_63_] = (new int[3]));
														is[0] = (stream.readByte());
														is[1] = (stream.readByte());
														is[2] = (stream.readByte());
													}
												}
											} else
												walkMask = (byte) (stream.readByte());
										} else {
											aShort862 = (short) (stream.readUnsignedShort());
											aShort894 = (short) (stream.readUnsignedShort());
										}
									} else
										aBoolean857 = false;
								} else
									aBoolean841 = false;
							} else
								anInt853 = stream.readUnsignedShort();
						} else
							anInt864 = stream.readUnsignedShort();
					} else {
						int i = stream.readUnsignedByte();
						anIntArray892 = new int[i];
						for (int i_64_ = 0; (i_64_ ^ 0xffffffff) > (i ^ 0xffffffff); i_64_++)
							anIntArray892[i_64_] = stream.readBigSmart();
					}
				} else {
					int i = stream.readUnsignedByte();
					aShortArray859 = new short[i];
					aShortArray896 = new short[i];
					for (int i_65_ = 0; (i ^ 0xffffffff) < (i_65_ ^ 0xffffffff); i_65_++) {
						aShortArray896[i_65_] = (short) stream.readUnsignedShort();
						aShortArray859[i_65_] = (short) stream.readUnsignedShort();
					}
				}
			//} else
				//size = stream.readUnsignedByte();
		} else {
			int i = stream.readUnsignedByte();
			modelIds = new int[i];
			for (int i_66_ = 0; i_66_ < i; i_66_++) {
				modelIds[i_66_] = stream.readBigSmart();
				if ((modelIds[i_66_] ^ 0xffffffff) == -65536)
					modelIds[i_66_] = -1;
			}
		}
   }
	}

   private void readValues(InputStream stream, int opcode) {
      int i;
      int i_66_;
      if(opcode != 1) {
         if(opcode == 2) {
            this.name = stream.readString();
         } else if(~opcode != -13) {
            if(opcode >= 30 && ~opcode > -36) {
               this.options[opcode - 30] = stream.readString();
               if(this.options[-30 + opcode].equalsIgnoreCase("Hidden")) {
                  this.options[-30 + opcode] = null;
               }
            } else if(~opcode != -41) {
               if(opcode == 41) {
                  i = stream.readUnsignedByte();
                  this.aShortArray880 = new short[i];
                  this.aShortArray866 = new short[i];

                  for(i_66_ = 0; ~i_66_ > ~i; ++i_66_) {
                     this.aShortArray880[i_66_] = (short)stream.readUnsignedShort();
                     this.aShortArray866[i_66_] = (short)stream.readUnsignedShort();
                  }
               } else {
                  int i_63_;
                  int is;
                  short var8;
                  byte var9;
                  if(opcode == 44) {
                     var8 = (short)stream.readUnsignedShort();
                     i_66_ = 0;

                     for(i_63_ = var8; i_63_ > 0; i_63_ >>= 1) {
                        ++i_66_;
                     }

                     this.aByteArray12930 = new byte[i_66_];
                     var9 = 0;

                     for(is = 0; is < i_66_; ++is) {
                        if((var8 & 1 << is) > 0) {
                           this.aByteArray12930[is] = var9++;
                        } else {
                           this.aByteArray12930[is] = -1;
                        }
                     }
                  } else if(45 == opcode) {
                     var8 = (short)stream.readUnsignedShort();
                     i_66_ = 0;

                     for(i_63_ = var8; i_63_ > 0; i_63_ >>= 1) {
                        ++i_66_;
                     }

                     this.aByteArray1293 = new byte[i_66_];
                     var9 = 0;

                     for(is = 0; is < i_66_; ++is) {
                        if((var8 & 1 << is) > 0) {
                           this.aByteArray1293[is] = var9++;
                        } else {
                           this.aByteArray1293[is] = -1;
                        }
                     }
                  } else if(~opcode == -43) {
                     i = stream.readUnsignedByte();
                     this.aByteArray861 = new byte[i];

                     for(i_66_ = 0; i > i_66_; ++i_66_) {
                        this.aByteArray861[i_66_] = (byte)stream.readByte();
                     }
                  } else if(~opcode != -61) {
                     if(opcode == 93) {
                        this.isVisibleOnMap = false;
                     } else if(~opcode == -96) {
                        this.combatLevel = stream.readUnsignedShort();
                     } else if(opcode != 97) {
                        if(~opcode == -99) {
                           this.anInt899 = stream.readUnsignedShort();
                        } else if(~opcode == -100) {
                           this.aBoolean863 = true;
                        } else if(opcode == 100) {
                           this.anInt869 = stream.readByte();
                        } else if(~opcode == -102) {
                           this.anInt897 = stream.readByte() * 5;
                        } else if(~opcode == -103) {
                           this.headIcons = stream.readUnsignedShort();
                        } else if(opcode != 103) {
                           if(opcode != 106 && opcode != 118) {
                              if(~opcode != -108) {
                                 if(~opcode == -110) {
                                    this.aBoolean852 = false;
                                 } else if(~opcode != -112) {
                                    if(opcode != 113) {
                                       if(opcode == 114) {
                                          this.aByte851 = (byte)stream.readByte();
                                          this.aByte854 = (byte)stream.readByte();
                                       } else if(opcode == 115) {
                                          stream.readUnsignedByte();
                                          stream.readUnsignedByte();
                                       } else if(~opcode != -120) {
                                          if(opcode != 121) {
                                             if(~opcode != -123) {
                                                if(opcode == 123) {
                                                   this.anInt846 = stream.readUnsignedShort();
                                                } else if(opcode != 125) {
                                                   if(opcode == 127) {
                                                      this.renderEmote = stream.readUnsignedShort();
                                                   } else if(~opcode == -129) {
                                                      stream.readUnsignedByte();
                                                   } else if(opcode != 134) {
                                                      if(opcode == 135) {
                                                         this.anInt833 = stream.readUnsignedByte();
                                                         this.anInt874 = stream.readUnsignedShort();
                                                      } else if(opcode != 136) {
                                                         if(opcode != 137) {
                                                            if(opcode != 138) {
                                                               if(~opcode != -140) {
                                                                  if(opcode == 140) {
                                                                     this.anInt850 = stream.readUnsignedByte();
                                                                  } else if(opcode == 141) {
                                                                     this.aBoolean849 = true;
                                                                  } else if(~opcode != -143) {
                                                                     if(opcode == 143) {
                                                                        this.aBoolean856 = true;
                                                                     } else if(~opcode <= -151 && opcode < 155) {
                                                                        this.options[opcode - 150] = stream.readString();
                                                                        if(this.options[opcode - 150].equalsIgnoreCase("Hidden")) {
                                                                           this.options[opcode + -150] = null;
                                                                        }
                                                                     } else if(~opcode == -161) {
                                                                        i = stream.readUnsignedByte();
                                                                        this.anIntArray885 = new int[i];

                                                                        for(i_66_ = 0; i > i_66_; ++i_66_) {
                                                                           this.anIntArray885[i_66_] = stream.readUnsignedShort();
                                                                        }
                                                                     } else if(opcode == 155) {
                                                                        i = stream.readByte();
                                                                        i_66_ = stream.readByte();
                                                                        i_63_ = stream.readByte();
                                                                        is = stream.readByte();
                                                                     } else {
                                                                        boolean var10;
                                                                        if(opcode == 158) {
                                                                           var10 = true;
                                                                        } else if(opcode == 159) {
                                                                           var10 = false;
                                                                        } else if(opcode == 162) {
                                                                           this.aBoolean3190 = true;
                                                                        } else if(opcode == 163) {
                                                                           i = stream.readUnsignedByte();
                                                                        } else if(opcode == 164) {
                                                                           i = stream.readUnsignedShort();
                                                                           i_66_ = stream.readUnsignedShort();
                                                                        } else if(opcode == 165) {
                                                                           i = stream.readUnsignedByte();
                                                                        } else if(opcode == 168) {
                                                                           i = stream.readUnsignedByte();
                                                                        } else if(opcode >= 170 && opcode < 176) {
                                                                           if(this.anIntArray2930 == null) {
                                                                              this.anIntArray2930 = new int[6];
                                                                              Arrays.fill(this.anIntArray2930, -1);
                                                                           }

                                                                           var8 = (short)stream.readUnsignedShort();
                                                                           if(var8 == '\uffff') {
                                                                              var8 = -1;
                                                                           }

                                                                           this.anIntArray2930[opcode - 170] = var8;
                                                                        } else if(opcode == 249) {
                                                                           i = stream.readUnsignedByte();
                                                                           if(this.clientScriptData == null) {
                                                                              this.clientScriptData = new HashMap(i);
                                                                           }

                                                                           for(i_66_ = 0; i > i_66_; ++i_66_) {
                                                                              boolean var12 = stream.readUnsignedByte() == 1;
                                                                              is = stream.read24BitInt();
                                                                              Object value;
                                                                              if(var12) {
                                                                                 value = stream.readString();
                                                                              } else {
                                                                                 value = Integer.valueOf(stream.readInt());
                                                                              }

                                                                              this.clientScriptData.put(Integer.valueOf(is), value);
                                                                           }
                                                                        }
                                                                     }
                                                                  } else {
                                                                     this.anInt870 = stream.readUnsignedShort();
                                                                  }
                                                               } else {
                                                                  this.anInt879 = stream.readBigSmart();
                                                               }
                                                            } else {
                                                               this.anInt901 = stream.readBigSmart();
                                                            }
                                                         } else {
                                                            this.anInt872 = stream.readUnsignedShort();
                                                         }
                                                      } else {
                                                         this.anInt837 = stream.readUnsignedByte();
                                                         this.anInt889 = stream.readUnsignedShort();
                                                      }
                                                   } else {
                                                      this.anInt876 = stream.readUnsignedShort();
                                                      if(this.anInt876 == '\uffff') {
                                                         this.anInt876 = -1;
                                                      }

                                                      this.anInt842 = stream.readUnsignedShort();
                                                      if(this.anInt842 == '\uffff') {
                                                         this.anInt842 = -1;
                                                      }

                                                      this.anInt884 = stream.readUnsignedShort();
                                                      if(~this.anInt884 == -65536) {
                                                         this.anInt884 = -1;
                                                      }

                                                      this.anInt871 = stream.readUnsignedShort();
                                                      if(~this.anInt871 == -65536) {
                                                         this.anInt871 = -1;
                                                      }

                                                      this.anInt875 = stream.readUnsignedByte();
                                                   }
                                                } else {
                                                   this.respawnDirection = (byte)stream.readByte();
                                                }
                                             } else {
                                                this.anInt836 = stream.readBigSmart();
                                             }
                                          } else {
                                             this.anIntArrayArray840 = new int[this.modelIds.length][];
                                             i = stream.readUnsignedByte();

                                             for(i_66_ = 0; ~i_66_ > ~i; ++i_66_) {
                                                i_63_ = stream.readUnsignedByte();
                                                int[] var11 = this.anIntArrayArray840[i_63_] = new int[3];
                                                var11[0] = stream.readByte();
                                                var11[1] = stream.readByte();
                                                var11[2] = stream.readByte();
                                             }
                                          }
                                       } else {
                                          this.walkMask = (byte)stream.readByte();
                                       }
                                    } else {
                                       this.aShort862 = (short)stream.readUnsignedShort();
                                       this.aShort894 = (short)stream.readUnsignedShort();
                                    }
                                 } else {
                                    this.aBoolean857 = false;
                                 }
                              } else {
                                 this.aBoolean841 = false;
                              }
                           } else {
                              this.bConfig = stream.readUnsignedShort();
                              if(this.bConfig == '\uffff') {
                                 this.bConfig = -1;
                              }

                              this.config = stream.readUnsignedShort();
                              if(this.config == '\uffff') {
                                 this.config = -1;
                              }

                              i = -1;
                              if(~opcode == -119) {
                                 i = stream.readUnsignedShort();
                                 if(~i == -65536) {
                                    i = -1;
                                 }
                              }

                              i_66_ = stream.readUnsignedByte();
                              this.transformTo = new int[2 + i_66_];

                              for(i_63_ = 0; i_66_ >= i_63_; ++i_63_) {
                                 this.transformTo[i_63_] = stream.readUnsignedShort();
                                 if(this.transformTo[i_63_] == '\uffff') {
                                    this.transformTo[i_63_] = -1;
                                 }
                              }

                              this.transformTo[i_66_ - -1] = i;
                           }
                        } else {
                           this.anInt853 = stream.readUnsignedShort();
                        }
                     } else {
                        this.anInt864 = stream.readUnsignedShort();
                     }
                  } else {
                     i = stream.readUnsignedByte();
                     this.anIntArray892 = new int[i];

                     for(i_66_ = 0; ~i_66_ > ~i; ++i_66_) {
                        this.anIntArray892[i_66_] = stream.readBigSmart();
                     }
                  }
               }
            } else {
               i = stream.readUnsignedByte();
               this.aShortArray859 = new short[i];
               this.aShortArray896 = new short[i];

               for(i_66_ = 0; ~i < ~i_66_; ++i_66_) {
                  this.aShortArray896[i_66_] = (short)stream.readUnsignedShort();
                  this.aShortArray859[i_66_] = (short)stream.readUnsignedShort();
               }
            }
			} else {
            this.size = stream.readUnsignedByte();
         }
      } else {
		  int i1 = stream.readUnsignedByte();
			modelIds = new int[i1];
			for (int i_69_ = 0; i_69_ < i1; i_69_++) {
				modelIds[i_69_] = stream.readUnsignedShort();
				if ((modelIds[i_69_] ^ 0xffffffff) == -65536)
					modelIds[i_69_] = -1;
			}
      }

   }

   public static final void clearNPCDefinitions() {
      npcDefinitions.clear();
   }

   public NPCDefinitions(int id) {
      this.id = id;
      this.anInt842 = -1;
      this.bConfig = -1;
      this.anInt837 = -1;
      this.anInt846 = -1;
      this.anInt853 = 32;
      this.combatLevel = -1;
      this.anInt836 = -1;
      this.name = "null";
      this.anInt869 = 0;
      this.walkMask = 0;
      this.anInt850 = 255;
      this.anInt871 = -1;
      this.aBoolean852 = true;
      this.aShort862 = 0;
      this.anInt876 = -1;
      this.aByte851 = -96;
      this.anInt875 = 0;
      this.anInt872 = -1;
      this.renderEmote = -1;
      this.respawnDirection = 7;
      this.aBoolean857 = true;
      this.anInt870 = -1;
      this.anInt874 = -1;
      this.anInt833 = -1;
      this.anInt864 = 128;
      this.headIcons = -1;
      this.aBoolean856 = false;
      this.config = -1;
      this.aByte854 = -16;
      this.aBoolean863 = false;
      this.isVisibleOnMap = true;
      this.anInt889 = -1;
      this.anInt884 = -1;
      this.aBoolean841 = true;
      this.anInt879 = -1;
      this.anInt899 = 128;
      this.aShort894 = 0;
      this.options = new String[5];
      this.anInt897 = 0;
      this.anInt901 = -1;
   }

   public String toString() {
      return this.id + " - " + this.name;
   }

   public boolean hasMarkOption() {
      String[] var4 = this.options;
      int var3 = this.options.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         String option = var4[var2];
         if(option != null && option.equalsIgnoreCase("mark")) {
            return true;
         }
      }

      return false;
   }

   public boolean hasOption(String op) {
      String[] var5 = this.options;
      int var4 = this.options.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         String option = var5[var3];
         if(option != null && option.equalsIgnoreCase(op)) {
            return true;
         }
      }

      return false;
   }

   public byte getRespawnDirection() {
      return this.respawnDirection;
   }

   public void setRespawnDirection(byte respawnDirection) {
      this.respawnDirection = respawnDirection;
   }

   public int getSize() {
      return this.size;
   }

   public void setSize(int size) {
      this.size = size;
   }

   public int getRenderEmote() {
      return this.renderEmote;
   }

   public void setRenderEmote(int renderEmote) {
      this.renderEmote = renderEmote;
   }

   public boolean isVisibleOnMap() {
      return this.isVisibleOnMap;
   }

   public void setVisibleOnMap(boolean isVisibleOnMap) {
      this.isVisibleOnMap = isVisibleOnMap;
   }

   public String[] getOptions() {
      return this.options;
   }

   public void setOptions(String[] options) {
      this.options = options;
   }

   public int getNpcId() {
      return this.npcId;
   }

   public void setNpcId(int npcId) {
      this.npcId = npcId;
   }

   public boolean hasAttackOption() {
      if(this.id == 14899) {
         return true;
      } else {
         String[] var4 = this.options;
         int var3 = this.options.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            String option = var4[var2];
            if(option != null && option.equalsIgnoreCase("attack")) {
               return true;
            }
         }

         return false;
      }
   }

   public byte[] encode() {
      OutputStream stream = new OutputStream();
      stream.writeByte(1);
      stream.writeByte(this.modelIds.length);

      int data;
      for(data = 0; data < this.modelIds.length; ++data) {
         stream.writeBigSmart(this.modelIds[data]);
      }

      if(!this.name.equals("null")) {
         stream.writeByte(2);
         stream.writeString(this.name);
      }

      //if(this.size != 1) {
      if(this.size > 1) {
         stream.writeByte(12);
         stream.writeByte(this.size);
      }

      for(data = 0; data < this.options.length; ++data) {
         if(this.options[data] != null && this.options[data] != "Hidden") {
            stream.writeByte(30 + data);
            stream.writeString(this.options[data]);
         }
      }

      if(this.originalModelColors != null && this.modifiedModelColors != null) {
         stream.writeByte(40);
         stream.writeByte(this.originalModelColors.length);

         for(data = 0; data < this.originalModelColors.length; ++data) {
            stream.writeShort(this.originalModelColors[data]);
            stream.writeShort(this.modifiedModelColors[data]);
         }
      }

      if(this.originalTextureColors != null && this.modifiedTextureColors != null) {
         stream.writeByte(41);
         stream.writeByte(this.originalTextureColors.length);

         for(data = 0; data < this.originalTextureColors.length; ++data) {
            stream.writeShort(this.originalTextureColors[data]);
            stream.writeShort(this.modifiedTextureColors[data]);
         }
      }

      if(this.unknownArray1 != null) {
         stream.writeByte(42);
         stream.writeByte(this.unknownArray1.length);

         for(data = 0; data < this.unknownArray1.length; ++data) {
            stream.writeByte(this.unknownArray1[data]);
         }
      }

      if(this.npcChatHeads != null) {
         stream.writeByte(60);
         stream.writeByte(this.npcChatHeads.length);

         for(data = 0; data < this.npcChatHeads.length; ++data) {
            stream.writeBigSmart(this.npcChatHeads[data]);
         }
      }

      if(!this.isVisibleOnMap) {
         stream.writeByte(93);
      }

      //if(this.combatLevel != 0) {
      if(this.combatLevel > -1) {
         stream.writeByte(95);
         stream.writeShort(this.combatLevel);
      }

      if(this.npcHeight != 0) {
         stream.writeByte(97);
         stream.writeShort(this.npcHeight);
      }

      if(this.npcWidth != 0) {
         stream.writeByte(98);
         stream.writeShort(this.npcWidth);
      }

      if(this.unknownBoolean1) {
         stream.writeByte(99);
      }

      if(this.unknownInt1 != 0) {
         stream.writeByte(100);
         stream.writeByte(this.unknownInt1);
      }

      if(this.unknownInt2 != 0) {
         stream.writeByte(101);
         stream.writeByte(this.unknownInt2 / 5);
      }

      if(this.headIcons != 0) {
         stream.writeByte(102);
         stream.writeShort(this.headIcons);
      }

      if(this.walkMask != -1) {
         stream.writeByte(119);
         stream.writeByte(this.walkMask);
      }

      if(this.respawnDirection != 7) {
         stream.writeByte(125);
         stream.writeByte(this.respawnDirection);
      }

      if(this.renderEmote != -1) {
         stream.writeByte(127);
         stream.writeShort(this.renderEmote);
      }

      if(this.clientScriptData != null) {
         stream.writeByte(249);
         stream.writeByte(this.clientScriptData.size());
         Iterator var6 = this.clientScriptData.keySet().iterator();

         while(var6.hasNext()) {
            int key = ((Integer)var6.next()).intValue();
            Object value = this.clientScriptData.get(Integer.valueOf(key));
            stream.writeByte(value instanceof String?1:0);
            stream.write24BitInt(key);
            if(value instanceof String) {
               stream.writeString((String)value);
            } else {
               stream.writeInt(((Integer)value).intValue());
            }
         }
      }

      stream.writeByte(0);
      byte[] var61 = new byte[stream.getOffset()];
      stream.setOffset(0);
      stream.getBytes(var61, 0, var61.length);
      return var61;
   }

   public int getCombatLevel() {
      return this.combatLevel;
   }

   public void setCombatLevel(int combatLevel) {
      this.combatLevel = combatLevel;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public void resetTextureColors() {
      this.originalTextureColors = null;
      this.modifiedTextureColors = null;
   }

   public void changeTextureColor(int originalModelColor, int modifiedModelColor) {
      if(this.originalTextureColors != null) {
         for(int var5 = 0; var5 < this.originalTextureColors.length; ++var5) {
            if(this.originalTextureColors[var5] == originalModelColor) {
               this.modifiedTextureColors[var5] = modifiedModelColor;
               return;
            }
         }

         int[] var51 = Arrays.copyOf(this.originalTextureColors, this.originalTextureColors.length + 1);
         int[] newModifiedModelColors = Arrays.copyOf(this.modifiedTextureColors, this.modifiedTextureColors.length + 1);
         var51[var51.length - 1] = originalModelColor;
         newModifiedModelColors[newModifiedModelColors.length - 1] = modifiedModelColor;
         this.originalTextureColors = var51;
         this.modifiedTextureColors = newModifiedModelColors;
      } else {
         this.originalTextureColors = new int[]{originalModelColor};
         this.modifiedTextureColors = new int[]{modifiedModelColor};
      }

   }

   public void resetModelColors() {
      this.originalModelColors = null;
      this.modifiedModelColors = null;
   }

   public void changeModelColor(int originalModelColor, int modifiedModelColor) {
      if(this.originalModelColors != null) {
         for(int var5 = 0; var5 < this.originalModelColors.length; ++var5) {
            if(this.originalModelColors[var5] == originalModelColor) {
               this.modifiedModelColors[var5] = modifiedModelColor;
               return;
            }
         }

         int[] var51 = Arrays.copyOf(this.originalModelColors, this.originalModelColors.length + 1);
         int[] newModifiedModelColors = Arrays.copyOf(this.modifiedModelColors, this.modifiedModelColors.length + 1);
         var51[var51.length - 1] = originalModelColor;
         newModifiedModelColors[newModifiedModelColors.length - 1] = modifiedModelColor;
         this.originalModelColors = var51;
         this.modifiedModelColors = newModifiedModelColors;
      } else {
         this.originalModelColors = new int[]{originalModelColor};
         this.modifiedModelColors = new int[]{modifiedModelColor};
      }

   }
}
