package filter;

import database.Database;
import entertainment.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FilterMovies extends FilterVideos {

    public FilterMovies(final Database database) {
        super(database);
    }

    /**
     * @param yearFilter: years
     * @return list of movies that were released that year/years
     */
    public ArrayList<Movie> getMoviesByYear(final List<String> yearFilter) {
        if (yearFilter.contains(null)) {
            return getDatabase().getMyMovies();
        }
        ArrayList<Movie> filteredMovies = new ArrayList<>();
        for (Movie movie: getDatabase().getMyMovies()) {
            if (yearFilter.contains(movie.getYear().toString())) {
                filteredMovies.add(movie);
            }
        }

        return filteredMovies;
    }

    /**
     * @param genreFilter: genres
     * @return list of movies that are of that genre/genres
     */
    public ArrayList<Movie> getMoviesByGenre(final List<String> genreFilter) {
        if (genreFilter.contains(null)) {
            return getDatabase().getMyMovies();
        }
        ArrayList<Movie> filteredMovies = new ArrayList<>();
        for (Movie movie: getDatabase().getMyMovies()) {
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

    /**
     * @param filters: years and genres
     * @return list of movies that match the filters
     */
    public ArrayList<Movie> filterMovies(final List<List<String>> filters) {
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

    /**
     * @param filters: filters
     * @param sortType: ascending or descending
     * sort movies based on their rating
     */
    @Override
    public void getVideoByRating(final List<List<String>> filters,
                                 final String sortType) {
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

    /**
     * @param filters: filters
     * @param sortType: ascending or descending
     * @param type: recommendations do not require a 2nd sorting
     * sort movies based on their favourite count
     */
    @Override
    public void getFavouriteVideo(final List<List<String>> filters,
                                  final String sortType,
                                  final String type) {
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

    /**
     * @param filters: filters
     * @param sortType: ascending or descending
     * sort movies based on their duration
     */
    @Override
    public void getLongestVideo(final List<List<String>> filters,
                                final String sortType) {
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

    /**
     * @param filters: filters
     * @param sortType: ascending or descending
     *  sort movies based on their view count
     */
    @Override
    public void getMostViewedVideo(final List<List<String>> filters,
                                   final String sortType) {
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
