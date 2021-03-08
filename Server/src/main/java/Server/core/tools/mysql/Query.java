package core.tools.mysql;

public class Query {
	
		private String start;
		private StringBuilder content;
		private String end;

		public Query() {
			this.start = "";
			this.content = new StringBuilder();
			this.end = "";
		}

		public String start(String start) {
			this.start = start;
			return this.start;
		}

		public String end(String end) {
			this.end = end;
			return this.end;
		}

		public StringBuilder add(String key, String value) {
			this.content.append(key + "='" + value + "'").append(",");
			return this.content;
		}

		public StringBuilder add(String key, int value) {
			this.content.append(key + "='" + value + "'").append(",");
			return this.content;
		}

		public StringBuilder add(String key, boolean value) {
			this.content.append(key + "='" + (value ? 1 : 0) + "'").append(",");
			return this.content;
		}

		public String total() {
			StringBuilder total = new StringBuilder();
			total.append(this.start);
			total.append(this.content.substring(0, this.content.length() - 1));
			total.append(this.end);
			return total.toString();
		}

	}
