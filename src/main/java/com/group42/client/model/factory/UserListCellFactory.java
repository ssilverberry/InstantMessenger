package com.group42.client.model.factory;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class UserListCellFactory implements Callback<ListView<String>, ListCell<String>> {

    @Override
    public ListCell<String> call(ListView<String> param) {
        return new ListCell<String>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                } else {
                    setText("");
                }
            }
        };
    }
}
