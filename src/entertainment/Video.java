package entertainment;

import fileio.ShowInput;

import java.util.ArrayList;

public abstract class Video {
    private String title;
    private Integer year;
    private ArrayList<String> genres;
    private ArrayList<String> cast;
    private Double grade;
    private Integer favCount;
    private Integer viewCount;

    public Video(ShowInput video) {
        this.title = video.getTitle();
        this.year = video.getYear();
        this.genres = video.getGenres();
        this.cast = video.getCast();
        grade = 0.0;
        favCount = 0;
        viewCount = 0;
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

    public Double getGrade() {
        calculateRating();
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
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

    public void calculateRating() {

    }

    public void calculateFavCount() {

    }

    public void calculateViewCount() {

    }

    public ArrayList<String> formattedGenres() {
        ArrayList<String> formatted = new ArrayList<>();

        for (String genre: genres) {
            genre = genre.replace(" & ", " ");
            genre = genre.replace("-", "_");
            genre = genre.replace(" ", "_");
            genre = genre.toLowerCase();
            formatted.add(genre);
        }
        return formatted;
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
