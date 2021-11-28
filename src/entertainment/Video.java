package entertainment;

import fileio.ShowInput;

import java.util.ArrayList;

public abstract class Video {
    private String title;
    private Integer year;
    private ArrayList<String> genres;
    private ArrayList<String> cast;

    public Video(ShowInput video) {
        this.title = video.getTitle();
        this.year = video.getYear();
        this.genres = video.getGenres();
        this.cast = video.getCast();
    }


    public String getTitle() {
        return title;
    }

    public Integer getYear() {
        return year;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public ArrayList<String> getCast() {
        return cast;
    }

    public Double CalculateRating() {
        return 0.0;
    }

    @Override
    public String toString() {
        return "Video{" +
                "title='" + title + '\'' +
                ", year=" + year +
                ", genres=" + genres +
                ", cast=" + cast +
                '}';
    }
}
