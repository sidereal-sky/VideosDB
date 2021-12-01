package filter;

import actor.Actor;
import actor.ActorsAwards;
import database.Database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FilterActors extends Filter {

    public FilterActors(final Database database) {
        super(database);
    }

    /**
     * @param sortType: ascending or descending
     * @return list of actors sorted by their average
     */
    public ArrayList<Actor> getActorsByAvg(final String sortType) {
        getDatabase().calculateActorAvg();

        return getDatabase().getMyActors().stream()
                .distinct()
                .filter(s -> s.getAverage() > 0).sorted((o1, o2) -> {
                    int comparison;
                    if (sortType.equals("asc")) {
                        comparison = Double.compare(o1.getAverage(),
                                o2.getAverage());
                        if (comparison == 0) {
                            comparison = o1.getName().compareTo(o2.getName());
                        }
                    } else {
                        comparison = Double.compare(o2.getAverage(),
                                o1.getAverage());
                        if (comparison == 0) {
                            comparison = o2.getName().compareTo(o1.getName());
                        }
                    }
                    return comparison;
                }).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * @param filters: types of awards
     * @return list of actors that have won all the filters awards
     */
    public ArrayList<Actor> filterActorsByAwards(final List<String> filters) {
        if (filters.contains(null)) {
            return new ArrayList<>(getDatabase().getMyActors());
        }

        ArrayList<Actor> filteredActors = new ArrayList<>();
        for (Actor actor: getDatabase().getMyActors()) {
            ArrayList<String> awards =  new ArrayList<>();

            for (ActorsAwards awardType: ActorsAwards.values()) {
                if (actor.getAwards().containsKey(awardType)) {
                    awards.add(awardType.name());
                }
            }

            if (awards.containsAll(filters)) {
                filteredActors.add(actor);
            }
        }

        return filteredActors;
    }

    /**
     * @param filters: operation filters
     * @param sortType: ascending or descending
     * @return list of actors sorted based on how many awards they have won
     */
    public ArrayList<Actor> getActorsByAwards(final List<List<String>> filters,
                                              final String sortType) {
        int awardFilter = 0;
        while (filters.get(awardFilter) != null) {
            awardFilter += 1;
        }
        awardFilter++;
        // a bit of hardcoding here, I know
        ArrayList<Actor> filteredActors =
                filterActorsByAwards(filters.get(awardFilter));

        filteredActors.sort((o1, o2) -> {
            int comparison;
            if (sortType.equals("asc")) {
                comparison = o1.awardCount() - o2.awardCount();
                if (comparison == 0) {
                    comparison = o1.getName().compareTo(o2.getName());
                }
            } else {
                comparison = o2.awardCount() - o1.awardCount();
                if (comparison == 0) {
                    comparison = o2.getName().compareTo(o1.getName());
                }
            }
            return comparison;
        });
        return filteredActors;
    }

    /**
     * @param filters: list of keywords
     * @return list of actors whose descriptions contain all keywords
     */
    public ArrayList<Actor> filterActorsByWords(final List<String> filters) {
        if (filters.contains(null)) {
            return new ArrayList<>(getDatabase().getMyActors());
        }

        ArrayList<Actor> filteredActors = new ArrayList<>();
        for (Actor actor: getDatabase().getMyActors()) {
            int count = 0;
            for (String word: filters) {
                if (Arrays.asList(actor.getCareerDescription().toLowerCase().
                        split("[-,:;_. ]")).contains(word)) {
                    count += 1;
                }
            }
            if (count == filters.size()) {
                filteredActors.add(actor);
            }
        }
        return filteredActors;
    }

    /**
     * @param filters: operation filters
     * @param sortType: ascending or descending
     * @return list of actors sorted by keywords and names
     */
    public ArrayList<Actor> getActorsByKeywords(final List<List<String>> filters,
                                                final String sortType) {
        ArrayList<Actor> filteredActors = filterActorsByWords(filters.get(2));

        filteredActors.sort((o1, o2) -> {
            if (sortType.equals("asc")) {
                return o1.getName().compareTo(o2.getName());
            } else {
                return o2.getName().compareTo(o1.getName());
            }
        });
        return filteredActors;
    }
}
