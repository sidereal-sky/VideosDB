package action;

import database.Database;
import fileio.ActionInputData;
import fileio.Writer;
import user.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Action {
    private int actionId;
    private String actionType;
    private String type;
    private String username;
    private String objectType;
    private String sortType;
    private String criteria;
    private String title;
    private String genre;
    private int number;
    private double grade;
    private int seasonNumber;
    private List<List<String>> filters = new ArrayList<>();
    private Database database;
    private Writer output;

    public Action(ActionInputData action, Database database, Writer output) {
        this.actionId = action.getActionId();
        this.actionType = action.getActionType();
        this.type = action.getType();
        if (actionType.equals("command")) {
            this.username = action.getUsername();
            this.title = action.getTitle();
            this.grade = action.getGrade();
            this.seasonNumber = action.getSeasonNumber();
        }
        this.database = database;
        this.output = output;
    }

    public void execute(String type) {

    }

    public void writeOutput(String message) {
        try {
            output.writeFile(getActionId(), "", message);
        } catch (IOException e) {
            System.out.println("Couldn't write to file");
        }
    }

    public int getActionId() {
        return actionId;
    }

    public String getActionType() {
        return actionType;
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getObjectType() {
        return objectType;
    }

    public String getSortType() {
        return sortType;
    }

    public String getCriteria() {
        return criteria;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getNumber() {
        return number;
    }

    public double getGrade() {
        return grade;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public List<List<String>> getFilters() {
        return filters;
    }

    public Database getDatabase() {
        return database;
    }

    public Writer getOutput() {
        return output;
    }
}
