package core.tools.mysql;

public class Database {

	private String host;
	private String name;
	private String username;
	private String password;

	public Database(String host, String name, String username, String password) {
		this.host = host;
		this.name = name;
		this.username = username;
		this.password = password;
	}

	public String host() {
		return host;
	}

	public String name() {
		return name;
	}

	public String username() {
		return username;
	}

	public String password() {
		return password;
	}

}