package core.tools.mysql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class Results {

	private ResultSet set;

	public Results(ResultSet set) {
		this.set = set;
	}

	public String string(String column) {

		try {

			String result = set().getString(column);

			return result;

		} catch (Exception e) {
			System.out.println("Column '" + column + "' not found.");
		}

		return null;

	}

	public int integer(String column) {

		try {

			int result = set().getInt(column);

			return result;

		} catch (Exception e) {
			System.out.println("Column '" + column + "' not found.");
		}

		return -1;

	}

	public List<String> columns() {

		try {

			ResultSetMetaData meta = set().getMetaData();
			int count = meta.getColumnCount();

			List<String> columns = new ArrayList<String>(count);

			for (int i = 1; i <= count; i++)
				columns.add(meta.getColumnName(i));

			return columns;

		} catch (Exception e) {
			System.out.println("Unable to gather columns.");
		}

		return null;

	}

	public boolean empty() {

		try {
			return !set().next();
		} catch (Exception e) {
			System.out.println("Error occurred, while checking for results.");
		}

		return true;
	}

	public static int[] integers(String[] values) {

		int[] integers = new int[values.length];

		for (int i = 0; i < integers.length; i++)
			integers[i] = Integer.parseInt(values[i]);

		return integers;

	}

	public static double[] doubles(String[] values) {

		double[] integers = new double[values.length];

		for (int i = 0; i < integers.length; i++)
			integers[i] = Double.parseDouble(values[i]);

		return integers;

	}

	public ResultSet set() {
		return set;
	}

}
