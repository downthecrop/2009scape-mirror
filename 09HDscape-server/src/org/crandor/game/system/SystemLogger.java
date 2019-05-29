package org.crandor.game.system;

import org.crandor.game.world.GameWorld;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Printing log messages class.
 * @author Apache Ah64
 */
public class SystemLogger {

	/**
	 * The date format string.
	 */
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * Print a log message.
	 * @param message
	 */
	public static void log(String message) {
		dateFormat.setTimeZone(TimeZone.getDefault());
		if (message == null) {
			return;
		}
		System.out.println("[" + dateFormat.format(new Date()) + "][" + GameWorld.getName() + "]: " + message);
	}

	/**
	 * Print a log message with class name.
	 * @param thread
	 * @param message
	 */
	public static void log(Class<?> thread, String message) {
		if (message == null) {
			return;
		}
		System.out.println("[" + dateFormat.format(new Date()) + "][" + Class.class.getSimpleName() + "]: " + message);
	}

	/**
	 * Print a log message with class name.
	 * @param thread
	 * @param message
	 */
	public static void log(String className, String message) {
		if (message == null) {
			return;
		}
		System.out.println("[" + dateFormat.format(new Date()) + "][" + className + "]: " + message);
	}

	/**
	 * Print a error message.
	 * @param message
	 */
	public static void error(String message) {
		if (message == null) {
			return;
		}
		System.err.println("[" + dateFormat.format(new Date()) + "][" + GameWorld.getName() + "]: " + message);
	}

	/**
	 * Print a error message with class name.
	 * @param thread
	 * @param message
	 */
	public static void error(Class<?> thread, String message) {
		if (message == null) {
			return;
		}
		System.err.println("[" + dateFormat.format(new Date()) + "][" + Class.class.getSimpleName() + "]: " + message);
	}

	/**
	 * Print a error message with class name.
	 * @param thread
	 * @param message
	 */
	public static void error(String className, String message) {
		if (message == null) {
			return;
		}
		System.err.println("[" + dateFormat.format(new Date()) + "][" + className + "]: " + message);
	}
}
