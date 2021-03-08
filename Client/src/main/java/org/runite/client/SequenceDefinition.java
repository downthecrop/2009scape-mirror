package org.runite.client;


import org.rs09.client.data.HashTable;

public final class SequenceDefinition {

	int delayType = 2;
	boolean aBoolean1846 = false;
	static volatile long aLong1847 = 0L;
	boolean aBoolean1848 = false;
	int rightHandItem = -1;
	int priority = -1;
	int[] frames;
	public static CacheIndex aClass153_1852;
	int leftHandItem = -1;
	boolean[] aBooleanArray1855;
	static AbstractIndexedSprite aClass109_1856;
	int forcedPriority = 5;
	boolean aBoolean1859 = false;
	static CacheIndex aClass153_1860;
	int maxLoops = 99;
	static int anInt1862 = 0;
	int animId;
	int anInt1865 = -1;
	int resetWhenWalk = -1;
	int[][] sounds;
	static Class25[] aClass25Array1868;
	int[] duration;
	private int[] baseIds;
	static int[] anIntArray1871 = new int[2];
	boolean aBoolean1872 = false;

	static SequenceDefinition getAnimationDefinition(int var0) {
		try {

			SequenceDefinition var2 = (SequenceDefinition) Class82.aReferenceCache_1146.get(var0);
			if (var2 == null) {
				byte[] var3 = aClass153_1860.getFile(Class129.method1765(var0), Class67.method1262(117, var0));
				var2 = new SequenceDefinition();
				var2.animId = var0;
				if (var3 != null) {
					var2.parseDefinitions(new DataBuffer(var3));
				}

				var2.method2058();
				Class82.aReferenceCache_1146.put(var2, var0);
			}
			return var2;
		} catch (RuntimeException var4) {
			throw ClientErrorException.clientError(var4, "client.D(" + var0 + ',' + (byte) -20 + ')');
		}
	}


	final void parseDefinitions(DataBuffer var1) {
		try {
			//	System.out.print("Animation " + animId + " - parsed [");
			while(true) {
				int var3 = var1.readUnsignedByte();
				if(var3 == 0) {
					//System.out.println("].");
					return;
				}
				//System.out.print(var3 + ", ");
				this.parseOpcode(var3, var1);
			}
		} catch (RuntimeException var4) {
			throw ClientErrorException.clientError(var4, "tk.I(" + (var1 != null?"{...}":"null") + ',' + (byte) -102 + ')');
		}
	}

	final Model method2054(int var2, int var3, Model var4, int var5, int var6) {
		try {
			int var7 = this.duration[var2];
			var2 = this.frames[var2];
			Class3_Sub28_Sub5 var8 = Class3_Sub9.method133(var2 >> 16);
			var2 &= '\uffff';
			if(var8 == null) {
				return var4.method1890(true, true, true);
			} else {
				var5 &= 3;
				Class3_Sub28_Sub5 var9 = null;

				if((this.aBoolean1846 || ClientCommands.tweeningEnabled) && var3 != -1 && this.frames.length > var3) {
					var3 = this.frames[var3];
					var9 = Class3_Sub9.method133(var3 >> 16);
					var3 &= '\uffff';
				}

				Model var10;
				if(var9 == null) {
					var10 = var4.method1890(!var8.method559(var2), !var8.method561(var2, (byte)121), !this.aBoolean1848);
				} else {
					var10 = var4.method1890(!var8.method559(var2) & !var9.method559(var3), !var8.method561(var2, (byte)125) & !var9.method561(var3, (byte)118), !this.aBoolean1848);
				}

				if(HDToolKit.highDetail && this.aBoolean1848) {
					if(var5 == 1) {
						((Class140_Sub1_Sub1)var10).method1902();
					} else if (2 == var5) {
						((Class140_Sub1_Sub1) var10).method1911();
					} else if (var5 == 3) {
						((Class140_Sub1_Sub1) var10).method1925();
					}
				} else if(var5 == 1) {
					var10.method1900();
				} else if (2 == var5) {
					var10.method1874();
				} else if (3 == var5) {
					var10.method1885();
				}

				var10.method1880(var8, var2, var9, var3, -1 + var6, var7, this.aBoolean1848);
				if(HDToolKit.highDetail && this.aBoolean1848) {
					if(1 == var5) {
						((Class140_Sub1_Sub1)var10).method1925();
					} else if (var5 == 2) {
						((Class140_Sub1_Sub1) var10).method1911();
					} else if (var5 == 3) {
						((Class140_Sub1_Sub1) var10).method1902();
					}
				} else if(var5 == 1) {
					var10.method1885();
				} else if (var5 == 2) {
					var10.method1874();
				} else if (var5 == 3) {
					var10.method1900();
				}

				return var10;
			}
		} catch (RuntimeException var11) {
			throw ClientErrorException.clientError(var11, "tk.D(" + 19749 + ',' + var2 + ',' + var3 + ',' + (var4 != null?"{...}":"null") + ',' + var5 + ',' + var6 + ')');
		}
	}

