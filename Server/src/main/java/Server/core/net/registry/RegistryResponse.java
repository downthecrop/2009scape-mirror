package core.net.registry;

/**
 * An account registry response.
 * @author Vexia
 *
 */
public enum RegistryResponse {
	SUCCESS(2),
	CONTACT_ERROR(3),
	SERVER_BUSY(7),
	CANNOT_CREATE(9),
	INVALID_BIRTH(10),
	FUTURE_BIRTH(11),
	BIRTH_THIS_YEAR(12),
	BIRTH_LAST_YEAR(13),
	INVALID_COUNTRY(14),
	NOT_AVAILBLE_USER(20),
	INVALID_USERNAME(22),
	INVALID_PASS_LENGTH(30),
	INVALID_PASS(31),
	WEAK_PASS(32),
	PASS_SIMILAR_TO_USER(33);
	
	/**
	 * The response id.
	 */
	private final int id;
	
	/**
	 * Constructs a new {@Code RegistryResponse} {@Code Object}
	 * @param id the id.
	 */
	RegistryResponse(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the id.
	 * @return the id.
	 */
	public int getId() {
		return id;
	}
	
}
