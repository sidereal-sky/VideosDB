package filter;

import database.Database;
import entertainment.Show;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FilterShows extends FilterVideos {

    public FilterShows(final Database database) {
        super(database);
    }

    /**
     * @param yearFilter: years
     * @return list of shows that were released that year/years
     */
    public ArrayList<Show> getShowsByYear(final List<String> yearFilter) {
        if (yearFilter.contains(null)) {
            return getDatabase().getMyShows();
        }
        ArrayList<Show> filteredShows = new ArrayList<>();
        for (Show show: getDatabase().getMyShows()) {
            if (yearFilter.contains(show.getYear().toString())) {
                filteredShows.add(show);
            }
        }
        return filteredShows;
    }

    /**
     * @param genreFilter: genres
     * @return list of shows that are of that genre/genres
     */
    public ArrayList<Show> getShowsByGenre(final List<String> genreFilter) {
        if (genreFilter.contains(null)) {
            return getDatabase().getMyShows();
        }
        ArrayList<Show> filteredShows = new ArrayList<>();
        for (Show show: getDatabase().getMyShows()) {
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

    /**
     * @param filters: years and genres
     * @return list of shows that match the filters
     */
    public ArrayList<Show> filterShows(final List<List<String>> filters) {
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

    /**
     * @param filters: filters
     * @param sortType: ascending or descending
     * sort shows based on their rating
     */
    @Override
    public void getVideoByRating(final List<List<String>> filters,
                                 final String sortType) {
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

    /**
     * @param filters: filters
     * @param sortType: ascending or descending
     * @param type: recommendations do not require a 2nd sorting
     * sort shows based on their favourite count
     */
    @Override
    public void getFavouriteVideo(final List<List<String>> filters,
                                  final String sortType,
                                  final String type) {
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

    /**
     * @param filters: filters
     * @param sortType: ascending or descending
     * sort shows based on their duration
     */
    @Override
    public void getLongestVideo(final List<List<String>> filters,
                                final String sortType) {
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

    /**
     * @param filters: filters
     * @param sortType: ascending or descending
     *  sort shows based on their view count
     */
    @Override
    public void getMostViewedVideo(final List<List<String>> filters,
                                   final String sortType) {
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
