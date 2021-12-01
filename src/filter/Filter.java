package filter;

import database.Database;

public abstract class Filter {
    private Database database;

    public Filter(final Database database) {
        this.database = database;
    }

    /**
     * @return database of users, actors and videos
     */
    public Database getDatabase() {
        return database;
    }
}
