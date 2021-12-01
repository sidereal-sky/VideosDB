package filter;

import database.Database;

public abstract class Filter {
	private Database database;

	public Filter(Database database) {
		this.database = database;
	}

	public Database getDatabase() {
		return database;
	}
}
