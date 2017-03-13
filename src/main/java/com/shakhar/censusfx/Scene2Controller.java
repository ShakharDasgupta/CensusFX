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
package com.shakhar.censusfx;

import com.shakhar.census.Population;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Controller for the second scene.
 *
 * @author Shakhar Dasgupta
 */
public class Scene2Controller implements Initializable {

    private final Stage stage;
    private final Population population1;
    private final Population population2;

    @FXML
    private PieChart place1Chart;
    @FXML
    private PieChart place2Chart;
    @FXML
    private Text place1Text;
    @FXML
    private Text county1Text;
    @FXML
    private Text state1Text;
    @FXML
    private Text place2Text;
    @FXML
    private Text county2Text;
    @FXML
    private Text state2Text;

    /**
     * Constructs <tt>Scene2Controller</tt>.
     *
     * @param stage the stage where the second scene has been set
     * @param population1 first <tt>Population</tt>
     * @param population2 second <tt>Population</tt>
     */
    public Scene2Controller(Stage stage, Population population1, Population population2) {
        this.stage = stage;
        this.population1 = population1;
        this.population2 = population2;
    }

    /**
     * Initialize values of GUI components
     *
     * @param url the location used to resolve relative paths for the root
     * object
     * @param rb the resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        place1Text.setText(population1.getPlace().getName());
        county1Text.setText(population1.getPlace().getCounty());
        state1Text.setText(population1.getPlace().getState().getName());
        place2Text.setText(population2.getPlace().getName());
        county2Text.setText(population2.getPlace().getCounty());
        state2Text.setText(population2.getPlace().getState().getName());
        place1Chart.setData(populationToList(population1));
        place2Chart.setData(populationToList(population2));
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        Scene1Controller scene1Controller = new Scene1Controller(stage);
        FXMLLoader scene1Loader = new FXMLLoader(getClass().getResource("/fxml/Scene1.fxml"));
        scene1Loader.setController(scene1Controller);
        Parent root = scene1Loader.load();
        Scene scene1 = new Scene(root);
        scene1.getStylesheets().add("/styles/Styles.css");
        stage.setScene(scene1);
    }

    @FXML
    private void handleExit(ActionEvent event) {
        System.exit(0);
    }

    private ObservableList<PieChart.Data> populationToList(Population population) {
        return FXCollections.observableArrayList(
                new PieChart.Data("Whites (" + population.getWhitePopulation() + ")", population.getWhitePopulation()),
                new PieChart.Data("Blacks (" + population.getBlackPopulation() + ")", population.getBlackPopulation()),
                new PieChart.Data("American Indians (" + population.getAmericanIndianAndAlaskaNativePopulation() + ")", population.getAmericanIndianAndAlaskaNativePopulation()),
                new PieChart.Data("Asians (" + population.getAsianPopulation() + ")", population.getAsianPopulation()),
                new PieChart.Data("Native Hawaiians (" + population.getNativeHawaiianAndOtherPacificIslanderPopulation() + ")", population.getNativeHawaiianAndOtherPacificIslanderPopulation()),
                new PieChart.Data("Other Races (" + population.getOtherRacesPopulation() + ")", population.getOtherRacesPopulation()),
                new PieChart.Data("Multi Racials (" + population.getMultiRacialPopulation() + ")", population.getMultiRacialPopulation()));
    }
}
