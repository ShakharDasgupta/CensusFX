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

import com.shakhar.census.Population;

/**
 *
 * @author Shakhar Dasgupta
 */
public class ClusteredPopulation extends Population {

    Cluster cluster;

    public ClusteredPopulation(Population population) {
        super(population.getPlace(), population.getTotalPopulation(), population.getWhitePopulation(), population.getBlackPopulation(), population.getAmericanIndianAndAlaskaNativePopulation(), population.getAsianPopulation(), population.getNativeHawaiianAndOtherPacificIslanderPopulation(), population.getOtherRacesPopulation(), population.getMultiRacialPopulation());
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public Cluster getCluster() {
        return cluster;
    }
}
