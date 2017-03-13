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

import com.shakhar.util.MyHashMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.json.Json;
import javax.json.stream.JsonParser;

/**
 * Data from 2010 Census. Data is fetched from
 * <a href="http://api.census.gov/data/2010/sf1">2010 Census SF1</a>.
 *
 * @author Shakhar Dasgupta
 */
public class CensusData {

    private Map<String, State> states;
    private Map<String, Place> places;
    private Map<String, Population> populations;

    /**
     * Constructs <tt>CensusData</tt> after fetching data from the Census API.
     *
     * @throws IOException if there is problem connecting to the Census API
     */
    public CensusData() throws IOException {
        fetch();
    }

    /**
     * Fetches data from the Census API. This method is automatically called by
     * the constructor. However, it may be called to refresh data though it is
     * not expected that the Census Data will ever update.
     *
     * @throws IOException if there is problem connecting to the Census API
     */
    public final void fetch() throws IOException {
        URL statesURL = new URL("http://www2.census.gov/geo/docs/reference/state.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(statesURL.openStream()))) {
            states = new MyHashMap<>();
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\\|");
                states.put(tokens[0], new State(tokens[0], tokens[1], tokens[2]));
            }
        }

        URL placesURL = new URL("http://www2.census.gov/geo/docs/reference/codes/files/national_places.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(placesURL.openStream()))) {
            places = new MyHashMap<>();
            String line;
            br.readLine();
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\\|");
                places.put(tokens[1] + tokens[2], new Place(states.get(tokens[1]), tokens[2], tokens[3], tokens[6]));
                br.readLine();
            }
        }

        URL censusURL = new URL("http://api.census.gov/data/2010/sf1?get=P0030001,P0030002,P0030003,P0030004,P0030005,P0030006,P0030007,P0030008&for=place:*");
        try (JsonParser parser = Json.createParser(censusURL.openStream())) {
            populations = new MyHashMap<>();
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
     * Returns a <tt>Map</tt> mapping <tt>Place</tt> names to associated
     * <tt>Population</tt>s.
     *
     * @return a <tt>Map</tt> mapping <tt>Place</tt> names to associated
     * <tt>Population</tt>s.
     */
    public Map<String, Population> getPopulations() {
        return populations;
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
        return (float) Math.sqrt(Math.pow(population1.getWhitePopulation() - population2.getWhitePopulation(), 2) + Math.pow(population1.getBlackPopulation() - population2.getBlackPopulation(), 2) + Math.pow(population1.getAmericanIndianAndAlaskaNativePopulation() - population2.getAmericanIndianAndAlaskaNativePopulation(), 2) + Math.pow(population1.getAsianPopulation() - population2.getAsianPopulation(), 2) + Math.pow(population1.getNativeHawaiianAndOtherPacificIslanderPopulation() - population2.getNativeHawaiianAndOtherPacificIslanderPopulation(), 2) + Math.pow(population1.getOtherRacesPopulation() - population2.getOtherRacesPopulation(), 2) + Math.pow(population1.getMultiRacialPopulation() - population2.getMultiRacialPopulation(), 2));
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
            if (p != population && (state == null || p.getPlace().getState().getName().equals(state)) && (d = euclideanDistance(population, p)) < min) {
                min = d;
                similar = p;
            }
        }

        return similar;
    }

}
