package org.runite.client;

final class TextureOperation35 extends TextureOperation {

	static int anInt3332 = 50;
	static int[] anIntArray3321 = new int[]{76, 8, 137, 4, 0, 1, 38, 2, 19};
	private int anInt3322 = 4096;
	static int anInt3323 = 50;
	static int[] anIntArray3328 = new int[8];
	static byte[][] aByteArrayArray3335;


	public static void method294(byte var0) {
		try {
			Texture.anIntArray3327 = null;
			Texture.anIntArray3337 = null;
			Texture.aClass94Array3317 = null;
			anIntArray3328 = null;
			Texture.anIntArray3319 = null;
			aByteArrayArray3335 = null;
			Texture.anIntArray3329 = null;
			Texture.anIntArray3336 = null;
			anIntArray3321 = null;
			Texture.anIntArray3318 = null;
			Texture.anIntArray3331 = null;
		} catch (RuntimeException var2) {
			throw ClientErrorException.clientError(var2, "pg.B(" + var0 + ')');
		}
	}

	public TextureOperation35() {
		super(1, true);
	}

	final int[] method154(int var1, byte var2) {
		try {
			int[] var4 = this.aClass114_2382.method1709(var1);
			if(this.aClass114_2382.aBoolean1580) {
				int[] var5 = this.method152(0, var1 - 1 & Class3_Sub20.anInt2487);
				int[] var6 = this.method152(0, var1);
				int[] var7 = this.method152(0, Class3_Sub20.anInt2487 & var1 + 1);

				for(int var8 = 0; Class113.anInt1559 > var8; ++var8) {
					int var9 = (var7[var8] + -var5[var8]) * this.anInt3322;
					int var10 = this.anInt3322 * (-var6[var8 - 1 & RenderAnimationDefinition.anInt396] + var6[RenderAnimationDefinition.anInt396 & var8 - -1]);
					int var11 = var10 >> 12;
					int var12 = var9 >> 12;
					int var13 = var11 * var11 >> 12;
					int var14 = var12 * var12 >> 12;
					int var15 = (int)(Math.sqrt((float)(4096 + var14 + var13) / 4096.0F) * 4096.0D);
					int var16 = 0 != var15?16777216 / var15:0;
					var4[var8] = 4096 + -var16;
				}
			}

			return var4;
		} catch (RuntimeException var17) {
			throw ClientErrorException.clientError(var17, "pg.D(" + var1 + ',' + var2 + ')');
		}
	}

	final void decode(int var1, DataBuffer var2) {
		try {
			if(!true) {
				method294((byte)-57);
			}

			if(var1 == 0) {
				this.anInt3322 = var2.readUnsignedShort();
			}

		} catch (RuntimeException var5) {
			throw ClientErrorException.clientError(var5, "pg.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + true + ')');
		}
	}

}
