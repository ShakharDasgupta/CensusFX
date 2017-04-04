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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Shakhar Dasgupta
 */
public class PopulationClusterer {

    private final int k;
    private final int maxIterations;

    public PopulationClusterer(int k, int maxIterations) {
        this.k = k;
        this.maxIterations = maxIterations;
    }

    private List<Cluster> randomClusters(int k) {
        Random random = new Random();
        List<Cluster> clusters = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            clusters.add(new Cluster(new Centroid(random.nextInt(100), random.nextInt(100), random.nextInt(100), random.nextInt(100), random.nextInt(100), random.nextInt(100), random.nextInt(100))));
        }
        return clusters;
    }

    private int assignPopulationsToClusters(List<ClusteredPopulation> populations, List<Cluster> clusters) {
        int count = 0;
        for (ClusteredPopulation p : populations) {
            float min = Float.MAX_VALUE;
            Cluster nearest = null;
            float dist;
            for (Cluster c : clusters) {
                if ((dist = euclideanDistance(p, c.getCentroid())) < min) {
                    min = dist;
                    nearest = c;
                }
            }
            if (nearest != p.getCluster()) {
                nearest.addPopulation(p);
                count++;
            }
        }
        return count;
    }

    private void adjustCentroids(List<Cluster> clusters) {
        for (Cluster cluster : clusters) {
            int whiteMean = 0;
            int blackMean = 0;
            int indianMean = 0;
            int asianMean = 0;
            int hawaiianMean = 0;
            int otherMean = 0;
            int multiMean = 0;
            List<ClusteredPopulation> populations = cluster.getPopulations();
            int count = populations.size();
            if (count > 0) {
                for (ClusteredPopulation p : populations) {
                    whiteMean += Math.round((float) p.getWhitePopulation() / p.getTotalPopulation() * 100);
                    blackMean += Math.round((float) p.getBlackPopulation() / p.getTotalPopulation() * 100);
                    indianMean += Math.round((float) p.getAmericanIndianAndAlaskaNativePopulation() / p.getTotalPopulation() * 100);
                    asianMean += Math.round((float) p.getAsianPopulation() / p.getTotalPopulation() * 100);
                    hawaiianMean += Math.round((float) p.getNativeHawaiianAndOtherPacificIslanderPopulation() / p.getTotalPopulation() * 100);
                    otherMean += Math.round((float) p.getOtherRacesPopulation() / p.getTotalPopulation() * 100);
                    multiMean += Math.round((float) p.getMultiRacialPopulation() / p.getTotalPopulation() * 100);
                }
                whiteMean /= count;
                blackMean /= count;
                indianMean /= count;
                asianMean /= count;
                hawaiianMean /= count;
                otherMean /= count;
                multiMean /= count;
                cluster.setCentroid(new Centroid(whiteMean, blackMean, indianMean, asianMean, hawaiianMean, otherMean, multiMean));
            }
        }
    }

    private float euclideanDistance(Population population1, Centroid population2) {
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

    public List<Cluster> cluster(List<Population> populations) {
        List<Cluster> clusters = randomClusters(k);
        List<ClusteredPopulation> clusteredPopulations = new ArrayList<>();
        for (Population p : populations) {
            clusteredPopulations.add(new ClusteredPopulation(p));
        }
        int changes;
        int c = 0;
        do {
            changes = assignPopulationsToClusters(clusteredPopulations, clusters);
            adjustCentroids(clusters);
            c++;
        } while (changes != 0 && c < maxIterations);
        return clusters;
    }
}