	final Model method2055(Model var1, byte var2, int var3, int var4, int var5) {
		try {
			int var7 = this.frames[var5];
			int var6 = this.duration[var5];
			Class3_Sub28_Sub5 var8 = Class3_Sub9.method133(var7 >> 16);
			var7 &= '\uffff';
			if(null == var8) {
				return var1.method1894(true, true, true);
			} else {
				Class3_Sub28_Sub5 var9 = null;
				if((this.aBoolean1846 || ClientCommands.tweeningEnabled) && var3 != -1 && this.frames.length > var3) {
					var3 = this.frames[var3];
					var9 = Class3_Sub9.method133(var3 >> 16);
					var3 &= '\uffff';
				}

				Class3_Sub28_Sub5 var10 = null;
				Class3_Sub28_Sub5 var11 = null;
				int var13 = 0;
				int var14 = 0;
				if(null != this.baseIds) {
					if(var5 < this.baseIds.length) {
						var13 = this.baseIds[var5];
						if(var13 != 65535) {
							var10 = Class3_Sub9.method133(var13 >> 16);
							var13 &= '\uffff';
						}
					}

					if((this.aBoolean1846 || ClientCommands.tweeningEnabled) && -1 != var3 && this.baseIds.length > var3) {
						var14 = this.baseIds[var3];
						if(var14 != 65535) {
							var11 = Class3_Sub9.method133(var14 >> 16);
							var14 &= '\uffff';
						}
					}
				}

				boolean var15 = !var8.method559(var7);
				boolean var16 = !var8.method561(var7, (byte)119);
				if(var10 != null) {
					var15 &= !var10.method559(var13);
					var16 &= !var10.method561(var13, (byte)115);
				}

				if(null != var9) {
					var15 &= !var9.method559(var3);
					var16 &= !var9.method561(var3, (byte)123);
				}

				if(null != var11) {
					var15 &= !var11.method559(var14);
					var16 &= !var11.method561(var14, (byte)121);
				}

				Model var17 = var1.method1894(var15, var16, !this.aBoolean1848);
				var17.method1880(var8, var7, var9, var3, var4 - 1, var6, this.aBoolean1848);
				if(null != var10) {
					var17.method1880(var10, var13, var11, var14, var4 + -1, var6, this.aBoolean1848);
				}

				return var17;
			}
		} catch (RuntimeException var18) {
			throw ClientErrorException.clientError(var18, "tk.E(" + (var1 != null?"{...}":"null") + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ')');
		}
	}

	final Model method2056(int var1, int var2, int var3, int var4, Model var5) {
		try {
			int var7 = this.duration[var2];
			var2 = this.frames[var2];
			Class3_Sub28_Sub5 var8 = Class3_Sub9.method133(var2 >> 16);
			var2 &= '\uffff';
			if(null == var8) {
				return var5.method1894(true, true, true);
			} else {
				var4 &= 3;
				Class3_Sub28_Sub5 var9 = null;
				if((this.aBoolean1846 || ClientCommands.tweeningEnabled) && var1 != -1 && this.frames.length > var1) {
					var1 = this.frames[var1];
					var9 = Class3_Sub9.method133(var1 >> 16);
					var1 &= '\uffff';
				}

				Model var10;
				if(null == var9) {
					var10 = var5.method1894(!var8.method559(var2), !var8.method561(var2, (byte)123), !this.aBoolean1848);
				} else {
					var10 = var5.method1894(!var8.method559(var2) & !var9.method559(var1), !var8.method561(var2, (byte)125) & !var9.method561(var1, (byte)123), !this.aBoolean1848);
				}

				if(this.aBoolean1848 && HDToolKit.highDetail) {
					if(1 == var4) {
						((Class140_Sub1_Sub1)var10).method1902();
					} else if (var4 == 2) {
						((Class140_Sub1_Sub1) var10).method1911();
					} else if (var4 == 3) {
						((Class140_Sub1_Sub1) var10).method1925();
					}
				} else if(var4 == 1) {
					var10.method1900();
				} else if(var4 == 2) {
					var10.method1874();
				} else if(var4 == 3) {
					var10.method1885();
				}

				var10.method1880(var8, var2, var9, var1, var3 + -1, var7, this.aBoolean1848);
				if(this.aBoolean1848 && HDToolKit.highDetail) {
					if(var4 == 1) {
						((Class140_Sub1_Sub1)var10).method1925();
					} else if(var4 == 2) {
						((Class140_Sub1_Sub1)var10).method1911();
					} else if(var4 == 3) {
						((Class140_Sub1_Sub1)var10).method1902();
					}
				} else if(1 == var4) {
					var10.method1885();
				} else if (var4 == 2) {
					var10.method1874();
				} else if (3 == var4) {
					var10.method1900();
				}

				return var10;
			}
		} catch (RuntimeException var11) {
			throw ClientErrorException.clientError(var11, "tk.B(" + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + (var5 != null?"{...}":"null") + ',' + 3 + ')');
		}
	}

