package entertainment;

import fileio.ShowInput;

import java.util.ArrayList;

public class Show extends Video {
    private ArrayList<Season> seasons;

    public Show(ShowInput input) {
        super(input);
        seasons = new ArrayList<>();
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }
}
