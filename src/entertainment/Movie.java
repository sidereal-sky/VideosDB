package entertainment;

import fileio.MovieInputData;

import java.util.ArrayList;

public class Movie extends Video {
    private ArrayList<Double> ratings;
    private Integer duration;

    public Movie(final MovieInputData input) {
        super(input);
        duration = input.getDuration();
        ratings = new ArrayList<>();
    }

    /**
     * @return video's list of grades
     */
    public ArrayList<Double> getRatings() {
        return ratings;
    }

    /**
     * @return video's duration
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * calculate average grade
     */
    @Override
    public void calculateRating() {
        if (ratings.size() == 0) {
            setGrade(0.0);
            return;
        }
        Double sum = 0.0;
        for (Double grade: ratings) {
            sum += grade;
        }
        setGrade(sum / ratings.size());
    }
}