	final void method2058() {
		try {
			if(this.resetWhenWalk == -1) {
				if(null == this.aBooleanArray1855) {
					this.resetWhenWalk = 0;
				} else {
					this.resetWhenWalk = 2;
				}
			}

			if(-1 == this.priority) {
				if(null == this.aBooleanArray1855) {
					this.priority = 0;
				} else {
					this.priority = 2;
				}
			}

		} catch (RuntimeException var3) {
			throw ClientErrorException.clientError(var3, "tk.C(" + (byte) -41 + ')');
		}
	}

	final Model method2059(int var1, int var2, int var3, Model var5) {
		try {
			int var6 = this.duration[var3];
			var3 = this.frames[var3];
			Class3_Sub28_Sub5 var7 = Class3_Sub9.method133(var3 >> 16);
			var3 &= '\uffff';
			if(var7 == null) {
				return var5.method1882(true, true, true);
			} else {
				Class3_Sub28_Sub5 var9 = null;
				if((this.aBoolean1846 || ClientCommands.tweeningEnabled) && var1 != -1 && var1 < this.frames.length) {
					var1 = this.frames[var1];
					var9 = Class3_Sub9.method133(var1 >> 16);
					var1 &= '\uffff';
				}

				Model var10;
				if(null == var9) {
					var10 = var5.method1882(!var7.method559(var3), !var7.method561(var3, (byte)118), !this.aBoolean1848);
				} else {
					var10 = var5.method1882(!var7.method559(var3) & !var9.method559(var1), !var7.method561(var3, (byte)119) & !var9.method561(var1, (byte)118), !this.aBoolean1848);
				}

				var10.method1880(var7, var3, var9, var1, var2 + -1, var6, this.aBoolean1848);
				return var10;
			}
		} catch (RuntimeException var11) {
			throw ClientErrorException.clientError(var11, "tk.G(" + var1 + ',' + var2 + ',' + var3 + ',' + (byte) -52 + ',' + (var5 != null?"{...}":"null") + ')');
		}
	}

