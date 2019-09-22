package org.crandor;

public class Util {

	/**
	 * Capitalize the first letter of the string
	 * @return Capitalized string
	 */
	public static String capitalize(String name) {
		if (name != null && name.length() != 0) {
			char[] chars = name.toCharArray();
			chars[0] = Character.toUpperCase(chars[0]);
			return new String(chars);
		} else {
			return name;
		}
	}
	
	public static String strToEnum(String name) {
		name = name.toUpperCase();
		return name.replaceAll(" ", "_");
	}
	
	public static String enumToString(String name) {
		name = name.toLowerCase();
		name = name.replaceAll("_", " ");
		return capitalize(name);
	}
}
