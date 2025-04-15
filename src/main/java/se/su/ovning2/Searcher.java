package se.su.ovning2;

import java.util.*;

public class Searcher implements SearchOperations {

  private Map<String, Set<Recording>> recrdingsByArtist = new HashMap<>();
  private Map<String, Set<Recording>> getRecordingsByGenre = new HashMap<>();
  private Map<String, Recording> recrdingsByTitle = new HashMap<>();
  private SortedMap<Integer, Set<Recording>> recrdingsByYear = new TreeMap<>();
  private Set<Recording> allRecordings = new HashSet<>();


  public Searcher(Collection<Recording> data) {
    allRecordings.addAll(data);
    for( Recording rec : allRecordings) {
      String artist = rec.getArtist();
      Set<Recording> sameArtist = recrdingsByArtist.get(artist);
      if( sameArtist == null ) {
        sameArtist = new HashSet<>();
        recrdingsByArtist.put(artist, sameArtist);
      }
      sameArtist.add(rec);
      for(String genre : rec.getGenre()) {
        Set<Recording> sameGenre = getRecordingsByGenre.get(genre);
        if( sameGenre == null ) {
          sameGenre = new HashSet<>();
          getRecordingsByGenre.put(genre, sameGenre);
        }
        sameGenre.add(rec);
      }
      recrdingsByTitle.put(rec.getTitle(), rec);

      int year = rec.getYear();
      Set<Recording> sameYear = recrdingsByYear.get(year);
      if(sameYear == null) {
        sameYear = new HashSet<>();
        recrdingsByYear.put(year, sameYear);
      }
      sameYear.add(rec);
    }
    allRecordings.addAll(data);
  }

  @Override
  public long numberOfArtists() {
    return recrdingsByArtist.size();
  }

  @Override
  public long numberOfGenres() {
    return getRecordingsByGenre.keySet().size();
  }

  @Override
  public long numberOfTitles() {
    return recrdingsByTitle.size();
  }

  @Override
  public boolean doesArtistExist(String name) {
    return recrdingsByArtist.containsKey(name);
  }

  @Override
  public Collection<String> getGenres() {
   return Collections.unmodifiableCollection(getRecordingsByGenre.keySet());
  }

  @Override
  public Recording getRecordingByName(String title) {
    return recrdingsByTitle.get(title);
  }

  @Override
  public Collection<Recording> getRecordingsAfter(int year) {
    Set<Recording> set = new HashSet<>();
    for(Set <Recording> sr : recrdingsByYear.tailMap(year).values()) {
      set.addAll(sr);
    }
    return Collections.unmodifiableCollection(set);
  }

  @Override
  public SortedSet<Recording> getRecordingsByArtistOrderedByYearAsc(String artist) {
    SortedSet<Recording> set = new TreeSet<>(Comparator.comparingInt(Recording::getYear));
    Set<Recording> artistRecordings = recrdingsByArtist.getOrDefault(artist, Collections.emptySet());
    set.addAll(artistRecordings);
    return Collections.unmodifiableSortedSet(set);
  }

  @Override
  public Collection<Recording> getRecordingsByGenre(String genre) {
    return Collections.unmodifiableCollection(getRecordingsByGenre.getOrDefault(genre,Collections.emptySet()));
  }

  @Override
  public Collection<Recording> getRecordingsByGenreAndYear(String genre, int yearFrom, int yearTo) {
    Collection<Recording> recs = new HashSet<>();
    for (Set<Recording> set : recrdingsByYear.subMap(yearFrom, yearTo + 1).values()) {
      for (Recording rec : set) {
        for (String g : rec.getGenre()) {
          if (g.equals(genre)) {
            recs.add(rec);
          }
        }

      }
    }
    return Collections.unmodifiableCollection(recs);
  }

  @Override
  public Collection<Recording> offerHasNewRecordings(Collection<Recording> offered) {
    Set<Recording> missing = new HashSet<>(offered);
    missing.removeAll(allRecordings);
    return Collections.unmodifiableCollection(missing);
  }
}
