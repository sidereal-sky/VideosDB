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

    @Override
    public Double CalculateRating() {
        if (ratings.size() == 0) {
            return 0.0;
        }
        Double sum = 0.0;
        for (Double grade: ratings) {
            sum += grade;
        }
//        System.out.println(sum/ratings.size());
        return sum/ratings.size();
    }

}
