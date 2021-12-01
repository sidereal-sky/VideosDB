package actor;

import fileio.ActorInputData;

import java.util.ArrayList;
import java.util.Map;

public class Actor {
    private String name;
    private String careerDescription;
    private ArrayList<String> filmography;
    private Map<ActorsAwards, Integer> awards;
    private Double average;

    public Actor(final ActorInputData actor) {
        this.name = actor.getName();
        this.careerDescription = actor.getCareerDescription();
        this.filmography = actor.getFilmography();
        this.awards = actor.getAwards();
        average = 0.0;
    }

    /**
     * @return actor's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return actor's career description
     */
    public String getCareerDescription() {
        return careerDescription;
    }

    /**
     * @return videos the actor played in
     */
    public ArrayList<String> getFilmography() {
        return filmography;
    }

    /**
     * @return awards and number of times the actor won them
     */
    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    /**
     * @return actor's average based on their videos' rating
     */
    public Double getAverage() {
        return average;
    }

    /**
     * @param average: new average
     */
    public void setAverage(final Double average) {
        this.average = average;
    }

    /**
     * @return number of awards won by the actor
     */
    public Integer awardCount() {
        Integer count = 0;
        for (Integer value: awards.values()) {
            count += value;
        }
        return count;
    }
}
