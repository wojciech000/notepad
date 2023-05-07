package edu.application.notepad.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class NotePadFindAndReplaceController {

    private TextArea textArea;

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }

    @FXML
    private TextField textFieldSearch;

    @FXML
    private TextField textFieldReplace;

    private int index = 0;

    @FXML
    protected void onButtonReplaceAllClick() {
        textArea.replaceText(0, textArea.getText().length(), textArea.getText().replaceAll(textFieldSearch.getText(), textFieldReplace.getText()));
    }

    @FXML
    protected void onButtonReplaceClick() {
        if(!textArea.getSelectedText().isEmpty()) textArea.replaceSelection(textFieldReplace.getText());
    }

    @FXML
    protected void onButtonFindClick() {

        String searchText = textFieldSearch.getText();
        String fullText = textArea.getText();

        if (index + searchText.length() > fullText.length()) return;

        int newStartIndex = textArea.getText().indexOf(searchText, index);

        if (newStartIndex >= 0) {
            index = newStartIndex + searchText.length();
            textArea.selectRange(newStartIndex, index);
        } else {
            index = 0;
        }
    }
}
