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
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Shakhar Dasgupta
 */
public class Scene3Controller implements Initializable {
    
    private final Stage stage;
    private final List<Population> populations;

    @FXML
    private ListView<Population> listView;
    @FXML
    private PieChart chart;
    @FXML
    private Text placeText;
    @FXML
    private Text countyText;
    @FXML
    private Text stateText;

    public Scene3Controller(Stage stage, List<Population> populations) {
        this.stage = stage;
        this.populations = populations;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listView.setItems(FXCollections.observableArrayList(populations));
        listView.getSelectionModel().selectionModeProperty().setValue(SelectionMode.SINGLE);
        listView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Population>() {
                    @Override
                    public void changed(ObservableValue<? extends Population> observable, Population oldValue, Population newValue) {
                        placeText.setText(newValue.getPlace().getName());
                        countyText.setText(newValue.getPlace().getCounty());
                        stateText.setText(newValue.getPlace().getState().getName());
                        chart.setData(populationToList(newValue));
                    }
                }
        );
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
