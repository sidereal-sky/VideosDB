package database;

import actor.Actor;
import actor.ActorsAwards;
import entertainment.Movie;
import entertainment.Show;
import user.User;

import java.lang.Double;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

    public ArrayList<User> getUsersByRating(String sortType) {
        ArrayList<User> sortedUsers = new ArrayList<>(myUsers);

        sortedUsers.sort((o1, o2) -> {
            int comparison;
            if (sortType.equals("asc")) {
                comparison = o1.getNumberOfRatings() - o2.getNumberOfRatings();
                if (comparison == 0) {
                    comparison = o1.getUsername().compareTo(o2.getUsername());
                }
            }else {
                comparison = o2.getNumberOfRatings() - o1.getNumberOfRatings();
                if (comparison == 0) {
                    comparison = o2.getUsername().compareTo(o1.getUsername());
                }
            }
            return comparison;
        });
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

    public ArrayList<Movie> filterMovies(List<List<String>> filters) {
        if (filters.isEmpty()) {
            return new ArrayList<>(myMovies);
        }

        ArrayList<Movie> yearFiltered = getMoviesByYear(filters.get(0));
        ArrayList<Movie> genreFiltered = getMoviesByGenre(filters.get(1));

        return yearFiltered.stream()
                .distinct()
                .filter(genreFiltered::contains)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Movie> getMoviesByRating(List<List<String>> filters, String sortType) {
        ArrayList<Movie> filteredMovies = filterMovies(filters);

        filteredMovies.sort((o1, o2) -> {
            if (sortType.equals("asc")) {
                return Double.compare(o1.calculateRating(), o2.calculateRating());
            } else {
                return Double.compare(o2.calculateRating(), o1.calculateRating());
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

    public ArrayList<Show> filterShows(List<List<String>> filters) {
        if (filters.isEmpty()) {
            return new ArrayList<>(myShows);
        }

        ArrayList<Show> genreFiltered = getShowsByGenre(filters.get(1));
        ArrayList<Show> yearFiltered = getShowsByYear(filters.get(0));

        return yearFiltered.stream()
                .distinct()
                .filter(genreFiltered::contains)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Show> getShowsByRating(List<List<String>> filters, String sortType) {
        ArrayList<Show> filteredShows = filterShows(filters);

        filteredShows.sort((o1, o2) -> {
            if (sortType.equals("asc")) {
                return Double.compare(o1.calculateRating(), o2.calculateRating());
            } else {
                return Double.compare(o2.calculateRating(), o1.calculateRating());
            }
        });

        return filteredShows;
    }

    public void countFavourite() {
        for(User user: myUsers) {
            for (String title: user.getFavourite()) {
                Movie movie = findMovie(title);
                if(movie != null) {
                    movie.setFavCount(movie.getFavCount() + 1);
                }
                Show show = findShow(title);
                if (show != null) {
                    show.setFavCount(show.getFavCount() + 1);
                }
            }
        }
    }

    public ArrayList<Movie> getFavouriteMovies(List<List<String>> filters, String sortType) {
        countFavourite();
        ArrayList<Movie> filteredMovies = filterMovies(filters);

        filteredMovies.sort((o1, o2) -> {
            int comparison;
            if (sortType.equals("asc")) {
                comparison = o1.getFavCount() - o2.getFavCount();
                if (comparison == 0) {
                    comparison = o1.getTitle().compareTo(o2.getTitle());
                }
            } else {
                comparison = o2.getFavCount() - o1.getFavCount();
                if (comparison == 0) {
                    comparison = o2.getTitle().compareTo(o1.getTitle());
                }
            }
            return comparison;
        });

//        System.out.println("-----BEGIN----");
//        System.out.println(filteredMovies);
//        System.out.println("-----END-----");

        return filteredMovies;
    }

    public ArrayList<Show> getFavouriteShows(List<List<String>> filters, String sortType) {
        countFavourite();
        ArrayList<Show> filteredShows = filterShows(filters);

        filteredShows.sort((o1, o2) -> {
            int comparison;
            if (sortType.equals("asc")) {
                comparison = o1.getFavCount() - o2.getFavCount();
                if (comparison == 0) {
                    comparison = o1.getTitle().compareTo(o2.getTitle());
                }
            } else {
                comparison = o2.getFavCount() - o1.getFavCount();
                if (comparison == 0) {
                    comparison = o2.getTitle().compareTo(o1.getTitle());
                }
            }
            return comparison;
        });

        return filteredShows;
    }

    public ArrayList<Movie> getLongestMovies(List<List<String>> filters, String sortType) {
        ArrayList<Movie> filteredMovies = filterMovies(filters);

        filteredMovies.sort((o1, o2) -> {
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

        return filteredMovies;
    }

    public ArrayList<Show> getLongestShows(List<List<String>> filters, String sortType) {
        ArrayList<Show> filteredShows = filterShows(filters);

        filteredShows.sort((o1, o2) -> {
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

        return filteredShows;
    }

    public void countViews() {
        for(User user: myUsers) {
            for (Map.Entry<String, Integer> entry: user.getHistory().entrySet()) {
                Movie movie = findMovie(entry.getKey());
                if(movie != null) {
                    movie.setViewCount(movie.getViewCount() + entry.getValue());
                }
                Show show = findShow(entry.getKey());
                if (show != null) {
                    show.setViewCount(show.getViewCount() + entry.getValue());
                }
            }
        }
    }

    public ArrayList<Movie> getMostViewedMovies(List<List<String>> filters, String sortType) {
        countViews();
        ArrayList<Movie> filteredMovies = filterMovies(filters);

        filteredMovies.sort((o1, o2) -> {
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

        return filteredMovies;
    }

    public ArrayList<Show> getMostViewedShows(List<List<String>> filters, String sortType) {
        countViews();
        ArrayList<Show> filteredShows = filterShows(filters);

        filteredShows.sort((o1, o2) -> {
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
        return filteredShows;
    }

    public void calculateActorAvg() {
        for (Actor actor: myActors) {
            Double sum = 0.0;
            int count = 0;
            for (String title: actor.getFilmography()) {
                Movie movie = findMovie(title);
                Show show = findShow(title);

                if (movie != null && movie.calculateRating() > 0) {
                    sum += movie.calculateRating();
                    count += 1;
                } else if (show != null && show.calculateRating() > 0) {
                    sum += show.calculateRating();
                    count += 1;
                }
            }
            actor.setAverage(sum / count);
        }
    }

    public ArrayList<Actor> getActorsByAvg(String sortType) {
        calculateActorAvg();

        ArrayList<Actor> filteredActors = myActors.stream()
                .distinct()
                .filter(s -> s.getAverage() > 0)
                .collect(Collectors.toCollection(ArrayList::new));

        filteredActors.sort((o1, o2) -> {
            if(sortType.equals("asc")) {
                int comparison = Double.compare(o1.getAverage(), o2.getAverage());
                if (comparison == 0) {
                    comparison = o1.getName().compareTo(o2.getName());
                }
                return comparison;
            } else {
                int comparison = Double.compare(o2.getAverage(), o1.getAverage());
                if (comparison == 0) {
                    comparison = o2.getName().compareTo(o1.getName());
                }
                return comparison;
            }
        });
        return filteredActors;
    }

    public ArrayList<Actor> filterActorsByAwards(List<String> filters) {
        if(filters.contains(null)) {
            return new ArrayList<>(myActors);
        }

        ArrayList<Actor> filteredActors = new ArrayList<>();
        for (Actor actor: myActors) {
            ArrayList<String> awards =  new ArrayList<>();

            for (ActorsAwards awardType: ActorsAwards.values()) {
                if (actor.getAwards().containsKey(awardType)) {
                    awards.add(awardType.name());
                }
            }

            if (awards.containsAll(filters)) {
                filteredActors.add(actor);
            }
        }

        return filteredActors;
    }

    public ArrayList<Actor> getActorsByAwards(List<List<String>> filters, String sortType) {
        ArrayList<Actor> filteredActors = filterActorsByAwards(filters.get(3));

        filteredActors.sort(new Comparator<Actor>() {
            @Override
            public int compare(Actor o1, Actor o2) {
                int comparison;
                if(sortType.equals("asc")) {
                    comparison = o1.calculateAwardCount() - o2.calculateAwardCount();
                    if(comparison == 0) {
                        comparison = o1.getName().compareTo(o2.getName());
                    }
                } else {
                    comparison = o2.calculateAwardCount() - o1.calculateAwardCount();
                    if(comparison == 0) {
                        comparison = o2.getName().compareTo(o1.getName());
                    }
                }
                return comparison;
            }
        });
        return filteredActors;
    }

    public ArrayList<Actor> filterActorsByWords(List<String> filters) {
        if (filters.contains(null)) {
            return new ArrayList<>(myActors);
        }

        ArrayList<Actor> filteredActors = new ArrayList<>();
        for (Actor actor: myActors) {
            int count = 0;
            for (String word: filters) {
                if (Arrays.asList(actor.getCareerDescription().toLowerCase().
                        split("[-,:;_ ]")).contains(word)) {
                    count += 1;
                }
            }
            if (count == filters.size()) {
                filteredActors.add(actor);
            }
//            System.out.println(actor.getName() + " " + count);
        }
        return filteredActors;
    }

    public ArrayList<Actor> getActorsByKeywords(List<List<String>> filters, String sortType) {
        ArrayList<Actor> filteredActors = filterActorsByWords(filters.get(2));

        filteredActors.sort(new Comparator<Actor>() {
            @Override
            public int compare(Actor o1, Actor o2) {
                if(sortType.equals("asc")) {
                    return o1.getName().compareTo(o2.getName());
                } else {
                    return o2.getName().compareTo(o1.getName());
                }
            }
        });

        return filteredActors;
    }
}
