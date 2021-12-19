package org.runite.client;


import org.rs09.client.config.GameConfig;

public final class TextureOperation12 extends TextureOperation {

	public static BufferedDataStream outgoingBuffer = new BufferedDataStream();
	private int anInt3036 = 0;
	private int anInt3037 = 1;
	private int anInt3038 = 0;
	static RSString aClass2323;
	static String aString2324;
	static RSString aClass2325;


	static void method167(int var0) {
		try {
			if(TextureOperation33.aClass148_3049 != null) {
				KeyboardListener var1 = TextureOperation33.aClass148_3049;
				synchronized(var1) {
					TextureOperation33.aClass148_3049 = null;
				}
			}

			if(var0 != 0) {
				method171(119, -44, -76, -104, 29, -65, 34, 18, 104);
			}

		} catch (RuntimeException var4) {
			throw ClientErrorException.clientError(var4, "ag.B(" + var0 + ')');
		}
	}

	public TextureOperation12() {
		super(0, true);
	}

	final int[] method154(int var1, byte var2) {
		try {
			int[] var3 = this.aClass114_2382.method1709(var1);
			int var4;
			if(this.aClass114_2382.aBoolean1580) {
				var4 = Class163_Sub3.anIntArray2999[var1];
				int var5 = var4 - 2048 >> 1;

				for(int var6 = 0; var6 < Class113.anInt1559; ++var6) {
					int var8 = Class102.anIntArray2125[var6];
					int var9 = -2048 + var8 >> 1;
				int var7;
				if(this.anInt3038 == 0) {
					var7 = (var8 + -var4) * this.anInt3037;
				} else {
					int var10 = var9 * var9 - -(var5 * var5) >> 12;
					var7 = (int)(Math.sqrt((float)var10 / 4096.0F) * 4096.0D);
					var7 = (int)(3.141592653589793D * (double)(var7 * this.anInt3037));
				}

					var7 -= var7 & -4096;
				if(this.anInt3036 == 0) {
					var7 = TextureOperation23.anIntArray3212[(var7 & 4085) >> 4] + 4096 >> 1;
				} else if(this.anInt3036 == 2) {
					var7 -= 2048;
					if(var7 < 0) {
						var7 = -var7;
					}

					var7 = -var7 + 2048 << 1;
				}

				var3[var6] = var7;
				}
			}

            return var3;
		} catch (RuntimeException var11) {
			throw ClientErrorException.clientError(var11, "ag.D(" + var1 + ',' + var2 + ')');
		}
	}

	static void method229() {
		DataBuffer buffer = outgoingBuffer;
		buffer.writeString(aClass2323);
		for (char c : aString2324.toCharArray()) {
			if (c == '-') {
				c = ':';
			}
			buffer.writeByte(c);
		}
		buffer.writeByte(0);
		buffer.writeString(aClass2325);
	}

	static void method169() {
		try {
			Class32.method995();

			for(int var1 = 0; 4 > var1; ++var1) {
				AtmosphereParser.aClass91Array1182[var1].method1496();
			}

			System.gc();
		} catch (RuntimeException var2) {
			throw ClientErrorException.clientError(var2, "ag.O(" + 22230 + ')');
		}
	}

	static Class<?> method170(String var1) throws ClassNotFoundException {
		try {

			return var1.equals("B")?Byte.TYPE:(!var1.equals("I")?(var1.equals("S")?Short.TYPE:(!var1.equals("J")?(var1.equals("Z")?Boolean.TYPE:(var1.equals("F")?Float.TYPE:(var1.equals("D")?Double.TYPE:(var1.equals("C")?Character.TYPE:Class.forName(GameConfig.PACKAGE_NAME + "." + var1))))):Long.TYPE)):Integer.TYPE);
		} catch (RuntimeException var3) {
			throw ClientErrorException.clientError(var3, "ag.C(" + 6092 + ',' + (var1 != null?"{...}":"null") + ')');
		}
	}

	final void postDecode() {
		try {
			Class8.method844((byte)-9);

		} catch (RuntimeException var3) {
			throw ClientErrorException.clientError(var3, "ag.P(" + ')');
		}
	}

	static void method171(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
		try {
			if(var0 != -101) {
				method167(-46);
			}

			if(Unsorted.loadInterface(var1)) {
				Unsorted.method1095(var2, var8, var4, GameObject.interfaces1834[var1], var3, -1, var7, var6, (byte)119, var5);
			} else if (var5 == -1) {
				for (int var9 = 0; var9 < 100; ++var9) {
					Unsorted.aBooleanArray3674[var9] = true;
				}
			} else {
				Unsorted.aBooleanArray3674[var5] = true;
			}
		} catch (RuntimeException var10) {
			throw ClientErrorException.clientError(var10, "ag.E(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ')');
		}
	}

	final void decode(int var1, DataBuffer var2) {
		try {

			if(var1 == 0) {
				this.anInt3038 = var2.readUnsignedByte();
			} else if (1 == var1) {
				this.anInt3036 = var2.readUnsignedByte();
			} else if (var1 == 3) {
				this.anInt3037 = var2.readUnsignedByte();
			}

		} catch (RuntimeException var5) {
			throw ClientErrorException.clientError(var5, "ag.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + true + ')');
		}
	}


	static void method445() {
		aClass2323 = RSString.parse(System.getProperty("user.name"));
		aString2324 = Class39.method132893();
		aClass2325 = Signlink.osName.startsWith("win") ? Class44.method3435() : Class44.method3434();
	}
}
