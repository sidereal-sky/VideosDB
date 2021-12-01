package filter;

import actor.Actor;
import actor.ActorsAwards;
import database.Database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FilterActors extends Filter{

	public FilterActors(Database database) {
		super(database);
	}

	public ArrayList<Actor> getActorsByAvg(String sortType) {
		getDatabase().calculateActorAvg();

		ArrayList<Actor> filteredActors = getDatabase().getMyActors().stream()
				.distinct()
				.filter(s -> s.getAverage() > 0)
				.collect(Collectors.toCollection(ArrayList::new));

		filteredActors.sort((o1, o2) -> {
			int comparison;
			if(sortType.equals("asc")) {
				comparison = Double.compare(o1.getAverage(), o2.getAverage());
				if (comparison == 0) {
					comparison = o1.getName().compareTo(o2.getName());
				}
			} else {
				comparison = Double.compare(o2.getAverage(), o1.getAverage());
				if (comparison == 0) {
					comparison = o2.getName().compareTo(o1.getName());
				}
			}
			return comparison;
		});
		return filteredActors;
	}

	public ArrayList<Actor> filterActorsByAwards(List<String> filters) {
		if(filters.contains(null)) {
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

	public ArrayList<Actor> getActorsByAwards(List<List<String>> filters,
											  String sortType) {
		ArrayList<Actor> filteredActors = filterActorsByAwards(filters.get(3));

		filteredActors.sort(new Comparator<Actor>() {
			@Override
			public int compare(Actor o1, Actor o2) {
				int comparison;
				if(sortType.equals("asc")) {
					comparison = o1.awardCount() - o2.awardCount();
					if(comparison == 0) {
						comparison = o1.getName().compareTo(o2.getName());
					}
				} else {
					comparison = o2.awardCount() - o1.awardCount();
					if(comparison == 0) {
						comparison = o2.getName().compareTo(o1.getName());
					}
				}
				return comparison;
			}
		});
		return filteredActors;
	}

	public ArrayList<Actor> filterActorsByWords(List<String> filters) {
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

	public ArrayList<Actor> getActorsByKeywords(List<List<String>> filters, String sortType) {
		ArrayList<Actor> filteredActors = filterActorsByWords(filters.get(2));

		filteredActors.sort(new Comparator<Actor>() {
			@Override
			public int compare(Actor o1, Actor o2) {
				if(sortType.equals("asc")) {
					return o1.getName().compareTo(o2.getName());
				} else {
					return o2.getName().compareTo(o1.getName());
				}
			}
		});

		return filteredActors;
	}
}
