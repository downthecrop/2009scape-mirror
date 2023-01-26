package core.net.registry;

import java.sql.Date;

/**
 * Represents details of an account to register.
 * @author Vexia
 *
 */
public class RegistryDetails {

	/**
	 * The username.
	 */
	private final String username;
	
	/**
	 * The password.
	 */
	private final String password;
	
	/**
	 * The date of birth.
	 */
	private final Date birth;
	
	/**
	 * The country.
	 */
	private final int country;
	
	/**
	 * Constructs a new {@Code RegistryDetails} {@Code Object}
	 * @param username The username to register.
	 * @param password The password to register.
	 * @param birth The birth year.
	 * @param country The country code.
	 */
	public RegistryDetails(String username, String password, Date birth, int country) {
		this.username = username;
		this.password = password;
		this.birth = birth;
		this.country = country;
	}

	/**
	 * Gets the username.
	 * @return the username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Gets the password.
	 * @return the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Gets the birth.
	 * @return the birth.
	 */
	public Date getBirth() {
		return birth;
	}

	/**
	 * Gets the country.
	 * @return the country.
	 */
	public int getCountry() {
		return country;
	}

}
