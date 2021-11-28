package database;

import actor.Actor;
import entertainment.Movie;
import entertainment.Show;
import user.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Database {
    private ArrayList<User> myUsers;
    private ArrayList<Actor> myActors;
    private ArrayList<Movie> myMovies;
    private ArrayList<Show> myShows;

    public Database(ArrayList<User> myUsers, ArrayList<Actor> myActors,
                    ArrayList<Movie> myMovies, ArrayList<Show> myShows) {
        this.myUsers = myUsers;
        this.myActors = myActors;
        this.myMovies = myMovies;
        this.myShows = myShows;
    }

    public ArrayList<User> getMyUsers() {
        return myUsers;
    }

    public User findUser(String username) {
        for (User user: getMyUsers()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public ArrayList<Actor> getMyActors() {
        return myActors;
    }

    public ArrayList<Movie> getMyMovies() {
        return myMovies;
    }

    public Movie findMovie(String videoTitle) {
        for (Movie movie: getMyMovies()) {
            if (movie.getTitle().equals(videoTitle)) {
                return movie;
            }
        }
        return null;
    }

    public ArrayList<Show> getMyShows() {
        return myShows;
    }

    public Show findShow(String videoTitle) {
        for (Show show: getMyShows()) {
            if (show.getTitle().equals(videoTitle)) {
                return show;
            }
        }
        return null;
    }

    public ArrayList<User> getUsersByRating() {
        ArrayList<User> sortedUsers = new ArrayList<>(myUsers);

        sortedUsers.sort(Comparator.comparingInt(User::getNumberOfRatings));
        return sortedUsers;
    }

    public ArrayList<Movie> getMoviesByYear(List<String> yearFilter) {
        if (yearFilter.contains(null)) {
            return myMovies;
        }
        ArrayList<Movie> filteredMovies = new ArrayList<>();
        for(Movie movie: myMovies) {
            if (yearFilter.contains(movie.getYear().toString())) {
                filteredMovies.add(movie);
            }
        }

        return filteredMovies;
    }

    public ArrayList<Movie> getMoviesByGenre(List<String> genreFilter) {
        if (genreFilter.contains(null)) {
            return myMovies;
        }
        ArrayList<Movie> filteredMovies = new ArrayList<>();
        for(Movie movie: myMovies) {
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

    public ArrayList<Movie> getMoviesByRating(List<List<String>> filters, String sortType) {
        ArrayList<Movie> genreFiltered = getMoviesByGenre(filters.get(1));
        ArrayList<Movie> yearFiltered = getMoviesByYear(filters.get(0));

        ArrayList<Movie> filteredMovies = yearFiltered.stream()
                .distinct()
                .filter(genreFiltered::contains)
                .collect(Collectors.toCollection(ArrayList::new));

        filteredMovies.sort((o1, o2) -> {
            if (sortType.equals("asc")) {
                return (int) (o1.CalculateRating() - o2.CalculateRating());
            } else {
                return (int) (o2.CalculateRating() - o1.CalculateRating());
            }
        });

        return filteredMovies;
    }

    public ArrayList<Show> getShowsByYear(List<String> yearFilter) {
        if (yearFilter.contains(null)) {
            return myShows;
        }
        ArrayList<Show> filteredShows = new ArrayList<>();
        for(Show show: myShows) {
            if (yearFilter.contains(show.getYear().toString())) {
                filteredShows.add(show);
            }
        }
        return filteredShows;
    }

    public ArrayList<Show> getShowsByGenre(List<String> genreFilter) {
        if (genreFilter.contains(null)) {
            return myShows;
        }
        ArrayList<Show> filteredShows = new ArrayList<>();
        for(Show show: myShows) {
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

    public ArrayList<Show> getShowsByRating(List<List<String>> filters, String sortType) {
        ArrayList<Show> genreFiltered = getShowsByGenre(filters.get(1));
        ArrayList<Show> yearFiltered = getShowsByYear(filters.get(0));

        ArrayList<Show> filteredShows = yearFiltered.stream()
                .distinct()
                .filter(genreFiltered::contains)
                .collect(Collectors.toCollection(ArrayList::new));

        filteredShows.sort((o1, o2) -> {
            if (sortType.equals("asc")) {
                return (int) (o1.CalculateRating() - o2.CalculateRating());
            } else {
                return (int) (o2.CalculateRating() - o1.CalculateRating());
            }
        });

        return filteredShows;
    }
}
