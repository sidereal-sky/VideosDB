package action;

import database.Database;
import entertainment.Movie;
import entertainment.Season;
import entertainment.Show;
import fileio.ActionInputData;
import fileio.Writer;
import user.User;

import java.util.ArrayList;
import java.util.HashMap;

public class Command extends Action {

    public Command(ActionInputData action, Database database, Writer output) {
        super(action, database, output);
    }

    public void addFavourite(User user) {
        System.out.println(user);
        // the user has viewed the video
        String message;
        if (user.getHistory().containsKey(getTitle())) {
            if (!user.getFavourite().contains(getTitle())) {
                user.getFavourite().add(getTitle());
                message = "success -> " + getTitle() + "was added as favourite";
            } else {
                message = "error -> " + getTitle() + " is already in favourite list";
            }
        } else {
            message = "error -> " + getTitle() + " is not seen";
        }
        writeOutput(message);
        System.out.println(user);
    }

    public void viewVideo(User user) {
        if (user.getHistory().containsKey(getTitle())) {
            user.getHistory().put(getTitle(), user.getHistory().get(getTitle()) + 1);
        } else {
            user.getHistory().put(getTitle(), 1);
        }
        Integer views = user.getHistory().get(getTitle());
        String message = "success -> " + getTitle() + " was viewed with total views of " + views;
        writeOutput(message);
    }

    public void rateShow(User user) {
        Show show = getDatabase().findShow(getTitle());
        HashMap<Show, ArrayList<Integer>> ratedSeasonsMap = user.getRatedShows();
        if (ratedSeasonsMap.isEmpty() && user.getHistory().containsKey(getTitle())) {
            ratedSeasonsMap.put(show, new ArrayList<>());
            ratedSeasonsMap.get(show).add(getSeasonNumber());
        }
        // the user viewed the show and hasn't rated that particular season yet
        else if (user.getHistory().containsKey(getTitle()) &&
                !ratedSeasonsMap.get(show).contains(getSeasonNumber())) {
            // the list of seasons rated was empty
            ratedSeasonsMap.get(show).add(getSeasonNumber());

            // add rating to the getSeasonNumber-th Season's ratings list if that makes sense
            show.getSeasons().get(getSeasonNumber()).getRatings().add(getGrade());
        }
    }

    public void rateMovie(User user) {
        Movie movie = getDatabase().findMovie(getTitle());

        // the user viewed the movie and hasn't rated it yet
        if (user.getHistory().containsKey(getTitle()) && !user.getRatedMovies().contains(movie)) {
            user.getRatedMovies().add(movie);
            movie.getRatings().add(getGrade());
        }
    }

    @Override
    public void execute(String type) {
        User user = getDatabase().findUser(getUsername());
        if (user == null) {
            return;
        }

        switch (type) {
            case "favorite":
                addFavourite(user);
                break;
            case "view":
                viewVideo(user);
                break;
//            case "rating":
//                if (getSeasonNumber() > 0) {
//                    rateShow(user);
//                } else {
//                    rateMovie(user);
//                }
//                break;
        }
    }
}
