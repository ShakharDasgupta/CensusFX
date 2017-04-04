/*
 * Copyright (C) 2017 Shakhar Dasgupta
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.shakhar.census;

import com.shakhar.clusterer.Cluster;
import com.shakhar.clusterer.ClusteredPopulation;
import com.shakhar.clusterer.PopulationClusterer;
import com.shakhar.util.BTreeMap;
import com.shakhar.util.HashCache;
import com.shakhar.util.MyHashMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.stream.JsonParser;

/**
 * Data from 2010 Census. Data is fetched from
 * <a href="http://api.census.gov/data/2010/sf1">2010 Census SF1</a>.
 *
 * @author Shakhar Dasgupta
 */
public class CensusData {

    private static final String CACHE_FILENAME = "cache";
    private static final String STATES_URL = "http://www2.census.gov/geo/docs/reference/state.txt";
    private static final String PLACES_URL = "http://www2.census.gov/geo/docs/reference/codes/files/national_places.txt";
    private static final String CENSUS_URL = "http://api.census.gov/data/2010/sf1?get=P0030001,P0030002,P0030003,P0030004,P0030005,P0030006,P0030007,P0030008&for=place:*";
    private static final int BTREE_DEGREE = 32;
    private static final String STATES_BTREE_NAME = "states";
    private static final String POPULATIONS_BTREE_NAME = "populations";
    private static final int CLUSTER_COUNT = 10;
    private static final int MAX_ITERATION_COUNT = 40;

    private final HashCache<String, String> cache;
    private BTreeMap<String, State> states;
    private BTreeMap<String, Population> populations;

    /**
     * Constructs <tt>CensusData</tt> after fetching data from the Census API.
     *
     * @throws IOException if there is problem connecting to the Census API
     */
    public CensusData() throws IOException {
        cache = new HashCache<>(CACHE_FILENAME);
        if (cache.isEmpty()) {
            fetch();
        }
        states = new BTreeMap<>(STATES_BTREE_NAME, BTREE_DEGREE);
        populations = new BTreeMap<>(POPULATIONS_BTREE_NAME, BTREE_DEGREE);
        if (states.isEmpty() || populations.isEmpty()) {
            parse();
        }
    }

