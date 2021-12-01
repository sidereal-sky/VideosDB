package database;

import actor.Actor;
import entertainment.Genre;
import entertainment.Movie;
import entertainment.Show;
import user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Database {
    private ArrayList<User> myUsers;
    private ArrayList<Actor> myActors;
    private ArrayList<Movie> myMovies;
    private ArrayList<Show> myShows;

    public Database(final ArrayList<User> myUsers,
                    final ArrayList<Actor> myActors,
                    final ArrayList<Movie> myMovies,
                    final ArrayList<Show> myShows) {
        this.myUsers = myUsers;
        this.myActors = myActors;
        this.myMovies = myMovies;
        this.myShows = myShows;
    }

    /**
     * @return users in the db
     */
    public ArrayList<User> getMyUsers() {
        return myUsers;
    }

    /**
     * @return actors in the db
     */
    public ArrayList<Actor> getMyActors() {
        return myActors;
    }

    /**
     * @return movies in the db
     */
    public ArrayList<Movie> getMyMovies() {
        return myMovies;
    }

    /**
     * @return tv shows in the db
     */
    public ArrayList<Show> getMyShows() {
        return myShows;
    }

    /**
     * @param username: username
     * @return the user with the given username from the db
     */
    public User findUser(final String username) {
        for (User user: myUsers) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * @param videoTitle: movie title
     * @return the movie with the given title from the db
     */
    public Movie findMovie(final String videoTitle) {
        for (Movie movie: myMovies) {
            if (movie.getTitle().equals(videoTitle)) {
                return movie;
            }
        }
        return null;
    }

    /**
     * @param videoTitle: show title
     * @return the show with the given title from the db
     */
    public Show findShow(final String videoTitle) {
        for (Show show: myShows) {
            if (show.getTitle().equals(videoTitle)) {
                return show;
            }
        }
        return null;
    }

    /**
     * counts how many times a video appears in a user's favourite list
     */
    public void countFavourite() {
        for (User user: myUsers) {
            for (String title: user.getFavourite()) {
                Movie movie = findMovie(title);
                if (movie != null) {
                    movie.setFavCount(movie.getFavCount() + 1);
                }
                Show show = findShow(title);
                if (show != null) {
                    show.setFavCount(show.getFavCount() + 1);
                }
            }
        }
    }

    /**
     * counts how many times a video has been viewed
     */
    public void countViews() {
        for (User user: myUsers) {
            for (Map.Entry<String, Integer>
                    entry: user.getHistory().entrySet()) {
                Movie movie = findMovie(entry.getKey());
                if (movie != null) {
                    movie.setViewCount(movie.getViewCount() + entry.getValue());
                }
                Show show = findShow(entry.getKey());
                if (show != null) {
                    show.setViewCount(show.getViewCount() + entry.getValue());
                }
            }
        }
    }

    /**
     * calculate actors' average based on the ratings of their filmography
     */
    public void calculateActorAvg() {
        for (Actor actor: myActors) {
            Double sum = 0.0;
            int count = 0;
            for (String title: actor.getFilmography()) {
                Movie movie = findMovie(title);
                Show show = findShow(title);

                if (movie != null && movie.getGrade() > 0) {
                    sum += movie.getGrade();
                    count += 1;
                } else if (show != null && show.getGrade() > 0) {
                    sum += show.getGrade();
                    count += 1;
                }
            }
            actor.setAverage(sum / count);
        }
    }

    /**
     * @param unsortedMap hashmap of video genres
     * @return sort hashmap based on values
     */
    public HashMap<String, Integer>
    sortByValues(final HashMap<String, Integer> unsortedMap) {
        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>(unsortedMap.entrySet());

        // sort in descending order by value
        list.sort((o1, o2) -> o2.getValue() - o1.getValue());

        HashMap<String, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry: list) {
            temp.put(entry.getKey(), entry.getValue());
        }
        return temp;
    }

    /**
     * @return hashmap with genre title and popularity
     */
    public HashMap<String, Integer> getPopularGenres() {
        HashMap<String, Integer> genreMap = new HashMap<>();
        for (Genre genre: Genre.values()) {
            genreMap.put(genre.toString().toLowerCase(), 0);
        }

        countViews();

        for (Movie movie: myMovies) {
            for (String genre: movie.formattedGenres()) {
                genreMap.put(genre, genreMap.get(genre) + movie.getViewCount());
            }
        }

        for (Show show: myShows) {
            for (String genre: show.formattedGenres()) {
                genreMap.put(genre, genreMap.get(genre) + show.getViewCount());
            }
        }

        return sortByValues(genreMap);
    }
}
