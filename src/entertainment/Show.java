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


    public Double CalculateRating() {
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
}
