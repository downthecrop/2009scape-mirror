package plugin.interaction.object.dmc;

/**
 * The DMC aiming directions.
 * @author Emperor
 */
public enum DMCRevolution {

	NORTH(515) {
		@Override
		public boolean isInSight(int offsetX, int offsetY) {
			return offsetY > 0 && Math.abs(offsetX) <= offsetY;
		}
	},
	NORTH_EAST(516) {
		@Override
		public boolean isInSight(int offsetX, int offsetY) {
			return offsetY > 0 && offsetX > 0;
		}
	},
	EAST(517) {
		@Override
		public boolean isInSight(int offsetX, int offsetY) {
			return offsetX > 0 && Math.abs(offsetY) <= offsetX;
		}
	},
	SOUTH_EAST(518) {
		@Override
		public boolean isInSight(int offsetX, int offsetY) {
			return offsetY < 0 && offsetX > 0;
		}
	},
	SOUTH(519) {
		@Override
		public boolean isInSight(int offsetX, int offsetY) {
			return offsetY < 0 && Math.abs(offsetX) <= -offsetY;
		}
	},
	SOUTH_WEST(520) {
		@Override
		public boolean isInSight(int offsetX, int offsetY) {
			return offsetY < 0 && offsetX < 0;
		}
	},
	WEST(521) {
		@Override
		public boolean isInSight(int offsetX, int offsetY) {
			return offsetX < 0 && Math.abs(offsetY) <= -offsetX;
		}
	},
	NORTH_WEST(514) {
		@Override
		public boolean isInSight(int offsetX, int offsetY) {
			return offsetY > 0 && offsetX < 0;
		}
	};

	/**
	 * The animation id.
	 */
	private final int animationId;

	/**
	 * Constructs a new {@Code DMCRevolution} {@Code Object}
	 * @param animationId The animation id.
	 */
	private DMCRevolution(int animationId) {
		this.animationId = animationId;
	}

	/**
	 * Gets the animationId.
	 * @return the animationId
	 */
	public int getAnimationId() {
		return animationId;
	}

	/**
	 * Checks if the offsets are in the line of sight for the current direction.
	 * @param offsetX The x-offset.
	 * @param offsetY The y-offset.
	 * @return {@code True} if so.
	 */
	public boolean isInSight(int offsetX, int offsetY) {
		return false;
	}
}