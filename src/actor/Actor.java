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

    public Actor(ActorInputData actor) {
        this.name = actor.getName();
        this.careerDescription = actor.getCareerDescription();
        this.filmography = actor.getFilmography();
        this.awards = actor.getAwards();
        average = 0.0;
    }

    public String getName() {
        return name;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Integer calculateAwardCount() {
        Integer count = 0;
        for (Integer value: awards.values()) {
            count += value;
        }
        return count;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "name='" + name + '\'' +
                '}';
    }
}
