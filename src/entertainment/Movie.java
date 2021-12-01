package entertainment;

import fileio.MovieInputData;

import java.util.ArrayList;

public class Movie extends Video {
    private ArrayList<Double> ratings;
    private Integer duration;

    public Movie(MovieInputData input) {
        super(input);
        duration = input.getDuration();
        ratings = new ArrayList<>();
    }

    public ArrayList<Double> getRatings() {
        return ratings;
    }

    public Integer getDuration() {
        return duration;
    }

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
//        System.out.println(sum/ratings.size());
        setGrade(sum/ratings.size());
    }


    @Override
    public String toString() {
        return "Movie{" +
                "title=" + getTitle() +
                " genres=" + getGenres() +
                " ratings=" + ratings +
                " grade=" + getGrade() +
                ", viewCount=" + getViewCount() +
                '}';
    }
}
