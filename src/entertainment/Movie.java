package entertainment;

import fileio.ShowInput;

import java.util.ArrayList;

public class Movie extends Video {
    private ArrayList<Double> ratings;

    public Movie(ShowInput input) {
        super(input);
        ratings = new ArrayList<>();
    }

    public ArrayList<Double> getRatings() {
        return ratings;
    }
}