	private void parseOpcode(int opcode, DataBuffer buffer) {
		try {
			int count;
			int index;
			if(opcode == 1) {
				count = buffer.readUnsignedShort();
				this.duration = new int[count];

				for(index = 0; count > index; ++index) {
					this.duration[index] = buffer.readUnsignedShort();
				}

				this.frames = new int[count];

				for(index = 0; count > index; ++index) {
					this.frames[index] = buffer.readUnsignedShort();
				}

				for(index = 0; count > index; ++index) {
					this.frames[index] += buffer.readUnsignedShort() << 16;
				}
			} else if(opcode == 2) {
				this.anInt1865 = buffer.readUnsignedShort();
			} else if(opcode == 3) {
				this.aBooleanArray1855 = new boolean[256];
				count = buffer.readUnsignedByte();

				for(index = 0; index < count; ++index) {
					this.aBooleanArray1855[buffer.readUnsignedByte()] = true;
				}
			} else if (opcode == 4) {
				this.aBoolean1859 = true;
			} else if (opcode == 5) {
				this.forcedPriority = buffer.readUnsignedByte();
			} else if (6 == opcode) {
				this.leftHandItem = buffer.readUnsignedShort();
			} else if (opcode == 7) {
				this.rightHandItem = buffer.readUnsignedShort();
			} else if (8 == opcode) {
				this.maxLoops = buffer.readUnsignedByte();
			} else if (9 == opcode) {
				this.resetWhenWalk = buffer.readUnsignedByte();
			} else if (10 == opcode) {
				this.priority = buffer.readUnsignedByte();
			} else if (opcode == 11) {
				this.delayType = buffer.readUnsignedByte();
			} else if (12 == opcode) {
				count = buffer.readUnsignedByte();
				this.baseIds = new int[count];

				for (index = 0; index < count; ++index) {
					this.baseIds[index] = buffer.readUnsignedShort();
				}

				for (index = 0; index < count; ++index) {
					this.baseIds[index] += buffer.readUnsignedShort() << 16;
				}
			} else if (13 == opcode) {
				count = buffer.readUnsignedShort();
				this.sounds = new int[count][];

				for (index = 0; index < count; ++index) {
					int var6 = buffer.readUnsignedByte();
					if (var6 > 0) {
						this.sounds[index] = new int[var6];
						this.sounds[index][0] = buffer.readMedium();

						for (int var7 = 1; var7 < var6; ++var7) {
							this.sounds[index][var7] = buffer.readUnsignedShort();
						}
					}
				}
			} else if (opcode == 14) {
				this.aBoolean1848 = true;
			} else if (15 == opcode) {
				this.aBoolean1846 = true;
			} else if (16 == opcode) {
				this.aBoolean1872 = true;
			}

		} catch (RuntimeException var8) {
			throw ClientErrorException.clientError(var8, "tk.H(" + opcode + ',' + (byte) -73 + ',' + (buffer != null?"{...}":"null") + ')');
		}
	}

