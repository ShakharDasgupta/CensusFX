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
package com.shakhar.clusterer;

/**
 *
 * @author Shakhar Dasgupta
 */
class Centroid {

    private final int totalPopulation;
    private final int whitePopulation;
    private final int blackPopulation;
    private final int americanIndianAndAlaskaNativePopulation;
    private final int asianPopulation;
    private final int nativeHawaiianAndOtherPacificIslanderPopulation;
    private final int otherRacesPopulation;
    private final int multiRacialPopulation;

    public Centroid(int whitePopulation, int blackPopulation, int americanIndianAndAlaskaNativePopulation, int asianPopulation, int nativeHawaiianAndOtherPacificIslanderPopulation, int otherRacesPopulation, int multiRacialPopulation) {
        this.totalPopulation = whitePopulation+ blackPopulation + americanIndianAndAlaskaNativePopulation + asianPopulation + nativeHawaiianAndOtherPacificIslanderPopulation + otherRacesPopulation + multiRacialPopulation;
        this.whitePopulation = whitePopulation;
        this.blackPopulation = blackPopulation;
        this.americanIndianAndAlaskaNativePopulation = americanIndianAndAlaskaNativePopulation;
        this.asianPopulation = asianPopulation;
        this.nativeHawaiianAndOtherPacificIslanderPopulation = nativeHawaiianAndOtherPacificIslanderPopulation;
        this.otherRacesPopulation = otherRacesPopulation;
        this.multiRacialPopulation = multiRacialPopulation;
    }

    public int getTotalPopulation() {
        return totalPopulation;
    }

    public int getWhitePopulation() {
        return whitePopulation;
    }

    public int getBlackPopulation() {
        return blackPopulation;
    }

    public int getAmericanIndianAndAlaskaNativePopulation() {
        return americanIndianAndAlaskaNativePopulation;
    }

    public int getAsianPopulation() {
        return asianPopulation;
    }

    public int getNativeHawaiianAndOtherPacificIslanderPopulation() {
        return nativeHawaiianAndOtherPacificIslanderPopulation;
    }

    public int getOtherRacesPopulation() {
        return otherRacesPopulation;
    }

    public int getMultiRacialPopulation() {
        return multiRacialPopulation;
    }
}
