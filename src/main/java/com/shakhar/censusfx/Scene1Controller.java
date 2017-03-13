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

import com.shakhar.census.CensusData;
import com.shakhar.census.Population;
import com.shakhar.control.AutoCompleteTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller for the first scene.
 *
 * @author Shakhar Dasgupta
 */
public class Scene1Controller implements Initializable {

    private static final String ALL_STRING = "All States and Territories";
    private static CensusData censusData;
    private final Stage stage;

    static {
        try {
            censusData = new CensusData();
        } catch (IOException ex) {
            Logger.getLogger(Scene1Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Constructs <tt>Scene1Controller</tt>.
     *
     * @param stage the stage where the first scene has been set
     */
    public Scene1Controller(Stage stage) {
        this.stage = stage;

    }

    @FXML
    private AutoCompleteTextField placeField;
    @FXML
    private AutoCompleteTextField stateField;
    @FXML
    private Label errorLabel;

    @FXML
    private void handleSearch(ActionEvent event) throws IOException {
        String placeString = placeField.getText();
        String stateString = stateField.getText();
        Population placePopulation = censusData.getPopulationByPlace(placeString);
        if (placePopulation == null) {
            errorLabel.setText("Invalid Place entered.");
            return;
        }
        Population similarPlacePopulation;
        if (stateString.equals(ALL_STRING)) {
            similarPlacePopulation = censusData.getSimilarPopulation(placePopulation, null);
        } else if (censusData.getStateNames().contains(stateString)) {
            similarPlacePopulation = censusData.getSimilarPopulation(placePopulation, stateString);
        } else {
            errorLabel.setText("Invalid State/Territory entered.");
            return;
        }
        Scene2Controller scene2Controller = new Scene2Controller(stage, placePopulation, similarPlacePopulation);
        FXMLLoader scene2Loader = new FXMLLoader(getClass().getResource("/fxml/Scene2.fxml"));
        scene2Loader.setController(scene2Controller);
        Parent root = scene2Loader.load();
        Scene scene2 = new Scene(root);
        scene2.getStylesheets().add("/styles/Styles.css");
        stage.setScene(scene2);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        placeField.getEntries().addAll(censusData.getPlaceNames());
        stateField.getEntries().add(ALL_STRING);
        stateField.getEntries().addAll(censusData.getStateNames());
        placeField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                errorLabel.setText("");
            }
        });
        stateField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                errorLabel.setText("");
            }
        });
    }
}