	static void resetAll() {
		try {
			Class3_Sub26.anInt2556 = 0;
			Class3_Sub13_Sub13.aBoolean2774 = true;
			Class3_Sub13_Sub13.aLong1465 = 0L;
			Class3_Sub13_Sub13.aClass67_1443.anInt1018 = 0;
			Class3_Sub13_Sub6.aBoolean3078 = true;
			Class3_Sub13.method153(112);
			Class24.anInt469 = -1;
			Class7.anInt2166 = -1;
			Unsorted.incomingOpcode = -1;
			Class159.anInt2023 = 0;
			Class38_Sub1.anInt2617 = 0;
			Class3_Sub13_Sub1.outgoingBuffer.index = 0;
			LinkableRSString.anInt2582 = -1;
			AbstractSprite.anInt3699 = 0;
			GraphicDefinition.incomingBuffer.index = 0;

			int var1;
			for(var1 = 0; ClientErrorException.aClass96Array2114.length > var1; ++var1) {
				ClientErrorException.aClass96Array2114[var1] = null;
			}

			Unsorted.menuOptionCount = 0;
			Class38_Sub1.aBoolean2615 = false;
			Class23.method940(119, 0);

			for(var1 = 0; var1 < 100; ++var1) {
				LinkableRSString.aClass94Array2580[var1] = null;
			}

			Class164_Sub1.anInt3012 = 0;
			Class3_Sub13_Sub13.anInt3216 = (int)(Math.random() * 100.0D) + -50;
			Class45.anInt733 = 0;
			GraphicDefinition.CAMERA_DIRECTION = 2047 & (int)(Math.random() * 20.0D) - 10;
			Class58.anInt909 = -1;
			Class159.localPlayerCount = 0;
			Class161.anInt2028 = 0;
			Unsorted.anInt42 = (int)(110.0D * Math.random()) + -55;
			GameObject.aBoolean1837 = false;
			Class164_Sub2.anInt3020 = -20 + (int)(30.0D * Math.random());
			Class113.anInt1552 = 0;
			Class65.anInt987 = 0;
			Class3_Sub13_Sub8.anInt3102 = -60 + (int)(Math.random() * 120.0D);
			Class3_Sub13_Sub9.anInt3114 = 0;
			LinkableRSString.anInt2589 = (int)(80.0D * Math.random()) - 40;
			Class163.localNPCCount = 0;

			for(var1 = 0; 2048 > var1; ++var1) {
				Class3_Sub13_Sub22.players[var1] = null;
				Class65.aClass3_Sub30Array986[var1] = null;
			}

			for(var1 = 0; var1 < '\u8000'; ++var1) {
				NPC.npcs[var1] = null;
			}

			Class102.player = Class3_Sub13_Sub22.players[2047] = new Player();
			Class3_Sub13_Sub30.aClass61_3364.method1211(-112);
			Class3_Sub13_Sub15.aClass61_3177.method1211(-58);
			if(null != Class3_Sub13_Sub22.aClass61ArrayArrayArray3273) {
				for(var1 = 0; 4 > var1; ++var1) {
					for(int var2 = 0; var2 < 104; ++var2) {
						for(int var3 = 0; var3 < 104; ++var3) {
							Class3_Sub13_Sub22.aClass61ArrayArrayArray3273[var1][var2][var3] = null;
						}
					}
				}
			}

			Class3_Sub13_Sub6.aClass61_3075 = new Class61();
			CS2Script.anInt1357 = 0;
			Class8.anInt104 = 0;
			Class3_Sub13_Sub2.method176(-114);
			Class3_Sub2.method103();
			Class75.anInt1105 = 0;
			Class163_Sub2_Sub1.anInt4014 = 0;
			Class157.anInt1996 = 0;
			Class3_Sub13_Sub34.anInt3414 = 0;
			Class146.anInt1904 = 0;
			Unsorted.anInt30 = 0;
			GraphicDefinition.anInt529 = 0;
			MouseListeningClass.anInt1923 = 0;
			Unsorted.anInt3631 = 0;
			Class163_Sub2_Sub1.anInt4021 = 0;

			for(var1 = 0; var1 < NPCDefinition.anIntArray1277.length; ++var1) {
				NPCDefinition.anIntArray1277[var1] = -1;
			}

			if(Class3_Sub28_Sub12.anInt3655 != -1) {
				Class60.method1208((byte)-128, Class3_Sub28_Sub12.anInt3655);
			}

			for(Class3_Sub31 var7 = (Class3_Sub31)Class3_Sub13_Sub17.aHashTable_3208.first(); var7 != null; var7 = (Class3_Sub31)Class3_Sub13_Sub17.aHashTable_3208.next()) {
				Class3_Sub13_Sub18.method254(true, var7);
			}

			Class3_Sub28_Sub12.anInt3655 = -1;
			Class3_Sub13_Sub17.aHashTable_3208 = new HashTable(8);
			Class3_Sub7.method122(-113);
			Class3_Sub13_Sub7.aClass11_3087 = null;
			Class38_Sub1.aBoolean2615 = false;
			Unsorted.menuOptionCount = 0;
			Unsorted.aClass52_1112.method1161(new int[]{0, 0, 0, 0, 0}, -1, false, (int[])null, -1);

			for(var1 = 0; 8 > var1; ++var1) {
				Class91.aClass94Array1299[var1] = null;
				Class1.aBooleanArray54[var1] = false;
				Class3_Sub13_Sub26.anIntArray3328[var1] = -1;
			}

			Class3_Sub28_Sub9.method580((byte)80);
			Class3_Sub13_Sub4.aBoolean3064 = true;

			for(var1 = 0; var1 < 100; ++var1) {
				Unsorted.aBooleanArray3674[var1] = true;
			}

			Unsorted.clanSize = 0;
			PacketParser.aClass3_Sub19Array3694 = null;
			RSInterface.aClass94_251 = null;

			for(var1 = 0; 6 > var1; ++var1) {
				Class3_Sub13_Sub33.aClass133Array3393[var1] = new Class133();
			}

			for(var1 = 0; var1 < 25; ++var1) {
				Class3_Sub13_Sub15.anIntArray3185[var1] = 0;
				Class3_Sub20.anIntArray2480[var1] = 0;
				Class133.anIntArray1743[var1] = 0;
			}

			if(HDToolKit.highDetail) {
				Class3_Sub13_Sub14.method236();
			}

			Unsorted.aBoolean4068 = true;
			Class113.interfacePacketCounter = 0;
			Class3_Sub13_Sub28.aClass94_3353 = TextCore.HasWalkHere;
			Unsorted.aBoolean1084 = false;
			Class3_Sub13_Sub38.aShortArray3455 = Class3_Sub13_Sub9.aShortArray3110 = Class136.aShortArray1779 = Class3_Sub13_Sub38.aShortArray3453 = new short[256];
			Class3_Sub13.method165();
			Unsorted.aBoolean1951 = false;
			Class3_Sub13_Sub8.method204(-3);
		} catch (RuntimeException var6) {
			throw ClientErrorException.clientError(var6, "tk.A(" + true + ')');
		}
	}

}
