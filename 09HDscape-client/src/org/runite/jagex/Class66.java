package org.runite.jagex;
import java.io.IOException;

final class Class66 {

	private NodeList highPriorityRequests = new NodeList();
	static int anInt994;
	static RSString aClass94_995 = RSString.createRSString("(Y<)4col>");
	static Class3_Sub28_Sub16[] aClass3_Sub28_Sub16Array996;
	static int maskUpdateCount = 0;
	static int anInt998 = 0;
	static int anInt999 = -1;
	private NodeList aClass13_1000 = new NodeList();
	private NodeList lowPriorityRequests = new NodeList();
	static int wlPacketSize = 0;
	private NodeList aClass13_1003 = new NodeList();
	private long aLong1004;
	private IOHandler aClass89_1005;
	private int anInt1006;
	private RSByteBuffer aClass3_Sub30_1007 = new RSByteBuffer(4);
	private RSByteBuffer aClass3_Sub30_1008 = new RSByteBuffer(8);
	private byte aByte1009 = 0;
	volatile int anInt1010 = 0;
	volatile int anInt1011 = 0;
	private Class3_Sub28_Sub10_Sub2 aClass3_Sub28_Sub10_Sub2_1012;


	final boolean method1241(int var1) {
		try {
			if(var1 != -30064) {
				this.aClass13_1000 = (NodeList)null;
			}

			return 20 <= this.method1246(11706);
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "jb.N(" + var1 + ')');
		}
	}

	public static void method1242(byte var0) {
		try {
			aClass3_Sub28_Sub16Array996 = null;
			if(var0 != -88) {
				method1250(-72, true);
			}

			aClass94_995 = null;
		} catch (RuntimeException var2) {
			throw Class44.method1067(var2, "jb.G(" + var0 + ')');
		}
	}

	final boolean method1243(byte var1) {
		try {
			int var4;
			if(null != this.aClass89_1005) {
				long var2 = Class5.method830((byte)-55);
				var4 = (int)(-this.aLong1004 + var2);
				this.aLong1004 = var2;
				if(var4 > 200) {
					var4 = 200;
				}

				this.anInt1006 += var4;
				if(30000 < this.anInt1006) {
					try {
						this.aClass89_1005.close(14821);
					} catch (Exception var18) {
						;
					}

					this.aClass89_1005 = null;
				}
			}

			if(this.aClass89_1005 == null) {
				return 0 == this.method1253(4) && -1 == ~this.method1246(11706);
			} else {
				try {
					this.aClass89_1005.method1466(127);

					Class3_Sub28_Sub10_Sub2 var21;
					for(var21 = (Class3_Sub28_Sub10_Sub2)this.highPriorityRequests.method876((byte)125); null != var21; var21 = (Class3_Sub28_Sub10_Sub2)this.highPriorityRequests.method878(119)) {
						this.aClass3_Sub30_1007.index = 0;
						this.aClass3_Sub30_1007.putByte((byte)-26, 1); //High priority JS5 request
						this.aClass3_Sub30_1007.putTriByte((int)var21.aLong2569, 6517);
						
						this.aClass89_1005.sendBytes(false, 0, this.aClass3_Sub30_1007.buffer, 4);
						this.aClass13_1000.method879(var21, (byte)-125);
					}

					for(var21 = (Class3_Sub28_Sub10_Sub2)this.lowPriorityRequests.method876((byte)51); var21 != null; var21 = (Class3_Sub28_Sub10_Sub2)this.lowPriorityRequests.method878(-53)) {
						this.aClass3_Sub30_1007.index = 0;
						this.aClass3_Sub30_1007.putByte((byte)-22, 0); //Low priority JS5 request
						this.aClass3_Sub30_1007.putTriByte((int)var21.aLong2569, 6517);
						this.aClass89_1005.sendBytes(false, 0, this.aClass3_Sub30_1007.buffer, 4);
						this.aClass13_1003.method879(var21, (byte)-128);
					}

					int var22 = 100 % ((33 - var1) / 54);

					for(int var3 = 0; 100 > var3; ++var3) {
						var4 = this.aClass89_1005.availableBytes(-18358);
						if(var4 < 0) {
							throw new IOException();
						}

						if(-1 == ~var4) {
							break;
						}

						this.anInt1006 = 0;
						byte var5 = 0;
						if(null != this.aClass3_Sub28_Sub10_Sub2_1012) {
							if(~this.aClass3_Sub28_Sub10_Sub2_1012.anInt4067 == -1) {
								var5 = 1;
							}
						} else {
							var5 = 8;
						}

						int var6;
						int var7;
						int var8;
						if(0 >= var5) {
							var6 = -this.aClass3_Sub28_Sub10_Sub2_1012.aByte4064 + this.aClass3_Sub28_Sub10_Sub2_1012.aClass3_Sub30_4069.buffer.length;
							var7 = -this.aClass3_Sub28_Sub10_Sub2_1012.anInt4067 + 512;
							if(var7 > -this.aClass3_Sub28_Sub10_Sub2_1012.aClass3_Sub30_4069.index + var6) {
								var7 = -this.aClass3_Sub28_Sub10_Sub2_1012.aClass3_Sub30_4069.index + var6;
							}

							if(~var7 < ~var4) {
								var7 = var4;
							}

							this.aClass89_1005.readBytes(this.aClass3_Sub28_Sub10_Sub2_1012.aClass3_Sub30_4069.index, var7, -18455, this.aClass3_Sub28_Sub10_Sub2_1012.aClass3_Sub30_4069.buffer);
							if(-1 != ~this.aByte1009) {
								for(var8 = 0; ~var7 < ~var8; ++var8) {
									this.aClass3_Sub28_Sub10_Sub2_1012.aClass3_Sub30_4069.buffer[this.aClass3_Sub28_Sub10_Sub2_1012.aClass3_Sub30_4069.index - -var8] = (byte)Class93.method1519(this.aClass3_Sub28_Sub10_Sub2_1012.aClass3_Sub30_4069.buffer[this.aClass3_Sub28_Sub10_Sub2_1012.aClass3_Sub30_4069.index + var8], this.aByte1009);
								}
							}

							this.aClass3_Sub28_Sub10_Sub2_1012.anInt4067 += var7;
							this.aClass3_Sub28_Sub10_Sub2_1012.aClass3_Sub30_4069.index += var7;
							if(~this.aClass3_Sub28_Sub10_Sub2_1012.aClass3_Sub30_4069.index != ~var6) {
								if(~this.aClass3_Sub28_Sub10_Sub2_1012.anInt4067 == -513) {
									this.aClass3_Sub28_Sub10_Sub2_1012.anInt4067 = 0;
								}
							} else {
								this.aClass3_Sub28_Sub10_Sub2_1012.method524((byte)-107);
								this.aClass3_Sub28_Sub10_Sub2_1012.aBoolean3632 = false;
								this.aClass3_Sub28_Sub10_Sub2_1012 = null;
							}
						} else {
							var6 = -this.aClass3_Sub30_1008.index + var5;
							if(var4 < var6) {
								var6 = var4;
							}

							this.aClass89_1005.readBytes(this.aClass3_Sub30_1008.index, var6, -18455, this.aClass3_Sub30_1008.buffer);
							if(0 != this.aByte1009) {
								for(var7 = 0; var7 < var6; ++var7) {
									this.aClass3_Sub30_1008.buffer[var7 + this.aClass3_Sub30_1008.index] = (byte)Class93.method1519(this.aClass3_Sub30_1008.buffer[var7 + this.aClass3_Sub30_1008.index], this.aByte1009);
								}
							}

							this.aClass3_Sub30_1008.index += var6;
							if(~this.aClass3_Sub30_1008.index <= ~var5) {
								if(this.aClass3_Sub28_Sub10_Sub2_1012 != null) {
									if(this.aClass3_Sub28_Sub10_Sub2_1012.anInt4067 != 0) {
										throw new IOException();
									}

									if(~this.aClass3_Sub30_1008.buffer[0] != 0) {
										this.aClass3_Sub28_Sub10_Sub2_1012 = null;
									} else {
										this.aClass3_Sub28_Sub10_Sub2_1012.anInt4067 = 1;
										this.aClass3_Sub30_1008.index = 0;
									}
								} else {
									this.aClass3_Sub30_1008.index = 0;
									var7 = this.aClass3_Sub30_1008.getByte((byte)-34);
									var8 = this.aClass3_Sub30_1008.getShort(1);
									int var9 = this.aClass3_Sub30_1008.getByte((byte)-26);
									int var10 = this.aClass3_Sub30_1008.getInt();
									int var11 = 127 & var9;
									boolean var12 = -1 != ~(var9 & 128);
									Class3_Sub28_Sub10_Sub2 var15 = null;
									long var13 = (long)((var7 << 16) - -var8);
									if(!var12) {
										for(var15 = (Class3_Sub28_Sub10_Sub2)this.aClass13_1000.method876((byte)65); var15 != null && ~var13 != ~var15.aLong2569; var15 = (Class3_Sub28_Sub10_Sub2)this.aClass13_1000.method878(-15)) {
											;
										}
									} else {
										for(var15 = (Class3_Sub28_Sub10_Sub2)this.aClass13_1003.method876((byte)76); null != var15 && ~var15.aLong2569 != ~var13; var15 = (Class3_Sub28_Sub10_Sub2)this.aClass13_1003.method878(122)) {
											;
										}
									}

									if(null == var15) {
										throw new IOException("Could not find cache file " + var7 + ", " + var8 + "!");
									}

									int var16 = var11 != 0?9:5;
									this.aClass3_Sub28_Sub10_Sub2_1012 = var15;
									this.aClass3_Sub28_Sub10_Sub2_1012.aClass3_Sub30_4069 = new RSByteBuffer(var10 - (-var16 - this.aClass3_Sub28_Sub10_Sub2_1012.aByte4064));
									this.aClass3_Sub28_Sub10_Sub2_1012.aClass3_Sub30_4069.putByte((byte)-127, var11);
									this.aClass3_Sub28_Sub10_Sub2_1012.aClass3_Sub30_4069.putInt(-125, var10);
									this.aClass3_Sub28_Sub10_Sub2_1012.anInt4067 = 8;
									this.aClass3_Sub30_1008.index = 0;
								}
							}
						}
					}

					return true;
				} catch (IOException var19) {
					var19.printStackTrace();
					try {
						this.aClass89_1005.close(14821);
					} catch (Exception var17) {
						;
					}

					this.anInt1010 = -2;
					++this.anInt1011;
					this.aClass89_1005 = null;
					return 0 == this.method1253(4) && ~this.method1246(11706) == -1;
				}
			}
		} catch (RuntimeException var20) {
			throw Class44.method1067(var20, "jb.H(" + var1 + ')');
		}
	}

	final void method1244(boolean var1) {
		try {
			if(this.aClass89_1005 != null) {
				try {
					this.aClass3_Sub30_1007.index = 0;
					if(!var1) {
						this.method1256((byte)21);
					}

					this.aClass3_Sub30_1007.putByte((byte)-48, 7);
					this.aClass3_Sub30_1007.putTriByte(0, 6517);
					this.aClass89_1005.sendBytes(false, 0, this.aClass3_Sub30_1007.buffer, 4);
				} catch (IOException var5) {
					var5.printStackTrace();
					try {
						this.aClass89_1005.close(14821);
					} catch (Exception var4) {
						var4.printStackTrace();
					}

					++this.anInt1011;
					this.anInt1010 = -2;
					this.aClass89_1005 = null;
				}

			}
		} catch (RuntimeException var6) {
			throw Class44.method1067(var6, "jb.O(" + var1 + ')');
		}
	}

	static final Class3_Sub15 method1245(int var0, CacheIndex var1, int var2) {
		try {
			if(var0 <= 12) {
				wlPacketSize = 107;
			}

			byte[] var3 = var1.method2138(var2, 0);
			return var3 != null?new Class3_Sub15(var3):null;
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "jb.F(" + var0 + ',' + (var1 != null?"{...}":"null") + ',' + var2 + ')');
		}
	}

	private final int method1246(int var1) {
		try {
			if(var1 != 11706) {
				aClass3_Sub28_Sub16Array996 = (Class3_Sub28_Sub16[])null;
			}

			return this.lowPriorityRequests.method874(-79) - -this.aClass13_1003.method874(-118);
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "jb.K(" + var1 + ')');
		}
	}

	final void method1247(boolean var1, boolean var2) {
		try {
			if(var2) {
				if(null != this.aClass89_1005) {
					try {
						this.aClass3_Sub30_1007.index = 0;
						this.aClass3_Sub30_1007.putByte((byte)-27, var1?2:3);
						this.aClass3_Sub30_1007.putTriByte(0, 6517);
						this.aClass89_1005.sendBytes(false, 0, this.aClass3_Sub30_1007.buffer, 4);
					} catch (IOException var6) {
						var6.printStackTrace();
						try {
							this.aClass89_1005.close(14821);
						} catch (Exception var5) {
							;
						}

						++this.anInt1011;
						this.anInt1010 = -2;
						this.aClass89_1005 = null;
					}

				}
			}
		} catch (RuntimeException var7) {
			throw Class44.method1067(var7, "jb.B(" + var1 + ',' + var2 + ')');
		}
	}

	final void method1248(int var1) {
		try {
			if(var1 != -29340) {
				this.method1246(-28);
			}

			if(this.aClass89_1005 != null) {
				this.aClass89_1005.method1467(false);
			}

		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "jb.P(" + var1 + ')');
		}
	}

	final void method1249(boolean var1, IOHandler stream, int var3) {
		try {
			if(null != this.aClass89_1005) {
				try {
					this.aClass89_1005.close(var3 ^ 14821);
				} catch (Exception var8) {
					;
				}

				this.aClass89_1005 = null;
			}

			this.aClass89_1005 = stream;
			this.method1256((byte)-77);
			this.method1247(var1, true);
			this.aClass3_Sub30_1008.index = 0;
			this.aClass3_Sub28_Sub10_Sub2_1012 = null;

			while(true) {
				Class3_Sub28_Sub10_Sub2 var4 = (Class3_Sub28_Sub10_Sub2)this.aClass13_1000.method877(-1);
				if(null == var4) {
					while(true) {
						var4 = (Class3_Sub28_Sub10_Sub2)this.aClass13_1003.method877(-1);
						if(var4 == null) {
							if(this.aByte1009 != var3) {
								try {
									this.aClass3_Sub30_1007.index = 0;
									this.aClass3_Sub30_1007.putByte((byte)-52, 4);
									this.aClass3_Sub30_1007.putByte((byte)-24, this.aByte1009);
									this.aClass3_Sub30_1007.putShort(0);
									this.aClass89_1005.sendBytes(false, 0, this.aClass3_Sub30_1007.buffer, 4);
								} catch (IOException var7) {
					            	var7.printStackTrace();
									try {
										this.aClass89_1005.close(14821);
									} catch (Exception var6) {
										;
									}

									this.anInt1010 = -2;
									++this.anInt1011;
									this.aClass89_1005 = null;
								}
							}

							this.anInt1006 = 0;
							this.aLong1004 = Class5.method830((byte)-55);
							return;
						}

						this.lowPriorityRequests.method879(var4, (byte)95);
					}
				}

				this.highPriorityRequests.method879(var4, (byte)80);
			}
		} catch (RuntimeException var9) {
			throw Class44.method1067(var9, "jb.M(" + var1 + ',' + (stream != null?"{...}":"null") + ',' + var3 + ')');
		}
	}

	static final void method1250(int var0, boolean var1) {
		try {
			Class3_Sub10.aByteArrayArrayArray2339 = (byte[][][])null;
			Class44.anIntArrayArrayArray720 = (int[][][])null;
			if(var0 < 14) {
				method1250(10, true);
			}

			Class3_Sub28_Sub3.aClass11_3551 = null;
			RenderAnimationDefinition.aByteArrayArrayArray383 = (byte[][][])null;
			Class84.anIntArray1161 = null;
			CS2Script.aByteArrayArrayArray2452 = (byte[][][])null;
			if(var1 && null != RSByteBuffer.aClass3_Sub28_Sub3_2600) {
				Class3_Sub13_Sub19.aClass94_3220 = RSByteBuffer.aClass3_Sub28_Sub3_2600.aClass94_3561;
			} else {
				Class3_Sub13_Sub19.aClass94_3220 = null;
			}

			Class36.aByteArrayArrayArray640 = (byte[][][])null;
			Class3_Sub13_Sub33.aByteArrayArrayArray3390 = (byte[][][])null;
			Class29.anIntArrayArrayArray558 = (int[][][])null;
			Class146.anIntArrayArrayArray1903 = (int[][][])null;
			Class140_Sub3.anInt2737 = 0;
			RSByteBuffer.aClass3_Sub28_Sub3_2600 = null;
			Class84.aClass61_1162.method1211(-108);
			Class119.aClass131_1624 = null;
			Class3_Sub13_Sub30.anInt3362 = -1;
			Class75_Sub2.aClass33_2648 = null;
			Class91.aClass33_1305 = null;
			IOHandler.aClass33_1238 = null;
			Class161.aClass33_2034 = null;
			Class164_Sub2.aClass33_3019 = null;
			Class99.aClass33_1399 = null;
			Class75_Sub2.aClass33_2637 = null;
			Class119.aClass33_1626 = null;
			Class36.aClass3_Sub28_Sub16_637 = null;
			Class82.anInt1150 = -1;
			Class3_Sub13_Sub19.aClass3_Sub28_Sub16_Sub2_3221 = null;
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "jb.E(" + var0 + ',' + var1 + ')');
		}
	}

	final boolean method1251(byte var1) {
		try {
			int var2 = 33 % ((2 - var1) / 58);
			return 20 <= this.method1253(4);
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "jb.L(" + var1 + ')');
		}
	}

	final void method1252(byte var1) {
		try {
			int var2 = -116 / ((var1 - 45) / 51);

			try {
				this.aClass89_1005.close(14821);
			} catch (Exception var4) {
				;
			}

			this.anInt1010 = -1;
			this.aByte1009 = (byte)((int)(255.0D * Math.random() + 1.0D));
			this.aClass89_1005 = null;
			++this.anInt1011;
		} catch (RuntimeException var5) {
			throw Class44.method1067(var5, "jb.A(" + var1 + ')');
		}
	}

	final int method1253(int var1) {
		try {
			if(var1 != 4) {
				this.method1252((byte)-45);
			}

			return this.highPriorityRequests.method874(-127) - -this.aClass13_1000.method874(-108);
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "jb.C(" + var1 + ')');
		}
	}

	final void method1254(boolean var1) {
		try {
			if(this.aClass89_1005 != null) {
				this.aClass89_1005.close(14821);
			}

			if(var1) {
				this.method1247(true, false);
			}

		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "jb.D(" + var1 + ')');
		}
	}

	final Class3_Sub28_Sub10_Sub2 addJS5Request(int var1, int index, byte var3, int archive, boolean highPriority) {
		try {
			Class3_Sub28_Sub10_Sub2 var8 = new Class3_Sub28_Sub10_Sub2();
			long var6 = (long)(archive + (index << 16));
			var8.aBoolean3628 = highPriority;
			var8.aLong2569 = var6;
			var8.aByte4064 = var3;
			int var9 = 120 / ((63 - var1) / 47);
			if(highPriority) {
				if(~this.method1253(4) <= -21) {
					throw new RuntimeException();
				}

				this.highPriorityRequests.method879(var8, (byte)-125);
			} else {
				if(this.method1246(11706) >= 20) {
					throw new RuntimeException();
				}

				this.lowPriorityRequests.method879(var8, (byte)78);
			}

			return var8;
		} catch (RuntimeException var10) {
			throw Class44.method1067(var10, "jb.I(" + var1 + ',' + index + ',' + var3 + ',' + archive + ',' + highPriority + ')');
		}
	}

	private final void method1256(byte var1) {
		try {
			if(this.aClass89_1005 != null) {
				if(var1 == -77) {
					try {
						this.aClass3_Sub30_1007.index = 0;
						this.aClass3_Sub30_1007.putByte((byte)-125, 6);
						this.aClass3_Sub30_1007.putTriByte(3, 6517);
						this.aClass89_1005.sendBytes(false, 0, this.aClass3_Sub30_1007.buffer, 4);
					} catch (IOException var5) {
		            	var5.printStackTrace();
						try {
							this.aClass89_1005.close(14821);
						} catch (Exception var4) {
							;
						}

						++this.anInt1011;
						this.aClass89_1005 = null;
						this.anInt1010 = -2;
					}

				}
			}
		} catch (RuntimeException var6) {
			throw Class44.method1067(var6, "jb.J(" + var1 + ')');
		}
	}

}
