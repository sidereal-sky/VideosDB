package database;

import actor.Actor;
import entertainment.Genre;
import entertainment.Movie;
import entertainment.Show;
import user.User;

import java.lang.Double;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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

	public ArrayList<Actor> getMyActors() {
		return myActors;
	}

	public ArrayList<Movie> getMyMovies() {
		return myMovies;
	}

	public ArrayList<Show> getMyShows() {
		return myShows;
	}

	public User findUser(String username) {
		for (User user: myUsers) {
			if (user.getUsername().equals(username)) {
				return user;
			}
		}
		return null;
	}

	public Movie findMovie(String videoTitle) {
		for (Movie movie: myMovies) {
			if (movie.getTitle().equals(videoTitle)) {
				return movie;
			}
		}
		return null;
	}

	public Show findShow(String videoTitle) {
		for (Show show: myShows) {
			if (show.getTitle().equals(videoTitle)) {
				return show;
			}
		}
		return null;
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

	public void countViews() {
		for(User user: myUsers) {
			for (Map.Entry<String, Integer> entry:
					user.getHistory().entrySet()) {
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

	public HashMap<String, Integer> sortByValues(HashMap<String, Integer> unsortedMap) {
		ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>(unsortedMap.entrySet());

		// sort in descending order by value
		list.sort(new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return o2.getValue() - o1.getValue();
			}
		});

		HashMap<String, Integer> temp = new LinkedHashMap<>();
		for (Map.Entry<String, Integer> entry: list) {
			temp.put(entry.getKey(), entry.getValue());
		}
		return temp;
	}

	public HashMap<String, Integer> getPopularGenres () {
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
