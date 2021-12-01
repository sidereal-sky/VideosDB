package entertainment;

import fileio.SerialInputData;

import java.util.ArrayList;

public class Show extends Video {
    private Integer numberOfSeasons;
    private ArrayList<Season> seasons;


    public Show(SerialInputData input) {
        super(input);
        seasons = input.getSeasons();
        numberOfSeasons = input.getNumberSeason();
    }

    public Integer getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public void calculateRating() {
        double sum = 0.0;
        for (Season season: getSeasons()) {
            Double seasonSum = 0.0;
            for (Double rating: season.getRatings()) {
                seasonSum += rating;
            }
            if (season.getRatings().size() > 0) {
                sum += seasonSum / season.getRatings().size();
            }
        }
        setGrade(sum/numberOfSeasons);
    }

    public Integer calculateDuration() {
        int duration = 0;
        for(Season season: getSeasons()) {
            duration += season.getDuration();
        }
        return duration;
    }

    @Override
    public String toString() {
        return "Show{" +
                "title=" + getTitle() +
                " genres=" + getGenres() +
                " rating=" + getGrade() +
                " viewCount=" + getViewCount() +
                " favCount=" + getFavCount() +
                '}';
    }
}
