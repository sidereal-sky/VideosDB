package entertainment;

import fileio.SerialInputData;

import java.util.ArrayList;

public class Show extends Video {
    private Integer numberOfSeasons;
    private ArrayList<Season> seasons;


    public Show(final SerialInputData input) {
        super(input);
        seasons = input.getSeasons();
        numberOfSeasons = input.getNumberSeason();
    }

    /**
     * @return list of seasons
     */
    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    /**
     * calculate show's average grade
     */
    public void calculateRating() {
        double sum = 0.0;
        for (Season season: seasons) {
            Double seasonSum = 0.0;
            for (Double rating: season.getRatings()) {
                seasonSum += rating;
            }
            if (season.getRatings().size() > 0) {
                sum += seasonSum / season.getRatings().size();
            }
        }
        setGrade(sum / numberOfSeasons);
    }

    /**
     * @return calculate show's duration
     */
    public Integer calculateDuration() {
        int duration = 0;
        for (Season season: seasons) {
            duration += season.getDuration();
        }
        return duration;
    }
}
