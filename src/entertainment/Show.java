package entertainment;

import fileio.SerialInputData;

import java.util.ArrayList;

public class Show extends Video {
    private Integer numberOfSeasons;
    private ArrayList<Season> seasons;
    private Integer favCount;
    private Integer viewCount;

    public Show(SerialInputData input) {
        super(input);
        seasons = input.getSeasons();
        numberOfSeasons = input.getNumberSeason();
        favCount = 0;
        viewCount = 0;
    }

    public Integer getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public Integer getFavCount() {
        return favCount;
    }

    public void setFavCount(Integer favCount) {
        this.favCount = favCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Double calculateRating() {
        double sum = 0.0;
        for (Season season: getSeasons()) {
            Double seasonSum = 0.0;
            for (Double rating: season.getRatings()) {
                seasonSum += rating;
            }
            sum += seasonSum/getSeasons().size();
        }
        return sum/numberOfSeasons;
    }

    public Integer calculateDuration() {
        int duration = 0;
        for(Season season: getSeasons()) {
            duration += season.getDuration();
        }
        return duration;
    }
}
