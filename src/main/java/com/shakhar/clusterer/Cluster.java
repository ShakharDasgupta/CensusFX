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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Shakhar Dasgupta
 */
public class Cluster {

    Centroid centroid;
    List<ClusteredPopulation> populations;

    Cluster(Centroid centroid) {
        this.centroid = centroid;
        populations = new ArrayList<>();
    }

    public List<ClusteredPopulation> getPopulations() {
        return populations;
    }

    Centroid getCentroid() {
        return centroid;
    }

    void setCentroid(Centroid centroid) {
        this.centroid = centroid;
    }

    public ClusteredPopulation getPopulation(int i) {
        return populations.get(i);
    }

    public void addPopulation(ClusteredPopulation population) {
        Cluster prev;
        if ((prev = population.getCluster()) != null) {
            prev.removePopulation(population);
        }
        population.setCluster(this);
        populations.add(population);
    }

    private void removePopulation(ClusteredPopulation population) {
        populations.remove(population);
    }

}
