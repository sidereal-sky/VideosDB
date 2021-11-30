package entertainment;

import fileio.MovieInputData;

import java.util.ArrayList;

public class Movie extends Video {
    private ArrayList<Double> ratings;
    private Integer favCount;
    private Integer duration;
    private Integer viewCount;

    public Movie(MovieInputData input) {
        super(input);
        duration = input.getDuration();
        ratings = new ArrayList<>();
        favCount = 0;
        viewCount = 0;
    }

    public ArrayList<Double> getRatings() {
        return ratings;
    }

    public Integer getFavCount() {
        return favCount;
    }

    public void setFavCount(Integer favCount) {
        this.favCount = favCount;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    @Override
    public Double calculateRating() {
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

    @Override
    public String toString() {
        return "Movie{" +
                "title=" + getTitle() +
                " ratings=" + ratings +
                ", favCount=" + favCount +
                ", duration=" + duration +
                ", viewCount=" + viewCount +
                '}';
    }
}
