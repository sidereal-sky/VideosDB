package entertainment;

import fileio.SerialInputData;

import java.util.ArrayList;

public class Show extends Video {
    private Integer numberOfSeasons;
    private ArrayList<Season> seasons;
    private ArrayList<Double> ratings;

    public Show(SerialInputData input) {
        super(input);
        seasons = input.getSeasons();
        numberOfSeasons = input.getNumberSeason();
        ratings = new ArrayList<>();
    }

    public Integer getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public ArrayList<Double> getRatings() {
        return ratings;
    }
}
