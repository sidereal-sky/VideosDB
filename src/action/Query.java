package action;

import database.Database;
import entertainment.Movie;
import entertainment.Show;
import entertainment.Video;
import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONArray;
import user.User;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

public class Query extends Action{
	private int number;
	private String sortType;
	private String criteria;
	private List<List<String>> filters;

	public Query(ActionInputData action, Database database, Writer output,
				 JSONArray arrayResult) {
		super(action, database, output, arrayResult);
		setType(action.getObjectType());
		number = action.getNumber();
		sortType = action.getSortType();
		criteria = action.getCriteria();
		filters = action.getFilters();
	}

	public void sortUsers(ArrayList<User> sortedUsers) {
		ArrayList<String> queriedUsers = new ArrayList<>();
		number = min(number, sortedUsers.size());
		for (int i = 0; i < number; i++) {
			if (sortedUsers.get(i).getNumberOfRatings() > 0) {
				queriedUsers.add(sortedUsers.get(i).getUsername());
			}
		}
		message = message + queriedUsers;
		writeOutput();
	}

	public void sortMovies(ArrayList<Movie> sortedVideos) {
//		System.out.println("-------START PRINT-------");
//		System.out.println(sortedVideos);
//		System.out.println("-------END PRINT-----");
		ArrayList<String> queriedVideos = new ArrayList<>();
		number = min(number, sortedVideos.size());
		for (int i = 0; i < number; i++) {
			if (sortedVideos.get(i).CalculateRating() > 0) {
				queriedVideos.add(sortedVideos.get(i).getTitle());
			}
		}
		message = message + queriedVideos;
		writeOutput();
	}

	public void sortShows(ArrayList<Show> sortedShows) {
//		System.out.println("-------START PRINT-------");
//		System.out.println(sortedVideos);
//		System.out.println("-------END PRINT-----");
		ArrayList<String> queriedShows = new ArrayList<>();
		number = min(number, sortedShows.size());
		for (int i = 0; i < number; i++) {
			if (sortedShows.get(i).CalculateRating() > 0) {
				queriedShows.add(sortedShows.get(i).getTitle());
			}
		}
		message = message + queriedShows;
		writeOutput();
	}

	@Override
	public void execute(String type) {
		message = "Query result: ";
		switch (type) {
			case "users":
				sortUsers(getDatabase().getUsersByRating());
				break;
			case "movies":
				if (criteria.equals("ratings")) {
					sortMovies(getDatabase().getMoviesByRating(filters, sortType));
				}
				break;
			case "shows":
				if (criteria.equals("ratings")) {
					sortShows(getDatabase().getShowsByRating(filters, sortType));
				}
				break;
		}
	}

	public int getNumber() {
		return number;
	}

	public String getSortType() {
		return sortType;
	}

	public String getCriteria() {
		return criteria;
	}

	public List<List<String>> getFilters() {
		return filters;
	}
}
