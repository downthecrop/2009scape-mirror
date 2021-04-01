package org.runite.client;

public final class GraphicDefinition {

	static int anInt529;
	private int anInt530 = 128;
	public static int CAMERA_DIRECTION = 0;
	private short[] aShortArray533;
	private short[] aShortArray534;
	private short[] aShortArray535;
	boolean aBoolean536 = false;
	private int anInt537 = 0;
	private int anInt538 = 0;
	int graphicId;
	private int anInt540 = 128;
	private int anInt541;
	int anInt542 = -1;
	private int anInt543 = 0;
	private short[] aShortArray545;
	static int anInt546;
	static int anInt548 = 0;
	static volatile int anInt549 = 0;

	static GraphicDefinition getGraphicDefinition(int graphicId) {
		try {
			GraphicDefinition def = (GraphicDefinition) Class3_Sub31.aReferenceCache_2604.get(graphicId);
			if (def == null) {
				byte[] var3 = TextureOperation19.aClass153_3214.getFile(graphicId >>> 8, graphicId & 0xFF);
				def = new GraphicDefinition();
				def.graphicId = graphicId;

				if (null != var3) {
					def.parse(new DataBuffer(var3));
				}

				Class3_Sub31.aReferenceCache_2604.put(def, graphicId);
			}
			return def;
		} catch (RuntimeException var4) {
			throw ClientErrorException.clientError(var4, "ck.D(" + (byte) 42 + ',' + graphicId + ')');
		}
	}

	final void parse(DataBuffer var1) {
		try {

            while(true) {
				int var3 = var1.readUnsignedByte();
				if(var3 == 0) {
					return;
				}

				this.decode(var1, var3);
			}
		} catch (RuntimeException var4) {
			throw ClientErrorException.clientError(var4, "eg.A(" + (var1 != null?"{...}":"null") + ',' + (byte) -113 + ')');
		}
	}

	private void decode(DataBuffer var1, int var2) {
		try {
			if(var2 == 1) {
				this.anInt541 = var1.readUnsignedShort();
			} else if(2 == var2) {
				this.anInt542 = var1.readUnsignedShort();
			} else if(var2 == 4) {
				this.anInt530 = var1.readUnsignedShort();
			} else if (var2 == 5) {
				this.anInt540 = var1.readUnsignedShort();
			} else if (6 == var2) {
				this.anInt543 = var1.readUnsignedShort();
			} else if (var2 == 7) {
				this.anInt538 = var1.readUnsignedByte();
			} else if (var2 == 8) {
				this.anInt537 = var1.readUnsignedByte();
			} else if (var2 == 9) {
				this.aBoolean536 = true;
			} else {
				int var4;
				int var5;
				if (40 == var2) {
					var4 = var1.readUnsignedByte();
					this.aShortArray533 = new short[var4];
					this.aShortArray545 = new short[var4];

					for (var5 = 0; var4 > var5; ++var5) {
						this.aShortArray533[var5] = (short) var1.readUnsignedShort();
						this.aShortArray545[var5] = (short) var1.readUnsignedShort();
					}
				} else if (41 == var2) {
					var4 = var1.readUnsignedByte();
					this.aShortArray534 = new short[var4];
					this.aShortArray535 = new short[var4];

					for (var5 = 0; var5 < var4; ++var5) {
						this.aShortArray534[var5] = (short) var1.readUnsignedShort();
						this.aShortArray535[var5] = (short) var1.readUnsignedShort();
					}
				}
			}

        } catch (RuntimeException var6) {
			throw ClientErrorException.clientError(var6, "eg.D(" + (var1 != null?"{...}":"null") + ',' + var2 + ',' + 128 + ')');
		}
	}

	final Model method966(int var1, int var3, int var4) {
		try {
			Model var5 = (Model)Class27.aReferenceCache_511.get(this.graphicId);
            if(var5 == null) {
                Model_Sub1 var6 = Model_Sub1.method2015(Unsorted.aClass153_4048, this.anInt541);
                if(null == var6) {
                    return null;
                }

                int var7;
                if(null != this.aShortArray533) {
                    for(var7 = 0; this.aShortArray533.length > var7; ++var7) {
                        var6.method2016(this.aShortArray533[var7], this.aShortArray545[var7]);
                    }
                }

                if(this.aShortArray534 != null) {
                    for(var7 = 0; var7 < this.aShortArray534.length; ++var7) {
                        var6.method1998(this.aShortArray534[var7], this.aShortArray535[var7]);
                    }
                }

                var5 = var6.method2008(64 - -this.anInt538, this.anInt537 + 850, -30, -50, -30);
                Class27.aReferenceCache_511.put(var5, this.graphicId);
            }

            Model var9;
            if(this.anInt542 == -1 || var3 == -1) {
                var9 = var5.method1882(true, true, true);
            } else {
                var9 = SequenceDefinition.getAnimationDefinition(this.anInt542).method2059(var1, var4, var3, var5);
            }

            if(128 != this.anInt530 || 128 != this.anInt540) {
                var9.resize(this.anInt530, this.anInt540, this.anInt530);
            }

            if(this.anInt543 != 0) {
                if(this.anInt543 == 90) {
                    var9.method1885();
                }

                if(180 == this.anInt543) {
                    var9.method1874();
                }

                if(270 == this.anInt543) {
                    var9.method1900();
                }
            }

            return var9;
        } catch (RuntimeException var8) {
			throw ClientErrorException.clientError(var8, "eg.C(" + var1 + ',' + (byte) -30 + ',' + var3 + ',' + var4 + ')');
		}
	}

	static void method967(int var0, int var1, int var3, int var4, int var5, int var6, int var7) {
		try {
			if(Unsorted.loadInterface(var5)) {
                Client.handleItemSwitch(GameObject.aClass11ArrayArray1834[var5], -1, var6, var1, var4, var7, var0, var3);
            }
		} catch (RuntimeException var9) {
			throw ClientErrorException.clientError(var9, "eg.B(" + var0 + ',' + var1 + ',' + 2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ')');
		}
	}

}
