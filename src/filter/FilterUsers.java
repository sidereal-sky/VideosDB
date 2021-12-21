package filter;

import database.Database;
import user.User;

import java.util.ArrayList;

public class FilterUsers extends Filter {

    public FilterUsers(final Database database) {
        super(database);
    }

    /**
     * @param sortType: ascending or descending
     * @return list of users sorted by the number of times they rated videos
     */
    public ArrayList<User> getUsersByRating(final String sortType) {
        ArrayList<User> sortedUsers = new ArrayList<>(
                getDatabase().getMyUsers());

        sortedUsers.sort((o1, o2) -> {
            int comparison;
            if (sortType.equals("asc")) {
                comparison = o1.getNumberOfRatings() - o2.getNumberOfRatings();
                if (comparison == 0) {
                    comparison = o1.getUsername().compareTo(o2.getUsername());
                }
            } else {
                comparison = o2.getNumberOfRatings() - o1.getNumberOfRatings();
                if (comparison == 0) {
                    comparison = o2.getUsername().compareTo(o1.getUsername());
                }
            }
            return comparison;
        });
        return sortedUsers;
    }
}