    /**
     * Fetches data from the Census API. This method is automatically called by
     * the constructor. However, it may be called to refresh data though it is
     * not expected that the Census Data will ever update.
     *
     * @throws IOException if there is problem connecting to the Census API
     */
    public final void fetch() throws IOException {
        String line;
        StringBuilder lines = new StringBuilder();
        URL statesURL = new URL(STATES_URL);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(statesURL.openStream()))) {
            while ((line = br.readLine()) != null) {
                lines.append(line).append(System.lineSeparator());
            }
        }
        cache.put(STATES_URL, lines.toString());

        lines.setLength(0);
        URL placesURL = new URL(PLACES_URL);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(placesURL.openStream()))) {
            while ((line = br.readLine()) != null) {
                lines.append(line).append(System.lineSeparator());
            }
        }
        cache.put(PLACES_URL, lines.toString());

        lines.setLength(0);
        URL censusURL = new URL(CENSUS_URL);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(censusURL.openStream()))) {
            while ((line = br.readLine()) != null) {
                lines.append(line).append(System.lineSeparator());
            }
        }
        cache.put(CENSUS_URL, lines.toString());
    }

    public final void parse() throws IOException {
        try (BufferedReader br = new BufferedReader(new StringReader(cache.get(STATES_URL)))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\\|");
                states.put(tokens[0], new State(tokens[0], tokens[1], tokens[2]));
            }
        }

        MyHashMap<String, Place> places = new MyHashMap<>();
        try (BufferedReader br = new BufferedReader(new StringReader(cache.get(PLACES_URL)))) {
            String line;
            br.readLine();
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\\|");
                places.put(tokens[1] + tokens[2], new Place(states.get(tokens[1]), tokens[2], tokens[3], tokens[6]));
                br.readLine();
            }
        }

        try (JsonParser parser = Json.createParser(new StringReader(cache.get(CENSUS_URL)))) {
            int c = 0;
            String[] arr = new String[10];
            boolean header = true;
            while (parser.hasNext()) {
                JsonParser.Event event = parser.next();
                switch (event) {
                    case START_ARRAY:
                        c = 0;
                        arr = new String[10];
                        break;
                    case VALUE_STRING:
                        arr[c++] = parser.getString();
                        break;
                    case END_ARRAY:
                        if (!header && arr != null) {
                            Place place = places.get(arr[8] + arr[9]);
                            populations.put(place.toString(), new Population(place, Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), Integer.parseInt(arr[5]), Integer.parseInt(arr[6]), Integer.parseInt(arr[7])));
                        }
                        header = false;
                        arr = null;
                        break;
                }
            }
        }
    }

    /**
     * Returns the <tt>List</tt> of <tt>Place</tt> names.
     *
     * @return the <tt>List</tt> of <tt>Place</tt> names
     */
    public List<String> getPlaceNames() {
        List<String> placeNames = new ArrayList<>();
        for (Population population : populations.values()) {
            placeNames.add(population.getPlace().toString());
        }
        return placeNames;
    }

    /**
     * Returns the <tt>List</tt> of <tt>State</tt> names.
     *
     * @return the <tt>List</tt> of <tt>State</tt> names
     */
    public List<String> getStateNames() {
        List<String> stateNames = new ArrayList<>();
        for (State state : states.values()) {
            stateNames.add(state.getName());
        }
        return stateNames;
    }

    /**
     * Returns the <tt>Population</tt> associated with the specified place name.
     *
     * @param place name of the place
     * @return the <tt>Population</tt> associated with the specified place name
     */
    public Population getPopulationByPlace(String place) {
        return populations.get(place);
    }

    // Returns the euclidean distance between two Population objects. The distance is calculated for every racial category.
    private float euclideanDistance(Population population1, Population population2) {
        float white1Percent = (float) population1.getWhitePopulation() / population1.getTotalPopulation() * 100;
        float black1Percent = (float) population1.getBlackPopulation() / population1.getTotalPopulation() * 100;
        float indian1Percent = (float) population1.getAmericanIndianAndAlaskaNativePopulation() / population1.getTotalPopulation() * 100;
        float asian1Percent = (float) population1.getAsianPopulation() / population1.getTotalPopulation() * 100;
        float hawaiian1Percent = (float) population1.getNativeHawaiianAndOtherPacificIslanderPopulation() / population1.getTotalPopulation() * 100;
        float other1Percent = (float) population1.getOtherRacesPopulation() / population1.getTotalPopulation() * 100;
        float multi1Percent = (float) population1.getMultiRacialPopulation() / population1.getTotalPopulation() * 100;
        float white2Percent = (float) population2.getWhitePopulation() / population2.getTotalPopulation() * 100;
        float black2Percent = (float) population2.getBlackPopulation() / population2.getTotalPopulation() * 100;
        float indian2Percent = (float) population2.getAmericanIndianAndAlaskaNativePopulation() / population2.getTotalPopulation() * 100;
        float asian2Percent = (float) population2.getAsianPopulation() / population2.getTotalPopulation() * 100;
        float hawaiian2Percent = (float) population2.getNativeHawaiianAndOtherPacificIslanderPopulation() / population2.getTotalPopulation() * 100;
        float other2Percent = (float) population2.getOtherRacesPopulation() / population2.getTotalPopulation() * 100;
        float multi2Percent = (float) population2.getMultiRacialPopulation() / population2.getTotalPopulation() * 100;
        
        return (float) Math.sqrt(Math.pow(white1Percent - white2Percent, 2) + Math.pow(black1Percent - black2Percent, 2) + Math.pow(indian1Percent - indian2Percent, 2) + Math.pow(asian1Percent - asian2Percent, 2) + Math.pow(hawaiian1Percent - hawaiian2Percent, 2) + Math.pow(other1Percent - other2Percent, 2) + Math.pow(multi1Percent - multi2Percent, 2));
    }

    /**
     * Returns the <tt>Population</tt> of a place in the specified state which
     * is the most similar to the specified <tt>Population</tt>.
     *
     * @param population <tt>Population</tt> similar to which the other
     * <tt>Population</tt> is to be found
     * @param state name of state where the similar <tt>Population</tt> is to be
     * found, or null to find the most similar <tt>Population</tt> from all
     * states and territories
     * @return the <tt>Population</tt> of a place in the specified state which
     * is the most similar to the specified <tt>Population</tt>
     */
    public Population getSimilarPopulation(Population population, String state) {
        float min = Float.MAX_VALUE;
        Population similar = null;
        float d;
        for (Population p : populations.values()) {
            if (!p.equals(population) && (state == null || p.getPlace().getState().getName().equals(state)) && (d = euclideanDistance(population, p)) < min) {
                min = d;
                similar = p;
            }
        }

        return similar;
    }
    
    public List<Population> getSimilarPopulations(Population population, String state) {
        List<Population> pops = new ArrayList<>();
        pops.add(population);
        for (Population p : populations.values()) {
            if (!p.equals(population) && (state == null || p.getPlace().getState().getName().equals(state))) {
                pops.add(p);
            }
        }
        PopulationClusterer clusterer = new PopulationClusterer(CLUSTER_COUNT, MAX_ITERATION_COUNT);
        List<Cluster> clusters = clusterer.cluster(pops);
        pops = new ArrayList<>();
        for(Cluster c : clusters) {
            if(c.getPopulations().contains(population)) {
                for(ClusteredPopulation cp : c.getPopulations()) {
                    if(!population.equals(cp))
                        pops.add((Population)cp);
                }
                break;
            }
        }
        return pops;
    }

}
