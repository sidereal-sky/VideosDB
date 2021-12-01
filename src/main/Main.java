package main;

import action.Action;
import action.Command;
import action.Query;
import action.Recommendation;
import actor.Actor;
import checker.Checkstyle;
import checker.Checker;
import common.Constants;
import database.Database;
import entertainment.Movie;
import entertainment.Show;
import fileio.ActionInputData;
import fileio.ActorInputData;
import fileio.Input;
import fileio.InputLoader;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import fileio.Writer;
import org.checkerframework.checker.units.qual.A;
import org.json.simple.JSONArray;
import user.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        //TODO add here the entry point to your implementation
        ArrayList<User> myUsers = new ArrayList<>();
        for (UserInputData user: input.getUsers()) {
            myUsers.add(new User(user));
        }

        ArrayList<Movie> myMovies = new ArrayList<>();
        for (MovieInputData movie: input.getMovies()) {
            myMovies.add(new Movie(movie));
        }

        ArrayList<Show> myShows = new ArrayList<>();
        for (SerialInputData show: input.getSerials()) {
            myShows.add(new Show(show));
        }

        ArrayList<Actor> myActors = new ArrayList<>();
        for (ActorInputData actor: input.getActors()) {
            myActors.add(new Actor(actor));
        }

        Database database = new Database(myUsers, myActors, myMovies, myShows);


        ArrayList<Action> myActions = new ArrayList<>();
        for (ActionInputData action: input.getCommands()) {
            if (action.getActionType().equals("command")) {
                myActions.add(new Command(action, database, fileWriter, arrayResult));
            } else if (action.getActionType().equals("query")) {
                myActions.add(new Query(action, database, fileWriter, arrayResult));
            } else if (action.getActionType().equals("recommendation")) {
                myActions.add(new Recommendation(action, database, fileWriter, arrayResult));
            }
        }

        for (Action action: myActions) {
//            System.out.println("------BEGIN ACTION----");
            action.execute(action.getType());
//            System.out.println("------END ACTION-----");
        }


        fileWriter.closeJSON(arrayResult);
    }
}
