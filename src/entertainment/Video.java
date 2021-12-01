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

    public Video(final ShowInput video) {
        this.title = video.getTitle();
        this.year = video.getYear();
        this.genres = video.getGenres();
        this.cast = video.getCast();
        grade = 0.0;
        favCount = 0;
        viewCount = 0;
    }

    /**
     * @return video's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return year the video was released
     */
    public Integer getYear() {
        return year;
    }

    /**
     * @return list of genres
     */
    public ArrayList<String> getGenres() {
        return genres;
    }

    /**
     * @return list of actors that play in the video
     */
    public ArrayList<String> getCast() {
        return cast;
    }

    /**
     * @return average grade based on user rating
     */
    public Double getGrade() {
        calculateRating();
        return grade;
    }

    /**
     * @param grade: new grade
     */
    public void setGrade(final Double grade) {
        this.grade = grade;
    }

    /**
     * @return times the video has appeared in users' favorite list
     */
    public Integer getFavCount() {
        return favCount;
    }

    /**
     * @param favCount: new count
     */
    public void setFavCount(final Integer favCount) {
        this.favCount = favCount;
    }

    /**
     * @return times the video has been viewed
     */
    public Integer getViewCount() {
        return viewCount;
    }

    /**
     * @param viewCount: new count
     */
    public void setViewCount(final Integer viewCount) {
        this.viewCount = viewCount;
    }

    /**
     * calculate video rating
     */
    public void calculateRating() {

    }

    /**
     * @return modify genre name to match operation filters
     */
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
}
