package action;

import actor.Actor;
import database.Database;
import filter.FilterActors;
import filter.FilterMovie;
import filter.FilterShow;
import filter.FilterUsers;
import entertainment.Movie;
import entertainment.Show;
import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONArray;
import user.User;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

public class Query extends Action {
	private int number;
	private String sortType;
	private String criteria;
	private List<List<String>> filters;

	public Query(final ActionInputData action, final Database database,
				 final Writer output, final JSONArray arrayResult) {
		super(action, database, output, arrayResult);
		setType(action.getObjectType());
		number = action.getNumber();
		sortType = action.getSortType();
		criteria = action.getCriteria();
		filters = action.getFilters();
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setFilters(List<List<String>> filters) {
		this.filters = filters;
	}

	public void sortUsers(final ArrayList<User> sortedUsers) {
		ArrayList<String> queriedUsers = new ArrayList<>();
		sortedUsers.removeIf(s -> s.getNumberOfRatings() == 0);

		number = min(number, sortedUsers.size());
		for (int i = 0; i < number; i++) {
			queriedUsers.add(sortedUsers.get(i).getUsername());

		}
		message = message + queriedUsers;
		writeOutput();
	}

	public void sortMovies(final ArrayList<Movie> sortedMovies,
						   final String criteria) {
		switch (criteria) {
			case "ratings" -> sortedMovies.removeIf(s -> s.getGrade() == 0);
			case "favorite" -> sortedMovies.removeIf(s -> s.getFavCount() == 0);
			case "most_viewed" -> sortedMovies.removeIf(
					s -> s.getViewCount() == 0);
		}

		number = min(number, sortedMovies.size());
		ArrayList<String> queriedMovies = new ArrayList<>();

		for (int i = 0; i < number; i++) {
			queriedMovies.add(sortedMovies.get(i).getTitle());
		}
		message = message + queriedMovies;
		writeOutput();
	}

	public void sortShows(ArrayList<Show> sortedShows, String criteria) {
		ArrayList<String> queriedShows = new ArrayList<>();

		if ("ratings".equals(criteria)) {
			sortedShows.removeIf(s -> s.getGrade() == 0);
		} else if ("favorite".equals(criteria)) {
			sortedShows.removeIf(s -> s.getFavCount() == 0);
		} else if ("most_viewed".equals(criteria)) {
			sortedShows.removeIf(s -> s.getViewCount() == 0);
		}

		number = min(number, sortedShows.size());

		for (int i = 0; i < number; i++) {
			queriedShows.add(sortedShows.get(i).getTitle());
		}
		message = message + queriedShows;
		writeOutput();
	}

	public void getActors(ArrayList<Actor> sortedActors) {
		ArrayList<String> queriedActors = new ArrayList<>();
		number = min(number, sortedActors.size());

		for (int i = 0; i < getNumber(); i++) {
			queriedActors.add(sortedActors.get(i).getName());
		}
		message = message + queriedActors;
		writeOutput();
	}

	@Override
	public void execute(String type) {
		message = "Query result: ";

		if ("users".equals(type)) {
			FilterUsers filterUsers = new FilterUsers(getDatabase());
			sortUsers(filterUsers.getUsersByRating(sortType));
		} else if ("actors".equals(type)) {
			FilterActors filterActors = new FilterActors(getDatabase());
			switch (criteria) {
				case "average" -> getActors(
						filterActors.getActorsByAvg(sortType));
				case "awards" -> getActors(
						filterActors.getActorsByAwards(filters, sortType));
				case "filter_description" -> getActors(
						filterActors.getActorsByKeywords(filters, sortType));
			}
		} else {
			FilterMovie filterMovie = new FilterMovie(getDatabase());
			FilterShow filterShow = new FilterShow(getDatabase());
			switch (criteria) {
				case "ratings" -> {
					filterMovie.getVideoByRating(filters, sortType);
					filterShow.getVideoByRating(filters, sortType);
				}
				case "favorite" -> {
					filterMovie.getFavouriteVideo(filters, sortType, "");
					filterShow.getFavouriteVideo(filters, sortType, "");
				}
				case "longest" -> {
					filterMovie.getLongestVideo(filters, sortType);
					filterShow.getLongestVideo(filters, sortType);
				}
				default -> {
					filterMovie.getMostViewedVideo(filters, sortType);
					filterShow.getMostViewedVideo(filters, sortType);
				}
			}

			if (type.equals("movies")) {
				sortMovies(filterMovie.getFilteredMovies(), criteria);
			} else {
				sortShows(filterShow.getFilteredShows(), criteria);
			}
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
