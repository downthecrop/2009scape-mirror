package core.tools;

/**
 * Used for debugging duration of executing code.
 * @author Emperor
 */
public final class TimeStamp {

	/**
	 * The start
	 */
	private long start;

	/**
	 * The interval.
	 */
	private long interval;

	/**
	 * Constructs a new {@code TimeStamp} {@code Object}.
	 */
	public TimeStamp() {
		start = System.currentTimeMillis();
		interval = start;
	}

	/**
	 * Set current interval.
	 * @return The duration of this interval.
	 */
	public long interval() {
		return interval(true, "");
	}

	/**
	 * Set current interval.
	 * @param debug If we should print out the duration.
	 * @return The duration of this interval.
	 */
	public long interval(boolean debug) {
		return interval(debug, "");
	}

	/**
	 * Set current interval.
	 * @param debug If we should print out the duration.
	 * @param info Extra information.
	 * @return The duration of this interval.
	 */
	public long interval(boolean debug, String info) {
		long current = System.currentTimeMillis();
		long difference = current - interval;
		if (debug || difference > 100) {
			System.out.println("Interval " + info + " - time elapsed=" + difference + "ms.");
		}
		interval = current;
		return difference;
	}

	/**
	 * Gets the amount of milliseconds passed since the creation of this object.
	 * @return The duration.
	 */
	public long duration(boolean debug, String info) {
		long current = System.currentTimeMillis();
		long difference = current - start;
		if (debug) {
			System.out.println("Interval " + info + " - time elapsed=" + difference + "ms.");
		}
		return difference;
	}

}