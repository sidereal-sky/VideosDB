package filter;

import database.Database;
import entertainment.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FilterMovie extends FilterVideo {

	public FilterMovie(Database database) {
		super(database);
	}

	public ArrayList<Movie> getMoviesByYear(List<String> yearFilter) {
		if (yearFilter.contains(null)) {
			return getDatabase().getMyMovies();
		}
		ArrayList<Movie> filteredMovies = new ArrayList<>();
		for(Movie movie: getDatabase().getMyMovies()) {
			if (yearFilter.contains(movie.getYear().toString())) {
				filteredMovies.add(movie);
			}
		}

		return filteredMovies;
	}

	public ArrayList<Movie> getMoviesByGenre(List<String> genreFilter) {
		if (genreFilter.contains(null)) {
			return getDatabase().getMyMovies();
		}
		ArrayList<Movie> filteredMovies = new ArrayList<>();
		for(Movie movie: getDatabase().getMyMovies()) {
			List<String> common = movie.getGenres().stream()
					.distinct()
					.filter(genreFilter::contains)
					.collect(Collectors.toList());
			if (!common.isEmpty()) {
				filteredMovies.add(movie);
			}
		}
		return filteredMovies;
	}

	public ArrayList<Movie> filterMovies(List<List<String>> filters) {
		if (filters.isEmpty()) {
			return new ArrayList<>(getDatabase().getMyMovies());
		}

		ArrayList<Movie> yearFiltered = getMoviesByYear(filters.get(0));
		ArrayList<Movie> genreFiltered = getMoviesByGenre(filters.get(1));

		return yearFiltered.stream()
				.distinct()
				.filter(genreFiltered::contains)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public void getVideoByRating(List<List<String>> filters,
											 String sortType) {
		ArrayList<Movie> tempMovies = filterMovies(filters);

		tempMovies.sort((o1, o2) -> {
			if (sortType.equals("asc")) {
				return Double.compare(o1.getGrade(), o2.getGrade());
			} else {
				return Double.compare(o2.getGrade(), o1.getGrade());
			}
		});
		setFilteredMovies(tempMovies);
	}

	@Override
	public void getFavouriteVideo(List<List<String>> filters, String sortType,
								  String type) {
		getDatabase().countFavourite();
		ArrayList<Movie> tempMovies = filterMovies(filters);

		tempMovies.sort((o1, o2) -> {
			int comparison;
			if (sortType.equals("asc")) {
				comparison = o1.getFavCount() - o2.getFavCount();
				if (comparison == 0 && !type.equals("recom")) {
					comparison = o1.getTitle().compareTo(o2.getTitle());
				}
			} else {
				comparison = o2.getFavCount() - o1.getFavCount();
				if (comparison == 0 && !type.equals("recom")) {
					comparison = o2.getTitle().compareTo(o1.getTitle());
				}
			}
			return comparison;
		});
		setFilteredMovies(tempMovies);
	}

	@Override
	public void getLongestVideo(List<List<String>> filters, String sortType) {
		ArrayList<Movie> tempMovies = filterMovies(filters);

		tempMovies.sort((o1, o2) -> {
			int comparison;
			if (sortType.equals("asc")) {
				comparison = o1.getDuration() - o2.getDuration();
				if (comparison == 0) {
					comparison = o1.getTitle().compareTo(o2.getTitle());
				}
			} else {
				comparison = o2.getDuration() - o1.getDuration();
				if (comparison == 0) {
					comparison = o2.getTitle().compareTo(o1.getTitle());
				}
			}
			return comparison;
		});
		setFilteredMovies(tempMovies);
	}

	@Override
	public void getMostViewedVideo(List<List<String>> filters,
								   String sortType) {
		getDatabase().countViews();
		ArrayList<Movie> tempMovies = filterMovies(filters);

		tempMovies.sort((o1, o2) -> {
			int comparison;
			if (sortType.equals("asc")) {
				comparison = o1.getViewCount() - o2.getViewCount();
				if (comparison == 0) {
					comparison = o1.getTitle().compareTo(o2.getTitle());
				}
			} else {
				comparison = o2.getViewCount() - o1.getViewCount();
				if (comparison == 0) {
					comparison = o2.getTitle().compareTo(o1.getTitle());
				}
			}
			return comparison;
		});
		setFilteredMovies(tempMovies);
	}
}
