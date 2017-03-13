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
package com.shakhar.control;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Text field with a dropdown providing auto-complete suggestions.
 *
 * @author Shakhar Dasgupta
 */
public class AutoCompleteTextField extends TextField {

    private final SortedSet<String> entries;
    private final ContextMenu dropdown;

    /**
     * Constructs an <tt>AutoCompleteTextField</tt>.
     */
    public AutoCompleteTextField() {
        super();
        entries = new TreeSet<>();
        dropdown = new ContextMenu();
        textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (getText().length() > 0) {
                    LinkedList<String> result = new LinkedList<>();
                    result.addAll(entries.subSet(getText(), getText() + Character.MAX_VALUE));
                    if (result.size() > 0) {
                        populateDropdown(result);
                        if (!dropdown.isShowing()) {
                            dropdown.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0);
                        }
                    } else {
                        dropdown.hide();
                    }
                } else {
                    dropdown.hide();
                }
            }
        });
        focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                dropdown.hide();
            }
        });
    }

    /**
     * Returns the <tt>SortedSet</tt> containing auto-complete suggestions. To
     * add new suggestions add entries to the <tt>SortedSet</tt> returned.
     *
     * @return the <tt>SortedSet</tt> containing auto-complete suggestions.
     */
    public SortedSet<String> getEntries() {
        return entries;
    }

    private void populateDropdown(List<String> result) {
        List<CustomMenuItem> menuItems = new LinkedList<>();
        int maxEntries = 10;
        int count = Math.min(result.size(), maxEntries);
        for (int i = 0; i < count; i++) {
            final String itemString = result.get(i);
            CustomMenuItem item = new CustomMenuItem(new Label(itemString));
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    setText(itemString);
                    dropdown.hide();
                }
            });
            menuItems.add(item);
        }
        dropdown.getItems().clear();
        dropdown.getItems().addAll(menuItems);
    }
}
