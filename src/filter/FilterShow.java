package filter;

import database.Database;
import entertainment.Show;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FilterShow extends FilterVideo {

	public FilterShow(Database database) {
		super(database);
	}

	public ArrayList<Show> getShowsByYear(List<String> yearFilter) {
		if (yearFilter.contains(null)) {
			return getDatabase().getMyShows();
		}
		ArrayList<Show> filteredShows = new ArrayList<>();
		for(Show show: getDatabase().getMyShows()) {
			if (yearFilter.contains(show.getYear().toString())) {
				filteredShows.add(show);
			}
		}
		return filteredShows;
	}

	public ArrayList<Show> getShowsByGenre(List<String> genreFilter) {
		if (genreFilter.contains(null)) {
			return getDatabase().getMyShows();
		}
		ArrayList<Show> filteredShows = new ArrayList<>();
		for(Show show: getDatabase().getMyShows()) {
			List<String> common = show.getGenres().stream()
					.distinct()
					.filter(genreFilter::contains)
					.collect(Collectors.toList());
			if (!common.isEmpty()) {
				filteredShows.add(show);
			}
		}
		return filteredShows;
	}

	public ArrayList<Show> filterShows(List<List<String>> filters) {
		if (filters.isEmpty()) {
			return new ArrayList<>(getDatabase().getMyShows());
		}

		ArrayList<Show> genreFiltered = getShowsByGenre(filters.get(1));
		ArrayList<Show> yearFiltered = getShowsByYear(filters.get(0));

		return yearFiltered.stream()
				.distinct()
				.filter(genreFiltered::contains)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public void getVideoByRating(List<List<String>> filters, String sortType) {
		ArrayList<Show> tempShows = filterShows(filters);

		tempShows.sort((o1, o2) -> {
			int comparison;
			if (sortType.equals("asc")) {
				return Double.compare(o1.getGrade(), o2.getGrade());
			} else {
				return Double.compare(o2.getGrade(), o1.getGrade());
			}
		});
		setFilteredShows(tempShows);
	}

	@Override
	public void getFavouriteVideo(List<List<String>> filters, String sortType,
								  String type) {
		getDatabase().countFavourite();
		ArrayList<Show> tempShows = filterShows(filters);

		tempShows.sort((o1, o2) -> {
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
		setFilteredShows(tempShows);
	}

	@Override
	public void getLongestVideo(List<List<String>> filters, String sortType) {
		ArrayList<Show> tempShows = filterShows(filters);

		tempShows.sort((o1, o2) -> {
			int comparison;
			if (sortType.equals("asc")) {
				comparison = o1.calculateDuration() - o2.calculateDuration();
				if (comparison == 0) {
					comparison = o1.getTitle().compareTo(o2.getTitle());
				}
			} else {
				comparison = o2.calculateDuration() - o1.calculateDuration();
				if (comparison == 0) {
					comparison = o2.getTitle().compareTo(o1.getTitle());
				}
			}
			return comparison;
		});
		setFilteredShows(tempShows);
	}

	@Override
	public void getMostViewedVideo(List<List<String>> filters,
								   String sortType) {
		getDatabase().countViews();
		ArrayList<Show> tempShows = filterShows(filters);

		tempShows.sort((o1, o2) -> {
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
		setFilteredShows(tempShows);
	}
}
