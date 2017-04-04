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

import java.io.Serializable;
import java.util.Objects;

/**
 * Population profile for a <tt>Place</tt>. The profile contains the population
 * of each racial category defined by the US Census Bureau.
 *
 * @author Shakhar Dasgupta
 */
public class Population implements Serializable {

    private final Place place;
    private final int totalPopulation;
    private final int whitePopulation;
    private final int blackPopulation;
    private final int americanIndianAndAlaskaNativePopulation;
    private final int asianPopulation;
    private final int nativeHawaiianAndOtherPacificIslanderPopulation;
    private final int otherRacesPopulation;
    private final int multiRacialPopulation;

    /**
     * Constructs a <tt>Population</tt> from <tt>place</tt>,
     * <tt>totalPopulation</tt>, <tt>whitePopulation</tt>,
     * <tt>blackPopulation</tt>,
     * <tt>americanIndianAndAlaskaNativePopulation</tt>,
     * <tt>asianPopulation</tt>,
     * <tt>nativeHawaiianAndOtherPacificIslanderPopulation</tt>,
     * <tt>otherRacesPopulation</tt> and <tt>multiRacialPopulation</tt>.
     *
     * @param place the <tt>Place</tt> whose population profile is described by
     * this <tt>Population</tt>
     * @param totalPopulation total population
     * @param whitePopulation population of the Whites
     * @param blackPopulation population of the Blacks
     * @param americanIndianAndAlaskaNativePopulation population of the American
     * Indians and Alaska Natives
     * @param asianPopulation population of the Asians
     * @param nativeHawaiianAndOtherPacificIslanderPopulation population of the
     * Native Hawaiians and Other Pacific Islanders
     * @param otherRacesPopulation population of people belonging to other races
     * @param multiRacialPopulation population of people belonging to two or
     * more races
     */
    public Population(Place place, int totalPopulation, int whitePopulation, int blackPopulation, int americanIndianAndAlaskaNativePopulation, int asianPopulation, int nativeHawaiianAndOtherPacificIslanderPopulation, int otherRacesPopulation, int multiRacialPopulation) {
        this.place = place;
        this.totalPopulation = totalPopulation;
        this.whitePopulation = whitePopulation;
        this.blackPopulation = blackPopulation;
        this.americanIndianAndAlaskaNativePopulation = americanIndianAndAlaskaNativePopulation;
        this.asianPopulation = asianPopulation;
        this.nativeHawaiianAndOtherPacificIslanderPopulation = nativeHawaiianAndOtherPacificIslanderPopulation;
        this.otherRacesPopulation = otherRacesPopulation;
        this.multiRacialPopulation = multiRacialPopulation;
    }

    /**
     * Returns the <tt>Place</tt> whose population profile is described by this
     * <tt>Population</tt>.
     *
     * @return the <tt>Place</tt> whose population profile is described by this
     * <tt>Population</tt>
     */
    public Place getPlace() {
        return place;
    }

    /**
     * Returns total population.
     *
     * @return total population
     */
    public int getTotalPopulation() {
        return totalPopulation;
    }

    /**
     * Returns population of the Whites.
     *
     * @return population of the Whites
     */
    public int getWhitePopulation() {
        return whitePopulation;
    }

    /**
     * Returns population of the Blacks.
     *
     * @return population of the Blacks
     */
    public int getBlackPopulation() {
        return blackPopulation;
    }

    /**
     * Returns population of the American Indians and Alaska Natives.
     *
     * @return population of the American Indians and Alaska Natives
     */
    public int getAmericanIndianAndAlaskaNativePopulation() {
        return americanIndianAndAlaskaNativePopulation;
    }

    /**
     * Returns population of the Asians.
     *
     * @return population of the Asians
     */
    public int getAsianPopulation() {
        return asianPopulation;
    }

    /**
     * Returns population of the Native Hawaiians and Other Pacific Islanders.
     *
     * @return population of the Native Hawaiians and Other Pacific Islanders
     */
    public int getNativeHawaiianAndOtherPacificIslanderPopulation() {
        return nativeHawaiianAndOtherPacificIslanderPopulation;
    }

    /**
     * Returns population of people belonging to other races.
     *
     * @return population of people belonging to other races
     */
    public int getOtherRacesPopulation() {
        return otherRacesPopulation;
    }

    /**
     * Returns population of people belonging to two or more races.
     *
     * @return population of people belonging to two or more races
     */
    public int getMultiRacialPopulation() {
        return multiRacialPopulation;
    }

    /**
     * Returns the <tt>String</tt> representation of this <tt>Population</tt>.
     * The <tt>String</tt> representation contains the place and population of
     * each racial category.
     *
     * @return the <tt>String</tt> representation of this <tt>Population</tt>
     */
    @Override
    public String toString() {
        return place.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.place);
        hash = 97 * hash + this.totalPopulation;
        hash = 97 * hash + this.whitePopulation;
        hash = 97 * hash + this.blackPopulation;
        hash = 97 * hash + this.americanIndianAndAlaskaNativePopulation;
        hash = 97 * hash + this.asianPopulation;
        hash = 97 * hash + this.nativeHawaiianAndOtherPacificIslanderPopulation;
        hash = 97 * hash + this.otherRacesPopulation;
        hash = 97 * hash + this.multiRacialPopulation;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Population)) {
            return false;
        }
        final Population other = (Population) obj;
        if (this.totalPopulation != other.totalPopulation) {
            return false;
        }
        if (this.whitePopulation != other.whitePopulation) {
            return false;
        }
        if (this.blackPopulation != other.blackPopulation) {
            return false;
        }
        if (this.americanIndianAndAlaskaNativePopulation != other.americanIndianAndAlaskaNativePopulation) {
            return false;
        }
        if (this.asianPopulation != other.asianPopulation) {
            return false;
        }
        if (this.nativeHawaiianAndOtherPacificIslanderPopulation != other.nativeHawaiianAndOtherPacificIslanderPopulation) {
            return false;
        }
        if (this.otherRacesPopulation != other.otherRacesPopulation) {
            return false;
        }
        if (this.multiRacialPopulation != other.multiRacialPopulation) {
            return false;
        }
        if (!Objects.equals(this.place, other.place)) {
            return false;
        }
        return true;
    }

}
